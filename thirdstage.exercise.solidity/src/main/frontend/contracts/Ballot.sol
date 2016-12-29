pragma solidity ^0.4.0;

contract Ballot{

   //https://solidity.readthedocs.io/en/develop/solidity-by-example.html#voting
   
   struct Voter {
      unit weight;
      bool voted;
      address delegate;
      uint vote;
   }
   
   
   struct Proposal{
      byte32 name;
      uint voteCount;
   }
   
   
   address public chairperson;
   
   mapping(address => Voter) public voters;
   
   Proposal[] public proposals;
   
   
   function Ballout(bytes32[] proposalNames){
      chairperson = msg.sender;
      voters[chairperson].weight = 1;
      
      for(uint i = 0; i < proposalNames.length; i++){
         proposals.push(Proposal({
            name : proposalNames[i],
            voteCount : 1
         }));
      }
   }
   
   function giveRightToVote(address voter){
      if(msg.sender != chairperson || voters[voter].voted){
         throw;
      }
      voters[voter].weight = 1;
      
   }
   
   function delegate(address to){
      Voter sender = voter[msg.sender];
      
      if(sender.voted){
         throw;
      }
      
      while(voters[to].delegate != address(0) && voters[to].delegate != msg.sender){
         to = voters[to].delegate;
      }
      
      if(to == msg.sender){
         throw;
      }
      
      sender.voted = true;
      sender.delegate = to;
      Voter delegate = voters[to];
      
      if(delegate.voted){
         proposals[delegate.vote].voteCount += sender.weight;
      }else {
         delegate.weight += sender.weight;
      }
   }
   
   
   
}   