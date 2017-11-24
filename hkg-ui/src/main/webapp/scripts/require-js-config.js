require.config({
    waitSeconds: 0,
    baseUrl: 'scripts/controllers',
    // alias libraries paths.  Must set 'angular'
    paths: {
        // used for menu view : Alpesh 
        'menu': '../directives/menu.directive',
        //        Jquery Path
//        'jquery': '../third-party/jquery/jquery-1.10.2',
        'jquery': '../third-party/jquery/jquery.min',
        //        BOOTSTRAP
        'bootstrap': '../third-party/bootstrap/bootstrap.min',
        'uiBootstrap': '../third-party/angular-ui/ui-bootstrap-tpls.min',
        //         LOADING MASK
        //minified version of this file has been deleted as it's code has been modified by Akta Kalariya
        'loadmask': '../third-party/loading-mask/jquery.loadmask',
        //        ANGULAR
        'angular': '../third-party/angular/angular.min',
        'angular-route': '../third-party/angular/angular-route.min',
        'angular-resource': '../third-party/angular/angular-resource.min',
        'angular-idle': '../third-party/angular-idle/angular-idle',
        //        ANGULAR AMD for requirejs
        'angularAMD': '../third-party/require/angularAMD.min',
        'ngload': '../third-party/require/ngload.min',
        'hotkeys': '../third-party/angular-hotkey/hotkeys.min',
        'infiniteScroll': '../third-party/angular-infinitescroll/ng-infinite-scroll',
        'ngTable': '../third-party/ngtable/ng-table',
        'angular-scroll': '../third-party/angular-scroll/angular-scroll',
        'uiGrid': '../third-party/ui-grid/ui-grid-unstable.min',
        'tableExport': '../third-party/reportbuilder/tableExport/tableExport',
//        'colResizable' : '../third-party/colResizable/colResizeable',
        'colResizable': '../third-party/colResizable/colResizeable',
        'colResizeableDirective': '../directives/colResizable.directive',
//        'pdfmake': '../third-party/pdfmake/pdfmake',
//        'vfsFonts': '../third-party/vfs-fonts/vfs-fonts',
//        'csv': '../third-party/csv/csv',
        //date difference
        'dateDifference': '../third-party/date-difference',
        'dateFormatter': '../third-party/date',
        //Internationalization script
        'angularTranslate': '../third-party/angular-translate/angular-translate',
        'angularTranslateLoaderStaticFiles': '../third-party/angular-translate/angular-translate-loader-static-files',
        'jquery-i18n-property': '../third-party/jquery/jquery.i18n.properties-min-1.0.9',
        //ARGUS SCRIPTS
        'hkg': '../app',
        // HKG JS for transitions
        'hkgjs': '../hkg',
        'systemConfigSerivce': '../services/usermanagement/system-configuration.service',
        'loadingMaskDirective': '../directives/loading-mask.directive',
        'internationalizationService': '../services/internationalization/internationalization.service',
        'templateService': '../services/email/template-configuration-service',
        'syncService': '../services/sync/sync.service',
        'multiselectDirective': '../directives/multiselect.directive',
        'featureService': '../services/usermanagement/feature.service',
        'departmentService': '../services/department/department.service',
        'activityFlowService': '../services/activityflow/activity-flow.service',
        //TREE VIEW Directive
        'treeview.directive': '../directives/treeview.directive',
        'awDatepickerPattern.directive': '../directives/awDatepickerPattern.directive',
        //Dynamic Form
        'dynamicForm': '../directives/dynamicform.directive',
        'dynamicFormGrid': '../directives/customgrid/dynamicform-grid.directive',
        'editableCustomGrid': '../directives/customgrid/editablecustomgrid.directive',
        'dateValidate': '../directives/datevalidate',
        //ruleField 
        'ruleField': '../directives/rulefield.directive',
        //addMasterValue 
        'addMasterValue': '../directives/master/master.directive',
        //rule template
        'ruleTemplate': '../directives/ruletemplate/ruletemplate.directive',
        //designation template
        'designationTemplate': '../directives/designationtemplate/desigtemplate.directive',
        //printBarcodeValue 
        'printBarcodeValue': '../directives/printbarcode/print-barcode.directive',
        //Accordion Collapse 
        'accordionCollapse': '../directives/accordion/accordion.directive',
        //Camera directive 
        'webcam': '../directives/webcapture/webcam.directive',
        //dynamic Tabs
        'tabs': '../directives/dynamic-tabs.directive',
        //MAX LENGTH Directive
        'maxlength': '../directives/ag-maxlength',
        //popover directive
        'popover.directive': '../directives/popover.directive',
        'checkedList': '../directives/department-management/department.directive',
        //TREEVIEW WITH MULTISELECT direvtive
        'treeviewMultiselect.directive': '../directives/treeview-multiselect.directive',
        'checklist.directive': '../directives/checklist.directive',
        //download file
        'fileSaver': '../third-party/download-file/FileSaver',
        //location Service
        'locationService': '../services/location-management/location.service',
        //Added by : vvaghela
        //Date : 22-03-2014
        //asset management service
        //Akta Kalariya   image file for imageUpload directive
        'imageUploadDirective': '../directives/imageupload.directive',
        'reportBuilderService': '../services/reportbuilder/reportbuilder.service',
        //Added by kvithlani for multiselect with tagging support used in report builder
        //Select2 jquery file
        //This file is modified by kvithlani so generate min file accordingly
        'select2': '../third-party/select2/select2.min',
        //Select2 angular directive
        'select2.directive': '../directives/select2.directive',
        //Alert validation message angular directive Piyush & Kuldeep
        'alert.directive': '../directives/alert.directive',
        //Added by kvithlani and vvaghela
        //For File upload Ng-flow
        'ngflow': '../third-party/ngflow/ng-flow-standalone.min',
//        Added by kvithlani for drag-drop support in formula configuration
        'jqueryUi': '../third-party/jquery-ui/jquery-ui-1.10.4.custom.min',
//          Added by kvithlani for formula configuration
        'selectize': '../third-party/selectize/selectize.min',
        'selectize.directive': '../directives/selectize.directive',
        'selectizeforpointer.directive': '../directives/selectizeforpointer.directive',
        'selectizeforconstraint.directive': '../directives/selectizeforconstraint.directive',
        'selectizefordate.directive':'../directives/selectizefordate.directive',
        'digitonly.directive': '../directives/digitonly.directive',
        'whitespace-remove.directive': '../directives/whitespace-remove.directive',
        'enterworkas-tab.directive': '../directives/enterworkas-tab.directive',
        'customsearch.directive': '../directives/customsearch.directive',
        'subentity.directive': '../directives/subentity.directive',
        'manageHolidayService': '..//services/holiday/holiday.service',
        'messageService': '..//services/usermanagement/message.service',
        'leaveWorkflowService': '..//services/leavemanagement/leave-workflow.service',
        'departmentFlowService': '../services/departmentconfig/departmentflow.service',
        //custom field service
        'customFieldService': '../services/customfield/customfield.service',
        'designationService': '..//services/designation/designation.service',
        //dynamic form service
        'dynamicFormService': '../services/dynamicform/dynamicform.service',
        //notifications
        'notificationService': '../services/notifications/notification.service',
        //Added by kvithlani and skoyani for initialize hkg.directive module.

        //Please do not remove this because all directives will be loaded after initialization.
        'initDirective': '../directives/init.directive',
        //          Added by kvithlani for scrollable datatable
        'jqueryDataTable': '../third-party/datatable/jquery.dataTables.min',
        'dataTableScroller': '../third-party/datatable/dataTables.scroller.min',
        'datatableService': '../services/datatable/datatable.service',
        'datatable.directive': '../directives/datatable.directive',
        'datatable.bootstrap': '../third-party/datatable/dataTables.bootstrap',
        'taskService': '../services/taskmanagement/task.service',
        'assetService': '../services/assetmanagement/asset.service',
        'franchiseService': '../services/franchise/franchise.service',
        'fileUploadService': '../services/fileupload/fileuploadservice',
        'leaveService': '../services/leavemanagement/leave.service',
        'manageMasterService': '..//services/master/master.service',
        'manageCurrencyMasterService': '..//services/master/currency-master.service',
        'employeeService': '..//services/usermanagement/employee.service',
        //added by dhwani mehta for multiselect checkbox in dropdown with partition
        'angularMultiSelect': '../directives/angular-multi-select',
        'datetimepicker.directive': '../directives/datetimepicker.directive',
        'datepickercustom.directive': '../directives/datepickercustom.directive',
        'datepickersearch.directive': '../directives/datepickersearch.directive',
        //Added by kvithlani for color picker in manage events
        'colorpicker.directive': '../directives/colorpicker.directive',
        'eventService': '../services/eventmanagement/event.service',
        'localesService': '../services/locales/locales.service',
        'shiftService': '../services/shift/shift.service',
        //Added by vipul vaghela for search
        'typeahead': '../third-party/typeahead/typeahead.bundle',
        'search.directive': '../directives/search.directive',
        'franchiseConfigService': '../services/franchise/franchiseconfig.service',
        'animate': '../third-party/angular/angular-animate.min',
        'touch': '../third-party/angular/angular-touch.min',
        'flowChart': '../directives/flowchart.directive',
        'dragging': '../directives/flowchart/dragging_service',
        'mouseCapture': '../directives/flowchart/mouse_capture_service',
        'ruleService': '..//services/usermanagement/rule.service',
        'invoiceService': '../services/invoicemanagement/invoice.service',
        'lotService': '../services/lotmanagement/lot.service',
        'sellstockService': '../services/stockmanagement/sellstock.service',
        'mergestockService': '../services/stockmanagement/mergestock.service',
        'splitstockService': '../services/stockmanagement/splitstock.service',
        'transferstockService': '../services/stockmanagement/transferstock.service',
        'packetService': '../services/packetmanagement/packet.service',
        'parcelService': '../services/parcelmanagement/parcel.service',
        'goalService': '../services/goalmanagement/goal.service',
        'caratRangeService': '../services/caratrange/caratrange.service',
        'jqueryCaret': '../third-party/tagging/jquery.caret',
        'jqueryAtWho': '../third-party/tagging/jquery.atwho',
        'priceListService': '../services/pricelist/pricelist.service',
        'printService': '../services/print/print.service',
        'mockService': '../mockservice',
        'mergeService': '../services/mergestock/merge.service',
        'issuereceivestockService': '../services/issuereceivestock/issuereceivestock.service',
        'changestausService': '../services/change-status/change-status.service',
        'trackstockService': '../services/track-stock/track-stock.service',
        'allotmentService': '../services/allotment/allotment.service',
        'transferemployeeService': '../services/usermanagement/transfer-employee.service',
        'reRoutStockService': '../services/rerout-stock/rerout-stock.service',
        'endOfTheDayService': '../services/endoftheday/endoftheday.service',
        'startOfTheDayService': '../services/startoftheday/startoftheday.service',
        'writeService': '../services/write/write.service',
        'goalsheetService': '../services/goalsheet/goalsheet.service',
        'transactionLogService': '../services/transactionlog/transactionlog.service',
        'editvalueService': '../services/stockmanagement/editvalue.service',
        'finalizeService': '../services/finalize/finalize.service',
        'generateSlipService': '../services/generateslip/generateslip.service',
        'printBarcodeService': '../services/printbarcode/print-barcode.service',
        'mergeparcelService': '../services/stockmanagement/mergeparcel.service',
        'rapCalcyDirective': '../directives/rapcalcy/rapcalcy.directive',
        'rapCalcyService': '../services/rapcalcy/rapcalcy.service',
        'ruleConfigService': '../services/ruleconfig/rule-config.service',
        'ruleExecutionService': '../services/ruleconfig/rule-execution.service',
        'departmentConfigService': '../services/departmentconfig/departmentconfig.service',
        'designationConfigService': '../services/designationconfig/designationconfig.service',
        'estPredictService': '../services/estpredict/estimateprediction.service',
        'roughsaleService': '../services/stockmanagement/roughsale.service',
        'ParcelTemplateService': '../services/parcelmanagement/parceltemplate.service',
        'ParcelTemplateController': '../controllers/parcelmanagement/parcel-template.controller',
        'planService': '../services/stockmanagement/plan.service',
        'SublotController': '../controllers/sublot/sublot.controller',
        'sublotService': '../services/sublot/sublot.service',
        'roughPurchaseService': '../services/roughpurchase/roughpurchase.service',
        'roughMakeableService': '../services/roughmakeable/roughmakeable.service',
        'finalMakeableService': '../services/finalmakeable/finalmakeable.service'

    },
    // Add angular modules that does not support AMD out of the box, put it in a shim
    shim: {
        'angularTranslateLoaderStaticFiles': ['angularTranslate'],
        'angularAMD': ['angular'],
        'angular-route': ['angular'],
        'angular-resource': ['angular'],
        //Added by kvithlani for jquery datatable
        'datatable.bootstrap': ['jqueryDataTable'],
        //Please do not remove this because all directives will be loaded after initialization.
        'hkg': ['jquery', 'initDirective'],
        'flowChart': ['angular'],
        'ngload': ['angularAMD']
    },
    // kick start application
    deps: ['hkg']
});
//Add required dependency(scripts) that will be available in all the pages
require(["hkg-root.controller"]);

//requirejs.onError = function(err) {
//    console.log(err.requireType);
//    if (err.requireType === 'timeout') {
//        console.log('modules: ' + err.requireModules);
//    }
//    var url = "http://" + window.location.hostname + ":9090/hkg/tryagain.html";
//    $(location).attr('href', url);
//    throw err;
//};
