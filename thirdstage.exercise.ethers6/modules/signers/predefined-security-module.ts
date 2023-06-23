import { SecurityModule } from './security-module';


export interface PredefinedSecurityModule implements Partial<SecurityModule>{

  signMessage(message: string, publicKey: string): Signature;

}

export class PlainJsonSecurityModule implements PredefinedSecurityModule {



}

export class EncryptedJsonSecurityModule implements PredefinedSecurityModule{


}