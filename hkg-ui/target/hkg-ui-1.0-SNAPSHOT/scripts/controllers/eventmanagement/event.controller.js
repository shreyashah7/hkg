define(['hkg', 'eventService', 'messageService', 'fileUploadService', 'colorpicker.directive', 'addMasterValue', 'dynamicForm'], function (hkg) {
    hkg.register.controller('ManageEvents', ["$rootScope", "$scope", "Event", "Messaging", "$filter", "$compile", "$templateCache", "$timeout", "DynamicFormService", "FileUploadService", "$location", function ($rootScope, $scope, Event, Messaging, $filter, $compile, $templateCache, $timeout, DynamicFormService, FileUploadService, $location) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageEvents";
            $rootScope.activateMenu();
            //Initialization
            $scope.entity = "EVENTS.";
            $scope.ADD_EVENT = "addEvent";
            $scope.MANAGE_EVENT = "manageEvent";
            $scope.EDIT_EVENT = "editEvent";
            $scope.REGISTER_EVENT = "registerEvent";
            $scope.VIEW_PHOTO_GALLERY = "photoGallery";
            $scope.searchEventRecords = [];
            $scope.attendeesOnstatus = [];
            $scope.eventCategories = [];
            $scope.eventToSaveGallery = {};
            $scope.eventForGallery = {};
            $scope.attendeeList = [];
            var text = '';
            $scope.editRegistrationFlag = false;
            $scope.viewMeetingDetailsPopup = false;
            $scope.eventToSaveGallery.fileList = [];
            $scope.availableEventStatus = [];
            $scope.fileNames = [];
            $scope.attendeeStatus = [];
            $scope.exportTypeList = [];
            $scope.attendee = {};
            $scope.photos = [];
            $scope.viewPhotos = [];
            $scope.selection = [];
            $scope.eventDetails = {};
            $scope.addEventForm = {};
            $scope.count = 1;
//            $scope.userId = $rootScope.session.id;
//            $scope.deptId = $rootScope.session.department;
//            $scope.roleIds = $rootScope.session.roleIds;

            $scope.addCategoryData = {};
            $scope.printTypeList = [];
            $scope.dbTypeForCategory = {};
            $scope.accessFlag = $rootScope.canAccess('eventsAddEdit');
            $scope.categoryFormSubmitted = false;
            $scope.getSearchedEvents = function (list) {
                $scope.searchedEventList = list;
            };
            $scope.checkFranchise = function (event) {
                var franchiseId = $rootScope.session.companyId;
                if (event.franchise === franchiseId) {
                    return true;
                } else {
                    return false;
                }
            };
            $scope.attendeeStatus = [{id: 'AwaitingResponse', label: 'Awaiting Response'}, {id: 'Attending', label: 'Attending'}, {id: 'NonAttending', label: 'Non-Attending'}];
            $scope.setDefaultStatus = function () {
                $scope.attendee.status = $scope.attendeeStatus[0].id;
            };
            $scope.exportTypeList = [{id: 'PDF', label: 'PDF'}, {id: 'DOC', label: 'DOC'}, {id: 'Image', label: 'Image'}];
            $scope.setDefaultExportType = function () {
                $scope.exportType = $scope.exportTypeList[0].id;
            };
            $scope.printTypeList = [{id: 'Attending', label: 'Attending'}, {id: 'All', label: 'All'}];
            $scope.setDefaultPrintType = function () {
                $scope.printType = $scope.printTypeList[1].id;
            };

            $scope.initForm = function (addEventForm) {
                $scope.addEventForm = addEventForm;
            };
            $scope.setEventOperation = function (operation) {
                $scope.addCategoryData = {};
                if (operation !== "searchedEvent") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.editFlag = false;
                if (operation === $scope.ADD_EVENT) {
                    $scope.addEventData = DynamicFormService.resetSection($scope.generalEventTemplate);
                    $scope.addRegFormData = DynamicFormService.resetSection($scope.generalRegFormTemplate);
                    $scope.addInvitationData = DynamicFormService.resetSection($scope.generalInviTemplate);
//Init Add Event
                    $scope.editFlag = false;
                    $scope.event = undefined;
                    $scope.initAddEvent();
                } else if (operation === $scope.EDIT_EVENT) {
//Init Add Task
                    $scope.editFlag = true;
                    $scope.initAddEvent();
                }
                $scope.eventOperation = operation;
            };
            $scope.checkDate = function (event) {
                var eventPublishedOn = event.publishedOn;
                var eventRegistrationLastDt = event.registrationLastDate;
                if (event.registrationType !== "OFLN") {
                    if (eventRegistrationLastDt !== undefined || eventRegistrationLastDt !== null) {
                        if ((eventPublishedOn <= $rootScope.getCurrentServerDate().setHours(0, 0, 0, 0)) && (eventRegistrationLastDt >= $rootScope.getCurrentServerDate().setHours(0, 0, 0, 0))) {
                            if (event.enableRegister) {
                                return false;
                            }
                            return true;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }

            };

            $scope.showEventDetailsPopup = function (event, status) {
                $scope.eventsId = event.id;
                $scope.attendee.status = status;
                if (status === 'Attending') {
                    $scope.statusField = 'AT';
                } else if (status === 'NonAttending') {
                    $scope.statusField = 'NAT';
                } else if (status === 'AwaitingResponse') {
                    $scope.statusField = null;
                }
                $scope.retrieveUserRegistration($scope.eventsId);
                $scope.viewMeetingDetailsPopup = true;
            };

            $scope.enableCard = function (event) {
                if (event.enableRegister) {
                    return true;
                } else {
                    return false;
                }
            };
//Init manage event
            $scope.initManageEvent = function () {
//Init todays date
                if ($rootScope.getCurrentServerDate() !== undefined) {
                    var todayDate = $rootScope.getCurrentServerDate();
                    todayDate.setHours(0);
                    todayDate.setMinutes(0);
                    todayDate.setSeconds(0);
                    todayDate.setMilliseconds(0);
                    $scope.todayDate = todayDate.getTime();
                } else {
                    var todayDate = new Date();
                    todayDate.setHours(0);
                    todayDate.setMinutes(0);
                    todayDate.setSeconds(0);
                    todayDate.setMilliseconds(0);
                    $scope.todayDate = todayDate.getTime();
                }

                $scope.editCategory = false;
                $scope.configForm = false;
                //Datatable options for upcoming events
                $scope.upcomingEventsDtOptions = {
                    aoColumnDefs: [
                        {
                            bSortable: false,
                            aTargets: [-1, -2, -3, -7]
                        }
                    ]
                };
                //Datatable options for completed events
                $scope.completedEventsDtOptions = {
                    aoColumnDefs: [
                        {
                            bSortable: false,
                            aTargets: [-1, -2, -4]
                        }
                    ]
                };
                $scope.attendeeDtOptions = {
                    aoColumnDefs: [
                        {
                            bSortable: false,
                            aTargets: [-1, -2]
                        }
                    ]
                };
                $scope.setEventOperation($scope.MANAGE_EVENT);
                $scope.retrieveEvents("null");
                $scope.retriveEventCategories();
                $scope.retrieveActiveEventCategories();
                $scope.addEventData = {};
                $scope.preview = true;
                $scope.addRegFormData = {};
                $scope.addInvitationData = {};
                $scope.dbType = {};
                $scope.dbType1 = {};
                $scope.dbType2 = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageEvents");
                templateData.then(function (section) {

                    // Method modified by Shifa Salheen on 20 April for implementing sequence number
                    $scope.customGeneralEventTemplateData = angular.copy(section['genralSection']);
                    $scope.generalEventTemplate = $rootScope.getCustomDataInSequence($scope.customGeneralEventTemplateData);
                    $scope.customGeneralRegTemplateData = angular.copy(section['registration']);
                    $scope.generalRegFormTemplate = $rootScope.getCustomDataInSequence($scope.customGeneralRegTemplateData);
                    $scope.customInviTemplateData = angular.copy(section['Invitationcard']);
                    $scope.generalInviTemplate = $rootScope.getCustomDataInSequence($scope.customInviTemplateData);
                    $scope.customCategoryTemplateData = angular.copy(section['category']);
                    $scope.categoryTemplate = $rootScope.getCustomDataInSequence($scope.customCategoryTemplateData);
                }, function (reason) {
                    console.log('Failed: ' + reason);
                }, function (update) {
                    console.log('Got notification: ' + update);
                });
            };
            //To get thumbnail path of banner image
            $scope.getThumbnailPath = function (path) {
                var dirPath = path.substring(0, path.lastIndexOf("."));
                dirPath += "_T.jpg";
                return dirPath;
            };
            $scope.searchpopover =
                    "<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@C'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Categories</td></tr>\ " +
                    "</table>\ ";
            $scope.initEvnetCategories = function () {
                $scope.eventCategoryListDropdown = $scope.eventCategories;
                //To add element to first index
                if ($scope.eventCategoryListDropdown !== undefined) {
                    $scope.eventCategoryListDropdown.unshift({"id": "D", "displayName": "Select event category"});
                }
                //Init dropdown category
                $scope.selectedEventCategoryDropdown = {};
                $scope.selectedEventCategoryDropdown.currentNode = $scope.eventCategoryListDropdown[0];
                $scope.selectedEventCategoryPopup = {};
                $scope.eventCategoryListPopup = angular.copy($scope.eventCategoryListDropdown);
                $scope.eventCategoryListPopup[0].displayName = "Select category or leave blank";
                $scope.selectedEventCategoryPopup.currentNode = $scope.eventCategoryListPopup[0];
                $scope.eventCategoryId = 'D';
                $scope.popupEventCategoryId = 'D';
            };
//To retrieve category names suggestions
            $scope.retrieveEventCategorySuggestions = function () {
                $rootScope.maskLoading();
                Event.retrieveCategorySuggestions(function (res) {
                    $rootScope.unMaskLoading();
                    $scope.eventCategorySuggestions = res;
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
//To retrieve event categories
            $scope.retriveEventCategories = function () {
                //Retrieve event category suggestions
                $scope.retrieveEventCategorySuggestions();
                $rootScope.maskLoading();
                Event.retrieveEventCategories(function (res) {
                    $rootScope.unMaskLoading();
                    $scope.eventCategoriesTemp = [];
                    $scope.eventCategories = [];
                    $scope.eventCategoriesTemp = res;
                    if ($scope.eventCategoriesTemp != null && angular.isDefined($scope.eventCategoriesTemp) && $scope.eventCategoriesTemp.length > 0) {
                        angular.forEach($scope.eventCategoriesTemp, function (item) {
//                            item.displayName = $rootScope.translateValue("EVNTCTG_NM." + item.displayName);
                            $scope.eventCategories.push(item);
                        });
                    }
                    $scope.initEvnetCategories();
                    //Init popup category
                }, function () {
                    $rootScope.unMaskLoading();
                    $scope.initEvnetCategories();
                });
            };
            //Retrieve only active event categories
            $scope.retrieveActiveEventCategories = function () {
                $rootScope.maskLoading();
                Event.retrieveActiveEventCategories(function (res) {
                    $rootScope.unMaskLoading();
                    $scope.existingEventCategoryListTemp = [];
                    $scope.existingEventCategoryList = [];
                    $scope.existingEventCategoryListTemp = res;
                    if ($scope.existingEventCategoryListTemp != null && angular.isDefined($scope.existingEventCategoryListTemp) && $scope.existingEventCategoryListTemp.length > 0) {
                        angular.forEach($scope.existingEventCategoryListTemp, function (item) {
//                            item.displayName = $rootScope.translateValue("EVNTCTG_NM." + item.displayName);
                            $scope.existingEventCategoryList.push(item);
                        });
                    }
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
            //To remove event from dashboard
            $scope.archieveEvent = function (index) {
                $rootScope.maskLoading();
                $scope.eventResponse = false;
                Event.archieveEvent($scope.allCompletedEvents[index].id, function (res) {
                    $scope.allCompletedEvents.splice(index, 1);
                    $scope.eventResponse = true;
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
//To retrieve events list.
            $scope.retrieveEvents = function (categoryId) {
                $rootScope.maskLoading();
                $scope.eventResponse = false;
                var send = {
                    'categoryId': categoryId,
                    'haveAddEditRights': $rootScope.canAccess('eventsAddEdit')
                };
                Event.retrieveAllEvents(send, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.eventResponse = true;
                    $scope.allUpcomingEvents = [];
                    $scope.allCompletedEvents = [];
                    if (res.upcomingEvents != null && angular.isDefined(res.upcomingEvents) && res.upcomingEvents.length > 0) {
                        angular.forEach(res.upcomingEvents, function (item) {
                            item.eventTitle = $rootScope.translateValue("EVNT_TITLE." + item.eventTitle);
                            $scope.allUpcomingEvents.push(item);
                        });
                    }
                    if (res.completedEvents != null && angular.isDefined(res.completedEvents) && res.completedEvents.length > 0) {
                        angular.forEach(res.completedEvents, function (item) {
                            item.eventTitle = $rootScope.translateValue("EVNT_TITLE." + item.eventTitle);
                            $scope.allCompletedEvents.push(item);
                        });
                    }
                    if ($scope.allUpcomingEvents.length > 0) {
                        $scope.eventDetails = {
                            'employeeName': $scope.allUpcomingEvents[0].employeeName,
                            'employeeAddress': $scope.allUpcomingEvents[0].employeeAddress,
                            'employeeEmail': $scope.allUpcomingEvents[0].employeeEmail,
                            'employeePhoneNo': $scope.allUpcomingEvents[0].employeePhoneNo
                        };
                    }


                }, function () {
                    $rootScope.unMaskLoading();
                    $scope.eventResponse = true;
                });
            };
            $scope.initManageEvent();
            //To display banner image in popup
            $scope.bannerImagePopup = function (bannerImageName) {
                $scope.selectedBannerImageName = bannerImageName;
                $("#bannerImagePopup").modal("show");
            };
            $scope.galleryPopup = function (index) {
                var eventId = $scope.eventForGallery.id;
                $scope._Index = index;
                //view gallery methods 
                $scope.viewPhotos = [];
                if ($scope.viewPhotos.length === 0) {
                    Event.retrieveImagePaths(eventId, function (res) {
                        var data = res.src;
                        angular.forEach(data, function (name) {
                            $scope.viewPhotos.push({
                                src: $rootScope.appendAuthToken($rootScope.apipath + 'event/getimage?file_name=' + name)
                            });
                        });
                        $("#galleryPopup").modal("show");
                    });
                } else {
                    $("#galleryPopup").modal("show");
                }
                ;
            };
            $scope.hideSlidergalleryPopup = function () {
                $scope.viewPhotos = [];
                $scope.selection = [];
                $scope._Index = 0;
                $("#galleryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.hideRegDetialsOfUserCancel = function () {
                $("#registrationDetailsOfUserPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.hideMeetingDetailCancel = function () {
                $scope.viewMeetingDetailsPopup = false;
            };
//Init add event
            $scope.initAddEvent = function () {
                $scope.configForm = false;
                $scope.editCategory = false;
                $scope.previewConfigureForm = false;
                $scope.viewMeetingDetailsPopup = false;
                $scope.eventCategoryId = 'D';
                $scope.regFields = {noOfAdults: true, noOfChildren: true, relWithGuest: true, nameOfGuest: true};
                if (angular.isDefined($scope.eventCategoryListDropdown)) {
                    $scope.selectedEventCategoryDropdown.currentNode = $scope.eventCategoryListDropdown[0];
                }
                $scope.initInvitees();
                //To retrieve registration form names
                $scope.retrieveRegistrationFormNames();
                if (!$scope.editFlag) {
                    $scope.event = {};
                    $scope.event.invitees = "";
//                    $scope.event.strtTime = $rootScope.getCurrentServerDate()
//                    $scope.event.endTime = $rootScope.getCurrentServerDate()
                    $scope.event.registrationType = 'ONLN';
                    $scope.event.selectedRegForm = "";
                } else {//When edit event select default event category
                    if ($scope.event.status === 'Created') {
                        $scope.availableEventStatusTemp = [];
                        $scope.availableEventStatus = [];
                        $scope.availableEventStatusTemp = [{name: "Created"}, {name: "Published"}, {name: "Cancelled"}, {name: "Remove event"}];
                        if ($scope.availableEventStatusTemp != null && angular.isDefined($scope.availableEventStatusTemp) && $scope.availableEventStatusTemp.length > 0) {
                            angular.forEach($scope.availableEventStatusTemp, function (item) {
                                item.name = $rootScope.translateValue("EVENTS." + item.name);
                                $scope.availableEventStatus.push(item);
                            });
                        }
                    } else if ($scope.event.status === 'Upcoming') {
                        $scope.availableEventStatusTemp = [];
                        $scope.availableEventStatus = [];
                        $scope.availableEventStatusTemp = [{name: "Upcoming"}, {name: "Cancelled"}];
                        if ($scope.availableEventStatusTemp != null && angular.isDefined($scope.availableEventStatusTemp) && $scope.availableEventStatusTemp.length > 0) {
                            angular.forEach($scope.availableEventStatusTemp, function (item) {
                                item.name = $rootScope.translateValue("EVENTS." + item.name);
                                $scope.availableEventStatus.push(item);
                            });
                        }
                    } else if ($scope.event.status === 'Ongoing') {
                        $scope.availableEventStatusTemp = [];
                        $scope.availableEventStatus = [];
                        $scope.availableEventStatusTemp = [{name: "Ongoing"}, {name: "Cancelled"}];
                        if ($scope.availableEventStatusTemp != null && angular.isDefined($scope.availableEventStatusTemp) && $scope.availableEventStatusTemp.length > 0) {
                            angular.forEach($scope.availableEventStatusTemp, function (item) {
                                item.name = $rootScope.translateValue("EVENTS." + item.name);
                                $scope.availableEventStatus.push(item);
                            });
                        }
                    } else if ($scope.event.status === 'Cancelled') {
                        $scope.availableEventStatusTemp = [];
                        $scope.availableEventStatus = [];
                        $scope.availableEventStatusTemp = [{name: "Cancelled"}, {name: "Remove event"}];
                        if ($scope.availableEventStatusTemp != null && angular.isDefined($scope.availableEventStatusTemp) && $scope.availableEventStatusTemp.length > 0) {
                            angular.forEach($scope.availableEventStatusTemp, function (item) {
                                item.name = $rootScope.translateValue("EVENTS." + item.name);
                                $scope.availableEventStatus.push(item);
                            });
                        }
                    }
                    $scope.eventCategoryId = $scope.event.categoryDataBean.id;
                    $scope.selectedEventCategoryDropdown.currentNode = $scope.event.categoryDataBean;
                }

                $scope.submitted = false;
                $scope.categoryForm = {};
                $scope.bannerImg = {};
                $scope.backgroundImg = {};
                $scope.popover =
                        "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                        "\n\ " +
                        "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                        "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                        "<tr><td>'@F'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Franchise</td></tr>\ " +
                        "</table>\ ";
                //Register select2 selecting event on init add event
                $(document).on("select2-selecting", "#copyEventName", function (e) {
                    if (e.val !== "0") {
                        $scope.retrieveRegistrationFields(e.val);
                    } else {
                        $scope.event.registrationFieldsDataBean = $scope.configuredRegistrationFieldDataBean;
                    }
                });
                $scope.retrieveRegistrationFields = function (selectedEventToCopy) {
                    $rootScope.maskLoading();
                    Event.retrieveRegistrationFields(selectedEventToCopy, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.event.registrationFieldsDataBean = res;
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                };
            };
            $scope.initInvitees = function () {
                //For recipients,
                $scope.autoCompleteInvitees = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select invitees',
                    initSelection: function (element, callback) {
                        if ($scope.editFlag) {
                            var data = [];
                            angular.forEach($scope.recipients, function (recipient) {
                                data.push({
                                    id: recipient.recipientInstance + ":" + recipient.recipientType,
                                    text: recipient.recipientValue
                                });
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
                        if (selected.substring(0, 2) === '@F' || selected.substring(0, 2) === '@f') {
                            if (selected.substring(2, 3) === ':' && (selected.substring(3, 5) === '@D' || selected.substring(3, 5) === '@D')) {
                                var search = query.term.slice(5);
                                Messaging.retrieveDepartmentListOfOtherFranchise(search.trim(), success, failure);
                            }
                        } else if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                            var search = query.term.slice(2);
                            Messaging.retrieveUserList(search.trim(), success, failure);
                        } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
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
            };
            //When user clicks on item of event category dropdown
            $scope.popupEventCategoryDropdownClick = function (selectedCategory) {
                $scope.popupEventCategoryId = selectedCategory.currentNode.id;
            };
            //When user clicks on item of event category dropdown
            $scope.eventCategoryDropdownClick = function (selectedCategory, addEventForm) {
                $scope.eventCategoryId = selectedCategory.currentNode.id;
                if (selectedCategory !== null) {
                    $scope.addEventForm = addEventForm;
                    $scope.addEventForm.$dirty = true;
                }
            };
            //To display add category popup
            $scope.showAddCategoryPopup = function (addCategoryPopupForm) {
                $scope.showCategoryModal = true;
                $scope.category = {};
                addCategoryPopupForm.$setPristine();
                $scope.categoryForm.submitted = false;
                addCategoryPopupForm.categoryName.$setValidity("exists", true);
                $("#addCategoryPopup").modal("show");
            };
            //Init popup parent category due to caching issue of modal
            $scope.initPopupParentCategory = function () {
                $scope.selectedEventCategoryPopup.currentNode = $scope.eventCategoryListPopup[0];
            };
            //To hide add category popup
            $scope.hideAddCategoryPopup = function (addCategoryPopupForm) {
                $scope.showCategoryModal = false;
                $scope.category = {};
                $scope.addCategoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                addCategoryPopupForm.$setPristine();
                $('#addCategoryPopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            //To set configuration form flag
            $scope.setConfigFormFlag = function (value) {
                $scope.regformSubmitted = false;
                $scope.field = {};
                if (angular.isUndefined($scope.event.registrationFieldsDataBean) || $scope.event.registrationFieldsDataBean === null) {
                    $scope.event.registrationFieldsDataBean = [];
                }
                if (value) {
                    //Store data for cancel registration form
                    $scope.prevRegistrationFormName = angular.copy($scope.event.registrationFormName);
                    $scope.prevRegistrationFieldsDataBean = angular.copy($scope.event.registrationFieldsDataBean);
                    $scope.event.selectedEventToCopy = "";
                }

                $scope.configForm = value;
            };
            $scope.cancelRegForm = function (regForm) {
                //Restore registration form to previous state
                $scope.event.registrationFormName = $scope.prevRegistrationFormName;
                $scope.event.registrationFieldsDataBean = $scope.prevRegistrationFieldsDataBean;
                regForm.$setPristine();
                $scope.setConfigFormFlag(false);
            };
            //Init reg form names for select2
            $scope.regFormNames = {
                multiple: false,
                formatResult: function (item) {
                    return item.text;
                },
                data: function () {
                    return {'results': $scope.select2FormNames};
                }
            };
            //To retrieve registration form names
            $scope.retrieveRegistrationFormNames = function () {
                $rootScope.maskLoading();
                $scope.select2FormNames = [];
                Event.retrieveRegistrationFormNames(function (res) {
                    res.$promise = undefined;
                    res.$resolved = undefined;
                    angular.forEach(res, function (regForm, id) {
                        $scope.select2FormNames.push({id: id.toString(), text: regForm});
                    });
                    if ($scope.editFlag) {
                        $scope.event.selectedRegForm = $scope.event.id;
                    }
                    if (!$scope.editFlag) {
                        if ($scope.addEventForm !== undefined) {
                            $scope.addEventForm.$dirty = false;
                        }
                    }

                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
            $scope.setBackground = function () {
                if (!$scope.preview) {
                    $(".draggable").draggable({cursor: "move", opacity: 0.7, revert: 'invalid'});
                    $("#templateBackground").droppable({
                        drop: function (event, ui) {
                            var idDiv = $(ui.draggable).parent().attr("id");
                            if (idDiv != "templateBackground") {
                                $compile($("#templateBackground").append("<div class='draggable' style='display:\'inline-block\''>" + $(ui.draggable).html() + "</div>"))($scope);
                                $(ui.draggable).remove();
                                $(".draggable").draggable({cursor: "move", opacity: 0.7, revert: 'invalid'});
                            }
                        }
                    });
                    var drop = document.getElementById("drop");
                    drop.style.display = "block";
                    $("#drop").droppable({
                        hoverClass: "ui-state-active",
                        drop: function (event, ui) {
                            $compile($("#drop").append("<div class='draggable' style='display:\'inline-block\''>" + $(ui.draggable).html() + "</div>"))($scope);
                            $(ui.draggable).remove();
                            $(".draggable").draggable({cursor: "move", opacity: 0.7, revert: 'invalid'});
                        }
                    });
                }
                else {
                    var drop = document.getElementById("drop");
                    drop.style.display = "none";
                    var closebtn = document.getElementById("closebtn");
                    closebtn.style.display = "none";
                    var footer = document.getElementById("footer");
                    footer.style.display = "none";

                }
                if ($('#imgId').attr('src') !== undefined && $('#imgId').attr('src') !== '') {
                    $(document).find("#templateBackground").css("background", "url(" + $('#imgId').attr('src') + ")");
                    $(document).find("#templateBackground").css("background-size", "700px 400px");
                    $(document).find("#templateBackground").css("background-repeat", "no-repeat");
                }
                $scope.preview = false;
            };
            $scope.redirectToSearchedPage = function (list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchEventRecords = [];
                        $scope.setEventOperation("searchedEvent");
                    } else {
                        $scope.searchEventRecords = angular.copy(list);
                        $scope.setEventOperation("searchedEvent");
                        for (var i = 0; i < $scope.searchEventRecords.length; i++) {
                            $scope.startDate = $scope.searchEventRecords[i].fromDate;
                            $scope.startDates = (new Date($scope.startDate));
                        }
                    }
                }

            };

            $scope.removeFromPage = function () {
                $scope.imageName = null;
                $scope.event.bannerImageName = null;
            };

            $scope.removeFromInvitationPage = function () {
                $scope.invitationImageName = null;
                FileUploadService.removeImageFile($scope.event.invitationTemplateName, function () {
                    $scope.event.invitationTemplateName = undefined;
                });
                $(document).find("#templateBackground").css("background", "url('')");
            };

            $scope.editEvent = function (eventId) {
                $rootScope.maskLoading();
                Event.retrieveEventById({primaryKey: eventId}, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.event = res;
                    if ($scope.event.fromDate !== null || $scope.event.fromDate !== undefined) {
                        $scope.event.fromDate = new Date($scope.event.fromDate);
                    }
                    if ($scope.event.toDate !== null || $scope.event.toDate) {
                        $scope.event.toDate = new Date($scope.event.toDate);
                    }
                    if ($scope.event.publishedOn !== null || $scope.event.publishedOn) {
                        $scope.event.publishedOn = new Date($scope.event.publishedOn);
                    }
                    if ($scope.event.registrationLastDate !== null && $scope.event.registrationLastDate) {
                        $scope.event.registrationLastDate = new Date($scope.event.registrationLastDate);
                    }
                    if ($scope.event.bannerImageName !== null && $scope.event.bannerImageName !== undefined) {
                        $scope.imageName = $scope.event.bannerImageName.split("~~")[1];
                    }
                    if ($scope.event.invitationTemplateName !== null && $scope.event.invitationTemplateName !== undefined) {
                        $scope.invitationImageName = $scope.event.invitationTemplateName.split("~~")[1];
                    }
                    if (angular.isDefined(res.eventCustom) && res.eventCustom != null) {
                        $scope.addEventData = angular.copy(res.eventCustom);
                    }
                    if (angular.isDefined(res.regCustom) && res.regCustom != null) {
                        $scope.addRegFormData = angular.copy(res.regCustom);
                    }
                    if (angular.isDefined(res.invitationCustom) && res.invitationCustom != null) {
                        $scope.addInvitationData = angular.copy(res.invitationCustom);
                    }
                    //Store eventPrevStatus for restoring when user cancels on removeEvent Popup
                    $scope.eventPrevStatus = $scope.event.status;
                    $scope.recipients = res.eventRecipientDataBeanList;
                    $(document).find($("#invitees")).select2("val", []);
                    if ($scope.event.fromDate !== undefined) {
                        $scope.minEventFromDate = new Date($scope.event.fromDate);
                    }
                    if ($scope.event.publishedOn !== undefined) {
                        $scope.minEventPublisedDate = new Date($scope.event.publishedOn);
                    }
                    $scope.setEventOperation($scope.EDIT_EVENT);
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
//          To save uploaded files and event
            $scope.saveFilesAndEvent = function (addEventForm) {
                $scope.submitted = true;
                $rootScope.maskLoading();
                if (addEventForm.$valid && $scope.eventCategoryId !== 'D' && !$scope.invalidTime) {
                    $scope.saveEventFlag = true;
                    if (angular.isDefined($scope.backgroundFlow) && $scope.backgroundFlow.files.length > 0) {
                        $scope.saveEventFlag = false;
                        if ($scope.backgroundUploaded) {
                            $scope.saveEventFlag = true;
                        }
                    }
                    if (angular.isDefined($scope.bannerFlow) && $scope.bannerFlow.files.length > 0) {
                        $scope.saveEventFlag = false;
                        if ($scope.bannerUploaded) {
                            $scope.saveEventFlag = true;
                        }
                    }
                    if ($scope.saveEventFlag) {
                        $scope.saveEvent(addEventForm);
                    }
                }
                $rootScope.unMaskLoading();
            };
            //To add or edit event
            $scope.saveEvent = function (addEventForm) {
                if ($scope.event.registrationType === 'ONLN') {
                    if ($scope.event.selectedRegForm.id === undefined) {
                        if ($scope.event.selectedRegForm !== "0") {
                            $scope.event.registrationFormEventId = $scope.event.selectedRegForm;
                        }
                    } else {
                        if ($scope.event.selectedRegForm.id !== "0") {
                            $scope.event.registrationFormEventId = $scope.event.selectedRegForm.id;
                        }
                    }
                }
                $scope.eventToSave = angular.copy($scope.event);
                $scope.eventToSave.eventCustom = $scope.addEventData;
                $scope.eventToSave.eventDbType = $scope.dbType;
                $scope.eventToSave.regCustom = $scope.addRegFormData;
                $scope.eventToSave.regDbType = $scope.dbType1;
                $scope.eventToSave.invitationCustom = $scope.addInvitationData;
                $scope.eventToSave.invitationDbType = $scope.dbType2;
                $scope.clearImageOfInvitationTemplate();
                if ($scope.eventToSave.invitationTemplateName === undefined && !$scope.previewConfigureForm && !$scope.editFlag) {
                    $scope.eventToSave.invitationTemplateName = 'ND';
                } else if ($scope.eventToSave.invitationTemplateName === null && !$scope.previewConfigureForm && $scope.editFlag) {
                    $scope.eventToSave.invitationTemplateName = 'ND';
                }
                $scope.setModelValues();
                if (!$scope.editFlag) {
                    $scope.createEvent(addEventForm);
                } else {
//                    if ($scope.eventToSave.registrationType === 'OFLN') {
//                        $scope.eventToSave.registrationFormName = undefined;
//                        $scope.eventToSave.registrationFormEventId = undefined;
//                        $scope.eventToSave.registrationLastDate = undefined;
//                        $scope.eventToSave.registrationFieldsDataBean = undefined;
//                    }
                    if ($scope.eventToSave.status === 'Published') {
                        $scope.eventToSave.status = 'Upcoming';
                        $scope.eventToSave.publishedOn = $rootScope.getCurrentServerDate().setHours(0, 0, 0, 0);
                    }
                    if ($scope.eventToSave.status === 'Remove event') {
                        $("#removeEventPopup").modal("show");
                    } else if ($scope.eventToSave.status === 'Cancelled') {
                        $("#cancelEventPopup").modal("show");
                    } else {
                        $scope.updateEvent(addEventForm);
                    }
                }
                $scope.preview = true;
            };
//To create new event
            $scope.createEvent = function (addEventForm) {
                $rootScope.maskLoading();
                $scope.eventToSave.token = $rootScope.authToken;
                Event.createEvent($scope.eventToSave, function (res) {
                    addEventForm.$setPristine();
                    $scope.preview = false;
                    $rootScope.unMaskLoading();
                    $scope.initManageEvent();
                }, function () {
                    $rootScope.addMessage("Could not save details, please try again.", 1);
                    $scope.resetImageOfInvitationTemplate();
                    $rootScope.unMaskLoading();
                });
            };
            //To update already created event
            $scope.updateEvent = function (addEventForm) {
                $rootScope.maskLoading();
                $scope.eventToSave.token = $rootScope.authToken;
                Event.updateEvent($scope.eventToSave, function (res) {
                    addEventForm.$setPristine();
                    $scope.preview = false;
                    $rootScope.unMaskLoading();
                    $scope.initManageEvent();
                }, function () {
                    $rootScope.addMessage("Event could not be updated. Try again.", 1);
                    $scope.resetImageOfInvitationTemplate();
                    $rootScope.unMaskLoading();
                });
            };
            $scope.removeEventOk = function (addEventForm) {
                $("#removeEventPopup").modal("hide");
                $('.modal-backdrop').remove();
                $rootScope.removeModalOpenCssAfterModalHide();
                $rootScope.maskLoading();
                //Remove event
                Event.removeEvent({primaryKey: $scope.eventToSave.id}, function (res) {
                    addEventForm.$setPristine();
                    $rootScope.unMaskLoading();
                    $scope.initManageEvent();
                }, function () {
                    $rootScope.addMessage("Event could not be removed. Try again.", 1);
                    $rootScope.unMaskLoading();
                });
            };

            $scope.cancelEventOk = function (addEventForm) {
                $("#cancelEventPopup").modal("hide");
                $('.modal-backdrop').remove();
                $rootScope.removeModalOpenCssAfterModalHide();
                $rootScope.maskLoading();
                //Remove event
                $scope.updateEvent(addEventForm);
            };


//When user press cancel in removeevent popup
            $scope.removeEventCancel = function () {
                $scope.event.status = $scope.eventPrevStatus;
                $("#removeEventPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.cancelEventCancel = function () {
                $scope.event.status = $scope.eventPrevStatus;
                $("#cancelEventPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            //Clear image of invitation template before save
            $scope.clearImageOfInvitationTemplate = function () {
                $scope.imgDiv = $('#templateImg').html();
                if ($("#imgId").attr("src") !== undefined && $('#imgId').attr('src') !== '') {
                    $(document).find("#templateBackground").css("background", "");
                    $('#imgId').attr("src", "");
                }
            };
//Reset image of invitation template in case of failure
            $scope.resetImageOfInvitationTemplate = function () {
                $(document).find("#templateImg").html($scope.imgDiv);
            };
//Set model values before saving object
            $scope.setModelValues = function () {

                $scope.eventToSave.templateHtml = $("#templateModal").html();
//                $scope.eventToSave.category = $scope.selectedEventCategoryDropdown.currentNode.id;
                $scope.eventToSave.category = $scope.eventCategoryId;
                $scope.eventToSave.eventRecipientDataBeanList = [];
                //if ofline registration selected
//                if ($scope.eventToSave.registrationType === 'OFLN') {
////                    $scope.eventToSave.registrationFormName = undefined;
//                    $scope.eventToSave.registrationLastDate = undefined;
//                }
//                if ($scope.eventToSave.registrationFormName === undefined) {
//                    $scope.eventToSave.
//                }

                if ($scope.editFlag) {
                    $scope.eventToSave.invitees = $("#invitees").select2("val").toString();
                }
                if ($scope.eventToSave.invitees.length > 0) {
                    var invitees = $scope.eventToSave.invitees.split(",");
                    for (var i = 0; i < invitees.length; i++) {
                        var recipient = invitees[i].split(":");
                        $scope.eventToSave.eventRecipientDataBeanList.push({recipientInstance: recipient[0], recipientType: recipient[1]});
                    }
                }
                $scope.eventToSave.selectedRegForm = undefined;
                $scope.eventToSave.selectedEventToCopy = undefined;
                $scope.eventToSave.invitees = undefined;
            };
            //Set event end time equal to start time while chaging event start time
            $scope.eventStartTimeChange = function () {
//                $scope.event.endTime = $scope.event.strtTime;
            };
            //Set event endtime to event start time while eventEndTime<eventStartTime
            $scope.eventEndTimeChange = function () {
//                if ($scope.event.endTime < $scope.event.strtTime) {
//                    $scope.event.endTime = $scope.event.strtTime;
//                }

            };
//            $scope.checkEventTime = function () {
//                $scope.invalidTime = false;
//                var startTime, endTime;
//                if ($scope.event.strtTime) {
//                    startTime = new Date($scope.event.strtTime).getTime();
//                    if (startTime <= new Date().getTime()) {
//                        $scope.invalidTime = true;
//                    }
//                }
//                if ($scope.event.endTime) {
//                    endTime = new Date($scope.event.endTime).getTime();
//                    if (endTime <= new Date().getTime()) {
//                        $scope.invalidTime = true;
//                    }
//                }
//                if (startTime && endTime) {
//                    var tempStartTime = new Date($scope.event.strtTime);
//                    var tempEndTime = new Date($scope.event.endTime);
//                    if ((tempStartTime.getHours() === tempEndTime.getHours()) && (tempStartTime.getMinutes() === tempEndTime.getMinutes())) {
//                        $scope.invalidTime = true;
//                    } else if (endTime <= startTime) {
//                        $scope.invalidTime = true;
//                    }
//                }
//            };
//            
            $scope.checkEventTime = function () {
                $scope.invalidTime = false;
                var startTime, endTime, startDate;
                if ($scope.event.fromDate) {
                    startDate = new Date($scope.event.fromDate);
                } else {
                    startDate = new Date();
                }
                if (startDate.getTime() <= new Date().getTime()) {
                    //handling today or previous day.
                    if ($scope.event.strtTime) {
//                        console.log("start defined::::");
                        var startDateTime = new Date($scope.event.strtTime);
                        if (startDateTime.getHours() < new Date().getHours()) {
//                            console.log("less start define ::::");
                            $scope.invalidTime = true;
                        } else if (startDateTime.getHours() === new Date().getHours() && startDateTime.getMinutes() <= new Date().getMinutes()) {
                            $scope.invalidTime = true;
                        }
                    }
                    if ($scope.event.endTime) {
                        var endDateTime = new Date($scope.event.endTime);
                        if (endDateTime.getHours() < new Date().getHours()) {
                            $scope.invalidTime = true;
                        } else if (endDateTime.getHours() === new Date().getHours() && endDateTime.getMinutes() <= new Date().getMinutes()) {
                            $scope.invalidTime = true;
                        } else if ($scope.event.strtTime) {
                            var startDateTime = new Date($scope.event.strtTime);
                            if (endDateTime.getHours() < startDateTime.getHours()) {
                                $scope.invalidTime = true;
                            } else if (endDateTime.getHours() === startDateTime.getHours() && endDateTime.getMinutes() <= startDateTime.getMinutes()) {
                                $scope.invalidTime = true;
                            }
                        }
                    }
                } else {
//                    console.log("future handling");
                    //handling for future date
                    if ($scope.event.strtTime && $scope.event.endTime) {
                        var startDateTime = new Date($scope.event.strtTime);
                        var endDateTime = new Date($scope.event.endTime);
                        if (endDateTime.getHours() < startDateTime.getHours()) {
                            $scope.invalidTime = true;
                        } else if (endDateTime.getHours() === startDateTime.getHours() && endDateTime.getMinutes() <= startDateTime.getMinutes()) {
                            $scope.invalidTime = true;
                        }
                    }
                }
//                }
            };
            //When user click on category
            $scope.setEventCategory = function () {
//                if(angular.isDefined($scope.addEventForm)){
//                    $scope.addEventForm.$setPristine();
//                }
                $rootScope.maskLoading();
                if (angular.isDefined($scope.selectedEventCategory.currentNode)) {
                    $scope.setEventOperation($scope.MANAGE_EVENT);
                    $scope.editCategory = true;
                    $scope.retrieveEvents($scope.selectedEventCategory.currentNode.id);
                    //Declare dropdown variable for editeventcategory
                    if (angular.isUndefined($scope.editEventCategoryDropdown)) {
                        $scope.editEventCategoryDropdown = {};
                    }
                    $scope.parentEventCategory = {};
                    $scope.selectedCategory = angular.copy($scope.selectedEventCategory.currentNode);
                    $scope.parentId = $scope.selectedCategory.parentId;
                    $scope.parentEventCategory.id = $scope.selectedCategory.parentId;
                    //To display in header
                    $scope.parentCategoryHeader = $scope.selectedCategory.parentName;
                    $scope.childCategoryHeader = $scope.selectedCategory.displayName;
                    $scope.parentEventCategory.displayName = $scope.selectedCategory.parentName;
//                    $scope.editCategoryEventCategoryListDropdown[0].selected = undefined;
                    $scope.category = {};
                    $scope.categoryForm = {};
                    $scope.category.id = $scope.selectedCategory.id;
                    $scope.category.displayName = $scope.selectedCategory.displayName;
                    $scope.category.status = $scope.selectedCategory.status;
                    Event.addCustomDataToCategoryDataBean($scope.category, function (res) {
                        $scope.category.categoryCustomData = res.categoryCustomData;
                        $scope.category = res;
                        $scope.addCategoryData = $scope.category.categoryCustomData;
                        $scope.editCategoryEventCategoryListDropdown = angular.copy($scope.eventCategories);
                        $scope.deleteSelectedNode($scope.editCategoryEventCategoryListDropdown);
                        $rootScope.unMaskLoading();
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                }
            };
            //To delete selected node from list of parent category
            $scope.deleteSelectedNode = function (node) {
                if (node === null || node === undefined) {
                    return;
                }
                for (var i = 0; i < node.length; i++) {
                    if ($scope.selectedEventCategory.currentNode.id === node[i].id) {
                        node.splice(i, 1);
                    }
                    else {
                        for (var j = 0; j < node.length; j++) {
                            if (node[j].children !== null) {
                                $scope.deleteSelectedNode(node[j].children);
                            }
                        }
                    }
                }
                $scope.editCategoryEventCategoryListDropdown = angular.copy(node);
            };

            $scope.editCategoryDropdownClick = function () {
                if (angular.isDefined($scope.editEventCategoryDropdown.currentNode)) {
                    $scope.parentId = $scope.editEventCategoryDropdown.currentNode.id;
                    $scope.parentEventCategory.displayName = $scope.editEventCategoryDropdown.currentNode.displayName;
                }
            };
            //Check event category already exist
            $scope.checkCategoryUniqueName = function (addCategoryPopupForm) {
                Event.doesCategoryNameExist($scope.category, function (res) {
                    if (res.messages === null) {
                        addCategoryPopupForm.categoryName.$setValidity('exists', true);
                    } else {
                        $scope.categoryExistsMsg = res.messages[0].message;
                        addCategoryPopupForm.categoryName.$setValidity('exists', false);
                    }
                }, function () {
                });
            };
            //Check event name already exist
            $scope.checkEventUniqueName = function (addEventForm) {
                Event.doesEventNameExist({id: $scope.event.id, eventTitle: $scope.event.eventTitle}, function (res) {
                    if (res.messages === null) {
                        addEventForm.eventTitle.$setValidity('exists', true);
                    } else {
                        $scope.eventExistsMsg = res.messages[0].message;
                        addEventForm.eventTitle.$setValidity('exists', false);
                    }
                }, function () {
                });
            };
            //To create or save eventcategory
            $scope.saveEventCategory = function (addCategoryPopupForm) {
                $scope.categoryForm.submitted = true;
                if (addCategoryPopupForm.$valid) {
                    $rootScope.maskLoading();
                    var count = 0;
                    $scope.categoryToSave = angular.copy($scope.category);
                    $scope.categoryToSave.categoryCustomData = $scope.addCategoryData;
                    $scope.categoryToSave.dbTypeForCategory = $scope.dbTypeForCategory;
                    if (!$scope.editCategory) {
                        if ($scope.popupEventCategoryId !== 'D') {
                            $scope.categoryToSave.parentId = $scope.popupEventCategoryId;
                        }
                        $scope.createEventCategory(addCategoryPopupForm);
                    } else {
                        if ($scope.parentId !== 'D') {
                            $scope.categoryToSave.parentId = $scope.parentId;
                        }
                        if ($scope.categoryToSave.status === 'Remove') {
                            $scope.categoryName = $scope.categoryToSave.displayName;
                            if ($scope.allUpcomingEvents.length > 0) {
                                $rootScope.addMessage("Category is not empty. Remove events and try again.", 1);
                                $rootScope.unMaskLoading();
                            } else {
                                angular.forEach($scope.existingEventCategoryList, function (item) {
//                                    console.log("item.children.id---"+item.children.id);
                                    if ($scope.categoryToSave.id === item.id) {
                                        count++;
                                        if (item.children !== null && item.children.length > 0) {
                                            $rootScope.addMessage("Category is not empty. Remove child categories and try again.", 1);
                                            $rootScope.unMaskLoading();
                                        } else {
                                            $("#removeEventCategoryPopup").modal("show");
                                            $rootScope.unMaskLoading();
                                        }
                                    }
                                });
                                if (count > 0) {
                                } else {
                                    $("#removeEventCategoryPopup").modal("show");
                                    $rootScope.unMaskLoading();
                                }
                            }
                        } else {
                            $scope.editEventCategory(addCategoryPopupForm);
                        }
                    }
                    $rootScope.unMaskLoading();
                }
            };
            //To remove category
            $scope.removeAndSaveCategory = function (editCategoryForm) {
                $("#removeEventCategoryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.editEventCategory(editCategoryForm);
            };
//To hide category remove popup
            $scope.removeCategoryCancel = function () {
                $scope.category.status = "Active";
                $("#removeEventCategoryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
//To create new category
            $scope.createEventCategory = function (addCategoryPopupForm) {
                Event.createEventCategory($scope.categoryToSave, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.retriveEventCategories();
                    $scope.retrieveActiveEventCategories();
                    $scope.hideAddCategoryPopup(addCategoryPopupForm);
                }, function () {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Could not save details, please try again.", 1);
                });
            };
//To edit event category            
            $scope.editEventCategory = function (editCategoryForm) {
                Event.updateEventCategory($scope.categoryToSave, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.initManageEvent();
                    $('.modal-backdrop').remove();
                }, function () {
                    $rootScope.unMaskLoading();
                    if ($scope.categoryToSave.status === 'Remove') {
                        $rootScope.addMessage("Event category could not be removed. Try again.", 1);
                    } else {
                        $rootScope.addMessage("Could not save details, please try again.", 1);
                    }
                });
            };
            //For file upload
            $scope.uploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: true,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Event'
                }
            };
            //Banner file upload success
            $scope.bannerFileUploaded = function (file, flow, addEventForm, response) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "banner";
                $scope.bannerUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                var modelName = 'Event';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "true";
                var info;
                Event.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;
                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function (response) {
                        $scope.event.bannerImageName = response.res;
                        $scope.imageName = null;
                    });
                });
            };

            $scope.removeFromList = function ($flow, index) {
                $flow.files.splice(index, 1);
                $scope.event.bannerImageName = null;
            };
            $scope.removeBackground = function ($flow, index) {
                $flow.files.splice(index, 1);
                $flow.cancel();
                FileUploadService.removeImageFile($scope.event.invitationTemplateName, function () {
                    $scope.event.invitationTemplateName = undefined;
                });
                $(document).find("#templateBackground").css("background", "url('')");
            };
            $scope.getTemplateName = function () {
                return $rootScope.apipath + "event/getbackgroundimage?file_name=" + $scope.event.invitationTemplatePath;
            };
            //Background file upload success
            $scope.backgroundFileUploaded = function (file, flow, addEventForm, response) {
                $rootScope.maskLoading();
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "background";
                $scope.backgroundUploaded = true;
                $timeout(function () {
                    $scope.showInvitationCardPopup();
                    $scope.previewForm = true;
                    $scope.setBackground();
                });
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                var modelName = 'Event';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "true";
                var info;
                Event.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;
                    info = [file.name, modelName, filenameformat, thumbnail, 700, 400];
                    FileUploadService.uploadFiles(info, function (response) {
                        $rootScope.unMaskLoading();
                        $scope.event.invitationTemplateName = response.res;
                        $scope.invitationImageName = null;
                    });
                });
//                $scope.saveEvent(addEventForm);
            };
            //For Banner file 
            $scope.bannerFileAdded = function (file, flow) {
                $scope.bannerFlow = flow;
                $scope.uploadFile.query.fileType = "banner";
                $scope.bannerUploaded = false;
                if ((file.getExtension() !== "jpg") && (file.getExtension() !== "jpeg") && (file.getExtension() !== "png") && (file.getExtension() !== "gif") && (file.getExtension() !== "bmp")) {
                    $scope.bannerImg.invalidFileFlag = true;
                    $scope.bannerImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 10485760) {
                        $scope.bannerImg.invalidFileSizeFlag = true;
                        return false;
                    }
                }
                return !!{
                    jpg: 1,
                    jpeg: 1,
                    gif: 1,
                    png: 1,
                    bmp: 1
                }
                [file.getExtension()];
            };
            //For Background file 
            $scope.backgroundFileAdded = function (file, flow) {
                $scope.backgroundFlow = flow;
                $scope.backgroundUploaded = false;
                $scope.uploadFile.query.fileType = "background";
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")) {
                    $scope.backgroundImg.invalidFileFlag = true;
                    $scope.backgroundImg.fileName = file.name;
                } else {
                    //Check file size greater than 5 MB
                    if (file.size > 5242880) {
                        $scope.backgroundImg.invalidFileSizeFlag = true;
                        return false;
                    }
                }
                return !!{
                    jpg: 1,
                    jpeg: 1,
                    gif: 1,
                    png: 1
                }
                [file.getExtension()];
            };
//          For gallery file upload
            $scope.galleryUploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: false,
                testChunks: false,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Event'
                }

            };
            //For gallery file 
            $scope.galleryFileAdded = function (file, flow) {
                $scope.galleryFlow = flow;
                $scope.galleryUploadFile.query.fileType = "gallery";
                var maxsize = 5000000;
                var filesize = file.size;
                if ((file.getExtension() !== "jpg") && (file.getExtension() !== "jpeg") && (file.getExtension() !== "png") && (file.getExtension() !== "gif")) {
                    $scope.validFileFlag = true;
                    $scope.fileNames.push(file.name);
                    alert('Only images are supported');
                    return false;
                } else {
                    if (maxsize < filesize) {
                        $scope.validFileFlag = true;
                        $scope.fileNames.push(file.name);
                        alert('You can upload a file upto 5 MB ');
                        return false;
                    }
                    return true;
                }
            };
            // //gallery file upload success
            $scope.galleryFileUploaded = function (file, flow, response) {

                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "gallery";
                var modelName = 'Event';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "false";
                var info;

                Event.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;
                    info = [file.name, modelName, filenameformat, thumbnail];

                    FileUploadService.uploadFiles(info, function (response) {

                        $scope.eventToSaveGallery = $rootScope.eventToSaveGallery;
                        file.msg = response.res;
                        $scope.eventToSaveGallery.fileList.push(response.res);
                    });
                });
            };
            $scope.removeUploadedImage = function (file, msg) {
                var index = $scope.eventToSaveGallery.fileList.indexOf(msg);
                if (index > -1) {
                    $scope.eventToSaveGallery.fileList.splice(index, 1);
                }
                file.cancel();
            };
            //initializing gallery from manange event page
            $scope.createPhotoGallery = function (event) {
                $rootScope.eventToSaveGallery = event;
                $rootScope.eventToSaveGallery.fileList = [];
            };
            //save gallery files in folder as well as database if not updated
            $scope.submitFilesAndSaveGallery = function (flow) {
                $rootScope.maskLoading();
                if (flow.files.length > 0) {
                    Event.createPhotoGallery($scope.eventToSaveGallery, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.displayGallery($scope.eventToSaveGallery.id);
                        $scope.eventToSaveGallery = {};
                        flow.cancel();
                        $scope.hideGalleryPopup();
                        $scope.retrieveEvents("null");
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                } else {
                    $rootScope.addMessage("Select Photos for the album.", 1);
                    $scope.hideGalleryPopup();
                    $scope.eventToSaveGallery = {};
                    flow.cancel();
                    $rootScope.unMaskLoading();
                }
            };
            $scope.cancelGallery = function (flow) {
                $rootScope.maskLoading();
                $scope.hideGalleryPopup();
                $scope.eventToSaveGallery = {};
                flow.cancel();
                $rootScope.unMaskLoading();
            };
            $scope.closeGalleryDisplay = function () {
                $scope.eventForGallery = {};
                $scope.initManageEvent();
            };
            $scope.toggleSelection = function toggleSelection(imageName) {
                var idx = $scope.selection.indexOf(imageName);
                // is currently selected
                if (idx > -1) {
                    $scope.selection.splice(idx, 1);
                }

                // is newly selected
                else {
                    $scope.selection.push(imageName);
                }
            };
            $scope.deletePhotos = function () {
                for (var i = 0; i < $scope.selection.length; i++) {
                    FileUploadService.removeImageFile($scope.selection[i], function () {
                    });
                }
                $scope.selection = [];
                $scope.displayGallery($scope.eventForGallery.id);
            };
            //Set invitation card model values
            $scope.initInvtitationCard = function () {
                $scope.invitationCard = {eventTitle: $scope.event.eventTitle,
                    eventDescription: $scope.event.description,
                    address: $scope.event.address,
                    fromDate: $scope.event.fromDate,
                    strtTime: $scope.event.strtTime,
                    contentColor: $scope.event.contentColor,
                    labelColor: $scope.event.labelColor,
                    employeeName: "$Employee Name",
                    employeeAddress: "$Employee Address Line 1\n$Employee Address Line 2",
                    employeeEmail: "$Employee Email",
                    employeePhoneNo: "$Employee Phone No.",
                    totalAdults: "$Total adults accompaning",
                    totalChildren: "$Total children accompaning"
                };
            };
            //To display invitation card preview
            $scope.showInvitationCardPopup = function () {
                $scope.previewConfigureForm = true;
                $scope.preview = false;
                $templateCache.remove($rootScope.apipath + "event/getbackgroundimage?file_name=" + $scope.event.invitationTemplatePath);
                $scope.initInvtitationCard();
                $("#invitationCardPopup").modal("show");
            };
            $scope.showPreviewPopup = function (event) {
                $scope.event = event;
                $scope.previewForm = true;
                $scope.preview = true;
                $templateCache.remove($rootScope.apipath + "event/getbackgroundimage?file_name=" + $scope.event.invitationTemplatePath);
                $scope.initInvtitationCard();
                $scope.invitationCard.employeeName = $scope.eventDetails.employeeName;
                $scope.invitationCard.employeeAddress = $scope.eventDetails.employeeAddress;
                $scope.invitationCard.employeeEmail = $scope.eventDetails.employeeEmail;
                $scope.invitationCard.employeePhoneNo = $scope.eventDetails.employeePhoneNo;
                $scope.invitationCard.totalAdults = $scope.event.adultCount;
                $scope.invitationCard.totalChildren = $scope.event.childCount;
                $scope.eventsId = $scope.event.id;
                $("#invitationCardPopup").modal("show");
            };
            ;
            //To hide invitation card preview
            $scope.hideInvitationCardPopup = function () {
                $("#invitationCardPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            //To display gallery popup
            $scope.showGalleryPopup = function () {
                $("#addGalleryPopup").modal("show");
            };
            $scope.viewPhotoGallery = function (event) {
                $scope.eventForGallery = angular.copy(event);
                $scope.setEventOperation($scope.VIEW_PHOTO_GALLERY);
                $scope.displayGallery($scope.eventForGallery.id);
            };
            //To hide gallery popup
            $scope.hideGalleryPopup = function () {
                $("#addGalleryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            //Configure registration form methods
            //Check unique label names 
            $scope.checkUniqueLabelName = function (fieldLabel, fieldForm, indexToSkip) {
                if ($scope.findFieldLabel(fieldLabel, indexToSkip)) {
                    fieldForm.fieldLabel.$setValidity("exists", false);
                }
                ;
            };
//To add new field
            $scope.addNewField = function (addNewField) {
                $scope.newFieldFormSubmitted = true;
                if (addNewField.$valid) {
                    $scope.fieldToAdd = angular.copy($scope.field);
                    $scope.event.registrationFieldsDataBean.push($scope.fieldToAdd);
                    $scope.newFieldFormSubmitted = false;
                    $scope.field = {};
                    addNewField.$setPristine();
                }
            };
            //To remove field
            $scope.removeField = function (index) {
                $scope.event.registrationFieldsDataBean.splice(index, 1);
            };
            //To find field name to check its already exist
            $scope.findFieldLabel = function (fieldName, indexToSkip) {
                if (fieldName !== undefined) {
                    for (var i = 0; i < $scope.event.registrationFieldsDataBean.length; i++) {
                        if (i !== indexToSkip && $scope.event.registrationFieldsDataBean[i].fieldName.toLowerCase() === fieldName.toLowerCase()) {
                            return true;
                        }
                    }
                }
                return false;
            };
            //To save registratio form
            $scope.saveRegistrationForm = function (regForm) {
                $scope.regformSubmitted = true;
                //Check validity of form
                if (regForm.configureRegForm.$valid && (regForm.addedFieldForm === undefined || regForm.addedFieldForm.$valid)) {
                    if ($scope.select2FormNames[0].id !== "0") {
                        $scope.select2FormNames.unshift({id: "0", text: $scope.event.registrationFormName});
                    } else {
                        $scope.select2FormNames[0] = {id: "0", text: $scope.event.registrationFormName};
                    }
                    $rootScope.addMessage("Registration form configured.", 0);
                    $scope.setConfigFormFlag(false);
                    $scope.event.selectedRegForm = "0";
                    $scope.configuredRegistrationFieldDataBean = $scope.event.registrationFieldsDataBean;
                    $scope.event.registrationFormUpdated = true;
                }
            };
            //Date picker options and function
            $scope.minEventFromDate = $rootScope.getCurrentServerDate();
            $scope.minEventPublisedDate = $rootScope.getCurrentServerDate();
            $scope.setEditEventMinFromDate = function () {
                $scope.minEventFromDate = $rootScope.getCurrentServerDate();
            };
            $scope.setEditEventMinPublishedOnDate = function () {
                $scope.minEventPublisedDate = $rootScope.getCurrentServerDate();
            };
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
            // initial image index
            $scope._Index = 0;
            // if a current image is the same as requested image
            $scope.isActive = function (index) {
                return $scope._Index === index;
            };
            // show prev image
            $scope.showPrev = function () {
                $scope._Index = ($scope._Index > 0) ? --$scope._Index : $scope.viewPhotos.length - 1;
            };
            // show next image
            $scope.showNext = function () {
                $scope._Index = ($scope._Index < $scope.viewPhotos.length - 1) ? ++$scope._Index : 0;
            };
            // show a certain image
            $scope.showPhoto = function (index) {
                $scope._Index = index;
            };
            $scope.initpop = function () {
                $("body").keydown(function (e) {
                    if (e.keyCode == 37) { // left
                        alert("left");
                    }
                    else if (e.keyCode == 39) { // right
                        alert("right");
                    }
                });
            }
            $scope.viewlist = true;
            $scope.reg = [];
//            $scope.getThumbnailPath = function(path) {
//                var dirPath = path.substring(0, path.lastIndexOf("."));
//                dirPath += "_T.jpg";
//                return dirPath;
//            };

            $scope.viewRegistrationDetails = function (event) {
                if (event.registrationType !== 'OFLN') {
                    $scope.viewMeetingDetailsPopup = false;
                    if (event.status === 'Upcoming' || event.status === 'Ongoing') {
                        $scope.viewRegistrationDetail = true;
                        $scope.setEventOperation($scope.REGISTER_EVENT);
                        var eventId = event.id;
                        $scope.retrieveUserRegistration(eventId);
                        $scope.selectedEventForRegistration = event;
                    }
                }
            };
            $scope.registerForEvent = function (event, editRegistrationFlag) {
                $scope.viewRegistrationDetail = false;
                $scope.editRegistrationFlag = editRegistrationFlag;
                $scope.setEventOperation($scope.REGISTER_EVENT);
                Event.retrieveRegistrationFields(event.id, function (res) {
                    event.registrationFieldsDataBean = res;
                    $scope.selectedEventForRegistration = event;
                    Event.retrieveCustomValues(event.id, function (res) {
                        if (res !== undefined && res !== null && res.length > 0) {
                            $scope.selectedEventForRegistration.registrationFieldsDataBean = res;
                        }

                    }, function (res) {
                    });
                }, function () {
                });
                if (event.registrationStatus !== undefined) {
                    $scope.reg.adults = event.userAdultCount;
                    $scope.reg.notAttending = !event.registrationStatus;
                    $scope.reg.reason = event.reason;
                    $scope.reg.children = event.userChildCount;
                    if (event.guests === null) {
                        $scope.guests = [];
                    } else {
                        $scope.guests = event.guests;
                    }
                } else {
                    $scope.adults = undefined;
                    $scope.children = undefined;
                    $scope.guests = [];
                }
            };

            $scope.openRegDetails = function (event, attendee) {
                if ((attendee.adultCount !== undefined && attendee.adultCount !== 0) || (attendee.childCount !== undefined && attendee.childCount !== 0)) {
                    $scope.registrationDetailsForUser(event, attendee, true);
                    $("#registrationDetailsOfUserPopup").modal("show");
                } else {
                    $rootScope.addMessage("User have not registered yet.", 1);
                }
            };

            $scope.registrationDetailsForUser = function (event, attendee, editRegistrationFlag) {
                $scope.editRegistrationFlag = editRegistrationFlag;
                Event.retrieveRegistrationFields(event.id, function (res) {
                    event.registrationFieldsDataBean = res;
                }, function () {
                });
                $scope.regDetails = event;
                $scope.regDtls = [];
                $scope.regDtls.empName = attendee.empName;
                if (event.registrationStatus !== undefined) {
                    $scope.regDtls.adults = attendee.adultCount;
                    $scope.regDtls.children = attendee.childCount;
                    if (attendee.guests === null) {
                        $scope.guestsDtls = [];
                    } else {
                        $scope.guestsDtls = attendee.guests;
                    }
                    $scope.dataToSend = {
                        "eventId": event.id,
                        "userId": attendee.userId
                    };
                    Event.retrieveCustomValuesByUser($scope.dataToSend, function (res) {
                        $scope.regDetails.registrationFieldsDataBean = res;
                    }, function (res) {
                    });
                }
            };

            $scope.cancel = function (regForm) {
                $scope.formSubmitted = false;
                $scope.newFieldFormSubmitted = false;
                regForm.$setPristine();
                $scope.reg = [];
                $scope.initManageEvent();
            };
            $scope.returnFromDetails = function () {
                $scope.initManageEvent();
            };
            $scope.guests = [];
            $scope.addGuest = function (addGuestForm, regGuest) {
                $scope.regGuest = regGuest;
                $scope.newFieldFormSubmitted = true;
                if (addGuestForm.$valid) {
                    $scope.guests.push({name: $scope.regGuest.gname, relation: $scope.regGuest.grel});
                    $scope.regGuest.gname = "";
                    $scope.newFieldFormSubmitted = false;
                    $scope.regGuest.grel = "";
                    addGuestForm.$setPristine();
                }
            };
            $scope.removeGuest = function (index) {
                $scope.guests.splice(index, 1);
            };
            $scope.reg = [];
            $scope.register = function (regForm, reg) {
                $scope.formSubmitted = true;
                $scope.reg = reg;
                if (((regForm.countForm !== undefined && regForm.countForm.$valid) || (regForm.reasonForm !== undefined && regForm.reasonForm.$valid))) {
                    if (regForm.addedGuestForm === undefined || (regForm.addedGuestForm !== undefined && regForm.addedGuestForm.$valid))
                        $scope.formSubmitted = false;
                    $scope.newFieldFormSubmitted = false;
                    if ($scope.notAttending === undefined)
                    {
                        $scope.notAttending = false;
                    }
                    var registrationForm = {
                        id: $scope.selectedEventForRegistration.id,
                        adultCount: $scope.reg.adults,
                        reason: $scope.reg.reason,
                        isAttending: $scope.reg.notAttending,
                        childCount: $scope.reg.children,
                        guestCount: $scope.guests.length,
                        registrationFieldsDataBean: [],
                        guests: []
                    };
                    $scope.selectedEventForRegistration.adultCount = $scope.reg.adults;
                    $scope.selectedEventForRegistration.childCount = $scope.reg.children;
                    $scope.selectedEventForRegistration.guests = $scope.guests;
                    if ($scope.selectedEventForRegistration.registrationFieldsDataBean !== undefined && $scope.selectedEventForRegistration.registrationFieldsDataBean !== null) {
                        for (var i = 0; i < $scope.selectedEventForRegistration.registrationFieldsDataBean.length; i++) {
                            var item = $scope.selectedEventForRegistration.registrationFieldsDataBean[i];
                            if (item.value !== undefined && item.value !== null) {
                                registrationForm.registrationFieldsDataBean.push({
                                    "id": item.id,
                                    "fieldName": item.fieldName,
                                    "value": item.value
                                });
                            }
                        }
                    }

                    for (var i = 0; i < $scope.guests.length; i++) {
                        var item = $scope.guests[i];
                        registrationForm.guests.push({
                            name: item.name,
                            relation: item.relation
                        });
                    }

                    if (!$scope.editRegistrationFlag) {
                        $rootScope.maskLoading();
                        Event.registerForEvent(registrationForm, function (res) {
                            $scope.selectedEventForRegistration.registrationStatus = !$scope.reg.notAttending;
                            $scope.selectedEventForRegistration.reason = $scope.reg.reason;
                            regForm.$setPristine();
                            $scope.reg = {};
                            $rootScope.unMaskLoading();
                            $scope.initManageEvent();
                        }, function (res) {

                        });
                    } else {
                        $rootScope.maskLoading();
                        Event.editRegistrationforevent(registrationForm, function (res) {
                            $scope.selectedEventForRegistration.registrationStatus = !$scope.reg.notAttending;
                            $scope.selectedEventForRegistration.reason = $scope.reg.reason;
                            regForm.$setPristine();
                            $scope.editRegistrationFlag = false;
                            $scope.reg = {};
                            $rootScope.unMaskLoading();
                            $scope.initManageEvent();
                        }, function (res) {

                        });
                    }
                }
            };
            $scope.cancelRegistration = function (event) {
                $scope.selectedEventForRegistration = event;
                Event.cancelRegistration(event.id, function (res) {
                    $scope.selectedEventForRegistration.guests = [];
                    $scope.initManageEvent();
                    var list = $scope.selectedEventForRegistration.registrationFieldsDataBean;
                    for (var i = 0; i < list.length; i++) {
                        list[i].value = "";
                    }

                }, function () {
                });
            };
            $scope.retrieveUserRegistration = function (eventId) {
                $scope.regResponse = false;
                Event.retrieveUserRegistrationEntities(eventId, function (res) {
//                    console.log("res :::---" + JSON.stringify(res));
                    $scope.regResponse = true;
                    $scope.attendeeList = res;
                    $scope.totalCount = $scope.attendeeList.length;
                    $scope.attendeeCount = 0;
                    $scope.notAttendingCount = 0;
                    $scope.awaitingResponseCount = 0;
                    angular.forEach($scope.attendeeList, function (item) {
                        if (item.status === "AT") {
                            $scope.attendeeCount = $scope.attendeeCount + 1;
                        } else if (item.status === "NAT") {
                            $scope.notAttendingCount = $scope.notAttendingCount + 1;
                        } else if (item.status === null) {
                            $scope.awaitingResponseCount = $scope.awaitingResponseCount + 1;
                        }
                    });
                    $scope.attendeesOnstatus = [];
                    if ($scope.attendeeList !== null && angular.isDefined($scope.attendeeList) && $scope.attendeeList.length > 0) {
                        angular.forEach($scope.attendeeList, function (item) {
                            if (item.status === $scope.statusField) {
                                $scope.attendeesOnstatus.push(item);
                            }
                        });
                    }
                });
            };
            $scope.retrieveAttendeeListOnStatus = function (status) {
                $scope.attendeesOnstatus = [];
                if ($scope.attendeeList !== null && angular.isDefined($scope.attendeeList) && $scope.attendeeList.length > 0) {
                    if (status === "Attending") {
                        angular.forEach($scope.attendeeList, function (item) {
                            if (item.status === "AT") {
                                $scope.attendeesOnstatus.push(item);
                            }
                        });
                    } else if (status === "NonAttending") {
                        angular.forEach($scope.attendeeList, function (item) {
                            if (item.status === "NAT") {
                                $scope.attendeesOnstatus.push(item);
                            }
                        });
                    } else if (status === "AwaitingResponse") {
                        angular.forEach($scope.attendeeList, function (item) {
                            if (item.status === null) {
                                $scope.attendeesOnstatus.push(item);
                            }
                        });
                    }
                }
            };
            $scope.displayGallery = function (id) {
                $scope.photos = [];
                if ($scope.photos.length === 0) {
                    Event.retrieveImageThumbnailPaths(id, function (res) {
                        var data = res.src;
                        angular.forEach(data, function (name) {
                            var imgName = name.replace("_T", "");
                            $scope.photos.push({
                                src: $rootScope.appendAuthToken($rootScope.apipath + 'event/getimage?file_name=' + name),
                                name: imgName
                            });
                        });
                    });
                }
            };

            $scope.exportChange = function (exportType) {
                if (exportType === "Attending") {
                    if ($scope.attendeeCount === 0) {
                        $scope.disablePdf = true;
                    } else {
                        $scope.disablePdf = false;
                    }
                } else {
                    $scope.disablePdf = false;
                }
            };
            $rootScope.unMaskLoading();
        }]);
});



