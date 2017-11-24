define(["hkg"],function(a){a.register.factory("TransferStockService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"transferstock/:action",{},{retrieve:{method:"GET",params:{action:"/retrieve"}},update:{method:"POST",params:{action:"/update"}},create:{method:"PUT",params:{action:"/create"}},retrieveStocks:{method:"POST",isArray:false,params:{action:"retrievesearchedstock"}},retrieveStockByWorkAllotmentId:{method:"POST",isArray:false,params:{action:"retrievestockbyworkallotmentid"}},transferStock:{method:"POST",isArray:false,params:{action:"transfer"}}});
return d
}])
});