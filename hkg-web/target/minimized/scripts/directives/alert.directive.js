define(["angular"],function(){globalProvider.compileProvider.directive("agAlert",["$compile",function(a){return{restrict:"A",replace:true,link:function(f,d,c){var b=c.entity;
var e='<div class="form-group">&nbsp;</div><div ng-repeat="alert in validations"  style="cursor: pointer"><a class="list-group-item"  style="cursor: pointer;text-decoration: none" ng-class="alert.type" close="closeAlertMessage($index)"><div class="list-group-item-text"><i18n entity="'+b+'" environment="S" text-expr="alert.msg" type="VM"></i18n><span class="glyphicon  glyphicon-remove pull-right" title="Cancel" ng-click="closeAlertMessage($index)"></span></div></div></a><div class="form-group" ng-show="validations.length !== 0">&nbsp;</div>';
d.html("").append(a(e)(f))
}}
}])
});