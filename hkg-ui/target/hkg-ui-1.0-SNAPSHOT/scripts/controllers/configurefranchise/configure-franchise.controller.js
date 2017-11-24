define(['hkg', "leavemanagement/leave-workflow.controller", "franchiseConfigService", "shift/shift.controller", "fileUploadService"], function(hkg) {
    hkg.register.controller('ConfigureFranchise', ["$rootScope", "$scope", "FranchiseConfigService", "FileUploadService", "$filter", function($rootScope, $scope, FranchiseConfigService, FileUploadService, $filter) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "";
            $rootScope.childMenu = "";
            $rootScope.activateMenu();
            $scope.entity = "CONFIGFRANCHISE.";
            $scope.isHkadmin = $rootScope.session.isHKAdmin;
            $scope.configKeyNames = {msgsPerUser: "MESSAGING_MAX_MSG_PER_USER",
                mstAuthPwd: "MASTERS_AUTH_PWD",
                shiftReminderPeriod: "SHIFT_REMINDER_PERIOD",
                taskStatusUpdate: "TASK_STATUS_ON_LOGIN",
                taskArchivePeriod: "TASK_ARCHIVE_PERIOD",
                empSendMailTo: "EMP_SEND_MAIL_TO",
                empMinAge: "EMP_MIN_AGE",
                empMaxAge: "EMP_MAX_AGE",
                empDefaultImage: "EMP_DEFAULT_IMAGE",
                leaveMaxYearlyLeaves: "LEAVE_MAX_YEARLY_LEAVES",
                leaveBeyondLimit: "ALLOW_LEAVE_BEYOND_LIMIT",
                idConfiguration: "ID_CONFIGURATION",
                currentVisibilityStatus: "CURR_VISIBILITY_STATUS",
                noOfSnapids: "NO_OF_SNAP_IDS",
                email: "DEFAULT_XMPP_EMAIL_ADDRESS",
                noOfDiamondInQueue: "NO_OF_DIAMOND_ALLOWED_QUEUE",
                carrierBoyDesignation: "CARRIER_BOY_DESIGNATION",
                analyticEngineUsername: "ANALYTICS_ENGINE_USERNAME",
                analyticEnginePwd: "ANALYTICS_ENGINE_PWD",
                analyticServerUrl: "ANALYTICS_SERVER_URL",
                allowCarateVariation: "ALLOW_CARATE_VARIATION",
            };
            $scope.resetConfigurationForm = function(configFranchieForm) {
                $scope.submitted = false;
                configFranchieForm.$setPristine();
            };
            $scope.retrieveAllConfiguration = function() {
                $rootScope.maskLoading();
                $scope.responseComplete = false;
                FranchiseConfigService.retrieveAllConfiguration(function(res) {
                    $scope.responseComplete = true;
                    $rootScope.unMaskLoading();
                    $scope.franchiseConfig = res;
                    if ($scope.franchiseConfig[$scope.configKeyNames.mstAuthPwd] !== undefined) {
                        $scope.configExist = true;
                        $scope.franchiseConfig[$scope.configKeyNames.mstAuthPwd] = undefined;
                    }
                    if ($scope.franchiseConfig[$scope.configKeyNames.analyticEngineUsername] !== undefined
                            && $scope.franchiseConfig[$scope.configKeyNames.analyticEnginePwd] !== undefined) {
                        $scope.analytics.analyticConfig = true;
                        $scope.analytics.resetAnalyticConfig = false;
                        $scope.franchiseConfig[$scope.configKeyNames.analyticEnginePwd] = undefined;
                    } else {
                        $scope.analytics.isAnalytics = false;
                        $scope.analytics.analyticConfig = false;
                        $scope.franchiseConfig[$scope.configKeyNames.analyticEngineUsername] = undefined;
                        $scope.franchiseConfig[$scope.configKeyNames.analyticEnginePwd] = undefined;
                    }
                    $scope.setDefaultValues();
                    $scope.configFranchiseForm.$dirty = false;
                }, function() {
                    $scope.responseComplete = true;
                    $rootScope.addMessage("Could not retrieve configurations, please try again.", 1);
                    $rootScope.unMaskLoading();
                });
            };
            $scope.retrieveEmployeeTypes = function() {
                $rootScope.maskLoading();
                FranchiseConfigService.retrieveEmployeeTypes(function(res) {
                    $rootScope.unMaskLoading();
                    angular.forEach(res, function(empType, key) {
                        $scope.employeeTypes.push({key: "EMP_TYPE_" + empType.value, type: empType.label});
                    });
                    $scope.retrieveAllConfiguration();
                }, function() {
                    $rootScope.addMessage("Could not retrieve employee types, please try again.", 1);
                    $scope.retrieveAllConfiguration();
                    $rootScope.unMaskLoading();
                });
            };
            $scope.setDefaultValues = function() {
                if ($scope.franchiseConfig[$scope.configKeyNames.msgsPerUser] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.msgsPerUser] = "30";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.empSendMailTo] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.empSendMailTo] = "work";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.taskStatusUpdate] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.taskStatusUpdate] = "false";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.leaveBeyondLimit] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.leaveBeyondLimit] = "false";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.empMinAge] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.empMinAge] = "18";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.empMaxAge] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.empMaxAge] = "55";
                }
                if ($scope.franchiseConfig[$scope.configKeyNames.currentVisibilityStatus] === undefined) {
                    $scope.franchiseConfig[$scope.configKeyNames.currentVisibilityStatus] = "on";
                }
            };
            $scope.initConfigureFranchise = function(configFranchieForm) {
                //$("#myNav").affix();

                $rootScope.configureDefaultShift = false;
                $rootScope.configureWorkFlow = false;
                $scope.configFranchiseForm = configFranchieForm;
                $scope.franchiseConfig = {};
                $scope.pwd = {};
                $scope.resetPwd = undefined;
                $scope.analytics = {};
                $scope.employeeTypes = [];
                $scope.resetConfigurationForm(configFranchieForm);
                $scope.retrieveEmployeeTypes();
                //$scope.retrieveDiamondEntities();
                //$scope.retrieveFranchiseDesignations();
            };
            $scope.displayAvatar = function(view) {
                var imagename = $scope.franchiseConfig.EMP_DEFAULT_IMAGE;
                if (angular.isDefined(imagename)) {
                    if (!view) {
                        return $rootScope.appendAuthToken($rootScope.apipath + "franchiseconfig/getimage?file_name=" + imagename);
                    } else {
                        $scope.avtarImagePopup(imagename);
                    }
                }
            };
            //To display avtar image in popup
            $scope.avtarImagePopup = function(avtarImageName) {
                $scope.selectedAvtarImageName = avtarImageName;
                $("#avtarImagePopup").modal("show");
            };
            $scope.avtarFileRemove = function() {
                FranchiseConfigService.removeFileFromTemp($scope.franchiseConfig.EMP_DEFAULT_IMAGE);
                $scope.franchiseConfig.EMP_DEFAULT_IMAGE = undefined;
            };
            $scope.confirmPasswordValidation = function(configFranchieForm) {
                if (!angular.equals($scope.franchiseConfig[$scope.configKeyNames.mstAuthPwd], $scope.pwd.confirmPassword)) {
                    configFranchieForm.confPwd.$setValidity("pwdMismatch", false);
                }
                else {
                    configFranchieForm.confPwd.$setValidity("pwdMismatch", true);
                }
            };
