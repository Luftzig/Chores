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
Parse.Cloud.beforeSave("ChoresInfo", function(request, response) {
    var query = new Parse.Query("choreStatistics");
    query.equalTo("chore", request.object.get("choreName"));
    Parse.Promise.when(query.find(), new Parse.Query("ChoresInfo").get(request.object.id))
        .then(function (results, oldChoreInfo) {
            console.log("results: " + results);
            logKeys(request.object);
            if (results.length == 0) {
                console.log("New chore info");
                var ChoreStatistics = Parse.Object.extend("choreStatistics");
                var choreStatistics = new ChoreStatistics();
                choreStatistics.set("chore", request.object.get("choreName"));
                choreStatistics.set("totalCoins", request.object.get("coins"));
                choreStatistics.set("totalCount", 1);
                choreStatistics.set("totalDone", 0);
                choreStatistics.set("totalMissed", 0);
                choreStatistics.set("totalAssigned", 0);
                choreStatistics.save();
            } else {
                console.log("oldChoreInfo " + oldChoreInfo);
                for (var i in results) {
                    if (!oldChoreInfo) {
                        results[i].increment("totalCount");
                        results[i].increment("totalCoins", request.object.get("coins"));
                    } else {
                        var difference = request.object.get("coins") - oldChoreInfo.get("coins");
                        console.log("ChoreInfo being updated, coins value difference " + difference);
                        results[i].increment("totalCoins", difference);
                    }
                    results[i].save();
                }
            }
            response.success();
        });
});

Parse.Cloud.beforeSave("Chores", function(request, response) {
    var query = new Parse.Query("choreStatistics");
    query.equalTo("chore", request.object.get("name"));
    Parse.Promise.when(query.find(), new Parse.Query("Chores").get(request.object.id))
        .then(function (statistics, oldChore) {
            if (oldChore === undefined) {
                // This chore is new, update total assigned count
                for (var i in statistics) {
                    statistics[i].increment("totalAssigned");
                }
            } else {
                if (oldChore.get("status") == "STATUS_FUTURE" && request.object.get("status") == "STATUS_MISSED") {
                    for (var i in statistics) {
                        statistics[i].increment("totalMissed");
                    }
                } else if (request.object.get("status") == "STATUS_DONE") {
                    for (var i in statistics) {
                        statistics[i].increment("totalDone");
                    }
                }
            }
            response.success();
        });
});
