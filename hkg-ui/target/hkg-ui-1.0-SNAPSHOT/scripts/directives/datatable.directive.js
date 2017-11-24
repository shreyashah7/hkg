/**
 * @author Kuldeep Vithlani
 * 
 * This directive is integrated with jquery datatable.
 * 
 * You can use all the options provided by jquery datatable. 
 * To use options of jquery datatable refer api at https://datatables.net
 * 
 * ====================== 
 * Client Side Datatable
 * ======================
 * For client side datatable use as following
 * 
 * ----------------------
 * |Basic Initialization|
 * ----------------------
 * Example:
 *  
 * HTML:
 * ----
 *  <table class="table" cellspacing="0" width="100%" ag-data-table="records" >
 <thead>
 <tr>
 <th>ID</th>
 <th>Category</th>
 <th>Status</th>
 <th>Created On</th>
 </tr>
 </thead>
 <tbody>
 <tr ng-repeat="record in records" dt-rows>
 <td>{{record[0]}}</td>
 <td>{{record[1]}}</td>
 <td>{{record[2]}}</td>
 <td>{{record[3]}}</td>
 </tr>
 </tbody>
 </table>
 * 
 * 
 * Here records is a scope variable in which we are going to store our data. 
 * Specify name of scope variable as ag-data-table attribute. This option is manadatory.
 * 
 * In Controller:
 * 
 *  var success=function(res){
 $scope.records = res.records;
 }
 var failure=function(res){
 console.log("failure");
 }
 //Factory method
 DatatableService.retrieveData(success,failure);
 * 
 * ----------------------------
 * |Overriding default options|
 * ----------------------------
 * 
 * If you want to override datatable options that are given by jquery datatable then use dt-options atrribute like following.
 * This attribute is optional. If you do not specify this then default options will be considered.
 
 *  <table  class="table" cellspacing="0" width="100%" ag-data-table="records"  dt-options="dataTableOptions" >
 *  
 *  where dataTableOptions is scope variable(For more options visit: https://datatables.net)
 *  e.g.
 $scope.dataTableOptions = {
 dom: "rtiS",
 scrollY: 370,
 scrollCollapse: true,
 scroller: {
 loadingIndicator: false
 }
 };
 * 
 * 
 * ====================== 
 * Server Side Datatable
 * ======================
 * For server side datatable use as following
 * 
 * HTML:
 * ----
 * 
 *  <table class="table" cellspacing="0" width="100%" ag-data-table="serverRecords" source-function="serverSidePaginationFunction" dt-options="dataTableOptions2" dt-search="search">
 <thead>
 <tr>
 <th>ID</th>
 <th>Category</th>
 <th>Status</th>
 <th>Created On</th>
 </tr>
 </thead>
 </table>
 * 
 * Here also you can override dt-options otherwise default options will be considered. 
 
 ---------------
 * Custom search
 * -------------
 * You can also specify custom search field in serverside pagination. For custom search specify scope variable in dt-search attribute.
 
 * * -----------------
 * |source-function|
 * -----------------
 * 
 * Here source-function is a funciton which will be called everytime when user change the page or when sorting or serching is applied.
 * Here you have to manage sorting or searching on serverside. 
 * 
 * Here source-function is serverSidePaginationFunction which is declared in controller as follwing:
 * 
 * In Controller:
 * --------------
 *          $scope.recordsTotal=300000;
 $scope.recordsFiltered=300000;
 $scope.serverSidePaginationFunction = function(data, recordsConfig, callback) {
 var success = function(res) {
 $scope.serverRecords = res.records;
 //                    //If you don't want to add html
 //                    recordsConfig.data=res.records;
 angular.forEach(res.records, function(row, index) {
 recordsConfig.data.push(["<a href='' ng-click='$parent.callFunction2("+row[0]+")'>"+row[0]+"</a>{{row[0]}}", "<a href='' ng-click='$parent.callFunction2("+row[1]+")'>"+row[1]+"</a>", row[2], row[3]]);
 });
 //If you want to add html
 //                    recordsConfig.data = res.records;
 recordsConfig.recordsTotal = $scope.recordsTotal;
 recordsConfig.recordsFiltered = $scope.recordsFiltered;
 callback(recordsConfig);
 
 }
 var failure = function(res) {
 console.log("failure");
 }
 DatatableService.retrieveData(data, success, failure);
 };
 
 Here recordsTotal and recordsFiltered are declared static only for example.
 You have to declare your source-function as this way:
 
 $scope.serverSidePaginationFunction = function(data, recordsConfig, callback){
 
 }
 
 Here also you have to set recordsTotal and recordsFiltered property    of recordsConfig object.
 Using this configuration pagination will be displayed. 
 
 After specifying this values call callback() funciton by passing recordsConfig object.
 
 
 
 Server side (Paging)
 =======================
 Example:
 ========
 
 If you want to use paging instead of scrolling then you can use this directive as follows:
 
 HTML:
 ----
 <h3>Server Side(Paging)</h3>
 <hr/>
 <table class="table" cellspacing="0" width="100%" ag-data-table="serverRecords2" source-function="serverSidePaginationFunction2" dt-options="dataTableOptionsPaging" >
 <thead>
 <tr>
 <th>ID</th>
 <th>Category</th>
 <th>Status</th>
 <th>Created On</th>
 </tr>
 </thead>
 <tbody>
 <tr ng-repeat="record in serverRecords2" dt-rows>
 <td>{{record[0]}}</td>
 <td>{{record[1]}}</td>
 <td>{{record[2]}}</td>
 <td>{{record[3]}}<input type='button' value='hi' ng-click="callFunction2(record[0])"/></td>
 </tr>
 </tbody>
 </table>
 
 Controller
 ==========
 In controller declare function in this way:
 
 $scope.serverSidePaginationFunction2 = function(data, recordsConfig, callback) {
 var success = function(res) {
 $scope.serverRecords2 = res.records;
 recordsConfig.recordsTotal = $scope.recordsTotal;
 recordsConfig.recordsFiltered = $scope.recordsFiltered;
 callback(recordsConfig);
 }
 var failure = function(res) {
 console.log("failure");
 }
 DatatableService.retrieveData(data, success, failure);
 };
 
 */
