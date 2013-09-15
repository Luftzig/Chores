
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

function findPhones(phones) {
    var splitted = phones.split("; ");
    var queries = new Array();
    for (i in splitted) {
        if (splitted[i] == null || splitted[i] === undefined) {
            continue;
        }
        var number = splitted[i].replace("[^0-9]", "");
        console.log("Looking for phone number: " + number);
        var query = findPhoneNumber(number);
        if (query !== undefined && query != null) {
            queries.push(query);
        }
    }
    return Parse.Query.or.apply(null, queries);
}

function findPhoneNumber(number) {
    var suffix = number.slice(-6);
    var prefix = number.slice(0, -6);
    var query = new Parse.Query(Parse.User);
    query.endsWith("phoneNumber", suffix).startsWith("phoneNumber", prefix);
    return query;
}

Parse.Cloud.define("invite", function(request, response) {
    var user = request.user, name = request.params.name, 
        phone = request.params.phone;
    if (phone == null || phone === undefined || phone == "" ) {
        console.log("No phone number was provided");
        response.error("No phone number was provided");
    }
    var query = findPhones(phone);
    console.log("invite: querying for user " + name + ", " + phone);
    query.find().then(function (results) {
        if (results.length > 0) {
            for (i in results) {
                if (results[i] == null || results[i] === undefined) {
                    continue;
                }
                console.log("Inviting user " + results[i].getUsername());
                var query = new Parse.Query(Parse.Installation);
                query.equalsTo("userId", results[i].get("objectId"));
                Parse.Push.send({
                    where: query,
                    data: {
                        by: user.getUsername(),
                        alert: "You were invited to join apartment",
                        apartmentId: user.get("apartmentID")
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
    });
});
