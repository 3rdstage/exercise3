var GlobalSessionRepo = artifacts.require("GlobalSessionRepo");

/* References
     http://truffleframework.com/docs/getting_started/javascript-tests
     http://truffleframework.com/docs/getting_started/contracts
     https://github.com/trufflesuite/truffle-contract/blob/develop/README.md
     http://www.chaijs.com/api/assert/
     https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference
  */
contract("GlobalSessionRepo", function(accounts){

  it("shoud add a new session correctly.", async function(){
    const domain = "SKCC";
    const id = "06599";
    const token = "12345679";

    let sessRepo = await GlobalSessionRepo.deployed();
    await sessRepo.createSession(domain, id, token);

    let at = (await sessRepo.findSession(domain, token)).toNumber();  //for Quorum, this is in nanosec instead of millisec

    console.log(new Date(Math.round(at / 1000000)));

    assert.notEqual(at, 0, "The 'findSession' should be able to find session created" );

  });
});
