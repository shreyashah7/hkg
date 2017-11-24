define(['hkg', 'roughMakeableService', 'customFieldService', 'customsearch.directive', 'ngload!uiGrid',
    'dynamicForm', 'ruleExecutionService', 'addMasterValue', 'rapCalcyDirective'], function (hkg) {
    hkg.register.controller('RoughMakeableController', ["$rootScope", "$scope", "RoughMakeableService",
        "DynamicFormService", "CustomFieldService", "RuleExecutionService", "$timeout", "RapCalcyService",
        function ($rootScope, $scope, RoughMakeableService, DynamicFormService, CustomFieldService,
                RuleExecutionService, $timeout, RapCalcyService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "RoughMakeable";
            $scope.entity = 'ROUGHMAKEABLE.';
            var gridApipacket,
                    uiGridpacketCustomFieldData = {},
                    dbFieldNamePacket,
                    fieldIdNameMap,
                    inputRetrievePktInStkOf,
                    roughMakeableTemplate = {},
                    roughMakeableTemplateRoughCalc = {},
                    fieldIdNameArm = {},
                    fieldIdNameRc = {},
                    dbFieldNameArm = [],
                    dbFieldNameRc = [];

            $scope.entity = "ROUGHMAKEABLE.";
            $scope.staticRoughMakeableModel = {};
            $scope.categoryCustomRoughMakeable = {};
            $scope.categoryCustomRoughMakeableRoughCalc = {};
            $scope.fieldsNotConfiguredFlag = {};



            var initializePacketGrid = function () {
                console.log("masking");$rootScope.maskLoading();
                $scope.gridOptionspacket = {
                    enableRowSelection: true,
                    enableSelectAll: true,
                    enableFiltering: true,
                    multiSelect: false,
                    data: [],
                    columnDefs: [],
                    onRegisterApi: function (gridApi) {
                        gridApipacket = gridApi;
                        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                            $scope.roughMakeableToUpdateId = null;
                            if (row.isSelected == true) {
                                $scope.staticRoughMakeableModel.selectedPacket = row.entity;
                                initAddRoughMakeableForm();
                            }
                            else {
                                $scope.staticRoughMakeableModel.selectedPacket = null;
                            }
                        });
                    }
                };
                // makeableRough = feature name
                // GEN = section name 
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableRough", "GEN"], function (response) {
                    if (response.Packet == null || response.Packet.length == 0) {
                        $scope.fieldsNotConfiguredFlag.packet = "No fields are configured for packet";
                        $rootScope.unMaskLoading();
                    }
                    else {
                        $scope.fieldsNotConfiguredFlag.packet = null;
                        $scope.packetDataBean = {};
                        // packet : entity name 
                        var templateForPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        templateForPacket.then(function (section) {
                            uiGridpacketCustomFieldData.template = null;
                            uiGridpacketCustomFieldData.template = section['genralSection'];
                            var packetField = [];
                            Object.keys(response).map(function (key, value) {
                                angular.forEach(this[key], function (itr) {
                                    if (key === 'Packet') {
                                        packetField.push({packet: itr});
                                    }
                                });
                            }, response);
                            dbFieldNamePacket = [];
                            fieldIdNameMap = {};
                            uiGridpacketCustomFieldData.template = DynamicFormService.retrieveCustomData(uiGridpacketCustomFieldData.template, packetField);
                            angular.forEach(uiGridpacketCustomFieldData.template, function (itr) {
                                if (itr.fromModel) {
                                    fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                    $scope.gridOptionspacket.columnDefs.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addpacketScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    if (dbFieldNamePacket.indexOf(itr.fromModel) === -1)
                                        dbFieldNamePacket.push(itr.fromModel);
                                } else if (itr.toModel) {
                                    fieldIdNameMap[itr.fieldId] = itr.toModel;
                                    $scope.gridOptionspacket.columnDefs.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addpacketScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    if (dbFieldNamePacket.indexOf(itr.toModel) === -1)
                                        dbFieldNamePacket.push(itr.toModel);
                                } else if (itr.model) {
                                    fieldIdNameMap[itr.fieldId] = itr.model;
                                    $scope.gridOptionspacket.columnDefs.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addpacketScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    if (dbFieldNamePacket.indexOf(itr.model) === -1)
                                        dbFieldNamePacket.push(itr.model);
                                }
                            });

                            inputRetrievePktInStkOf = {};
                            inputRetrievePktInStkOf['dbFieldNames'] = dbFieldNamePacket;
                            if (dbFieldNamePacket.length > 0) {
                                var packetDatabean = {
                                    featureDbFieldMap: inputRetrievePktInStkOf,
                                    ruleConfigMap: {
                                        fieldIdNameMap: fieldIdNameMap,
                                        featureName: "roughMakeable"
                                    }

                                };
                                RoughMakeableService.retrievePacketsInStockOf(packetDatabean, function (res) {
                                    if (res.length <= 0) {
                                        $scope.fieldsNotConfiguredFlag.numOfPkt = 'No packets available';
                                        $rootScope.unMaskLoading();
                                    }
                                    else {
                                        var packets;
                                        var success = function (result) {
                                            packets = angular.copy(result);
                                            for (var packet in packets) {
                                                var packetObjWithId = {};
                                                packets[packet].categoryCustom.$$packetId$$ = packets[packet].value;
                                                packets[packet].categoryCustom.screenRuleDetailsWithDbFieldName = packets[packet].screenRuleDetailsWithDbFieldName;
                                                angular.extend(packetObjWithId, packets[packet].categoryCustom, {id: packets[packet].value})
                                                $scope.gridOptionspacket.data.push(packetObjWithId);
                                            }
                                            console.log("unmasking");$rootScope.unMaskLoading();
                                        }
                                        var newVar = angular.copy(res);
                                        if (newVar !== undefined) {
                                            DynamicFormService.convertorForCustomField(newVar, success);
                                        }
                                    }
                                    $scope.fieldsNotConfiguredFlag.numOfPkt=null;
                                }
                                , function () {
                                    var msg = "Could not retrieve packets, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    console.log("unmasking");$rootScope.unMaskLoading();
                                });
                            }
                        }, function (reason) {
                            console.log("unmasking");$rootScope.unMaskLoading();
                        });
                    }
                }, function () {
                    console.log("unmasking");$rootScope.unMaskLoading();
                    var msg = "Failed to retrieve data";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
                $scope.addpacketScreenRule = function (row, colField) {
                    var color;

                    if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                    return color;
                };
//                console.log("unmasking");$rootScope.unMaskLoading();
            };

            var initAddRoughMakeableForm = function () {
                var dataToSend = {
                    featureName: 'roughMakeable',
                    entityId: $scope.staticRoughMakeableModel.selectedPacket.id,
                    entityType: 'packet',
                };
                RuleExecutionService.executePreRule(dataToSend, function (res) {
                    //Prevent the record from edit if pre rule satisfies.
                    if (!!res.validationMessage) {
                        $scope.preRuleSatisfied = true;
                        var type = $rootScope.warning;
                        $rootScope.addMessage(res.validationMessage, type);
                        $scope.staticRoughMakeableModel.selectedPacket = null;
                    }
                    else {
                        var fieldIdNameArmAndRc = {};
                        angular.extend(fieldIdNameArmAndRc, fieldIdNameArm, fieldIdNameRc);
                        var dbFieldNameArmAndRc = dbFieldNameArm.concat(dbFieldNameRc);
                        if (dbFieldNameArmAndRc.length > 0) {
                            var roughMakeableDatabean = {
                                dbFieldNames: dbFieldNameArmAndRc,
                                ruleConfigMap: {
                                    fieldIdNameMap: fieldIdNameArmAndRc,
                                    featureName: "roughMakeable"
                                },
                                packetId: $scope.staticRoughMakeableModel.selectedPacket.id
                            };
                        }
                        RoughMakeableService.retrieveRoughMakeableByPktId(roughMakeableDatabean, function (roughMakeable) {
                            $scope.resetAddRoughMakeableForm(roughMakeable);
                        });

                    }
                }, function (failure) {
                    console.log("unmasking");$rootScope.unMaskLoading();
                    var msg = "Failed to retrieve pre rule.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });

            };

            var retrieveTemplateForAddRoughMakeable = function () {
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableRough", "ARM"], function (responseARM) {
                    if (responseARM.RoughMakeable == null || responseARM.RoughMakeable.length == 0) {
                        $scope.fieldsNotConfiguredFlag.RoughMakeable = 'No fields are configured for Rough Makeable';
                    }
                    else {
                        $scope.fieldsNotConfiguredFlag.RoughMakeable = null;
                    }
                    // RoughMakeableEntity : entity name 
                    var templateForRoughMakeable = DynamicFormService.retrieveSectionWiseCustomFieldInfo("roughMakeableEntity");
                    templateForRoughMakeable.then(function (section) {
                        var roughMakeableField = [];
                        Object.keys(responseARM).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'RoughMakeable') {
                                    roughMakeableField.push({RoughMakeable: itr});
                                }
                            });
                        }, responseARM);
                        roughMakeableTemplate = DynamicFormService.retrieveCustomData(section['genralSection'], roughMakeableField);

                        angular.forEach(roughMakeableTemplate, function (itr) {
                            if (itr.fromModel) {
                                fieldIdNameArm[itr.fieldId] = itr.fromModel;
                                if (dbFieldNameArm.indexOf(itr.fromModel) === -1)
                                    dbFieldNameArm.push(itr.fromModel);
                            } else if (itr.toModel) {
                                fieldIdNameArm[itr.fieldId] = itr.toModel;
                                if (dbFieldNameArm.indexOf(itr.toModel) === -1)
                                    dbFieldNameArm.push(itr.toModel);
                            } else if (itr.model) {
                                fieldIdNameArm[itr.fieldId] = itr.model;
                                if (dbFieldNameArm.indexOf(itr.model) === -1)
                                    dbFieldNameArm.push(itr.model);
                            }
                        });
                        CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableRough", "ARMRC"], function (responseARMRC) {
                            if (responseARMRC.RoughCalcy == null || responseARMRC.RoughCalcy.length == 0) {
                                $scope.fieldsNotConfiguredFlag.RoughCalcy = 'No fields are configured for Rap Calc';
                            }
                            else {
                                $scope.fieldsNotConfiguredFlag.RoughCalcy = null;
                            }
                            var templateForRoughCalcy = DynamicFormService.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
                            templateForRoughCalcy.then(function (section) {
                                var roughCalcField = [];
                                Object.keys(responseARMRC).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'RoughCalcy') {
                                            roughCalcField.push({RoughMakeable: itr});
                                        }
                                    });
                                }, responseARMRC);
                                section['genralSection'] = DynamicFormService.retrieveCustomData(section['genralSection'], roughCalcField);
                                angular.forEach(section['genralSection'], function (itr) {
                                    if (itr.model) {
                                        fieldIdNameRc[itr.fieldId] = itr.model;
                                        if (dbFieldNameRc.indexOf(itr.model) === -1)
                                            dbFieldNameRc.push(itr.model);
                                    } else if (itr.toModel) {
                                        fieldIdNameRc[itr.fieldId] = itr.toModel;
                                        if (dbFieldNameRc.indexOf(itr.toModel) === -1)
                                            dbFieldNameRc.push(itr.toModel);
                                    } else if (itr.fromModel) {
                                        fieldIdNameRc[itr.fieldId] = itr.fromModel;
                                        if (dbFieldNameRc.indexOf(itr.fromModel) === -1)
                                            dbFieldNameRc.push(itr.fromModel);
                                    }
                                });
