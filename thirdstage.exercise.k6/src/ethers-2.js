
import http from 'k6/http';
import { sleep } from 'k6';
import { ethers } from 'ethers';

export const options = {
  vus : 1,
  duration : '2s'
};

const eth = new ethers.JsonRpcProvider(
  'https://mainnet.infura.io/v3/abc');

export default function(){

  http.get('https://test.k6.io/');
  sleep(1);

}