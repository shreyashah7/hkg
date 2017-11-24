define(["hkg"],function(a){a.register.factory("CaratRangeService",["$resource","$rootScope",function(d,b){var c=d(b.apipath+"caratrange/:action",{},{retrieveAll:{method:"GET",isArray:true,params:{action:"/retrieve"}},saveAll:{method:"POST",params:{action:"saveAll"}},retrieveCaratWithNoRange:{method:"GET",params:{action:"retrieveCaratWithNoRange"}}});
return c
}])
});