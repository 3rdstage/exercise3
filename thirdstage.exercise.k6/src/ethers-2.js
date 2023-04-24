
import http from 'k6/http';
import { sleep } from 'k6';
import { ethers } from 'ethers';

export const options = {
  vus : 1,
  duration : '2s'
};

export default function(){


  let a = ethers.MessagePrefix;
  http.get('https://test.k6.io/');
  sleep(1);

}