define(['hkg'], function (hkg) {
    hkg.register.factory('FranchiseConfigService', ['$resource', '$rootScope', function (resource, rootScope) {
            var FranchiseConfig = resource(rootScope.apipath + 'franchiseconfig/:action', {},
                    {
                        saveConfiguration: {
                            method: 'PUT',
                            params: {
                                action: 'create'
                            }
                        },
                        uploadAvtar: {
                            method: 'POST',
                            params: {
                                action: 'uploadfile'
                            }
                        },
                        retrieveAllConfiguration: {
                            method: 'GET',
                            params: {
                                action: 'retrieveallconfig'
                            }
                        },
                        checkOldPassword: {
                            method: 'POST',
                            params: {
                                action: 'checkoldpassword'
                            }
                        },
                        retrieveEmployeeTypes: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieveemptypes'
                            }
                        },
                        removeFileFromTemp: {
                            method: 'POST',
                            params: {
                                action: 'removeFileFromTemp'
                            }
                        },
                        retrieveEntities: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieveentities'
                            }
                        },
                        saveIdConfiguration: {
                            method: 'PUT',
                            params: {
                                action: 'saveIdConfiguration'
                            }
                        },
                        retrieveFranchiseDesignation: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieveFranchiseDesignations'
                            }
                        }
                    });
            return FranchiseConfig;
        }]);
});