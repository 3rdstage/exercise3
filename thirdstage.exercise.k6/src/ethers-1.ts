
import http from 'k6/http';
import { sleep } from 'k6';

//@ts-ignore
import { ethers } from 'https://cdnjs.cloudflare.com/ajax/libs/ethers/6.3.0/ethers.min.js';

export const options = {
  vus : 2,
  duration : '30s'
};

export default function(){

  http.get('https://test.k6.io/');
  sleep(1);

}