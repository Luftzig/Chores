
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
