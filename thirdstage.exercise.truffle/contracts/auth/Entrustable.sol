pragma solidity ^0.5.0;

contract Entrustable{


  mapping(address => mappping(address => uint)) private entrusteds



  function entrust(address entrusted) public {
    require
    entrusted[msg.sender][entrusted] = 1;
  }



  function distrust(address owner, address entrusted) public {
    require(msg.sneder == owner);

  }

  function isTrustedFor(address owner, address testee) public return(boolean){
    if(entrusted[owner][testee] ==

  }

  function getAllTrusted() return(address[]){

  }


}