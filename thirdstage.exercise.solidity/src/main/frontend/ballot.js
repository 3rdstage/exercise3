var solc = require('solc');
var fs = require('fs');

fs.readFile('./contracts/ballot.sol', 'utf8', (err, data) => {
   
   if(err) throw err;
   console.log(data);
   
   var output = solc.compile(data, 1);
   for(var name in output.contracts){

      console.log(name + ': ' + output.contracts[name].bytecode);
      console.log(name + "; " + JSON.parse(output.contracts[name].interface));

   }   
});

