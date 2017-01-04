var solc = require('solc');
var fs = require('fs');

fs.readFile('./contracts/bank.sol', 'utf8', (err, data) => {
   
   if(err) throw err;
   console.log(data);
   
   var output = solc.compile(data, 0);
   if(output.errors) throw output.errors;
   for(var name in output.contracts){

      console.log(name + ': ' + output.contracts[name].bytecode);
      console.log(name + "; " + JSON.parse(output.contracts[name].interface));

   }   
});