//                                dbFieldNameArmAndRc = dbFieldNameArmAndRc.concat(rouchCalcConfiguredFields);
                                for (var index = 0; index < $rootScope.fourCMap.length; index++) {
                                    if (dbFieldNameRc.indexOf($rootScope.fourCMap[index]) < 0) {
                                        $scope.mandatoryRoughCalcFieldNotconfigured = 'Mandatory fields are not configured for your designation, Please contact administrator';
                                    }
                                }
                                roughMakeableTemplateRoughCalc = section['genralSection'];
                                $scope.categoryCustomRoughMakeable = {};
                                $scope.categoryCustomRoughMakeableRoughCalc = {};
                                $scope.roughMakeableDbType = {};
                                $scope.roughMakeableDbTypeRoughCalc = {};
                                $scope.templateAddRoughMakeAble = angular.copy(roughMakeableTemplate);
                                $scope.templateAddRoughMakeAbleRoughCalc = angular.copy(roughMakeableTemplateRoughCalc);
                                $scope.$watch("categoryCustomRoughMakeableRoughCalc", function () {
                                    calculateRate();
                                }, true);
                            });
                        }, function () {
                            console.log("unmasking");$rootScope.unMaskLoading();
                            var msg = "Failed to retrieve RoughCalcy data";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    });
                }, function () {
                    console.log("unmasking");$rootScope.unMaskLoading();
                    var msg = "Failed to retrieve RoughMakeable data";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };



            $scope.resetAddRoughMakeableForm = function (roughMakeable, clearSelectedGridRow) {
                $scope.templateAddRoughMakeAble = angular.copy(roughMakeableTemplate);
                $scope.templateAddRoughMakeAbleRoughCalc = angular.copy(roughMakeableTemplateRoughCalc);
                $scope.categoryCustomRoughMakeable = {};
                $scope.categoryCustomRoughMakeableRoughCalc = {};
                if (roughMakeable != null && roughMakeable.data != null) {
                    var roughMakeableCategoryCustomToUpdate = roughMakeable.data.categoryCustom;
                    $scope.roughMakeableToUpdateId = roughMakeable.data.value;
                    if (roughMakeableCategoryCustomToUpdate != null) {
                        angular.forEach(dbFieldNameArm, function (value) {
                            $scope.categoryCustomRoughMakeable[value] = roughMakeableCategoryCustomToUpdate[value];
                        });
                        angular.forEach(dbFieldNameRc, function (value) {
                            $scope.categoryCustomRoughMakeableRoughCalc[value] = roughMakeableCategoryCustomToUpdate[value];
                        });
                    }
                }
                $scope.roughMakeableDbType = {};
                $scope.roughMakeableDbTypeRoughCalc = {};
                $scope.submitted = false;
                /**
                 * start
                 * This code is to refresh dynamic form so that component like usermultiselect can get refreshed.
                 */


                //End
                if (clearSelectedGridRow == true && gridApipacket != null) {
                    gridApipacket.selection.clearSelectedRows();
                    $scope.staticRoughMakeableModel.selectedPacket = null;
                }
                else {
                    var selectedpacket = $scope.staticRoughMakeableModel.selectedPacket;
                    $scope.staticRoughMakeableModel.selectedPacket = null;
                    $timeout(function () {
                        $scope.staticRoughMakeableModel.selectedPacket = selectedpacket;
                    }, 100);
                }
            };

            $scope.createOrUpdateRoughMakeable = function (roughMakeableForm) {
                $scope.submitted = true;
                if (roughMakeableForm.$valid) {
                    angular.extend($scope.categoryCustomRoughMakeable, $scope.categoryCustomRoughMakeableRoughCalc);
                    angular.extend($scope.roughMakeableDbType, $scope.roughMakeableDbTypeRoughCalc);
                    var dataToSend = {
                        featureName: 'roughMakeable',
                        entityId: null,
                        entityType: 'roughMakeableEntity',
                        currentFieldValueMap: $scope.categoryCustomRoughMakeable,
                        dbType: $scope.roughMakeableDbType,
                        otherEntitysIdMap: {packetId: $scope.staticRoughMakeableModel.selectedPacket.id}
                    };
                    $scope.submitted = false;
                    RuleExecutionService.executePostRule(dataToSend, function (res) {
                        if (!!res.validationMessage) {
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                        } else {
                            var roughMakeableDatabean = {
                                id: $scope.roughMakeableToUpdateId,
                                roughMakeableCustom: $scope.categoryCustomRoughMakeable,
                                roughMakeableDbType: $scope.roughMakeableDbType,
                                packetId: $scope.staticRoughMakeableModel.selectedPacket.id
                            };
                            if (roughMakeableDatabean.id == null) {
                                RoughMakeableService.createRoughMakeable(roughMakeableDatabean, function () {
                                    $scope.resetAddRoughMakeableForm(null, true);
                                });
                            } else {
                                RoughMakeableService.updateRoughMakeable(roughMakeableDatabean, function () {
                                    $scope.resetAddRoughMakeableForm(null, true);
                                });
                            }
                        }
                    });
                }
            };



            var calculateRate = function () {
//                Check if all 4c are selected
                var fourCPresent = true;
                var calc = {};
                calc.fourCMap = {};
                for (var i = 0; i < $rootScope.fourCMap.length; i++) {
                    var value = $scope.categoryCustomRoughMakeable[$rootScope.fourCMap[i]];
                    if (!value) {
                        fourCPresent = false;
                        break;
                    }
                    calc.fourCMap[$rootScope.fourCMap[i]] = value;
                }

                //If they are apresent then calculate basprice and discount
                if (fourCPresent) {
                    calc.discountDetailsMap = $scope.categoryCustomRoughMakeable;
                    RapCalcyService.calculateDiamondPrice(calc, function (response) {
                        $scope.calcFinal = response.data;
                        if ($scope.calcFinal.baseAmount == null) {
                            $scope.calcFinal.baseAmount = 0.0
                        }
                        if ($scope.calcFinal.discount == null) {
                            $scope.calcFinal.discount = 0.0
                        }
                        if ($scope.calcFinal.amount == null) {
                            $scope.calcFinal.amount = 0.0
                        }
                        if ($scope.calcFinal.mixAmount == null) {
                            $scope.calcFinal.mixAmount = 0.0
                        }
                    });
                }
            };

            initializePacketGrid();
            retrieveTemplateForAddRoughMakeable();
        }]);
});

