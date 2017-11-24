define(["hkg"],function(a){a.register.factory("SysFeature",["$resource","$rootScope",function(c,b){var d=c(b.apipath+"feature/:action",{action:"@actionName"},{retreiveSystemFeatures:{method:"GET",isArray:true,params:{action:"retrieve/features"}},retreiveFeatureCategoryList:{method:"GET",params:{action:"retrievefeaturecategory"}},createSysFeature:{method:"POST",params:{action:"create/feature"},isArray:false},updateSysFeature:{method:"POST",params:{action:"update/feature"}},saveSequence:{method:"POST",params:{action:"savesequencing"}}});
return d
}])
});