
// https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.8.3/contracts/token/ERC721/presets/ERC721PresetMinterPauserAutoId.sol

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

  const meta = JSON.parse(fs.readFileSync('contracts/ERC721PresetMinterPauserAutoId.json'));
  const abi = JSON.stringify(meta.abi);
  const bytecode = meta.bytecode;

  //console.log(typeof(abi));
  //console.log(typeof(bytecode));
  //console.log(abi);

  const options = {gasLimit : 100000, gasPrice: 0, from: wallet.address};
  const factory = new ethers.ContractFactory(abi, bytecode, wallet);
  const contract = await factory.deploy('Deep Sky', 'OBJ', '');


  console.log(`Contract Deployed`)
  console.log(Object.keys(contract));
  const props = [];
  props.push(["Address", await contract.getAddress()]);
  props.push(["Name", await contract.name()]);
  props.push(["Symbol", await contract.symbol()]);
  props.push(["Total Supply", await contract.totalSupply()]);

  console.table(props);

})();
