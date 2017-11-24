/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'internationalizationService', 'alert.directive'], function(hkg, internationalizationService) {
    hkg.register.controller('InternationalizationLabelList', ["$rootScope", "$scope", "Internationalization", function($rootScope, $scope, Internationalization) {
            console.log("-->InternationalizationLabelList initialized");
            $scope.i18Int = "INTERNATIONALIZATION.";
            $scope.changeAutomaticLabelFlag = $rootScope.createLabelAutomatically;
            $scope.setCreateLabelAutomatically = function() {
                $rootScope.createLabelAutomatically = $scope.changeAutomaticLabelFlag;
            };
            $scope.retrieveLanguages = function() {

                $scope.languageDetails = Internationalization.getAllLanguages(function(data) {
                    //Success Call Back
                    Internationalization.getConstants(function(response) {
                        $scope.labelTypes = response["types"];
                        $scope.labelEnvironments = response["environments"];
                        $scope.labelEntitys = response["entities"];
                        $scope.setDefaultTable($scope.labelTypes, $scope.labelEnvironments, $scope.labelEntitys);
                    }, function() {
//                        console.log("failed");
                        var msg = "Failed to get Constants";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    });
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].code === "EN") {
                            $scope.selectedLanguageObject = data[i];
//                        $scope.selectedLanguage = data[i].name;
//                        $scope.selectedLanguageCode = data[i].code;
                            $scope.selectedLanguageObj = data[i];
                            $rootScope.maskLoading();
                            $scope.translationPendingLabelList = Internationalization.getTranslationPendingLabels(data[i], function(data) {
                                $rootScope.unMaskLoading();
                            });
                            break;
                        }
                    }
                    $scope.setDefaultTable = function(types, envs, entitys) {
                        types[0] = "All";
                        $scope.selectedLabelTypeForAll = Object.keys(types)[0];
                        envs[0] = "All";
                        $scope.selectedLabelEnvironmentForAll = Object.keys(envs)[0];
                        entitys[entitys.length] = "All";
                        $scope.selectedLabelEntityForAll = entitys[entitys.length - 1];
                        $scope.setLetter("pending");
                        $scope.isDisable = false;
                    };
                }, function() {
                    //Failure Call back
//                    toastr.error("failure");
                    var msg = "Failed to get All Languages";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });
            };


            $scope.orderProp = 'defaultText';
            $scope.direction = false;

            $scope.sort = function(column) {
                if ($scope.orderProp === column) {
                    $scope.direction = !$scope.direction;
                } else {
                    $scope.orderProp = column;
                    $scope.direction = false;
                }
            };

            $scope.setSelectedTypeAll = function() {
                $scope.isDisable = true;
//                $scope.selectedLabelTypeForAll = selected_type_for_all;
            };
            $scope.setSelectedEnvironmentAll = function() {
                $scope.isDisable = true;
//                $scope.selectedLabelEnvironmentForAll = selected_env_for_all;
            };
            $scope.setSelectedEntityAll = function() {
                $scope.isDisable = true;
//                $scope.selectedLabelEntityForAll = selected_entity_for_all;
            };

            $scope.setSelectedEnvironment = function(selected_environment_key, label) {
                $scope.filters = {};
                label.environment = selected_environment_key;
                $scope.selectedLabelEnvironment = selected_environment_key;
            };

            $scope.setSelectedType = function(selected_type_key, label) {
                $scope.filters = {};
                label.type = selected_type_key;
                $scope.selectedLabelType = selected_type_key;
            };

            $scope.updatedTranslation = null;
            $scope.setTranslationPending = function(pendingStatus) {
                if (pendingStatus === true) {
                    $scope.updatedTranslation = false;
                } else {
                    $scope.updatedTranslation = true;
                }
                $scope.filters = {};
            };

            $scope.setLanguage = function() {
                $scope.isDisable = true;
                $scope.selectedLanguageObject = $scope.selectedLanguageObj;
                $scope.selectedLanguage = $scope.selectedLanguageObj.name;
                $scope.selectedLanguageCode = $scope.selectedLanguageObj.code;
            };

            var selectedLetter;
            $scope.setLetter = function(letter) {
                selectedLetter = letter;
                $scope.selectedLetter = selectedLetter;
                $scope.filters = {};
            };

            $scope.otherCondition = true;
            $scope.alphabetCustomFilter = function(labelList) {
                if (selectedLetter !== null) {
                    if (selectedLetter === "all") {
                        return labelList && $scope.otherCondition;
                    }
                    if (selectedLetter === "pending") {
                        return (labelList.translationPending === $scope.otherCondition) && $scope.otherCondition;
                    } else {
                        var reg = new RegExp(selectedLetter, "gi");
                        return labelList.defaultText.substring(0, 1).match(reg) && $scope.otherCondition;
                    }
                } else {
                    return labelList && $scope.otherCondition;
                }
            };

            $rootScope.showLabelDialog = function(currentLabel) {

                $scope.selectedLabelEnvironment = currentLabel.environment;
                $scope.selectedLabelType = currentLabel.type;
                $scope.translationPendingUpdate = currentLabel.translationPending;
                $scope.currentLanguageUpdate = $scope.selectedLanguageCode;
                $scope.keyUpdate = currentLabel.key;
                $scope.countryUpdate = currentLabel.country;
            };

            $scope.updateLabel = function(label) {
                $scope.labelsubmitted = true;

                if ($scope.updatelabelform.$valid) {
                    if ($scope.updatedTranslation === null) {
                        $scope.updatedTranslation = label.translationPending;
                    }
                    var labelJsonObj = {
                        "key": label.key,
                        "country": label.country,
                        "language": label.language,
                        "defaultText": label.defaultText,
                        "text": label.text,
                        "translationPending": $scope.updatedTranslation,
                        "environment": label.environment,
                        "type": label.type,
                        "entity": label.entity
                    };
                    Internationalization.updateLabels(labelJsonObj, function(data) {
                        //Success Call Back
//                        toastr.success("Label updated");
                        var msg = "Label updated Succesfully.";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);

                        $scope.applyFilter();
                        $scope.labelsubmitted = false;
                        $scope.updatelabelform.$setPristine();
                    }, function() {
                        //Failure Call back
                        $scope.retrieveLanguages();
//                        toastr.error("failure");
                        var msg = "Failed to update Label.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };

            $scope.filters = {};
            $scope.applyFilter = function() {
                $scope.isDisable = false;
                $scope.filters = {};
                $rootScope.maskLoading();
                $scope.translationPendingLabelList = Internationalization.getTranslationPendingLabels($scope.selectedLanguageObject, function(data) {
                    //Success Call Back
                    $rootScope.unMaskLoading();
                }, function() {
                    //Failure Call back
                    toastr.error("failure");
                });
                if ($scope.selectedLabelEnvironmentForAll !== "0") {
                    $scope.filters.environment = $scope.selectedLabelEnvironmentForAll;
                }
                if ($scope.selectedLabelTypeForAll !== "0") {
                    $scope.filters.type = $scope.selectedLabelTypeForAll;
                }
                if ($scope.selectedLabelEntityForAll !== "All") {
                    $scope.filters.entity = $scope.selectedLabelEntityForAll;
                }
            };

            $scope.deleteLabel = function(label) {
                $scope.labelsubmitted = true;

                if ($scope.updatelabelform.$valid) {
                    Internationalization.deleteLabel(label, function(data) {
                        //Success Call Back
//                        toastr.success("Label deleted");
                        var msg = "Label deleted Succesfully.";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                        $scope.applyFilter();
                    }, function() {
                        //Failure Call back
                        $scope.retrieveLanguages();
//                        toastr.error("failure");
                        var msg = "Failed to delete Label.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);

                    });
                }
            };

        }]);
});