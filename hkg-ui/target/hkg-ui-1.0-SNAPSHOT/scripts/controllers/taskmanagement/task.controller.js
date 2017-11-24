define(['hkg', 'taskService', 'messageService', 'addMasterValue', 'dynamicForm'], function (hkg) {
    hkg.register.controller('ManageTasks', ["$rootScope", "$scope", "Task", "Messaging", "$filter", "$timeout", "DynamicFormService", function ($rootScope, $scope, Task, Messaging, $filter, $timeout, DynamicFormService) {
            $rootScope.maskLoading();
            //Initialization
            $scope.entity = "TASKS.";
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageTasks";
            $rootScope.activateMenu();
            $scope.searchTaskRecords = [];
            //for Custom field
            $scope.addTaskData = {};
            $scope.addTaskData = DynamicFormService.resetSection($scope.genralTaskTemplate);
            $scope.dbType = {};
            $scope.categoryresult = [];
//            $scope.allDaysSelected = false;
            $scope.availableTaskStatus = [];
            $scope.isCategoryEmpty = false;
            $scope.categoryName = "";
            $scope.listOfModelsOfDateType = [];
            $scope.listOfModelsOfDateType1 = [];
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageTasks");
            templateData.then(function (section) {
                // Method modified by Shifa Salheen on 17 April for implementing sequence number
                $scope.customTaskTemplateData = angular.copy(section['genralSection']);
                $scope.genralTaskTemplate = $rootScope.getCustomDataInSequence($scope.customTaskTemplateData);
                if ($scope.genralTaskTemplate !== null && $scope.genralTaskTemplate !== undefined)
                {
                    angular.forEach($scope.genralTaskTemplate, function (updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            $scope.listOfModelsOfDateType.push(updateTemplate.model);
                        }
                    })
                }
                $scope.customCategoryTemplateData = angular.copy(section['category']);
                $scope.categoryTemplate = $rootScope.getCustomDataInSequence($scope.customCategoryTemplateData);
                if ($scope.categoryTemplate !== null && $scope.categoryTemplate !== undefined)
                {
                    angular.forEach($scope.categoryTemplate, function (updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            $scope.listOfModelsOfDateType1.push(updateTemplate.model);
                        }
                    })
                }

            }, function (reason) {
                console.log('Failed: ' + reason);
            }, function (update) {
                console.log('Got notification: ' + update);
            });
            $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
            $scope.dbTypeForCategory = {};

            $scope.initForm = function (form) {
                $scope.addTaskForm = form;
            };
            $scope.setTaskOperation = function (operation) {
                $scope.displayTaskFlag = 'view';

                //clear custom field
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);

                if (operation !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                if (operation === 'addTask') {
                    //Init Add Task
                    $scope.task = undefined;
                    $scope.initAddTask();
                    $scope.editFlag = false;
                } else if (operation === 'editTask') {
                    //Init Add Task
                    $scope.editFlag = true;
                    $scope.initAddTask();
                } else {
                    $scope.editFlag = false;
                }
                $scope.taskOperation = operation;
            };

            $scope.$watch('task.endRepeatMode', function () {
                if ($scope.task && $scope.task.endRepeatMode) {
                    if ($scope.task.endRepeatMode === 'OD') {
                        $scope.endRepeat.afterDaysUnits = undefined;
                        $scope.endRepeat.afterRepititionsUnits = undefined;
                    } else if ($scope.task.endRepeatMode === 'AD') {
                        $scope.task.endDate = undefined;
                        $scope.endRepeat.afterRepititionsUnits = undefined;
                    } else if ($scope.task.endRepeatMode === 'AR') {
                        $scope.task.endDate = undefined;
                        $scope.endRepeat.afterDaysUnits = undefined;
                    }
                }
            });

            //Datatable options for completed events
            $scope.taskForYouDtOptions = {
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: [1]
                    }
                ]
            };
            $scope.taskFromYouDtOptions = {
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: [1]
                    }
                ]
            };
            $scope.taskRepeatitionDtOptions = {
                order: [[0, "desc"]]
            };

            $scope.openSearchedTask = function (id) {
                Task.retrieveTasksById({primaryKey: id}, function (res) {
                    $scope.searchedTask = res;
                    if ($scope.searchedTask.assignedById === $rootScope.session.id) {
                        $scope.openEditTask(id);
                    } else {
                        $scope.showTaskDetails($scope.searchedTask);
                    }
                });
            };

//          Show task details popup
            $scope.showTaskDetails = function (selectedTask) {
//                $("#taskDetailsPopup").modal("show");
                Task.attendTask(selectedTask.taskRecipientDetailDataBeanList[0].id, function (res) {
                    //Task attended
                    selectedTask.taskRecipientDetailDataBeanList[0].taskEdited = undefined;
                }, function () {
                });
                $scope.selectedTask = selectedTask;
                $scope.displayTaskDetails = true;
                $scope.changeTaskCategoryFlag = false;
                Task.retrieveTasksById({primaryKey: selectedTask.id}, function (res) {
                    $rootScope.unMaskLoading();
//                    $scope.selectedTask = res;
                    $scope.addTaskData = res.taskCustom;
                    if (!!$scope.addTaskData) {
                        angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                        {
                            if ($scope.addTaskData.hasOwnProperty(listOfModel))
                            {
                                if ($scope.addTaskData[listOfModel] !== null && $scope.addTaskData[listOfModel] !== undefined)
                                {
                                    $scope.addTaskData[listOfModel] = new Date($scope.addTaskData[listOfModel]);
                                } else
                                {
                                    $scope.addTaskData[listOfModel] = '';
                                }
                            }
                        })
                    }
//                    $scope.selectedTask.dueDate = new Date(res.dueDate);
//                    $scope.selectedTask.repeatTask = res.repeatTask.toString();
                }, function () {
                    $rootScope.unMaskLoading();
                });

//                $scope.selectedTask = selectedTask;
                $scope.displayTaskDetails = true;
                $scope.changeTaskCategoryFlag = false;
            };
