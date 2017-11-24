/**
 * This controller is for manage leave workflow feature
 * Author : Mansi Parekh
 * Date : 30 June 2014
 */
define(['hkg', 'leaveWorkflowService', 'departmentService', 'messageService', 'treeviewMultiselect.directive', 'addMasterValue', 'dynamicForm'], function(hkg, leaveWorkflowService, departmentService, messageService) {

    hkg.register.controller('LeaveWorkflowController', ["$rootScope", "$scope", "$filter", "LeaveWorkflow", "$location", "DepartmentService", "DynamicFormService", "Messaging", "$timeout", function($rootScope, $scope, $filter, LeaveWorkflow, $location, DepartmentService, DynamicFormService, Messaging, $timeout) {
            $rootScope.maskLoading();
            $scope.searchRecords = [];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageLeaveWorkflow";
            $rootScope.activateMenu();
            if ($location.path() === '/manageleaveworkflow') {
                $rootScope.configureWorkFlow = false;
                $scope.isDepInvalid = true;
            }

            $scope.entity = "LEAVEWORKFLOW.";
            $scope.message = {};
            $scope.editWorkflowDepName = '';
            $scope.message.messageType = "";
            $scope.submitted = false;
            $scope.searchtooltip = "Search";
            $scope.workflow = {};
            $scope.editworkflow = '';
            $scope.editworkflow.departmentName = '';
            $scope.workflow.nameApprover = {};
            $scope.approvalMap = [];
            $scope.$item = [];
            $scope.isEditing = false;
            $scope.isDefault = false;
            $scope.selectedDepValues = [];
            $scope.existingIds = [];
            $scope.overrideDep = [];
            $scope.selectedInString = [];
            $scope.selectedInStringIds = [];
            $scope.selectedDepartment = {};
            $scope.departmentName = '';
            $scope.displayLeaveWorkflowFlag = 'view';
            $scope.approverInValid = true;
            $scope.editapproverInValid = true;
            $scope.workflowFlag = false;
            $scope.reload = true;
            $scope.listOfModelsOfDateType = [];
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@R'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";
//            $("#approver").select2("data", undefined);
//            $("#approver").select2("val", undefined);
//            $("#approver1").select2("data", undefined);
//            $("#approver1").select2("val", undefined);
            $scope.addLeaveWorkflowData = {};
//               DynamicFormService.resetSection( $scope.addLeaveWorkflowData);
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageLeaveWorkflow");

            $scope.dbType = {};
            templateData.then(function(section) {
                // Method modified by Shifa Salheen on 17 April for implementing sequence number
                $scope.customTemplateDate = angular.copy(section['genralSection']);
                $scope.generalLeaveWorkflowTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
            }, function(reason) {

            }, function(update) {

            });
            $scope.$on('$viewContentLoaded', function() {
                retrieveAccessRightsForWorkflow();
//                $scope.departments = $scope.retrieveAllDepartments();

            });
            /* Method added by Shifa Salheen on 2 April 2015 for clearing custom fields like multiselect and usermultiselect
             which were not getting cleared after add operation.Also On reset or cancel custom fields need to be cleared
             And On edit from tree u need to reload the directive*/
            $scope.resetCustomFields = function(callback)
            {
                $scope.listOfModelsOfDateType = [];
                $scope.addLeaveWorkflowData = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageLeaveWorkflow");

                $scope.dbType = {};
                templateData.then(function(section) {
                    $scope.customTemplateDate = angular.copy(section['genralSection']);
                    $scope.generalLeaveWorkflowTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);

                    if ($scope.generalLeaveWorkflowTemplate !== null && $scope.generalLeaveWorkflowTemplate !== undefined)
                    {
                        angular.forEach($scope.generalLeaveWorkflowTemplate, function(updateTemplate)
                        {
                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                            {
                                $scope.listOfModelsOfDateType.push(updateTemplate.model);
                            }
                        })
                    }
                    if (!!callback) {
                        callback();
                    }
                }, function(reason) {

                }, function(update) {

                });
            };
            function retrieveAccessRightsForWorkflow()
            {
                $scope.isAccessWorkflow = true;
                if (!$rootScope.canAccess('leaveWorkflowAddEdit'))
                {
                    $scope.isAccessWorkflow = false;
                }
            }
            ;
            $scope.changeDepartment = function()
            {
                if ($scope.selectedItemList != null)
                {
                    $scope.workflow_form.$dirty = true;
                }
            };
            $scope.changeMakeACopyOf = function(workflow_form)
            {
                if ($scope.departmentListDropdown1 != null)
                {
                    $scope.workflow_form = workflow_form;
                    $scope.workflow_form.$dirty = true;
                }
            };
            $scope.initDept = function() {
                $scope.multiselecttree = {"text": "All Dep", "isChecked": false,
                    items: []};
                $scope.departments = $scope.retrieveAllDepartments();

                $scope.createWatchOnSelectedItemList();
            };

            $scope.autoCompleteApprover = {
                //            allowClear : true,
                multiple: true,
                closeOnSelect: false,
//                reverse: true,
                placeholder: 'Select approvers',
                initSelection: function(element, callback) {

                    if (!$scope.isEditing) {
                        var data = [];
                        callback(data);
                    }
                    else
                    {
                        var data = [];
                        angular.forEach($scope.approvalMap, function(recipient) {
                            data.push({
                                id: recipient.value,
                                text: recipient.label
                            });
                        });


                        callback(data);
                    }
                },
                formatResult: function(item) {
                    return item.text;
                },
                formatSelection: function(item) {
                    $scope.$item.push(item);
                    return item.text;
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        if (data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            $scope.names = data;
                            angular.forEach(data, function(item) {
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
                    var failure = function() {
                    };
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                    }
                    else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                        var search = query.term.slice(2);
                        Messaging.retrieveRoleList(search.trim(), success, failure);
                    }
                    else if (selected.length > 0) {
                        var search = selected;
                        LeaveWorkflow.retrieveUserList(search.trim(), success, failure);
                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }

            };
            $scope.retrieveAllDepartments = function() {

                if (!$rootScope.configureWorkFlow) {
                    $rootScope.maskLoading();
                    var failure = function() {
                        var msg = "Failed to retrieve departments. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    };
                    LeaveWorkflow.retrieveDepartmentsCombine(function(res) {
                        // all department tree view
                        $scope.departmentListOption = [];
                        var data = res['tree1'];
                        for (var i = 0; i < data.length; i++) {
                            convertTreeToList(data[i]);
                        }
                        $scope.searchDepartmentList = angular.copy($scope.departmentListOption);
                        $scope.departmentListOption = [];
                        if ($scope.selectedDepartment !== undefined && $scope.selectedDepartment.currentNode !== undefined) {
                            $scope.selectedDepartment.currentNode = undefined;
                        }
                        // only having workflow department tree view



                        $scope.departments1 = res['tree2'];
                        $scope.setDefaultWorkflowRootOption();
                        for (var i = 0; i < $scope.departments1.length; i++) {
                            convertTreeToList($scope.departments1[i]);
                        }
                        if ($scope.selectedDepartment !== undefined && $scope.selectedDepartment.currentNode !== undefined) {
                            $scope.selectedDepartment.selected = undefined;
                            $scope.selectedDepartment.currentNode = undefined;
                        }
                        $scope.invalidParent1 = false;
                        $scope.selectedParent1 = $scope.departmentListDropdown1[0];
                        $scope.parentId1 = $scope.departmentListDropdown1[0].id;
                        // multiselect view

                        $scope.multiselect = []; // your list declaration
                        if (res['tree3'].items != null && angular.isDefined(res['tree3'].items) && res['tree3'].items.length > 0) {
                            angular.forEach(res['tree3'].items, function(item) {
//                                item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                                $scope.multiselect.push(item);
                            });
                        }
                        $scope.multiselecttree.items = $scope.multiselect;
                        $rootScope.unMaskLoading();
                    }, failure);
                }
            };
            $scope.departmentListOption = [];
            function convertTreeToList(node) {
                if (node === null || node === undefined) {
                    return;
                }
//                node.displayName = $rootScope.translateValue("DPT_NM." + node.displayName);
                $scope.departmentListOption.push({"id": node.id, "displayName": node.displayName, "description": node.description, "children": node.children, "parentId": node.parentId, "parentName": node.parentName, "companyId": node.companyId, "isActive": node.isActive, "showAllDepartment": node.showAllDepartment});
                if (node.children === null || node.children.length === 0) {
                    return;
                } else {
                    for (var i = 0; i < node.children.length > 0; i++) {
                        convertTreeToList(node.children[i]);
                    }
                }

            }
            ;
            $scope.searchDepartmentList = [];
            //Set 'None' as an option in dropdown
            $scope.setRootOption = function() {
                $scope.departmentListDropdown = [];
                $scope.departmentListDropdown.push({id: '0', displayName: 'None'});
                $.merge($scope.departmentListDropdown, angular.copy($scope.departments));
            };
            //Set 'Default Workflow' as root node
            $scope.setDefaultWorkflowRootOption = function() {
                $scope.departmentListDropdown1 = [];
                $scope.departmentListDropdown1.push({id: '0', displayName: 'None', "children": null});
                $.merge($scope.departmentListDropdown1, angular.copy($scope.departments1));
                $scope.departmentListOptiontemp = [];
                $scope.departmentListOptiontemp.push({"id": 0, "displayName": 'Default workflow', "description": 'default node', "children": null, "parentId": 0, "parentName": "None", "companyId": 0, "isActive": true, "showAllDepartment": null});
                $.merge($scope.departmentListOptiontemp, angular.copy($scope.departments1));
                $scope.departments1 = angular.copy($scope.departmentListOptiontemp);
                $scope.departmentListOptiontemp = [];
                if (angular.isDefined($scope.workflow_form)) {
                    $scope.workflow_form.$dirty = false;
                }

            };
            //        Called when make copy dropdown is clicked
            $scope.setSelectedParent1 = function(selected) {
                if (selected != undefined) {
                    var isNone = selected.currentNode.displayName;
                    $scope.invalidParent1 = false;
                    $scope.selectedParent1 = selected.currentNode;
                    $scope.parentId1 = selected.currentNode.id;
                    for (var i = 0; i < $scope.departmentListDropdown1.length; i++) {
                        if ($scope.lastSelectedOfDropdown1 !== undefined) {
                            if ($scope.lastSelectedOfDropdown1.id === $scope.departmentListDropdown1[i].id) {
                                $scope.departmentListDropdown1[i].selected = null;
                            }
                        }
                    }
                    if (isNone == 'None') {
                        $("#approver").select2("data", "");
                        $scope.list = [];
                        $scope.approverInValid = true;
                    } else {
                        $scope.setApprovers(false, $scope.parentId1, false);
                    }
                }
            };
            //        Called when existing workflow tree is clicked
            $scope.openEditWorkflow = function(selected, fromTree, configure) {
                $scope.reload = false;
                $scope.reload = false;
                $scope.displayLeaveWorkflowFlag = 'view';
                if (angular.isDefined(selected)) {
                    if (!fromTree) {
                        $scope.selectedDepartment.currentNode = angular.copy(selected);
                    }
                    $scope.searchDepId = selected.id;
                    $scope.editworkflow = selected;
                    if (selected.id == 0) {
                        $scope.isDefault = true;
                    } else {
                        $scope.isDefault = false;
                    }
                    $scope.editworkflow.departmentName = selected.displayName;
                    $scope.parentId1 = selected.id;
                    $scope.isEditing = true;
                    $scope.editworkflow.status = 'true';
                    $scope.setApprovers(true, selected.id, configure);
                }
//                $scope.reload = true;
            };
            $scope.editWorkflow = function(id)
            {
                $scope.displayLeaveWorkflowFlag = 'view';
                $scope.workflowFlag = true;
                $scope.parentId1 = id;
                $scope.searchDepId = id;
                if (id == 0) {
                    $scope.isDefault = true;
                } else {
                    $scope.isDefault = false;
                }
                $scope.isEditing = true;
                $scope.editworkflow.status = 'true';
                $scope.setApprovers(true, id, false);
            };
            $scope.setApprovers = function(isedit, id, configure) {
                $rootScope.maskLoading();
                var success = function(data) {
//                    $scope.reload=false; 

                    if (data.departmentName != null)
                    {
                        $scope.editWorkflowDepName = data.departmentName;
                    }
                    else
                    {
                        $scope.editWorkflowDepName = '';
                    }
                    if (data.id == null && $rootScope.configureWorkFlow) {
                        $scope.isEditing = false;
                        $scope.submitted = false;
                        if (angular.isDefined($scope.workflow_form)) {
                            $scope.workflow_form.$setPristine();
                        }
                    } else {
                        if (data.id == null) {
                            $("#approver").select2("data", "");
                            $("#approver1").select2("data", "");
                            $scope.list = [];
                        } else {
                            $("#approver").select2("data", "");
                            $("#approver1").select2("data", "");
                            var map = data.approvalMap;
                            $scope.approvalMap = data.approvalMap;
                            var approvers = '';
                            var temp = [];
                            $scope.names = [];
                            var i = 0;
                            angular.forEach(map, function(item) {
                                $scope.names.push({
                                    id: item.value,
                                    text: item.label
                                });
                                i++;
                                temp.push(item.value);
                            });
                            $scope.names.reverse();
                            $("#approver").select2("data", $scope.names);
                            $("#approver1").select2("data", $scope.names);
                            $scope.approverInValid = false;
                            if ($scope.selectedInString.length > 0) {
                                $scope.workflow_form.$invalid = false;
                            }
                            temp.reverse();
                            approvers = temp.toString();
                            if (isedit) {
                                $scope.editworkflow.departmentName = data.departmentName;
                                $scope.editworkflow.nameApprover = angular.copy(approvers.toString());
                            } else {
                                $scope.workflow.nameApprover = angular.copy(approvers.toString());
                            }
                        }
                        if (angular.isDefined(data.leaveWorkflowCustom) && data.leaveWorkflowCustom != null) {
                            $scope.resetCustomFields(function() {
                                $scope.addLeaveWorkflowData = angular.copy(data.leaveWorkflowCustom);
                                angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                                {
                                    if ($scope.addLeaveWorkflowData.hasOwnProperty(listOfModel))
                                    {
                                        if ($scope.addLeaveWorkflowData[listOfModel] !== null && $scope.addLeaveWorkflowData[listOfModel] !== undefined)
                                        {
                                            $scope.addLeaveWorkflowData[listOfModel] = new Date($scope.addLeaveWorkflowData[listOfModel]);
                                        } else
                                        {
                                            $scope.addLeaveWorkflowData[listOfModel] = '';
                                        }
                                    }
                                })
                                $scope.reload = true;
                            });

                        }
                    }
                    if (data.department == 0) {
                        $scope.isDefault = true;
                    }
                    if (!configure) {
                        $scope.submitted = true;
                    }
                    $rootScope.unMaskLoading();
                };
                var failure = function() {
                    var msg = "Failed to retrieve workflow detail. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                LeaveWorkflow.retrieveWorkflowByDepartmentId(id.toString(), success, failure);
            };
            if ($rootScope.configureWorkFlow) {
                var selected = {id: 0};
                $scope.openEditWorkflow(selected, false, true);
                $scope.submitted = false;
            }

            $scope.list = [];
            $scope.$watch('workflow.nameApprover', function() {
                if ($scope.workflow.nameApprover != undefined) {
                    if (typeof ($scope.workflow.nameApprover) == "string") {
                        var temp = [];
                        var exist = '';
                        var i = 0;
                        angular.forEach($scope.$item, function(item) {
                            var names = $scope.workflow.nameApprover.split(",");
                            $.each(names, function(index, approve) {
                                if (approve == item.id) {
                                    var already = exist.split(',');
                                    var contain = false;
                                    angular.forEach(already, function(check) {
                                        if (!contain) {
                                            if (check == item.id) {
                                                contain = true;
                                            }
                                        }
                                    });
                                    if (!contain) {
                                        var a = item.id.split(':');
                                        var fullname = '';
                                        if (a[1] == 'R') {
                                            fullname = '@Designation: ';
                                        }
                                        temp.push({
                                            id: fullname + item.text,
                                            text: item.text,
                                            level: i,
                                            key: item.id
                                        });
                                        if (exist.length != 0) {
                                            exist = exist + ",";
                                        }
                                        exist = exist + item.id;
                                        i++;
                                    }
                                }
                            });
                        });
                        $scope.list = angular.copy(temp.reverse());
                        if ($scope.list.length > 0) {
                            $scope.approverInValid = false;
                            if ($scope.selectedInString !== undefined && $scope.selectedInString.length > 0) {
                                $scope.workflow_form.$invalid = false;
                            }
                        } else {
                            $scope.approverInValid = true;
                            if (angular.isDefined($scope.workflow_form)) {
                                $scope.workflow_form.$invalid = true;
                            }
                        }
                    }
                } else {
                    $scope.workflow.nameApprover = '';
                }
            });
            $scope.createWatchOnSelectedItemList = function() {
                $scope.$watch('selectedItemList', function(value) {
                    if ($scope.selectedItemList != undefined) {
                        var finalname = [];
                        var finalSelectedArray = [];
                        $scope.selectedInString = [];
                        $scope.selectedInStringIds = [];
                        if ($scope.selectedItemList.items.length > 0) {
                            $scope.existingIds = [];
                            for (var i = 0; i < $scope.departmentListDropdown1.length; i++) {
                                getIdsOfExistingDepartment($scope.departmentListDropdown1[i]);
                            }
                            for (var i = 0; i < $scope.existingIds.length; i++) {
                                var depnid = $scope.existingIds[i].id;
                                $scope.selectedDepValues = [];
                                if (i == 0) {
                                    $scope.iterateChildOfTree($scope.selectedItemList.items, true, false, depnid, false, true);
                                    finalSelectedArray = angular.copy($scope.selectedDepValues);
                                }
                                $scope.selectedDepValues = [];
                                $scope.iterateChildOfTree($scope.selectedItemList.items, true, true, depnid, false, true);
                                $.merge(finalname, angular.copy($scope.selectedDepValues));
                            }
                        }

                        if (finalSelectedArray.length > 0) {
                            angular.forEach(finalSelectedArray, function(item) {
                                $scope.selectedInString.push(item.text);
                                $scope.selectedInStringIds.push(item.id);
                            });
                        }

                        $scope.isDepInvalid = false;
                        if ($scope.selectedInString.length == 0 && !$rootScope.configureWorkFlow) {
                            $scope.isDepInvalid = true;
                            $scope.workflow_form.$invalid = true;
                        } else {
                            if (!angular.isDefined($scope.list) || $scope.list.length == 0) {
                                $scope.workflow_form.$invalid = true;
                            } else {
                                $scope.workflow_form.$invalid = false;
                            }
                        }
                        $scope.selectedDepValues = [];
                        if (finalname.length > 0) {
                            $scope.overrideDep = finalname;
                            $('#overrideModal').modal('show');
                        }
                    }
                });
            };
            $scope.$watch('editworkflow.nameApprover', function() {
                if ($scope.editworkflow.nameApprover != undefined) {
                    if (typeof ($scope.editworkflow.nameApprover) == "string") {
                        var temp = [];
                        var exist = '';
                        var i = 0;
                        var names = $scope.editworkflow.nameApprover.split(",");
                        $.each(names, function(index, approve) {
                            angular.forEach($scope.$item, function(item) {
                                if (approve == item.id) {
                                    var already = exist.split(',');
                                    var contain = false;
                                    angular.forEach(already, function(check) {
                                        if (!contain) {
                                            if (check == item.id) {
                                                contain = true;
                                            }
                                        }
                                    });
                                    if (!contain) {
                                        var a = item.id.split(':');
                                        var fullname = '';
                                        if (a[1] == 'R') {
                                            fullname = '@Designation: ';
                                        }
                                        temp.push({
                                            id: fullname + item.text,
                                            text: item.text,
                                            level: i,
                                            key: item.id
                                        });
                                        if (exist.length != 0) {
                                            exist = exist + ",";
                                        }
                                        exist = exist + item.id;
                                        i++;
                                    }
                                }
                            });
                        });
                        temp.reverse();
                        $scope.list = angular.copy(temp);
                        if ($scope.list.length > 0) {
                            $scope.editapproverInValid = false;
                        } else {
                            $scope.editapproverInValid = true;
                        }
                    }
                } else {
                    $scope.editworkflow.nameApprover = '';
                }
            });
            $scope.clearData = function(workflow_form, workflow_form1) {
                $scope.reload = false;
                $scope.setViewFlag('view');
                $scope.isEditing = false;
                $scope.submitted = false;
                $scope.list = [];
                $scope.workflow.nameApprover = '';
                $scope.editworkflow.nameApprover = '';
                $scope.editworkflow.status = '';
                $scope.isDefault = false;
                $scope.invalidParent1 = false;
//                $scope.selectedParent1.selected = undefined;
                $scope.selectedInString = [];
                $scope.selectedInStringIds = [];
                $scope.selectedDepValues = [];
                $scope.existingIds = [];
                $scope.overrideDep = [];
                $scope.searchDepId = undefined;
                if ($scope.selectedDepartment.currentNode !== undefined) {
                    $scope.selectedDepartment.selected = undefined;
                    $scope.selectedDepartment.currentNode.selected = undefined;
                }
                if (angular.isDefined($scope.departmentListDropdown1)) {
                    $scope.selectedParent1 = $scope.departmentListDropdown1[0];
                    $scope.parentId1 = $scope.departmentListDropdown1[0].id;
                }
                $("#approver").select2("data", "");
                $("#approver1").select2("data", "");
                $scope.isDepInvalid = true;
                $scope.approverInValid = true;
                $scope.editapproverInValid = true;
                if (angular.isDefined(workflow_form)) {
                    workflow_form.$invalid = true;
                    workflow_form.$setPristine();
                }
                if (angular.isDefined(workflow_form1)) {
                    workflow_form1.$setPristine();
                }
                if ($rootScope.configureWorkFlow) {
                    $rootScope.configureWorkFlow = false;
                    $rootScope.defaultWorkFlow = false;
                }
                $scope.searchtext = undefined;
//                $scope.addLeaveWorkflowData = DynamicFormService.resetSection($scope.generalLeaveWorkflowTemplate);
//                $scope.reload = false;
                $timeout(function() {
                    $scope.resetCustomFields();
                    $scope.reload = true;
                }, 50);

                ;
            };
            $scope.up = function($index) {
                if ($index != 0) {
                    var index = $index;
                    var temp = $scope.list[index];
                    $scope.list[index] = $scope.list[index - 1];
                    $scope.list[index - 1] = temp;
//                    $scope.list[index].level = (index + 1);
//                    $scope.list[index - 1].level = (index);
                }
//                $scope.setInNameApprover();
            };
            $scope.down = function($index, last) {
                if (last != true) {
                    var index = $index;
                    var temp = $scope.list[index];
                    $scope.list[index] = $scope.list[index + 1];
                    $scope.list[index + 1] = temp;

//                    $scope.list[index].level = (index + 1);
//                    $scope.list[index + 1].level = (index + 2);
                }
//                $scope.setInNameApprover();
            };
            $scope.deleteRow = function($index, list) {
                var isEdit = $scope.isEditing;
                $scope.list.splice($index, 1);
                if (isEdit)
                    $("#approver1").select2("data", "");
                else {
                    $("#approver").select2("data", "");
                }
                $scope.names = [];
                angular.forEach(list, function(item) {
                    $scope.names.push({
                        id: item.key,
                        text: item.text
                    });
                });
                if (isEdit) {
                    $("#approver1").select2("data", $scope.names);
                } else {
                    $("#approver").select2("data", $scope.names);
                }
                $scope.setInNameApprover();
            };
            $scope.setInNameApprover = function() {
                var name = '';
                angular.forEach($scope.list, function(item) {
                    if (name.length != 0) {
                        name = name + ',';
                    }
                    name = name + item.key;
                });
                $scope.workflow.nameApprover = name.toString();
            };
            $scope.save = {
                submit: function(form) {

                    $scope.submitted = true;
                    if (form.approver.$invalid || !form.$valid || $scope.isDepInvalid === true || $scope.approverInValid) {
                    } else {
                        $scope.selectedIndex = undefined;
                        $scope.saveWorkflowDetail(form);
                    }
                },
                submitEdit: function(form) {
                    $scope.submitted = true;
                    if ($scope.editworkflow.status === 'false') {
                        $('#removeModal').modal('show');
                    }
                    else {
                        if (form.$valid) {
                            $scope.selectedIndex = undefined;
                            $scope.updateWorkflowDetail(form);
                        }
                    }
                }
            };
            $scope.hidePanel = function() {
                $('#removeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.noOverrideData = function() {
                $scope.checkOrUncheckAllItemsMultiselectTree(false, $scope.multiselecttree.items, true, $scope.existingIds);
                if ($scope.selectedInString.length == 0) {
                    $scope.isDepInvalid = true;
                }
                $('#overrideModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.yesOverrideData = function() {
                $('#overrideModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.removeWorkflow = function(workflow_form, workflow_form1) {
                var success = function() {
                    $scope.retrieveAllDepartments();
                    $scope.clearData(workflow_form, workflow_form1);
                };
                var failure = function() {
                    var msg = "Failed to remove workflow. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                };
                LeaveWorkflow.deleteWorkflow($scope.parentId1, success, failure);
                $('#removeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.saveWorkflowDetail = function(workflow_form) {
                $scope.reload = false;
                $rootScope.maskLoading();
                var temp = [];
                $scope.submitted = true;
                var approvers = '';
                angular.forEach($scope.list, function(item) {
                    approvers = approvers + item.key + ',';
                });
                if (angular.isDefined($scope.selectedItemList) && $scope.selectedItemList.items.length > 0) {
                    $scope.selectedDepValues = [];
                    $scope.iterateChildOfTree($scope.selectedItemList.items, true, false, null, true, false);
                    if ($scope.selectedDepValues.length > 0) {
                        angular.forEach($scope.selectedDepValues, function(item) {
                            temp.push(item.id);
                        });
                    }
                    $scope.selectedDepValues = [];
                }
                $scope.workflowMap = {"departmentIds": temp, "approvers": approvers, "leaveworkflowcustom": $scope.addLeaveWorkflowData, "leaveworkflowdbtypes": $scope.dbType};
                var success = function() {
                    if (!$rootScope.configureWorkFlow) {
                        $scope.retrieveAllDepartments();
                    }
                    $scope.clearData(workflow_form, undefined);
                    $rootScope.unMaskLoading();
                };
                var failure = function() {
                    var msg = "Failed to save wokflow detail. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();

                };
                LeaveWorkflow.createWorkflow($scope.workflowMap, success, failure);
            };
            $scope.updateWorkflowDetail = function(workflow_form1) {
                $rootScope.maskLoading();
                $scope.submitted = true;
                var approvers = '';
                angular.forEach($scope.list, function(item) {
                    approvers = approvers + item.key + ',';
                });
                $scope.workflowMap = {"departmentId": $scope.parentId1.toString(), "approvers": approvers, "leaveworkflowcustom": $scope.addLeaveWorkflowData, "leaveworkflowdbtypes": $scope.dbType};
                var success = function() {
                    $scope.retrieveAllDepartments();
                    $scope.clearData(undefined, workflow_form1);
                    $rootScope.unMaskLoading();
                };
                var failure = function() {
                    var msg = "Failed to update workflow. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                LeaveWorkflow.updateWorkflow($scope.workflowMap, success, failure);
            };
            $scope.iterateChildOfTree = function(childItems, addOnlyIfTrue, matchWithThis, matchItem, idRequired, both) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (addOnlyIfTrue && currentitems[i].isChecked) {
                        if (matchWithThis) {
                            if (currentitems[i].id == matchItem) {
                                if (both) {
                                    $scope.selectedDepValues.push({id: currentitems[i].id, text: currentitems[i].text});
                                } else {
                                    if (idRequired) {
                                        $scope.selectedDepValues.push({id: currentitems[i].id});
                                    } else {
                                        $scope.selectedDepValues.push({text: currentitems[i].text});
                                    }
                                }
                            }
                        } else {
                            if (both) {
                                $scope.selectedDepValues.push({id: currentitems[i].id, text: currentitems[i].text});
                            } else {
                                if (idRequired) {
                                    $scope.selectedDepValues.push({id: currentitems[i].id});
                                } else {
                                    $scope.selectedDepValues.push({text: currentitems[i].text});
                                }
                            }
                        }
                    }
                    if (currentitems[i].items) {
                        $scope.iterateChildOfTree(currentitems[i].items, addOnlyIfTrue, matchWithThis, matchItem, idRequired, both);
                    }
                }
            };
            $scope.checkOrUncheckAllItemsMultiselectTree = function(isCheckValue, passitems, checkMatch, existing) {
                passitems.isChecked = isCheckValue;
                if (passitems) {
                    for (var i = 0; i < passitems.length; i++) {
                        if (!checkMatch) {
                            passitems[i].isChecked = isCheckValue;
                        } else {
                            angular.forEach(existing, function(name) {
                                if (name.id == passitems[i].id) {
                                    passitems[i].isChecked = isCheckValue;
                                    angular.forEach($scope.selectedInStringIds, function(id, $index) {
                                        if (name.id == id) {
                                            $scope.selectedInStringIds.splice($index, 1);
                                            $scope.selectedInString.splice($index, 1);
                                        }
                                    });
                                }
                            });
                        }
                        if (passitems[i].items) {
                            $scope.checkOrUncheckAllItemsMultiselectTree(isCheckValue, passitems[i].items, checkMatch, existing);
                        }
                    }
                }
            };
            function getIdsOfExistingDepartment(node) {
                if (node === null || node === undefined) {
                    return;
                }
                $scope.existingIds.push({id: node.id});
                if (node.children === null || node.children.length === 0) {
                    return;
                } else {
                    for (var i = 0; i < node.children.length > 0; i++) {
                        getIdsOfExistingDepartment(node.children[i]);
                    }
                }
            }
            ;
            $scope.getSearchedDepartment = function(list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function(item) {
//                            item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                }
            };
            $scope.setViewFlag = function(flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayLeaveWorkflowFlag = flag;
            };
            $rootScope.unMaskLoading();
        }]);
});
