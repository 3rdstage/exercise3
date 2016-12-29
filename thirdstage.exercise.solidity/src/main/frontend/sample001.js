var solc = require('solc');
var input = 'contract x { function g() {} }';
var output = solc.compile(input, 1);

for(var name in output.contracts){

   console.log(name + ': ' + output.contracts[name].bytecode);
   console.log(name + "; " + JSON.parse(ouput.contracts[name].interface));

}