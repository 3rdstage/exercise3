pragma solidity ^0.4.18;

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