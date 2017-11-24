define(['hkg'], function(hkg) {
    hkg.register.factory('Sync', ['$resource', '$rootScope', function(resource, rootScope) {
            var Sync = resource(rootScope.apipath + 'sync/:action',
                    {},
                    {
                        getFeatures: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: 'retrieve/features'
                            }
                        },
                        getConfigs: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieve/configs'
                            }
                        },
                        retrieveAllParameters: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieve/allparameters'
                            }
                        },
                        saveClientFeatureConfig: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'save/clientfeatureconfig'
                            }
                        },
                        saveFeature: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'save/feature'
                            }
                        },
                        createParameter: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'create/parameter'
                            }
                        },
                        getParams: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieve/params'
                            }
                        },
                        createConfig: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'create/configparam'
                            }
                        },
                        updateParam: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'update/param'
                            }
                        },
                        retrieveSyncDetails: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrieve/clientfeatureconfig'
                            }
                        },
                        updateConfig: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'update/featureconfig'
                            }
                        },
                        deleteFeatureConfig: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'delete/featureconfig'
                            }
                        }
                    });
            return Sync;
        }]);
});

