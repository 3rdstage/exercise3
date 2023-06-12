// https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.8.3/contracts/token/ERC1155/presets/ERC1155PresetMinterPauser.sol

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
  const n = 10;
  for(let i = 0, wallet; i < n; i++){
    wallet = parentWallet.deriveChild(i).connect(provider);
    console.log(`Account ${i} : ${wallet.address}, ${wallet.path}`);
    wallets.push(wallet);
  }

  const meta = JSON.parse(fs.readFileSync('contracts/ERC1155PresetMinterPauser.json'));
  const abi = JSON.stringify(meta.abi);
  const bytecode = meta.bytecode;

  //console.log(typeof(abi));
  //console.log(typeof(bytecode));
  //console.log(abi);

  const options = {gasLimit : 100000, gasPrice: 0, from: wallets[0].address};
  const factory = new ethers.ContractFactory(abi, bytecode, wallets[0]);
  const contract = await factory.deploy('http://nft.org/collections/1131/');

  console.log(`Contract Deployed`)
  console.log(Object.keys(contract));
  const props = [];
  props.push(["Address", await contract.getAddress()]);
  props.push(["URI", await contract.uri(1)]);

  console.table(props);

  console.log(`Contract Runner: ${Object.keys(contract.runner)}`);
  const network = await contract.runner.provider.getNetwork();
  console.log(`Network - name: ${network.name}, chain ID: ${network.chainId}`);

  console.log(`Balances: initial`)
  const balances = [];
  const type = 1;
  for(const w of wallets){
    balances.push({"Account": w.address, "Type 1 Balance": await contract.balanceOf(w.address, type)});
  }
  console.table(balances);

  for(let i = 0, tx; i < n; i++){
    //tx = await contract.mint.send(wallets[i].address);
    tx = await contract.mint(wallets[i].address, type, 1000, '0x99');

    await tx.wait();
  }

  await contract.mint.send

  console.log(`Balances: after initial mint`)
  balances.length = 0;
  for(const w of wallets){
    balances.push({"Account": w.address, "Type 1 Balance": await contract.balanceOf(w.address, type)});
  }
  console.table(balances);

})();
