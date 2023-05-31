
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

  const parentWallet = ethers.HDNodeWallet.fromPhrase(process.env.BIP39_MNEMONIC, '', "m/44'/60'/0'/0");
  const wallets = [];
  for(let i = 0, wallet; i < 10; i++){
    wallet = parentWallet.deriveChild(i).connect(provider);
    console.log(`Account ${i} : ${wallet.address}, ${wallet.path}`);
    wallets.push(wallet);
  }

  const meta = JSON.parse(fs.readFileSync('contracts/RegularERC20.json'));
  const abi = JSON.stringify(meta.abi);
  const bytecode = meta.bytecode;

  //console.log(typeof(abi));
  //console.log(typeof(bytecode));
  //console.log(abi);

  const options = {gasLimit : 100000, gasPrice: 0, from: wallets[0].address};
  const factory = new ethers.ContractFactory(abi, bytecode, wallets[0]);
  const contract = await factory.deploy('Rainbow Tokens', 'RGB');


  console.log(`Contract Deployed`)
  console.log(Object.keys(contract));
  const props = [];
  props.push(["Address", await contract.getAddress()]);
  props.push(["Symbol", await contract.symbol()]);
  props.push(["Admin", await contract.admin()]);

  console.table(props);

})();
