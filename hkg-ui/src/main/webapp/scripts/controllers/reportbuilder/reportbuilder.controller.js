define(['hkg', 'reportBuilderService', 'selectize', 'manageMasterService', 'messageService', 'ngload!uiGrid', 'colorpicker.directive', 'addMasterValue'], function (hkg) {
    hkg.register.controller('ReportBuilder', ["$rootScope", "$scope", "$timeout", "Messaging", "$filter", "$location", "ReportBuilderService", "ManageMasterService", function ($rootScope, $scope, $timeout, Messaging, $filter, $location, ReportBuilderService, ManageMasterService) {
//Init variables in scope
            $scope.entity = "REPORTBUILDER.";
//            $rootScope.mainMenu = "manageLink";
//            $rootScope.childMenu = "manageReports";
            $scope.columns = [];
            $scope.columnFormats = [];
            $scope.rows = [];
//            List of fetures displayed in multiselect
            $scope.featureList = [];
            $scope.tempFeatureList = [];
//            List of fields displyed in multiselect
            $scope.fieldList = [];
            $scope.totalFeatures = [];
            $scope.subbmited = false;
            $scope.orderFields = [];
            //Ordering Purpose
            $scope.primaryOrderFields = [];
            $scope.secondaryOrderFields = [];
            $scope.primaryOrder = {};
            $scope.secondaryOrder = {};
            $scope.primaryOrder.orderValue = 'asc';
            $scope.secondaryOrder.orderValue = 'asc';
            $scope.primaryOrder.isPrimaryOrderRequired = false;
            $scope.selectedFeatures = '';
            $scope.models = {};
            $scope.selectedFields = '';
            $scope.AllFieldDetailList = [];
            $scope.sectionList = [];
            $scope.AllSectionDetailList = [];
            $scope.isEditMode = false;
            $scope.submitted = false;
            $scope.columnsResponse = false;
            $scope.orderSaved = false;
            $scope.invalidColumn = false;
            $scope.updateReportFlag = false;
            $scope.isAnyRecordsExist = undefined;
            $scope.order = [];
            $scope.savedOrderMap = [];
            $scope.localFilter = [];
            $scope.selectedFeaturesList = [];
            $scope.featureDetailsList = [];
            //Db base name set to detect inner documents.
            $scope.innerDocumentBaseNameSet = ["lot_status_history", "lot_allotment_history", "packet_status_history", "packet_allotment_history", "issue_status_history",
                "plan_status_history"];
            $scope.caratRaangeFieldSet = ["carate_range_of_lot$DRP$Long", "carate_range_of_packet$DRP$Long", "carate_range_of_plan$DRP$Long"];
            $scope.tabs = [{id: 1}, {id: 2, disabled: true}, {id: 3, disabled: true}, {id: 4, disabled: true}];
            $scope.joinTypes = ["=", "<", ">", "<=", ">="];
            $scope.availabeFilterTypesByDataType = {int8: [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "<=", text: "less than equal to"}, {id: ">=", text: "greater than equal to"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "varchar": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "in", text: "in"}, {id: "not in", text: "not in"}, {id: "like", text: "like"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "timestamp": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "double precision": [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "<", text: "less than"}, {id: ">", text: "greater than"}, {id: "<=", text: "less than equal to"}, {id: ">=", text: "greater than equal to"}, {id: "between", text: "in between"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}],
                "boolean": [{id: "=", text: "equal to"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}]};
            $scope.filterValueNameMap = {"=": "equal to", "!=": "not equal to", "<": "less than", ">": "greater than",
                "<=": "less than equal to", ">=": "greater than equal to", "between": "in between",
                "in": "in", "not in": "not in", "like": "like", "is null": "has no value", "is not null": "has any value"};
            $scope.skipOrderComponentTypes = ["Image", "Upload", "UserMultiSelect", "MultiSelect"];
            $scope.dt = {};
            $scope.selectedId;
            //Variables for Group by
            $scope.isGroupByCheck = false;
            $scope.groups = [];
            $scope.groupByColumnsList = [];
            $scope.currentGroupIndex = 0;
            $scope.groupInfo = {};
            $scope.groupInfo.selectedGroup = '';
            $scope.groupInfo.groupName = '';
            $scope.groupInfo.groupByColumns = '';
            $scope.groupByLevels = [];
            $scope.remainingFields = [];
            $scope.groupByColumns = [];
            $scope.groupsColumns = [];
            $scope.otherColumns = [];
            $scope.totalLevel = 0;
            $scope.levelMap = [];
            $scope.columnOrderMap = {};
            $scope.currentColor = {};
            $scope.colorConfigData = [];
            $scope.colorConfig = {};
            $scope.isColorInitialized = false;
            $scope.colorApplied = false;
            $scope.isEditModeColorInitialized = false;
            $scope.isGroupInitialized = false;
            $scope.isEditModeFilterInitialized = false;
            $scope.isEditModeColumnsInitialized = false;
            $scope.isEditModeOrderInitialized = false;
            $scope.isEditModeGroupInitialized = false;
            $scope.isGlobalFilterAdded = false;
            $scope.reverse = false;
            $scope.currencyMap = {};
            //Code for master.
            $scope.code = "RG";
            //For user multi select filters
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr>\ " +
                    "</table>\ ";

            $scope.recipientMap = {};
            $scope.recipientMap['E'] = 'Employee';
            $scope.recipientMap['D'] = 'Deparment';
            $scope.recipientMap['R'] = 'Designation';
            $scope.recipientMap['A'] = 'Activity';
            $scope.recipientMap['F'] = 'Franchise';
            $scope.recipientMap['X'] = 'Franchise Department';
            $scope.recipientMap['G'] = 'Group';
            $scope.recipientMap['S'] = 'Service';
            $scope.recipientCodes = [];

            // Check if edit needed to be enabled
            $scope.checkEditMode = function () {
                var id = localStorage.getItem('reportId');
                if (id !== null) {
                    $scope.selectedId = id;
                    $scope.isEditMode = true;
                    $scope.updateReportFlag = false;
                }
                else {
                    $scope.isEditMode = false;
                    $scope.models = {
                        selected: null,
                        templates: [
                            {type: "container", id: 0, text: "New-Table", columns:
                                        [[{text: "Column", colField: "column"}]], searchData: [[{column: ""}]]}
                        ],
                        dropzones: {
                            "A": [
                                {
                                    "type": "container",
                                    allowedTypes: ['col'],
                                    "id": 1,
                                    "text": "User", "columns": [[{text: "Column", colField: "column"}]],
                                    searchData: [[{column: ""}]]}
                            ]}};
                }
            };
            $scope.checkEditMode();
            //-----Code For DatePickerDialog
            $scope.open = function ($event, element, opened) {
                $event.preventDefault();
                $event.stopPropagation();
//                $scope.dt[opened] = true;
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
                    isFilter: true,
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
                            $scope.gridOptions.totalItems = $scope.totalItems;
                            if (!$scope.isGroupByCheck) {
                                //Format previewData to new format includes color.
                                $scope.UIPreviewData = angular.copy($scope.previewData);
                                console.log("previwDate----in paginated non filter" + JSON.stringify($scope.UIPreviewData));
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
                            }
                            //Prepare data for preview if Group by is applied.
                            if ($scope.isGroupByCheck) {
                                $scope.groupRecordsForPreview($scope.previewData);
                            }
                            $scope.gridOptions.columnDefs = $scope.gridColumnDef;
                            $scope.gridOptions.data = $scope.UIPreviewData;

                            //Adjust selected page to show filtered records. 
                            if ($scope.gridApi.pagination.getPage() > $scope.gridApi.pagination.getTotalPages()) {
                                $scope.gridApi.pagination.seek(1);
                            }
                        }
                    }
                }, function () {
                    $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                });
            };

            $scope.resetGrid = function () {
                //Reset page to one. //Called only if franchise change happens.
                $scope.gridOptions.paginationCurrentPage = 1;
                //$scope.gridApi.pagination.seek(1);
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

            //Retrieve Master values for report category.
            ManageMasterService.retrieveDetailsOfMaster({primaryKey: $scope.code}, function (response) {
                $scope.detailOfMaster = response;

                $scope.detailOfMaster.masterDataBeans = $filter('orderBy')($scope.detailOfMaster.masterDataBeans, ['-isOftenUsed', 'shortcutCode', 'value']);
            });

            //Check unique report name
            $scope.checkReportNameExists = function (element, reportName) {
                if (reportName !== undefined && reportName.length > 0) {
                    ReportBuilderService.checkReportNameExists(reportName, function (res) {
                        if (res.data === true) {
                            element.$setValidity('exists', false);
                        }
                    }, function (res) {
                        console.log('Report name check failed');
                    });
                }
            };

            $scope.retrieveReportForEdit = function () {
                if ($scope.isEditMode) {
                    ReportBuilderService.retrieveReport($scope.selectedId, function (response) {
                        $scope.report = response;
                        if ($scope.report.externalReport !== true) {
                            var featureIds = [];
                            angular.forEach($scope.report.columns, function (column) {
                                if (featureIds.indexOf(column.feature) === -1) {
                                    featureIds.push(column.feature);
                                }
                            });
                            $scope.selectedFieldsList = [];
                            $scope.report.columns.sort(SortByFieldSequence);
                            $scope.models = {
                                selected: null,
                                templates: [
                                    {type: "container", id: 0, text: "New-Table", columns:
                                                [[{text: "Column", colField: "column", feature: 0}]], searchData: [[{column: ""}]]}
                                ],
                                dropzones: {
                                    "A": []}};
                            angular.forEach($scope.report.tableDtls, function (dtl) {
                                $scope.obj = {"type": "container",
                                    allowedTypes: ['col'],
                                    text: dtl.tableName,
                                    "id": dtl.id, columns: [[]], searchData: [[{column: ""}]]};
                                $scope.models.dropzones.A.push(angular.copy($scope.obj));
                            });
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
                                }
                            });
                        }
                        ;

                    });
                }
            };

            $scope.retrieveReportForEdit();
            // Initialization of ui-select2 for features dialog
            $scope.multiFeatures = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select Features',
                data: $scope.featureList,
                initSelection: function (element, callback) {
                    if ($scope.isEditMode) {
                        var data = [];
                        ReportBuilderService.retrieveReport($scope.selectedId, function (response) {
                            $scope.report = response;
                            var featureIds = [];
                            var subFormFeatureIds = [];
                            if ($scope.report.externalReport !== true) {
                                $scope.report.columns.sort(SortByFieldSequence);
                                angular.forEach($scope.report.columns, function (column) {
                                    if (!(column.isSubFormValue === true && column.dbBaseName === 'subformvalue')) {
                                        if (featureIds.indexOf(column.feature) === -1) {
                                            featureIds.push(column.feature);
                                        }
                                    } else {
                                        if (subFormFeatureIds.indexOf(column.parentDbBaseName + '.' + column.parentFieldLabel) === -1) {
                                            subFormFeatureIds.push(column.parentDbBaseName + '.' + column.parentFieldLabel);
                                        }
                                    }
                                });
                                ReportBuilderService.retrieveFeatureNameByIds(featureIds, function (res) {
                                    var selectedFeaturesList = [];
                                    angular.forEach(featureIds, function (item) {
                                        angular.forEach(res.data, function (value, key) {
                                            if (item == key) {
                                                selectedFeaturesList.push({id: value, text: value});
                                            }

                                        });
                                    })

                                    angular.forEach(selectedFeaturesList, function (item) {
                                        data.push(item);
                                    });
                                    if (subFormFeatureIds.length > 0) {
                                        angular.forEach(subFormFeatureIds, function (item) {
                                            data.push({id: item, text: item});
                                        });
                                    }
                                    callback(data);
                                });
                            }
                        }, function () {
                        });
                    }
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                }
            };
            $scope.joinSelect = {
                placeholder: 'Select Columns',
                formatSelection: formatJoinColumns
            };
            function formatJoinColumns(col) {
                return col.id;
            }

// Common method for removing un-necessary fields from report json.
            $scope.setReportColumnsAndGenearteQuery = function (generateQuery) {
                $scope.reportDuplicate = angular.copy($scope.report);
                $scope.reportDuplicate.columns = angular.copy($scope.columns);
                if (!$scope.isEditMode) {
                    $scope.report.columns = angular.copy($scope.columns);
                }
                angular.forEach($scope.reportDuplicate.columns, function (col) {
                    col.availableFilterTypes = undefined;
                    col.availableFilterValues = undefined;
                    col.availableFilterValueSelect = undefined;
                    col.availableFilterValueSelectEqual = undefined;
                    col.availableFilterValueSelectNotEqual = undefined;
                    col.filterValFirst = undefined;
                    col.selectedFilterType = undefined;
                    col.selectedFilterTypesList = undefined;
                    col.filterValSecond = undefined;
                    col.selectedFilterTypes = undefined;
//                    col.id = undefined;
                    col.order = undefined;
                    col.total = undefined;
                    col.checked = undefined;
                    col.filter = undefined;
                    if (col.columnFilter !== undefined) {
                        col.columnFilter = undefined;
                    }
                    if (col.availableColumnFormats !== undefined) {
                        col.availableColumnFormats = undefined;
                    }
                    col.isGroupBy = undefined;
                    col.filterValFirstInvalid = undefined;
                    col.filterValSecondInvalid = undefined;
                    col.level = undefined;
                    col.currencySymbol = undefined;
                    col.format = undefined;
                    col.associatedCurrency = undefined;
                    col.userMultiSelection = undefined;
                });
                if (generateQuery !== undefined && generateQuery === true) {
                    $scope.generateQuery();
                }
            };

            // Method for genarating the query or updating the query as per the criteria added.
            $scope.generateQuery = function () {
                var index = 0;
                angular.forEach($scope.reportDuplicate.columns, function (item) {
                    item.fieldSequence = ++index;
                });
                if ($scope.reportDuplicate.columns !== undefined) {
                    ReportBuilderService.generateQuery($scope.reportDuplicate, function (response) {
                        if (response.query) {
                            $scope.report.query = response.query;
                        }

                    });
                }
            };

            // For the columns whose meta-data is not available.
            $scope.columnForAlias = function () {
                $scope.columnNameForAlias = [];
                if ($scope.selectedFields.length !== 0) {
                    var fields = $scope.selectedFields.split(",");
                    angular.forEach($scope.fieldList, function (item) {
                        angular.forEach(item.children, function (childrenItems) {
                            for (var i = 0; i < fields.length; i++) {
                                if (fields[i] === childrenItems.id) {
                                    $scope.columnNameForAlias.push({id: childrenItems.id, text: childrenItems.text});
                                }
                            }
                        });
                    });
                }
            };

            $scope.setPage = function (pageNo) {
                console.log("pageNo : " + pageNo);
                $scope.currentPage = pageNo;
                $scope.pageChanged();
            };

            $scope.pageChanged = function () {
                console.log('Page changed to: ' + $scope.currentPage);
                $scope.retrievePaginatedData();
            };

            // Initialization of ui-select2 for fields dialog on First Tab.
            $scope.multiFields = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select Fields',
                data: $scope.fieldList,
                formatSelection: formatTableColumns,
                initSelection: function (element, callback) {
                    if ($scope.isEditMode) {
                        var data = [];
                        ReportBuilderService.retrieveReport($scope.selectedId, function (response) {
                            $scope.report = response;
                            if ($scope.report.externalReport !== true) {
                                var featureIds = [];
                                angular.forEach($scope.report.columns, function (column) {
                                    if (featureIds.indexOf(column.feature) === -1) {
                                        featureIds.push(column.feature);
                                    }
                                });
                                $scope.selectedFieldsList = [];
                                $scope.report.columns.sort(SortByFieldSequence);
                                angular.forEach($scope.report.columns, function (item) {
                                    $scope.selectedFieldsList.push({id: item.dbBaseName + "." + item.colName, text: item.fieldLabel});
                                });
                                if ($scope.selectedFieldsList !== undefined && $scope.selectedFieldsList !== null && $scope.selectedFieldsList.length > 0) {
                                    angular.forEach($scope.selectedFieldsList, function (item) {
                                        data.push(item);
                                    });
                                    callback(data);
//                                    $scope.loadEditData();
                                }


                            }
                        }, function () {
                        });
                    }
                },
                formatResult: function (item) {
                    return item.text;
                }


            };
            function formatTableColumns(col) {
                if ($scope.selectedFields !== undefined) {
                    if ($scope.selectedFields.indexOf(col.text) > 0) {
                        return col.id;
                    } else {
                        return col.text;
                    }
                }
            }

            var availableFormats = {"numberFormats": [{"format": "00.00"}, {"format": "00.000"}],
                "dateFormats": [{"format": "dd/MM/yyyy"}, {"format": "dd-MM-yyyy"}, {"format": "dd-MM-yy"}, {"format": "dd-MM-yyyy hh:mm:ss"}, {"format": "dd Mon,yyyy"}],
                "textFormats": [{"format": "Minimum Text"}, {"format": "Full Text"}]};
            $scope.report = {};
            $scope.report.externalReport = false;
            //ng-table pagination
            var data = [];

            $scope.loadColumnValues = function (reportBasicForm) {
                $scope.submitted = true;
                if ($("#selectFeatures").select2("data").length === 0) {
                    reportBasicForm.internalForm.selectFeatures.$invalid = true;
                    reportBasicForm.internalForm.selectFeatures.$dirty = true;
                    reportBasicForm.internalForm.selectFeatures.$error.required = true;
                } else {
                    reportBasicForm.internalForm.selectFeatures.$invalid = false;
                    reportBasicForm.internalForm.selectFeatures.$dirty = true;
                    reportBasicForm.internalForm.selectFeatures.$error.required = false;
                }
                var valid = true;
                for (var key in reportBasicForm) {
                    if (key !== 'passwordPopUpForm' && key !== 'editMasterForm') {
                        if (reportBasicForm[key].$invalid) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    $scope.tabs[1].disabled = false;
                    $scope.tabs[1].active = true;
                    if ($scope.isEditMode) {
                        $scope.retrieveReportForEdit();
                        angular.forEach($scope.report.columns, function (item) {
                            if ($scope.selectedFields !== undefined && $scope.selectedFields !== '') {
                                $scope.selectedFields = $scope.selectedFields + "," + item.dbBaseName + "." + item.colName;
                            } else {
                                $scope.selectedFields = item.dbBaseName + "." + item.colName;
                            }
                            $scope.selectedFieldsList.push({id: item.dbBaseName + "." + item.colName, text: item.fieldLabel});
                        });
                        $timeout(function () {
                            $scope.loadColumnValues1();
                        }, 500);

                    }

                }
            };

            $scope.dropColumnCallbackAfterSave = function (event, index, item, external, type, allowedType) {
                if ($scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                    $scope.selectedFields = '';
                    angular.forEach($scope.models.dropzones.A, function (key) {
                        angular.forEach(key.columns[0], function (data) {
                            if ($scope.selectedFields != undefined && $scope.selectedFields.length > 0) {
                                var tempFields = angular.copy($scope.selectedFields.split(","));
                                if (tempFields.length > 0) {
                                    var fieldCount = 0;
                                    angular.forEach(tempFields, function (field) {
                                        if (field == data.colField) {
                                            fieldCount++;
                                        }
                                    });
                                    if (fieldCount == 0) {
                                        $scope.selectedFields = $scope.selectedFields + "," + data.colField;
                                    }
                                }
                                $scope.loadColumnValues1();
                                $scope.configureRelationship();
                            } else {
                                $scope.selectedFields = data.colField;
                                $scope.loadColumnValues1();
                                $scope.configureRelationship();
                            }
                        });
                    });
                }
            }

            // To load all the selected fields for the report and configuring them(Like : alias,filters etc).
            $scope.loadColumnValues1 = function () {
                $scope.primaryOrder.isPrimaryOrderRequired = false;
                if ($scope.secondaryOrder.column !== undefined && $scope.secondaryOrder.column !== null) {
                    if ($scope.primaryOrder.column === undefined || $scope.primaryOrder.column === null) {
                        $scope.primaryOrder.isPrimaryOrderRequired = true;
                    }
                }
                if (!$scope.primaryOrder.isPrimaryOrderRequired) {
                    $scope.fieldToDataTypeMap = {};
                    $scope.fieldToComponentTypeMap = {};
                    //set Currently Selected Fields
                    if ($scope.selectedFeatures !== undefined && $scope.selectedFields !== undefined) {
                        var featureList = $scope.selectedFeatures.split(',');
                        var fieldList = $scope.selectedFields.split(',');
                        var tempFieldList = [];
                        var indexSequence = 0;
                        angular.forEach(fieldList, function (field) {
                            if ($scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                                angular.forEach($scope.models.dropzones.A, function (key) {
                                    angular.forEach(key.columns[0], function (data) {
                                        if (data.colField === field) {
                                            var cnt = 0;
                                            angular.forEach(tempFieldList, function (tpField) {
                                                if ((tpField.dbBaseName + "." + tpField.dbFieldName) === field) {
                                                    cnt++;
                                                }
                                            });
                                            if (cnt === 0) {
                                                var fields = {tableName: key.text, dbBaseName: field.split(".")[0], dbFieldName: field.split(".")[1], fieldSequence: ++indexSequence};
                                                tempFieldList.push(fields);
                                            }
                                        }
                                    });
                                });
                            }
                        });
                        angular.forEach(tempFieldList, function (field) {
                            if ($scope.models.dropzones.A != undefined && $scope.models.dropzones.A.length > 0) {
                                angular.forEach($scope.models.dropzones.A, function (key) {
                                    angular.forEach(key.columns[0], function (data) {
                                        if (data.colField == (field.dbBaseName + "." + field.dbFieldName)) {
                                            var tables = field.tableName.split(",");
                                            if (tables.indexOf(key.text) == -1) {
                                                field.tableName = field.tableName + "," + key.text;
                                            }
                                        }
                                    });
                                });
                            }
                        });
                        $scope.associatedFieldList = [];
                        angular.forEach(featureList, function (feature) {
                            for (var key in $scope.featureFieldMap) {
                                if (key === feature) {
                                    $scope.sectionList = [];
                                    $scope.sectionFieldMap = $scope.featureFieldMap[key];
                                    for (var sectionKey in $scope.sectionFieldMap) {
                                        $scope.fieldNames = [];
                                        angular.forEach(tempFieldList, function (field) {
                                            angular.forEach($scope.sectionFieldMap[sectionKey], function (fieldItem) {
                                                if (fieldItem.dbBaseName === field.dbBaseName && fieldItem.dbFieldName === field.dbFieldName) {
                                                    fieldItem.fieldSequence = field.fieldSequence;
                                                    fieldItem.sectionName = sectionKey;
                                                    fieldItem.orderType = null;
                                                    fieldItem.tableName = field.tableName;
                                                    $scope.associatedFieldList.push(fieldItem);
                                                }
                                                var isDffFlag = false;
                                                if (fieldItem.validationPattern !== null && fieldItem.validationPattern !== undefined) {
                                                    try {
                                                        var validationPattern = JSON.parse(fieldItem.validationPattern);
                                                        if (validationPattern.isDff !== null || validationPattern.isDff !== undefined) {
                                                            if (validationPattern.isDff) {
                                                                isDffFlag = true;
                                                            }
                                                        }
                                                    } catch (exception) {
                                                        console.log('Can not parse JSON validationPattern' + exception);
                                                    }
                                                }
                                                if (!isDffFlag && sectionKey === 'General' && fieldItem.dbBaseType === 'MDB') {
                                                    $scope.fieldToDataTypeMap[fieldItem.dbBaseName + '.' + fieldItem.dbFieldName] = fieldItem.fieldType;
                                                    $scope.fieldToComponentTypeMap[fieldItem.dbBaseName + '.' + fieldItem.dbFieldName] = fieldItem.componentType;
                                                }
                                            });
                                        });
                                    }
                                }
                            }
                        });
                    }
                    if ($scope.associatedFieldList.length === 0) {
                        $scope.columnEmptyError = true;
                        $rootScope.addMessage("Fields are not configured,Please contact administrator", $rootScope.failure);
                        return;
                    } else {
                        $scope.columnEmptyError = false;
                    }
                    $scope.associatedFieldList.sort(SortByFieldSequence);
                    $scope.existingColumns;
                    if ($scope.columns === undefined || $scope.columns.length === 0) {
                        $scope.existingColumns = [];
                    } else {
                        $scope.existingColumns = angular.copy($scope.columns);
                    }
                    $scope.columns = [];
                    var count = 0;
                    angular.forEach($scope.associatedFieldList, function (field) {
                        var col = {};
                        col.tableName = field.tableName;
                        col.colName = field.dbFieldName;
                        col.isSubFormValue = field.isSubFormValue;
                        col.parentDbFieldName = field.parentDbFieldName;
                        col.parentDbBaseName = field.parentDbBaseName;
                        col.parentFieldLabel = field.parentFieldLabel;
                        var dataType = field.fieldType;
                        if (dataType === 'String') {
                            col.dataType = 'varchar';
                            if (field.validationPattern !== null && field.validationPattern !== undefined) {
                                try {
                                    var validationPattern = JSON.parse(field.validationPattern);
                                    if (validationPattern.allowedTypes !== null || validationPattern.allowedTypes !== undefined) {
                                        if (validationPattern.allowedTypes === 'Numeric') {
                                            col.dataType = 'int8';
                                        }
                                    }
                                } catch (exception) {
                                    console.log('Can not parse JSON validationPattern' + exception);
                                }
                            }
                            if (field.componentType === 'AutoGenerated') {
                                col.dataType = 'varchar';
                            }
                        }
                        else if (dataType === 'Date' || dataType === 'timestamp') {
                            col.dataType = 'timestamp';
                        } else if (dataType === 'StringArray' || dataType === 'ObjectId') {
                            col.dataType = 'varchar';
                        } else if (dataType === 'Double') {
                            col.dataType = 'double precision';
                        } else if (dataType === 'Boolean') {
                            col.dataType = 'boolean';
                        } else if (dataType === 'Image' || dataType === 'File') {
                            col.dataType = 'varchar';
                        }
                        else {
                            col.dataType = 'int8';
                        }
                        col.masterCode = null;
                        if (field.validationPattern !== null && field.validationPattern !== undefined) {
                            try {
                                var validationPattern = JSON.parse(field.validationPattern);
                                if (validationPattern.masterCode !== null && validationPattern.masterCode !== undefined) {
                                    col.masterCode = validationPattern.masterCode;
                                    col.dataType = 'varchar';
                                }
                            } catch (exception) {
                                console.log('Can not parse JSON validationPattern' + exception);
                            }
                        }
                        if (field.validationPattern !== null && field.validationPattern !== undefined) {
                            try {
                                var validationPattern = JSON.parse(field.validationPattern);
                                if (validationPattern.isDff !== null || validationPattern.isDff !== undefined) {
                                    if (validationPattern.isDff) {
                                        col.isDff = true;
                                    } else {
                                        col.isDff = false;
                                    }
                                } else {
                                    col.isDff = false;
                                }
                                //include Time
                                if (validationPattern.includeTime !== null || validationPattern.includeTime !== undefined) {
                                    if (validationPattern.includeTime) {
                                        col.includeTime = true;
                                    } else {
                                        col.includeTime = false;
                                    }
                                } else {
                                    col.includeTime = false;
                                }
                                //IsRule
                                if (validationPattern.isRule !== null || validationPattern.isRule !== undefined) {
                                    if (validationPattern.isRule) {
                                        col.isRule = true;
                                    } else {
                                        col.isRule = false;
                                    }
                                } else {
                                    col.isRule = false;
                                }
                            } catch (exception) {
                                console.log('Can not parse JSON validationPattern' + exception);
                            }
                        }
                        col.alias = field.fieldLabel;
                        col.associatedCurrency = field.associatedCurrency;
                        col.checked = true;
                        col.id = count++;
                        col.fieldLabel = field.fieldLabel;
                        col.editedFieldLabel = field.editedFieldLabel;
                        col.dbBaseName = field.dbBaseName;
                        col.dbBaseType = (field.dbBaseType === null || field.dbBaseType === '') ? 'RDB' : field.dbBaseType;
                        col.feature = field.feature;
                        col.orderType = field.orderType;
                        col.showTotal = field.showTotal;
                        col.sectionName = field.sectionName;
                        col.componentType = field.componentType;
                        col.hkFieldId = field.id;
                        if (col.dbBaseType === 'MDB') {
                            if (col.componentType === 'Dropdown' || col.componentType === 'MultiSelect' || col.componentType === 'UserMultiSelect' || col.componentType === 'Radio') {
                                col.dataType = 'varchar';
                            }
                        }
                        var availableColumnFormats = $scope.getAvailableFormat(col.dataType);
                        var format = (col.dataType === 'timestamp' ? 'dd/MM/yyyy' : null);
                        col.availableColumnFormats = availableColumnFormats;
                        col.format = format;
                        var filters = $scope.availabeFilterTypesByDataType[col.dataType] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, , {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}] : $scope.availabeFilterTypesByDataType[col.dataType];
                        if (col.componentType === "Date range") {
                            filters = [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}];
                        }
                        if (col.componentType === "UserMultiSelect") {
                            filters = [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}, {id: "in", text: "in"}, {id: "not in", text: "not in"}, {id: "is not null", text: "has any value"}, {id: "is null", text: "has no value"}];
                        }
                        $scope.filterTypes = []
                        angular.forEach(filters, function (filter) {
                            $scope.filterTypes.push({id: filter.id, text: filter.text});
                        });
                        col.availableFilterTypes = angular.copy($scope.filterTypes);
                        col.availableFilterValues = [];
                        col.selectedFilterTypesList = [];
                        if (col.componentType === 'UserMultiSelect') {
                            col.userMultiSelection = {
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Filter Value',
                                initSelection: function (element, callback) {
                                    var data = [];
                                    if ($scope.existingColumns.length > 0) {
                                        angular.forEach($scope.existingColumns, function (column) {
                                            if (col.colName === column.colName && col.dbBaseName === column.dbBaseName) {
                                                if (column.filterValFirst !== undefined) {
                                                    if (column.filterValFirst instanceof Object === true && angular.isArray(column.filterValFirst)) {
                                                        angular.forEach(column.filterValFirst, function (item) {
                                                            data.push({id: item.id, text: item.text});
                                                        });
                                                        callback(data);
                                                    } else {
                                                        var filterTypes = column.filterValFirst.split(",");
                                                        angular.forEach(filterTypes, function (item) {
                                                            var text = item;
                                                            angular.forEach($scope.recipientCodes, function (code) {
                                                                if (item === code.id) {
                                                                    text = code.text;
                                                                }
                                                            });
                                                            data.push({id: item, text: text});
                                                        });
                                                        callback(data);
                                                    }
                                                }
                                            }
                                        });
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
                                    var success = function (data) {
                                        if (data.length !== 0) {
                                            $scope.names = data;
                                            angular.forEach(data, function (item) {
                                                $scope.names.push({
                                                    id: item.value + ":" + item.description,
                                                    text: item.label

                                                });
                                                var isExists = false;
                                                angular.forEach($scope.recipientCodes, function (code) {
                                                    if (code.id === item.value + ":" + item.description) {
                                                        isExists = true;
                                                    }
                                                });
                                                if (!isExists) {
                                                    $scope.recipientCodes.push({
                                                        id: item.value + ":" + item.description,
                                                        text: item.label

                                                    });
                                                }
                                            });
                                        }
                                        query.callback({
                                            results: $scope.names
                                        });
                                    };
                                    var failure = function () {
                                    };
                                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveUserList(search.trim(), success, failure);
                                    } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                    } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveRoleList(search.trim(), success, failure);
                                    } else if (selected.length > 0) {
                                        var search = selected;
                                        Messaging.retrieveUserList(search.trim(), success, failure);
                                    } else {
                                        query.callback({
                                            results: $scope.names
                                        });
                                    }
                                }
                            };
                        }
                        $scope.columns.push(col);
                    });
                    //Set the previous column values to the new columns
                    if ($scope.existingColumns.length > 0) {
                        var updateColumns = [];
                        angular.forEach($scope.existingColumns, function (exColumn) {
                            angular.forEach($scope.columns, function (column, colIndex) {
                                if (column.colName === exColumn.colName && column.dbBaseName === exColumn.dbBaseName) {
                                    $scope.columns.splice(colIndex, 1);
                                    updateColumns.push(exColumn);
                                }
                            });
                        });
                        angular.forEach(updateColumns, function (col) {
                            $scope.columns.push(col);
                        });

                        angular.forEach($scope.associatedFieldList, function (field) {
                            angular.forEach($scope.columns, function (column) {
                                if (column.colName === field.dbFieldName && column.dbBaseName === field.dbBaseName) {
                                    column.fieldSequence = field.fieldSequence;
                                    column.tableName = field.tableName;
                                    column.orderType = field.orderType;
                                }
                            });
                        });
                        $scope.columns.sort(SortByFieldSequence);
                        $scope.associatedFieldList.sort(SortByFieldSequence);
                    }
                    //Update value of edit mode for the first time.
                    if ($scope.isEditMode && !$scope.isEditModeColumnsInitialized) {
                        $scope.isEditModeColumnsInitialized = true;
                        $scope.tempColumns = [];
                        var varcount = 1;
                        angular.forEach($scope.columns, function (column) {
                            column.id = null;
                            angular.forEach($scope.report.columns, function (item) {
                                if (item.colName === column.colName && item.dbBaseName === column.dbBaseName) {
                                    column.alias = item.alias;
                                    column.format = item.format;
                                    column.colI18nRequired = item.colI18nRequired;
                                    column.converterDataBeanList = item.converterDataBeanList;
                                    column.id = item.id;
                                    column.fieldSequence = item.fieldSequence;
                                    column.fieldLabel = item.fieldLabel;
                                    column.showTotal = item.showTotal;
                                    column.filter = item.filter;
                                    column.tableName = item.tableName;
                                }
                            });
                        });
                        angular.forEach($scope.associatedFieldList, function (field) {
                            angular.forEach($scope.columns, function (column) {
                                if (column.colName === field.dbFieldName && column.dbBaseName === field.dbBaseName) {
                                    field.fieldSequence = column.fieldSequence;
                                }
                            });
                        });
                        $scope.columns.sort(SortByFieldSequence);
                        $scope.associatedFieldList.sort(SortByFieldSequence);

                    }
                    //console.log('Columns EDIT---' + JSON.stringify($scope.columns));

