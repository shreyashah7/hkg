define(['hkg', 'reportBuilderService'], function (hkg) {
    hkg.register.controller('ViewReport', ["$rootScope", "$scope", "$filter", "ngTableParams", "ReportBuilderService", "$sce", function ($rootScope, $scope, $filter, ngTableParams, ReportBuilderService, $sce) {
            $rootScope.maskLoading();
            $scope.entity = "REPORTBUILDER.";
            $scope.reportList = [];
            $scope.reportColumns = [];
            $scope.dt = {};
            //-----Code For DatePickerDialog
            $scope.open = function ($event, opened) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.dt[opened] = true;
            };
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            $scope.format = $rootScope.dateFormat;
            $scope.today = new Date();
            //-----End Code For DatePickerDialog

            $scope.availabeFilterTypesByDataType = {int8: ["=", "!=", "<", ">", "<=", ">=", "between"], varchar: ["=", "!="], timestamp: ["=", "!=", "<", ">", "between"]};
            $scope.selectFilterColumns = {
                multiple: true,
                placeholder: 'Add Filter',
                data: $scope.reportColumns
            };
            ReportBuilderService.retrieveReportTitles(function (res) {
                $scope.reportList = res;
            }, function () {
                $rootScope.addMessage("Failed to load reports.", $rootScope.failure);
            });
            //Server side pagination
            $scope.previewTableParams = new ngTableParams({
                page: 1, // show first page
                count: 10 // count per page
            }, {
                total: 0, // length of data
                getData: function ($defer, params) {
                    // ajax request to api
                    if ($scope.report !== undefined) {
                        $scope.retrieveParams.count = $scope.previewTableParams.count();
                        $scope.retrieveParams.page = $scope.previewTableParams.page();
                        ReportBuilderService.retrieveReportTable($scope.retrieveParams, function (res) {
                            if (res.queryValid !== undefined) {
                                var msg = "Invalid Query.";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                            } else {
                                $scope.previewData = res.records;
                                $scope.previewTableParams.total(res.totalRecords);
                                $defer.resolve($scope.previewData);
                                $rootScope.unMaskLoading();
                            }
                        }, function () {

                        });
                    }
                }
            });
            $scope.executeQuery = function () {
                $scope.previewData = [];
                $scope.filterColumns = [];
                $scope.reportColumns.splice(0, $scope.reportColumns.length);
                $scope.convertedQuery = undefined;
                if ($scope.report.selectedReport !== null && $scope.report.selectedReport !== '') {
                    $scope.filterColumns = [];
                    console.log("$scope.report.selectedReport--" + JSON.stringify($scope.report.selectedReport));
                    ReportBuilderService.retrieveReport($scope.report.selectedReport, function (res) {
                        $scope.report.selectedReportJSON = res;
                        $scope.reportColumns.splice(0, $scope.reportColumns.length);
                        $scope.selectedFilterColumns = "";
                        $("#reportFilterColumns").select2("data", "");
                        angular.forEach($scope.report.selectedReportJSON.columns, function (col, key) {
                            var sameColumnNameCount = 0;
                            angular.forEach($scope.report.selectedReportJSON.columns, function (colObj, index) {
                                var colNameFirst = col.colName.substring(col.colName.indexOf(".") + 1);
                                var colNameSecond = colObj.colName.substring(colObj.colName.indexOf(".") + 1);
                                if (colNameFirst === colNameSecond) {
                                    sameColumnNameCount++;
                                    if (sameColumnNameCount > 1) {
                                        colObj.joinAlias = colObj.colName.replace(".", "_");
                                        if (colObj.alias === '') {
                                            colObj.alias = colObj.colName;
                                        }
                                    }
                                }
                            });

                            $scope.reportColumns.push({id: key, text: (col.alias === "") ? col.colName.substring(col.colName.indexOf(".") + 1) : col.alias});
                        });
                        if ($scope.report.selectedReportJSON.externalReport) {
                            ReportBuilderService.retrieveReportLink({"reportCode": $scope.report.selectedReportJSON.reportCode, "editable": $scope.report.selectedReportJSON.editable}, function (res) {
                                $scope.externalReportLink = $sce.trustAsResourceUrl(res.reportLink);
                            }, function () {
                            });
                        } else {
                            $scope.retrieveParams = {"query": $scope.report.selectedReportJSON.query, "count": $scope.previewTableParams.count(), 'page': $scope.previewTableParams.page(), saveQuery: false};
                            $scope.previewTableParams.page(1);
                            $scope.previewTableParams.reload();
                        }
                    }, function () {

                    });
                }
            }
            $("#reportFilterColumns").on("select2-selecting", function (e) {
                var col = $scope.report.selectedReportJSON.columns[e.val];
                col.id = e.val;
                col.availableFilterTypes = $scope.availabeFilterTypesByDataType[col.dataType] === undefined ? ["=", "!="] : $scope.availabeFilterTypesByDataType[col.dataType];
                col.availableFilterValues = [];
                if (col.dataType === 'varchar') {
                    col.availableFilterValueSelect = {
                        multiple: true,
                        placeholder: '  Filter Value',
                        closeOnSelect: false,
                        ajax: {
                            url: $rootScope.apipath + "report/retrievelimitedcolumnvalues",
                            dataType: 'json',
                            data: function (term, page) { // page is the one-based page number tracked by Select2
                                return {
                                    q: term, //search term
                                    page_limit: 10, // page size
                                    page: page - 1, // page numbe
                                    col_name: col.colName.substring(col.colName.indexOf(".") + 1)
                                };
                            },
                            results: function (data, page) {
                                data = data.data;
                                var more = (page * 10) < data.total; // whether or not there are more results available
                                var columnData = [];
                                angular.forEach(data.columnValues, function (value, key) {
                                    columnData.push({id: value, text: value});
                                });
                                // notice we return the value of more so Select2 knows if more results can be loaded
                                return {results: columnData, more: more};
                            }
                        }
                    };
                }
                $scope.filterColumns.push(col);
            });
            $("#reportFilterColumns").on("select2-removing", function (e) {
                angular.forEach($scope.filterColumns, function (col, key) {
                    if (col.id === e.val) {
                        $scope.filterColumns.splice(key, 1);
                        col.availableFilterTypes = undefined;
                        col.availableFilterValues = undefined;
                        col.availableFilterValueSelect = undefined;
                        col.filterValFirst = undefined;
                        col.selectedFilterType = undefined;
                        col.filterValSecond = undefined;
                        col.filter = null;

                    }
                    //Clear all the filters
                    if ($scope.filterColumns.length === 0) {
                        $scope.executeQuery();
                    }
                });
            });
            $scope.changeOrder = function (col) {
                if (col.order === undefined || col.order === 'desc') {
                    col.order = 'asc';
                } else {
                    col.order = 'desc';
                }
                var orderByQuery = $scope.report.selectedReportJSON.query;
                var converted = false;
                if ($scope.convertedQuery !== undefined) {
                    orderByQuery = $scope.convertedQuery;
                    converted = true;
                }
                if (orderByQuery.toLowerCase().indexOf("order by") > 0) {
                    orderByQuery = orderByQuery.substring(0, orderByQuery.toLowerCase().indexOf("order by"));
                }
                orderByQuery += ' order by ' + col.colName + ' ' + col.order;
                $scope.retrieveParams = {"query": orderByQuery, "count": $scope.previewTableParams.count(), 'page': $scope.previewTableParams.page(), 'saveQuery': converted};
                $scope.previewTableParams.reload();
                if ($scope.prevCol !== undefined && $scope.prevCol.colName !== col.colName) {
                    $scope.prevCol.order = undefined;
                }
                $scope.prevCol = col;
            };
            $scope.applyFilters = function () {
                $scope.columnsCopy = angular.copy($scope.report.selectedReportJSON.columns);
                angular.forEach($scope.columnsCopy, function (col, index) {

                    if (col.selectedFilterType !== undefined) {
                        //Configure string filters
                        if (col.dataType === 'varchar') {
                            var filterVal = col.filterValFirst.replace(/,/gi, "','");
                            col.filter = " IN ('" + filterVal + "')";
                            if (col.selectedFilterType === '!=') {
                                col.filter = " NOT " + col.filter;
                            }
                        }
                        //Configure date filters
                        else if (col.dataType === 'timestamp') {
                            //Configure =,!=,between filters
                            if (col.selectedFilterType === '=' || col.selectedFilterType === '!=' || col.selectedFilterType === 'between') {
                                col.filter = ' BETWEEN ' + "'" + $filter('date')(col.filterValFirst, "MMM dd yyyy") + "' AND ";
                                //Between Filter
                                if (col.selectedFilterType === 'between') {
                                    var secondDate = new Date(col.filterValSecond);
                                    secondDate.setDate(secondDate.getDate() + 1);
                                    col.filter += "'" + $filter('date')(secondDate, "MMM dd yyyy") + "'"
                                } else {
                                    var secondDate = new Date(col.filterValFirst);
                                    secondDate.setDate(secondDate.getDate() + 1);
                                    col.filter += "'" + $filter('date')(secondDate, "MMM dd yyyy") + "'"
                                    //Configure != filter
                                    if (col.selectedFilterType === '!=') {
                                        col.filter = " NOT " + col.filter;
                                    }
                                }
                            }
                            else if (col.selectedFilterType === '<') {
                                var firstDate = new Date(col.filterValFirst);
                                firstDate.setDate(firstDate.getDate() + 1);
                                col.filter = col.selectedFilterType + "'" + $filter('date')(firstDate, "MMM dd yyyy") + "'";
                            }
                            else {
                                col.filter = col.selectedFilterType + "'" + $filter('date')(col.filterValFirst, "MMM dd yyyy") + "'";

                            }
                        }
                        //Configure all other filters
                        else {
                            col.filter = col.selectedFilterType + col.filterValFirst;
                        }
                    }
                    col.availableFilterTypes = undefined;
                    col.availableFilterValues = undefined;
                    col.availableFilterValueSelect = undefined;
                    col.filterValFirst = undefined;
                    col.selectedFilterType = undefined;
                    col.filterValSecond = undefined;
                    col.id = undefined;
                    col.order = undefined;
                    col.joinAlias = undefined;
                });
                $scope.reportWithFilter = angular.copy($scope.report.selectedReportJSON);
                $scope.reportWithFilter.columns = angular.copy($scope.columnsCopy);
                $scope.reportWithFilter.$promise = undefined;
                $scope.reportWithFilter.$resolved = undefined;
                console.log(JSON.stringify($scope.reportWithFilter));
                ReportBuilderService.configureReport($scope.reportWithFilter, function (res) {
                    $scope.convertedQuery = res.convertedQuery;
                    $scope.retrieveParams = {"query": $scope.convertedQuery, "count": $scope.previewTableParams.count(), 'page': $scope.previewTableParams.page(), 'saveQuery': true};
                    $scope.previewTableParams.page(1);
                    $scope.previewTableParams.reload();

                }, function () {

                });
            };
            $scope.downloadExcel = function () {
                $rootScope.maskLoading();
                ReportBuilderService.generateExcel(function (res) {
                    window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadexcel");
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }
            $scope.downloadPdf = function () {
                $rootScope.maskLoading();
                console.log("selectedReport---" + JSON.stringify($scope.report.selectedReport));
                ReportBuilderService.generatePdf($scope.report.selectedReport, function (res) {
                    window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadpdf");
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }
            $rootScope.unMaskLoading();
        }]);
});

