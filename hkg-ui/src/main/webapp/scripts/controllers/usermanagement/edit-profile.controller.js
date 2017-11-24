/**
 * This controller is for manage employee feature
 * Author : Mansi Parekh
 * Date : 30 July 2014
 */
define(['hkg', 'employeeService', 'franchiseService', 'fileUploadService', 'leaveWorkflowService', 'designationService', 'departmentService', 'webcam', 'addMasterValue'], function (hkg, employeeService, franchiseService, fileUploadService, leaveWorkflowService, designationService, departmentService) {

    hkg.register.controller('EditProfile', ["$rootScope", "$timeout", "$scope", "$filter", "Employee", "$location", "$anchorScroll", "FranchiseService", "LeaveWorkflow", "Designation", "DepartmentService", "DynamicFormService", "$route", "FileUploadService", function ($rootScope, $timeout, $scope, $filter, Employee, $location, $anchorScroll, FranchiseService, LeaveWorkflow, Designation, DepartmentService, DynamicFormService, $route, FileUploadService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "";
            $rootScope.childMenu = "";
            $rootScope.activateMenu();
            $scope.entity = "EMPLOYEE.";
            $scope.today = new Date();
            $scope.notAvailable = 'NA';
            $scope.notAvailableSection = 'No Information Available for this section';
            $scope.flag = false;
            var orderBy = $filter('orderBy');
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";
            $scope.$on('$viewContentLoaded', function () {
                $scope.select2Locations = [];

                initializtion();
                retrievePrerequisite();
//                retrieveCustomField();
            });

            function retrievePrerequisite() {
                $rootScope.maskLoading();
                Employee.retrieveProfilePrerequisite(function (res) {
                    var combo = res['combovalues'];
                    var locations = res['locations'];
                    var detail = res['employeedetail'];
                    var empcode = detail.employeeCode;
                    if (empcode !== null)
                    {
                        $scope.isEmpCode = true;
                    }
                    else
                    {
                        $scope.isEmpCode = false;
                        $rootScope.addMessage("You don't have the permission to edit your profile", 1);

                    }
                    var empconfig = res['employeeConfig'];
                    var agelimit = res['agelimit'];
                    var languages = res['languages'];
                    var empconfigcount = Object.keys(empconfig).length;
                    var agelimitcount = Object.keys(agelimit).length;
                    if (empconfigcount == 0 && agelimitcount == 0)
                    {
                        $rootScope.addMessage("please configure franchise first and then try creating employee", 1);
                    }
                    if (agelimitcount != 0) {
                        var minagearray = agelimit['minAge'];
                        var maxagearray = agelimit['maxAge'];
                        if (angular.isDefined(minagearray) && angular.isDefined(maxagearray) && minagearray !== null && maxagearray != null) {
                            var minAge = agelimit['minAge'][0].label;
                            var maxAge = agelimit['maxAge'][0].label;
                            $scope.mindobDate = new Date($scope.today.getFullYear() - maxAge, $scope.today.getMonth(), $scope.today.getDay());
                            $scope.maxdobDate = new Date($scope.today.getFullYear() - minAge, $scope.today.getMonth(), $scope.today.getDay());
                        }
                    }
                    setComboValues(combo);
                    setLocations(locations);
                    $scope.languageList = languages.sort($rootScope.predicateBy("label"));
                    setEmployeeDetail(detail);
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }

            function initializtion() {
                resetVariable();
            }
            ;
            $scope.employee = {};
            function  resetVariable() {
                $scope.isSuperAdmin = false;
                $scope.employee = {};
                $scope.isCreate = false;
                $scope.submitted = false;
                $scope.policyHoldersMap = {};
                $scope.hasImage = {};
                $scope.isDepInvalid = true;
                $scope.employeeList = [];
                $scope.notUniqueEmpname = false;
                $scope.isEmailValidate = true;
                $scope.isWorkEmailValidate = true;
                $scope.joiningDateValidate = true;
                $scope.ipValidate = true;
                $scope.isfamilyNameDuplicate = true;
                $scope.iseditfamilyNameDuplicate = true;
                $scope.displayEmployeeFlag = 'view';
                $scope.isInValidSearch = false;
                $scope.educationIndex = 0;
                $scope.hasAnyEdu = false;
                $scope.degreeReq = false;
                $scope.edusubmitted = false;
                $scope.employee.edu = [];
                $scope.employee.edu[$scope.educationIndex] = {};
                $scope.expIndex = 0;
                $scope.hasAnyExp = false;
                $scope.prevCmpnyReq = false;
                $scope.expsubmitted = false;
                $scope.employee.exp = [];
                $scope.employee.exp[$scope.expIndex] = {};
                $scope.familyIndex = 0;
                $scope.hasAnyFamily = false;
                $scope.familynameReq = false;
                $scope.familysubmitted = false;
                $scope.employee.family = [];
                $scope.employee.family[$scope.familyIndex] = {'index': 'F0'};
                $scope.policyIndex = 0;
                $scope.hasAnypolicy = false;
                $scope.policynameReq = false;
                $scope.policysubmitted = false;
                $scope.employee.policy = [];
                $scope.employee.policy[$scope.policyIndex] = {'status': 'Active'};
                $scope.wasHKGEmp = false;
                $scope.shiftList = [];
                $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'MM/dd/yyyy'];
                $scope.format = $rootScope.dateFormat;
                $scope.hasAnyDoc = false;
                $("#input_empReportsTo").select2("data", undefined);
                $("#input_empReportsTo").select2("val", undefined);
                $("#input_empcurrentaddress").select2("data", undefined);
                $("#input_empcurrentaddress").select2("val", undefined);
                $("#input_empnativeaddress").select2("data", undefined);
                $("#input_empnativeaddress").select2("val", undefined);
                $scope.personalData = {};
                $scope.contactData = {};
                $scope.identificationData = {};
                $scope.otherData = {};
                $scope.hkgworkData = {};
                $scope.educationData = [];
                $scope.educationData[$scope.educationIndex] = {index: $scope.educationIndex};
                $scope.experienceData = [];
                $scope.experienceData[$scope.expIndex] = {index: $scope.expIndex};
                $scope.familyData = [];
                $scope.familyData[$scope.familyIndex] = {index: $scope.familyIndex};
                $scope.policyData = [];
                $scope.policyData[$scope.policyIndex] = {index: $scope.policyIndex};
                $scope.personaldbType = {};
                $scope.contactdbType = {};
                $scope.identificationdbType = {};
                $scope.otherdbType = {};
                $scope.hkgworkdbType = {};
                $scope.educationdbType = {};
                $scope.experiencedbType = {};
                $scope.familydbType = {};
                $scope.policydbType = {};
                $scope.languageList = [];
            }
            ;
            $scope.setYearOfPassing = function (dob) {
                dob = new Date(dob);
                $scope.yearOfPassing = [];
                var defaultdob = angular.copy(dob.getFullYear());
                var currentYear = $scope.today.getFullYear();
                var i;
                for (i = currentYear; i > defaultdob; i--)
                {
                    $scope.yearOfPassing.push(i);

                }

            }
            $scope.phoneValidate = function (phone) {
                if (phone.length >= 10) {
                    if (!validateContact(phone)) {
                        $scope.isPhoneValidate = false;
                        return;
                    } else {
                        $scope.isPhoneValidate = true;
                    }
                }
                else {
                    $scope.isPhoneValidate = false;
                    return;
                }

            };
            $scope.altPhoneValidate = function (phone) {
                if (!!phone) {
                    if (phone.length >= 10) {
                        if (!validateContact(phone)) {
                            $scope.isaltPhoneValidate = false;
                            return;
                        } else {
                            $scope.isaltPhoneValidate = true;
                        }
                    }
                    else {
                        $scope.isaltPhoneValidate = false;
                        return;
                    }
                }
                else
                {
                    $scope.isaltPhoneValidate = true;
                }
            };
            function validateContact(contact) {
                var reg = /^(\+91-|\+91|0|\0)?\d{10}$/;
                contact = contact.replace(/ /g, '');
                if (!reg.test(contact)) {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            ;

            function setComboValues(data) {
                if (data != null) {
                    $scope.empTypes = [];
                    if (data["EMPTYPE"] != null && angular.isDefined(data["EMPTYPE"]) && data["EMPTYPE"].length > 0) {
                        angular.forEach(data["EMPTYPE"], function (item) {
                            item.label = $rootScope.translateValue("EMPTYPE." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.empTypes.push(item);
                            $scope.empTypes = orderBy($scope.empTypes, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.bloodGroups = [];
                    if (data["BG"] != null && angular.isDefined(data["BG"]) && data["BG"].length > 0) {
                        angular.forEach(data["BG"], function (item) {
                            item.label = $rootScope.translateValue("BG." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.bloodGroups.push(item);
                            $scope.bloodGroups = orderBy($scope.bloodGroups, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.maritalStatusList = [];
                    if (data["MS"] != null && angular.isDefined(data["MS"]) && data["MS"].length > 0) {
                        angular.forEach(data["MS"], function (item) {
                            item.label = $rootScope.translateValue("MS." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.maritalStatusList.push(item);
                            $scope.maritalStatusList = orderBy($scope.maritalStatusList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.casteList = [];
                    if (data["CASTE"] != null && angular.isDefined(data["CASTE"]) && data["CASTE"].length > 0) {
                        angular.forEach(data["CASTE"], function (item) {
                            item.label = $rootScope.translateValue("CASTE." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.casteList.push(item);
                            $scope.casteList = orderBy($scope.casteList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.expDepList = [];
                    if (data["DEG"] != null && angular.isDefined(data["DEG"]) && data["DEG"].length > 0) {
                        angular.forEach(data["DEG"], function (item) {
                            item.label = $rootScope.translateValue("DEG." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.expDepList.push(item);
                            $scope.expDepList = orderBy($scope.expDepList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.policyCompanyList = [];
                    if (data["POLICYCMPNY"] != null && angular.isDefined(data["POLICYCMPNY"]) && data["POLICYCMPNY"].length > 0) {
                        angular.forEach(data["POLICYCMPNY"], function (item) {
                            item.label = $rootScope.translateValue("POLICYCMPNY." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.policyCompanyList.push(item);
                            $scope.policyCompanyList = orderBy($scope.policyCompanyList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.nationalityMap = [];
                    if (data["NTNLTY"] != null && angular.isDefined(data["NTNLTY"]) && data["NTNLTY"].length > 0) {
                        angular.forEach(data["NTNLTY"], function (item) {
                            item.label = $rootScope.translateValue("NTNLTY." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.nationalityMap.push(item);
                            $scope.nationalityMap = orderBy($scope.nationalityMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.educationDegreeMap = [];
                    if (data["EDUDEG"] != null && angular.isDefined(data["EDUDEG"]) && data["EDUDEG"].length > 0) {
                        angular.forEach(data["EDUDEG"], function (item) {
                            item.label = $rootScope.translateValue("EDUDEG." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.educationDegreeMap.push(item);
                            $scope.educationDegreeMap = orderBy($scope.educationDegreeMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.mediumMap = [];
                    if (data["MDIUM"] != null && angular.isDefined(data["MDIUM"]) && data["MDIUM"].length > 0) {
                        angular.forEach(data["MDIUM"], function (item) {
                            item.label = $rootScope.translateValue("MDIUM." + item.label);
                            $scope.mediumMap.push(item);
                            $scope.mediumMap = orderBy($scope.mediumMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.occupationList = [];
                    if (data["OCCUPSN"] != null && angular.isDefined(data["OCCUPSN"]) && data["OCCUPSN"].length > 0) {
                        angular.forEach(data["OCCUPSN"], function (item) {
                            item.label = $rootScope.translateValue("OCCUPSN." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.occupationList.push(item);
                            $scope.occupationList = orderBy($scope.occupationList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.relationList = [];
                    if (data["RELESN"] != null && angular.isDefined(data["RELESN"]) && data["RELESN"].length > 0) {
                        angular.forEach(data["RELESN"], function (item) {
                            item.label = $rootScope.translateValue("RELESN." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.relationList.push(item);
                            $scope.relationList = orderBy($scope.relationList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.universityList = [];
                    if (data["UNI"] != null && angular.isDefined(data["UNI"]) && data["UNI"].length > 0) {
                        angular.forEach(data["UNI"], function (item) {
                            item.label = $rootScope.translateValue("UNI." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.universityList.push(item);
                            $scope.universityList = orderBy($scope.universityList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.otherDetailsEmpMap = [];
                    if (data["EMPOTHRDTILS"] != null && angular.isDefined(data["EMPOTHRDTILS"]) && data["EMPOTHRDTILS"].length > 0) {
                        angular.forEach(data["EMPOTHRDTILS"], function (item) {
                            item.label = $rootScope.translateValue("EMPOTHRDTILS." + item.label);
                            item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            $scope.otherDetailsEmpMap.push(item);
                            $scope.otherDetailsEmpMap = orderBy($scope.otherDetailsEmpMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    if (angular.isDefined($scope.otherDetailsEmpMap) && $scope.otherDetailsEmpMap !== null) {
                        for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                            $scope.otherDetailsEmpMap[i].isActive = false;
                        }
                    }

                    $scope.employee.gender = "male";
                    $scope.employee.isNativeAddressSame = true;

                }
            }
            ;
            function setLocations(data) {
                $scope.allLocations = data.sort($rootScope.predicateBy("label"));
                angular.forEach($scope.allLocations, function (locationData) {
                    $scope.select2Locations.push({id: locationData.value, text: locationData.label});
                });
                $("#location").select2('val', null);
            }

            $scope.retrieveAvatar = function () {
                return $rootScope.appendAuthToken($rootScope.apipath + "employee/retrieve/avatar" + '?decache=' + $rootScope.randomCount);
            }
            $scope.setPassingYear = function (minYear) {
                if (minYear && angular.isDefined($scope.passingYears)) {
                    $scope.passingYearArray = [];
                    for (var i = 0; i < $scope.passingYears.length; i++) {
                        if (minYear < $scope.passingYears[i].label) {
                            $scope.passingYearArray.push($scope.passingYears[i]);
                        }

                    }
                }
            }
            $scope.updateYearsOfPassing = function () {
                var minYear = $scope.employee.dob.getYear() + 1900;
                if ($scope.employee.dob.getYear()) {
                    $scope.setPassingYear(minYear);
                }
            };
            $scope.changeholdername = function () {
                if (!angular.isDefined($scope.employee.empName)) {
                    delete $scope.policyHoldersMap['E0'];
                } else {
                    $scope.policyHoldersMap['E0'] = $scope.employee.empName;
                }
            };
            $scope.multiLocations = {
                multiple: false,
                closeOnSelect: false,
                placeholder: "Select District",
                allowClear: true,
                data: function () {
                    return {'results': $scope.select2Locations};
                }
            };
            $scope.cancelEditProfile = function ()
            {
                $location.path('dashboard');
            }
            $scope.multiLocations1 = {
                multiple: false,
                closeOnSelect: false,
                placeholder: "Select District",
                allowClear: true,
                data: function () {
                    return {'results': $scope.select2Locations};
                }
            };

            $scope.scrollTo = function (eID) {
                // This scrolling function 
                // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript

                var startY = currentYPosition();
                var stopY = elmYPosition(eID);
                var distance = stopY > startY ? stopY - startY : startY - stopY;
                if (distance < 100) {
                    scrollTo(0, stopY - 60);
                    return;
                }
                var speed = Math.round(distance / 100);
                if (speed >= 20)
                    speed = 20;
                var step = Math.round(distance / 25);
                var leapY = stopY > startY ? startY + step : startY - step;
                var timer = 0;
                if (stopY > startY) {
                    for (var i = startY; i < stopY; i += step) {
                        setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                        leapY += step;
                        if (leapY > stopY)
                            leapY = stopY;
                        timer++;
                    }
                    return;
                }
                for (var i = startY; i > stopY; i -= step) {
                    setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                    leapY -= step;
                    if (leapY < stopY)
                        leapY = stopY;
                    timer++;
                }

                function currentYPosition() {
                    // Firefox, Chrome, Opera, Safari
                    if (self.pageYOffset)
                        return self.pageYOffset;
                    // Internet Explorer 6 - standards mode
                    if (document.documentElement && document.documentElement.scrollTop)
                        return document.documentElement.scrollTop;
                    // Internet Explorer 6, 7 and 8
                    if (document.body.scrollTop)
                        return document.body.scrollTop;
                    return 0;
                }

                function elmYPosition(eID) {
                    var elm = document.getElementById(eID);
                    var y = elm.offsetTop;
                    var node = elm;
                    while (node.offsetParent && node.offsetParent != document.body) {
                        node = node.offsetParent;
                        y += node.offsetTop;
                    }
                    return y;
                }

            };

            function setEmployeeDetail(data) {
                $scope.setYearOfPassing(data.dob);
                if (data.companyId == 0)
                {
                    $scope.isSuperAdmin = true;
                }
                else
                {
                    $scope.isSuperAdmin = false;
                }
                $scope.isCreate = false;
                $rootScope.editMyProfile = true;
                $scope.searchtext = undefined;
                $scope.displayEmployeeFlag = 'view';
                $scope.flag = true;
                if (data.nativepincode === null) {
                    data.nativepincode = undefined;
                }
                if (data.altphnno === null) {
                    data.altphnno = undefined;
                }
                if (angular.isDefined(data.profileImageName) && data.profileImageName != null && data.profileImageName.length > 0) {
                    $scope.hasImage[data.id] = true;
                }
                $scope.policyHoldersMap['E0'] = data.empName;
                for (var i = 0; i < data.family.length; i++) {
                    if (angular.isDefined(data.family[i].familyImageName) && data.family[i].familyImageName != null && data.family[i].familyImageName.length > 0) {
                        $scope.hasImage[data.family[i].id] = true;
                    }
                    $scope.policyHoldersMap['F' + i] = data.family[i].firstName;
                }
                if (angular.isDefined(data.edu) && data.edu.length > 1) {
                    $scope.hasAnyEdu = true;
                }
                if (angular.isDefined(data.exp) && data.exp.length > 1) {
                    $scope.hasAnyExp = true;
                }
                if (angular.isDefined(data.family) && data.family.length > 1) {
                    $scope.hasAnyFamily = true;
                }
                if (angular.isDefined(data.policy) && data.policy.length > 1) {
                    $scope.hasAnyPolicy = true;
                }
                if (angular.isDefined(data.otherdocs) && data.otherdocs.length > 0) {
                    $scope.hasAnyDoc = true;
                }
                $scope.invalidParent = false;
                $scope.isDepInvalid = false;
                if (angular.isDefined(data.personalCustom) && data.personalCustom != null) {
                    $scope.personalData = angular.copy(data.personalCustom);
                }
                if (angular.isDefined(data.contactCustom) && data.contactCustom != null) {
                    $scope.contactData = angular.copy(data.contactCustom);
                }
                if (angular.isDefined(data.identificationCustom) && data.identificationCustom != null) {
                    $scope.identificationData = angular.copy(data.identificationCustom);
                }
                if (angular.isDefined(data.otherCustom) && data.otherCustom != null) {
                    $scope.otherData = angular.copy(data.otherCustom);
                }
                if (angular.isDefined(data.hkgworkCustom) && data.hkgworkCustom != null) {
                    $scope.hkgworkData = angular.copy(data.hkgworkCustom);
                }
                for (var i = 0; i < data.edu.length; i++) {
                    if (angular.isDefined(data.edu[i].educationCustom) && data.edu[i].educationCustom != null) {
                        $scope.educationData[i] = angular.copy(data.edu[i].educationCustom);
                    } else {
                        $scope.educationData[i] = {};
                    }
//                        $scope.educationdbType = angular.copy(data.edu[i].educationDbType);
                }
                for (var i = 0; i < data.exp.length; i++) {
                    if (angular.isDefined(data.exp[i].experienceCustom) && data.exp[i].experienceCustom != null) {
                        $scope.experienceData[i] = angular.copy(data.exp[i].experienceCustom);
                    } else {
                        $scope.experienceData[i] = {};
                    }
//                        $scope.experiencedbType = angular.copy(data.exp[i].experienceDbType);
                }
                for (var i = 0; i < data.family.length; i++) {
                    if (angular.isDefined(data.family[i].familyCustom) && data.family[i].familyCustom != null) {
                        $scope.familyData[i] = angular.copy(data.family[i].familyCustom);
                    } else {
                        $scope.familyData[i] = {};
                    }
//                        $scope.familydbType = angular.copy(data.family[i].familyDbType);
                }
                for (var i = 0; i < data.policy.length; i++) {
                    if (angular.isDefined(data.policy[i].policyCustom) && data.policy[i].policyCustom != null) {
                        $scope.policyData[i] = angular.copy(data.policy[i].policyCustom);
                    } else {
                        $scope.policyData[i] = {};
                    }
//                        $scope.policydbType = angular.copy(data.policy[i].policyDbType);
                }
                $scope.employee = angular.copy(data);
//                $("#multiLocations").select2("data", data.currentaddress);
//                angular.forEach($scope.select2Locations, function(item) {
//                    if (item.id === data.currentaddress) {
//                        data.currentaddress = item;
//                    }
//                });
                $("#multiLocations1").select2("data", data.nativeaddress);
                angular.forEach($scope.select2Locations, function (item) {
                    if (item.id === data.nativeaddress) {
                        data.nativeaddress = item;
                    }
                });
                $("#input_empReportsTo").select2("data", "");
                $scope.names = {
                    id: data.reportsToId,
                    text: data.reportsToName
                };
                $("#input_empReportsTo").select2("data", $scope.names);
                var date = new Date($scope.employee.dob);
                $scope.employee.dob = date;
                var minYear = date.getYear() + 1900;
                $scope.setPassingYear(minYear);
                $scope.isCreate = false;
                $scope.viewAllEduDetails();
                $scope.viewAllExpDetails();
                $scope.viewAllFamilyDetails();
                $scope.viewAllPolicyDetails();
                angular.forEach($scope.empTypes, function (i) {
                    if (i.value === $scope.employee.empType) {
                        $scope.employee.emplTypeLabel = i.label;
                    }
                });
                if (!$scope.employee.gender) {
                    $scope.employee.gender = "male";
                }
                $scope.oldname = $scope.employee.empName;
                $scope.flag = true;
            }
            ;
//            $scope.multiLocations = {
//                multiple: false,
//                closeOnSelect: false,
//                placeholder: "Select District",
//                allowClear: true,
//                data: function() {
//                    return {'results': $scope.select2Locations};
//                }
//            };
            $scope.multiLocations1 = {
                multiple: false,
                closeOnSelect: false,
                placeholder: "Select District",
                allowClear: true,
                data: function () {
                    return {'results': $scope.select2Locations};
                }
            };
            $scope.addEmployee = function (form) {
                $scope.submitted = true;
                $scope.phoneValidate($scope.employee.phnno);
                $scope.altPhoneValidate($scope.employee.altphnno);
                $scope.validateEmail($scope.employee.email);
                var finalEmployee = angular.copy($scope.employee);
                var valid = true;
                for (var key in form) {
                    if (form[key].$invalid) {
                        valid = false;
                        break;
                    }
                }
                var allconditionStatisfied = false;
//                console.log("!$scope.isDepInvalid" + !$scope.isDepInvalid);
//                console.log("!$scope.notUniqueEmpname" + !$scope.notUniqueEmpname);
//                console.log("$scope.isPhoneValidate " + $scope.isPhoneValidate);
//                console.log("$scope.isAltPhoneInvalid" + $scope.isaltPhoneValidate)
//                console.log("!$scope.isEmailInvalid" + !$scope.isEmailInvalid);
//                console.log("$scope.isWorkEmailValidate" + $scope.isWorkEmailValidate);
//                console.log("$scope.isfamilyNameDuplicate" + $scope.isfamilyNameDuplicate);
//                console.log("!$scope.degreeReq" + !$scope.degreeReq);
//                console.log("!$scope.prevCmpnyReq" + !$scope.prevCmpnyReq);
//                console.log("!$scope.familynameReq" + !$scope.familynameReq);
//                console.log("!$scope.policynameReq" + !$scope.policynameReq);
//                console.log("$scope.nativevalid " + $scope.nativevalid);
//                console.log("$scope.ipValidate" + $scope.ipValidate);
                if (!$scope.isDepInvalid && !$scope.invalidname && !$scope.notUniqueEmpname && $scope.isPhoneValidate && $scope.isaltPhoneValidate && $scope.isEmailValidate && $scope.isWorkEmailValidate && $scope.isfamilyNameDuplicate && !$scope.degreeReq && !$scope.prevCmpnyReq && !$scope.familynameReq && !$scope.policynameReq && $scope.ipValidate) {
                    allconditionStatisfied = true;
                }
                var success = function (data) {
                    $rootScope.randomCount = Math.random();
                    $rootScope.switchLanguage($scope.employee.prefferedLang, $rootScope.session.companyId);
//                    form.$setPristine();
//                    $timeout(function () {
//                        $route.reload();
//                    }, 500);
                    $rootScope.maskLoading();
                    $rootScope.mainMenu = "";
                    $rootScope.childMenu = "";
                    $rootScope.activateMenu();
                    $scope.entity = "EMPLOYEE.";
                    $scope.today = new Date();
                    $scope.notAvailable = 'NA';
                    $scope.notAvailableSection = 'No Information Available for this section';
                    $scope.flag = false;
                    $scope.popover =
                            "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                            "\n\ " +
                            "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                            "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                            "</table>\ ";
                    $scope.select2Locations = [];
                    initializtion();
                    retrievePrerequisite();



//                    initializtion();
//                    retrievePrerequisite();
                    $scope.searchtext = undefined;
                    $scope.scrollTo("pageTop");

//                    $location.path("/");
                };
                var failure = function () {


                };
                if (valid && !$scope.isDepInvalid && !$scope.isSuperAdmin && allconditionStatisfied) {
                    if (finalEmployee.currentaddress instanceof Object) {
                        finalEmployee.currentaddress = finalEmployee.currentaddress.id;
                    }
                    if (finalEmployee.nativeaddress instanceof Object) {
                        finalEmployee.nativeaddress = finalEmployee.nativeaddress.id;
                    }
//                  
//                

                    var userObj = {
                        "empName": finalEmployee.empName,
                        "email": finalEmployee.email,
                        "phnno": finalEmployee.phnno,
                        "pincode": finalEmployee.pincode,
                        "fulladdress": finalEmployee.fulladdress,
                        "currentaddress": finalEmployee.currentaddress,
                        "bloodGrp": finalEmployee.bloodGrp,
                        "nationality": finalEmployee.nationality,
                        "caste": finalEmployee.caste,
                        "maritalstatus": finalEmployee.maritalstatus,
                        "dob": finalEmployee.dob,
                        "userId": finalEmployee.userId,
                        "profileImageName": finalEmployee.profileImageName,
                        "nativeaddress": finalEmployee.currentaddress,
                        "altphnno": finalEmployee.altphnno,
                        "gender": finalEmployee.gender,
                        "prefferedLang": finalEmployee.prefferedLang

//                        "employee.prefferedLang"

                    };
                    if (userObj.currentaddress instanceof Object) {
                        userObj.currentaddress = userObj.currentaddress.id;
                    }
                    Employee.updateProfile(userObj, success, failure);

                }

                if ($scope.isSuperAdmin)

                {
                    var superadminObj = {
                        "empName": finalEmployee.empName,
                        "dob": finalEmployee.dob,
                        "gender": finalEmployee.gender,
                        "userId": finalEmployee.userId,
                        "profileImageName": finalEmployee.profileImageName,
                        "profileImageId": finalEmployee.profileImageId,
                        "prefferedLang": finalEmployee.prefferedLang
                    }
                    Employee.updateProfileOfSuperAdmin(superadminObj, success, failure);
                }

            };
            $scope.openNativeAddressModal = function (isSame) {
                if (!isSame) {
                    $scope.nativesubmitted = false;
                    $('#nativeAddressModal').modal('show');
                }
            };
            $scope.resetNativeAddress = function () {
                $scope.nativesubmitted = false;
                $scope.employee.nativeaddress = undefined;
                $scope.employee.nativefulladdress = undefined;
                $scope.employee.nativepincode = undefined;
            };
            $scope.cancelNativeAddress = function () {
                $scope.resetNativeAddress();
                $('#nativeAddressModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.okNativeAddress = function (form) {
                $scope.nativesubmitted = true;
                if (form.$valid) {
                    $scope.nativesubmitted = false;
                    $('#nativeAddressModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();

                }
            };
            $scope.viewAllEduDetails = function () {
                $scope.viewAllEdu = [];
                $.each($scope.employee.edu, function (index, item) {
                    var item1 = angular.copy(item);
                    item1.index = index;
                    if ($scope.educationDegreeMap)
                    {
                        for (var i = 0; i < $scope.educationDegreeMap.length; i++) {
                            if ($scope.educationDegreeMap[i].value === item1.degree) {
                                item1.degreename = $scope.educationDegreeMap[i].label;
                                break;
                            }
                        }
                    }
                    if ($scope.universityList) {
                        for (var i = 0; i < $scope.universityList.length; i++) {
                            if ($scope.universityList[i].value === item1.university) {
                                item1.universityname = $scope.universityList[i].label;
                                break;
                            }
                        }
                    }
                    if ($scope.mediumMap) {
                        for (var i = 0; i < $scope.mediumMap.length; i++) {
                            if ($scope.mediumMap[i].value === item1.medium) {
                                item1.mediumname = $scope.mediumMap[i].label;
                                break;
                            }
                        }
                    }
                    for (var i = 0; i < $scope.yearOfPassing.length; i++) {
                        if ($scope.yearOfPassing[i] === item1.passingYear) {
                            item1.yearname = $scope.yearOfPassing[i];
                            break;
                        }
                    }
                    $scope.viewAllEdu.push(item1);
                });
            };

            $scope.viewAllExpDetails = function () {
                $scope.viewAllExp = [];
                $.each($scope.employee.exp, function (index, item) {
                    var item1 = angular.copy(item);
                    item1.index = index;
                    if ($scope.expDepList)
                    {
                        for (var i = 0; i < $scope.expDepList.length; i++) {
                            if ($scope.expDepList[i].value === item1.designation) {
                                item1.designationname = $scope.expDepList[i].label;
                                break;
                            }
                        }
                    }
                    $scope.viewAllExp.push(item1);
                });
            };
            $scope.viewAllFamilyDetails = function () {
                $scope.viewAllFamily = [];
                $.each($scope.employee.family, function (index, item) {
                    var item1 = angular.copy(item);
                    item1.index = index;
                    if ($scope.relationList)
                    {
                        for (var i = 0; i < $scope.relationList.length; i++) {
                            if ($scope.relationList[i].value === item1.relation) {
                                item1.relationname = $scope.relationList[i].label;
                                break;
                            }
                        }
                    }
                    if ($scope.bloodGroups)
                    {
                        for (var i = 0; i < $scope.bloodGroups.length; i++) {
                            if ($scope.bloodGroups[i].value === item1.bloodGroup) {
                                item1.bloodGroupname = $scope.bloodGroups[i].label;
                                break;
                            }
                        }
                    }
                    if ($scope.occupationList)
                    {
                        for (var i = 0; i < $scope.occupationList.length; i++) {
                            if ($scope.occupationList[i].value === item1.occupation) {
                                item1.occupationname = $scope.occupationList[i].label;
                                break;
                            }
                        }
                    }
                    $scope.viewAllFamily.push(item1);
                });
            };
            $scope.viewAllPolicyDetails = function () {
                $scope.viewAllPolicy = [];
                $.each($scope.employee.policy, function (index, item) {
                    var item1 = angular.copy(item);
                    item1.index = index;
                    if ($scope.policyCompanyList)
                    {
                        for (var i = 0; i < $scope.policyCompanyList.length; i++) {
                            if ($scope.policyCompanyList[i].value === item1.company) {
                                item1.companyname = $scope.policyCompanyList[i].label;
                                break;
                            }
                        }
                    }
                    for (var key in $scope.policyHoldersMap) {
                        if (key === item1.contactUser) {
                            item1.holdername = $scope.policyHoldersMap[key];
                            break;
                        }
                    }
                    $scope.viewAllPolicy.push(item1);
                });
            };
            $scope.doesEmployeeNameExist = function (name) {
                if (name && name.length > 0) {
                    Employee.doesEmployeeNameExist(name, function (resp) {

                        if (resp.data && name != $scope.oldname) {
                            $scope.notUniqueEmpname = true;
                        } else {
                            $scope.notUniqueEmpname = false;
                        }
                    }, function () {
                    });
                } else {
                    $scope.notUniqueEmpname = false;
                }
            };
            $scope.validateEmailId = function (email)
            {
                var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
                if (!reg.test(email)) {
                    return false;
                }
                else
                {
                    return true;
                }
            };
            $scope.validateEmail = function (email) {
                if(!!email){
                var emailresult = $scope.validateEmailId(email);
                if (!emailresult) {

                    $scope.isEmailValidate = false;
                }
                else
                {
                    $scope.isEmailValidate = true;
                }
            }else
            {
                $scope.isEmailValidate=true;
            }
            };
            $scope.validateWorkEmailId = function (email) {
                var workEmailId = $scope.validateEmailId(email);
                if (!workEmailId) {

                    $scope.isWorkEmailValidate = false;
                }
                else
                {
                    $scope.isWorkEmailValidate = true;
                }
            };
            $scope.ValidateIPaddress = function (Ipaddress)
            {
                if (Ipaddress !== undefined)
                {
                    var ipformat = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
                    if (Ipaddress.match(ipformat))
                    {
                        $scope.ipValidate = true;
                    }
                    else
                    {
                        $scope.ipValidate = false;
                    }
                }
            }
            ;
            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };
            $scope.setMinDate = function () {
                if ($scope.employee.dob === "" || $scope.employee.dob === undefined) {
                    $scope.minToDate = new Date();
                } else {
                    $scope.minToDate = $scope.employee.dob;
                }
                if ($scope.employee.dob > $scope.employee.joiningDate) {
                    $scope.employee.joiningDate = $scope.employee.dob;
                }

            };
            //For file upload
            $scope.uploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: true,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Franchise'
                }
            };
            //For Profile file 
            $scope.profileImg = {};
            $scope.profileFileAdded = function (file, flow) {
                $scope.profileFlow = flow;
                $scope.uploadFile.query.fileType = "PROFILE";
                $scope.profileUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")) {
                    $scope.profileImg.invalidFileFlag = true;
                    $scope.profileImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 10485760) {
                        $scope.profileImg.invalidFileSizeFlag = true;
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
            //Profile file upload success
            $scope.profileFileUploaded = function (file, flow, addEventForm, response) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "PROFILE";
                $scope.hasImage[$scope.employee.id] = undefined;
                $scope.profileUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                var modelName = 'Franchise';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "true";
                var info;
                Employee.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;

                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function (response) {
                        $scope.employee.profileImageName = response.res;


                    });
                });
                flow.cancel();
            };
            $scope.profileFileRemove = function () {
                $scope.hasImage[$scope.employee.id] = undefined;
                Employee.removeFileFromTemp($scope.employee.profileImageName);
                $scope.employee.profileImageName = undefined;
            };
            //For Family file 
            $scope.familyImg = {};
            $scope.familyFileAdded = function (file, flow) {
                $scope.familyFlow = flow;
                $scope.uploadFile.query.fileType = "PROFILE";
                $scope.familyUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")) {
                    $scope.familyImg.invalidFileFlag = true;
                    $scope.familyImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 10485760) {
                        $scope.familyImg.invalidFileSizeFlag = true;
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
            //Family file upload success
            $scope.familyFileUploaded = function (flow, addEventForm, response, isEdit) {
                if (isEdit) {
                    $scope.hasImage[$scope.editfamily.id] = undefined;
                    $scope.editfamily.familyImageName = response;
                } else {
                    $scope.employee.family[$scope.familyIndex].familyImageName = response;
                }
                $scope.familyUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                flow.cancel();
            };
            $scope.familyFileRemove = function (index) {
                $scope.hasImage[$scope.employee.family[index].id] = undefined;
                Employee.removeFileFromTemp($scope.employee.family[index].familyImageName);
                $scope.employee.family[index].familyImageName = undefined;
            };
            //For Salaryslip file 
            $scope.salaryslipImg = {};
            $scope.salaryslipFileAdded = function (file, flow) {
                $scope.salaryslipFlow = flow;
                $scope.uploadFile.query.fileType = "SALARYSLIP";
                $scope.salaryslipUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif") && (file.getExtension() != "pdf")) {
                    $scope.salaryslipImg.invalidFileFlag = true;
                    $scope.salaryslipImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 10485760) {
                        $scope.salaryslipImg.invalidFileSizeFlag = true;
                        return false;
                    }
                }
                return !!{
                    jpg: 1,
                    jpeg: 1,
                    gif: 1,
                    png: 1,
                    pdf: 1
                }
                [file.getExtension()];
            };
            //Salaryslip file upload success
            $scope.salaryslipFileUploaded = function (flow, addEventForm, response, isEdit) {
                if (isEdit) {
                    $scope.editexp.salaryslipImageName = response;
                } else {
                    $scope.employee.exp[$scope.expIndex].salaryslipImageName = response;
                }
                $scope.salaryslipUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                flow.cancel();
            };
            $scope.salaryslipRemove = function (index) {
                Employee.removeFileFromTemp($scope.employee.exp[index].salaryslipImageName);
                $scope.employee.exp[index].salaryslipImageName = undefined;
            };
            //For Otherdocs file 
            $scope.otherdocsImg = {};
            $scope.employee.otherdocs = [];
            $scope.otherDocIndex = 0;
            $scope.otherdocsFileAdded = function (file, flow) {
                $scope.otherdocsFlow = flow;
                $scope.uploadFile.query.fileType = "OTHER";
                $scope.otherdocsUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif") && (file.getExtension() != "pdf") && (file.getExtension() != "txt")) {
                    $scope.otherdocsImg.invalidFileFlag = true;
                    $scope.otherdocsImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 10485760) {
                        $scope.otherdocsImg.invalidFileSizeFlag = true;
                        return false;
                    }
                }
                return !!{
                    jpg: 1,
                    jpeg: 1,
                    gif: 1,
                    png: 1,
                    pdf: 1,
                    txt: 1
                }
                [file.getExtension()];
            };
            //Otherdocs file upload success
            $scope.otherdocsFileUploaded = function (flow, addEventForm, response) {
                $scope.employee.otherdocs[$scope.otherDocIndex] = response;
                $scope.otherDocIndex = $scope.otherDocIndex + 1;
                $scope.hasAnyDoc = true;
                $scope.otherdocsUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
            };
            $scope.otherdocRemove = function (index) {
                Employee.removeFileFromTemp($scope.employee.otherdocs[index]);
                $scope.employee.otherdocs[index] = null;
                $scope.employee.otherdocs.slice(index, 1);
                var j = 0;
                for (var i = 0; i < $scope.employee.otherdocs.length; i++) {
                    var item = $scope.employee.otherdocs[i];
                    if (item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.employee.otherdocs = [];
                    $scope.hasAnyDoc = false;
                    $('#viewdocsModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();

                }
            };
            $scope.openViewDocs = function () {
                $('#viewdocsModal').modal('show');
            };
            $scope.isResigned = false;
            $scope.getStatusValue = function () {
                $scope.isResigned = false;
                for (var i = 0; i < $scope.statusList.length; i++) {
                    if ($scope.statusList[i].value === $scope.employee.workstatus && $scope.statusList[i].label === 'Resigned') {
                        $scope.isResigned = true;
                        $('#terminateEmployeeModal').modal('show');
                        break;
                    }
                }
            };
            $scope.beforeStatus = function () {
                $scope.beforeStatus = $scope.employee.workstatus;
            };
            $scope.proceed = function () {
                var proceed = {id: $scope.employee.id, relievingDate: $scope.employee.relievingDate, status: $scope.employee.workstatus};
                Employee.terminateEmployee(proceed);
                $('#terminateEmployeeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.resetPage();
            };
            $scope.cancelproceed = function () {
                $scope.employee.workstatus = $scope.beforeStatus;
                $('#terminateEmployeeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.resetPage = function () {
                $route.reload();
            };
            $scope.getProfileImageEmployee = function () {
                var imagename = $scope.employee.profileImageName;
                imagename = imagename.substring(0, imagename.lastIndexOf("."));
                imagename += "_T.jpg";
                return $rootScope.appendAuthToken($rootScope.apipath+"/employee/getimage?file_name=" + imagename);
//                return "api/employee/getimage?file_name=" + $scope.employee.profileImageName;
            };
            $scope.getProfileImageContact = function (familyIndex, isEdit) {
                var imagename = '';
                if (isEdit) {
                    imagename = $scope.editfamily.familyImageName;
                } else {
                    imagename = $scope.employee.family[familyIndex].familyImageName;
                }
                 return $rootScope.appendAuthToken($rootScope.apipath+"/employee/getimage?file_name=" + imagename);
            };

            $scope.setViewFlag = function (flag) {
                $scope.displayEmployeeFlag = flag;
            };
            $scope.getSearchedEmployee = function (list) {
                $scope.searchedEmployeeList = list;
                $scope.setViewFlag('search');
            };

            $scope.openChangePassword = function () {
                $scope.passsubmitted = false;
                $('#changePasswordModal').modal('show');
            };
            $scope.resetPassword = function () {
                $scope.confirmpassword = undefined;
                $scope.password = undefined;
                $scope.passsubmitted = false;
                $('#changePasswordModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.confirmPasswordValidation = function () {
                if (!angular.equals($scope.password, $scope.confirmpassword)) {
                    $scope.isMatched = true;
                }
                else {
                    $scope.isMatched = false;
                }
            };
            $scope.changePassword = function (form) {
                $scope.passsubmitted = true;
                if (form.$valid && !$scope.isMatched) {
                    $rootScope.maskLoading();
                    var success = function () {
                        $rootScope.unMaskLoading();
                        $('#changePasswordModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $scope.passsubmitted = false;
                    };
                    var failure = function () {
                        $rootScope.unMaskLoading();
                        $('#changePasswordModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $scope.passsubmitted = false;
                    };
                    var userid = $rootScope.session.id;
                    var json = {id: userid.toString(), password: $scope.password, userid: $scope.employee.userId};

                    Employee.changePassword(json, success, failure);

                }
            };

            $(function () {
                $(window).bind('mousewheel', function (event, delta) {
                    $("#sidemenu").height($("#mainPanel").height());
                });

                $(window).bind('scroll', function (event) {
                    $("#sidemenu").height($("#mainPanel").height());
                });
            });

            $rootScope.unMaskLoading();
        }]);

});
