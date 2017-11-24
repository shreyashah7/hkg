/**
 * This controller is for manage employee feature
 * Author : Mansi Parekh
 * Date : 30 July 2014
 */
define(['hkg', 'employeeService', 'franchiseService', 'fileUploadService', 'leaveWorkflowService', 'designationService', 'departmentService', 'addMasterValue', 'dynamicForm', 'datepickercustom.directive', 'webcam'], function (hkg, employeeService, franchiseService, fileUploadService, leaveWorkflowService, designationService, departmentService) {

    hkg.register.controller('EmployeeController', ["$rootScope", "$scope", "$filter", "Employee", "$location", "$anchorScroll", "FranchiseService", "FileUploadService", "LeaveWorkflow", "Designation", "DepartmentService", "DynamicFormService", "$route", "$timeout", "$modal", function ($rootScope, $scope, $filter, Employee, $location, $anchorScroll, FranchiseService, FileUploadService, LeaveWorkflow, Designation, DepartmentService, DynamicFormService, $route, $timeout, $modal) {
            $rootScope.maskLoading();
            $scope.searchRecords = [];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageEmployees";
            $rootScope.activateMenu();
            $scope.familyreload = true;
            $scope.generalreload = true;
            $scope.personalreload = true;
            $scope.contactreload = true;
            $scope.identificationreload = true;
            $scope.tempeducationflag = true;
            $scope.tempexperienceflag = true;
            $scope.temppolicyflag = true;
            $scope.otherDataflag = true;
            $scope.hkgworkDataflag = true;

            $scope.dt = null;
            $scope.listOfModelsOfDateType = {};
            $scope.entity = "EMPLOYEE.";
            var orderBy = $filter('orderBy');
            $scope.today = $rootScope.getCurrentServerDate();
            $scope.notAvailableSection = 'No Information Available for this section';
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";
            $scope.searchpopover =
                    "<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@R'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                    "</table>\ ";
            $scope.temp = '<input type="text" placeholder="mm/dd/yyyy" min="mindobDate" max="maxdobDate" ng-change="updateYearsOfPassing()" class="form-control" datepicker-popup="{{format}}" ng-model="employee.dob" is-open="emp_dob_opened" ng-required="true" close-text="Close" name="input_empdob"/>';
            $scope.$on('$viewContentLoaded', function () {
                $scope.initializeEmployee();
            });
            $scope.initializeEmployee = function ()
            {
                $scope.select2Locations = [];
                initializtion();
                retrievePrerequisite();
                retrieveCustomField();
                setAccessRights();
            }
            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };
            
            $scope.searchEmployeeInPrevious = function(franchiseId){
                console.log("franchiseId :"+franchiseId)
                $scope.previousFranchise = franchiseId;
            }

            function retrievePrerequisite() {
                $rootScope.maskLoading();

                Employee.retrievePrerequisite(function (res) {
                    var date = $rootScope.getCurrentServerDate();
                    var minYear = date.getFullYear() + 1;
                    date.setFullYear(minYear);
                    $scope.joiningdate = date;
                    var combo = res['combovalues'];
                    var designations = res['designations'];
                    var locations = res['locations'];
                    var franchise = res['franchise'];
                    var department = res['departments'];
                    var empconfig = res['employeeConfig'];
                    var agelimit = res['agelimit'];
                    var empstatus = res['employeeStatus'];
                    var empconfigcount = Object.keys(empconfig).length;
                    var agelimitcount = Object.keys(agelimit).length;
                    var bdate = $rootScope.getCurrentServerDate();
                    var dob;
                    if (agelimit['minAge'] != null)
                    {
                        dob = bdate.getFullYear() - agelimit['minAge'];
                    }
                    else {
                        dob = bdate.getFullYear() - 18;
                    }
                    bdate.setFullYear(dob);
                    $scope.initDate = new Date(bdate);
                    $scope.dateOptions = {
                        'init-date': '$parent.$parent.initDate'
                    };
//                    $scope.employee.dob = bdate;

                    $scope.setJoiningDate();
                    if (empconfigcount == 0 || agelimitcount == 0)
                    {
                        $scope.isFranchiseConfigured = false;
                        $rootScope.addMessage("Please configure employee type & its id along with age limit first and then try creating employee", 1);
                    }
                    if (agelimitcount != 0) {

                        var minagearray = agelimit['minAge'];

                        var maxagearray = agelimit['maxAge'];
                        if (angular.isDefined(minagearray) && angular.isDefined(maxagearray) && minagearray !== null && maxagearray != null) {
                            $scope.today = $rootScope.getCurrentServerDate();
                            $scope.mindobDate = new Date($scope.today.getFullYear() - maxagearray, $scope.today.getMonth(), $scope.today.getDate());
                            $scope.maxdobDate = new Date($scope.today.getFullYear() - minagearray, $scope.today.getMonth(), $scope.today.getDate());
                        } else {

                            $scope.mindobDate = new Date($scope.today.getFullYear() - 55, $scope.today.getMonth(), $scope.today.getDay());
                            $scope.maxdobDate = new Date($scope.today.getFullYear() - 18, $scope.today.getMonth(), $scope.today.getDay());
                        }
                    }
                    setComboValues(combo);
                    $scope.setYearOfPassing();
                    setStatus(empstatus);
//                    setDesignations(designations);
                    setDepartments(department);
                    setLocations(locations);
                    $scope.franchiseList = franchise;
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }
            $scope.setYearOfPassing = function () {
                $scope.yearOfPassing = [];
                if (!!$scope.employee.dob) {

                    var defaultdob = angular.copy($scope.employee.dob.getFullYear());

                    var currentYear = $scope.today.getFullYear();
                    var i;
                    for (i = currentYear; i > defaultdob; i--)
                    {
                        $scope.yearOfPassing.push(i);

                    }
                }

            };
            $scope.setJoiningDate = function ()
            {
                if (!!$scope.employee.dob) {
                    $scope.minJoiningDate = new Date($scope.employee.dob.getFullYear() + 1, 0, 1);
                }
            }

            $scope.setWorkExperienceEndDate = function (date)
            {
                var endRange = date.getDate() + 1;
                $scope.endExperienceInitrange = date.setDate(endRange);
                date.setDate(endRange - 1);
            };
            $scope.setWorkExperienceStartDate = function (date)
            {
                var startRange = date.getDate() - 1;
                $scope.startExperienceRange = date.setDate(startRange);
                date.setDate(startRange + 1);
            };
            $scope.setEditWorkExperienceStartDate = function (date)
            {
                var startEditRange = date.getDate() - 1;
                $scope.startEditExperienceRange = date.setDate(startEditRange);
            };
            $scope.setEditWorkExperienceEndDate = function (date)
            {
                var endEditRange = date.getDate() + 1;
                $scope.endEditExperienceInitrange = date.setDate(endEditRange);
            };

            function setAccessRights()
            {

                $scope.isAccessAddEmployee = true;
                $scope.isAccessUpdateEmployee = true;

                if (!$rootScope.canAccess('employeesAdd'))
                {
                    $scope.isAccessAddEmployee = false;
                }
                if (!$rootScope.canAccess('employeesEdit'))
                {
                    $scope.isAccessUpdateEmployee = false;
                }
            }
            $scope.initializeDbType = function ()
            {
                $scope.personaldbType = {};
                $scope.generaldbType = {};
                $scope.contactdbType = {};
                $scope.identificationdbType = {};
                $scope.otherdbType = {};
                $scope.hkgworkdbType = {};
                $scope.educationdbType = {};
                $scope.experiencedbType = {};
                $scope.familydbType = {};
                $scope.policydbType = {};
                $scope.editpolicydbType = {};
                $scope.editfamilydbType = {};
                $scope.editexperiencedbType = {};
                $scope.editeducationdbType = {};
            };
            $scope.openPage = function (operation) {
                if (operation == 'C') {
                    $scope.isCreate = true;
                    $scope.searchtext = undefined;
                } else {
                    $scope.isCreate = false;
                }
                initializtion();

                retrievePrerequisite();
            };
            function initializtion() {
                resetVariable();
                $scope.initializeDbType();
            }
            ;
            $scope.employee = {};
            function resetVariable() {
                $scope.reload = true;
                $scope.numLimit = 10;
                $scope.modelName = [];
                $scope.designationListForWorkAs = [];

                $scope.isEmployeeTerminationConfirmed = false
                        ;
                $scope.isEmployeeRelieved = false;
                $scope.isResigned = false;
                $scope.employee = {};
                $scope.empName = '';
                $scope.empId = '';
                $scope.empstatus = '';
                $scope.isFranchiseConfigured = true;
                $scope.employee.gender = "male";
                if ($scope.employee.isNativeAddressSame == null)
                {
                    $scope.employee.isNativeAddressSame = true;
                }
                $scope.isCreate = true;
                $scope.submitted = false;
                $scope.isUpdate = false;
                $scope.policyHoldersMap = {};
                $scope.tempAddress = {};
                $scope.isDepInvalid = true;
                $scope.employeeList = [];
                $scope.notUniqueEmpname = false;
                $scope.isEmailInvalid = false;
                $scope.isWorkEmailValidate = true;
                $scope.joiningDateValidate = true;
                $scope.ipValidate = true;
                $scope.isfamilyNameDuplicate = true;
                $scope.isfamilyContact = true;
                $scope.iseditfamilyNameDuplicate = true;
                $scope.displayEmployeeFlag = 'view';
                $scope.isInValidSearch = false;
                $scope.educationIndex = 0;
                $scope.hasAnyEdu = false;
                $scope.degreeReq = false;
                $scope.edusubmitted = false;
                $scope.employee.edu = [];
                $scope.tepedudata = {};
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
                $scope.employee.family[$scope.familyIndex] = {'index': '0'};
                $scope.policyIndex = 0;
                $scope.hasAnypolicy = false;
                $scope.policynameReq = false;
                $scope.policysubmitted = false;
                $scope.employee.policy = [];
                $scope.employee.policy[$scope.policyIndex] = {'status': 'Active', 'index': '0'};
                $scope.wasHKGEmp = false;
                $scope.shiftList = [];
                $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'MM/dd/yyyy'];
                $scope.format = $rootScope.dateFormat;
                $scope.hasAnyDoc = false;
                $scope.employee.otherdocs = [];
                $scope.employee.otherdocsDate = [];
                $scope.otherDocIndex = 0;
                $("#input_empReportsTo").select2("data", undefined);
                $("#input_empReportsTo").select2("val", undefined);
                $("#input_empcurrentaddress").select2("data", undefined);
                $("#input_empcurrentaddress").select2("val", undefined);
                $("#input_empnativeaddress").select2("data", undefined);
                $("#input_empnativeaddress").select2("val", undefined);
//                $scope.tempeducationData = {};
//                $scope.tempexperienceData = {};
//                $scope.tempfamilyData = {};
//                $scope.temppolicyData = {};
                $scope.educationData = [];
                $scope.educationData[$scope.educationIndex] = {index: $scope.educationIndex};
                $scope.experienceData = [];
                $scope.experienceData[$scope.expIndex] = {index: $scope.expIndex};
                $scope.familyData = [];
                $scope.familyData[$scope.familyIndex] = {index: $scope.familyIndex};
                $scope.policyData = [];
                $scope.policyData[$scope.policyIndex] = {index: $scope.policyIndex};
                $scope.personalData = DynamicFormService.resetSection($scope.personalTemplate);
                $scope.generalData = DynamicFormService.resetSection($scope.generaltemplate);


                $scope.contactData = DynamicFormService.resetSection($scope.contactTemplate);

                $scope.identificationData = DynamicFormService.resetSection($scope.identificationTemplate);

                $scope.otherData = DynamicFormService.resetSection($scope.otherTemplate);

                $scope.hkgworkData = DynamicFormService.resetSection($scope.hkgworkTemplate);
                $scope.tempeducationData = DynamicFormService.resetSection($scope.educationTemplate);
//                $scope.tempexperienceData = {};
                $scope.tempexperienceData = DynamicFormService.resetSection($scope.experienceTemplate);
//                $scope.tempfamilyData = {};
                $scope.tempfamilyData = DynamicFormService.resetSection($scope.familyTemplate);
//                $scope.temppolicyData = {};
                $scope.temppolicyData = DynamicFormService.resetSection($scope.policyTemplate);



                if ($scope.selectedParent !== undefined) {
                    $scope.selectedParent = undefined;
                }
//                if (angular.isDefined($scope.statusListCreate) && $scope.statusListCreate !== null && $scope.statusListCreate.length !== 0) {
//                    $scope.employee.workstatus = $scope.statusListCreate[0].value;
//                    alert('here');
//                }

            }
            ;
            function setDefault() {
                if (!angular.isDefined($scope.employee.edu) || $scope.employee.edu == null || $scope.employee.edu.length == 0) {
                    $scope.employee.edu = [];
                }
                if (!angular.isDefined($scope.employee.exp) || $scope.employee.exp == null || $scope.employee.exp.length == 0) {
                    $scope.employee.exp = [];
                }
                if (!angular.isDefined($scope.employee.policy) || $scope.employee.policy == null || $scope.employee.policy.length == 0) {
                    $scope.employee.policy = [];
                }
                if (!angular.isDefined($scope.employee.family) || $scope.employee.family == null || $scope.employee.family.length == 0) {
                    $scope.employee.family = [];
                }
                $scope.employee.edu[$scope.educationIndex] = {};
                $scope.employee.exp[$scope.expIndex] = {};
                $scope.employee.policy[$scope.policyIndex] = {'status': 'Active', 'index': $scope.policyIndex};
                $scope.employee.family[$scope.familyIndex] = {'index': $scope.familyIndex};
            }
            function setComboValues(data) {
                if (data != null) {
                    $scope.empTypes = [];
                    if (data["EMPTYPE"] != null && angular.isDefined(data["EMPTYPE"]) && data["EMPTYPE"].length > 0) {
                        angular.forEach(data["EMPTYPE"], function (item) {
                            item.label = $rootScope.translateValue("EMPTYPE." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.empTypes.push(item);
                            $scope.empTypes = orderBy($scope.empTypes, ['-isOftenUsed', 'shortcutCode', 'value']);

                        });
                    }
                    $scope.bloodGroups = [];
                    if (data["BG"] != null && angular.isDefined(data["BG"]) && data["BG"].length > 0) {
                        angular.forEach(data["BG"], function (item) {
                            item.label = $rootScope.translateValue("BG." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.bloodGroups.push(item);
                            $scope.bloodGroups = orderBy($scope.bloodGroups, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.maritalStatusList = [];
                    if (data["MS"] != null && angular.isDefined(data["MS"]) && data["MS"].length > 0) {
                        angular.forEach(data["MS"], function (item) {
                            item.label = $rootScope.translateValue("MS." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.maritalStatusList.push(item);
                            $scope.maritalStatusList = orderBy($scope.maritalStatusList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.casteList = [];
                    if (data["CASTE"] != null && angular.isDefined(data["CASTE"]) && data["CASTE"].length > 0) {
                        angular.forEach(data["CASTE"], function (item) {
                            item.label = $rootScope.translateValue("CASTE." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.casteList.push(item);
                            $scope.casteList = orderBy($scope.casteList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.expDepList = [];
                    if (data["DEG"] != null && angular.isDefined(data["DEG"]) && data["DEG"].length > 0) {
                        angular.forEach(data["DEG"], function (item) {
                            item.label = $rootScope.translateValue("DEG." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.expDepList.push(item);
                            $scope.expDepList = orderBy($scope.expDepList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.policyCompanyList = [];
                    if (data["POLICYCMPNY"] != null && angular.isDefined(data["POLICYCMPNY"]) && data["POLICYCMPNY"].length > 0) {
                        angular.forEach(data["POLICYCMPNY"], function (item) {
                            item.label = $rootScope.translateValue("POLICYCMPNY." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.policyCompanyList.push(item);
                            $scope.policyCompanyList = orderBy($scope.policyCompanyList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.nationalityMap = [];
                    if (data["NTNLTY"] != null && angular.isDefined(data["NTNLTY"]) && data["NTNLTY"].length > 0) {
                        angular.forEach(data["NTNLTY"], function (item) {
                            item.label = $rootScope.translateValue("NTNLTY." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.nationalityMap.push(item);
                            $scope.nationalityMap = orderBy($scope.nationalityMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.educationDegreeMap = [];
                    if (data["EDUDEG"] != null && angular.isDefined(data["EDUDEG"]) && data["EDUDEG"].length > 0) {
                        angular.forEach(data["EDUDEG"], function (item) {
                            item.label = $rootScope.translateValue("EDUDEG." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.educationDegreeMap.push(item);
                            $scope.educationDegreeMap = orderBy($scope.educationDegreeMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.mediumMap = [];
                    if (data["MDIUM"] != null && angular.isDefined(data["MDIUM"]) && data["MDIUM"].length > 0) {
                        angular.forEach(data["MDIUM"], function (item) {
                            item.label = $rootScope.translateValue("MDIUM." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.mediumMap.push(item);
                            $scope.mediumMap = orderBy($scope.mediumMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.occupationList = [];
                    if (data["OCCUPSN"] != null && angular.isDefined(data["OCCUPSN"]) && data["OCCUPSN"].length > 0) {
                        angular.forEach(data["OCCUPSN"], function (item) {
                            item.label = $rootScope.translateValue("OCCUPSN." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.occupationList.push(item);
                            $scope.occupationList = orderBy($scope.occupationList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.relationList = [];
                    if (data["RELESN"] != null && angular.isDefined(data["RELESN"]) && data["RELESN"].length > 0) {
                        angular.forEach(data["RELESN"], function (item) {
                            item.label = $rootScope.translateValue("RELESN." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.relationList.push(item);
                            $scope.relationList = orderBy($scope.relationList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.universityList = [];
                    if (data["UNI"] != null && angular.isDefined(data["UNI"]) && data["UNI"].length > 0) {
                        angular.forEach(data["UNI"], function (item) {
                            item.label = $rootScope.translateValue("UNI." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.universityList.push(item);
                            $scope.universityList = orderBy($scope.universityList, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    $scope.otherDetailsEmpMap = [];
                    if (data["EMPOTHRDTILS"] != null && angular.isDefined(data["EMPOTHRDTILS"]) && data["EMPOTHRDTILS"].length > 0) {
                        angular.forEach(data["EMPOTHRDTILS"], function (item) {
                            item.label = $rootScope.translateValue("EMPOTHRDTILS." + item.label);
                            if (item.label.length > $rootScope.maxValueForTruncate) {
                                item.label = item.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                            }
                            $scope.otherDetailsEmpMap.push(item);
                            $scope.otherDetailsEmpMap = orderBy($scope.otherDetailsEmpMap, ['-isOftenUsed', 'shortcutCode', 'value']);
                        });
                    }
                    if (angular.isDefined($scope.otherDetailsEmpMap) && $scope.otherDetailsEmpMap !== null && $scope.otherDetailsEmpMap !== undefined) {
                        for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                            $scope.otherDetailsEmpMap[i].isActive = false;
                        }
                    }

                    $scope.employee.gender = "male";
                    $scope.employee.isNativeAddressSame = true;
                    if (angular.isDefined($scope.statusListCreate) && $scope.statusList !== null && $scope.statusListCreate.length !== 0) {
                        $scope.employee.workstatus = $scope.statusListCreate[0].value;
                    }


                    if ($scope.empTypes == null || $scope.bloodGroups == null || $scope.maritalStatusList == null || $scope.casteList == null || $scope.expDepList == null || $scope.policyCompanyList == null
                            || $scope.nationalityMap == null || $scope.educationDegreeMap == null || $scope.mediumMap == null || $scope.occupationList == null || $scope.relationList == null
                            || $scope.universityList == null || $scope.otherDetailsEmpMap == null)
                    {
                        $rootScope.addMessage("Please enter the master values before creating employee", 1);
                    }
                }
            }
            ;
            function setStatus(data)
            {
                $scope.statusList = data;
                $scope.statusListCreate = [];
                if (angular.isDefined($scope.statusList) && $scope.statusList !== null) {
                    for (var i = 0; i < $scope.statusList.length; i++) {
                        if ($scope.statusList[i].label === 'Selected' || $scope.statusList[i].label === 'Awaiting Decision') {
                            $scope.statusList[i].label = $rootScope.translateValue("EMPLOYEE." + $scope.statusList[i].label);
                            $scope.statusListCreate.push($scope.statusList[i]);
                        }
                    }
                }
                if (angular.isDefined($scope.statusListCreate) && $scope.statusListCreate !== null && $scope.statusListCreate.length !== 0) {
                    $scope.employee.workstatus = $scope.statusListCreate[0].value;
                }
            }
            ;
//            function retrieveEmployeeCode() {
//                Employee.retrieveEmployeeCode(function(data) {
//                    $scope.employeeCode = data;
//                });
//            }
//            ;
            $scope.setDesignation = function (designationDetail)
            {
                console.log("d detail" + JSON.stringify(designationDetail))
                $scope.desigIds = [];
                angular.forEach(designationDetail, function (desig)
                {
                    $scope.desigIds.push(desig.value);
                });
                $scope.employee.workdeg = angular.copy($scope.desigIds);
            }
            function setDesignations(data) {
                if (data != null && angular.isDefined(data) && data.length > 0) {
                    $scope.designationList = [];
//                    $scope.designationList.push(
//                            {
//                                designationName: '<strong>Designation</strong>',
//                                multiSelectGroup: true
//                            });
                    angular.forEach(data, function (item) {
                        item.designationName = $rootScope.translateValue("DESIG_NM." + item.displayName);

                        $scope.designationList.push(item);
                        item.modelNumber = item.id;
                        $scope.designationListForWorkAs.push(item);
                    });
                }
                if (!$scope.designationList || $scope.designationList === null) {
                    $scope.designationList = [];
                }

            }

            function setLocations(data) {
                $scope.allLocations = data.sort($scope.predicateBy("label"));
                angular.forEach($scope.allLocations, function (locationData) {
                    $scope.select2Locations.push({id: locationData.value, text: locationData.label});
                });
                $("#location").select2('val', null);
            }
            function setDepartments(data) {
                $scope.departmentList = [];
                if (data != null && angular.isDefined(data) && data.length > 0) {
                    angular.forEach(data, function (item) {
//                        item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                        $scope.departmentList.push(item);
                    });
                }
                $scope.departmentListDropdown = [];
                $.merge($scope.departmentListDropdown, angular.copy($scope.departmentList));
                if ($scope.selectedParent !== undefined) {
                    $scope.selectedParent = undefined;
                }
            }
            function retrieveCustomField() {
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageEmployees");
                templateData.then(function (section) {
                    // Method modified by Shifa Salheen on 20 April for implementing sequence number
                    $scope.customPersonalTemplateDate = angular.copy(section['PERSONAL']);
                    $scope.personalTemplate = $rootScope.getCustomDataInSequence($scope.customPersonalTemplateDate);
                    $scope.addDateCustomField($scope.personalTemplate, "PERSONAL");

                    $scope.customContactTemplateDate = angular.copy(section['CONTACT']);
                    $scope.contactTemplate = $rootScope.getCustomDataInSequence($scope.customContactTemplateDate);
                    $scope.addDateCustomField($scope.contactTemplate, "CONTACT");

                    $scope.customIdentificationTemplateDate = angular.copy(section['IDENTIFICATION']);
                    $scope.identificationTemplate = $rootScope.getCustomDataInSequence($scope.customIdentificationTemplateDate);
                    $scope.addDateCustomField($scope.identificationTemplate, "IDENTIFICATION");

                    $scope.customOtherTemplateDate = angular.copy(section['OTHER']);
                    $scope.otherTemplate = $rootScope.getCustomDataInSequence($scope.customOtherTemplateDate);
                    $scope.addDateCustomField($scope.otherTemplate, "OTHER");

                    $scope.customWorkTemplateDate = angular.copy(section['HKGWORK']);
                    $scope.hkgworkTemplate = $rootScope.getCustomDataInSequence($scope.customWorkTemplateDate);
                    $scope.addDateCustomField($scope.hkgworkTemplate, "HKGWORK");

                    $scope.customEducationTemplateDate = angular.copy(section['EDUCATION']);
                    $scope.educationTemplate = $rootScope.getCustomDataInSequence($scope.customEducationTemplateDate);
                    $scope.addDateCustomField($scope.educationTemplate, "EDUCATION");

                    $scope.customExperienceTemplateDate = angular.copy(section['EXPERIENCE']);
                    $scope.experienceTemplate = $rootScope.getCustomDataInSequence($scope.customExperienceTemplateDate);
                    $scope.addDateCustomField($scope.experienceTemplate, "EXPERIENCE");

                    $scope.customFamilyTemplateDate = angular.copy(section['FAMILY']);
                    $scope.familyTemplate = $rootScope.getCustomDataInSequence($scope.customFamilyTemplateDate);
                    $scope.addDateCustomField($scope.familyTemplate, "FAMILY");

                    $scope.customPolicyTemplateDate = angular.copy(section['POLICY']);
                    $scope.policyTemplate = $rootScope.getCustomDataInSequence($scope.customPolicyTemplateDate);
                    $scope.addDateCustomField($scope.policyTemplate, "POLICY");

                    $scope.customGeneralTemplateDate = angular.copy(section['genralSection']);
                    $scope.generaltemplate = $rootScope.getCustomDataInSequence($scope.customGeneralTemplateDate);
                    $scope.addDateCustomField($scope.generaltemplate, "genralSection");


                }, function (reason) {
                }, function (update) {
                });
            }
            ;
            $scope.addDateCustomField = function (template, sectionName) {
                if (template !== null && template !== undefined)
                {
                    angular.forEach(template, function (updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            if (!$scope.listOfModelsOfDateType[sectionName]) {
                                $scope.listOfModelsOfDateType[sectionName] = [];
                            }
                            $scope.listOfModelsOfDateType[sectionName].push(updateTemplate.model);
                        }
                    });
                }
            }
            $scope.checkIsUpdate = function ()
            {
                $scope.isUpdate = true;
            }
            $scope.retrieveAvatar = function () {
//                return "api/employee/retrieve/avatar";
                return $rootScope.appendAuthToken($rootScope.apipath + "employee/retrieve/avatar" + '?decache=' + $rootScope.randomCount);
            }
//            $scope.setPassingYear = function(minYear) {
//                if (minYear && angular.isDefined($scope.passingYears)) {
//                    $scope.passingYearArray = [];
//                    for (var i = 0; i < $scope.passingYears.length; i++) {
//                        if (minYear < $scope.passingYears[i].label) {
//                            $scope.passingYearArray.push($scope.passingYears[i]);
//                        }
//
//                    }
//                }
//            }
            $scope.setPassportExpiryDate = function ()
            {

                var expireDate = $scope.employee.empPassportIssueOn.getFullYear() + 10;
                $scope.expiry = angular.copy($scope.employee.empPassportIssueOn);
                var expiryDate = $scope.expiry.setFullYear(expireDate);
            }
//            $scope.updateYearsOfPassing = function() {
//                if (angular.isDefined($scope.employee.dob)) {
//                    var minYear = $scope.employee.dob.getYear() + 1900;
//                    if ($scope.employee.dob.getYear()) {
//                        $scope.setPassingYear(minYear);
//                        if ($scope.employee.joiningDate !== undefined) {
//                            if ($scope.employee.joiningDate <= $scope.employee.dob) {
//                                $scope.employee.joiningDate = undefined;
//                            }
//                        }
//                    }
//                }
//            };
            $scope.changeholdername = function () {
                if (!angular.isDefined($scope.employee.empName)) {
                    delete $scope.policyHoldersMap['E0'];
                } else {
                    $scope.policyHoldersMap['E0'] = $scope.employee.empName;
                }
            };
            $scope.predicateBy = function (prop) {
                return function (a, b) {
                    if (a[prop] > b[prop]) {
                        return 1;
                    } else if (a[prop] < b[prop]) {
                        return -1;
                    }
                    return 0;
                };
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

            $scope.$watch('employee.nativeaddress', function (newValue) {
                if (newValue !== undefined && newValue !== null) {
                    if (newValue instanceof Object) {
                        $scope.employee.nativeaddress = newValue.id;
                    }
                }
            });

            $scope.multiLocations1 = {
                multiple: false,
                closeOnSelect: false,
                placeholder: "Select District",
                allowClear: true,
                initSelection: function (element, callback) {
                    if ($scope.employee.nativeaddress !== undefined && $scope.employee.nativeaddress !== null && $scope.employee.nativeaddress.length > 0) {
                        var data = {};
                        angular.forEach($scope.select2Locations, function (item) {
                            if ($scope.employee.nativeaddress instanceof Object === false) {
                                if (item.id === $scope.employee.nativeaddress) {
                                    data = angular.copy(item);
                                }
                            } else {
                                if (item.id === $scope.employee.nativeaddress.id) {
                                    data = angular.copy(item);
                                }
                            }
                        });
//                        if (data.length > 0) {
                        callback(data);
//                        }
                    }
                },
                data: function () {
                    return {'results': $scope.select2Locations};
                }
            };

            $scope.doesEmployeeNameExist = function (name) {
                $scope.invalidname = true;
                if (name && name.length > 0) {
                    Employee.doesEmployeeNameExist(name, function (resp) {

                        if (resp.data) {
                            if (!$scope.isCreate && name != $scope.oldname) {
                                $scope.notUniqueEmpname = true;
                            }
                            if ($scope.isCreate) {
                                $scope.notUniqueEmpname = true;
                            }
                        } else {
                            $scope.notUniqueEmpname = false;
                            var split = name.trim().split(" ");
                            if (split.length == 3) {
                                $scope.invalidname = false;
                            } else {
                                $scope.invalidname = true;
                            }
                        }
                    }, function () {
                    });
                } else {
                    $scope.notUniqueEmpname = false;
                }
            };
            $scope.empNameFormat = function (name)
            {
                if (name && name.length > 0) {
                    var split = name.trim().split(" ");
                    if (split.length == 3) {
                        $scope.invalidname = false;
                    } else {
                        $scope.invalidname = true;
                    }
                }
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

            function getDepartmentOfId(node, id) {
                if (node === null || node === undefined) {
                    return;
                }
                if (node.id === id) {
                    $scope.selectedDepObj = node;
                    return;
                }
                if (node.children === null || node.children.length === 0) {
                    return;
                } else {
                    for (var i = 0; i < node.children.length > 0; i++) {
                        getDepartmentOfId(node.children[i], id);
                    }
                }
            }

            //Called when dropdown is clicked
            $scope.setSelectedParent = function (selected) {
                $scope.isDepInvalid = true;
                if (!angular.equals(selected, {})) {
                    $scope.searchDepId = selected.id;
                    $scope.invalidParent = false;
                    $scope.selectedParent = selected.currentNode;
                    $scope.parentId = selected.currentNode.id;
                    $scope.isDepInvalid = false;
                    selected.currentNode.displayName = $rootScope.translateValue('DPT_NM.' + selected.currentNode.displayName);
                    $scope.setShitsByDep($scope.parentId);
                }
            };
            $scope.setShitsByDep = function (id) {
                $scope.shiftList = [];
                Employee.retrieveShiftDepMap({primaryKey: id}, function (data) {
                    if (data != null && angular.isDefined(data) && data.length > 0) {
                        angular.forEach(data, function (item) {
                            item.label = $rootScope.translateValue("SHIFT_NM." + item.label);
                            $scope.shiftList.push(item);
                        });
                    }
                });
            }
            ;
            $scope.scrollTo = function (eID) {
//                alert('scroll called   '+eID);
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

            $scope.phoneValidate = function () {
                if (angular.isDefined($scope.employee.phnno) && $scope.employee.phnno != null) {
                    if ($scope.employee.phnno.length >= 10) {
                        if (!validateContact($scope.employee.phnno)) {
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
                }
            };
            $scope.familyPhoneValidate = function (familyphone)
            {
                if (!validateContact(familyphone)) {
                    $scope.isfamilyContact = false;
                    return;
                } else {
                    $scope.isfamilyContact = true;
                }

            }
            $scope.altphoneValidate = function () {
                if (angular.isDefined($scope.employee.altphnno)) {
                    if ($scope.employee.altphnno.length >= 10) {
                        if (!validateContact($scope.employee.altphnno)) {
                            $scope.isAltPhoneInvalid = true;
                            return;
                        } else {
                            $scope.isAltPhoneInvalid = false;
                        }
                    } else if ($scope.employee.altphnno.length == 0) {
                        $scope.isAltPhoneInvalid = false;
                        return;
                    }
                    else {
                        $scope.isAltPhoneInvalid = true;
                        return;
                    }
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
            };
            
            $scope.retrieveTransferredEmployee = function(id){
                $scope.isCreate = true;
                $scope.reload = false;
                $scope.invalidname = false;
                $scope.notUniqueEmpname = false;
                $scope.invalidname = false;
                $scope.searchtext = undefined;
                $scope.isfamilyContact = true;
                $scope.displayEmployeeFlag = 'view';
                $rootScope.maskLoading();
                Employee.retrieveByIdForTransfer({primaryKey: id}, function (data) {
                    console.log("data :::"+JSON.stringify(data));
                    if ($scope.empTypes != null) {
                        $scope.tempListForEmployeetypes = angular.copy($scope.empTypes);
                        $scope.editempTypes = [];
                        var shortcutcode;
                        if (data.empType == null)
                        {
                            data.empType = $scope.tempListForEmployeetypes[0].value;
                            shortcutcode = $scope.tempListForEmployeetypes[0].value;
                        }
                        for (var i = 0; i < $scope.tempListForEmployeetypes.length; i++)
                        {
                            if ($scope.tempListForEmployeetypes[i].value == data.empType)
                            {
                                shortcutcode = $scope.tempListForEmployeetypes[i].shortcutCode;
                                break;

                            }

                        }

                        for (var i = 0; i < $scope.tempListForEmployeetypes.length; i++)
                        {
                            if ($scope.tempListForEmployeetypes[i].shortcutCode <= shortcutcode)
                            {
                                $scope.editempTypes.push($scope.tempListForEmployeetypes[i]);
                            }

                        }

                    }
                    $scope.oldNativeId = angular.copy(data.nativeAddressId);

                    if (!data.isNativeAddressSame)
                    {

                        $scope.employee.isNativeAddressSame = false;
                    }
                    else
                    {
                        $scope.employee.isNativeAddressSame = true;
                    }
                    if (data.nativepincode === null) {
                        data.nativepincode = undefined;
                    }
                    if (data.altphnno === null) {
                        data.altphnno = undefined;
                    }
                    if (angular.isDefined($scope.otherDetailsEmpMap) && $scope.otherDetailsEmpMap !== null) {
                        for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                            $scope.otherDetailsEmpMap[i].isActive = false;
                        }
                    }
                    if ($scope.otherDetailsEmpMap !== undefined && $scope.otherDetailsEmpMap !== null && data.otherDetailsOfEmployeeKeysString !== undefined && data.otherDetailsOfEmployeeKeysString !== null) {
                        var temp = data.otherDetailsOfEmployeeKeysString.split(',');
                        for (var j = 0; j < temp.length; j++) {
                            for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                                var a = "#" + $scope.otherDetailsEmpMap[i].value + "#";
                                if (temp[j] === a) {
                                    $scope.otherDetailsEmpMap[i].isActive = true;
                                    break;
                                }

                            }
                        }
                    }
                    $scope.policyHoldersMap['E0'] = data.empName;
                    for (var i = 0; i < data.family.length; i++) {
                        $scope.policyHoldersMap['F' + i] = data.family[i].firstName;
                    }
                    if (angular.isDefined(data.edu) && data.edu.length > 0) {
                        $scope.hasAnyEdu = true;
                    } else {
                        $scope.hasAnyEdu = false;
                    }
                    if (angular.isDefined(data.exp) && data.exp.length > 0) {
                        $scope.hasAnyExp = true;
                    } else {
                        $scope.hasAnyExp = false;
                    }
                    if (angular.isDefined(data.family) && data.family.length > 0) {
                        $scope.hasAnyFamily = true;
                    } else {
                        $scope.hasAnyFamily = false;
                    }
                    if (angular.isDefined(data.policy) && data.policy.length > 0) {
                        $scope.hasAnyPolicy = true;
                    } else {
                        $scope.hasAnyPolicy = false;
                    }
                    if (angular.isDefined(data.otherdocs) && data.otherdocs.length > 0) {
                        $scope.hasAnyDoc = true;
                    } else {
                        $scope.hasAnyDoc = false;
                    }
                    $("#multiLocations").select2("data", data.currentaddress);
                    angular.forEach($scope.select2Locations, function (item) {
                        if (item.id === data.currentaddress) {
                            data.currentaddress = item;
                        }
                    });
                    $("#multiLocations1").select2("data", data.nativeaddress);
                    angular.forEach($scope.select2Locations, function (item) {
                        if (item.id === data.nativeaddress) {
                            data.nativeaddress = item;
                        }
                    });
                    $("#input_empReportsTo").select2("data", "");
                    retrieveCustomField();

                    if (angular.isDefined(data.personalCustom) && data.personalCustom != null) {
                        $scope.personalData = angular.copy(data.personalCustom);
                        if (!!$scope.personalData && !!$scope.listOfModelsOfDateType["PERSONAL"]) {
                            angular.forEach($scope.listOfModelsOfDateType["PERSONAL"], function (listOfModel)
                            {
                                if ($scope.personalData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.personalData[listOfModel] !== null && $scope.personalData[listOfModel] !== undefined)
                                    {
                                        $scope.personalData[listOfModel] = new Date($scope.personalData[listOfModel]);
                                    } else
                                    {
                                        $scope.personalData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.generalCustom) && data.generalCustom != null) {
                        $scope.generalData = angular.copy(data.generalCustom);
                        if (!!$scope.generalData && !!$scope.listOfModelsOfDateType["genralSection"]) {
                            angular.forEach($scope.listOfModelsOfDateType["genralSection"], function (listOfModel)
                            {
                                if ($scope.generalData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.generalData[listOfModel] !== null && $scope.generalData[listOfModel] !== undefined)
                                    {
                                        $scope.generalData[listOfModel] = new Date($scope.generalData[listOfModel]);
                                    } else
                                    {
                                        $scope.generalData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.contactCustom) && data.contactCustom != null) {
                        $scope.contactData = angular.copy(data.contactCustom);
                        if (!!$scope.contactData && !!$scope.listOfModelsOfDateType["CONTACT"]) {
                            angular.forEach($scope.listOfModelsOfDateType["CONTACT"], function (listOfModel)
                            {
                                if ($scope.contactData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.contactData[listOfModel] !== null && $scope.contactData[listOfModel] !== undefined)
                                    {
                                        $scope.contactData[listOfModel] = new Date($scope.contactData[listOfModel]);
                                    } else
                                    {
                                        $scope.contactData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.identificationCustom) && data.identificationCustom != null) {
                        $scope.identificationData = angular.copy(data.identificationCustom);
                        if (!!$scope.identificationData && !!$scope.listOfModelsOfDateType["IDENTIFICATION"]) {
                            angular.forEach($scope.listOfModelsOfDateType["IDENTIFICATION"], function (listOfModel)
                            {
                                if ($scope.identificationData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.identificationData[listOfModel] !== null && $scope.identificationData[listOfModel] !== undefined)
                                    {
                                        $scope.identificationData[listOfModel] = new Date($scope.identificationData[listOfModel]);
                                    } else
                                    {
                                        $scope.identificationData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.otherCustom) && data.otherCustom != null) {
                        $scope.otherData = angular.copy(data.otherCustom);
                        if (!!$scope.otherData && !!$scope.listOfModelsOfDateType["OTHER"]) {
                            angular.forEach($scope.listOfModelsOfDateType["OTHER"], function (listOfModel)
                            {
                                if ($scope.otherData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.otherData[listOfModel] !== null && $scope.otherData[listOfModel] !== undefined)
                                    {
                                        $scope.otherData[listOfModel] = new Date($scope.otherData[listOfModel]);
                                    } else
                                    {
                                        $scope.otherData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    $scope.reload = true;
                    $scope.educationIndex = data.edu.length;
                    $scope.expIndex = data.exp.length;
                    $scope.familyIndex = data.family.length;
                    $scope.policyIndex = data.policy.length;
                    $scope.otherDocIndex = data.otherdocs.length;
                    for (var i = 0; i < data.edu.length; i++) {
                        if (angular.isDefined(data.edu[i].educationCustom) && data.edu[i].educationCustom != null) {
                            $scope.educationData[i] = angular.copy(data.edu[i].educationCustom);
                        } else {
                            $scope.educationData[i] = {};
                        }
                        $scope.educationdbType = angular.copy(data.edu[i].educationDbType);
                    }
                    for (var i = 0; i < data.exp.length; i++) {
                        data.exp[i].startedFrom = new Date(data.exp[i].startedFrom);
                        data.exp[i].workedTill = new Date(data.exp[i].workedTill);

                        if (angular.isDefined(data.exp[i].experienceCustom) && data.exp[i].experienceCustom != null) {
                            $scope.experienceData[i] = angular.copy(data.exp[i].experienceCustom);
                        } else {
                            $scope.experienceData[i] = {};
                        }
                        $scope.experiencedbType = angular.copy(data.exp[i].experienceDbType);
                    }
                    for (var i = 0; i < data.family.length; i++) {
                        data.family[i].dateOfBirth = new Date(data.family[i].dateOfBirth);
                        if (angular.isDefined(data.family[i].familyCustom) && data.family[i].familyCustom != null) {
                            $scope.familyData[i] = angular.copy(data.family[i].familyCustom);
                        } else {
                            $scope.familyData[i] = {index: $scope.familyIndex};
                        }
                        $scope.familydbType = angular.copy(data.family[i].familyDbType);
                    }
                    for (var i = 0; i < data.policy.length; i++) {
                        if (angular.isDefined(data.policy[i].policyCustom) && data.policy[i].policyCustom != null) {

                            $scope.policyData[i] = angular.copy(data.policy[i].policyCustom);
                        } else {
                            $scope.policyData[i] = {};
                        }
                        $scope.policydbType = angular.copy(data.policy[i].policyDbType);

                    }
                    data.dob = new Date(data.dob);
                    data.joiningDate = new Date(data.joiningDate);
                    if (data.empPucExpiresOn != null) {
                        data.empPucExpiresOn = new Date(data.empPucExpiresOn);
                    }
                    if (data.empPassportIssueOn != null) {
                        data.empPassportIssueOn = new Date(data.empPassportIssueOn);
                    }
                    if (data.empPassportExpiresOn != null) {
                        data.empPassportExpiresOn = new Date(data.empPassportExpiresOn);
                    }
                    $scope.employee = angular.copy(data);
                    $scope.employee.custom5 = data.id;
                    $scope.employee.employeeCode = undefined;
                    $scope.employee.joiningDate = undefined;
                    $scope.employee.workemailId = undefined;
                    $scope.employee.workstatus = undefined;
                    $scope.employee.workdeg = undefined;
                    $scope.employee.reportsToId = undefined;
                    $scope.employee.reportsToName = undefined;
                    $scope.employee.workshift = undefined;
                    $scope.employee.ipaddress = undefined;
                    $scope.employee.id = undefined;
                    $scope.employee.currentAddressId = undefined;
                    $scope.employee.nativeAddressId = undefined;
                    $scope.employee.departmentId = undefined;
                    $scope.employee.selecteddep = undefined;
                    $scope.employee.relievingDate = undefined;
                    for (var i = 0; i < $scope.employee.exp.length; i++) {
                        if (angular.isDefined($scope.employee.exp[i]) && $scope.employee.exp[i] != null) {
                            $scope.employee.exp[i].id = undefined;
                        } 
                    }
                    for (var i = 0; i < $scope.employee.edu.length; i++) {
                        if (angular.isDefined($scope.employee.edu[i]) && $scope.employee.edu[i] != null) {
                            $scope.employee.edu[i].id = undefined;
                        } 
                    }
                    for (var i = 0; i < $scope.employee.policy.length; i++) {
                        if (angular.isDefined($scope.employee.policy[i]) && $scope.employee.policy[i] != null) {
                            $scope.employee.policy[i].id = undefined;
                        } 
                    }
                    //Re initialize family details on every employee change.
                    $scope.viewAllFamilyDetails();
                    $scope.empName = data.empName;
                    setDefault();
                    var date = new Date($scope.employee.dob);
                    var minYear = date.getYear() + 1900;

                    if ($scope.employee.gender == null) {
                        $scope.employee.gender = "male";
                    }
                    $scope.phoneValidate();
                    $scope.oldname = $scope.employee.empName;
                    $scope.nativevalid = false;
                    if ($scope.nativeValid = false && angular.isUndefined($scope.employee.nativeaddress) && $scope.employee.nativeaddress == null && angular.isUnDefined($scope.employee.nativefulladdress) && $scope.employee.nativefulladdress == null && angular.isUnDefined($scope.employee.nativepincode) && $scope.employee.nativepincode == null) {
                        $scope.nativevalid = true;
                    }
                    $rootScope.unMaskLoading();
                    $scope.makeAScroll();
                }, function () {
                    $rootScope.unMaskLoading();
                    $scope.makeAScroll();
                });
            }

            $scope.retrieveEmployeeById = function (id) {
                $scope.reload = false;
                $scope.invalidname = false;
                $scope.notUniqueEmpname = false;
                $scope.invalidname = false;
                $scope.isCreate = false;
                $scope.searchtext = undefined;
                $scope.isfamilyContact = true;
                $scope.displayEmployeeFlag = 'view';
                $rootScope.maskLoading();
//                $scope.makeAScroll();
                Employee.retrieveEmployeeDetail({primaryKey: id}, function (data) {
                    if (!data.isUserRoleHigher)
                    {
                        $location.path('/accessdenied');
                    }
                    if ($scope.empTypes != null) {
                        $scope.tempListForEmployeetypes = angular.copy($scope.empTypes);
                        $scope.editempTypes = [];
                        var shortcutcode;
                        if (data.empType == null)
                        {
                            data.empType = $scope.tempListForEmployeetypes[0].value;
                            shortcutcode = $scope.tempListForEmployeetypes[0].value;
                        }
                        for (var i = 0; i < $scope.tempListForEmployeetypes.length; i++)
                        {
                            if ($scope.tempListForEmployeetypes[i].value == data.empType)
                            {
                                shortcutcode = $scope.tempListForEmployeetypes[i].shortcutCode;
                                break;

                            }

                        }

                        for (var i = 0; i < $scope.tempListForEmployeetypes.length; i++)
                        {
                            if ($scope.tempListForEmployeetypes[i].shortcutCode <= shortcutcode)
                            {
                                $scope.editempTypes.push($scope.tempListForEmployeetypes[i]);
                            }

                        }

                    }
                    $scope.oldNativeId = angular.copy(data.nativeAddressId);

                    if (!data.isNativeAddressSame)
                    {

                        $scope.employee.isNativeAddressSame = false;
                    }
                    else
                    {
                        $scope.employee.isNativeAddressSame = true;
                    }
                    if (data.nativepincode === null) {
                        data.nativepincode = undefined;
                    }
                    if (data.altphnno === null) {
                        data.altphnno = undefined;
                    }
                    if (angular.isDefined($scope.otherDetailsEmpMap) && $scope.otherDetailsEmpMap !== null) {
                        for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                            $scope.otherDetailsEmpMap[i].isActive = false;
                        }
                    }
                    if ($scope.otherDetailsEmpMap !== undefined && $scope.otherDetailsEmpMap !== null && data.otherDetailsOfEmployeeKeysString !== undefined && data.otherDetailsOfEmployeeKeysString !== null) {
                        var temp = data.otherDetailsOfEmployeeKeysString.split(',');
                        for (var j = 0; j < temp.length; j++) {
                            for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                                var a = "#" + $scope.otherDetailsEmpMap[i].value + "#";
                                if (temp[j] === a) {
                                    $scope.otherDetailsEmpMap[i].isActive = true;
                                    break;
                                }

                            }
                        }
                    }
                    if ($scope.departmentList !== null && $scope.departmentList !== undefined) {
                        for (var i = 0; i < $scope.departmentList.length; i++) {
                            getDepartmentOfId($scope.departmentList[i], data.selecteddep);
                        }
                    }
                    $scope.policyHoldersMap['E0'] = data.empName;
                    for (var i = 0; i < data.family.length; i++) {
                        $scope.policyHoldersMap['F' + i] = data.family[i].firstName;
                    }
                    if (angular.isDefined(data.edu) && data.edu.length > 0) {
                        $scope.hasAnyEdu = true;
                    } else {
                        $scope.hasAnyEdu = false;
                    }
                    if (angular.isDefined(data.exp) && data.exp.length > 0) {
                        $scope.hasAnyExp = true;
                    } else {
                        $scope.hasAnyExp = false;
                    }
                    if (angular.isDefined(data.family) && data.family.length > 0) {
                        $scope.hasAnyFamily = true;
                    } else {
                        $scope.hasAnyFamily = false;
                    }
                    if (angular.isDefined(data.policy) && data.policy.length > 0) {
                        $scope.hasAnyPolicy = true;
                    } else {
                        $scope.hasAnyPolicy = false;
                    }
                    if (angular.isDefined(data.otherdocs) && data.otherdocs.length > 0) {
                        $scope.hasAnyDoc = true;
                    } else {
                        $scope.hasAnyDoc = false;
                    }
                    $scope.invalidParent = false;
                    $scope.isDepInvalid = false;
                    $scope.selectedParent = angular.copy($scope.selectedDepObj);

                    console.log("parent" + JSON.stringify($scope.selectedParent));
                    if (angular.isDefined($scope.selectedParent) && $scope.selectedParent !== undefined) {
                        $scope.selectedParent.displayName = $rootScope.translateValue('DPT_NM.' + $scope.selectedParent.displayName);
                    }
                    if ($scope.selectedDepObj != undefined) {
                        $scope.parentId = $scope.selectedDepObj.id;
                    } else {
                        $scope.isDepInvalid = true;
                    }
                    $("#multiLocations").select2("data", data.currentaddress);
                    angular.forEach($scope.select2Locations, function (item) {
                        if (item.id === data.currentaddress) {
                            data.currentaddress = item;
                        }
                    });
                    $("#multiLocations1").select2("data", data.nativeaddress);
                    angular.forEach($scope.select2Locations, function (item) {
                        if (item.id === data.nativeaddress) {
                            data.nativeaddress = item;
                        }
                    });
                    $("#input_empReportsTo").select2("data", "");
//                    if (data.reportsToId == null) {
//                        $(document).ready(function() {
//                            $("#input_empReportsTo").select2({width: 'resolve'});
//                        });
//                    }

                    if (data.reportsToId != null) {
                        $scope.names = {
                            id: data.reportsToId,
                            text: data.reportsToName
                        };
                        $("#input_empReportsTo").select2("data", $scope.names);
                    }

                    if ($scope.selectedDepObj != undefined) {
                        $scope.setShitsByDep($scope.selectedDepObj.id);
                    }
                    retrieveCustomField();

                    if (angular.isDefined(data.personalCustom) && data.personalCustom != null) {
                        $scope.personalData = angular.copy(data.personalCustom);
                        if (!!$scope.personalData && !!$scope.listOfModelsOfDateType["PERSONAL"]) {
                            angular.forEach($scope.listOfModelsOfDateType["PERSONAL"], function (listOfModel)
                            {
                                if ($scope.personalData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.personalData[listOfModel] !== null && $scope.personalData[listOfModel] !== undefined)
                                    {
                                        $scope.personalData[listOfModel] = new Date($scope.personalData[listOfModel]);
                                    } else
                                    {
                                        $scope.personalData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.generalCustom) && data.generalCustom != null) {
                        $scope.generalData = angular.copy(data.generalCustom);
                        if (!!$scope.generalData && !!$scope.listOfModelsOfDateType["genralSection"]) {
                            angular.forEach($scope.listOfModelsOfDateType["genralSection"], function (listOfModel)
                            {
                                if ($scope.generalData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.generalData[listOfModel] !== null && $scope.generalData[listOfModel] !== undefined)
                                    {
                                        $scope.generalData[listOfModel] = new Date($scope.generalData[listOfModel]);
                                    } else
                                    {
                                        $scope.generalData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.contactCustom) && data.contactCustom != null) {
                        $scope.contactData = angular.copy(data.contactCustom);
                        if (!!$scope.contactData && !!$scope.listOfModelsOfDateType["CONTACT"]) {
                            angular.forEach($scope.listOfModelsOfDateType["CONTACT"], function (listOfModel)
                            {
                                if ($scope.contactData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.contactData[listOfModel] !== null && $scope.contactData[listOfModel] !== undefined)
                                    {
                                        $scope.contactData[listOfModel] = new Date($scope.contactData[listOfModel]);
                                    } else
                                    {
                                        $scope.contactData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.identificationCustom) && data.identificationCustom != null) {
                        $scope.identificationData = angular.copy(data.identificationCustom);
                        if (!!$scope.identificationData && !!$scope.listOfModelsOfDateType["IDENTIFICATION"]) {
                            angular.forEach($scope.listOfModelsOfDateType["IDENTIFICATION"], function (listOfModel)
                            {
                                if ($scope.identificationData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.identificationData[listOfModel] !== null && $scope.identificationData[listOfModel] !== undefined)
                                    {
                                        $scope.identificationData[listOfModel] = new Date($scope.identificationData[listOfModel]);
                                    } else
                                    {
                                        $scope.identificationData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.otherCustom) && data.otherCustom != null) {
                        $scope.otherData = angular.copy(data.otherCustom);
                        if (!!$scope.otherData && !!$scope.listOfModelsOfDateType["OTHER"]) {
                            angular.forEach($scope.listOfModelsOfDateType["OTHER"], function (listOfModel)
                            {
                                if ($scope.otherData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.otherData[listOfModel] !== null && $scope.otherData[listOfModel] !== undefined)
                                    {
                                        $scope.otherData[listOfModel] = new Date($scope.otherData[listOfModel]);
                                    } else
                                    {
                                        $scope.otherData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    if (angular.isDefined(data.hkgworkCustom) && data.hkgworkCustom != null) {
                        $scope.hkgworkData = angular.copy(data.hkgworkCustom);
                        if (!!$scope.hkgworkData && !!$scope.listOfModelsOfDateType["HKGWORK"]) {
                            angular.forEach($scope.listOfModelsOfDateType["HKGWORK"], function (listOfModel)
                            {
                                if ($scope.hkgworkData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.hkgworkData[listOfModel] !== null && $scope.hkgworkData[listOfModel] !== undefined)
                                    {
                                        $scope.hkgworkData[listOfModel] = new Date($scope.hkgworkData[listOfModel]);
                                    } else
                                    {
                                        $scope.hkgworkData[listOfModel] = '';
                                    }
                                }
                            });
                        }
                    }
                    $scope.reload = true;
                    $scope.educationIndex = data.edu.length;
                    $scope.expIndex = data.exp.length;
                    $scope.familyIndex = data.family.length;
                    $scope.policyIndex = data.policy.length;
                    $scope.otherDocIndex = data.otherdocs.length;
                    for (var i = 0; i < data.edu.length; i++) {
                        if (angular.isDefined(data.edu[i].educationCustom) && data.edu[i].educationCustom != null) {
                            $scope.educationData[i] = angular.copy(data.edu[i].educationCustom);
                        } else {
                            $scope.educationData[i] = {};
                        }
                        $scope.educationdbType = angular.copy(data.edu[i].educationDbType);
                    }
                    for (var i = 0; i < data.exp.length; i++) {
                        data.exp[i].startedFrom = new Date(data.exp[i].startedFrom);
                        data.exp[i].workedTill = new Date(data.exp[i].workedTill);

                        if (angular.isDefined(data.exp[i].experienceCustom) && data.exp[i].experienceCustom != null) {
                            $scope.experienceData[i] = angular.copy(data.exp[i].experienceCustom);
                        } else {
                            $scope.experienceData[i] = {};
                        }
                        $scope.experiencedbType = angular.copy(data.exp[i].experienceDbType);
                    }
                    for (var i = 0; i < data.family.length; i++) {
                        data.family[i].dateOfBirth = new Date(data.family[i].dateOfBirth);
                        if (angular.isDefined(data.family[i].familyCustom) && data.family[i].familyCustom != null) {
                            $scope.familyData[i] = angular.copy(data.family[i].familyCustom);
                        } else {
                            $scope.familyData[i] = {index: $scope.familyIndex};
                        }
                        $scope.familydbType = angular.copy(data.family[i].familyDbType);
                    }
                    for (var i = 0; i < data.policy.length; i++) {
                        if (angular.isDefined(data.policy[i].policyCustom) && data.policy[i].policyCustom != null) {

                            $scope.policyData[i] = angular.copy(data.policy[i].policyCustom);
                        } else {
                            $scope.policyData[i] = {};
                        }
                        $scope.policydbType = angular.copy(data.policy[i].policyDbType);

                    }
                    data.dob = new Date(data.dob);
                    data.joiningDate = new Date(data.joiningDate);
                    if (data.empPucExpiresOn != null) {
                        data.empPucExpiresOn = new Date(data.empPucExpiresOn);
                    }
                    if (data.empPassportIssueOn != null) {
                        data.empPassportIssueOn = new Date(data.empPassportIssueOn);
                    }
                    if (data.empPassportExpiresOn != null) {
                        data.empPassportExpiresOn = new Date(data.empPassportExpiresOn);
                    }
                    if (data.relievingDate != null) {
                        data.relievingDate = new Date(data.relievingDate);
                    }
                    if (data.workstatus == 3)
                    {
                        $scope.isResigned = true;
                    }
                    else
                    {
                        $scope.isResigned = false;
                    }
                    $scope.employee = angular.copy(data);
                    $scope.todayDate = new Date($scope.today).setHours(0, 0, 0, 0);
                    if ($scope.employee.relievingDate != null && data.workstatus == 3) {
                        $scope.employee.relievingDateWithoutTime = new Date($scope.employee.relievingDate).setHours(0, 0, 0, 0);
                        if ((angular.equals($scope.employee.relievingDateWithoutTime, $scope.todayDate)) || ($scope.employee.relievingDateWithoutTime < $scope.todayDate))
                        {
                            $scope.isEmployeeRelieved = true;
                        }
                        else
                        {
                            $scope.isEmployeeRelieved = false;
                        }
                    } else
                    {
                        $scope.isEmployeeRelieved = false;
                    }
//                    $scope.employee = angular.copy(data);
                    //Re initialize family details on every employee change.
                    $scope.viewAllFamilyDetails();
//                    $("#multiLocations").select2("data", []);
////                    $("#multiLocations").select2("data", data.currentaddress);
//                    angular.forEach($scope.select2Locations, function(item) {
//                        if (item.id ===    $scope.employee.currentaddress) {
//                               $scope.employee.currentaddress = item;
//                        }
//                    });
                    $scope.empName = data.empName;
                    $scope.empId = data.userId;
                    $scope.empstatus = data.workstatus;

                    setDefault();
                    var date = new Date($scope.employee.dob);
                    var minYear = date.getYear() + 1900;
//                    $scope.setPassingYear(minYear);

                    if ($scope.employee.gender == null) {
                        $scope.employee.gender = "male";
                    }
                    if ($scope.employee.workstatus == null && $scope.statusList !== null && $scope.statusList !== undefined) {
                        $scope.employee.workstatus = $scope.statusList[0].value;
                    }
                    $scope.phoneValidate();
                    $scope.oldname = $scope.employee.empName;
                    if (!$scope.isCreate)
                    {
                        // Code for filling the multiselect of work as designation on edit
                        // Call function for filling designation 

                        console.log("inside")

                        $scope.designationList = [];
                        $scope.designationListForWorkAs = [];
                        var parentdeptId;
                        if ($scope.selectedParent.parentId !== null && $scope.selectedParent.parentId !== undefined && $scope.selectedParent.parentId !== 0)
                        {
                            parentdeptId = $scope.selectedParent.parentId;
                        } else
                        {
                            parentdeptId = $scope.selectedParent.id;
                        }

                        Employee.retrieveDesgByDept(parentdeptId, function (res)
                        {

                            angular.forEach(res, function (item) {
                                item.designationName = $rootScope.translateValue("DESIG_NM." + item.label);

                                $scope.designationList.push(item);
                                item.modelName = item.value;
                                $scope.designationListForWorkAs.push(item);
                            });
                            $scope.modelName = [];
                            if ($scope.designationListForWorkAs !== null && $scope.designationListForWorkAs !== undefined) {
                                angular.forEach($scope.designationListForWorkAs, function (editItem) {

                                    var index = $filter('filter')($scope.employee.workdeg, function (result) {

                                        return editItem.value.toString() === result.toString();

                                    })[0];
                                    if (index !== null && index !== undefined) {
                                        editItem.ticked = true;
                                        $scope.modelName.push(editItem);
////                                 editItem.ticked = false;
                                    }

                                });
                            }
                        });

                    }
                    $scope.nativevalid = false;
                    if ($scope.nativeValid = false && angular.isUndefined($scope.employee.nativeaddress) && $scope.employee.nativeaddress == null && angular.isUnDefined($scope.employee.nativefulladdress) && $scope.employee.nativefulladdress == null && angular.isUnDefined($scope.employee.nativepincode) && $scope.employee.nativepincode == null) {
                        $scope.nativevalid = true;
                    }
                    $rootScope.unMaskLoading();
//                    $timeout(function() {
                    $scope.makeAScroll();
//                        // console.log('in time out');
//                    }, 100);
                }, function () {
                    $rootScope.unMaskLoading();
                    $scope.makeAScroll();
                });
            };
            function checkNativeValid() {
                $scope.nativevalid = false;
                if (angular.isDefined($scope.employee.nativeaddress) && $scope.employee.nativeaddress != null && $scope.employee.nativeaddress.toString().length > 0 && angular.isDefined($scope.employee.nativefulladdress) && $scope.employee.nativefulladdress != null && $scope.employee.nativefulladdress.toString().length > 0 && angular.isDefined($scope.employee.nativepincode) && $scope.employee.nativepincode != null && $scope.employee.nativepincode.toString().length > 0) {
                    $scope.nativevalid = true;
                }
            }
            ;
            $scope.updateEmployee = function (form) {
                if ($scope.isResigned)
                {
                    $scope.terminateEmpModal = $modal.open({
                        templateUrl: 'terminateEmpTmpl.html',
                        scope: $scope,
                        size: 'lg'
                    });
//                    $('#terminateEmployeeModal').modal('show');
                }
                else
                {
                    $scope.addEmployee(form);
                }
            }
            $scope.addEmployee = function (form) {
                console.log("form :"+JSON.stringify(form));
                $scope.reload = false;
                // console.log("in add employee method");
//                if ($scope.isResigned)
//                {
//                    $('#terminateEmployeeModal').modal('show');
//                } 
//                else {
                $scope.submitted = true;
                $scope.eduAddAnother(true, false);
                $scope.expAddAnother(true);
                $scope.familyAddAnother(true);
                $scope.policyAddAnother(true);
                // console.log("native address" + !$scope.employee.isNativeAddressSame);

                if (!$scope.employee.isNativeAddressSame) {
                    checkNativeValid();
                } else {
                    $scope.nativevalid = true;
                }
                var valid = true;
                for (var key in form) {
                    if (key !== 'passwordPopUpForm' && key !== 'editMasterForm') {
                        if (form[key].$invalid) {
//                        // console.log("foem"+form[key]+"and key is"+key)
                            valid = false;
                            break;
                        }
                    }
                }
                var allconditionStatisfied = false;
//                // console.log("!$scope.isDepInvalid" + !$scope.isDepInvalid);
//                // console.log("!$scope.notUniqueEmpname" + !$scope.notUniqueEmpname);
//                // console.log("$scope.isPhoneValidate " + $scope.isPhoneValidate);
//                // console.log("!$scope.isAltPhoneInvalid" + !$scope.isAltPhoneInvalid)
//                // console.log("!$scope.isEmailInvalid" + !$scope.isEmailInvalid);
//                // console.log("$scope.isWorkEmailValidate" + $scope.isWorkEmailValidate);
//                // console.log("$scope.isfamilyNameDuplicate" + $scope.isfamilyNameDuplicate);
//                // console.log("!$scope.degreeReq" + !$scope.degreeReq);
//                // console.log("!$scope.prevCmpnyReq" + !$scope.prevCmpnyReq);
//                // console.log("!$scope.familynameReq" + !$scope.familynameReq);
//                // console.log("!$scope.policynameReq" + !$scope.policynameReq);
//                // console.log("$scope.nativevalid " + $scope.nativevalid);
//                // console.log("$scope.ipValidate" + $scope.ipValidate);
                if (!$scope.isDepInvalid && !$scope.invalidname && !$scope.notUniqueEmpname && $scope.isPhoneValidate && !$scope.isAltPhoneInvalid && !$scope.isEmailInvalid && $scope.isWorkEmailValidate && $scope.isfamilyNameDuplicate && !$scope.degreeReq && !$scope.prevCmpnyReq && !$scope.familynameReq && !$scope.policynameReq && $scope.ipValidate) {
                    allconditionStatisfied = true;
                }
console.log("valid :::"+valid)
console.log("allconditionStatisfied :::"+allconditionStatisfied)
                if (valid && allconditionStatisfied) {

                    var success = function () {
//                        retrieveCustomField();
                        $scope.familyreload = false;
                        $scope.generalreload = false;
                        $scope.personalreload = false;
                        $scope.contactreload = false;
                        $scope.identificationreload = false;
                        $scope.tempeducationflag = false;
                        $scope.tempexperienceflag = false;
                        $scope.temppolicyflag = false;
                        $scope.otherDataflag = false;
                        $scope.hkgworkDataflag = false;
                        $scope.resetCustomFields(null);
                        $scope.reload = true;
                        if (form !== undefined && form !== null) {
                            form.$setPristine();
                        }
                        resetVariable();

                        $scope.searchtext = undefined;
                        $scope.scrollTo("mainPanel");
                        $scope.otherDocIndex = 0;

                        if (angular.isDefined($scope.otherDetailsEmpMap) && $scope.otherDetailsEmpMap !== null) {
                            for (var i = 0; i < $scope.otherDetailsEmpMap.length; i++) {
                                $scope.otherDetailsEmpMap[i].isActive = false;
                            }
                        }
                        if (!$scope.isCreate)
                        {
                            $rootScope.randomCount = Math.random();
                        }
                        // console.log("designationlist.." + JSON.stringify($scope.designationList));
                        angular.forEach($scope.designationList, function (ds)
                        {
                            ds.ticked = false;
                        })
                        $scope.designationListForWorkAs = angular.copy($scope.designationList);
                        $rootScope.unMaskLoading();
                    };
                    var failure = function () {
                        retrieveCustomField();
                        $scope.reload = true;
                        $rootScope.unMaskLoading();
                    };
                    var temp = '';
                    angular.forEach($scope.otherDetailsEmpMap, function (i) {
                        if (i.isActive) {
                            if (temp.length > 0) {
                                temp = temp + ",";
                            }
                            temp = temp + "#" + i.value + "#";
                        }
                    });
                    var finalEmployee = angular.copy($scope.employee);

                    finalEmployee.otherDetailsOfEmployeeKeysString = temp;
                    finalEmployee.selecteddep = $scope.parentId;
                    finalEmployee.personalCustom = $scope.personalData;
                    finalEmployee.generalCustom = $scope.generalData;
                    finalEmployee.personalDbType = $scope.personaldbType;
                    finalEmployee.generalDbType = $scope.generaldbType;
                    finalEmployee.contactCustom = $scope.contactData;
                    finalEmployee.contactDbType = $scope.contactdbType;
                    finalEmployee.identificationCustom = $scope.identificationData;
                    finalEmployee.identificationDbType = $scope.identificationdbType;
                    finalEmployee.otherCustom = $scope.otherData;
                    finalEmployee.otherDbType = $scope.otherdbType;
                    finalEmployee.hkgworkCustom = $scope.hkgworkData;
                    finalEmployee.hkgworkDbType = $scope.hkgworkdbType;
                    for (var i = 0; i < finalEmployee.edu.length; i++) {
                        if (finalEmployee.edu[i] != null && angular.isDefined($scope.educationData[i]) && $scope.educationData[i] !== 'undefined') {
                            $scope.educationData[i].index = undefined;
                            finalEmployee.edu[i].educationCustom = angular.copy($scope.educationData[i]);
                            finalEmployee.edu[i].educationDbType = angular.copy($scope.educationdbType);
                        }
                        if (finalEmployee.edu[i] != null && angular.isDefined($scope.editeducationData) && $scope.editeducationData !== 'undefined') {
                            finalEmployee.edu[i].educationCustom = angular.copy($scope.editeducationData);
                            finalEmployee.edu[i].educationDbType = angular.copy($scope.editeducationdbType);
                        }
                    }
                    for (var i = 0; i < finalEmployee.exp.length; i++) {
                        if (finalEmployee.exp[i] != null && angular.isDefined($scope.experienceData[i]) && $scope.experienceData[i] !== 'undefined') {
                            $scope.experienceData[i].index = undefined;
                            finalEmployee.exp[i].experienceCustom = angular.copy($scope.experienceData[i]);
                            finalEmployee.exp[i].experienceDbType = angular.copy($scope.experiencedbType);
                        }
                        if (finalEmployee.exp[i] != null && angular.isDefined($scope.editexperienceData) && $scope.editexperienceData !== 'undefined') {
                            finalEmployee.exp[i].experienceCustom = angular.copy($scope.editexperienceData);
                            finalEmployee.exp[i].experienceDbType = angular.copy($scope.editexperiencedbType);
                        }
                    }
                    for (var i = 0; i < finalEmployee.family.length; i++) {
                        if (finalEmployee.family[i] != null && angular.isDefined($scope.familyData[i]) && $scope.familyData[i] !== 'undefined') {
                            $scope.familyData[i].index = undefined;
                            finalEmployee.family[i].familyCustom = angular.copy($scope.familyData[i]);
                            finalEmployee.family[i].familyDbType = angular.copy($scope.familydbType);
                        }
                        if (finalEmployee.family[i] != null && angular.isDefined($scope.editfamilyData) && $scope.editfamilyData !== 'undefined') {
                            finalEmployee.family[i].familyCustom = angular.copy($scope.editfamilyData);
                            finalEmployee.family[i].familyDbType = angular.copy($scope.editfamilydbType);
                        }
                    }

                    for (var i = 0; i < finalEmployee.policy.length; i++) {
                        if (finalEmployee.policy[i] != null && angular.isDefined($scope.policyData[i]) && $scope.policyData[i] !== 'undefined') {
                            $scope.policyData[i].index = undefined;
                            finalEmployee.policy[i].policyCustom = angular.copy($scope.policyData[i]);
                            finalEmployee.policy[i].policyDbType = angular.copy($scope.policydbType);
                            finalEmployee.policy[i].holdername = undefined;
                        }
                        if (finalEmployee.policy[i] != null && angular.isDefined($scope.editpolicyData) && $scope.editpolicyData !== 'undefined') {
                            finalEmployee.policy[i].policyCustom = angular.copy($scope.editpolicyData);
                            finalEmployee.policy[i].policyDbType = angular.copy($scope.editpolicydbType);
                            finalEmployee.policy[i].holdername = undefined;
                        }

                    }
                    if (finalEmployee.currentaddress instanceof Object) {
                            finalEmployee.currentaddress = finalEmployee.currentaddress.id;
                        }
                        if (finalEmployee.nativeaddress instanceof Object) {
                            finalEmployee.nativeaddress = finalEmployee.nativeaddress.id;
                        }
                     console.log("Add employeeee" + JSON.stringify(finalEmployee));
                    if ($scope.isCreate) {

                        $rootScope.maskLoading();
                        Employee.addEmployee(finalEmployee, success, failure);
                        // console.log('termnate flaf' + !$scope.isEmployeeTerminationConfirmed);
                    } else if (!$scope.isCreate && !$scope.isEmployeeTerminationConfirmed) {
                        // console.log("in update");
                        $rootScope.maskLoading();
                        if (finalEmployee.currentaddress instanceof Object) {
                            finalEmployee.currentaddress = finalEmployee.currentaddress.id;
                        }
                        if (finalEmployee.nativeaddress instanceof Object) {
                            finalEmployee.nativeaddress = finalEmployee.nativeaddress.id;
                        }
                        Employee.updateEmployee(finalEmployee, success, failure);
                    }
                    else if ($scope.isEmployeeTerminationConfirmed)
                    {
                        if (finalEmployee.currentaddress instanceof Object) {
                            finalEmployee.currentaddress = finalEmployee.currentaddress.id;
                        }
                        if (finalEmployee.nativeaddress instanceof Object) {
                            finalEmployee.nativeaddress = finalEmployee.nativeaddress.id;
                        }
                        Employee.terminateEmployee(finalEmployee, function ()
                        {
                            $scope.terminateEmpModal.dismiss();
//                            $('#terminateEmployeeModal').modal('hide');
//                            $rootScope.removeModalOpenCssAfterModalHide();
                            $('.modal-backdrop').remove();
                            success();
                        }, failure
                                );
//                        resetVariable();

                    }
                } else if (!valid) {
                    for (var key in form) {
                        if (key.indexOf("$") !== 0) {
                            if (form[key].$invalid) {
                                $scope.scrollTo(key.toString());
                                break;
                            }
                        }
                    }
                }
//                }
            };
            $scope.openNativeAddressModal = function (event) {
                if ($scope.employee.isNativeAddressSame === true) {
                    $scope.nativesubmitted = false;
                    $scope.nativeAddModal = $modal.open({
                        templateUrl: 'nativeAddTmpl.html',
                        scope: $scope,
                        size: 'lg'
                    });
//                    $('#nativeAddressModal').modal('show');
                    event.preventDefault();
                } else {
                    $scope.employee.isNativeAddressSame = true;
                    $scope.resetNativeAddress();
                }

            };
            //Just open the modal for view.
            $scope.showNativeAddressModal = function (event) {
                $scope.nativesubmitted = false;
                $scope.nativeAddModal = $modal.open({
                    templateUrl: 'nativeAddTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
            };
            $scope.resetNativeAddress = function () {
                $scope.nativesubmitted = false;
                if ($scope.employee.isNativeAddressSame === true) {
                    $scope.employee.nativeaddress = "";
                    $scope.employee.nativefulladdress = undefined;
                    $scope.employee.nativepincode = undefined;
                } else {
                    if ($scope.employee.nativeaddress !== undefined && $scope.employee.nativeaddress !== null && $scope.employee.nativeaddress.length > 0)
                        $scope.tempAddress.nativeaddress = $scope.employee.nativeaddress;
                    if ($scope.employee.nativefulladdress !== undefined && $scope.employee.nativefulladdress !== null && $scope.employee.nativefulladdress.length > 0)
                        $scope.tempAddress.nativefulladdress = $scope.employee.nativefulladdress;
                    if ($scope.employee.nativepincode !== undefined && $scope.employee.nativepincode !== null && $scope.employee.nativepincode.length > 0)
                        $scope.tempAddress.nativepincode = $scope.employee.nativepincode;
                    $scope.employee.nativeaddress = "";
                    $scope.employee.nativefulladdress = undefined;
                    $scope.employee.nativepincode = undefined;
                }
            };
            $scope.cancelNativeAddress = function () {
//                if ($scope.oldNativeId == null)
//                {
//                    $scope.resetNativeAddress();
//                    $scope.employee.isNativeAddressSame = true;
//                }
//             
                $scope.nativeAddModal.dismiss();
//                $('#nativeAddressModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                if ($scope.employee.isNativeAddressSame === true) {
                    $scope.nativesubmitted = false;
                    $scope.employee.nativeaddress = "";
                    $scope.employee.nativefulladdress = undefined;
                    $scope.employee.nativepincode = undefined;
                } else {
                    if ($scope.tempAddress.nativeaddress !== undefined && $scope.tempAddress.nativeaddress !== null && $scope.tempAddress.nativeaddress.length > 0)
                        $scope.employee.nativeaddress = $scope.tempAddress.nativeaddress;
                    if ($scope.tempAddress.nativefulladdress !== undefined && $scope.tempAddress.nativefulladdress !== null && $scope.tempAddress.nativefulladdress.length > 0)
                        $scope.employee.nativefulladdress = $scope.tempAddress.nativefulladdress;
                    if ($scope.tempAddress.nativepincode !== undefined && $scope.tempAddress.nativepincode !== null && $scope.tempAddress.nativepincode.length > 0)
                        $scope.employee.nativepincode = $scope.tempAddress.nativepincode;
                }

            };
            $scope.okNativeAddress = function (form) {
                $scope.nativesubmitted = true;
                if (form.$valid) {
                    $scope.nativesubmitted = false;
                    $scope.employee.isNativeAddressSame = false;
                    $scope.nativeAddModal.dismiss();
//                    $('#nativeAddressModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();

                }
            };
            // Education
            $scope.eduAddAnother = function (isFromAnother, invalidFlag) {
                $scope.edusubmitted = true;
//                if ($scope.employee.edu[$scope.educationIndex].degree == undefined)
//                {
//                    $scope.edusubmitted = false;
//                }
                $scope.degreeReq = false;
                if ((!$scope.employee.edu[$scope.educationIndex].degree) &&
                        (!$scope.employee.edu[$scope.educationIndex].passingYear) &&
                        (!$scope.employee.edu[$scope.educationIndex].empPercentage) &&
                        (!$scope.employee.edu[$scope.educationIndex].university) &&
                        (!$scope.employee.exp[$scope.expIndex].salary) &&
                        (!$scope.employee.edu[$scope.educationIndex].medium))

                {
                    $scope.edusubmitted = false;
                }
                if (!invalidFlag) {
                    if (($scope.employee.edu[$scope.educationIndex].degree !== undefined && $scope.employee.edu[$scope.educationIndex].degree !== null) &&
                            ($scope.employee.edu[$scope.educationIndex].passingYear !== undefined && $scope.employee.edu[$scope.educationIndex].passingYear !== null) &&
                            ($scope.employee.edu[$scope.educationIndex].empPercentage !== undefined && $scope.employee.edu[$scope.educationIndex].empPercentage !== null) &&
                            ($scope.employee.edu[$scope.educationIndex].university !== undefined && $scope.employee.edu[$scope.educationIndex].university !== null) &&
                            ($scope.employee.edu[$scope.educationIndex].medium !== undefined && $scope.employee.edu[$scope.educationIndex].medium !== null)) {
                        if (isFromAnother) {
                            $scope.edusubmitted = false;

                            $scope.educationData[$scope.educationIndex] = angular.copy($scope.tempeducationData);
                            $scope.educationIndex = $scope.educationIndex + 1;
                            $scope.hasAnyEdu = true;
                            $scope.employee.edu[$scope.educationIndex] = {};
//                            $scope.tempeducationData = DynamicFormService.resetSection($scope.educationTemplate);
                            $scope.tempeducationflag = false;
                            $scope.resetCustomFields("EDUCATION");
//                            $scope.tempeducationData = {};
                        }
                    } else if (($scope.employee.edu[$scope.educationIndex].degree === undefined || $scope.employee.edu[$scope.educationIndex].degree === null && $scope.employee.edu[$scope.educationIndex].degree === null) &&
                            (($scope.employee.edu[$scope.educationIndex].passingYear !== undefined && $scope.employee.edu[$scope.educationIndex].passingYear !== null) ||
                                    ($scope.employee.edu[$scope.educationIndex].empPercentage !== undefined && $scope.employee.edu[$scope.educationIndex].empPercentage !== null && $scope.employee.edu[$scope.educationIndex].empPercentage) ||
                                    ($scope.employee.edu[$scope.educationIndex].university !== undefined && $scope.employee.edu[$scope.educationIndex].university !== null && $scope.employee.edu[$scope.educationIndex].university) ||
                                    ($scope.employee.edu[$scope.educationIndex].medium !== undefined && $scope.employee.edu[$scope.educationIndex].medium !== null && $scope.employee.edu[$scope.educationIndex].medium !== null))) {
                        $scope.degreeReq = true;
                    }
                }
            };
            $scope.eduDegreeOnChange = function () {
                if ($scope.employee.edu[$scope.educationIndex].degree === null) {
                    $scope.resetEduDetails();
                }
            };
            $scope.resetEduDetails = function () {
                $scope.employee.edu[$scope.educationIndex].passingYear = undefined;
                $scope.employee.edu[$scope.educationIndex].university = undefined;
                $scope.employee.edu[$scope.educationIndex].medium = undefined;
                $scope.employee.edu[$scope.educationIndex].empPercentage = undefined;
            };
            $scope.viewAllEduDetails = function () {
                var current = $scope.educationIndex;
                $scope.viewAllEdu = [];
                $.each($scope.employee.edu, function (index, item) {
                    var item1 = angular.copy(item);
                    if (index !== current && item1 !== null) {
                        item1.index = index;
                        for (var i = 0; i < $scope.educationDegreeMap.length; i++) {
                            if ($scope.educationDegreeMap[i].value === item1.degree) {
                                item1.degreename = $scope.educationDegreeMap[i].label;
                                break;
                            }
                        }
                        for (var i = 0; i < $scope.universityList.length; i++) {
                            if ($scope.universityList[i].value === item1.university) {
                                item1.universityname = $scope.universityList[i].label;
                                break;
                            }
                        }
                        for (var i = 0; i < $scope.mediumMap.length; i++) {
                            if ($scope.mediumMap[i].value === item1.medium) {
                                item1.mediumname = $scope.mediumMap[i].label;
                                break;
                            }
                        }
                        for (var i = 0; i < $scope.yearOfPassing.length; i++) {
                            if ($scope.yearOfPassing === item1.passingYear) {
                                item1.yearname = $scope.yearOfPassing[i];
                                break;
                            }
                        }
                        $scope.viewAllEdu.push(item1);
                    }
                });
                $scope.eduViewAllModal = $modal.open({
                    templateUrl: 'eduViewAllTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#viewAllEducationModal').modal('show');
            };
            $scope.openEditEduModal = function (edu) {
                $scope.editedu = angular.copy(edu);
                $scope.editeducationData = {};
                $scope.editEducationTemplate = $scope.educationTemplate;
                $scope.editeducationData = angular.copy($scope.educationData[edu.index]);
                if (!!$scope.editeducationData && !!$scope.listOfModelsOfDateType["EDUCATION"]) {
                    angular.forEach($scope.listOfModelsOfDateType["EDUCATION"], function (listOfModel)
                    {
                        if ($scope.editeducationData.hasOwnProperty(listOfModel))
                        {
                            if ($scope.editeducationData[listOfModel] !== null && $scope.editeducationData[listOfModel] !== undefined)
                            {
                                $scope.editeducationData[listOfModel] = new Date($scope.editeducationData[listOfModel]);
                            } else
                            {
                                $scope.editeducationData[listOfModel] = '';
                            }
                        }
                    });
                }
//                $scope.educationdbType = {};
                $scope.eduViewAllModal.dismiss();
//                $('#viewAllEducationModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.editEduModal = $modal.open({
                    templateUrl: 'editEduTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#editEducationModal').modal('show');
            };
            $scope.removeEdu = function (edu) {
                $scope.employee.edu[edu.index] = null;
                $scope.educationData[edu.index] = null;
                $scope.viewAllEdu.splice(edu.index, 1);
                var j = 0;
                var current = $scope.educationIndex;
                for (var i = 0; i < $scope.employee.edu.length; i++) {
                    var item = $scope.employee.edu[i];
                    if (i !== current && item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.hasAnyEdu = false;
                    $scope.eduViewAllModal.dismiss();
//                    $('#viewAllEducationModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.hideEduViewAll = function () {
                $scope.editEduModal.dismiss();
//                $('#editEducationModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.saveEditEduChanges = function (form) {
                $scope.editedusubmitted = true;
                if (form.$valid) {
                    var i = $scope.editedu.index;
                    $scope.editedu.index = undefined;
                    $scope.editedu.degreename = undefined;
                    $scope.editedu.mediumname = undefined;
                    $scope.editedu.universityname = undefined;
                    $scope.editedu.yearname = undefined;
                    $scope.employee.edu[i] = angular.copy($scope.editedu);
//                    $scope.educationData[i] = angular.copy($scope.editeducationData);
                    $scope.employee.edu[i].educationDbType = angular.copy($scope.editeducationdbType);
                    $scope.employee.edu[i].educationCustom = angular.copy($scope.editeducationData);
                    $scope.educationData[i] = angular.copy($scope.editeducationData);
                    $scope.editEduModal.dismiss();
//                    $('#editEducationModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            // Education ends

            // Experience
            $scope.expAddAnother = function (isFromAnother) {
                $scope.expsubmitted = true;
                $scope.prevCmpnyReq = false;
//                if ($scope.employee.exp[$scope.expIndex].company == undefined && form.$dirty)
//                {
//                    // console.log('dont go...');
//                    $scope.expsubmitted = false;
//                }
                if ((!$scope.employee.exp[$scope.expIndex].company) &&
                        (!$scope.employee.exp[$scope.expIndex].designation) &&
                        (!$scope.employee.exp[$scope.expIndex].startedFrom) &&
                        (!$scope.employee.exp[$scope.expIndex].workedTill) &&
                        (!$scope.employee.exp[$scope.expIndex].salary) &&
                        (!$scope.employee.exp[$scope.expIndex].reasonOfLeaving))

                {
                    $scope.expsubmitted = false;
                }
                if (($scope.employee.exp[$scope.expIndex].company !== undefined && $scope.employee.exp[$scope.expIndex].company !== null) &&
                        ($scope.employee.exp[$scope.expIndex].designation !== undefined && $scope.employee.exp[$scope.expIndex].designation !== null) &&
                        ($scope.employee.exp[$scope.expIndex].startedFrom !== undefined && $scope.employee.exp[$scope.expIndex].startedFrom !== null) &&
                        ($scope.employee.exp[$scope.expIndex].workedTill !== undefined && $scope.employee.exp[$scope.expIndex].workedTill !== null) &&
                        ($scope.employee.exp[$scope.expIndex].salary !== undefined && $scope.employee.exp[$scope.expIndex].salary !== null) &&
                        ($scope.employee.exp[$scope.expIndex].reasonOfLeaving !== undefined && $scope.employee.exp[$scope.expIndex].reasonOfLeaving !== null)) {
                    if (isFromAnother) {
                        $scope.expsubmitted = false;
                        $scope.experienceData[$scope.expIndex] = angular.copy($scope.tempexperienceData);
                        $scope.expIndex = $scope.expIndex + 1;
                        $scope.hasAnyExp = true;
                        $scope.employee.exp[$scope.expIndex] = {};
//                        $scope.tempexperienceData = {};
//                        $scope.tempexperienceData = DynamicFormService.resetSection($scope.experienceTemplate);
                        $scope.tempexperienceflag = false;
                        $scope.resetCustomFields("EXPERIENCE");
                    }
                } else if (($scope.employee.exp[$scope.expIndex].company === undefined || $scope.employee.exp[$scope.expIndex].company === null) && (($scope.employee.exp[$scope.expIndex].designation !== undefined && $scope.employee.exp[$scope.expIndex].designation !== null) ||
                        ($scope.employee.exp[$scope.expIndex].startedFrom !== undefined && $scope.employee.exp[$scope.expIndex].startedFrom !== null && $scope.employee.exp[$scope.expIndex].startedFrom) ||
                        ($scope.employee.exp[$scope.expIndex].workedTill !== undefined && $scope.employee.exp[$scope.expIndex].workedTill !== null && $scope.employee.exp[$scope.expIndex].workedTill) ||
                        ($scope.employee.exp[$scope.expIndex].salary !== undefined && $scope.employee.exp[$scope.expIndex].salary !== null && $scope.employee.exp[$scope.expIndex].salary) ||
                        ($scope.employee.exp[$scope.expIndex].reasonOfLeaving !== undefined && $scope.employee.exp[$scope.expIndex].reasonOfLeaving !== null && $scope.employee.exp[$scope.expIndex].reasonOfLeaving))) {
                    $scope.prevCmpnyReq = true;
                }

            };
            $scope.expComapnyOnBlur = function () {
                if ($scope.employee.exp[$scope.expIndex].company === null) {
                    $scope.resetExpDetails();
                }
            };
            $scope.resetExpDetails = function () {
                $scope.employee.exp[$scope.expIndex].designation = undefined;
                $scope.employee.exp[$scope.expIndex].startedFrom = undefined;
                $scope.employee.exp[$scope.expIndex].workedTill = undefined;
                $scope.employee.exp[$scope.expIndex].salary = undefined;
                $scope.employee.exp[$scope.expIndex].reasonOfLeaving = undefined;
            };
            $scope.viewAllExpDetails = function () {
                var current = $scope.expIndex;
                $scope.viewAllExp = [];
                $.each($scope.employee.exp, function (index, item) {
                    var item1 = angular.copy(item);
                    if (index !== current && item1 !== null) {
                        item1.index = index;
                        for (var i = 0; i < $scope.expDepList.length; i++) {
                            if ($scope.expDepList[i].value === item1.designation) {
                                item1.designationname = $scope.expDepList[i].label;
                                break;
                            }
                        }
                        $scope.viewAllExp.push(item1);
                    }
                });
                $scope.expViewAllModal = $modal.open({
                    templateUrl: 'expViewAllTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#viewAllExperienceModal').modal('show');
            };
            $scope.openEditExpModal = function (exp) {
                $scope.editexp = angular.copy(exp);
                $scope.editexperienceData = {};
                $scope.editexperienceTemplate = $scope.experienceTemplate;
                $scope.editexperienceData = angular.copy($scope.experienceData[exp.index]);

                if (!!$scope.editexperienceData && !!$scope.listOfModelsOfDateType["EXPERIENCE"]) {
                    angular.forEach($scope.listOfModelsOfDateType["EXPERIENCE"], function (listOfModel)
                    {
                        if ($scope.editexperienceData.hasOwnProperty(listOfModel))
                        {
                            if ($scope.editexperienceData[listOfModel] !== null && $scope.editexperienceData[listOfModel] !== undefined)
                            {
                                $scope.editexperienceData[listOfModel] = new Date($scope.editexperienceData[listOfModel]);
                            } else
                            {
                                $scope.editexperienceData[listOfModel] = '';
                            }
                        }
                    });
                }
//                $scope.experiencedbType = {};
//                $('#viewAllExperienceModal').modal('hide');
                $scope.expViewAllModal.dismiss();
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.editExpModal = $modal.open({
                    templateUrl: 'editExpTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#editExperienceModal').modal('show');
            };
            $scope.removeExp = function (exp) {
                $scope.employee.exp[exp.index] = null;
                $scope.experienceData[exp.index] = null;
                $scope.viewAllExp.splice(exp.index, 1);
                var j = 0;
                var current = $scope.expIndex;
                for (var i = 0; i < $scope.employee.exp.length; i++) {
                    var item = $scope.employee.exp[i];
                    if (i !== current && item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.hasAnyExp = false;
//                    $('#viewAllExperienceModal').modal('hide');
                    $scope.expViewAllModal.dismiss();
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.hideExpViewAll = function () {
//                $('#editExperienceModal').modal('hide');
                $scope.editExpModal.dismiss();
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.saveEditExpChanges = function (form) {
                $scope.editexpsubmitted = true;
                if (form.$valid) {
                    var i = $scope.editexp.index;
                    $scope.editexp.index = undefined;
                    $scope.editexp.designationname = undefined;
                    $scope.employee.exp[i] = angular.copy($scope.editexp);
//                    $scope.experienceData[i] = angular.copy($scope.editexperienceData);
                    $scope.employee.exp[i].experienceDbType = angular.copy($scope.editexperiencedbType);
                    $scope.employee.exp[i].experienceCustom = angular.copy($scope.editexperienceData);
                    $scope.experienceData[i] = angular.copy($scope.editexperienceData);

//                    $('#editExperienceModal').modal('hide');

                    $scope.editExpModal.dismiss();
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            // Experience ends

            // Family
            $scope.checkMemberName = function (name)
            {
                console.log('viewAllFamily----' + JSON.stringify($scope.viewAllFamily));
                if ($scope.viewAllFamily != null && $scope.viewAllFamily.length > 0)
                {
                    for (var i = 0; i < $scope.viewAllFamily.length; i++)
                    {
                        if ($scope.viewAllFamily[i].firstName.toLowerCase() === name.toLowerCase())
                        {
                            $scope.isfamilyNameDuplicate = false;
                            break;
                        }
                        else
                        {
                            $scope.isfamilyNameDuplicate = true;
                        }
                    }

                }
            };
            $scope.familyAddAnother = function (isFromAnother) {
                $scope.familysubmitted = true;
//                if ($scope.employee.family[$scope.familyIndex].firstName == undefined)
//                {
//                    $scope.familysubmitted = false;
//                }
                $scope.familynameReq = false;
                if ((!$scope.employee.family[$scope.familyIndex].firstName) &&
                        (!$scope.employee.family[$scope.familyIndex].dateOfBirth) &&
                        (!$scope.employee.family[$scope.familyIndex].occupation) &&
                        (!$scope.employee.family[$scope.familyIndex].relation) &&
                        (!$scope.employee.family[$scope.familyIndex].bloodGroup))

                {
                    $scope.familysubmitted = false;
                }
//                var i, j, n;
//                if ($scope.viewAllFamily != null) {
//
//                    n = $scope.employee.family.length;
//                    // console.log('got n value' + n);
//                }
//                // to ensure the fewest possible comparisons
//                if (n > 0)
//                {
//                    mainloop:
//                            for (i = 0; i < n; i++) {                        // outer loop uses each item i at 0 through n
//                        for (j = i + 1; j < n; j++) {
//
//                            // inner loop only compares items j at i+1 to n
//                            if ($scope.employee.family[i].firstName != null && $scope.employee.family[j].firstName != null) {
//                                if ($scope.employee.family[i].firstName.toLowerCase().replace(/ /g, '') === $scope.employee.family[j].firstName.toLowerCase().replace(/ /g, ''))
//                                {
//                                    $scope.isfamilyNameDuplicate = false;
//                                    break mainloop;
//
//                                }
//                                else
//                                {
//                                    $scope.isfamilyNameDuplicate = true;
//                                }
//                            }
//                        }
//                    }
//                }
                if (($scope.employee.family[$scope.familyIndex].firstName !== undefined && $scope.employee.family[$scope.familyIndex].firstName !== null) &&
                        ($scope.employee.family[$scope.familyIndex].dateOfBirth !== undefined && $scope.employee.family[$scope.familyIndex].dateOfBirth !== null) &&
                        ($scope.employee.family[$scope.familyIndex].occupation !== undefined && $scope.employee.family[$scope.familyIndex].occupation !== null) &&
                        ($scope.employee.family[$scope.familyIndex].relation !== undefined && $scope.employee.family[$scope.familyIndex].relation !== null) &&
                        ($scope.employee.family[$scope.familyIndex].bloodGroup !== undefined && $scope.employee.family[$scope.familyIndex].bloodGroup !== null)
                        && $scope.isfamilyContact
//                        &&
//                        ($scope.employee.family[$scope.familyIndex].mobileNumber !== undefined && $scope.employee.family[$scope.familyIndex].mobileNumber !== null)
                        ) {

                    if (isFromAnother && ($scope.isfamilyNameDuplicate === true)) {
                        $scope.familysubmitted = false;
                        // add to policyHoldersMap
                        $scope.policyHoldersMap['F' + $scope.familyIndex] = $scope.employee.family[$scope.familyIndex].firstName;
                        $scope.familyData[$scope.familyIndex] = angular.copy($scope.tempfamilyData);
                        $scope.familyIndex = $scope.familyIndex + 1;
                        $scope.hasAnyFamily = true;
                        $scope.employee.family[$scope.familyIndex] = {'index': $scope.familyIndex};
//                        $scope.tempfamilyData = {};
//                        $scope.tempfamilyData = DynamicFormService.resetSection($scope.familyTemplate);
                        $scope.familyreload = false;
                        $scope.resetCustomFields("FAMILY");
                    }

                } else if (($scope.employee.family[$scope.familyIndex].firstName === undefined || $scope.employee.family[$scope.familyIndex].firstName === null) &&
                        (($scope.employee.family[$scope.familyIndex].dateOfBirth !== undefined && $scope.employee.family[$scope.familyIndex].dateOfBirth !== null && $scope.employee.family[$scope.familyIndex].dateOfBirth) ||
                                ($scope.employee.family[$scope.familyIndex].occupation !== undefined && $scope.employee.family[$scope.familyIndex].occupation !== null && $scope.employee.family[$scope.familyIndex].occupation) ||
                                ($scope.employee.family[$scope.familyIndex].relation !== undefined && $scope.employee.family[$scope.familyIndex].relation !== null && $scope.employee.family[$scope.familyIndex].relation) ||
                                ($scope.employee.family[$scope.familyIndex].bloodGroup !== undefined && $scope.employee.family[$scope.familyIndex].bloodGroup !== null && $scope.employee.family[$scope.familyIndex].bloodGroup) ||
                                ($scope.employee.family[$scope.familyIndex].mobileNumber !== undefined && $scope.employee.family[$scope.familyIndex].mobileNumber !== null))) {
                    $scope.familynameReq = true;
                }
                $scope.viewAllFamilyDetails();
            };
            $scope.familynameOnBlur = function () {
                if ($scope.employee.family[$scope.familyIndex].company === null) {
                    $scope.resetFamilyDetails();
                }
            };
            $scope.resetFamilyDetails = function () {
                $scope.employee.family[$scope.familyIndex].dateOfBirth = undefined;
                $scope.employee.family[$scope.familyIndex].occupation = undefined;
                $scope.employee.family[$scope.familyIndex].relation = undefined;
                $scope.employee.family[$scope.familyIndex].bloodGroup = undefined;
                $scope.employee.family[$scope.familyIndex].mobileNumber = undefined;
            };
            $scope.viewAllFamilyDetails = function () {
                var current = $scope.familyIndex;
                $scope.viewAllFamily = [];
                $.each($scope.employee.family, function (index, item) {
                    var item1 = angular.copy(item);

                    if (index !== current && item1 !== null) {
                        if (!angular.isDefined(item1.mobileNumber)) {
                            item1.mobileNumber = "NA";
                        }
                        item1.index = index;
                        for (var i = 0; i < $scope.relationList.length; i++) {
                            if ($scope.relationList[i].value === item1.relation) {
                                item1.relationname = $scope.relationList[i].label;
                                break;
                            }
                        }
                        for (var i = 0; i < $scope.bloodGroups.length; i++) {
                            if ($scope.bloodGroups[i].value === item1.bloodGroup) {
                                item1.bloodGroupname = $scope.bloodGroups[i].label;
                                break;
                            }
                        }
                        for (var i = 0; i < $scope.occupationList.length; i++) {
                            if ($scope.occupationList[i].value === item1.occupation) {
                                item1.occupationname = $scope.occupationList[i].label;
                                break;
                            }
                        }
                        $scope.viewAllFamily.push(item1);
                    }
                });

            };
            $scope.openModal = function ()
            {
                $scope.viewAllFamilyDetails();
                $scope.familyViewAllModal = $modal.open({
                    templateUrl: 'familyViewAllTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#viewAllFamilyModal').modal('show');
            }
            $scope.openEditFamilyModal = function (family) {
                $scope.editfamily = angular.copy(family);
                $scope.editfamilyData = {};
                $scope.editfamilyTemplate = $scope.familyTemplate;
                $scope.editfamilyData = angular.copy($scope.familyData[family.index]);
                if (!!$scope.editfamilyData && !!$scope.listOfModelsOfDateType["FAMILY"]) {
                    angular.forEach($scope.listOfModelsOfDateType["FAMILY"], function (listOfModel)
                    {
                        if ($scope.editfamilyData.hasOwnProperty(listOfModel))
                        {
                            if ($scope.editfamilyData[listOfModel] !== null && $scope.editfamilyData[listOfModel] !== undefined)
                            {
                                $scope.editfamilyData[listOfModel] = new Date($scope.editfamilyData[listOfModel]);
                            } else
                            {
                                $scope.editfamilyData[listOfModel] = '';
                            }
                        }
                    });
                }
                $scope.familydbType = {};
//                $('#viewAllFamilyModal').modal('hide');
                $scope.familyViewAllModal.dismiss();
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.editFamilyModal = $modal.open({
                    templateUrl: 'editFamilyTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#editFamilyModal').modal('show');
            };
            $scope.removeFamily = function (family, index) {
                $scope.employee.family[family.index] = null;
                $scope.familyData[index] = null;
                $scope.viewAllFamily.splice(index, 1);
                // remove from policy also
                var deleteindex = 'F' + family.index;
                delete $scope.policyHoldersMap[deleteindex];
                for (var i = 0; i < $scope.employee.policy.length; i++) {
                    var item = $scope.employee.policy[i];
                    if (item != null && item.contactUser === deleteindex) {
                        $scope.removePolicy(item);
                    }
                }
                //
                var j = 0;
                var current = $scope.familyIndex;
                for (var i = 0; i < $scope.employee.family.length; i++) {
                    var item = $scope.employee.family[i];
                    if (i !== current && item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.hasAnyFamily = false;
//                    $('#viewAllFamilyModal').modal('hide');
                    $scope.familyViewAllModal.dismiss();
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.hideFamilyViewAll = function () {
//                $('#editFamilyModal').modal('hide');
                $scope.editFamilyModal.dismiss();
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.saveEditFamilyChanges = function (form) {
                $scope.editfamilysubmitted = true;
                if (form.$valid) {
                    var i, n;
                    n = $scope.employee.family.length;
                    var current = $scope.editfamily.index;
                    // to ensure the fewest possible comparisons
                    for (i = 0; i < n - 1; i++) {                        // outer loop uses each item i at 0 through n
                        if (i != current) {
                            // inner loop only compares items j at i+1 to n
                            if ($scope.employee.family[i].firstName.toLowerCase().replace(/ /g, '') === $scope.editfamily.firstName.toLowerCase().replace(/ /g, ''))
                            {
                                if (n > 2)
                                {
                                    $scope.iseditfamilyNameDuplicate = false;
                                    break;
                                }
                            }
                            else
                            {
                                $scope.iseditfamilyNameDuplicate = true;
                            }
                        }
                    }
                    if ($scope.iseditfamilyNameDuplicate === true)
                    {
                        var i = $scope.editfamily.index;
                        $scope.policyHoldersMap['F' + $scope.editfamily.index] = $scope.editfamily.firstName;
//                        $scope.editfamily.index = undefined;
                        $scope.editfamily.relationname = undefined;
                        $scope.editfamily.bloodGroupname = undefined;
                        $scope.editfamily.occupationname = undefined;
                        $scope.employee.family[i] = angular.copy($scope.editfamily);
                        $scope.employee.family[i].familyDbType = angular.copy($scope.editfamilydbType);
                        $scope.employee.family[i].familyCustom = angular.copy($scope.editfamilyData);
                        $scope.familyData[i] = angular.copy($scope.editfamilyData);
//                        $scope.familyData[i] = angular.copy($scope.editfamilyData);



                        $scope.editFamilyModal.dismiss();
//                        $('#editFamilyModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                    }
                }
            };
            // Family ends

            // Policy
            $scope.policyAddAnother = function (isFromAnother) {
                $scope.policysubmitted = true;
//                if ($scope.employee.policy[$scope.policyIndex].contactUser == undefined)
//                {
//                    $scope.policysubmitted = false;
//                }
                $scope.policynameReq = false;
                if ((!$scope.employee.policy[$scope.policyIndex].contactUser) &&
                        (!$scope.employee.policy[$scope.policyIndex].company) &&
                        (!$scope.employee.policy[$scope.policyIndex].policyNumber) &&
                        (!$scope.employee.family[$scope.familyIndex].relation) &&
                        (!$scope.employee.policy[$scope.policyIndex].policyName))

                {
                    $scope.policysubmitted = false;
                }
                if (($scope.employee.policy[$scope.policyIndex].contactUser !== undefined && $scope.employee.policy[$scope.policyIndex].contactUser !== null) &&
                        ($scope.employee.policy[$scope.policyIndex].company !== undefined && $scope.employee.policy[$scope.policyIndex].company !== null) &&
                        ($scope.employee.policy[$scope.policyIndex].policyNumber !== undefined && $scope.employee.policy[$scope.policyIndex].policyNumber !== null) &&
                        ($scope.employee.policy[$scope.policyIndex].policyName !== undefined && $scope.employee.policy[$scope.policyIndex].policyName !== null)) {
                    if (isFromAnother) {
//                        // console.log('is another');
                        $scope.policysubmitted = false;
                        $scope.policyData[$scope.policyIndex] = angular.copy($scope.temppolicyData);
                        $scope.policyIndex = $scope.policyIndex + 1;
                        $scope.hasAnyPolicy = true;
                        $scope.employee.policy[$scope.policyIndex] = {'status': 'Active', 'index': $scope.policyIndex};
//                        $scope.temppolicyData = {};
//                        $scope.temppolicyData = DynamicFormService.resetSection($scope.policyTemplate);
                        $scope.temppolicyflag = false;
                        $scope.resetCustomFields("POLICY");
                    }
                } else if (($scope.employee.policy[$scope.policyIndex].contactUser === undefined || $scope.employee.policy[$scope.policyIndex].contactUser === null) &&
                        (($scope.employee.policy[$scope.policyIndex].company !== undefined && $scope.employee.policy[$scope.policyIndex].company !== null && $scope.employee.policy[$scope.policyIndex].company) ||
                                ($scope.employee.policy[$scope.policyIndex].policyNumber !== undefined && $scope.employee.policy[$scope.policyIndex].policyNumber !== null && $scope.employee.policy[$scope.policyIndex].policyNumber) ||
                                ($scope.employee.policy[$scope.policyIndex].policyName !== undefined && $scope.employee.policy[$scope.policyIndex].policyName !== null && $scope.employee.policy[$scope.policyIndex].policyName))) {
                    $scope.policynameReq = true;
                }
//                // console.log('Policy Data' + JSON.stringify($scope.policyData));
            };
            $scope.policynameOnBlur = function () {
                if ($scope.employee.policy[$scope.policyIndex].contactUser === null) {
                    $scope.resetPolicyDetails();
                }
            };
            $scope.resetPolicyDetails = function () {
                $scope.employee.policy[$scope.policyIndex].company = undefined;
                $scope.employee.policy[$scope.policyIndex].status = undefined;
                $scope.employee.policy[$scope.policyIndex].policyNumber = undefined;
                $scope.employee.policy[$scope.policyIndex].policyName = undefined;
            };
            $scope.viewAllPolicyDetails = function () {
                var current = $scope.policyIndex;
                $scope.viewAllPolicy = [];
                $.each($scope.employee.policy, function (index, item) {
                    var item1 = angular.copy(item);
                    if (index !== current && item1 !== null) {
                        item1.index = index;
                        for (var i = 0; i < $scope.policyCompanyList.length; i++) {
                            if ($scope.policyCompanyList[i].value === item1.company) {
                                item1.companyname = $scope.policyCompanyList[i].label;
                                break;
                            }
                        }
                        for (var key in $scope.policyHoldersMap) {
                            if (key === item1.contactUser) {
                                item1.holdername = $scope.policyHoldersMap[key];
                                break;
                            }
                        }
                        $scope.viewAllPolicy.push(item1);
                    }
                });
                $scope.policyViewAllModal = $modal.open({
                    templateUrl: 'policyViewAllTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#viewAllPolicyModal').modal('show');
            };
            $scope.openEditPolicyModal = function (policy) {
                $scope.editpolicy = angular.copy(policy);
                $scope.editpolicyData = {};
                $scope.editpolicyTemplate = angular.copy($scope.policyTemplate);
                $scope.editpolicyData = angular.copy($scope.policyData[policy.index]);
                if (!!$scope.editpolicyData && !!$scope.listOfModelsOfDateType["POLICY"]) {
                    angular.forEach($scope.listOfModelsOfDateType["POLICY"], function (listOfModel)
                    {
                        if ($scope.editpolicyData.hasOwnProperty(listOfModel))
                        {
                            if ($scope.editpolicyData[listOfModel] !== null && $scope.editpolicyData[listOfModel] !== undefined)
                            {
                                $scope.editpolicyData[listOfModel] = new Date($scope.editpolicyData[listOfModel]);
                            } else
                            {
                                $scope.editpolicyData[listOfModel] = '';
                            }
                        }
                    });
                }
//                $scope.editpolicydbType = {};
                $scope.policyViewAllModal.dismiss();
//                $('#viewAllPolicyModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $scope.editPolicyModal = $modal.open({
                    templateUrl: 'editPolicyTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#editPolicyModal').modal('show');
            };
            $scope.removePolicy = function (policy) {
//                    $scope.employee.policy.splice(policy.index, 1);
                $scope.employee.policy[policy.index] = null;
                if (policy.index != $scope.policyIndex) {
                    $scope.employee.policy[policy.index] = null;
                } else {
                    $scope.employee.policy[policy.index] = {'status': 'Active', 'index': policy.index};
                }

//                  $scope.employee.policy[policy.index] = {'status': 'Active', 'index': policy.index};
                $scope.policyData[policy.index] = null;
                if ($scope.viewAllPolicy != undefined) {
                    $scope.viewAllPolicy.splice(policy.index, 1);
                }
                var j = 0;
                var current = $scope.policyIndex;
                for (var i = 0; i < $scope.employee.policy.length; i++) {
                    var item = $scope.employee.policy[i];
                    if (i !== current && item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.hasAnyPolicy = false;
                    $scope.policyViewAllModal.dismiss();
//                    $('#viewAllPolicyModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.hidePolicyViewAll = function () {
                $scope.editPolicyModal.dismiss();
//                $('#editPolicyModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.saveEditPolicyChanges = function (form) {
                $scope.editpolicysubmitted = true;
                if (form.$valid) {
                    var i = $scope.editpolicy.index;
//                    $scope.editpolicy.index = undefined;
                    $scope.editpolicy.companyname = undefined;
                    $scope.editpolicy.holdernaame = undefined;
                    $scope.employee.policy[i] = angular.copy($scope.editpolicy);
                    $scope.employee.policy[i].policyDbType = angular.copy($scope.editpolicydbType);
                    $scope.employee.policy[i].policyCustom = angular.copy($scope.editpolicyData);
                    $scope.policyData[i] = angular.copy($scope.editpolicyData);
                    $scope.editPolicyModal.dismiss();
//                    $('#editPolicyModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.doesEmployeeNameExist = function (name) {
                $scope.invalidname = true;
                if (name && name.length > 0) {
                    Employee.doesEmployeeNameExist(name, function (resp) {

                        if (resp.data) {
                            if (!$scope.isCreate && name != $scope.oldname) {
                                $scope.notUniqueEmpname = true;
                            }
                            if ($scope.isCreate) {
                                $scope.notUniqueEmpname = true;
                            }
                        } else {
                            $scope.notUniqueEmpname = false;
                            var split = name.trim().split(" ");
                            if (split.length == 3) {
                                $scope.invalidname = false;
                            } else {
                                $scope.invalidname = true;
                            }
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
                if (!angular.isDefined(email) || email.trim().length == 0) {
                    $scope.isEmailInvalid = false;
                } else {
                    var emailresult = $scope.validateEmailId(email);
                    if (!emailresult) {
                        $scope.isEmailInvalid = true;
                    } else {
                        $scope.isEmailInvalid = false;
                    }
                }
            };
            $scope.validateWorkEmailId = function (email) {
                if ($scope.employee.workemailId !== undefined) {
                    var workEmailId = $scope.validateEmailId(email);
                    if (!workEmailId) {

                        $scope.isWorkEmailValidate = false;
                    }
                    else
                    {
                        $scope.isWorkEmailValidate = true;
                    }
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
            $scope.setMinDate = function () {
                if ($scope.employee.dob === "" || $scope.employee.dob === undefined) {
                    $scope.minToDate = $rootScope.getCurrentServerDate();
                } else {
                    $scope.minToDate = $scope.employee.dob;
                }
                if ($scope.employee.dob > $scope.employee.joiningDate) {
                    $scope.employee.joiningDate = $scope.employee.dob;
                }

            };
            //Calculate joining duration from joining date.
            $scope.joiningdate = new Date();
            $scope.$watch('employee.joiningDate', function (joiningDate) {
                if (joiningDate !== undefined) {
                    var doj = new Date(joiningDate).getTime();
                    var now = new Date().getTime();
                    if (doj > now || doj === 0 || doj === null) {
                        if (doj === 0 || doj === null) {
                            $scope.joiningDuration = '';
                        } else {
                            $scope.joiningDuration = '0 Month(s)';
                        }
                    } else {
                        var days = (now - doj) / (1000 * 60 * 60 * 24);
                        var months, years;
                        if (days < 30) {
                            months = 0;
                        } else {
                            months = Math.floor(days / 30);
                        }
                        if (months < 12) {
                            years = 0;
                        } else {
                            years = Math.floor(months / 12);
                            months = months % 12;
                        }
                        if (years > 0) {
                            $scope.joiningDuration = years + ' year(s) ' + months + ' month(s) ';
                        } else {
                            $scope.joiningDuration = months + ' month(s) ';
                        }
                    }
                } else {
                    $scope.joiningDuration = '';
                }
            });
            // Policy ends
            $scope.searchEmployee = function () {
                $scope.employeeList = [];
                if ($scope.searchtext.length > 0) {
                    Employee.searchEmployee({search: $scope.searchtext}, function (data) {
                        $scope.employeeList = angular.copy(data);
                    });
                }
            };
            //For file upload
            $scope.uploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: true,
//                accept:'application/pdf',
                "attributes": {accept: 'application/pdf'},
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
                    //Check file size greater than 5 MB
                    if (file.size > 5000000) {
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
            $scope.getProfileImageEmployee = function (isThumbnail) {
                if (isThumbnail) {
                    var imagename = $scope.employee.profileImageName;
                    imagename = imagename.substring(0, imagename.lastIndexOf("."));
                    imagename += "_T.jpg";
                    return $rootScope.appendAuthToken($rootScope.apipath + "/employee/getimage?file_name=" + imagename);
//                    return "api/employee/getimage?file_name=" + imagename + "&token=" + $rootScope.authToken;
                } else {
                    $scope.profileImagePopup($scope.employee.profileImageName);
                }
            };
            //Profile file upload success
            $scope.profileFileUploaded = function (file, flow, addEventForm, response) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "PROFILE";

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
//                        $scope.getProfileImageEmployee(true);
                        $scope.employee.profileImageName = response.res;

                    });
                });


                flow.cancel();
            };
            $scope.profileFileRemove = function () {
                // console.log("profiel remove");
                Employee.removeFileFromTemp($scope.employee.profileImageName);
                $scope.employee.profileImageName = undefined;
            };
            //For Family file 
            $scope.familyImg = {};
            $scope.familyFileAdded = function (file, flow) {
                $scope.familyImg.invalidFileFlag = false;
                $scope.familyImg.invalidFileSizeFlag = false;

                $scope.familyFlow = flow;
                $scope.uploadFile.query.fileType = "PROFILE";
                $scope.familyUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")) {
                    $scope.familyImg.invalidFileFlag = true;
                    $scope.familyImg.fileName = file.name;
                } else {
                    //Check file size greater than 5 MB
                    if (file.size > 5000000) {
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
            $scope.familyFileUploaded = function (file, flow, addEventForm, response, isEdit) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "PROFILE";
                if (isEdit) {
                    $scope.editfamily.familyImageName = response;
                } else {
                    $scope.employee.family[$scope.familyIndex].familyImageName = response;
                }
                $scope.familyUploaded = true;
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
                        if (isEdit) {
                            $scope.editfamily.familyImageName = response.res;
                        } else {
                            $scope.employee.family[$scope.familyIndex].familyImageName = response.res;
                        }
                    });
                });
                flow.cancel();
            };
            $scope.familyFileRemove = function (index) {
                Employee.removeFileFromTemp($scope.employee.family[index].familyImageName);
                $scope.employee.family[index].familyImageName = undefined;
            };
            $scope.familyFileRemoveEdit = function () {
                Employee.removeFileFromTemp($scope.editfamily.familyImageName);
                $scope.editfamily.familyImageName = undefined;

            };
            //For Salaryslip file 
            $scope.salaryslipImg = {};
            $scope.salaryslipFileAdded = function (file, flow) {
                $scope.salaryslipImg.invalidFileFlag = false;
                $scope.salaryslipImg.invalidFileSizeFlag = false;
                $scope.salaryslipFlow = flow;
                $scope.uploadFile.query.fileType = "SALARYSLIP";
                $scope.salaryslipUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif") && (file.getExtension() != "pdf")) {
                    $scope.salaryslipImg.invalidFileFlag = true;
                    $scope.salaryslipImg.fileName = file.name;
                } else {
                    //Check file size greater than 5 MB
                    if (file.size > 5000000) {
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
            $scope.salaryslipFileUploaded = function (file, flow, addEventForm, response, isEdit) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "SALARYSLIP";


                $scope.salaryslipUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                var modelName = 'Franchise';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "false";
                var info;
                Employee.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;

                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function (response) {
                        if (isEdit) {

                            $scope.editexp.salaryslipImageName = response.res;
                        } else {
                            $scope.employee.exp[$scope.expIndex].salaryslipImageName = response.res;

                        }
                    });
                });

                flow.cancel();
            };
            $scope.salaryslipRemove = function (index) {
                Employee.removeFileFromTemp($scope.employee.exp[index].salaryslipImageName);
                $scope.employee.exp[index].salaryslipImageName = undefined;
            };
            $scope.salaryslipRemoveEdit = function () {
                Employee.removeFileFromTemp($scope.editexp.salaryslipImageName);
                $scope.editexp.salaryslipImageName = undefined;
            };
            //For Otherdocs file 
            $scope.otheruploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: false,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Franchise'
                }
            };
            $scope.otherdocsImg = {};
            $scope.employee.otherdocs = [];
            $scope.otherDocIndex = 0;
            $scope.otherdocsFileAdded = function (file, flow) {
                $scope.otherdocsImg.invalidFileFlag = false;
                $scope.otherdocsImg.invalidFileSizeFlag = false;
                $scope.otherdocsFlow = flow;
                $scope.uploadFile.query.fileType = "OTHER";
                $scope.otherdocsUploaded = false;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif") && (file.getExtension() != "pdf") && (file.getExtension() != "txt") && (file.getExtension() != "doc") && (file.getExtension() != "docx")) {
                    $scope.otherdocsImg.invalidFileFlag = true;
                    $scope.otherdocsImg.fileName = file.name;
                } else {
                    //Check file size greater than 5 MB
                    if (file.size > 5000000) {
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
                    txt: 1,
                    doc: 1,
                    docx: 1
                }
                [file.getExtension()];
            };
            //Otherdocs file upload success
            $scope.otherdocsFileUploaded = function (file, flow, addEventForm, response) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "OTHER";
//                $scope.employee.otherdocs.push(response);
                $scope.hasAnyDoc = true;
                $scope.otherdocsUploaded = true;
                if ($scope.saveEventFlag === false) {
                    $scope.saveFilesAndEvent(addEventForm);
                }
                var modelName = 'Franchise';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "false";
                var info;
                Employee.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;

                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function (response) {
                        $scope.employee.otherdocs.push(response.res);
                        $scope.employee.otherdocsDate.push(new Date());
                    });
                });
            };
            $scope.otherdocRemove = function (index) {
                Employee.removeFileFromTemp($scope.employee.otherdocs[index]);
                $scope.employee.otherdocs[index] = null;
                $scope.employee.otherdocs.splice(index, 1);
                $scope.employee.otherdocsDate[index] = null;
                $scope.employee.otherdocsDate.splice(index, 1);
                var j = 0;
                for (var i = 0; i < $scope.employee.otherdocs.length; i++) {
                    var item = $scope.employee.otherdocs[i];
                    if (item !== null) {
                        j = j + 1;
                    }
                }
                if (j === 0) {
                    $scope.employee.otherdocs = [];
                    $scope.employee.otherdocsDate = [];
                    $scope.hasAnyDoc = false;
                    $scope.docsViewAllModal.dismiss();
//                    $('#viewdocsModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
            };
            $scope.openViewDocs = function () {
                $scope.docsViewAllModal = $modal.open({
                    templateUrl: 'docsViewAllTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#viewdocsModal').modal('show');
            };
            $scope.isResigned = false;
            $scope.getStatusValue = function () {
                $scope.isResigned = false;
                for (var i = 0; i < $scope.statusList.length; i++) {
                    $scope.statusList[i].label = $rootScope.translateValue("EMPLOYEE." + $scope.statusList[i].label);
                    var resigned = $rootScope.translateValue("EMPLOYEE." + 'Resigned');
                    if ($scope.statusList[i].value === $scope.employee.workstatus && $scope.statusList[i].label === resigned) {
                        $scope.isResigned = true;

                        break;
                    }
                }
            };
            $scope.beforeStatus = function () {
                $scope.beforeStatus = $scope.empstatus;
            };
            $scope.proceed = function (form) {
                $scope.isEmployeeTerminationConfirmed = true;
                $scope.addEmployee(form);
//                var proceed = [$scope.employee.id, $scope.employee.relievingDate, $scope.employee.workstatus];
//
//                Employee.terminateEmployee(proceed, function()
//                {
//                    $('#terminateEmployeeModal').modal('hide');
//                    $('.modal-backdrop').remove();
//                }
//                );
//                resetVariable();
//                $scope.resetPage();
            };
            $scope.cancelproceed = function () {
                $scope.employee.workstatus = $scope.beforeStatus;
                $scope.isResigned = false;
                $scope.terminateEmpModal.dismiss();
//                $('#terminateEmployeeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.resetPage = function () {
                $route.reload();
            };

            $scope.getProfileImageContact = function (familyIndex, isEdit, isThumbnail) {
                var imagename = '';
                if (isEdit) {
                    imagename = $scope.editfamily.familyImageName;
                } else {
                    imagename = $scope.employee.family[familyIndex].familyImageName;
                }
                if (isThumbnail) {
                    imagename = imagename.substring(0, imagename.lastIndexOf("."));
                    imagename += "_T.jpg";
//                    return "api/employee/getimage?file_name=" + imagename + "&token=" + $rootScope.authToken;
                    return $rootScope.appendAuthToken($rootScope.apipath + "/employee/getimage?file_name=" + imagename);
                } else {

                    $scope.familyImagePopup(imagename);
                }
            };

            $scope.setViewFlag = function (flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayEmployeeFlag = flag;
            };
            $scope.getSearchedEmployee = function (list) {
//                // console.log('list' + JSON.stringify(list));
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function (item) {
                            item.empName = $rootScope.translateValue("EMP_NM." + item.empName);
                            item.department = $rootScope.translateValue("DPT_NM." + item.department);
                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                }
            };

            $scope.setEmployeeCode = function () {
//                $scope.employee.employeeCode = $scope.employeeCode[$scope.employee.empType];
            }
            ;
            $scope.openChangePassword = function () {
                $scope.passwordObj = {};
                $scope.userid = $scope.employee.userId;
                $scope.olduserid = $scope.employee.userId;
                $scope.passwordObj.notUniqueUserId = false;
                $scope.passwordObj.passsubmitted = false;
                $scope.passworChangeModal = $modal.open({
                    templateUrl: 'changePasswordTmpl.html',
                    scope: $scope,
                    size: 'lg'
                });
//                $('#changePasswordModal').modal('show');
            };
            $scope.$watch('userid', function () {
                $scope.userid = $filter('lowercase')($scope.userid);
            });
            $scope.doesUserIdExist = function (name) {
                if (name && name.length > 0) {
                    if (name !== $scope.olduserid) {
                        Employee.doesUserIdExist(name, function (res) {
                            if (res.data) {
                                $scope.passwordObj.notUniqueUserId = true;
                            } else {
                                $scope.passwordObj.notUniqueUserId = false;
                            }
                        }, function () {
                        });
                    } else {
                        $scope.passwordObj.notUniqueUserId = false;
                    }
                } else {
                    $scope.passwordObj.notUniqueUserId = false;
                }
            };
            $scope.resetPassword = function () {
                $scope.passwordObj.confirmpassword = undefined;
                $scope.passwordObj.password = undefined;
                $scope.passwordObj.passsubmitted = false;
                $scope.passwordObj = {};
                $scope.passworChangeModal.dismiss();
//                $('#changePasswordModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.confirmPasswordValidation = function () {
                if (!angular.equals($scope.passwordObj.password, $scope.passwordObj.confirmpassword)) {
                    $scope.passwordObj.isMatched = true;
                }
                else {
                    $scope.passwordObj.isMatched = false;
                }
            };
            $scope.changePassword = function (form) {
                $scope.passwordObj.passsubmitted = true;
                if (form.$valid && !$scope.passwordObj.isMatched && !$scope.passwordObj.notUniqueUserId) {
                    $rootScope.maskLoading();
                    var success = function () {
                        $rootScope.unMaskLoading();
                        $scope.passworChangeModal.dismiss();
//                        $('#changePasswordModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $scope.passwordObj.passsubmitted = false;
                    };
                    var failure = function () {
                        $rootScope.unMaskLoading();
                        $scope.passworChangeModal.dismiss();
//                        $('#changePasswordModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $scope.passwordObj.passsubmitted = false;
                    };
                    var userid = $scope.employee.id;
                    var json = {id: userid.toString(), password: $scope.passwordObj.password, userid: $scope.userid};
                    Employee.changePassword(json, success, failure);

                }
            };

            //To display profile image in popup
            $scope.profileImagePopup = function (profileImageName) {
                $scope.selectedProfileImageName = profileImageName;
                $("#profileImagePopup").modal("show");
            };
            //To display family image in popup
            $scope.familyImagePopup = function (familyImageName) {
                $scope.selectedFamilyImageName = familyImageName;
                $("#familyImagePopup").modal("show");
            };
            $scope.resetCustomFields = function (sectionName)
            {
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageEmployees");
                templateData.then(function (section) {

                    if (sectionName === 'PERSONAL' || sectionName === null) {
                        $scope.personalData = {};
                        $scope.customPersonalTemplateDate = angular.copy(section['PERSONAL']);
                        $scope.personalTemplate = $rootScope.getCustomDataInSequence($scope.customPersonalTemplateDate);
                    }
                    if (sectionName === 'CONTACT' || sectionName === null) {
                        $scope.contactData = {};
                        $scope.customContactTemplateDate = angular.copy(section['CONTACT']);
                        $scope.contactTemplate = $rootScope.getCustomDataInSequence($scope.customContactTemplateDate);
                    }
                    if (sectionName === 'IDENTIFICATION' || sectionName === null) {
                        $scope.identificationData = {};
                        $scope.customIdentificationTemplateDate = angular.copy(section['IDENTIFICATION']);
                        $scope.identificationTemplate = $rootScope.getCustomDataInSequence($scope.customIdentificationTemplateDate);
                    }
                    if (sectionName === 'OTHER' || sectionName === null) {
                        $scope.otherData = {};
                        $scope.customOtherTemplateDate = angular.copy(section['OTHER']);
                        $scope.otherTemplate = $rootScope.getCustomDataInSequence($scope.customOtherTemplateDate);
                    }
                    if (sectionName === 'HKGWORK' || sectionName === null) {
                        $scope.hkgworkData = {};
                        $scope.customWorkTemplateDate = angular.copy(section['HKGWORK']);
                        $scope.hkgworkTemplate = $rootScope.getCustomDataInSequence($scope.customWorkTemplateDate);
                    }
                    if (sectionName === 'EDUCATION' || sectionName === null) {
                        $scope.tempeducationData = {};
                        $scope.customEducationTemplateDate = angular.copy(section['EDUCATION']);
                        $scope.educationTemplate = $rootScope.getCustomDataInSequence($scope.customEducationTemplateDate);
                    }
                    if (sectionName === 'EXPERIENCE' || sectionName === null) {
                        $scope.tempexperienceData = {};
                        $scope.customExperienceTemplateDate = angular.copy(section['EXPERIENCE']);
                        $scope.experienceTemplate = $rootScope.getCustomDataInSequence($scope.customExperienceTemplateDate);
                    }
                    if (sectionName === 'FAMILY' || sectionName === null) {
                        $scope.tempfamilyData = {};
                        $scope.customFamilyTemplateDate = angular.copy(section['FAMILY']);
                        $scope.familyTemplate = $rootScope.getCustomDataInSequence($scope.customFamilyTemplateDate);
                    }
                    if (sectionName === 'POLICY' || sectionName === null) {
                        $scope.temppolicyData = {};
                        $scope.customPolicyTemplateDate = angular.copy(section['POLICY']);
                        $scope.policyTemplate = $rootScope.getCustomDataInSequence($scope.customPolicyTemplateDate);
                    }
                    if (sectionName === 'genralSection' || sectionName === null) {
                        $scope.generalData = {};
                        $scope.customGeneralTemplateDate = angular.copy(section['genralSection']);
                        $scope.generaltemplate = $rootScope.getCustomDataInSequence($scope.customGeneralTemplateDate);
                    }
                    $timeout(function () {
                        $scope.personalreload = true;
                        $scope.contactreload = true;
                        $scope.identificationreload = true;
                        $scope.otherDataflag = true;
                        $scope.hkgworkDataflag = true;
                        $scope.tempeducationflag = true;
                        $scope.tempexperienceflag = true;
                        $scope.familyreload = true;
                        $scope.temppolicyflag = true;
                        $scope.generalreload = true;
                    }, 50);
                }, function (reason) {
                }, function (update) {
                });
            };

            $scope.setDesignationForDept = function (selectedDepartmentDropdown, isCreate)
            {
                $scope.designationList = [];
                $scope.designationListForWorkAs = [];
                var parentdeptId;
                console.log("parent,,," + JSON.stringify(selectedDepartmentDropdown));
                if (isCreate) {
                    if (selectedDepartmentDropdown.currentNode.parentId !== null && selectedDepartmentDropdown.currentNode.parentId !== undefined && selectedDepartmentDropdown.currentNode.parentId !== 0)
                    {
                        console.log("in if")
                        parentdeptId = selectedDepartmentDropdown.currentNode.parentId;
                    } else
                    {
                        parentdeptId = selectedDepartmentDropdown.currentNode.id;
                    }
                } else
                {
                    if (selectedDepartmentDropdown.parentId !== null && selectedDepartmentDropdown.parentId !== undefined && selectedDepartmentDropdown.parentId !== 0)
                    {
                        console.log("in if")
                        parentdeptId = selectedDepartmentDropdown.parentId;
                    } else
                    {
                        parentdeptId = selectedDepartmentDropdown.id;
                    }
                }
                console.log("Dept" + parentdeptId);
                Employee.retrieveDesgByDept(parentdeptId, function (res)
                {

                    console.log("Result..." + JSON.stringify(res));
                    angular.forEach(res, function (item) {
                        item.designationName = $rootScope.translateValue("DESIG_NM." + item.label);

                        $scope.designationList.push(item);
                        item.modelName = item.value;
                        $scope.designationListForWorkAs.push(item);
                    });
                });
            };
            $(function () {
                $(window).bind('mousewheel', function (event, delta) {
                    $("#sidemenu").height($("#mainPanel").height());
                });

                $(window).bind('scroll', function (event) {
                    $("#sidemenu").height($("#mainPanel").height());
                });
            });

            $scope.makeAScroll = function () {
                $scope.scrollTo('mainPanel');
            }
            $scope.closeViewAllEdu = function () {
                $scope.eduViewAllModal.dismiss();
            };
            $scope.closeViewAllExp = function () {
                $scope.expViewAllModal.dismiss();
            };
            $scope.closeViewAllFamily = function () {
                $scope.familyViewAllModal.dismiss();
            };
            $scope.closeViewAllPolicy = function () {
                $scope.policyViewAllModal.dismiss();
            };
            $scope.hidedocsViewAll = function () {
                $scope.docsViewAllModal.dismiss();
            };
            $rootScope.unMaskLoading();
        }]);
    hkg.register.filter('optionsWrap', function () {
        return function (option, len) {
            var num = parseInt(n, 10);
            len = parseInt(len, 10);
            if (isNaN(num) || isNaN(len)) {
                return n;
            }
            num = '' + num;
            while (num.length < len) {
                num = '0' + num;
            }
            return num;
        };
    });

});