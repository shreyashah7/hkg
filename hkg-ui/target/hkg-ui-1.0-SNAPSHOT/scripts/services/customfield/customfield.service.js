/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function (hkg) {
    hkg.register.factory('CustomFieldService', ['$resource', '$rootScope', function (resource, rootScope) {
            var customfieldManagment = resource(
                    rootScope.apipath + 'customfield/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                //methods
                retrieveFeatures: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: "retrievefeatures"
                    }

                },
                retrieveCustomFieldMastersForDependantField:
                        {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "retrieveCustomFieldMastersForDependantField"
                            }
                        },
                retrieveCustomFieldValuesForDependantField:
                        {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "retrieveCustomFieldValuesForDependantField"
                            }
                        },
                retriveCustomFieldById: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retriveCustomFieldById'
                    }
                },
                remove: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'remove'
                    }
                },
                retrieveSectionAndCustomFieldInfoByFeatureId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrievesectionandcustomfieldsbyid"
                    }

                },
                retrieveSectionAndCustomFieldInfoTemplateByFeatureId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrievesectionandcustomfieldtemplate"
                    },
                    url: rootScope.centerapipath + "customfield/:action"

                },
                retrieveEntitiesWithCustomFieldsForFormula: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'searchCustomFields'
                    }


                },
                retrievePrerequisite: {
                    method: 'GET',
                    params: {
                        action: 'retrieveprerequisite'
                    },
//                    url: rootScope.centerapipath + "customfield/:action"
                },
                create: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "createcustomfields"
                    }
                },
                createCustomField: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "createcustomfield"
                    }
                },
                retrieveDesignationBasedFields: {
                    method: 'POST',
                    params: {
                        action: '/retrievedesignationbasedfields'
                    },
//                    url: rootScope.centerapipath + "customfield/:action"
                },
                retrieveDesignationBasedFieldsBySection: {
                    method: 'POST',
                    params: {
                        action: '/retrievedesignationbasedfieldsbysection'
                    },
                    url: rootScope.centerapipath + "customfield/:action"
                },
                        retrieveExternalDesignationBasedFields: {
                            method: 'POST',
                            params: {
                                action: '/retrieveexternaldesignationbasedfields'
                            }
                        },
                retrieveCustomFieldOfTypeSubEntityByFeatureId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveCustomFieldOfTypeSubEntityByFeatureId"
                    }
                },
                saveSubEntitiesValue: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "saveSubEntitiesValue"
                    }
                },
                retrievelistofSubEntitiesValuesByInstanceId: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrievelistofSubEntitiesValuesByInstanceId"
                    }
                },
                retrieveAllFields: {
                    method: 'GET',
                    params: {
                        action: 'retrieveAllFields'
                    }
                },
                checkFieldIsInvolvedInOtherFields: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'checkFieldIsInvolvedInOtherFields'
                    }
                },
                // For Currency Component
                retrieveCurrencies: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievecurrencies'
                    }
                },
                defaultSelection: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'defaultSelection'
                    }
                },
                retrieveTreeViewFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveTreeViewFeatures'
                    }
                },
                retrieveFeatureOrSectionCustomFields: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieveFeatureOrSectionCustomFields'
                    }
                },
                updateSequenceNum: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'updateSequenceNum'
                    }
                },
                retrieveSubentityTreeViewFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveSubentityTreeViewFeatures'
                    }
                },
                createDropDownListForSubEntity: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'createDropDownListForSubEntity'
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
                retrievePrerequisiteForException: {
                    method: 'POST',
                    params: {
                        action: 'retrieveprerequisiteforexception'
                    }
                },
                retrieveDept: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveDept'
                    }
                },
                retrieveDesg: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveDesg'
                    }
                },
                retrieveFieldsForUniqueness: {
                    method: 'POST',
                    params: {
                        action: 'retrieveFieldForUniqueness'
                    }
                },
                retrieveFieldsForDate: {
                    method: 'POST',
                    params: {
                        action: 'retrieveDateEntities'
                    }
                }
            });
            return customfieldManagment;
        }]);
});