//                    //Set Relationship to record level.
//                    var relationship = [];
//                    angular.forEach($scope.selectedFeaturesList, function (featureRelation, featureIndex) {
//                        var relationAttr = {};
//                        relationAttr.featureId = featureRelation.featureId;
//                        if (featureRelation.localField === undefined || featureRelation.referenceField === undefined) {
//                            relationAttr.joinAttributes = null;
//                            relationAttr.refFeatureId = null;
//                        }
//                        else {
//                            var localField;
//                            var referenceField;
//                            if (featureRelation.localField instanceof Object === true) {
//                                localField = featureRelation.localField.id;
//                            } else {
//                                localField = featureRelation.localField;
//                            }
//                            if (featureRelation.referenceField instanceof Object === true) {
//                                referenceField = featureRelation.referenceField.id;
//                            } else {
//                                referenceField = featureRelation.referenceField;
//                            }
//                            var localDatatype = null;
//                            var foreignDatatype = null;
//                            var localComponentType = null;
//                            var foreignComponentType = null;
//                            if ($scope.fieldToDataTypeMap[localField.replace(/"/g, "")] !== undefined && $scope.fieldToDataTypeMap[localField.replace(/"/g, "")] !== null) {
//                                localField = localField.replace(/"/g, "");
//                                localDatatype = $scope.setDatabaseDatatype($scope.fieldToDataTypeMap[localField.replace(/"/g, "")]);
//                                localComponentType = $scope.fieldToComponentTypeMap[localField];
//                            }
//                            if ($scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")] !== undefined && $scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")] !== null) {
//                                referenceField = referenceField.replace(/"/g, "");
//                                foreignDatatype = $scope.setDatabaseDatatype($scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")]);
//                                foreignComponentType = $scope.fieldToComponentTypeMap[referenceField];
//                            }
//                            relationAttr.joinAttributes = localField + "=" + referenceField;
//                            relationAttr.refFeatureId = parseInt(featureRelation.referenceFeature);
//                            relationAttr.localDataType = localDatatype;
//                            relationAttr.refDataType = foreignDatatype;
//                            relationAttr.localComponentType = localComponentType;
//                            relationAttr.refComponentType = foreignComponentType;
//                        }
//                        if (featureIndex === 0) {
//                            relationAttr.joinAttributes = null;
//                            relationAttr.refFeatureId = null;
//                        }
//                        relationAttr.sequence = featureIndex + 1;
//                        relationship.push(relationAttr);
//                    });
////                        console.log('relation---------'+JSON.stringify(relationship));
//                    $scope.report.joinAttributes = JSON.stringify(relationship);
                    $scope.columnForAlias();
                    $scope.tabs[1].disabled = false;
                    $scope.tabs[1].active = true;
                    $scope.retrieveTableRelation();
                }

            };

            $scope.setDatabaseDatatype = function (dataType) {
                var modifiedDataType = 'text';
                if (dataType === 'String') {
                    modifiedDataType = 'text';
                }
                else if (dataType === 'Date' || dataType === 'timestamp') {
                    modifiedDataType = 'timestamp';
                } else if (dataType === 'StringArray' || dataType === 'ObjectId') {
                    modifiedDataType = 'text';
                } else if (dataType === 'Double') {
                    modifiedDataType = 'numeric';
                } else if (dataType === 'Boolean') {
                    modifiedDataType = 'boolean';
                }
                else {
                    modifiedDataType = 'numeric';
                }
                return modifiedDataType;
            };
            // local function for sorting the fields.
            function SortByFieldSequence(x, y) {
                return x.fieldSequence - y.fieldSequence;
            }

            function SortBySequence(x, y) {
                return x.sequence - y.sequence;
            }

            //Disable tabs in case of external report
            $scope.$watch('report.externalReport', function (value) {
                if (value === true) {
                    $scope.tabs[1].disabled = true;
                    $scope.tabs[1].active = false;
                    $scope.tabs[2].disabled = true;
                    $scope.tabs[2].active = false;
                }
            });

            $scope.retrieveColumnMetaData = function () {
                $scope.setReportColumnsAndGenearteQuery(false);
                ReportBuilderService.retreiveColumnMetadata($scope.reportDuplicate, function (res) {
                    if (res.data !== null && res.data !== undefined) {
                        angular.forEach(res.data.columns, function (item) {
                            var localtable = item.dbBaseName;
                            var localColumn = item.colName;
                            if (item.componentType === 'SubEntity') {
                                angular.forEach($scope.columns, function (columnItem) {
                                    if (columnItem.dbBaseName === localtable && columnItem.colName === localColumn) {
                                        columnItem.dataType = item.dataType;
                                        var availableColumnFormats = $scope.getAvailableFormat(columnItem.dataType);
                                        var format = (columnItem.dataType === 'timestamp' ? 'dd/MM/yyyy' : null);
                                        columnItem.availableColumnFormats = availableColumnFormats;
                                        columnItem.format = format;
                                        var filters = $scope.availabeFilterTypesByDataType[columnItem.dataType] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[columnItem.dataType];
                                        columnItem.availableFilterTypes = angular.copy(filters);
                                    }
                                });
                            }
                            else if (item.convertedColumn !== null && item.convertedColumn !== undefined) {
                                var convertedColumn = item.convertedColumn;
                                var fktable = convertedColumn.split(".")[0];
                                var fkColumn = convertedColumn.split(".")[1];
                                if (fkColumn.indexOf(":") === -1) {
                                    angular.forEach($scope.AllFieldsEntitys, function (fieldItem) {
                                        if (fieldItem.dbBaseName === fktable && fieldItem.dbFieldName === fkColumn) {
                                            var dataType = fieldItem.fieldType;
                                            if (dataType === 'String') {
                                                item.dataType = 'varchar';
                                                if (fieldItem.validationPattern !== null && fieldItem.validationPattern !== undefined) {
                                                    try {
                                                        var validationPattern = JSON.parse(fieldItem.validationPattern);
                                                        if (validationPattern.allowedTypes !== null || validationPattern.allowedTypes !== undefined) {
                                                            if (validationPattern.allowedTypes === 'Numeric') {
                                                                item.dataType = 'int8';
                                                            }
                                                        }
                                                    } catch (exception) {
                                                        console.log('Can not parse JSON validationPattern' + exception);
                                                    }
                                                }
                                                if (fieldItem.componentType === 'AutoGenerated') {
                                                    item.dataType = 'varchar';
                                                }
                                            }
                                            else if (dataType === 'Date' || dataType === 'timestamp') {
                                                item.dataType = 'timestamp';
                                            } else if (dataType === 'StringArray' || dataType === 'ObjectId') {
                                                item.dataType = 'varchar';
                                            } else if (dataType === 'Double') {
                                                item.dataType = 'double precision';
                                            } else if (dataType === 'Boolean') {
                                                item.dataType = 'boolean';
                                            } else if (dataType === 'Image' || dataType === 'File') {
                                                item.dataType = 'varchar';
                                            }
                                            else {
                                                item.dataType = 'int8';
                                            }
                                        }
                                    });
                                } else {
                                    item.dataType = 'varchar';
                                }
                                angular.forEach($scope.columns, function (columnItem) {
                                    if (columnItem.dbBaseName === localtable && columnItem.colName === localColumn) {
                                        columnItem.dataType = item.dataType;
                                        var availableColumnFormats = $scope.getAvailableFormat(columnItem.dataType);
                                        var format = (columnItem.dataType === 'timestamp' ? 'dd/MM/yyyy' : null);
                                        columnItem.availableColumnFormats = availableColumnFormats;
                                        columnItem.format = format;
                                        var filters = $scope.availabeFilterTypesByDataType[columnItem.dataType] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[columnItem.dataType];
                                        columnItem.availableFilterTypes = angular.copy(filters);
//                                        $scope.filterTypes = []
//                                        columnItem.availableFilterTypes.data.splice(0, columnItem.availableFilterTypes.data.length);
//                                        angular.forEach(filters, function(filter) {
//                                            $scope.filterTypes.push({id: filter.id, text: filter.text});
//                                            columnItem.availableFilterTypes.data.push({id: filter.id, text: filter.text});
//                                        });
//                                        columnItem.availableFilterTypes = {
//                                            multiple: true,
//                                            placeholder: 'Select Filter',
//                                            closeOnSelect: false,
//                                            data: '',
//                                            initSelection: function(element, callback) {
//                                                if ($scope.isEditMode) {
//                                                    var data = [];
//                                                    angular.forEach($scope.report.columns, function(item) {
//                                                        if (item.colName === columnItem.colName && item.dbBaseName === columnItem.dbBaseName) {
//                                                            if (item.filter !== undefined) {
//                                                                columnItem.filter = JSON.parse(item.filter);
//                                                                var filterTypes = [];
//                                                                angular.forEach(columnItem.filter, function(filterRow) {
//                                                                    filterTypes.push({id: filterRow.filter, text: filterRow.filter});
//                                                                });
//                                                                data = filterTypes;
//                                                            }
//                                                        }
//                                                    });
//                                                    //$scope.changeFilter(data, count - 1);
//                                                    callback(data);
//                                                } else
//                                                {
//                                                    var data = [];
//                                                    callback(data);
//                                                }
//                                            },
//                                            formatResult: function(item) {
//                                                return item.text;
//                                            },
//                                            formatSelection: function(item) {
//                                                return item.text;
//                                            }
//                                        };

                                    }
                                });
                            } else if (item.dbBaseType === 'MDB') {
                                if (item.componentType === "Dropdown" || item.componentType === "MultiSelect") {
                                    angular.forEach($scope.columns, function (columnItem) {
                                        if (columnItem.dbBaseName === localtable && columnItem.colName === localColumn) {
                                            columnItem.dataType = item.dataType;
                                            var availableColumnFormats = $scope.getAvailableFormat(columnItem.dataType);
                                            var format = (columnItem.dataType === 'timestamp' ? 'dd/MM/yyyy' : null);
                                            columnItem.availableColumnFormats = availableColumnFormats;
                                            columnItem.format = format;
                                            var filters = $scope.availabeFilterTypesByDataType[columnItem.dataType] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[columnItem.dataType];
                                            columnItem.availableFilterTypes = angular.copy(filters);
                                        }
                                    });
                                }
                            }
                        });
                    }
//                    console.log('called------------');
                    if ($scope.isGroupByCheck || $scope.isEditMode) {
                        $scope.updateGroupConfigurations();
                    } else {
                        $scope.updateExistingFilters();
                    }
                }, function (res) {
//                    console.log('Column meta data request failed');
                    if ($scope.isGroupByCheck || $scope.isEditMode) {
                        $scope.updateGroupConfigurations();
                    } else {
                        $scope.updateExistingFilters();
                    }
                });
            };
            //Retrieving table relation between the fields for joins.
            $scope.retrieveTableRelation = function () {
                $scope.setReportColumnsAndGenearteQuery(false);

                ReportBuilderService.retrieveTableRelationship($scope.reportDuplicate, function (res) {
                    $scope.relationMap = angular.copy(res.data);
                    $scope.relationList = angular.copy(res.data);
                    angular.forEach($scope.columns, function (columnItem) {
                        angular.forEach($scope.relationList, function (item) {
                            if (columnItem.dbBaseName === item.table1) {
                                var joinAttributes = {
                                    FK_TABLE: item.table2,
                                    FK_COLUMN: item.table2Column,
                                    LOCAL_COLUMN: item.table1Column,
                                    REL_TYPE: item.relationLeftToRight,
                                    JOIN_TABLE: item.joinTable,
                                    JOINED_LOCAL_COLUMN: item.joinColumnTable1,
                                    JOINED_FK_COLUMN: item.joinColumnTable2
                                };
                                columnItem.joinAttributes = JSON.stringify(joinAttributes);
                            } else if (columnItem.dbBaseName === item.table2) {
                                var joinAttributes = {
                                    FK_TABLE: item.table1,
                                    FK_COLUMN: item.table1Column,
                                    LOCAL_COLUMN: item.table2Column,
                                    REL_TYPE: item.relationLeftToRight,
                                    JOIN_TABLE: item.joinTable,
                                    JOINED_LOCAL_COLUMN: item.joinColumnTable2,
                                    JOINED_FK_COLUMN: item.joinColumnTable1
                                };
                                columnItem.joinAttributes = JSON.stringify(joinAttributes);
                            }
                        });
                    });
                    $scope.retrieveColumnMetaData();
                }, function () {
                    console.log("failure----");
                });
            };
            $scope.addFilter = function (column, index, type) {
                //Type will specify which type columns like columns, groupbycolumns or othercolumns.
                $scope.isGlobalFilterAdded = true;
                if ($scope[type][index].selectedFilterType !== 'between' && !($scope[type][index].selectedFilterType === 'is null' || $scope[type][index].selectedFilterType === 'is not null')) {
                    if ($scope[type][index].filterValFirst !== undefined && ($scope[type][index].filterValFirst.length > 0 || $scope[type][index].filterValFirst !== null)) {
                        $scope[type][index].filterValFirstInvalid = false;
                        var filterValueFirst = $scope[type][index].filterValFirst;
                        var multiSelectLabel = {};
                        var multiSelectStringLabel = "";
                        if (column.componentType === 'UserMultiSelect') {
                            var multiSelectValues;
                            if (filterValueFirst instanceof Object && angular.isArray(filterValueFirst)) {
                                var result = "";
                                angular.forEach(filterValueFirst, function (item) {
                                    if (result.length === 0) {
                                        result = item.id;
                                    } else {
                                        result += "," + item.id;
                                    }
                                });
                                multiSelectValues = result.split(",");
                            } else {
                                multiSelectValues = filterValueFirst.split(",");
                            }
                            angular.forEach(multiSelectValues, function (item) {
                                angular.forEach($scope.recipientCodes, function (name) {
                                    if (name.id === item) {
                                        var multiSelectType = item.split(":")[1];
                                        var multiSelectCatagory = $scope.recipientMap[multiSelectType];
                                        if (multiSelectLabel[multiSelectCatagory] === undefined || multiSelectLabel[multiSelectCatagory] === null) {
                                            multiSelectLabel[multiSelectCatagory] = name.text;
                                        } else {
                                            multiSelectLabel[multiSelectCatagory] += ", " + name.text;
                                        }
                                    }
                                });
                            });
                            angular.forEach(multiSelectLabel, function (value, key) {
                                if (multiSelectStringLabel.length === 0) {
                                    multiSelectStringLabel = key + ": " + value;
                                } else {
                                    multiSelectStringLabel += "; " + key + ": " + value;
                                }
                            });
                        }
                        $scope[type][index].selectedFilterTypesList.push({filter: $scope[type][index].selectedFilterType,
                            filterLabel: $scope.filterValueNameMap[$scope[type][index].selectedFilterType], filterValFirst: filterValueFirst,
                            userMultiSelectLabel: multiSelectStringLabel});
                        if (column.componentType !== 'UserMultiSelect') {
                            $scope[type][index].filterValFirst = undefined;
                        }
                        angular.forEach($scope[type][index].availableFilterTypes, function (filterType, filterIndex) {
                            if (filterType.id === $scope[type][index].selectedFilterType) {
                                $scope[type][index].selectedFilterType = undefined;
                                $scope[type][index].availableFilterTypes.splice(filterIndex, 1);
                            }
                        });
                    } else {
                        $scope[type][index].filterValFirstInvalid = true;
                    }
                } else if ($scope[type][index].selectedFilterType === 'is null' || $scope[type][index].selectedFilterType === 'is not null') {
                    $scope[type][index].selectedFilterTypesList.push({filter: $scope[type][index].selectedFilterType,
                        filterLabel: $scope.filterValueNameMap[$scope[type][index].selectedFilterType]});
                    angular.forEach($scope[type][index].availableFilterTypes, function (filterType, filterIndex) {
                        if (filterType.id === $scope[type][index].selectedFilterType) {
                            $scope[type][index].selectedFilterType = undefined;
                            $scope[type][index].availableFilterTypes.splice(filterIndex, 1);
                        }
                    });
                } else {
                    if ($scope[type][index].filterValFirst !== undefined && $scope[type][index].filterValSecond !== undefined &&
                            ($scope[type][index].filterValFirst.length > 0 || $scope[type][index].filterValFirst !== null) &&
                            ($scope[type][index].filterValSecond.length > 0 || $scope[type][index].filterValSecond !== null)) {
                        switch ($scope[type][index].dataType) {
                            case "int8":
                                $scope[type][index].filterValFirst = parseInt($scope[type][index].filterValFirst);
                                $scope[type][index].filterValSecond = parseInt($scope[type][index].filterValSecond);
                                break;
                            case "double precision":
                                $scope[type][index].filterValFirst = parseFloat($scope[type][index].filterValFirst);
                                $scope[type][index].filterValSecond = parseFloat($scope[type][index].filterValSecond);
                                break;
                            case "timestamp":
                                $scope[type][index].filterValFirst = new Date($scope[type][index].filterValFirst);
                                $scope[type][index].filterValSecond = new Date($scope[type][index].filterValSecond);
                                break;
                        }
                        if ($scope[type][index].filterValFirst <= $scope[type][index].filterValSecond) {
                            $scope[type][index].filterValFirstInvalid = false;
                            $scope[type][index].filterValSecondInvalid = false;
                            $scope[type][index].filterValFirstMinInvalid = false;
                            $scope[type][index].selectedFilterTypesList.push({filter: $scope[type][index].selectedFilterType,
                                filterLabel: $scope.filterValueNameMap[$scope[type][index].selectedFilterType], filterValFirst: $scope[type][index].filterValFirst,
                                filterValSecond: $scope[type][index].filterValSecond});
                            $scope[type][index].filterValFirst = undefined;
                            $scope[type][index].filterValSecond = undefined;
                            angular.forEach($scope[type][index].availableFilterTypes, function (filterType, filterIndex) {
                                if (filterType.id === $scope[type][index].selectedFilterType) {
                                    $scope[type][index].selectedFilterType = undefined;
                                    $scope[type][index].availableFilterTypes.splice(filterIndex, 1);
                                }
                            });
                        } else {
                            $scope[type][index].filterValFirstMinInvalid = true;
                        }
                    }
                    if ($scope[type][index].filterValFirst === undefined || $scope[type][index].filterValFirst === null || $scope[type][index].filterValFirst.length === 0) {
                        $scope[type][index].filterValFirstInvalid = true;
                    }
                    if ($scope[type][index].filterValSecond === undefined || $scope[type][index].filterValSecond === null || $scope[type][index].filterValSecond.length === 0) {
                        $scope[type][index].filterValSecondInvalid = true;
                    }
                }
                $scope.persistGroupChanges();
            };
            $scope.removeFilter = function (filterIndex, columnIndex, type) {
//                console.log('Remove Requested---' + filterIndex + columnIndex);
                if ($scope[type][columnIndex].selectedFilterTypesList.length > 0) {
                    var currentFilter = $scope[type][columnIndex].selectedFilterTypesList[filterIndex];
                    $scope[type][columnIndex].selectedFilterTypesList.splice(filterIndex, 1);
//                    console.log('Bingo !! Filter deleted-' + filterIndex);
                    $scope[type][columnIndex].availableFilterTypes.push({id: currentFilter.filter, text: $scope.filterValueNameMap[currentFilter.filter]});
                }
                $scope.persistGroupChanges();
            };

            $scope.updateExistingFilters = function () {
                if ($scope.existingColumns !== undefined) {
                    if ($scope.isEditMode && !$scope.isEditModeFilterInitialized) {
                        $scope.isEditModeFilterInitialized = true;
                        angular.forEach($scope.report.columns, function (item) {
                            angular.forEach($scope.columns, function (col) {
                                if (item.colName === col.colName && item.dbBaseName === col.dbBaseName) {
                                    if (item.filter !== undefined) {
                                        col.filter = JSON.parse(item.filter);

                                        angular.forEach(col.filter, function (filterRow) {
                                            //Remove existing filter from Available filter types.
                                            angular.forEach(col.availableFilterTypes, function (availFilter, index) {
                                                if (availFilter.id === filterRow.filter) {
                                                    col.selectedFilterTypesList.push({filter: availFilter.id, filterLabel: $scope.filterValueNameMap[availFilter.id]});
                                                    col.availableFilterTypes.splice(index, 1);
                                                }
                                            });
                                        });
                                    }
                                }
                            });
                        });
                        $scope.updatePreFilterValues();
                        //$scope.changeFilter(data, count - 1);
                    } else
                    {

                        if ($scope.existingColumns.length > 0) {
                            angular.forEach($scope.existingColumns, function (column) {
                                angular.forEach($scope.columns, function (col) {
                                    if (col.colName === column.colName && col.dbBaseName === column.dbBaseName) {
                                        if (column.selectedFilterTypesList.length > 0) {
                                            angular.forEach(column.selectedFilterTypesList, function (existingFilter) {
                                                angular.forEach(col.availableFilterTypes, function (availFilter, index) {
                                                    if (existingFilter.filter === availFilter.id) {
                                                        col.availableFilterTypes.splice(index, 1);
                                                    }
                                                });
                                            });
                                        }
                                    }
                                });
                            });
                        }
                    }
                }

            };

            $scope.updatePreFilterValues = function () {
                angular.forEach($scope.columns, function (column) {
                    angular.forEach(column.selectedFilterTypesList, function (item) {
                        if (column.filter instanceof Object === false || !angular.isArray(column.filter)) {
                            column.filter = JSON.parse(column.filter);
                        }
                        angular.forEach(column.filter, function (preFilterValue) {
                            if (preFilterValue.filter === item.filter) {
                                if (preFilterValue.filterValFirst !== undefined) {
                                    item.filterValFirst = preFilterValue.filterValFirst;
                                }
                                if (preFilterValue.filterValSecond !== undefined) {
                                    item.filterValSecond = preFilterValue.filterValSecond;
                                }
                                //userMultiSelectLabel
                                if (preFilterValue.userMultiSelectLabel !== undefined) {
                                    item.userMultiSelectLabel = preFilterValue.userMultiSelectLabel;
                                }
                            }
                        });
                    });
                });
                if ($scope.isGroupByCheck) {
                    $scope.updateGroupConfig(true);
                }
            };
            // Loading the report data for edit.
            $scope.loadEditData = function () {
                if ($scope.selectedFields !== undefined && $scope.selectedFields instanceof Object === true && angular.isArray($scope.selectedFields)) {
                    var field = '';
                    angular.forEach($scope.selectedFields, function (item, index) {
                        if (index === 0) {
                            field = item.id;
                        } else {
                            field += ',' + item.id;
                        }
                    });
                    $scope.selectedFields = field;
                }
            };
            $scope.$watch("report.query", function (value) {
                if (value !== undefined) {
                    $scope.queryChanged = true;
                }
            });
            $scope.$watch("selectedFeatures", function (feature) {
                if ($scope.selectedFeatures !== undefined && $scope.selectedFeatures instanceof Object === true && angular.isArray($scope.selectedFeatures)) {
                    var feature = '';
                    angular.forEach($scope.selectedFeatures, function (item, index) {
                        if (index === 0) {
                            feature = item.id;
                        } else {
                            feature += ',' + item.id;
                        }
                    });
                    $scope.selectedFeatures = feature;
                    $scope.fieldList.splice(0, $scope.fieldList.length);
                    $scope.retrieveSectionFields($scope.selectedFeatures);
                    $scope.removeColumnsFromTableModel($scope.selectedFeatures);
                }
                else {
                    $scope.fieldList.splice(0, $scope.fieldList.length);
                    $scope.retrieveSectionFields($scope.selectedFeatures);
                    $scope.configureRelationship();
                }

            });

            $scope.removeColumnsFromTableModel = function (selectedFeatures) {
                if (selectedFeatures !== undefined) {
                    var features = selectedFeatures.split(",");
                    if ($scope.models != undefined && $scope.models.dropzones != undefined && $scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                        angular.forEach($scope.models.dropzones.A, function (key) {
                            var i;
                            for (i = key.columns[0].length; i--; ) {
                                var count = 0;
                                if (key.columns[0][i].feature === 0) {
                                    count++;
                                } else {
                                    angular.forEach(features, function (feat) {
                                        if (feat === key.columns[0][i].feature) {
                                            count++;
                                        }
                                    });
                                }
                                if (count === 0) {
                                    key.columns[0].splice(i, 1);
                                }
                            }
                            if (key.columns[0].length === 0) {
                                key.columns[0].push({text: "Column", colField: "column"});
                            }
                        });
                    }
                }

            };
            // watch for changes in seletced fields ui-select for changing orders and custom joins
            $scope.$watch("selectedFields", function (feature) {
                if ($scope.selectedFields !== undefined && $scope.selectedFields instanceof Object === true && angular.isArray($scope.selectedFields)) {
                    var field = '';
                    angular.forEach($scope.selectedFields, function (item, index) {
                        if (index === 0) {
                            field = item.id;
                        } else {
                            field += ',' + item.id;
                        }
                    });
                    $scope.extraFields = [];
                    angular.forEach($scope.AllFieldDetailList, function (key) {
                        angular.forEach($scope.selectedFields, function (item) {
                            if (item.id === key.id) {
                                var count = 0;
                                angular.forEach($scope.extraFields, function (data) {
                                    if (data.text == key.text) {
                                        count++;
                                    }
                                })
                                if (count == 0) {
                                    $scope.extraFields.push({text: key.text, colField: key.id, newField: true});
                                }
                            }
                        });
                    });
                    $scope.selectedFields = field;
                }
                else {
//                    $scope.addOrders();
                    $scope.updateOrders();
                    $scope.configureRelationship();
                }
            });
            // Remove method for ui-select2 select features to change fields associated with the fields
            $(document).on("select2-removing", "#selectFeatures", function (e) {
                tableRemovedFlag = true;
                var count = 0;
                var selectedFields = $("#fieldSelect").select2("data");
                var selectedFieldsNew = [];
                $scope.selectedFields = "";
                var valuesCheck = [];
                var selectedFieldsNews = [];
                for (var key in $scope.featureFieldMap) {
                    if (key === e.val) {
                        $scope.sectionFieldMap = $scope.featureFieldMap[key];
                        for (var sectionKey in $scope.sectionFieldMap) {
                            angular.forEach($scope.sectionFieldMap[sectionKey], function (fieldItem) {
                                var customName = fieldItem.dbBaseName + "." + fieldItem.dbFieldName;
                                angular.forEach(selectedFields, function (col) {
                                    if (col.id === customName) {
                                        for (var i = 0; i < selectedFieldsNew.length; i++) {
                                            if (selectedFieldsNew[i] === col) {
                                                count++;
                                            }
                                        }
                                        if (count === 0) {
                                            selectedFieldsNew.push(col);
                                            valuesCheck.push(col.id);
                                        }

                                    }
                                });
                            });
                        }
                    }
                }
                Array.prototype.remove = function () {
                    var what, a = arguments, L = a.length, ax;
                    while (L && this.length) {
                        what = a[--L];
                        while ((ax = this.indexOf(what)) !== -1) {
                            this.splice(ax, 1);
                        }
                    }
                    return this;
                };
                var finalValueCheck = [];
                angular.forEach(selectedFields, function (item) {
                    finalValueCheck.push(item.id);
                    selectedFieldsNews.push(item);
                });
                angular.forEach(selectedFields, function (item) {
                    angular.forEach(valuesCheck, function (valueItem) {
                        if (item.id === valueItem) {
                            for (var i = 0; i < finalValueCheck.length; i++) {
                                if (finalValueCheck[i] === valueItem) {
                                    finalValueCheck.remove(valueItem);
                                }
                            }
                            for (var i = 0; i < selectedFieldsNews.length; i++) {
                                if (selectedFieldsNews[i].id === valueItem) {
                                    selectedFieldsNews.remove(selectedFieldsNews[i]);
                                }

                            }
                        }
                    });
                });
                for (var i = 0; i < finalValueCheck.length; i++) {
                    if ($scope.selectedFields.length === 0) {
                        $scope.selectedFields = $scope.selectedFields + finalValueCheck[i];
                    } else {
                        $scope.selectedFields = $scope.selectedFields + "," + finalValueCheck[i];
                    }
                }
                $("#fieldSelect").select2("data", selectedFieldsNews);
            });
            //For join section in tab 1.
            $scope.configureRelationship = function () {
                if ($scope.selectedFeatures !== undefined && $scope.selectedFields !== undefined &&
                        $scope.selectedFeatures !== '' && $scope.selectedFields !== '' &&
                        !angular.isArray($scope.selectedFeatures) && !angular.isArray($scope.selectedFields)) {
                    //Initialize selected field List.
                    var featureList = $scope.selectedFeatures.split(',');
                    var fieldList = $scope.selectedFields.split(',');
                    $scope.associatedFieldList = [];
                    angular.forEach(fieldList, function (field) {
                        angular.forEach(featureList, function (feature) {
                            for (var key in $scope.featureFieldMap) {
                                if (key === feature) {
                                    $scope.sectionList = [];
                                    $scope.sectionFieldMap = $scope.featureFieldMap[key];
                                    for (var sectionKey in $scope.sectionFieldMap) {
                                        $scope.fieldNames = [];
                                        angular.forEach($scope.sectionFieldMap[sectionKey], function (fieldItem) {
                                            fieldItem.orderType = null;
                                            if (fieldItem.dbBaseName === field.split(".")[0] && fieldItem.dbFieldName === field.split(".")[1]) {
//                                                angular.forEach($scope.savedOrderMap, function(order) {
//                                                    if (fieldItem.dbBaseName === order.columnId.split(".")[0] && fieldItem.dbFieldName === order.columnId.split(".")[1]) {
//                                                        fieldItem.orderType = order.orderValue;
//                                                    }
//                                                });
                                                $scope.associatedFieldList.push(fieldItem);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    });
                    var featureIds = [];
                    angular.forEach($scope.associatedFieldList, function (item) {
                        if (featureIds.indexOf(item.feature) === -1) {
                            featureIds.push(item.feature);
                        }
                    });
                    //Retrieve feature names by features ids.
                    if (angular.isArray(featureIds) && featureIds.length > 0) {
                        ReportBuilderService.retrieveFeatureNameByIds(featureIds, function (res) {
                            var index = 0;
                            var newFeatures = [];
                            angular.forEach(featureIds, function (featureId) {
                                angular.forEach(res.data, function (value, key) {

                                    if (featureId === parseInt(key)) {
                                        var isExists = false;
                                        angular.forEach($scope.selectedFeaturesList, function (temp) {
                                            if (temp.featureId === featureId) {
                                                isExists = true;
                                            }
                                        });
                                        if (!isExists) {
                                            $scope.selectedFeaturesList.push({featureName: value, featureId: featureId});
                                            $scope.featureDetailsList.push({featureName: value, featureId: featureId});
                                            newFeatures.push(featureId);
                                        }
                                    }
                                });
                            });
                            for (var i = $scope.selectedFeaturesList.length - 1; i >= 0; i--) {
                                if (featureIds.indexOf($scope.selectedFeaturesList[i].featureId) === -1) {
                                    $scope.selectedFeaturesList.splice(i, 1);
                                }
                            }
                            for (var i = $scope.featureDetailsList.length - 1; i >= 0; i--) {
                                if (featureIds.indexOf($scope.featureDetailsList[i].featureId) === -1) {
                                    $scope.featureDetailsList.splice(i, 1);
                                }
                            }
                            if ($scope.selectedFeaturesList.length > 0) {
                                var index1 = 0;
                                angular.forEach($scope.selectedFeaturesList, function (item) {
                                    item.sequence = ++index1;
                                });
                            }
                            $scope.selectedFeaturesList.sort(SortBySequence);
                            //Set fieldList with section name wrt feature
                            angular.forEach($scope.selectedFeaturesList, function (item, itemIndex) {
                                if (newFeatures.indexOf(item.featureId) > -1) {
                                    var fieldsList = $scope.retrieveSectionFieldsOfFeature(item.featureName);
                                    item.fieldsList = fieldsList;
                                    var multiFields = {
                                        multiple: false,
                                        closeOnSelect: true,
                                        placeholder: 'Select Field',
                                        data: fieldsList,
                                        initSelection: function (element, callback) {
                                            if ($scope.isEditMode) {
                                                var data = {};
                                                if ($scope.report.joinAttributes !== undefined) {
                                                    var joinAttributes = JSON.parse($scope.report.joinAttributes);
                                                    if (joinAttributes !== null && joinAttributes !== undefined) {
                                                        angular.forEach(joinAttributes, function (reportJoin) {
                                                            if (reportJoin.featureId === item.featureId) {
                                                                if (reportJoin.joinAttributes !== null) {
                                                                    var local = reportJoin.joinAttributes.split("=")[0];
                                                                    var foreign = reportJoin.joinAttributes.split("=")[1];
                                                                    angular.forEach(item.fieldsList, function (item) {
                                                                        angular.forEach(item.children, function (fieldItem) {
                                                                            if (fieldItem.id === local) {
                                                                                data = fieldItem;
                                                                            }
                                                                        });
                                                                        callback(data);
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            } else
                                            {
                                                var data = [];
                                                callback(data);
                                            }
                                        },
                                        formatResult: function (item) {
                                            return item.text;
                                        },
                                        formatSelection: function (item) {
                                            return item.text;
                                        }
                                    };
                                    item.localFields = multiFields;
                                    var referenceFields = angular.copy(multiFields);
                                    referenceFields.data = [];
                                    referenceFields.initSelection = undefined;
                                    referenceFields.initSelection = function (element, callback) {
                                        if ($scope.isEditMode) {
                                            var data = {};
                                            if ($scope.report.joinAttributes !== undefined) {
                                                var joinAttributes = JSON.parse($scope.report.joinAttributes);
                                                if (joinAttributes !== null && joinAttributes !== undefined) {
                                                    angular.forEach(joinAttributes, function (reportJoin) {
                                                        if (reportJoin.featureId === item.featureId) {
                                                            if (reportJoin.joinAttributes !== null) {
                                                                var local = reportJoin.joinAttributes.split("=")[0];
                                                                var foreign = reportJoin.joinAttributes.split("=")[1];
                                                                angular.forEach(item.referenceFields.data, function (item) {
                                                                    angular.forEach(item.children, function (fieldItem) {
                                                                        if (fieldItem.id === foreign) {
                                                                            data = fieldItem;
                                                                        }
                                                                    });
                                                                    callback(data);
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        } else
                                        {
                                            var data = [];
                                            callback(data);
                                        }
                                    };
                                    item.referenceFields = referenceFields;
                                }
                            });
                            if ($scope.isEditMode) {
                                if ($scope.report.joinAttributes !== undefined) {
                                    var joinAttributes = JSON.parse($scope.report.joinAttributes);
                                    if (joinAttributes !== null && joinAttributes !== undefined) {
                                        joinAttributes.sort(SortBySequence);
                                        angular.forEach(joinAttributes, function (reportJoin) {
                                            angular.forEach($scope.selectedFeaturesList, function (currentJoin, index) {
                                                if (reportJoin.featureId === currentJoin.featureId) {
                                                    currentJoin.sequence = reportJoin.sequence;
                                                    if (reportJoin.refFeatureId !== null) {
                                                        currentJoin.referenceFeature = reportJoin.refFeatureId;
                                                        $scope.updateReferenceFields(reportJoin.refFeatureId, index);
                                                    }
                                                    if (reportJoin.joinAttributes !== null) {
                                                        var local = reportJoin.joinAttributes.split("=")[0];
                                                        var foreign = reportJoin.joinAttributes.split("=")[1];
                                                        angular.forEach(currentJoin.fieldsList, function (item) {
                                                            angular.forEach(item.children, function (fieldItem) {
                                                                if (fieldItem.id === local) {
                                                                    currentJoin.localField = fieldItem;
                                                                }
                                                            });
                                                        });
                                                        angular.forEach(currentJoin.referenceFields.data, function (item) {
                                                            angular.forEach(item.children, function (fieldItem) {
                                                                if (fieldItem.id === foreign) {
                                                                    currentJoin.referenceField = fieldItem;
                                                                }
                                                            });
                                                        });
                                                    }
                                                }
                                            });
                                        });
                                    }
                                    $scope.selectedFeaturesList.sort(SortBySequence);
                                }
                            }
                        });
                    }
                } else {
                    $scope.selectedFeaturesList = [];
                }
            };
            $scope.updateReferenceFields = function (featureId, index) {
                var fields;
                angular.forEach($scope.selectedFeaturesList, function (item) {
                    if (item.featureId === parseInt(featureId)) {
                        fields = item.localFields.data;
                    }
                });
                $scope.selectedFeaturesList[index].referenceField = '';
                $scope.selectedFeaturesList[index].referenceFields.data.splice(0, $scope.selectedFeaturesList[index].referenceFields.data.length);
                angular.forEach(fields, function (item) {
                    $scope.selectedFeaturesList[index].referenceFields.data.push(item);
                });
            };
            $scope.shiftRelationshipUp = function (index) {
                if (index !== 0) {
                    var prev = $scope.selectedFeaturesList[index - 1];
                    $scope.selectedFeaturesList[index - 1] = $scope.selectedFeaturesList[index];
                    $scope.selectedFeaturesList[index] = prev;
                }
            };
            $scope.shiftRelationshipDown = function (index) {
                if (index !== $scope.selectedFeaturesList.length - 1) {
                    var next = $scope.selectedFeaturesList[index + 1];
                    $scope.selectedFeaturesList[index + 1] = $scope.selectedFeaturesList[index];
                    $scope.selectedFeaturesList[index] = next;
                }
            };
            $scope.retrieveSectionFields = function (features) {
                if (features !== undefined && features instanceof Object === false && features !== '') {
                    var featureList = features.split(",");
                    $scope.extraFields = [];
                    $scope.fieldsByFeature = [];
                    angular.forEach(featureList, function (item) {
                        for (var key in $scope.featureFieldMap) {
                            if (key === item) {
                                $scope.sectionList = [];
                                $scope.sectionFieldMap = $scope.featureFieldMap[key];
                                for (var sectionKey in $scope.sectionFieldMap) {
                                    $scope.fieldNames = [];
                                    angular.forEach($scope.sectionFieldMap[sectionKey], function (fieldItem) {
                                        $scope.fieldsByFeature.push(fieldItem.dbBaseName + "." + fieldItem.dbFieldName);
                                        $scope.extraFields.push({text: fieldItem.fieldLabel, colField: fieldItem.dbBaseName + "." + fieldItem.dbFieldName, newField: true, type: "col", feature: item});
                                        $scope.fieldNames.push({id: fieldItem.dbBaseName + "." + fieldItem.dbFieldName, text: fieldItem.fieldLabel});
                                    });
                                    $scope.sectionList.push({});
                                    $scope.sectionList[$scope.sectionList.length - 1].text = sectionKey;
                                    $scope.sectionList[$scope.sectionList.length - 1].children = $scope.fieldNames;
                                }
                                $scope.fieldList.push({});
                                $scope.fieldList[$scope.fieldList.length - 1].text = item;
                                $scope.fieldList[$scope.fieldList.length - 1].children = $scope.sectionList;
                            }

                        }
                    });
//                    $('#fieldSelect').select2('val',$scope.fieldList);
                }
            };
            $scope.retrieveSectionFieldsOfFeature = function (feature) {
                if (feature !== undefined && feature instanceof Object === false && feature !== '') {
                    var featureList = feature.split(",");
                    var fieldList = [];
                    angular.forEach(featureList, function (item) {
                        for (var key in $scope.featureFieldMap) {
                            if (key === item) {
                                var sectionList = [];
                                var sectionFieldMap = $scope.featureFieldMap[key];
                                for (var sectionKey in sectionFieldMap) {
                                    var fieldNames = [];
                                    angular.forEach(sectionFieldMap[sectionKey], function (fieldItem) {
                                        var isDffFlag = false;
                                        if (fieldItem.validationPattern !== null && fieldItem.validationPattern !== undefined) {
                                            try {
                                                var validationPattern = JSON.parse(fieldItem.validationPattern);
                                                if (validationPattern.isDff !== null || validationPattern.isDff !== undefined) {
                                                    if (validationPattern.isDff) {
                                                        isDffFlag = true;
                                                    }
                                                }
                                            } catch (exception) {
                                                console.log('Can not parse JSON validationPattern' + exception);
                                            }
                                        }
//                                        console.log(JSON.stringify(fieldItem));
                                        if (fieldItem.dbBaseType !== 'MDB' && fieldItem.componentType !== 'UserMultiSelect') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + fieldItem.dbFieldName, text: fieldItem.fieldLabel});
                                        }
                                        else if (fieldItem.dbBaseName === 'invoice' && fieldItem.dbFieldName === 'invoiceId$AG$String') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + "_id", text: fieldItem.fieldLabel});
                                        }
                                        else if (fieldItem.dbBaseName === 'parcel' && fieldItem.dbFieldName === 'parcelId$AG$String') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + "_id", text: fieldItem.fieldLabel});
                                        }
                                        else if (fieldItem.dbBaseName === 'lot' && fieldItem.dbFieldName === 'lotID$AG$String') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + "_id", text: fieldItem.fieldLabel});
                                        }
                                        else if (fieldItem.dbBaseName === 'packet' && fieldItem.dbFieldName === 'packetID$AG$String') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + "_id", text: fieldItem.fieldLabel});
                                        } else if (isDffFlag && $scope.innerDocumentBaseNameSet.indexOf(fieldItem.dbBaseName) === -1 && $scope.caratRaangeFieldSet.indexOf(fieldItem.dbFieldName) === -1) {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + '"' + fieldItem.dbFieldName + '"', text: fieldItem.fieldLabel});
                                        } else if (sectionKey === 'General' && fieldItem.dbBaseType === 'MDB' && fieldItem.isSubFormValue !== true && fieldItem.componentType !== 'SubEntity') {
                                            fieldNames.push({id: fieldItem.dbBaseName + "." + '' + fieldItem.dbFieldName + '', text: fieldItem.fieldLabel});
                                        }
                                    });
                                    if (fieldNames.length > 0) {
                                        sectionList.push({});
                                        sectionList[sectionList.length - 1].text = sectionKey;
                                        sectionList[sectionList.length - 1].children = fieldNames;
                                    }
                                }
//                                fieldList.push({});
//                                fieldList[fieldList.length - 1].text = item;
//                                fieldList[fieldList.length - 1].children = sectionList;
                                fieldList = sectionList;
                            }

                        }
                    });
                    return fieldList;
                }
            };
            //Fill column format options as per datatype
            $scope.getAvailableFormat = function (dataType) {
                if (dataType === 'int8') {
                    return availableFormats.numberFormats;
                } else if (dataType === 'timestamp') {
                    return availableFormats.dateFormats;
                } else {
                    return availableFormats.textFormats;
                }
            };
//            
            $scope.updateOrders = function () {
                $scope.orderFields = [];
                if ($scope.selectedFields !== undefined && $scope.selectedFields.length !== 0) {
                    if ($scope.isEditMode && !$scope.isEditModeOrderInitialized) {
                        $scope.isEditModeOrderInitialized = true;
                        if ($scope.report.orderAttributes !== undefined && $scope.report.orderAttributes !== null) {
                            var orderAttributes = JSON.parse($scope.report.orderAttributes);
                            angular.forEach(orderAttributes, function (orderAttr) {
                                if (orderAttr.sequence === 1) {
                                    $scope.primaryOrder.column = orderAttr.columnName;
                                    $scope.primaryOrder.orderValue = orderAttr.orderValue;
                                } else {
                                    $scope.secondaryOrder.column = orderAttr.columnName;
                                    $scope.secondaryOrder.orderValue = orderAttr.orderValue;
                                }
                            });
                        }
                    }
                    var fields = $scope.selectedFields.split(",");
                    //Retrieve all field list
                    angular.forEach($scope.AllFieldsEntitys, function (item) {
                        for (var i = 0; i < fields.length; i++) {
                            if (fields[i] === item.dbBaseName + "." + item.dbFieldName && $scope.skipOrderComponentTypes.indexOf(item.componentType) === -1) {
                                $scope.orderFields.push({id: item.dbBaseName + "." + item.dbFieldName, text: item.fieldLabel});
                            }
                        }
                    });
                    //Update selected value in UI, In case if it is removed from field selection.
//                    console.log('primary-----' + JSON.stringify($scope.primaryOrder));
//                    console.log('secondary-----' + JSON.stringify($scope.secondaryOrder));
//                    console.log('OrderFields----' + JSON.stringify($scope.orderFields));
//                    console.log('selected fields----' + JSON.stringify($scope.selectedFields));
//                    console.log('AllFieldDetailList----' + JSON.stringify($scope.AllFieldDetailList.length));
                    if ($scope.orderFields.length > 0) {
                        if ($scope.primaryOrder.column !== undefined) {
                            var isSelectedOrderExists = false;
                            angular.forEach($scope.orderFields, function (orderField) {
                                if (!isSelectedOrderExists) {
                                    if ($scope.primaryOrder.column === orderField.id) {
                                        isSelectedOrderExists = true;
                                    }
                                }
                            });
                            if (!isSelectedOrderExists) {
                                $scope.primaryOrder.column = undefined;
                            }
                        }
                        if ($scope.secondaryOrder.column !== undefined) {
                            var isSelectedOrderExists = false;
                            angular.forEach($scope.orderFields, function (orderField) {
                                if (!isSelectedOrderExists) {
                                    if ($scope.secondaryOrder.column === orderField.id) {
                                        isSelectedOrderExists = true;
                                    }
                                }
                            });
                            if (!isSelectedOrderExists) {
                                $scope.secondaryOrder.column = undefined;
                            }
                        }
                    }
                    //Update primary order selection list
                    if ($scope.primaryOrderFields.length > 0) {
                        angular.forEach($scope.orderFields, function (orderField) {
                            var isOrderExists = false;
                            angular.forEach($scope.primaryOrderFields, function (primaryOrderField) {
                                if (orderField.id === primaryOrderField.id) {
                                    isOrderExists = true;
                                }
                            });
                            if (!isOrderExists) {
                                $scope.primaryOrderFields.push(orderField);
                            }
                        });
                        for (var i = $scope.primaryOrderFields.length - 1; i >= 0; i--) {
                            var isOrderExist = false;
                            angular.forEach($scope.orderFields, function (orderField) {
                                if ($scope.primaryOrderFields[i].id === orderField.id) {
                                    isOrderExist = true;
                                }
                            });
                            if (!isOrderExist) {
                                $scope.primaryOrderFields.splice(i, 1);
                            }
                        }
                    } else {
                        $scope.primaryOrderFields = angular.copy($scope.orderFields);
                    }
                    //Update secondary order selection list.
                    if ($scope.secondaryOrderFields.length > 0) {
                        angular.forEach($scope.orderFields, function (orderField) {
                            var isOrderExists = false;
                            angular.forEach($scope.secondaryOrderFields, function (secondaryOrderField) {
                                if (orderField.id === secondaryOrderField.id) {
                                    isOrderExists = true;
                                }
                            });
                            if (!isOrderExists) {
                                $scope.secondaryOrderFields.push(orderField);
                            }
                        });
                        for (var i = $scope.secondaryOrderFields.length - 1; i >= 0; i--) {
                            var isOrderExist = false;
                            angular.forEach($scope.orderFields, function (orderField) {
                                if ($scope.secondaryOrderFields[i].id === orderField.id) {
                                    isOrderExist = true;
                                }
                            });
                            if (!isOrderExist) {
                                $scope.secondaryOrderFields.splice(i, 1);
                            }
                        }
                    } else {
                        $scope.secondaryOrderFields = angular.copy($scope.orderFields);
                    }
//                    console.log('Secondary fields---'+JSON.stringify($scope.secondaryOrderFields));
                    if ($scope.primaryOrder.column !== undefined) {
                        for (var i = $scope.secondaryOrderFields.length - 1; i >= 0; i--) {
                            if ($scope.primaryOrder.column === $scope.secondaryOrderFields[i].id) {
                                $scope.secondaryOrderFields.splice(i, 1);
                            }
                        }
                    }
                    if ($scope.secondaryOrder.column !== undefined) {
                        for (var i = $scope.primaryOrderFields.length - 1; i >= 0; i--) {
                            if ($scope.secondaryOrder.column === $scope.primaryOrderFields[i].id) {
                                $scope.primaryOrderFields.splice(i, 1);
                            }
                        }
                    }
                } else {
                    $scope.primaryOrder.column = undefined;
                    $scope.secondaryOrder.column = undefined;
                    $scope.primaryOrderFields = [];
                    $scope.secondaryOrderFields = [];
                }
            };

            $scope.doesAliasExists = function (col, filterForm, index, element) {
                if (col.alias === '') {
                    col.alias = col.fieldLabel;
                }
                //Update the column value in case of grouping.
                $scope.persistGroupChanges();
                var isValid = true;
                angular.forEach($scope.columns, function (item, colIndex) {
                    if ((item.alias !== undefined && item.alias !== '') && item.alias === col.alias) {
                        if (col.dbBaseName + '.' + col.colName !== item.dbBaseName + '.' + item.colName) {
                            isValid = false;
                        }
                    }
                });
                if (isValid) {
                    console.log('if--------------------');
                    element.$setValidity("exists", true);
                } else {
                    console.log('else----------');
                    element.$setValidity("exists", false);
                    element.$dirty = true;
                }
            };

            $scope.doesTableNameExists = function (tableName, renameTableForm) {
                //Update the column value in case of grouping.
                var isValid = true;
                if ($scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                    angular.forEach($scope.models.dropzones.A, function (key) {
                        if (key.text == tableName) {
                            isValid = false;
                        }
                    });
                }
                if (isValid) {
                    console.log('if--------------------');
                    renameTableForm.changeName.$setValidity("exists", true);
                } else {
                    console.log('else----------');
                    renameTableForm.changeName.$setValidity("exists", false);
                }
            };

            $scope.submitColumnConfigurations = function (configureForm, tabs) {
                $scope.colorApplied = false;
                $scope.subbmited = true;
                $scope.registerGrid = false;
                $scope.groupInfo.isGroupAdded = false;
                $scope.isGlobalFilterAdded = false;
                $scope.persistGroupChanges();
                var seqCount = 0;
                var oldItem;
                var fieldList = $scope.selectedFields.split(',');
                var tempFieldList = [];
                angular.forEach(fieldList, function (field) {
                    if ($scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                        angular.forEach($scope.models.dropzones.A, function (key) {
                            angular.forEach(key.columns[0], function (data) {
                                if (data.colField === field) {
                                    var cnt = 0;
                                    angular.forEach(tempFieldList, function (tpField) {
                                        if ((tpField.dbBaseName + "." + tpField.dbFieldName) === field) {
                                            cnt++;
                                        }
                                    });
                                    if (cnt === 0) {
                                        var fields = {tableName: key.text, dbBaseName: field.split(".")[0], dbFieldName: field.split(".")[1]};
                                        tempFieldList.push(fields);
                                    }
                                }
                            });
                        });
                    }
                });
                angular.forEach(tempFieldList, function (field) {
                    if ($scope.models.dropzones.A != undefined && $scope.models.dropzones.A.length > 0) {
                        angular.forEach($scope.models.dropzones.A, function (key) {
                            angular.forEach(key.columns[0], function (data) {
                                if (data.colField == (field.dbBaseName + "." + field.dbFieldName)) {
                                    var tables = field.tableName.split(",");
                                    if (tables.indexOf(key.text) == -1) {
                                        field.tableName = field.tableName + "," + key.text;
                                    }
                                }
                            });
                        });
                    }
                });
                angular.forEach($scope.columns, function (colField) {
                    angular.forEach(tempFieldList, function (field) {
                        if (field.dbBaseName + '.' + field.dbFieldName === colField.dbBaseName + '.' + colField.colName) {
                            colField.tableName = field.tableName;
                        }
                    });
                });
                angular.forEach($scope.columns, function (item) {
                    oldItem = item;
                    angular.forEach($scope.columns, function (newItem) {
                        if ((oldItem.alias === '' && newItem.alias === '') || (oldItem.alias === newItem.alias)) {
                            if (oldItem.dbBaseName + '.' + oldItem.colName !== newItem.dbBaseName + '.' + newItem.colName) {
                                seqCount++;
                            }
                        }
                    });
                });

                //Set Relationship to record level.
                var relationship = [];
                angular.forEach($scope.selectedFeaturesList, function (featureRelation, featureIndex) {
                    var relationAttr = {};
                    relationAttr.featureId = featureRelation.featureId;
                    if (featureRelation.localField === undefined || featureRelation.referenceField === undefined) {
                        relationAttr.joinAttributes = null;
                        relationAttr.refFeatureId = null;
                    }
                    else {
                        var localField;
                        var referenceField;
                        if (featureRelation.localField instanceof Object === true) {
                            localField = featureRelation.localField.id;
                        } else {
                            localField = featureRelation.localField;
                        }
                        if (featureRelation.referenceField instanceof Object === true) {
                            referenceField = featureRelation.referenceField.id;
                        } else {
                            referenceField = featureRelation.referenceField;
                        }
                        var localDatatype = null;
                        var foreignDatatype = null;
                        var localComponentType = null;
                        var foreignComponentType = null;
                        if ($scope.fieldToDataTypeMap[localField.replace(/"/g, "")] !== undefined && $scope.fieldToDataTypeMap[localField.replace(/"/g, "")] !== null) {
                            localField = localField.replace(/"/g, "");
                            localDatatype = $scope.setDatabaseDatatype($scope.fieldToDataTypeMap[localField.replace(/"/g, "")]);
                            localComponentType = $scope.fieldToComponentTypeMap[localField];
                        }
                        if ($scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")] !== undefined && $scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")] !== null) {
                            referenceField = referenceField.replace(/"/g, "");
                            foreignDatatype = $scope.setDatabaseDatatype($scope.fieldToDataTypeMap[referenceField.replace(/"/g, "")]);
                            foreignComponentType = $scope.fieldToComponentTypeMap[referenceField];
                        }
                        relationAttr.joinAttributes = localField + "=" + referenceField;
                        relationAttr.refFeatureId = parseInt(featureRelation.referenceFeature);
                        relationAttr.localDataType = localDatatype;
                        relationAttr.refDataType = foreignDatatype;
                        relationAttr.localComponentType = localComponentType;
                        relationAttr.refComponentType = foreignComponentType;
                    }
                    if (featureIndex === 0) {
                        relationAttr.joinAttributes = null;
                        relationAttr.refFeatureId = null;
                    }
                    relationAttr.sequence = featureIndex + 1;
                    relationship.push(relationAttr);
                });
//                        console.log('relation---------'+JSON.stringify(relationship));
                $scope.report.joinAttributes = JSON.stringify(relationship);
                $scope.columnForAlias();

                if (seqCount > 0) {
                    tabs[2].active = false;
                    tabs[2].disabled = true;
                    $rootScope.addMessage("Unique alias name required", $rootScope.failure);
                } else if ($scope.isGroupByCheck && $scope.groupByLevels.length === 0) {
                    tabs[2].active = false;
                    tabs[2].disabled = true;
                    $rootScope.addMessage("Select at least one level grouping", $rootScope.failure);
                } else {
                    if (configureForm.$valid) {
                        //Clear all validation messages.
                        $rootScope.validations.splice(0, $rootScope.validations.length);
                        $scope.report.columns = angular.copy($scope.columns);
                        angular.forEach($scope.report.columns, function (col) {
                            col.filter = null;
                            if (col.selectedFilterTypesList !== undefined && col.selectedFilterTypesList !== null) {
                                var resultFilterObj = [];
                                angular.forEach(col.selectedFilterTypesList, function (item) {
                                    var filterType = {};
                                    filterType.filter = item.filter;
                                    filterType.filterValFirst = item.filterValFirst;
                                    filterType.filterValSecond = item.filterValSecond;
                                    if (col.dataType === "varchar" && angular.isArray(filterType.filterValFirst)) {
                                        var filterValueString = '';
                                        angular.forEach(filterType.filterValFirst, function (item, index) {
                                            if (index === 0) {
                                                filterValueString = item.id;
                                            } else {
                                                filterValueString += ',' + item.id;
                                            }
                                        });
                                        filterType.filterValFirst = filterValueString;
                                    }
                                    if (col.dataType === "timestamp" && filterType.filterValFirst !== undefined) {
                                        var d = new Date(filterType.filterValFirst);
                                        var dateToSend;
                                        var month;
                                        var date;
//                                        if (col.componentType === "Date range") {
//                                            if ((d.getMonth() + 1) < 10) {
//                                                dateToSend = d.getDate() + "/0" + (d.getMonth() + 1) + "/" + d.getFullYear();
//                                            } else {
//                                                dateToSend = d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();
//                                            }
//                                            filterType.filterValFirst = dateToSend;
//                                            if (filterType.filterValSecond !== undefined) {
//                                                d = new Date(filterType.filterValSecond);
//                                                if ((d.getMonth() + 1) < 10) {
//                                                    dateToSend = d.getDate() + "/0" + (d.getMonth() + 1) + "/" + d.getFullYear();
//                                                } else {
//                                                    dateToSend = d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();
//                                                }
//                                                filterType.filterValSecond = dateToSend;
//                                            }
//                                        } else {
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
                                        filterType.filterValFirst = dateToSend;
                                        if (filterType.filterValSecond !== undefined) {
                                            d = new Date(filterType.filterValSecond);
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
                                            filterType.filterValSecond = dateToSend;
                                        }
//                                        }

                                    }
                                    if (col.componentType === 'UserMultiSelect') {
                                        filterType.userMultiSelectLabel = item.userMultiSelectLabel;
                                    }
                                    resultFilterObj.push(filterType);
                                });
                                if (resultFilterObj.length > 0) {
                                    col.filter = JSON.stringify(resultFilterObj);
                                }
                            }
                            col.availableFilterTypes = undefined;
                            col.availableFilterValues = undefined;
                            col.availableFilterValueSelect = undefined;
                            col.availableFilterValueSelectEqual = undefined;
                            col.availableFilterValueSelectNotEqual = undefined;
                            col.filterValFirst = undefined;
                            col.selectedFilterType = undefined;
                            col.selectedFilterTypesList = undefined;
                            col.filterValSecond = undefined;
                            col.selectedFilterTypes = undefined;
//                    col.id = undefined;
                            col.order = undefined;
                            col.total = undefined;
                            col.checked = undefined;
                            if (col.columnFilter !== undefined) {
                                col.columnFilter = undefined;
                            }
                            if (col.availableColumnFormats !== undefined) {
                                col.availableColumnFormats = undefined;
                            }
                            col.isGroupBy = undefined;
                            col.filterValFirstInvalid = undefined;
                            col.filterValSecondInvalid = undefined;
                            col.level = undefined;
                            col.currencySymbol = undefined;
                            col.format = undefined;
                            col.associatedCurrency = undefined;
                            col.userMultiSelection = undefined;
                            col.first_dt_inp_opnd = undefined;
                            col.second_dt_inp_opnd = undefined;
                        });
                        //Again generate Query is called to retrieve modified Query.
                        var index = 0;
                        angular.forEach($scope.report.columns, function (item) {
                            item.fieldSequence = ++index;
                        });
                        angular.forEach($scope.columns, function (item) {
                            item.fieldSequence = ++index;
                        });
                        //Adding group by attributes to report.
                        $scope.report.groupAttributes = null;
                        if ($scope.isGroupByCheck) {
                            var groupAttr = {};
                            if ($scope.groups.length > 0) {
                                groupAttr.groups = [];
                                angular.forEach($scope.groups, function (group, index) {
                                    var obj = {};
                                    obj.groupName = group.groupName;
                                    obj.sequence = index + 1;
                                    if (group.sequence === 1 && $scope.groups.length === 1) {
                                        obj.groupName = null;
                                    }
                                    if (group.groupItems.length > 0) {
                                        var groupItems = group.groupItems.split(",");
                                        for (var i = 0; i < groupItems.length; i++) {
                                            var isBreak = false;
                                            if (!isBreak) {
                                                angular.forEach($scope.report.columns, function (column) {
                                                    if (groupItems[i].split(".")[0] === column.dbBaseName && groupItems[i].split(".")[1] === column.colName) {
                                                        if (column.alias === undefined || column.alias.length === 0) {
                                                            groupItems[i] = column.fieldLabel;
                                                        } else {
                                                            groupItems[i] = column.alias;
                                                        }
                                                        isBreak = true;
                                                    }
                                                });
                                            }
                                        }
                                        var groupItemsString = '';
                                        angular.forEach($scope.report.columns, function (column) {
                                            var label;
                                            if (column.alias === undefined || column.alias.length === 0) {
                                                label = column.fieldLabel;
                                            } else {
                                                label = column.alias;
                                            }
                                            angular.forEach(groupItems, function (item) {
                                                if (item === label) {
                                                    if (groupItemsString.length === 0) {
                                                        groupItemsString = item;
                                                    } else {
                                                        groupItemsString += "," + item;
                                                    }
                                                }
                                            });
                                        });
                                        obj.groupItems = groupItemsString;
                                    }
                                    groupAttr.groups.push(obj);
                                });
                            }
                            if ($scope.groupByLevels.length > 0) {
                                groupAttr.groupBy = [];
                                angular.forEach($scope.groupByLevels, function (group, index) {
                                    var obj = {};
                                    obj.level = index + 1;
                                    obj.sequence = index + 1;
                                    if (group.groupByItems.length > 0) {
                                        var groupItems = group.groupByItems.split(",");
                                        for (var i = 0; i < groupItems.length; i++) {
                                            var isBreak = false;
                                            if (!isBreak) {
                                                angular.forEach($scope.report.columns, function (column) {
                                                    if (groupItems[i].split(".")[0] === column.dbBaseName && groupItems[i].split(".")[1] === column.colName) {
                                                        if (column.alias === undefined || column.alias.length === 0) {
                                                            groupItems[i] = column.fieldLabel;
                                                        } else {
                                                            groupItems[i] = column.alias;
                                                        }
                                                        isBreak = true;
                                                    }
                                                });
                                            }
                                        }
                                        var groupItemsString = '';
                                        angular.forEach($scope.report.columns, function (column) {
                                            var label;
                                            if (column.alias === undefined || column.alias.length === 0) {
                                                label = column.fieldLabel;
                                            } else {
                                                label = column.alias;
                                            }
                                            angular.forEach(groupItems, function (item) {
                                                if (item === label) {
                                                    if (groupItemsString.length === 0) {
                                                        groupItemsString = item;
                                                    } else {
                                                        groupItemsString += "," + item;
                                                    }
                                                }
                                            });
                                        });
                                        obj.fields = groupItemsString;
                                    }
                                    groupAttr.groupBy.push(obj);
                                });
                            }
                            $scope.report.groupAttributes = JSON.stringify(groupAttr);
                            $scope.groupInfo.groupAttr = groupAttr;
                        }
                        //Add order attributes.
                        $scope.report.orderAttributes = null;
                        $scope.columnOrderMap = {};
                        if ($scope.primaryOrder.column !== undefined && $scope.primaryOrder.column !== null) {
                            var orderAttributes = [];
                            var primaryOrderObj = {};
                            primaryOrderObj.columnName = $scope.primaryOrder.column;
                            primaryOrderObj.orderValue = $scope.primaryOrder.orderValue;
                            primaryOrderObj.sequence = 1;
                            orderAttributes.push(primaryOrderObj);
                            var columnLabel = '';
                            angular.forEach($scope.report.columns, function (column) {
                                if (primaryOrderObj.columnName === column.dbBaseName + "." + column.colName) {
                                    if (column.alias !== undefined && column.alias !== '') {
                                        columnLabel = column.alias;
                                    } else {
                                        columnLabel = column.fieldLabel;
                                    }
                                }
                            });
                            $scope.columnOrderMap[columnLabel] = primaryOrderObj.orderValue;
                            if ($scope.secondaryOrder.column !== undefined && $scope.secondaryOrder.column !== null) {
                                var orderObj = {};
                                orderObj.columnName = $scope.secondaryOrder.column;
                                orderObj.orderValue = $scope.secondaryOrder.orderValue;
                                orderObj.sequence = 2;
                                orderAttributes.push(orderObj);
                                var columnLabel = '';
                                angular.forEach($scope.report.columns, function (column) {
                                    if (orderObj.columnName === column.dbBaseName + "." + column.colName) {
                                        if (column.alias !== undefined && column.alias !== '') {
                                            columnLabel = column.alias;
                                        } else {
                                            columnLabel = column.fieldLabel;
                                        }
                                    }
                                });
                                $scope.columnOrderMap[columnLabel] = orderObj.orderValue;
                            }
                            $scope.report.orderAttributes = JSON.stringify(orderAttributes);
                        }
                        if ($scope.report.columns !== undefined) {
//                            console.log(JSON.stringify($scope.report));
                            ReportBuilderService.generateQuery($scope.report, function (response) {
                                console.log("response.query : " + response.query);
                                if (response.query) {
                                    $scope.report.query = response.query;
                                    $scope.convertedQuery = response.query;
                                    $scope.clearReportData();
                                    $scope.currentPage = 1;
//                                    $scope.initializeColor();
                                    $scope.executeConvertedQuery();
                                }
                            });
                        }
                    } else {
                        console.log('invalid' + configureForm);
                        tabs[2].active = false;
                        tabs[2].disabled = true;
                    }
                }
            };
            $scope.numPages = function () {
                return Math.ceil($scope.totalItems / 10);
            };
            $scope.clearReportData = function () {
                ReportBuilderService.clearReportData(function () {
                }, function () {
                });
            };
            $scope.executeConvertedQuery = function () {
                $scope.previewData = [];
                $rootScope.maskLoading();
                // ajax request to api
                $scope.report.convertedQuery = $scope.convertedQuery;
                //Initialize currency Map.
                $scope.currencyIdList = [];
                angular.forEach($scope.columns, function (column) {
                    if (column.componentType === 'Currency') {
                        $scope.currencyIdList.push(column.associatedCurrency);
                    }
                });
                $scope.resultModels = angular.copy($scope.models);
//                ReportBuilderService.retrieveCurrencyDetails($scope.currencyIdList, function(result) {
////                    console.log(JSON.stringify(result.data));
//                    if (result.data !== null) {
//                        $scope.currencyMap = result.data;
//                    }
                $scope.resultList = [];
                ReportBuilderService.retrieveReportTable($scope.report, function (res) {
                    console.log("res :" + JSON.stringify(res));
                    if ($scope.isGroupByCheck) {
                        angular.forEach(res.data.records, function (value, key) {
                            angular.forEach(value, function (item) {
                                $scope.resultList.push(angular.copy(item));
                            });
                        })
                        res.data.records = angular.copy($scope.resultList);
                    }
                    console.log("$scope.resultList :" + JSON.stringify($scope.resultList));
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

                    $scope.retrievePaginatedData();
//                    if (res.data !== undefined) {
//                        if (res.data.queryValid !== undefined) {
//                            var msg = "Invalid Query.";
//                            var type = $rootScope.failure;
//                            $rootScope.addMessage(msg, type);
//                            $rootScope.unMaskLoading();
//                        } else {
//                            $scope.previewData = res.data.records;
//                            $scope.totalItems = (res.data.totalRecords);
//                            angular.forEach($scope.columns, function(reportColumn) {
//                                reportColumn.total = undefined;
//                                if (reportColumn.showTotal !== undefined && reportColumn.showTotal === true) {
//                                    var total = 0;
//                                    angular.forEach($scope.previewData, function(row) {
//                                        var data = row[reportColumn.alias] === undefined ? (row[reportColumn.fieldLabel] === undefined ? 0 : row[reportColumn.fieldLabel]) : row[reportColumn.alias];
//                                        total += Number(data);
//                                    });
//                                    if (isNaN(total)) {
//                                        total = 0;
//                                    }
//                                    reportColumn.total = total;
//                                }
//                            });
//                            $rootScope.unMaskLoading();
////                                        $defer.resolve($scope.previewData);
//                        }
//                    }

//                                $scope.localList = angular.copy($scope.columns);
//                                $scope.localData = angular.copy($scope.previewData);
//                                console.log("localList--- : "+JSON.stringify($scope.localList));
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
//                }, function(res) {
//                    $rootScope.unMaskLoading();
//                });
//                        }
//                    });
//                }
            };
            $scope.retrieveViewCurrencyDataRightsForUser = function () {
                ReportBuilderService.retrieveViewCurrencyDataRightsOfLoggedInUser({}, function (result) {
                    $scope.viewCurrencyDataPermission = result.data;
                });
            };
            $scope.retrieveViewCurrencyDataRightsForUser();
            $scope.retrievePaginatedData = function () {
                $scope.previewData = [];
//                console.log("current page : " + $scope.currentPage);
                $rootScope.maskLoading();
                $scope.send = {
                    offSet: $scope.paginationOptions.pageNumber,
                    limit: $scope.paginationOptions.pageSize,
                    isFilter: true,
                    isGrouped: $scope.isGroupByCheck,
                    sortColumn: $scope.paginationOptions.sortColumn,
                    sortDirection: $scope.paginationOptions.sortDirection,
                    sortColumnType: $scope.paginationOptions.sortColumnType,
                    filterOptions: $scope.filterOptions
                };
                ReportBuilderService.retrieveCurrencyConfiguration({}, function (result) {
                    console.log('----------------------' + JSON.stringify(result));
                    $scope.isCurrencyVisible = result.data;
                    $scope.modifiedCurrencyColumns = [];
                    if (result.data === false || (result.data === true && $scope.viewCurrencyDataPermission === false)) {
                        angular.forEach($scope.columns, function (col) {
                            if (col.componentType !== "Currency") {
                                $scope.modifiedCurrencyColumns.push(col);
                            }
                        });
                    } else {
                        $scope.modifiedCurrencyColumns = angular.copy($scope.columns);
                    }
                    ReportBuilderService.retrievePaginatedData($scope.send, function (res) {
                        if (res.data !== undefined) {
                            if (res.data.queryValid !== undefined) {
                                var msg = "Invalid Query.";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            } else {
                                $scope.previewData = res.data.records;
                                $scope.totalItems = (res.data.totalRecords);
                                $scope.gridOptions.totalItems = $scope.totalItems;
//                                console.log('previewData---' + JSON.stringify($scope.previewData));
                                if ($scope.totalItems > 0) {
                                    $scope.isAnyRecordsExist = true;
                                } else {
                                    $scope.isAnyRecordsExist = false;
                                }
                                if (!$scope.isGroupByCheck) {
                                    //Format previewData to new format includes color.
                                    $scope.UIPreviewData = angular.copy($scope.previewData);
//                                    angular.forEach($scope.UIPreviewData, function(data, rowIndex) {
//                                        $scope.UIPreviewData[rowIndex].rowColor = 'white';
//                                        angular.forEach(data, function(value, key) {
//                                            $scope.UIPreviewData[rowIndex][key] = {value: value, color: 'white'};
//                                        });
//                                    });
                                    $scope.gridColumnDef = [];
                                    angular.forEach($scope.modifiedCurrencyColumns, function (reportColumn) {
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
                                    //Register grid for the first time
                                    $scope.registerGrid = true;
                                }
                                //Prepare data for preview if Group by is applied.
                                if ($scope.isGroupByCheck && $scope.groupInfo.groupAttr !== undefined) {
                                    $scope.groupRecordsForPreview($scope.previewData);
                                } else {
                                    if ($scope.isEditMode && !$scope.isEditModeColorInitialized) {
                                        $scope.initializeColor();
                                        $scope.updateColorConfiguration();
                                    } else {
                                        //Apply coloring if required.
                                        $scope.initializeColor();
                                        if ($scope.colorConfigData.length > 0) {
                                            $scope.applyColor($scope.colorConfigForm, true);
                                        }
                                    }
                                }
                                $rootScope.unMaskLoading();
                            }
                        }
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                        $rootScope.unMaskLoading();
                    });
                }, function (failure) {
                    $rootScope.addMessage("Some error occurred while retrieving data, try different fields and criteria", $rootScope.failure);
                    $rootScope.unMaskLoading();
                });
            };

            $scope.saveReport = function (reportDesignerForm, skipForm) {
                $scope.colorApplied = true;
                var valid = true;
                var originalForm = reportDesignerForm;
                if ($scope.report.externalReport === true) {
                    reportDesignerForm = reportDesignerForm.reportBasicForm;
                }
                $scope.submitted = true;
                for (var key in reportDesignerForm) {
                    if (key !== 'internalForm' && key !== 'relationshipForm') {
                        if (reportDesignerForm[key].$invalid) {
//                            console.log("foem" + reportDesignerForm[key] + "and key is" + key)
                            valid = false;
                            break;
                        }
                    }
                }
                if ((reportDesignerForm !== undefined && valid) || (skipForm !== undefined && skipForm === true)) {
                    $scope.clearReportData();
                    //Set pristine to original form as it is registered for location change event.
                    reportDesignerForm = originalForm;
                    if ($scope.report.externalReport !== true) {
                        $scope.report.query = $scope.convertedQuery;
                    } else {
                        var reportTemp = angular.copy($scope.report);
                        $scope.report = {};
                        $scope.report.id = reportTemp.id;
                        $scope.report.query = reportTemp.query;
                        $scope.report.description = reportTemp.description;
                        $scope.report.reportName = reportTemp.reportName;
                        $scope.report.externalReport = reportTemp.externalReport;
                        $scope.report.status = reportTemp.status;
                        $scope.report.featureId = reportTemp.featureId;
                        $scope.report.reportGroup = reportTemp.reportGroup;
                    }
                    //Add color Attributes.
                    $scope.report.colorAttributes = null;
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
                                    obj.type = colorConfig.type;
                                    obj.componentType = colorConfig.componentType;
                                    obj.operator = colorConfig.operator;
                                    obj.isGroupBy = colorConfig.isGroupBy;
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
                            if (columnColorAttributes.length > 0) {
                                compositeObj.columns = columnColorAttributes;
                                colorAttributes.push(compositeObj);
                            }
                        });
                    }
                    if (colorAttributes.length > 0) {
                        $scope.report.colorAttributes = JSON.stringify(colorAttributes);
                    }
                    if ($scope.isEditMode) {
                        angular.forEach($scope.resultModels.dropzones.A, function (key, index) {
                            var indexmodel = index;
                            if (key.id === 0) {
                                $scope.report.tableDtls.push({tableName: key.text, tableSeq: ++indexmodel});
                            } else {
                                angular.forEach($scope.report.tableDtls, function (dtl) {
                                    if (dtl.id == key.id) {
                                        dtl.tableSeq = ++indexmodel;
                                    }
                                });
                            }
                        });
                        if ($scope.report.status === 'A') {
                            $scope.updateReportFlag = true;
                        }
                        if ($scope.updateReportFlag)
                        {
                            ReportBuilderService.updateReport($scope.report, function (res) {
                                console.log("Report UPDATED--" + JSON.stringify(res));
                                if (reportDesignerForm !== undefined) {
                                    reportDesignerForm.$setPristine();
                                }
                                localStorage.removeItem('reportId');
                                $location.path("/managereport");
                            }, function () {
                                var msg = "Report cannot be updated.";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                        else {
                            $('#removePopup').modal('show');
                        }
                    }
                    else {
                        $scope.tableDtls = [];
                        angular.forEach($scope.resultModels.dropzones.A, function (key, index) {
                            var indexmodel = index;
                            $scope.tableDtls.push({tableName: key.text, tableSeq: ++indexmodel});
                        });
                        $scope.report.tableDtls = angular.copy($scope.tableDtls);
                        ReportBuilderService.saveReport($scope.report, function (res) {
                            if (reportDesignerForm !== undefined) {
                                reportDesignerForm.$setPristine();
                            }
                            localStorage.removeItem('reportId');
                            $location.path("/managereport");
                        }, function () {
                            var msg = "Report cannot be saved.";
                            var type = $rootScope.failure;
                            $rootScope.addMessage(msg, type);
                        });
                    }
                }
            };

            $scope.renameSubmitted = false;
            $scope.cancelRenamePopup = function (renameTableForm) {
                $scope.renameSubmitted = false;
                $scope.changeText = '';
                renameTableForm.$setPristine();
                $('#renameElements').modal('hide');
            };

            $scope.renameOk = function (renameTableForm) {
                $scope.renameSubmitted = true;
                if (renameTableForm.$valid) {
                    $scope.models.selected.text = angular.copy($scope.changeText);
                    angular.forEach($scope.report.tableDtls, function (dtl) {
                        if (dtl.id == $scope.models.selected.id) {
                            dtl.tableName = $scope.changeText;
                        }
                    });
                    $scope.cancelRenamePopup(renameTableForm);
                }

            };
            $scope.reportConfig = {};
            $scope.reportConfig.joins = [];
            $scope.addJoin = function () {
                if ($scope.reportConfig.joins.length === 0) {
                    $scope.reportConfig.joins.push({firstColumn: "", secondColumn: "", joinType: "="});
                }
                else {
                    var lastJoin = $scope.reportConfig.joins[$scope.reportConfig.joins.length - 1];
                    if (lastJoin.firstColumn !== undefined && lastJoin.firstColumn !== '') {
                        $scope.applyJoin();
                        $scope.reportConfig.joins.push({firstColumn: "", secondColumn: "", joinType: "="});
                    }
                }
            };
            $scope.removeJoin = function (index) {
                var joinColumn = $scope.reportConfig.joins[index];
                var joinStateMent = joinColumn.firstColumn + joinColumn.joinType + joinColumn.secondColumn;
                $scope.report.query = $scope.report.query.toLowerCase().replace(" and " + joinStateMent, "");
                if ($scope.reportConfig.joins.length === 1 || ($scope.reportConfig.joins.length === 2 && $scope.reportConfig.joins[i].firstColumn == '')) {
                    $scope.report.query = $scope.report.query.toLowerCase().replace(" where " + joinStateMent, "");
                }
                $scope.report.query = $scope.report.query.toLowerCase().replace(joinStateMent + " and ", "");
                $scope.reportConfig.joins.splice(index, 1);
            };
            $scope.applyJoin = function () {
                var query = "";
                if ($scope.report.query.toLowerCase().indexOf("where") > 0) {
                    query = $scope.report.query.substring($scope.report.query.toLowerCase().indexOf("where"));
                }
                var queryWithJoin = "";
                if (query.lastIndexOf("where") < 0) {
                    queryWithJoin += " where ";
                } else {
                    queryWithJoin += " and ";
                }

                angular.forEach($scope.reportConfig.joins, function (col, index) {
                    var joinStateMent = col.firstColumn + col.joinType + col.secondColumn;
                    if (query.indexOf(joinStateMent) < 0) {
                        queryWithJoin += joinStateMent;
                        if (index < $scope.reportConfig.joins.length - 1) {
                            queryWithJoin += " and ";
                        }
                    }
                    col.disabled = true;
                });
                if (query !== "") {
                    if (queryWithJoin != " and ") {
                        $scope.report.query += queryWithJoin;
                    }
                } else {
                    var orderByQuery = "";
                    if ($scope.report.query.toLowerCase().lastIndexOf("order by") > 0) {
                        orderByQuery = $scope.report.query.substring($scope.report.query.toLowerCase().lastIndexOf(" order by"));
                        $scope.report.query = $scope.report.query.substring(0, $scope.report.query.toLowerCase().lastIndexOf(" order by"));
                    }
                    $scope.report.query += queryWithJoin + orderByQuery;
                }

            };
            /**
             * Methods for Configuring Group By Fields List.
             */
            $scope.initializeGroupByFieldsList = function (fieldsList) {
                if (fieldsList !== undefined && fieldsList instanceof Object === false && fieldsList !== '') {
                    var fieldList = fieldsList.split(",");
                    var featureList = $scope.selectedFeatures.split(",");
                    var resultList = [];
                    angular.forEach(featureList, function (item) {
                        for (var key in $scope.featureFieldMap) {
                            if (key === item) {
                                var sectionList = [];
                                var sectionFieldMap = $scope.featureFieldMap[key];
                                for (var sectionKey in sectionFieldMap) {
                                    var fieldNames = [];
                                    angular.forEach(sectionFieldMap[sectionKey], function (fieldItem) {
                                        angular.forEach(fieldList, function (column) {
                                            if (fieldItem.dbBaseName === column.split(".")[0] && fieldItem.dbFieldName === column.split(".")[1]) {
                                                fieldNames.push({id: fieldItem.dbBaseName + "." + fieldItem.dbFieldName, text: fieldItem.fieldLabel});
                                            }
                                        });
                                    });
                                    if (fieldNames.length > 0) {
                                        sectionList.push({});
                                        sectionList[sectionList.length - 1].text = sectionKey;
                                        sectionList[sectionList.length - 1].children = fieldNames;
                                    }
                                }
                                resultList.push({});
                                resultList[resultList.length - 1].text = item;
                                resultList[resultList.length - 1].children = sectionList;
                            }
                        }
                    });
                    return resultList;
                }
            };
            $scope.initializeGroupConfiguration = function () {

//                if ($scope.groupInfo.groupByColumns.length === 0) {
                $scope.groupByColumnsList = $scope.initializeGroupByFieldsList($scope.selectedFields);
                // Initialization of ui-select2 for Group By fields.
                $scope.selectGroupByFields = {
                    allowClear: true,
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Fields',
                    data: $scope.groupByColumnsList,
                    initSelection: function (element, callback) {
                        if ($scope.isEditMode && !$scope.isGroupInitialized) {
                            $scope.isGroupInitialized = true;
                            var data = [];
                            callback(data);
                        } else
                        {
                            var data = [];
                            if ($scope.groupInfo.groupByColumns !== undefined || $scope.groupInfo.groupByColumns.length > 0) {
                                var fields;
                                if (!angular.isArray($scope.groupInfo.groupByColumns)) {
                                    fields = $scope.groupInfo.groupByColumns.split(",");
                                    angular.forEach(fields, function (item) {
                                        angular.forEach($scope.columns, function (field) {
                                            if (item === field.dbBaseName + "." + field.colName) {

                                                data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                            }
                                        });
                                    });
                                    callback(data);
                                } else {
                                    fields = angular.copy($scope.groupInfo.groupByColumns);
                                    angular.forEach(fields, function (item) {
                                        angular.forEach($scope.columns, function (field) {
                                            if (item.id === field.dbBaseName + "." + field.colName) {
                                                data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                            }
                                        });
                                    });
                                    callback(data);
                                }
                            }
                        }
                    },
                    formatResult: function (item) {
                        return item.text;
                    },
                    formatSelection: function (item) {
                        return item.text;
                    }
                };
                // Initialization of ui-select2 for Groups.
                $scope.selectGroups = {
                    allowClear: true,
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Fields',
                    data: $scope.groupByColumnsList,
                    initSelection: function (element, callback) {
                        if ($scope.isEditMode) {
                            var data = [];
                            callback(data);
                        } else
                        {
                            var data = [];
                            if ($scope.groupInfo.selectedGroup !== undefined || $scope.groupInfo.selectedGroup.length > 0) {
                                var fields;
                                if (!angular.isArray($scope.groupInfo.selectedGroup)) {
                                    fields = $scope.groupInfo.selectedGroup.split(",");
                                    angular.forEach(fields, function (item) {
                                        angular.forEach($scope.columns, function (field) {
                                            if (item === field.dbBaseName + "." + field.colName) {
                                                data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                            }
                                        });
                                    });
                                    callback(data);
                                } else {
                                    fields = angular.copy($scope.groupInfo.selectedGroup);
                                    angular.forEach(fields, function (item) {
                                        angular.forEach($scope.columns, function (field) {
                                            if (item.id === field.dbBaseName + "." + field.colName) {
                                                data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                            }
                                        });
                                    });
                                    callback(data);
                                }
                            }
                        }
                    },
                    formatResult: function (item) {
                        return item.text;
                    },
                    formatSelection: function (item) {
                        return item.text;
                    }
                };
                $scope.currentGroupIndex = 1;
//                } else {
//
//                }
            };
            $scope.updateGroupConfig = function (isGroupByCheck) {
                $scope.groupInfo.isGroupAdded = false;
                $scope.groupInfo.isGroupFieldsEmpty = false;
                $scope.groupInfo.isGroupNameEmpty = false;
                $scope.groupInfo.isGroupNameDuplicate = false;
                $scope.isGroupByCheck = isGroupByCheck;
                if ($scope.groupInfo.groupByColumns !== undefined) {
                    if ($scope.groupInfo.groupByColumns.length > 0) {
                        var fields;
                        if (!angular.isArray($scope.groupInfo.groupByColumns)) {
                            fields = $scope.groupInfo.groupByColumns.split(",");
                            for (var i = fields.length - 1; i >= 0; i--) {
                                var isExists = false;
                                angular.forEach($scope.columns, function (col) {
                                    if (fields[i] === col.dbBaseName + "." + col.colName) {
                                        isExists = true;
                                    }
                                });
                                if (!isExists) {
                                    fields.splice(i, 1);
                                }
                            }
                        } else {
                            fields = $scope.groupInfo.groupByColumns;
                            for (var i = fields.length - 1; i >= 0; i--) {
                                var isExists = false;
                                angular.forEach($scope.columns, function (col) {
                                    if (fields[i] === col.dbBaseName + "." + col.colName) {
                                        isExists = true;
                                    }
                                });
                                if (!isExists) {
                                    fields.splice(i, 1);
                                }
                            }
                        }
                        if (fields.length > 0) {
                            var res = '';
                            angular.forEach(fields, function (item) {
                                if (res.length === 0) {
                                    res = item;
                                } else {
                                    res += "," + item;
                                }
                            });
                            $scope.groupInfo.groupByColumns = res;
                        } else {
                            $scope.groupInfo.groupByColumns = '';
                        }
                    }
                }
                if ($scope.groupInfo.selectedGroup !== undefined || $scope.groupInfo.selectedGroup.length > 0) {
                    var fields;
                    if (!angular.isArray($scope.groupInfo.selectedGroup)) {
                        fields = $scope.groupInfo.selectedGroup.split(",");
                        for (var i = fields.length - 1; i >= 0; i--) {
                            var isExists = false;
                            angular.forEach($scope.columns, function (col) {
                                if (fields[i] === col.dbBaseName + "." + col.colName) {
                                    isExists = true;
                                }
                            });
                            if (!isExists) {
                                fields.splice(i, 1);
                            }
                        }
                    } else {
                        fields = $scope.groupInfo.selectedGroup;
                        for (var i = fields.length - 1; i >= 0; i--) {
                            var isExists = false;
                            angular.forEach($scope.columns, function (col) {
                                if (fields[i] === col.dbBaseName + "." + col.colName) {
                                    isExists = true;
                                }
                            });
                            if (!isExists) {
                                fields.splice(i, 1);
                            }
                        }
                    }
                    if (fields.length > 0) {
                        var res = '';
                        angular.forEach(fields, function (item) {
                            if (res.length === 0) {
                                res = item;
                            } else {
                                res += "," + item;
                            }
                        });
                        $scope.groupInfo.selectedGroup = res;
                    } else {
                        $scope.groupInfo.selectedGroup = '';
                    }
                }
                if ($scope.groups.length > 0) {
                    for (var i = $scope.groups.length - 1; i >= 0; i--) {
                        var groupItems = $scope.groups[i].groupItems.split(",");
                        var isExists = true;
                        angular.forEach(groupItems, function (groupItem) {
                            if (isExists) {
                                var isGroupItemExist = false;
                                angular.forEach($scope.columns, function (col) {
                                    if (groupItem === col.dbBaseName + "." + col.colName) {
                                        isGroupItemExist = true;
                                    }
                                });
                                if (!isGroupItemExist) {
                                    isExists = false;
                                }
                            }
                        });
                        if (!isExists) {
                            $scope.groups.splice(i, 1);
                        }
                    }
                }
                if ($scope.groupByLevels.length > 0) {
                    for (var i = $scope.groupByLevels.length - 1; i >= 0; i--) {
                        var groupItems = $scope.groupByLevels[i].groupByItems.split(",");
                        var isExists = true;
                        angular.forEach(groupItems, function (groupItem) {
                            if (isExists) {
                                var isGroupItemExist = false;
                                angular.forEach($scope.columns, function (col) {
                                    if (groupItem === col.dbBaseName + "." + col.colName) {
                                        isGroupItemExist = true;
                                    }
                                });
                                if (!isGroupItemExist) {
                                    isExists = false;
                                }
                            }
                        });
                        if (!isExists) {
                            $scope.groupByLevels.splice(i, 1);
                        }
                    }
                }
                if (isGroupByCheck) {
                    if ($scope.selectGroupByFields === undefined || $scope.selectGroups === undefined) {
                        $scope.initializeGroupConfiguration();
                    } else {
                        var data = [];
                        if ($scope.groupInfo.selectedGroup !== undefined || $scope.groupInfo.selectedGroup.length > 0) {
                            var fields;
                            if (!angular.isArray($scope.groupInfo.selectedGroup)) {
                                fields = $scope.groupInfo.selectedGroup.split(",");
                                angular.forEach(fields, function (item) {
                                    angular.forEach($scope.columns, function (field) {
                                        if (item === field.dbBaseName + "." + field.colName) {
                                            data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                        }
                                    });
                                });
                            } else {
                                fields = angular.copy($scope.groupInfo.selectedGroup);
                                angular.forEach(fields, function (item) {
                                    angular.forEach($scope.columns, function (field) {
                                        if (item.id === field.dbBaseName + "." + field.colName) {
                                            data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                        }
                                    });
                                });
                            }
                            $("#groupFields").select2("data", data);
                        }
                        data = [];
                        if ($scope.groupInfo.groupByColumns !== undefined || $scope.groupInfo.groupByColumns.length > 0) {
                            var fields;
                            if (!angular.isArray($scope.groupInfo.groupByColumns)) {
                                fields = $scope.groupInfo.groupByColumns.split(",");
                                angular.forEach(fields, function (item) {
                                    angular.forEach($scope.columns, function (field) {
                                        if (item === field.dbBaseName + "." + field.colName) {
                                            data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                        }
                                    });
                                });
                            } else {
                                fields = angular.copy($scope.groupInfo.groupByColumns);
                                angular.forEach(fields, function (item) {
                                    angular.forEach($scope.columns, function (field) {
                                        if (item.id === field.dbBaseName + "." + field.colName) {
                                            data.push({id: field.dbBaseName + "." + field.colName, text: field.fieldLabel});
                                        }
                                    });
                                });
                            }
                            $("#groupByFields").select2("data", data);
                        }
                    }
                }
                $scope.updateGroupColumns(true);
                $scope.updateGroups();
            };
            $scope.updateGroupConfigurations = function () {
                if ($scope.isEditMode && $scope.isEditModeGroupInitialized === false) {
                    $scope.isEditModeGroupInitialized = true;
//                Retrieve group information in edit mode.
                    if ($scope.report.groupAttributes !== undefined && $scope.report.groupAttributes !== null) {
                        var groupAttr = JSON.parse($scope.report.groupAttributes);
                        if (groupAttr.groups !== undefined) {
                            var groups = groupAttr.groups;
                            $scope.groups = [];
                            angular.forEach(groups, function (grp) {
                                var obj = {groupName: grp.groupName, sequence: grp.sequence};
                                var grpItems = grp.groupItems.split(",");
                                var groupItems = null;
                                var groupItemLabels = null;
                                var isMemberMissing = false;
                                angular.forEach(grpItems, function (grpItem) {
                                    var grpItemDef = null;
                                    var grpItemLabel = null;
                                    angular.forEach($scope.columns, function (item) {
                                        var label;
                                        if (item.alias === undefined || item.alias === null || item.alias === '') {
                                            label = item.fieldLabel;
                                        } else {
                                            label = item.alias;
                                        }
                                        if (label === grpItem) {
                                            grpItemDef = item.dbBaseName + "." + item.colName;
                                            grpItemLabel = item.fieldLabel;
                                        }
                                    });
                                    if (grpItemDef !== null && grpItemLabel !== null) {
                                        if (groupItems === null) {
                                            groupItems = grpItemDef;
                                        } else {
                                            groupItems += "," + grpItemDef;
                                        }
                                        if (groupItemLabels === null) {
                                            groupItemLabels = grpItemLabel;
                                        } else {
                                            groupItemLabels += ", " + grpItemLabel;
                                        }
                                    } else {
                                        isMemberMissing = true;
                                    }
                                });
                                if ((groupItems !== null && groupItemLabels !== null) && isMemberMissing !== true) {
                                    obj.groupItems = groupItems;
                                    obj.groupItemLabels = groupItemLabels;
                                    $scope.groups.push(obj);
                                }
                            });
                            //Change group name of first(default) group to 'Default Group'
                            if ($scope.groups.length === 1) {
                                $scope.groups[0].groupName = 'Default Group';
                            }
                        }
                        if (groupAttr.groupBy !== undefined) {
                            var groups = groupAttr.groupBy;
                            $scope.groupByLevels = [];
                            angular.forEach(groups, function (grp) {
                                var obj = {sequence: grp.level};
                                var groupByItems = grp.fields.split(",");
                                var groupItems = null;
                                var groupItemLabels = null;
                                var isMemberMissing = false;
                                angular.forEach(groupByItems, function (grpItem) {
                                    var grpItemDef = null;
                                    var grpItemLabel = null;
                                    angular.forEach($scope.columns, function (item) {
                                        var label;
                                        if (item.alias === undefined || item.alias === null || item.alias === '') {
                                            label = item.fieldLabel;
                                        } else {
                                            label = item.alias;
                                        }
                                        if (label === grpItem) {
                                            grpItemDef = item.dbBaseName + "." + item.colName;
                                            grpItemLabel = item.fieldLabel;
                                        }
                                    });
                                    if (grpItemDef !== null && grpItemLabel !== null) {
                                        if (groupItems === null) {
                                            groupItems = grpItemDef;
                                        } else {
                                            groupItems += "," + grpItemDef;
                                        }
                                        if (groupItemLabels === null) {
                                            groupItemLabels = grpItemLabel;
                                        } else {
                                            groupItemLabels += ", " + grpItemLabel;
                                        }
                                    } else {
                                        isMemberMissing = true;
                                    }
                                });
                                if ((groupItems !== null && groupItemLabels !== null) && isMemberMissing !== true) {
                                    obj.groupByItems = groupItems;
                                    obj.groupByItemLabels = groupItemLabels;
                                    $scope.groupByLevels.push(obj);
                                }
                            });
                        }
                        //Group config will update groups as well as filters.
                        $scope.updateGroupConfig(true);
                    } else {
                        if ($scope.isGroupByCheck) {
                            $scope.updateGroupConfig(true);
                        } else {
                            $scope.updateExistingFilters();
                        }
                    }

                } else {
                    if ($scope.isGroupByCheck) {
                        //If not in edit mode, just update existing groups and filters.
                        $scope.updateGroupConfig(true);
                    }
                }
            };
            $scope.updateGroups = function (isUpdateColumns) {
                if ($scope.selectGroups !== undefined && $scope.selectGroupByFields !== undefined) {
                    $scope.remainingFields = angular.copy($scope.selectedFields.split(","));
                    var grpByColValues = [];
                    if ($scope.groupInfo.groupByColumns !== undefined && $scope.groupInfo.groupByColumns.length > 0 && $scope.groupInfo.groupByColumns.length > 0 && $scope.groupInfo.groupByColumns instanceof Object === false && !angular.isArray($scope.groupInfo.groupByColumns)) {
                        grpByColValues = $scope.groupInfo.groupByColumns.split(",");
                    }
                    if ($scope.groupInfo.selectedGroup !== undefined && $scope.groupInfo.selectedGroup.length > 0 && $scope.groupInfo.selectedGroup.length > 0 && $scope.groupInfo.selectedGroup instanceof Object === false && !angular.isArray($scope.groupInfo.selectedGroup)) {
                        var grpItems = $scope.groupInfo.selectedGroup.split(",");
                        if (grpItems.length > 0) {
                            angular.forEach(grpItems, function (item) {
                                grpByColValues.push(item);
                            });
                        }
                    }
                    if ($scope.groups.length > 0) {
                        angular.forEach($scope.groups, function (grp) {
                            var grpItems = grp.groupItems.split(",");
                            if (grpItems.length > 0) {
                                angular.forEach(grpItems, function (item) {
                                    grpByColValues.push(item);
                                });
                            }
                        });
                    }
                    if ($scope.groupByLevels.length > 0) {
                        angular.forEach($scope.groupByLevels, function (grp) {
                            var grpItems = grp.groupByItems.split(",");
                            if (grpItems.length > 0) {
                                angular.forEach(grpItems, function (item) {
                                    grpByColValues.push(item);
                                });
                            }
                        });
                    }
                    angular.forEach(grpByColValues, function (item) {
                        angular.forEach($scope.remainingFields, function (field, index) {
                            if (item === field) {
                                $scope.remainingFields.splice(index, 1);
                            }
                        });
                    });
                    var remainingFields = '';
                    angular.forEach($scope.remainingFields, function (item) {
                        if (remainingFields.length === 0) {
                            remainingFields = item;
                        } else {
                            remainingFields += "," + item;
                        }
                    });
                    var newGroupByList = $scope.initializeGroupByFieldsList(remainingFields);
                    $scope.selectGroups.data.splice(0, $scope.selectGroups.data.length);
                    angular.forEach(newGroupByList, function (item) {
                        $scope.selectGroups.data.push(item);
                    });
                    $scope.selectGroupByFields.data.splice(0, $scope.selectGroupByFields.data.length);
                    angular.forEach(newGroupByList, function (item) {
                        $scope.selectGroupByFields.data.push(item);
                    });
                }
                if (isUpdateColumns !== undefined && isUpdateColumns === true) {
                    $scope.updateGroupColumns();
                }
            };
            $scope.$watch("groupInfo.groupByColumns", function (newValue) {
                if ($scope.groupInfo.groupByColumns !== undefined && $scope.groupInfo.groupByColumns instanceof Object === true && angular.isArray($scope.groupInfo.groupByColumns)) {
                    var feature = '';
                    angular.forEach($scope.groupInfo.groupByColumns, function (item, index) {
                        if (index === 0) {
                            feature = item.id;
                        } else {
                            feature += ',' + item.id;
                        }
                    });
                    $scope.groupInfo.groupByColumns = feature;
                }
                else {
                    $scope.updateGroups(false);
                }

            });
            $scope.$watch("groupInfo.selectedGroup", function (newValue) {
                if ($scope.groupInfo.selectedGroup !== undefined && $scope.groupInfo.selectedGroup instanceof Object === true && angular.isArray($scope.groupInfo.selectedGroup)) {
                    var feature = '';
                    angular.forEach($scope.groupInfo.selectedGroup, function (item, index) {
                        if (index === 0) {
                            feature = item.id;
                        } else {
                            feature += ',' + item.id;
                        }
                    });
                    $scope.groupInfo.selectedGroup = feature;
                }
                else {
                    $scope.updateGroups(false);
                }

            });
            $scope.addGroup = function (groupForm) {
                $scope.groupInfo.isGroupAdded = true;
                $scope.groupInfo.isGroupFieldsEmpty = false;
                $scope.groupInfo.isGroupNameEmpty = false;
                if (groupForm.$valid) {
                    if ($scope.groupInfo.selectedGroup === undefined || $scope.groupInfo.selectedGroup === '' || $scope.groupInfo.selectedGroup.length === 0) {
                        $scope.groupInfo.isGroupFieldsEmpty = true;
                    }
                    if ($scope.groupInfo.groupName === undefined || $scope.groupInfo.groupName === '') {
                        $scope.groupInfo.isGroupNameEmpty = true;
                    }
                    if ($scope.groups.length === 0) {
                        $scope.groupInfo.isGroupNameEmpty = false;
                    }
                    if ($scope.groupInfo.isGroupFieldsEmpty === false && $scope.groupInfo.isGroupNameEmpty === false && $scope.groupInfo.isGroupNameDuplicate === false) {
                        if ($scope.currentGroupIndex === 1 && $scope.remainingFields.length === 0) {
                            //Save Fields for the first time.
                            $scope.remainingFields = angular.copy($scope.selectedFields.split(","));
                            $scope.currentGroupIndex++;
                        }
                        var groupItemLabels = '';
                        var groupItems;
                        if ($scope.groupInfo.selectedGroup !== undefined) {
                            groupItems = $scope.groupInfo.selectedGroup.split(",");
                            angular.forEach(groupItems, function (item) {
                                angular.forEach($scope.AllFieldDetailList, function (column) {
                                    if (column.id === item) {
                                        if (groupItemLabels.length === 0) {
                                            groupItemLabels = column.text;
                                        } else {
                                            groupItemLabels += ", " + column.text;
                                        }
                                    }
                                });
                            });
                        }
                        //Add default group name to the first group
                        if ($scope.groups.length === 0) {
                            $scope.groupInfo.groupName = 'Default Group';
                        }
                        $scope.groups.push({sequence: $scope.groups.length + 1, groupName: $scope.groupInfo.groupName, groupItems: $scope.groupInfo.selectedGroup, groupItemLabels: groupItemLabels});
                        $scope.groupInfo.groupName = '';
                        $scope.groupInfo.selectedGroup = '';
                        $scope.groupInfo.isGroupAdded = false;
                        angular.forEach(groupItems, function (grpItem) {
                            angular.forEach($scope.remainingFields, function (item, index) {
                                if (item === grpItem) {
                                    $scope.remainingFields.splice(index, 1);
                                }
                            });
                        });
                        var remainingFields = '';
                        angular.forEach($scope.remainingFields, function (item) {
                            if (remainingFields.length === 0) {
                                remainingFields = item;
                            } else {
                                remainingFields += "," + item;
                            }
                        });
                        //Rerender the list for fiels and group by select 2.
                        var newGroupByList = $scope.initializeGroupByFieldsList(remainingFields);
                        $scope.selectGroups.data.splice(0, $scope.selectGroups.data.length);
                        angular.forEach(newGroupByList, function (item) {
                            $scope.selectGroups.data.push(item);
                        });
                        $scope.selectGroupByFields.data.splice(0, $scope.selectGroupByFields.data.length);
                        angular.forEach(newGroupByList, function (item) {
                            $scope.selectGroupByFields.data.push(item);
                        });
                        groupForm.$setPristine();
                    }
                    $scope.updateGroupColumns();
                }
            };
            $scope.removeGroup = function (groupIndex) {
                var currentGroup = $scope.groups[groupIndex];
                $scope.groups.splice(groupIndex, 1);
                var groupItems = currentGroup.groupItems.split(",");
                angular.forEach(groupItems, function (item) {
                    $scope.remainingFields.push(item);
                });
                var remainingFields = '';
                angular.forEach($scope.remainingFields, function (item) {
                    if (remainingFields.length === 0) {
                        remainingFields = item;
                    } else {
                        remainingFields += "," + item;
                    }
                });
                //Rerender the list for fiels and group by select 2.
                var newGroupByList = $scope.initializeGroupByFieldsList(remainingFields);
                $scope.selectGroups.data.splice(0, $scope.selectGroups.data.length);
                angular.forEach(newGroupByList, function (item) {
                    $scope.selectGroups.data.push(item);
                });
                $scope.selectGroupByFields.data.splice(0, $scope.selectGroupByFields.data.length);
                angular.forEach(newGroupByList, function (item) {
                    $scope.selectGroupByFields.data.push(item);
                });
                if ($scope.groups.length > 0) {
                    angular.forEach($scope.groups, function (item, index) {
                        item.sequence = index + 1;
                    });
                }
                $scope.updateGroupColumns();
            };

            $scope.addGroupByLevel = function () {
                if ($scope.groupInfo.groupByColumns !== undefined && $scope.groupInfo.groupByColumns.length > 0) {
                    var groupItemLabels = '';
                    var groupItems;
                    groupItems = $scope.groupInfo.groupByColumns.split(",");
                    angular.forEach(groupItems, function (item) {
                        angular.forEach($scope.AllFieldDetailList, function (column) {
                            if (column.id === item) {
                                if (groupItemLabels.length === 0) {
                                    groupItemLabels = column.text;
                                } else {
                                    groupItemLabels += ", " + column.text;
                                }
                            }
                        });
                    });
                    $scope.groupByLevels.push({sequence: $scope.groupByLevels.length + 1, groupByItems: $scope.groupInfo.groupByColumns, groupByItemLabels: groupItemLabels});
                    $scope.groupInfo.groupByColumns = '';
                    $scope.updateGroups(true);
                }
            };

            $scope.removeGroupByLevel = function (index) {
                $scope.groupByLevels.splice(index, 1);
                $scope.updateGroups(true);
            };

            //Update columns according to group configuration.(For sequencing purpose)
            $scope.updateGroupColumns = function (isRootCheck) {
//                console.log('Columns-----------------------' + JSON.stringify($scope.columns));
                if (isRootCheck !== undefined && isRootCheck === true) {
                    if (!$scope.isGroupByCheck) {
                        var allColumns = [];
                        //Copy all columns to main info to update the changes.
                        //Mostly happen, if we remove a group or group by column with some existing information.
                        angular.forEach($scope.groupByColumns, function (group) {
                            angular.forEach(group.columns, function (item) {
                                allColumns.push(item);
                            });
                        });
                        angular.forEach($scope.groupsColumns, function (group) {
                            angular.forEach(group.groupColumns, function (item) {
                                allColumns.push(item);
                            });
                        });
                        angular.forEach($scope.otherColumns, function (item) {
                            allColumns.push(item);
                        });
                        if (allColumns.length > 0) {
                            $scope.existingColumns = angular.copy(allColumns);
//                        var orderMap = $scope.columns.orderMap;
                            $scope.columns = angular.copy(allColumns);
//                        $scope.columns.orderMap = orderMap;
                        }
                    }
                } else {
                    $scope.persistGroupChanges();
                }
//                if ($scope.groupInfo.groupByColumns !== undefined && $scope.groupInfo.groupByColumns !== '' && $scope.groupInfo.groupByColumns.length > 0 && $scope.groupInfo.groupByColumns instanceof Object === false && !angular.isArray($scope.groupInfo.groupByColumns)) {
//                    //Add group by columns to the list.
//                    console.log("columns:" + $scope.groupInfo.groupByColumns);
//                    var grpByColValues = $scope.groupInfo.groupByColumns.split(",");
//                    angular.forEach(grpByColValues, function(grpBy) {
//                        var isExist = false;
//                        var origColumn;
//                        angular.forEach($scope.columns, function(col) {
//                            if (grpBy === col.dbBaseName + "." + col.colName) {
//                                origColumn = col;
//                            }
//                        });
//                        if ($scope.groupByColumns !== undefined) {
//                            if (angular.isArray($scope.groupByColumns) && $scope.groupByColumns.length > 0) {
//                                angular.forEach($scope.groupByColumns, function(item, grpByIndex) {
//                                    if (grpBy === item.dbBaseName + "." + item.colName) {
//                                        isExist = true;
//                                        $scope.groupByColumns[grpByIndex] = angular.copy(origColumn);
//                                    }
//                                });
//                            }
//                            if (!isExist) {
//                                $scope.groupByColumns.push(origColumn);
//                            }
//                        } else {
//                            $scope.groupByColumns = [];
//                            $scope.groupByColumns.push(origColumn);
//                        }
//                    });
//                    for (var i = $scope.groupByColumns.length - 1; i >= 0; i--) {
//                        var isExist = false;
//                        var colDef = $scope.groupByColumns[i].dbBaseName + "." + $scope.groupByColumns[i].colName;
//                        angular.forEach(grpByColValues, function(grpBy) {
//                            if (colDef === grpBy) {
//                                isExist = true;
//                            }
//                        });
//                        if (!isExist) {
//                            $scope.groupByColumns.splice(i, 1);
//                        }
//                    }
//                } else {
//                    $scope.groupByColumns = [];
//                }
                if ($scope.groups.length > 0) {
                    if ($scope.groupsColumns.length === 0) {
                        angular.forEach($scope.groups, function (group, index) {
                            var obj = {};
                            obj.groupName = group.groupName;
                            if (index === 0) {
                                obj.groupName = 'Default Group';
                            }
                            var groupItems = group.groupItems.split(",");
                            var groupItemColumns = [];
                            angular.forEach(groupItems, function (item) {
                                angular.forEach($scope.columns, function (col) {
                                    if (item === col.dbBaseName + "." + col.colName) {
                                        groupItemColumns.push(col);
                                    }
                                })
                            });
                            obj.groupColumns = groupItemColumns;
                            obj.sequence = index + 1;
                            $scope.groupsColumns.push(obj);
                        });
                    } else {
                        angular.forEach($scope.groups, function (group) {
                            var isExists = false;
                            angular.forEach($scope.groupsColumns, function (grpColumn, mainGroupIndex) {
                                if (group.groupName === grpColumn.groupName) {
                                    isExists = true;
                                    angular.forEach(grpColumn.groupColumns, function (grpCol, grpColIndex) {
                                        angular.forEach($scope.columns, function (col) {
                                            if (grpCol.dbBaseName + "." + grpCol.colName === col.dbBaseName + "." + col.colName) {
                                                $scope.groupsColumns[mainGroupIndex].groupColumns[grpColIndex] = angular.copy(col);
                                            }
                                        });
                                    });
                                }
                            });
                            if (!isExists) {
                                var origColumns = [];
                                angular.forEach(group.groupItems.split(","), function (item) {
                                    angular.forEach($scope.columns, function (col) {
                                        if (item === col.dbBaseName + "." + col.colName) {
                                            origColumns.push(col);
                                        }
                                    });
                                });
                                $scope.groupsColumns.push({groupName: group.groupName, groupColumns: origColumns, sequence: $scope.groupsColumns.length + 1});
                            }
                        });
                        for (var i = $scope.groupsColumns.length - 1; i >= 0; i--) {
                            var isExist = false;
                            angular.forEach($scope.groups, function (group) {
                                if ($scope.groupsColumns[i].groupName === group.groupName) {
                                    isExist = true;
                                }
                            });
                            if (!isExist) {
                                $scope.groupsColumns.splice(i, 1);
                            }
                        }
                        if ($scope.groups.length === 1) {
                            var previousName = $scope.groups[0].groupName;
                            $scope.groups[0].groupName = "Dafult Group";
                            angular.forEach($scope.groupsColumns, function (grpColumn, mainGroupIndex) {
                                if (grpColumn.groupName === previousName) {
                                    $scope.groupsColumns[mainGroupIndex].groupName = "Default Group";
                                }
                            });
                        }
                    }
                } else {
                    $scope.groupsColumns = [];
                }
                //Group by columns with level.
                if ($scope.groupByLevels.length > 0) {
                    angular.forEach($scope.groupByLevels, function (group) {
                        var isExists = false;
                        angular.forEach($scope.groupByColumns, function (grpColumn, mainGroupIndex) {
                            if (group.groupByItems === grpColumn.groupByItems) {
                                isExists = true;
                                angular.forEach(grpColumn.columns, function (grpCol, grpColIndex) {
                                    angular.forEach($scope.columns, function (col) {
                                        if (grpCol.dbBaseName + "." + grpCol.colName === col.dbBaseName + "." + col.colName) {
                                            $scope.groupByColumns[mainGroupIndex].columns[grpColIndex] = angular.copy(col);
                                        }
                                    });
                                });
                            }
                        });
                        if (!isExists) {
                            var origColumns = [];
                            angular.forEach(group.groupByItems.split(","), function (item) {
                                angular.forEach($scope.columns, function (col) {
                                    if (item === col.dbBaseName + "." + col.colName) {
                                        origColumns.push(col);
                                    }
                                });
                            });
                            $scope.groupByColumns.push({columns: origColumns, sequence: $scope.groupByColumns.length + 1, groupByItems: group.groupByItems});
                        }
                    });
                    for (var i = $scope.groupByColumns.length - 1; i >= 0; i--) {
                        var isExist = false;
                        angular.forEach($scope.groupByLevels, function (group) {
                            if ($scope.groupByColumns[i].groupByItems === group.groupByItems) {
                                isExist = true;
                            }
                        });
                        if (!isExist) {
                            $scope.groupByColumns.splice(i, 1);
                        }
                    }
                } else {
                    $scope.groupByColumns = [];
                }
                var remainingFields = angular.copy($scope.selectedFields.split(","));
                if (remainingFields.length > 0) {
                    var grpByColValues = [];
                    if ($scope.groups.length > 0) {
                        angular.forEach($scope.groups, function (grp) {
                            var grpItems = grp.groupItems.split(",");
                            if (grpItems.length > 0) {
                                angular.forEach(grpItems, function (item) {
                                    grpByColValues.push(item);
                                });
                            }
                        });
                    }
                    if ($scope.groupByLevels.length > 0) {
                        angular.forEach($scope.groupByLevels, function (grp) {
                            var grpItems = grp.groupByItems.split(",");
                            if (grpItems.length > 0) {
                                angular.forEach(grpItems, function (item) {
                                    grpByColValues.push(item);
                                });
                            }
                        });
                    }
                    angular.forEach(grpByColValues, function (item) {
                        angular.forEach(remainingFields, function (field, index) {
                            if (item === field) {
                                remainingFields.splice(index, 1);
                            }
                        });
                    });
                    angular.forEach(remainingFields, function (field) {
                        var isExists = false;
                        angular.forEach($scope.otherColumns, function (othColumn, othIndex) {
                            if (field === othColumn.dbBaseName + "." + othColumn.colName) {
                                isExists = true;
                                angular.forEach($scope.columns, function (col) {
                                    if (field === col.dbBaseName + "." + col.colName) {
                                        $scope.otherColumns[othIndex] = angular.copy(col);
                                    }
                                });
                            }
                        });
                        if (!isExists) {
                            angular.forEach($scope.columns, function (col) {
                                if (field === col.dbBaseName + "." + col.colName) {
                                    $scope.otherColumns.push(col);
                                }
                            });
                        }
                    });
                    for (var i = $scope.otherColumns.length - 1; i >= 0; i--) {
                        var isExist = false;
                        angular.forEach(remainingFields, function (field) {
                            if (field === $scope.otherColumns[i].dbBaseName + "." + $scope.otherColumns[i].colName) {
                                isExist = true;
                            }
                        });
                        if (!isExist) {
                            $scope.otherColumns.splice(i, 1);
                        }
                    }
                } else {
                    $scope.otherColumns = [];
                }
                //Sort The Group columns
                angular.forEach($scope.otherColumns, function (col, index) {
                    col.fieldSequence = index + 1;
                });
                $scope.otherColumns.sort(SortByFieldSequence);
                if ($scope.groups.length > 0) {
                    angular.forEach($scope.groups, function (group, index) {
                        angular.forEach($scope.groupsColumns, function (column) {
                            if (group.groupName === column.groupName) {
                                column.sequence = index + 1;
                            }
                        });
                    });
                    $scope.groupsColumns.sort(SortBySequence);
                }
                if ($scope.groupByLevels.length > 0) {
                    angular.forEach($scope.groupByLevels, function (group, index) {
                        angular.forEach($scope.groupByColumns, function (column) {
                            if (group.groupByItems === column.groupByItems) {
                                column.sequence = index + 1;
                            }
                        });
                    });
                    $scope.groupByColumns.sort(SortBySequence);
                }
//                console.log('$scope.groupByColumns' + JSON.stringify($scope.groupByColumns));
//                console.log('$scope.groupsColumns' + JSON.stringify($scope.groupsColumns));
//                console.log('$scope.otherColumns' + JSON.stringify($scope.otherColumns));
                var allColumns = [];
                //Copy all columns to main info to update the changes.
                //Mostly happen, if we remove a group or group by column with some existing information.
                angular.forEach($scope.groupByColumns, function (group) {
                    angular.forEach(group.columns, function (item) {
                        allColumns.push(item);
                    });
                });
                angular.forEach($scope.groupsColumns, function (group) {
                    angular.forEach(group.groupColumns, function (item) {
                        allColumns.push(item);
                    });
                });
                angular.forEach($scope.otherColumns, function (item) {
                    allColumns.push(item);
                });
                if (allColumns.length > 0) {
//                    console.log('others-----------------' + JSON.stringify($scope.otherColumns));
                    $scope.existingColumns = angular.copy(allColumns);
//                    var orderMap = $scope.columns.orderMap;
                    $scope.columns = angular.copy(allColumns);
//                    $scope.columns.orderMap = orderMap;
//                    console.log('Columns below---------------' + JSON.stringify($scope.columns));
                }
                $scope.updateExistingFilters();
            };
            //Method to check duplicate group names
            $scope.checkGroupNameExists = function (value, element, isPrimary, index) {
                if (value !== undefined) {
                    if (isPrimary === false) {
                        angular.forEach($scope.groups, function (group) {
                            if (group.groupName !== null && group.groupName !== undefined && group.groupName !== '') {
                                if (value === group.groupName) {
                                    $scope.groupInfo.isGroupNameDuplicate = true;
                                }
                            }
                        });
                    } else {
                        var isUnique = true;
                        angular.forEach($scope.groups, function (group, grpIndex) {
                            if (group.groupName !== null && group.groupName !== undefined && group.groupName !== '' && grpIndex !== 0) {
                                if (value === group.groupName) {
                                    isUnique = false;
                                    element.$setValidity("exists", false);
                                }
                            }
                        });
                        if (isUnique) {
                            var allColumns = [];
                            //Copy all columns to main info to update the changes.
                            //Mostly happen, if we remove a group or group by column with some existing information.
                            angular.forEach($scope.groupByColumns, function (group) {
                                angular.forEach(group.columns, function (item) {
                                    allColumns.push(item);
                                });
                            });
                            angular.forEach($scope.groupsColumns, function (group) {
                                angular.forEach(group.groupColumns, function (item) {
                                    allColumns.push(item);
                                });
                            });
                            angular.forEach($scope.otherColumns, function (item) {
                                allColumns.push(item);
                            });
                            if (allColumns.length > 0) {
                                $scope.existingColumns = angular.copy(allColumns);
//                                var orderMap = $scope.columns.orderMap;
                                $scope.columns = angular.copy(allColumns);
//                                $scope.columns.orderMap = orderMap;
                            }
                            $scope.updateGroupColumns();
                        }
                    }
                }
            };
            //Persist changes of group if goes backward.
            $scope.persistGroupChanges = function () {
                if ($scope.isGroupByCheck) {
                    var allColumns = [];
                    //Copy all columns to main info to update the changes.
                    //Mostly happen, if we remove a group or group by column with some existing information.
                    angular.forEach($scope.groupByColumns, function (group) {
                        angular.forEach(group.columns, function (item) {
                            allColumns.push(item);
                        });
                    });
                    angular.forEach($scope.groupsColumns, function (group) {
                        angular.forEach(group.groupColumns, function (item) {
                            allColumns.push(item);
                        });
                    });
                    angular.forEach($scope.otherColumns, function (item) {
                        allColumns.push(item);
                    });
                    if (allColumns.length > 0) {
                        $scope.existingColumns = angular.copy(allColumns);
                        $scope.columns = angular.copy(allColumns);
                    }
                }
            };

            $scope.addGroupFilter = function (column, index) {

                var parentIndex;
                var isGotParent = false;
                if (column !== undefined) {
                    angular.forEach($scope.groupsColumns, function (group, grpIndex) {
                        if (!isGotParent) {
                            angular.forEach(group.groupColumns, function (grpItem) {
                                if (grpItem.dbBaseName === column.dbBaseName && grpItem.colName === column.colName) {
                                    parentIndex = grpIndex;
                                    isGotParent = true;
                                }
                            });
                        }
                    });
                }
//                console.log('index-------------' + index + parentIndex);
//                console.log('GroupbyColumn-----------' + JSON.stringify($scope.groupsColumns[parentIndex].groupColumns[index]));


                $scope.isGlobalFilterAdded = true;
                if ($scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType !== 'between' && !($scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType === 'is null' || $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType === 'is not null')) {
                    if ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst !== undefined && ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst.length > 0 || $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst !== null)) {
                        $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstInvalid = false;
                        $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterTypesList.push({filter: $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType,
                            filterLabel: $scope.filterValueNameMap[$scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType], filterValFirst: $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst});
                        $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst = undefined;
                        angular.forEach($scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes, function (filterType, filterIndex) {
                            if (filterType.id === $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType) {
                                $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType = undefined;
                                $scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes.splice(filterIndex, 1);
                            }
                        });
                    } else {
                        $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstInvalid = true;
                    }
                } else if (($scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType === 'is null' || $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType === 'is not null')) {
                    $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterTypesList.push({filter: $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType,
                        filterLabel: $scope.filterValueNameMap[$scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType]});
                    angular.forEach($scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes, function (filterType, filterIndex) {
                        if (filterType.id === $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType) {
                            $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType = undefined;
                            $scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes.splice(filterIndex, 1);
                        }
                    });
                } else {
                    if ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst !== undefined && $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond !== undefined &&
                            ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst.length > 0 || $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst !== null) &&
                            ($scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond.length > 0 || $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond !== null)) {
                        switch ($scope.groupsColumns[parentIndex].groupColumns[index].dataType) {
                            case "int8":
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst = parseInt($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst);
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond = parseInt($scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond);
                                break;
                            case "double precision":
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst = parseFloat($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst);
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond = parseFloat($scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond);
                                break;
                            case "timestamp":
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst = new Date($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst);
                                $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond = new Date($scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond);
                                break;
                        }
                        if ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst <= $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond) {
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstInvalid = false;
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecondInvalid = false;
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstMinInvalid = false;
                            $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterTypesList.push({filter: $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType,
                                filterLabel: $scope.filterValueNameMap[$scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType], filterValFirst: $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst,
                                filterValSecond: $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond});
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst = undefined;
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond = undefined;
                            angular.forEach($scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes, function (filterType, filterIndex) {
                                if (filterType.id === $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType) {
                                    $scope.groupsColumns[parentIndex].groupColumns[index].selectedFilterType = undefined;
                                    $scope.groupsColumns[parentIndex].groupColumns[index].availableFilterTypes.splice(filterIndex, 1);
                                }
                            });
                        } else {
                            $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstMinInvalid = true;
                        }
                    }
                    if ($scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst === undefined || $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst === null || $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirst.length === 0) {
                        $scope.groupsColumns[parentIndex].groupColumns[index].filterValFirstInvalid = true;
                    }
                    if ($scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond === undefined || $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond === null || $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecond.length === 0) {
                        $scope.groupsColumns[parentIndex].groupColumns[index].filterValSecondInvalid = true;
                    }
                }
                $scope.persistGroupChanges();
            };
            $scope.removeGroupFilter = function (filterIndex, columnIndex, column) {
                var groupIndex;
                var isGotParent = false;
                if (column !== undefined) {
                    angular.forEach($scope.groupsColumns, function (group, grpIndex) {
                        if (!isGotParent) {
                            angular.forEach(group.groupColumns, function (grpItem) {
                                if (grpItem.dbBaseName === column.dbBaseName && grpItem.colName === column.colName) {
                                    groupIndex = grpIndex;
                                    isGotParent = true;
                                }
                            });
                        }
                    });
                }

//                console.log('Remove Requested---' + filterIndex + columnIndex + groupIndex);
                if ($scope.groupsColumns[groupIndex].groupColumns[columnIndex].selectedFilterTypesList.length > 0) {
                    var currentFilter = $scope.groupsColumns[groupIndex].groupColumns[columnIndex].selectedFilterTypesList[filterIndex];
                    $scope.groupsColumns[groupIndex].groupColumns[columnIndex].selectedFilterTypesList.splice(filterIndex, 1);
//                    console.log('Bingo !! Filter deleted-' + filterIndex);
                    $scope.groupsColumns[groupIndex].groupColumns[columnIndex].availableFilterTypes.push({id: currentFilter.filter, text: $scope.filterValueNameMap[currentFilter.filter]});
                }
                $scope.persistGroupChanges();
            };
            $scope.addGroupByLevelFilter = function (column, index) {

                var parentIndex;
                var isGotParent = false;
                if (column !== undefined) {
                    angular.forEach($scope.groupByColumns, function (group, grpIndex) {
                        if (!isGotParent) {
                            angular.forEach(group.columns, function (grpItem) {
                                if (grpItem.dbBaseName === column.dbBaseName && grpItem.colName === column.colName) {
                                    parentIndex = grpIndex;
                                    isGotParent = true;
                                }
                            });
                        }
                    });
                }
//                console.log('index-------------' + index + parentIndex);
//                console.log('GroupbyColumn-----------' + JSON.stringify($scope.groupsColumns[parentIndex].groupColumns[index]));


                $scope.isGlobalFilterAdded = true;
                if ($scope.groupByColumns[parentIndex].columns[index].selectedFilterType !== 'between' && !($scope.groupByColumns[parentIndex].columns[index].selectedFilterType === 'is null' || $scope.groupByColumns[parentIndex].columns[index].selectedFilterType === 'is not null')) {
                    if ($scope.groupByColumns[parentIndex].columns[index].filterValFirst !== undefined && ($scope.groupByColumns[parentIndex].columns[index].filterValFirst.length > 0 || $scope.groupByColumns[parentIndex].columns[index].filterValFirst !== null)) {
                        $scope.groupByColumns[parentIndex].columns[index].filterValFirstInvalid = false;
                        $scope.groupByColumns[parentIndex].columns[index].selectedFilterTypesList.push({filter: $scope.groupByColumns[parentIndex].columns[index].selectedFilterType,
                            filterLabel: $scope.filterValueNameMap[$scope.groupByColumns[parentIndex].columns[index].selectedFilterType], filterValFirst: $scope.groupByColumns[parentIndex].columns[index].filterValFirst});
                        $scope.groupByColumns[parentIndex].columns[index].filterValFirst = undefined;
                        angular.forEach($scope.groupByColumns[parentIndex].columns[index].availableFilterTypes, function (filterType, filterIndex) {
                            if (filterType.id === $scope.groupByColumns[parentIndex].columns[index].selectedFilterType) {
                                $scope.groupByColumns[parentIndex].columns[index].selectedFilterType = undefined;
                                $scope.groupByColumns[parentIndex].columns[index].availableFilterTypes.splice(filterIndex, 1);
                            }
                        });
                    } else {
                        $scope.groupByColumns[parentIndex].columns[index].filterValFirstInvalid = true;
                    }
                } else if (($scope.groupByColumns[parentIndex].columns[index].selectedFilterType === 'is null' || $scope.groupByColumns[parentIndex].columns[index].selectedFilterType === 'is not null')) {
                    $scope.groupByColumns[parentIndex].columns[index].selectedFilterTypesList.push({filter: $scope.groupByColumns[parentIndex].columns[index].selectedFilterType,
                        filterLabel: $scope.filterValueNameMap[$scope.groupByColumns[parentIndex].columns[index].selectedFilterType]});
                    angular.forEach($scope.groupByColumns[parentIndex].columns[index].availableFilterTypes, function (filterType, filterIndex) {
                        if (filterType.id === $scope.groupByColumns[parentIndex].columns[index].selectedFilterType) {
                            $scope.groupByColumns[parentIndex].columns[index].selectedFilterType = undefined;
                            $scope.groupByColumns[parentIndex].columns[index].availableFilterTypes.splice(filterIndex, 1);
                        }
                    });
                } else {
                    if ($scope.groupByColumns[parentIndex].columns[index].filterValFirst !== undefined && $scope.groupByColumns[parentIndex].columns[index].filterValSecond !== undefined &&
                            ($scope.groupByColumns[parentIndex].columns[index].filterValFirst.length > 0 || $scope.groupByColumns[parentIndex].columns[index].filterValFirst !== null) &&
                            ($scope.groupByColumns[parentIndex].columns[index].filterValSecond.length > 0 || $scope.groupByColumns[parentIndex].columns[index].filterValSecond !== null)) {
                        switch ($scope.groupByColumns[parentIndex].columns[index].dataType) {
                            case "int8":
                                $scope.groupByColumns[parentIndex].columns[index].filterValFirst = parseInt($scope.groupByColumns[parentIndex].columns[index].filterValFirst);
                                $scope.groupByColumns[parentIndex].columns[index].filterValSecond = parseInt($scope.groupByColumns[parentIndex].columns[index].filterValSecond);
                                break;
                            case "double precision":
                                $scope.groupByColumns[parentIndex].columns[index].filterValFirst = parseFloat($scope.groupByColumns[parentIndex].columns[index].filterValFirst);
                                $scope.groupByColumns[parentIndex].columns[index].filterValSecond = parseFloat($scope.groupByColumns[parentIndex].columns[index].filterValSecond);
                                break;
                            case "timestamp":
                                $scope.groupByColumns[parentIndex].columns[index].filterValFirst = new Date($scope.groupByColumns[parentIndex].columns[index].filterValFirst);
                                $scope.groupByColumns[parentIndex].columns[index].filterValSecond = new Date($scope.groupByColumns[parentIndex].columns[index].filterValSecond);
                                break;
                        }
                        if ($scope.groupByColumns[parentIndex].columns[index].filterValFirst <= $scope.groupByColumns[parentIndex].columns[index].filterValSecond) {
                            $scope.groupByColumns[parentIndex].columns[index].filterValFirstInvalid = false;
                            $scope.groupByColumns[parentIndex].columns[index].filterValSecondInvalid = false;
                            $scope.groupByColumns[parentIndex].columns[index].filterValFirstMinInvalid = true;
                            $scope.groupByColumns[parentIndex].columns[index].selectedFilterTypesList.push({filter: $scope.groupByColumns[parentIndex].columns[index].selectedFilterType,
                                filterLabel: $scope.filterValueNameMap[$scope.groupByColumns[parentIndex].columns[index].selectedFilterType], filterValFirst: $scope.groupByColumns[parentIndex].columns[index].filterValFirst,
                                filterValSecond: $scope.groupByColumns[parentIndex].columns[index].filterValSecond});
                            $scope.groupByColumns[parentIndex].columns[index].filterValFirst = undefined;
                            $scope.groupByColumns[parentIndex].columns[index].filterValSecond = undefined;
                            angular.forEach($scope.groupByColumns[parentIndex].columns[index].availableFilterTypes, function (filterType, filterIndex) {
                                if (filterType.id === $scope.groupByColumns[parentIndex].columns[index].selectedFilterType) {
                                    $scope.groupByColumns[parentIndex].columns[index].selectedFilterType = undefined;
                                    $scope.groupByColumns[parentIndex].columns[index].availableFilterTypes.splice(filterIndex, 1);
                                }
                            });
                        } else {
                            $scope.groupByColumns[parentIndex].columns[index].filterValFirstMinInvalid = true;
                        }
                    }
                    if ($scope.groupByColumns[parentIndex].columns[index].filterValFirst === undefined || $scope.groupByColumns[parentIndex].columns[index].filterValFirst === null || $scope.groupByColumns[parentIndex].columns[index].filterValFirst.length === 0) {
                        $scope.groupByColumns[parentIndex].columns[index].filterValFirstInvalid = true;
                    }
                    if ($scope.groupByColumns[parentIndex].columns[index].filterValSecond === undefined || $scope.groupByColumns[parentIndex].columns[index].filterValSecond === null || $scope.groupByColumns[parentIndex].columns[index].filterValSecond.length === 0) {
                        $scope.groupByColumns[parentIndex].columns[index].filterValSecondInvalid = true;
                    }
                }
                $scope.persistGroupChanges();
            };
            $scope.removeGroupByLevelFilter = function (filterIndex, columnIndex, column) {
                var groupIndex;
                var isGotParent = false;
                if (column !== undefined) {
                    angular.forEach($scope.groupByColumns, function (group, grpIndex) {
                        if (!isGotParent) {
                            angular.forEach(group.columns, function (grpItem) {
                                if (grpItem.dbBaseName === column.dbBaseName && grpItem.colName === column.colName) {
                                    groupIndex = grpIndex;
                                    isGotParent = true;
                                }
                            });
                        }
                    });
                }

//                console.log('Remove Requested---' + filterIndex + columnIndex + groupIndex);
                if ($scope.groupByColumns[groupIndex].columns[columnIndex].selectedFilterTypesList.length > 0) {
                    var currentFilter = $scope.groupByColumns[groupIndex].columns[columnIndex].selectedFilterTypesList[filterIndex];
                    $scope.groupByColumns[groupIndex].columns[columnIndex].selectedFilterTypesList.splice(filterIndex, 1);
//                    console.log('Bingo !! Filter deleted-' + filterIndex);
                    $scope.groupByColumns[groupIndex].columns[columnIndex].availableFilterTypes.push({id: currentFilter.filter, text: $scope.filterValueNameMap[currentFilter.filter]});
                }
                $scope.persistGroupChanges();
            };
//           
            $scope.shiftGroupColUp = function (parentIndex, grpIndex) {
                if (grpIndex !== 0) {
                    var prevGroup = $scope.groupsColumns[parentIndex].groupColumns[grpIndex - 1];
                    $scope.groupsColumns[parentIndex].groupColumns[grpIndex - 1] = $scope.groupsColumns[parentIndex].groupColumns[grpIndex];
                    $scope.groupsColumns[parentIndex].groupColumns[grpIndex] = prevGroup;
                }
            };
            $scope.shiftGroupColDown = function (parentIndex, grpIndex) {
                if (grpIndex !== $scope.groupsColumns[parentIndex].groupColumns.length - 1) {
                    var nextCol = $scope.groupsColumns[parentIndex].groupColumns[grpIndex + 1];
                    $scope.groupsColumns[parentIndex].groupColumns[grpIndex + 1] = $scope.groupsColumns[parentIndex].groupColumns[grpIndex];
                    $scope.groupsColumns[parentIndex].groupColumns[grpIndex] = nextCol;
                }
            };
            $scope.shiftGroupByLevelColUp = function (parentIndex, grpIndex) {
                if (grpIndex !== 0) {
                    var prevGroup = $scope.groupByColumns[parentIndex].columns[grpIndex - 1];
                    $scope.groupByColumns[parentIndex].columns[grpIndex - 1] = $scope.groupByColumns[parentIndex].columns[grpIndex];
                    $scope.groupByColumns[parentIndex].columns[grpIndex] = prevGroup;
                }
            };
            $scope.shiftGroupByLevelColDown = function (parentIndex, grpIndex) {
                if (grpIndex !== $scope.groupByColumns[parentIndex].columns.length - 1) {
                    var nextCol = $scope.groupByColumns[parentIndex].columns[grpIndex + 1];
                    $scope.groupByColumns[parentIndex].columns[grpIndex + 1] = $scope.groupByColumns[parentIndex].columns[grpIndex];
                    $scope.groupByColumns[parentIndex].columns[grpIndex] = nextCol;
                }
            };
            $scope.shiftOtherColUp = function (grpIndex) {
                if (grpIndex !== 0) {
                    var prevGroup = $scope.otherColumns[grpIndex - 1];
                    $scope.otherColumns[grpIndex - 1] = $scope.otherColumns[grpIndex];
                    $scope.otherColumns[grpIndex] = prevGroup;
                }
            };
            $scope.shiftOtherColDown = function (grpIndex) {
                if (grpIndex !== $scope.otherColumns.length - 1) {
                    var nextCol = $scope.otherColumns[grpIndex + 1];
                    $scope.otherColumns[grpIndex + 1] = $scope.otherColumns[grpIndex];
                    $scope.otherColumns[grpIndex] = nextCol;
                }
            };
            $scope.shiftGroupUp = function (grpIndex) {
                if (grpIndex !== 0) {
                    var prevGroup = $scope.groups[grpIndex - 1];
                    $scope.groups[grpIndex - 1] = $scope.groups[grpIndex];
                    $scope.groups[grpIndex] = prevGroup;
                    $scope.updateGroupColumns();
                }
            };
            $scope.shiftGroupDown = function (grpIndex) {
                if (grpIndex !== $scope.groups.length - 1) {
                    var nextCol = $scope.groups[grpIndex + 1];
                    $scope.groups[grpIndex + 1] = $scope.groups[grpIndex];
                    $scope.groups[grpIndex] = nextCol;
                    $scope.updateGroupColumns();
                }
            };
            $scope.shiftGroupByLevelUp = function (grpIndex) {
                if (grpIndex !== 0) {
                    var prevGroup = $scope.groupByLevels[grpIndex - 1];
                    $scope.groupByLevels[grpIndex - 1] = $scope.groupByLevels[grpIndex];
                    $scope.groupByLevels[grpIndex] = prevGroup;
                    $scope.updateGroupColumns();
                }
            };
            $scope.shiftGroupByLevelDown = function (grpIndex) {
                if (grpIndex !== $scope.groupByLevels.length - 1) {
                    var nextCol = $scope.groupByLevels[grpIndex + 1];
                    $scope.groupByLevels[grpIndex + 1] = $scope.groupByLevels[grpIndex];
                    $scope.groupByLevels[grpIndex] = nextCol;
                    $scope.updateGroupColumns();
                }
            };

            /**
             * Methods to modify preview According to grouping.
             */

            $scope.groupRecordsForPreview = function (previewData) {
                $scope.modifiedColumns = angular.copy($scope.modifiedCurrencyColumns);
                $scope.modifiedColumns.sort(SortByFieldSequence);

                if ($scope.isGroupByCheck && previewData !== undefined) {
                    var tempPreviewData = [];
                    var dataIndex = 0;
                    angular.forEach(previewData, function (value, key) {
                        tempPreviewData.push(value);
                    });
                    $scope.previewData = angular.copy(tempPreviewData);
                    var tempColumnList = [];
                    var tempRowColumnList = [];
                    $scope.groupItemListForPreviw = [];
//                    var groupByFields = $scope.groupInfo.groupAttr.groupBy.split(",");
//                    angular.forEach(groupByFields, function(item) {
//                        angular.forEach($scope.modifiedColumns, function(column, index) {
//                            var fieldName;
//                            if (column.alias === undefined || column.alias === '') {
//                                fieldName = column.fieldLabel;
//                            } else {
//                                fieldName = column.alias;
//                            }
//                            if (fieldName === item) {
//                                column.isGroupBy = true;
//                                tempColumnList.push(column);
//                                tempRowColumnList.push(column);
//                                $scope.modifiedColumns.splice(index, 1);
//                            }
//                        });
//                    });
                    //Check for group by levels
                    if ($scope.groupInfo.groupAttr.groupBy !== undefined && $scope.groupInfo.groupAttr.groupBy.length > 0) {
                        //Sort groupby accroding to sequence.
                        $scope.groupInfo.groupAttr.groupBy.sort(SortBySequence);
                        var totalLevel = 0;
                        $scope.levelMap = [];
                        angular.forEach($scope.groupInfo.groupAttr.groupBy, function (groupByInfo) {
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

                        $scope.UIPreviewData = angular.copy($scope.previewData);
                    }
                    //Check for groups.
                    //                    console.log('Groups-------------' + JSON.stringify($scope.groupInfo.groupAttr));
                    if ($scope.groupInfo.groupAttr.groups !== undefined && $scope.groupInfo.groupAttr.groups.length > 0) {
                        //Sort groups accroding to sequence.
                        $scope.groupInfo.groupAttr.groups.sort(SortBySequence);
                        //                        console.log(JSON.stringify($scope.groupInfo.groupAttr.groups));
                        if ($scope.groupInfo.groupAttr.groups.length > 1) {
                            angular.forEach($scope.groupInfo.groupAttr.groups, function (groupInfo) {
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
                                        if (grpItem === label) {
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
                                tempColumnList.push(obj);
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
                            });
                        } else {
                            angular.forEach($scope.groupInfo.groupAttr.groups, function (groupInfo) {
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
//                    angular.forEach(tempColumnList, function(item, index) {
//                        item.fieldSequence = index + 1;
//                    });
//                    angular.forEach(tempRowColumnList, function(item, index) {
//                        item.fieldSequence = index + 1;
//                    });
                    tempColumnList.sort(SortByFieldSequence);
                    tempRowColumnList.sort(SortByFieldSequence);
                    $scope.modifiedColumns = angular.copy(tempColumnList);
                    $scope.modifiedCurrencyColumns = angular.copy(tempRowColumnList); //                    console.log('Preview Data--------------' + JSON.stringify($scope.previewData));
//                                        console.log('Modified Columns--------------' + JSON.stringify($scope.modifiedColumns));
//                    console.log('Actual Columns--------------' + JSON.stringify($scope.columns));
                    //                    console.log('Group Items--------------' + JSON.stringify($scope.groupItemListForPreviw));

                    //Evaluate total
                    $scope.gridColumnDef = [];
                    angular.forEach($scope.modifiedCurrencyColumns, function (reportColumn) {
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
                    $scope.registerGrid = true;
                    if ($scope.isEditMode && !$scope.isEditModeColorInitialized) {
                        $scope.initializeColor();
                        $scope.updateColorConfiguration();
                    } else {
                        $scope.initializeColor();
//                        Apply coloring if required.
                        if ($scope.colorConfigData.length > 0) {
                            $scope.applyColor($scope.colorConfigForm, true);
                        }
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

            //valueFields represents others columns than group by. (For ordering purpose)
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

            /**
             * Methods for Configuring Group by ends.
             */
            //Methods for Color purpose
            $scope.initializeColor = function () {
                $scope.colorColumns = [];

//                console.log("group in color :" + $scope.isGroupByCheck);
                if (!$scope.isGroupByCheck) {
                    angular.forEach($scope.modifiedCurrencyColumns, function (column) {
                        var columnLabel;
                        if (column.alias !== null && column.alias !== '') {
                            columnLabel = column.alias;
                        } else {
                            columnLabel = column.fieldLabel;
                        }
                        var columnType = column.dataType;
                        $scope.colorColumns.push({label: columnLabel, type: columnType, componentType: column.componentType, isGroupBy: false});
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
                        angular.forEach($scope.modifiedCurrencyColumns, function (column) {
                            var columnLabel;
                            if (column.alias !== null && column.alias !== '') {
                                columnLabel = column.alias;
                            } else {
                                columnLabel = column.fieldLabel;
                            }
                            var columnType = column.dataType;
                            if (columnLabel === grpByField) {
                                $scope.colorColumns.push({label: columnLabel, type: columnType, componentType: column.componentType, isGroupBy: true});
                            }
                        });
                    });
                }
                //Add a default color combination
                if (!$scope.isColorInitialized) {
                    $scope.isColorInitialized = true;
                    $scope.colorConfigData = [];
                    var obj = {};
                    obj.combinationType = 'ANY';
                    obj.columns = [];
                    obj.colorColumns = $scope.colorColumns;
                    obj.combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                    $scope.colorConfigData.push(obj);
                } else {
                    for (var parentIndex = $scope.colorConfigData.length - 1; parentIndex >= 0; parentIndex--) {
                        $scope.colorConfigData[parentIndex].colorColumns = angular.copy($scope.colorColumns);
                        for (var index = $scope.colorConfigData[parentIndex].columns.length - 1; index >= 0; index--) {
                            var isColorColumnExists = false;
                            angular.forEach($scope.colorColumns, function (column) {

                                if (column.label === $scope.colorConfigData[parentIndex].columns[index].label) {
                                    isColorColumnExists = true;
                                }
                            });
                            if (!isColorColumnExists) {
                                $scope.removeColor(index, parentIndex);
                            }
                        }
                        //Not required. removeColor() will do it if list is empty.
//                        if ($scope.colorConfigData[parentIndex].columns.length === 0) {
//                            $scope.removeColorCombination(parentIndex);
//                        }
                    }
                }
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
                            colorComposition.colorColumns = $scope.colorColumns;
                            colorComposition.combinationTypes = [{id: 'ALL', text: 'ALL'}, {id: 'ANY', text: 'ANY'}];
                            var columnColorAttributes = [];
                            angular.forEach(colorCombination.columns, function (colorColumn) {
                                var isColorColumnExists = false;
                                var currentColorColumn = null;
                                angular.forEach($scope.colorColumns, function (origColumn) {
                                    if (origColumn.label === colorColumn.label) {
                                        isColorColumnExists = true;
                                        currentColorColumn = origColumn;
                                    }
                                });
                                if (isColorColumnExists) {
                                    var obj = {};
                                    obj.label = colorColumn.label;
                                    obj.type = colorColumn.type;
                                    obj.componentType = colorColumn.componentType;
                                    obj.label = colorColumn.label;
                                    obj.isGroupBy = currentColorColumn.isGroupBy;
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
                                                if ($scope.isEditMode) {
                                                    var data = {};
                                                    data = {id: colorColumn.filterValue, text: colorColumn.filterValue};
                                                    callback(data);
                                                }
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
                                                if ($scope.isEditMode) {
                                                    var data = {};
                                                    data = {id: colorColumn.filterValue, text: colorColumn.filterValue};
                                                    callback(data);
                                                }
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
                                                if ($scope.isEditMode) {
                                                    var data = [];
                                                    angular.forEach(colorColumn.filterValue.split(","), function (item) {
                                                        data.push({id: item, text: item});
                                                    });
                                                    callback(data);
                                                }
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
                                                if ($scope.isEditMode) {
                                                    var data = [];
                                                    angular.forEach(colorColumn.filterValue.split(","), function (item) {
                                                        data.push({id: item, text: item});
                                                    });
                                                    callback(data);
                                                }
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
                            if (columnColorAttributes.length > 0) {
                                colorComposition.colorName = colorCombination.colorName;
                            }
                            $scope.colorConfigData.push(colorComposition);
                        });
//                       //Apply coloring if required.
                        if ($scope.colorConfigData.length > 0) {
                            $scope.applyColor($scope.filterForm, true);
                        }
                    }
                }
            };

            $scope.updateColorColumnFilter = function (colorColumn, index) {
                $scope.colorApplied = false;
                if (colorColumn !== undefined && colorColumn !== null) {
                    var filters = $scope.availabeFilterTypesByDataType[colorColumn.type] === undefined ? [{id: "=", text: "equal to"}, {id: "!=", text: "not equal to"}] : $scope.availabeFilterTypesByDataType[colorColumn.type];
                    var obj = {};
                    obj.label = colorColumn.label;
                    obj.type = colorColumn.type;
                    obj.isGroupBy = colorColumn.isGroupBy;
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
//           
            $scope.removeColor = function (index, parentIndex) {
                $scope.colorApplied = false;
                if ($scope.colorConfigData[parentIndex].columns.length > 0) {
                    $scope.colorConfigData[parentIndex].columns.splice(index, 1);
                }
                if ($scope.colorConfigData[parentIndex].columns.length === 0) {
                    $scope.removeColorCombination(parentIndex);
                }
                $scope.applyColor($scope.filterForm);
            };
            $scope.removeColorCombination = function (index) {
                $scope.colorApplied = false;
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
                $scope.colorApplied = false;
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
                            value = value.substring(0, 10);
                            var dateValues = value.split("/");
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
                    }
                    else if (colorConfig.filterValue instanceof Object) {
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

            $scope.shiftColUp = function (colIndex) {
                if (colIndex != 0) {
                    var prevCol = $scope.columns[colIndex - 1];
                    $scope.columns[colIndex - 1] = $scope.columns[colIndex];
                    $scope.columns[colIndex] = prevCol;
                    var prev = $scope.report.columns[colIndex - 1];
                    $scope.report.columns[colIndex - 1] = $scope.report.columns[colIndex];
                    $scope.report.columns[colIndex] = prev;
                    var assocPrev = $scope.associatedFieldList[colIndex - 1];
                    $scope.associatedFieldList[colIndex - 1] = $scope.associatedFieldList[colIndex];
                    $scope.associatedFieldList[colIndex] = assocPrev;
                    selectedFields = '';
                    angular.forEach($scope.associatedFieldList, function (item) {
                        if ($scope.selectedFields.length === 0) {
                            $scope.selectedFields += item.dbBaseName + '.' + item.dbFieldName;
                        } else {
                            $scope.selectedFields += "," + item.dbBaseName + '.' + item.dbFieldName;
                        }
                    });
                    //                    $scope.setReportColumnsAndGenearteQuery(true);
                }
            };
            $scope.shiftColDown = function (colIndex) {
                if (colIndex != $scope.columns.length - 1) {
                    var nextCol = $scope.columns[colIndex + 1];
                    $scope.columns[colIndex + 1] = $scope.columns[colIndex];
                    $scope.columns[colIndex] = nextCol;
                    var prev = $scope.report.columns[colIndex + 1];
                    $scope.report.columns[colIndex + 1] = $scope.report.columns[colIndex];
                    $scope.report.columns[colIndex] = prev;
                    var assocPrev = $scope.associatedFieldList[colIndex + 1];
                    $scope.associatedFieldList[colIndex + 1] = $scope.associatedFieldList[colIndex];
                    $scope.associatedFieldList[colIndex] = assocPrev;
                    $scope.selectedFields = '';
                    angular.forEach($scope.associatedFieldList, function (item) {
                        if ($scope.selectedFields.length === 0) {
                            $scope.selectedFields += item.dbBaseName + '.' + item.dbFieldName;
                        } else {
                            $scope.selectedFields += "," + item.dbBaseName + '.' + item.dbFieldName;
                        }
                    });
                    //                    $scope.setReportColumnsAndGenearteQuery(true);
                }
            };

            $scope.retrieveFeatureSectionFieldMap = function () {
                $scope.featureFieldMap = [];
                $scope.AllFieldsEntitys = [];
                ReportBuilderService.retrieveFeatureSectionField(function (res) {
                    $scope.featureFieldMap = angular.copy(res.data);
                    for (var key in res.data) {
                        $scope.tempFeatureList.push({id: key, text: key});
                        $scope.sectionFieldMap = res.data[key];
                        for (var sectionKey in $scope.sectionFieldMap) {
                            $scope.AllSectionDetailList.push({id: sectionKey, text: sectionKey});
                            angular.forEach($scope.sectionFieldMap[sectionKey], function (fieldItem) {
                                $scope.AllFieldDetailList.push({id: fieldItem.dbBaseName + "." + fieldItem.dbFieldName, text: fieldItem.fieldLabel});
                                $scope.AllFieldsEntitys.push(fieldItem);
                            });
                        }
                    }
                    $scope.entityFeatureList = [];
                    $scope.subEntityFeatureList = [];
                    angular.forEach($scope.tempFeatureList, function (tempFeature) {

                        var temp = tempFeature.id;
                        var key = temp.split(".");
                        if (key.length > 1) {
                            $scope.subEntityFeatureList.push(tempFeature);
                        } else {
                            $scope.entityFeatureList.push(tempFeature);
                        }
                    });
                    if ($scope.entityFeatureList.length > 0) {
                        $scope.featureList.push({});
                        $scope.featureList[$scope.featureList.length - 1].text = "Entity";
                        $scope.featureList[$scope.featureList.length - 1].children = $scope.entityFeatureList;
                    }
                    if ($scope.subEntityFeatureList.length > 0) {
                        $scope.featureList.push({});
                        $scope.featureList[$scope.featureList.length - 1].text = "Sub-Entity";
                        $scope.featureList[$scope.featureList.length - 1].children = $scope.subEntityFeatureList;
                    }
                    //Again update the fields list.
                    $scope.fieldList.splice(0, $scope.fieldList.length);
                    $scope.retrieveSectionFields($scope.selectedFeatures);
                    $scope.updateOrders();
                    $scope.configureRelationship();
//                    $scope.retrieveSectionFields($scope.selectedFeatures);
//                    console.log("$scope.featureList :"+JSON.stringify($scope.featureList));

                    //                    console.log(JSON.stringify($scope.featureFieldMap));
                });
            }; //            $scope.fillFieldList = function(featureName) {

            $scope.resetReport = function (reportDesignerForm) {
                reportDesignerForm.$setPristine();
                $scope.report = {};
                $scope.report.externalReport = false;
                $scope.columns = [];
                $scope.clearReportData();
                $location.path("managereport");
            };
            $scope.retrieveFeatureSectionFieldMap();
            $scope.updateCancel = function () {
                $('#removePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
            };
            $scope.updateOk = function () {
                $('#removePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.updateReportFlag = true;
                $scope.saveReport(undefined, true);
            };




//            --------------------
            $scope.address1 = false;
            $scope.address2 = false;
            $scope.reverse = false;
            $scope.city = false;
            $scope.state = false;
            $scope.orderByField = "null";
            $scope.reverseSort = null;
            $scope.showCol = false;
            $scope.showChecked = false;
//            $scope.addressFull = [{text: "Address1", value: "Address1", checked: false,
//                    grpElement: {
//                        "grp": true,
//                        "values": [[{obj: "lane", text: "road no 1"},
//                                {obj: "place", text: "sector 29"}, {obj: "landmark", text:
//                                            "opp excel co"}]]
//                    }
//                }, {value: "City", text: "City", checked: false}, {value: "State", text: "State", checked: false}, {text: "Address2", value: "Address2"
//                    , checked: false}]
//            $scope.extraFields = [{text: "Contact", colField: "contact", newField: true
//                },
//                {text: "Email", colField: "email", newField: true, type: "col"
//
//                }, {text: "DOB", colField: "dob", newField: true, type: "col"
//                },
//                {text: "Role", colField: "role", newField: true, type: "col"
//                },
//                {text: "Emp Id", colField: "empid", newField: true, type: "col"},
//                {text: "Address", colField: "address", newField: true, value:
//                            "gandhinagar Gujrat India", type: "col"},
//                {text: "Full Address", colField: "fulladdress", type: "col", newField:
//                            true, value: "gandhinagar Gujrat India",
////                   
//                }];
            $scope.extraFields = [];
//            $scope.allowedColumns = ['column'], $scope.extraFields = [{text: "Contact", colField: "contact", newField: true
//                },
//                {text: "Email", colField: "email", newField: true, type: "col"
//
//                }, {text: "DOB", colField: "dob", newField: true, type: "col"
//                },
//                {text: "Role", colField: "role", newField: true, type: "col"
//                },
//                {text: "Emp Id", colField: "empid", newField: true, type: "col"},
//                {text: "Address", colField: "address", newField: true, value:
//                            "gandhinagar Gujrat India", type: "col"},
//                {text: "Full Address", colField: "fulladdress", type: "col", newField:
//                            true, value: "gandhinagar Gujrat India",
//                    grpElement: {
//                        "grp": true,
//                        "colu1mns": [[{colField: "address", text: "Address 1"},
//                                {colField: "city", text: "City"}, {colField: "state", text:
//                                            "State"}]],
//                        "search12Data": [[{address: "sector 1", city:
//                                            "Gandhinager", state: "Gujrat"}
//                                , {address: "sector 45", city: "Gandhinager",
//                                    state: "Gujrat"},
//                                {address: "sector 6", city: "Mumbai", state:
//                                            "Maharastra"}]],
//                    }}];
            $scope.allowedColumns = ['column'];

            $scope.sort = function (table, field) {
                $scope.reverse = !$scope.reverse;
                if ($scope.resultModels !== undefined && $scope.resultModels.dropzones !== undefined && $scope.resultModels.dropzones.A[table].searchData[0] !== undefined) {
                    $scope.resultModels.dropzones.A[table].searchData[0].sort(function (a, b) {
                        if ($scope.reverse) {
                            return b[field]["value"] > a[field]["value"];
                        } else {
                            return b[field]["value"] < a[field]["value"];
                        }
                    });
                }
            };

            $scope.renameMainList = function (col) {
                if ($scope.models.dropzones.A != undefined && $scope.models.dropzones.A.length > 0) {
                    angular.forEach($scope.models.dropzones.A, function (key) {
                        angular.forEach(key.columns[0], function (data) {
                            if (data.colField === col.dbBaseName + "." + col.colName) {
                                data.text = angular.copy(col.alias);
                            }
                        });
                    });
                }
            };

            $scope.dropTrashCallback = function (event, index, item, external, type, allowedType) {
                if ($scope.models.dropzones.A !== undefined && $scope.models.dropzones.A.length > 0) {
                    $scope.selectedFields = '';
                    angular.forEach($scope.models.dropzones.A, function (key) {
                        angular.forEach(key.columns[0], function (data,index) {
                            if(data.colField === item.colField){
                                key.columns[0].splice(index,1);
                            }
                        });
                    });
                    angular.forEach($scope.models.dropzones.A, function (key) {
                        angular.forEach(key.columns[0], function (data) {
                            if ($scope.selectedFields != undefined && $scope.selectedFields.length > 0) {
                                var tempFields = angular.copy($scope.selectedFields.split(","));
                                if (tempFields.length > 0) {
                                    var fieldCount = 0;
                                    angular.forEach(tempFields, function (field) {
                                        if (field == data.colField) {
                                            fieldCount++;
                                        }
                                    });
                                    if (fieldCount == 0) {
                                        $scope.selectedFields = $scope.selectedFields + "," + data.colField;
                                    }
                                }
                                $scope.loadColumnValues1();
                                $scope.configureRelationship();
                            } else {
                                $scope.selectedFields = data.colField;
                                $scope.loadColumnValues1();
                                $scope.configureRelationship();
                            }
                        });
                    });
                }
                return item;
            }
            $scope.dropColumnCallback = function (event, index, item, external, type, allowedType) {
                var item1;
                var dg = [];
                if (item.newCol) {
                    item.newCol = false;
                    angular.forEach($scope.models.dropzones.A[0].searchData[0],
                            function (row) {
                                row[item.colField] = null;
                            });
                } else if (item.newField) {
                    angular.forEach($scope.models.dropzones.A[0].searchData[0],
                            function (row) {
                                if (item.grpElement && item.grpElement.grp) {
//                row[item.colField] = item.grpElement;
                                    angular.forEach($scope.overviewList,
                                            function (chk) {
                                                var item = {text: chk, colField: chk, newField: true, type: "col"};
                                                if ($scope.overViewMap[chk].grpElement.grp) {
                                                    item.grpElement = $scope.overViewMap[chk].grpElement.values;
                                                }
                                                dg.push(item);
                                                row[chk] = chk;
                                            });
                                }
                                else {
                                    row[item.colField] = item.value;
                                }
                            });
                }
                if (dg.length > 0) {
                    return dg;
                }
                else {
                    return item;
                }
            };
            $scope.content = "kfjhgdfj"
            $scope.dropRowCallback = function (event, index, item, external,
                    type, allowedType) {
                if (item.newRow) {
                    item.newRow = false;
                    angular.forEach($scope.models.dropzones.A[0].searchData[0][0],
                            function (value, key) {
                                item[key] = null;
                            });
                } else {

                }

                return item;
            };
            $scope.exportAction = function (pdf) {
                switch (pdf) {
                    case 'pdf':
                        $scope.$broadcast('export-pdf', {});
                        break;
                    case 'excel':
                        console.log("----excel-----")
                        $scope.$broadcast('export-excel', {});
                        break;
                    case 'doc':
                        $scope.$broadcast('export-doc', {});
                        break;
                    default:
                        console.log('no event caught');
                }
            }

            $scope.$watch('resultModels.dropzones', function (model) {
                $scope.modelAsJson = angular.toJson(model, true);
            }, true);
            $scope.toPdf = function () {

                var element = $('#exportZone');
                html2canvas(element, {
                    onrendered: function (canvas) {
                        var doc = new jsPDF('p', 'mm');
                        var dataUrl = canvas.toDataURL('image/jpeg');
                        doc.setFontSize(30);
                        doc.text(15, 15, "Report");
                        doc.addImage(dataUrl, 'PNG', 10, 30, 190, 60);
                        doc.save('Report.pdf');
                    }
                });
            };
            $scope.expandedFeatures = [];
            $scope.toggle = function (value, index) {
                var currentIndex = 'tblFeatureId' + (value + 'T') + (index + 1);
                if ($scope.expandedFeatures.indexOf(currentIndex) > -1) {
                    $('.' + currentIndex).collapse('hide');

                    $scope.expandedFeatures.splice($scope.expandedFeatures.indexOf(currentIndex), 1);
                }
                else {
                    $scope.expandedFeatures.push(currentIndex);
                    $('.' + currentIndex).collapse('show');

                }

            }
            $scope.check = function (value) {
                if (value.colField == "fulladdress" && !$scope.showChecked) {
                    $scope.showChecked = true;
                }
                else if (value.colField == "fulladdress" && $scope.showChecked) {
                    $scope.showChecked = false;
                }
            }
            $scope.overviewList = [];
            $scope.overViewMap = {};
            $scope.selectOverView = function (selectedOverviewItem) {
                if (selectedOverviewItem.checked) {
                    $scope.overviewList.push(selectedOverviewItem.value);
                    $scope.overViewMap[selectedOverviewItem.value] = selectedOverviewItem;
                } else {
                    $scope.overviewList.splice($scope.overviewList.indexOf(selectedOverviewItem.value), 1);
                    delete $scope.overViewMap[selectedOverviewItem.value];
                }
            };
        }]);
});
