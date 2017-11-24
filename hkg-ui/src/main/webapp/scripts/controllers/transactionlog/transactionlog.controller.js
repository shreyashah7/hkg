define(['hkg', 'transactionLogService', 'ngload!uiGrid'], function (hkg) {
    hkg.register.controller('TransactionLogController', ["$rootScope", "$scope", "TransactionLogService", function ($rootScope, $scope, TransactionLogService) {

            $scope.entity = "TRANSACTIONLOG.";
            $scope.transactionlog = {};
            $scope.transactionlog.fromDate = null;
            $scope.fromdate = null;
            $scope.transactionlog.toDate = null;
            $scope.todate = null;
            $scope.logRecords = [];
            $scope.statusList = [];
            $scope.transactionlog.status = "";
            $scope.franchiseList = {};
            $scope.isEmptyRecordSet = true;
            $scope.transactionlog.franchise = "";
            $scope.init = true;
            var currentFranchise = "";
            $scope.$on('$viewContentLoaded', function () {

                initializeTransactionLog();
            });
            var initializeTransactionLog = function () {

                retrievePrerequisite();
            };
            var retrievePrerequisite = function () {
                $scope.isEmptyRecordSet = true;
                TransactionLogService.retrievePrerequisite(function (response) {
                    $scope.statusList = response.status;
                    $scope.franchiseList = response.franchise;
                    currentFranchise = response.currentFranchise;
                });
                $scope.gridOptions = {};
                $scope.gridOptions.columnDefs = [];
                $scope.gridOptions.columnDefs.push({name: "transactionId", displayName: "Transaction Id", minWidth: 200});
                $scope.gridOptions.columnDefs.push({name: "receiverJid", displayName: "Receiver", minWidth: 200});
                $scope.gridOptions.columnDefs.push({name: "sentDate", displayName: "Sent Time", cellTemplate: "<span> &nbsp; {{row.entity.sentDate | date:'yyyy-MM-dd HH:mm:ss'}}</span>", minWidth: 200});
                $scope.gridOptions.columnDefs.push({name: "noOfRetry", displayName: "Retries", minWidth: 200});
                $scope.gridOptions.columnDefs.push({name: "status", displayName: "Status", minWidth: 200});
                $scope.gridOptions.columnDefs.push({name: "errorFile", displayName: "Error File", cellTemplate: "<div><button class='btn btn-link' ng-disabled='row.entity.errorFile==null || row.entity.errorFile==undefined' ng-click='grid.appScope.clickHandler(row.entity.errorFile)'> <span class='glyphicon glyphicon-save'></span></button></div>", minWidth: 200});
                $scope.gridOptions.enableFiltering = true;
                $scope.gridOptions.expandableRowTemplate = '<div ui-grid="row.entity.subGridOptions" ui-grid-resize-columns style="height:150px;"></div>';
//                $scope.gridOptions.externalScopes = "clickHandler";
                $scope.gridOptions.expandableRowScope = {
                    subGridVariable: 'subGridScopeVariable'
                };
                $scope.gridOptions.multiSelect = true;
//                $scope.clickHandler = {
//                    onClick: function (value) {
//                        alert('Name: ');
//                    }
//                };
                $scope.gridOptions.enableRowSelection = true;
                $scope.gridOptions.enableSelectAll = true;
//                $scope.gridOptions.enablePaginationControls = true;
                $scope.gridOptions.paginationPageSizes = [10, 20, 30];
                $scope.gridOptions.paginationPageSize = 10;
                $scope.gridOptions.data = [];
                $scope.gridOptions.isRowSelectable = function (row) {
//                    console.log("currentfranchise= " + currentFranchise + "   " + row.entity.senderJid);
                    if (row.entity.status == "Delivered") {
                        return false;
                    } else {
                        return true;
                    }
                };
                $scope.gridOptions.onRegisterApi = function (gridApi) {
                    $scope.gridApi = gridApi;
                };
//                TransactionLogService.retrieveAllTransactionLogs(function (data) {
////                    alert("data: " + data)
//                    $scope.logRecords = data;
//                    console.log("data:: " + JSON.stringify(data));
//
//                    for (i = 0; i < data.length; i++) {
//                        data[i].subGridOptions = {
//                            columnDefs: [{name: "className", displayName: "className"}, {name: "idMap", displayName: "idMap"}, {name: "isSqlEntity", displayName: "isSqlEntity"}],
//                            data: data[i].entityMetadataList
//                        };
//                        if (data[i].entityMetadataList.idMap) {
//                            for (j = 0; j < data[i].entityMetadataList.idMap.length; j++) {
//                                data[i].subGridOptions[j].subMapOptions = {
//                                    columnDefs: [{name: "id", displayName: "id"}, {name: "id", displayName: "id"}],
//                                    data: data[i].entityMetadataList.idMap
//                                }
//                            }
//                        }
//                    }
//                    $scope.gridOptions.data = data;
//                });
            }
            ;
            $scope.clickHandler = function (value) {
                var hiddenElement = document.createElement('a');
                $scope.fileName = value;
                hiddenElement.href = "api/transactionlog/retrieve";
                hiddenElement.target = '_blank';
                hiddenElement.download = value;
                hiddenElement.innerHTML = "abc";
                hiddenElement.click();
            };
            $scope.selectAll = function () {
                $scope.gridApi.selection.selectAllRows();
            };

            $scope.clearAll = function () {
                $scope.gridApi.selection.clearSelectedRows();
            };
            $scope.expandAllRows = function () {
                $scope.gridApi.expandable.expandAllRows();
            }

            $scope.collapseAllRows = function () {
                $scope.gridApi.expandable.collapseAllRows();
            }
            $scope.execute = function () {
                var selectedRows = $scope.gridApi.selection.getSelectedRows();
                TransactionLogService.resend(selectedRows, function () {
                    TransactionLogService.retrieveTransactionLogsBycriteria($scope.transactionlog, function (data) {
//                    alert("data: " + data)
                        $scope.logRecords = data;

                        for (i = 0; i < data.length; i++) {
                            data[i].subGridOptions = {
                                columnDefs: [{name: "className", displayName: "className"}, {name: "isSqlEntity", displayName: "isSqlEntity"}],
                                data: data[i].entityMetadataList
                            }

                        }
                        $scope.gridOptions.data = data;
                    });
                })
            };


            $scope.filterTransactionLog = function () {
                $scope.init = false;

                if ($scope.fromdate) {
                    $scope.transactionlog.fromDate = getDateFromFormat($scope.fromdate, $rootScope.dateFormatWithTime);
                }
                if ($scope.todate) {
                    $scope.transactionlog.toDate = getDateFromFormat($scope.todate, $rootScope.dateFormatWithTime);
                }
                $rootScope.maskLoading();
//                alert(" $scope.transactionlog.fromDate." + $scope.transactionlog.fromDate + "   " + $scope.transactionlog.toDate);
                TransactionLogService.retrieveTransactionLogsBycriteria($scope.transactionlog, function (data) {
                    /**
                     * Do not change to strict type checking i.e ===
                     */
                    if (!data || data.length === 0) {
                        $scope.isEmptyRecordSet = true;
                    } else {
                        $scope.isEmptyRecordSet = false;
                    }
                    $scope.logRecords = data;
                    $scope.gridOptions.data = data;
                    for (i = 0; i < data.length; i++) {
                        data[i].subGridOptions = {
                            columnDefs: [{name: "className", displayName: "className"}, {name: "idMap", displayName: "idMap"}, {name: "isSqlEntity", displayName: "isSqlEntity"}],
                            data: data[i].entityMetadataList
                        };

                        if (data[i].entityMetadataList.idMap) {
                            for (j = 0; j < data[i].entityMetadataList.idMap.length; j++) {
                                data[i].subGridOptions[j].subMapOptions = {
                                    columnDefs: [{name: "id", displayName: "id"}, {name: "id", displayName: "id"}],
                                    data: data[i].entityMetadataList.idMap
                                }
                            }
                        }
                    }
                    $rootScope.unMaskLoading();
                    $scope.gridOptions.data = data;
                }, $rootScope.unMaskLoading());
            };
        }]);
});