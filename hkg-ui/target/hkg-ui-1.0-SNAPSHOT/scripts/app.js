
define(['angularAMD', 'angular-route', 'angular-resource', 'angularTranslate', 'angularTranslateLoaderStaticFiles', 'bootstrap', 'angular-idle', 'loadingMaskDirective', 'multiselectDirective', 'treeview.directive', 'tabs', 'uiBootstrap', 'menu', 'select2', 'select2.directive', 'ngflow', 'jqueryDataTable', 'dataTableScroller', 'datatable.bootstrap', 'datatable.directive', 'digitonly.directive', 'angularMultiSelect', 'hkgjs', 'datetimepicker.directive', 'popover.directive', 'typeahead', 'search.directive', 'dynamicFormService', 'notificationService', 'dateFormatter', 'animate', 'touch', 'enterworkas-tab.directive', 'angular-scroll', 'dateValidate','hotkeys'], function(angularAMD) {


/** the message to be shown to the user */
var message;
        /**
         * Define the main module.
         * The module is accessible everywhere using "window.hkg", therefore global variables can be avoided totally.
         */
    var hkg = angular.module("hkg", ["ngRoute", "ngResource", "hkg.directives", 'ui.bootstrap', 'flow', 'pascalprecht.translate', 'dynamicformmodule', 'notificationmodule', 'ngAnimate', 'ngTouch', 'ngIdle', 'duScroll','cfp.hotkeys','dndLists']);
//.config method will be called prior to .run method and this are basic configuration which are to be used though out the angular application

        //angularAmd.route() is used for loading contoller using requirejs
        hkg.config(['$routeProvider', '$locationProvider', '$translateProvider', '$keepaliveProvider', '$idleProvider', function(routeProvider, locationProvider, translateProvider, $keepaliveProvider, $idleProvider) {
        //this declaration is possible in two ways <1.hkg.config(['$routeProvider', function(routeProvider){ LOGIC... }])> and <2.hkg.config(function($routeProvider){ LOGIC... })>
        translateProvider.useStaticFilesLoader({
        prefix: 'i18n/',
                suffix: '.json'
        });
                /*==== Configure idle duration period(in seconds) ======*/
                $idleProvider.idleDuration(60 * 60);
                /*==== Configure warning duration period(in seconds) ======*/
                $idleProvider.warningDuration(1);
                translateProvider.use('EN0');
                routeProvider
                .when('/login', angularAMD.route({
                templateUrl: 'views/unsecure/login.html',
                        controller: 'Login',
                        controllerUrl: 'usermanagement/login.controller'
                }))
                .when('/accessdenied', angularAMD.route({
                templateUrl: 'views/unsecure/accessdenied.html'
                }))
                .when('/systemconfig', angularAMD.route({
                templateUrl: 'views/secure/systemconfig.html',
                        controller: 'SystemConfiguration',
                        controllerUrl: 'usermanagement/system-configuration.controller'
                }))
                .when('/createlabel', angularAMD.route({
                templateUrl: 'views/secure/createlabel.html',
                        controller: 'InternationalizationAddLabel',
                        controllerUrl: 'internationalization/create-label.controller'
                }))
                .when('/updatelabel', angularAMD.route({
                templateUrl: 'views/secure/updatelabel.html',
                        controller: 'InternationalizationLabelList',
                        controllerUrl: 'internationalization/update-label.controller'
                }))
                .when('/managefeature', angularAMD.route({
                templateUrl: 'views/secure/managefeature.html',
                        controller: 'ManageFeature',
                        controllerUrl: 'usermanagement/feature.controller'
                }))
                .when('/diamondmaster', angularAMD.route({
                templateUrl: 'views/secure/managemaster.html',
                        controller: 'MasterController',
                        controllerUrl: 'usermanagement/master.controller'
                }))
                .when('/manageuserleave', angularAMD.route({
                templateUrl: 'views/secure/manageuserleave.html',
                        controller: 'ManageUserLeaveController',
                        controllerUrl: 'usermanagement/manage-user-leave.controller'
                }))
                .when('/leaverequest', angularAMD.route({
                templateUrl: 'views/secure/createleaverequest.html',
                        controller: 'LeaveRequestController',
                        controllerUrl: 'usermanagement/leave-request.controller'
                }))
                .when('/stockmaster', angularAMD.route({
                templateUrl: 'views/secure/managemaster.html',
                        controller: 'MasterController',
                        controllerUrl: 'usermanagement/master.controller'
                }))
                .when('/managegroup', angularAMD.route({
                templateUrl: 'views/secure/managegroup.html',
                        controller: 'ManageGroup',
                        controllerUrl: 'usermanagement/manage-group.controller'
                }))
                .when('/creategroup', angularAMD.route({
                templateUrl: 'views/secure/creategroup.html',
                        controller: 'ManageGroup',
                        controllerUrl: 'usermanagement/manage-group.controller'
                }))
                .when('/createemployee', angularAMD.route({
                templateUrl: 'views/secure/createemployee.html',
                        controller: 'AddEmployee',
                        controllerUrl: 'usermanagement/create-employee.controller'
                }))
                .when('/managedepartment', angularAMD.route({
                templateUrl: 'views/secure/managedepartment.html',
                        controller: 'DepartmentController',
                        controllerUrl: 'department/department.controller'
                }))
                .when('/addtemplate', angularAMD.route({
                templateUrl: 'views/admin/addTemplate.html',
                        controller: 'AddTemplate',
                        controllerUrl: 'email/add-template-controller'
                }))
                .when('/manageleaveworkflow', angularAMD.route({
                templateUrl: 'views/secure/manageleaveworkflow.html',
                        controller: 'LeaveWorkflowController',
                        controllerUrl: 'leavemanagement/leave-workflow.controller'
                }))
                .when('/addDepartment', angularAMD.route({
                templateUrl: 'views/secure/addDepartment.html',
                        controller: 'departmentController',
                        controllerUrl: 'department-management/department.controller'

                }))
                .when('/createasset', angularAMD.route({
                templateUrl: 'views/secure/createasset.html',
                        controller: 'CategoryController',
                        controllerUrl: 'assetmanagement/asset.controller'
                }))
                .when('/viewasset', angularAMD.route({
                templateUrl: 'views/secure/viewasset.html',
                        controller: 'ViewAssetController',
                        controllerUrl: 'assetmanagement/view-asset.controller'
                }))
                .when('/manageassetcategory', angularAMD.route({
                templateUrl: 'views/secure/manageassetcategory.html',
                        controller: 'ManageAssetCategory',
                        controllerUrl: 'assetmanagement/asset-category.controller'
                }))
                .when('/viewdesignation', angularAMD.route({
                templateUrl: 'views/secure/viewdesignation.html',
                        controller: 'ViewDesignation',
                        controllerUrl: 'usermanagement/view-designation.controller'
                }))
                .when('/managedesignation', angularAMD.route({
                templateUrl: 'views/secure/managedesignation.html',
                        controller: 'DesignationCtrl',
                        controllerUrl: 'designation/designation.controller'
                }))
                .when('/managepermissionset', angularAMD.route({
                templateUrl: 'views/secure/managepermissionset.html',
                        controller: 'ManagePermissionSet',
                        controllerUrl: 'usermanagement/permissionset.controller'
                }))
                .when('/shiftmanagement', angularAMD.route({
                templateUrl: 'views/secure/shiftmanagement.html',
                        controller: 'ShiftManagementController',
                        controllerUrl: 'shiftmanagement/shift-management.controller'
                }))
                .when('/managemessage', angularAMD.route({
                templateUrl: 'views/secure/managemessage.html',
                        controller: 'MessageController',
                        controllerUrl: 'usermanagement/message.controller'
                }))
                .when('/exitinterviewform', angularAMD.route({
                templateUrl: 'views/secure/configureexitinterviewform.html',
                        controller: 'ConfigureIntervewForm',
                        controllerUrl: 'systemmanagement/configure-interviewform.controller'
                }))
                .when('/fillexitinterviewform', angularAMD.route({
                templateUrl: 'views/secure/fillexitinterviewform.html',
                        controller: 'ConfigureIntervewForm',
                        controllerUrl: 'systemmanagement/configure-interviewform.controller'
                }))
                .when('/managelocation', angularAMD.route({
                templateUrl: 'views/secure/locationdetails.html',
                        controller: 'LocationController',
                        controllerUrl: 'location-management/location.controller'
                }))
                .when('/reportbuilder', angularAMD.route({
                templateUrl: 'views/secure/reportbuilder.html',
                        controller: 'ReportBuilder',
                        controllerUrl: 'reportbuilder/reportbuilder.controller'
                }))
                .when('/viewemployee', angularAMD.route({
                templateUrl: 'views/secure/viewemployee.html',
                        controller: 'ViewEmployeeCtrl',
                        controllerUrl: 'usermanagement/view-employee.controller'
                }))
                .when('/updateemployee', angularAMD.route({
                templateUrl: 'views/secure/updateemployee.html',
                        controller: 'UpdateEmployeeCtrl',
                        controllerUrl: 'usermanagement/update-employee.controller'
                }))
                .when('/managereport', angularAMD.route({
                templateUrl: 'views/secure/managereport.html',
                        controller: 'ManageReport',
                        controllerUrl: 'reportbuilder/report.controller'
                }))
                .when('/viewreports', angularAMD.route({
                templateUrl: 'views/secure/viewreports.html',
                        controller: 'PreviewReport',
                        controllerUrl: 'reportbuilder/reportpreview.controller'
                }))
                .when('/configureformula', angularAMD.route({
                templateUrl: 'views/secure/formulaConfiguration.html',
                        controller: 'ConfigureFormula',
                        controllerUrl: 'formula/configure-formula.controller'
                }))
                .when('/manageholiday', angularAMD.route({
                templateUrl: 'views/secure/manageholiday.html',
                        controller: 'ManageHoliday',
                        controllerUrl: 'holiday/holiday.controller'
                }))
//                    .when('/addholiday', angularAMD.route({
//                        templateUrl: 'views/secure/addholiday.html',
//                        controller: 'AddHoliday',
//                        controllerUrl: 'holiday/add-holiday.controller'
//
//                    }))

                .when('/managecustomfield', angularAMD.route({
                templateUrl: 'views/secure/managecustomfield.html',
                        controller: 'CustomField',
                        controllerAs: 'Custom',
                        controllerUrl: 'customfield/customfield.controller'

                }))
                .when('/newmanagecustomfield', angularAMD.route({
                templateUrl: 'views/secure/newmanagecustomfield.html',
                        controller: 'CustomField',
                        controllerUrl: 'customfield/newcustomfield'

                }))
                .when('/managetasks', angularAMD.route({
                templateUrl: 'views/secure/managetask.html',
                        controller: 'ManageTasks',
                        controllerUrl: 'taskmanagement/task.controller'
                }))
                .when('/applyleave', angularAMD.route({
                templateUrl: 'views/secure/applyleave.html',
                        controller: 'ApplyLeave',
                        controllerUrl: 'leavemanagement/apply-leave.controller'
                }))
                .when('/manageleave', angularAMD.route({
                templateUrl: 'views/secure/manageleave.html',
                        controller: 'ManageLeave',
                        controllerUrl: 'leavemanagement/leave.controller'
                }))
                .when('/manageasset', angularAMD.route({
                templateUrl: 'views/secure/manageasset.html',
                        controller: 'CategoryController',
                        controllerUrl: 'assetmanagement/asset.controller'
                }))
                .when('/managefranchise', angularAMD.route({
                templateUrl: 'views/secure/managefranchise.html',
                        controller: 'FranchiseController',
                        controllerUrl: 'franchise/franchise.controller'
                }))
                .when('/managemaster', angularAMD.route({
                templateUrl: 'views/secure/managemaster.html',
                        controller: 'ManageMaster',
                        controllerUrl: 'master/master.controller'
                }))
                .when('/example', angularAMD.route({
                templateUrl: 'views/secure/example.html',
                        controller: 'Login',
                        controllerUrl: 'usermanagement/login.controller'
                }))
                .when('/managedynamicform', angularAMD.route({
                templateUrl: 'views/secure/managedynamicform.html',
                        controller: 'ManageDynamicForm',
                        controllerUrl: 'dynamicform/dynamic-form.controller'
                }))
                .when('/manageevents', angularAMD.route({
                templateUrl: 'views/secure/manageevent.html',
                        controller: 'ManageEvents',
                        controllerUrl: 'eventmanagement/event.controller'
                }))
                .when('/manageemployee', angularAMD.route({
                templateUrl: 'views/secure/manageemployee.html',
                        controller: 'EmployeeController',
                        controllerUrl: 'usermanagement/employee.controller'
                }))
                .when('/managelocales', angularAMD.route({
                templateUrl: 'views/secure/managelocales.html',
                        controller: 'LocalesContoller',
                        controllerUrl: 'locales/locale.controller'
                }))
                .when('/manageshift', angularAMD.route({
                templateUrl: 'views/secure/manageshift.html',
                        controller: 'ShiftController',
                        controllerUrl: 'shift/shift.controller'
                }))
                .when('/manageshift', angularAMD.route({
                templateUrl: 'views/secure/manageshift.html',
                        controller: 'ShiftController',
                        controllerUrl: 'shift/shift.controller'
                }))
                .when('/configurefranchise', angularAMD.route({
                templateUrl: 'views/secure/configurefranchise.html',
                        controller: 'ConfigureFranchise',
                        controllerUrl: 'configurefranchise/configure-franchise.controller'
                }))
                .when('/shownotification', angularAMD.route({
                templateUrl: 'views/secure/shownotification.html',
                        controller: 'showNotification',
                        controllerUrl: 'notifications/show-notification.controller'

                }))
                .when('/registerevent', angularAMD.route({
                templateUrl: 'views/secure/registerevent.html',
                        controller: 'ManageEvents',
                        controllerUrl: 'eventmanagement/manage-events.controller'

                }))
                .when('/editprofile', angularAMD.route({
                templateUrl: 'views/secure/editprofile.html',
                        controller: 'EditProfile',
                        controllerUrl: 'usermanagement/edit-profile.controller'

                }))
                .when('/dashboard', angularAMD.route({
                templateUrl: 'views/secure/dashboard.html',
                        controller: 'DashboardController',
                        controllerUrl: 'usermanagement/dashboard.controller'

                }))
                .when('/logout', angularAMD.route({
                templateUrl: 'views/unsecure/logout.html',
                        controller: 'LogoutController',
                        controllerUrl: 'usermanagement/logout.controller'

                }))

                .when('/manageactivityflow', angularAMD.route({
                templateUrl: 'views/secure/manageactivityflow.html',
                        controller: 'ActivityFlowController',
                        controllerUrl: 'activityflow/activity-flow.controller'

                }))
                .when('/managecurrency', angularAMD.route({
                templateUrl: 'views/secure/managecurrency.html',
                        controller: 'ManageCurrencyMaster',
                        controllerUrl: 'master/currency-master.controller'
                }))
                .when('/managereferencerate', angularAMD.route({
                templateUrl: 'views/secure/managereferencerate.html',
                        controller: 'ManageCurrencyMaster',
                        controllerUrl: 'master/currency-master.controller'
                }))
                .when('/flowchart', angularAMD.route({
                templateUrl: 'views/unsecure/flowchart.html',
                        controller: 'FlowChartController12',
                        controllerUrl: 'flowchart/flowchart-controller'
                }))
                .when('/managerule', angularAMD.route({
                templateUrl: 'views/secure/managerule.html',
                        controller: 'RuleController',
                        controllerUrl: 'usermanagement/rule.controller'
                }))
                .when('/printsvg', angularAMD.route({
                templateUrl: 'views/secure/printsvg.html',
                        controller: 'ActivityFlowController',
                        controllerUrl: 'activityflow/activity-flow.controller'

                }))
                .when('/trackstockprint', angularAMD.route({
                templateUrl: 'views/secure/trackstockprint.html',
                        controller: 'TrackStockController',
                        controllerUrl: 'trackstock/track-stock.controller'

                }))
                .when('/addinvoice', angularAMD.route({
                templateUrl: 'views/secure/addinvoice.html',
                        controller: 'AddInvoiceController',
                        controllerUrl: 'invoicemanagement/add-invoice.controller'

                }))
                .when('/roughinvoice', angularAMD.route({
                templateUrl: 'views/secure/invoice.html',
                        controller: 'InvoiceController',
                        controllerUrl: 'invoicemanagement/invoice.controller'

                }))
                .when('/editinvoice', angularAMD.route({
                templateUrl: 'views/secure/editinvoice.html',
                        controller: 'EditInvoiceController',
                        controllerUrl: 'invoicemanagement/edit-invoice.controller'

                }))
                .when('/addlot', angularAMD.route({
                templateUrl: 'views/secure/addlot.html',
                        controller: 'AddLotController as AddLot',
                        controllerAs: 'AddLot',
                        controllerUrl: 'lotmanagement/add-lot.controller'

                }))
                .when('/editlot', angularAMD.route({
                templateUrl: 'views/secure/editlot.html',
                        controller: 'EditLotController',
                        controllerUrl: 'lotmanagement/edit-lot.controller'

                }))
                .when('/mergelot', angularAMD.route({
                templateUrl: 'views/secure/mergelot.html',
                        controller: 'MergeLotController',
                        controllerUrl: 'lotmanagement/merge-lot.controller'

                }))
                .when('/stocksell', angularAMD.route({
                templateUrl: 'views/secure/sellstock.html',
                        controller: 'SellStockController',
                        controllerUrl: 'stockmanagement/sell-stock.controller'

                }))
                .when('/roughsale', angularAMD.route({
                templateUrl: 'views/secure/roughsale.html',
                        controller: 'RoughSaleController',
                        controllerAs: 'roughSale',
                        controllerUrl: 'stockmanagement/roughsale.controller'

                }))
                .when('/splitlot', angularAMD.route({
                templateUrl: 'views/secure/splitlot.html',
                        controller: 'SplitLotController',
                        controllerUrl: 'lotmanagement/split-lot.controller'

                }))
                .when('/stocktransfer', angularAMD.route({
                templateUrl: 'views/secure/transferstock.html',
                        controller: 'TransferStockController',
                        controllerUrl: 'stockmanagement/transfer-stock.controller'

                }))
                .when('/addpacket', angularAMD.route({
                templateUrl: 'views/secure/addpacket.html',
                        controller: 'AddPacketController',
                        controllerAs: 'AddPacketCtrl',
                        controllerUrl: 'packetmanagement/add-packet.controller'

                }))
                .when('/editpacket', angularAMD.route({
                templateUrl: 'views/secure/editpacket.html',
                        controller: 'EditPacketController',
                        controllerUrl: 'packetmanagement/edit-packet.controller'

                }))
                .when('/mergepacket', angularAMD.route({
                templateUrl: 'views/secure/mergepacket.html',
                        controller: 'MergePacketController',
                        controllerUrl: 'packetmanagement/merge-packet.controller'

                }))
                .when('/splitpacket', angularAMD.route({
                templateUrl: 'views/secure/splitpacket.html',
                        controller: 'SplitPacketController',
                        controllerAs: 'Packet',
                        controllerUrl: 'packetmanagement/split-packet.controller'

                }))
                .when('/addparcel', angularAMD.route({
                templateUrl: 'views/secure/addparcel.html',
                        controller: 'AddParcelController as AddParcel',
                        controllerAs: 'AddParcel',
                        controllerUrl: 'parcelmanagement/add-parcel.controller'

                }))
                .when('/editparcel', angularAMD.route({
                templateUrl: 'views/secure/editparcel.html',
                        controller: 'EditParcelController',
                        controllerUrl: 'parcelmanagement/edit-parcel.controller'

                }))
                .when('/managesubentity', angularAMD.route({
                templateUrl: 'views/secure/managesubentity.html',
                        controller: 'SubEntityController',
                        controllerUrl: 'customfield/managesubentity.controller'

                }))
                .when('/theme', angularAMD.route({
                templateUrl: 'views/secure/theme.html',
                        controller: 'ThemeController',
                        controllerUrl: 'usermanagement/theme.controller'

                }))
                .when('/managegoal', angularAMD.route({
                templateUrl: 'views/secure/managegoal.html',
                        controller: 'GoalController',
                        controllerUrl: 'goalmanagement/goal.controller'
                }))
                .when('/managegoaltemplate', angularAMD.route({
                templateUrl: 'views/secure/managegoaltemplate.html',
                        controller: 'GoalTemplateController',
                        controllerUrl: 'goalmanagement/goaltemplate.controller'
                }))
                .when('/managenotification', angularAMD.route({
                templateUrl: 'views/secure/shownotification.html',
                        controller: 'showNotification',
                        controllerUrl: 'notifications/show-notification.controller'
                }))
                .when('/managenotificationconfig', angularAMD.route({
                templateUrl: 'views/secure/managenotification.html',
                        controller: 'NotificationController',
                        controllerUrl: 'notifications/managenotification.controller'
                }))
                .when('/manageCaratRange', angularAMD.route({
                templateUrl: 'views/secure/managecaratrange.html',
                        controller: 'CaratRangeController',
                        controllerUrl: 'caratrange/caratrange.controller'
                }))
                .when('/managePriceList', angularAMD.route({
                templateUrl: 'views/secure/managepricelist.html',
                        controller: 'PriceListController',
                        controllerUrl: 'pricelist/pricelist.controller'
                }))
                .when('/setup', angularAMD.route({
                templateUrl: 'views/unsecure/setup.html',
                        controller: 'SetUpController',
                        controllerUrl: 'sync-demo/setup.controller'

                }))
                .when('/printstatic', angularAMD.route({
                templateUrl: 'views/secure/printstatic.html',
                        controller: 'PrintStaticController',
                        controllerUrl: 'printstatic/print-static.controller'
                }))
                .when('/mergestock', angularAMD.route({
                templateUrl: 'views/secure/mergestock.html',
                        controller: 'MergeStockController',
                        controllerUrl: 'stockmanagement/mergestock.controller'
                }))
                .when('/issuereceive', angularAMD.route({
                templateUrl: 'views/secure/issuereceivestock.html',
                        controller: 'IssueReceiveController',
                        controllerAs: 'IssueController',
                        controllerUrl: 'issuereceivestock/issuereceive-stock.controller'
                }))
                .when('/statuschange', angularAMD.route({
                templateUrl: 'views/secure/changestatus.html',
                        controller: 'ChangeStatusController',
                        controllerUrl: 'changestatus/change-status.controller'
                }))
                .when('/trackstock', angularAMD.route({
                templateUrl: 'views/secure/trackstock.html',
                        controller: 'TrackStockController',
                        controllerUrl: 'trackstock/track-stock.controller'
                }))
                .when('/totalstock', angularAMD.route({
                templateUrl: 'views/secure/totalstock.html',
                        controller: 'TrackStockController',
                        controllerUrl: 'trackstock/track-stock.controller'
                }))
                .when('/printdynamic', angularAMD.route({
                templateUrl: 'views/secure/printdynamic.html',
                        controller: 'PrintDynamicController',
                        controllerUrl: 'printdynamic/print-dynamic.controller'
                }))
                .when('/managetransferemployee', angularAMD.route({
                templateUrl: 'views/secure/managetransferemployee.html',
                        controller: 'TransferEmployeeController',
                        controllerUrl: 'usermanagement/transfer-employee.controller'
                }))
                .when('/stockreroute', angularAMD.route({
                templateUrl: 'views/secure/reroutstock.html',
                        controller: 'ReRoutStockController',
                        controllerUrl: 'rerout-stock/rerout-stock.controller'
                }))
                .when('/splitstock', angularAMD.route({
                templateUrl: 'views/secure/splitstock.html',
                        controller: 'SplitStockController',
                        controllerAs: 'SplitStock',
                        controllerUrl: 'stockmanagement/splitstock.controller'
                }))
                .when('/allotment', angularAMD.route({
                templateUrl: 'views/secure/allotment.html',
                        controller: 'AllotmentController',
                        controllerUrl: 'allotment/allotment.controller'
                }))
                .when('/endoftheday', angularAMD.route({
                templateUrl: 'views/secure/endoftheday.html',
                        controller: 'EndOfTheDayController',
                        controllerUrl: 'endoftheday/endoftheday.controller'
                }))
                .when('/startoftheday', angularAMD.route({
                templateUrl: 'views/secure/startoftheday.html',
                        controller: 'StartOfTheDayController',
                        controllerUrl: 'startoftheday/startoftheday.controller'
                }))
                .when('/writeservice', angularAMD.route({
                templateUrl: 'views/secure/writeservice.html',
                        controller: 'WriteServiceController',
                        controllerUrl: 'writeservice/writeservice.controller'
                }))
                .when('/managegoalsheet', angularAMD.route({
                templateUrl: 'views/secure/managegoalsheet.html',
                        controller: 'ManageGoalSheetController',
                        controllerUrl: 'goalsheet/goalsheet.controller'
                }))
                .when('/transactionlog', angularAMD.route({
                templateUrl: 'views/unsecure/transactionlog.html',
                        controller: 'TransactionLogController',
                        controllerUrl: 'transactionlog/transactionlog.controller'
                }))
                .when('/editvalue', angularAMD.route({
                templateUrl: 'views/secure/editvalue.html',
                        controller: 'EditValueController',
                        controllerUrl: 'stockmanagement/edit-value.controller'
                }))
                .when('/generateslip', angularAMD.route({
                templateUrl: 'views/secure/generateslip.html',
                        controller: 'GenerateSlipController',
                        controllerUrl: 'generateslip/generateslip.controller'
                }))
                .when('/finalize', angularAMD.route({
                templateUrl: 'views/secure/finalize.html',
                        controller: 'FinalizeServiceController',
                        controllerUrl: 'finalizeservice/finalizeservice.controller'
                }))
                .when('/printbarcode', angularAMD.route({
                templateUrl: 'views/secure/printbarcode.html',
                        controller: 'PrintBarcodeController',
                        controllerUrl: 'printbarcode/print-barcode.controller'
                }))
                .when('/roughmerge', angularAMD.route({
                templateUrl: 'views/secure/mergeparcel.html',
                        controller: 'MergeParcelController',
                        controllerUrl: 'stockmanagement/mergeparcel.controller'
                }))
                .when('/configureDepartment', angularAMD.route({
                templateUrl: 'views/secure/configureDepartment.html',
                        controller: 'DeptConfigController',
                        controllerUrl: 'departmentconfig/department.config.controller'
                }))
                .when('/configureDesignation', angularAMD.route({
                templateUrl: 'views/secure/configureDesignation.html',
                        controller: 'DesignConfigController',
                        controllerUrl: 'designationconfig/designation.config.controller'
                }))
                .when('/printflow', angularAMD.route({
                templateUrl: 'views/secure/printflow.html',
                        controller: 'DeptConfigController',
                        controllerUrl: 'departmentconfig/department.config.controller'

                }))
                .when('/configureRule', angularAMD.route({
                templateUrl: 'views/secure/configureRule.html',
                        controller: 'RuleConfigController',
                        controllerUrl: 'ruleconfig/ruleconfig.controller'
                }))
                .when('/estimateprediction', angularAMD.route({
                templateUrl: 'views/secure/estimateprediction.html',
                        controller: 'EstPredctController',
                        controllerUrl: 'estpredict/estimateprediction.controller'
                }))
                .when('/roughcalcy', angularAMD.route({
                templateUrl: 'views/secure/roughcalcy.html',
                        controller: 'RoughCalcyController',
                        controllerUrl: 'roughcalcy/roughcalcy.controller'
                }))
                .when('/sublot', angularAMD.route({
                templateUrl: 'views/secure/sublot.html',
                        controller: 'SublotController',
                        controllerUrl: 'sublot/sublot.controller'
                }))
                .when('/roughpurchase', angularAMD.route({
                templateUrl: 'views/secure/roughpurchase.html',
                        controller: 'RoughPurchaseController',
                        controllerUrl: 'roughpurchase/roughpurchase.controller'
                }))
                .when('/parceltemplate', angularAMD.route({
                templateUrl: 'views/secure/parceltemplate.html',
                        controller: 'ParcelTemplateController',
                        controllerUrl: 'parcelmanagement/parceltemplate.controller'
                }))
                .when('/roughmakeable', angularAMD.route({
                templateUrl: 'views/secure/roughmakeable.html',
                        controller: 'RoughMakeableController',
                        controllerUrl: 'roughmakeable/roughmakeable.controller'
                }))
                .when('/finalmakeable', angularAMD.route({
                templateUrl: 'views/secure/finalmakeable.html',
                        controller: 'FinalMakeableController',
                        controllerUrl: 'finalmakeable/finalmakeable.controller'
                }));
        }]);
        //Regitster MainController
        //    hkg = MainController(hkg);

        /**
         * @author Akta Kalariya
         * @description seperating variable initialization before injection of dynamicform service
         */

        hkg.run(['$rootScope', function(rootScope) {
        rootScope.masterHkgPath = corsConfig.masterHkgPath;
                rootScope.isMaster = corsConfig.isMaster;
                rootScope.apipath = rootScope.masterHkgPath + "api/";
                rootScope.centerapipath = 'api/';
        }]);
        //when angular is loaded and configured completely it will call .run method
        //we have intialized some variables, loaded some variable to rootScope checked authentication here

        hkg.run(['$rootScope', '$http', '$location', 'DynamicFormService', '$idle', '$q', '$route', function(rootScope, http, location, DynamicFormService, $idle, $q, $route) {
        // for security entription we have Added this
        // for security entription we have Added this
        $idle.watch();
                rootScope.bootstrapcsspath = "css/default/bootstrap.min.css";
                rootScope.hkgcsspath = "css/default/hkg.css";
                rootScope.notificationimagepath = "css/default/notification.png";
                rootScope.composeimagepath = "css/default/compose.png";
                rootScope.chatimagepath = "css/default/chat.png";
                rootScope.printableChars = '~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:"ZXCVBNM<>?`1234567890-=qwertyuiop[]\\asdfghjkl;\'zxcvbnm,./';
                initializeAngularScope(rootScope, http, $q, $route);
                //make current message accessible to root scope and therefore all scopes
                //make current message accessible to root scope and therefore all scopes
                rootScope.setMessage = function(text, type, show) {
                rootScope.message = {
                text: text,
                        type: type,
                        show: show,
                        showLoginLink: false
                };
                };
                // sync helper code start
                rootScope.setMessage = function(text, type, show, showLoginLink, linkText) {
                setMessage(text, type, show, showLoginLink, linkText);
                };
                // sync helper code end
                rootScope.canAccess = function(featureCode) {
                rootScope.hasAccess = false;
                        rootScope.canAccessMenu(featureCode, rootScope.menu);
                        return rootScope.hasAccess;
                };
                rootScope.canAccessMenu = function(featureCode, menu) {
                return rootScope.containsFeature(featureCode, menu);
                };
                rootScope.getProfilePicture = function() {
                return getProfilePicture();
                };
                rootScope.containsFeature = function(featureCode, menu) {
                if (menu) {
                angular.forEach(menu, function(item) {
                if (!rootScope.hasAccess) {
                if (item.featureName === featureCode) {
                return rootScope.hasAccess = true;
                } else {
                if (item.children)
                {
                return rootScope.canAccessMenu(featureCode, item.children);
                }
                }
                }

                });
                }
                return rootScope.hasAccess;
                };
                /**
                 * Holds all the requests which failed due to 401.
                 */
                rootScope.requests401 = [];
                /**
                 * Holds all the requests which failed due to 403.
                 */
                rootScope.requests403 = [];
                /*======================= Registering different login event listners ==============================*/
                rootScope.isMasterDown = false;
                rootScope.$on('event:loginRequired', function() {
                console.log("run : loginrequired event : loginTry : " + rootScope.loginTry);
                        if (rootScope.isLoggedIn) {
                location.path('/accessdenied');
                } else {
                location.path('/login');
                }
                rootScope.showLogin();
                });
                /*========== After timeout period logout from system============== */
                rootScope.$on('$idleTimeout', function() {
                rootScope.$broadcast('event:logoutRequest');
                });
                /**
                 * On 'event:loginConfirmed', resend all the 401 requests.
                 */
                //rootScope.menuurl = "api/common/getmenu";
                rootScope.$on('event:loginConfirmed', function(event, confirmUrl) {                    
//                if (!rootScope.isMaster){
                var dataToSend = {
                featureName: "login",
                        entityId: null,
                        entityType: 'login',
                        currentFieldValueMap: {username: rootScope.session.userCode},
                        dbType: null,
                        otherEntitysIdMap: null
                };
                        var url = rootScope.centerapipath + "executerule" + "/postrule";
                        http.post(url, dataToSend).success(function(data) { //pingurl variable should be set before pingServer first time called in $rootScope                
                if (!!data.validationMessage){
                rootScope.$broadcast('event:logoutRequest');
                }
                }, function() {

                });
//                }
                        rootScope.isLoggedIn = true;
                        rootScope.loginTry = - 1;
//                if (rootScope.requests401.length !== 0) {
//                    requests = rootScope.requests401;
//                } else if (rootScope.requests403.length !== 0) {
//                    requests = rootScope.requests403;
//                }
//
//                for (i = 0; i < requests.length; i++) {
////                    console.log("Retry...." + requests[i]);
//                    retry(requests[i]);
//                }
//                rootScope.requests401 = [];
//                rootScope.requests403 = [];
//                function retry(req) {
//                    http(req.config).then(function(response) {
//                        req.deferred.resolve(response);
//                    });
//                }


                        //rootScope.setMenuItemCss(rootScope.menu, 0);
                        //Added by Mayank 05-Sep-2014 - Main Menu activation based on feature rights
                        if (rootScope.menu) {
                angular.forEach(rootScope.menu, function(item) {
                if (item.menuCategory === rootScope.menuManage) {
                rootScope.menuManageAccess = true;
                } else if (item.menuCategory === rootScope.menuStock && !rootScope.isMaster) {
                rootScope.menuStockAccess = true;
                } else if (item.menuCategory === rootScope.menuReport) {
                rootScope.menuReportAccess = true;
                }
                });
                }


                if (location.path() === '/login') {
                location.path('/dashboard');
                }
                //To retrieve custom field data from server
                console.log("company id..." + rootScope.session.companyId);
                        rootScope.$broadcast('event:pollingStart');
                        //Code that is related to login confirmed event of master only will be written here
                        if (confirmUrl.indexOf(rootScope.apipath) >= 0) {
                rootScope.retrieveMinAge();
                        DynamicFormService.storeAllCustomFieldData(rootScope.session.companyId);
                }
                })
                /**
                 * On 'event:loginRequest' send credentials to the server.
                 */
//            rootScope.$on('event:loginRequest', function(event, username, password, rememberme) {
//                if (rootScope.loginTry < 1) {
//                    rootScope.loginTry = 1;
//                }
//                console.log("run : login request event");
//                var payload = $.param({
//                    j_username: username,
//                    j_password: password,
//                    _spring_security_remember_me: rememberme
//                });
//                var config = {
//                    headers: {
//                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
//                    }
//                };
//                http.post('j_spring_security_check', payload, config).success(function(data, textStatus, jqXHR, dataType) {
//                    //                console.log("run : login request event : post sucess : data : " + data);
//                    console.log("run : login request event : post sucess : textStatus : " + textStatus);
//                    rootScope.pingServer();
//                });
//            });

                /**
                 * On 'logoutRequest' invoke logout on the server and broadcast 'event:loginRequired'.
                 */
                rootScope.$on('event:logoutRequest', function() {
                rootScope.isLoggedIn = false;
                        rootScope.$broadcast('event:pollingStop');
                        rootScope.beforeLogoutUrl = rootScope.centerapipath + "common/adduseroperationbeforelogout";
                        http.get(rootScope.beforeLogoutUrl).success(function() {
                console.log("run : logout request event");
                        location.path("/logout");
                });
                });
//            rootScope.workAllocationCount = function() {
//                rootScope.counUrl = rootScope.centerapipath + "allotment/retrieveworkallocationcount";
//                http.get(rootScope.counUrl).success(function(res) {
////                    console.log("data result :"+JSON.stringify(res.data));
//                    rootScope.allocationMap = res.data;
//                });
//            }
                /*======================= Login event registration over ===========================================*/


                /**
                 * Ping server to figure out if user is already logged in.
                 */
                // now Remove Ping to server and add get menu from server
//            rootScope.pingurl = "api/common/ping";            

                rootScope.pingurl = rootScope.centerapipath + "common/getsession";
                rootScope.pingServer = function() {
                var deferred = $q.defer();
                        rootScope.ping = false;
                        rootScope.clientTimezoneOffset = new Date().getTimezoneOffset();
                        http.post(rootScope.pingurl, rootScope.clientTimezoneOffset).success(function(data) { //pingurl variable should be set before pingServer first time called in $rootScope
                rootScope.session = data;
                        rootScope.session.currentDesignation = rootScope.session.currentDesignation.toString();
                        rootScope.menu = undefined;
                        if (data !== undefined && data.features !== undefined) {
                rootScope.menu = data.features;
                }
                rootScope.serverDate = new Date(rootScope.session.serverDate);
                        rootScope.checkUserTimezone(true);
                        rootScope.switchLanguage(rootScope.session.prefferedLang, rootScope.session.companyId);
                        rootScope.viewEncryptedData = rootScope.canAccess("view_encrypted_data");
                        rootScope.getCurrentServerDate();
                        rootScope.$broadcast('event:loginConfirmed', rootScope.pingurl);
                        var theme = rootScope.session.theme;
                        if (theme != undefined && theme !== null && theme.length > 0) {
                rootScope.setThemeFolder(theme);
                }
                if (rootScope.session.hasBg) {
                rootScope.retrieveWallpaper();
                } else {
                rootScope.setDefaultWallpaper();
                }

                if (!!rootScope.session.reportsToUsers) {
                console.log(rootScope.session.reportsToUsers);
                        var v = rootScope.session.id;
                        var data = {};
                        rootScope.session.reportsToUsers[v] = rootScope.session.firstName + " " + rootScope.session.lastName;
                        if (!!!localStorage.getItem("proxyLogin")) {
                localStorage.setItem("proxyLogin", rootScope.session.id);
                        rootScope.userProxy = localStorage.getItem("proxyLogin");
                } else {
                rootScope.userProxy = localStorage.getItem("proxyLogin");
                }
                }
                deferred.resolve();
                }, function() {
                deferred.reject();
                });
                        return deferred.promise;
                };
                rootScope.masterPingUrl = rootScope.apipath + "common/getsession";
                rootScope.pingMasterServer = function() {
                return pingMaster();
                };
                rootScope.closeLoginModal = function() {
                closeLoginModal();
                };
                rootScope.loginToMaster = function(data, closeModalRequired) {
                return loginToMaster(data, closeModalRequired);
                }
        rootScope.checkUserTimezone = function(pinged) {
        rootScope.invalidTimezone = false;
                var serverDateGmt = new Date(rootScope.serverDate).toGMTString();
                var clientDateGmt = new Date().toGMTString();
                var diff = new Date(serverDateGmt) - new Date(clientDateGmt);
//                console.log("diff :::"+diff)
                if (diff >= 600000 || diff <= ( - 600000)) {
        rootScope.invalidTimezone = true;
                rootScope.setMessage("Server and system timezone are not in sync", rootScope.failure, true);
                rootScope.logout();
        } else {
        rootScope.invalidTimezone = false;
        }


        };
                rootScope.fetchVersion = function() {
                http.get(rootScope.apipath + "common/getbuildversion").success(function(data) {
                rootScope.projectBuildVersion = data;
                });
                };
                rootScope.getServerDate = function() {
                http.get(rootScope.apipath + "common/getserverdate").success(function(data) {
                rootScope.serverDate = new Date(parseInt(data));
                        rootScope.checkUserTimezone(false);
                });
                };
                rootScope.setThemeFolder = function(folder) {
                rootScope.bootstrapcsspath = "css/" + folder + "/bootstrap.min.css";
                        rootScope.hkgcsspath = "css/" + folder + "/hkg.css";
                        rootScope.notificationimagepath = "css/" + folder + "/notification.png";
                        rootScope.composeimagepath = "css/" + folder + "/compose.png";
                        rootScope.chatimagepath = "css/" + folder + "/chat.png";
                        rootScope.session.theme = folder;
                };
                rootScope.retrieveWallpaper = function() {
                var url = rootScope.apipath + "employee/getwallpaper";
                        http.get(url).success(function(data) {
                var map = {src: data};
                        rootScope.defaultWallpaperName = map.src;
                        rootScope.defaultPath = rootScope.apipath + 'employee/getimage?file_name=' + map.src + "&token=" + rootScope.authToken;
                });
                };
                rootScope.setDefaultWallpaper = function() {
                var a = jQuery('#content.right-content-bg').css('background');
                        var b = a.substr(0, a.indexOf('no-repeat'));
                        rootScope.defaultPath = b;
                };
                /**
                 * Added by raj: Method to append authentication token to url
                 * @param {type} uri
                 * @returns {String}
                 */
                rootScope.appendAuthToken = function(uri) {
                return appendAuthToken(uri);
                };
                /**
                 * @author Akta Kalariya
                 * @description This method provides config headers for request we are doing using direct $http instead of 
                 * $resource , Useful when want to send master/center authtoken in header instead of query parameter
                 */
                rootScope.getConfigHeaderForHttpRequest = function(uri) {
                return getConfigHeaderForHttpRequest(uri);
                };
                /**
                 *   If user data is stored in localStorage than retrieve token and set
                 *   in rootScope variable
                 **/
                var authToken = localStorage.getItem('user');
                if (authToken !== undefined && authToken !== null) {
        rootScope.authToken = authToken;
        }
        var masterAuthToken = localStorage.getItem('masterAuthToken');
                if (masterAuthToken !== undefined && masterAuthToken !== null) {
        rootScope.masterAuthToken = masterAuthToken;
        }
        }
        ]);
        hkg.config(['$httpProvider', '$provide', function(httpProvider, $provide) {
        /**
         * Request interceptor to send authorization token with every request
         */
        addCustomRequestInterceptor(httpProvider); /**
         * Request interceptor ends...
         */


                var interceptor = ['$rootScope', '$q', function(rootScope, q) {

                function success(response) {
//                        console.log(JSON.stringify(response))
//                        //console.log("interceptor : sucess : " + response.config.url);
                var st = '' + response.config.url;
                        //Extract message and status if request was made to api.
                        if (st.indexOf("deployserver") >= 0) {
                console.log(JSON.stringify(response.data.messages));
                }
                if ((st.indexOf(rootScope.apipath) >= 0 || st.indexOf(rootScope.centerapipath) >= 0) && response.data.porcessResponse == true) {
//                            console.log("hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+JSON.stringify(response.data.data));
                var extraMessages = response.data.messages;
                        if (extraMessages && extraMessages.length > 0) {
                for (var i = 0; i < extraMessages.length; i++) {
                var extraMessage = extraMessages[i];
                        if (extraMessage) {
                rootScope.addMessage(extraMessage.message, extraMessage.responseCode, null, null, extraMessage.translateValueMap);
                }
                }
                }
                var dataExtracted = response.data.data;
                        response.data = dataExtracted;
                }
                return response;
                }

                function error(response) {
                /* response.config – {object} – Object describing the request to be made and how it should be processed.
                 * The object has following properties:
                 *
                 * method – {string} – HTTP method (e.g. 'GET', 'POST', etc)
                 * url – {string} – Absolute or relative URL of the resource that is being requested.
                 * params – {Object.<string|Object>} – Map of strings or objects which will be 
                 *          turned to ?key1=value1&key2=value2 after the url. If the value is not a string, it will be JSONified.
                 * data – {string|Object} – Data to be sent as the request message data.
                 * headers – {Object} – Map of strings representing HTTP headers to send to the server.
                 * transformRequest – {function(data, headersGetter)|Array.
                 *                    <function(data, headersGetter)>} – transform function or an array of such functions.
                 *                    The transform function takes the http request body and headers and returns its transformed 
                 *                    (typically serialized) version.
                 * transformResponse – {function(data, headersGetter)|Array.
                 *                     <function(data, headersGetter)>} – transform function or an array of such functions. 
                 *                     The transform function takes the http response body and headers and returns its transformed 
                 *                     (typically deserialized) version.
                 * cache – {boolean|Cache} – If true, a default $http cache will be used to cache the GET request,
                 *         otherwise if a cache instance built with $cacheFactory, this cache will be used for caching.
                 * timeout – {number} – timeout in milliseconds.
                 * withCredentials - {boolean} - whether to to set the withCredentials flag on the XHR object.
                 *                   See requests with credentials for more information.
                 */

//                        console.log("interceptor : error : url : " + response.config.url);
                var status = response.status;
                        var deferred = q.defer();
                        var req = {
                        config: response.config,
                                deferred: deferred
                        };
                        if (status === 401) {
                error401(response, deferred, req);
                        q.reject(response);
                } else if (status === 403) {
                error403(response);
//                            console.log("403");
                        rootScope.requests403.push(req);
                        rootScope.$broadcast('event:loginRequired');
                        if (response.data !== "") {
//                                console.log('interceptor : error : ' + response.data);
                rootScope.addMessage(response.data, 3);
                        rootScope.setMessage(response.data, rootScope.failure, true);
                }
                return deferred.promise;
                } else if (status === 404) {
                error404(response, q);
                        q.reject(response);
                } else if (status >= 500) {
                error500(response, q);
                        q.reject(response);
                }

                // otherwise
                return q.reject(response);
                }

                return function(promise) {
                return promise.then(success, error);
                };
                }];
                httpProvider.responseInterceptors.push(interceptor);
        }]);
        hkg.factory('Notification', ['$resource', '$rootScope', function(resource, rootScope) {
        var Notify = resource(rootScope.centerapipath + 'notification/:action', {
        action: '@actionName'}, {
        retrieveNotificationCount: {
        method: 'GET',
                isArray: false,
                params: {
                action: 'retrievecount'
                }
        },
                retrieveNotificationsPopUp: {
                method: 'GET',
                        isArray: true,
                        params: {
                        action: 'retrievenotificationpopup'
                        }
                }


        });
                return Notify;
        }]);
        hkg.factory('CustomField', ['$resource', '$q', '$rootScope', function(resource, $q, rootScope) {
        var customfieldManagment = resource(rootScope.apipath + 'customfield/:action', {
        action: '@actionName'},
        {
        retrieveSectionAndCustomFieldInfoTemplateByFeatureId: {
        method: 'POST',
                isArray: false,
                params: {
                action: "retrievesectionandcustomfieldtemplate"
                },
                url: rootScope.centerapipath

        },
                retrieveCustomFieldByFeatureName: {
                method: 'POST',
                        isArray: false,
                        params: {
                        action: "retrievesectionandcustomfieldtemplate"
                        },
                        url: rootScope.centerapipath + "customfield/:action"
                },
                retriveAccesibleCustomFieldForFeature: {
                method: 'POST',
                        isArray: false,
                        params: {
                        action: "retriveAccesibleCustomFieldForFeature"
                        }
                }

        });
                return {
                retrieveSectionWiseCustomFieldInfos: function(featureName) {
                var deferred = $q.defer();
                        var data = null;
                        if (featureName) {
                data = encryptPass(localStorage.getItem(featureName));
                        if (data !== null) {
                deferred.resolve(JSON.parse(data));
                        return deferred.promise;
                } else {
                customfieldManagment.retrieveCustomFieldByFeatureName(featureName, function(response) {
                if (response) {
                var encryptedValue = encryptPass(JSON.stringify(response));
                        try {
                        localStorage.setItem(featureName, encryptedValue);
                        } catch (e) {
                //console.log("LIMIT REACHED: (" + i + ")");
                }
                deferred.resolve(response);
                }
                });
                        return deferred.promise;
                }
                }
                }
                };
                return customfieldManagment;
        }]);
        hkg.factory('LABELMASTER', ['$resource', function(resource) {
        var Notify = resource(rootScope.apipath + 'locale/:action', {
        action: '@actionName'}, {
        initLabelMaster: {
        method: 'GET',
                isArray: false,
                params: {
                action: 'initLabelMaster'
                }
        },
        });
                return Notify;
        }]);
        hkg.factory('ReportMaster', ['$resource', function(resource) {
        var report = resource(rootScope.apipath + 'report/:action', {
        action: '@actionName'}, {
        retrieveReportByFeature: {
        method: 'POST',
                params: {
                action: 'retrievereportbyfeature'
                }
        }

        });
                return report;
        }]);
        hkg.factory('Common', ['$resource', function(resource) {
        var common = resource(rootScope.centerapipath + 'common/:action', {
        action: '@actionName'}, {
        authenticateProxy: {
        method: 'POST',
                params: {
                action: 'authenticateProxy'
                }
        }
        });
                return common;
        }]);
        hkg.factory('MasterProxyLogin', ['$resource', function(resource) {
        var common = resource(rootScope.apipath + 'common/:action', {
        action: '@actionName'}, {
        authenticateProxy: {
        method: 'POST',
                params: {
                action: 'authenticateProxy'
                }
        }
        });
                return common;
        }]);
        hkg.factory('EmpTemp', ['$resource', function(resource) {
        var common = resource(rootScope.apipath + 'employee/:action', {
        action: '@actionName'}, {
        retrievePrerequisite: {
        method: 'GET',
                params: {
                action: 'retrieveprerequisite'
                }
        },
        });
                return common;
        }]);
        hkg.factory('WorkAllocation', ['$resource', '$rootScope', function(resource, rootScope) {
        var workAllocation = resource(rootScope.centerapipath + 'allotment/:action', {
        action: '@actionName'}, {
        retrieveWorkAllocationCount: {
        method: 'GET',
                params: {
                action: 'retrieveworkallocationcount'
                }
        },
        });
                return workAllocation;
        }]);
        hkg.factory('MessagingPopup', ['$resource', '$rootScope', function(resource, rootScope) {
        var Notify = resource(rootScope.apipath + 'messaging/:action', {
        action: '@actionName'}, {
        saveMessage: {
        method: 'POST',
                isArray: false,
                params: {
                action: 'savemessagepopup'
                }
        },
                retrieveUserList: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/users'
                        }
                },
                retrieveRoleList: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/roles'
                        }
                },
                retrieveDepartmentList: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/departments'
                        }
                },
                retrieveActivityList: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/activities'
                        }
                },
                retrieveGroupList: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/groups'
                        }
                },
                retrieveAlerts: {
                method: 'POST',
                        params: {
                        action: 'retrieve/alerts'
                        },
                        url: rootScope.centerapipath + "messaging/:action"
                },
                markAsClosed: {
                method: 'POST',
                        isArray: false,
                        params: {
                        action: 'mark/closed'
                        }
                },
                retrieveUnreadMessages: {
                method: 'POST',
                        isArray: true,
                        params: {
                        action: 'retrieve/unreadmessages'
                        },
                        url: rootScope.centerapipath + "messaging/:action"
                }

        });
                return Notify;
        }]);
        hkg.factory('prompt', function() {

        /* Uncomment the following to test that the prompt service is working as expected.
         return function () {
         return "Test!";
         }
         */

        // Return the browsers prompt function.
        return prompt;
        });
        hkg.factory('FranchiseConfig', ['$resource', '$rootScope', function(resource, rootScope) {
        var franchiseconfig = resource(rootScope.centerapipath + 'franchiseconfig/:action', {
        action: '@actionName'}, {
        retrieveAllConfiguration: {
        method: 'GET',
                params: {
                action: 'retrieveallconfig'
                }
        },
        });
                return franchiseconfig;
        }]);
        angularAMD.bootstrap(hkg);
        return hkg;
        });
