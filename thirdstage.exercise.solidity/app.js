
/**
 * Generated Node.js application that can run on IBM Bluemix
 */

var http = require("http");

var appport = process.env.VCAP_APP_PORT || 8888;

http.createServer(function(request, response) {

    response.writeHead(200, {"Content-Type": "text/plain"});
    response.write("Generated Node.js application that runs on IBM Bluemix");        
    response.end();	

}).listen(appport);
