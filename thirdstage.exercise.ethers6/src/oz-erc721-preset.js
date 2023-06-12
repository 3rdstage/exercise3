
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
  provider.pollingInterval = 300;

  const parentWallet = ethers.HDNodeWallet.fromPhrase(process.env.BIP39_MNEMONIC, '', "m/44'/60'/0'/0");
  const wallets = [];
  const n = 10;
  for(let i = 0, wallet; i < n; i++){
    wallet = parentWallet.deriveChild(i).connect(provider);
    console.log(`Account ${i} : ${wallet.address}, ${wallet.path}`);
    wallets.push(wallet);
  }

  const meta = JSON.parse(fs.readFileSync('contracts/ERC721PresetMinterPauserAutoId.json'));
  const abi = JSON.stringify(meta.abi);
  const bytecode = meta.bytecode;

  //console.log(typeof(abi));
  //console.log(typeof(bytecode));
  //console.log(abi);

  const options = {gasLimit : 100000, gasPrice: 0, from: wallets[0].address};
  const factory = new ethers.ContractFactory(abi, bytecode, wallets[0]);
  const contract = await factory.deploy('Deep Sky', 'OBJ', '');

  console.log(`Contract Deployed`)
  console.log(Object.keys(contract));
  const props = [];
  props.push(["Address", await contract.getAddress()]);
  props.push(["Name", await contract.name()]);
  props.push(["Symbol", await contract.symbol()]);
  props.push(["Total Supply", await contract.totalSupply()]);

  console.table(props);

  console.log(`Contract Runner: ${Object.keys(contract.runner)}`);
  const network = await contract.runner.provider.getNetwork();
  console.log(`Network - name: ${network.name}, chain ID: ${network.chainId}`);

  console.log(`Balances: initial`)
  const balances = [];
  for(const w of wallets){
    balances.push({"Account": w.address, "Balance": await contract.balanceOf(w.address)});
  }
  console.table(balances);

  for(let i = 0, tx; i < n; i++){
    //tx = await contract.mint.send(wallets[i].address);
    tx = await contract.mint(wallets[i].address);

    await tx.wait();
  }

  console.log(`Balances: after initial mint`)
  balances.length = 0;
  for(const w of wallets){
    balances.push({"Account": w.address, "Balance": await contract.balanceOf(w.address)});
  }
  console.table(balances);

})();
