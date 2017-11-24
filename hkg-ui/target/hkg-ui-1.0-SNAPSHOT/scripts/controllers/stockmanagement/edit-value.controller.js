define(['hkg', 'customFieldService', 'editvalueService', 'ngload!uiGrid', 'finalizeService'], function(hkg) {
    hkg.register.controller('EditValueController', ["$rootScope", "$scope", "DynamicFormService", "CustomFieldService", "EditValueService", "FinalizeService", function($rootScope, $scope, DynamicFormService, CustomFieldService, EditValueService, FinalizeService) {
            // console.log("Edit value controller loaded");
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "editValue";
            $rootScope.activateMenu();

            $scope.headerListForPlans = [{name: "planNumber", displayName: "Plan Id"},
                {name: "stockNumber", displayName: "Lot/Packet Id"},
                {name: "cut", displayName: "Cut"},
                {name: "color", displayName: "Color"},
                {name: "clarity", displayName: "Clarity"},
                {name: "carat", displayName: "Carat"},
                {name: "price", displayName: "Price"}];
            $scope.initializeData = function() {
                $scope.stockdataflag = false;
                $scope.lotPacketMap = {};
                $scope.featureMap = {};
                $scope.flag = {};
                $scope.flag = {};
                $scope.flag.showstockPage = false;
                $scope.stockList = [];
                $scope.listFilled = false;
                $scope.flag.rowSelectedflag = false;
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        $scope.lotPacketMap = {};
                        for (var index = 0; index < $scope.gridApi.selection.getSelectedRows().length; index++) {
                            var item = $scope.gridApi.selection.getSelectedRows()[index];
                            if (item["~@type"] === 'lot') {
                                if (!!!$scope.lotPacketMap["lot"]) {
                                    $scope.lotPacketMap["lot"] = [];
                                }
                                $scope.lotPacketMap["lot"].push({id: item["~@stockid"], value: item["~@stocknumber"]});
                            } else if (item["~@type"] === 'packet') {
                                if (!!!$scope.lotPacketMap["packet"]) {
                                    $scope.lotPacketMap["packet"] = [];
                                }
                                $scope.lotPacketMap["packet"].push({id: item["~@stockid"], value: item["~@stocknumber"]});
                            }
                        }
                    });

                };
                $scope.searchedStockList = [];
                $scope.generalSearchTemplate = [];
                $scope.stockLabelListForUiGrid = [];

                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("valueEdit");
                templateData.then(function(section) {
                    $scope.generalSearchTemplate = section['genralSection'];

                    if (!!$scope.generalSearchTemplate && $scope.generalSearchTemplate.length > 0) {
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            $scope.featureMap[item.model] = item.featureName;
                            if (item.fromModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                            }

                        }
                    }
                    $scope.searchResetFlag = true;

                    $scope.map = {};
                    var finalMap = {};
                    angular.forEach($scope.featureMap, function(val, label) {
                        if (!finalMap[val]) {
                            finalMap[val] = [];
                        }
                        finalMap[val].push(label);
                    });
                    $rootScope.maskLoading();
                    EditValueService.retrieveLotsPacketsEditValue(finalMap, function(result1) {
                        // console.log("resultttttt:::::" + JSON.stringify(result1))
                        $scope.searchedStockList = angular.copy(result1.stockList);
                        if (!$scope.generalSearchTemplate) {
                            $scope.flag.configSearchFlag = true;
                            $rootScope.unMaskLoading();
                        } else {
                            var success = function(result)
                            {
                                // console.log("In sucess");
                                angular.forEach($scope.searchedStockList, function(itr) {
                                    angular.forEach($scope.stockLabelListForUiGrid, function(list) {
                                        if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                            itr.categoryCustom[list.name] = "NA";
                                        }
                                        else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                            if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                        }
                                        if (itr.hasOwnProperty("description")) {
                                            itr.categoryCustom["~@type"] = itr.description;
                                        }

                                        if (itr.hasOwnProperty("value")) {
                                            itr.categoryCustom["~@stocknumber"] = itr.value;
                                        }

                                        if (itr.hasOwnProperty("label")) {
                                            itr.categoryCustom["~@stockid"] = itr.label;
                                        }

                                    });
                                    $scope.stockList.push(itr.categoryCustom);
                                });
                                $scope.gridOptions.data = $scope.stockList;
                                $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                $scope.listFilled = true;
                                $scope.flag.configSearchFlag = false;
                                $rootScope.unMaskLoading();
                            };
                            DynamicFormService.convertorForCustomField($scope.searchedStockList, success, function() {
                                $rootScope.unMaskLoading();
                            });
                        }
                    }, function() {
                        $rootScope.unMaskLoading();
                    });

                }, function(reason) {

                }, function(update) {

                });
            };


            $scope.onCanelOfSearch = function() {
                $scope.gridApi.selection.clearSelectedRows();
            };

            $scope.editvalueNext = function() {
                $scope.flag.showstockPage = true;
                $scope.stockdataflag = true;
                $scope.listFilled1 = false;
                FinalizeService.retrievePriceList(function(result) {
                    $scope.pricelistDtl = JSON.parse(angular.toJson(result));
                    for (var key in $scope.pricelistDtl) {
                        if ($scope.pricelistDtl.hasOwnProperty(key)) {
                            var temp = $scope.pricelistDtl[key];
                            $scope.pricelistDtl[key] = new Date(temp).toUTCString().replace(/\s*(GMT|UTC)$/, "");
                        }
                    }
                    // console.log("PriceListDtl::" + JSON.stringify($scope.pricelistDtl));
                });
                var payload = {};
                if (!!$scope.lotPacketMap["lot"]) {
                    angular.forEach($scope.lotPacketMap["lot"], function(itr) {
                        if (!!!payload["lot"]) {
                            payload["lot"] = [];
                        }
                        payload["lot"].push(itr.id);
                    });
                }
                if (!!$scope.lotPacketMap["packet"]) {
                    angular.forEach($scope.lotPacketMap["packet"], function(itr) {
                        if (!!!payload["packet"]) {
                            payload["packet"] = [];
                        }
                        payload["packet"].push(itr.id);
                    });
                }
                // console.log("Payload::" + JSON.stringify(payload));
                $scope.gridForPlan = {};
                $scope.gridForPlan.enableFiltering = true;
                $scope.gridForPlan.multiSelect = true;
                $scope.gridForPlan.columnDefs = [];
                $scope.gridForPlan.data = [];
                $scope.gridForPlan.onRegisterApi = function(gridApi) {
                    //set gridApi on scope
                    $scope.gridApi1 = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                        $scope.planIdsToChange = [];
                        for (var index = 0; index < $scope.gridApi1.selection.getSelectedRows().length; index++) {
                            var item = $scope.gridApi1.selection.getSelectedRows()[index];
                            $scope.planIdsToChange.push({"planId": item.planId, "previousPrice": item.previousPrice});
                        }
                        // console.log("toChange:::" + JSON.stringify($scope.planIdsToChange));
                    });

                };
                $rootScope.maskLoading();
                EditValueService.retrievePlansByLotOrPacket(payload, function(res) {

                    $rootScope.unMaskLoading();
                    // console.log("before result::::::" + JSON.stringify(res));
                    angular.forEach(res, function(itr) {
                        if (itr.stockType === "lot") {
                            var result = $.grep($scope.lotPacketMap["lot"], function(e) {
                                return e.id === itr.stockId;
                            });
                            // console.log("result::::" + JSON.stringify(result));
                            if (result.length === 1) {
                                itr.stockNumber = result[0].value;
                            }
                        }
                    });
                    // console.log("after result::::::" + JSON.stringify(res));
                    $scope.gridForPlan.data = res;
                    $scope.gridForPlan.columnDefs = $scope.headerListForPlans;

                    $scope.listFilled1 = true;

                }, function() {
                    $scope.listFilled1 = true;
                    $rootScope.unMaskLoading();
                });
            };

            $scope.retrievePreviousValue = function() {
                if (!!$scope.gridApi1) {
                    $scope.gridApi1.selection.clearSelectedRows();
                    if (!!$scope.flag && !!$scope.flag.pricelist) {
                        // console.log("$scope.gridForPlan.data:::" + JSON.stringify($scope.gridForPlan.data));
                        var map = {};
                        angular.forEach($scope.gridForPlan.data, function(itr) {
                            var tempMap = {};

                            tempMap['cut'] = itr.cutId;
                            tempMap['color'] = itr.colorId;
                            tempMap['clarity'] = itr.clarityId;
                            tempMap['fluorescence'] = itr.flurosceneId;
                            tempMap['carat'] = itr.caratId;
//                            tempMap['carat'] = 1;
//                                map[$scope.enteredPlan[i].planObjectId] = tempMap;
                            map['planNumber'] = tempMap;
                        });
                        map["pricelist"] = {"id": $scope.flag.pricelist};

                        FinalizeService.retrievevaluefrompricelist(map, function(res) {

//                            var i = 70;
                            angular.forEach($scope.gridForPlan.data, function(item) {
                                item["previousPrice"] = res[item.planID$AG$String];
//                                i = i - 17;
//                                item["previousPrice"] = i;
                            });
                            var col = {name: "previousPrice", displayName: "Previous Price", cellTemplate: '<div><span class="col-md-3">{{row.entity.previousPrice}}</span><div class="col-md-9" ng-if="row.entity.previousPrice!==\'N/A\'"><span ng-if="row.entity.previousPrice<row.entity.price" class="glyphicon glyphicon-arrow-down" style="color: red"></span><span ng-if="row.entity.previousPrice>row.entity.price" class="glyphicon glyphicon-arrow-up" style="color: green"></span>{{((row.entity.previousPrice-row.entity.price)/row.entity.price)*100 | number:2}}</div><div class="col-md-9" ng-if="row.entity.previousPrice===N/A || !row.entity.previousPrice">{{entity +"N/A" | translate}}</div></div>'};

                            $scope.gridForPlan.columnDefs = $scope.gridForPlan.columnDefs
                                    .filter(function(el) {
                                        return el.name !== "previousPrice";
                                    });

                            $scope.gridForPlan.columnDefs.splice($scope.gridForPlan.columnDefs.length, 0, col);

                        });
                    } else {
                        $scope.gridForPlan.columnDefs = $scope.gridForPlan.columnDefs
                                .filter(function(el) {
                                    return el.name !== "previousPrice";
                                });
                    }
                }
            };
            $scope.editPlans = function() {
                // console.log(!!$scope.planIdsToChange);
                if (!!$scope.planIdsToChange) {
                    var payload = [];
                    // console.log("$scope.planIdsToChange:::" + JSON.stringify($scope.planIdsToChange));
                    angular.forEach($scope.planIdsToChange, function(itr) {
                        // console.log("itr::::" + JSON.stringify(itr));
                        if (itr.previousPrice !== null && $scope.previousPrice !== 'N/A') {
                            payload.push(itr);
                            // console.log("Payload:::" + JSON.stringify(payload));
                        }
                    });
                    // console.log("sending payload:::" + JSON.stringify(payload));
                    EditValueService.editValues(payload, function(response) {
                        $scope.initializeData();
                    }, function() {
                        $scope.initializeData();
                    });
                }
            };
            $rootScope.unMaskLoading();
        }]);
});


