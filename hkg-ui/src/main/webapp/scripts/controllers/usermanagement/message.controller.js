/**
 * This controller is for manage messaging feature
 * Author : Mansi Parekh
 * Date : 5 June 2014
 */
define(['hkg', 'messageService', 'dynamicForm'], function (hkg, messageService) {

    hkg.register.controller('MessageController', ["$rootScope", "$scope", "$filter", "DynamicFormService", "Messaging", function ($rootScope, $scope, $filter, DynamicFormService, Messaging) {
            $rootScope.maskLoading();
            $scope.searchRecords = [];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageMessages";
            $rootScope.activateMenu();
            $scope.entity = "MESSAGING.";
            $scope.message = {};
            $scope.searchFlag = false;
            $scope.message.messageType = "";
            $scope.submitted = false;
            $scope.isViewPage = false;
            $scope.totalItems = 0;
            $scope.selectedTreeMessage = {};
            $scope.searchtext = '';
            $scope.allChecked = false;
            $scope.treeMessages = [];
            $scope.checkedCount = 0;
            $scope.filteredmessages = [];
            $scope.selectedType = '';
            $scope.displayMessageDetails = false;
            $scope.addMessageData = DynamicFormService.resetSection($scope.generalMessageTemplate);
            $scope.temp = {};
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr>\ " +
                    "</table>\ ";
            $scope.prioritytooltip = "Set as priority";
            $scope.hasprioritytooltip = "Has priority";
            $("#recipients").select2("data", undefined);
            $("#recipients").select2("val", undefined);

            $scope.messageTreeListOption = [];
            $scope.$on('$viewContentLoaded', function () {
                $rootScope.maskLoading();
                $scope.messageTreeListOption.push({"id": 1, "displayName": "Inbox", "children": null, "parentId": null, "parentName": null});
                $scope.messageTreeListOption.push({"id": 2, "displayName": "Priority", "children": null, "parentId": null, "parentName": null});
                $scope.messageTreeListOption.push({"id": 3, "displayName": "Sent", "children": null, "parentId": null, "parentName": null});
                $scope.retrieveMessageInitializationDetails();
                retrieveAccessRightsForMessage();
                if (localStorage.getItem('rootMessage') !== null && localStorage.getItem('rootMessage') !== undefined) {
                    var msgData = localStorage.getItem('rootMessage');
                    msgData = JSON.parse(msgData);
                    $scope.retrieveMessageDetailsById(msgData);
                    $scope.selectedMsgType = 'Inbox';
                    localStorage.removeItem('rootMessage');
                } else {
                    $scope.displayMessageDetails = false;
                }
                $rootScope.unMaskLoading();
            });
            $scope.isAccessSendMessage = false;
            function retrieveAccessRightsForMessage()
            {
                $scope.isAccessSendMessage = $rootScope.canAccess('messageSend');
            }

            $scope.retrieveMessageInitializationDetails = function () {
                $scope.messageTypes = [];
                $scope.users = [];
                $scope.roles = [];
                $scope.departments = [];
                $scope.groups = [];
                $scope.activities = [];
                $scope.messages = [];
            };

            $rootScope.$on('rootMessage', function (event, args) {
                var msgData = args.rootMessage;
                $scope.retrieveMessageDetailsById(msgData);
                localStorage.removeItem('rootMessage');
            });

            $scope.dbType = {};
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageMessages");
            templateData.then(function (section) {

                // Method modified by Shifa Salheen on 20 April for implementing sequence number
                $scope.customGeneralMessageTemplateData = angular.copy(section['genralSection']);
                $scope.generalMessageTemplate = $rootScope.getCustomDataInSequence($scope.customGeneralMessageTemplateData);
            }, function (reason) {
                console.log('Failed: ' + reason);
            }, function (update) {
                console.log('Got notification: ' + update);
            });

            $scope.autoCompleteRecipient = {
                //            allowClear : true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select recipients',
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
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
                        Messaging.retrieveUserList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                        var search = query.term.slice(2);
                        Messaging.retrieveRoleList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                        var search = query.term.slice(2);
                        Messaging.retrieveDepartmentList(search.trim(), success, failure);
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
            $scope.recipientValid = false;
            $scope.$watch('message.nameRecipient', function () {
                $scope.recipientValid = false;
                if ($scope.message.nameRecipient !== undefined) {
                    if (typeof ($scope.message.nameRecipient) === "string") {
                        $scope.recipientValid = true;
                    }
                }
            });
            $scope.retrieveMessageTemplates = function () {
                if ($scope.message.messageBody != undefined) {
                    var JsonObj = {
                        message: $scope.message.messageBody
                    };
                    var success = function (data) {
                        $scope.messages = data;
                        $scope.searchMessages(false);
                    };
                    var failure = function () {
                        var msg = "Failed to retrieve message templates. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    };
                    Messaging.retrieveMessages(JsonObj, success, failure);
                }
            };
            $scope.archiveMessage = function (message, $index) {
                var success = function () {
                    $scope.message = {};
                    $scope.message.messageType = "";
                    $scope.messages.splice($index, 1);
                    $scope.selectedIndex = undefined;
                    $scope.clear();
                    $scope.searchMessages(false);
                };
                var failure = function () {
                    var msg = "Failed to archive message. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                };
                Messaging.archiveTemplate(message.messageObj, success, failure);
            };
            $scope.saveMessageDetail = function () {
                $rootScope.maskLoading();
                var success = function (res) {
                    $scope.submitted = false;
                    $scope.clear();
                    $rootScope.unMaskLoading();
                };
                var failure = function () {
                    var msg = "Failed to save message. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage(msg, type);

                };
                $scope.message.messageCustom = $scope.addMessageData;
                $scope.message.messageDbType = $scope.dbType;
                Messaging.saveMessage($scope.message, success, failure);
            };
            $scope.save = {
                submit: function (form) {

                    $scope.submitted = true;
                    if (form.$valid) {
                        $scope.selectedIndex = undefined;
                        if ($scope.message.nameRecipient !== null && $scope.message.nameRecipient.length > 0) {
                            $scope.saveMessageDetail();
                        } else {
                            $scope.message.nameRecipient = "";
                        }
                    }
                }
            };
            $scope.message.hasPriority = false;
            $scope.setpriority = function () {
                if ($scope.message.hasPriority === false) {
                    $scope.message.hasPriority = true;
                } else {
                    $scope.message.hasPriority = false;
                }
            };
            $scope.changeStatus = function () {
                if ($scope.message.messageType == undefined) {
                    $scope.message.messageType = "";
                }
            };
            $scope.setSelectedMessage = function (message, $index) {
                $scope.selectedIndex = $index;
                $scope.setSelectedMessageSearch(message);
            };
            $scope.setSelectBlank = function () {
                $scope.selectedIndex = undefined;
                $scope.clear();
            };
            $scope.setZero = function () {
                $scope.totalItems = 0;
                $scope.filteredmessages = [];
            };
            $scope.clear = function () {
                $scope.searchFlag = false;
                $scope.totalItems = 0;
                $scope.filteredmessages = [];
                $scope.selectedType = '';
                if ($scope.selectedTreeMessage !== undefined && $scope.selectedTreeMessage.currentNode !== undefined) {
                    $scope.selectedTreeMessage.selected = undefined;
                    $scope.selectedTreeMessage.currentNode.selected = undefined;
                }
                $scope.searchtext = '';
                $("#recipients").select2("data", "");
                $scope.message = {};
                $scope.message.messageType = "";
                $scope.message.nameRecipient = "";
                if ($scope.messaging != undefined) {
                    $scope.messaging.$setPristine();
                }
                $scope.submitted = false;
                $scope.selectedIndex = undefined;
                $scope.isViewPage = false;
                $scope.checkedCount = 0;
                $scope.temp = {};
                $scope.addMessageData = DynamicFormService.resetSection($scope.generalMessageTemplate);
            };
            $scope.resetSelected = function () {
                if ($scope.selectedTreeMessage.currentNode !== undefined) {
                    $scope.selectedTreeMessage.selected = undefined;
                    $scope.selectedTreeMessage.currentNode.selected = undefined;
                }
            };
            $scope.searchMessages = function (fromSearch) {
                $scope.filteredmessages = [];
                $scope.pageSize = 10;
                $scope.maxSize = 5;
                //            $scope.currentPage = 1;
                $scope.totalItems = $scope.messages.length;
                $scope.selectedIndex = undefined;
                if ($scope.messages !== undefined) {
                    $scope.filteredmessages = $filter('filter')($scope.messages);
                    $.each($scope.filteredmessages, function (index, msg) {
                        var gcs = msg.nameRecipient.split(",");
                        var temp = '';
                        $.each(gcs, function (i, data) {
                            temp = temp + data + '\n';
                        });
                        msg.nameRecipient = temp;
                    });
                    $scope.totalItems = $scope.filteredmessages.length;
                    $scope.noOfPages = $scope.filteredmessages.length / $scope.pageSize;
                }
            };

            $scope.openViewPage = function (selected) {
                if (angular.isDefined(selected)) {
                    if ($scope.messaging != undefined) {
                        $scope.messaging.$setPristine();
                    }
                    $scope.selectedType = selected.displayName + " messages";
                    $scope.selectedMsgType = selected.displayName;
                    $scope.isViewPage = true;
                    $scope.searchFlag = false;
                    $scope.searchtext = '';
                    $scope.treeMessages = [];
                    $scope.temp = {};
                    $scope.itemsPerPage = 5;
                    $scope.currentPage = 0;
                    Messaging.retrieveTotalMessagesLength($scope.selectedMsgType, function (res) {
                        $scope.total = res;
                    });
                    $scope.retrieveMessagesForTree();
//                    $scope.serverSidePaginationOfMessage();
                }
            };
            $scope.retrieveMessagesForTree = function () {
                $rootScope.maskLoading();
                $scope.displayMessageDetails = false;
                var success = function (data) {
                    angular.forEach(data, function (item) {
                        if ($scope.selectedMsgType == 'Sent') {
                            var a = item.nameRecipient.split("\n");
                            if (a.length == 1) {
                            } else {
                                item.fullname = angular.copy(a.toString());
                                item.nameRecipient = a[0];
                            }
                        } else {
                            item.nameRecipient = item.createdBy;
                        }
                    });
                    $scope.treeMessages = data;
                    $rootScope.unMaskLoading();
                };
                var failure = function () {
                    var msg = "Failed to retrieve messages. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                Messaging.retrieveMessagesByType($scope.selectedMsgType, success, failure);
            };
            $scope.onSelectCheckbox = function (data) {
                if (data.isChecked) {
                    $scope.checkedCount++;
                } else {
                    $scope.checkedCount--;
                    $scope.temp.allChecked = false;
                }
            };
            $scope.onSelectRow = function (data) {
                data.isChecked = !data.isChecked;
                $scope.onSelectCheckbox(data);
            };
            $scope.onSelectCheckboxAll = function () {
                if ($scope.temp.allChecked) {
                    $scope.checkedCount = $scope.treeMessages.length;
                    angular.forEach($scope.treeMessages, function (item) {
                        item.isChecked = true;
                    });
                } else {
                    $scope.checkedCount = 0;
                    angular.forEach($scope.treeMessages, function (item) {
                        item.isChecked = false;
                    });
                }
            };
            $scope.archieveMessages = function () {
                $rootScope.maskLoading();
                var ids = [];
                angular.forEach($scope.treeMessages, function (item) {
                    if (item.isChecked) {
                        var item1 = {};
                        item1.messageObj = item.messageObj;
                        item1.id = item.id;
                        item1.messageType = item.messageType;
                        ids.push(item1);
                    }
                });
                var success = function () {
                    $scope.retrieveMessagesForTree();
                    $rootScope.unMaskLoading();
                }
                ;
                var failure = function () {
                    var msg = "Failed to archive message. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                Messaging.archiveMessages(ids, success, failure);
            };

            $scope.formatDate = function (date) {
                if ($scope.getDateWithoutTimeStamp(new Date(date)) === $scope.addDaysToDate(0)) {
                    return new Date(date).toLocaleTimeString();
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
            //Adds days in date and clears time
            $scope.getDateWithoutTimeStamp = function (date) {
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                date.setMilliseconds(0);
                return date.getTime();
            };
            $scope.addDaysToDate = function (daysToAdd) {
                var date = new Date($scope.getDateWithoutTimeStamp(new Date()));
                date.setDate(date.getDate() + daysToAdd);
                return date.getTime();
            };

            $scope.callsearch = function () {
                if ($scope.searchtext != undefined) {
                    var JsonObj = {
                        message: $scope.searchtext
                    };
                    var success = function (data) {
                        $scope.messages = data;
                        $scope.searchMessages(true);
                        if ($scope.totalItems.length > 0) {
                            $scope.isInValidSearch = false;
                        }
                        else {
                            $scope.isInValidSearch = true;
                        }
                    };
                    var failure = function () {
                        var msg = "Failed to retrieve message templates. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    };
                    Messaging.retrieveMessages(JsonObj, success, failure);
                }
            };
            $scope.setSelectedMessageSearch = function (message) {
                $scope.clear();
                $("#recipients").select2("data", "");
                $scope.names = [];
                if (message.nameRecipientIds) {
                    var recipientIds = message.nameRecipientIds.split(",");
                    var recipientNames = message.nameRecipient.split("\n");
                    $.each(recipientIds, function (index, id) {
                        $.each(recipientNames, function (index1, name) {
                            if (index == index1) {
                                $scope.names.push({
                                    id: id,
                                    text: name
                                });
                            }
                        });
                    });
                    $("#recipients").select2("data", $scope.names);
                }
                $scope.message = angular.copy(message);
                if (!!message.messageCustom) {
                    $scope.addMessageData = message.messageCustom;
                } else {
                    $scope.addMessageData = DynamicFormService.resetSection($scope.generalMessageTemplate);
                }
                $scope.message.nameRecipient = message.nameRecipientIds;
                $scope.messages = null;
                $scope.totalItems = 0;
                $scope.isViewPage = false;
            };
            $scope.getSearchedMessage = function (list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.resetSelected();
                        $scope.searchRecords = angular.copy(list);
                        angular.forEach($scope.searchRecords, function (item) {
                            var a = item.nameRecipient.split("\n");
                            if (a.length == 1) {
                            } else {
                                item.fullname = angular.copy(a.toString());
                                item.nameRecipient = a[0];
                            }
                        });
                    }
                    $scope.searchFlag = true;
                }
            };
            $scope.setMessageForEdit = function (id) {
                Messaging.retrieve({primaryKey: id}, function (res) {
                    $scope.setSelectedMessageSearch(res);
                    $scope.isViewPage = false;
                    $scope.searchFlag = false;
                }, function () {
                    $rootScope.addMessage("Could not retrive Message , please try again.", 1);
                });
            };

            $scope.retrieveUnreadMessages = function () {
                $scope.unreadMessages = [];
                var success = function (data) {
                    $scope.unreadMessages = data;
                    if (angular.isDefined($scope.unreadMessages) && $scope.unreadMessages.length > 0) {
                        $('#showmsgmodal').modal({
                            keyboard: false,
                            show: true
                        });
                    }
                };
                Messaging.retrieveUnreadMessages(success);
            };

            $scope.retrieveMessageDetailsById = function (message) {
                $scope.addMessageData = message.messageCustom;
                $scope.selectedMessage = angular.copy(message);
                $scope.displayMessageDetails = true;

            };

            $scope.markClosedMsg = function (data) {
                var success = function () {
                    $scope.displayMessageDetails = false;
                    $scope.retrieveMessagesForTree();
                    $scope.selectedMessage = {};
                    $scope.addMessageData = DynamicFormService.resetSection($scope.generalMessageTemplate);
                };
                var failure = function () {
                };
                if (data !== undefined && data.isAttended === false) {
                    Messaging.markAsClosed(data, success, failure);
                } else {
                    $scope.displayMessageDetails = false;
                    $scope.selectedMessage = {};
                    $scope.addMessageData = DynamicFormService.resetSection($scope.generalMessageTemplate);
                    $scope.retrieveMessagesForTree();
                }
                if ($scope.selectedTreeMessage !== undefined && $scope.selectedTreeMessage.currentNode !== undefined) {
                } else {
                    $scope.selectedMsgType = 'Inbox';
                    $scope.selectedTreeMessage.currentNode = {};
                    $scope.selectedTreeMessage.currentNode.selected = $scope.messageTreeListOption[0];
                    $scope.openViewPage($scope.messageTreeListOption[0]);
                }

            };

            $scope.checkDate = function (createdOnDate) {
                var date = $scope.addDaysToDate(-2);
                if (createdOnDate >= date) {
                    return false;
                } else {
                    return true;
                }
            };

//            $scope.serverSidePaginationOfMessage = function(data, recordsConfig, callback) {
//                console.log("inside method----");
//                $scope.treeMessages = [];
//                console.log("data : " + JSON.stringify(data));
//                if (angular.isDefined(data)) {
////                    data.customParameters = {"language": "EN", "category": scope.selectedCategoryInTree.id};
//                    $scope.responceComplete = false;
//                    var success = function(res) {
//                        angular.forEach(res, function(row, index) {
//                            recordsConfig.data.push([index + 1, row.messageBody, $scope.formatDate(row.createdOn)]);
//                        });
//                        console.log("::: " + JSON.stringify(res.length));
////                        $scope.treeMessages = res;
//                        recordsConfig.recordsTotal = res.length;
//                        recordsConfig.recordsFiltered = res.length;
//                        $scope.responceComplete = true;
//                        callback(recordsConfig);
//                    };
//                    //Failure function executes if record could not be retrieved.
//                    var failure = function(res) {
//                        console.log("failure");
//                    };
//                    Messaging.retrieveMessageAsScroll(data, success, failure);
//                }
//            };

            $rootScope.unMaskLoading();
        }]);

});
