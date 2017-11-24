define(['hkg', 'dynamicForm', 'rapCalcyDirective', 'customFieldService', 'planService', 'rapCalcyService'], function(hkg) {
    hkg.register.controller('EstPredctController', ["$rootScope", "$scope", "DynamicFormService", "CustomFieldService", "PlanService", "RapCalcyService", function($rootScope, $scope, DynamicFormService, CustomFieldService, PlanService, RapCalcyService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "estimatePrediction";
            $scope.entity = "ESTIMATEPREDCT.";
            $rootScope.activateMenu();
            $scope.editIndex = 0;
            $scope.vals = {};
            $scope.index = 0;
            $scope.tab = {index: 0};
            $scope.rapcalcyflag = false;
            $scope.previousPacketNumber = "";
            var CONSTANTS = {
                ID: "id",
                PLAN_ID: "planId",
                TAG: "tag",
                INDEX: "index",
                CARAT: "size$DRP$long",
                GRAPH_CARAT: "gsize$DRP$long",
                EMP_ID: "empId",
                EMP_NAME: "empName",
                IS_ACTIVE: "isActive",
                IN_STOCK_OF: "packetInStockOf",
                COPIED_FROM: "copiedFrom",
                EMP_NAME_MODEL: "empname$DRP$String"
            };
            var planToCompare = [{index: 0, planId: 1, tag: 'A'}];
            $scope.planToIds = {};
            $scope.tagVal = {val: "A"};
            $scope.headerList = ["Carat", "GraphCarat", "Plan Id", "Tag"];
            $scope.headerListModel = {"Carat": "size$DRP$long", "GraphCarat": "gsize$DRP$long", "Plan Id": "planId", "Tag": "tag"};

            $scope.conditions = [{cond: {"carat_type$DRP$Long": "Expected", "size$DRP$long": 0.001, "fluroscene$DRP$String": 18, "clarity$DRP$String": 9, "color$DRP$String": 14},
                    def: {field1$DRP$Long: {"id": 28, "text": "3-F3"}}}];
            PlanService.retrieveModifiers(function(res) {
                $scope.modifiers = angular.copy(res.data);
            });
            CustomFieldService.retrieveDesignationBasedFields("predictionEstimate", function(response) {
                var templateDataTmp = DynamicFormService.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
                templateDataTmp.then(function(section) {
                    $scope.generaRoughCalcyTemplate = angular.copy(section['genralSection']);
                    var roughCalcyField = [];
                    $scope.response = angular.copy(response);
                    Object.keys(response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'RoughCalcy') {
                                roughCalcyField.push({RoughCalcy: itr});
                            }
                        });
                    }, response);
                    $scope.generaRoughCalcyTemplate = DynamicFormService.retrieveCustomData($scope.generaRoughCalcyTemplate, roughCalcyField);
                    angular.forEach($scope.generaRoughCalcyTemplate, function(tmpl) {
                        tmpl.isViewFromDesignation = false;
                    });

                    $scope.planTemplate = angular.copy(section['genralSection']);
                    var planField = [];
                    var result1 = Object.keys(response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'RoughCalcy') {
                                planField.push({RoughCalcy: itr});
                            }
                        });
                    }, response);
                    $scope.planTemplate = DynamicFormService.retrieveCustomData($scope.planTemplate, planField);

                    if (($scope.planTemplate !== undefined && $scope.planTemplate !== null)) {
                        var sortedTmpl = DynamicFormService.mergeAndSortTemplates($scope.planTemplate, []);
                        angular.forEach(sortedTmpl, function(item) {
                            if (item.label !== 'Carat Type' && $scope.headerList.indexOf(item.label) === -1 && item.model !== CONSTANTS.EMP_NAME_MODEL) {
                                $scope.headerList.push(item.label);
                                $scope.headerListModel[item.label] = item.model;
                            }
                            if (item.model === CONSTANTS.EMP_NAME_MODEL) {
                                $scope.headerList.push("Emp Name");
                                $scope.headerListModel["Emp Name"] = "empName";
                            }
                        });
                    }
                }, response);

