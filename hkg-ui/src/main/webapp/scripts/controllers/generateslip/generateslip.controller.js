define(['hkg', 'customFieldService', 'generateSlipService', 'lotService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'dynamicForm', 'accordionCollapse'], function(hkg) {
    hkg.register.controller('GenerateSlipController', ["$rootScope", "$scope", "DynamicFormService", "GenerateSlipService", "CustomFieldService", "LotService", "$filter", function($rootScope, $scope, DynamicFormService, GenerateSlipService, CustomFieldService, LotService, $filter) {
            $rootScope.maskLoading();
            $scope.generateSlipList = [];
            $scope.initializeData = function() {
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("generateSlip");
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];
                }, function(reason) {
                    console.log("reason :" + reason);
                }, function(update) {
                    console.log("update :" + update);
                });
                $scope.flag = {};
                $scope.generateSlipList = [];
                $scope.listFilled = false;
                $scope.generateSlipDataBean = {};
                $scope.slipLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function(rows) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                };
                $scope.searchedSlipList = [];
                $scope.slipLabelListForUiGrid = [];
                $rootScope.maskLoading();
                GenerateSlipService.retrieveLotsAndPackets(function(result) {
                    $rootScope.unMaskLoading();
                    $scope.dataRetrieved = true;
                    if (result['generateSlipList'] !== null && result['tabelHeaders'] !== null && result['tabelHeaders'].length > 0) {
                        $scope.searchedSlipList = result['generateSlipList'];
                        $scope.nodeDetailsInfo = [];
                        $scope.slipLabelListForUiGrid = result['tabelHeaders'];
                        if ($scope.generalSearchTemplate === null || $scope.generalSearchTemplate === undefined) {
                            $scope.flag.configSearchFlag = true;
                        } else {
                            if (result.dynamicServiceInitBean !== null && result.dynamicServiceInitBean !== undefined) {
                                var dynamicServiceInitBean = result.dynamicServiceInitBean;
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
                            var success = function(result)
                            {
                                angular.forEach($scope.searchedSlipList, function(itr) {
                                    angular.forEach($scope.slipLabelListForUiGrid, function(list) {
                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                            itr.categoryCustom[list.name] = "NA";
                                        }
                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                        }
                                        if (itr.hasOwnProperty("value")) {
                                            itr.categoryCustom["~@value"] = itr.value;
                                        }
                                        if (itr.hasOwnProperty("label")) {
                                            itr.categoryCustom["~@label"] = itr.label;
                                        }
                                        if (itr.hasOwnProperty("description")) {
                                            itr.categoryCustom["~@description"] = itr.description;
                                        }
                                        if (itr.hasOwnProperty("id")) {
                                            itr.categoryCustom["~@id"] = itr.id;
                                        }
                                        if (itr.hasOwnProperty("status")) {
                                            itr.categoryCustom["~@status"] = itr.status;
                                        }
                                    });
                                    $scope.generateSlipList.push(itr.categoryCustom);
                                });
//                                $scope.gridOptions.data = $scope.generateSlipList;
//                                $scope.gridOptions.columnDefs = $scope.slipLabelListForUiGrid;
//                                $scope.listFilled = true;
                                $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                $scope.flag.configSearchFlag = false;
                            }
                            DynamicFormService.convertorForCustomField($scope.searchedSlipList, success);
                        }
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                });
            };
            $scope.updateStockAccordingToNode = function(nodeId) {
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
                        console.log(workAllocationIds.length > 0);
                        angular.forEach($scope.generateSlipList, function(item, index) {
                            if (workAllocationIds.length > 0 && workAllocationIds.indexOf(item["~@value"]) !== -1) {
                                item["~@index"] = index + 1;
                                result.push(item);
                            }
                        });
                        $scope.currentNodeStocks = angular.copy(result);
                        if (angular.isDefined($scope.gridApi)) {
                            $scope.gridApi.selection.clearSelectedRows();
                        }
                        $scope.gridOptions.data = result;
                        $scope.gridOptions.columnDefs = $scope.slipLabelListForUiGrid;
                        $scope.listFilled = true;
                        if (!!$scope.isQuingRequired) {
                            $scope.gridOptions.enableSorting = false;
                            $scope.gridOptions.isRowSelectable = function(row) {
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
                }
            };
            $scope.initGenerateSlipForm = function(generateSlipForm) {
                $scope.generateSlipForm = generateSlipForm;
            };
            $scope.onCancelOfSearch = function() {
                $scope.gridApi.selection.clearSelectedRows();
            };

            $scope.reset = function(sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("generateslip");
                    $scope.dbType = {};
                    templateData.then(function(section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function(reason) {

                    }, function(update) {

                    });
                }
            };

            $scope.generateSlip = function() {
                if ($scope.gridApi.selection.getSelectedRows().length > 1) {
                    $scope.generateSlipDataBean.workAllotmentIds = [];
                    $scope.noOfPackets = 0;
                    $scope.noOfLots = 0;
                    $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                    $scope.slipObj = angular.copy($scope.selectedRows);
                    for (var row = 0; row < $scope.selectedRows.length; row++) {
                        if ($scope.selectedRows[row]["~@status"] !== null) {
                            $scope.noOfPackets = $scope.noOfPackets + 1;
                        }
                        else if ($scope.selectedRows[row]["~@status"] === null && $scope.selectedRows[row]["~@id"] !== null) {
                            $scope.noOfLots = $scope.noOfLots + 1;
                        }
                    }
                    if ($scope.noOfLots > 0 && $scope.noOfPackets > 0) {
                        delete $scope.selectedRows;
                        $scope.selectedRows = [];
                        $scope.gridApi.selection.clearSelectedRows();
                        var msg = "Only select either lots or packets";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                    } else {
                        if ($scope.selectedRows[0]["~@status"] !== null) {
                            $scope.generateSlipDataBean.isPacket = true;
                        } else {
                            $scope.generateSlipDataBean.isPacket = false;
                        }
                        for (var row = 0; row < $scope.selectedRows.length; row++) {
                            if ($scope.selectedRows[row]["~@value"] !== null) {
                                $scope.generateSlipDataBean.workAllotmentIds.push($scope.selectedRows[row]["~@value"].toString());
                            }
                        }
                        if ($scope.generateSlipDataBean.workAllotmentIds !== undefined && $scope.generateSlipDataBean.workAllotmentIds !== null) {
                            GenerateSlipService.generateSlip($scope.generateSlipDataBean, function(res) {
                                $scope.initializeData();
                            }, function() {
                                console.log("faliure");
                            });
                        }
                    }

                } else {
                    $scope.gridApi.selection.clearSelectedRows();
                    var msg = "Please select more than one stock to generate slip";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }

            };

            $rootScope.unMaskLoading();
        }]);
});