define(['angular'], function() {
    angular.module('hkg.directives').directive('agDataTable', ['DT_LAST_ROW_KEY', '$timeout', '$compile', function(DT_LAST_ROW_KEY, $timeout, $compile) {
            var $loading = angular.element('<h5>Loading...</h5>');
            return {
                restrict: 'A',
                scope: {agDataTable: '=', sourceFunction: '=', dtOptions: '=', dtSearch: '=', dtTotalRecords: '='},
                link: function(scope, $elem, attrs, controller) {
                    scope.dtable = undefined;
                    var _doRenderDataTable = function($elem, options) {

                        //To modify total records while using custom search
                        if (attrs.dtTotalRecords !== undefined) {
                            scope.$watch("dtTotalRecords", function(value, oldvalue) {
                                if (value !== oldvalue) {
                                    if (value !== undefined && value > 0) {
                                        if (scope.dtable === undefined) {
                                            $timeout(function() {
                                                scope.dtable = $elem.dataTable(options);
                                            }, 100);
                                        } else {
//                                            scope.dtable.fnSettings()._iRecordsTotal = value;
//                                            scope.dtable.fnSettings().aiDisplayMaster.length = value;
//                                            scope.dtable.fnSettings().aiDisplay.length = value;
                                            var newRows = $elem.find($("tbody tr"));
                                            scope.dtable.fnClearTable();
                                            scope.dtable._fnAddTr(newRows);
                                            scope.dtable.fnDraw();

                                        }
//                                        $compile($elem.find($("tbody")))(scope);
                                    } else if (value === 0) {
                                        if (scope.dtable !== undefined) {
                                            scope.dtable.fnClearTable();
                                            scope.dtable.fnDestroy();
                                        }
                                        scope.dtable = undefined;
                                    }
                                }

                            });
                        }
                        // Add $timeout to be sure that angular has finished rendering before calling datatables
                        $timeout(function() {
                            // Show datatable & hide loading
                            $elem.show();
//                            $loading.hide();
                            if (scope.dtable === undefined) {
                                //Code to display datatable in modal
                                //Put timeout while modal is displayed
                                if ($elem.closest('.modal').html() !== undefined) {
                                    $timeout(function() {
                                        scope.dtable = $elem.dataTable(options);
                                        $elem.parent().parent().parent().find("div.DTS_Loading").hide();
                                    }, 250);
                                } else {

                                    scope.dtable = $elem.dataTable(options);
                                    $elem.parent().parent().parent().find("div.DTS_Loading").hide();
                                }

                            }

                        });
                    };
                    
                    //An external order property to be used to order input field(DOM) with numeric data.
                    //To activate, use dt-options with columns=[{"orderDataType": "dom-text-numeric"},null,null].
                    //To be matched with respective column. null, if default required.
                    //Reference link : http://datatables.net/examples/plug-ins/dom_sort.html
                    /* Create an array with the values of all the input boxes in a column, parsed as numbers */
                    $.fn.dataTable.ext.order['dom-text-numeric'] = function(settings, col)
                    {
                        return this.api().column(col, {order: 'index'}).nodes().map(function(td, i) {
                            return $('input', td).val() * 1;
                        });
                    }
                    //Resize columns when window gets resized.
                    $(window).resize(function() {
                        if (scope.dtable !== undefined) {
                            $timeout(function() {
                                scope.dtable.fnAdjustColumnSizing();
                            });
                        }
                    });
                    
                    // apply DataTable options, use defaults if none specified by user
                    if (attrs.dtOptions !== undefined) {
                        scope.dataTableOptions = scope.dtOptions;
                        //set defaults if any option is not spcified by user
                        scope.dataTableOptions.scrollCollapse = true;
                        if (angular.isUndefined(scope.dtOptions.dom)) {
                            scope.dataTableOptions.dom = "rtiS";
                        }
                        if (angular.isUndefined(scope.dtOptions.scrollY)) {
                            scope.dataTableOptions.scrollY = 370;
                        }
                        if (angular.isUndefined(scope.dtOptions.scroller)) {
                            scope.dataTableOptions.scroller = {
                                loadingIndicator: true
                            };
                        }

                    } else {
                        if (attrs.sourceFunction !== undefined) {
                            scope.dataTableOptions = {
                                serverSide: true,
                                ordering: true,
                                searching: true,
                                dom: "rtiS",
                                scrollY: 370,
                                scrollCollapse: true,
                                scroller: {
                                    loadingIndicator: true
                                }
                            };
                        } else {
                            scope.dataTableOptions = {
                                dom: "rtiS",
                                scrollY: 370,
                                scrollCollapse: true,
                                scroller: {
                                    loadingIndicator: true
                                }
                            };
                        }
                    }
                    //To resize header columns of table
                    var $body = document.body
                            , $menu_trigger = $body.getElementsByClassName('menu-trigger')[0];

                    if (typeof $menu_trigger !== 'undefined') {
                        $menu_trigger.addEventListener('click', function() {
                            if (scope.dtable !== undefined) {
                                $timeout(function() {
                                    scope.dtable.fnAdjustColumnSizing();
                                }, 300);
                            }
                        })
                    }
                    ;
                    //Server side function
                    if (attrs.sourceFunction !== undefined) {

                        scope.dataTableFunction = scope.sourceFunction;

                        scope.dataTableOptions.ajax = function(data, callback, settings) {
                            scope.recordsConfig =
                                    {draw: data.draw,
                                        data: [],
                                        recordsTotal: 0,
                                        recordsFiltered: 0};
                            scope.responseComplete = function(recordsConfig) {
                                callback(recordsConfig);
                                //Newly added
                                if (scope.dataTableOptions.dom !== undefined && scope.dataTableOptions.dom.indexOf("S") >= 0) {
                                    $compile($elem.find($("tbody")))(scope);
                                    if (recordsConfig.data.length === 0) {
                                        $elem.parent().parent().parent().find("div.DTS_Loading").hide();
                                        $elem.parent().parent().parent().find(".dataTables_info").hide();
                                        $elem.parent().parent().parent().find(".dataTables_scroll").css("background", "none");
                                    }
                                } else {
                                    if (recordsConfig.recordsTotal > 0) {
                                        $(".dataTables_empty").hide();
                                    }
                                }
                            };
                            scope.data = data;
                            scope.dataTableFunction(scope.data, scope.recordsConfig, scope.responseComplete);
                        };

                        if (attrs.dtSearch !== undefined) {
                            scope.$watch("dtSearch", function(value) {
                                if (value !== undefined && value !== '') {
//                                    scope.data.search.value=value;
                                    scope.dtable.fnFilter(value);
//                                    scope.dtable.fnDraw();
                                }
                            });

                        }

                        scope.dtable = $elem.dataTable(scope.dataTableOptions);

                    }

                    //For client side
                    if (attrs.sourceFunction === undefined) {
//                        $elem.after($loading);
//                        $elem.hide();
                        scope.$watchCollection("agDataTable", function(newvalue) {

                            if (newvalue !== undefined && newvalue !== null && newvalue.length === 0 && scope.dtable !== undefined) {
//                                $loading.hide();

//                                 scope.dtable.fnDestroy();
//                                 $elem.hide();
//                                 scope.dtable=undefined;
                            } else {

                                $elem.show();
//                                    alert("hi");
                                if (scope.dtable !== undefined) {

//                                      $elem.after($loading);
                                    //                                $elem.hide();
                                    $timeout(function() {

                                        var newRows = $elem.find($("tbody tr"));
                                        var currentPage;
                                        var settings = scope.dtable.fnSettings();
                                        var
                                                start = settings._iDisplayStart,
                                                len = settings._iDisplayLength,
                                                visRecords = settings.fnRecordsDisplay(),
                                                all = len === -1;
                                        currentPage = all ? 0 : Math.floor(start / len);

                                        scope.dtable.fnClearTable();
                                        scope.dtable._fnAddTr(newRows);
                                        scope.dtable.fnDraw();
                                        scope.dtable.fnPageChange(currentPage);

                                        //                                    $elem.show();
                                    }, 1);


                                }
                            }
                            if (attrs.dtSearch !== undefined) {
                                scope.$watch("dtSearch", function(value) {
                                    if (scope.dtable !== undefined) {
                                        if (value !== undefined && value !== '') {
//                                        scope.dtable.fnFilter(value);
                                            scope.dtable.fnDraw();
                                        } else if (value === '') {
                                            scope.dtable.fnDraw(true);
                                        }
                                    }
                                });
                            }



//                            _doRenderDataTable($elem, scope.dataTableOptions);
                        });

                    }
//                    $elem.hide();
                    scope.$on(DT_LAST_ROW_KEY, function() {
                        _doRenderDataTable($elem, scope.dataTableOptions);
                    });

                }
            };
        }])
    angular.module('hkg.directives').directive('dtRows', [
        '$rootScope',
        'DT_LAST_ROW_KEY',
        function($rootScope, DT_LAST_ROW_KEY) {
            return {
                restrict: 'A',
                link: function($scope) {

                    if ($scope.$last === true) {

                        $rootScope.$broadcast(DT_LAST_ROW_KEY);
                    }
                }
            };
        }
    ]);
});
(function(angular) {
    'use strict';
    angular.module('hkg.directives').value('DT_LAST_ROW_KEY', 'datatable:lastRow');
}(angular));

