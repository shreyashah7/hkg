define(['hkg'], function(hkg) {
    hkg.register.factory('GoalService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Goal = resource(rootScope.apipath + 'goal/:action', {},
                    {
                        retrieveGoalPermissionByDesignations: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retrievegoalpermissionbydesignations'
                            }
                        },
                        saveGoalTemplate: {
                            method: 'POST',
                            params: {
                                action: 'savegoaltemplate'
                            }
                        },
                        retrieveAllGoalTemplates: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveallgoaltemplates'
                            }
                        },
                        retrieveGoalTemplateById: {
                            method: 'POST',
                            params: {
                                action: 'retrievegoaltemplatebyid'
                            }
                        },
                        retrieveGoalTemplatesBySearch: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'searchgoaltemplate'
                            }
                        },
                        retrieveActiveGoalTemplatesByService: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveactivegoaltemplatesbyservice'
                            }
                        },
                        retrieveActiveGoalTemplatesByDepartment: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveactivegoaltemplatesbydepartment'
                            }
                        },
                        retrieveActiveGoalTemplatesByDesignation: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveactivegoaltemplatesbydesignation'
                            }
                        },
                        saveGoalTemplates: {
                            method: 'POST',
                            params: {
                                action: 'saveGoalTemplates'
                            }
                        },
                        retrieveActiveGoalTemplatesBySearch: {
                            method: 'GET',
                            params: {
                                action: 'retrieveActiveGoalTemplatesBySearch'
                            }
                        },
                        deleteActiveGoalTemplates: {
                            method: 'POST',
                            params: {
                                action: 'deleteActiveGoalTemplates'
                            }
                        },
                        retrieveGoalTemplateModifiers: {
                            method: 'POST',
                            params: {
                                action: 'retrieveGoalTemplateModifiers'
                            }
                        },
                        retrievependinggoalsheet: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievependinggoalsheet'
                            }
                        },
                        submitGoalSheet: {
                            method: 'POST',
                            params: {
                                action: 'submitgoalsheet'
                            }
                        },
                        retrievesubmittedgoalsheet: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievesubmittedgoalsheet'
                            }
                        },
                        retrieveusersforgoalsheet: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveusersforgoalsheet'
                            }
                        }
                    });
            return Goal;
        }]);
});
