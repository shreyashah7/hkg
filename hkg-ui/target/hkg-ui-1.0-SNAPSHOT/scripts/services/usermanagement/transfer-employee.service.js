define(['hkg'], function (hkg) {
    hkg.register.factory('TransferEmployee', ['$resource', '$rootScope', function (resource, rootScope) {
            var transferEmployee = resource(
                    rootScope.apipath + 'transferemployee/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods
                retrieveShiftsWithDepartmentName: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: 'retrieveshiftwithdeptname'
                    }
                },
                retrieveEmployeeStatus: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: 'retrieveemployeestatus'
                    }
                },
                retrieveEmployeesByShiftByDept : {
                    method: 'POST',
                    params: {
                        action: 'retrieveeployeesbyshiftbydept'
                    }
                },
                transferEmployeeByCriteria :{
                    method: 'POST',
                    params: {
                        action: 'transferemployee'
                    }
                }
            }
            );
            return transferEmployee;
        }]);
});