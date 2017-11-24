define(["hkg"],function(a){a.register.factory("SysConfig",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"systemconfiguration/:method",{method:"@methodName"},{createSys:{method:"POST",url:c.apipath+"systemconfiguration/create/:key/:value",params:{key:"@skey",value:"@svalue"}},retreiveAll:{method:"GET",isArray:true}});
return b
}])
});