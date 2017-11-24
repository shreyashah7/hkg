define(["hkg"],function(a){a.register.factory("LocationService",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"location/:action",{},{retriveAllLocations:{method:"GET",isArray:false,params:{action:"retrieve/locations"}},retriveLocationstree:{method:"GET",isArray:true,params:{action:"retrieve/locationtree"}},saveAllLocations:{method:"POST",isArray:false,params:{action:"create"}},updateLocations:{method:"POST",isArray:false,params:{action:"update"}}});
return b
}])
});