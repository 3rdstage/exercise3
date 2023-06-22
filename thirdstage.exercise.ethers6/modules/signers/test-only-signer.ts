import { Signer,
  TransactionRequest,
  SigningKey
} from "ethers";
import * as fs from 'fs';


export class TestOnlySigner {


  constructor(path: string){
    if(!fs.statSync(path).isFile()){
      throw new Error("The specified file doesn't exist");
    }
    const keys = JSON.parse(fs.readFileSync(path).toString());
    console.log(keys);
  }


  async signTransaction(tx: TransactionRequest): Promise<string>{
    return '';
  }

  help(): string{
    return "Later";
  }

}