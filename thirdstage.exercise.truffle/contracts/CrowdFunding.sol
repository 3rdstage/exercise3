//pragma solidity ^0.4.18;
pragma solidity ^0.5.0;

// https://ethereumbuilders.gitbooks.io/guide/content/en/solidity_tutorials.html

contract CrowdFunding{

  struct Funder{
    address addr;
    uint amount;
  }


  struct Campaign{
    address beneficiary;
    uint fundingGoal;
    uint numFunders;
    uint amount;
    mapping(uint => Funder) funders;
  }


  uint numCampaigns;
  mapping(uint => Campaign) campaigns;

}
