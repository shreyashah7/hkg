define(['hkg'], function(hkg) {
    hkg.register.factory('DepartmentConfigService', ['$resource', '$rootScope', function(resource, rootScope) {
            var DepartmentManagement = resource(
                    rootScope.apipath + 'configdept/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                
                retrieveDesignationByDept: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveDesignationForDept'
                    }
                },
                retrieveAllDepartments: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieveAllDepartments"
                    }

                },
                updateDepartmentConfig: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                retrieveConfigDetailByDepId: {
                    method: 'POST',
                    params: {
                        action: 'retriveConfigDetailByDepId'
                    }
                },
                updateDepartment: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                retrieveDepartmentSimpleTreeView: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrievedeptreeview"
                    }

                }, addCustomDataToDepartmentDataBean: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'addCustomDataToDepartmentDataBean'
                    }
                },
                retrieveAssociatedDepartments: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveAssociatedDepartments'
                    }
                }
            });
            return DepartmentManagement;
        }]);
});