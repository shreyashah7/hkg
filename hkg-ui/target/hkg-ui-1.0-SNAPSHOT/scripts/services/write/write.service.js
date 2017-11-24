define(['hkg'], function(hkg) {
    hkg.register.factory('WriteService', ['$resource', '$rootScope', function(resource, rootScope) {
            var write = resource(rootScope.centerapipath + 'write/:action', {}, {
                retrieve: {
                    method: 'GET',
                    params: {
                        action: '/retrieve'
                    }
                },
                searchLotFromWorkAllocation: {
                    method: 'POST',
                    params: {
                        action: '/retrievestockfromworkallocation'
                    }
                },
                retrieveStockById: {
                    method: 'POST',
                    params: {
                        action: '/retrievestockbyid'
                    }
                },
                retrieveRootNodeDesignationIds: {
                    method: 'POST',
                    params: {
                        action: 'retrieverootnodedesignationid'
                    }
                },
                retrieveValuesOfMaster: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievevaluesofmaster'
                    }
                },
                checkCaratRange: {
                    method: 'POST',
                    params: {
                        action: 'checkcaratrange'
                    }
                },
                retrievePrice: {
                    method: 'POST',
                    params: {
                        action: 'retrieveprice'
                    }
                },
                savePlan: {
                    method: 'POST',
                    params: {
                        action: 'save'
                    }
                },
                submitPlan: {
                    method: 'PUT',
                    params: {
                        action: 'submit'
                    }
                },
                retrieveAccessiblePlan: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievesubmittedplan'
                    }
                },
                retrieveCurrency: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievecurrencycode'
                    }
                },
                retrieveSavedPlan: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievesavedplan'
                    }
                },
                deletePlan: {
                    method: 'PUT',
                    params: {
                        action: 'deleteplan'
                    }
                },
                saveEditedPlan: {
                    method: 'POST',
                    params: {
                        action: 'editsavedplan'
                    }
                },
            }

            );
            return write;
        }]);
});


