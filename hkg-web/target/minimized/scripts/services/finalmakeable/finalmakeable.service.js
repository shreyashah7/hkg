define(["hkg"],function(a){a.register.factory("FinalMakeableService",["$resource","$rootScope",function(d,b){var c=d(b.centerapipath+"finalmakeable/:action",{},{retrievePacketsInStockOf:{method:"POST",isArray:true,params:{action:"retrievepacketforfinalmakeable"}},createFinalMakeable:{method:"PUT",params:{action:"createfinalmakeable"}},retrieveFinalMakeableByPktId:{method:"POST",params:{action:"retrievefinalmakeablebypacketid"}},updateFinalMakeable:{method:"POST",params:{action:"updatefinalmakeable"}}});
return c
}])
});