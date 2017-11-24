define(['hkg', 'transferemployeeService', 'employeeService', 'franchiseService', 'leaveWorkflowService', 'designationService', 'departmentService'], function (hkg, transferemployeeService, employeeService, franchiseService, leaveWorkflowService, designationService, departmentService) {

    hkg.register.controller('TransferEmployeeController', ["$rootScope", "$scope", "$filter", "TransferEmployee", "Employee", "$location", "$anchorScroll", "FranchiseService", "LeaveWorkflow", "Designation", "DepartmentService", "DynamicFormService", "$route", "$timeout", function ($rootScope, $scope, $filter, TransferEmployee, Employee, $location, $anchorScroll, FranchiseService, LeaveWorkflow, Designation, DepartmentService, DynamicFormService, $route, $timeout) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageTransferEmployees";
            $rootScope.activateMenu();
            //Initialization
            $scope.entity = "TRANSFEREMPLOYEE.";
            $scope.departmentList = [];
//            List of fields displyed in multiselect
            $scope.shiftList = [];
            $scope.selectedDepartments = '';
            $scope.selectedShifts = '';
            $scope.transfer = {};
            $scope.submitted = false;

            $scope.$on('$viewContentLoaded', function () {
                $scope.retrieveShiftsWithDepartmentName();
                $scope.retrieveEmployeeStatus();
                $scope.retrieveUsers();
                $scope.retrieveDepartments();
            });

            $scope.retrieveShiftsWithDepartmentName = function () {
                TransferEmployee.retrieveShiftsWithDepartmentName(function (res) {
//                    console.log("res ::" + JSON.stringify(res.data));
                    $scope.deptShiftMap = res.data;
                    for (var key in res.data) {
                        if (key) {
                            var split = key.split('$@$');
                            $scope.departmentList.push({id: parseInt((split[0])), text: split[1]});
                        }
                    }
                }, function () {

                });
            };

            $scope.$watch("selectedDepartments", function (dept) {
                if ($scope.selectedDepartments !== undefined && $scope.selectedDepartments instanceof Object === true && angular.isArray($scope.selectedDepartments)) {
                    var department = '';
                    angular.forEach($scope.selectedDepartments, function (item, index) {
                        if (index === 0) {
                            department = item.id;
                        } else {
                            department += ',' + item.id;
                        }
                    });
                    $scope.selectedDepartments = department;
                }
                else {
                    $scope.retrieveShiftsFromDept($scope.selectedDepartments);
                }

            });

            $scope.retrieveShiftsFromDept = function (depts) {
//                console.log("depts :" + depts)
                if (depts !== undefined && depts instanceof Object === false && depts !== '') {
                    if ($scope.shiftList.length > 0) {
                        $scope.shiftList.splice(0, $scope.shiftList.length);
                    }
                    var deptList = depts.toString().split(",");
//                    console.log("list :" + deptList)
                    angular.forEach(deptList, function (item) {
                        for (var key in $scope.deptShiftMap) {
//                            console.log("key :" + key)
                            var split = key.split('$@$');
                            if (split[0] === item) {
                                $scope.tempshiftList = [];
                                $scope.tempshiftMap = $scope.deptShiftMap[key];
                                angular.forEach($scope.tempshiftMap, function (item) {
                                    $scope.tempshiftList.push({id: item.value, text: item.label});
                                });
                                if ($scope.tempshiftList.length !== 0) {
                                    $scope.shiftList.push({});
                                    $scope.shiftList[$scope.shiftList.length - 1].text = split[1];
                                    $scope.shiftList[$scope.shiftList.length - 1].children = $scope.tempshiftList;
                                }
                            }

                        }

                    });
                }
            };

            $scope.multiDepartments = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select Department',
                data: $scope.departmentList,
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                }
            };

            $scope.multiShifts = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select Shifts',
                data: $scope.shiftList,
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
                },
                formatResult: function (item) {
                    return item.text;
                }
            };

            $(document).on("select2-selecting", "#input_empReportsTo", function (e) {
//                console.log("e ::" + JSON.stringify(e.val));
//                console.log("e.object.text::" + e.object.text);
                $scope.obj = {id: e.val, text: e.object.text};
                $scope.flag = true;
//                var selectedShifts = $("#input_empReportsTo").select2("data");
                angular.forEach($scope.newEmployeeList, function (item) {
                    item.reportsToId = e.val;
                    var id = "#input_empReportsToTable" + item.id;
//                    console.log("ID:::" + id);
                    $(id).select2("val", []);

                });


            });

            $(document).on("select2-removing", "#selectDepartment", function (e) {
                var count = 0;
                var selectedShifts = $("#selectShift").select2("data");
                var selectedShiftsNew = [];
                $scope.selectedShifts = '';
                var valuesCheck = [];
                var selectedShiftsNews = [];
                for (var key in $scope.deptShiftMap) {
                    var split = key.split('$@$');
                    if (split[0] === e.val.toString()) {
                        angular.forEach($scope.deptShiftMap[key], function (item) {
                            angular.forEach(selectedShifts, function (col) {
                                if (col.id === parseInt((item.value))) {
                                    for (var i = 0; i < selectedShiftsNew.length; i++) {
                                        if (selectedShiftsNew[i] === col) {
                                            count++;
                                        }
                                    }
                                    if (count === 0) {
                                        selectedShiftsNew.push(col);
                                        valuesCheck.push(col.id);
                                    }

                                }
                            });
                        });
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
                angular.forEach(selectedShifts, function (item) {
                    finalValueCheck.push(item.id);
                    selectedShiftsNews.push(item);
                });
                angular.forEach(selectedShifts, function (item) {
                    angular.forEach(valuesCheck, function (valueItem) {
                        if (item.id === valueItem) {
                            for (var i = 0; i < finalValueCheck.length; i++) {
                                if (finalValueCheck[i] === valueItem) {
                                    finalValueCheck.remove(valueItem);
                                }
                            }
                            for (var i = 0; i < selectedShiftsNews.length; i++) {
                                if (selectedShiftsNews[i].id === valueItem) {
                                    selectedShiftsNews.remove(selectedShiftsNews[i]);
                                }

                            }
                        }
                    });
                });
                for (var i = 0; i < finalValueCheck.length; i++) {
                    if ($scope.selectedShifts.length === 0) {
                        $scope.selectedShifts = $scope.selectedShifts + finalValueCheck[i];
                    } else {
                        $scope.selectedShifts = $scope.selectedShifts + "," + finalValueCheck[i];
                    }
                }
                $("#selectShift").select2("data", selectedShiftsNews);
            });

            $scope.changeShiftByToDept = function () {
                if ($scope.transfer.toDept) {
                    for (var key in $scope.deptShiftMap) {
                        var split = key.split('$@$');
                        if (split[0] === $scope.transfer.toDept) {
                            $scope.toShiftList = [];
                            $scope.tempshiftMap = $scope.deptShiftMap[key];
                            angular.forEach($scope.tempshiftMap, function (item) {
                                $scope.toShiftList.push({id: parseInt(item.value), text: item.label});
                            });
                        }
                    }
                }
                angular.forEach($scope.newEmployeeList, function (item) {
                    item.workshift = '';
                    item.empShift = angular.copy($scope.toShiftList);
//                    console.log("shift :::" + JSON.stringify(item.empShift));
                })
                $scope.changeEmployeeDepartment();
            };

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };

            $scope.format = $rootScope.dateFormat;

            $scope.changeEmployeeDepartment = function () {
                if ($scope.newEmployeeList && $scope.transfer) {
                    angular.forEach($scope.newEmployeeList, function (item) {
                        item.departmentId = parseInt(($scope.transfer.toDept));
                    });
                }
            };
            $scope.changeEmployeeShift = function () {
                if ($scope.newEmployeeList && $scope.transfer) {
//                    console.log("shift change" + JSON.stringify($scope.transfer));
                    angular.forEach($scope.newEmployeeList, function (item) {
                        item.workshift = parseInt(($scope.transfer.toShift));
                    });
                }
            };

            $scope.changeEmployeeReportsToId = function () {
//                console.log("inside-333333--")
                if ($scope.newEmployeeList && $scope.transfer) {
                    angular.forEach($scope.newEmployeeList, function (item) {
//                        console.log("item :" + JSON.stringify(item));
                        item.reportsToId = parseInt($scope.transfer.reportsToId);
                    });
                }
            };
            $scope.changeEmployeeStatus = function () {
                if ($scope.newEmployeeList && $scope.transfer) {
                    angular.forEach($scope.newEmployeeList, function (item) {
                        item.workstatus = parseInt($scope.transfer.workstatus);
                    });
                }
                if ($scope.transfer.workstatus !== 3) {
                    $scope.transfer.relievingDate = null;
                    if ($scope.newEmployeeList && $scope.transfer) {
                        angular.forEach($scope.newEmployeeList, function (item) {
                            item.relievingDate = null;
                        });
                    }
                }
                $scope.count = 0;
                angular.forEach($scope.newEmployeeList, function (item) {
                    if (item.workstatus === 3) {
                        $scope.count++;
                    }
                });
            };

            $scope.changeEmployeeRelievingDate = function () {
                if ($scope.newEmployeeList && $scope.transfer) {
                    angular.forEach($scope.newEmployeeList, function (item) {
                        item.relievingDate = $scope.transfer.relievingDate;
                    });
                }
            }
            $scope.retrieveEmployeeStatus = function () {
                TransferEmployee.retrieveEmployeeStatus(function (res) {
                    if (res) {
                        $scope.statusList = res.data;
                    }
                }, function () {
                    console.log("failed to load employee status")
                });
            }

            $scope.autoCompleteApprover = {
                multiple: true,
                closeOnSelect: false,
                allowClear: true,
                placeholder: 'Select approvers',
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
                    var success = function (data) {
                        if (data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            $scope.names = data;
                            angular.forEach(data, function (item) {
                                $scope.names.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label
                                });
                            });
                            query.callback({
                                results: $scope.names
                            });
                        }
                    };
                    var failure = function () {
                    };
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                        var search = query.term.slice(2);
                        LeaveWorkflow.retrieveDepartmentList(search.trim(), success, failure);
                    } else if (selected.length > 0) {
                        var search = selected;
                        LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }
            };

            $scope.cancelSelectedValues = function () {
//                console.log("inside-------");
                $scope.selectedDepartments = '';
                $scope.selectedShifts = '';
                $scope.submitted = false;
                $scope.transfer = {};
                if ($scope.shiftList.length > 0) {
                    $scope.shiftList.splice(0, $scope.shiftList.length);
                }
                if ($scope.newEmployeeList) {
                    $scope.newEmployeeList = {};
                }
                $scope.transferEmployeeForm.$setPristine();
            };

            $scope.selectDeselectEmployee = function () {
                if ($scope.newEmployeeList) {
                    if ($scope.transfer.selectAll) {
                        angular.forEach($scope.newEmployeeList, function (item) {
                            item.selected = true;
                        });
                    } else {
                        angular.forEach($scope.newEmployeeList, function (item) {
                            item.selected = false;
                        });
                    }
                }
            };

            $scope.retrieveEmployeesByShiftAndByDept = function () {
                $scope.submitted = true;

                if (!$scope.selectedDepartments && $scope.submitted) {
                    $scope.transferEmployeeForm.selectShift.$invalid = true;
                    $scope.transferEmployeeForm.selectShift.$valid = false;
                    $scope.transferEmployeeForm.selectShift.$error.required = true;
                }
                if ($scope.selectedDepartments && $scope.selectedShifts !== undefined && $scope.selectedShifts instanceof Object === false && !angular.isArray($scope.selectedShifts)) {
                    var shiftArray = $scope.selectedShifts.split(',');
                    $scope.send = {
                        "shiftIds": $scope.selectedShifts,
                        "deptIds": $scope.selectedDepartments
                    }
                    TransferEmployee.retrieveEmployeesByShiftByDept($scope.send, function (res) {
                        $scope.orginalEmployeeList = res.data;
//                        console.log("data :" + JSON.stringify(res.data));
                        $scope.newEmployeeList = angular.copy($scope.orginalEmployeeList);
                        $scope.employeeDepartment = $scope.departmentList;
                        if ($scope.newEmployeeList !== null && $scope.newEmployeeList) {
                            $scope.transfer.selectAll = true;
                            angular.forEach($scope.newEmployeeList, function (item) {
                                item.selected = true;
                                if (item.departmentId) {
                                    for (var key in $scope.deptShiftMap) {
                                        var split = key.split('$@$');
//                                        console.log("split :" + typeof split[0] + "dept id :" + typeof item.departmentId);
                                        if (split[0] === item.departmentId.toString()) {
//                                            console.log("inside if")
                                            item.empShift = [];
                                            $scope.tempshiftMap = $scope.deptShiftMap[key];
                                            angular.forEach($scope.tempshiftMap, function (item1) {
                                                item.empShift.push({id: item1.value, text: item1.label});
                                            });
                                        }
                                    }
                                }

                                $scope.flag = false;
                                item.select2Config = {
                                    multiple: true,
                                    closeOnSelect: false,
                                    allowClear: true,
                                    placeholder: 'Select approvers',
                                    maximumSelectionSize: 1,
                                    initSelection: function (element, callback) {
//                                    console.log("check")
                                        var data = [];
                                        var tmp = "";
                                        if ($scope.flag) {
                                            tmp = $scope.obj.id;
                                            data.push($scope.obj);
                                        }
                                        if (!!item.reportsToId && !$scope.flag) {
                                            tmp = item.reportsToId;
                                            for (var i = 0; i < $scope.users.length; i++) {
                                                var id = $scope.users[i].value + ":" + $scope.users[i].description;
                                                if (id == item.reportsToId) {
                                                    data.push({
                                                        id: item.reportsToId,
                                                        text: $scope.users[i].label
                                                    });
                                                    break;
                                                }
                                            }
                                            for (var i = 0; i < $scope.Alldepartments.length; i++) {
                                                var id = $scope.Alldepartments[i].value + ":" + $scope.Alldepartments[i].description;
                                                if (id == item.reportsToId) {
                                                    data.push({
                                                        id: item.reportsToId,
                                                        text: $scope.Alldepartments[i].label
                                                    });
                                                    break;
                                                }
                                            }

                                        }
                                        callback(data);
                                        item.reportsToId = tmp;
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
                                            if (data.length == 0) {
                                                query.callback({
                                                    results: $scope.names
                                                });
                                            } else {
                                                $scope.names = data;
                                                angular.forEach(data, function (item) {
                                                    $scope.names.push({
                                                        id: item.value + ":" + item.description,
                                                        text: item.label
                                                    });
                                                });
                                                query.callback({
                                                    results: $scope.names
                                                });
                                            }
                                        };
                                        var failure = function () {
                                        };
                                        if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                                            var search = query.term.slice(2);
                                            LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                                        } else if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                                            var search = query.term.slice(2);
                                            LeaveWorkflow.retrieveDepartmentList(search.trim(), success, failure);
                                        } else if (selected.length > 0) {
                                            var search = selected;
                                            LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                                        } else {
                                            query.callback({
                                                results: $scope.names
                                            });
                                        }
                                    }
                                };
                            });
                        }
                    }, function () {
                    });
                } else if ($scope.selectedShifts === undefined || $scope.selectedShifts instanceof Object === true && angular.isArray($scope.selectedShifts)) {
                    $scope.transferEmployeeForm.selectShift.$invalid = true;
                    $scope.transferEmployeeForm.selectShift.$valid = false;
                    $scope.transferEmployeeForm.selectShift.$error.required = true;
                }
            };

            $scope.retrieveUsers = function () {
                LeaveWorkflow.retrieveUserList('', function (res) {
                    $scope.users = res;
                });
            };

            $scope.retrieveDepartments = function () {
                LeaveWorkflow.retrieveDepartmentList('', function (res) {
                    $scope.Alldepartments = res;
                });
            };

            $scope.changeEmployeeSiftDropdown = function (index) {
                var dept = $scope.newEmployeeList[index].departmentId;
//                console.log("dept :" + dept + "index " + index)
                if (dept) {
                    for (var key in $scope.deptShiftMap) {
                        var split = key.split('$@$');
                        if (split[0] == dept) {
                            $scope.newEmployeeList[index].empShift = [];
                            $scope.tempshiftMap = $scope.deptShiftMap[key];
                            angular.forEach($scope.tempshiftMap, function (item) {
                                $scope.newEmployeeList[index].empShift.push({id: item.value, text: item.label});
                            });
                        }
                    }
                }
            };

            $scope.changeEmployeeRelievingTableDateOnStatus = function (index) {
//                console.log("index :"+index + "$scope.newEmployeeList[index] ::"+JSON.stringify($scope.newEmployeeList[index]));
                if ($scope.newEmployeeList[index].workstatus !== 3) {
                    $scope.newEmployeeList[index].relievingDate = null;
                }
                $scope.count = 0;
                angular.forEach($scope.newEmployeeList, function (item) {
                    if (item.workstatus === 3) {
                        $scope.count++;
                    }
                });
            };


            $scope.employeeTransfer = function () {
                if ($scope.employeeTableForm.$valid) {
                    angular.forEach($scope.newEmployeeList, function (item) {
                        if (item.selected) {
                            angular.forEach($scope.orginalEmployeeList, function (emp) {
                                if (item.id === emp.id) {
                                    emp.departmentId = item.departmentId;
                                    emp.workshift = item.workshift;
                                    emp.reportsToId = item.reportsToId;
                                    emp.workstatus = item.workstatus;
                                    if (item.workstatus === 3) {
                                        emp.relievingDate = item.relievingDate;
                                    }
                                }
                            });
                        }
                    });
                    TransferEmployee.transferEmployeeByCriteria($scope.orginalEmployeeList, function () {
                        console.log("success");
                    }, function () {
                        console.log("faliure");
                    });
                } else {
                    console.log("inside not valid");
                }

            };
            if ($rootScope.getCurrentServerDate()) {
                $scope.minRelievedDate = $rootScope.getCurrentServerDate();
            } else {
                $scope.minRelievedDate = new Date();
            }


            $scope.empDtOptions = {
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: [-1, -2, -3, -4]
                    }
                ]
            };
            $rootScope.unMaskLoading();
        }]);
});
