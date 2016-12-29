var solc = require('solc');
var fs = require('fs');

fs.readFile('./contracts/Ballout.sol', (err, data) => {
   
   if(err) throw err;

   
   
   console.log(data);
});

var input = fs.readFileSync('./contracts/Ballot.sol'); 
console.log(input)

var output = solc.compile(input, 1);

for(var name in output.contracts){

   console.log(name + ': ' + output.contracts[name].bytecode);
   console.log(name + "; " + JSON.parse(output.contracts[name].interface));

}