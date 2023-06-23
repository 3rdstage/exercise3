
import { TestOnlySignerFactory } from '../modules/signers/test-only-signer-factory';

console.log('Testing `TestOnlySignerFactory`.');

let signer = new TestOnlySignerFactory('./tests/test-accounts.json');
