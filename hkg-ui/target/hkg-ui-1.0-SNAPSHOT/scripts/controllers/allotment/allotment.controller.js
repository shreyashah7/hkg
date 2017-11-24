define(['hkg', 'customFieldService', 'allotmentService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'printBarcodeValue', 'dynamicForm', 'datepickercustom.directive', 'accordionCollapse'], function (hkg) {
    hkg.register.controller('AllotmentController', ["$rootScope", "$scope", "DynamicFormService", "AllotmentService", "$timeout", function ($rootScope, $scope, DynamicFormService, AllotmentService, $timeout) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "allotmentMenu";
            $rootScope.activateMenu();
            var featureMap = {};
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'>" +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";
            $scope.initializeData = function () {
                $scope.flag = {};
                $scope.allotmentDataBean = {};
                $scope.searchResetFlag = false;
                $scope.flag.rowSelectedflag = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.flag.allotflag = false;
                $scope.searchCustom = {};
                var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("allotment");
                $scope.dbType = {};
                $scope.stockLabelListForUiGrid = [];
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.multiSelect = true;
                $scope.gridOptions.enableRowSelection = true;
                $scope.gridOptions.enableSelectAll = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                $scope.gridOptions.selectedItems = [];
                $scope.manualAllocation = {};
                $scope.gridOptions.onRegisterApi = function (gridApi) {
                    //set gridApi on scope
                    $scope.gridApi = gridApi;
                    gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });
                    gridApi.selection.on.rowSelectionChangedBatch($scope, function (rows) {
                        if ($scope.gridApi.selection.getSelectedRows().length > 0) {
                            $scope.flag.rowSelectedflag = true;
                        } else {
                            $scope.flag.rowSelectedflag = false;
                        }
                    });

                };
                templateData.then(function (section) {
                    $scope.searchInvoiceTemplate = [];
                    $scope.searchParcelTemplate = [];
                    $scope.searchLotTemplate = [];
                    $scope.searchPacketTemplate = [];
                    $scope.generalSearchTemplate = section['genralSection'];
                    if ($scope.generalSearchTemplate !== undefined && $scope.generalSearchTemplate !== null && $scope.generalSearchTemplate.length > 0) {
                        $scope.flag.configSearchFlag = false;
                        for (var i = 0; i < $scope.generalSearchTemplate.length; i++) {
                            var item = $scope.generalSearchTemplate [i];
                            if (item.featureName.toLowerCase() === 'invoice') {
                                $scope.searchInvoiceTemplate.push(angular.copy(item));
                            } else if (item.featureName.toLowerCase() === 'parcel') {
                                $scope.searchParcelTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'lot') {
                                $scope.searchLotTemplate.push(angular.copy(item));
                            }
                            else if (item.featureName.toLowerCase() === 'packet') {
                                $scope.searchPacketTemplate.push(angular.copy(item));
                            }
                            featureMap[item.model] = item.featureName;
                            $scope.stockLabelListForUiGrid.push({name: "packetID$AG$String", displayName: "Packet ID", minWidth: 200});
                            $scope.stockLabelListForUiGrid.push({name: "lotID$AG$String", displayName: "Lot ID", minWidth: 200});
                            if (item.fromModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.fromModel, displayName: item.label, minWidth: 200});
                            } else if (item.toModel) {
                                $scope.stockLabelListForUiGrid.push({name: item.toModel, displayName: item.label, minWidth: 200});
                            } else if (item.model) {
                                $scope.stockLabelListForUiGrid.push({name: item.model, displayName: item.label, minWidth: 200});
                            }

                        }
                    } else {
                        $scope.flag.configSearchFlag = true;
                    }
                    $scope.searchResetFlag = true;
                }, function (reason) {

                }, function (update) {

                });
            };

            $scope.retrieveSearchedData = function () {
                $scope.selectOneParameter = false;
                $scope.searchedData = [];
                $scope.searchedDataFromDb = [];
                $scope.listFilled = false;
                $scope.stockList = [];
                $scope.submitted = true;
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.data = [];
                if ($scope.allotmentForm.$valid) {
                    if (Object.getOwnPropertyNames($scope.searchCustom).length > 0) {
                        $rootScope.maskLoading();
                        var mapHasValue = false;
                        for (var prop in $scope.searchCustom) {
                            if (typeof $scope.searchCustom[prop] === 'object' && $scope.searchCustom[prop] !== null) {
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
                            $scope.allotmentDataBean.featureCustomMapValue = {};
                            $scope.map = {};
                            var finalMap = {};
                            var searchResult = DynamicFormService.convertSearchData($scope.searchInvoiceTemplate, $scope.searchParcelTemplate, $scope.searchLotTemplate, $scope.searchPacketTemplate, $scope.searchCustom);
                            angular.forEach(featureMap, function (val, label) {
                                var vlaueOfCus = searchResult[label];
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
                                    vlaueOfCus = searchResult['to' + label];
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
                                    vlaueOfCus = searchResult['from' + label];
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
                            $scope.allotmentDataBean.featureCustomMapValue = finalMap;
                            $scope.allotmentDataBean.stockCustom = featureMap;
                            AllotmentService.retrieveSearchedData($scope.allotmentDataBean, function (res) {
                                console.log("res :" + JSON.stringify(res));
                                $scope.searchedDataFromDb = angular.copy(res);
                                var success = function ()
                                {
                                    $scope.stockLabelListForUiGrid.push({name: "status", displayName: "Status", minWidth: 200});
                                    angular.forEach($scope.searchedDataFromDb, function (itr) {
                                        angular.forEach($scope.stockLabelListForUiGrid, function (list) {
                                            if (!itr.categoryCustom.hasOwnProperty(list.name)) {
                                                itr.categoryCustom[list.name] = "NA";
                                            }
                                            else if (itr.categoryCustom.hasOwnProperty(list.name)) {
                                                if (itr.categoryCustom[list.name] === null || itr.categoryCustom[list.name] === '' || itr.categoryCustom[list.name] === undefined) {
                                                    itr.categoryCustom[list.name] = "NA";
                                                }
                                            }
                                            if (itr.hasOwnProperty("value")) {
                                                itr.categoryCustom["~@packetid"] = itr.value;
                                            }
                                            if (itr.hasOwnProperty("label")) {
                                                itr.categoryCustom["~@lotid"] = itr.label;
                                            }
                                            if (itr.hasOwnProperty("description")) {
                                                itr.categoryCustom["~@parcelid"] = itr.description;
                                            }
                                            if (itr.hasOwnProperty("id")) {
                                                itr.categoryCustom["~@invoiceid"] = itr.id;
                                            }
                                        });
                                        $scope.stockList.push(itr.categoryCustom);
                                    });
                                    $scope.gridOptions.data = $scope.stockList;
                                    $scope.gridOptions.columnDefs = $scope.stockLabelListForUiGrid;
                                    $scope.gridOptions.isRowSelectable = function (row) {
                                        console.log(row);
                                        if (row.entity["status"] == 'Already Alloted') {
                                            return false;
                                        } else {
                                            return true;
                                        }
                                    };
                                    $scope.listFilled = true;
                                    $scope.flag.configSearchFlag = false;
                                    window.setTimeout(function () {
                                        $(window).resize();
                                        $(window).resize();
                                    }, 100);
                                };
                                DynamicFormService.convertorForCustomField($scope.searchedDataFromDb, success);
                                $rootScope.unMaskLoading();
                            }, function () {
                                var msg = "Could not retrieve, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        } else {
                            var msg = "Please select atleast one search criteria for search";
                            var type = $rootScope.warning;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                    } else {
                        var msg = "Please select atleast one search criteria for search";
                        var type = $rootScope.warning;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    }
                }
                $rootScope.unMaskLoading();
            };

            $scope.clearForm = function (manualPacketAddForm) {
                $scope.allotSubmitted = false;
                manualPacketAddForm.$setPristine();
            };

            $scope.changeUser = function (changedEmployee, index) {
                if (changedEmployee && !(changedEmployee instanceof Array)) {
                    var userId = changedEmployee.split(':')[0];
                    if (userId) {
                        AllotmentService.retrieveUserGradeSuggestionByUserId(userId, function (res) {
                            if (res && res !== null) {
                                var item = $scope.userGradeSuggestions[index];
                                var indexUser = -1;
                                for (var i = $scope.userGradeSuggestions.length - 1; i >= 0; i--) {
                                    if ($scope.userGradeSuggestions[i].userId == userId) {
                                        $scope.userGradeSuggestions[i].newStock = $scope.userGradeSuggestions[i].newStock + 1;
                                        item.newStock = $scope.userGradeSuggestions[i].newStock + 1;
                                        indexUser = index;
                                    }
                                    ;
                                    if ($scope.userGradeSuggestions[i].userId == item.userId) {
                                        $scope.userGradeSuggestions[i].newStock = $scope.userGradeSuggestions[i].newStock - 1;
                                    }
                                }
                                if (indexUser === -1) {
                                    item.newStock = 1;
                                }
                                item.userId = userId;
                                item.userName = res.data.userName;
                                item.grade = res.data.grade;
                                item.goingToGrade = res.data.goingToGrade;
                                item.gradeName = res.data.gradeName;
                                item.goingToGradeName = res.data.goingToGradeName;
                                $scope.userGradeSuggestions[index] = item;
                            }
                        });
                    }
                }

            };

            $scope.retrieveUserDetails = function (user) {
                if (user && !(user instanceof Array)) {
                    var userId = user.split(':')[0];
                    if (userId) {
                        AllotmentService.retrieveUserGradeSuggestionByUserId(userId, function (res) {
                            if (res && res !== null) {
                                var packetObjId = $scope.manualAllocation.packetObjectId;
                                var userValue = $scope.manualAllocation.userValue;
                                $scope.manualAllocation = res.data;
                                $scope.manualAllocation.userValue = userValue;
                                $scope.manualAllocation.packetObjectId = packetObjId;
                                $scope.manualAllocation.newStock = 1;
                                $scope.manualAllocation.totalStock = 1 + $scope.manualAllocation.currentStock;
                                angular.forEach($scope.packetList, function (packet) {
                                    if (packet.value == packetObjId) {
                                        $scope.manualAllocation.packetId = angular.copy(packet.label);
                                    }
                                });

                            }
                        });
                    }
                }
            };

            $scope.openAttendance = function (employee) {
                $scope.userDetails = angular.copy(employee);
                $("#attendanceModal").modal('show');
            };

            $scope.hideAttendancePopUp = function () {
                $("#attendanceModal").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.allotPacket = function () {
                $scope.allotSubmitted = false;
                $scope.selectedRows = $scope.gridApi.selection.getSelectedRows();
                if ($scope.selectedRows.length > 0) {
                    $scope.flag.allotflag = true;
                    $scope.packetIds = [];
                    angular.forEach($scope.selectedRows, function (item) {
                        $scope.packetIds.push(item['~@packetid']);
                    });
                    if ($scope.packetIds.length > 0) {
                        AllotmentService.retrieveUserGradeSuggestion($scope.packetIds, function (res) {
                            if (res != undefined && res.length > 0) {
                                $scope.userGradeSuggestions = angular.copy(res);
                                angular.forEach($scope.userGradeSuggestions, function (item) {
                                    item.userValue = item.userId + ":E";
                                    item.select2Config = {
                                        multiple: true,
                                        closeOnSelect: false,
                                        allowClear: true,
                                        placeholder: 'Select Employees',
                                        maximumSelectionSize: 1,
                                        initSelection: function (element, callback) {
                                            if (item.userId !== null && item.userName !== null) {
                                                var data = [];
                                                data.push({
                                                    id: item.userId + ":E",
                                                    text: item.userName
                                                });
                                                callback(data);
                                            }
                                        },
                                        formatResult: function (item) {
                                            return item.text;
                                        },
                                        formatSelection: function (item) {
                                            return item.text;
                                        },
                                        query: function (query) {
                                            var selected = query.term;
                                            $scope.names = [];
                                            if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                                                var search = query.term.slice(2);
                                                AllotmentService.retrieveUsers(search.trim(), function (data) {
                                                    if (data.length == 0) {
                                                        $scope.names.push({
                                                            id: item.userId + ":E",
                                                            text: item.userName
                                                        });
                                                        query.callback({
                                                            results: $scope.names
                                                        });
                                                    } else {
                                                        $scope.names = data.data;
                                                        angular.forEach(data.data, function (item) {
                                                            $scope.names.push({
                                                                id: item.value + ":E",
                                                                text: item.label
                                                            });
                                                        });
                                                        query.callback({
                                                            results: $scope.names
                                                        });
                                                    }
                                                }, function () {
                                                    console.log("failure");
                                                });

                                            } else {
                                                query.callback({
                                                    results: $scope.names
                                                });
                                            }
                                        }
                                    };
                                });
                            }
                            $scope.retrievePacketsAvailableInStock();
                        });
                    }
                }
                ;
            };

            $scope.reset = function (sectionTobeReset) {
                if (sectionTobeReset === "searchCustom") {
                    $scope.searchCustom = {};
                    var templateData = DynamicFormService.retrieveSearchWiseCustomFieldInfo("allotment");
                    $scope.dbType = {};
                    templateData.then(function (section) {
                        $scope.generalSearchTemplate = section['genralSection'];
                        $scope.searchResetFlag = true;
                    }, function (reason) {
                        console.log("reason :" + reason);
                    }, function (update) {
                        console.log("update :" + update);
                    });
                }
            };

            $scope.onCancelOfSearch = function () {
                if ($scope.allotmentForm !== null) {
                    $scope.allotmentForm.$dirty = false;
                    $scope.listFilled = false;
                    $scope.searchResetFlag = false;
                    $scope.reset("searchCustom");
                    $scope.initializeData();
                    $scope.allotmentForm.$setPristine();
                }
            };

            $scope.selectedTableRows = [];
            $scope.removePacketViewRow = function (index) {
                $scope.index = -1;
                var row = $scope.userGradeSuggestions[index];
                angular.forEach($scope.userGradeSuggestions, function (ugSuggestion) {
                    if (ugSuggestion.userId === row.userId) {
                        ugSuggestion.newStock = ugSuggestion.newStock - 1;
                    }
                });
                $scope.userGradeSuggestions.splice(index, 1);
                $scope.retrievePacketsAvailableInStock();
            };

            $scope.removeEmployeeViewRow = function (userId) {
                for (var i = $scope.userGradeSuggestions.length - 1; i >= 0; i--) {
                    if ($scope.userGradeSuggestions[i].userId === userId) {
                        $scope.userGradeSuggestions.splice(i, 1);
                    }
                }
                $scope.retrievePacketsAvailableInStock();
            };

            $scope.backToHomeScreen = function () {
                $scope.flag.allotflag = false;
                $scope.userGradeSuggestions = [];
                $scope.onCancelOfSearch();
            };

            $scope.initAllotmentForm = function (allotmentForm) {
                $scope.allotmentForm = allotmentForm;
            };

            $scope.retrievePacketsAvailableInStock = function () {
                AllotmentService.retrievePacketsAvailableInStock(function (result) {
                    if (result && result !== null && result.data) {
                        $scope.packetList = angular.copy(result.data);

                        angular.forEach($scope.userGradeSuggestions, function (userGrade) {
                            for (var i = $scope.packetList.length - 1; i >= 0; i--) {
                                if ($scope.packetList[i].value == userGrade.packetObjectId) {
                                    $scope.packetList.splice(i, 1);
                                }
                            }
                        });
                    }
                });
            };

            $scope.allotment = function () {
                console.log("$scope.userGradeSuggestions :" + JSON.stringify($scope.userGradeSuggestions));
                AllotmentService.allotPacket($scope.userGradeSuggestions, function (result) {
                    console.log("success");
                    $scope.backToHomeScreen();
                });
            };

            $scope.addManualAllocation = function (manualPacketAddForm) {
                $scope.allotSubmitted = true;
                if (manualPacketAddForm.$valid) {
                    if ($scope.manualAllocation != undefined && $scope.manualAllocation != null) {
                        $scope.manualAllocation.userValue = $scope.manualAllocation.userId + ":E";
                        $scope.manualAllocation.select2Config = {
                            multiple: true,
                            closeOnSelect: false,
                            allowClear: true,
                            placeholder: 'Select Employees',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {
                                if ($scope.manualAllocation.userId !== null && $scope.manualAllocation.userName !== null) {
                                    var data = [];
                                    data.push({
                                        id: $scope.manualAllocation.userId + ":E",
                                        text: $scope.manualAllocation.userName
                                    });
                                    callback(data);
                                }
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
                                if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                                    var search = query.term.slice(2);
                                    AllotmentService.retrieveUsers(search.trim(), function (data) {
                                        if (data.length == 0) {
                                            $scope.names.push({
                                                id: $scope.manualAllocation.userId + ":E",
                                                text: $scope.manualAllocation.userName
                                            });
                                            query.callback({
                                                results: $scope.names
                                            });
                                        } else {
                                            $scope.names = data.data;
                                            angular.forEach(data.data, function (item) {
                                                $scope.names.push({
                                                    id: item.value + ":E",
                                                    text: item.label
                                                });
                                            });
                                            query.callback({
                                                results: $scope.names
                                            });
                                        }
                                    }, function () {
                                        console.log("failure");
                                    });

                                } else {
                                    query.callback({
                                        results: $scope.names
                                    });
                                }
                            }
                        };
                        var indexUser = -1;
                        for (var i = $scope.userGradeSuggestions.length - 1; i >= 0; i--) {
                            if ($scope.userGradeSuggestions[i].userId == $scope.manualAllocation.userId) {
                                $scope.userGradeSuggestions[i].newStock = $scope.userGradeSuggestions[i].newStock + 1;
                                $scope.manualAllocation.newStock = $scope.userGradeSuggestions[i].newStock + 1;
                                indexUser = i;
                            }
                            ;
                        }
                        if (indexUser === -1) {
                            $scope.manualAllocation.newStock = 1;
                        }
                        $scope.userGradeSuggestions.push(angular.copy($scope.manualAllocation));
                        $timeout(function () {
                            $scope.manualAllocation = {};
                            $('#manualPacket').select2('data', "");
                            $('#manualUser').select2('data', "");
                            $scope.allotSubmitted = false;
                            manualPacketAddForm.$setPristine();

                        }, 500);
                        $scope.retrievePacketsAvailableInStock();
                    }
                }
            };
            hkg.register.filter('unique', function () {
                return function (items, filterOn) {
                    if (filterOn === false) {
                        return items;
                    }
                    if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
                        var newItems = [];
                        var extractValueToCompare = function (item) {
                            if (angular.isObject(item) && angular.isString(filterOn)) {
                                return item[filterOn];
                            } else {
                                return item;
                            }
                        };
                        angular.forEach(items, function (item) {
                            var isDuplicate = false;
                            for (var i = 0; i < newItems.length; i++) {
                                if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                            if (!isDuplicate) {
                                newItems.push(item);
                            }
                        });
                        items = newItems;
                    }
                    return items;
                };
            });

            //For recipients,
            $scope.autoCompleteUser = {
                multiple: true,
                closeOnSelect: false,
                allowClear: true,
                placeholder: 'Select Employees',
                maximumSelectionSize: 1,
                initSelection: function (element, callback) {
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    var selected = query.term;
                    $scope.names = [];
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        AllotmentService.retrieveUsers(search.trim(), function (data) {
                            if (data.data.length > 0) {
                                $scope.names = data.data;
                                angular.forEach(data.data, function (item) {
                                    $scope.names.push({
                                        id: item.value + ":E",
                                        text: item.label
                                    });
                                });
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        }, function () {
                            console.log("failure");
                        });

                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }
            }

            $scope.autoCompletePackets = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select Packet',
                maximumSelectionSize: 1,
                initSelection: function (element, callback) {
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    $scope.names = [];
                    if ($scope.packetList.length !== 0) {
                        angular.forEach($scope.packetList, function (item) {
                            $scope.names.push({
                                id: item.value,
                                text: item.label
                            });
                        });
                    }
                    query.callback({
                        results: $scope.names
                    });


                }
            };

            $rootScope.unMaskLoading();
        }]);
});