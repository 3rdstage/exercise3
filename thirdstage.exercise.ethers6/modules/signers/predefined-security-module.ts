import { SecurityModule } from './security-module';
import { SigningKey, Signature } from 'ethers';
import * as fs from 'fs';


export interface PredefinedSecurityModule extends Partial<SecurityModule>{

  signMessage(message: string, publicKey: string): Signature;

}

export class PlainJsonSecurityModule implements PredefinedSecurityModule {

  #keys: Map<string, SigningKey> = {}};

  constructor(path: string){
    if(!fs.statSync(path).isFile()){
      throw new Error("The specified file doesn't exist");
    }
    const keys = JSON.parse(fs.readFileSync(path).toString());

    let sk: SigningKey;
    for(const key of keys){
      sk = new SigningKey(key);
      this.#keys.set(sk.publicKey, sk);
      console.log(`Loaded public key : ${sk.publicKey}`);
    }

    console.log(`Loaded ${this.#keys.size} key-pairs.`);
  }

  signMessage(message: string, publicKey: string): Signature{


    return undefined;
  }

}

export abstract class EncryptedJsonSecurityModule implements PredefinedSecurityModule{

  signMessage(message: string, publicKey: string): Signature;

}