//            Removing task details popup
            $scope.hideTaskDetails = function () {
//                $("#taskDetailsPopup").modal("hide");
                $scope.displayTaskDetails = false;
                $scope.changeTaskCategoryFlag = false;
                $scope.selectedTaskForYouList = [];
                $scope.selectedTaskByYouList = [];
//                console.log("$scope.taskFilter :"+$scope.taskFilters)
                $scope.retrieveTasks();
            };
            $scope.setTaskCategoryFlag = function (value) {
                $scope.changeTaskCategoryFlag = value;
            };

            //To get category name of task from id
            $scope.getCategoryNameById = function (taskId) {
                for (var i = 0; i < $scope.taskCategories.length; i++) {
                    if ($scope.taskCategories[i].id === taskId) {
                        return $scope.taskCategories[i].text;
                    }
                }
            };
            //Fill existing task list
            $scope.fillExistingTaskList = function () {
                $scope.taskList = [
                    {"id": "S", "displayName": $rootScope.translateValue("TASKS." + "Smart List:"),
                        "children": [{"id": "2", "displayName": $rootScope.translateValue("TASKS." + "Recent tasks"), "selected": "selected"}, {"id": "3", "displayName": $rootScope.translateValue("TASKS." + "All tasks")}, {"id": "4", "displayName": $rootScope.translateValue("TASKS." + "Due today")}, {"id": "5", "displayName": $rootScope.translateValue("TASKS." + "Completed")}, {"id": "6", "displayName": $rootScope.translateValue("TASKS." + "Received")}, {"id": "7", "displayName": $rootScope.translateValue("TASKS." + "Assigned")}]
                    }];



                if ($scope.taskCategories.length > 0) {
                    $scope.taskList.push({"id": "C", "displayName": $rootScope.translateValue("TASKS." + "Categories:"),
                        "children": $scope.taskCategories
                    });

                }
                if ($scope.taskAssigners.length > 0) {
                    $scope.taskList.push({"id": "P", "displayName": $rootScope.translateValue("TASKS." + "People:"),
                        "children": $scope.taskAssigners
                    });
                }
                if ($scope.selectedSmartList) {
                    $scope.selectedSmartList.currentNode = $scope.taskList[0].children[0];
                }


            };

            $scope.fillExistingTaskListAfterCancel = function (node) {
                $scope.taskList = [
                    {"id": "S", "displayName": $rootScope.translateValue("TASKS." + "Smart List:"),
                        "children": [{"id": "2", "displayName": $rootScope.translateValue("TASKS." + "Recent tasks"), "selected": "selected"}, {"id": "3", "displayName": $rootScope.translateValue("TASKS." + "All tasks")}, {"id": "4", "displayName": $rootScope.translateValue("TASKS." + "Due today")}, {"id": "5", "displayName": $rootScope.translateValue("TASKS." + "Completed")}, {"id": "6", "displayName": $rootScope.translateValue("TASKS." + "Received")}, {"id": "7", "displayName": $rootScope.translateValue("TASKS." + "Assigned")}]
                    }];



                if ($scope.taskCategories.length > 0) {
                    $scope.taskList.push({"id": "C", "displayName": $rootScope.translateValue("TASKS." + "Categories:"),
                        "children": $scope.taskCategories
                    });

                }
                if ($scope.taskAssigners.length > 0) {
                    $scope.taskList.push({"id": "P", "displayName": $rootScope.translateValue("TASKS." + "People:"),
                        "children": $scope.taskAssigners
                    });
                }
                $scope.taskList[0].children[0].selected = undefined;
                if (node !== undefined) {
                    if (node.type !== 'C') {
                        angular.forEach($scope.taskList[0].children, function (data) {
                            if (data.id === node.id) {
                                data.selected = "selected";
                                $scope.selectedSmartList.currentNode = data;
                            }
                        });
                    } else {
                        $scope.selectedSmartList.currentNode = $scope.taskList[0].children[0];
                        $scope.setTask();
                    }
                } else {
                    $scope.selectedSmartList.currentNode = $scope.taskList[0].children[0];
                    $scope.setTask();
                }

//                console.log("$scope.selectedSmartList.currentNode ::::" + JSON.stringify($scope.selectedSmartList.currentNode))

            };

            $scope.retrieveTaskCategories = function (initTaskList, cancelFlag) {
                $rootScope.maskLoading();
                if ($scope.taskCategories === undefined) {
                    $scope.taskCategories = [];
                } else {
                    $scope.taskCategories.splice(0, $scope.taskCategories.length);
                }
                Task.retrieveCategories(function (res) {
                    $rootScope.unMaskLoading();
                    $scope.categoryresult = [];
                    if (res != null && angular.isDefined(res) && res.length > 0) {
                        angular.forEach(res, function (item) {
//                            item.displayName = $rootScope.translateValue("TSKCTG_NM." + item.displayName);
                            $scope.categoryresult.push(item);
                        });
                    }
                    $scope.taskCategories = [];
                    angular.forEach(res, function (item) {
//                        item.displayName = $rootScope.translateValue("TSKCTG_NM." + item.displayName);
                        $scope.taskCategories.push({
                            id: item.id,
                            text: item.displayName,
                            displayName: item.displayName + "(" + item.categoryCount + ")",
                            categoryCustomData: null,
                            type: "C"
                        });
                    });
                    if (initTaskList) {
                        if (cancelFlag) {
                            $scope.fillExistingTaskListAfterCancel($scope.selectedSmartList.currentNode);
                        } else {
                            $scope.fillExistingTaskList();
                        }

                    }
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };

            //To retrieve all task assigner names displayed in smartlist of People
            $scope.retrieveTaskAssignerNames = function (cancelFlag) {
                if ($scope.taskAssigners === undefined) {
                    $scope.taskAssigners = [];
                } else {
                    $scope.taskAssigners.splice(0, $scope.taskAssigners.length);
                }

                $rootScope.maskLoading();
                Task.retrieveTaskAssignerNames(function (res) {
                    $rootScope.unMaskLoading();
                    angular.forEach(res, function (assigner) {
                        assigner.label = $rootScope.translateValue("TASKS." + assigner.label);
                        $scope.taskAssigners.push({
                            id: assigner.value,
                            text: assigner.label,
                            displayName: assigner.label,
                            type: "P"
                        });
                    });
                    if (cancelFlag) {
                        $scope.fillExistingTaskListAfterCancel($scope.selectedSmartList.currentNode);
                    } else {
                        $scope.fillExistingTaskList();
                    }

                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }

//            $scope.searchpopover =
//                    "<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\
//\n\
//'@C'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Categories</td></tr>\
//</table>\
//";

            //To retrieve all tasks
            $scope.retrieveTasks = function () {
                $scope.taskResponse = false;
//                $scope.allTaskForYouList=undefined;
                $rootScope.maskLoading();
                if ($scope.taskFilter !== undefined) {
                    Task.retrieveAllTasks($scope.taskFilter, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.taskResponse = true;

                        if (res.tasksForUser !== '') {
                            $scope.allTaskForYouList = [];
                            if (res.tasksForUser != null && angular.isDefined(res.tasksForUser) && res.tasksForUser.length > 0) {
                                angular.forEach(res.tasksForUser, function (item) {
                                    item.taskName = $rootScope.translateValue("TSK_NM." + item.taskName);
                                    $scope.allTaskForYouList.push(item);
                                });
                            } else {
                                $scope.allTaskForYouList = [];
                            }
                            if (res.tasksByUser !== '') {
                                $scope.allTaskFromYouList = [];
                                if (res.tasksByUser != null && angular.isDefined(res.tasksByUser) && res.tasksByUser.length > 0) {
                                    angular.forEach(res.tasksByUser, function (item) {
                                        item.taskName = $rootScope.translateValue("TSK_NM." + item.taskName);
                                        if (item.recipientNames !== null) {
                                            var result = item.recipientNames.split(",");
                                            var names = unique(result);
//                                        console.log("names :" + names);
                                            if (names.length > 5) {
                                                for (var i = 0; i < 5; i++) {
                                                    if (i === 0) {
                                                        item.recipientNames = names[i];
                                                    } else {
                                                        item.recipientNames += ", " + names[i];
                                                    }
                                                }

                                                item.recipientNames += " ..." + (names.length - 5) + "more";
                                            } else {
                                                item.recipientNames = names.toString();
                                            }
                                        }
                                        $scope.allTaskFromYouList.push(item);
                                    });
                                }
                            }
                        } else {
                            $scope.allTaskFromYouList = [];
                        }
                        if ($scope.addTaskForm !== undefined) {
                            $scope.addTaskForm.$dirty = false;
                        }
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                }

            };

            $scope.recipientNameSplitMethod = function (names) {
                var split = names.split(',');
                if (split.length <= 1) {
                    return true;
                } else {
                    return false;
                }
            };
            function unique(arr) {
                var hash = {}, result = [];
                for (var i = 0, l = arr.length; i < l; ++i) {
                    if (!hash.hasOwnProperty(arr[i])) { //it works with objects! in FF, at least
                        hash[ arr[i] ] = true;
                        result.push(arr[i]);
                    }
                }
                return result;
            }




            //To retrieve tasks by category
            $scope.retrieveTasksByCategory = function () {
                $rootScope.maskLoading();
                $scope.taskResponse = false;
                $scope.allTaskForYouList = [];
                $scope.allTaskFromYouList = [];
                Task.retrieveTasksByCategory($scope.category.id, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.taskResponse = true;
                    if (res.tasksForUser !== '') {
                        if (res.tasksForUser != null && angular.isDefined(res.tasksForUser) && res.tasksForUser.length > 0) {
                            angular.forEach(res.tasksForUser, function (item) {
                                item.taskName = $rootScope.translateValue("TSK_NM." + item.taskName);
                                $scope.allTaskForYouList.push(item);
                            });
                        }
                    } else {
                        $scope.allTaskForYouList = [];
                    }
                    if (res.tasksByUser !== '') {
                        if (res.tasksByUser != null && angular.isDefined(res.tasksByUser) && res.tasksByUser.length > 0) {
                            angular.forEach(res.tasksByUser, function (item) {
                                item.taskName = $rootScope.translateValue("TSK_NM." + item.taskName);
                                $scope.allTaskFromYouList.push(item);
                            });
                        }
                    } else {
                        $scope.allTaskFromYouList = [];
                    }
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
            //Retrieve tasks assigned  by specific people
            $scope.retrieveTasksByAssigner = function () {
                $rootScope.maskLoading();
                $scope.taskResponse = false;
                $scope.allTaskForYouList = [];
                $scope.assignerId = $scope.selectedSmartList.currentNode.id;
                Task.retrieveTasksByAssigner($scope.assignerId, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.taskResponse = true;
                    if (res != null && angular.isDefined(res) && res.length > 0) {
                        angular.forEach(res, function (item) {
                            item.taskName = $rootScope.translateValue("TSK_NM." + item.taskName);
                            $scope.allTaskForYouList.push(item);
                        });
                    }
                    $scope.allTaskFromYouList = [];
                }, function () {

                });
            };
            //Set task from smartlist
            $scope.setTask = function () {
                $scope.setTaskOperation("manageTask");
                $scope.initSelectedTaskList();
                $scope.displayTaskDetails = false;
                $scope.changeTaskCategoryFlag = false;
                $scope.editCategoryFlag = false;
                $scope.displayTaskRepitition = false;
                if ($scope.selectedSmartList.currentNode.type === undefined) {
                    $scope.smartListSelection = $scope.selectedSmartList.currentNode.id;

                    //Set to recent task when smartlist is selected
                    if ($scope.smartListSelection === 'S') {
                        $scope.selectedSmartList.currentNode.selected = undefined;
                        $scope.selectedSmartList.currentNode = $scope.taskList[0].children[0];
                        $scope.selectedSmartList.currentNode.selected = "selected";
                        $scope.setTask();
                    }
                    //Set to first category when Categories is selected
                    else if ($scope.smartListSelection === 'C') {
                        $scope.selectedSmartList.currentNode.selected = undefined;
                        $scope.selectedSmartList.currentNode = $scope.taskCategories[0];
                        $scope.selectedSmartList.currentNode.selected = "selected";
                        $scope.setTask();
                    }
                    //Set to first People when People is selected
                    else if ($scope.smartListSelection === 'P') {
                        $scope.selectedSmartList.currentNode.selected = undefined;
                        $scope.selectedSmartList.currentNode = $scope.taskAssigners[0];
                        $scope.selectedSmartList.currentNode.selected = "selected";
                        $scope.setTask();
                    }
                    //Display completed Tasks
                    else if ($scope.smartListSelection === '5') {
                        $scope.taskForYouHeader = "Tasks completed by you";
                        $scope.taskFromYouHeader = "Tasks completed for you";
                        $scope.emptyTaskListMsg = "No completed tasks";
                        $scope.taskFilter = "completed";
                    }
                    //Display due today tasks
                    else if ($scope.smartListSelection === '4') {
                        $scope.taskForYouHeader = "Due today tasks for you";
                        $scope.taskFromYouHeader = "Due today tasks from you";
                        $scope.emptyTaskListMsg = "No due tasks";
                        $scope.taskFilter = "duetoday";
                    }
                    //Display Recent Tasks
                    else if ($scope.smartListSelection === '2') {
                        $scope.taskForYouHeader = "Recent tasks for you";
                        $scope.taskFromYouHeader = "Recent tasks from you";
                        $scope.emptyTaskListMsg = "No recent tasks";
                        $scope.taskFilter = "recent";
                    }
                    //Display all tasks
                    else if ($scope.smartListSelection === '3') {
                        $scope.taskForYouHeader = "All tasks for you";
                        $scope.taskFromYouHeader = "All tasks from you";
                        $scope.emptyTaskListMsg = "No tasks found";
                        $scope.taskFilter = "all";
                    }
                    //Display received tasks
                    else if ($scope.smartListSelection === '6') {
                        $scope.taskForYouHeader = "Tasks for you";
                        $scope.taskFromYouHeader = "";
                        $scope.emptyTaskListMsg = "No tasks found";
                        $scope.taskFilter = "received";
                    }
                    //Display assigned tasks
                    else if ($scope.smartListSelection === '7') {
                        $scope.taskForYouHeader = "";
                        $scope.taskFromYouHeader = "Tasks from you";
                        $scope.emptyTaskListMsg = "No tasks found";
                        $scope.taskFilter = "assigned";
                    }
                    $scope.retrieveTasks();
                } else if ($scope.selectedSmartList.currentNode.type === 'C') {
                    $scope.smartListSelection = undefined;
                    $scope.editCategoryFlag = true;
                    $scope.taskForYouHeader = "Tasks for you";
                    $scope.taskFromYouHeader = "Tasks from you";
                    $scope.emptyTaskListMsg = "No associated tasks";
                    //Reset taskcategory exist flag
                    if ($scope.manageTaskForm.categoryName !== undefined) {
                        $scope.manageTaskForm.categoryName.$setValidity("exists", true);
                    }
                    for (var i = 0; i < $scope.categoryresult.length; i++) {
                        if ($scope.categoryresult[i].id === $scope.selectedSmartList.currentNode.id) {
                            Task.addCustomDataToCategoryDataBean($scope.categoryresult[i], function (res) {
                                $scope.categoryresult[i].categoryCustomData = res.categoryCustomData;
                                $scope.selectedSmartList.currentNode.categoryCustomData = $scope.categoryresult[i].categoryCustomData;
                                $scope.category = angular.copy($scope.selectedSmartList.currentNode);
                                $scope.addCategoryData = $scope.category.categoryCustomData;
                                if (!!$scope.addCategoryData) {
                                    angular.forEach($scope.listOfModelsOfDateType1, function (listOfModel)
                                    {
                                        if ($scope.addCategoryData.hasOwnProperty(listOfModel))
                                        {
                                            if ($scope.addCategoryData[listOfModel] !== null && $scope.addCategoryData[listOfModel] !== undefined)
                                            {
                                                $scope.addCategoryData[listOfModel] = new Date($scope.addCategoryData[listOfModel]);
                                            } else
                                            {
                                                $scope.addCategoryData[listOfModel] = '';
                                            }
                                        }
                                    });
                                }
                                if (angular.isUndefined($scope.category.status)) {
                                    $scope.category.status = "Active";
                                }
                                $scope.retrieveTasksByCategory();
                            }, function () {
                            });
                            break;
                        }
                    }


                    //                    $scope.category = angular.copy($scope.selectedSmartList.currentNode);
                    //setting custom data to scope

                } else if ($scope.selectedSmartList.currentNode.type === 'P') {
                    $scope.smartListSelection = undefined;
                    $scope.taskForYouHeader = "Tasks for you";
                    $scope.retrieveTasksByAssigner();
                }


            };
            //To initialize selcted task list to move it to particular category
            $scope.initSelectedTaskList = function () {
                $scope.selectedCategoryLabel = "Change category";
                $scope.selectedTaskForYouList = [];
                $scope.selectedTaskByYouList = [];
            };
            //Init manage task
            $scope.initManageTask = function () {
                $scope.hideTaskDetails();
                $scope.taskList = [];
                $scope.initSelectedTaskList();
                $scope.selectedTask = {};
                $scope.categoryViewForm = {};
                $scope.changeTaskCategoryFlag = false;
                $scope.setTaskOperation("manageTask");
                $scope.retrieveTaskCategories(true, true);
                $scope.retrieveTaskAssignerNames(true);

                $scope.taskFilter = "recent";
                $scope.taskForYouHeader = "Recent tasks for you";
                $scope.taskFromYouHeader = "Recent tasks from you";
                $scope.emptyTaskListMsg = "No recent tasks";
                $scope.editCategoryFlag = false;

                $scope.retrieveTasks();
                //clear custom data on cancel
                $scope.addTaskData = DynamicFormService.resetSection($scope.genralTaskTemplate);
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                //Function for custom search
                $.fn.dataTable.ext.search = [];
                $.fn.dataTable.ext.search.push(
                        function (settings, data, dataIndex) {
                            if ($scope.searchTask !== undefined && $scope.searchTask !== '' && data[3] !== undefined) {
                                if (data[3].match(new RegExp($scope.searchTask, "i"))) {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                );
            };

            $scope.cancelUpdatePage = function () {
                $scope.hideTaskDetails();
                $scope.taskList = [];
                $scope.initSelectedTaskList();
                $scope.selectedTask = {};
                $scope.categoryViewForm = {};
                $scope.changeTaskCategoryFlag = false;
                $scope.setTaskOperation("manageTask");
                $scope.retrieveTaskCategories(true, true);
                $scope.retrieveTaskAssignerNames(true);
//                $scope.taskFilter = "recent";
//                $scope.taskForYouHeader = "Recent tasks for you";
//                $scope.taskFromYouHeader = "Recent tasks from you";
//                $scope.emptyTaskListMsg = "No recent tasks";
                $scope.editCategoryFlag = false;
                $scope.retrieveTasks();
                //clear custom data on cancel
                $scope.addTaskData = DynamicFormService.resetSection($scope.genralTaskTemplate);
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                //Function for custom search
                $.fn.dataTable.ext.search = [];
                $.fn.dataTable.ext.search.push(
                        function (settings, data, dataIndex) {
                            if ($scope.searchTask !== undefined && $scope.searchTask !== '' && data[3] !== undefined) {
                                if (data[3].match(new RegExp($scope.searchTask, "i"))) {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                );
            };
            $scope.initManageTask();

            //To complete task
            $scope.completeTask = function () {
                $rootScope.maskLoading();
                Task.completeTask($scope.selectedTask.taskRecipientDetailDataBeanList[0].id, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.retrieveTaskCategories(true, true);
                    $scope.retrieveTasks();
                    $scope.hideTaskDetails();
                }, function () {
                    $rootScope.unMaskLoading();
                })
            };

            $scope.removetTaskFromList = function (index, taskRecipientId, taskStatus) {
                if (taskStatus === 'Completed' || taskStatus === 'Cancelled') {
                    $scope.taskResponse = false;
                    $rootScope.maskLoading();
                    Task.removeTaskOfUserFromList(taskRecipientId, function (res) {
                        $scope.allTaskForYouList.splice(index, 1);
                        $scope.taskResponse = true;
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                }
            };

            //To edit task
            $scope.editTask = function (task) {
                $rootScope.maskLoading();
                $scope.displayTaskFlag = 'view';
                Task.retrieveTasksById({primaryKey: task.id}, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.task = res;
                    $scope.task.dueDate = new Date($scope.task.dueDate);
                    if ($scope.task.monthlyOnDay === null) {
                        $scope.task.monthlyOnDay = 1;
                    }
                    if ($scope.task.endDate !== null && $scope.task.endDate !== undefined) {
                        $scope.task.endDate = new Date($scope.task.endDate);
                    } else {
                        $scope.task.endDate = null;
                    }
                    $scope.task.repeatTask = task.repeatTask.toString();
                    $scope.minDueDate = new Date($scope.task.dueDate);
//                    var tomorrow = new Date();
//                    if ($rootScope.getCurrentServerDate()) {
//                        tomorrow.setDate($rootScope.getCurrentServerDate().getDate() + 1);
//                    } else {
//                        tomorrow.setDate(tomorrow.getDate() + 1);
//                    }
//                    if ($scope.task.endDate !== undefined && $scope.task.endDate !== null) {
//                        $scope.minEndRepeatDate = new Date($scope.task.endDate);
//                    } else {
//                        $scope.minEndRepeatDate = new Date(tomorrow);
//                    }
                    $scope.setEditTaskEndRepeatMinDate();
                    $scope.setTaskOperation("editTask");
                    $scope.setEditTaskModelValues();
                    $scope.recipients = res.taskRecipientDataBeanList;
                    $(document).find($("#recipients")).select2("val", []);
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };

            $scope.getProfilePictureOfUser = function (userId) {
                return $rootScope.apipath + "employee/getprofilepicture/" + userId + '?decache=' + $rootScope.randomCount;
            };
            //Set edit task model values
            $scope.setEditTaskModelValues = function () {
                //setting custom data for addTask
                $scope.addTaskData = $scope.task.taskCustom;
                if (!!$scope.addTaskData) {
                    angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                    {
                        if ($scope.addTaskData.hasOwnProperty(listOfModel))
                        {
                            if ($scope.addTaskData[listOfModel] !== null && $scope.addTaskData[listOfModel] !== undefined)
                            {
                                $scope.addTaskData[listOfModel] = new Date($scope.addTaskData[listOfModel]);
                            } else
                            {
                                $scope.addTaskData[listOfModel] = '';
                            }
                        }
                    })
                }
                if ($scope.task.endRepeatMode === 'AD') {
                    $scope.endRepeat.afterDaysUnits = $scope.task.afterUnits;
                } else if ($scope.task.endRepeatMode === 'AR') {
                    $scope.endRepeat.afterRepititionsUnits = $scope.task.afterUnits;
                }
                //Add due status if task is due
                if ($scope.task.status === 'Due') {
                    if ($scope.task.repeatTask === "false") {
                        $scope.task.tempStatus = 'Due';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Due", id: "Due"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    } else {
                        $scope.task.tempStatus = 'Active';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Active", id: "Active"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    }
                }
                if ($scope.task.status === 'Completed') {
                    if ($scope.task.repeatTask === "false") {
                        $scope.task.tempStatus = 'Completed';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Completed", id: "Completed"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    } else {
                        $scope.task.tempStatus = 'Active';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Active", id: "Active"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    }

                }
                if ($scope.task.status === 'Cancelled') {
                    if ($scope.task.repeatTask === "false") {
                        $scope.task.tempStatus = 'Cancelled';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Cancel", id: "Cancelled"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    } else {
                        $scope.task.tempStatus = 'Active';
                        $scope.availableTaskStatusTemp = [];
                        $scope.availableTaskStatusTemp = {name: "Active", id: "Active"};
                        $scope.availableTaskStatusTemp.name = $rootScope.translateValue("TASKS." + $scope.availableTaskStatusTemp.name);
                        $scope.availableTaskStatus.unshift($scope.availableTaskStatusTemp);
                    }
                }
                if ($scope.task.repeatativeMode === 'W') {
                    var weekDays = $scope.task.weeklyOnDays.split("|");
                    var count = 0;
                    angular.forEach($scope.weekList, function (week, index) {
                        if (week.value === weekDays[count]) {
                            week.isChecked = "true";
                            count++;
                        }
                    });
                }

            };
            //Finds taskcategory by category id
            $scope.findTaskCategory = function (categoryId) {
                for (var i = 0; i < $scope.taskCategories.length; i++) {
                    if ($scope.taskCategories[i].id === categoryId) {
                        return $scope.taskCategories[i];
                    }
                }
                ;
            };
            //Set selected task list to move to category
            $scope.setSelectedTaskList = function (task, taskList) {
                if (task.selected) {
                    $scope.addTaskToList(task, taskList);
                } else {
                    $scope.removeTaskFromList(task, taskList);
                }
            };
            //To add task to list
            $scope.addTaskToList = function (item, taskList) {
                taskList.push(item);
            };

            //To remove task from selected task list
            $scope.removeTaskFromList = function (item, taskList) {
                var index = taskList.indexOf(item)
                taskList.splice(index, 1);
            };

            $scope.changeTaskCategoryFromTaskDetails = function (task, category) {
                $scope.selectedTaskForYouList = [task];
                $scope.changeTaskCategory(category);

            };

            $scope.checkDueDateFunct = function (dueDate) {
                if (dueDate !== undefined && $scope.editFlag) {
                    if (dueDate >= $rootScope.getCurrentServerDate()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            };

            $scope.changeTaskCategory = function (category) {
                $scope.selectedCategoryLabel = category.text;
                $scope.taskRecipeintDetailListToUpdate = [];
                $scope.taskListToUpdate = [];
                $scope.categoryIds = [category.id];
                $scope.receivedTaskIds = [];
                $scope.createdTaskIds = [];

                angular.forEach($scope.selectedTaskForYouList, function (task, index) {
                    if (task.assignedById === $rootScope.session.id) {
                        $scope.createdTaskIds.push(task.id);
                    }
                    $scope.receivedTaskIds.push(task.taskRecipientDetailDataBeanList[0].id);
                });
                angular.forEach($scope.selectedTaskByYouList, function (task, index) {
                    angular.forEach(task.taskRecipientDetailDataBeanList, function (data) {
                        if (data.userId === $rootScope.session.id) {
                            $scope.receivedTaskIds.push(task.taskRecipientDetailDataBeanList[0].id);
                        }
                    });

                    $scope.createdTaskIds.push(task.id);
                });
                $scope.taskIdAndCategoryJSON = {categoryId: $scope.categoryIds, receivedTasksIds: $scope.receivedTaskIds, createdTasksIds: $scope.createdTaskIds};
                $rootScope.maskLoading();

                Task.updateTaskCategories($scope.taskIdAndCategoryJSON, function (res) {
                    angular.forEach($scope.selectedTaskForYouList, function (task, index) {
                        task.taskRecipientDetailDataBeanList[0].recipientCategory = $scope.categoryIds[0];
                        if (task.assignedById === $rootScope.session.id) {
                            task.taskCategory = $scope.categoryIds[0];
                        }
                        task.selected = false;
                    });
                    angular.forEach($scope.selectedTaskByYouList, function (task, index) {
                        task.taskCategory = $scope.categoryIds[0];
                        angular.forEach(task.taskRecipientDetailDataBeanList, function (data) {
                            if (data.userId === $rootScope.session.id) {
                                data.recipientCategory = $scope.categoryIds[0];
                            }
                        });
                        task.selected = false;
                    });
                    $scope.initSelectedTaskList();
                    $scope.retrieveTaskCategories(true, true);
                    $scope.retrieveTasks();
                    $scope.changeTaskCategoryFlag = false;
                    $scope.selectedCategoryLabel = "Change category";
                    $rootScope.unMaskLoading();
                }, function () {
                    $scope.taskResponse = true;
                    $scope.initSelectedTaskList();
                    $rootScope.addMessage("Cannot update task categories.Try again.", 1);
                    $rootScope.unMaskLoading();
                });
            };
            $scope.calculateCompleted = function (taskRecipientDetailDataBeanList) {
                var completedCount = 0;
                angular.forEach(taskRecipientDetailDataBeanList, function (taskRecipient) {
                    if (taskRecipient.status === 'Completed' || taskRecipient.status === 'Completed Archived') {
                        completedCount++;
                    }
                });
                return (completedCount * 100 / taskRecipientDetailDataBeanList.length).toFixed(2);
            };
            //For comparing two float values.
            $scope.compareFloat = function (x, y) {
                if (parseFloat(x) === parseFloat(y)) {
                    return true;
                } else {
                    return false;
                }
            };
            //Adds days in date and clears time
            $scope.getDateWithoutTimeStamp = function (date) {
                if (date !== undefined) {
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    date.setMilliseconds(0);
                    return date.getTime();
                }
                return null;
            };

            //Displays task complete status details 
            $scope.checkTaskRepititions = function (task) {
                $scope.displayCompletionPopup = false;
                $scope.displayTaskDetails = false;
                $scope.displayTaskRepitition = true;
                $scope.repeatitionResponse = false;
                $scope.taskCompletionResponse = false;
                $scope.selectedTaskForCompletionStatus = task;
                if (!angular.equals({}, $scope.selectedTaskForCompletionStatus.repititionTaskRecipeintDetailMap)) {
                    $scope.repeatitionResponse = true;
                }
                $scope.recipientsObjById = {};
                $rootScope.maskLoading();
                Task.retrieveTaskRecipients(task.id, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.employeeList = [];
                    $scope.departmentList = [];
                    $scope.roleList = [];
                    angular.forEach(res, function (recipient) {
                        $scope.recipientsObjById[recipient.recipientInstance] = recipient.recipientValue;
                        if (recipient.recipientType === 'E') {
                            $scope.employeeList.push(recipient.recipientValue);
                        } else if (recipient.recipientType === 'R') {
                            $scope.roleList.push(recipient.recipientValue);
                        } else if (recipient.recipientType === 'D') {
                            $scope.departmentList.push(recipient.recipientValue);
                        }
                    });

                    $scope.recipientsToDisplay = "";
                    if ($scope.departmentList.length > 0) {
                        if ($scope.recipientsToDisplay === "") {
                            $scope.recipientsToDisplay += "Department: ";
                        } else {
                            $scope.recipientsToDisplay += ", Department: ";
                        }
                        var index = 0;
                        angular.forEach($scope.departmentList, function (item) {
                            if (index === 0) {
                                $scope.recipientsToDisplay += item;
                            } else {
                                $scope.recipientsToDisplay += ", " + item;
                            }
                            index++;

                        });
//                        $scope.recipientsToDisplay += "Department: " + $scope.departmentList.toString();
                    }
                    if ($scope.roleList.length > 0) {
                        if ($scope.recipientsToDisplay === "") {
                            $scope.recipientsToDisplay += "Role: ";
                        } else {
                            $scope.recipientsToDisplay += ", Role: ";
                        }
                        var index = 0;
                        angular.forEach($scope.roleList, function (item) {
                            if (index === 0) {
                                $scope.recipientsToDisplay += item;
                            } else {
                                $scope.recipientsToDisplay += ", " + item;
                            }
                            index++;

                        });
//                        $scope.recipientsToDisplay += "Role: " + $scope.roleList.toString();
                    }
                    if ($scope.employeeList.length > 0) {
                        if ($scope.recipientsToDisplay === "") {
                            $scope.recipientsToDisplay += "Employee: ";
                        } else {
                            $scope.recipientsToDisplay += ", Employee: ";
                        }

                        var index = 0;
                        angular.forEach($scope.employeeList, function (item) {
                            if (index === 0) {
                                $scope.recipientsToDisplay += item;
                            } else {
                                $scope.recipientsToDisplay += ", " + item;
                            }
                            index++;

                        });
//                        $scope.recipientsToDisplay += "Employee: " + $scope.employeeList.toString();
                    }

                    $scope.taskCompletionResponse = true;
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };

            //to hide task repition details
            $scope.hideTaskRepitition = function () {
                $scope.displayTaskRepitition = false;
            };

            $scope.showCompletionStatusPopup = function (repitition) {
                $scope.selectedRepitition = repitition;
                angular.forEach($scope.selectedRepitition, function (item) {
                    var splitName = item.userName.split("-");
                    if (splitName[1] !== undefined && splitName[1] !== null) {
                        item.name = splitName[1].trim();
                    }
                });
                $scope.displayCompletionPopup = true;
                $("#taskCompletedStatus").modal("show");
            };
            //To close taskcompletestatus poup
            $scope.hideTaskCompleteStatusPopup = function () {
                $scope.displayCompletionPopup = false;
                $("#taskCompletedStatus").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.addDaysToDate = function (daysToAdd) {
                var date = new Date($scope.getDateWithoutTimeStamp($rootScope.getCurrentServerDate()));
                date.setDate(date.getDate() + daysToAdd);
                return date.getTime();
            }

            $scope.editTaskCategory = function (manageTaskForm) {
                manageTaskForm.categoryName.$setValidity("exists", true);
                if (manageTaskForm.$valid) {
                    $scope.saveEditedCategory(manageTaskForm);
                }
            };

            $scope.saveEditedCategory = function (manageTaskForm) {
                $scope.categoryToSave = angular.copy($scope.category);
                $scope.categoryToSave.displayName = $scope.categoryToSave.text;
                $scope.categoryToSave.type = undefined;
                $scope.categoryToSave.selected = undefined;
                $scope.categoryToSave.text = undefined;
                $scope.isCategoryEmpty = true;
                //for custom Field
                $scope.categoryToSave.categoryCustomData = $scope.addCategoryData;
                $scope.categoryToSave.dbTypeForCategory = $scope.dbTypeForCategory;
                //end custom field
//                console.log("from you :" + JSON.stringify($scope.allTaskFromYouList));
                if (($scope.allTaskForYouList.length !== 0 || $scope.allTaskFromYouList.length !== 0) && $scope.categoryToSave.status === 'Remove') {
                    //$rootScope.addMessage("Category is not empty it cannot be removed.", 1);
                    $scope.showRemoveCategoryPopup($scope.categoryToSave.displayName, false);
                } else if ($scope.categoryToSave.status === 'Remove') {
                    $scope.categoryName = $scope.categoryToSave.displayName;
                    $("#removeCategoryPopup").modal("show");
                } else {
                    $rootScope.maskLoading();
                    Task.editTaskCategory($scope.categoryToSave, function (res) {
                        $rootScope.unMaskLoading();
                        if (res.messages !== undefined) {
                            $scope.categoryExistsMsg = res.messages[0].message;
                            manageTaskForm.categoryName.$setValidity("exists", false);
                        }
                        $scope.initManageTask();
                    }, function () {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Category could not be updated. Try again.", 1);
                    });
                }
                ;
            }
            $scope.removeCategory = function (manageTaskForm) {
                $scope.categoryToSave = angular.copy($scope.category);
                $scope.categoryToSave.displayName = $scope.categoryToSave.text;
                $scope.categoryToSave.type = undefined;
                $scope.categoryToSave.selected = undefined;
                $scope.categoryToSave.text = undefined;
                $scope.isCategoryEmpty = true;
                //for custom Field
                $scope.categoryToSave.categoryCustomData = $scope.addCategoryData;
                $scope.categoryToSave.dbTypeForCategory = $scope.dbTypeForCategory;
                //end custom field
                $rootScope.maskLoading();
                Task.editTaskCategory($scope.categoryToSave, function (res) {
                    $rootScope.unMaskLoading();
                    if (res.messages !== undefined) {
                        $scope.categoryExistsMsg = res.messages[0].message;
                        manageTaskForm.categoryName.$setValidity("exists", false);
                    }
                    $scope.initManageTask();
                }, function () {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Category could not be updated. Try again.", 1);
                });

                ;
            }
            $scope.formatDate = function (date) {

                if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(0)) {
                    return "Today";
                }
                else if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(-1)) {
                    return "Yesterday";
                } else if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(1)) {
                    return "Tomorrow";
                }
                else {
                    return $filter("date")(date, $rootScope.dateFormat);
                }

            };
            $scope.accessFlag = $rootScope.canAccess('tasksAssignToAll');
            $scope.initRecipients = function () {
                //For recipients,
                $scope.autoCompleteRecipient = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select recipients',
                    initSelection: function (element, callback) {
                        if ($scope.editFlag && $scope.accessFlag) {
                            var data = [];
                            angular.forEach($scope.recipients, function (recipient) {
                                if (recipient.recipientType === 'E') {
                                    var label = recipient.recipientValue.split("-");
                                    label[1] = $rootScope.translateValue("EMP_NM." + label[1]);
                                    var finalLabel = label[0] + "-" + label[1];
                                    recipient.recipientValue = finalLabel;
                                } else if (recipient.recipientType === 'D') {
                                    var label = recipient.recipientValue;
                                    label = $rootScope.translateValue("DPT_NM." + label);
                                    var finalLabel = label;
                                    recipient.recipientValue = finalLabel;
                                } else if (recipient.recipientType === 'R') {
                                    var label = recipient.recipientValue;
                                    label = $rootScope.translateValue("DESIG_NM." + label);
                                    var finalLabel = label;
                                    recipient.recipientValue = finalLabel;
                                }
                                data.push({
                                    id: recipient.recipientInstance + ":" + recipient.recipientType,
                                    text: recipient.recipientValue
                                });
                            });
                            callback(data);
                        } else if (!$scope.accessFlag) {
                            var data = [];
                            data.push({
                                id: $rootScope.session.id + ":" + "E",
                                text: $rootScope.session.userCode + "-" + $rootScope.session.firstName + " " + $rootScope.session.lastName
                            });
                            callback(data);
                        }

                    },
                    formatResult: function (item) {
                        return item.text;
                    },
                    formatSelection: function (item) {
                        var temp = item.id.split(":");
                        var recipientType = temp[1];
                        if (recipientType === 'E') {
                            var label = item.text.split("-");
                            label[1] = $rootScope.translateValue("EMP_NM." + label[1].trim());
                            //Add a space in text like hkAdmin - hk Admin(for view purpose only)
                            var finalLabel = label[0].trim() + " - " + label[1];
                            item.text = finalLabel;
                        } else if (recipientType === 'D') {
                            var label = item.text;
                            label = $rootScope.translateValue("DPT_NM." + label);
                            var finalLabel = label;
                            item.text = finalLabel;
                        } else if (recipientType === 'R') {
                            var label = item.text;
                            label = $rootScope.translateValue("DESIG_NM." + label);
                            var finalLabel = label;
                            item.text = finalLabel;
                        }
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
                            Task.retrieveUsers(search.trim(), success, failure);
                        } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                            var search = query.term.slice(2);
                            Messaging.retrieveRoleList(search.trim(), success, failure);
                        } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                            var search = query.term.slice(2);
                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                        }else if (selected.length > 0) {
                            var search = selected;
                            Task.retrieveUsers(search.trim(), success, failure);
                        } else {
                            query.callback({
                                results: $scope.names
                            });
                        }
                    }
                };
            };
            //Add Task controller
            //Initialization of add task form
            $scope.initAddTaskForm = function () {
                //clear custom data
                $scope.addTaskData = DynamicFormService.resetSection($scope.genralTaskTemplate);
                $scope.taskName = "";
                $scope.disableWeeklyChecbox = [];
                $scope.editCategoryFlag = false;
                if ($scope.task === undefined) {
                    $scope.task = {};
                    $scope.task.taskCategory = '';
                    $scope.task.repeatTask = "false";
                    $scope.task.taskRecipients = '';
                }
                $scope.endRepeat = {};
                if ($scope.task.repeatTask === 'false') {
                    $scope.task.repeatativeMode = "D";
                    $scope.task.endRepeatMode = "OD";
                    $scope.task.monthlyOnDay = 1;
                }

                $scope.initRecipients();
                $scope.selectedSmartList.currentNode.selected = undefined;
                $scope.weekList = [{code: "M", value: "2"}, {code: "Tu", value: "3"}, {code: "W", value: "4"}, {code: "Th", value: "5"}, {code: "F", value: "6"}, {code: "Sa", value: "7"}, {code: "Su", value: "1"}];
                $scope.categoryForm = {};
                $scope.submitted = false;
            };
//            Initialization of add task controller
            $scope.initAddTask = function () {
                $scope.initAddTaskForm();
                $scope.availableTaskStatusTemp = [];
                $scope.availableTaskStatus = [];
                $scope.availableTaskStatusTemp = [{name: "Remove", id: "Remove"}];
                if ($scope.availableTaskStatusTemp != null && angular.isDefined($scope.availableTaskStatusTemp) && $scope.availableTaskStatusTemp.length > 0) {
                    angular.forEach($scope.availableTaskStatusTemp, function (item) {
                        item.name = $rootScope.translateValue("TASKS." + item.name);
                        $scope.availableTaskStatus.push(item);
                    });
                }
                $scope.dayList = new Array();

                for (var i = 0; i < 31; i++) {
                    $scope.dayList.push(i + 1);
                }


                //For tooltip
                $scope.popover =
                        "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                        "\n\ " +
                        "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                        "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                        "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                        "</table>\ ";
                $scope.retrieveTaskSuggestions();
            };



            $scope.retrieveTaskSuggestions = function () {
                $rootScope.maskLoading();
                Task.retrieveTaskSuggestions(function (res) {
                    $rootScope.unMaskLoading();
                    $scope.taskSuggestions = res;
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };


            //To create new task
            $scope.addTask = function (addTaskForm) {
                $scope.submitted = true;
//                console.log("$scope.allDaysSelected :" + $scope.allDaysSelected)
                if ($scope.task.repeatativeMode === 'W') {
                    $scope.countWeek = 0;
                    angular.forEach($scope.weekList, function (week, index) {
                        if (week.isChecked) {
                            $scope.countWeek++;
                        }
                    });
                    if ($scope.countWeek === 0) {
                        $scope.noDaysSelected = true;
                    }
                }
                if (addTaskForm.$valid && (($scope.task.repeatativeMode === 'W' && $scope.countWeek !== 0) || $scope.task.repeatativeMode !== 'W')) {
                    $scope.taskToSave = angular.copy($scope.task);
                    $scope.taskToSave.taskCustom = $scope.addTaskData;
                    $scope.taskToSave.dbType = $scope.dbType;
                    if (!$scope.editFlag) {
                        $scope.setModelValues();
                        $rootScope.maskLoading();
                        Task.createTask($scope.taskToSave, function (res) {
                            $rootScope.unMaskLoading();
                            $scope.resetAddTaskForm(addTaskForm);
                            $scope.initManageTask();
                            $scope.setTask();
                        }, function () {
                            $rootScope.unMaskLoading();
                            $rootScope.addMessage("Could not save details, please try again.", 1);
                        });
                    } else {
                        if ($scope.taskToSave.tempStatus === 'Remove') {
                            $scope.taskToSave.status = 'Remove';
                            $scope.taskToSave.tempStatus = undefined;
                            $scope.showDeleteTaskPopup($scope.taskToSave.taskName);
                        } else {
                            $scope.taskToSave.tempStatus = undefined;
                            $scope.setModelValues();
                            $scope.updateTask();
                            $scope.addTaskData = DynamicFormService.resetSection($scope.genralTaskTemplate);
                        }
                    }
                }
            };

            $scope.showDeleteTaskPopup = function (taskName) {
                $scope.taskName = $scope.taskToSave.taskName;
                $("#deleteTaskPopup").modal("show");
            };

            $scope.cancelTask = function () {
                $("#deleteTaskPopup").modal("hide");
                $('.modal-backdrop').remove();
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.setModelValues();
                $scope.updateTask();
            };
            $scope.removeTaskCancel = function () {
                $("#deleteTaskPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.updateTask = function () {
                $rootScope.maskLoading();
                Task.updateTask($scope.taskToSave, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.initManageTask();
                    $scope.setTask();
                }, function () {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Task could not be updated. Try again.", 1);
                });
            };
//To remove task by task id
            $scope.removeTask = function () {
                Task.removeTask($scope.taskToSave.id, function (res) {
                    $("#deleteTaskPopup").modal("hide");
                    $('.modal-backdrop').remove();
                    $rootScope.removeModalOpenCssAfterModalHide();
                    $scope.initManageTask();
                }, function () {
                    $rootScope.addMessage("Task could not be removed. Try again.", 1);
                    $("#deleteTaskPopup").modal("hide");
                    $('.modal-backdrop').remove();
                    $rootScope.removeModalOpenCssAfterModalHide();
                });
            };
//            $('#deleteTaskPopup').on('hidden.bs.modal', function (e) {
//                alert(1)
//
//            });
            //Set model values for add task
            $scope.setModelValues = function () {
                if ($scope.editFlag) {
                    $scope.taskToSave.taskRecipients = $("#recipients").select2("val").toString().split(",");

                } else {
                    if ($scope.task.taskRecipients !== undefined && $scope.task.taskRecipients.length > 0) {
                        if (!$scope.accessFlag) {
                            $scope.taskToSave.taskRecipients = $scope.task.taskRecipients.id;
                        } else {
                            $scope.taskToSave.taskRecipients = $scope.task.taskRecipients.split(",");
                        }
                    }
                }
                //Fill weekdays value
                if ($scope.taskToSave.repeatativeMode === 'W') {
                    $scope.taskToSave.weeklyOnDays = '';
                    angular.forEach($scope.weekList, function (week, index) {
                        if (week.isChecked) {
                            $scope.taskToSave.weeklyOnDays += week.value;
                            $scope.taskToSave.weeklyOnDays += "|";
                        }
                    });
                    $scope.taskToSave.weeklyOnDays = $scope.taskToSave.weeklyOnDays.substring(0, $scope.taskToSave.weeklyOnDays.length - 1);
                }
                if ($scope.taskToSave.repeatTask === 'true') {
                    if ($scope.taskToSave.endRepeatMode === 'AD') {
                        $scope.taskToSave.afterUnits = $scope.endRepeat.afterDaysUnits;
                    } else if ($scope.taskToSave.endRepeatMode === 'AR')
                    {
                        $scope.taskToSave.afterUnits = $scope.endRepeat.afterRepititionsUnits;
                    }
                }

            };
            //For day selection validation for week days
            $scope.getSelectedWeekCount = function () {
                var count = 0;
                angular.forEach($scope.weekList, function (week, index) {
                    $scope.disableWeeklyChecbox[index] = false;
                    if (week.isChecked) {
                        $scope.noDaysSelected = false;
                        count++;
                    }
                });
//                if (count > 5) {
//                    angular.forEach($scope.weekList, function (week, index) {
//                        if (!week.isChecked) {
//                            $scope.disableWeeklyChecbox[index] = true;
//                        }
//                    });
//                }
//                if (count === 7) {
//                    $scope.allDaysSelected = true;
//                } else {
//                    $scope.allDaysSelected = false;
//                }
                return count;
            };
            //Reset add task form
            $scope.resetAddTaskForm = function (addTaskForm) {
                $("#recipients").select2("data", undefined);
                $("#recipients").select2("val", undefined);
                $scope.task = undefined;
                $scope.taskToSave = undefined;
//                $scope.retrieveTaskCategories(false);
                $scope.initAddTaskForm();
                addTaskForm.$setPristine();
            }
            //To create new category
            $scope.createTaskCategory = function (addCategoryPopupForm) {
                if (!$scope.viewManageCategory) {
                    $scope.categoryForm.submitted = true;
                } else {
                    $scope.categoryViewForm.submitted = true;
                }
                if (addCategoryPopupForm.$valid) {
                    $rootScope.maskLoading();
                    $scope.category.categoryCustomData = $scope.addCategoryData;
                    $scope.category.dbTypeForCategory = $scope.dbTypeForCategory;
                    Task.createTaskCategory($scope.category, function (res) {
                        $scope.viewManageCategory = false;
                        $rootScope.unMaskLoading();
                        if (res.messages !== undefined) {
                            $scope.categoryExistsMsg = res.messages[0].message;
                            addCategoryPopupForm.categoryName.$setValidity("exists", false);

                        } else {
                            if (!$scope.viewManageCategory) {
                                $scope.hideAddCategoryPopup(addCategoryPopupForm);
                            } else {
                                $scope.hideCategoryPopupFromView(addCategoryPopupForm);
                            }
                            $scope.retrieveTaskCategories(true, true);
                        }

                    }, function () {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Could not save details, please try again.", 1);
                    });
                }
            };
            //Date picker options and function
//            var tomorrow = new Date();
//            if ($rootScope.getCurrentServerDate()) {
//                tomorrow.setDate($rootScope.getCurrentServerDate().getDate() + 1);
//            } else {
//                tomorrow.setDate(tomorrow.getDate() + 1);
//            }
//
//            $scope.minEndRepeatDate = tomorrow;
            $scope.minDueDate = $rootScope.getCurrentServerDate();

            $scope.setEditTaskMinDate = function () {
                $scope.minDueDate = $rootScope.getCurrentServerDate();
            };

            $scope.setEditTaskEndRepeatMinDate = function () {
                if (!!$scope.task && $scope.task.dueDate !== undefined) {
                    $scope.minEndRepeatDate = $scope.task.dueDate;
                } else {
                    $scope.minEndRepeatDate = $rootScope.getCurrentServerDate();
                }
            };
            $scope.setEditTaskEndRepeatMinDate();
            $scope.datePicker = {};
            $scope.open = function ($event, opened) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.datePicker[opened] = true;
            };
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            $scope.format = $rootScope.dateFormat;

            $scope.showAddCategoryPopup = function (addCategoryPopupForm) {
                $scope.category = {};
                $scope.categoryForm.submitted = false;
                addCategoryPopupForm.categoryName.$setValidity("exists", true);
                $("#addCategoryPopup").modal("show");
            };

            $scope.showCategoryPopupFromView = function (addCategoryPopupForm) {
                $scope.category = {};
                $scope.categoryViewForm.submitted = false;
                $scope.viewManageCategory = true;
                addCategoryPopupForm.categoryName.$setValidity("exists", true);
                $("#addCategoryPopup").modal("show");
            }
            $scope.hideAddCategoryPopup = function (addCategoryPopupForm) {
                $scope.category = {};
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                addCategoryPopupForm.$setPristine();
                $('#addCategoryPopup').modal('hide');
                $('.modal-backdrop').remove();
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.hideCategoryPopupFromView = function (addCategoryPopupForm) {
                $scope.category = {};
                $scope.viewManageCategory = false;
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                addCategoryPopupForm.$setPristine();
                $('#addCategoryPopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.getSearchedTask = function (list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchTaskRecords = [];
                        $scope.setViewFlag('search');
                    } else {
                        $scope.searchTaskRecords = angular.copy(list);
                        $scope.setViewFlag('search');
                    }
                }
            };
            $scope.setViewFlag = function (flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayTaskFlag = flag;
            };
            //To edit task
            $scope.openEditTask = function (id) {
                $rootScope.maskLoading();
                Task.retrieveTasksById({primaryKey: id}, function (res) {
                    $scope.displayTaskFlag = 'view';
                    $rootScope.unMaskLoading();
                    $scope.task = res;
                    $scope.task.dueDate = new Date(res.dueDate);
                    if ($scope.task.monthlyOnDay === null) {
                        $scope.task.monthlyOnDay = 1;
                    }
                    $scope.task.repeatTask = res.repeatTask.toString();
                    $scope.minDueDate = new Date($scope.task.dueDate);
//                    var tomorrow = new Date();
//                    if ($rootScope.getCurrentServerDate()) {
//                        tomorrow.setDate($rootScope.getCurrentServerDate().getDate() + 1);
//                    } else {
//                        tomorrow.setDate(tomorrow.getDate() + 1);
//                    }
//                    if ($scope.task.endDate !== undefined && $scope.task.endDate !== null) {
//                        $scope.minEndRepeatDate = new Date($scope.task.endDate);
//                    } else {
//                        $scope.minEndRepeatDate = new Date(tomorrow);
//                    }
                    $scope.setEditTaskEndRepeatMinDate();
                    $scope.setTaskOperation("editTask");
                    $scope.setEditTaskModelValues();
                    $scope.recipients = res.taskRecipientDataBeanList;
                    $(document).find($("#recipients")).select2("val", []);
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };

            $scope.cancelRepeatition = function (key, repeatition) {
                if (!$scope.compareFloat($scope.calculateCompleted(repeatition), '100.00') && repeatition[0].status !== 'Cancelled') {
                    $scope.selectedRepeatitionKey = key;
                    $scope.repeatitionMap = {taskId: repeatition[0].task, repetitionCount: repeatition[0].repetitionCount};
                    $("#cancelTaskRepatitionPopup").modal("show");
                }
            };

            $scope.cancelRepeatitionOk = function () {
                $scope.repeatitionResponse = false;
                $rootScope.maskLoading();
                Task.cancelRepeatedTask($scope.repeatitionMap, function (res) {
                    delete $scope.selectedTaskForCompletionStatus.repititionTaskRecipeintDetailMap[$scope.selectedRepeatitionKey];
                    $scope.repeatitionResponse = true;
                    $scope.checkTaskRepititions($scope.selectedTaskForCompletionStatus);
                    $rootScope.unMaskLoading();
                    $scope.cancelRepeatitionCancel();
                }, function () {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Task repeatation cannot be cancelled. Try again.", 1);
                });

            };
            $scope.cancelRepeatitionCancel = function () {
                $("#cancelTaskRepatitionPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.showRemoveCategoryPopup = function (categoryName, isCategoryEmpty) {
                $scope.isCategoryEmpty = isCategoryEmpty;
                $scope.categoryName = $scope.categoryToSave.displayName;
                $("#removeCategoryPopup").modal("show");
            }
            $scope.removeCategoryCancelPress = function () {
                $scope.isCategoryEmpty = false;
                $("#removeCategoryPopup").modal("hide");
                $('.modal-backdrop').remove();
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.removeCategoryOkPress = function () {
                $scope.isCategoryEmpty = false;
                $("#removeCategoryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.removeCategoryRemovePress = function (manageTaskForm) {
                $scope.isCategoryEmpty = false;
                $("#removeCategoryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.removeCategory(manageTaskForm);

            }
            $rootScope.unMaskLoading();
        }]);
});
