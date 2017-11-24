define(['hkg'], function (hkg) {
    hkg.register.factory('ManageMasterService', ['$resource', '$rootScope', function (resource, rootScope) {
            var Master = resource(rootScope.apipath + 'master/:action', {}, {
                retrieveListOfMaster: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveDetailsOfMaster: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteById: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                retrieveSystemFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve/systemfeatures'
                    }
                },
                authenticateForEditMaster: {
                    method: 'PUT',
                    params: {
                        action: 'checkpassword'
                    }
                },
                retrieveLanguage: {
                    method: 'GET',
                    params: {
                        action: 'retrievelanguages'
                    }
                },
                retrieveCustomFields: {
                    method: 'POST',
                    params: {
                        action: 'retrievecustomfields'
                    }
                },
                retrieveCustomFieldsValueByKey: {
                    method: 'POST',
                    params: {
                        action: 'retrievecustomfieldsvaluebykey'
                    }
                },
                saveException: {
                    method: 'POST',
                    params: {
                        action: 'saveexception'
                    }
                },
                retrieveValueExceptions: {
                    method: 'POST',
                    params: {
                        action: 'retrievevalueexceptions'
                    }
                },
                retrievePrerequisite : {
                    method: 'POST',
                    params: {
                        action: 'retrieveprerequisites'
                    }
                }
            });
            return Master;
        }]);
});