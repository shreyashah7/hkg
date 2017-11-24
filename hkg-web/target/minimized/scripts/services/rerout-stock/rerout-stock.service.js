define(["hkg"],function(a){a.register.factory("ReRoutStockService",["$resource","$rootScope",function(d,b){var c=d(b.centerapipath+"reroutstock/:action",{},{search:{method:"POST",isArray:true,params:{action:"retrievesearcheddata"}},retrieveAllotToByActivityFlowNodeId:{method:"POST",isArray:true,params:{action:"retrieveallotto"}},stockReRout:{method:"POST",isArray:false,params:{action:"stockrerout"}}});
return c
}])
});