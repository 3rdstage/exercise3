
import { Signature } from "ethers";


export interface SecurityModule {

  generateKeyPair(): string;

  // A signing algorithm that, given a message and a private key, produces a signature.
  // - https://en.wikipedia.org/wiki/Digital_signature#Definition
  signMessage(message: string, publicKey: string): Signature;

}


export abstract class DatabaseSecurityModule implements SecurityModule{

}

export abstract class MariaDBSecurityModule implements DatabaseSecp256k1Module{

}

export abstract class PostgreSQLSecurityModule implements DatabaseSecp256k1Module{


}

export abstract class AWSCloudHSMSecurityModule implements SecurityModule{

}



