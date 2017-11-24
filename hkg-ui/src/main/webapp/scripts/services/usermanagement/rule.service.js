define(['hkg'], function (hkg) {
    hkg.register.factory('RuleService', ['$resource', '$rootScope', '$http', '$templateCache', function (resource, rootScope, $http, $templateCache) {

            var Rule = resource(
                    rootScope.apipath + 'rulemanagement/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods

                retrieveAll: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: '/retrieve'
                    }
                },
                retrieve: {
                    method: 'POST',
                    params: {
                        action: '/retrieve'
                    }
                },
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
//                delete: {
//                    method: 'POST',
//                    params: {
//                        action: '/delete'
//                    }
//                },
                retrievePrerequisite: {
                    method: 'GET',
                    params: {
                        action: 'retrieveprerequisite'
                    }
                },
                retrieveFieldsByEntity: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/field'
                    }
                },
                createRuleSet: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                updateRule: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                retrieveAllRule: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveRuleById: {
                    method: 'POST',
                    params: {
                        action: 'retrieve/rule'
                    }
                },
                deleteRuleById: {
                    method: 'POST',
                    params: {
                        action: 'remove'
                    }
                },
                retrieveMasterByFieldById: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/master'
                    }
                },
                retrieveActivityMasterByFieldById: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/activitymaster'
                    }
                },
                retrieveServiceMasterByFieldById: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/servicemaster'
                    }
                },
                saveRuleSet: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/save/ruleset'
                    }
                },
                retrieveDropListForSubEntity: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveDropListForSubEntity'
                    }
                },
                createDropDownListForSubEntity: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "createDropDownListForSubEntity"
                    }
                },
                retrieveLotStatus: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveLotStatus"
                    }
                },
                retrievePlanStatus: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrievePlanStatus"
                    }
                },
                retrieveInvoiceAndParcelStatus: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveInvoiceAndParcelStatus"
                    }
                },
                retrieveIssueStatus: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveIssueStatus"
                    }
                },
                typeOfPlan: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "typeOfPlan"
                    }
                }
            }
            );

            return Rule;
        }]);
});


