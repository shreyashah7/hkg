define(["hkg"],function(a){a.register.factory("TransferEmployee",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"transferemployee/:action",{action:"@actionName"},{retrieveShiftsWithDepartmentName:{method:"GET",isArray:false,params:{action:"retrieveshiftwithdeptname"}},retrieveEmployeeStatus:{method:"GET",isArray:false,params:{action:"retrieveemployeestatus"}},retrieveEmployeesByShiftByDept:{method:"POST",params:{action:"retrieveeployeesbyshiftbydept"}},transferEmployeeByCriteria:{method:"POST",params:{action:"transferemployee"}}});
return b
}])
});