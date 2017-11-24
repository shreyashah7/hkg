define(['angular', 'rapCalcyService'], function() {
    globalProvider.compileProvider.directive('rapCalcy', ["DynamicCenterMasterService", "RapCalcyService", '$timeout', function(DynamicCenterMasterService, RapCalcyService, $timeout) {
            return {
                restrict: 'E',
                replace: true,
                scope: {
                    outputData: '=',
                    inputData: '=',
                    rules: '=conditions',
                    calculateRateFnc: '&calculateRate'
                },
                templateUrl: 'scripts/directives/rapcalcy/rapcalcy.tmpl.html',
                link: function($scope) {
                    $scope.entity = "RAPCALCY.";
                    $scope.orderSeq = {
                        "shape$DRP$String": 1,
                        "clarity$DRP$String": 2,
                        "color$DRP$String": 3,
                        "carat_type$DRP$Long": 4
                    };
                    if ($scope.inputData !== undefined && $scope.inputData !== null && $scope.inputData.length > 0) {
                        var primaryFieldTmpl = [];
                        var secondaryFieldTmpl = [];
                        for (var index = $scope.inputData.length - 1; index >= 0; index--) {
                            var item = angular.copy($scope.inputData[index]);
                            if (item.model !== "empname$DRP$String") {
                                if ($scope.$root.primaryFields.indexOf(item.model) !== -1) {
                                    item["order"] = $scope.orderSeq[item.model];
                                    primaryFieldTmpl.push(item);
                                } else {
                                    secondaryFieldTmpl.push(item);
                                }
                            }
                            $scope.inputData.splice(index, 1);
                        }
                        $scope.inputData = [];
                        $scope.secondaryData = [];
                        if (primaryFieldTmpl.length > 0) {
                            $scope.inputData = angular.copy(primaryFieldTmpl);
                        }
                        if (secondaryFieldTmpl.length > 0) {
                            $scope.secondaryData = angular.copy(secondaryFieldTmpl);
                        }
                    }
                    $scope.flag = false;
                    $scope.valExist = false;
                    $scope.pointerFieldIds = [];
                    if ($scope.secondaryData !== undefined && $scope.secondaryData !== null && $scope.secondaryData.length > 0) {
                        for (var index = $scope.secondaryData.length - 1; index >= 0; index--) {
                            if ($scope.secondaryData[index].type !== null && $scope.secondaryData[index].type !== 'select' && $scope.secondaryData[index].type !== 'pointerselect') {
                                $scope.secondaryData.splice(index, 1);
                            } else if ($scope.secondaryData[index].type !== null && $scope.secondaryData[index].type === 'pointerselect') {
                                $scope.pointerFieldIds.push($scope.secondaryData[index].pointerFieldId);
                            }
                        }
                    }
                    if ($scope.inputData !== undefined && $scope.inputData !== null && $scope.inputData.length > 0) {
                        for (var index1 = $scope.inputData.length - 1; index1 >= 0; index1--) {
                            if ($scope.inputData[index1].type !== null && $scope.inputData[index1].type !== 'select') {
                                $scope.inputData.splice(index1, 1);
                            }
                        }
                    }
                    $scope.priceCalcyDtl = angular.copy($scope.inputData);
                    $scope.changeData = function(newValue) {
                        if (newValue !== undefined && newValue !== null) {
                            var finalVal = "";
                            if (newValue.caratFirst !== null && newValue.caratFirst !== undefined) {
                                finalVal += newValue.caratFirst;
                            } else {
                                finalVal += "0";
                            }
                            if (newValue.caratSecond !== null && newValue.caratSecond !== undefined) {
                                finalVal += newValue.caratSecond;
                            } else {
                                finalVal += "0";
                            }
                            finalVal += ".";
                            if (newValue.centFirst !== null && newValue.centFirst !== undefined) {
                                finalVal += newValue.centFirst;
                            } else {
                                finalVal += "0";
                            }
                            if (newValue.centSecond !== null && newValue.centSecond !== undefined) {
                                finalVal += newValue.centSecond;
                            } else {
                                finalVal += "0";
                            }
                            if (newValue.centThird !== null && newValue.centThird !== undefined) {
                                finalVal += newValue.centThird;
                            } else {
                                finalVal += "0";
                            }
                            $scope.outputData = $scope.outputData || {};
                            if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[0].value) {
                                $scope.outputData.size$DRP$Long = parseFloat(finalVal);
                            } else if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[1].value) {
                                $scope.outputData.gsize$DRP$long = parseFloat(finalVal);
                            }

                        }
                    };
                    $scope.changeCaratValues = function(newValue) {
                        if (newValue !== undefined && newValue !== null) {
                            var varFloat = parseFloat(newValue).toFixed(3);
                            $scope.sampleData = $scope.sampleData || {};
                            if (varFloat.toString().split(".")[0] < 10 && varFloat.toString().split(".")[0] >= 0) {
                                $scope.sampleData.caratFirst = 0;
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                            } else if (varFloat.toString().split(".")[0] >= 10 && varFloat.toString().split(".")[0] <= 99) {
                                $scope.sampleData.caratFirst = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[1]);
                            }
                            $scope.sampleData.centFirst = parseInt(('' + varFloat.toString().split(".")[1])[0]);
                            $scope.sampleData.centSecond = parseInt(('' + varFloat.toString().split(".")[1])[1]);
                            $scope.sampleData.centThird = parseInt(('' + varFloat.toString().split(".")[1])[2]);

                        } else {
                            $scope.sampleData = {};
                        }
                    };
                    $scope.$watch("outputData.size$DRP$Long", function(newValue) {
                        if (newValue !== undefined && newValue !== null) {
                            var varFloat = parseFloat(newValue).toFixed(3);
                            $scope.sampleData = $scope.sampleData || {};
                            if (varFloat.toString().split(".")[0] < 10 && varFloat.toString().split(".")[0] >= 0) {
                                $scope.sampleData.caratFirst = 0;
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                            } else if (varFloat.toString().split(".")[0] >= 10 && varFloat.toString().split(".")[0] <= 99) {
                                $scope.sampleData.caratFirst = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[1]);
                            }
                            $scope.sampleData.centFirst = parseInt(('' + varFloat.toString().split(".")[1])[0]);
                            $scope.sampleData.centSecond = parseInt(('' + varFloat.toString().split(".")[1])[1]);
                            $scope.sampleData.centThird = parseInt(('' + varFloat.toString().split(".")[1])[2]);

                        } else {
                            $scope.sampleData = {};
                        }
                    });
                    $scope.$watch("outputData.gsize$DRP$long", function(newValue) {
                        if (newValue !== undefined && newValue !== null) {
                            var varFloat = parseFloat(newValue).toFixed(3);
                            $scope.sampleData = $scope.sampleData || {};
                            if (varFloat.toString().split(".")[0] < 10 && varFloat.toString().split(".")[0] >= 0) {
                                $scope.sampleData.caratFirst = 0;
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                            } else if (varFloat.toString().split(".")[0] >= 10 && varFloat.toString().split(".")[0] <= 99) {
                                $scope.sampleData.caratFirst = parseInt(('' + varFloat.toString().split(".")[0])[0]);
                                $scope.sampleData.caratSecond = parseInt(('' + varFloat.toString().split(".")[0])[1]);
                            }
                            $scope.sampleData.centFirst = parseInt(('' + varFloat.toString().split(".")[1])[0]);
                            $scope.sampleData.centSecond = parseInt(('' + varFloat.toString().split(".")[1])[1]);
                            $scope.sampleData.centThird = parseInt(('' + varFloat.toString().split(".")[1])[2]);

                        } else {
                            $scope.sampleData = {};
                        }
                    });
                    $scope.manageCaratTypeVal = function(newValue) {
                        if (newValue === undefined || newValue === null) {
                            if ($scope.caratTypeValues !== undefined && $scope.caratTypeValues !== null && $scope.caratTypeValues.length >= 1) {
                                $scope.outputData.carat_type$DRP$Long = $scope.caratTypeValues[0].value;
                            }

                        } else {
                            if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[0].value) {
                                $scope.changeCaratValues($scope.outputData.size$DRP$Long);
                            } else if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[1].value) {
                                $scope.changeCaratValues($scope.outputData.gsize$DRP$long);
                            }
                        }
                        if ($scope.caratTypeValues !== undefined && $scope.caratTypeValues !== null && $scope.caratTypeValues.length >= 1) {
                            if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[0].value) {
                                $scope.secondaryData = angular.copy($scope.copySecondaryData);
                                if ($scope.secondaryData !== undefined && $scope.secondaryData !== null) {
                                    for (var i = $scope.secondaryData.length - 1; i >= 0; i--) {
                                        var item = $scope.secondaryData[i];
                                        if (item.type === 'pointerselect') {
                                            $scope.secondaryData.splice(i, 1);
                                        }
                                    }
                                }
                            } else if ($scope.outputData.carat_type$DRP$Long === $scope.caratTypeValues[1].value) {
                                $scope.secondaryData = angular.copy($scope.copySecondaryData);
                                if ($scope.secondaryData !== undefined && $scope.secondaryData !== null) {
                                    for (var i = $scope.secondaryData.length - 1; i >= 0; i--) {
                                        var item = $scope.secondaryData[i];
                                        if ($scope.pointerFieldIds.indexOf(item.fieldId) !== -1) {
                                            $scope.secondaryData.splice(i, 1);
                                        }
                                    }
                                }
                            }
                            $timeout(function() {
                                $scope.$root.unMaskLoading();
                                $scope.flag = true;
                            }, 2);
                        }
                    };
                    $scope.$watch("outputData", function(newValue, oldValue) {
                        //Check if primaryFields are selected and carat value is selected if yes than call calculate rate function otherwise skip
                        if (newValue !== undefined && newValue !== null) {
                            //If Carat type is expected than carat value should be selected, else if carat type is graph than graph carat value should be selected
                            if ($scope.caratTypeValues !== undefined && $scope.caratTypeValues !== null && $scope.caratTypeValues.length > 0) {
                                var flag = newValue.carat_type$DRP$Long === $scope.caratTypeValues[0].value && (newValue["size$DRP$Long"] !== undefined && newValue["size$DRP$Long"] !== null);
                                if (flag === false) {
                                    flag = newValue.carat_type$DRP$Long === $scope.caratTypeValues[1].value && (newValue["gsize$DRP$long"] !== undefined && newValue["gsize$DRP$long"] !== null);
                                }
                                //Check all primary fields stored in rootscope is selected or not
                                if (flag === true) {
                                    angular.forEach($scope.$root.primaryFields, function(item) {
                                        if (newValue[item] === undefined || newValue[item] === null) {
                                            flag = false;
                                        }
                                    });
                                }
                                //If all conditions satisfied than call calculate rate function
                                if (flag === true) {
                                    $scope.calculateRateFnc();
                                }
                            }
                        }
                        //Ends

                        if (oldValue !== undefined && oldValue !== null && oldValue.carat_type$DRP$Long !== newValue.carat_type$DRP$Long) {
                            $scope.$root.maskLoading();
                            $scope.flag = false;
                        }
                        $scope.manageCaratTypeVal(newValue.carat_type$DRP$Long);
                    }, true);

                    $scope.defineLabelForSingleSelect = function(fieldIds) {
                        $scope.dropdown = [];
                        $scope.fieldIds = fieldIds;
                        var success = function(response) {
                            console.log("all values:::"+JSON.stringify(response));
                            $scope.dropdownValuesForPrimary = {};
                            $scope.dropdownValuesForSecondary = {};
                            angular.forEach(response.data, function(value, key) {
                                if ($scope.primaryFieldIds.indexOf(key.toString()) >= 0) {
                                    $scope.dropdownValuesForPrimary[key] = value;
                                } else if ($scope.secondaryFieldIds.indexOf(key.toString()) >= 0) {
                                    $scope.dropdownValuesForSecondary[key] = value;
                                }
                                if ($scope.caratTypeItem !== undefined && $scope.caratTypeItem !== null && key.toString() === $scope.caratTypeItem.fieldId.toString()) {
                                    $scope.caratTypeValues = angular.copy(value);
                                }
                            });
                            if ($scope.caratTypeItem !== undefined && $scope.caratTypeItem !== null && $scope.caratTypeValues !== undefined && $scope.caratTypeValues !== null && $scope.caratTypeValues.length > 0) {
                                $scope.valExist = true;
                            } else {
                                var msg = "Please enter carat type values before proceed";
                                var type = $scope.$root.failure;
                                $scope.$root.addMessage(msg, type);
                            }
                            $scope.manageCaratTypeVal(null);
                        };
                        var failure = function() {
                        };
                        console.log("retrieveing........."+JSON.stringify($scope.fieldIds));
                        DynamicCenterMasterService.retrieveAllValuesForAllMasters($scope.fieldIds, success, failure);
                    };

                    if (($scope.secondaryData !== undefined && $scope.secondaryData !== null) || ($scope.inputData !== undefined && $scope.inputData !== null)) {
                        $scope.fieldIds = [];
                        $scope.primaryFieldIds = [];
                        $scope.secondaryFieldIds = [];
                        angular.forEach($scope.secondaryData, function(item) {
                            if ((item.type !== null && item.type === 'select') || (item.type !== null && item.type === 'pointerselect')) {
                                $scope.fieldIds.push(item.fieldId.toString());
                                $scope.secondaryFieldIds.push(item.fieldId.toString());
                            }
                        });
                        angular.forEach($scope.inputData, function(item) {
                            if ((item.type !== null && item.type === 'select') || (item.type !== null && item.type === 'pointerselect')) {
                                $scope.fieldIds.push(item.fieldId.toString());
                                $scope.primaryFieldIds.push(item.fieldId.toString());
                            }
                            if (item.modelWithoutSeperators === 'carat_type') {
                                $scope.caratTypeItem = angular.copy(item);
                            }
                        });
                        $scope.defineLabelForSingleSelect($scope.fieldIds);
                    }
                    $scope.copySecondaryData = angular.copy($scope.secondaryData);
                }
            };
        }]);
});