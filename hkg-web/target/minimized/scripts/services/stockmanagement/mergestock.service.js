define(["hkg"],function(a){a.register.factory("MergeStockService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"stock/:action",{},{retrieveSearchedLotsAndPackets:{method:"POST",params:{action:"/retrieveSearchedLotsAndPackets"}},retrieveSearchedLotsAndPacketsNew:{method:"POST",params:{action:"/retrieveSearchedLotsAndPacketsNew"}},mergeStock:{method:"POST",params:{action:"/mergeStock"}},retrieveLotOrPacketByAllotmentId:{method:"POST",params:{action:"/retrieveselectedstockdetails"}}});
return d
}])
});