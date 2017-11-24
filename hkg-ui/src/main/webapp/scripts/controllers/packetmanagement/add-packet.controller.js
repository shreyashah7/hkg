define(['hkg', 'packetService', 'customFieldService', 'lotService', 'packetmanagement/edit-packet.controller', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'accordionCollapse', 'ruleExecutionService'], function (hkg, packetService) {
    hkg.register.controller('AddPacketController', ["$rootScope", "$scope", "PacketService", "$timeout", "$filter", "$location", "$window", "CustomFieldService", "DynamicFormService", "LotService", "RuleExecutionService", function ($rootScope, $scope, PacketService, $timeout, $filter, $location, $window, CustomFieldService, DynamicFormService, LotService, RuleExecutionService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "addPacket";
            $rootScope.activateMenu();
            var featureMap = {};
            var featureCustomMap = {};
            $scope.afterLabel = 'A';
            var initializeData = function (flag) {
                if (flag) {
                    $scope.nodeDetailsInfo = [];
                    $scope.lotDataBean = {};
                    $scope.packetListTodisplay = [];
                    $scope.searchCustom = {};
                    featureCustomMap = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetAdd");
                    $scope.flag = {};
                    $scope.flag.searchFieldNotAvailable = false;
                    $scope.flag.noResultFound = false;
                    if (!($scope.dbType !== undefined && $scope.dbType !== null)) {
                        $scope.dbType = {};
                    }
                    featureMap = {};
                    $scope.modelAndHeaderList = [];
                    $scope.modelAndHeaderListForLot = [];
                    $scope.labelListForUiGrid = [];
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchInvoiceTemplate = [];
                        $scope.searchParcelTemplate = [];
                        $scope.searchLotTemplate = [];
                        $scope.fieldIdNameMap = {};
                        var invoiceDbFieldName = [];
                        var parcelDbFieldName = [];
                        var lotDbFieldName = [];
                        if ($scope.generalSearchTemplate != null && $scope.generalSearchTemplate.length > 0) {
                            for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                                var item = $scope.generalSearchTemplate [i];
                                if (item.featureName.toLowerCase() === 'invoice') {
                                    $scope.searchInvoiceTemplate.push(angular.copy(item));
                                    invoiceDbFieldName.push(angular.copy(item.model))
                                } else if (item.featureName.toLowerCase() === 'parcel') {
                                    $scope.searchParcelTemplate.push(angular.copy(item));
                                    parcelDbFieldName.push(angular.copy(item.model));
                                }
                                else if (item.featureName.toLowerCase() === 'lot') {
                                    $scope.searchLotTemplate.push(angular.copy(item));
                                    lotDbFieldName.push(angular.copy(item.model));
                                }
                                if (item.fromModel) {
                                    featureMap[item.fromModel] = item.featureName;
                                    $scope.labelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addPacketScreenRule(row, \'' + item.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.fromModel;
                                } else if (item.toModel) {
                                    featureMap[item.toModel] = item.featureName;
                                    $scope.labelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addPacketScreenRule(row, \'' + item.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.toModel;
                                } else if (item.model) {
                                    featureMap[item.model] = item.featureName;
                                    $scope.labelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200,
                                        cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.addPacketScreenRule(row, \'' + item.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                    });
                                    $scope.fieldIdNameMap[item.fieldId] = item.model;
                                }
                            }
                            if (invoiceDbFieldName.length > 0) {
                                featureCustomMap['invoice'] = invoiceDbFieldName;
                            }
                            if (parcelDbFieldName.length > 0) {
                                featureCustomMap['parcel'] = parcelDbFieldName;
                            }
                            if (lotDbFieldName.length > 0) {
                                featureCustomMap['lot'] = lotDbFieldName;
                            }
                        } else {
                            $scope.flag.searchFieldNotAvailable = true;
                        }
                        $scope.dataRetrieved = true;
                        $scope.searchResetFlag = true;
                    }, function (reason) {

                    }, function (update) {

                    });
                }
            };
            $scope.addPacketScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    if ($scope.selectedIssueStock.length === 0 || ($scope.selectedIssueStock[0].$$lotId$$ !== row.entity.$$lotId$$)) {
                        color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                    }
                }
                return color;
            };
            $scope.retrievedPacketsScreenRule = function (row, colField) {
                var color;
                if (!!row.entity.screenRuleDetailsWithDbFieldName && row.entity.screenRuleDetailsWithDbFieldName[colField] !== undefined && row.entity.screenRuleDetailsWithDbFieldName[colField] !== null) {
                    color = row.entity.screenRuleDetailsWithDbFieldName[colField].colorCode;
                }
                return color;
            };
            $scope.retrieveSearchedData = function () {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.issuedStockList = [];
                $scope.listFilled = false;
                if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.searchCustom) {
                        if (typeof $scope.searchCustom[prop] === 'object' && $scope.searchCustom[prop] != null) {
                            var toString = angular.copy($scope.searchCustom[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.searchCustom[prop] === 'string' && $scope.searchCustom[prop] !== null && $scope.searchCustom[prop] !== undefined && $scope.searchCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchCustom[prop] === 'number' && !!($scope.searchCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.searchCustom[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if (mapHasValue) {
                        $rootScope.maskLoading();
                        $scope.lotDataBean.featureCustomMapValue = {};
                        $scope.map = {};
//                        console.log('search custom : ' + JSON.stringify($scope.searchCustom));
//                        console.log('feature map :: ' + JSON.stringify(featureMap));
                        var finalMap = {};
                        var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, null, $scope.searchCustom);
                        $scope.searchCustom = angular.copy(searchResult);
                        angular.forEach(featureMap, function (val, label) {

                            var vlaueOfCus = $scope.searchCustom[label];
                            if (vlaueOfCus !== undefined) {
                                var temp = {};
                                if (!finalMap[val]) {
                                    temp[label] = vlaueOfCus;
                                    finalMap[val] = temp;
                                } else {
                                    var tempMap = finalMap[val];
                                    tempMap[label] = vlaueOfCus;
                                    finalMap[val] = tempMap;
                                }
                            } else {
                                vlaueOfCus = $scope.searchCustom['to' + label];
                                if (vlaueOfCus !== undefined) {
                                    var temp = {};
                                    if (!finalMap[val]) {
                                        temp['to' + label] = vlaueOfCus;
                                        finalMap[val] = temp;
                                    } else {
                                        var tempMap = finalMap[val];
                                        tempMap['to' + label] = vlaueOfCus;
                                        finalMap[val] = tempMap;
                                    }
                                }
                                vlaueOfCus = $scope.searchCustom['from' + label];
                                if (vlaueOfCus !== undefined) {
                                    var temp = {};
                                    if (!finalMap[val]) {
                                        temp['from' + label] = vlaueOfCus;
                                        finalMap[val] = temp;
                                    } else {
                                        var tempMap = finalMap[val];
                                        tempMap['from' + label] = vlaueOfCus;
                                        finalMap[val] = tempMap;
                                    }
                                }
                            }
                        });
                        $scope.lotDataBean.featureCustomMapValue = finalMap;
                        $scope.lotDataBean.hasPacket = false;
                        $scope.lotDataBean.featureDbFieldMap = featureCustomMap;
                        $scope.lotDataBean.ruleConfigMap = {
                            fieldIdNameMap: $scope.fieldIdNameMap,
                            featureName: "addPacketMenu"
                        };
//                        console.log(JSON.stringify($scope.lotDataBean));
                        $scope.lotDataBean.loggedInFranchise = true;
                        LotService.search($scope.lotDataBean, function (res) {
                            if (res !== undefined && res !== null && res.length > 0) {
                                $scope.searchedDataFromDb = angular.copy(res);
                                $scope.issuedStock = {};
                                $scope.issuedStock.enableFiltering = true;
                                $scope.issuedStock.multiSelect = false;
                                $scope.issuedStock.enableRowSelection = true;
                                $scope.issuedStock.enableSelectAll = true;
                                $scope.selectedIssueStock = [];
                                $scope.issuedStock.onRegisterApi = function (gridApi) {
                                    $scope.gridApi = gridApi;
                                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                        if ($scope.selectedIssueStock.length > 0) {
                                            $.each($scope.selectedIssueStock, function (index, result) {
                                                if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                    //Remove from array
                                                    $scope.flag.repeatedRow = true;
                                                    $scope.selectedIssueStock.splice(index, 1);
                                                    return false;
                                                } else {
                                                    $scope.flag.repeatedRow = false;
                                                }
                                            });
                                            if (!$scope.flag.repeatedRow) {
                                                $scope.selectedIssueStock.push(row["entity"]);
                                            }
                                        } else {
                                            $scope.selectedIssueStock.push(row["entity"]);
                                        }
                                        if ($scope.selectedIssueStock.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                    gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                                        if ($scope.selectedIssueStock.length > 0) {
                                            angular.forEach(rows, function (row) {
                                                $.each($scope.selectedIssueStock, function (index, result) {
                                                    if (result["$$hashKey"] === row["entity"]["$$hashKey"]) {
                                                        //Remove from array
                                                        $scope.flag.repeatedRow = true;
                                                        $scope.selectedIssueStock = [];
                                                        return false;
                                                    } else {
                                                        $scope.flag.repeatedRow = false;
                                                    }
                                                });
                                                if (!$scope.flag.repeatedRow) {
                                                    $scope.selectedIssueStock.push(row["entity"]);
                                                }
                                            });
                                        } else {
                                            angular.forEach(rows, function (row) {
                                                $scope.selectedIssueStock.push(row["entity"]);
                                            });
                                        }
                                        if ($scope.selectedIssueStock.length > 0) {
                                            $scope.flag.rowSelectedflag = true;
                                        } else {
                                            $scope.flag.rowSelectedflag = false;
                                        }
                                    });
                                };
                                for (var i = 0; i < res.length; i++) {
                                    if (res[i].categoryCustom != null) {
                                        res[i].categoryCustom["$$$value"] = res[i].value;
                                    }
                                }
                                var success = function (result)
                                {
                                    $scope.searchedData = angular.copy(result);
                                    $scope.issuedStockList = [];
                                    angular.forEach($scope.searchedData, function (itr) {
                                        angular.forEach($scope.labelListForUiGrid, function (list) {
                                            if (itr.categoryCustom != null && !itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom != null && itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (!(!!(itr.categoryCustom[list.name]))) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                        });
                                        if (itr.categoryCustom != null) {
                                            itr.categoryCustom.$$lotId$$ = itr.value;
                                            itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                            $scope.issuedStockList.push(itr.categoryCustom);
                                        }

                                    });
                                    $scope.issuedStock.data = $scope.issuedStockList;
                                    $scope.issuedStock.columnDefs = $scope.labelListForUiGrid;
                                    $scope.listFilled = true;
                                    $rootScope.unMaskLoading();
//                            $scope.updateStockAccordingToNode($scope.currentActivityNode);
                                }
                                DynamicFormService.convertorForCustomField(res, success);
                                window.setTimeout(function () {
                                    $(window).resize();
                                    $(window).resize();
                                }, 200);
                                $scope.issueListFilled = true;

                            } else {
                                $scope.issuedStock = {};
                                $scope.issuedStock.data = [];
                                $scope.listFilled = true
                                $scope.flag.noResultFound = true;
                                $rootScope.unMaskLoading();
                            }
                        }

                        , function () {
                            var msg = "Could not retrieve, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                    }
                } else {
                    var msg = "Please select atleast one search criteria for search";
                    var type = $rootScope.warning;
                    $rootScope.addMessage(msg, type);
                }
            };
            initializeData(true);
            $scope.initAddPacketForm = function (addPacketForm) {
                $scope.addPacketForm = addPacketForm;
            };
            $scope.addPacket = function () {
                $scope.caratOfLot = undefined;
                $scope.invoiceCustom = undefined;
                $scope.parcelCustom = undefined;
                $scope.lotCustom = undefined;
                $scope.flag.showAddPage = false;
                if ($scope.selectedIssueStock != null && $scope.selectedIssueStock.length > 0) {
                    $scope.preRuleSatisfied = false;
                    var dataToSend = {
                        featureName: 'addPacketMenu',
                        entityId: $scope.selectedIssueStock[0].$$$value,
                        entityType: 'lot'
                    };

                    RuleExecutionService.executePreRule(dataToSend, function (res) {
                        //Prevent the record from edit if pre rule satisfies.

                        if (!!res.validationMessage) {
                            $scope.preRuleSatisfied = true;
                            var type = $rootScope.warning;
                            $rootScope.addMessage(res.validationMessage, type);
                        } else {
                            $scope.count = 0;
                            $scope.gridOptions = {};
                            $scope.gridOptions.enableFiltering = true;
                            $scope.gridOptions.data = [];
                            $scope.packetLabelForUIGrid = [];
                            $rootScope.maskLoading();
                            $scope.workAllocationId = undefined;
                            $scope.lotIDWithSeqNumber = null;
                            $scope.stockCarat = angular.copy($scope.selectedIssueStock[0].stockCarat);
                            $scope.stockPieces = angular.copy($scope.selectedIssueStock[0].stockPieces);
                            $scope.lotIDWithSeqNumber = angular.copy($scope.selectedIssueStock[0].lotID$AG$String);
                            for (var i = 0; i < $scope.searchedDataFromDb.length; i++) {
                                if ($scope.searchedDataFromDb[i].value === $scope.selectedIssueStock[0].$$$value) {
                                    $scope.invoiceIdForConstraint = $scope.searchedDataFromDb[i].description;
                                    $scope.parcelIdForConstraint = $scope.searchedDataFromDb[i].label;
                                    $scope.lotIdForConstraint = $scope.searchedDataFromDb[i].value;
//                            console.log($scope.lotIdForConstraint);
                                    break;
                                }
                            }
                            $scope.nextSeqNumber = null;
                            PacketService.getNextPacketSequence($scope.lotIdForConstraint, function (res) {
                                if (res !== undefined && res !== null) {
                                    if (res.sequenceNumber.toString().length === 1) {
                                        $scope.nextSeqNumber = "0" + res.sequenceNumber.toString();
                                    } else {
                                        $scope.nextSeqNumber = res.sequenceNumber;
                                    }

                                }
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to retrieve sequence number, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                            $scope.flag.showAddPage = true;
                            var featureDbfieldNameMap = {};
                            var dbFieldName = [];
                            $scope.packetDataBean = {};
                            $scope.packetListToSave = [];
                            $scope.parcelDataBean = {invoiceDataBean: {invoiceCustom: '', invoiceDbType: ''}};
                            CustomFieldService.retrieveDesignationBasedFields("packetAdd", function (response) {
                                $scope.response = angular.copy(response);
//                        $scope.invoiceCustom = DynamicFormService.resetSection($scope.generaInvoiceTemplate);
                                var templateDataInvoice = DynamicFormService.retrieveSectionWiseCustomFieldInfo("invoice");
                                $scope.invoiceDbType = {};
                                templateDataInvoice.then(function (section) {
                                    $scope.generaInvoiceTemplate = section['genralSection'];
                                    $scope.generaInvoiceTemplate = DynamicFormService.retrieveCustomData($scope.generaInvoiceTemplate, response);
                                    if ($scope.generaInvoiceTemplate != null && $scope.generaInvoiceTemplate.length > 0) {
                                        angular.forEach($scope.generaInvoiceTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });
                                        featureDbfieldNameMap["invoiceDbFieldName"] = dbFieldName;
                                    }
                                }, function (reason) {
                                }, function (update) {
                                });
                                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("parcel");
                                $scope.parcelDbType = {};
                                templateData.then(function (section) {
                                    $scope.generaParcelTemplate = section['genralSection'];
                                    $scope.generaParcelTemplate = DynamicFormService.retrieveCustomData($scope.generaParcelTemplate, response);
                                    var dbFieldName = [];
                                    if ($scope.generaParcelTemplate != null && $scope.generaParcelTemplate.length > 0) {
                                        angular.forEach($scope.generaParcelTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });
                                        featureDbfieldNameMap["parcelDbFieldName"] = dbFieldName;
                                    }
                                }, function (reason) {
                                }, function (update) {
                                });
                                var templateDataOfLot = DynamicFormService.retrieveSectionWiseCustomFieldInfo("lot");
                                $scope.lotDbType = {};
                                templateDataOfLot.then(function (section) {
                                    $scope.generalLotTemplate = section['genralSection'];
                                    $scope.generalLotTemplate = DynamicFormService.retrieveCustomData($scope.generalLotTemplate, response);
                                    var dbFieldName = [];
                                    if ($scope.generalLotTemplate != null && $scope.generalLotTemplate.length > 0) {
                                        angular.forEach($scope.generalLotTemplate, function (itr) {
                                            if (itr.model) {
                                                dbFieldName.push(itr.model);
                                            } else if (itr.fromModel) {
                                                dbFieldName.push(itr.fromModel);
                                            }
                                            else if (itr.toModel) {
                                                dbFieldName.push(itr.toModel);
                                            }
                                        });
                                        featureDbfieldNameMap["lotDbFieldName"] = dbFieldName;
                                        var dbFieldName = [];
                                        dbFieldName.push($scope.lotIdForConstraint);
                                        featureDbfieldNameMap["id"] = dbFieldName;
                                    }
                                }, function (reason) {

                                }, function (update) {
                                });
                                $scope.categoryCustom = {};
                                $scope.categoryCustom['packetID$AG$String'] = $scope.nextSeqNumber;
                                var templateDataPacket = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                                if (!($scope.packetDbType != null)) {
                                    $scope.packetDbType = {};
                                }
                                templateDataPacket.then(function (section) {
                                    $scope.generalPacketTemplate = [];
                                    $scope.fieldIdNameMap = {};
                                    $scope.generalPacketTemplate = section['genralSection'];
                                    var packetField = [];
                                    var result = Object.keys(response).map(function (key, value) {
                                        angular.forEach(this[key], function (itr) {
                                            if (key !== 'Packet#P#') {
                                                packetField.push({Packet: itr});
                                            }
                                        });
                                    }, response);
                                    $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetField);
                                    $scope.fieldNotConfigured = false;
                                    $scope.mandatoryFields = ['carat_of_packet$NM$Double', 'no_of_pieces_of_packet$NM$Long', 'due_date_of_packet$DT$Date', 'packetID$AG$String'];
                                    var packetDbFieldName = [];
                                    angular.forEach($scope.generalPacketTemplate, function (itr) {
                                        if (itr.model === 'no_of_pieces_of_packet$NM$Long') {
                                            itr.required = true;
                                        } else if (itr.model === 'carat_of_packet$NM$Double') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'due_date_of_packet$DT$Date') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'in_stock_of_packet$UMS$String') {
                                            itr.required = true;
                                        }
                                        else if (itr.model === 'packetID$AG$String') {
                                            itr.required = true;
                                        }
                                        if (itr.model) {
                                            packetDbFieldName.push(itr.model);
                                            $scope.packetLabelForUIGrid.push({name: itr.model, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.retrievedPacketsScreenRule(row, \'' + itr.model + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.model;
                                        } else if (itr.fromModel) {
                                            $scope.packetLabelForUIGrid.push({name: itr.fromModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.retrievedPacketsScreenRule(row, \'' + itr.fromModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.fromModel;
                                        }
                                        else if (itr.toModel) {
                                            $scope.packetLabelForUIGrid.push({name: itr.toModel, displayName: itr.label, minWidth: 200,
                                                cellTemplate: '<div class="ui-grid-cell-contents" ng-style="{ \'background-color\': grid.appScope.retrievedPacketsScreenRule(row, \'' + itr.toModel + '\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'
                                            });
                                            $scope.fieldIdNameMap[itr.fieldId] = itr.toModel;
                                        }
                                    });
                                    featureDbfieldNameMap["packetDbFieldName"] = packetDbFieldName;
                                    var dbFieldName = [];
                                    dbFieldName.push($scope.lotIdForConstraint);
                                    featureDbfieldNameMap["lotObjectId"] = dbFieldName;
//                            featureDbfieldNameMap.ruleConfigMap = {
//                                fieldIdNameMap: $scope.fieldIdNameMap,
//                                featureName: "addPacketMenu"
//                            };
                                    PacketService.retrievePacketByLotId(featureDbfieldNameMap, function (res) {
//                                console.log(JSON.stringify(res));
                                        angular.forEach(res, function (itr) {

                                        });
                                        if (res != null) {
                                            if (res[0].custom1 !== undefined && res[0].custom1 !== null) {
                                                $scope.invoiceCustom = res[0].custom1;
                                            } else {
                                                $scope.invoiceCustom = {};
                                            }
                                            if (res[0].custom3 !== undefined && res[0].custom3 !== null) {
                                                $scope.parcelCustom = res[0].custom3;
                                            } else {
                                                $scope.parcelCustom = {};
                                            }
                                            if (res[0].custom4 !== undefined && res[0].custom4 !== null) {
                                                $scope.lotCustom = res[0].custom4;
                                            } else {
                                                $scope.lotCustom = {};
                                            }
                                            var success = function (result)
                                            {
                                                $scope.packetDataForUiGrid = [];
                                                angular.forEach(result, function (itr) {
                                                    if (itr.categoryCustom !== undefined && itr.categoryCustom !== null) {
                                                        angular.forEach($scope.packetLabelForUIGrid, function (list) {
                                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                                itr.categoryCustom[list.name] = "NA";
                                                            }
                                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                                    itr.categoryCustom[list.name] = "NA";
                                                                }
                                                            }
                                                            itr.objectIdOfParcel = {id: itr.value};
                                                            //                                            itr.categoryCustom["value"] = itr.value;
                                                        });
                                                        itr.categoryCustom.$$packetId$$ = itr.value;
                                                        itr.categoryCustom.screenRuleDetailsWithDbFieldName = itr.screenRuleDetailsWithDbFieldName;
                                                        $scope.packetDataForUiGrid.push(itr.categoryCustom);
                                                    }
                                                });
                                                $scope.gridOptions.data = $scope.packetDataForUiGrid;
                                                if ($scope.packetDataForUiGrid.length > 0) {
                                                    $scope.tempListFilled = true;
                                                }
                                                $scope.gridOptions.columnDefs = $scope.packetLabelForUIGrid;
                                                if ($scope.editPacketFeature) {
                                                    $scope.gridOptions.columnDefs.push({name: 'Action',
                                                        cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updatePacket(row.entity)">Update</i></a></div>', enableFiltering: false, minWidth: 200});
                                                }
                                                window.setTimeout(function () {
                                                    $(window).resize();
                                                    $(window).resize();
                                                    $rootScope.unMaskLoading();
                                                }, 100);
                                            }
                                            DynamicFormService.convertorForCustomField(res, success);

                                        } else {
                                            $scope.invoiceCustom = {};
                                            $scope.parcelCustom = {};
                                            $scope.lotCustom = {};
                                        }
                                    }
                                    , function () {
                                        var msg = "Could not retrieve, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });
                                    if (packetDbFieldName.length > 0 && $scope.mandatoryFields != null && $scope.mandatoryFields.length > 0) {
                                        for (var field = 0; field < $scope.mandatoryFields.length; field++) {
                                            if (packetDbFieldName.indexOf($scope.mandatoryFields[field]) === -1) {
                                                $scope.fieldNotConfigured = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        $scope.fieldNotConfigured = true;
                                    }
                                    $scope.flag.customFieldGenerated = true;
                                    $scope.AddPacketCtrl = this;
                                    this.packetAddFlag = true;
                                    $rootScope.unMaskLoading();
                                    if ($scope.stockCarat === 0 || $scope.stockPieces === 0) {
                                        var msg = "Packet(s) already created for this lot";
                                        var type = $rootScope.warning;
                                        $rootScope.addMessage(msg, type);
                                    }
                                }, function (reason) {

                                }, function (update) {

                                });
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "Failed to retrieve data";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    }, function (failure) {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve pre rule.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }

            };
            $scope.onCanel = function () {
                if ($scope.addPacketForm != null) {
                    $scope.addPacketForm.$dirty = false;
                }
                $scope.submitted = false;
                angular.forEach($scope.fieldsToClear, function (item) {
                    delete $scope.categoryCustom[item];
                });
            };
            $scope.onCanelOfSearch = function (addPacketForm) {
                if (addPacketForm != null) {
                    $scope.addPacketForm = addPacketForm;
                    $scope.addPacketForm.$dirty = false;
                }
                $scope.searchedData = [];
                $scope.searchResetFlag = false;
                $scope.reset("searchCustom");
            };
            $scope.reset = function (sectionTobeReset, flag) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("packetAdd");
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                        $rootScope.unMaskLoading();
                        $scope.flag.customFieldGenerated = true;
                    }, function (reason) {
                    }, function (update) {
                    });
                } else if (sectionTobeReset === "categoryCustom") {
                    angular.forEach($scope.categoryCustom, function (value, key) {
                        if (flag) {
                            if (key !== 'carat_of_packet$NM$Double') {
                                delete $scope.categoryCustom[key];
                            }
                        } else {
                            delete $scope.categoryCustom[key];
                        }
                    });
                    var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("packet");
                    templateData.then(function (section) {
                        var packetField = [];
                        var result = Object.keys($scope.response).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key !== 'Packet#P#') {
                                    packetField.push({Packet: itr});
                                }
                            });
                        }, $scope.response);
                        $scope.generalPacketTemplate = null;
                        $scope.generalPacketTemplate = section['genralSection'];
                        $scope.generalPacketTemplate = DynamicFormService.retrieveCustomData($scope.generalPacketTemplate, packetField);
                        $timeout(function () {
                            // Controller As
                            $scope.AddPacketCtrl = this;
                            this.packetAddFlag = true;
                            $rootScope.unMaskLoading();
                        }, 100);
                        console.log($scope.generalPacketTemplate.length);
                        ;
                        angular.forEach($scope.generalPacketTemplate, function (itr) {
                            if (itr.model === 'no_of_pieces_of_packet$NM$Long') {
                                itr.required = true;
                            } else if (itr.model === 'carat_of_packet$NM$Double') {
                                itr.required = true;
                            }
                            else if (itr.model === 'due_date_of_packet$DT$Date') {
                                itr.required = true;
                            }
                            else if (itr.model === 'in_stock_of_packet$UMS$String') {
                                itr.required = true;
                            }
                            else if (itr.model === 'packetID$AG$String') {
                                itr.required = true;
                            }
                        });
                        $scope.flag.customFieldGenerated = true;
                        $rootScope.unMaskLoading();
                    }, function (reason) {
                    }, function (update) {
                    });
                }
            };
            $scope.resetAddForm = function (Flag, clearField) {
                if (Flag) {
                    if ($scope.addLotForm != null) {
                        $scope.addLotForm.$dirty = false;
                    }
                    $scope.flag.editPacket = false;
                    $scope.AddPacketCtrl = this;
                    this.packetAddFlag = false;
                    $scope.reset("categoryCustom", clearField);
                }
            };

            $scope.back = function () {
                if ($scope.addLotForm != null) {
                    $scope.addLotForm.$dirty = false;
                }
                $scope.flag.showAddPage = false;
                $scope.selectedIssueStock = [];

            };
            $scope.createPacket = function (addPacketForm) {
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (typeof $scope.categoryCustom[prop] === 'object' && $scope.categoryCustom[prop] != null) {
                            var toString = angular.copy($scope.categoryCustom[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.categoryCustom[prop] === 'string' && $scope.categoryCustom[prop] !== null && $scope.categoryCustom[prop] !== undefined && $scope.categoryCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.categoryCustom[prop] === 'number' && !!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.categoryCustom[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    $scope.submitted = true;
                    if (addPacketForm.$valid && mapHasValue) {
                        $scope.caratDoesNotMatch = false;
                        $scope.submitted = false;
                        if ($scope.categoryCustom['carat_of_packet$NM$Double'] > $scope.stockCarat || $scope.categoryCustom['no_of_pieces_of_packet$NM$Long'] > $scope.stockPieces) {
                            $scope.caratDoesNotMatch = true;
                        }
                        if (!$scope.caratDoesNotMatch) {
                            var sequenceNumber = $scope.categoryCustom['packetID$AG$String'];
                            if (sequenceNumber !== undefined && sequenceNumber !== null) {
                                if (sequenceNumber !== null && $scope.lotIDWithSeqNumber) {
                                    var franchiseSeqNumberMap = {seqNumber: sequenceNumber, lotId: $scope.lotIdForConstraint};
                                    PacketService.isPacketSequenceNumberExist(franchiseSeqNumberMap, function (res) {
                                        if (res.data) {
                                            $rootScope.unMaskLoading();
                                            var msg = "Packet already exists for the same id.";
                                            var type = $rootScope.error;
                                            $rootScope.addMessage(msg, type);
                                        } else {
                                            var dataForRule = angular.copy($scope.categoryCustom);
                                            dataForRule['packetID$AG$String'] = $scope.lotIDWithSeqNumber.concat("-").concat($scope.categoryCustom['packetID$AG$String']).concat("A");
                                            var dataToSend = {
                                                featureName: 'addPacketMenu',
                                                entityId: null,
                                                entityType: 'packet',
                                                currentFieldValueMap: dataForRule,
                                                dbType: $scope.packetDbType,
                                                otherEntitysIdMap: {invoiceId: $scope.invoiceIdForConstraint, parcelId: $scope.parcelIdForConstraint, lotId: $scope.lotIdForConstraint}
                                            };
                                            console.log('post rule dataToSend::::' + JSON.stringify(dataToSend));
                                            RuleExecutionService.executePostRule(dataToSend, function (res) {
                                                console.log('post rule res::::' + JSON.stringify(res));
                                                if (!!res.validationMessage) {
                                                    var type = $rootScope.warning;
                                                    $rootScope.addMessage(res.validationMessage, type);
                                                    $rootScope.unMaskLoading();
                                                } else {
                                                    var packetSeqnumber = angular.copy($scope.categoryCustom['packetID$AG$String']);
                                                    var sectionData = [];
                                                    sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                                                    var packetDataBean = {};
                                                    packetDataBean.invoiceDataBean = {};
                                                    packetDataBean.parcelDataBean = {};
                                                    packetDataBean.lotDataBean = {};
                                                    packetDataBean.invoiceDataBean.id = $scope.invoiceIdForConstraint;
                                                    packetDataBean.parcelDataBean.id = $scope.parcelIdForConstraint;
                                                    packetDataBean.lotDataBean.id = $scope.lotIdForConstraint;
                                                    packetDataBean.packetDbType = $scope.packetDbType;
                                                    packetDataBean.packetCustom = angular.copy($scope.categoryCustom);
                                                    packetDataBean.packetCustom['packetID$AG$String'] = $scope.lotIDWithSeqNumber.concat("-").concat($scope.categoryCustom['packetID$AG$String']).concat("A");
                                                    packetDataBean.sequenceNumber = packetSeqnumber;
                                                    packetDataBean.ruleConfigMap = {
                                                        fieldIdNameMap: $scope.fieldIdNameMap,
                                                        featureName: "addPacketMenu"
                                                    };
//                                            console.log(JSON.stringify(packetDataBean.packetCustom));
                                                    PacketService.createPacket(packetDataBean, function (response) {
                                                        $scope.tempListFilled = false;
//                                                        console.log(JSON.stringify(response));
                                                        if (response !== undefined && response !== null) {
                                                            $scope.stockCarat = $scope.stockCarat - $scope.categoryCustom['carat_of_packet$NM$Double'];
                                                            $scope.stockPieces = $scope.stockPieces - $scope.categoryCustom['no_of_pieces_of_packet$NM$Long'];
                                                            $scope.selectedIssueStock[0].stockCarat = angular.copy($scope.stockCarat);
                                                            $scope.selectedIssueStock[0].stockPieces = angular.copy($scope.stockPieces);
                                                            PacketService.getNextPacketSequence($scope.lotIdForConstraint, function (res) {
//                                                                console.log(JSON.stringify(res));
                                                                if (res !== undefined && res !== null) {
                                                                    if (res.sequenceNumber.toString().length === 1) {
                                                                        $scope.categoryCustom['packetID$AG$String'] = "0" + res.sequenceNumber.toString();
                                                                    } else {
                                                                        $scope.categoryCustom['packetID$AG$String'] = res.sequenceNumber;
                                                                    }
                                                                }
                                                            }, function () {
                                                                $rootScope.unMaskLoading();
                                                                var msg = "Failed to delete packet, please try again.";
                                                                var type = $rootScope.error;
                                                                $rootScope.addMessage(msg, type);
                                                            });
                                                            if ($scope.issuedStock.data != null && $scope.issuedStock.data.length > 0) {
                                                                for (var i = 0; i < $scope.issuedStock.data.length; i++) {
                                                                    if ($scope.issuedStock.data[i].$$$value === $scope.selectedIssueStock[0].$$$value) {
                                                                        $scope.issuedStock.data[i].carat_of_packet$NM$Double = $scope.issuedStock.data[i].carat_of_packet$NM$Double - $scope.categoryCustom['carat_of_packet$NM$Double'];
                                                                        $scope.issuedStock.data[i].no_of_pieces_of_packet$NM$Long = $scope.issuedStock.data[i].no_of_pieces_of_packet$NM$Long - $scope.categoryCustom['no_of_pieces_of_packet$NM$Long'];
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            var success = function (result)
                                                            {
                                                                result[0].categoryCustom["$$objectId"] = $scope.count;
                                                                angular.forEach($scope.packetLabelForUIGrid, function (list) {
                                                                    if (!result[0].categoryCustom.hasOwnProperty(list.name)) {
                                                                        result[0].categoryCustom[list.name] = "NA";
                                                                    }
                                                                    else if (result[0].categoryCustom.hasOwnProperty(list.name)) {
                                                                        if (result[0].categoryCustom[list.name] === null || result[0].categoryCustom[list.name] === '' || result[0].categoryCustom[list.name] === undefined) {
                                                                            result[0].categoryCustom[list.name] = "NA";
                                                                        }
                                                                    }
                                                                });
                                                                //Update with current lot id.
                                                                result[0].categoryCustom['packetID$AG$String'] = response[response.packetId];
                                                                //Rules related code
                                                                //Preserving lot id in bot ui-grid and actual data.
                                                                result[0].categoryCustom.$$packetId$$ = response.packetId;
                                                                result[0].categoryCustom.screenRuleDetailsWithDbFieldName = response.screenRuleDetailsWithDbFieldName;
                                                                $scope.gridOptions.data.push(angular.copy(result[0].categoryCustom));
                                                                $scope.gridOptions.columnDefs = $scope.packetLabelForUIGrid;
                                                                if ($scope.editPacketFeature) {
                                                                    $scope.gridOptions.columnDefs.push({name: 'Action',
                                                                        cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-click="grid.appScope.updatePacket(row.entity)">Update</i></a></div>', enableFiltering: false, minWidth: 200});
                                                                }
                                                            }
                                                            DynamicFormService.convertorForCustomField(sectionData, success);
                                                            $scope.resetAddForm(true, true);
                                                            $scope.addPacketForm.$dirty = false;
                                                            $scope.tempListFilled = true;
                                                        }
                                                    }, function () {
                                                        $rootScope.unMaskLoading();
                                                        var msg = "Could not create packet, please try again.";
                                                        var type = $rootScope.error;
                                                        $rootScope.addMessage(msg, type);
                                                    });
                                                }
                                            }, function (failure) {
                                                $rootScope.unMaskLoading();
                                                var msg = "Failed to authenticate post rule, please try again.";
                                                var type = $rootScope.error;
                                                $rootScope.addMessage(msg, type);
                                            });
                                        }
                                    }, function () {
                                        $rootScope.unMaskLoading();
                                        var msg = "Could not retrieve sequence number, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                    });
                                }

                            }


                        } else {
                            $scope.tempListFilled = true;
                            $rootScope.unMaskLoading();
                            var msg = "Carat value does not match, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        }

                    }
                }
            };
            $scope.editPacketLocally = function (packetObj) {
                $scope.index = packetObj.$$objectId;
                $scope.oldObj = angular.copy(packetObj, $scope.oldObj);
                if (packetObj != null) {
                    $scope.flag.editPacket = true;
                    if (!!($scope.packetListToSave && $scope.packetListToSave.length > 0)) {
                        for (var i = 0; i < $scope.packetListToSave.length; i++) {
                            if (packetObj.$$objectId != null && $scope.packetListToSave[i].$$objectId === packetObj.$$objectId) {
                                $scope.categoryCustom = angular.copy($scope.packetListToSave[i].categoryCustom);
                                break;
                            }
                        }
                    }
                }
            };
            $scope.savePacket = function (addPacketForm) {
                $rootScope.maskLoading();
                $scope.tempListFilled = false;
                $scope.submitted = true;
                if (Object.getOwnPropertyNames($scope.categoryCustom).length > 0) {
                    var mapHasValue = false;
                    for (var prop in $scope.categoryCustom) {
                        if (typeof $scope.categoryCustom[prop] === 'object' && $scope.categoryCustom[prop] != null) {
                            var toString = angular.copy($scope.categoryCustom[prop].toString());
                            if (typeof toString === 'string' && toString !== null && toString !== undefined && toString.length > 0) {
                                mapHasValue = true;
                                break;
                            }
                        }
                        if (typeof $scope.categoryCustom[prop] === 'string' && $scope.categoryCustom[prop] !== null && $scope.categoryCustom[prop] !== undefined && $scope.categoryCustom[prop].length > 0) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.categoryCustom[prop] === 'number' && !!($scope.categoryCustom[prop])) {
                            mapHasValue = true;
                            break;
                        }
                        if (typeof $scope.categoryCustom[prop] === 'boolean') {
                            mapHasValue = true;
                            break;
                        }
                    }
                    if ((mapHasValue && addPacketForm.$valid)) {
                        var sectionData = [];
                        sectionData.push({categoryCustom: angular.copy($scope.categoryCustom)});
                        angular.forEach($scope.packetListToSave, function (itr) {
                            if (itr.$$objectId === $scope.index) {
                                itr.categoryCustom = ($scope.categoryCustom);
                            }
                        });
                        var success = function (result)
                        {
                            for (var i = 0; i < $scope.gridOptions.data.length; i++) {
                                var item = $scope.gridOptions.data[i];
                                if (item.$$objectId === $scope.index) {
                                    $scope.gridOptions.data[i] = angular.copy(((result[0].categoryCustom)));
                                    $scope.gridOptions.data[i].$$objectId = $scope.index;
                                    break;
                                }
                            }
                        };
                        DynamicFormService.convertorForCustomField(sectionData, success);
                        $scope.tempListFilled = true;
                        $scope.flag.editPacket = false;
                        $scope.resetAddForm(true);
                    }
                }
                $rootScope.unMaskLoading();
            };
            $scope.showPopUp = function (lotObj) {
                $scope.index = lotObj.$$objectId;
                $("#deleteDialog").modal('show');
            };
            $scope.hidePopUp = function () {
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.deletePacket = function () {
                for (var i = 0; i < $scope.gridOptions.data.length; i++) {
                    var item = $scope.gridOptions.data[i];
                    if (item.$$objectId === $scope.index) {
                        $scope.gridOptions.data.splice(i, 1);
                        break;
                    }
                }
                for (var i = 0; i < $scope.packetListToSave.length; i++) {
                    var item = $scope.packetListToSave[i];
                    if (item.$$objectId === $scope.index) {
                        $scope.packetListToSave.splice(i, 1);
                        break;
                    }
                }
//                todo
                $scope.resetAddForm(true);
                $scope.flag.editPacket = false;
                $("#deleteDialog").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.onCancel = function (addLotForm) {
                if (addLotForm != null) {
                    $rootScope.maskLoading();
                    addLotForm.$dirty = false;
                    $scope.packetListToSave = [];
                    $scope.packetListTodisplay = [];
                    $scope.reset("categoryCustom");
                    $scope.AddPacketCtrl = this;
                    this.packetAddFlag = false;
                }
            };
            $scope.saveAllPacket = function (addPacketForm) {
                $scope.caratDoesNotMatch = false;
                var packetList = [];
                var totalCaratFromUI = undefined;
                angular.forEach($scope.packetListToSave, function (itr) {
                    if (!$scope.caratDoesNotMatch && itr.categoryCustom['carat_of_packet$NM$Double'] !== undefined && itr.categoryCustom['carat_of_packet$NM$Double'] !== null) {
                        if (totalCaratFromUI !== undefined) {
                            totalCaratFromUI = totalCaratFromUI + parseFloat(itr.categoryCustom['carat_of_packet$NM$Double']);
                        } else {
                            totalCaratFromUI = parseFloat(itr.categoryCustom['carat_of_packet$NM$Double']);
                        }
                    } else {
                        $scope.caratDoesNotMatch = true;
                    }
                    packetList.push({packetCustom: itr.categoryCustom, packetDbType: $scope.packetDbType});
                });
                if (!$scope.caratDoesNotMatch) {
                    if ($scope.caratOfLot !== totalCaratFromUI) {
                        $scope.caratDoesNotMatch = true;
                    }
                }
                var packetDataBean = {};
                packetDataBean.invoiceDataBean = {};
                packetDataBean.parcelDataBean = {};
                packetDataBean.lotDataBean = {};
                packetDataBean.invoiceDataBean.id = $scope.invoiceIdForConstraint;
                packetDataBean.parcelDataBean.id = $scope.parcelIdForConstraint;
                packetDataBean.lotDataBean.id = $scope.lotIdForConstraint;
                packetDataBean.packetList = packetList;
                packetDataBean.id = $scope.workAllocationId;
//                console.log(JSON.stringify(packetDataBean));
//                if (addPacketForm.$valid) {
                if (!$scope.caratDoesNotMatch) {
                    PacketService.create(packetDataBean, function (response) {
                        $scope.packetListToSave = [];
                        $scope.packetListTodisplay = [];
                        $scope.listFilled = false;
                        $scope.addPacketForm.$dirty = false;
                        initializeData(true);
                        $scope.flag.showAddPage = false;
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = "Could not create packet, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                } else {
                    $rootScope.unMaskLoading();
                    var msg = "Carat value does not match, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                }
//                }
            };
            $scope.updatePacket = function (searchDataObj) {
//                console.log(JSON.stringify(searchDataObj));
                $rootScope.packetId = angular.copy(searchDataObj['$$packetId$$']);
                if (!!($rootScope.packetId)) {
                    $rootScope.unMaskLoading();
                    $scope.flag.showUpdatePage = true;
                    $scope.addPacketForm.$dirty = false;
                    $location.path('/editpacket');
                    $rootScope.editPacket = true;
                }
            };

            $scope.sequenceNumberExists = function (sequenceNumber, addLotForm) {
                $scope.sequenceNumber = sequenceNumber;
                addLotForm.sequenceNumber.$setValidity('exists', true);
                if (sequenceNumber !== undefined && sequenceNumber !== null) {
                    if ($scope.sequenceNumber !== null && $scope.lotIDWithSeqNumber) {
                        var franchiseSeqNumberMap = {seqNumber: $scope.sequenceNumber, lotId: $scope.lotIdForConstraint};
                        PacketService.isPacketSequenceNumberExist(franchiseSeqNumberMap, function (res) {
//                            console.log(JSON.stringify(res));
                            if (res.data) {
                                addLotForm.sequenceNumber.$setValidity('exists', false);
                            }
                        }, function () {
                            $rootScope.unMaskLoading();
                            var msg = "Could not retrieve sequence number, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                        });
                    }

                }
            };

            $scope.hasEditPacketPermission = function (flag) {
                $scope.editPacketFeature = flag;
            };
        }]);
});