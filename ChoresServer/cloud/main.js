//noinspection JSUnfilteredForInLoop
function findPhones(phones) {
    var splitted = phones.split(";");
    var queries = [];
    for (var i in splitted) {
        if (splitted[i] == null || splitted[i] === undefined) {
            continue;
        }
        var number = splitted[i].replace(new RegExp('[^0-9]', 'g'), "");
        if (number == "") {
            continue;
        }
        console.log("Looking for phone number: " + number);
        var query = findPhoneNumber(number);
        if (query !== undefined && query != null) {
            queries.push(query);
        }
    }
    return Parse.Query.or.apply(null, queries);
}

function findPhoneNumber(number) {
    var query = new Parse.Query(Parse.User);
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
        inviterName = request.params.inviterName, apartmentId = request.params.apartmentId;
    if (phone == null || phone === undefined || phone == "" ) {
        console.log("No phone number was provided");
        response.error("No phone number was provided");
    }
    var query = findPhones(phone);
    var usersPromise = query.find(function (results) {
        console.log("Found users: " + results.length);
    });
    query.each(function (user) {
        console.log("creating for user " + user + " apartmentId " + apartmentId);
        var Notification = Parse.Object.extend("Notifications");
        var notification = new Notification();
        return notification.save({
            sender: inviterName,
            type: "PARSE_INVITATION_CHANNEL_KEY",
            target: [user.getUsername()],
            info: "{apartmentId: " + apartmentId + "}"
        }, {
            success: function () {
                console.log("Save success");
            },
            error: function () {
                console.log("Save failed");
            }
        });
    }).then(function () {
                response.success("Invitations sent");
            }, function (error) {
                response.error(error);
    });

/*    Parse.Promise.when(usersPromise).then(function (users) {
        if (users.length > 0) {
            var promises = [];
            for (var i in users) {
                if (users[i] == null || users[i] === undefined) {
                    continue;
                }
                console.log("Inviting user " + users[i].getUsername());
                var installationsQuery = new Parse.Query(Parse.Installation);
                var createPromise = createNotification(inviterName, users[i].getUsername(), apartmentId);
                createPromise.then("Save completed");
                return createPromise;
                console.log("Should not be called");
                promises.push(createPromise);
                installationsQuery.equalTo("username", users[i].get("username"));
                Parse.Push.send({
                    where: installationsQuery,
                    data: {
                        alert: "You were invited to join an apartment",
                        apartmentId: apartmentId,
                        msg: "You were invited to join " + inviterName + "'s apartment.",
                        action: "il.ac.huji.chores.choresNotification",
                        notificationType: "PARSE_INVITATION_CHANNEL_KEY",
                        inviter: inviterName
                    }
                });
            }
            return Parse.Promise.when(promises);
        } else {
            console.log("User with request phone number " + phone + "was not found");
            console.log("Inviting by SMS");
            return;
        }
    }).then(function() {
            response.success("Invitations sent");
    }, function(error) {
            respose.error(error);
    });*/
});

function createNotification(inviter, invited, apartmentId) {
    var Notification = Parse.Object.extend("Notification");
    var notification = new Notification();
    return notification.save({
        sender: inviter,
        type: "PARSE_INVITATION_CHANNEL_KEY",
        target: [invited],
        info: {apartmentId: apartmentId}
    });
}

/* Updating statistics */
Parse.Cloud.job("statisticsUpdate", function (request, status) {
//    1. Get old stats
    Parse.Cloud.useMasterKey();
    var Statistic = Parse.Object.extend("choreStatistics");
    var statsQuery = new Parse.Query("choreStatistics");
    var statisticsMap = {};
    statsQuery.find().then(function (statistics) {
        // Create map of statistics
        for (var i in statistics) {
            statisticsMap[statistics[i].get("chore")] = statistics[i];
            statistics[i].clear();
        }
    }).then(function (choreInfos) {
//        3. Update statistics with choreInfos {}
        var choreInfosQuery = new Parse.Query("ChoresInfo");
        choreInfosQuery.count(function (result) {
            status.message("Reading " + result + " choreInfos");
        })
        return choreInfosQuery.each(function(choreInfo) {
            var choreName = choreInfo.get("choreName");
            var statObj = statisticsMap[choreName];
            var updatedAt;
            if (!statObj) {
                statisticsMap[choreName] = new Statistic();
                // We're making as old as time itself...
                statObj = statisticsMap[choreName];
                status.message("Creating new object for" + choreName);
            }
            statObj.set("chore", choreName);
            statObj.increment("totalCoins", choreInfo.get("coins"));
            statObj.increment("totalCount");
        });
        // 4. now get the chores
    }).then(function (chores) {
        var choresQuery = new Parse.Query("Chores");
        choresQuery.count(function (result) {
            status.message("Reading " + result + " chores");
        })
        return choresQuery.each(function (chore) {
            var choreName = chore.get("name");
            var statObj = statisticsMap[choreName];
            if (!statObj) {
                return;
            }
            if (chore.get("status") == "STATUS_MISSED") {
                statObj.increment("totalMissed");
            } else if (chore.get("status") == "STATUS_DONE") {
                statObj.increment("totalDone");
            }
            statObj.increment("totalAssigned");
        });
    }).then(function (success) {
            var resultList = [];
            for (var name in statisticsMap) {
                resultList.push(statisticsMap[name]);
            }
            Parse.Object.saveAll(resultList).then(function () {
                status.success("Statistics updated");
            })
        }, function (error) {
            status.error("Failed to update statistics due to ", error);
    });
});

