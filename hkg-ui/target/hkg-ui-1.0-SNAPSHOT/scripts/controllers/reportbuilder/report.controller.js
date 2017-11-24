define(['hkg', 'reportBuilderService', 'messageService', 'franchiseService', 'ngload!uiGrid', 'colorpicker.directive'], function (hkg) {
    hkg.register.controller('ManageReport', ["$rootScope", "$scope", "$location", "$filter", "$timeout", "$filter", "ReportBuilderService", "FranchiseService", "Messaging", function ($rootScope, $scope, $location, filter, $timeout, $filter, ReportBuilderService, FranchiseService, Messaging) {
            $rootScope.maskLoading();
            //Init variables in scope
            $scope.entity = "REPORTBUILDER.";
            $rootScope.mainMenu = "manageLink";
            if ($location.path() === '/managereport') {
                $rootScope.childMenu = "manageReports";
                $scope.isReportBuilder = true;
                $scope.reportCurrentPage = 1;
            } else {
                $rootScope.childMenu = "reports";
                $scope.isReportBuilder = false;
            }

            $rootScope.activateMenu();
            var data = [];
            $scope.subbmited = false;
            $scope.isPreview = false;
            $scope.dt = {};
            $scope.isFilter = true;
            $scope.isAnyColumnHidden = false;
            $scope.hiddenFieldDetail = [];
            $scope.displayName = '';
            $scope.isHKAdmin = false;
            $scope.displayLocalData = [];
            $scope.isFranchiseChange = false;
            $scope.isAnyRecordsExist = false;
            $scope.levelMap = [];
            $scope.totalLevel = 0;
            $scope.models = {};
            $scope.columnOrderMap = {};
            $scope.currentColor = {};
            $scope.colorConfigData = [];
            $scope.colorConfig = {};
            $scope.isEditModeColorInitialized = false;
            $scope.currencyMap = {};
            //-----Code For DatePickerDialog
            $scope.open = function ($event, opened, element) {
                $event.preventDefault();
                $event.stopPropagation();
                element[opened] = true;
            };
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            $scope.format = $rootScope.dateFormat;
            $scope.today = new Date();
            //Grid options
            /**
             * pagination parameters.
             */
            $scope.paginationOptions = {
                pageNumber: 1,
                pageSize: 50,
                sortDirection: null,
                sortColumn: null,
                sortColumnType: null
            };
            $scope.filterOptions = [];
            $scope.gridOptions = {};
            $scope.gridOptions.enableFiltering = true;
            $scope.gridOptions.useExternalFiltering = true;
            $scope.gridOptions.paginationPageSizes = [50, 100];
            $scope.gridOptions.paginationPageSize = 50;
            $scope.gridOptions.useExternalPagination = true;
            $scope.gridOptions.useExternalSorting = true;
            //To change background color of selected rows !!
            $scope.gridOptions.rowTemplate = '<div ng-style="{ \'background-color\': grid.appScope.rowFormatter(row) }">' +
                    '  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
                    '</div>';
            $scope.gridOptions.columnDefs = [];
            $scope.gridOptions.data = [];
            $scope.gridOptions.onRegisterApi = function (gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
//                    console.log(sortColumns);
                    if (sortColumns.length === 0) {
                        $scope.paginationOptions.sortDirection = null;
                        $scope.paginationOptions.sortColumn = null;
                        $scope.paginationOptions.sortColumnType = null;
                    } else {
                        //Add sort values to externally sort collection.
                        $scope.paginationOptions.sortDirection = sortColumns[0].sort.direction;
                        $scope.paginationOptions.sortColumn = sortColumns[0].field;
                        $scope.paginationOptions.sortColumnType = sortColumns[0].colDef.type;
                    }
                    $scope.getPage();
                });
                $scope.gridApi.core.on.filterChanged($scope, function () {
                    var grid = this.grid;
                    if (grid.columns.length > 0) {
                        $scope.filterOptions = [];
                        angular.forEach(grid.columns, function (column) {
                            if (column.filters[0].term !== undefined && column.filters[0].term !== null && column.filters[0].term !== '') {
                                var obj = {};
                                obj.filterColumn = column.field;
                                obj.filterColumnType = column.colDef.type;
                                obj.filterValue = column.filters[0].term;
//                                console.log(JSON.stringify(obj));
                                $scope.filterOptions.push(obj);
                            }
                        });

                        $scope.getPage();
                    }
                });
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.paginationOptions.pageNumber = newPage;
                    $scope.paginationOptions.pageSize = pageSize;
                    $scope.getPage();
                });
            };
            $scope.getPage = function () {
                $scope.send = {
                    offSet: $scope.paginationOptions.pageNumber,
                    limit: $scope.paginationOptions.pageSize,
                    isFilter: $scope.isFilter,
                    isGrouped: $scope.isGroupByCheck,
                    sortColumn: $scope.paginationOptions.sortColumn,
                    sortDirection: $scope.paginationOptions.sortDirection,
                    sortColumnType: $scope.paginationOptions.sortColumnType,
                    filterOptions: $scope.filterOptions
                };
                ReportBuilderService.retrievePaginatedData($scope.send, function (res) {
                    if (res.data !== undefined) {
                        if (res.data.queryValid !== undefined) {
                            var msg = "Invalid Query.";
                            var type = $rootScope.failure;
                            $rootScope.addMessage(msg, type);
                        } else {
                            $scope.previewData = res.data.records;
                            $scope.totalItems = (res.data.totalRecords);
                            $scope.hiddenFieldDetail = [];
//                            console.log("total records :" + $scope.totalItems);
                            $scope.gridOptions.totalItems = $scope.totalItems;
                            if (!$scope.isGroupByCheck) {
                                //Format previewData to new format includes color.
                                $scope.UIPreviewData = angular.copy($scope.previewData);
//                                console.log("previwDate----in paginated non filter" + JSON.stringify($scope.UIPreviewData));
                                $scope.gridColumnDef = [];
                                angular.forEach($scope.columns, function (reportColumn) {
                                    reportColumn.total = undefined;
                                    if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
                                        var total = 0;
                                        angular.forEach($scope.previewData, function (row) {
                                            var data = row[reportColumn.alias] === undefined ? (row[reportColumn.fieldLabel] === undefined ? 0 : row[reportColumn.fieldLabel]) : row[reportColumn.alias];
                                            total += Number(data);
                                        });
                                        if (isNaN(total)) {
                                            total = 0;
                                        }
                                        reportColumn.total = total;
                                    }
                                    if (reportColumn.isHide === true) {
                                        var label = (reportColumn.alias === undefined || reportColumn.alias === '') ? reportColumn.fieldLabel : reportColumn.alias;
                                        var values = [];
                                        angular.forEach($scope.previewData, function (row) {
                                            var data = (row[label] === undefined || row[label] === null) ? 'N/A' : row[label];
                                            var isValueExist = false;
                                            angular.forEach(values, function (value) {
                                                if (value === data) {
                                                    isValueExist = true;
                                                }
                                            });
                                            if (!isValueExist) {
                                                values.push(data);
                                            }
                                        });
                                        var labelValue = '';
                                        angular.forEach(values, function (item) {
                                            if (labelValue.length === 0) {
                                                labelValue = item;
                                            } else {
                                                labelValue += "," + item;
                                            }
                                        });
                                        $scope.hiddenFieldDetail.push({label: label, value: labelValue});
                                    }
                                    //Add symbol for currency.
                                    if (reportColumn.componentType === 'Currency') {
                                        if ($scope.currencyMap[reportColumn.associatedCurrency] !== undefined) {
                                            reportColumn.currencySymbol = $scope.currencyMap[reportColumn.associatedCurrency].symbol;
                                            reportColumn.format = $scope.currencyMap[reportColumn.associatedCurrency].format;
                                        }
                                    }
                                    var type;
                                    switch (reportColumn.dataType) {
                                        case 'int8':
                                            type = 'number';
                                            break;
                                        case 'varchar':
                                            type = 'string';
                                            break;
                                        case 'timestamp':
                                            type = 'date';
                                            break;
                                        case 'boolean':
                                            type = 'boolean';
                                            break;
                                        case 'double precision':
                                            type = 'double';
                                            break;
                                        default:
                                            type = 'object';
                                            break;
                                    }
                                    if (reportColumn.isHide !== true) {
                                        $scope.gridColumnDef.push({name: reportColumn.alias, displayName: reportColumn.alias,
                                            type: type, enableHiding: false,
                                            cellTemplate: '<div class="ui-grid-cell-contents" title="{{(COL_FIELD === null || COL_FIELD === \'\') ? \'N/A\':COL_FIELD}}">{{(COL_FIELD === null || COL_FIELD === "") ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                    }
                                });
                            }
                            //Prepare data for preview if Group by is applied.
                            if ($scope.isGroupByCheck) {
                                $scope.groupRecordsForPreview($scope.previewData);
                            }
                            $scope.gridOptions.columnDefs = $scope.gridColumnDef;
                            $scope.gridOptions.data = $scope.UIPreviewData;
                            if ($scope.isFranchiseChange && $scope.isFilter) {
                                $scope.applyFilter($scope.filterForm, true);
                                $scope.isFranchiseChange = false;
                            }
                        }
                    }
                }, function () {
                    $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                });
            };

            $scope.$on('$viewContentLoaded', function () {
                $timeout(function () {
                    $scope.initializeFranchise();
                }, 0);
            });
            $scope.availabeFilterTypesByDataType = {int8: [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "<=", text: "less than equal to"}, {id: ">=", text: "greater than equal to"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "varchar": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "in", text: "in"}, {id: "not in", text: "not in"}, {id: "like", text: "like"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "timestamp": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "double precision": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "<=", text: "less than equal to"}, {id: ">=", text: "greater than equal to"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "boolean": [{id: "=", text: "equal to"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}]};

            $scope.retrieveReportPaginatedData = function () {
//                console.log('mask----');
                $rootScope.maskLoading();
                $scope.send = {
                    offSet: $scope.reportCurrentPage,
                    limit: 10,
                    showAll: $scope.isReportBuilder
                };
                ReportBuilderService.retrieveAllReports($scope.send, function (res) {
                    if (res !== undefined && res !== null) {
                        $scope.reportList = [];
                        angular.forEach(res.records, function (items) {
                            items.displayName = items.reportName;
                            $scope.reportList.push(items);
                        });
                        $scope.reportTotalItems = (res.totalRecords);
                        $scope.indexReport = $scope.reportCurrentPage;
//                        console.log("total records :" + $scope.reportTotalItems);
                    } else {
                        $scope.reportList = [];
                    }
                    $rootScope.unMaskLoading();
//                    console.log('unmask----');
                }, function () {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Failed to load reports.", $rootScope.failure);
                });
            };


            //Edit report
            $scope.editReport = function (report) {
                localStorage.setItem('reportId', report.id);
                $location.path('/reportbuilder');
            };
            $scope.goToReportBuilder = function () {
                localStorage.removeItem('reportId');
                $location.path('/reportbuilder');
            };
            // local function for sorting the fields.
            function SortByFieldSequence(x, y) {
                return x.fieldSequence - y.fieldSequence;
            }

            $scope.previewReport = function (report) {
                $scope.currentPage = 1;
                $scope.clearReportData();
                $scope.retrieveReportById(report);
            };

            $scope.isGroupedReport = function () {
                if ($scope.report.groupAttributes !== undefined && $scope.report.groupAttributes !== null) {
                    $scope.isGroupByCheck = true;
                } else {
                    $scope.isGroupByCheck = false;
                }
            };
            //Retrieve initial data set. Same operation will be done in case of change in franchise and page.
            $scope.retrievePaginatedData = function () {
                $rootScope.maskLoading();
//                console.log($scope.isFilter);
//                $scope.isFilter = true;
//                $scope.previewData = [];
                $scope.isPreview = true;
                $scope.tempData = [];
                $scope.isGroupedReport();
//                console.log("group :" + $scope.isGroupByCheck);
//                console.log("currentPage :" + $scope.currentPage);

//                $scope.send = {
//                    offSet: $scope.paginationOptions.pageNumber,
//                    limit: $scope.paginationOptions.pageSize,
//                    isFilter: $scope.isFilter,
//                    isGrouped: $scope.isGroupByCheck,
//                    sortColumn: $scope.paginationOptions.sortColumn,
//                    sortDirection: $scope.paginationOptions.sortDirection,
//                    sortColumnType: $scope.paginationOptions.sortColumnType,
//                    filterOptions: $scope.filterOptions
//                };
//                ReportBuilderService.retrievePaginatedData($scope.send, function (res) {
//                    if (res.data !== undefined) {
//                        if (res.data.queryValid !== undefined) {
//                            var msg = "Invalid Query.";
//                            var type = $rootScope.failure;
//                            $rootScope.addMessage(msg, type);
//                            $rootScope.unMaskLoading();
//                        } else {
//                            $scope.previewData = res.data.records;
//                            $scope.totalItems = (res.data.totalRecords);
                            $scope.hiddenFieldDetail = [];
//                            console.log("total records :" + $scope.totalItems);
                            $scope.gridOptions.totalItems = $scope.totalItems;
                            if ($scope.totalItems > 0) {
                                $scope.isAnyRecordsExist = true;
                            }
                            if (!$scope.isGroupByCheck) {
                                //Format previewData to new format includes color.
                                $scope.UIPreviewData = angular.copy($scope.previewData);
//                                angular.forEach($scope.UIPreviewData, function(data, rowIndex) {
//                                    $scope.UIPreviewData[rowIndex].rowColor = 'white';
//                                    angular.forEach(data, function(value, key) {
//                                        $scope.UIPreviewData[rowIndex][key] = {value: value, color: 'white'};
//                                    });
//                                });
//                                console.log("previwDate----in paginated non filter" + JSON.stringify($scope.UIPreviewData));
                                $scope.gridColumnDef = [];
                                angular.forEach($scope.columns, function (reportColumn) {
                                    reportColumn.total = undefined;
                                    if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
                                        var total = 0;
                                        angular.forEach($scope.previewData, function (row) {
                                            var data = row[reportColumn.alias] === undefined ? (row[reportColumn.fieldLabel] === undefined ? 0 : row[reportColumn.fieldLabel]) : row[reportColumn.alias];
                                            total += Number(data);
                                        });
                                        if (isNaN(total)) {
                                            total = 0;
                                        }
                                        reportColumn.total = total;
                                    }
                                    if (reportColumn.isHide === true) {
                                        var label = (reportColumn.alias === undefined || reportColumn.alias === '') ? reportColumn.fieldLabel : reportColumn.alias;
                                        var values = [];
                                        angular.forEach($scope.previewData, function (row) {
                                            var data = (row[label] === undefined || row[label] === null) ? 'N/A' : row[label];
                                            var isValueExist = false;
                                            angular.forEach(values, function (value) {
                                                if (value === data) {
                                                    isValueExist = true;
                                                }
                                            });
                                            if (!isValueExist) {
                                                values.push(data);
                                            }
                                        });
                                        var labelValue = '';
                                        angular.forEach(values, function (item) {
                                            if (labelValue.length === 0) {
                                                labelValue = item;
                                            } else {
                                                labelValue += "," + item;
                                            }
                                        });
                                        $scope.hiddenFieldDetail.push({label: label, value: labelValue});
                                    }
                                    //Add symbol for currency.
                                    if (reportColumn.componentType === 'Currency') {
                                        if ($scope.currencyMap[reportColumn.associatedCurrency] !== undefined) {
                                            reportColumn.currencySymbol = $scope.currencyMap[reportColumn.associatedCurrency].symbol;
                                            reportColumn.format = $scope.currencyMap[reportColumn.associatedCurrency].format;
                                        }
                                    }
                                    var type;
                                    switch (reportColumn.dataType) {
                                        case 'int8':
                                            type = 'number';
                                            break;
                                        case 'varchar':
                                            type = 'string';
                                            break;
                                        case 'timestamp':
                                            type = 'date';
                                            break;
                                        case 'boolean':
                                            type = 'boolean';
                                            break;
                                        case 'double precision':
                                            type = 'double';
                                            break;
                                        default:
                                            type = 'object';
                                            break;
                                    }
                                    $scope.gridColumnDef.push({name: reportColumn.alias, displayName: reportColumn.alias,
                                        type: type, enableHiding: false,
                                        cellTemplate: '<div class="ui-grid-cell-contents" title="{{(COL_FIELD === null || COL_FIELD === \'\') ? \'N/A\':COL_FIELD}}">{{(COL_FIELD === null || COL_FIELD === "") ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                });
                                $scope.gridOptions.columnDefs = $scope.gridColumnDef;
                                $scope.gridOptions.data = $scope.UIPreviewData;
                            }
                            //Prepare data for preview if Group by is applied.
                            if ($scope.isGroupByCheck) {
                                $scope.groupRecordsForPreview($scope.previewData);
                            } else {
                                if (!$scope.isEditModeColorInitialized) {
                                    $scope.updateColorConfiguration();
                                }
                                //May not be required.(ui grid will apply color itself.)
                                else {
                                    //Apply coloring if required.
                                    if ($scope.colorConfigData.length > 0) {
                                        $scope.applyColor($scope.filterForm, true);
                                    }
                                }
                            }
                            $rootScope.unMaskLoading();
//                        }
//                    }
                    if ($scope.isFranchiseChange && $scope.isFilter) {
                        $scope.applyFilter($scope.filterForm, true);
                    }
                    $rootScope.unMaskLoading();
//                }, function () {
//                    $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
//                    $rootScope.unMaskLoading();
//                });
            };

            /**
             * Methods to modify preview According to grouping.
             */
            //Previous version. Only for reference.
            $scope.groupRecordsForPreviewTemp = function (previewData) {
                $scope.columns.sort(SortByFieldSequence);
                $scope.modifiedColumns = angular.copy($scope.columns);
                angular.forEach($scope.modifiedColumns, function (reportColumn) {
                    if (reportColumn.isHide === true) {
                        var label = (reportColumn.alias === undefined || reportColumn.alias === '') ? reportColumn.fieldLabel : reportColumn.alias;
                        var values = [];
                        angular.forEach($scope.previewData, function (value, key) {
                            angular.forEach(value, function (row) {
                                var data = (row[label] === undefined || row[label] === null) ? 'N/A' : row[label];
                                var isValueExist = false;
                                angular.forEach(values, function (value) {
                                    if (value === data) {
                                        isValueExist = true;
                                    }
                                });
                                if (!isValueExist) {
                                    values.push(data);
                                }
                            });
                        });
                        var labelValue = '';
                        angular.forEach(values, function (item) {
                            if (labelValue.length === 0) {
                                labelValue = item;
                            } else {
                                labelValue += "," + item;
                            }
                        });
                        $scope.hiddenFieldDetail.push({label: label, value: labelValue});
                    }
                    //Add symbol for currency.
                    if (reportColumn.componentType === 'Currency') {
                        if ($scope.currencyMap[reportColumn.associatedCurrency] !== undefined) {
                            reportColumn.currencySymbol = $scope.currencyMap[reportColumn.associatedCurrency].symbol;
                            reportColumn.format = $scope.currencyMap[reportColumn.associatedCurrency].format;
                        }
                    }
                });
                if ($scope.isGroupByCheck && previewData !== undefined) {
                    var tempPreviewData = [];
                    var dataIndex = 0;
                    angular.forEach(previewData, function (value, key) {
                        var obj = {};
                        obj.key = key;
                        obj.value = value;
                        obj.index = ++dataIndex;
                        tempPreviewData.push(obj);

                    });

                    $scope.previewData = angular.copy(tempPreviewData);
//                    console.log('preview group before...' + JSON.stringify($scope.previewData));
                    var tempColumnList = [];
                    var tempRowColumnList = [];
                    $scope.groupItemListForPreviw = [];
//                    console.log("fields :" + JSON.stringify($scope.report.groupAttributes));
                    $scope.groupAttributes = JSON.parse($scope.report.groupAttributes);

//                    //Check for group by levels
                    if ($scope.groupAttributes['groupBy'] !== undefined && $scope.groupAttributes['groupBy'].length > 0) {
                        //Sort groupby accroding to sequence.
                        $scope.groupAttributes['groupBy'].sort(SortBySequence);
                        var totalLevel = 0;
                        $scope.levelMap = [];
                        angular.forEach($scope.groupAttributes['groupBy'], function (groupByInfo) {
                            $scope.levelMap.push({level: groupByInfo.level, levelItems: groupByInfo.fields});
                            totalLevel++;
                            var groupByFields = groupByInfo.fields.split(",");
                            angular.forEach(groupByFields, function (item) {
                                angular.forEach($scope.modifiedColumns, function (column, index) {
                                    var fieldName;
                                    if (column.alias === undefined || column.alias === '') {
                                        fieldName = column.fieldLabel;
                                    } else {
                                        fieldName = column.alias;
                                    }

                                    if (fieldName === item) {
                                        column.level = groupByInfo.level;
                                        tempColumnList.push(column);
                                        tempRowColumnList.push(column);
                                        $scope.modifiedColumns.splice(index, 1);
                                    }
                                });
                            });
                        });
                        $scope.totalLevel = totalLevel;
//                        console.log('LevelMap---' + JSON.stringify($scope.levelMap));
//                        console.log('Total level---' + $scope.totalLevel);
                        //Add all column field name to check for ordering.
                        var valueFields = [];
                        angular.forEach($scope.modifiedColumns, function (column) {
                            var fieldName;
                            if (column.alias === undefined || column.alias === '') {
                                fieldName = column.fieldLabel;
                            } else {
                                fieldName = column.alias;
                            }
                            valueFields.push(fieldName);
                        });
                        angular.forEach($scope.levelMap, function (level) {
                            var levelItems = level.levelItems.split(",");
                            angular.forEach(levelItems, function (item) {
                                angular.forEach(valueFields, function (valField, index) {
                                    if (valField === item) {
                                        valueFields.splice(index, 1);
                                    }
                                });
                            });
                        });
                        $scope.previewData = $scope.recursiveGroup($scope.previewData, 2, valueFields);
                        $scope.combineRows();
//                        console.log('preview group' + JSON.stringify($scope.previewData));
                        var UIpreviewData = angular.copy($scope.previewData);
                        var tempUIPreviewData = [];
                        var groupIndex = 0;
                        angular.forEach(UIpreviewData, function (previewGroup) {
                            var obj = {};
                            obj.key = previewGroup.key;
                            obj.value = previewGroup.value;
                            obj.groupRows = previewGroup.groupRows;
                            obj.rowCount = previewGroup.rowCount;
                            //Format previewData to new format includes color.
                            angular.forEach(obj.groupRows, function (data, rowIndex) {
                                obj.groupRows[rowIndex].rowColor = 'white';
                                angular.forEach(data, function (value, key) {
                                    obj.groupRows[rowIndex][key] = {value: value, color: 'white'};
                                });
                            });
                            obj.index = ++groupIndex;
                            tempUIPreviewData.push(obj);

                        });
                        $scope.UIPreviewData = angular.copy(tempUIPreviewData);
//                    console.log('temp---ui ---preview group' + JSON.stringify($scope.UIPreviewData));
//                        console.log(JSON.stringify($scope.previewData));
                    }
                    //Check for groups.
//                    console.log('Groups-------------' + JSON.stringify($scope.groupInfo.groupAttr));
                    if ($scope.groupAttributes['groups'] !== undefined && $scope.groupAttributes['groups'].length > 0) {
                        //Sort groups accroding to sequence.
                        $scope.groupAttributes['groups'].sort(SortBySequence);
//                        console.log(JSON.stringify($scope.groupInfo.groupAttr.groups));
                        if ($scope.groupAttributes['groups'].length > 1) {
                            angular.forEach($scope.groupAttributes['groups'], function (groupInfo) {
                                var groupItems = groupInfo.groupItems.split(",");
                                var groupName = groupInfo.groupName;
                                var obj = {};
                                obj.groupName = groupName;
                                obj.isGroup = true;
                                var groupItemsWithSequence = [];
                                //Add all group Items to a separate List
                                angular.forEach($scope.modifiedColumns, function (modifiedColumn) {
                                    var label;
                                    if (modifiedColumn.alias !== undefined && modifiedColumn.alias !== '') {
                                        label = modifiedColumn.alias;
                                    } else {
                                        label = modifiedColumn.fieldLabel;
                                    }
                                    angular.forEach(groupItems, function (grpItem) {
                                        if (grpItem === label && (modifiedColumn.isHide === undefined || modifiedColumn.isHide === false)) {
                                            $scope.groupItemListForPreviw.push(grpItem);
                                            groupItemsWithSequence.push(grpItem);
                                        }
                                    });
                                });
                                obj.groupItems = groupItemsWithSequence;
                                //Maintain data to be viewed as a group.
                                angular.forEach($scope.previewData, function (data) {
                                    var groupValues = [];
//                                data.groupObj = [];
                                    angular.forEach(data.groupRows, function (row) {
                                        var rowGroupValues = [];
                                        angular.forEach(groupItemsWithSequence, function (item) {
                                            var itemValue = row[item] === undefined ? '-' : row[item];
                                            rowGroupValues.push(itemValue);
                                        });
                                        groupValues.push(rowGroupValues);
                                    });
//                                var groupObj = {groupName: groupName, groupValues: groupValues};
                                    data[groupName] = groupValues;
                                });
                                if (groupItemsWithSequence.length > 0) {
                                    tempColumnList.push(obj);
                                }
                                //Clear group columns from actual columns.
                                for (var i = $scope.modifiedColumns.length - 1; i >= 0; i--) {
                                    var column = $scope.modifiedColumns[i];
                                    var columnLabel;
                                    if (column.alias !== undefined && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                    if (groupItems.indexOf(columnLabel) > -1) {
                                        tempRowColumnList.push($scope.modifiedColumns[i]);
                                        $scope.modifiedColumns.splice(i, 1);
                                    }
                                }
                                ;
                            });
                        } else {
                            angular.forEach($scope.groupAttributes['groups'], function (groupInfo) {
                                var groupItems = groupInfo.groupItems.split(",");
                                //Rearrange the groups columns.(for group of length one.)
                                for (var i = $scope.modifiedColumns.length - 1; i >= 0; i--) {
                                    var column = $scope.modifiedColumns[i];
                                    var columnLabel;
                                    if (column.alias !== undefined && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                    if (groupItems.indexOf(columnLabel) > -1) {
                                        tempRowColumnList.push($scope.modifiedColumns[i]);
                                        $scope.modifiedColumns.splice(i, 1);
                                    }
                                }
                                ;
                            });
                        }
                    }

                    //Adding rest of columns.
                    if ($scope.modifiedColumns.length > 0) {
                        angular.forEach($scope.modifiedColumns, function (item) {
                            tempColumnList.push(item);
                            tempRowColumnList.push(item);
                        });
                    }
//                    angular.forEach(tempColumnList, function(item, index) {
//                        item.fieldSequence = index + 1;
//                    });
//                    angular.forEach(tempRowColumnList, function(item, index) {
//                        item.fieldSequence = index + 1;
//                    });
                    tempColumnList.sort(SortByFieldSequence);
                    tempRowColumnList.sort(SortByFieldSequence);
                    $scope.modifiedColumns = angular.copy(tempColumnList);
                    $scope.columns = angular.copy(tempRowColumnList);
//                    console.log('Preview Data--------------' + JSON.stringify($scope.previewData));
//                    console.log('Modified Columns--------------' + JSON.stringify($scope.modifiedColumns));
//                    console.log('Actual Columns--------------' + JSON.stringify($scope.columns));
//                    console.log('Group Items--------------' + JSON.stringify($scope.groupItemListForPreviw));

                    //Evaluate total
                    angular.forEach($scope.columns, function (reportColumn) {
                        reportColumn.total = undefined;
                        if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
                            var total = 0;
                            angular.forEach($scope.previewData, function (data) {
                                angular.forEach(data.groupRows, function (row) {
                                    var data = row[reportColumn.alias] === undefined ? (row[reportColumn.fieldLabel] === undefined ? 0 : row[reportColumn.fieldLabel]) : row[reportColumn.alias];
                                    total += Number(data);
                                });
                            });
                            if (isNaN(total)) {
                                total = 0;
                            }
                            reportColumn.total = total;
                        }
                    });
                    if (!$scope.isEditModeColorInitialized) {
                        $scope.updateColorConfiguration();
                    } else {
                        //Apply coloring if required.
                        if ($scope.colorConfigData.length > 0) {
                            $scope.applyColor($scope.filterForm, true);
                        }
                    }
                }
            };

            $scope.groupRecordsForPreview = function (previewData) {
                $scope.columns.sort(SortByFieldSequence);
                $scope.modifiedColumns = angular.copy($scope.columns);

                if ($scope.isGroupByCheck && previewData !== undefined) {
                    var tempPreviewData = [];
                    var dataIndex = 0;
                    angular.forEach(previewData, function (value, key) {
                        tempPreviewData.push(value);
                    });

                    $scope.previewData = angular.copy(tempPreviewData);
//                    console.log('preview group before...' + JSON.stringify($scope.previewData));
                    var tempColumnList = [];
                    var tempRowColumnList = [];
                    $scope.groupItemListForPreviw = [];
                    $scope.groupAttributes = JSON.parse($scope.report.groupAttributes);
//                    //Check for group by levels
                    if ($scope.groupAttributes['groupBy'] !== undefined && $scope.groupAttributes['groupBy'].length > 0) {
                        //Sort groupby accroding to sequence.
                        $scope.groupAttributes['groupBy'].sort(SortBySequence);
                        var totalLevel = 0;
                        $scope.levelMap = [];
                        angular.forEach($scope.groupAttributes['groupBy'], function (groupByInfo) {
                            $scope.levelMap.push({level: groupByInfo.level, levelItems: groupByInfo.fields});
                            totalLevel++;
                            var groupByFields = groupByInfo.fields.split(",");
                            angular.forEach(groupByFields, function (item) {
                                angular.forEach($scope.modifiedColumns, function (column, index) {
                                    var fieldName;
                                    if (column.alias === undefined || column.alias === '') {
                                        fieldName = column.fieldLabel;
                                    } else {
                                        fieldName = column.alias;
                                    }

                                    if (fieldName === item) {
                                        column.level = groupByInfo.level;
                                        tempColumnList.push(column);
                                        tempRowColumnList.push(column);
                                        $scope.modifiedColumns.splice(index, 1);
                                    }
                                });
                            });
                        });
                        $scope.totalLevel = totalLevel;
                        //Add all column field name to check for ordering.
                        var valueFields = [];
                        angular.forEach($scope.modifiedColumns, function (column) {
                            var fieldName;
                            if (column.alias === undefined || column.alias === '') {
                                fieldName = column.fieldLabel;
                            } else {
                                fieldName = column.alias;
                            }
                            valueFields.push(fieldName);
                        });
                        angular.forEach($scope.levelMap, function (level) {
                            var levelItems = level.levelItems.split(",");
                            angular.forEach(levelItems, function (item) {
                                angular.forEach(valueFields, function (valField, index) {
                                    if (valField === item) {
                                        valueFields.splice(index, 1);
                                    }
                                });
                            });
                        });
                        $scope.UIPreviewData = angular.copy($scope.previewData);
                    }
                    //Check for groups.
                    if ($scope.groupAttributes['groups'] !== undefined && $scope.groupAttributes['groups'].length > 0) {
                        //Sort groups accroding to sequence.
                        $scope.groupAttributes['groups'].sort(SortBySequence);
                        if ($scope.groupAttributes['groups'].length > 1) {
                            angular.forEach($scope.groupAttributes['groups'], function (groupInfo) {
                                var groupItems = groupInfo.groupItems.split(",");
                                var groupName = groupInfo.groupName;
                                var obj = {};
                                obj.groupName = groupName;
                                obj.isGroup = true;
                                var groupItemsWithSequence = [];
                                //Add all group Items to a separate List
                                angular.forEach($scope.modifiedColumns, function (modifiedColumn) {
                                    var label;
                                    if (modifiedColumn.alias !== undefined && modifiedColumn.alias !== '') {
                                        label = modifiedColumn.alias;
                                    } else {
                                        label = modifiedColumn.fieldLabel;
                                    }
                                    angular.forEach(groupItems, function (grpItem) {
                                        if (grpItem === label && (modifiedColumn.isHide === undefined || modifiedColumn.isHide === false)) {
                                            $scope.groupItemListForPreviw.push(grpItem);
                                            groupItemsWithSequence.push(grpItem);
                                        }
                                    });
                                });
                                obj.groupItems = groupItemsWithSequence;
                                //Maintain data to be viewed as a group.
                                angular.forEach($scope.previewData, function (data) {
                                    var groupValues = [];
                                    angular.forEach(data.groupRows, function (row) {
                                        var rowGroupValues = [];
                                        angular.forEach(groupItemsWithSequence, function (item) {
                                            var itemValue = row[item] === undefined ? '-' : row[item];
                                            rowGroupValues.push(itemValue);
                                        });
                                        groupValues.push(rowGroupValues);
                                    });
                                    data[groupName] = groupValues;
                                });
                                if (groupItemsWithSequence.length > 0) {
                                    tempColumnList.push(obj);
                                }
                                //Clear group columns from actual columns.
                                for (var i = $scope.modifiedColumns.length - 1; i >= 0; i--) {
                                    var column = $scope.modifiedColumns[i];
                                    var columnLabel;
                                    if (column.alias !== undefined && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                    if (groupItems.indexOf(columnLabel) > -1) {
                                        tempRowColumnList.push($scope.modifiedColumns[i]);
                                        $scope.modifiedColumns.splice(i, 1);
                                    }
                                }
                                ;
                            });
                        } else {
                            angular.forEach($scope.groupAttributes['groups'], function (groupInfo) {
                                var groupItems = groupInfo.groupItems.split(",");
                                //Rearrange the groups columns.(for group of length one.)
                                for (var i = $scope.modifiedColumns.length - 1; i >= 0; i--) {
                                    var column = $scope.modifiedColumns[i];
                                    var columnLabel;
                                    if (column.alias !== undefined && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                    if (groupItems.indexOf(columnLabel) > -1) {
                                        tempRowColumnList.push($scope.modifiedColumns[i]);
                                        $scope.modifiedColumns.splice(i, 1);
                                    }
                                }
                            });
                        }
                    }

                    //Adding rest of columns.
                    if ($scope.modifiedColumns.length > 0) {
                        angular.forEach($scope.modifiedColumns, function (item) {
                            tempColumnList.push(item);
                            tempRowColumnList.push(item);
                        });
                    }
                    tempColumnList.sort(SortByFieldSequence);
                    tempRowColumnList.sort(SortByFieldSequence);
                    $scope.modifiedColumns = angular.copy(tempColumnList);
                    $scope.columns = angular.copy(tempRowColumnList);
//                    console.log('Preview Data--------------' + JSON.stringify($scope.previewData));
//                    console.log('Modified Columns--------------' + JSON.stringify($scope.modifiedColumns));
//                    console.log('Actual Columns--------------' + JSON.stringify($scope.columns));
//                    console.log('Group Items--------------' + JSON.stringify($scope.groupItemListForPreviw));

                    //Evaluate total
                    $scope.gridColumnDef = [];
                    angular.forEach($scope.columns, function (reportColumn) {
                        reportColumn.total = undefined;
                        if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
                            var total = 0;
                            angular.forEach($scope.previewData, function (data) {
                                var dataTemp = data[reportColumn.alias] === undefined ? (data[reportColumn.fieldLabel] === undefined ? 0 : data[reportColumn.fieldLabel]) : data[reportColumn.alias];
                                total += Number(dataTemp);
                            });
                            if (isNaN(total)) {
                                total = 0;
                            }
                            reportColumn.total = total;
                        }
                        if (reportColumn.isHide === true) {
                            var label = (reportColumn.alias === undefined || reportColumn.alias === '') ? reportColumn.fieldLabel : reportColumn.alias;
                            var values = [];
                            angular.forEach($scope.previewData, function (row) {
                                var data = (row[label] === undefined || row[label] === null) ? 'N/A' : row[label];
                                var isValueExist = false;
                                angular.forEach(values, function (value) {
                                    if (value === data) {
                                        isValueExist = true;
                                    }
                                });
                                if (!isValueExist) {
                                    values.push(data);
                                }
                            });
                            var labelValue = '';
                            angular.forEach(values, function (item) {
                                if (labelValue.length === 0) {
                                    labelValue = item;
                                } else {
                                    labelValue += "," + item;
                                }
                            });
                            $scope.hiddenFieldDetail.push({label: label, value: labelValue});
                        }
                        var type;
                        switch (reportColumn.dataType) {
                            case 'int8':
                                type = 'number';
                                break;
                            case 'varchar':
                                type = 'string';
                                break;
                            case 'timestamp':
                                type = 'date';
                                break;
                            case 'boolean':
                                type = 'boolean';
                                break;
                            case 'double precision':
                                type = 'double';
                                break;
                            default:
                                type = 'object';
                                break;
                        }
                        if (reportColumn.isHide !== true) {
                            $scope.gridColumnDef.push({name: reportColumn.alias, displayName: reportColumn.alias,
                                type: type, enableHiding: false,
                                cellTemplate: '<div class="ui-grid-cell-contents" title="{{(COL_FIELD === null || COL_FIELD === \'\') ? \'N/A\':COL_FIELD}}">{{(COL_FIELD === null || COL_FIELD === "") ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                        }
                    });
                    $scope.gridOptions.columnDefs = $scope.gridColumnDef;
                    $scope.gridOptions.data = $scope.UIPreviewData;


                    if (!$scope.isEditModeColorInitialized) {
                        $scope.updateColorConfiguration();
                    }
                }
            };

            $scope.rowFormatter = function (row) {
                var rowColor = 'white';
                if ($scope.colorConfigData.length > 0) {
                    angular.forEach($scope.colorConfigData, function (colorCombination) {
                        if (colorCombination.columns.length > 0) {
                            var isRowValid = $scope.executeFilterOnRow(row.entity, colorCombination);
                            if (isRowValid) {
                                rowColor = colorCombination.colorName;
                            }
                        }
                    });
                }
                return rowColor;
            };


            function SortBySequence(x, y) {
                return x.sequence - y.sequence;
            }

            $scope.clearReportData = function () {
                ReportBuilderService.clearReportData(function () {
                }, function () {
                });
            };

            $scope.retrieveViewCurrencyDataRightsForUser = function () {
                ReportBuilderService.retrieveViewCurrencyDataRightsOfLoggedInUser({}, function (result) {
                    $scope.viewCurrencyDataPermission = result.data;
                });
            };
            $scope.retrieveViewCurrencyDataRightsForUser();
            $scope.retrieveReportById = function (report) {
                ReportBuilderService.retrieveCurrencyConfiguration({}, function (result) {
//                    console.log('----------------------' + JSON.stringify(result));
                    $scope.isCurrencyVisible = result.data;
                    $scope.modifiedCurrencyColumns = [];
                    report.columns.sort(SortByFieldSequence);
                    if (result.data === false || (result.data === true && $scope.viewCurrencyDataPermission === false)) {
                        angular.forEach(report.columns, function (col) {
                            if (col.componentType !== "Currency") {
                                $scope.modifiedCurrencyColumns.push(col);
                            }
                        });
                    } else {
                        $scope.modifiedCurrencyColumns = angular.copy(report.columns);
                    }
                    $scope.columns = angular.copy($scope.modifiedCurrencyColumns);
                    angular.forEach($scope.columns, function (item) {
                        item.fieldDisplayName = $rootScope.translateValue("RPRT_NM." + item.alias);
                    });
                    $scope.report = angular.copy(report);
                    $scope.models = {
                        selected: null,
                        templates: [
                            {type: "container", id: 2, text: "New-Table", columns:
                                        [[{text: "Column", colField: "column", feature: 0}]], searchData: [[{column: ""}]]}
                        ],
                        dropzones: {
                            "A": []}};
                    angular.forEach($scope.report.columns, function (col) {
                        if ($scope.models.dropzones.A != undefined) {
                            var dataCount = 0;
                            angular.forEach($scope.models.dropzones.A, function (key) {
                                var tabs = col.tableName.split(",");
                                if (tabs.indexOf(key.text) !== -1) {
                                    dataCount++;
                                    key.columns[0].push({text: col.alias, colField: col.dbBaseName + "." + col.colName, newField: true, type: "col", feature: col.feature});
                                }
                            });
                            if (dataCount == 0) {
                                var t = col.tableName.split(",");
                                angular.forEach(t, function (item) {
                                    $scope.obj = {"type": "container",
                                        allowedTypes: ['col'],
                                        "id": 1, columns: [[]], searchData: [[{column: ""}]]};
                                    $scope.obj.text = item;
                                    var colData = {text: col.alias, colField: col.dbBaseName + "." + col.colName, newField: true, type: "col", feature: col.feature};
                                    $scope.obj.columns[0].push(colData);
                                    $scope.models.dropzones.A.push($scope.obj);
                                })

                            }
                        }
                    });


                    $scope.displayName = $scope.report.reportName;
                    $scope.convertedQuery = report.query;
                    if (!$scope.isFranchiseChange) {
                        $scope.filterColumns = [];
                        $scope.filterAttributes = [];
                        $scope.initializeFilter();
                        $scope.initializeColor();
                    }
                    $rootScope.maskLoading();
                    $scope.report.convertedQuery = $scope.convertedQuery;
                    $scope.report.franchiseIds = [];
                    if ($scope.franchise !== undefined && $scope.franchise !== null) {
                        $scope.report.franchiseIds.push($scope.franchise);
                    }
                    //Initialize ordering options
                    if ($scope.report.orderAttributes !== undefined && $scope.report.orderAttributes !== null) {
                        var orderAttributes = JSON.parse($scope.report.orderAttributes);
                        angular.forEach(orderAttributes, function (orderAttr) {
                            angular.forEach($scope.columns, function (column) {
                                var fieldName;
                                if (column.alias === undefined || column.alias === '') {
                                    fieldName = column.fieldLabel;
                                } else {
                                    fieldName = column.alias;
                                }
                                if (orderAttr.columnName === column.dbBaseName + "." + column.colName) {
                                    $scope.columnOrderMap[fieldName] = orderAttr.orderValue;
                                }
                            });
                        });
                    }
                    //Initialize currency Map.
                    $scope.currencyIdList = [];
                    angular.forEach($scope.columns, function (column) {
                        if (column.componentType === 'Currency') {
                            $scope.currencyIdList.push(column.associatedCurrency);
                        }
                    });
//                console.log('currency id----' + JSON.stringify($scope.currencyIdList));
//                    ReportBuilderService.retrieveCurrencyDetails($scope.currencyIdList, function(result) {
//                    console.log(JSON.stringify(result.data));
//                        if (result.data !== null) {
//                            $scope.currencyMap = result.data;
//                        }
                    $scope.resultList = [];
                    ReportBuilderService.retrieveReportTable($scope.report, function (res) {
                        if (res.data !== undefined) {
                            if (res.data.queryValid !== undefined) {
                                var msg = "Invalid Query.";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            } else {
                                $scope.previewData = res.data.records;
                            $scope.totalItems = (res.data.totalRecords);
                                if ($scope.isGroupByCheck) {
                                    angular.forEach(res.data.records, function (value, key) {
                                        angular.forEach(value, function (item) {
                                            $scope.resultList.push(angular.copy(item));
                                        });
                                    })
                                    res.data.records = angular.copy($scope.resultList);
                                }
                                $scope.resultModels = angular.copy($scope.models);
                                if ($scope.resultModels.dropzones.A != undefined && $scope.resultModels.dropzones.A.length > 0 && res.data != undefined) {
                                    angular.forEach($scope.resultModels.dropzones.A, function (key) {
                                        key.searchData[0] = [];
                                        if (key.columns[0].length > 0) {
                                            angular.forEach(res.data.records, function (record) {
                                                var obj = {};
                                                angular.forEach(key.columns[0], function (data) {
                                                    if (record[data.text] !== undefined) {
                                                        if (record[data.text] != null) {
                                                            obj[data.text] = {value: record[data.text]};
                                                        } else {
                                                            obj[data.text] = {value: 'N/A'};
                                                        }

                                                    }
                                                });
                                                if (obj != null && obj !== undefined) {
                                                    key.searchData[0].push(obj);
                                                }
                                            })

                                        }
                                    })
                                }
//                            $scope.originalGeneratedData = res.data.records;
                                $rootScope.unMaskLoading();
                            }
                        }
                        $scope.isFilter = true;
                        if (!$scope.isFranchiseChange) {
                            $scope.retrievePaginatedData();
                        }
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
//                    }, function(res) {
//                        $rootScope.unMaskLoading();
//                    });
                }, function (failure) {
                    $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                });

            };
            $scope.setPage = function (pageNo) {
                $scope.currentPage = pageNo;
                $scope.pageChanged();
            };

            $scope.setReportPage = function (pageNo) {
//                console.log("pageNo :" + pageNo)
                $scope.reportCurrentPage = pageNo;
                $scope.retrieveReportPaginatedData();
//                $scope.pageChanged();
            };
            $scope.pageChanged = function () {
                $scope.retrievePaginatedData();
            };
            $scope.initializeFilter = function () {
                angular.forEach($scope.columns, function (column) {
                    var columnLabel;
                    if (column.alias !== null && column.alias !== '') {
                        columnLabel = column.alias;
                    } else {
                        columnLabel = column.fieldLabel;
                    }
                    var columnType = column.dataType;
                    $scope.filterColumns.push({label: columnLabel, type: columnType, componentType: column.componentType});
                });
            };
            $scope.updateFilter = function (filterColumn) {
                $scope.subbmited = false;
                if (filterColumn !== undefined) {
                    var filters = $scope.availabeFilterTypesByDataType[filterColumn.type] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[filterColumn.type];
                    $scope.filterTypes = []
                    angular.forEach(filters, function (filter) {
                        $scope.filterTypes.push({id: filter.id, text: filter.text});
                    });
                    var obj = {};
                    obj.label = filterColumn.label;
                    obj.type = filterColumn.type;
                    obj.componentType = filterColumn.componentType;
                    obj.filters = $scope.filterTypes;
                    if (obj.type === 'varchar') {
                        $scope.columnData = [];
                        obj.availableFilterValueSelectEqual = {
                            multiple: false,
                            placeholder: '  Filter Value',
                            closeOnSelect: true,
                            initSelection: function (element, callback) {
                                var data = undefined;
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }


                        };
                        obj.availableFilterValueSelectNotEqual = {
                            multiple: false,
                            placeholder: '  Filter Value',
                            closeOnSelect: true,
                            initSelection: function (element, callback) {
                                var data = undefined;
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }

                        };
                        obj.availableFilterValueSelectIn = {
                            multiple: true,
                            placeholder: '  Filter Value',
                            closeOnSelect: false,
                            initSelection: function (element, callback) {
                                var data = [];
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }


                        };
                        obj.availableFilterValueSelectNotIn = {
                            multiple: true,
                            placeholder: '  Filter Value',
                            closeOnSelect: false,
                            initSelection: function (element, callback) {
                                var data = [];
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }

                        };
                    }
                    $scope.filterAttributes.push(obj);
                    angular.forEach($scope.filterColumns, function (column, index) {
                        if (column.label === filterColumn.label) {
                            $scope.filterColumns.splice(index, 1);
                        }
                    });
                }
            };
            $scope.removeReport = function (id) {
                ReportBuilderService.retrieveReport(id, function (response) {
                    $scope.report = response;
                    $scope.report.status = 'RM';
                    ReportBuilderService.updateReport($scope.report, function (res) {
//                        console.log("Report UPDATED--" + JSON.stringify(res));
                        $scope.retrieveReportPaginatedData();
                    }, function () {
                        var msg = "Report cannot be updated.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    });
                });
            };
            $scope.showRemovePopup = function (id) {
                $scope.selectedId = id;
                $('#removePopup').modal('show');
            };
            $scope.removeOk = function () {
                $scope.removeReport($scope.selectedId);
                $('#removePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
            };
            $scope.removeCancel = function () {
                $('#removePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
            };
            $scope.removeFilterByColumn = function (index, filterForm) {
                $scope.subbmited = false;
                var filterObj = $scope.filterAttributes[index];
                $scope.filterAttributes.splice(index, 1);
                $scope.filterColumns.push({label: filterObj.label, type: filterObj.type, componentType: filterObj.componentType});
                $scope.applyFilter(filterForm);
            };
            $scope.applyFilter = function (filterForm, isSkipForm) {
                $scope.filterForm = filterForm;

                if ((filterForm !== undefined && filterForm.$valid) || (isSkipForm !== undefined && isSkipForm === true)) {
                    $rootScope.maskLoading();
                    $scope.filter = {};
                    $scope.isAnyColumnHidden = false;
                    $scope.hiddenFieldDetail = [];
                    angular.forEach($scope.columns, function (column) {
                        column.isHide = false;
                    });
                    var isAllColumnHidden = false;
                    if ($scope.columns.length === $scope.filterAttributes.length) {
                        var hiddenColumnCount = 0;
                        angular.forEach($scope.filterAttributes, function (item) {
                            if (item.hideColumn !== undefined && item.hideColumn === true) {
                                hiddenColumnCount++;
                            }
                        });
                        if (hiddenColumnCount === $scope.columns.length) {
                            isAllColumnHidden = true;
                        }
                    }
                    if (!isAllColumnHidden) {
                        console.log("$scope.filterAttributes :" + JSON.stringify($scope.filterAttributes));
                        angular.forEach($scope.filterAttributes, function (item) {
                            if (item.type === 'timestamp') {
                                var d = new Date(item.filterValue);
                                var dateToSend;
                                var month;
                                var date;
                                if ((d.getMonth() + 1) < 10) {
                                    month = "0" + (d.getMonth() + 1);
                                } else {
                                    month = (d.getMonth() + 1);
                                }
                                if (d.getDate() < 10) {
                                    date = "0" + d.getDate();
                                } else {
                                    date = d.getDate();
                                }
                                dateToSend = d.getFullYear() + "-" + month + "-" + date;
                                item.filterValue = dateToSend;
                                if (item.filterValueSecond !== undefined) {
                                    d = new Date(item.filterValueSecond);
                                    if ((d.getMonth() + 1) < 10) {
                                        month = "0" + (d.getMonth() + 1);
                                    } else {
                                        month = (d.getMonth() + 1);
                                    }
                                    if (d.getDate() < 10) {
                                        date = "0" + d.getDate();
                                    } else {
                                        date = d.getDate();
                                    }
                                    dateToSend = d.getFullYear() + "-" + month + "-" + date;
                                    item.filterValueSecond = dateToSend;
                                }
                            }
                            var key = item.label;
                            $scope.filterObj = {
                                operator: item.operator,
                                type: item.type,
                                componentType: item.componentType,
                                filterValue: item.filterValue,
                                filterValueSecond: item.filterValueSecond
                            };
                            $scope.filter[item.label] = JSON.stringify($scope.filterObj);
                            if (item.hideColumn !== undefined && item.hideColumn === true) {
                                angular.forEach($scope.columns, function (column) {
                                    var columnLabel;
                                    if (column.alias !== null && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                    if (columnLabel === item.label) {
                                        column.isHide = true;
                                        $scope.isAnyColumnHidden = true;
                                    }
                                });
                            }
//                        console.log("filter : " + JSON.stringify($scope.filter));
                        });
                        var firstLevelGroupByFields = '';
                        if ($scope.groupAttributes !== undefined) {
                            angular.forEach($scope.groupAttributes['groupBy'], function (groupByCol) {
                                if (groupByCol.level === 1) {
                                    firstLevelGroupByFields = groupByCol.fields;
                                }
                            });
                        }
                        $scope.send = {
                            filters: $scope.filter,
                            isGrouped: $scope.isGroupByCheck,
                            groupBy: $scope.groupAttributes === undefined ? null : firstLevelGroupByFields
                        };
                        $scope.currentPage = 1;
                        $scope.resultList = [];
                        ReportBuilderService.retrieveFilteredData($scope.send, function (res) {
                            if ($scope.isGroupByCheck) {
                                angular.forEach(res.data.records, function (value, key) {
                                    angular.forEach(value, function (item) {
                                        $scope.resultList.push(angular.copy(item));
                                    });
                                })
                                res.data.records = angular.copy($scope.resultList);
                            }
                            if (res.data.records.length > 0) {
                                $scope.previewData = res.data.records;
                                angular.forEach($scope.columns, function (reportColumn) {
//                                    reportColumn.total = undefined;
//                                    if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
//                                        var total = 0;
//                                        angular.forEach($scope.previewData, function (row) {
//                                            var data = row[reportColumn.alias] === undefined ? (row[reportColumn.fieldLabel] === undefined ? 0 : row[reportColumn.fieldLabel]) : row[reportColumn.alias];
//                                            total += Number(data);
//                                        });
//                                        if (isNaN(total)) {
//                                            total = 0;
//                                        }
//                                        reportColumn.total = total;
//                                    }
                                    if (reportColumn.isHide === true) {
                                        var label = (reportColumn.alias === undefined || reportColumn.alias === '') ? reportColumn.fieldLabel : reportColumn.alias;
                                        var values = [];
                                        angular.forEach($scope.previewData, function (row) {
                                            var data = (row[label] === undefined || row[label] === null) ? 'N/A' : row[label];
                                            var isValueExist = false;
                                            angular.forEach(values, function (value) {
                                                if (value === data) {
                                                    isValueExist = true;
                                                }
                                            });
                                            if (!isValueExist) {
                                                values.push(data);
                                            }
                                        });
                                        var labelValue = '';
                                        angular.forEach(values, function (item) {
                                            if (labelValue.length === 0) {
                                                labelValue = item;
                                            } else {
                                                labelValue += "," + item;
                                            }
                                        });
                                        $scope.hiddenFieldDetail.push({label: label, value: labelValue});
                                    }
                                });


                                $scope.isAnyRecordsExist = true;
                                $scope.isFilter = false;
                                $scope.resultModels = angular.copy($scope.models);
                                if ($scope.resultModels.dropzones.A != undefined && $scope.resultModels.dropzones.A.length > 0 && res.data != undefined) {
                                    angular.forEach($scope.resultModels.dropzones.A, function (key) {
                                        key.searchData[0] = [];
                                        if (key.columns[0].length > 0) {
                                            angular.forEach(res.data.records, function (record) {
                                                var obj = {};
                                                angular.forEach(key.columns[0], function (data) {
                                                    if (record[data.text] !== undefined) {
                                                        if (record[data.text] != null) {
                                                            obj[data.text] = {value: record[data.text]};
                                                        } else {
                                                            obj[data.text] = {value: 'N/A'};
                                                        }

                                                    }
                                                });
                                                if (obj != null && obj !== undefined) {
                                                    key.searchData[0].push(obj);
                                                }
                                            })

                                        }
                                    })
                                }
                            } else {
                                $scope.isAnyRecordsExist = false;
                            }

                            $rootScope.unMaskLoading();
                        }, function () {
                            $scope.currentPage = 1;
                            $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        $rootScope.addMessage("Can not hide all fields.", $rootScope.failure);
                        $rootScope.unMaskLoading();
                    }
                }

            };
            $scope.generateFilteredPdf = function () {
                var data;
                data = $scope.tempData;
                $scope.map = {'records': data};
                var colorAttributes = [];
                if ($scope.colorConfigData.length > 0) {
                    angular.forEach($scope.colorConfigData, function (colorComposition) {
                        var columnColorAttributes = [];
                        var compositeObj = {};
                        compositeObj.combinationType = colorComposition.combinationType;
                        compositeObj.colorName = colorComposition.colorName;
                        angular.forEach(colorComposition.columns, function (colorConfig) {
                            if ($scope.checkIsValidColorConfig(colorConfig)) {
                                var obj = {};
                                obj.label = colorConfig.label;
                                var isGroupBy = false;
                                angular.forEach($scope.levelMap, function (level) {
                                    var groupByFields = level.levelItems.split(",");
                                    if (groupByFields.indexOf(colorConfig.label) > -1) {
                                        isGroupBy = true;
                                    }
                                });
                                obj.isGroupBy = isGroupBy;
                                obj.type = colorConfig.type;
                                obj.componentType = colorConfig.componentType;
                                obj.operator = colorConfig.operator;
                                if (colorConfig.filterValue instanceof Object === true) {
                                    if (angular.isArray(colorConfig.filterValue)) {
                                        var temp = '';
                                        angular.forEach(colorConfig.filterValue, function (item) {
                                            if (temp.length === 0) {
                                                temp = item.id;
                                            } else {
                                                temp += "," + item.id;
                                            }
                                        });
                                        obj.filterValue = temp;
                                    } else {
                                        if (obj.type !== 'timestamp') {
                                            obj.filterValue = colorConfig.filterValue.id;
                                        } else {
                                            if (colorConfig.filterValue !== undefined) {
                                                var d = new Date(colorConfig.filterValue);
                                                var dateToSend;
                                                var month;
                                                var date;
                                                if ((d.getMonth() + 1) < 10) {
                                                    month = "0" + (d.getMonth() + 1);
                                                } else {
                                                    month = (d.getMonth() + 1);
                                                }
                                                if (d.getDate() < 10) {
                                                    date = "0" + d.getDate();
                                                } else {
                                                    date = d.getDate();
                                                }
                                                dateToSend = d.getFullYear() + "-" + month + "-" + date;
                                                obj.filterValue = dateToSend;
                                                if (colorConfig.filterValueSecond !== undefined) {
                                                    d = new Date(colorConfig.filterValueSecond);
                                                    if ((d.getMonth() + 1) < 10) {
                                                        month = "0" + (d.getMonth() + 1);
                                                    } else {
                                                        month = (d.getMonth() + 1);
                                                    }
                                                    if (d.getDate() < 10) {
                                                        date = "0" + d.getDate();
                                                    } else {
                                                        date = d.getDate();
                                                    }
                                                    dateToSend = d.getFullYear() + "-" + month + "-" + date;
                                                    obj.filterValueSecond = dateToSend;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    obj.filterValue = colorConfig.filterValue;
                                }
                                if (colorConfig.filterValueSecond !== undefined && colorConfig.filterValueSecond !== null && obj.type !== 'timestamp') {
                                    obj.filterValueSecond = colorConfig.filterValueSecond;
                                }
//                                obj.colorName = colorConfig.colorName;
                                columnColorAttributes.push(obj);
                            }
                        });
                        compositeObj.columns = columnColorAttributes;
                        if (columnColorAttributes.length > 0) {
                            colorAttributes.push(compositeObj);
                        }
                    });
                }
                var hiddenFields = [];
                if ($scope.hiddenFieldDetail.length > 0) {
                    angular.forEach($scope.hiddenFieldDetail, function (field) {
                        hiddenFields.push(field.label);
                    });
                }
                console.log("hiddenFields :" + JSON.stringify(hiddenFields));
                $scope.send = {
                    'records': $scope.map,
                    'reportId': $scope.report.id,
                    'extension': '.pdf',
                    'filterAttributes': $scope.filterAttributes,
                    'colorAttributes': colorAttributes,
                    'hiddenFields': hiddenFields
                };
                $rootScope.maskLoading();
                ReportBuilderService.generateFilteredPdf($scope.send, function () {
                    window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadfilteredpdf?reportId=" + $scope.report.id + "&extension=" + '.pdf');
                    $rootScope.unMaskLoading();
                });
            };
            $scope.generateFilteredXls = function () {
                var data;
                data = $scope.tempData;
                $scope.map = {'records': data};
                var colorAttributes = [];
                if ($scope.colorConfigData.length > 0) {
                    angular.forEach($scope.colorConfigData, function (colorComposition) {
                        var columnColorAttributes = [];
                        var compositeObj = {};
                        compositeObj.combinationType = colorComposition.combinationType;
                        compositeObj.colorName = colorComposition.colorName;
                        angular.forEach(colorComposition.columns, function (colorConfig) {
                            if ($scope.checkIsValidColorConfig(colorConfig)) {
                                var obj = {};
                                obj.label = colorConfig.label;
                                var isGroupBy = false;
                                angular.forEach($scope.levelMap, function (level) {
                                    var groupByFields = level.levelItems.split(",");
                                    if (groupByFields.indexOf(colorConfig.label) > -1) {
                                        isGroupBy = true;
                                    }
                                });
                                obj.isGroupBy = isGroupBy;
                                obj.type = colorConfig.type;
                                obj.componentType = colorConfig.componentType;
                                obj.operator = colorConfig.operator;
                                if (colorConfig.filterValue instanceof Object === true) {
                                    if (angular.isArray(colorConfig.filterValue)) {
                                        var temp = '';
                                        angular.forEach(colorConfig.filterValue, function (item) {
                                            if (temp.length === 0) {
                                                temp = item.id;
                                            } else {
                                                temp += "," + item.id;
                                            }
                                        });
                                        obj.filterValue = temp;
                                    } else {
                                        obj.filterValue = colorConfig.filterValue.id;
                                    }
                                } else {
                                    obj.filterValue = colorConfig.filterValue;
                                }
                                if (colorConfig.filterValueSecond !== undefined || colorConfig.filterValueSecond !== null) {
                                    obj.filterValueSecond = colorConfig.filterValueSecond;
                                }
//                                obj.colorName = colorConfig.colorName;
                                columnColorAttributes.push(obj);
                            }
                        });
                        compositeObj.columns = columnColorAttributes;
                        if (columnColorAttributes.length > 0) {
                            colorAttributes.push(compositeObj);
                        }
                    });
                }
                var hiddenFields = [];
                if ($scope.hiddenFieldDetail.length > 0) {
                    angular.forEach($scope.hiddenFieldDetail, function (field) {
                        hiddenFields.push(field.label);
                    });
                }
                $scope.send = {
                    'records': $scope.map,
                    'reportId': $scope.report.id,
                    'extension': '.xls',
                    'filterAttributes': $scope.filterAttributes,
                    'colorAttributes': colorAttributes,
                    'hiddenFields': hiddenFields
                };
                $rootScope.maskLoading();
                ReportBuilderService.generateFilteredPdf($scope.send, function () {
                    window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadfilteredpdf?reportId=" + $scope.report.id + "&extension=" + '.xls');
                    $rootScope.unMaskLoading();
                });
            };

            $scope.generateFullPdf = function (report) {
                $rootScope.maskLoading();
                window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadpdf?reportId=" + report.id);
                $rootScope.unMaskLoading();
            };
            $scope.generateFullExcel = function (report) {
                $rootScope.maskLoading();
                window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadexcel?reportId=" + report.id);
                $rootScope.unMaskLoading();
            };
            $scope.generateFullXml = function (report) {
                $rootScope.maskLoading();
                window.location.href = $rootScope.appendAuthToken($rootScope.apipath + "report/downloadxml?reportId=" + report.id);
                $rootScope.unMaskLoading();
            };
            //            $scope.hidePreviewModal = function() {
//                $("#previewReportModal").modal("hide");
//            };
            $scope.cancelPreview = function () {
                $scope.isPreview = false;
                $scope.isFilter = true;
                $scope.isHKAdmin = false;
                $scope.isFranchiseChange = false;
                $scope.currentColor = {};
                $scope.colorConfigData = [];
                $scope.colorConfig = {};
                $scope.clearReportData();
                $scope.isEditModeColorInitialized = false;
                $scope.isAnyColumnHidden = false;
                $scope.isAnyRecordsExist = false;
            };
            //Method to filter according to Franchise. Will be available to HKAdmin.
            $scope.initializeFranchise = function () {
                if ($rootScope.session !== undefined) {
                    //$rootScope.session.isHKAdmin
                    if ($rootScope.session.isHKAdmin) {
                        FranchiseService.retrieveAllFranchise({tree: false}, function (res1) {
                            var res = [];
                            angular.forEach(res1, function (item) {
                                item.franchiseName = $rootScope.translateValue("FRNCSE_NM." + item.franchiseName);
                                res.push(item);
                            });
                            $scope.franchiseModelList = res;
                            $scope.existingFranchiseList = [];
                            var data = [];
                            for (var i = 0; i < res.length; i++) {
                                $scope.existingFranchiseList.push({"id": res[i].id, "displayName": res[i].franchiseName});
                                data.push({id: res[i].id, text: res[i].franchiseName});
                            }
                            $scope.isHKAdmin = true;
                            $scope.franchiseSelectCombo = {
                                multiple: true,
                                placeholder: 'Select Franchise',
                                closeOnSelect: false,
                                data: data
                            };
                        }, function () {
                            $rootScope.addMessage("Failed to retrieve franchises", 1);
                        });
                    } else {
                        $scope.isHKAdmin = false;
                    }
                }
            };
            $scope.repopulateData = function (franchiseId) {
                $scope.franchise = franchiseId;
                $scope.isFranchiseChange = true;
                $scope.retrieveReportById($scope.report);
            };
            //Methods for Color purpose
            $scope.initializeColor = function () {
                $scope.colorColumns = [];
                $scope.isGroupedReport();
//                console.log("group in color :" + $scope.isGroupByCheck);
                if (!$scope.isGroupByCheck) {
                    angular.forEach($scope.columns, function (column) {
                        var columnLabel;
                        if (column.alias !== null && column.alias !== '') {
                            columnLabel = column.alias;
                        } else {
                            columnLabel = column.fieldLabel;
                        }
                        var columnType = column.dataType;
                        $scope.colorColumns.push({label: columnLabel, type: columnType, componentType: column.componentType});
                    });
                } else {
                    $scope.groupAttributes = JSON.parse($scope.report.groupAttributes);
                    var groupByFields = [];
                    angular.forEach($scope.groupAttributes['groupBy'], function (item) {
                        if (item.level === 1) {
                            groupByFields = item.fields.split(",");
                        }
                    });
                    angular.forEach(groupByFields, function (grpByField) {
                        angular.forEach($scope.columns, function (column) {
                            var columnLabel;
                            if (column.alias !== null && column.alias !== '') {
                                columnLabel = column.alias;
                            } else {
                                columnLabel = column.fieldLabel;
                            }
                            var columnType = column.dataType;
                            if (columnLabel === grpByField) {
                                $scope.colorColumns.push({label: columnLabel, type: columnType, componentType: column.componentType});
                            }
                        });
                    });
                }
                //Add a default color combination
                $scope.colorConfigData = [];
                var obj = {};
                obj.combinationType = 'ANY';
                obj.columns = [];
                obj.colorColumns = $scope.colorColumns;
                obj.combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                $scope.colorConfigData.push(obj);
            };
            $scope.updateColorConfiguration = function () {
                $scope.isEditModeColorInitialized = true;
                if ($scope.report.colorAttributes !== undefined && $scope.report.colorAttributes !== null && $scope.report.colorAttributes !== '') {
                    var colorAttributes = JSON.parse($scope.report.colorAttributes);
                    if (colorAttributes.length > 0) {
                        $scope.colorConfigData = [];
                        angular.forEach(colorAttributes, function (colorCombination) {
                            var colorComposition = {};
                            colorComposition.combinationType = colorCombination.combinationType;
                            colorComposition.colorName = colorCombination.colorName;
                            colorComposition.colorColumns = $scope.colorColumns;
                            colorComposition.combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                            var columnColorAttributes = [];
                            angular.forEach(colorCombination.columns, function (colorColumn) {
                                var isColorColumnExists = false;
                                angular.forEach($scope.colorColumns, function (origColumn) {
                                    if (origColumn.label === colorColumn.label) {
                                        isColorColumnExists = true;
                                    }
                                });
                                if (isColorColumnExists) {
                                    var obj = {};
                                    obj.label = colorColumn.label;
                                    obj.type = colorColumn.type;
                                    obj.componentType = colorColumn.componentType;
                                    obj.label = colorColumn.label;
                                    var filters = $scope.availabeFilterTypesByDataType[colorColumn.type] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[colorColumn.type];
                                    if (colorColumn.componentType === "Date range") {
                                        filters = [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}];
                                    }
                                    obj.filters = filters;
                                    obj.filterValue = colorColumn.filterValue;
                                    obj.filterValueSecond = colorColumn.filterValueSecond;
                                    var operator;
                                    angular.forEach(filters, function (filter) {
                                        if (filter.id === colorColumn.operator) {
                                            operator = filter;
                                        }
                                    });
                                    obj.operator = operator.id;
                                    if (obj.type === 'varchar') {
                                        $scope.colorColumnData = [];
                                        obj.availableFilterValueSelectEqual = {
                                            multiple: false,
                                            placeholder: '  Filter Value',
                                            closeOnSelect: true,
                                            initSelection: function (element, callback) {
                                                var data = {};
                                                data = {id: colorColumn.filterValue, text: colorColumn.filterValue};
                                                callback(data);
                                            },
                                            query: function (query) {
                                                var page = query.page - 1;
                                                var payload = {
                                                    q: query.term, //search term
                                                    page_limit: 10, // page size
                                                    page: page, // page numbe
                                                    col_name: obj.label,
                                                    isGrouped: $scope.isGroupByCheck
                                                };
                                                $scope.names = [];
                                                var success = function (data) {

                                                    $scope.names = [];
                                                    if (data.length !== 0) {
                                                        $scope.names = [];
                                                        angular.forEach(data.columnValues, function (item) {

                                                            $scope.names.push({
                                                                id: item,
                                                                text: item
                                                            });
                                                        });
                                                    }
                                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                                    query.callback({
                                                        results: $scope.names, more: more
                                                    });
                                                };
                                                var failure = function () {
                                                };
                                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                                            }


                                        };
                                        obj.availableFilterValueSelectNotEqual = {
                                            multiple: false,
                                            placeholder: '  Filter Value',
                                            closeOnSelect: true,
                                            initSelection: function (element, callback) {
                                                var data = {};
                                                data = {id: colorColumn.filterValue, text: colorColumn.filterValue};
                                                callback(data);
                                            },
                                            query: function (query) {
                                                var page = query.page - 1;
                                                var payload = {
                                                    q: query.term, //search term
                                                    page_limit: 10, // page size
                                                    page: page, // page numbe
                                                    col_name: obj.label,
                                                    isGrouped: $scope.isGroupByCheck
                                                };
                                                $scope.names = [];
                                                var success = function (data) {

                                                    $scope.names = [];
                                                    if (data.length !== 0) {
                                                        $scope.names = [];
                                                        angular.forEach(data.columnValues, function (item) {

                                                            $scope.names.push({
                                                                id: item,
                                                                text: item
                                                            });
                                                        });
                                                    }
                                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                                    query.callback({
                                                        results: $scope.names, more: more
                                                    });
                                                };
                                                var failure = function () {
                                                };
                                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                                            }

                                        };
                                        obj.availableFilterValueSelectIn = {
                                            multiple: true,
                                            placeholder: '  Filter Value',
                                            closeOnSelect: false,
                                            initSelection: function (element, callback) {
                                                var data = [];
                                                angular.forEach(colorColumn.filterValue.split(","), function (item) {
                                                    data.push({id: item, text: item});
                                                });
                                                callback(data);
                                            },
                                            query: function (query) {
                                                var page = query.page - 1;
                                                var payload = {
                                                    q: query.term, //search term
                                                    page_limit: 10, // page size
                                                    page: page, // page numbe
                                                    col_name: obj.label,
                                                    isGrouped: $scope.isGroupByCheck
                                                };
                                                $scope.names = [];
                                                var success = function (data) {

                                                    $scope.names = [];
                                                    if (data.length !== 0) {
                                                        $scope.names = [];
                                                        angular.forEach(data.columnValues, function (item) {

                                                            $scope.names.push({
                                                                id: item,
                                                                text: item
                                                            });
                                                        });
                                                    }
                                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                                    query.callback({
                                                        results: $scope.names, more: more
                                                    });
                                                };
                                                var failure = function () {
                                                };
                                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                                            }


                                        };
                                        obj.availableFilterValueSelectNotIn = {
                                            multiple: true,
                                            placeholder: '  Filter Value',
                                            closeOnSelect: false,
                                            initSelection: function (element, callback) {
                                                var data = [];
                                                angular.forEach(colorColumn.filterValue.split(","), function (item) {
                                                    data.push({id: item, text: item});
                                                });
                                                callback(data);
                                            },
                                            query: function (query) {
                                                var page = query.page - 1;
                                                var payload = {
                                                    q: query.term, //search term
                                                    page_limit: 10, // page size
                                                    page: page, // page numbe
                                                    col_name: obj.label,
                                                    isGrouped: $scope.isGroupByCheck
                                                };
                                                $scope.names = [];
                                                var success = function (data) {

                                                    $scope.names = [];
                                                    if (data.length !== 0) {
                                                        $scope.names = [];
                                                        angular.forEach(data.columnValues, function (item) {

                                                            $scope.names.push({
                                                                id: item,
                                                                text: item
                                                            });
                                                        });
                                                    }
                                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                                    query.callback({
                                                        results: $scope.names, more: more
                                                    });
                                                };
                                                var failure = function () {
                                                };
                                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                                            }

                                        };
                                    }
                                    columnColorAttributes.push(obj);
                                }
                            });
                            colorComposition.columns = angular.copy(columnColorAttributes);
                            $scope.colorConfigData.push(colorComposition);
                        });
//                        console.log('colorConfigData---' + JSON.stringify($scope.colorConfigData));
                        //Apply coloring if required.
                        if ($scope.colorConfigData.length > 0) {
                            $scope.applyColor($scope.filterForm, true);
                        }
                    }
                }
            };
            $scope.updateColorColumnFilter = function (colorColumn, index) {
                $scope.subbmited = false;
                if (colorColumn !== undefined && colorColumn !== null) {
//                    $scope.currentColor.colorFilterOperator = undefined;
//                    $scope.currentColor.filterValue = undefined;
//                    $scope.currentColor.filterValueSecond = undefined;
//                    $scope.currentColor.colorName = undefined;
//                    $scope.currentColor.isColorAdded = false;
                    var filters = $scope.availabeFilterTypesByDataType[colorColumn.type] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[colorColumn.type];
                    var obj = {};
                    obj.label = colorColumn.label;
                    obj.type = colorColumn.type;
                    obj.componentType = colorColumn.componentType;
                    if (colorColumn.componentType === "Date range") {
                        filters = [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}];
                    }
                    obj.filters = filters;
                    if (colorColumn.type === 'varchar') {
                        $scope.colorColumnData = [];
                        obj.availableFilterValueSelectEqual = {
                            multiple: false,
                            placeholder: '  Filter Value',
                            closeOnSelect: true,
                            initSelection: function (element, callback) {
                                var data = undefined;
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }

                        };
                        obj.availableFilterValueSelectNotEqual = {
                            multiple: false,
                            placeholder: '  Filter Value',
                            closeOnSelect: true,
                            initSelection: function (element, callback) {
                                var data = undefined;
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }

                        };
                        obj.availableFilterValueSelectIn = {
                            multiple: true,
                            placeholder: '  Filter Value',
                            closeOnSelect: false,
                            initSelection: function (element, callback) {
                                var data = [];
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }


                        };
                        obj.availableFilterValueSelectNotIn = {
                            multiple: true,
                            placeholder: '  Filter Value',
                            closeOnSelect: false,
                            initSelection: function (element, callback) {
                                var data = [];
                                callback(data);
                            },
                            query: function (query) {
                                var page = query.page - 1;
                                var payload = {
                                    q: query.term, //search term
                                    page_limit: 10, // page size
                                    page: page, // page numbe
                                    col_name: obj.label,
                                    isGrouped: $scope.isGroupByCheck
                                };
                                $scope.names = [];
                                var success = function (data) {

                                    $scope.names = [];
                                    if (data.length !== 0) {
                                        $scope.names = [];
                                        angular.forEach(data.columnValues, function (item) {

                                            $scope.names.push({
                                                id: item,
                                                text: item
                                            });
                                        });
                                    }
                                    var more = (page * 10) < data.total; // whether or not there are more results available
                                    query.callback({
                                        results: $scope.names, more: more
                                    });
                                };
                                var failure = function () {
                                };
                                ReportBuilderService.retrieveLimitedColumnValues(payload, success, failure);
                            }

                        };
                    }
                    $scope.colorConfigData[index].columns.push(obj);
                    $scope.colorConfigData[index].currentColumn = undefined;
                }
            };
//            $scope.addColor = function(filterForm) {
//                $scope.currentColor.isColorAdded = true;
//                if (filterForm !== undefined && filterForm.$valid) {
//                    var obj = {};
//                    obj.label = $scope.currentColor.colorColumn.label;
//                    obj.type = $scope.currentColor.colorColumn.type;
//                    obj.operator = $scope.currentColor.colorFilterOperator.id;
//                    obj.operatorLabel = $scope.currentColor.colorFilterOperator.text;
//                    obj.filterValue = $scope.currentColor.filterValue;
//                    if ($scope.currentColor.filterValueSecond !== undefined) {
//                        obj.filterValueSecond = $scope.currentColor.filterValueSecond;
//                    }
//                    obj.colorName = $scope.currentColor.colorName;
//                    $scope.colorConfigData.push(obj);
//                    $scope.currentColor = {};
//                }
//            };
            $scope.removeColor = function (index, parentIndex) {
                $scope.subbmited = false;
                if ($scope.colorConfigData[parentIndex].columns.length > 0) {
                    $scope.colorConfigData[parentIndex].columns.splice(index, 1);
                }
                if ($scope.colorConfigData[parentIndex].columns.length === 0 && parentIndex !== 0) {
                    $scope.removeColorCombination(parentIndex);
                }
                $scope.applyColor($scope.filterForm);
            };
            $scope.removeColorCombination = function (index) {
                $scope.subbmited = false;
                if ($scope.colorConfigData.length === 1) {
                    $scope.colorConfigData[0].combinationType = 'ANY';
                    $scope.colorConfigData[0].columns = [];
                    $scope.colorConfigData[0].colorColumns = $scope.colorColumns;
                    $scope.colorConfigData[0].combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                    $scope.colorConfigData[0].colorName = undefined;
                } else {
                    $scope.colorConfigData.splice(index, 1);
                }
            };
            $scope.addColorCombination = function () {
                $scope.subbmited = false;
                //Check if Previous Combination got any criterias.
                if ($scope.colorConfigData[$scope.colorConfigData.length - 1].columns.length > 0) {
                    var obj = {};
                    obj.combinationType = 'ANY';
                    obj.columns = [];
                    obj.colorColumns = $scope.colorColumns;
                    obj.combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                    $scope.colorConfigData.push(obj);
                } else {
                    $rootScope.addMessage('Select atleast one criteria field to add new.', $rootScope.failure);
                }
            };

            $scope.applyColor = function (filterForm, isSkipForm) {
                if ((filterForm !== undefined && filterForm.$valid) || (isSkipForm !== undefined && isSkipForm === true)) {
                    $scope.filterForm = filterForm;
                    if ($scope.colorConfigData.length > 0) {
                        console.log("$scope.previewData :"+JSON.stringify($scope.previewData))
                        if ($scope.previewData.length > 0) {
                            if (true) {
                                var previewData = angular.copy($scope.previewData);

                                angular.forEach($scope.resultModels.dropzones.A, function (key) {
                                    if (key.columns[0].length > 0) {
                                        angular.forEach(key.searchData[0], function (data, rowIndex) {
                                            key.searchData[0][rowIndex].rowColor = 'white';
                                            angular.forEach(data, function (value, key1) {
                                                key.searchData[0][rowIndex][key1].color = 'white';
                                            });
                                        });
                                    }
                                });

                                angular.forEach($scope.resultModels.dropzones.A, function (key) {
                                    if (key.columns[0].length > 0) {
                                        angular.forEach(key.searchData[0], function (data, rowIndex) {
                                            var isRowSelected = false;
                                            var rowColor = 'white';
                                            angular.forEach($scope.colorConfigData, function (colorCombination) {
                                                if (colorCombination.columns.length > 0) {
                                                    var isRowValid = $scope.executeFilterOnRow(data, colorCombination);
                                                    if (isRowValid) {
                                                        isRowSelected = true;
                                                        rowColor = colorCombination.colorName;
                                                    }
                                                }
                                            });
                                            console.log("data :"+JSON.stringify(data));
                                            console.log("isRowSelected :"+JSON.stringify(isRowSelected));
                                            if (isRowSelected) {
                                                angular.forEach(data, function (value, key1) {
                                                    key.searchData[0][rowIndex][key1].color = rowColor;
                                                });
                                                key.searchData[0][rowIndex].rowColor = rowColor;
                                            }
                                        });
                                    }
                                });

//                                angular.forEach(previewData, function (data, rowIndex) {
//                                    previewData[rowIndex].rowColor = 'white';
//                                    angular.forEach(data, function (value, key) {
//                                        previewData[rowIndex][key] = {value: value, color: 'white'};
//                                    });
//                                });
//                                angular.forEach(previewData, function (data, rowIndex) {
//                                    var isRowSelected = false;
//                                    var rowColor = 'white';
//                                    angular.forEach($scope.colorConfigData, function (colorCombination) {
//                                        if (colorCombination.columns.length > 0) {
//                                            var isRowValid = $scope.executeFilterOnRow(data, colorCombination);
//                                            if (isRowValid) {
//                                                isRowSelected = true;
//                                                rowColor = colorCombination.colorName;
//                                            }
//                                        }
//                                    });
//                                    if (isRowSelected) {
//                                        angular.forEach(data, function (value, key) {
//                                            previewData[rowIndex][key].color = rowColor;
//                                        });
//                                        previewData[rowIndex].rowColor = rowColor;
//                                    }
//                                });
//                                console.log("previwDate----" + JSON.stringify(previewData));
//                                $scope.UIPreviewData = angular.copy(previewData);
                            } else {
                                var previewData = angular.copy($scope.previewData);
                                angular.forEach(previewData, function (item, index) {
                                    angular.forEach(item.groupRows, function (data, rowIndex) {
                                        previewData[index].groupRows[rowIndex].rowColor = 'white';
                                        angular.forEach(data, function (value, key) {
                                            previewData[index].groupRows[rowIndex][key] = {value: value, color: 'white'};
                                        });
                                    });
                                });
                                angular.forEach(previewData, function (item, index) {
                                    //Coloring only applied to group by columns.
                                    var values = item.groupRows;
                                    var isGroupSelected = false;
                                    var groupColor;
                                    $scope.groupAttributes = JSON.parse($scope.report.groupAttributes);
                                    var groupByFields;
                                    angular.forEach($scope.groupAttributes['groupBy'], function (item) {
                                        if (item.level === 1) {
                                            groupByFields = item.fields.split(",");
                                        }
                                    });
                                    var tempRow = {};
                                    angular.forEach(groupByFields, function (grpByField, grpIndex) {
                                        var data;
                                        angular.forEach(values, function (rowValue) {
                                            data = rowValue[grpByField];
                                        });
                                        tempRow[grpByField] = data;
                                    });
                                    angular.forEach($scope.colorConfigData, function (colorCombination) {
                                        if (colorCombination.columns.length > 0) {
                                            var isGrpValid = $scope.executeFilterOnRow(tempRow, colorCombination);
                                            if (isGrpValid) {
                                                isGroupSelected = true;
                                                groupColor = colorCombination.colorName;
                                            }
                                        }
                                    });
                                    if (isGroupSelected) {
                                        angular.forEach(item.groupRows, function (data, rowIndex) {
                                            angular.forEach(data, function (value, key) {
                                                previewData[index].groupRows[rowIndex][key].color = groupColor;
                                            });
                                            previewData[index].groupRows[rowIndex].rowColor = groupColor;
                                        });
                                    }
                                });
//                                console.log("previwDategroup----" + JSON.stringify(previewData));
                                $scope.UIPreviewData = angular.copy(previewData);
                            }
                        }
                    } else {
                        if (!$scope.isGroupByCheck) {
                            angular.forEach($scope.UIPreviewData, function (data, rowIndex) {
                                $scope.UIPreviewData[rowIndex].rowColor = 'white';
                                angular.forEach(data, function (value, key) {
                                    $scope.UIPreviewData[rowIndex][key].color = 'white';
                                });
                            });
                        } else {
                            angular.forEach($scope.UIPreviewData, function (group, grpIndex) {
                                angular.forEach(group.value, function (grpRow, rowIndex) {
                                    $scope.UIPreviewData[grpIndex].groupRows[rowIndex].rowColor = 'white';
                                    angular.forEach(grpRow, function (value, key) {
                                        $scope.UIPreviewData[grpIndex].groupRows[rowIndex][key].color = 'white';
                                    });
                                });
                            });
                        }
                    }
                }
            };
            //Check if we have a valid item in color config list.
            //May not required if form got invalid.
            $scope.checkIsValidColorConfig = function (colorConfig) {
                if (colorConfig !== undefined) {
                    if (colorConfig.operator === undefined || colorConfig.operator === null) {
                        return false;
                    }
                    if (colorConfig.filterValue === undefined && colorConfig.operator !== 'is null' && colorConfig.operator !== 'is not null') {
                        return false;
                    }
                    if (colorConfig.operator === 'between' && (colorConfig.filterValueSecond === undefined || colorConfig.filterValueSecond === null)) {
                        return false;
                    }
                    return true;
                }
                return false;
            };
            $scope.executeFilterOnRow = function (row, filterBlock) {
                if (filterBlock.combinationType === 'ANY') {
                    var isRowSelected = false;
                    angular.forEach(row, function (value, key) {
                        if (isRowSelected === false) {
                            angular.forEach(filterBlock.columns, function (colorConfig) {
                                if (key === colorConfig.label && isRowSelected === false) {
                                    if ($scope.checkIsValidColorConfig(colorConfig)) {
                                        var tempColorConfig = angular.copy(colorConfig);
                                        if (tempColorConfig.type === 'timestamp') {
                                            var d = new Date(tempColorConfig.filterValue);
                                            //Stripe time component from the date.
                                            tempColorConfig.filterValue = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                                            if (tempColorConfig.filterValueSecond !== undefined) {
                                                d = new Date(tempColorConfig.filterValueSecond);
                                                //Stripe time component from the date.
                                                tempColorConfig.filterValueSecond = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                                            }
                                        }
                                        var isSelected = $scope.executeFilter(value.value, tempColorConfig);
                                        if (isSelected) {
                                            isRowSelected = true;
                                        }
                                    }
                                }
                            });
                        }
                    });
                    return isRowSelected;
                } else {
                    if (filterBlock.columns.length > 0) {
                        var isValid = true;
                        angular.forEach(filterBlock.columns, function (colorConfig) {
                            if (isValid === true) {
                                angular.forEach(row, function (value, key) {
                                    if (key === colorConfig.label && isValid === true) {
                                        if ($scope.checkIsValidColorConfig(colorConfig)) {
                                            var tempColorConfig = angular.copy(colorConfig);
                                            if (tempColorConfig.type === 'timestamp') {
                                                var d = new Date(tempColorConfig.filterValue);
                                                //Stripe time component from the date.
                                                tempColorConfig.filterValue = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                                                if (tempColorConfig.filterValueSecond !== undefined) {
                                                    d = new Date(tempColorConfig.filterValueSecond);
                                                    //Stripe time component from the date.
                                                    tempColorConfig.filterValueSecond = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                                                }
                                            }
                                            var isSelect = $scope.executeFilter(value.value, tempColorConfig);
                                            if (isSelect === false) {
                                                isValid = false;
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        return isValid;
                    } else {
                        return false;
                    }
                }
            };
            $scope.executeFilter = function (value, colorConfig) {
                if (((value !== undefined && value !== null && value !== '') || (colorConfig.type === 'double precision') || (colorConfig.type === 'int8'))
                        && (colorConfig.operator !== 'is null' && colorConfig.operator !== 'is not null')) {
                    var isSelected = false;
                    var timestampValues = [];
                    if (!(value !== undefined && value !== null && value !== '')) {
                        value = 0;
                    }
                    if (colorConfig.type === 'timestamp') {
                        if (colorConfig.componentType !== 'Date range') {
                            var dateValues = value.substring(0, 10).split("/");
                            value = new Date(parseInt(dateValues[2]), parseInt(dateValues[1]) - 1, parseInt(dateValues[0]));
                        } else {
                            var timestampValue = value.split("to");
                            var fromDateValue = timestampValue[0].trim().substring(0, 10).split("/");
                            value = new Date(fromDateValue[2], parseInt(fromDateValue[1]) - 1, fromDateValue[0]);
                            timestampValues.push(value);
                            var toDateValue = timestampValue[1].trim().substring(0, 10).split("/");
                            value = new Date(toDateValue[2], parseInt(toDateValue[1]) - 1, toDateValue[0]);
                            timestampValues.push(value);
                        }
                    } else if (colorConfig.componentType === 'Currency') {
                        value = parseFloat(value.split(" ")[0].trim());
                    } else if (colorConfig.filterValue instanceof Object) {
                        if (!angular.isArray(colorConfig.filterValue)) {
                            colorConfig.filterValue = colorConfig.filterValue.id;
                        } else {
                            var temp = '';
                            angular.forEach(colorConfig.filterValue, function (item) {
                                if (temp.length === 0) {
                                    temp = item.id;
                                } else {
                                    temp += "," + item.id;
                                }
                            });
                            colorConfig.filterValue = temp;
                        }
                    }
                    switch (colorConfig.type) {
                        case 'varchar':
                            switch (colorConfig.operator) {
                                case '=':
                                    if (value === colorConfig.filterValue) {
                                        isSelected = true;
                                    }
                                    break;
                                case '!=':
                                    if (value !== colorConfig.filterValue) {
                                        isSelected = true;
                                    }
                                    break;
                                case 'in':
                                    var cmpValues = colorConfig.filterValue.split(",");
                                    if (cmpValues.indexOf(value) > -1) {
                                        isSelected = true;
                                    }
                                    break;
                                case 'not in':
                                    var cmpValues = colorConfig.filterValue.split(",");
                                    if (cmpValues.indexOf(value) === -1) {
                                        isSelected = true;
                                    }
                                    break;
                                case 'like':
                                    colorConfig.filterValue = '%' + colorConfig.filterValue + '%';
                                    var newValue = colorConfig.filterValue.replace(/%/g, ".*");
                                    newValue = newValue.replace(/_/g, ".");
                                    if (value.match(newValue)) {
                                        isSelected = true;
                                    }
                                    break;
                            }
                            break;
                        case 'int8':
                            switch (colorConfig.operator) {
                                case '=':
                                    if (parseInt(value) === parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '!=':
                                    if (parseInt(value) !== parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '>':
                                    if (parseInt(value) > parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '>=':
                                    if (parseInt(value) >= parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '<':
                                    if (parseInt(value) < parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '<=':
                                    if (parseInt(value) <= parseInt(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case 'between':
                                    if (parseInt(value) >= parseInt(colorConfig.filterValue) && parseInt(value) <= parseInt(colorConfig.filterValueSecond)) {
                                        isSelected = true;
                                    }
                                    break;
                            }
                            break;
                        case 'double precision':
                            switch (colorConfig.operator) {
                                case '=':
                                    if (parseFloat(value) === parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '!=':
                                    if (parseFloat(value) !== parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '>':
                                    if (parseFloat(value) > parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '>=':
                                    if (parseFloat(value) >= parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '<':
                                    if (parseFloat(value) < parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case '<=':
                                    if (parseFloat(value) <= parseFloat(colorConfig.filterValue)) {
                                        isSelected = true;
                                    }
                                    break;
                                case 'between':
                                    if (parseFloat(value) >= parseFloat(colorConfig.filterValue) && parseFloat(value) <= parseFloat(colorConfig.filterValueSecond)) {
                                        isSelected = true;
                                    }
                                    break;
                            }
                            break;
                        case 'timestamp':
                            if (colorConfig.componentType === 'Date range') {
                                if (colorConfig.operator === '=') {
                                    if (timestampValues[0].getTime() <= colorConfig.filterValue.getTime() && timestampValues[1].getTime() >= colorConfig.filterValue.getTime()) {
                                        isSelected = true;
                                    }
                                } else if (colorConfig.operator === '!=') {
                                    if (timestampValues[0].getTime() > colorConfig.filterValue.getTime() || timestampValues[1].getTime() < colorConfig.filterValue.getTime()) {
                                        isSelected = true;
                                    }
                                }
                            } else {
                                switch (colorConfig.operator) {
                                    case '=':
                                        if (value.getTime() === colorConfig.filterValue.getTime()) {
                                            isSelected = true;
                                        }
                                        break;
                                    case '!=':
                                        if (value.getTime() !== colorConfig.filterValue.getTime()) {
                                            isSelected = true;
                                        }
                                        break;
                                    case '>':
                                        if (value.getTime() > colorConfig.filterValue.getTime()) {
                                            isSelected = true;
                                        }
                                        break;
                                    case '<':
                                        if (value.getTime() < colorConfig.filterValue.getTime()) {
                                            isSelected = true;
                                        }
                                        break;
                                    case 'between':
                                        if (value.getTime() >= colorConfig.filterValue.getTime() && value.getTime() <= colorConfig.filterValueSecond.getTime()) {
                                            isSelected = true;
                                        }
                                        break;
                                }
                            }
                            break;
                        case 'boolean':
                            switch (colorConfig.operator) {
                                case '=':
                                    if (value === colorConfig.filterValue) {
                                        isSelected = true;
                                    }
                                    break;
                                case '!=':
                                    if (value !== colorConfig.filterValue) {
                                        isSelected = true;
                                    }
                                    break;
                            }
                            break;
                    }
                    return isSelected;
                } else if (colorConfig.operator === 'is null' || colorConfig.operator === 'is not null') {
                    if ((value === undefined || value === null || value === '') && colorConfig.operator === 'is null') {
                        return true;
                    }
                    else if (!(value === undefined || value === null || value === '') && colorConfig.operator === 'is not null') {
                        return true;
                    } else {
                        return false;
                    }
                } else {
//                    console.log('value is empty or null or undefined');
                    return false;
                }
            };

            $scope.checkValidColorRange = function (operator, dataType, firstValue, secondValue, firstField, secondField) {
                firstField.$setValidity('invalidRange', true);
                if (secondField !== undefined && secondField !== null) {
                    secondField.$setValidity('invalidRange', true);
                }
                var actualFirstValue;
                var actualSecondValue;
                if (operator === 'between') {

                    if (firstValue === undefined || firstValue === null || firstValue.length === 0
                            || secondValue === undefined || secondValue === null || secondValue.length === 0) {
                        return;
                    }
                    switch (dataType) {
                        case 'int8':
                            actualFirstValue = parseInt(firstValue);
                            actualSecondValue = parseInt(secondValue);
                            break;
                        case 'double precision':
                            actualFirstValue = parseFloat(firstValue);
                            actualSecondValue = parseFloat(secondValue);
                            break;
                        case 'timestamp':
                            actualFirstValue = angular.copy(new Date(firstValue));
                            actualSecondValue = angular.copy(new Date(secondValue));
                            break;
                    }
                    if (actualFirstValue > actualSecondValue) {
                        firstField.$setValidity('invalidRange', false);
                        secondField.$setValidity('invalidRange', false);

                    }
                }
            };

            /**
             * 
             * Methods for multi level grouping in UI
             */
            //Work around for angular ordering issues.
            //Fields containing space, dot or dash can't be ordered(solved.)
            $scope.predicate = function (field) {
                return function (item) {
                    return item[field];
                };
            };

            $scope.applyMultipleOrdering = function multipleOrdering(group, origKeys) {
                var keys = angular.copy(origKeys);
                var resultGroups = [];
                if (group.value.length > 0) {
                    var sortInverse = false;
                    if ($scope.columnOrderMap[keys[0]] !== undefined && $scope.columnOrderMap[keys[0]] === 'desc') {
                        sortInverse = true;
                    }
                    group.value = $filter('orderBy')(group.value, $scope.predicate(keys[0]), sortInverse);
                    var currentValue = '****NO MATCH****';
                    var array = [];
                    angular.forEach(group.value, function (grpValue, index) {
                        var nowValue = '';
                        if (keys.length > 0) {
                            var key = keys[0];
                            if (key.indexOf('-') === 0) {
                                key = key.substr(1, key.length - 1);
                            }
                            if (nowValue.length === 0) {
                                if (grpValue[key] === null || grpValue[key] === undefined) {
                                    nowValue = 'null';
                                } else {
                                    nowValue = grpValue[key];
                                }
                            } else {
                                if (grpValue[key] === null || grpValue[key] === undefined) {
                                    nowValue += ',' + 'null';
                                } else {
                                    nowValue += ',' + grpValue[key];
                                }
                            }
                        }
                        if (currentValue !== nowValue) {
                            if (index !== 0) {
                                var obj = {};
                                obj.key = currentValue;
                                //Apply order in values (not group by values)
                                //                                array = $filter('orderBy')(array, 'pin');
                                obj.value = angular.copy(array);
                                resultGroups.push(obj);
                            }
                            currentValue = nowValue;
                            array = [];
                            array.push(grpValue);
                        } else {
                            array.push(grpValue);
                        }
                        if (index === group.value.length - 1) {
                            var obj1 = {};
                            obj1.key = currentValue;
                            //Apply order in values (not group by values)
                            //                            array = $filter('orderBy')(array, 'pin');
                            obj1.value = angular.copy(array);
                            resultGroups.push(obj1);
                        }
                    });
                    keys.splice(0, 1);
                    if (keys.length > 0) {
                        angular.forEach(resultGroups, function (resGroup) {
                            resGroup.value = multipleOrdering(resGroup, keys);
                        });
                    }
                }
                return resultGroups;
            };

            $scope.combineMultipleOrdering = function combineOrdering(data, keyLengthInput) {
                var keyLength = keyLengthInput;
                var allRows = [];
                if (keyLength > 1) {
                    angular.forEach(data, function (group) {
                        var finalValues = combineOrdering(group.value, (keyLength - 1));
                        angular.forEach(finalValues, function (item) {
                            allRows.push(item);
                        });
                    });
                } else {
                    angular.forEach(data, function (group) {
                        angular.forEach(group.value, function (item) {
                            allRows.push(item);
                        });
                    });
                }
                // console.log('allrows----'+JSON.stringify(allRows));
                return allRows;
            };

            $scope.divideGroup = function (group, keyLabel, valueFields) {
                var resultGroups = [];
                if (group.value.length > 0) {
                    var keys = keyLabel.split(",");
                    // group.value = $filter('orderBy')(group.value, keys);
                    //Work around to avoid angular ordering issues.
                    var temp = $scope.applyMultipleOrdering(group, keys);
                    // console.log(JSON.stringify(temp));
                    group.value = $scope.combineMultipleOrdering(temp, keys.length);
                    var currentValue = '****NO MATCH****';
                    var array = [];
                    angular.forEach(group.value, function (grpValue, index) {
                        var nowValue = '';
                        if (keys.length > 0) {
                            angular.forEach(keys, function (key) {
                                if (key.indexOf('-') === 0) {
                                    key = key.substr(1, key.length - 1);
                                }
                                if (nowValue.length === 0) {
                                    if (grpValue[key] === null || grpValue[key] === undefined) {
                                        nowValue = 'null';
                                    } else {
                                        nowValue = grpValue[key];
                                    }
                                } else {
                                    if (grpValue[key] === null || grpValue[key] === undefined) {
                                        nowValue += ',' + 'null';
                                    } else {
                                        nowValue += ',' + grpValue[key];
                                    }
                                }
                            });
                        }
                        if (currentValue !== nowValue) {
                            if (index !== 0) {
                                var obj = {};
                                obj.key = currentValue;
                                //Apply order in values (not group by values)
//                                array = $filter('orderBy')(array, 'pin');
                                angular.forEach(valueFields, function (field) {
                                    if ($scope.columnOrderMap[field] !== undefined) {
                                        var isReverse = false;
                                        if ($scope.columnOrderMap[field] === 'desc') {
                                            isReverse = true;
                                        }
                                        array = $filter('orderBy')(array, $scope.predicate(field), isReverse);
                                    }
                                });
                                obj.value = angular.copy(array);
                                resultGroups.push(obj);
                            }
                            currentValue = nowValue;
                            array = [];
                            array.push(grpValue);
                        } else {
                            array.push(grpValue);
                        }
                        if (index === group.value.length - 1) {
                            var obj1 = {};
                            obj1.key = currentValue;
                            //Apply order in values (not group by values)
//                            array = $filter('orderBy')(array, 'pin');
                            angular.forEach(valueFields, function (field) {
                                if ($scope.columnOrderMap[field] !== undefined) {
                                    var isReverse = false;
                                    if ($scope.columnOrderMap[field] === 'desc') {
                                        isReverse = true;
                                    }
                                    array = $filter('orderBy')(array, $scope.predicate(field), isReverse);
                                }
                            });
                            obj1.value = angular.copy(array);
                            resultGroups.push(obj1);
                        }
                    });
                }
                // console.log('result group---'+JSON.stringify(resultGroups));
                return resultGroups;
            };

            $scope.recursiveGroup = function recursiveGroup(groups, level, valueFields) {
                var levelItem;
                if (level !== undefined) {

                    angular.forEach($scope.levelMap, function (item) {
                        if (level === item.level) {
                            levelItem = item.levelItems;
//                            console.log('var levelItem;ffffffff---' + levelItem);
                        }
                    });
                }
                angular.forEach(groups, function (group) {
                    group.rowCount = group.value.length;
                    // console.log('var levelItem;---'+levelItem);
                    if (levelItem !== undefined) {
                        var newGroups = $scope.divideGroup(group, levelItem, valueFields);

                        if (level <= $scope.totalLevel) {
                            group.value = recursiveGroup(newGroups, level + 1, valueFields);
                        } else {
                            group.value = newGroups;
                        }
                    }

                });
//                console.log(JSON.stringify(groups));
                return groups;
            };

            $scope.combineRows = function () {
                angular.forEach($scope.previewData, function (group) {
                    var groupRows = [];
                    for (var mainRowIndex = 1; mainRowIndex <= group.rowCount; mainRowIndex++) {
                        var tempGroup = group;
                        var currentLevel = 2;
                        var currentSubGroupRowIndex = mainRowIndex;
                        var currentRow = [];
                        while (currentLevel <= $scope.totalLevel) {
                            var subgroups = tempGroup.value;
                            var isBreak = false;
                            var totalSubGroupsRowCount = 0;
                            angular.forEach(subgroups, function (subGroup) {
                                if (isBreak === false) {
                                    var subGroupRowCount = subGroup.rowCount;
                                    if (totalSubGroupsRowCount + subGroupRowCount >= currentSubGroupRowIndex) {
                                        isBreak = true;
                                        currentLevel++;
                                        tempGroup = subGroup;
                                        currentSubGroupRowIndex -= totalSubGroupsRowCount;
                                    } else {
                                        totalSubGroupsRowCount += subGroupRowCount;
                                    }
                                }
                            });
                        }
                        currentRow = tempGroup.value[currentSubGroupRowIndex - 1];
                        groupRows.push(currentRow);
                    }
                    group.groupRows = groupRows;
                });
                // console.log(JSON.stringify($scope.previewData));
            };

            $scope.checkRowSpan = function (group, row, level, rowIndex) {
                // console.log('***********************************************************************************');
                if (level !== undefined) {
                    var tempGroup = group;
                    var baseLevel = 2;
                    var currentSubGroupRowIndex = rowIndex + 1;
                    var rowSpan = 1;
                    if (level === 1) {
                        rowSpan = group.rowCount;
                    }
                    while (level >= baseLevel) {
                        var subGroups = tempGroup.value;
                        var isBreak = false;
                        var totalSubGroupsRowCount = 0;
                        angular.forEach(subGroups, function (subGroup) {
                            if (isBreak === false) {
                                var subGroupRowCount = subGroup.rowCount;
                                if (totalSubGroupsRowCount + subGroupRowCount >= currentSubGroupRowIndex) {
                                    isBreak = true;
                                    level--;
                                    tempGroup = subGroup;
                                    currentSubGroupRowIndex -= totalSubGroupsRowCount;
                                    rowSpan = subGroup.rowCount;
                                } else {
                                    totalSubGroupsRowCount += subGroupRowCount;
                                }
                            }
                        });
                    }

                    // console.log('Rowspan---'+rowSpan+'--level--'+level+'rowIndex---'+rowIndex);
                    return rowSpan;
                } else {
                    return 1;
                }
            };

            $scope.checkToRender = function (group, row, level, rowIndex) {
                var rowSpan = $scope.checkRowSpan(group, row, level, rowIndex);
                // console.log('***********************************************************************************');
                if (rowSpan === 1) {
                    return true;
                }
                if (level !== undefined) {
                    var tempGroup = group;
                    var baseLevel = 2;
                    var currentSubGroupRowIndex = rowIndex + 1;
                    while (level >= baseLevel) {
                        var subGroups = tempGroup.value;
                        var isBreak = false;
                        var totalSubGroupsRowCount = 0;
                        angular.forEach(subGroups, function (subGroup) {
                            if (isBreak === false) {
                                var subGroupRowCount = subGroup.rowCount;
                                if (totalSubGroupsRowCount + subGroupRowCount >= currentSubGroupRowIndex) {
                                    isBreak = true;
                                    level--;
                                    tempGroup = subGroup;
                                    currentSubGroupRowIndex -= totalSubGroupsRowCount;
                                } else {
                                    totalSubGroupsRowCount += subGroupRowCount;
                                }
                            }
                        });
                    }

                    // console.log('currentSubGroupRowIndex---'+(currentSubGroupRowIndex -1) +'--level--'+level+'rowIndex---'+rowIndex);
                    //All the calulation like row count represents index +1. changing to actual value.
                    if ((currentSubGroupRowIndex - 1) % rowSpan === 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            };
            /**
             * 
             * Methods end for multi level grouping in UI
             */
            //Apply method both for filter and color.
            $scope.applyAll = function (filterForm) {
                $scope.subbmited = true;
                if (filterForm !== undefined && filterForm.$valid) {
                    $scope.filterForm = filterForm;

                    if ($scope.filterAttributes.length > 0) {
                        $scope.applyFilter(filterForm);
                    }
                }
            };
            localStorage.removeItem('reportId');
            $rootScope.unMaskLoading();
            $scope.retrieveReportPaginatedData();
        }]);
});
