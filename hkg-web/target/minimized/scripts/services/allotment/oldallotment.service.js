define(["hkg"],function(a){a.register.factory("OldAllotmentService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"allot/:action",{},{retrieveSearchedLotsAndPackets:{method:"POST",params:{action:"/retrieveSearchedLotsAndPackets"}},saveAllotment:{method:"POST",params:{action:"/savemanualallotment"}},retrieveAllotmentSuggestion:{method:"POST",isArray:true,params:{action:"/retrieveallotmentsuggestion"}},retrieveNextNodeDesignationIds:{method:"POST",params:{action:"/retrievenextnodedesignationids"}},retrieveLotOrPacketByAllotmentId:{method:"POST",params:{action:"/retrieveselectedstockdetails"}}});
return d
}])
});