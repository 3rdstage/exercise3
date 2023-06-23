import { Signer,
  TransactionRequest,
  SigningKey
} from "ethers";
import * as fs from 'fs';


export class TestOnlySignerFactory{

  #keys : Array<SigningKey> = [];

  constructor(path: string){
    if(!fs.statSync(path).isFile()){
      throw new Error("The specified file doesn't exist");
    }
    const prvs = JSON.parse(fs.readFileSync(path).toString());

    for(const prv of prvs){
      this.#keys.push(new SigningKey(prv));
      console.log(`Loaded public key : ${this.#keys[this.#keys.length - 1].publicKey}`);
    }

    console.log(`Loaded ${this.#keys.length} key-pairs.`);
  }


  async signTransaction(tx: TransactionRequest): Promise<string>{
    return '';
  }

}