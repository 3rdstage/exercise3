
import { PlainJsonSecurityModule as SecurityModule } from '../modules/signers/predefined-security-module';

console.log('Testing `TestOnlySignerFactory`.');

let signer = new SecurityModule('./tests/test-accounts.json');
