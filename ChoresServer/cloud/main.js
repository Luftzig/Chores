
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

Parse.Cloud.define("invite", function(request, response) {
    var user = request.user, name = request.params.name, 
        phone = request.params.phone;
    var queryPhone = new Parse.Query(Parse.User);
    if (phone != null && phone !== undefined && phone != "") {
        queryPhone.equalTo("phone", phone);
        var query = queryPhone;
    }
    var queryEmail = new Parse.Query(Parse.User);
    if (email != null && email !== undefined && email != "") {
        queryEmail.equalTo("email", email);
        if (query) {
            query = Parse.Query.or(queryPhone, queryEmail);
        } else {
            query = queryEmail;
        }
    }
    console.log("invite: querying for user " + name + ", " + phone + ", " + email);
    query.find({
            success: function(results) {
                if (results.length > 0) {
                    // TODO: find Installation object and send notification
                    console.log("Alert user " + results[0]);
                }
            },

            error: function(error) {
                // TODO: send user an invite using email or SMS
                console.log("No such user");
            }
    });
    response.success(name + " was invited.");
});
