define(["hkg"],function(a){a.register.factory("RoughMakeableService",["$resource","$rootScope",function(d,c){var b=d(c.centerapipath+"roughmakeable/:action",{},{retrievePacketsInStockOf:{method:"POST",isArray:true,params:{action:"retrievepacketforroughmakeable"}},createRoughMakeable:{method:"PUT",params:{action:"createroughmakeable"}},retrieveRoughMakeableByPktId:{method:"POST",params:{action:"retrieveroughmakeablebypacketid"}},updateRoughMakeable:{method:"POST",params:{action:"updateroughmakeable"}}});
return b
}])
});