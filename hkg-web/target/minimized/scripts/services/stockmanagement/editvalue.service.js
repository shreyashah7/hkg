define(["hkg"],function(a){a.register.factory("EditValueService",["$resource","$rootScope",function(c,b){var d=c(b.centerapipath+"stock/:action",{},{retrieveLotsPacketsEditValue:{method:"POST",params:{action:"/retrieveLotsAndPacketsEditValue"}},retrievePlansByLotOrPacket:{method:"POST",isArray:true,params:{action:"/retrievePlansByLotOrPacket"}},editValues:{method:"POST",params:{action:"/editValues"}}});
return d
}])
});