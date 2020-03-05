pragma solidity ^0.5.0;

//import "../../node_modules/@openzeppelin/contracts/math/SafeMath.sol";

//import "../../node_modules/@openzeppelin/contracts/math/SafeMath.sol";
import "@openzeppelin/contracts/math/SafeMath.sol";


contract CampaignAccount {

    using SafeMath for uint256;

   mapping(uint256 => string) private _campaigns;

    /// @dev balances for each address and campaign
    ///
    /// `_balances[campaignId][address]` - campaignId: uint256
    mapping(uint256 => mapping(address => uint256)) private _balances;



    /// @dev total supply by campaign
    ///
    /// `_totalByCampaign[campaignId]` - campaignId: uint256
    mapping(uint256 => uint256) private _totalByCampaign;


    /**
     *
     * Preconditios
     *
     * - `account` cannot be zero address.
     */
    function _mint(uint256 campaignId, address account, uint256 amount) internal{
        require(account != address(0), "CampaignAccount: Minting to zero address is illegal.");

        _balances[campaignId][account] = _balances[campaignId][account].add(amount);
        _totalByCampaign[campaignId] = _totalByCampaign[campaignId].add(amount);
    }


}
