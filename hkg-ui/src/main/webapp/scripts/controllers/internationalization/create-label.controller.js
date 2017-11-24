/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'internationalizationService', 'alert.directive'], function(hkg, internationalizationService) {
    hkg.register.controller('InternationalizationAddLabel', ["$rootScope", "$scope", "Internationalization", function($rootScope, $scope, Internationalization) {
            $scope.i18Int = "INTERNATIONALIZATION.";
            $scope.pageSetup = function() {
                $scope.listLabelRowCount = [1, 2, 3, 4, 5, 10, 15, 20];
                $scope.selectedRowCount = 1;
                $scope.label = [];
//            $scope.labelEntitys = ["Entity1", "Entity2", "Entity3"];

                $scope.submitted = false;
                Internationalization.getConstants(function(response) {
                    $scope.labelTypes = response["types"];
                    $scope.labelEnvironments = response["environments"];
                    $scope.labelEntitys = response["entities"];
                    $scope.setDefaultTable($scope.labelTypes, $scope.labelEnvironments, $scope.labelEntitys);
                }, function() {
//                    console.log("failed");
                    var msg = "Failed to get Constants";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });

            };
            $scope.setDefaultTable = function(types, envs, entitys) {
                $scope.selectedLabelTypeForAll = Object.keys(types)[0];
                $scope.selectedLabelEnvironmentForAll = Object.keys(envs)[0];
                $scope.selectedLabelEntityForAll = entitys[0];
                $scope.label.push({"text": "", "type": Object.keys(types)[0], "environment": Object.keys(envs)[0], "entity": entitys[0]});
            };
            $scope.setNumberDropdown = function() {
//                $scope.selectedRowCount = selectedRowCount;
                for (var i = 0; i < ($scope.selectedRowCount - 1); i++) {
                    $scope.label.push({"text": "", "type": Object.keys($scope.labelTypes)[0], "environment": Object.keys($scope.labelEnvironments)[0], "entity": $scope.labelEntitys[0]});
                }
            };
            $scope.setSelectedType = function(selected_type, labellist_obj) {
                labellist_obj.type = selected_type;
            };
            $scope.setSelectedEnvironment = function(selected_env, labellist_obj) {
                labellist_obj.environment = selected_env;
            };
            $scope.setSelectedEntity = function(selected_entity, labellist_obj) {
                labellist_obj.entity = selected_entity;
            };
            $scope.getRange = function() {
                return new Array($scope.selectedRowCount);
            };
            $scope.setSelectedTypeAll = function(selected_type_for_all) {
                $scope.selectedLabelTypeForAll = selected_type_for_all;
                for (var i = 0; i < $scope.selectedRowCount; i++) {
                    $scope.label[i].type = selected_type_for_all;
                }
            };
            $scope.setSelectedEnvironmentAll = function(selected_env_for_all) {
                $scope.selectedLabelEnvironmentForAll = selected_env_for_all;
                for (var i = 0; i < $scope.selectedRowCount; i++) {
                    $scope.label[i].environment = selected_env_for_all;
                }
            };
            $scope.setSelectedEntityAll = function() {
//                $scope.selectedLabelEntityForAll = selected_entity_for_all;
                for (var i = 0; i < $scope.selectedRowCount; i++) {
                    $scope.label[i].entity = $scope.selectedLabelEntityForAll;
                }
            };
            $scope.addLabel = function() {
//                alert("Call");
                
                $scope.submitted = true;
                $scope.textsubmitted = true;
                for (var i = 0; i < $scope.selectedRowCount; i++) {
                    console.log(JSON.stringify($scope.label[i]));
                }
                if ($scope.labelform.$valid) {
                    var success = function() {
                        toastr.success("Labels created");
                        $scope.textsubmitted = false;
                        //set to default values
                        $scope.labelform.$setPristine();
                        $scope.pageSetup();

                    };
                    var failure = function() {
                        toastr.error("Labels not created");
                    };
                    Internationalization.createLabel($scope.label, success, failure);
                }
//                alert(JSON.stringify($scope.label));
            };
            $scope.fnCopyLabel = function() {
                Internationalization.copyLabelToPropertyFile(function() {
//                    toastr.success("Labels added");
                    var msg = "Labels Added Succesfully.";
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                }, function() {
//                    toastr.error("Labels not added");
                    var msg = "Failed to Add Labels. ";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });
            };
            //---------------------code to add language and country
//        Internationalization.retrieveCountryLanguages(function(response) {
//            $scope.countryMap = response["countries"];
//            $scope.languageMap = response["languages"];
//            $scope.setDefaultCountryLang($scope.countryMap, $scope.languageMap);
//        }, function() {
//        });
//
//        $scope.setDefaultCountryLang = function(country, language) {
//            $scope.selectedCountry = Object.keys(country)[0];
//            $scope.selectedLanguage = Object.keys(language)[0];
//            $scope.isLefttoRight = false;
//        };
//        $scope.setSelectedCountry = function(selected_country) {
//            $scope.selectedCountry = selected_country;
//        };
//        $scope.setSelectedLanguage = function(selected_lang) {
//            $scope.selectedLanguage = selected_lang;
//        };
//        $scope.addCountryLang = function() {
//            $scope.countryLangMap = {"countryCode": $scope.selectedCountry, "languageCode": $scope.selectedLanguage, "isLeftToRight": $scope.isLefttoRight, "encoding": "UTF-8"};
//            Internationalization.createCountryLanguage($scope.countryLangMap, function(response) {
//                if(response["status"] === "Country and Language exist"){
//                    toastr.error(response["status"]);
//                }
//                else{
//                    toastr.success("Country and language saved");
//                }
//            }, function() {
//                toastr.error("country and language not saved");
//            });
//        };
        }]);
});