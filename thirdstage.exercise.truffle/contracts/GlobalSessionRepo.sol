pragma solidity ^0.4.18;

/// @title Global session repository
contract GlobalSessionRepo{

  address manager;

  struct Origin{
    string domain;
    string id;
    mapping(string => string) attrbs;
  }

  struct Session{
    string key;
    Origin origin;
    uint createdAt;
    uint lastCheckedAt;
    uint timeout;
  }

  string[] domains; //valid domains

  mapping(string => Session) sessions;

  /// @param domain domain of session created or creating
  // @param id authentication identity within the domain who own the session
  // @param key unique identifier for the session
  function createSession(string domain, string id, string key) public payable returns(uint){
    Origin memory origin = Origin(domain, id);
    Session memory sess = Session(key, origin, block.timestamp, block.timestamp, 120);

  }

  function createSession(string domain, string id, string key, uint createdAt) public payable returns(bool){


  }

  function updateSession(string domain, string id, string key) public payable returns(bool){

  }

  function findSession(string domain, string key) public view returns(uint){

  }
}