
import http from 'k6/http';
import { sleep } from 'k6';

//@ts-ignore
import { ethers } from 'ethers';

const eth = new ethers.providers.JsonRpcProvider(
  'https://mainnet.infura.io/v3/');

export const options = {
  vus : 2,
  duration : '5s'
};

export default function(){

  //eth.getBlockNumber().then(n => console.log(n));

  sleep(1);
}