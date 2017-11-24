define(["hkg"],function(a){a.register.factory("SellStockService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"sellstock/:action",{},{retrieve:{method:"GET",params:{action:"/retrieve"}},update:{method:"POST",params:{action:"/update"}},create:{method:"PUT",params:{action:"/create"}},retrieveSellstocks:{method:"POST",isArray:false,params:{action:"retrievesearchedstock"}},retrieveStockByWorkAllotmentId:{method:"POST",isArray:false,params:{action:"retrievestockbyworkallotmentid"}},sellStock:{method:"POST",isArray:false,params:{action:"sell"}}});
return d
}])
});