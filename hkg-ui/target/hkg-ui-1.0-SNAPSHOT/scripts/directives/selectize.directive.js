/**
 * @author Kuldeep Vithlani
 * 
 * Created by kvithlani 
 * 
 * It is used in formula configuration currently.
 * 
 * It can be used for select box with tagging support.
 * It provides support for moving cursor's caret and adding new items in select box.
 * 
 * 
 * For more information see this api documentation of selectize
 * https://github.com/brianreavis/selectize.js/blob/master/docs/api.md
 * 
 * Example:
 * 
 * <input class="form-control" placeholder="Configure Formula..." name="formula" ng-model="formula" ag-selectize="selectizeOptions" options="options"/>
 * 
 * In controller define:
 * 
 * $scope.selectizeOptions = {
 plugins: ['drag_drop'],
 persist: false,
 enableDuplicate: true,
 create: true,
 valueField: 'id',
 labelField: 'text',
 searchField: 'text',
 delimiter: ','
 }
 $scope.options = [
 {id: 'field_1', text: 'Field 1'},
 {id: 'field_2', text: 'Field 2'},
 {id: 'field_3', text: 'Field 3'}
 ];
 
 //For opt group
 
 <input class="form-control" placeholder="Configure Formula..." name="formula3" ng-model="formula2" ag-selectize="selectizeOptions2" options="options2" optgroups="optgroups2"/>
 
 
 $scope.selectizeOptions2 = {
 labelField: 'model',
 valueField: 'id',
 optgroupField: 'make',
 optgroupLabelField: 'name',
 optgroupValueField: 'id',
 optgroupOrder: ['chevrolet', 'dodge', 'audi'],
 searchField: ['model'],
 plugins: ['optgroup_columns', 'remove_button'],
 openOnFocus: true,
 sortField: 'text'
 }
 $scope.optgroups2 = [
 {id: 'dodge', name: 'Dodge'},
 {id: 'audi', name: 'Audi'},
 {id: 'chevrolet', name: 'Chevrolet'}
 ];
 $scope.options2 = [
 {id: 'avenger', make: 'dodge', model: 'Avenger'},
 {id: 'caliber', make: 'dodge', model: 'Caliber'},
 {id: 'caravan-grand-passenger', make: 'dodge', model: 'Caravan Grand Passenger'},
 {id: 'challenger', make: 'dodge', model: 'Challenger'},
 {id: 'ram-1500', make: 'dodge', model: 'Ram 1500'},
 {id: 'viper', make: 'dodge', model: 'Viper'},
 {id: 'a3', make: 'audi', model: 'A3'},
 {id: 'a6', make: 'audi', model: 'A6'},
 {id: 'r8', make: 'audi', model: 'R8'},
 {id: 'rs-4', make: 'audi', model: 'RS 4'},
 {id: 's4', make: 'audi', model: 'S4'},
 {id: 's8', make: 'audi', model: 'S8'},
 {id: 'tt', make: 'audi', model: 'TT'},
 {id: 'avalanche', make: 'chevrolet', model: 'Avalanche'},
 {id: 'aveo', make: 'chevrolet', model: 'Aveo'},
 {id: 'cobalt', make: 'chevrolet', model: 'Cobalt'},
 {id: 'silverado', make: 'chevrolet', model: 'Silverado'},
 {id: 'suburban', make: 'chevrolet', model: 'Suburban'},
 {id: 'tahoe', make: 'chevrolet', model: 'Tahoe'},
 {id: 'trail-blazer', make: 'chevrolet', model: 'TrailBlazer'},
 ];
 * 
 */
define(['angular'], function () {
    globalProvider.compileProvider.directive('agSelectize', ["$filter", function ($filter) {
        return {
            restrict: 'A',
            require: '?ngModel',
//            scope: {selectize: '=agSelectize', options: '=',  items: '=',optgroups: '=', model: '=ngModel'},
            link: function (scope, element, attrs, controller) {
                var selectize;
                scope.selectize = scope.selectizeOptions;
                scope.selectize.options = scope.options;
                scope.selectize.items = scope.items;
                var modelCtrl = controller;
                var $select = element.selectize(scope.selectize);
                selectize = $select[0].selectize;


                scope.$watch('options', function (newTags, oldTags) {

                    if (newTags.length < oldTags.length) {
                        angular.forEach(oldTags, function (obj, index) {
                            if (JSON.stringify(newTags).indexOf(JSON.stringify(obj)) < 0) {

//                                selectize.removeOption(obj.id);
                            }
                        })
                    } else {

                    }
                }, true);



                scope.$watch('i', function ()
                {
                    var items = [];
                    if (scope.items !== null && scope.items !== undefined && scope.items.length >0) {
                        items = scope.items.split('|');
                        angular.forEach(items, function (result)
                        {
                            var index = $filter('filter')(scope.options, function (tag) {
                                return tag.id.toString() === result.toString();

                            })[0];
                            selectize.addOption(index);
                            selectize.addItem(index.id);
                        })
                    }

                })


                scope.$watch('optgroups', function (newGroups, oldGroups) {
                    if (newGroups !== undefined) {
                        if (newGroups.length < oldGroups.length) {
                            angular.forEach(oldGroups, function (obj, index) {
                                if (JSON.stringify(newGroups).indexOf(JSON.stringify(obj)) < 0) {
                                    angular.forEach(scope.selectize.options, function (option, index) {
                                        if (option[scope.selectize.optgroupField] === obj.id) {
                                            selectize.removeOption(option.id);
                                        }
                                    });
                                }
                            })
                        } else {
                            angular.forEach(newGroups, function (tag, index) {
                                selectize.addOptionGroup(tag.id, tag);
                            });
                        }
                    }
                }, true);
                var modelVar = attrs.ngModel;
            }
        };
    }]);
});