define(["hkg"],function(a){a.register.factory("DepartmentFlowService",["$resource","$rootScope",function(c,b){var d=c(b.apipath+"departmentflow/:action",{},{retrieve:{method:"GET",params:{action:"/retrieve"}},update:{method:"POST",params:{action:"/update"}},create:{method:"PUT",params:{action:"/create"}},retrieveDataForProcessFlow:{method:"GET",params:{action:"/retrieveDataForProcessFlow"}}});
return d
}])
});