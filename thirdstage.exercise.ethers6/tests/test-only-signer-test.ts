
import { TestOnlySigner } from '../modules/signers/test-only-signer';

console.log('Trying to load `TestOnlySigner`');

let signer = new TestOnlySigner('./tests/test-accounts.json');
console.log(signer.help());