import { Signer,
  TransactionRequest,
  SigningKey
} from "ethers";

export abstract class TestOnlySigner implements Signer {

  async signTransaction(tx: TransactionRequest): Promise<string>{

  }
}