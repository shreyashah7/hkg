define(['hkg', 'dynamicForm', 'rapCalcyDirective', 'customFieldService'], function(hkg) {
    hkg.register.controller('RoughCalcyController', ["$rootScope", "$scope", "DynamicFormService", "CustomFieldService", "RapCalcyService", function($rootScope, $scope, DynamicFormService, CustomFieldService, RapCalcyService) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "roughCalcy";
            $rootScope.activateMenu();
            $scope.calc = {};

            $scope.calc.discountDetailsMap = {};
            $scope.conditions = [{cond: {"carat_type$DRP$Long": "Expected", "size$DRP$long": 0.001, "fluroscene$DRP$String": 18, "clarity$DRP$String": 9, "color$DRP$String": 14},
                    def: {field1$DRP$Long: {"id": 28, "text": "3-F3"}}}];
            CustomFieldService.retrieveDesignationBasedFields("calcyRough", function(response) {

                var templateDataTmp = DynamicFormService.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
                templateDataTmp.then(function(section) {
                    $scope.generaRoughCalcyTemplate = section['genralSection'];
                    var roughCalcyField = [];
                    var result = Object.keys(response).map(function(key, value) {
                        angular.forEach(this[key], function(itr) {
                            if (key === 'RoughCalcy') {
                                roughCalcyField.push({RoughCalcy: itr});
                            }
                        });
                    }, response);
                    $scope.generaRoughCalcyTemplate = DynamicFormService.retrieveCustomData($scope.generaRoughCalcyTemplate, roughCalcyField);
                }, response);
            }, function() {
                $rootScope.unMaskLoading();
                var msg = "Failed to retrieve data";
                var type = $rootScope.error;
                $rootScope.addMessage(msg, type);
            });
            $scope.calculateRate = function() {
                //                console.log("calculate rate called");
//                Check if all 4c are selected
                var fourCPresent = true;
                $scope.calc.fourCMap = {};
                for (var i = 0; i < $rootScope.fourCMap.length; i++) {
                    var value = $scope.calc.discountDetailsMap[$rootScope.fourCMap[i]];
                    if (!value) {
                        fourCPresent = false;
                        break;
                    }
                    $scope.calc.fourCMap[$rootScope.fourCMap[i]] = value;
                }

                //If they are apresent then calculate basprice and discount
                if (fourCPresent) {

                    RapCalcyService.calculateDiamondPrice($scope.calc, function(response) {
                        $scope.calcFinal = response.data;
                        if ($scope.calcFinal.baseAmount == null) {
                            $scope.calcFinal.baseAmount = 0.0
                        }
                        if ($scope.calcFinal.discount == null) {
                            $scope.calcFinal.discount = 0.0
                        }
                        if ($scope.calcFinal.amount == null) {
                            $scope.calcFinal.amount = 0.0
                        }
                        if ($scope.calcFinal.mixAmount == null) {
                            $scope.calcFinal.mixAmount = 0.0
                        }
                    });
                }
            };
            $scope.reset = function() {
                var dropdownScope = angular.element('#field1').scope();
                dropdownScope.dropdown.push({"id": 5, "text": "6-Raj"});
            };
        }
    ]);
});