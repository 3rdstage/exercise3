
//import {ethers} from "ethers";
const ethers = require("ethers");

const eth = new ethers.JsonRpcProvider(
  `https://mainnet.infura.io/v3/${process.env.INFURA_PROJECT_ID}`);

// Compound's `Comptroller` contract instance at Mainnet
// https://docs.compound.finance/v2/comptroller/
// https://etherscan.io/address/0x3d9819210A31b4961b30EF54bE2aeD79B9c9Cd3B
// https://github.com/compound-finance/compound-protocol/blob/v2.8.1/contracts/Comptroller.sol
const meta = {
  address : '0x3d9819210A31b4961b30EF54bE2aeD79B9c9Cd3B',
  abi : [
    "function getCompAddress() view returns (address)",
    "function getAllMarkets() view returns (address[] memory)",
    "function compRate() view returns (uint256)",
  ]
};

const contract = new ethers.Contract(meta.address, meta.abi, eth);

contract.getCompAddress().then(addr => console.log(`COMP Token Address in Mainnet: ${addr}`));
contract.getAllMarkets().then(tokens => console.log(`Compound Market Count in Mainnet: ${tokens.length}`));

