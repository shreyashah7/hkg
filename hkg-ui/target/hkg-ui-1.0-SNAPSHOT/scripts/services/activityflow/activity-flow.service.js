define(['hkg'], function(hkg) {
    hkg.register.factory('ActivityFlowService', ['$resource', '$rootScope', function(resource, rootScope) {
            var activity = resource(rootScope.apipath + 'activityflow/:action',
                    {
                    },
                    {
                        retrieve: {
                            method: 'GET',
                            params: {
                                action: '/retrieve'
                            }
                        },
//                        retrieve: {
//                            method: 'POST',
//                            params: {
//                                action: '/retrieve'
//                            }
//                        },
                        update: {
                            method: 'POST',
                            params: {
                                action: '/update'
                            }
                        },
                        create: {
                            method: 'PUT',
                            params: {
                                action: '/create'
                            }
                        },
                        /** delete is a keyword. Don't use it as object property or any variable name **/
//                        delete: {
//                            method: 'POST',
//                            params: {
//                                action: '/delete'
//                            }
//                        },
                        retrievePrerequisite: {
                            method: 'GET',
                            params: {
                                action: '/retrieveprerequisite'
                            }
                        },
                        addActivityFlowVersion: {
                            method: 'POST',
                            params: {
                                action: 'addactivityflowversion'
                            }
                        },
                        publishVersion: {
                            method: 'POST',
                            params: {
                                action: 'publishversion'
                            }
                        },
                        addActivityFlowGroup: {
                            method: 'POST',
                            params: {
                                action: 'addactivityflowgroup'
                            }
                        },
                        retrieveServices: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieveservices'
                            }
                        },
                        saveActivityNode: {
                            method: 'POST',
                            params: {
                                action: 'saveactivitynode'
                            }
                        },
                        saveActivityNodeRoute: {
                            method: 'POST',
                            params: {
                                action: 'saveactivitynoderoute'
                            }
                        },
                        retrieveActivityFlowVersion: {
                            method: 'POST',
                            params: {
                                action: 'retrieveactivityflowversion'
                            }
                        },
                        deleteComponent: {
                            method: 'POST',
                            params: {
                                action: 'deletecomponent'
                            }
                        },
                        retrieveActivityFlowByCompany: {
                            method: 'POST',
//                            isArray: true,
                            params: {
                                action: 'retrieveactivityflowbycompany'
                            }
                        },
                        updateCoordinateOfNode: {
                            method: 'POST',
                            params: {
                                action: 'updatecoordinateofnode'
                            }
                        },
                        updateCoordinateOfGroup: {
                            method: 'POST',
                            params: {
                                action: 'updatecoordinateofgroup'
                            }
                        },
                        updateGroup: {
                            method: 'POST',
                            params: {
                                action: 'updategroup'
                            }
                        },
                        retrieveDesignations: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: 'retrievedesignations'
                            }
                        },
                        updateNode: {
                            method: 'POST',
                            params: {
                                action: 'updatenode'
                            }
                        },
                        retrieveServicesWithActivity: {
                            method: 'GET',
                            params: {
                                action: 'retrieveServicesWithActivity'
                            }
                        },
                        retrieveModifiers: {
                            method: 'GET',
                            params: {
                                action: 'retrieveModifiers'
                            }
                        },
                        retrievePlanStatus: {
                            method: 'GET',
                            params: {
                                action: 'retrievePlanStatus'
                            }
                        },
                        retrieveUsersByCode:{
                            method:'POST',
                            isArray:true,
                            params:{
                                action:'retrieveUsersByCode'
                            }
                        }
                    }
            );
            return activity;
        }]);
});


