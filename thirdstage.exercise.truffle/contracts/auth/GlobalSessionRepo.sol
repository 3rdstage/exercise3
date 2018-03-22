pragma solidity ^0.4.18;

import "../lib/LangLib.sol";

/// @title Global session repository
contract GlobalSessionRepo{

  address manager;

  struct Origin{
    string domain;
    string id;
    mapping(string => string) attrbs;
  }

  struct Session{
    Origin origin;
    string token;
    uint createdAt;
    uint lastCheckedAt;
    uint timeout;
  }

  string[] domains; //valid domains

  mapping(string => Session) sessions;

  /// @param domain unique identifier for the domain which has its own local session manager
  // @param id authentication identity within the domain who own the session
  // @param token unique identifier for the session provided by the local session manager
  function createSession(string domain, string id, string token) public payable returns(bool){
    Origin memory origin = Origin(domain, id);
    Session memory sess = Session(origin, token, block.timestamp, block.timestamp, 120);
    string memory key = StringUtils.concat(domain, token);

    if(StringUtils.isEmpty(sessions[key].token)){  //normal case
      //TODO Is it copied into storage object?
      sessions[key] = sess;
      return true;
    }else{ //exceptional case
      return false; //the session is already exists
    }
  }

  function createSession(string domain, string id, string key, uint createdAt) public payable returns(bool){


  }

  function updateSession(string domain, string id, string key) public payable returns(bool){

  }

  function findSession(string domain, string token) public view returns(uint){
    string memory key = StringUtils.concat(domain, token);
    Session sess = sessions[key];

    if(!StringUtils.isEmpty(sess.token)){
      return sess.lastCheckedAt;
    }else{
      return 0;
    }
  }
}