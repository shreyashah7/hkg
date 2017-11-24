define(["hkg"],function(a){a.register.factory("ChangeStausService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"statuschange/:action",{},{search:{method:"POST",isArray:true,params:{action:"searchlots"}},retrieveStockByLotIdOrPacketId:{method:"POST",isArray:true,params:{action:"retrievelot"}},retrieveStatusMapAndPraposedStatusMap:{method:"GET",isArray:false,params:{action:"retrievestatusmap"}},onSave:{method:"POST",isArray:false,params:{action:"onsave"}}});
return d
}])
});