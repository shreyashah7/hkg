/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'accordionCollapse', 'ruleService', 'designationService', 'designationConfigService', 'ruleTemplate', 'designationTemplate', 'departmentFlowService'], function (hkg) {
    hkg.register.controller('DesignConfigController', ["$rootScope", "$scope", "Designation", "DesignationConfigService", "$filter", "$q", "$location", function ($rootScope, $scope, Designation, DesignationConfigService, $filter, $q, $location) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "configureDesignation";
            $rootScope.activateMenu();
            $scope.entity = 'CONFIG_DESIGN.';
            $scope.initializePage = function () {
                $rootScope.maskLoading();
                $scope.selectedDesignation = undefined;
                $scope.designation = undefined;
                DesignationConfigService.retrieveAllDesignations(function (response) {
                    $rootScope.unMaskLoading();
                    $scope.designationList = response;
                }, function (error) {
                    $rootScope.unMaskLoading();
                });
            };

            $scope.retrieveReportGroupNames = function () {
                $scope.groupIds = [];
                if ($scope.reportSysFeatures !== undefined) {
                    for (var i = 0; i < $scope.reportSysFeatures.length; i++) {
                        if ($scope.reportSysFeatures[i].description !== null && $scope.reportSysFeatures[i].description !== '' && $scope.reportSysFeatures[i].type === "RMI") {
                            var rowInvalid = false;
                            angular.forEach($scope.groupIds, function (item) {
                                if ($scope.reportSysFeatures[i].description === item) {
                                    rowInvalid = true;
                                }
                            });
                            if (!rowInvalid) {
                                $scope.groupIds.push($scope.reportSysFeatures[i].description);
                            }
                        }
                    }
                }
                Designation.retrieveReportGroupNames($scope.groupIds, function (res) {
                    $scope.groupFeatureMap = [];
                    $scope.defaultFeatures = [];
                    $scope.reportGroupIdNameMap = res.data;
                    if (!!res.data) {
                        for (var key in $scope.reportGroupIdNameMap) {
                            $scope.groupFeatures = [];
                            for (var i = 0; i < $scope.reportSysFeatures.length; i++) {
                                var rowInvalidStatus = false;
                                if ($scope.reportSysFeatures[i].description !== null && $scope.reportSysFeatures[i].type === "RMI") {
                                    if ($scope.reportSysFeatures[i].description === key) {
                                        $scope.groupFeatures.push($scope.reportSysFeatures[i]);
                                    }
                                } else {
                                    angular.forEach($scope.defaultFeatures, function (item) {
                                        if (item.id === $scope.reportSysFeatures[i].id) {
                                            rowInvalidStatus = true;
                                        }
                                    });
                                    if (!rowInvalidStatus) {
                                        $scope.defaultFeatures.push($scope.reportSysFeatures[i]);
                                    }
                                }
                            }
                            $scope.groupFeatureMap.push({groupName: $scope.reportGroupIdNameMap[key], features: $scope.groupFeatures});
                        }
                    } else {
                        for (var i = 0; i < $scope.reportSysFeatures.length; i++) {
                            var rowInvalidStatus = false;
                            if ($scope.reportSysFeatures[i].description !== null && $scope.reportSysFeatures[i].type === "RMI") {

                            } else {
                                angular.forEach($scope.defaultFeatures, function (item) {
                                    if (item.id === $scope.reportSysFeatures[i].id) {
                                        rowInvalidStatus = true;
                                    }
                                });
                                if (!rowInvalidStatus) {
                                    $scope.defaultFeatures.push($scope.reportSysFeatures[i]);
                                }
                            }
                        }
                    }

                    if ($scope.defaultFeatures !== undefined && $scope.defaultFeatures !== null && $scope.defaultFeatures.length > 0) {
                        $scope.groupFeatureMap.push({groupName: "Default", features: $scope.defaultFeatures});
                    }
                }, function () {
                });
            };

            $scope.configureDesignation = function (selectedDesignation) {
                if (selectedDesignation.currentNode === null || selectedDesignation.currentNode === undefined || (selectedDesignation.currentNode.parentId !== null && selectedDesignation.currentNode.parentId !== undefined))
                    return;
                if ($scope.selectedDesignation !== undefined && $scope.selectedDesignation.id === selectedDesignation.currentNode.id)
                    return;
                $scope.isDepartmentSelected = true;
                $scope.selectedDesignation = selectedDesignation.currentNode;
                $scope.customLabel = '';
                var promise = $scope.retrieveAllSysFeatures();
                promise.then(function () {
                    $rootScope.maskLoading();
                    var desgIds = [];
                    desgIds.push($scope.selectedDesignation.id);
                    Designation.retrieveFeaturesSelectedForDesignation(desgIds, function (responseData) {
                        if (responseData.data !== null && responseData.data !== undefined) {
                        } else {
                            responseData.data = {};
                        }
                        var featureIds = [];
                        $scope.nonDiamondSysFeatures = [];
                        $scope.diamondSysFeatures = [];
                        $scope.reportSysFeatures = [];
                        $scope.designation = {id: $scope.selectedDesignation.id, name: $scope.selectedDesignation.displayName, features: responseData.data[$scope.selectedDesignation.id], featureFieldMap: {}, featureModifierMap: {}};
                        angular.forEach($scope.designation.features, function (feature) {
                            //Allow checked by default.
                            if (feature.type == 'DMI') {
                                feature.checked = true;
                                $scope.diamondSysFeatures.push(feature);
                            }
                            if (feature.type == 'MI') {
                                $scope.nonDiamondSysFeatures.push(feature);
                            }
                            if (feature.type == 'RMI') {
                                $scope.reportSysFeatures.push(feature);
                            }
                            featureIds.push(feature.id);
                        });
                        $scope.retrieveReportGroupNames();
                        var requestMap = {
                            featureIds: featureIds,
                            designationId: $scope.selectedDesignation.id
                        }
                        DesignationConfigService.retrieveDesignationConfiguration(requestMap, function (res) {
                            setFeatureFieldPermission($scope.designation, res.featureFieldPermissionDataBeans);
                            var featureModifierMap = setFeatureModifiers($scope.designation, res.roleFeatureModifiers);
                            $scope.designation.featureModifierMap = featureModifierMap;
                            $rootScope.unMaskLoading();
                        }, function (error) {
                            $rootScope.addMessage("Some error occured, please try again", $rootScope.failure);
                            $rootScope.unMaskLoading();
                        });
                    })
                });

            };

            function setFeatureModifiers(designation, modifiers) {
                if (modifiers !== null && modifiers !== undefined) {
                    var featureModifierMap = {};
                    angular.forEach(modifiers, function (item) {
                        if (item.designation === designation.id) {
                            featureModifierMap[item.feature] = item;
                        }
                    });
                    return featureModifierMap;
                } else {
                    return {};
                }
            }

            function setFeatureFieldPermission(destination, origin) {
                if (origin !== undefined && origin !== null && origin.length > 0) {
                    var featureFieldMap = {};
                    angular.forEach(origin, function (origDeg) {
                        var fieldObj = {
                            designation: origDeg.designation,
                            feature: origDeg.feature,
                            searchFlag: origDeg.searchFlag,
                            parentViewFlag: origDeg.parentViewFlag,
                            readonlyFlag: origDeg.readonlyFlag,
                            editableFlag: origDeg.editableFlag,
                            isRequired: origDeg.isRequired,
                            sequenceNo: origDeg.sequenceNo,
                            entity: origDeg.entityName,
                            field: origDeg.fieldId,
                            sectionCode: origDeg.sectionCode
                        };
                        if (featureFieldMap[origDeg.feature] === undefined || featureFieldMap[origDeg.feature] === null) {
                            featureFieldMap[origDeg.feature] = [];
                        }
                        featureFieldMap[origDeg.feature].push(fieldObj);

                    });
                    angular.forEach(featureFieldMap, function (val, key) {
                        angular.forEach(destination.features, function (feature) {
                            //Allow checked by default.
                            if (feature.id == key) {
                                feature.configure = true;
                            }
                        });
                    });
                    destination.featureFieldMap = angular.copy(featureFieldMap);
                }

            }

            $scope.retrieveAllSysFeatures = function () {
                var deferred = $q.defer();
                var promise = deferred.promise;
                $rootScope.maskLoading();
                Designation.retrieveSystemFeatures(function (data) {
                    $rootScope.unMaskLoading();
                    $scope.systemFeaturesList = data;
                    if (!$scope.systemFeaturesList || $scope.systemFeaturesList === null) {
                        $scope.systemFeaturesList = [];
                    } else {
                        $scope.tempSystemFeatureList = [];
                        $scope.diamondSystemFeatureList = [];
                        $scope.reportSystemFeatureList = [];
                        angular.forEach($scope.systemFeaturesList, function (item) {
                            if (item.type === "DMI" || item.type === "DEI") {
                                $scope.diamondSystemFeatureList.push(item);
                            } else if (item.type === "RMI") {
                                $scope.reportSystemFeatureList.push(item);
                            } else {
                                $scope.tempSystemFeatureList.push(item);
                            }
                        });
                        $scope.systemFeaturesList = [];
                        $scope.systemFeaturesList = angular.copy($scope.tempSystemFeatureList);
                    }
                    deferred.resolve();
                }, function () {

                    $rootScope.unMaskLoading();
                    deferred.reject();
                });
                return promise;
            };

            $scope.checkValidationForm = function (form) {
                var isValid = true;
                angular.forEach(form.$error, function (value) {
                    angular.forEach(value, function (val) {
                        if (val.$name !== 'configDesignationForm') {
                            isValid = false;
                        }
                    });
                });
                return isValid;
            };

            $scope.checkParameters = function (form) {
                var isValid = true;
                if (form.$valid) {
                    isValid = true;
                } else {
                    isValid = $scope.checkValidationForm(form);
                }
                if (isValid) {
                    $scope.prepareModelDataAndSave($scope.designation);
                } else {
                    console.log("invlaid");
                }
            };


            $scope.prepareModelDataAndSave = function (designation) {
                var roleFeatureModifiers = [];
                var designationId = designation.id;
                var featureFieldPermissionDataBeans = [];

                if (Object.keys(designation.featureFieldMap).length > 0) {
                    angular.forEach(designation.featureFieldMap, function (fieldList, featureId) {
                        if (fieldList.length > 0) {
                            angular.forEach(fieldList, function (field) {
                                var fieldId = field.field;
                                var fieldObj = {
                                    id: designationId + '-' + featureId + '-' + field.sectionCode + '-' + fieldId,
                                    designation: designationId,
                                    feature: featureId,
                                    searchFlag: field.searchFlag,
                                    parentViewFlag: field.parentViewFlag,
                                    readonlyFlag: field.readonlyFlag,
                                    editableFlag: field.editableFlag,
                                    isRequired: field.isRequired,
                                    sequenceNo: field.sequenceNo,
                                    entityName: field.entity,
                                    fieldId: fieldId,
                                    sectionCode: field.sectionCode
                                };
                                featureFieldPermissionDataBeans.push(fieldObj);
                            });
                        }
                    });
                }
                if (Object.keys(designation.featureModifierMap).length > 0) {
                    console.log("designation.featureModifierMap:::"+JSON.stringify(designation.featureModifierMap));
                    angular.forEach(designation.featureModifierMap, function (modifier, featureId) {
                        modifier.designation = designationId;
                        modifier.feature = featureId;
                        roleFeatureModifiers.push(modifier);

                    });
                }
                designation.featureFieldPermissionDataBeans = featureFieldPermissionDataBeans;
                designation.roleFeatureModifiers = roleFeatureModifiers;
                console.log("roleFeatureModifiers:::;"+JSON.stringify(roleFeatureModifiers));
                $scope.designationConfigToSend = {
                    featureFieldPermissionDataBeans: featureFieldPermissionDataBeans,
                    roleFeatureModifiers: roleFeatureModifiers
                };
                $rootScope.maskLoading();
                DesignationConfigService.updateDesignationConfiguration(designation, function (response) {
                    $rootScope.unMaskLoading();
                    console.log('success-----------');
                    $scope.initializePage();
                }, function (error) {
                    $rootScope.unMaskLoading();
                    //console.log('failed-----------');
                });
            };

            $scope.getSearchedDesignationRecords = function (list) {
                $scope.searchRecords = [];
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {

                        if (list != null && angular.isDefined(list) && list.length > 0) {
                            angular.forEach(list, function (item) {
                                item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                                $scope.searchRecords.push(item);
                            });
                        }
                    }
                    $scope.config.displaySearchedDepartment = 'search';
                    $scope.selectedDepartment = undefined;
                    $scope.customLabel = '';
                    if ($scope.selectedDept.currentNode !== undefined) {
                        $scope.selectedDept.currentNode.selected = undefined;
                    }
                }

            };

            $scope.editDesignationFromSearchBox = function (data) {
                var designation = null;
                if (data !== undefined && data !== null) {
                    $scope.selectedDesignation = undefined;
                    angular.forEach($scope.designationList, function (departmentObj) {
                        if (departmentObj.designationDataBeans !== null && departmentObj.designationDataBeans !== undefined) {
                            angular.forEach(departmentObj.designationDataBeans, function (item) {
                                if (item.id === data) {
                                    designation = item;
                                }
                            })
                        }
                    });
                    if ($scope.selectedDesign.currentNode !== undefined) {
                        $scope.selectedDesign.currentNode.selected = undefined;
                    }
                    if (designation !== null) {
                        $scope.configureDesignation({currentNode: designation});
                    }
                }
            };

            $scope.initializePage();
            $rootScope.unMaskLoading();
        }]);
});

