define(['hkg', 'finalMakeableService', 'customFieldService', 'customsearch.directive', 'ngload!uiGrid',
    'dynamicForm', 'ruleExecutionService', 'addMasterValue', 'rapCalcyDirective'], function (hkg) {
    hkg.register.controller('FinalMakeableController', ["$rootScope", "$scope", "FinalMakeableService",
        "DynamicFormService", "CustomFieldService", "RuleExecutionService", "$timeout", "RapCalcyService",
        function ($rootScope, $scope, FinalMakeableService, DynamicFormService, CustomFieldService,
                RuleExecutionService, $timeout, RapCalcyService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "FinalMakeable";
            $scope.entity = 'ROUGHMAKEABLE.';
            var gridApipacket,
                    uiGridpacketCustomFieldData = {},
                    dbFieldNamePacket,
                    fieldIdNameMap,
                    inputRetrievePktInStkOf,
                    finalMakeableTemplate = {},
                    finalMakeableTemplateRoughCalc = {},
                    fieldIdNameArm = {},
                    fieldIdNameRc = {},
                    dbFieldNameArm = [],
                    dbFieldNameRc = [];

            $scope.entity = "ROUGHMAKEABLE.";
            $scope.staticFinalMakeableModel = {};
            $scope.categoryCustomFinalMakeable = {};
            $scope.categoryCustomFinalMakeableRoughCalc = {};
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
                            $scope.finalMakeableToUpdateId = null;
                            if (row.isSelected == true) {
                                $scope.staticFinalMakeableModel.selectedPacket = row.entity;
                                initAddFinalMakeableForm();
                            }
                            else {
                                $scope.staticFinalMakeableModel.selectedPacket = null;
                            }
                        });
                    }
                };
                // makeableFinal = feature name
                // GEN = section name 
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableFinal", "GEN"], function (response) {
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
                                        featureName: "finalMakeable"
                                    }

                                };
                                FinalMakeableService.retrievePacketsInStockOf(packetDatabean, function (res) {
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

            var initAddFinalMakeableForm = function () {
                var dataToSend = {
                    featureName: 'finalMakeable',
                    entityId: $scope.staticFinalMakeableModel.selectedPacket.id,
                    entityType: 'packet',
                };
                RuleExecutionService.executePreRule(dataToSend, function (res) {
                    //Prevent the record from edit if pre rule satisfies.
                    if (!!res.validationMessage) {
                        $scope.preRuleSatisfied = true;
                        var type = $rootScope.warning;
                        $rootScope.addMessage(res.validationMessage, type);
                        $scope.staticFinalMakeableModel.selectedPacket = null;
                    }
                    else {
                        var fieldIdNameArmAndRc = {};
                        angular.extend(fieldIdNameArmAndRc, fieldIdNameArm, fieldIdNameRc);
                        var dbFieldNameArmAndRc = dbFieldNameArm.concat(dbFieldNameRc);
                        if (dbFieldNameArmAndRc.length > 0) {
                            var finalMakeableDatabean = {
                                dbFieldNames: dbFieldNameArmAndRc,
                                ruleConfigMap: {
                                    fieldIdNameMap: fieldIdNameArmAndRc,
                                    featureName: "finalMakeable"
                                },
                                packetId: $scope.staticFinalMakeableModel.selectedPacket.id
                            };
                        }
                        FinalMakeableService.retrieveFinalMakeableByPktId(finalMakeableDatabean, function (finalMakeable) {
                            $scope.resetAddFinalMakeableForm(finalMakeable);
                        });

                    }
                }, function (failure) {
                    console.log("unmasking");$rootScope.unMaskLoading();
                    var msg = "Failed to retrieve pre rule.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });

            };

            var retrieveTemplateForAddFinalMakeable = function () {
                CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableFinal", "AFM"], function (responseAFM) {
                    if (responseAFM.FinalMakeable == null || responseAFM.FinalMakeable.length == 0) {
                        $scope.fieldsNotConfiguredFlag.FinalMakeable = 'No fields are configured for Final Makeable';
                    }
                    else {
                        $scope.fieldsNotConfiguredFlag.FinalMakeable = null;
                    }
                    // FinalMakeableEntity : entity name 
                    var templateForFinalMakeable = DynamicFormService.retrieveSectionWiseCustomFieldInfo("finalMakeableEntity");
                    templateForFinalMakeable.then(function (section) {
                        var finalMakeableField = [];
                        Object.keys(responseAFM).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key === 'FinalMakeable') {
                                    finalMakeableField.push({FinalMakeable: itr});
                                }
                            });
                        }, responseAFM);
                        finalMakeableTemplate = DynamicFormService.retrieveCustomData(section['genralSection'], finalMakeableField);

                        angular.forEach(finalMakeableTemplate, function (itr) {
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
                        CustomFieldService.retrieveDesignationBasedFieldsBySection(["makeableFinal", "AFMRC"], function (responseAFMRC) {
                            if (responseAFMRC.RoughCalcy == null || responseAFMRC.RoughCalcy.length == 0) {
                                $scope.fieldsNotConfiguredFlag.RoughCalcy = 'No fields are configured for Rap Calc';
                            }
                            else {
                                $scope.fieldsNotConfiguredFlag.RoughCalcy = null;
                            }
                            var templateForRoughCalcy = DynamicFormService.retrieveSectionWiseCustomFieldInfo("finalCalcyEntity");
                            templateForRoughCalcy.then(function (section) {
                                var finalCalcField = [];
                                Object.keys(responseAFMRC).map(function (key, value) {
                                    angular.forEach(this[key], function (itr) {
                                        if (key === 'RoughCalcy') {
                                            finalCalcField.push({FinalMakeable: itr});
                                        }
                                    });
                                }, responseAFMRC);
                                section['genralSection'] = DynamicFormService.retrieveCustomData(section['genralSection'], finalCalcField);
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
                                finalMakeableTemplateRoughCalc = section['genralSection'];
                                $scope.categoryCustomFinalMakeable = {};
                                $scope.categoryCustomFinalMakeableRoughCalc = {};
                                $scope.finalMakeableDbType = {};
                                $scope.finalMakeableDbTypeRoughCalc = {};
                                $scope.templateAddFinalMakeAble = angular.copy(finalMakeableTemplate);
                                $scope.templateAddFinalMakeAbleRoughCalc = angular.copy(finalMakeableTemplateRoughCalc);
                                $scope.$watch("categoryCustomFinalMakeableRoughCalc", function () {
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
                    var msg = "Failed to retrieve FinalMakeable data";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            };



            $scope.resetAddFinalMakeableForm = function (finalMakeable, clearSelectedGridRow) {
                $scope.templateAddFinalMakeAble = angular.copy(finalMakeableTemplate);
                $scope.templateAddFinalMakeAbleRoughCalc = angular.copy(finalMakeableTemplateRoughCalc);
                $scope.categoryCustomFinalMakeable = {};
                $scope.categoryCustomFinalMakeableRoughCalc = {};
                if (finalMakeable != null && finalMakeable.data != null) {
                    var finalMakeableCategoryCustomToUpdate = finalMakeable.data.categoryCustom;
                    $scope.finalMakeableToUpdateId = finalMakeable.data.value;
                    if (finalMakeableCategoryCustomToUpdate != null) {
                        angular.forEach(dbFieldNameArm, function (value) {
                            $scope.categoryCustomFinalMakeable[value] = finalMakeableCategoryCustomToUpdate[value];
                        });
                        angular.forEach(dbFieldNameRc, function (value) {
                            $scope.categoryCustomFinalMakeableRoughCalc[value] = finalMakeableCategoryCustomToUpdate[value];
                        });
                    }
                }
                $scope.finalMakeableDbType = {};
                $scope.finalMakeableDbTypeRoughCalc = {};
                $scope.submitted = false;
                /**
                 * start
                 * This code is to refresh dynamic form so that component like usermultiselect can get refreshed.
                 */


                //End
                if (clearSelectedGridRow == true && gridApipacket != null) {
                    gridApipacket.selection.clearSelectedRows();
                    $scope.staticFinalMakeableModel.selectedPacket = null;
                }
                else {
                    var selectedpacket = $scope.staticFinalMakeableModel.selectedPacket;
                    $scope.staticFinalMakeableModel.selectedPacket = null;
                    $timeout(function () {
                        $scope.staticFinalMakeableModel.selectedPacket = selectedpacket;
                    }, 100);
                }
            };

            $scope.createOrUpdateFinalMakeable = function (finalMakeableForm) {
                $scope.submitted = true;
                if (finalMakeableForm.$valid) {
                    angular.extend($scope.categoryCustomFinalMakeable, $scope.categoryCustomFinalMakeableRoughCalc);
                    angular.extend($scope.finalMakeableDbType, $scope.finalMakeableDbTypeRoughCalc);
                    var dataToSend = {
                        featureName: 'finalMakeable',
                        entityId: null,
                        entityType: 'finalMakeableEntity',
                        currentFieldValueMap: $scope.categoryCustomFinalMakeable,
                        dbType: $scope.finalMakeableDbType,
                        otherEntitysIdMap: {packetId: $scope.staticFinalMakeableModel.selectedPacket.id}
                    };
                    $scope.submitted = false;
                    RuleExecutionService.executePostRule(dataToSend, function (res) {
                        if (!!res.validationMessage) {
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                        } else {
                            var finalMakeableDatabean = {
                                id: $scope.finalMakeableToUpdateId,
                                finalMakeableCustom: $scope.categoryCustomFinalMakeable,
                                finalMakeableDbType: $scope.finalMakeableDbType,
                                packetId: $scope.staticFinalMakeableModel.selectedPacket.id
                            };
                            if (finalMakeableDatabean.id == null) {
                                FinalMakeableService.createFinalMakeable(finalMakeableDatabean, function () {
                                    $scope.resetAddFinalMakeableForm(null, true);
                                });
                            } else {
                                FinalMakeableService.updateFinalMakeable(finalMakeableDatabean, function () {
                                    $scope.resetAddFinalMakeableForm(null, true);
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
                    var value = $scope.categoryCustomFinalMakeable[$rootScope.fourCMap[i]];
                    if (!value) {
                        fourCPresent = false;
                        break;
                    }
                    calc.fourCMap[$rootScope.fourCMap[i]] = value;
                }

                //If they are apresent then calculate basprice and discount
                if (fourCPresent) {
                    calc.discountDetailsMap = $scope.categoryCustomFinalMakeable;
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
            retrieveTemplateForAddFinalMakeable();
        }]);
});