//            $scope.checkMaxLimit=function(configFranchieForm){
//              if(parseInt($scope.franchiseConfig[$scope.configKeyNames.msgsPerUser])>100){
//                  configFranchieForm.maxMsgs.$setValidity("maxLimit",false);
//              }else{
//                  configFranchieForm.maxMsgs.$setValidity("maxLimit",true);
//              }  
//            };
            $scope.checkMaxLimit = function(component, value, validityVar) {
                if (parseInt(value) > 100) {
                    component.$setValidity(validityVar, false);
                } else {
                    component.$setValidity(validityVar, true);
                }
            };

            $scope.checkAgeLimitValidation = function(configFranchieForm, component, validityVar) {
                if (parseInt($scope.franchiseConfig[$scope.configKeyNames.empMinAge]) > parseInt($scope.franchiseConfig[$scope.configKeyNames.empMaxAge])) {
                    component.$setValidity(validityVar, false);
                } else {
                    configFranchieForm.maxAge.$setValidity("maxAgeLimit", true);
                    configFranchieForm.minAge.$setValidity("minAgeLimit", true);
                }
            };
            $scope.setConfigureWorkFlow = function(flag) {
                $rootScope.configureWorkFlow = flag;
            }
            $scope.setConfigureDefaultShift = function(flag) {
                $rootScope.configureDefaultShift = flag;
            }
            $scope.resetPassword = function() {
                $scope.franchiseConfig[$scope.configKeyNames.mstAuthPwd] = "";
                $scope.resetPwd = true;
            };

            $scope.cancelResetPassword = function() {
                $scope.franchiseConfig[$scope.configKeyNames.mstAuthPwd] = undefined;
                $scope.resetPwd = false;
            };

            $scope.checkOldPassword = function(configFranchieForm) {
                FranchiseConfigService.checkOldPassword($scope.pwd.oldPassword, function(res) {
                    if (res.messages === null) {
                        configFranchieForm.oldPwd.$setValidity('invalid', true);
                    } else {
                        $scope.oldPwdMsg = res.messages[0].message;
                        configFranchieForm.oldPwd.$setValidity('invalid', false);
                    }
                }, function() {
                });
            };

            $scope.saveConfiguration = function(configFranchieForm) {
                $scope.submitted = true;
                if (configFranchieForm.$valid) {
                    $rootScope.maskLoading();
                    if ($scope.analytics.isAnalytics === false || $scope.analytics.resetAnalyticConfig === false) {
                        $scope.franchiseConfig[$scope.configKeyNames.analyticEngineUsername] = undefined;
                        $scope.franchiseConfig[$scope.configKeyNames.analyticEnginePwd] = undefined;
                        $scope.franchiseConfig[$scope.configKeyNames.analyticServerUrl] = undefined;
                    }
                    FranchiseConfigService.saveConfiguration($scope.franchiseConfig, function(res) {
                        $rootScope.haveValue = angular.copy($scope.franchiseConfig[$scope.configKeyNames.currentVisibilityStatus]);
                        $rootScope.randomCount = Math.random();
                        //FranchiseConfigService.saveIdConfiguration($scope.entities, function (result) {
                        $rootScope.minAge = $scope.franchiseConfig[$scope.configKeyNames.empMinAge];
                        $scope.resetAll = false;
                        $rootScope.unMaskLoading();
                        $scope.initConfigureFranchise(configFranchieForm);
                        $scope.scrollTo("pageTop");
                        $rootScope.addMessage("Configuration saved succesfully.", 0);
                        //});

                    }, function() {
                        $scope.scrollTo("pageTop");
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Could not save details, please try again.", 1);
                    });
                }
            };

            //For file upload
            $scope.avtarImg = {};
            $scope.uploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: true,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Franchise'
                }
            };

            //Banner file upload success
            $scope.avtarFileUploaded = function(file, flow, configFranchiseForm, response) {
                var modelName = 'Franchise';
                var fileDetail = [file.name];
                var filenameformat;
                var thumbnail = "false";
                var info;
                FranchiseConfigService.uploadAvtar(fileDetail, function(result)
                {
                    filenameformat = result.res;
                    console.log('filenameformat' + filenameformat);
                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function(response) {
                        $scope.franchiseConfig[$scope.configKeyNames.empDefaultImage] = response.res;
                        $scope.avtarImg.fileName = response.res;
                    });
                });


            };

            //For Banner file 
            $scope.avtarFileAdded = function(file, flow) {
                $scope.avtarFlow = flow;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")) {
                    $scope.avtarImg.invalidFileFlag = true;
                    $scope.avtarImg.fileName = file.name;
                } else {
                    //Check file size greater than 10 MB
                    if (file.size > 5242880) {
                        $scope.avtarImg.invalidFileSizeFlag = true;
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

            $scope.heightInit = function() {
                $("#sidemenu").height($("#mainPanel").height());
            }
            $scope.scrollTo = function(eID) {
                $scope.setConfigureWorkFlow(false);
                $scope.setConfigureDefaultShift(false);

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

            $(function() {
                $(window).bind('mousewheel', function(event, delta) {
                    if (!$rootScope.configureWorkFlow && !$rootScope.configureDefaultShift) {
                        $("#sidemenu").height($("#mainPanel").height());
                    }
                });

                $(window).bind('scroll', function(event) {
                    if (!$rootScope.configureWorkFlow && !$rootScope.configureDefaultShift) {
                        $("#sidemenu").height($("#mainPanel").height());
                    }
                });
            });

            //------------------------- start of diamond ids - dmehta 16/12/2014 --------------------------------------------//           

            $scope.onlyCharacters = function(value, index) {
                var reg = /^[a-zA-Z]+$/;
                if (!reg.test(value)) {
                    $scope.entities[index].prefixCode = $scope.entities[index].prefixCode.substring(0, $scope.entities[index].prefixCode.length - 1);
                } else {
                    $scope.entities[index].prefixCode = $scope.entities[index].prefixCode.toUpperCase();
                }
            };

            $scope.onlyZeros = function(value, index) {
                if (value.length < 4) {
                    $scope.entities[index].pattern = '0000';
                }
                var regex = /^[0]+$/;
                if (!regex.test(value)) {
                    $scope.entities[index].pattern = $scope.entities[index].pattern.substring(0, $scope.entities[index].pattern.length - 1);
                }
            };

            $scope.confirmeReset = function(value) {
                if (value) {
                    $('#confirmResetPopUp').modal('show');
                } else {
                    angular.forEach($scope.entities, function(entity) {
                        entity.isReset = false;
                    });
                    $scope.resetAll = false;
                }
            };

            $scope.closeResetPopUp = function() {
                $scope.resetAll = false;
                $('#confirmResetPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };

            $scope.continueWithThis = function() {
                angular.forEach($scope.entities, function(entity) {
                    entity.isReset = true;
                });
                $('#confirmResetPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
         
            //------------------------- end of diamond ids - dmehta 16/12/2014 --------------------------------------------//

            $rootScope.unMaskLoading();
        }]);

    hkg.register.filter('numberFixedLen', function() {
        return function(n, len) {
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