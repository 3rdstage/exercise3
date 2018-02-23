var accs = personal.listAccounts
var num = accs.length;
for(var i = 0; i < num; i++){
   console.log("Unlocking account : " + i + "/" + num)
   personal.unlockAccount(accs[i], "", 0);
   
}