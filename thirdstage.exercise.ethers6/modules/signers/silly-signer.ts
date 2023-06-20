
import { AbstractSigner,
  TransactionRequest,
  SigningKey
} from "ethers";

export abstract class SillySigner extends AbstractSigner {


  // Refer
  // - https://github.com/ethers-io/ethers.js/blob/main/src.ts/wallet/base-wallet.ts#L26

  constructor(privatekey: string){


  }

  async signTransaction(tx: TransactionRequest): Promise<string>{



  }


}



