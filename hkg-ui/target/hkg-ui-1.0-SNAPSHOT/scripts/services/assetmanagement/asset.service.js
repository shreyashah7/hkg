define(['hkg'], function(hkg) {
    hkg.register.factory('AssetService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Asset = resource(rootScope.apipath + 'asset/:action',
                    {
                    },
                    {
                        issueAsset: {
                            method: 'POST',
                            params: {
                                action: '/issueasset'
                            }
                        },
                        createcategory: {
                            method: 'POST',
                            params: {
                                action: '/createcategory'
                            }
                        },
                        retrieveCategories: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievecategories'
                            }
                        },
                        updatecategory: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'updatecategory'
                            }
                        },
                        createasset: {
                            method: 'PUT',
                            isArray: false,
                            params: {
                                action: 'create'
                            }

                        },
                        retrieveAssets: {
                            method: 'POST',
                            isArray: 'true',
                            params: {
                                action: 'retrieveassets'
                            }
                        },
                        retrievemanifacturer: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievemanifacturer'
                            }
                        },
                        cancelform: {
                            method: 'POST',
                            params: {
                                action: '/cancelform'
                            }
                        },
                        fillstatuslist: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'fillstatuslist'
                            }
                        },
                        updateasset: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'update'
                            }
                        },
                        removecategory: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'removecategory'
                            }
                        },
                        retrievesamenamesuggestion: {
                            method: 'POST',
                            params: {
                                action: 'retrievesamenamesuggestion'
                            }
                        },
                        doesserialnumberexist: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'doesserialnumberexist'
                            }
                        },
                        retriveAssetById: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retriveAssetById'
                            }
                        },
                        addCustomDataToCategoryDataBean: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'addCustomDataToCategoryDataBean'
                            }
                        },
                        uploadFile: {
                            method: 'POST',
                            params: {
                                action: 'uploadFile'
                            }
                        },
                        addCustomDataToAssetDataBean: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'addCustomDataToAssetDataBean'
                            }
                        },
                        retrieveAssetsForIssue: {
                            method: 'POST',
                            isArray: 'true',
                            params: {
                                action: 'retrieveassetsforissue'
                            }
                        },
                        removeFileFromSession: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'removeFileFromSession'
                            }
                        },
                        retrieveUserList: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveusers'
                            }
                        },
                        retrieveDepartmentList: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveDepartmentList'
                            }
                        },
                        generateBarcode: {
                            method: 'POST',
                            params: {
                                action: 'generateBarcode'
                            }
                        }
                    });
            return Asset;
        }]);
});