define(['hkg', 'ruleService'], function(hkg, ruleService) {
    hkg.register.controller('RuleController', ["$rootScope", "$scope", "RuleService", "$log", "$templateCache", function($rootScope, $scope, RuleService, $log, $templateCache) {
            $rootScope.maskLoading();

            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageRule";
            $rootScope.activateMenu();
            $scope.entity = "RULE.";
            $scope.editFlag = true;
            $scope.submitted = false;
            $scope.priorityList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
            $scope.displayFlag = 'view';
            $scope.applyList = ["all", "any"];
            $scope.componentType = "";
            $scope.htmltext = " ";
            $scope.ruleObject = {}
            $scope.ruleList = [];

            $scope.showtemplate = false;
            $scope.entityList = {};


            $scope.operators = {};

            $scope.today = $rootScope.getCurrentServerDate();
            $scope.$on('$viewContentLoaded', function() {
                retrievePrerequisite();
            });
            $scope.makeEditFlagFalse = function() {
                $scope.editFlag = false;
                $scope.initAddRule();
            };
            var initializeRule = function() {
                var rule = {};
                rule.apply = "all";
                rule.entity = "";
                rule.field = "";
                rule.operator = "";
                rule.operatorList = [];
                rule.fieldList = [];
                rule.rowsubmitted = false;

                rule.priority = null;
                rule.values = [];
                rule.showtemplate = false;
                return rule;
            }
            $scope.initAddRule = function() {

                if (!$scope.editFlag) {
                    $scope.ruleList = [];
                    $scope.editFlag = false;
                    $scope.ruleObject = {};
                    $scope.ruleList = [];

                    var rule = initializeRule();
                    $scope.ruleList.push(rule);

                    $scope.ruleObject.remarks = "";
                    $scope.ruleObject.ruleName = "";

                    $scope.displayFlag = 'create';
                    RuleService.retrievePrerequisite(function(res) {
//                        console.log('-->>>'+JSON.stringify(res));
                        $scope.entityList = res.entity;
                        console.log($scope.entityList);
                        $scope.operators = res.operator;
                    });
                }

            };
            function retrievePrerequisite() {

                RuleService.retrievePrerequisite(function(res) {
                    console.log('entity::' + JSON.stringify(res['entity']));
//                    console.log('operator::' + JSON.stringify(res['operator']));
                    console.log('allrules::' + JSON.stringify(res['allrules']));
                });

// create method
//                var c = [];
//                var d = {entity: 61, field: 37, fieldType: "Text field", operator: "eq", value: "xyz", id: 1};
//                c.push(d);
//                var a = {apply: "all", ruleName: "packet-1-rule", rules: c};
//                RuleService.createRule(a, function(res) {
//                });
//                    var cc = [];
//                    var dd = {entity: 60, field: 2, fieldType: "Text field", operator: "eq", value: "xyz",id:1};
//                    cc.push(dd);
//                    var aa = {apply: "all", ruleName: "rule-2", rules: cc};
//                    RuleService.createRule(aa, function(res) {
//                    });

// update method
//                var cc = [];
//                var dd = {entity: 65, field: 2, fieldType: "\"Number\"", operator: "\"eq\"", value: "\"5\"",id:1};
//                cc.push(dd);
//                var aa = {apply: "all", id: 2, ruleName: "rule-2", rules: cc};
//                    RuleService.updateRule(aa, function(res1) {
//                    });

// retrieve fields by entity
//                var b = {primaryKey: 60};
//                RuleService.retrieveFieldsByEntity(b, function(res) {
//                });

// retrieve all rules
//                RuleService.retrieveAllRule(function(res) {
//                    console.log('rules:::' + JSON.stringify(res));
//                });

// retrieve rule by id
                var ruleId = {primaryKey: "54783ec644a29a8fc2936b80"};
                RuleService.retrieveRuleById(ruleId, function(res) {
                    console.log('retrieve by id......:::' + JSON.stringify(res));
                });

// delete rule by id
//                var ruleId = {primaryKey: "547719f344a2bbfdc05edea8"};
//                RuleService.deleteRuleById(ruleId, function(res) {
//                    console.log('delete by id......:::' + JSON.stringify(res));
//                });

// retrieve master by field id
//                var fieldId = {primaryKey: 7};
//                RuleService.retrieveMasterByFieldById(fieldId, function(res) {
//                    console.log('retrieve master by field id......:::' + JSON.stringify(res));
//                });
            }
            $scope.onEntityChange = function(index) {
                $log.info(index+" index");
                if ($scope.ruleList[index].entity!=null) {
                    console.log("entity--------------- "+$scope.ruleList[index].entity);

                    $scope.ruleList[index].operatorList = [];
                    var id = $scope.ruleList[index].entity;
//                    rule.entityName=
                    var json = {"primaryKey": id};
                    RuleService.retrieveFieldsByEntity("{\"primaryKey\": "+id+"}", function(res) {
                        $scope.ruleList[index].showtemplate = false;
                        $log.info(JSON.stringify(res));
                        $scope.ruleList[index].fieldList = res;
                    });
                }
            }

            $scope.onFieldChange = function(index) {
                if ($scope.ruleList[index].field!=null) {
                    $scope.componentType = $scope.ruleList[index].field.type;

                    $log.info("rule.field.shortcutCode"+$scope.ruleList[index].field.shortcutCode)
                    $scope.ruleList[index].operatorList = [];
                    $log.info("field--------------- "+JSON.stringify($scope.ruleList[index].field.type)+"    "+JSON.stringify($scope.operators[$scope.componentType]));
                    $scope.ruleList[index].operatorList = $scope.operators[$scope.componentType];
                    $scope.ruleList[index].showtemplate = false;
//                    alert("$scope.ruleList[index].showtemplate" + $scope.ruleList[index].showtemplate);
                }
            }
            $scope.onOperatorChange = function(index) {
                $scope.ruleList[index].showtemplate = false;
                if ($scope.ruleList[index].operator!=null&&$scope.componentType!=null) {
//                  
                    $scope.ruleList[index].showtemplate = true;
                }
            }
            $scope.onOperatorClick = function(index) {
                $scope.ruleList[index].showtemplate = false;
            }
            $scope.addRule = function(form, index) {
                $scope.ruleList[index].rowsubmitted = true;
                if (form.$valid) {
                    var flag = true;
                    var length = $scope.ruleList[index].operator.shortcutCode;
                    for (var i = 0;i<length;i++) {
                        var val = $scope.ruleList[index].values[i];
                        if (val!==null&&val!==undefined&&val.trim()!=="") {
                            flag = (flag&&true);
                        } else {
                            flag = false;
                        }
                    }
                    if (flag) {
                        var rule = initializeRule();
                        $scope.ruleList.push(rule);
                    }

//                    $scope.ruleList[index].rowsubmitted = false;
                }
            }
            $scope.remove = function(index) {
                $scope.ruleList.splice(index, 1);
                if ($scope.ruleList.length==0) {
                    var rule = initializeRule();
                    $scope.ruleList.push(rule);
                    $scope.ruleList[index].rowsubmitted = false;
                }

            }

            $scope.save = function(form) {
                $scope.submitted = true;
                var rules = [];
                var length = $scope.ruleList.length;
                for (var i = 0;i<length;i++) {
                    $scope.ruleList[i].rowsubmitted = true;
                    var tmprule = angular.copy($scope.ruleList[i]);
                    var rule = {};
                    rule.id = i;
                    rule.priority = tmprule.priority;
                    rule.entity = tmprule.entity;
                    rule.field = tmprule.field.id;
                    rule.operator = tmprule.operator.label;
                    rule.fieldType = tmprule.field.type;
                    rule.value = tmprule.values[0];
                    if (tmprule.values.length>1) {
                        rule.value1 = tmprule.values[1];
                    }
                    rules.push(rule);
                }
                if (form.$valid) {
                    alert("hello");
                    $scope.ruleObject.rules = rules;
                    $log.info(JSON.stringify($scope.ruleObject))
                    RuleService.createRule($scope.ruleObject, function(res) {
                        $log.info(JSON.stringify(res));
//                         $scope.initAddRule();
                    });

                    $scope.submitted = false;
                }
            }
            $scope.ruleCancel = function(form) {
                form.$setPristine();
                $scope.initAddRule();
            }
            $rootScope.unMaskLoading();
        }]);

});
