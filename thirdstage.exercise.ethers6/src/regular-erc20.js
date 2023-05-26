
const ethers = require("ethers");
const fs = require('fs');

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);

console.log(`Zero Address: '${ethers.ZeroAddress}'`);
console.log(`Zero Hash : '${ethers.ZeroHash}'`);
console.log(`Ether Symbol : ${ethers.EtherSymbol}`);
console.log(`Message Prefix : '${ethers.MessagePrefix}'`);

(async() => {
  const provider = new ethers.JsonRpcProvider('http://127.0.0.1:8545');
  const wallet = ethers.Wallet.fromPhrase(process.env.BIP39_MNEMONIC, provider);
  console.log(`Generated Account : ${wallet.address}`);

  const meta = JSON.parse(fs.readFileSync('contracts/RegularERC20.json'));
  const abi = JSON.stringify(meta.abi);
  const bytecode = meta.bytecode;

  //console.log(typeof(abi));
  //console.log(typeof(bytecode));
  //console.log(abi);

  const options = {gasLimit : 100000, gasPrice: 0, from: wallet.address};
  const factory = new ethers.ContractFactory(abi, bytecode, wallet);
  const contract = await factory.deploy('Rainbow Tokens', 'RGB');


  console.log(`Contract Deployed`)
  console.log(Object.keys(contract));
  const props = [];
  props.push(["Address", await contract.getAddress()]);
  props.push(["Symbol", await contract.symbol()]);
  props.push(["Admin", await contract.admin()]);

  console.table(props);

})();
