
import http from 'k6/http';
import { sleep } from 'k6';
import { ethers } from 'https://raw.githubusercontent.com/ethers-io/ethers.js/v6.3.0/dist/ethers.min.js';
import { Constants } from "ethers"

export const options = {
  vus : 2,
  duration : '30s'
};

export default function(){

  let a = Constants.MessagePrefix;
  http.get('https://test.k6.io/');
  sleep(1);

};