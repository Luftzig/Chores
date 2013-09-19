
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("test", function(request, response) {
    Parse.Push.send({
            where: new Parse.Query(Parse.Installation),
            data: {
                alert: "Push test!"
            }
    });
    response.success("Push sent");
});

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
                        by: inviter,
                        alert: "You were invited to join apartment " + inviter.get("apartmentID"),
                        apartmentId: inviter.get("apartmentID")
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
