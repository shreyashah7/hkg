define(['hkg', 'writeService', 'customFieldService', 'parcelService', 'lotmanagement/edit-lot.controller', 'ngload!uiGrid', 'addMasterValue', 'dynamicForm', 'accordionCollapse', 'finalizeService', 'printBarcodeValue'], function(hkg, writeService) {
    hkg.register.controller('WriteServiceController', ["$rootScope", "$scope", "WriteService", "$timeout", "$filter", "$location", "$window", "CustomFieldService", "DynamicFormService", "ParcelService", "FinalizeService", "$route", function($rootScope, $scope, WriteService, $timeout, $filter, $location, $window, CustomFieldService, DynamicFormService, ParcelService, FinalizeService, $route) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "writeService";
            $rootScope.activateMenu();
            var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("writeService");
            $scope.flag = {};
            $scope.flag.editMode = false;
            var deleteTagIds = [];
            $scope.planFlag = {};
            $scope.writeDbType = {};
            $scope.labelListForUiGrid = [];
            $scope.nodeDetailsInfo = [];
            $scope.submitted = false;
            $scope.modifyName = "";
            templateData.then(function(section) {
                $scope.generalSearchTemplate = section['genralSection'];
                if (section['genralSection'] === undefined || section['genralSection'] === null) {
                    $scope.generalSearchTemplate = [];
                }
                var featureDbfieldNameMap = {};
                var dbFieldName = [];
                if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                    var featureMap = {};
                    angular.forEach($scope.generalSearchTemplate, function(itr) {
//                        alert(itr.featureName);
                        if (itr.model) {
                            featureMap[itr.model] = itr.featureName;
                            $scope.labelListForUiGrid.push({name: itr.model, displayName: itr.label, minWidth: 200});

                        } else if (itr.fromModel) {
                            featureMap[itr.fromModel] = itr.featureName;
                            $scope.labelListForUiGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
                        }
                        else if (itr.toModel) {
                            featureMap[itr.toModel] = itr.featureName;
                            $scope.labelListForUiGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200});
                        }
                    });
                    var finalMap = {};
                    if (featureMap != null) {
                        angular.forEach(featureMap, function(value, key) {
//                            alert(JSON.stringify(value));
                            if (!finalMap[value]) {
                                finalMap[value] = [];
                            }
                            finalMap[value].push(key);
                        });
                    }
                }

                $scope.retrieveFromWorkAllocation = function() {
                    $scope.flag.showAddPage = false
                    if (finalMap != null) {
                        $scope.plantype = undefined;
                        WriteService.searchLotFromWorkAllocation(finalMap, function(res) {
                            $scope.nodeDetailsInfo = [];
                            if (res != null && res.custom2 && res.custom2.length > 0) {
                                $scope.modifierMap = {};
                                if (res.serviceInitDataBean !== null && res.serviceInitDataBean !== undefined) {
                                    $scope.modifierMap['nodeId'] = res.serviceInitDataBean.dynamicServiceInitDataBeans[0].nodeId;
                                    $scope.modiferList = res.serviceInitDataBean.dynamicServiceInitDataBeans[0].modifier.split("|");
//                                    console.log(JSON.stringify(res.serviceInitDataBean.dynamicServiceInitDataBeans));
                                    $scope.dynamicServiceDtaBeans = res.serviceInitDataBean.dynamicServiceInitDataBeans;
                                    $scope.modifierMap['plan'] = $scope.modiferList[0];
                                    $scope.modifyName = "Write ";
                                    if ($scope.modifierMap['plan'] === "PL") {
                                        $scope.modifyName += "Plan";
                                    } else if ($scope.modifierMap['plan'] === "ES") {
                                        $scope.modifyName += "Estimation";
                                    } else if ($scope.modifierMap['plan'] === "PA") {
                                        $scope.modifyName += "Parameter";
                                    }
                                    $scope.plantype = $scope.modiferList[0];
                                    if ($scope.modiferList[1] !== undefined && $scope.modiferList[1] !== null) {
                                        $scope.nonMandatoryModifierList = $scope.modiferList[1].split(",");
                                    }

                                    //Fetch node info
                                    var dynamicServiceInitBean = res.serviceInitDataBean;
                                    if (!!dynamicServiceInitBean) {
                                        if (dynamicServiceInitBean.nodeAndWorkAllocationIds !== null) {
                                            $scope.nodeIdAndWorkAllocationIdsMap = angular.copy(dynamicServiceInitBean.nodeAndWorkAllocationIds);
                                        }
                                        if (dynamicServiceInitBean.mandatoryFields !== null) {
                                            $scope.stockStaticFields = dynamicServiceInitBean.mandatoryFields;
                                        }
                                        if (dynamicServiceInitBean.diamondsInQueue !== null) {
                                            $scope.diamondsInQueue = dynamicServiceInitBean.diamondsInQueue;
                                        }
                                        if (!!dynamicServiceInitBean.dynamicServiceInitDataBeans) {
                                            var dynamicServiceInitDataBeans = dynamicServiceInitBean.dynamicServiceInitDataBeans;
                                            if (angular.isArray(dynamicServiceInitDataBeans) && dynamicServiceInitDataBeans.length > 0) {
                                                angular.forEach(dynamicServiceInitDataBeans, function(item) {
                                                    var data = {};
                                                    data.groupId = item.groupId;
                                                    data.groupName = item.groupName;
                                                    data.modifier = item.modifier;
                                                    data.nodeId = item.nodeId;
                                                    data.nodeName = item.nodeName;
                                                    $scope.nodeDetailsInfo.push(data);
                                                });
                                            }
                                            if ($scope.nodeDetailsInfo.length > 1) {
                                                $scope.flag.multipleIdInvolved = true;
                                            } else {
                                                $scope.flag.multipleIdInvolved = false;
                                            }
                                        }
                                        if ($scope.nodeDetailsInfo.length > 0) {
                                            $scope.currentActivityNode = $scope.nodeDetailsInfo[0].nodeId;
                                        }
                                    }
                                }
                                res = angular.copy(res.custom2)
                                $scope.searchedDataFromDb = angular.copy(res);
                                $scope.issuedStock = {};
                                $scope.issuedStock.enableFiltering = true;
                                $scope.issuedStock.multiSelect = false;
                                $scope.issuedStock.enableRowSelection = true;
                                $scope.selectedStock = [];
                                $scope.issuedStock.onRegisterApi = function(gridApi) {
                                    $scope.gridApi = gridApi;
                                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                                        if ($scope.selectedStock.length > 0) {
                                            $.each($scope.selectedStock, function(index, result) {
                                                if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                    //Remove from array
                                                    $scope.flag.repeatedRow = true;
                                                    $scope.selectedStock.splice(index, 1);
                                                    return false;
                                                } else {
                                                    $scope.flag.repeatedRow = false;
                                                }
                                            });
                                            if (!$scope.flag.repeatedRow) {
                                                $scope.selectedStock.push(row["entity"]);
                                            }
                                        } else {
                                            $scope.selectedStock.push(row["entity"]);
                                        }
                                        if ($scope.selectedStock.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                                        if ($scope.selectedStock.length > 0) {
                                            angular.forEach(rows, function(row) {
                                                $.each($scope.selectedStock, function(index, result) {
                                                    if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                        //Remove from array
                                                        $scope.flag.repeatedRow = true;
                                                        $scope.selectedStock = [];
                                                        return false;
                                                    } else {
                                                        $scope.flag.repeatedRow = false;
                                                    }
                                                });
                                                if (!$scope.flag.repeatedRow) {
                                                    $scope.selectedStock.push(row["entity"]);
                                                }
                                            });
                                        } else {
                                            angular.forEach(rows, function(row) {
                                                $scope.selectedStock.push(row["entity"]);
                                            });
                                        }
                                        if ($scope.selectedStock.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                };

                                $scope.issuedStockList = [];
                                for (var i = 0; i < res.length; i++) {
                                    if (res[i].custom1 != null) {
                                        res[i].categoryCustom = (res[i].custom1);
                                        res[i].categoryCustom["$$$value"] = (res[i].value);
                                    }
                                }
                                var success = function(result)
                                {
                                    $scope.searchedData = angular.copy(res);
                                    angular.forEach($scope.searchedData, function(itr) {
                                        angular.forEach($scope.labelListForUiGrid, function(list) {
                                            if (itr.custom1 != null && !itr.custom1.hasOwnProperty(list.name)) {
                                                itr.custom1[list.name] = "NA";
                                            }
                                            else if (itr.custom1 != null && itr.custom1.hasOwnProperty(list.name)) {
                                                if (!(!!(itr.custom1[list.name]))) {
                                                    itr.custom1[list.name] = "NA";
                                                }
                                            }
                                            if (itr.hasOwnProperty("status")) {
                                                itr.custom1["~@status"] = itr.status;
                                            }
                                        });
                                        if (itr.custom1 != null) {
                                            $scope.issuedStockList.push(itr.custom1);
                                        }

                                    });
//                                    $scope.issuedStock.data = $scope.issuedStockList;
//                                    $scope.issuedStock.columnDefs = $scope.labelListForUiGrid;
                                    $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                }
                                DynamicFormService.convertorForCustomField(res, success);
                                window.setTimeout(function() {
                                    $(window).resize();
                                    $(window).resize();
                                }, 200);
                                $scope.listFilled = true;
                                $scope.issueListFilled = true;
                                $scope.dataRetrieved = true;
                            } else {
                                $scope.dataRetrieved = true;
                                $scope.listFilled = true;
                            }
                        }
                        , function() {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        $scope.dataRetrieved = true;
                    }
                };

                $scope.retrieveFromWorkAllocation();
            }, function(reason) {

            }, function(update) {

            });

            $scope.updateStockAccordingToNode = function(nodeId) {
                console.log("updateStockAccordingToNode called");
                if (nodeId !== null && nodeId !== undefined) {
                    $scope.currentActivityNode = nodeId;
                    angular.forEach($scope.nodeDetailsInfo, function(item) {
                        if (item.nodeId === nodeId) {
                            var modifier = item.modifier;
                            if (modifier !== null) {
                                if (modifier.indexOf("|") > -1) {
                                    var modifiers = modifier.split("|");
                                    for (var i = 0; i < modifiers.length; i++) {
                                        if (modifiers[i] === 'Q') {
                                            $scope.isQuingRequired = true;
                                        }
                                    }
                                } else {
                                    if (modifier === 'AA' || modifier === 'MA') {
                                        $scope.typeOfAllocation = modifier;
                                    }
                                }
                            }
                        }
                    });
                    if (angular.isDefined($scope.nodeIdAndWorkAllocationIdsMap)) {
                        var result = [];
                        var workAllocationIds = $scope.nodeIdAndWorkAllocationIdsMap[nodeId];
//                        console.log(workAllocationIds.length > 0);
                        angular.forEach($scope.issuedStockList, function(item, index) {
                            if (workAllocationIds.length > 0 && workAllocationIds.indexOf(item["~@status"]) !== -1) {
                                item["~@index"] = index + 1;
                                result.push(item);
                            }
                        });
                        $scope.currentNodeStocks = angular.copy(result);
                        if (angular.isDefined($scope.gridApi)) {
                            $scope.gridApi.selection.clearSelectedRows();
                        }
                        $scope.issuedStock.data = result;
                        $scope.issuedStock.columnDefs = $scope.labelListForUiGrid;
                        $scope.listFilled = true;
                        if (!!$scope.isQuingRequired) {
                            $scope.issuedStock.enableSorting = false;
                            $scope.issuedStock.isRowSelectable = function(row) {
                                if (!!$scope.diamondsInQueue) {
                                    if (row.entity["~@index"] > $scope.diamondsInQueue) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else {
                                    if (row.entity["~@index"] > 0) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                            };
                        }
                    } else {
                        console.log("Node map not initialized");
                    }

                    if ($scope.dynamicServiceDtaBeans !== null && $scope.dynamicServiceDtaBeans !== undefined && $scope.dynamicServiceDtaBeans.length > 0) {
                        for (var index = 0; index < $scope.dynamicServiceDtaBeans.length; index++) {
                            if ($scope.dynamicServiceDtaBeans[index].nodeId === $scope.currentActivityNode) {
                                console.log("updateStockAccordingToNode called" + $scope.dynamicServiceDtaBeans[index].nodeId);
                                $scope.modifierMap['nodeId'] = $scope.dynamicServiceDtaBeans[index].nodeId;
                                $scope.modiferList = $scope.dynamicServiceDtaBeans[index].modifier.split("|");
                                console.log($scope.dynamicServiceDtaBeans[index].modifier.split("|"));
//                                console.log(JSON.stringify(res.serviceInitDataBean.dynamicServiceInitDataBeans));
                                var dynamicServiceInitBean = $scope.dynamicServiceDtaBeans[index];
                                $scope.modifierMap['plan'] = $scope.modiferList[0];
                                $scope.modifyName = "Write ";
                                if ($scope.modifierMap['plan'] === "PL") {
                                    $scope.modifyName += "Plan";
                                } else if ($scope.modifierMap['plan'] === "ES") {
                                    $scope.modifyName += "Estimation";
                                } else if ($scope.modifierMap['plan'] === "PA") {
                                    $scope.modifyName += "Parameter";
                                }
                                $scope.plantype = $scope.modiferList[0];
                                if ($scope.modiferList[1] !== undefined && $scope.modiferList[1] !== null) {
                                    $scope.nonMandatoryModifierList = $scope.modiferList[1].split(",");
                                } else {
                                    $scope.nonMandatoryModifierList = [];
                                }

                                //Fetch node info
//                                var dynamicServiceInitBean = $scope.dynamicServiceDtaBeans[index];
                                if (!!dynamicServiceInitBean) {
                                    if (dynamicServiceInitBean.nodeAndWorkAllocationIds !== null) {
                                        $scope.nodeIdAndWorkAllocationIdsMap = angular.copy(dynamicServiceInitBean.nodeAndWorkAllocationIds);
                                    }
                                    if (dynamicServiceInitBean.mandatoryFields !== null) {
                                        $scope.stockStaticFields = dynamicServiceInitBean.mandatoryFields;
                                    }
                                    if (dynamicServiceInitBean.diamondsInQueue !== null) {
                                        $scope.diamondsInQueue = dynamicServiceInitBean.diamondsInQueue;
                                    }
//                                    if (!!dynamicServiceInitBean.dynamicServiceInitDataBeans) {
//                                        var dynamicServiceInitDataBeans = dynamicServiceInitBean.dynamicServiceInitDataBeans;
//                                        if (angular.isArray(dynamicServiceInitDataBeans) && dynamicServiceInitDataBeans.length > 0) {
//                                            angular.forEach(dynamicServiceInitDataBeans, function(item) {
//                                                var data = {};
//                                                data.groupId = item.groupId;
//                                                data.groupName = item.groupName;
//                                                data.modifier = item.modifier;
//                                                data.nodeId = item.nodeId;
//                                                data.nodeName = item.nodeName;
//                                                $scope.nodeDetailsInfo.push(data);
//                                            });
//                                        }
//                                        if ($scope.nodeDetailsInfo.length > 1) {
//                                            $scope.flag.multipleIdInvolved = true;
//                                        } else {
//                                            $scope.flag.multipleIdInvolved = false;
//                                        }
//                                    }
                                }
                            }
                        }
                    }
                }
            };
            $scope.initWriteForm = function(writeForm) {
                $scope.writeForm = writeForm;
            };
            $scope.numberOfTagChanged = function(tagValue) {
                $scope.submitted = true;
                tagValue = parseInt(tagValue);
                if (tagValue !== null && ((tagValue > $scope.tagList.length && $scope.writeForm.$valid) || tagValue <= $scope.tagList.length || $scope.count === 0)) {
                    $scope.submitted = false;
                    var index = 0;

                    var count = 1;

                    var ary = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

                    var op = [""];

                    while (count <= tagValue) {
                        var c = op[index];
                        for (var ch in ary) {
                            var code = c + ary[ch];
                            op.push(code);
                            count++;
                            if (tagValue === 1 && $scope.count === 0) {
                                $scope.count++;
                                $scope.tagList.push({code: code, colorRadioList: ($scope.colorRadioList), carat: 0, cents: 0, clarity: angular.copy($scope.clarityRadioList), cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 1000, clarityGroup: 1001, cutGroup: 1002, fluorescenceGroup: 1003, selectColor: $scope.colorRadioList[0].color, selectClarity: $scope.clarityRadioList[0].clarity, selectCut: $scope.cutRadioList[0].cut, selectFluorescence: $scope.fluorescenceRadioList[0].fluorescence, price: null, currencyCode: $scope.currencyCode, breakage: false});
                            }
                            if (count === tagValue + 1) {
                                break;
                            }
                        }
                        index++;
                    }
                    op.splice(0, 1);
                    if (op.length > $scope.tagList.length) {
                        for (var i = $scope.tagList.length; i <= op.length; i++) {
                            if (op[i] !== undefined) {
                                $scope.tagList.push({code: op[i], colorRadioList: ($scope.colorRadioList), carat: 0, cents: 0, clarity: angular.copy($scope.clarityRadioList), cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: i, clarityGroup: i, cutGroup: i, fluorescenceGroup: i, selectColor: $scope.colorRadioList[0].color, selectClarity: $scope.clarityRadioList[0].clarity, selectCut: $scope.cutRadioList[0].cut, selectFluorescence: $scope.fluorescenceRadioList[0].fluorescence, price: null, currencyCode: $scope.currencyCode, breakage: false});
                                $scope.tabAndDynamicFormModel[op[i]] = null;
                            }
                        }
                    } else if (op.length < $scope.tagList.length) {
                        $scope.tagToRemove = [];
                        for (var j = 0; j <= $scope.tagList.length; j++) {
                            if ($scope.tagList[j] !== undefined && op.indexOf($scope.tagList[j].code) === -1) {
                                $scope.tagToRemove.push($scope.tagList[j].code);
                            }
                        }
                        if ($scope.tagToRemove.length > 0) {
                            for (var j = 0; j < $scope.tagList.length; j++) {
                                for (var asc = 0; asc < $scope.tagToRemove.length; asc++) {
                                    if ($scope.tagList[j] !== undefined && ($scope.tagToRemove[asc]) === $scope.tagList[j].code) {
                                        delete $scope.tabAndDynamicFormModel[$scope.tagList[j].code];
                                        $scope.tagList.splice(j, 1);
                                    }
                                }
                            }
                        }
                    }

//                    $scope.newList = [];
//                    for (var i = 1; i <= tagValue; i++) {
//                        $scope.newList.push(i);
//                    }
//                    console.log(JSON.stringify("selectedNoOfTag" + $scope.selectedNoOfTag));
//                    $scope.tagToAdd = [];
//                    if ($scope.newList.length > 0 && $scope.newList.length > $scope.selectedNoOfTag.length) {
//                        for (var i = 0; i < $scope.newList.length; i++) {
//                            if ($scope.selectedNoOfTag.indexOf($scope.newList[i]) === -1) {
//                                $scope.tagToAdd.push($scope.newList[i]);
//                            }
//                        }
//                    }
//                    console.log(JSON.stringify("tagToAdd" + $scope.tagToAdd));
//                    if ($scope.tagToAdd.length > 0) {
//                        var taglength = 65 + $scope.tagToAdd[$scope.tagToAdd.length - 1];
//                        var ascii = 65 + $scope.tagToAdd[0] - 1;
//                        for (ascii; ascii < taglength; ascii++) {
//                            $scope.tabAndDynamicFormModel[String.fromCharCode(ascii)] = null;

//                        }
//                    } else {
//                        $scope.tagToRemove = [];
//                        for (var remove = 0; remove < $scope.selectedNoOfTag.length; remove++) {
//                            if ($scope.newList.indexOf($scope.selectedNoOfTag[remove]) === -1) {
//                                $scope.tagToRemove.push($scope.selectedNoOfTag[remove]);
//                                if ($scope.flag.editMode) {
//                                    $scope.deleteIdsFromDb.push($scope.selectedNoOfTag[remove]);
//                                }
//                            }
//                        }
//                        if ($scope.tagToRemove != null && $scope.tagToRemove.length > 0) {
//                            var asciiListToRemove = [];
//                            var tagCodeList = [];
//                            for (var itr = 0; itr < $scope.tagToRemove.length; itr++) {
//                                asciiListToRemove.push($scope.tagToRemove[itr] + 64);
//                                tagCodeList.push(String.fromCharCode($scope.tagToRemove[itr] + 64));
//                            }
//                            angular.forEach($scope.enteredPlan, function(itr) {
//                                if (tagCodeList.indexOf(itr.tag) !== -1 && itr.planObjectId !== undefined && itr.planObjectId !== null) {
//                                    deleteTagIds.push(itr.planObjectId);
//                                }
//                            });
//                            for (var j = 0; j < $scope.tagList.length; j++) {
//                                for (var asc = 0; asc < asciiListToRemove.length; asc++) {
//                                    if ($scope.tagList[j] !== undefined && String.fromCharCode(asciiListToRemove[asc]) === $scope.tagList[j].code) {
//                                        delete $scope.tabAndDynamicFormModel[$scope.tagList[j].code];
//                                        $scope.tagList.splice(j, 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    $scope.selectedNoOfTag = angular.copy($scope.newList);
                } else {
                    $scope.tag = $scope.tagList.length;
                }
            };
            $scope.addPlan = function() {
                $scope.fieldNotConfigured = false;
                $scope.flag.editMode = false;
                $scope.numberOfMainTag = [];
                if ($scope.selectedStock !== undefined && $scope.selectedStock !== null && $scope.selectedStock.length > 0) {
                    $scope.writeAddFlag = false;
                    $scope.plansToBeSubmitted = $scope.selectedStock[0].plansToBeSubmitted;
//                    if (!!$scope.plansToBeSubmitted) {
//                        $scope.plansToBeSubmitted = undefined;
//                    }
                    console.log($scope.selectedStock[0]);
                    $scope.hasPriceCalculatorRight = false;
                    $rootScope.maskLoading();
                    deleteTagIds = [];
                    $scope.workAllocationId = undefined;
                    $scope.count = 0;
                    $scope.gridOptions = {};
                    $scope.selectedLotOrPacket = [];
                    $scope.gridOptions.onRegisterApi = function(gridApi) {
                        $scope.gridApi1 = gridApi;
                        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                            if ($scope.selectedLotOrPacket.length > 0) {
                                $.each($scope.selectedLotOrPacket, function(index, result) {
                                    if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                        //Remove from array
                                        $scope.flag.repeatedRow = true;
                                        $scope.selectedLotOrPacket.splice(index, 1);
                                        return false;
                                    } else {
                                        $scope.flag.repeatedRow = false;
                                    }
                                });
                                if (!$scope.flag.repeatedRow) {
                                    $scope.selectedLotOrPacket.push(row["entity"]);
                                }
                            } else {
                                $scope.selectedLotOrPacket.push(row["entity"]);
                            }
                            if ($scope.selectedLotOrPacket.length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        });
                        gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                            if ($scope.selectedLotOrPacket.length > 0) {
                                angular.forEach(rows, function(row) {
                                    $.each($scope.selectedLotOrPacket, function(index, result) {
                                        if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                            //Remove from array
                                            $scope.flag.repeatedRow = true;
                                            $scope.selectedLotOrPacket = [];
                                            return false;
                                        } else {
                                            $scope.flag.repeatedRow = false;
                                        }
                                    });
                                    if (!$scope.flag.repeatedRow) {
                                        $scope.selectedLotOrPacket.push(row["entity"]);
                                    }
                                });
                            } else {
                                angular.forEach(rows, function(row) {
                                    $scope.selectedLotOrPacket.push(row["entity"]);
                                });
                            }
                            if ($scope.selectedLotOrPacket.length > 0) {
                                $scope.flag.rowSelectedflag = true;
                            } else {
                                $scope.flag.rowSelectedflag = false;
                            }
                        });
                    };
                    $scope.labelForUIGrid = [];
                    $scope.lotIdForConstraint = undefined;
                    $scope.packetIdForConstraint = undefined;
                    for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                        if ($scope.searchedDataFromDb[i].value === $scope.selectedStock[0].$$$value) {
                            $scope.invoiceIdForConstraint = $scope.searchedDataFromDb[i].description;
                            $scope.parcelIdForConstraint = $scope.searchedDataFromDb[i].label;
                            $scope.lotIdForConstraint = angular.copy($scope.searchedDataFromDb[i].value);
                            $scope.modifierMap['objectId'] = angular.copy($scope.searchedDataFromDb[i].value);
                            $scope.modifierMap['isPacket'] = "false";
                            if ($scope.searchedDataFromDb[i].id != null) {
                                $scope.packetIdForConstraint = angular.copy($scope.searchedDataFromDb[i].id);
                                $scope.modifierMap['objectId'] = angular.copy($scope.searchedDataFromDb[i].value);
                                $scope.modifierMap['isPacket'] = "true";
                            }
                            $scope.workAllocationId = angular.copy($scope.searchedDataFromDb[i].status);
                            break;
                        }
                    }
                    FinalizeService.retrievePriceList(function(result) {
                        $scope.pricelistDtl = JSON.parse(angular.toJson(result));
                        for (var key in $scope.pricelistDtl) {
                            if ($scope.pricelistDtl.hasOwnProperty(key)) {
                                var temp = $scope.pricelistDtl[key];
                                $scope.pricelistDtl[key] = new Date(temp).toUTCString().replace(/\s*(GMT|UTC)$/, "");
                            }
                        }
//                        console.log("PriceListDtl::" + JSON.stringify($scope.pricelistDtl));
                    });
                    WriteService.retrieveCurrency(function(currency) {
                        $scope.currencyCodeList = [];
                        if (currency !== undefined && currency !== null) {
                            angular.forEach(currency, function(itr) {
                                $scope.currencyCodeList.push({currencyCode: itr.value, currency: itr.label});
                            });
                        }
                    }
                    , function() {
                        var msg = "Could not retrieve currency, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                    WriteService.retrieveValuesOfMaster(function(valueOfMaster) {
//                        $rootScope.unMaskLoading();
                        $scope.flag.masterValueNotExist = false;
                        $scope.colorRadioList = [];
                        $scope.clarityRadioList = [];
                        $scope.cutRadioList = [];
                        $scope.fluorescenceRadioList = [];
                        if (valueOfMaster != null && valueOfMaster.length > 0) {
                            var valueOfMasterFromDb = angular.copy(valueOfMaster);
                            angular.forEach(valueOfMasterFromDb, function(itr) {
                                if (itr.masterName === 'Color') {
                                    $scope.colorRadioList.push({color: angular.copy(itr.valueEntityId), name: angular.copy(itr.value)});
                                } else if (itr.masterName === 'Cut') {
                                    $scope.cutRadioList.push({cut: angular.copy(itr.valueEntityId), name: angular.copy(itr.value)});
                                } else if (itr.masterName === 'Clarity') {
                                    $scope.clarityRadioList.push({clarity: angular.copy(itr.valueEntityId), name: angular.copy(itr.value)});
                                } else if (itr.masterName === 'Fluroscene') {
                                    $scope.fluorescenceRadioList.push({fluorescence: angular.copy(itr.valueEntityId), name: angular.copy(itr.value)});
                                }
                            });
                            if ($scope.colorRadioList.length === 0 || $scope.cutRadioList.length === 0 || $scope.clarityRadioList.length === 0 || $scope.fluorescenceRadioList.length === 0) {
                                $scope.flag.masterEmpty = true;
                            } else {
                                $scope.flag.masterEmpty = false;
                                $scope.selectedRadio = angular.copy({color: $scope.colorRadioList[0].color, cut: $scope.cutRadioList[0].cut, clarity: $scope.clarityRadioList[0].clarity, fluorescence: $scope.fluorescenceRadioList[0].fluorescence});
                                $scope.numberOfTags = [];
                                for (var i = 1; i < 1000; i++) {
                                    $scope.numberOfTags.push(angular.copy({title: i}));
                                }
                                $scope.tabAndDynamicFormModel = {};
                                $scope.selectedNoOfTag = [];
                                $scope.checkDrop = true;
                                $scope.tag = 1;
                                $scope.currencyCode = 'USD($)';
                                $scope.tabAndDynamicFormModel['A'] = null;
                                $scope.previousTab = 'A';
                                $scope.selectedNoOfTag.push(1);
                                $scope.tabManager = {};
                                $scope.tagList = [];
//                            $scope.tagList.push({code: String.fromCharCode(65), colorRadioList: $scope.colorRadioList, carat: 0, cents: 0, clarity: $scope.clarityRadioList, cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 1000, clarityGroup: 2000, cutGroup: 3000, fluorescenceGroup: 4000, selectColor: $scope.colorRadioList[0].color, selectClarity: $scope.clarityRadioList[0].clarity, selectCut: $scope.cutRadioList[0].cut, selectFluorescence: $scope.fluorescenceRadioList[0].fluorescence, price: null, currencyCode: $scope.currencyCode});
                                $scope.numberOfTagChanged(1);
                                $scope.tabManager.tagList = $scope.tagList;
                            }
                            $scope.detailsFilled = true;
                        } else {
                            $scope.flag.masterValueNotExist = true;
                        }
//                        retrieve submitted plan if usr has access
                        $scope.gridOptionForSubmit = {};
                        $scope.submittedPlan = [];
                        $scope.planSubmit = false;
                        console.log("$scope.nonMandatoryModifierList" + JSON.stringify($scope.nonMandatoryModifierList));
//                        if (!!$scope.nonMandatoryModifierList && $scope.nonMandatoryModifierList.length > 0 && $scope.nonMandatoryModifierList.toString().indexOf("SHPC") !== -1) {
//                            $scope.hasPriceCalculatorRight = true;
//                        }
                        if (!!$scope.nonMandatoryModifierList && $scope.nonMandatoryModifierList.length > 0 && $scope.nonMandatoryModifierList.indexOf("SP") !== -1) {
                            $scope.modifierMap['workallotmentId'] = $scope.workAllocationId;
                            WriteService.retrieveAccessiblePlan($scope.modifierMap, function(res) {
//                                console.log(res);
                                angular.forEach(res, function(itr) {
                                    itr.carat = itr.caratValue;
                                    itr.cent = 0;
                                    if (itr.caratValue !== null && itr.caratValue.toString().indexOf(".") !== -1) {
                                        var carrayAndCent = itr.caratValue.toString().split(".");
                                        itr.carat = parseInt(carrayAndCent[0]);
                                        itr.cent = parseInt(carrayAndCent[1]);
                                    }
                                });
                                $scope.submittedPlan = angular.copy(res);
                                angular.forEach(res, function(itr) {
                                    itr.categoryCustom = {};
                                    itr.categoryCustom = angular.copy(itr.writeCustom);
//                                    if (itr.categoryCustom['cut'] != null) {
//                                        for (var i = 0; i < $scope.cutRadioList.length; i++) {
//                                            if ($scope.cutRadioList[i].cut === itr.categoryCustom['cut']) {
//                                                itr.categoryCustom['cut'] = angular.copy($scope.cutRadioList[i].name);
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    if (itr.categoryCustom['color'] != null) {
//                                        for (var i = 0; i < $scope.colorRadioList.length; i++) {
//                                            if ($scope.colorRadioList[i].color === itr.categoryCustom['color']) {
//                                                itr.categoryCustom['color'] = angular.copy($scope.colorRadioList[i].name);
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    if (itr.categoryCustom['clarity'] != null) {
//                                        for (var i = 0; i < $scope.clarityRadioList.length; i++) {
//                                            if ($scope.clarityRadioList[i].clarity === itr.categoryCustom['clarity']) {
//                                                itr.categoryCustom['clarity'] = angular.copy($scope.clarityRadioList[i].name);
//                                                break;
//                                            }
//                                        }
//                                    }
                                });
                                $scope.submiitedPlanColum = [];
                                $scope.submiitedPlanDataForUIGrid = [];
//                                $scope.submiitedPlanColum.push({name: "planID$AG$String", displayName: "Plan ID", cellTemplate: '<div><span class="col-md-10">{{row.entity.planID$AG$String}}</span><span ng-if="row.entity.##bestplanflag=true">{{entity + "*" | translate}}}}</span></div>', minWidth: 200});
                                $scope.submiitedPlanColum.push({name: "carate_range_of_plan", displayName: "Carat", minWidth: 200});
                                $scope.submiitedPlanColum.push({name: "tag", displayName: "Tag", minWidth: 200});
                                $scope.submiitedPlanColum.push({name: "cut$DRP$Long", displayName: "Cut", minWidth: 200});
                                $scope.submiitedPlanColum.push({name: "color$DRP$Long", displayName: "Color", minWidth: 200});
                                $scope.submiitedPlanColum.push({name: "clarity$DRP$Long", displayName: "Clarity", minWidth: 200});
                                if ($scope.hasPriceCalculatorRight) {
                                    $scope.submiitedPlanColum.push({name: "price", displayName: "Value", minWidth: 200});
                                } else {
                                    $scope.submiitedPlanColum.push({name: "value_of_plan$CRN$Double", displayName: "Value Of Plan", minWidth: 200});
                                }
                                $scope.submiitedPlanColum.push({name: "createdByName", displayName: "Plan By", minWidth: 200});
                                $scope.submiitedPlanColum.push({name: 'Action',
                                    cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,true)">Improve Plan</i></a></div>', enableFiltering: false, minWidth: 200});
                                var templateDataWrite = DynamicFormService.retrieveSectionWiseCustomFieldInfo("plan");
                                var generalWriteTemplateForTable = undefined;
                                templateDataWrite.then(function(section) {
                                    generalWriteTemplateForTable = section['genralSection'];
                                }, function(reason) {

                                }, function(update) {

                                });
                                var success = function(result) {
                                    $scope.submittedPlanData = angular.copy(result);
                                    angular.forEach($scope.submittedPlanData, function(itr) {
                                        angular.forEach($scope.submiitedPlanColum, function(list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                        });
                                        $scope.submiitedPlanDataForUIGrid.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptionForSubmit.data = $scope.submiitedPlanDataForUIGrid;
                                    $scope.gridOptionForSubmit.columnDefs = $scope.submiitedPlanColum;
                                    $scope.gridOptionForSubmit.enableFiltering = true;
                                    $scope.planSubmit = true;
                                    window.setTimeout(function() {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
//                                    $rootScope.unMaskLoading();
                                };
                                DynamicFormService.convertorForCustomField(res, success);
                            }

                            , function() {
                                var msg = "Could not retrieve, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        }
                        $scope.searchedDataFromDbForUiGrid = [];
                        WriteService.retrieveSavedPlan($scope.workAllocationId, function(res) {
//                            console.log(JSON.stringify(res));
                            angular.forEach(res, function(itr) {
                                itr.carat = itr.caratValue;
                                itr.cent = 0;
//                                console.log(JSON.stringify(itr.caratValue));
                                if (itr.caratValue !== null && itr.caratValue.toString().indexOf(".") !== -1) {
                                    var carrayAndCent = itr.caratValue.toString().split(".");
                                    itr.carat = parseInt(carrayAndCent[0]);
                                    itr.cent = parseInt(carrayAndCent[1]);
                                }
                            });
                            $scope.enteredPlan = angular.copy(res);
                            angular.forEach(res, function(itr) {
                                itr.categoryCustom = {};
                                itr.categoryCustom = angular.copy(itr.writeCustom);
//                                if (itr.categoryCustom['cut$DRP$Long'] != null) {
//                                    for (var i = 0; i < $scope.cutRadioList.length; i++) {
//                                        if ($scope.cutRadioList[i].cut === itr.categoryCustom['cut$DRP$Long']) {
//                                            itr.categoryCustom['cut$DRP$Long'] = angular.copy($scope.cutRadioList[i].name);
//                                            break;
//                                        }
//                                    }
//                                }
//                                if (itr.categoryCustom['color$DRP$Long'] != null) {
//                                    for (var i = 0; i < $scope.colorRadioList.length; i++) {
//                                        if ($scope.colorRadioList[i].color === itr.categoryCustom['color$DRP$Long']) {
//                                            itr.categoryCustom['color$DRP$Long'] = angular.copy($scope.colorRadioList[i].name);
//                                            break;
//                                        }
//                                    }
//                                }
//                                if (itr.categoryCustom['clarity$DRP$Long'] !== null) {
//                                    for (var i = 0; i < $scope.clarityRadioList.length; i++) {
//                                        if ($scope.clarityRadioList[i].clarity === itr.categoryCustom['clarity$DRP$Long']) {
//                                            itr.categoryCustom['clarity$DRP$Long'] = angular.copy($scope.clarityRadioList[i].name);
//                                            break;
//                                        }
//                                    }
//                                }
                            });
                            $scope.planListForUiGrid = [];
                            $scope.planListForUiGrid.push({name: "planID$AG$String", displayName: "Plan ID", minWidth: 200});
                            $scope.planListForUiGrid.push({name: "tag", displayName: "Tag", minWidth: 200});
                            $scope.planListForUiGrid.push({name: "cut$DRP$Long", displayName: "Cut", minWidth: 200});
                            $scope.planListForUiGrid.push({name: "color$DRP$Long", displayName: "Color", minWidth: 200});
                            $scope.planListForUiGrid.push({name: "clarity$DRP$Long", displayName: "Clarity", minWidth: 200});
                            if ($scope.hasPriceCalculatorRight) {
                                $scope.planListForUiGrid.push({name: "price", displayName: "Value", minWidth: 200});
                            } else {
                                $scope.planListForUiGrid.push({name: "value_of_plan$CRN$Double", displayName: "Value Of Plan", minWidth: 200});
                            }
                            $scope.planListForUiGrid.push({name: 'Action',
                                cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.editPlan(row.entity,editPlan)">Edit</i></a>&nbsp;<a ng-click="grid.appScope.openDeletePlanPopup(row.entity)">Delete</i></a></div>', enableFiltering: false, minWidth: 200});
                            var templateDataWrite = DynamicFormService.retrieveSectionWiseCustomFieldInfo("plan");
                            var generalWriteTemplateForTable = undefined;
                            templateDataWrite.then(function(section) {
                                generalWriteTemplateForTable = section['genralSection'];
                            }, function(reason) {

                            }, function(update) {

                            });
                            var success = function(result) {
                                $scope.searchedData = angular.copy(result);
                                angular.forEach($scope.searchedData, function(itr) {
                                    angular.forEach($scope.planListForUiGrid, function(list) {
                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                            itr.categoryCustom[list.name] = "NA";
                                        }
                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                        }
                                    });
                                    $scope.searchedDataFromDbForUiGrid.push(itr.categoryCustom);
                                });
                                $scope.gridOptions.data = $scope.searchedDataFromDbForUiGrid;
                                $scope.numberOfMainTag = [];
                                if (result !== null && result !== undefined && result.length > 0) {
                                    angular.forEach(result, function(itr) {
                                        if (itr.tag === 'A') {
                                            $scope.numberOfMainTag.push(itr.tag);
                                        }
                                    });
                                }
                                $scope.gridOptions.columnDefs = $scope.planListForUiGrid;
                                $scope.gridOptions.enableFiltering = true;
                                $scope.gridOptions.multiSelect = false;
                                $scope.gridOptions.enableRowSelection = true;
                                $scope.gridOptions.enableSelectAll = false;
                                window.setTimeout(function() {
                                    $(window).resize();
                                    $(window).resize();
                                }, 100);
//                                $rootScope.unMaskLoading();
                            }
                            DynamicFormService.convertorForCustomField(res, success);
//                            $rootScope.unMaskLoading();
                        }
                        , function() {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                    , function() {
                        var msg = "Could not retrieve, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                    $scope.flag.showAddPage = true;
                    var featureDbfieldNameMap = {};
                    var dbFieldName = [];
                    $scope.packetDataBean = {};
                    $scope.packetListToSave = [];
                    $scope.parcelDataBean = {invoiceDataBean: {invoiceCustom: '', invoiceDbType: ''}};
                    CustomFieldService.retrieveDesignationBasedFields("writeService", function(response) {
                        $scope.response = angular.copy(response);
//                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                        var templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                        $scope.invoiceDbType = {};
                        templateDataInvoice.then(function(section) {
                            $scope.generaInvoiceTemplate = section['genralSection'];
                            $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, response);
                            if ($scope.generaInvoiceTemplate != null && $scope.generaInvoiceTemplate.length > 0) {
                                angular.forEach($scope.generaInvoiceTemplate, function(itr) {
                                    if (itr.model) {
                                        dbFieldName.push(itr.model);
                                    } else if (itr.fromModel) {
                                        dbFieldName.push(itr.fromModel);
                                    }
                                    else if (itr.toModel) {
                                        dbFieldName.push(itr.toModel);
                                    }
                                });
                                if (dbFieldName != null && dbFieldName.length > 0) {
                                    featureDbfieldNameMap["invoice"] = dbFieldName;
                                }
                            }
                        }, function(reason) {
                        }, function(update) {
                        });
//                        $scope.parcelCustom = DynamicFormService.resetSection($scope.generaParcelTemplate);
                        var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                        $scope.parcelDbType = {};
                        templateData.then(function(section) {
                            $scope.generaParcelTemplate = section['genralSection'];
                            $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, response);
                            var dbFieldName = [];
                            if ($scope.generaParcelTemplate != null && $scope.generaParcelTemplate.length > 0) {
                                angular.forEach($scope.generaParcelTemplate, function(itr) {
                                    if (itr.model) {
                                        dbFieldName.push(itr.model);
                                    } else if (itr.fromModel) {
                                        dbFieldName.push(itr.fromModel);
                                    }
                                    else if (itr.toModel) {
                                        dbFieldName.push(itr.toModel);
                                    }
                                });
                                if (dbFieldName != null && dbFieldName.length > 0) {
                                    featureDbfieldNameMap["parcel"] = dbFieldName;
                                }
                            }
                        }, function(reason) {
                        }, function(update) {
                        });
//                        ----
//                        $scope.lotCustom = DynamicFormService.resetSection($scope.generalLotTemplate);
                        var templateDataForLot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                        $scope.lotDbType = {};
                        templateDataForLot.then(function(section) {
                            $scope.generalLotTemplate = section['genralSection'];
                            $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, response);
                            var dbFieldName = [];
                            if ($scope.generalLotTemplate != null && $scope.generalLotTemplate.length > 0) {
                                angular.forEach($scope.generalLotTemplate, function(itr) {
                                    if (itr.model) {
                                        dbFieldName.push(itr.model);
                                    } else if (itr.fromModel) {
                                        dbFieldName.push(itr.fromModel);
                                    }
                                    else if (itr.toModel) {
                                        dbFieldName.push(itr.toModel);
                                    }
                                });
                                if (dbFieldName != null && dbFieldName.length > 0) {
                                    featureDbfieldNameMap["lot"] = dbFieldName;
                                }
                            }
                        }, function(reason) {
                        }, function(update) {
                        });
//                        ------
//                        $scope.packetCustom = DynamicFormService.resetSection($scope.generalPacketTemplate);
                        var templateDataOfPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                        $scope.packetDbType = {};
                        templateDataOfPacket.then(function(section) {
                            $scope.generalPacketTemplate = section['genralSection'];
                            $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, response);
                            var dbFieldName = [];
                            if ($scope.generalPacketTemplate != null && $scope.generalPacketTemplate.length > 0) {
                                if ($scope.nonMandatoryModifierList !== undefined && $scope.nonMandatoryModifierList !== null && $scope.nonMandatoryModifierList.indexOf("SHPC")) {
                                    for (var i = 0; i < $scope.generalPacketTemplate.length; i++) {
//                                        if ($scope.generalPacketTemplate[i])
                                    }
                                }
                                angular.forEach($scope.generalPacketTemplate, function(itr) {
                                    if (itr.model) {
                                        dbFieldName.push(itr.model);
                                    } else if (itr.fromModel) {
                                        dbFieldName.push(itr.fromModel);
                                    }
                                    else if (itr.toModel) {
                                        dbFieldName.push(itr.toModel);
                                    }
                                });
                                if (dbFieldName != null && dbFieldName.length > 0) {
                                    featureDbfieldNameMap["packet"] = dbFieldName;
                                }
                            }
                            var dbFieldName = [];
                            if ($scope.packetIdForConstraint != null) {
                                dbFieldName.push($scope.packetIdForConstraint);
                                featureDbfieldNameMap["packetId"] = dbFieldName;
                            } else {
                                dbFieldName.push($scope.lotIdForConstraint);
                                featureDbfieldNameMap["lotId"] = dbFieldName;
                            }
                            WriteService.retrieveStockById(featureDbfieldNameMap, function(res) {
                                if (res != null) {
                                    $scope.invoiceCustom = res.custom1;
                                    $scope.parcelCustom = res.custom3;
                                    $scope.lotCustom = res.custom4;
                                    $scope.packetCustom = res.custom5;
                                    if (res.custom1 === null || res.custom1 === undefined) {
                                        $scope.invoiceCustom = {};
                                    }
                                    if (res.custom3 == null || res.custom3 === undefined) {
                                        $scope.parcelCustom = {};
                                    }
                                    if (res.custom4 == null || res.custom4 === undefined) {
                                        $scope.lotCustom = {};
                                    }
                                    if (res.custom5 == null || res.custom5 === undefined) {
                                        $scope.packetCustom = {};
                                    }
                                }
                            }
                            , function() {
                                var msg = "Could not retrieve, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });

                        }, function(reason) {

                        }, function(update) {
                        });
                        $scope.categoryCustom = DynamicFormService.resetSection($scope.generalWriteTemplate);
                        var templateDataWrite = DynamicFormService.retrieveSectionWiseCustomFieldInfo("plan");
                        if (!($scope.writeDbType != null)) {
                            $scope.writeDbType = {};
                        }
                        templateDataWrite.then(function(section) {
                            $scope.generalWriteTemplate = section['genralSection'];
                            var planField = [];
                            var result = Object.keys(response).map(function(key, value) {
                                angular.forEach(this[key], function(itr) {
                                    if (key !== 'Plan#P#') {
                                        planField.push({Plan: itr});
                                    }
                                });
                            }, response);
                            $scope.generalWriteTemplate = DynamicFormService.retrieveCustomData($scope.generalWriteTemplate, planField);
                            var writeDbField = [];
                            angular.forEach($scope.generalWriteTemplate, function(itr) {
                                writeDbField.push(itr.model);
                            });
                            console.log("condition");
                            console.log(!!$scope.nonMandatoryModifierList && $scope.nonMandatoryModifierList.length > 0 && $scope.nonMandatoryModifierList.toString().indexOf("SHPC") !== -1);
                            if (!!$scope.nonMandatoryModifierList && $scope.nonMandatoryModifierList.length > 0 && $scope.nonMandatoryModifierList.toString().indexOf("SHPC") !== -1) {
                                $scope.hasPriceCalculatorRight = true;
                                console.log("$scope.hasPriceCalculatorRight" + $scope.hasPriceCalculatorRight);
                                if ($scope.hasPriceCalculatorRight && $scope.generalWriteTemplate !== null && $scope.generalWriteTemplate !== undefined && $scope.generalWriteTemplate.length > 0) {
                                    for (var i = $scope.generalWriteTemplate.length - 1; i >= 0; i--) {
                                        if ($scope.generalWriteTemplate[i].model === 'color$DRP$Long') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'cut$DRP$Long') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'clarity$DRP$Long') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'fluroscene$DRP$Long') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'carat$NM$Double') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'carate_range_of_plan$DRP$Long') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'breakge$CB$Boolean') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                        else if ($scope.generalWriteTemplate[i].model === 'value_of_plan$CRN$Double') {
                                            $scope.generalWriteTemplate.splice(i, 1);
                                        }
                                    }
                                    $scope.writeAddFlag = true;
                                }
                            }

                            else if (!$scope.hasPriceCalculatorRight && $scope.generalWriteTemplate.length > 0) {
                                $scope.mandatoryFields = ['color$DRP$Long', 'cut$DRP$Long', 'clarity$DRP$Long', 'carat_of_plan$NM$Double', 'fluroscene$DRP$Long', 'value_of_plan$CRN$Double'];
                                if (writeDbField.length > 0 && $scope.mandatoryFields != null && $scope.mandatoryFields.length > 0) {
                                    for (var field = 0; field < $scope.mandatoryFields.length; field++) {
                                        if (writeDbField.indexOf($scope.mandatoryFields[field]) === -1) {
                                            $scope.fieldNotConfigured = true;
                                            break;
                                        }
                                    }
                                } else {
                                    $scope.fieldNotConfigured = true;
                                }
                                $scope.writeAddFlag = true;
                            }
                            else if ($scope.hasPriceCalculatorRight === false && $scope.generalWriteTemplate.length === 0) {
                                $scope.generalWriteTemplate = [];
                                $scope.writeAddFlag = true;
                                $scope.fieldNotConfigured = true;
                            }

                            angular.forEach($scope.generalWriteTemplate, function(itr) {
                                if (itr.model) {
                                    $scope.labelForUIGrid.push({name: itr.model, displayName: itr.label, minWidth: 200});
                                } else if (itr.fromModel) {
                                    $scope.labelForUIGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200});
                                }
                                else if (itr.toModel) {
                                    $scope.labelForUIGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200});
                                }
                            });
                            $scope.flag.customFieldGenerated = true;
//                            $scope.writeAddFlag = true;
                            $scope.planSelect = true;
                            $rootScope.unMaskLoading();
                        }, function(reason) {

                        }, function(update) {

                        });

                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve data";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };



            $scope.caratChange = function(caratValue, form, index, tab) {
                if (!(caratValue != null)) {
                    if ($scope.tabManager != null) {
                        if ($scope.tabManager.tagList != null) {
                            for (var i = 0; i < $scope.tabManager.tagList.length; i++) {
                                if ($scope.tabManager.tagList[i].code = tab) {
                                    $scope.tabManager.tagList[i].carat = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            $scope.calculatePrice = function(tabCode) {
                $scope.finalCarat = undefined;
                var map = {};
                if (tabCode != null && tabCode.length > 0) {
                    if ($scope.tabManager.tagList != null && $scope.tabManager.tagList.length > 0) {
                        for (var i = 0; i < $scope.tagList.length; i++) {
                            if ($scope.tabManager.tagList[i].code === tabCode) {
                                var carat = angular.copy($scope.tabManager.tagList[i].carat);
                                var cent = angular.copy($scope.tabManager.tagList[i].cents);
                                $scope.finalCarat = parseFloat(carat.toString().concat(".", cent.toString()));
                                map['color'] = angular.copy($scope.tabManager.tagList[i].selectColor);
                                map['cut'] = angular.copy($scope.tabManager.tagList[i].selectCut);
                                map['clarity'] = angular.copy($scope.tabManager.tagList[i].selectClarity);
                                map['fluroscene'] = angular.copy($scope.tabManager.tagList[i].selectFluorescence);
                                map['carat'] = $scope.finalCarat;
                                WriteService.checkCaratRange(map, function(res) {
                                    if (res != null && res.price != null) {
                                        $scope.tabManager.tagList[i].price = res.price;
                                    } else {
                                        res.price = 0;
                                        $scope.tabManager.tagList[i].price = res.price;
                                    }
                                }
                                , function() {
                                    var msg = "Could not retrieve, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    $rootScope.unMaskLoading();
                                });
                                break;
                            }
                        }
                    }
                }
            };

            $scope.centChange = function(centValue, form, index, tab) {
                if (!(centValue != null)) {
                    if ($scope.tabManager != null) {
                        if ($scope.tabManager.tagList != null) {
                            for (var i = 0; i < $scope.tabManager.tagList.length; i++) {
                                if ($scope.tabManager.tagList[i].code == tab) {
                                    $scope.tabManager.tagList[i].cents = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            $scope.reset = function(sectionTobeReset) {
                if (sectionTobeReset === "categoryCustom") {
                    $scope.categoryCustom = {};
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("plan");
                    templateData.then(function(section) {
                        var writeField = [];
                        var result = Object.keys($scope.response).map(function(key, value) {
                            angular.forEach(this[key], function(itr) {
                                if (key !== 'Plan#P#') {
                                    writeField.push({Plan: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalWriteTemplate = null;
                        $scope.generalWriteTemplate = section['genralSection'];
                        $scope.generalWriteTemplate = DynamicFormService.retrieveCustomData($scope.generalWriteTemplate, writeField);
                        if ($scope.hasPriceCalculatorRight && $scope.generalWriteTemplate.length > 0) {
                            for (var i = $scope.generalWriteTemplate.length - 1; i >= 0; i--) {
                                if ($scope.generalWriteTemplate[i].model === 'color$DRP$Long') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'cut$DRP$Long') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'clarity$DRP$Long') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'fluroscene$DRP$Long') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'carat$NM$Double') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'carate_range_of_plan$DRP$Long') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'breakge$CB$Boolean') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                                else if ($scope.generalWriteTemplate[i].model === 'value_of_plan$NM$Integer') {
                                    $scope.generalWriteTemplate.splice(i, 1);
                                }
                            }
                            console.log($scope.generalWriteTemplate.length);
                            $scope.writeAddFlag = true;
                        }
                        $scope.writeAddFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function(reason) {
                    }, function(update) {
                    });
                }
            };
            $scope.tabChanged = function(tab) {
                console.log($scope.tabAndDynamicFormModel);
                console.log("previousTab" + $scope.previousTab);
                console.log("tab" + tab);
                for (prop in $scope.tabAndDynamicFormModel) {
                    if (prop === $scope.previousTab) {
                        console.log("tab found");
                        $scope.tabAndDynamicFormModel[prop] = angular.copy($scope.categoryCustom);
                        $scope.writeAddFlag = false;
                        $scope.reset("categoryCustom");
                        $scope.previousTab = angular.copy(tab)
                        break;
                    }
                }
                if ($scope.tabAndDynamicFormModel[tab]) {
                    $scope.categoryCustom = angular.copy($scope.tabAndDynamicFormModel[tab]);
                }
            };

            $scope.savePlan = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    $rootScope.maskLoading();
                    $scope.submitted = false;
                    for (prop in $scope.tabAndDynamicFormModel) {
                        if (prop === $scope.previousTab) {
                            $scope.tabAndDynamicFormModel[prop] = angular.copy($scope.categoryCustom);
//                            $scope.writeAddFlag = false;
//                            $scope.reset("categoryCustom");
//                        $scope.previousTab = angular.copy($scope.previousTab)
                            break;
                        }
                    }
                    angular.forEach($scope.tabAndDynamicFormModel, function(value, key) {
                        for (var i = 0; i < $scope.tagList.length; i++) {
                            if ($scope.tagList[i].code === key) {
                                $scope.tagList[i].dynamicForm = value;
                            }
                        }
                    });
                    $scope.writeServiceDataBean = {};
//                here plan type has defined.
                    $scope.writeServiceDataBean.planType = $scope.plantype;
                    if (!!$scope.improveFrom) {
                        $scope.writeServiceDataBean.improveFromId = $scope.improveFrom;
                    } else {
                        $scope.writeServiceDataBean.improveFromId = null;
                    }
                    $scope.writeServiceDataBean.writeDbType = $scope.writeDbType;
                    if ($scope.packetIdForConstraint !== undefined) {
                        $scope.writeServiceDataBean.id = $scope.packetIdForConstraint;
                        $scope.writeServiceDataBean.hasPacket = true;
                    } else {
                        $scope.writeServiceDataBean.id = $scope.lotIdForConstraint;
                        $scope.writeServiceDataBean.hasPacket = false;
                    }
                    var writeServiceDataBeans = [];
                    if ($scope.hasPriceCalculatorRight) {
                        angular.forEach($scope.tagList, function(itr) {
                            writeServiceDataBeans.push({tag: itr.code, colorId: itr.selectColor, clarityId: itr.selectClarity, cutId: itr.selectCut, flurosceneId: itr.selectFluorescence, price: itr.price, writeCustom: itr.dynamicForm, currencyCode: itr.currencyCode, caratValue: parseFloat(itr.carat.toString().concat(".", itr.cents.toString())), breakage: itr.breakage});
                        });
                    } else if ($scope.hasPriceCalculatorRight === false) {
                        angular.forEach($scope.tagList, function(itr) {
                            writeServiceDataBeans.push({tag: itr.code, writeCustom: itr.dynamicForm});
                        });
                    }
                    $scope.writeServiceDataBean.writeServiceDataBeans = writeServiceDataBeans;
//                    console.log(deleteTagIds);
                    console.log(JSON.stringify(writeServiceDataBeans) + "length" + writeServiceDataBeans.length);
                    WriteService.savePlan($scope.writeServiceDataBean, function(res) {
                        $scope.checkDrop = false;
                        $scope.addPlan();
                    }
                    , function() {
                        var msg = "Could not save, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };

            var setValuesInTab = function(flag) {
                if (flag) {

                }
            };

            $scope.editPlan = function(object, flag) {
                var searchInPlan = undefined;
                $scope.improveFrom = undefined;
                if (object !== undefined && object !== null) {
                    $rootScope.maskLoading();
                    $scope.checkDrop = false;
                    $scope.flag.editMode = true;
                    $scope.deleteIdsFromDb = [];
                    var found = false;
                    if ($scope.enteredPlan !== undefined && $scope.enteredPlan !== null && $scope.enteredPlan.length > 0) {
                        for (var i = 0; i < $scope.enteredPlan.length; i++) {
                            if ($scope.enteredPlan[i].sequentialPlanId === object.planID$AG$String) {
                                if (flag) {
                                    $scope.improveFrom = $scope.enteredPlan[i].planObjectId;
                                }
                                found = true;
                                searchInPlan = angular.copy($scope.enteredPlan);
                                $scope.checkDrop = true;
                            }
                        }
                    }
                    if (!found && $scope.submittedPlan !== undefined && $scope.submittedPlan !== null && $scope.submittedPlan.length > 0) {
                        for (var i = 0; i < $scope.submittedPlan.length; i++) {
                            if ($scope.submittedPlan[i].sequentialPlanId === object.planID$AG$String) {
                                if (flag) {
                                    $scope.improveFrom = $scope.submittedPlan[i].planObjectId;
                                }
                                found = true;
                                searchInPlan = angular.copy($scope.submittedPlan);
                                $scope.checkDrop = true;
                            }
                        }
                    }
                }
//                console.log(JSON.stringify(searchInPlan));
                $scope.currentObject = undefined;
                if (object !== null && object !== undefined && searchInPlan !== null && searchInPlan !== undefined && searchInPlan.length > 0) {
                    var mainTag = false;
                    for (var i = 0; i < searchInPlan.length; i++) {
                        if (searchInPlan[i].sequentialPlanId === object.planID$AG$String) {
                            $scope.currentObject = angular.copy(searchInPlan[i]);
                            object = $scope.currentObject;
                            if ($scope.currentObject.referencePlan === null) {
                                mainTag = true;
                                $scope.detailsFilled = false;
//                                $scope.tabAndDynamicFormModel = {};
                                $scope.selectedNoOfTag = [];
                                $scope.tag = 1;
                                $scope.checkDrop = true;
                                $scope.currencyCode = object.currencyCode;
                                $scope.selectedRadio = angular.copy({color: object.colorId, cut: object.cutId, clarity: object.clarityId, fluorescence: object.flurosceneId});
                                $scope.categoryCustom = object.writeCustom;
                                $scope.previousTab = 'A';
                                $scope.selectedNoOfTag.push(1);
                                $scope.tabManager = {};
                                $scope.tagList = [];
                                var centTemp = angular.copy(object.cent);
                                var caratTemp = angular.copy(object.carat);
                                $scope.tagList.push({code: String.fromCharCode(65), colorRadioList: $scope.colorRadioList, carat: caratTemp, cents: centTemp, clarity: $scope.clarityRadioList, cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 1, clarityGroup: 2, cutGroup: 3, fluorescenceGroup: 4, selectColor: object.colorId, selectClarity: object.clarityId, selectCut: object.cutId, selectFluorescence: object.flurosceneId, price: object.price, currencyCode: $scope.currencyCode, breakage: object.breakage});
                                $scope.tabManager.tagList = $scope.tagList;
                                $scope.detailsFilled = true;
                            }
                            break;
                        }
                    }
                    if ($scope.currentObject !== undefined) {
                        console.log("here");
                        $scope.multipleCurrentObjects = [];
                        for (var i = 0; i < searchInPlan.length; i++) {
                            if (!mainTag && $scope.currentObject.referencePlan === searchInPlan[i].referencePlan) {
                                $scope.multipleCurrentObjects.push(searchInPlan[i]);
                            } else if (mainTag && $scope.currentObject.planObjectId === searchInPlan[i].referencePlan) {
                                $scope.multipleCurrentObjects.push(searchInPlan[i]);
                            }
                            if (!mainTag && $scope.currentObject.referencePlan === searchInPlan[i].planObjectId) {
                                object = angular.copy(searchInPlan[i]);
                            }

                        }
                        if ($scope.multipleCurrentObjects.length > 0) {
                            $scope.detailsFilled = false;
//                            $scope.tabAndDynamicFormModel = {};

                            $scope.tag = $scope.multipleCurrentObjects.length + 1;
                            for (var i = 0; i < $scope.multipleCurrentObjects.length; i++) {
                                $scope.selectedNoOfTag.push(i + 2);
                            }
                            $scope.currencyCode = object.currencyCode;
                            $scope.selectedRadio = angular.copy({color: object.colorId, cut: object.cutId, clarity: object.clarityId, fluorescence: object.flurosceneId});
//                            $scope.tabAndDynamicFormModel['A'] = object.writeCustom;
                            $scope.previousTab = 'A';
                            $scope.tabManager = {};
                            $scope.tagList = [];
                            var cent1 = object.cent;
                            var carat1 = object.carat;
                            $scope.tagList.push({code: String.fromCharCode(65), colorRadioList: $scope.colorRadioList, carat: carat1, cents: cent1, clarity: $scope.clarityRadioList, cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 27, clarityGroup: 28, cutGroup: 29, fluorescenceGroup: 30, selectColor: object.colorId, selectClarity: object.clarityId, selectCut: object.cutId, selectFluorescence: object.flurosceneId, price: object.price, currencyCode: $scope.currencyCode, breakage: object.breakage});
                            $scope.tabManager.tagList = $scope.tagList;
                            for (var ascii = 0; ascii < $scope.multipleCurrentObjects.length; ascii++) {
                                $scope.tagList.push({code: $scope.multipleCurrentObjects[ascii].tag, colorRadioList: ($scope.colorRadioList), carat: $scope.multipleCurrentObjects[ascii].carat, cents: $scope.multipleCurrentObjects[ascii].cent, clarity: angular.copy($scope.clarityRadioList), cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: ascii, clarityGroup: ascii, cutGroup: ascii, fluorescenceGroup: ascii, selectColor: $scope.multipleCurrentObjects[ascii].colorId, selectClarity: $scope.multipleCurrentObjects[ascii].clarityId, selectCut: $scope.multipleCurrentObjects[ascii].cutId, selectFluorescence: $scope.multipleCurrentObjects[ascii].flurosceneId, price: $scope.multipleCurrentObjects[ascii].price, currencyCode: $scope.multipleCurrentObjects[ascii].currencyCode, breakage: $scope.multipleCurrentObjects[ascii].breakage});
                            }
                            $scope.detailsFilled = true;
                        }
                    }
                }
                $rootScope.unMaskLoading();
            };

            $scope.editPlanWithoutCalc = function(object, flag) {
                var searchInPlan = undefined;
                $scope.improveFrom = undefined;
                if (object !== undefined && object !== null) {
                    $scope.checkDrop = false;
                    $scope.flag.editMode = true;
                    $scope.deleteIdsFromDb = [];
                    var found = false;
                    if ($scope.enteredPlan !== undefined && $scope.enteredPlan !== null && $scope.enteredPlan.length > 0) {
                        for (var i = 0; i < $scope.enteredPlan.length; i++) {
                            if ($scope.enteredPlan[i].sequentialPlanId === object.planID$AG$String) {
                                if (flag) {
                                    $scope.improveFrom = $scope.enteredPlan[i].planObjectId;
                                }
                                found = true;
                                searchInPlan = angular.copy($scope.enteredPlan);
                                $scope.checkDrop = true;
                            }
                        }
                    }
                    if (!found && $scope.submittedPlan !== undefined && $scope.submittedPlan !== null && $scope.submittedPlan.length > 0) {
                        for (var i = 0; i < $scope.submittedPlan.length; i++) {
                            if ($scope.submittedPlan[i].sequentialPlanId === object.planID$AG$String) {
                                if (flag) {
                                    $scope.improveFrom = $scope.submittedPlan[i].planObjectId;
                                }
                                found = true;
                                searchInPlan = angular.copy($scope.submittedPlan);
                                $scope.checkDrop = true;
                            }
                        }
                    }
                }
//                console.log(JSON.stringify(searchInPlan));
                $scope.currentObject = undefined;
                if (object !== null && object !== undefined && searchInPlan !== null && searchInPlan !== undefined && searchInPlan.length > 0) {
                    var mainTag = false;
                    $scope.writeAddFlag = false;
                    for (var i = 0; i < searchInPlan.length; i++) {
                        if (searchInPlan[i].sequentialPlanId === object.planID$AG$String) {
                            $scope.currentObject = angular.copy(searchInPlan[i]);
                            object = $scope.currentObject;
                            if ($scope.currentObject.referencePlan === null) {
                                mainTag = true;
                                $scope.detailsFilled = false;
//                                $scope.tabAndDynamicFormModel = {};
                                $scope.selectedNoOfTag = [];
                                $scope.tag = 1;
                                $scope.checkDrop = true;
//                                $scope.currencyCode = object.currencyCode;
//                                $scope.selectedRadio = angular.copy({color: object.colorId, cut: object.cutId, clarity: object.clarityId, fluorescence: object.flurosceneId});
                                $scope.categoryCustom = {};
                                $scope.categoryCustom = object.writeCustom;
                                $scope.previousTab = 'A';
                                $scope.selectedNoOfTag.push(1);
                                $scope.tabManager = {};
                                $scope.tagList = [];
                                $scope.tagList.push({code: String.fromCharCode(65)});
                                $scope.tabManager.tagList = $scope.tagList;
                                $scope.writeAddFlag = true;
                                $scope.detailsFilled = true;
                                console.log("here" + JSON.stringify(object.writeCustom));
                            }
                            break;
                        }
                    }
                    if ($scope.currentObject !== undefined) {
                        console.log("here");
                        $scope.multipleCurrentObjects = [];
                        for (var i = 0; i < searchInPlan.length; i++) {
                            if (!mainTag && $scope.currentObject.referencePlan === searchInPlan[i].referencePlan) {
                                $scope.multipleCurrentObjects.push(searchInPlan[i]);
                            } else if (mainTag && $scope.currentObject.planObjectId === searchInPlan[i].referencePlan) {
                                $scope.multipleCurrentObjects.push(searchInPlan[i]);
                            }
                            if (!mainTag && $scope.currentObject.referencePlan === searchInPlan[i].planObjectId) {
                                object = angular.copy(searchInPlan[i]);
                            }

                        }
                        if ($scope.multipleCurrentObjects.length > 0) {
                            console.log("multipleCurrentObjects");
                            $scope.detailsFilled = false;
//                            $scope.tabAndDynamicFormModel = {};

                            $scope.tag = $scope.multipleCurrentObjects.length + 1;
                            for (var i = 0; i < $scope.multipleCurrentObjects.length; i++) {
                                $scope.selectedNoOfTag.push(i + 2);
                            }
                            $scope.currencyCode = object.currencyCode;
                            $scope.selectedRadio = angular.copy({color: object.colorId, cut: object.cutId, clarity: object.clarityId, fluorescence: object.flurosceneId});
//                            $scope.tabAndDynamicFormModel['A'] = object.writeCustom;
                            $scope.previousTab = 'A';
                            $scope.tabManager = {};
                            $scope.tagList = [];
                            var cent1 = object.cent;
                            var carat1 = object.carat;
                            $scope.tagList.push({code: String.fromCharCode(65), colorRadioList: $scope.colorRadioList, carat: carat1, cents: cent1, clarity: $scope.clarityRadioList, cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 27, clarityGroup: 28, cutGroup: 29, fluorescenceGroup: 30, selectColor: object.colorId, selectClarity: object.clarityId, selectCut: object.cutId, selectFluorescence: object.flurosceneId, price: object.price, currencyCode: $scope.currencyCode, breakage: object.breakage});
                            $scope.tabManager.tagList = $scope.tagList;
                            for (var ascii = 0; ascii < $scope.multipleCurrentObjects.length; ascii++) {
                                $scope.tagList.push({code: $scope.multipleCurrentObjects[ascii].tag, colorRadioList: ($scope.colorRadioList), carat: $scope.multipleCurrentObjects[ascii].carat, cents: $scope.multipleCurrentObjects[ascii].cent, clarity: angular.copy($scope.clarityRadioList), cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: ascii, clarityGroup: ascii, cutGroup: ascii, fluorescenceGroup: ascii, selectColor: $scope.multipleCurrentObjects[ascii].colorId, selectClarity: $scope.multipleCurrentObjects[ascii].clarityId, selectCut: $scope.multipleCurrentObjects[ascii].cutId, selectFluorescence: $scope.multipleCurrentObjects[ascii].flurosceneId, price: $scope.multipleCurrentObjects[ascii].price, currencyCode: $scope.multipleCurrentObjects[ascii].currencyCode, breakage: $scope.multipleCurrentObjects[ascii].breakage});
                            }
                            $scope.detailsFilled = true;
                        }
                    }
                }
            };
            $scope.deletePlan = function() {
                var object = angular.copy($scope.objectToDelete);
                var planId = undefined;
                var mainTag = false;
                var referenceTag = undefined;
                if (object !== undefined && object !== null && $scope.enteredPlan !== undefined && $scope.enteredPlan !== null && $scope.enteredPlan.length > 0) {
                    for (var i = 0; i < $scope.enteredPlan.length; i++) {
                        if ($scope.enteredPlan[i].sequentialPlanId === object.planID$AG$String) {
                            referenceTag = $scope.enteredPlan[i];
                            if ($scope.enteredPlan[i].referencePlan === null) {
                                planId = $scope.enteredPlan[i].planObjectId;
                                mainTag = true;
                                break;
                            }
                        }
                    }
                    if (!mainTag) {
                        for (var i = 0; i < $scope.enteredPlan.length; i++) {
                            if ($scope.enteredPlan[i].sequentialPlanId === object.planID$AG$String) {
//                                console.log(JSON.stringify($scope.enteredPlan[i].planObjectId));
                                planId = angular.copy($scope.enteredPlan[i].planObjectId);
                                mainTag = true;
                                break;
                            }
                        }
                    }
                    console.log(JSON.stringify("planId" + planId));
                    if (mainTag) {
                        $scope.objectToDelete = {};
                        $("#deleteDialog").modal("hide");
                        $rootScope.removeModalOpenCssAfterModalHide();
                        WriteService.deletePlan(planId, function(res) {
                            $scope.addPlan();
                        }
                        , function() {
                            var msg = "Could not save, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            var reset = function(flag) {
                if (flag) {
                    $scope.flag.showAddPage = false;
                    $scope.retrieveFromWorkAllocation();
                    $scope.issuedStock = {};
                    $scope.issuedStock.data = [];
                }
            };
            $scope.submitPlan = function(form) {
                var map = {};
                var planIds = [];
                var workids = [];
                if ($scope.selectedLotOrPacket !== undefined && $scope.selectedLotOrPacket !== null && $scope.selectedLotOrPacket.length > 0) {
                    for (var i = 0; i < $scope.enteredPlan.length; i++) {
                        if ($scope.enteredPlan[i].sequentialPlanId === $scope.selectedLotOrPacket[0].planID$AG$String) {
                            var bestPlanId = [];
                            bestPlanId.push($scope.enteredPlan[i].planObjectId);
                            map["bestPlanId"] = bestPlanId;
                            break;
                        }
                    }
                }
                angular.forEach($scope.enteredPlan, function(itr) {
                    planIds.push(itr.planObjectId);
                });
                map["plan"] = planIds;
                workids.push(angular.copy($scope.workAllocationId));
                map["work"] = workids;
                console.log(map);

                WriteService.submitPlan(map, function(res) {
                    $rootScope.maskLoading();
                    $timeout(function() {
                        $route.reload();
                    }, 3000);
                    $rootScope.unMaskLoading();
                }
                , function() {
                    var msg = "Could not save, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
            };

//            save edited plan method
            $scope.saveEditedPlan = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    $scope.submitted = false;

                    for (prop in $scope.tabAndDynamicFormModel) {
                        if (prop === $scope.previousTab) {
                            $scope.tabAndDynamicFormModel[prop] = angular.copy($scope.categoryCustom);
                            $scope.writeAddFlag = false;
                            $scope.reset("categoryCustom");
//                        $scope.previousTab = angular.copy($scope.previousTab)
                            break;
                        }
                    }
                    angular.forEach($scope.tabAndDynamicFormModel, function(value, key) {
                        for (var i = 0; i < $scope.tagList.length; i++) {
                            if ($scope.tagList[i].code === key) {
                                $scope.tagList[i].dynamicForm = value;
                            }
                        }
                    });
                    $scope.writeServiceDataBean = {};
//                here plan type has defined.
                    $scope.writeServiceDataBean.planType = $scope.planType;
                    $scope.writeServiceDataBean.writeDbType = $scope.writeDbType;
                    if ($scope.packetIdForConstraint !== undefined) {
                        $scope.writeServiceDataBean.id = $scope.packetIdForConstraint;
                        $scope.writeServiceDataBean.hasPacket = true;
                    } else {
                        $scope.writeServiceDataBean.id = $scope.lotIdForConstraint;
                        $scope.writeServiceDataBean.hasPacket = false;
                    }
                    var writeServiceDataBeans = [];
                    var tagAndPlanObjectIdMap = {};
                    angular.forEach($scope.enteredPlan, function(itr) {
                        if (deleteTagIds.indexOf(itr.planObjectId) === -1) {
                            tagAndPlanObjectIdMap[itr.tag] = itr.planObjectId;
                        }
                    });
                    $scope.mainPlanObjectId = undefined;
                    angular.forEach($scope.tagList, function(itr) {
                        if (itr.code === 'A') {
                            $scope.mainPlanObjectId = tagAndPlanObjectIdMap[itr.code];
                        }
                        writeServiceDataBeans.push({planObjectId: tagAndPlanObjectIdMap[itr.code], tag: itr.code, colorId: itr.selectColor, clarityId: itr.selectClarity, cutId: itr.selectCut, flurosceneId: itr.selectFluorescence, price: itr.price, writeCustom: itr.dynamicForm, currencyCode: itr.currencyCode, caratValue: parseFloat(itr.carat.toString().concat(".", itr.cents.toString())), breakage: itr.breakage});
                    });
                    $scope.writeServiceDataBean.writeServiceDataBeans = writeServiceDataBeans;
                    $scope.writeServiceDataBean.deletedIds = deleteTagIds;
                    $scope.writeServiceDataBean.planObjectId = $scope.mainPlanObjectId;
//                console.log(deleteTagIds);
                    console.log("------>>>" + JSON.stringify($scope.writeServiceDataBean.writeServiceDataBeans));
//                console.log("------>>>" + JSON.stringify($scope.writeServiceDataBean.planObjectId));
                    WriteService.saveEditedPlan($scope.writeServiceDataBean, function(res) {
                        $scope.addPlan();
                    }
                    , function() {
                        var msg = "Could not save, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };


            $scope.retrievePreviousValue = function() {
                if (!!$scope.flag && !!$scope.flag.pricelist) {
                    console.log("$scope.gridOptions.data:::" + JSON.stringify($scope.gridOptions.data));
                    var map = {};
                    angular.forEach($scope.gridOptions.data, function(itr) {
                        var tempMap = {};
                        for (var i = 0; i < $scope.enteredPlan.length; i++) {
                            if (itr.planID$AG$String === $scope.enteredPlan[i].sequentialPlanId) {
                                tempMap['cut'] = $scope.enteredPlan[i].cutId;
                                tempMap['color'] = $scope.enteredPlan[i].colorId;
                                tempMap['clarity'] = $scope.enteredPlan[i].clarityId;
                                tempMap['fluorescence'] = $scope.enteredPlan[i].flurosceneId;
                                tempMap['carat'] = $scope.enteredPlan[i].caratId;
//                                tempMap['carat'] = 1;
//                                map[$scope.enteredPlan[i].planObjectId] = tempMap;
                                map[$scope.enteredPlan[i].sequentialPlanId] = tempMap;
                                break;
                            }
                        }
                    });
                    map["pricelist"] = {"id": $scope.flag.pricelist};

                    FinalizeService.retrievevaluefrompricelist(map, function(res) {

//                        var i = 70;
                        angular.forEach($scope.gridOptions.data, function(item) {
                            item["previousPrice"] = res[item.planID$AG$String];
//                            i = i - 17;
//                            item["previousPrice"] = i;
                        });
                        var col = {name: "previousPrice", displayName: "Previous Price", cellTemplate: '<div><span class="col-md-3">{{row.entity.previousPrice}}</span><div class="col-md-9" ng-show="row.entity.previousPrice!==N/A"><span ng-if="row.entity.previousPrice<row.entity.price" class="glyphicon glyphicon-arrow-down" style="color: red"></span><span ng-if="row.entity.previousPrice>row.entity.price" class="glyphicon glyphicon-arrow-up" style="color: green"></span>{{((row.entity.previousPrice-row.entity.price)/row.entity.price)*100 | number:2}}%</div></div>'};

                        $scope.gridOptions.columnDefs = $scope.gridOptions.columnDefs
                                .filter(function(el) {
                                    return el.name !== "previousPrice";
                                });

                        $scope.gridOptions.columnDefs.splice($scope.gridOptions.columnDefs.length - 1, 0, col);

                    })
                } else {
                    $scope.gridOptions.columnDefs = $scope.gridOptions.columnDefs
                            .filter(function(el) {
                                return el.name !== "previousPrice";
                            });
                }
            };

            $scope.openDeletePlanPopup = function(object) {
                $("#deleteDialog").modal("show");
                $scope.objectToDelete = object;
            }
            $scope.hidePopUp = function() {
                $("#deleteDialog").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.onCancel = function(form) {
                $scope.writeForm.$setPristine();
                $scope.writeForm.$valid = true;
                $scope.detailsFilled = false;
                $scope.tabAndDynamicFormModel = {};
                $scope.selectedNoOfTag = [];
                $scope.tag = 1;
                $scope.checkDrop = true;
                $scope.tabAndDynamicFormModel['A'] = null;
                $scope.previousTab = 'A';
                $scope.selectedNoOfTag.push(1);
                $scope.tabManager = {};
                $scope.tagList = [];
//                            $scope.tagList.push({code: String.fromCharCode(65), colorRadioList: $scope.colorRadioList, carat: 0, cents: 0, clarity: $scope.clarityRadioList, cut: $scope.cutRadioList, fluorescence: $scope.fluorescenceRadioList, colorGroup: 1, clarityGroup: 2, cutGroup: 3, fluorescenceGroup: 4, selectColor: $scope.colorRadioList[0].color, selectClarity: $scope.clarityRadioList[0].clarity, selectCut: $scope.cutRadioList[0].cut, selectFluorescence: $scope.fluorescenceRadioList[0].fluorescence, price: null, currencyCode: $scope.currencyCode});
                $scope.numberOfTagChanged(1);
                $scope.tabManager.tagList = $scope.tagList;

                $scope.detailsFilled = true;
            };
        }]);
});