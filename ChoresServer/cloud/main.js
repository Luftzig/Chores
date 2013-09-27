//noinspection JSUnfilteredForInLoop
function findPhones(phones) {
    var splitted = phones.split("; ");
    var queries = [];
    for (i in splitted) {
        if (splitted[i] == null || splitted[i] === undefined) {
            continue;
        }
        var number = splitted[i].replace("[^0-9]", "");
        console.log("Looking for phone number: " + number);
        if (number == "") {
            continue;
        }
        var query = findPhoneNumber(number);
        if (query !== undefined && query != null) {
            queries.push(query);
        }
    }
    return Parse.Query.or.apply(null, queries);
}

function findPhoneNumber(number) {
    var query = new Parse.Query(Parse.User);
/*    var suffix = number.slice(-6);
    var prefix = number.slice(0, -6);
    query.endsWith("phoneNumber", suffix).startsWith("phoneNumber", prefix);*/
    query.endsWith("phoneNumber", number);
    return query;
}

function logKeys(object) {
    var keys = "";
    for (var key in object) {
        keys = keys + key + ", ";
    }
    console.log("Keys for object: " + keys);
}

function getUserByName(username) {
    var query = new Parse.Query(Parse.User);
    return query.equalTo("username", username).first(function (results) {
        console.log("Looking for user " + username);
        if (results.length > 0) {
            console.log("Found user");
            return results[0]
        }
    });
}

Parse.Cloud.define("invite", function(request, response) {
    var name = request.params.name, phone = request.params.phone,
        inviterName = request.params.inviter.username;
    if (phone == null || phone === undefined || phone == "" ) {
        console.log("No phone number was provided");
        response.error("No phone number was provided");
    }
    var query = findPhones(phone);
    var inviterPromise = getUserByName(inviterName);
    /*console.log("invite: querying for user " + name + ", " + phone);*/
    var usersPromise = query.find(function (results) {
        console.log("Found users");
    });
    Parse.Promise.when(usersPromise, inviterPromise).then(function (users, inviter) {
        if (users.length > 0) {
            for (var i in users) {
                if (users[i] == null || users[i] === undefined) {
                    continue;
                }
                console.log("Inviting user " + users[i].getUsername());
                var installationsQuery = new Parse.Query(Parse.Installation);
                installationsQuery.equalTo("username", users[i].get("username"));
                Parse.Push.send({
                    where: installationsQuery,
                    data: {
                        alert: "You were invited to join an apartment",
                        apartmentId: inviter.get("apartmentID"),
                        msg: "You were invited to join " + inviter.get("username") + "'s apartment.",
                        action: "il.ac.huji.chores.choresNotification",
                        notificationType: "PARSE_INVITATION_CHANNEL_KEY",
                        inviter: inviter
                    }
                });
            }
        } else {
            console.log("User with request phone number " + phone + "was not found");
            console.log("Inviting by SMS");
        }
        return;
    }).then(function() {
            response.success("Invitations sent");
    }, function(error) {
            respose.error(error);
    });
});


/* Updating statistics */
Parse.Cloud.job("statisticsUpdate", function (request, status) {
    Parse.Cloud.useMasterKey();
    var statistics = {};
    (new Parse.Query("ChoresInfo")).find()
        .then(function (choreInfos) {
            for (var i in choreInfos) {
                var name = choreInfos[i].get("choreName");
                status.message(name + " is being processed");
//                console.log(name + " is being processed");
                if (name !== undefined && name != null && name != "") {
                    if (statistics[name] == undefined) {
                        statistics[name] = {count: 0, assigned: 0, missed: 0, done: 0, coins: 0};
                    }
                    statistics[name].count++;
                    statistics[name].coins += choreInfos[i].get("coins");
                }
                status.message(name + " was processed");
//                console.log(name + " was processed");
            }
        }, function (error) {
            status.error("Failed during get choreInfos" + error);
        }).then(function (ignored) {
            for (var name in statistics) {
                status.message("Collecting data on chores of type " + name);
                // console.log("Collecting data on chores of type " + name);
                (new Parse.Query("Chores")).equalTo("name", name).each(function (chore) {
                    if (chore.get("apartment") !== undefined && chore.get("assignedTo") !== undefined) {
                        statistics[name].assigned++;
                        if (chore.get("status") == "STATUS_MISSED") {
                            statistics[name].missed++;
                        } else if (chore.get("status") == "STATUS_DONE") {
                            statistics[name].done++;
                        }
                    }
                })
            }
        }, function (error) {
            status.error("Failed during parsing Chores " + error);
        }).then(function (ignored) {
            status.message("Updating statistics objects");
            console.log("Updating statistics objects");

            function createNewStatistic() {
                var Statistic = Parse.Object.extend("choreStatistics");
                status.message("Creating new object for " + name);
                var statistic = new Statistic();
                console.log("Creating new object for " + name + ": " + statistic.isNew());
                statistic.set("name", name);
                Parse.Cloud.useMasterKey();
                statistic.save( {
                        name: name,
                        totalAssigned: statistics[name].assigned,
                        totalCoins: statistics[name].coins,
                        totalCount: statistics[name].count,
                        totalDone: statistics[name].done,
                        totalMissed: statistics[name].missed
                    }, {
                        success: function(result) {
                            console.log("Saving object " + name + ": " + statistic);
                        },
                        error: function(something, error) {
                            console.log("Failed to update " + name + " with error " + error);
                        }
                    });
            }

            for (var name in statistics) {
                status.message("Updating statistics of " + name);
                console.log("Updating statistics of " + name);
                (new Parse.Query("choreStatistics")).equalTo("chore", name).first().then(
                    function (statistic) {
                        console.log("Got choreStatistic " + statistic);
                        if (statistic !== undefined) {
                            status.message("Found old object for " + name + " Updating statistics");
                            console.log("Found old object for " + name + " Updating statistics");
                            statistic.set("totalAssigned", statistics[name].assigned);
                            statistic.set("totalCoins", statistics[name].coins);
                            statistic.set("totalCount", statistics[name].count);
                            statistic.set("totalDone", statistics[name].done);
                            statistic.set("totalMissed", statistics[name].missed);
                            return statistic.save();
                        } else {
                            return createNewStatistic();
                        }
                    }, function (error) {
                        console.log("Caught error while querying for " + name);
                    });
                createNewStatistic();
            }
            status.success("Statistics update completed");
        }, function (error) {
            status.error("Failed when updating objects " + error);
        });
});
