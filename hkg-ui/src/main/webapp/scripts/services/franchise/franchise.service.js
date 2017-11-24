define(['hkg'], function(hkg) {
    hkg.register.factory('FranchiseService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Franchise = resource(rootScope.apipath + 'franchise/:action', {},
                    {
                        createFranchise: {
                            method: 'PUT',
                            params: {
                                action: 'create'
                            }
                        },
                        retrieveDesignations: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievedesignations'
                            }
                        },
                        retrieveMachines: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievemachines'
                            }
                        },
                        doesFranchiseNameExist: {
                            method: 'PUT',
                            params: {
                                action: "doesfranchisenameexist"
                            }
                        },
                        doesUserNameExist: {
                            method: 'PUT',
                            params: {
                                action: "doesusernameexist"
                            }
                        },
                        retrieveFranchiseDetailById: {
                            method: 'POST',
                            params: {
                                action: "retrieve"
                            }
                        },
                        retrieveAllLocations: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: "retrieve/locations"
                            }
                        },
                        retrieveAllFranchise: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrieve/allfranchise"
                            }
                        },
                        updateFranchise: {
                            method: 'POST',
                            params: {
                                action: 'update'
                            }
                        },
                        retrieveAllFranchiseMinReq: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrieve/franchiseminreq"
                            }
                        },
                        deleteFranchise: {
                            method: 'POST',
                            params: {
                                action: 'delete'
                            }
                        },
                        isThereAnyLinkWithFranchise: {
                            method: 'POST',
                            params: {
                                action: 'isthereanylinkwithfranchise'
                            }
                        },
                        retrieveLocationsByType: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrievelocationsbytype"
                            }
                        },
                        retrieveAllActiveFranchiseSelectlist: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrieve/allactivefranchiseselectlist"
                            }
                        },
                        retrieveFranchiseNames: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrievefranchisenames"
                            }
                        }
                    });
            return Franchise;
        }]);
});
