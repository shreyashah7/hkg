define(["hkg"],function(a){a.register.factory("SplitStockService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"stock/:action",{},{retrieveSearchedLotsAndPacketsForSplit:{method:"POST",params:{action:"/retrieveSearchedLotsAndPacketsForSplit"}},splitStock:{method:"POST",params:{action:"/splitStock"}},retrieveNextNodeDesignationIds:{method:"POST",params:{action:"/retrievenextnodedesignationids"}},retrieveSplitPacket:{method:"POST",isArray:true,params:{action:"/retrievesplitpacket"}}});
return d
}])
});