//                var templateDataTmp1 = DynamicFormService.retrieveSectionWiseCustomFieldInfo("plan");
//                templateDataTmp1.then(function(section) {
//                    $scope.plFieldsTmpl = angular.copy(section['genralSection']);
//                    var plField = [];
//                    $scope.response1 = angular.copy(response);
//                    var result = Object.keys(response).map(function(key, value) {
//                        angular.forEach(this[key], function(itr) {
//                            if (key === 'Plan') {
//                                plField.push({Plan: itr});
//                            }
//                        });
//                    }, response);
//                    $scope.plFieldsTmpl = DynamicFormService.retrieveCustomData($scope.plFieldsTmpl, plField);
//
//                    if ($scope.plFieldsTmpl !== undefined && $scope.plFieldsTmpl !== null) {
//                        angular.forEach($scope.plFieldsTmpl, function(item) {
//                            $scope.tableData.push({model: item.model, text: item.label});
//                        });
//                    }
//
//                }, response);
            }, function() {
                $rootScope.unMaskLoading();
                var msg = "Failed to retrieve data";
                var type = $rootScope.error;
                $rootScope.addMessage(msg, type);
            });
            $scope.planData = [];
            $scope.planList = [];
            $scope.orgPlanList = [];
            $scope.addPlan = function() {
                if (!checkFor4CEmpty()) {
                    $scope.editIndex = $scope.planList.length;
                    $scope.tab = {index: ++$scope.index, isActive: true, empId: $rootScope.session.id, empName: $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName, inStockOf: $rootScope.session.id};
                    $scope.planList[$scope.editIndex] = angular.copy($scope.tab);
                    $scope.orgPlanList[$scope.editIndex] = angular.copy($scope.tab);
                    if ($scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === null || $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === undefined) {
                        for (var index = $scope.planList.length - 2; index >= 0; index--) {
                            var obj = $scope.planList[index];
                            if (obj.empId === $rootScope.session.id) {
                                if ($scope.tagList.indexOf(obj.tag) >= $scope.tagList.indexOf($scope.tagVal.val)) {
                                    $scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] = $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] = parseInt(obj.planId + 1);
                                    $scope.orgPlanList[$scope.editIndex][CONSTANTS.TAG] = $scope.planList[$scope.editIndex][CONSTANTS.TAG] = "A";
                                } else {
                                    if ($scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === null || $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === undefined) {
                                        $scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] = $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] = obj.planId;
                                        $scope.orgPlanList[$scope.editIndex][CONSTANTS.TAG] = $scope.planList[$scope.editIndex][CONSTANTS.TAG] = $scope.tagList[parseInt($scope.tagList.indexOf(obj.tag) + 1)];
                                    }
                                }
                                break;
                            }
                        }

                    }
                    $scope.tab = angular.copy($scope.orgPlanList[$scope.editIndex]);
                    preparePlanIdList();
                }
            };
            $scope.tagValue = 10;

            $scope.prepareTagList = function(tabval) {
                $scope.tagValue = parseInt(tabval);
                var index = 0;
                var count = 1;
                var ary = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
                var op = [""];
                $scope.tagList = [];
                while (count <= $scope.tagValue) {
                    var c = op[index];
                    for (var ch in ary) {
                        var code = c + ary[ch];
                        op.push(code);
                        count++;
                        $scope.tagList.push(code);
                        if (count === $scope.tagValue + 1) {
                            break;
                        }
                    }
                    index++;
                }
            };

            $scope.$watch("tab", function(newVal) {
                if (newVal !== undefined) {
                    var sectionData = [];
                    var caratValue = angular.copy(newVal[CONSTANTS.CARAT]);
                    var gCaratValue = angular.copy(newVal[CONSTANTS.GRAPH_CARAT]);
                    sectionData.push({categoryCustom: angular.copy(newVal)});
                    delete sectionData[0]["categoryCustom"].carat_type$DRP$Long;
                    if ($scope.orgPlanList[$scope.editIndex] === undefined || $scope.orgPlanList[$scope.editIndex] === null) {
                        $scope.orgPlanList[$scope.editIndex] = {};
                    }
                    var oldPlanId = newVal[CONSTANTS.PLAN_ID];
                    var oldTag = newVal[CONSTANTS.TAG];
                    $scope.orgPlanList[$scope.editIndex] = angular.copy(newVal);
                    delete sectionData[0]["categoryCustom"].undefined;
                    delete sectionData[0]["categoryCustom"].size$DRP$long;
                    delete sectionData[0]["categoryCustom"].gsize$DRP$long;

                    var success = function(result) {
                        if ($scope.planList[$scope.editIndex] === undefined || $scope.planList[$scope.editIndex] === null) {
                            $scope.planList[$scope.editIndex] = {};
                        }
                        result[0].categoryCustom[CONSTANTS.CARAT] = caratValue;
                        result[0].categoryCustom[CONSTANTS.GRAPH_CARAT] = gCaratValue;
                        if ($scope.planList.length === 1) {
                            $scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] = result[0].categoryCustom[CONSTANTS.PLAN_ID] = 1;
                            $scope.orgPlanList[$scope.editIndex][CONSTANTS.TAG] = result[0].categoryCustom[CONSTANTS.TAG] = "A";
                            $scope.orgPlanList[$scope.editIndex][CONSTANTS.EMP_ID] = $rootScope.session.id;
                            $scope.orgPlanList[$scope.editIndex][CONSTANTS.EMP_NAME] = $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName;
                            preparePlanIdList();
                        } else {
                            if ($scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === null || $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === undefined) {
                                if ($scope.tagList.indexOf($scope.planList[$scope.planList.length - 2].tag) >= $scope.tagList.indexOf($scope.tagVal.val)) {
                                } else {
                                    if ($scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === null || $scope.planList[$scope.editIndex][CONSTANTS.PLAN_ID] === undefined) {
                                    } else {
                                        $scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] = result[0].categoryCustom[CONSTANTS.PLAN_ID] = oldPlanId;
                                        $scope.orgPlanList[$scope.editIndex][CONSTANTS.TAG] = result[0].categoryCustom[CONSTANTS.TAG] = oldTag;
                                    }
                                }
                            } else {
                                $scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] = result[0].categoryCustom[CONSTANTS.PLAN_ID] = oldPlanId;
                                $scope.orgPlanList[$scope.editIndex][CONSTANTS.TAG] = result[0].categoryCustom[CONSTANTS.TAG] = oldTag;
                            }
                        }
                        $scope.planList[$scope.editIndex] = result[0].categoryCustom;
                    };
                    DynamicFormService.convertorForCustomField(sectionData, success);
                }
            }, true);
            $scope.editPlan = function(index) {
                $scope.editIndex = index;
                $scope.tab = $scope.orgPlanList[index];
            };
            var checkFor4CEmpty = function() {
                var flag = false;
                for (index = 0; index < $rootScope.primaryFields.length; index++) {
                    for (var index1 = 0; index1 < $scope.planList.length; index1++) {
                        if ($scope.planList[index1][CONSTANTS.IS_ACTIVE] === true && $rootScope.primaryFields[index] !== 'carat_type$DRP$Long' && ($scope.planList[index1][$rootScope.primaryFields[index]] === undefined || $scope.planList[index1][$rootScope.primaryFields[index]] === null)) {
                            alert("Please select mandatory fields first!");
                            flag = true;
                            break;
                        }
                    }
                    if (flag === true) {
                        break;
                    }
                }
                return flag;
            };
            var checkModification = function() {
                angular.forEach(planToCompare, function(item) {
                    delete item.carat_type$DRP$Long;
                });
                var planListLocal = angular.copy($scope.planList);
                angular.forEach(planListLocal, function(item) {
                    delete item.carat_type$DRP$Long;
                });
                if (angular.equals(planToCompare, planListLocal)) {
                    return false;
                } else {
                    return true;
                }
            };
            $scope.savePlans = function(packetNumber) {
                if (packetNumber !== undefined && packetNumber !== null) {
                    $scope.packetNumber = packetNumber;
                }
                $scope.removeEventCancel();
                $scope.submitted = true;
                if ($scope.estimatePredictionForm.$valid) {
                    if ($scope.planList !== undefined && $scope.planList !== null && $scope.planList.length > 0) {
                        if (!checkFor4CEmpty()) {
                            var planData = {};
                            $scope.planToId = {};
                            var payload = [];
                            angular.forEach($scope.orgPlanList, function(item) {
                                if (item.empId === $rootScope.session.id) {
                                    if (planData[item.planId] === undefined || planData[item.planId] === null) {
                                        planData[item.planId] = [];
                                    }
                                    planData[item.planId].push(item);
                                    if (item.id !== undefined && item.id !== null) {
                                        $scope.planToId[item.planId] = item.id;
                                    }
                                    delete planData[item.planId].empId;
                                    delete planData[item.planId].empName;
                                }
                            });
                            for (var key in planData) {
                                if (planData.hasOwnProperty(key)) {
                                    var obj = {"planId": key, "tags": planData[key], "packetNumber": $scope.packetNumber};
                                    if (planData[key][0].copiedFrom !== undefined && planData[key][0].copiedFrom !== null) {
                                        obj.copiedFrom = planData[key][0].copiedFrom;
                                    }
                                    if ($scope.planToId !== undefined && $scope.planToId !== null && $scope.planToId[key] !== null) {
                                        obj[CONSTANTS.ID] = $scope.planToId[key];
                                    }
                                    if (parseInt(obj[CONSTANTS.PLAN_ID]) === parseInt($scope.vals.finalPlan)) {
                                        obj["finalPlan"] = true;
                                    } else {
                                        obj["finalPlan"] = false;
                                    }
                                    payload.push(obj);
                                }
                            }
                            $rootScope.maskLoading();
                            PlanService.savePlans(payload, function(res) {
                                $rootScope.unMaskLoading();
                                if (res !== undefined && res !== null) {
                                    for (var index = 0; index < $scope.planList.length; index++) {
                                        var item = $scope.planList[index];
                                        item[CONSTANTS.ID] = res[item.planId];
                                        $scope.orgPlanList[index][CONSTANTS.ID] = res[item.planId];
                                        $scope.planToIds[item.planId] = res[item.planId];
                                    }
                                    planToCompare = angular.copy($scope.planList);
                                    retrieveFinalPlans();
                                }
                            }, function() {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to save plans";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    }
                }
            };
            $scope.copyPlan = function() {
                if ($scope.orgPlanList[$scope.editIndex][CONSTANTS.ID] !== null && $scope.orgPlanList[$scope.editIndex][CONSTANTS.ID] !== undefined) {
                    var planData = {};
                    var orgPlanData = [];
                    var planIds = [];
                    var planIdToCopy = "";
                    for (index = 0; index < $scope.planList.length; index++) {
                        var item = $scope.planList[index];
                        if ($scope.orgPlanList[$scope.editIndex][CONSTANTS.PLAN_ID] === item.planId && $scope.orgPlanList[$scope.editIndex][CONSTANTS.EMP_ID] === item.empId) {
                            if (planData[item.planId] === undefined || planData[item.planId] === null) {
                                planData[item.planId] = [];
                                planIdToCopy = item.planId;
                            }
                            planData[item.planId].push(item);
                            orgPlanData.push(angular.copy($scope.orgPlanList[index]));
                        }
                        if (planIds.indexOf(item.planId) === -1) {
                            planIds.push(parseInt(item.planId));
                        }
                    }
                    if (planIdToCopy !== "") {
                        var tempIndex = angular.copy($scope.editIndex);
                        $scope.editIndex = $scope.planList.length - 1;
                        var plans = planData[planIdToCopy];
                        for (index = 0; index < plans.length; index++) {
                            var item1 = angular.copy(plans[index]);
                            var item = angular.copy(item1);
                            ++$scope.editIndex;
                            item.copiedFrom = angular.copy($scope.planList[tempIndex][CONSTANTS.ID]);
                            delete item[CONSTANTS.ID];
                            item[CONSTANTS.PLAN_ID] = Math.max.apply(null, planIds) + 1;
                            delete item["$$hashKey"];
                            item[CONSTANTS.INDEX] = ++$scope.index;
                            item[CONSTANTS.EMP_ID] = $rootScope.session.id;
                            item[CONSTANTS.EMP_NAME] = $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName;
                            $scope.planList.push(item);
                            var orgItem = angular.copy(orgPlanData[index]);
                            orgItem[CONSTANTS.PLAN_ID] = item[CONSTANTS.PLAN_ID];
                            orgItem[CONSTANTS.EMP_ID] = $rootScope.session.id;
                            orgItem[CONSTANTS.EMP_NAME] = $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName;
                            delete orgItem[CONSTANTS.ID];
                            orgItem[CONSTANTS.INDEX] = $scope.index;
                            orgItem.copiedFrom = item.copiedFrom;
                            $scope.orgPlanList.push(orgItem);
                        }
                        $scope.tab = $scope.orgPlanList[$scope.editIndex];
                        delete $scope.tab.carat_type$DRP$Long;
                    }
                }
            };
            $scope.cancelPlan = function() {
                var index = $scope.editIndex;
                var flag = false;
                for (var index1 = index + 1; index1 < $scope.planList.length; index1++) {
                    if ($scope.planList[index1].isActive === undefined || $scope.planList[index1].isActive === null || $scope.planList[index1].isActive === true) {
                        $scope.editIndex = index1;
                        flag = true;
                        break;
                    }
                }
                if (flag === false) {
                    for (var index1 = 0; index1 < index; index1++) {
                        if ($scope.planList[index1].isActive === undefined || $scope.planList[index1].isActive === null || $scope.planList[index1].isActive === true) {
                            $scope.editIndex = index1;
                            flag = true;
                            break;
                        }
                    }
                }
                $scope.planList[index][CONSTANTS.IS_ACTIVE] = false;
                $scope.orgPlanList[index][CONSTANTS.IS_ACTIVE] = false;
                if (flag === false) {
                    $scope.editIndex = ++$scope.index;
                    $scope.tab = {index: $scope.editIndex, planId: 1, tag: "A", isActive: true, empId: $rootScope.session.id, empName: $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName, inStockOf: $rootScope.session.id};
                    preparePlanIdList();
                } else {
                    $scope.tab = angular.copy($scope.orgPlanList[$scope.editIndex]);
                }
                preparePlanIdList();
            };
            $scope.filterFn = function(plan) {
                if (plan.isActive === undefined || plan.isActive === null || plan.isActive === true) {
                    return true;
                }
                return false;
            };

            $scope.searchPlan = function() {

                $scope.removeEventCancel();
                if ($scope.packetNumber !== undefined && $scope.packetNumber !== null && $scope.packetNumber !== "") {
                    PlanService.retrievePlansByPacket($scope.packetNumber, function(result) {
                        var res = result["result"];
                        var error = result["error"];
                        if (res !== undefined && res !== null) {
                            $scope.rapcalcyflag = true;
                            if (res.length > 0) {
                                $scope.previousPacketNumber = res[0].packetNumber;
                                var orgPlanList = [];
                                if (res !== undefined && res !== null) {
                                    angular.forEach(res, function(item) {
                                        if (item.tags !== undefined && item.tags !== null) {
                                            angular.forEach(item.tags, function(item1) {
                                                var itemToPush = angular.copy(item1);
                                                itemToPush[CONSTANTS.ID] = item[CONSTANTS.ID];
                                                itemToPush[CONSTANTS.EMP_ID] = item[CONSTANTS.EMP_ID];
                                                itemToPush[CONSTANTS.EMP_NAME] = item[CONSTANTS.EMP_NAME];
                                                itemToPush["inStockOf"] = item[CONSTANTS.IN_STOCK_OF];
                                                orgPlanList.push(itemToPush);
                                            });
                                        }
                                        if (item.finalPlan === true) {
                                            $scope.vals.finalPlan = item.planId;
                                        }
                                    });
                                }
                                $scope.orgPlanList = angular.copy(orgPlanList);

                                if (orgPlanList !== undefined && orgPlanList !== null) {
                                    $scope.planList = [];
                                    var planTagToCarat = {};
                                    var sectionData = [];
                                    angular.forEach(orgPlanList, function(item2) {
                                        var key = "" + item2.planId + item2.tag + item2.empId;

                                        var carat = angular.copy(item2.size$DRP$long);
                                        var gcarat = angular.copy(item2.gsize$DRP$long);
                                        var val = {"carat": carat, "gcarat": gcarat};
                                        planTagToCarat[key] = val;

                                        delete item2.size$DRP$long;
                                        delete item2.gsize$DRP$long;

                                        sectionData.push({"categoryCustom": angular.copy(item2), "value": item2.index});
                                    });
                                    var success = function(result) {
                                        var newResult = [];
                                        angular.forEach(result, function(itm) {
                                            itm.categoryCustom[CONSTANTS.CARAT] = planTagToCarat["" + itm.categoryCustom[CONSTANTS.PLAN_ID] + itm.categoryCustom[CONSTANTS.TAG] + itm.categoryCustom[CONSTANTS.EMP_ID]]["carat"];
                                            itm.categoryCustom[CONSTANTS.GRAPH_CARAT] = planTagToCarat["" + itm.categoryCustom[CONSTANTS.PLAN_ID] + itm.categoryCustom[CONSTANTS.TAG] + itm.categoryCustom[CONSTANTS.EMP_ID]]["gcarat"];
                                            newResult.push(itm.categoryCustom);
                                        });
                                        $scope.planList = angular.copy(newResult);
                                        $scope.index = $scope.planList.length - 1;
                                        for (var index = 0; index < $scope.planList.length; index++) {
                                            if ($scope.planToIds[$scope.planList[index].planId] === undefined || $scope.planToIds[$scope.planList[index].planId] === null) {
                                                $scope.planToIds[$scope.planList[index].planId] = $scope.planList[index].id;
                                            }
                                        }
                                        planToCompare = angular.copy(newResult);
                                    };
                                    DynamicFormService.convertorForCustomField(sectionData, success);
                                }
                                $scope.editIndex = 0;
                                $scope.tab = $scope.orgPlanList[0];
                                preparePlanIdList();
                                retrieveFinalPlans();
                            } else {
                                clearList();
                            }
                        } else {
                            var type = $rootScope.error;
                            $rootScope.addMessage(error, type);
                            $scope.rapcalcyflag = false;
                            clearList();
                            planToCompare = [{index: 0, isActive: true, empId: $rootScope.session.id, empName: $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName, inStockOf: $rootScope.session.id, "planId": 1, "tag": "A"}];
                        }
                    }, function() {
                        console.log("Failure");
                    });
                }
            };
            $scope.beforeSearch = function() {
                if (checkModification() === false) {
                    $scope.planList = [{"index": 0, "planId": 1, "tag": "A"}];
                    $scope.orgPlanList = [];
                    $scope.editIndex = 0;
                    $scope.index = 0;
                    index = 0;
                    $scope.planToIds = {};
                    preparePlanIdList();
                    $scope.searchPlan();
                } else {
                    $("#saveModificationPopup").modal("show");
                }
            }
            $scope.clearPlanData = function() {
                var index = $scope.tab.index;
                var planId = $scope.tab.planId;
                var tag = $scope.tab.tag;
                var empId = $scope.tab.empId;
                var empName = $scope.tab.empName;
                var inStockOf = $scope.tab.inStockOf;
                var id = $scope.tab.id;
                $scope.tab = {"id": id, "index": index, "planId": planId, "tag": tag, "empId": empId, "empName": empName, "inStockOf": inStockOf};
            };
            $scope.calculateRate = function() {
                var fourCPresent = true;
                var payload = {fourCMap: {}};
                payload.discountDetailsMap = angular.copy($scope.tab);
                for (var i = 0; i < $rootScope.fourCMap.length; i++) {
                    var value = payload.discountDetailsMap[$rootScope.fourCMap[i]];
                    if (!value) {
                        fourCPresent = false;
                        break;
                    }
                    payload.fourCMap[$rootScope.fourCMap[i]] = value;
                }
                //If they are apresent then calculate basprice and discount
                if (fourCPresent) {
                    delete payload.discountDetailsMap["fourCMap"];
                    delete payload.discountDetailsMap[CONSTANTS.INDEX];
                    delete payload.discountDetailsMap[CONSTANTS.IS_ACTIVE];
                    delete payload.discountDetailsMap[CONSTANTS.TAG];
                    delete payload.discountDetailsMap[CONSTANTS.ID];
                    delete payload.discountDetailsMap[CONSTANTS.COPIED_FROM];
                    delete payload.discountDetailsMap[CONSTANTS.CARAT];
                    delete payload.discountDetailsMap[CONSTANTS.GRAPH_CARAT];
                    delete payload.discountDetailsMap[CONSTANTS.EMP_NAME];
                    delete payload.discountDetailsMap[CONSTANTS.EMP_ID];
                    RapCalcyService.calculateDiamondPrice(payload, function(response) {
                        $scope.calcFinal = response.data;
                        if ($scope.calcFinal.baseAmount === null) {
                            $scope.calcFinal.baseAmount = 0.0;
                        }
                        if ($scope.calcFinal.discount === null) {
                            $scope.calcFinal.discount = 0.0;
                        }
                        if ($scope.calcFinal.amount === null) {
                            $scope.calcFinal.amount = 0.0;
                        }
                        if ($scope.calcFinal.mixAmount === null) {
                            $scope.calcFinal.mixAmount = 0.0;
                        }
                    });
                }
            };

            $scope.removeEventCancel = function() {
                $("#saveModificationPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            var clearList = function() {
                $scope.planList = [];
                $scope.orgPlanList = [];
                $scope.editIndex = 0;
                $scope.index = 0;
                index = 0;
                $scope.planToIds = {};
                $scope.tab = {index: 0, isActive: true, empId: $rootScope.session.id, empName: $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName, inStockOf: $rootScope.session.id};
                preparePlanIdList();
            };

            var preparePlanIdList = function() {
                $scope.planIds = [];
                if ($scope.orgPlanList !== null && $scope.orgPlanList !== undefined && $scope.orgPlanList.length > 0) {
                    var tmpPlanIds = [];
                    angular.forEach($scope.orgPlanList, function(item) {
                        if (item.isActive !== false && tmpPlanIds.indexOf(item.planId) === -1 && item.empId === $rootScope.session.id) {
                            $scope.planIds.push({key: item.planId, value: item.planId});
                            tmpPlanIds.push(item.planId);
                        }
                    });
                }
            };
            var retrieveFinalPlans = function() {
                if ($scope.packetNumber !== undefined && $scope.packetNumber !== null) {
                    $scope.finalPlans = [];
                    PlanService.retrieveFinalPlans($scope.packetNumber, function(result) {
                        if (result !== null && result !== undefined && result.length > 0) {
                            $scope.finalPlans = angular.copy(result);
                        }
                    });
                }
            };

            $scope.finalizePlans = function() {
                if ($scope.vals.finalizedPlan !== undefined && $scope.vals.finalizedPlan !== null) {
                    PlanService.finalizePlan($scope.vals.finalizedPlan, function() {
                        var msg = "Plan finalized successfully";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                    }, function() {
                        var msg = "Error while finalizing plan";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
            $scope.prepareTagList($scope.tagValue);
        }
    ]);
});