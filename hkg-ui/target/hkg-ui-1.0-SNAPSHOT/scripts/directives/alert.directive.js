define(['angular'], function() {
    globalProvider.compileProvider.directive('agAlert', ['$compile', function($compile) {
        return {
            restrict: 'A',
            replace:true,
            link: function(scope, element, attrs) {
              
                var entity = attrs.entity;
                //alert message template
                var template =
                '<div class="form-group">&nbsp;</div>'+
                '<div ng-repeat="alert in validations"  style="cursor: pointer">'+
                '<a class="list-group-item"  style="cursor: pointer;text-decoration: none" ng-class="alert.type" close="closeAlertMessage($index)">'+
                '<div class="list-group-item-text"><i18n entity="'+entity+'" environment="S" text-expr="alert.msg" type="VM"></i18n><span class="glyphicon  glyphicon-remove pull-right" title="Cancel" ng-click="closeAlertMessage($index)"></span></div>'+
                '</div>'+
                '</a>'+
                '<div class="form-group" ng-show="validations.length !== 0">&nbsp;</div>';

                //Rendering template.
                element.html('').append($compile(template)(scope));
            }
        };
    }]);
});