define(["hkg"],function(a){a.register.factory("RoughSaleService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"roughsale/:action",{},{saleParcels:{method:"POST",isArray:false,params:{action:"sell"}},retrieveAllSellDocument:{method:"POST",isArray:true,params:{action:"retrieve"}},retrieveselldocumentforparcelbyid:{method:"POST",params:{action:"retrieveselldocumentbyid"}},search:{method:"POST",isArray:true,params:{action:"retrievesearcheddata"}}});
return d
}])
});