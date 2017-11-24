define(['hkg', 'customFieldService', 'ngload!uiGrid', 'addMasterValue', 'dynamicForm', 'accordionCollapse', 'issuereceivestockService', 'lotService'], function(hkg) {
    hkg.register.controller('IssueReceiveController', ["$rootScope", "$scope", "DynamicFormService", "CustomFieldService", "IssueReceiveStockService", "CenterCustomFieldService", "$timeout", "LotService", "$filter", function($rootScope, $scope, DynamicFormService, CustomFieldService, IssueReceiveStockService, CenterCustomFieldService, $timeout, LotService, $filter) {
            $rootScope.maskLoading();

            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "newIssueReceive";
            $scope.entity = "ISSUERECEIVE.";
            $rootScope.activateMenu();
            $scope.dataVals = {roughFlag: true, lotFlag: true, packetFlag: true};
            $scope.stockDtls = [];
            $scope.IIStockDtls = [];
            $scope.associatedDeptList = [];
            $scope.designationList = [];
            $scope.medium = "";
            $scope.directReceiveArray = [];
            $scope.isUserInvalid = true;
            $scope.selectorIds = [];
            $scope.selectorIdsForIssue = [];
            $scope.generalCustom = {};
            $scope.fieldDbType = {};
            var stockNumbers = [];
            var collectArray = [];
            var issueArray = [];
            var requestArray = [];
            var stockToIds = {};
            var stockToIdsForIssue = {};
            var stockToIdsForReturn = {};
            var stockToIdsForRequest = {};
            var selectorIdsForReturn = [];
            var templatesForRequestType = {};
            var parcelStock = {
                "id": "Parcels",
                "displayName": "Parcels",
                "parentId": 0,
                "parentName": "None"
            };
            var lotStock = {
                "id": "Lots",
                "displayName": "Lots",
                "parentId": 0,
                "parentName": "None"
            };
            var packetStock = {
                "id": "Packets",
                "displayName": "Packets",
                "parentId": 0,
                "parentName": "None"
            };
            $scope.treeForCollect = [];
            $scope.treeForIssue = [];
            $scope.treeForReturn = [];
            $scope.treeForRequest = [];
            function prepareTreeForCollect() {
                $scope.treeForCollect = [];
                if (stockToIds["Parcels"] !== null && stockToIds["Parcels"] !== undefined && stockToIds["Parcels"].length > 0) {
                    var parcels = [];
                    angular.forEach(stockToIds["Parcels"], function(item) {
                        var parc = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Parcels",
                            "parentName": "Parcels"
                        };
                        parcels.push(parc);
                    });
                    var parcelMenu = angular.copy(parcelStock);
                    parcelMenu["children"] = parcels;
                    parcelMenu["displayName"] = "Parcels (" + parcels.length + ")";
                    $scope.treeForCollect.push(parcelMenu);
                }
                if (stockToIds["Lots"] !== null && stockToIds["Lots"] !== undefined && stockToIds["Lots"].length > 0) {
                    var lots = [];
                    angular.forEach(stockToIds["Lots"], function(item) {
                        var lt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        lots.push(lt);
                    });
                    var lotMenu = angular.copy(lotStock);
                    lotMenu["children"] = lots;
                    lotMenu["displayName"] = "Lots (" + lots.length + ")";
                    $scope.treeForCollect.push(lotMenu);
                }
                if (stockToIds["Packets"] !== null && stockToIds["Packets"] !== undefined && stockToIds["Packets"].length > 0) {
                    var packets = [];
                    angular.forEach(stockToIds["Packets"], function(item) {
                        var pckt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        packets.push(pckt);
                    });
                    var packetMenu = angular.copy(packetStock);
                    packetMenu["children"] = packets;
                    packetMenu["displayName"] = "Packets (" + packets.length + ")";
                    $scope.treeForCollect.push(packetMenu);
                }
            }
            function prepareTreeForIssue() {
                $scope.treeForIssue = [];
                if (stockToIdsForIssue["Parcels"] !== null && stockToIdsForIssue["Parcels"] !== undefined && stockToIdsForIssue["Parcels"].length > 0) {
                    var parcels = [];
                    angular.forEach(stockToIdsForIssue["Parcels"], function(item) {
                        var parc = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Parcels",
                            "parentName": "Parcels"
                        };
                        parcels.push(parc);
                    });
                    var parcelMenu = angular.copy(parcelStock);
                    parcelMenu["children"] = parcels;
                    parcelMenu["displayName"] = "Parcels (" + parcels.length + ")";
                    $scope.treeForIssue.push(parcelMenu);
                }
                if (stockToIdsForIssue["Lots"] !== null && stockToIdsForIssue["Lots"] !== undefined && stockToIdsForIssue["Lots"].length > 0) {
                    var lots = [];
                    angular.forEach(stockToIdsForIssue["Lots"], function(item) {
                        var lt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        lots.push(lt);
                    });
                    var lotMenu = angular.copy(lotStock);
                    lotMenu["children"] = lots;
                    lotMenu["displayName"] = "Lots (" + lots.length + ")";

                    $scope.treeForIssue.push(lotMenu);
                }
                if (stockToIdsForIssue["Packets"] !== null && stockToIdsForIssue["Packets"] !== undefined && stockToIdsForIssue["Packets"].length > 0) {
                    var packets = [];
                    angular.forEach(stockToIdsForIssue["Packets"], function(item) {
                        var pckt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        packets.push(pckt);
                    });
                    var packetMenu = angular.copy(packetStock);
                    packetMenu["children"] = packets;
                    packetMenu["displayName"] = "Packets (" + packets.length + ")";
                    $scope.treeForIssue.push(packetMenu);
                }
            }
            function prepareTreeForReturn() {
                $scope.treeForReturn = [];
                if (stockToIdsForReturn["Parcels"] !== null && stockToIdsForReturn["Parcels"] !== undefined && stockToIdsForReturn["Parcels"].length > 0) {
                    var parcels = [];
                    angular.forEach(stockToIdsForReturn["Parcels"], function(item) {
                        var parc = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Parcels",
                            "parentName": "Parcels"
                        };
                        parcels.push(parc);
                    });
                    var parcelMenu = angular.copy(parcelStock);
                    parcelMenu["children"] = parcels;
                    parcelMenu["displayName"] = "Parcels (" + parcels.length + ")";
                    $scope.treeForReturn.push(parcelMenu);
                }
                if (stockToIdsForReturn["Lots"] !== null && stockToIdsForReturn["Lots"] !== undefined && stockToIdsForReturn["Lots"].length > 0) {
                    var lots = [];
                    angular.forEach(stockToIdsForReturn["Lots"], function(item) {
                        var lt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        lots.push(lt);
                    });
                    var lotMenu = angular.copy(lotStock);
                    lotMenu["children"] = lots;
                    lotMenu["displayName"] = "Lots (" + lots.length + ")";

                    $scope.treeForReturn.push(lotMenu);
                }
                if (stockToIdsForReturn["Packets"] !== null && stockToIdsForReturn["Packets"] !== undefined && stockToIdsForReturn["Packets"].length > 0) {
                    var packets = [];
                    angular.forEach(stockToIdsForReturn["Packets"], function(item) {
                        var pckt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        packets.push(pckt);
                    });
                    var packetMenu = angular.copy(packetStock);
                    packetMenu["children"] = packets;
                    packetMenu["displayName"] = "Packets (" + packets.length + ")";
                    $scope.treeForReturn.push(packetMenu);
                }
            }
            function prepareTreeForRequest() {
                $scope.treeForRequest = [];
                if (stockToIdsForRequest["Parcels"] !== null && stockToIdsForRequest["Parcels"] !== undefined && stockToIdsForRequest["Parcels"].length > 0) {
                    var parcels = [];
                    angular.forEach(stockToIdsForRequest["Parcels"], function(item) {
                        var parc = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Parcels",
                            "parentName": "Parcels"
                        };
                        parcels.push(parc);
                    });
                    var parcelMenu = angular.copy(parcelStock);
                    parcelMenu["children"] = parcels;
                    parcelMenu["displayName"] = "Parcels (" + parcels.length + ")";
                    $scope.treeForRequest.push(parcelMenu);
                }
                if (stockToIdsForRequest["Lots"] !== null && stockToIdsForRequest["Lots"] !== undefined && stockToIdsForRequest["Lots"].length > 0) {
                    var lots = [];
                    angular.forEach(stockToIdsForRequest["Lots"], function(item) {
                        var lt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        lots.push(lt);
                    });
                    var lotMenu = angular.copy(lotStock);
                    lotMenu["children"] = lots;
                    lotMenu["displayName"] = "Lots (" + lots.length + ")";

                    $scope.treeForRequest.push(lotMenu);
                }
                if (stockToIdsForRequest["Packets"] !== null && stockToIdsForRequest["Packets"] !== undefined && stockToIdsForRequest["Packets"].length > 0) {
                    var packets = [];
                    angular.forEach(stockToIdsForRequest["Packets"], function(item) {
                        var pckt = {
                            "id": item,
                            "displayName": item,
                            "parentId": "Lots",
                            "parentName": "Lots"
                        };
                        packets.push(pckt);
                    });
                    var packetMenu = angular.copy(packetStock);
                    packetMenu["children"] = packets;
                    packetMenu["displayName"] = "Packets (" + packets.length + ")";
                    $scope.treeForRequest.push(packetMenu);
                }
            }
            $scope.availableCollectStock = [];
            $scope.format = $rootScope.dateFormat;
            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
            };
            LotService.retrieveFranchiseDetails(function(data) {
                $scope.franchiseList = angular.copy(data);
            });
            $scope.payload = {slipnoDate: new Date(), "selectedFranchise": $rootScope.session.companyId};
            $scope.dateOptions = {};
            var sortedOperations = ["II", "RI", "RQ", "CL", "IS", "RC", "RT"];
            var type = ["Inward", "Outward"];
            $scope.codeToType = {"IW": "Inward", "OW": "Outward"};
            var codeToMedium = {"R": "Rough", "L": "Lot", "P": "Packet"};
            var codeToAccessRights = {"RQ": "Request", "CL": "Collect", "IS": "Issue", "RC": "Receive", "RT": "Return"};
            $scope.codeToOperation = {"II": "Issue Direct", "RI": "Receive Direct", "RQ": "Request", "CL": "Collect", "IS": "Issue", "RC": "Receieve"};
            $scope.initForm = function() {
                IssueReceiveStockService.retrieveModifiers(function(res) {
                    $scope.type = [];
                    $scope.operation = [];

                    $scope.configuredMediums = [];
                    if (res["Types"] !== null && res["Types"] !== undefined) {
                        angular.forEach(res["Types"], function(key) {
                            $scope.type.push({"key": key, "value": $scope.codeToType[key]});
                        });
                        if ($scope.type.length > 0) {
                            $scope.dataVals.type = $scope.type[0]["key"];
                        }
                    }
                    if (res["Modes"] !== null && res["Modes"] !== undefined) {
                        if (res["Modes"].indexOf("DR") !== -1) {
                            $scope.operation.push({"key": "II", "value": "Issue Inward"});
                            $scope.operation.push({"key": "RI", "value": "Receive Inward"});
                        }
                        if (res["Modes"].indexOf("VS") !== -1) {
                            if (res["AccessRights"] !== null && res["AccessRights"] !== null) {
                                angular.forEach(res["AccessRights"], function(value) {
                                    $scope.operation.push({"key": value, "value": codeToAccessRights[value]});
                                });
                            }
                        }
                        var tempObj = {};
                        for (var i = 0; i < $scope.operation.length; i++) {
                            tempObj[$scope.operation[i].key] = $scope.operation[i];
                        }
                        var index = 0;
                        for (var i = 0; i < sortedOperations.length; i++) {
                            if (tempObj[sortedOperations[i]] !== undefined && tempObj[sortedOperations[i]] !== null) {
                                $scope.operation[index++] = tempObj[sortedOperations[i]];
                            }
                        }
                        tempObj = undefined;
                        if ($scope.operation.length > 0) {
                            $scope.dataVals.operation = $scope.operation[0]["key"];
                        }
                        retrieveFields($scope.dataVals.operation);
                    }
                    if (res["Medium"] !== null && res["Medium"] !== undefined) {
                        angular.forEach(res["Medium"], function(value) {
                            $scope.configuredMediums.push(codeToMedium[value]);
                        });
                    }
                    if ($scope.dataVals.operation === "CL") {
                        $scope.retrieveAvailableCollectStock();
                    } else if ($scope.dataVals.operation === "IS") {
                        $scope.retrieveAvailableIssueStock();
                        $scope.retrievePendingIssuedStock()();
                    } else if ($scope.dataVals.operation === "RT") {
                        $scope.retrieveAvailableReturnStock()();
                    } else if ($scope.dataVals.operation === "RC") {
                        $scope.retrievePendingReceivedStock();
                    } else if ($scope.dataVals.operation === "RQ") {
                        $scope.retrieveAvailableRequestStock();
                    }
                    $scope.loadList();
                });

            };
            $scope.loadList = function() {
                delete $scope.dataVals.userId;
                delete $scope.displayName;
                if ($scope.dataVals.type === "IW") {
                    if ($scope.dataVals.designation !== undefined && $scope.dataVals.designation !== null && $scope.designationList.length !== 0) {
                        $scope.userList = $scope.retrieveUsersByDesignation($scope.dataVals.designation);
                    }
                    if ($scope.designationList.length === 0) {
                        IssueReceiveStockService.retrieveDesignationByDept($rootScope.session.department, function(res) {
                            if (res !== undefined && res !== null && res.length > 0) {
                                $scope.designationList = res;
                                $scope.dataVals.designation = res[0].value.toString();
                                $scope.userList = $scope.retrieveUsersByDesignation($scope.dataVals.designation);
                            }
                        });
                    }
                    $scope.medium = $scope.configuredMediums[$scope.configuredMediums.length - 1];
                }
                if ($scope.dataVals.type === "OW") {
                    if ($scope.dataVals.department !== undefined && $scope.dataVals.department !== null && $scope.associatedDeptList.length !== 0) {
                        $scope.userList = retrieveUsersByDepartment($scope.dataVals.department);
                    }
                    if ($scope.associatedDeptList.length === 0) {
                        IssueReceiveStockService.retrieveAssociatedDepartments(null, function(res) {
                            if (res !== undefined && res !== null && res.length > 0) {
                                var i = res.length;
                                while (i--) {
                                    if ($scope.configuredMediums.indexOf(res[i].commonId) === -1) {
                                        res.splice(i, 1);
                                    }
                                }
                                $scope.associatedDeptList = res;
                                if (res !== undefined && res !== null && res.length > 0) {
                                    $scope.dataVals.department = res[0].value;
                                    $scope.userList = retrieveUsersByDepartment($scope.dataVals.department);
                                    $scope.medium = res[0].commonId;
                                }
                            }
                        });
                    } else {
                        $scope.dataVals.department = $scope.associatedDeptList[0].value;
                        $scope.medium = $scope.associatedDeptList[0].commonId;
                    }
                }
            };
            $scope.setMedium = function(dept) {
                $scope.userList = retrieveUsersByDepartment(dept.value);
                $scope.payload.assignee = "";
                $scope.issueReceiveForm.$setPristine();
                $scope.medium = dept.commonId;
                delete $scope.selectedUserDropdown;
                delete $scope.displayName;
            };
            function retrievedetails(type, parcel, lot, packet) {
                if (parcel === undefined || parcel === null) {
                    parcel = angular.copy($scope.payload.parcel);
                }
                if (lot === undefined || lot === null) {
                    lot = angular.copy($scope.payload.lot);
                }
                if (packet === undefined || packet === null) {
                    packet = angular.copy($scope.payload.packet);
                }

                if (($scope.medium === "Rough" && parcel !== null & parcel !== undefined && parcel !== "")
                        || ($scope.medium === "Lot" && lot !== null && lot !== undefined && lot !== "")
                        || ($scope.medium === "Packet" && packet !== null && packet !== undefined && packet !== "")) {

                    if (($scope.medium === "Rough" && stockNumbers.indexOf(parcel) === -1) ||
                            ($scope.medium === "Lot" && stockNumbers.indexOf(lot) === -1) ||
                            ($scope.medium === "Packet" && stockNumbers.indexOf(packet) === -1)) {

                        var payload = {};
                        var flg = true;
                        if (type === "Rough") {
                            if (parcel === null || parcel === undefined || parcel === "") {
                                flg = false;
                            } else {
                                payload["Parcel"] = parcel;
                            }
                        } else if (type === "Lot") {
                            if (lot === null || lot === undefined || lot === "") {
                                flg = false;
                            } else {
                                payload["Lot"] = lot;
                            }
                        } else if (type === "Packet") {
                            if (packet === null || packet === undefined || packet === "") {
                                flg = false;
                            } else {
                                payload["Packet"] = packet;
                            }
                        }

                        if (flg) {
                            payload["req_type"] = $scope.dataVals.operation;
                            $rootScope.maskLoading();
                            IssueReceiveStockService.retrieveStockById(payload, function(result) {
                                $rootScope.unMaskLoading();
                                if (result !== undefined && result !== null) {
                                    if (result["Message"] === null || result["Message"] === undefined) {
                                        if (type === "Rough") {
                                            if (result.Invoice !== undefined && result.Invoice !== null) {
                                                $scope.payload.invoice = result.Invoice["invoiceNumber"];
                                                $scope.payload.invoiceId = result.Invoice["invoiceId"];
                                            }
                                            if (result.Parcel !== undefined && result.Parcel !== null) {
                                                $scope.payload.parcel = result.Parcel["parcelNumber"];
                                                $scope.payload.parcelId = result.Parcel["parcelId"];
                                                stockNumbers.push(angular.copy(result.Parcel["parcelNumber"]));
                                            }
                                            $scope.payload.issueCarat = result.Parcel["issueCarat"];
                                            $scope.payload.issuePcs = result.Parcel["issuePcs"];
                                        } else if (type === "Lot") {
                                            if (result.Invoice !== undefined && result.Invoice !== null) {
                                                $scope.payload.invoice = result.Invoice["invoiceNumber"];
                                                $scope.payload.invoiceId = result.Invoice["invoiceId"];
                                            }
                                            if (result.Parcel !== undefined && result.Parcel !== null) {
                                                $scope.payload.parcel = result.Parcel["parcelNumber"];
                                                $scope.payload.parcelId = result.Parcel["parcelId"];
                                            }
                                            if (result.Lot !== undefined && result.Lot !== null) {
                                                $scope.payload.lot = result.Lot["lotNumber"];
                                                $scope.payload.lotId = result.Lot["lotId"];
                                                stockNumbers.push(angular.copy(result.Lot["lotNumber"]));
                                            }
                                            $scope.payload.issueCarat = result.Lot["issueCarat"];
                                            $scope.payload.issuePcs = result.Lot["issuePcs"];
                                        } else if (type === "Packet") {
                                            if (result.Invoice !== undefined && result.Invoice !== null) {
                                                $scope.payload.invoice = result.Invoice["invoiceNumber"];
                                                $scope.payload.invoiceId = result.Invoice["invoiceId"];
                                            }
                                            if (result.Parcel !== undefined && result.Parcel !== null) {
                                                $scope.payload.parcel = result.Parcel["parcelNumber"];
                                                $scope.payload.parcelId = result.Parcel["parcelId"];
                                            }
                                            if (result.Lot !== undefined && result.Lot !== null) {
                                                $scope.payload.lot = result.Lot["lotNumber"];
                                                $scope.payload.lotId = result.Lot["lotId"];
                                            }
                                            if (result.Packet !== undefined && result.Packet !== null) {
                                                $scope.payload.packet = result.Packet["packetNumber"];
                                                $scope.payload.packetId = result.Packet["packetId"];
                                                stockNumbers.push(angular.copy(result.Packet["packetNumber"]));
                                            }
                                            $scope.payload.issueCarat = result.Packet["issueCarat"];
                                            $scope.payload.issuePcs = result.Packet["issuePcs"];
                                        }
                                        addRequest();
                                    } else {
                                        $rootScope.addMessage(result["Message"]["Message"], 1);
                                        $scope.payload.parcel = "";
                                        $scope.payload.lot = "";
                                        $scope.payload.packet = "";
                                    }
                                }
                            }, function() {
                                $rootScope.unMaskLoading();
                            });
                        }
                    } else {
                        $rootScope.addMessage("Stock is already added", 1);
                        clearForm();
                    }
                }
            }

            function clearForm() {
                $scope.payload = {slipnoDate: new Date(), "selectedFranchise": $rootScope.session.companyId};
                $scope.submitted = false;
            }

            $scope.saveAll = function() {
                $scope.submitted = true;
//                if (form.$valid) {
                $scope.stockDtls = [];
                if ($scope.selectorIdsForRequest !== null && $scope.selectorIdsForRequest !== undefined && $scope.selectorIdsForRequest.length > 0) {
                    angular.forEach($scope.availableRequestStock, function(item) {
                        if ($scope.selectorIdsForRequest.indexOf(item.selectorId) !== -1) {
                            item.srcDeptId = $rootScope.session.department;
                            delete item.toRequest;
                            delete item.selectorId;
                            $scope.stockDtls.push(item);
                            item.type = "Request";
                            item.requestType = $scope.dataVals.operation;
                            if ($scope.dataVals.type !== undefined && $scope.dataVals.type !== null) {
                                if ($scope.dataVals.type === "OW") {
                                    item.destDeptId = $scope.dataVals.department;
                                    item.stockDeptId = parseInt($scope.associatedDeptList[0].label);
                                } else {
                                    item.destDeptId = item.srcDeptId;
                                    item.destinationDesignationId = $scope.dataVals.designation;
                                }
                            }
                            item.isActive = true;
                            item.modifier = $scope.dataVals.type;
                            if ($scope.dataVals.operation === "RQ") {
                                var sectionData = [];
                                sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                                var success = function(result) {
                                    item.fieldValue = angular.copy($scope.generalCustom);
                                    item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                    item.fieldDbType = angular.copy($scope.fieldDbType);
                                    $scope.stockDtls.push(angular.copy(item));
                                    reset();
                                };
                                DynamicFormService.convertorForCustomField(sectionData, success);

                            }
                        }
                    });
                }
                if ($scope.stockDtls.length > 0) {
                    angular.forEach($scope.stockDtls, function(item) {
                        delete item.fieldValueForUi;
                    });
                    IssueReceiveStockService.saveAll($scope.stockDtls, function(res) {
                        clearForm();
                        requestArray = [];
                        $scope.selectorIdsForRequest = [];
                        stockToIdsForRequest = {};
                        prepareTreeForRequest();
                        stockNumbers = [];
                        $scope.stockDtls = [];
                        $scope.retrieveAvailableRequestStock();
                        var msg = "Stock requested successfully";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                        resetStockIds();
                    });
                }
            };
            $scope.tabChanged = function(key) {
                resetStockIds();
                $scope.payload.parcel = [];
                $scope.dataVals.roughFlag = false;
                $timeout(function() {
                    $scope.dataVals.roughFlag = true;
                });
                $scope.payload.lot = [];
                $scope.dataVals.lotFlag = false;
                $timeout(function() {
                    $scope.dataVals.lotFlag = true;
                });
                $scope.payload.packet = [];
                $scope.dataVals.packetFlag = false;
                $timeout(function() {
                    $scope.dataVals.packetFlag = true;
                });
                clearForm();
                $scope.submitted = false;
                $scope.dataVals.operation = key;
                stockNumbers = [];
                issueArray = [];
                $scope.receiveArray = [];
                $scope.IIStockDtls = [];
                $scope.stockDtls = [];
                $scope.directReceiveArray = [];
                delete $scope.destinationDeptIdForIssue;
                collectArray = [];
                if ($scope.dataVals.operation === "CL") {
                    $scope.retrieveAvailableCollectStock();
                    $scope.usersForCompany = [];
                    delete $scope.selectedUserDropdown;
                    $scope.isUserInvalid = true;
                    delete $scope.displayName;
                    $scope.treeForCollect = [];
                    stockToIds = {};
                    $scope.selectorIds = [];
                } else if ($scope.dataVals.operation === "IS") {
                    $scope.retrieveAvailableIssueStock();
                    $scope.retrievePendingIssuedStock();
                    $scope.usersForCompany = [];
                    delete $scope.selectedUserDropdown;
                    $scope.isUserInvalid = true;
                    delete $scope.displayName;
                    $scope.treeForIssue = [];
                    stockToIdsForIssue = {};
                    $scope.selectorIdsForIssue = [];
                } else if ($scope.dataVals.operation === "RT") {
                    $scope.retrieveAvailableReturnStock();
                    $scope.treeForReturn = [];
                    stockToIdsForReturn = {};
                    selectorIdsForReturn = [];
                } else if ($scope.dataVals.operation === "RC") {
                    $scope.retrievePendingReceivedStock();
                } else if ($scope.dataVals.operation === "RQ") {
                    $scope.retrieveAvailableRequestStock();
                }
                retrieveFields($scope.dataVals.operation);
            };
            $scope.retrieveAvailableCollectStock = function() {
                collectArray = [];
                $scope.selectorIds = [];
                stockToIds = {};
                prepareTreeForCollect();
                $scope.availableCollectStock = [];
                var payload = {requestType: "CL"};
                IssueReceiveStockService.retrieveStockBySlip(payload, function(res) {
                    if (res !== undefined && res !== null && res.length > 0) {
                        angular.forEach(res, function(item) {
                            if (item.packetNumber !== undefined && item.packetNumber !== null) {
                                item.selectorId = "PAC" + item.packetNumber;
                            } else if (item.lotNumber !== undefined && item.lotNumber !== null) {
                                item.selectorId = "LOT" + item.lotNumber;
                            } else if (item.parcelNumber !== undefined && item.parcelNumber !== null) {
                                item.selectorId = "PAR" + item.parcelNumber;
                            }
                        });
                        angular.forEach(res, function(item) {
                            var sectionData = [];
                            sectionData.push({categoryCustom: angular.copy(item.fieldValue)});
                            var success = function(result) {
                                item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                $scope.availableCollectStock.push(angular.copy(item));
                            };
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        });
                    }
                });
            };
            $scope.retrieveAvailableIssueStock = function() {
                issueArray = [];
                $scope.selectorIdsForIssue = [];
                stockToIdsForIssue = {};
                prepareTreeForIssue();
                delete $scope.destinationDeptIdForIssue;

                $scope.availableIssueStock = [];
                var payload = {requestType: "IS"};
                IssueReceiveStockService.retrieveStockBySlip(payload, function(res) {
                    if (res !== undefined && res !== null && res.length > 0) {
                        angular.forEach(res, function(item) {
                            if (item.packetNumber !== undefined && item.packetNumber !== null) {
                                item.selectorId = "PAC" + item.packetNumber;
                            } else if (item.lotNumber !== undefined && item.lotNumber !== null) {
                                item.selectorId = "LOT" + item.lotNumber;
                            } else if (item.parcelNumber !== undefined && item.parcelNumber !== null) {
                                item.selectorId = "PAR" + item.parcelNumber;
                            }
                        });
//                        $scope.availableIssueStock = angular.copy(res);
                        angular.forEach(res, function(item) {
                            var sectionData = [];
                            sectionData.push({categoryCustom: angular.copy(item.fieldValue)});
                            var success = function(result) {
                                item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                $scope.availableIssueStock.push(angular.copy(item));
                            };
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        });
                    }
                });
            };
            $scope.retrieveAvailableReturnStock = function() {
                $scope.returnArray = [];
                selectorIdsForReturn = [];
                stockToIdsForReturn = {};
                prepareTreeForReturn();

                $scope.availableReturnStock = [];
                var payload = {requestType: "RT"};
                IssueReceiveStockService.retrieveStockBySlip(payload, function(res) {
                    if (res !== undefined && res !== null && res.length > 0) {
                        angular.forEach(res, function(item) {
                            if (item.packetNumber !== undefined && item.packetNumber !== null) {
                                item.selectorId = "PAC" + item.packetNumber;
                            } else if (item.lotNumber !== undefined && item.lotNumber !== null) {
                                item.selectorId = "LOT" + item.lotNumber;
                            } else if (item.parcelNumber !== undefined && item.parcelNumber !== null) {
                                item.selectorId = "PAR" + item.parcelNumber;
                            }
                        });
                        $scope.availableReturnStock = angular.copy(res);
                    }
                });
            };
            $scope.retreiveStockBySlip = function() {
                if ($scope.payload.slipnoDate !== null && $scope.payload.slipnoNumber !== undefined && $scope.payload.slipnoNumber !== null && $scope.payload.slipnoNumber !== "") {
                    $rootScope.validations = [];
                    if ($scope.dataVals.operation === "RC") {
                        $scope.receiveArray = [];
                    } else if ($scope.dataVals.operation === "RI") {
                        $scope.directReceiveArray = [];
                    }
                    var data = {"slipDate": $scope.payload.slipnoDate, "slipNo": $scope.payload.slipnoNumber, "requestType": $scope.dataVals.operation};
                    $rootScope.maskLoading();
                    IssueReceiveStockService.retrieveStockBySlip(data, function(result) {
                        $rootScope.unMaskLoading();
                        if (result !== null && result.length > 0) {
                            if ($scope.dataVals.operation === "CL") {
                                collectArray = angular.copy(result);
                            } else if ($scope.dataVals.operation === "RC") {
                                angular.forEach(result, function(item) {
                                    var sectionData = [];
                                    sectionData.push({categoryCustom: angular.copy(item.fieldValue)});
                                    var success = function(result) {
                                        item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                        $scope.receiveArray.push(angular.copy(item));
                                    };
                                    DynamicFormService.convertorForCustomField(sectionData, success);
                                });
//                                $scope.receiveArray = angular.copy(result);
                            } else if ($scope.dataVals.operation === "RI") {
                                angular.forEach(result, function(item) {
                                    item.maxCarat = angular.copy(item.receiveCarat);
                                    item.maxPcs = angular.copy(item.receivePcs);
                                });
                                for (var index = result.length - 1; index >= 0; index--) {
                                    if (result[index].maxCarat === 0 || result[index].maxPcs === 0) {
                                        result.splice(index, 1);
                                    }
                                }
                                if (result.length > 0) {
                                    angular.forEach(result, function(item) {
                                        var sectionData = [];
                                        sectionData.push({categoryCustom: angular.copy(item.fieldValue)});
                                        var success = function(result) {
                                            item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                            $scope.directReceiveArray.push(angular.copy(item));
                                        };
                                        DynamicFormService.convertorForCustomField(sectionData, success);
                                    });
//                                    $scope.directReceiveArray = angular.copy(result);
                                } else {
                                    var msg = "All stock is recieved for this slip number";
                                    var type = $rootScope.failure;
                                    $rootScope.addMessage(msg, type);
                                    clearForm();
                                }
                            }
                        } else {
                            var msg = "No record for specified slip number";
                            var type = $rootScope.failure;
                            $rootScope.addMessage(msg, type);
                            clearForm();
                        }
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                }
            };

            function retrieveUsersByDepartment(department) {
                var payload = {"destDeptId": department};
                $scope.usersForCompany = [];
                IssueReceiveStockService.retrieveUsersByDepartment(payload, function(result) {
                    $scope.usersForCompany = angular.copy(result);
                    $scope.usersForCompanyTemp = angular.copy(result);
                });
            }
            $scope.retrieveUsersByDesignation = function(designation) {
                $scope.payload.assignee = "";
                $scope.issueReceiveForm.$setPristine();
                var map = new Object();
                map["search"] = "";
                map["desgIds"] = designation;
                CenterCustomFieldService.retrieveusersByDesg(map, function(res) {
                    $scope.userList = angular.copy(res);

                });
            };
            function addRequest() {
                var payload = {};

                payload.invoiceId = $scope.payload.invoiceId;
                if ($scope.payload.invoice !== null && $scope.payload.invoice !== undefined && $scope.payload.invoice !== "") {
                    payload.invoiceNumber = $scope.payload.invoice;
                }
                payload.parcelId = $scope.payload.parcelId;
                if ($scope.payload.parcel !== null && $scope.payload.parcel !== undefined && $scope.payload.parcel !== "") {
                    payload.parcelNumber = $scope.payload.parcel;
                }
                payload.lotId = $scope.payload.lotId;
                if ($scope.payload.lot !== null && $scope.payload.lot !== undefined && $scope.payload.lot !== "") {
                    payload.lotNumber = $scope.payload.lot;
                }
                payload.packetId = $scope.payload.packetId;
                if ($scope.payload.packet !== null && $scope.payload.packet !== undefined && $scope.payload.packet !== "") {
                    payload.packetNumber = $scope.payload.packet;
                }
                payload.srcDeptId = $rootScope.session.department;
                if ($scope.dataVals.operation !== undefined && $scope.dataVals.operation !== null) {
                    if ($scope.dataVals.operation === "II") {
                        payload.type = "Issue Inward";
                    } else if ($scope.dataVals.operation === "RI") {
                        payload.type = "Receive Inward";
                    } else {
                        payload.type = "Request";
                    }
                    payload.requestType = $scope.dataVals.operation;
                }
                if ($scope.dataVals.type !== undefined && $scope.dataVals.type !== null) {
                    if ($scope.dataVals.type === "OW") {
                        payload.destDeptId = $scope.dataVals.department;
                        payload.stockDeptId = parseInt($scope.associatedDeptList[0].label);
                    } else {
                        payload.destDeptId = payload.srcDeptId;
                        payload.destinationDesignationId = $scope.dataVals.designation;
                    }
                }
                payload.issueCarat = $scope.payload.issueCarat;
                payload.issuePcs = $scope.payload.issuePcs;
                payload.isActive = true;
                payload.modifier = $scope.dataVals.type;
                if ($scope.dataVals.operation === "RQ") {
                    var sectionData = [];
                    sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                    var success = function(result) {
                        payload.fieldValue = angular.copy($scope.generalCustom);
                        payload.fieldValueForUi = angular.copy(result[0].categoryCustom);
                        payload.fieldDbType = angular.copy($scope.fieldDbType);
                        $scope.stockDtls.push(angular.copy(payload));
                        reset();
                    };
                    DynamicFormService.convertorForCustomField(sectionData, success);

                } else if ($scope.dataVals.operation === "II") {
                    var sectionData = [];
                    sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                    var success = function(result) {
                        payload.fieldValue = angular.copy($scope.generalCustom);
                        payload.fieldValueForUi = angular.copy(result[0].categoryCustom);
                        payload.fieldDbType = angular.copy($scope.fieldDbType);
                        $scope.IIStockDtls.push(angular.copy(payload));
                        reset();
                    };
                    DynamicFormService.convertorForCustomField(sectionData, success);

                }
                var tempAss = angular.copy($scope.payload.assignee);
                clearForm();
                $scope.payload.assignee = tempAss;
                $scope.submitted = false;

            }

            $scope.collect = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    collectArray = [];
                    if ($scope.selectorIds !== null && $scope.selectorIds !== undefined && $scope.selectorIds.length > 0) {
                        angular.forEach($scope.availableCollectStock, function(item) {
                            if ($scope.selectorIds.indexOf(item.selectorId) !== -1) {
                                item.fieldValue = angular.copy($scope.generalCustom);
                                item.fieldDbType = angular.copy($scope.fieldDbType);
                                delete item.toCollect;
                                delete item.selectorId;
                                collectArray.push(item);
                            }
                        });
                    }
                    if (collectArray.length > 0) {
                        $rootScope.maskLoading();
                        IssueReceiveStockService.collect(collectArray, function(res) {
                            $rootScope.unMaskLoading();
                            $scope.retrieveAvailableCollectStock();
                            var msg = "Collected successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            clearForm();
                            collectArray = [];
                            $scope.selectorIds = [];
                            stockToIds = {};
                            stockNumbers = [];
                            prepareTreeForCollect();
                        }, function() {
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.issue = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    issueArray = [];
                    if ($scope.selectorIdsForIssue !== null && $scope.selectorIdsForIssue !== undefined && $scope.selectorIdsForIssue.length > 0) {
                        angular.forEach($scope.availableIssueStock, function(item) {
                            if ($scope.selectorIdsForIssue.indexOf(item.selectorId) !== -1) {
                                delete item.toCollect;
                                delete item.selectorId;
                                issueArray.push(item);
                            }
                        });
                    }
                    if (issueArray !== undefined && issueArray !== null && issueArray.length > 0) {
                        angular.forEach(issueArray, function(item) {
                            item.destFranchiseId = $scope.payload.selectedFranchise;
                            item.issueTo = $scope.dataVals.userId;
                            item.fieldValue = angular.copy($scope.generalCustom);
                            item.fieldDbType = angular.copy($scope.fieldDbType);
                        });
                        $rootScope.maskLoading();
                        IssueReceiveStockService.issue(issueArray, function(res) {
                            $rootScope.unMaskLoading();
                            clearForm();
                            issueArray = [];
                            $scope.selectorIdsForIssue = [];
                            stockToIdsForIssue = {};
                            stockNumbers = [];
                            prepareTreeForIssue();
                            $scope.submitted = false;
                            $scope.retrieveAvailableIssueStock();
                        }, function() {
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.receive = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    if ($scope.receiveArray !== undefined && $scope.receiveArray !== null && $scope.receiveArray.length > 0) {
                        $rootScope.maskLoading();
                        angular.forEach($scope.receiveArray, function(item) {
                            item.fieldValue = angular.copy($scope.generalCustom);
                            item.fieldDbType = angular.copy($scope.fieldDbType);
                        });
                        IssueReceiveStockService.receive($scope.receiveArray, function(res) {
                            $rootScope.unMaskLoading();
                            var msg = "Received successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            clearForm();
                            $scope.receiveArray = [];
                            $scope.retrievePendingReceivedStock();
                        }, function() {
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.cancel = function() {
                stockNumbers = [];
                clearForm();
                if ($scope.dataVals.operation === "RQ") {
                    $scope.stockDtls = [];
                } else if ($scope.dataVals.operation === "CL") {
                    collectArray = [];
                    $scope.selectorIds = [];
                    stockToIds = {};
                    prepareTreeForCollect();
                    $scope.retrieveAvailableCollectStock();
                } else if ($scope.dataVals.operation === "IS") {
                    issueArray = [];
                    $scope.selectorIdsForIssue = [];
                    stockToIdsForIssue = {};
                    prepareTreeForIssue();
                    $scope.retrieveAvailableIssueStock();
                    delete $scope.destinationDeptIdForIssue;
                } else if ($scope.dataVals.operation === "II") {
                    $scope.IIStockDtls = [];
                } else if ($scope.dataVals.operation === "RI") {
                    $scope.directReceiveArray = [];
                } else if ($scope.dataVals.operation === "RC") {
                    $scope.receiveArray = [];
                } else if ($scope.dataVals.operation === "RT") {
                    $scope.returnArray = [];
                    selectorIdsForReturn = [];
                    stockToIdsForReturn = {};
                    prepareTreeForReturn();
                    $scope.retrieveAvailableReturnStock();
                }
            };

            $scope.issueInward = function(form) {
                $scope.submitted = true;
                if (form.$valid && $scope.dataVals.userId !== null && $scope.dataVals.userId !== undefined) {
                    angular.forEach($scope.IIStockDtls, function(item) {
                        delete item.fieldValueForUi;
                        item.issueTo = $scope.dataVals.userId;
                        if ($scope.dataVals.type !== undefined && $scope.dataVals.type !== null) {
                            if ($scope.dataVals.type === "OW") {
                                item.destDeptId = $scope.dataVals.department;
                            } else {
                                item.destDeptId = item.srcDeptId;
                                item.destinationDesignationId = $scope.dataVals.designation;
                            }
                        }
                    });
                    $rootScope.maskLoading();
                    IssueReceiveStockService.issueInward($scope.IIStockDtls, function() {
                        $rootScope.unMaskLoading();
                        clearForm();
                        $scope.IIStockDtls = [];
                        stockNumbers = [];
                        delete $scope.dataVals.userId;
                        delete $scope.displayName;
                        form.$setPristine();
                        resetStockIds();
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.receiveInward = function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    if ($scope.directReceiveArray !== undefined && $scope.directReceiveArray !== null && $scope.directReceiveArray.length > 0) {
                        angular.forEach($scope.directReceiveArray, function(item) {
                            item.fieldValue = $scope.generalCustom;
                            item.fieldDbType = $scope.fieldDbType;
                            delete item.maxCarat;
                            delete item.maxPcs;
                        });
                        $rootScope.maskLoading();
                        IssueReceiveStockService.receiveInward($scope.directReceiveArray, function() {
                            $rootScope.unMaskLoading();
                            var msg = "Received successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            clearForm();
                            $scope.directReceiveArray = [];
                            form.$setPristine();
                        }, function() {
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.onSubmitFunc = function(e, Type, form) {
                $scope.submitted = true;
                if (form.$valid) {

                    if (e.keyCode === 13 || e.keyCode === 9) {
                        $rootScope.validations = [];
                        if ($scope.dataVals.type === "IW" || ($scope.dataVals.type === "OW" && $scope.medium === Type)) {
                            if (Type === "Parcel") {
                                angular.element($("#parcel")).blur();
                            } else if (Type === "Lot") {
                                angular.element($("#lot")).blur();
                            } else if (Type === "Packet") {
                                angular.element($("#packet")).blur();
                            }
                            if ($scope.dataVals.type === "IW") {
                                if ($scope.payload.parcel !== undefined && $scope.payload.parcel !== null && $scope.payload.parcel !== "") {

                                    $scope.medium = "Rough";
                                    retrievedetails("Rough");
                                } else if ($scope.payload.lot !== undefined && $scope.payload.lot !== null && $scope.payload.lot !== "") {

                                    $scope.medium = "Lot";
                                    retrievedetails("Lot");
                                } else if ($scope.payload.packet !== undefined && $scope.payload.packet !== null && $scope.payload.packet !== "") {

                                    $scope.medium = "Packet";
                                    retrievedetails("Packet");
                                }
                            } else {
                                retrievedetails(Type);
                            }
                            $timeout(function() {
                                if (Type === "Parcel") {
                                    angular.element($("#parcel")).focus();
                                } else if (Type === "Lot" || Type === "Packet") {
                                    angular.element($("#lot")).focus();
                                }

                            });
                        }
                    }
                }
            };
            $scope.onSubmitFuncSlipNo = function(e) {
                $scope.submitted = true;
                if (e.keyCode === 13 || e.keyCode === 9) {
                    $rootScope.validations = [];
                    angular.element($("#slipnoNumber")).blur();
                    $scope.retreiveStockBySlip();
                    $timeout(function() {
                        angular.element($("#slipnoNumber")).focus();
                    });
                }
            };

            $scope.onSubmitFuncCollect = function(e, type) {
                var myMedium = "";
                if ($scope.configuredMediums.indexOf("Packet") !== -1) {
                    myMedium = "Packet";
                } else if ($scope.configuredMediums.indexOf("Lot") !== -1) {
                    myMedium = "Lot";
                } else if ($scope.configuredMediums.indexOf("Rough") !== -1) {
                    myMedium = "Rough";
                }

                if (e.keyCode === 13 || e.keyCode === 9) {
                    $rootScope.validations = [];
                    if (myMedium === type) {
                        retrieveStockForCollect();
                    }
                }
            };

            function retrieveStockForCollect() {

                var payload = {};
                var flg = true;
                var number = "";
                if ($scope.payload.parcel !== null && $scope.payload.parcel !== undefined && $scope.payload.parcel !== "") {
                    flg = false;
                    payload["Parcel"] = $scope.payload.parcel;
                    number = "PAR" + $scope.payload.parcel;
                } else if ($scope.payload.lot !== null && $scope.payload.lot !== undefined && $scope.payload.lot !== "") {
                    if ($scope.payload.packet !== null && $scope.payload.packet !== undefined && $scope.payload.packet !== "") {
                        flg = false;
                        payload["Packet"] = $scope.payload.packet;
                        number = "PAC" + $scope.payload.packet;
                    } else {
                        flg = false;
                        payload["Lot"] = $scope.payload.lot;
                        number = "LOT" + $scope.payload.lot;
                    }
                }
                if (!flg) {
                    if ($scope.dataVals.operation === "CL") {
                        if ($scope.selectorIds.indexOf(number) === -1) {
                            payload["req_type"] = $scope.dataVals.operation;
                            var flag = false;
                            angular.forEach($scope.availableCollectStock, function(item) {
                                if (flag === false && number === item.selectorId) {
                                    item.toCollect = true;
                                    flag = true;
                                    var sectionData = [];
                                    sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                                    var success = function(result) {
                                        item.fieldValue = angular.copy($scope.generalCustom);
//                                        item.fieldValueForUi = angular.copy(result[0].categoryCustom);
                                        item.fieldDbType = angular.copy($scope.fieldDbType);
                                        reset();
                                    };
                                    DynamicFormService.convertorForCustomField(sectionData, success);
                                }
                            });
                            if (flag === true) {
                                if (payload["Parcel"] !== undefined && payload["Parcel"] !== undefined) {
                                    if (stockToIds["Parcels"] === undefined || stockToIds["Parcels"] === null) {
                                        stockToIds["Parcels"] = [];
                                    }
                                    stockToIds["Parcels"].push(payload.Parcel);
                                    $scope.selectorIds.push("PAR" + payload.Parcel);
                                } else if (payload["Lot"] !== undefined && payload["Lot"] !== undefined) {
                                    if (stockToIds["Lots"] === undefined || stockToIds["Lots"] === null) {
                                        stockToIds["Lots"] = [];
                                    }

                                    stockToIds["Lots"].push(payload.Lot);
                                    $scope.selectorIds.push("LOT" + payload.Lot);
                                } else if (payload["Packet"] !== undefined && payload["Packet"] !== undefined) {
                                    if (stockToIds["Packets"] === undefined || stockToIds["Packets"] === null) {
                                        stockToIds["Packets"] = [];
                                    }
                                    stockToIds["Packets"].push(payload.Packet);
                                    $scope.selectorIds.push("PAC" + payload.Packet);
                                }
                                prepareTreeForCollect();
                            } else {
                                var type1 = $rootScope.failure;
                                $rootScope.addMessage("No stock available", type1);
                            }
                        } else {
                            var msg = "Stock already added";
                            var type1 = $rootScope.failure;
                            $rootScope.addMessage(msg, type1);
                        }
                        clearForm();
                    } else if ($scope.dataVals.operation === "IS") {
                        if ($scope.selectorIdsForIssue.indexOf(number) === -1) {
                            payload["req_type"] = $scope.dataVals.operation;
                            var flag = false;
                            var matchedItem = {};
                            angular.forEach($scope.availableIssueStock, function(item) {
                                if (flag === false && number === item.selectorId) {
                                    item.toCollect = true;
                                    matchedItem = angular.copy(item);
                                    var sectionData = [];
                                    sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                                    var success = function(result) {
                                        item.fieldValue = angular.copy($scope.generalCustom);
                                        item.fieldDbType = angular.copy($scope.fieldDbType);
                                        reset();
                                    };
                                    DynamicFormService.convertorForCustomField(sectionData, success);
                                    flag = true;
                                }
                            });
                            if (flag === true) {
                                if ($scope.selectorIdsForIssue === undefined || $scope.selectorIdsForIssue === null || $scope.selectorIdsForIssue.length === 0) {
                                    $scope.destinationDeptIdForIssue = matchedItem.destDeptId;
                                    retrieveUsersByDepartment($scope.destinationDeptIdForIssue);
                                }
                                if ($scope.selectorIdsForIssue.length > 0 && $scope.destinationDeptIdForIssue !== matchedItem.destDeptId) {
                                    flag = false;
                                }
                                if (flag) {
                                    if (payload["Parcel"] !== undefined && payload["Parcel"] !== undefined) {
                                        if (stockToIdsForIssue["Parcels"] === undefined || stockToIdsForIssue["Parcels"] === null) {
                                            stockToIdsForIssue["Parcels"] = [];
                                        }
                                        stockToIdsForIssue["Parcels"].push(payload.Parcel);
                                        $scope.selectorIdsForIssue.push("PAR" + payload.Parcel);
                                    } else if (payload["Lot"] !== undefined && payload["Lot"] !== undefined) {
                                        if (stockToIdsForIssue["Lots"] === undefined || stockToIdsForIssue["Lots"] === null) {
                                            stockToIdsForIssue["Lots"] = [];
                                        }

                                        stockToIdsForIssue["Lots"].push(payload.Lot);
                                        $scope.selectorIdsForIssue.push("LOT" + payload.Lot);
                                    } else if (payload["Packet"] !== undefined && payload["Packet"] !== undefined) {
                                        if (stockToIdsForIssue["Packets"] === undefined || stockToIdsForIssue["Packets"] === null) {
                                            stockToIdsForIssue["Packets"] = [];
                                        }
                                        stockToIdsForIssue["Packets"].push(payload.Packet);
                                        $scope.selectorIdsForIssue.push("PAC" + payload.Packet);
                                    }
                                    prepareTreeForIssue();
                                } else {
                                    var msg = "Destination department should be equal of all stock";
                                    var type1 = $rootScope.failure;
                                    $rootScope.addMessage(msg, type1);
                                }
                            } else {
                                var type1 = $rootScope.failure;
                                $rootScope.addMessage("No stock available", type1);
                            }
                        } else {
                            var msg = "Stock already added";
                            var type1 = $rootScope.failure;
                            $rootScope.addMessage(msg, type1);
                        }
                        clearForm();
                    } else if ($scope.dataVals.operation === "RT") {
                        if (selectorIdsForReturn.indexOf(number) === -1) {
                            payload["req_type"] = $scope.dataVals.operation;
                            var flag = false;
                            var matchedItem = {};
                            angular.forEach($scope.availableReturnStock, function(item) {
                                if (number === item.selectorId) {
                                    item.toCollect = true;
                                    matchedItem = angular.copy(item);
                                    flag = true;
                                }
                            });
                            if (flag === true) {
                                if (flag) {
                                    if (payload["Parcel"] !== undefined && payload["Parcel"] !== undefined) {
                                        if (stockToIdsForReturn["Parcels"] === undefined || stockToIdsForReturn["Parcels"] === null) {
                                            stockToIdsForReturn["Parcels"] = [];
                                        }
                                        stockToIdsForReturn["Parcels"].push(payload.Parcel);
                                        selectorIdsForReturn.push("PAR" + payload.Parcel);
                                    } else if (payload["Lot"] !== undefined && payload["Lot"] !== undefined) {
                                        if (stockToIdsForReturn["Lots"] === undefined || stockToIdsForReturn["Lots"] === null) {
                                            stockToIdsForReturn["Lots"] = [];
                                        }

                                        stockToIdsForReturn["Lots"].push(payload.Lot);
                                        selectorIdsForReturn.push("LOT" + payload.Lot);
                                    } else if (payload["Packet"] !== undefined && payload["Packet"] !== undefined) {
                                        if (stockToIdsForReturn["Packets"] === undefined || stockToIdsForReturn["Packets"] === null) {
                                            stockToIdsForReturn["Packets"] = [];
                                        }
                                        stockToIdsForReturn["Packets"].push(payload.Packet);
                                        selectorIdsForReturn.push("PAC" + payload.Packet);
                                    }
                                    prepareTreeForReturn();
                                }
                            } else {
                                var type1 = $rootScope.failure;
                                $rootScope.addMessage("No stock available", type1);
                            }
                        } else {
                            var msg = "Stock already added";
                            var type1 = $rootScope.failure;
                            $rootScope.addMessage(msg, type1);
                        }
                        clearForm();
                    }
                }
            }

            $scope.addOrRemoveForCollect = function(stock) {
                if (stock.toCollect === true) {
                    $scope.selectorIds.splice($scope.selectorIds.indexOf(stock.selectorId), 1);
                    if (stock.selectorId.substring(0, 3) === "PAR") {
                        stockToIds["Parcels"].splice(stockToIds["Parcels"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "LOT") {
                        stockToIds["Lots"].splice(stockToIds["Lots"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "PAC") {
                        stockToIds["Packets"].splice(stockToIds["Packets"].indexOf(stock.selectorId.substring(3)), 1);
                    }
                    prepareTreeForCollect();
                } else {
                    var flag = false;
                    angular.forEach($scope.availableCollectStock, function(item) {
                        if (flag === false && stock.selectorId === item.selectorId) {
                            item.toCollect = true;
                            var sectionData = [];
                            sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                            var success = function(result) {
                                item.fieldValue = angular.copy($scope.generalCustom);
                                item.fieldDbType = angular.copy($scope.fieldDbType);
//                                reset();
                            };
                            DynamicFormService.convertorForCustomField(sectionData, success);
                            flag = true;
                        }
                    });
                    if (flag === true) {
                        if (stock.packetNumber !== undefined && stock.packetNumber !== null) {
                            if (stockToIds["Packets"] === undefined || stockToIds["Packets"] === null) {
                                stockToIds["Packets"] = [];
                            }
                            stockToIds["Packets"].push(stock.packetNumber);
                            $scope.selectorIds.push("PAC" + stock.packetNumber);
                        } else if (stock.lotNumber !== undefined && stock.lotNumber !== null) {
                            if (stockToIds["Lots"] === undefined || stockToIds["Lots"] === null) {
                                stockToIds["Lots"] = [];
                            }
                            stockToIds["Lots"].push(stock.lotNumber);
                            $scope.selectorIds.push("LOT" + stock.lotNumber);
                        } else if (stock.parcelNumber !== undefined && stock.parcelNumber !== null) {
                            if (stockToIds["Parcels"] === undefined || stockToIds["Parcels"] === null) {
                                stockToIds["Parcels"] = [];
                            }

                            stockToIds["Parcels"].push(stock.parcelNumber);
                            $scope.selectorIds.push("PAR" + stock.parcelNumber);
                        }
                        prepareTreeForCollect();
                    } else {
                        var type1 = $rootScope.failure;
                        $rootScope.addMessage("No stock available", type1);
                    }
                }
            };
            $scope.addOrRemoveForIssue = function(stock) {
                if (stock.toCollect === true) {
                    $scope.selectorIdsForIssue.splice($scope.selectorIdsForIssue.indexOf(stock.selectorId), 1);
                    if (stock.selectorId.substring(0, 3) === "PAR") {
                        stockToIdsForIssue["Parcels"].splice(stockToIdsForIssue["Parcels"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "LOT") {
                        stockToIdsForIssue["Lots"].splice(stockToIdsForIssue["Lots"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "PAC") {
                        stockToIdsForIssue["Packets"].splice(stockToIdsForIssue["Packets"].indexOf(stock.selectorId.substring(3)), 1);
                    }
                    prepareTreeForIssue();
                } else {
                    var flag = false;
                    var matchedItem = {};
                    angular.forEach($scope.availableIssueStock, function(item) {
                        if (flag === false && stock.selectorId === item.selectorId) {
                            item.toCollect = true;
                            flag = true;
                            matchedItem = angular.copy(item);
                            var sectionData = [];
                            sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                            var success = function(result) {
                                item.fieldValue = angular.copy($scope.generalCustom);
                                item.fieldDbType = angular.copy($scope.fieldDbType);
//                                reset();
                            };
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        }
                    });
                    if (flag === true) {
                        if ($scope.selectorIdsForIssue === undefined || $scope.selectorIdsForIssue === null || $scope.selectorIdsForIssue.length === 0) {
                            $scope.destinationDeptIdForIssue = matchedItem.destDeptId;
                            retrieveUsersByDepartment($scope.destinationDeptIdForIssue);
                        }
                        if ($scope.selectorIdsForIssue.length > 0 && $scope.destinationDeptIdForIssue !== matchedItem.destDeptId) {
                            flag = false;
                        }
                        if (flag) {
                            if (stock.packetNumber !== undefined && stock.packetNumber !== null) {
                                if (stockToIdsForIssue["Packets"] === undefined || stockToIdsForIssue["Packets"] === null) {
                                    stockToIdsForIssue["Packets"] = [];
                                }
                                stockToIdsForIssue["Packets"].push(stock.packetNumber);
                                $scope.selectorIdsForIssue.push("PAC" + stock.packetNumber);
                            } else if (stock.lotNumber !== undefined && stock.lotNumber !== null) {
                                if (stockToIdsForIssue["Lots"] === undefined || stockToIdsForIssue["Lots"] === null) {
                                    stockToIdsForIssue["Lots"] = [];
                                }
                                stockToIdsForIssue["Lots"].push(stock.lotNumber);
                                $scope.selectorIdsForIssue.push("LOT" + stock.lotNumber);
                            } else if (stock.parcelNumber !== undefined && stock.parcelNumber !== null) {
                                if (stockToIdsForIssue["Parcels"] === undefined || stockToIdsForIssue["Parcels"] === null) {
                                    stockToIdsForIssue["Parcels"] = [];
                                }

                                stockToIdsForIssue["Parcels"].push(stock.parcelNumber);
                                $scope.selectorIdsForIssue.push("PAR" + stock.parcelNumber);
                            }
                            prepareTreeForIssue();
                        }
                    } else {
                        var type1 = $rootScope.failure;
                        $rootScope.addMessage("No stock available", type1);
                    }
                }
            };
            $scope.addOrRemoveForRequest = function(stock) {
                if (stock.toRequest === true) {
                    $scope.selectorIdsForRequest.splice($scope.selectorIdsForRequest.indexOf(stock.selectorId), 1);
                    if (stock.selectorId.substring(0, 3) === "PAR") {
                        stockToIdsForRequest["Parcels"].splice(stockToIdsForRequest["Parcels"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "LOT") {
                        stockToIdsForRequest["Lots"].splice(stockToIdsForRequest["Lots"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "PAC") {
                        stockToIdsForRequest["Packets"].splice(stockToIdsForRequest["Packets"].indexOf(stock.selectorId.substring(3)), 1);
                    }
                    prepareTreeForRequest();
                } else {
                    var flag = false;
                    var matchedItem = {};
                    angular.forEach($scope.availableRequestStock, function(item) {
                        if (flag === false && stock.selectorId === item.selectorId) {
                            item.toRequest = true;
                            flag = true;
                            matchedItem = angular.copy(item);
                        }
                    });

                    if (flag === true) {
                        if (stock.packetNumber !== undefined && stock.packetNumber !== null) {
                            if (stockToIdsForRequest["Packets"] === undefined || stockToIdsForRequest["Packets"] === null) {
                                stockToIdsForRequest["Packets"] = [];
                            }
                            stockToIdsForRequest["Packets"].push(stock.packetNumber);
                            $scope.selectorIdsForRequest.push("PAC" + stock.packetNumber);
                        } else if (stock.lotNumber !== undefined && stock.lotNumber !== null) {
                            if (stockToIdsForRequest["Lots"] === undefined || stockToIdsForRequest["Lots"] === null) {
                                stockToIdsForRequest["Lots"] = [];
                            }
                            stockToIdsForRequest["Lots"].push(stock.lotNumber);
                            $scope.selectorIdsForRequest.push("LOT" + stock.lotNumber);
                        } else if (stock.parcelNumber !== undefined && stock.parcelNumber !== null) {
                            if (stockToIdsForRequest["Parcels"] === undefined || stockToIdsForRequest["Parcels"] === null) {
                                stockToIdsForRequest["Parcels"] = [];
                            }

                            stockToIdsForRequest["Parcels"].push(stock.parcelNumber);
                            $scope.selectorIdsForRequest.push("PAR" + stock.parcelNumber);
                        }
                        prepareTreeForRequest();
                    } else {
                        var type1 = $rootScope.failure;
                        $rootScope.addMessage("No stock available", type1);
                    }
                }
            };
            $scope.addOrRemoveForReturn = function(stock) {
                if (stock.toCollect === true) {
                    selectorIdsForReturn.splice(selectorIdsForReturn.indexOf(stock.selectorId), 1);
                    if (stock.selectorId.substring(0, 3) === "PAR") {
                        stockToIdsForReturn["Parcels"].splice(stockToIdsForReturn["Parcels"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "LOT") {
                        stockToIdsForReturn["Lots"].splice(stockToIdsForReturn["Lots"].indexOf(stock.selectorId.substring(3)), 1);
                    } else if (stock.selectorId.substring(0, 3) === "PAC") {
                        stockToIdsForReturn["Packets"].splice(stockToIdsForReturn["Packets"].indexOf(stock.selectorId.substring(3)), 1);
                    }
                    prepareTreeForReturn();
                } else {
                    var flag = false;
                    var matchedItem = {};
                    angular.forEach($scope.availableReturnStock, function(item) {
                        if (stock.selectorId === item.selectorId) {
                            item.toCollect = true;
                            flag = true;
                            matchedItem = angular.copy(item);
                        }
                    });
                    if (flag === true) {

                        if (flag) {
                            if (stock.packetNumber !== undefined && stock.packetNumber !== null) {
                                if (stockToIdsForReturn["Packets"] === undefined || stockToIdsForReturn["Packets"] === null) {
                                    stockToIdsForReturn["Packets"] = [];
                                }
                                stockToIdsForReturn["Packets"].push(stock.packetNumber);
                                selectorIdsForReturn.push("PAC" + stock.packetNumber);
                            } else if (stock.lotNumber !== undefined && stock.lotNumber !== null) {
                                if (stockToIdsForReturn["Lots"] === undefined || stockToIdsForReturn["Lots"] === null) {
                                    stockToIdsForReturn["Lots"] = [];
                                }
                                stockToIdsForReturn["Lots"].push(stock.lotNumber);
                                selectorIdsForReturn.push("LOT" + stock.lotNumber);
                            } else if (stock.parcelNumber !== undefined && stock.parcelNumber !== null) {
                                if (stockToIdsForReturn["Parcels"] === undefined || stockToIdsForReturn["Parcels"] === null) {
                                    stockToIdsForReturn["Parcels"] = [];
                                }

                                stockToIdsForReturn["Parcels"].push(stock.parcelNumber);
                                selectorIdsForReturn.push("PAR" + stock.parcelNumber);
                            }
                            prepareTreeForReturn();
                        }
                    } else {
                        var type1 = $rootScope.failure;
                        $rootScope.addMessage("No stock available", type1);
                    }
                }
            };
            $scope.setSelectedParent = function(selected) {
                $scope.isUserInvalid = true;
                if (!angular.equals(selected, {})) {
                    if (selected.currentNode !== null && selected.currentNode !== undefined && selected.currentNode.children !== null && selected.currentNode.children !== undefined && selected.currentNode.children.length > 0) {
                        delete $scope.selectedUserDropdown;
                        delete $scope.displayName;
                        delete $scope.dataVals.userId;
                        $scope.usersForCompany = angular.copy($scope.usersForCompanyTemp);
                    } else {
                        $scope.isUserInvalid = false;
                        $scope.dataVals.userId = selected.currentNode.id;
                        $scope.displayName = selected.currentNode.displayName;
                    }
                }
            };

            $scope.reject = function() {
                $scope.submitted = true;
                collectArray = [];
                if ($scope.selectorIds !== null && $scope.selectorIds !== undefined && $scope.selectorIds.length > 0) {
                    angular.forEach($scope.availableCollectStock, function(item) {
                        if ($scope.selectorIds.indexOf(item.selectorId) !== -1) {
                            delete item.toCollect;
                            delete item.selectorId;
                            collectArray.push(item);
                        }
                    });
                }
                if (collectArray.length > 0) {
                    $rootScope.maskLoading();
                    IssueReceiveStockService.reject(collectArray, function(res) {
                        $rootScope.unMaskLoading();
                        $scope.retrieveAvailableCollectStock();
                        var msg = "Rejected successfully";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                        clearForm();
                        collectArray = [];
                        $scope.selectorIds = [];
                        stockToIds = {};
                        stockNumbers = [];
                        prepareTreeForCollect();
                    }, function() {
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.returnStock = function() {
                $scope.returnArray = [];
                if (selectorIdsForReturn !== null && selectorIdsForReturn !== undefined && selectorIdsForReturn.length > 0) {
                    angular.forEach($scope.availableReturnStock, function(item) {
                        if (selectorIdsForReturn.indexOf(item.selectorId) !== -1) {
                            delete item.toCollect;
                            delete item.selectorId;
                            $scope.returnArray.push(item);
                        }
                    });
                }
                if ($scope.returnArray !== undefined && $scope.returnArray !== null && $scope.returnArray.length > 0) {
                    angular.forEach($scope.returnArray, function(item) {
                        item.destFranchiseId = $scope.payload.selectedFranchise;
                        item.issueTo = $scope.dataVals.userId;
                    });
                    $rootScope.maskLoading();
                    IssueReceiveStockService.returnStock($scope.returnArray, function(res) {
                        $rootScope.unMaskLoading();
                        var msg = "Returned successfully";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                        clearForm();
                        $scope.returnArray = [];
                        selectorIdsForReturn = [];
                        stockToIdsForReturn = {};
                        stockNumbers = [];
                        prepareTreeForReturn();
                        $scope.submitted = false;
                        $scope.retrieveAvailableReturnStock();
                    }, function() {
                        var msg = "Error while returning stock";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };

            $scope.retrievePendingIssuedStock = function() {
                $scope.pendingIssuedStock = [];
                IssueReceiveStockService.retrievePendingIssued({"requestType": "IS"}, function(res) {
                    $scope.pendingIssuedStock = angular.copy(res);
                });
            };

            $scope.retrievePendingReceivedStock = function() {
                $scope.pendingReceiveStock = [];
                IssueReceiveStockService.retrievePendingIssued({"requestType": "RC"}, function(res) {
                    $scope.pendingReceiveStock = angular.copy(res);
                });
            };

            function retrieveFields(requestType) {
                if (requestType !== undefined && requestType !== null) {
                    $scope.templateFlag = false;
                    $scope.generalCustom = {};
                    if (templatesForRequestType[requestType] === undefined || templatesForRequestType[requestType] === null) {
                        CustomFieldService.retrieveDesignationBasedFieldsBySection(["issueReceive", requestType], function(response) {
                            templatesForRequestType[requestType] = angular.copy(response);
                            $scope.response = angular.copy(response);
                            reset();
                        });
                    } else {
                        $scope.response = angular.copy(templatesForRequestType[requestType]);
                        reset();
                    }
                }
            }

            function reset() {
                $scope.templateFlag = false;
                $scope.generalCustom = {};
                $rootScope.maskLoading();
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("issue");
                templateData.then(function(section) {
                    var generalField = [];
                    Object.keys($scope.response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'Issue') {
                                generalField.push({Issue: itr});
                            }
                        });
                    }, $scope.response);
                    $scope.generalTemplate = section['genralSection'];
                    $scope.generalTemplate = DynamicFormService.retrieveCustomData($scope.generalTemplate, generalField);

                    $timeout(function() {
                        $scope.generalTemplateForView = [];
                        if ($scope.generalTemplate !== null && $scope.generalTemplate !== undefined && $scope.generalTemplate.length > 0) {
                            for (var index = $scope.generalTemplate.length - 1; index >= 0; index--) {
                                var obj = angular.copy($scope.generalTemplate[index]);
                                if (obj.isViewFromDesignation === true) {
                                    $scope.generalTemplateForView.push(obj);
                                    $scope.generalTemplate.splice(index, 1);
                                }
                            }
                        }
                        $scope.templateFlag = true;
                        $rootScope.unMaskLoading();
                    });
                }, function(reason) {
                }, function(update) {
                });
            }

            $scope.franchiseChanged = function() {
                delete $scope.dataVals.userId;
            };

            $scope.autoCompleteRough = {
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select Parcel',
                allowClear: true,
                initSelection: function(element, callback) {
                    var data = [];
                    element.on("change", function(e) {

                        var added = e.added;
                        var removed = e.removed;
                        if (added !== undefined && added !== null) {
                            $scope.payload.parcel = e.added.id;
                            if ($scope.dataVals.operation === "RQ") {
                                var payload = {};
                                payload["Parcel"] = $scope.payload.parcel;
                                var number = "PAR" + $scope.payload.parcel;
                                selectStockForRequest(payload, number);
                            } else {
                                $scope.medium = "Rough";
                                retrievedetails("Rough", e.added.id);
                            }
                        } else if (removed !== undefined && removed !== null) {
                            stockNumbers.splice(stockNumbers.indexOf(removed.id), 1);
                            if ($scope.dataVals.operation === "RQ") {
                                $scope.selectorIdsForRequest.splice($scope.selectorIdsForRequest.indexOf("PAR" + removed.id), 1);
                                stockToIdsForRequest["Parcels"].splice(stockToIdsForRequest["Parcels"].indexOf(removed.id), 1);
                                prepareTreeForRequest();
                            } else if ($scope.dataVals.operation === "II") {
                                for (var index = 0; index < $scope.IIStockDtls.length; index++) {
                                    var item = $scope.IIStockDtls[index];
                                    if (item.parcelNumber === removed.id) {
                                        $scope.IIStockDtls.splice(index, 1);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    callback(data);
                },
                formatResult: function(item) {

                    return item.text;
                },
                formatSelection: function(item) {

                    return item.text;
                },
                ajax: {
                    url: $rootScope.appendAuthToken($rootScope.centerapipath + "customfield/searchautogenerated"),
                    dataType: 'json',
                    data: function(term, page) { // page is the one-based page number tracked by Select2
                        return {
                            q: term, //search term
                            page_limit: 10, // page size
                            page: page - 1, // page numbe
                            field_name: "parcelId$AG$String"
                        };
                    },
                    results: function(data, page) {
                        data = data.data;
                        var more = (page * 10) < data.total;
                        var parcelData = [];
                        angular.forEach(data.records, function(value, key) {
                            parcelData.push({id: value, text: value});
                        });
                        parcelData = $scope.orderByName(parcelData, ['text']);
                        // notice we return the value of more so Select2 knows if more results can be loaded
                        return {results: parcelData, more: more};
                    }
                }
            };
            $scope.autoCompleteLot = {
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select Lot',
                allowClear: true,
                initSelection: function(element, callback) {
                    var data = [];
                    element.on("change", function(e) {
                        var added = e.added;
                        var removed = e.removed;
                        if (added !== undefined && added !== null) {
                            $scope.payload.lot = e.added.id;
                            if ($scope.dataVals.operation === "RQ") {
                                var payload = {};
                                payload["Lot"] = $scope.payload.lot;
                                var number = "LOT" + $scope.payload.lot;
                                selectStockForRequest(payload, number);
                            } else {
                                $scope.medium = "Lot";
                                retrievedetails("Lot", null, e.added.id);
                            }
                        } else if (removed !== undefined && removed !== null) {
                            stockNumbers.splice(stockNumbers.indexOf(removed.id), 1);
                            if ($scope.dataVals.operation === "RQ") {
                                $scope.selectorIdsForRequest.splice($scope.selectorIdsForRequest.indexOf("LOT" + removed.id), 1);
                                stockToIdsForRequest["Lots"].splice(stockToIdsForRequest["Lots"].indexOf(removed.id), 1);
                                prepareTreeForRequest();
                            } else if ($scope.dataVals.operation === "II") {
                                for (var index = 0; index < $scope.IIStockDtls.length; index++) {
                                    var item = $scope.IIStockDtls[index];
                                    if (item.lotNumber === removed.id) {
                                        $scope.IIStockDtls.splice(index, 1);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    callback(data);
                },
                formatResult: function(item) {

                    return item.text;
                },
                formatSelection: function(item) {

                    return item.text;
                },
                ajax: {
                    url: $rootScope.appendAuthToken($rootScope.centerapipath + "customfield/searchautogenerated"),
                    dataType: 'json',
                    data: function(term, page) { // page is the one-based page number tracked by Select2
                        return {
                            q: term, //search term
                            page_limit: 10, // page size
                            page: page - 1, // page numbe
                            field_name: "lotID$AG$String"
                        };
                    },
                    results: function(data, page) {
                        data = data.data;
                        var more = (page * 10) < data.total;
                        var lotData = [];
                        angular.forEach(data.records, function(value, key) {
                            lotData.push({id: value, text: value});
                        });
                        lotData = $scope.orderByName(lotData, ['text']);
                        // notice we return the value of more so Select2 knows if more results can be loaded
                        return {results: lotData, more: more};
                    }
                }
            };
            $scope.autoCompletePacket = {
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select Packet',
                allowClear: true,
                initSelection: function(element, callback) {
                    var data = [];
                    element.on("change", function(e) {
                        var added = e.added;
                        var removed = e.removed;
                        if (added !== undefined && added !== null) {
                            $scope.payload.packet = e.added.id;
                            if ($scope.dataVals.operation === "RQ") {
                                var payload = {};
                                payload["Packet"] = $scope.payload.packet;
                                var number = "PAC" + $scope.payload.packet;
                                selectStockForRequest(payload, number);
                            } else {
                                $scope.medium = "Packet";
                                retrievedetails("Packet", null, null, e.added.id);
                            }
                        } else if (removed !== undefined && removed !== null) {
                            stockNumbers.splice(stockNumbers.indexOf(removed.id), 1);
                            if ($scope.dataVals.operation === "RQ") {
                                $scope.selectorIdsForRequest.splice($scope.selectorIdsForRequest.indexOf("PAC" + removed.id), 1);
                                stockToIdsForRequest["Packets"].splice(stockToIdsForRequest["Packets"].indexOf(removed.id), 1);
                                prepareTreeForRequest();
                            } else if ($scope.dataVals.operation === "II") {
                                for (var index = 0; index < $scope.IIStockDtls.length; index++) {
                                    var item = $scope.IIStockDtls[index];
                                    if (item.packetNumber === removed.id) {
                                        $scope.IIStockDtls.splice(index, 1);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    callback(data);
                },
                formatResult: function(item) {

                    return item.text;
                },
                formatSelection: function(item) {

                    return item.text;
                },
                ajax: {
                    url: $rootScope.appendAuthToken($rootScope.centerapipath + "customfield/searchautogenerated"),
                    dataType: 'json',
                    data: function(term, page) { // page is the one-based page number tracked by Select2
                        return {
                            q: term, //search term
                            page_limit: 10, // page size
                            page: page - 1, // page numbe
                            field_name: "packetID$AG$String"
                        };
                    },
                    results: function(data, page) {
                        data = data.data;
                        var more = (page * 10) < data.total;
                        var packetData = [];
                        angular.forEach(data.records, function(value, key) {
                            packetData.push({id: value, text: value});
                        });
                        packetData = $scope.orderByName(packetData, ['text']);
                        // notice we return the value of more so Select2 knows if more results can be loaded
                        return {results: packetData, more: more};
                    }
                }
            };
            $scope.orderByName = $filter('orderBy');

            $scope.retrieveAvailableRequestStock = function() {
                requestArray = [];
                $scope.selectorIdsForRequest = [];
                stockToIdsForRequest = {};
                prepareTreeForRequest();
                delete $scope.destinationDeptIdForRequest;

                $scope.availableRequestStock = [];
                var payload = {requestType: "RQ"};
                IssueReceiveStockService.retrieveStockBySlip(payload, function(res) {
                    if (res !== undefined && res !== null && res.length > 0) {
                        angular.forEach(res, function(item) {
                            if (item.packetNumber !== undefined && item.packetNumber !== null) {
                                item.selectorId = "PAC" + item.packetNumber;
                            } else if (item.lotNumber !== undefined && item.lotNumber !== null) {
                                item.selectorId = "LOT" + item.lotNumber;
                            } else if (item.parcelNumber !== undefined && item.parcelNumber !== null) {
                                item.selectorId = "PAR" + item.parcelNumber;
                            }
                            $scope.availableRequestStock.push(angular.copy(item));
                        });
                    }
                });
            };

            function selectStockForRequest(payload, number) {

                if ($scope.selectorIdsForRequest.indexOf(number) === -1) {
                    payload["req_type"] = $scope.dataVals.operation;
                    var flag = false;
                    angular.forEach($scope.availableRequestStock, function(item) {
                        if (flag === false && number === item.selectorId) {
                            item.toRequest = true;
                            flag = true;
                            var sectionData = [];
                            sectionData.push({categoryCustom: angular.copy($scope.generalCustom)});
                            var success = function(result) {
                                item.fieldValue = angular.copy($scope.generalCustom);
                                item.fieldDbType = angular.copy($scope.fieldDbType);
                                reset();
                            };
                            DynamicFormService.convertorForCustomField(sectionData, success);
                        }
                    });
                    if (flag === true) {
                        if (payload["Parcel"] !== undefined && payload["Parcel"] !== undefined) {
                            if (stockToIdsForRequest["Parcels"] === undefined || stockToIdsForRequest["Parcels"] === null) {
                                stockToIdsForRequest["Parcels"] = [];
                            }
                            stockToIdsForRequest["Parcels"].push(payload.Parcel);
                            $scope.selectorIdsForRequest.push("PAR" + payload.Parcel);
                        } else if (payload["Lot"] !== undefined && payload["Lot"] !== undefined) {
                            if (stockToIdsForRequest["Lots"] === undefined || stockToIdsForRequest["Lots"] === null) {
                                stockToIdsForRequest["Lots"] = [];
                            }

                            stockToIdsForRequest["Lots"].push(payload.Lot);
                            $scope.selectorIdsForRequest.push("LOT" + payload.Lot);
                        } else if (payload["Packet"] !== undefined && payload["Packet"] !== undefined) {
                            if (stockToIdsForRequest["Packets"] === undefined || stockToIdsForRequest["Packets"] === null) {
                                stockToIdsForRequest["Packets"] = [];
                            }
                            stockToIdsForRequest["Packets"].push(payload.Packet);
                            $scope.selectorIdsForRequest.push("PAC" + payload.Packet);
                        }
                        prepareTreeForRequest();
                    } else {
                        var type1 = $rootScope.failure;
                        $rootScope.addMessage("No stock available", type1);
                    }
                } else {
                    var msg = "Stock already added";
                    var type1 = $rootScope.failure;
                    $rootScope.addMessage(msg, type1);
                }
                clearForm();
            }
            ;

            function resetStockIds() {
                $scope.dataVals.selectedparcel = [];
                $scope.dataVals.selectedlot = [];
                $scope.dataVals.selectedpacket = [];

                $("#parcel").select2("val");
                $("#lot").select2("val");
                $("#packet").select2("val");
            }
            $rootScope.unMaskLoading();
        }]);
});