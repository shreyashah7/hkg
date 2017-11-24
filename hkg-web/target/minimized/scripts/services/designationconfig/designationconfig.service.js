define(["hkg"],function(a){a.register.factory("DesignationConfigService",["$resource","$rootScope",function(c,b){var d=c(b.apipath+"configdesign/:action",{action:"@actionName"},{retrieveAllDesignations:{method:"GET",isArray:true,params:{action:"retrieveAllDesignations"}},retrieveDesignationConfiguration:{method:"POST",params:{action:"retrieveDesignationConfiguration"}},updateDesignationConfiguration:{method:"POST",params:{action:"updateDesignationConfiguration"}}});
return d
}])
});