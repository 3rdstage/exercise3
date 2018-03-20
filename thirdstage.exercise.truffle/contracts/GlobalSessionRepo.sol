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

  /// @param domain
  // @param id
  // @param key
  function createSession(string domain, string id, string key) public payable returns(uint){
    Origin memory origin = Origin(domain, id);
    Session memory sess = Session(key, origin, block.timestamp, block.timestamp, 120);

  }

  function createSession(string domain, string id, string key, uint createdAt) public payable returns(bool created){


  }

  function updateSession(string domain, string id, string key) public payable returns(bool created){

  }

  function findSession(string domain, string key) public view returns(uint lastCheckedAt){

  }
}