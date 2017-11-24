define(["angularAMD","angular-route","angular-resource","angularTranslate","angularTranslateLoaderStaticFiles","bootstrap","angular-idle","loadingMaskDirective","multiselectDirective","treeview.directive","tabs","uiBootstrap","menu","select2","select2.directive","ngflow","jqueryDataTable","dataTableScroller","datatable.bootstrap","datatable.directive","digitonly.directive","angularMultiSelect","hkgjs","datetimepicker.directive","popover.directive","typeahead","search.directive","dynamicFormService","notificationService","dateFormatter","animate","touch","enterworkas-tab.directive","angular-scroll","dateValidate","hotkeys"],function(a){var c;
var b=angular.module("hkg",["ngRoute","ngResource","hkg.directives","ui.bootstrap","flow","pascalprecht.translate","dynamicformmodule","notificationmodule","ngAnimate","ngTouch","ngIdle","duScroll","cfp.hotkeys","dndLists"]);
b.config(["$routeProvider","$locationProvider","$translateProvider","$keepaliveProvider","$idleProvider",function(d,h,g,e,f){g.useStaticFilesLoader({prefix:"i18n/",suffix:".json"});
f.idleDuration(60*60);
f.warningDuration(1);
g.use("EN0");
d.when("/login",a.route({templateUrl:"views/unsecure/login.html",controller:"Login",controllerUrl:"usermanagement/login.controller"})).when("/accessdenied",a.route({templateUrl:"views/unsecure/accessdenied.html"})).when("/systemconfig",a.route({templateUrl:"views/secure/systemconfig.html",controller:"SystemConfiguration",controllerUrl:"usermanagement/system-configuration.controller"})).when("/createlabel",a.route({templateUrl:"views/secure/createlabel.html",controller:"InternationalizationAddLabel",controllerUrl:"internationalization/create-label.controller"})).when("/updatelabel",a.route({templateUrl:"views/secure/updatelabel.html",controller:"InternationalizationLabelList",controllerUrl:"internationalization/update-label.controller"})).when("/managefeature",a.route({templateUrl:"views/secure/managefeature.html",controller:"ManageFeature",controllerUrl:"usermanagement/feature.controller"})).when("/diamondmaster",a.route({templateUrl:"views/secure/managemaster.html",controller:"MasterController",controllerUrl:"usermanagement/master.controller"})).when("/manageuserleave",a.route({templateUrl:"views/secure/manageuserleave.html",controller:"ManageUserLeaveController",controllerUrl:"usermanagement/manage-user-leave.controller"})).when("/leaverequest",a.route({templateUrl:"views/secure/createleaverequest.html",controller:"LeaveRequestController",controllerUrl:"usermanagement/leave-request.controller"})).when("/stockmaster",a.route({templateUrl:"views/secure/managemaster.html",controller:"MasterController",controllerUrl:"usermanagement/master.controller"})).when("/managegroup",a.route({templateUrl:"views/secure/managegroup.html",controller:"ManageGroup",controllerUrl:"usermanagement/manage-group.controller"})).when("/creategroup",a.route({templateUrl:"views/secure/creategroup.html",controller:"ManageGroup",controllerUrl:"usermanagement/manage-group.controller"})).when("/createemployee",a.route({templateUrl:"views/secure/createemployee.html",controller:"AddEmployee",controllerUrl:"usermanagement/create-employee.controller"})).when("/managedepartment",a.route({templateUrl:"views/secure/managedepartment.html",controller:"DepartmentController",controllerUrl:"department/department.controller"})).when("/addtemplate",a.route({templateUrl:"views/admin/addTemplate.html",controller:"AddTemplate",controllerUrl:"email/add-template-controller"})).when("/manageleaveworkflow",a.route({templateUrl:"views/secure/manageleaveworkflow.html",controller:"LeaveWorkflowController",controllerUrl:"leavemanagement/leave-workflow.controller"})).when("/addDepartment",a.route({templateUrl:"views/secure/addDepartment.html",controller:"departmentController",controllerUrl:"department-management/department.controller"})).when("/createasset",a.route({templateUrl:"views/secure/createasset.html",controller:"CategoryController",controllerUrl:"assetmanagement/asset.controller"})).when("/viewasset",a.route({templateUrl:"views/secure/viewasset.html",controller:"ViewAssetController",controllerUrl:"assetmanagement/view-asset.controller"})).when("/manageassetcategory",a.route({templateUrl:"views/secure/manageassetcategory.html",controller:"ManageAssetCategory",controllerUrl:"assetmanagement/asset-category.controller"})).when("/viewdesignation",a.route({templateUrl:"views/secure/viewdesignation.html",controller:"ViewDesignation",controllerUrl:"usermanagement/view-designation.controller"})).when("/managedesignation",a.route({templateUrl:"views/secure/managedesignation.html",controller:"DesignationCtrl",controllerUrl:"designation/designation.controller"})).when("/managepermissionset",a.route({templateUrl:"views/secure/managepermissionset.html",controller:"ManagePermissionSet",controllerUrl:"usermanagement/permissionset.controller"})).when("/shiftmanagement",a.route({templateUrl:"views/secure/shiftmanagement.html",controller:"ShiftManagementController",controllerUrl:"shiftmanagement/shift-management.controller"})).when("/managemessage",a.route({templateUrl:"views/secure/managemessage.html",controller:"MessageController",controllerUrl:"usermanagement/message.controller"})).when("/exitinterviewform",a.route({templateUrl:"views/secure/configureexitinterviewform.html",controller:"ConfigureIntervewForm",controllerUrl:"systemmanagement/configure-interviewform.controller"})).when("/fillexitinterviewform",a.route({templateUrl:"views/secure/fillexitinterviewform.html",controller:"ConfigureIntervewForm",controllerUrl:"systemmanagement/configure-interviewform.controller"})).when("/managelocation",a.route({templateUrl:"views/secure/locationdetails.html",controller:"LocationController",controllerUrl:"location-management/location.controller"})).when("/reportbuilder",a.route({templateUrl:"views/secure/reportbuilder.html",controller:"ReportBuilder",controllerUrl:"reportbuilder/reportbuilder.controller"})).when("/viewemployee",a.route({templateUrl:"views/secure/viewemployee.html",controller:"ViewEmployeeCtrl",controllerUrl:"usermanagement/view-employee.controller"})).when("/updateemployee",a.route({templateUrl:"views/secure/updateemployee.html",controller:"UpdateEmployeeCtrl",controllerUrl:"usermanagement/update-employee.controller"})).when("/managereport",a.route({templateUrl:"views/secure/managereport.html",controller:"ManageReport",controllerUrl:"reportbuilder/report.controller"})).when("/viewreports",a.route({templateUrl:"views/secure/viewreports.html",controller:"PreviewReport",controllerUrl:"reportbuilder/reportpreview.controller"})).when("/configureformula",a.route({templateUrl:"views/secure/formulaConfiguration.html",controller:"ConfigureFormula",controllerUrl:"formula/configure-formula.controller"})).when("/manageholiday",a.route({templateUrl:"views/secure/manageholiday.html",controller:"ManageHoliday",controllerUrl:"holiday/holiday.controller"})).when("/managecustomfield",a.route({templateUrl:"views/secure/managecustomfield.html",controller:"CustomField",controllerAs:"Custom",controllerUrl:"customfield/customfield.controller"})).when("/newmanagecustomfield",a.route({templateUrl:"views/secure/newmanagecustomfield.html",controller:"CustomField",controllerUrl:"customfield/newcustomfield"})).when("/managetasks",a.route({templateUrl:"views/secure/managetask.html",controller:"ManageTasks",controllerUrl:"taskmanagement/task.controller"})).when("/applyleave",a.route({templateUrl:"views/secure/applyleave.html",controller:"ApplyLeave",controllerUrl:"leavemanagement/apply-leave.controller"})).when("/manageleave",a.route({templateUrl:"views/secure/manageleave.html",controller:"ManageLeave",controllerUrl:"leavemanagement/leave.controller"})).when("/manageasset",a.route({templateUrl:"views/secure/manageasset.html",controller:"CategoryController",controllerUrl:"assetmanagement/asset.controller"})).when("/managefranchise",a.route({templateUrl:"views/secure/managefranchise.html",controller:"FranchiseController",controllerUrl:"franchise/franchise.controller"})).when("/managemaster",a.route({templateUrl:"views/secure/managemaster.html",controller:"ManageMaster",controllerUrl:"master/master.controller"})).when("/example",a.route({templateUrl:"views/secure/example.html",controller:"Login",controllerUrl:"usermanagement/login.controller"})).when("/managedynamicform",a.route({templateUrl:"views/secure/managedynamicform.html",controller:"ManageDynamicForm",controllerUrl:"dynamicform/dynamic-form.controller"})).when("/manageevents",a.route({templateUrl:"views/secure/manageevent.html",controller:"ManageEvents",controllerUrl:"eventmanagement/event.controller"})).when("/manageemployee",a.route({templateUrl:"views/secure/manageemployee.html",controller:"EmployeeController",controllerUrl:"usermanagement/employee.controller"})).when("/managelocales",a.route({templateUrl:"views/secure/managelocales.html",controller:"LocalesContoller",controllerUrl:"locales/locale.controller"})).when("/manageshift",a.route({templateUrl:"views/secure/manageshift.html",controller:"ShiftController",controllerUrl:"shift/shift.controller"})).when("/manageshift",a.route({templateUrl:"views/secure/manageshift.html",controller:"ShiftController",controllerUrl:"shift/shift.controller"})).when("/configurefranchise",a.route({templateUrl:"views/secure/configurefranchise.html",controller:"ConfigureFranchise",controllerUrl:"configurefranchise/configure-franchise.controller"})).when("/shownotification",a.route({templateUrl:"views/secure/shownotification.html",controller:"showNotification",controllerUrl:"notifications/show-notification.controller"})).when("/registerevent",a.route({templateUrl:"views/secure/registerevent.html",controller:"ManageEvents",controllerUrl:"eventmanagement/manage-events.controller"})).when("/editprofile",a.route({templateUrl:"views/secure/editprofile.html",controller:"EditProfile",controllerUrl:"usermanagement/edit-profile.controller"})).when("/dashboard",a.route({templateUrl:"views/secure/dashboard.html",controller:"DashboardController",controllerUrl:"usermanagement/dashboard.controller"})).when("/logout",a.route({templateUrl:"views/unsecure/logout.html",controller:"LogoutController",controllerUrl:"usermanagement/logout.controller"})).when("/manageactivityflow",a.route({templateUrl:"views/secure/manageactivityflow.html",controller:"ActivityFlowController",controllerUrl:"activityflow/activity-flow.controller"})).when("/managecurrency",a.route({templateUrl:"views/secure/managecurrency.html",controller:"ManageCurrencyMaster",controllerUrl:"master/currency-master.controller"})).when("/managereferencerate",a.route({templateUrl:"views/secure/managereferencerate.html",controller:"ManageCurrencyMaster",controllerUrl:"master/currency-master.controller"})).when("/flowchart",a.route({templateUrl:"views/unsecure/flowchart.html",controller:"FlowChartController12",controllerUrl:"flowchart/flowchart-controller"})).when("/managerule",a.route({templateUrl:"views/secure/managerule.html",controller:"RuleController",controllerUrl:"usermanagement/rule.controller"})).when("/printsvg",a.route({templateUrl:"views/secure/printsvg.html",controller:"ActivityFlowController",controllerUrl:"activityflow/activity-flow.controller"})).when("/trackstockprint",a.route({templateUrl:"views/secure/trackstockprint.html",controller:"TrackStockController",controllerUrl:"trackstock/track-stock.controller"})).when("/addinvoice",a.route({templateUrl:"views/secure/addinvoice.html",controller:"AddInvoiceController",controllerUrl:"invoicemanagement/add-invoice.controller"})).when("/roughinvoice",a.route({templateUrl:"views/secure/invoice.html",controller:"InvoiceController",controllerUrl:"invoicemanagement/invoice.controller"})).when("/editinvoice",a.route({templateUrl:"views/secure/editinvoice.html",controller:"EditInvoiceController",controllerUrl:"invoicemanagement/edit-invoice.controller"})).when("/addlot",a.route({templateUrl:"views/secure/addlot.html",controller:"AddLotController as AddLot",controllerAs:"AddLot",controllerUrl:"lotmanagement/add-lot.controller"})).when("/editlot",a.route({templateUrl:"views/secure/editlot.html",controller:"EditLotController",controllerUrl:"lotmanagement/edit-lot.controller"})).when("/mergelot",a.route({templateUrl:"views/secure/mergelot.html",controller:"MergeLotController",controllerUrl:"lotmanagement/merge-lot.controller"})).when("/stocksell",a.route({templateUrl:"views/secure/sellstock.html",controller:"SellStockController",controllerUrl:"stockmanagement/sell-stock.controller"})).when("/roughsale",a.route({templateUrl:"views/secure/roughsale.html",controller:"RoughSaleController",controllerAs:"roughSale",controllerUrl:"stockmanagement/roughsale.controller"})).when("/splitlot",a.route({templateUrl:"views/secure/splitlot.html",controller:"SplitLotController",controllerUrl:"lotmanagement/split-lot.controller"})).when("/stocktransfer",a.route({templateUrl:"views/secure/transferstock.html",controller:"TransferStockController",controllerUrl:"stockmanagement/transfer-stock.controller"})).when("/addpacket",a.route({templateUrl:"views/secure/addpacket.html",controller:"AddPacketController",controllerAs:"AddPacketCtrl",controllerUrl:"packetmanagement/add-packet.controller"})).when("/editpacket",a.route({templateUrl:"views/secure/editpacket.html",controller:"EditPacketController",controllerUrl:"packetmanagement/edit-packet.controller"})).when("/mergepacket",a.route({templateUrl:"views/secure/mergepacket.html",controller:"MergePacketController",controllerUrl:"packetmanagement/merge-packet.controller"})).when("/splitpacket",a.route({templateUrl:"views/secure/splitpacket.html",controller:"SplitPacketController",controllerAs:"Packet",controllerUrl:"packetmanagement/split-packet.controller"})).when("/addparcel",a.route({templateUrl:"views/secure/addparcel.html",controller:"AddParcelController as AddParcel",controllerAs:"AddParcel",controllerUrl:"parcelmanagement/add-parcel.controller"})).when("/editparcel",a.route({templateUrl:"views/secure/editparcel.html",controller:"EditParcelController",controllerUrl:"parcelmanagement/edit-parcel.controller"})).when("/managesubentity",a.route({templateUrl:"views/secure/managesubentity.html",controller:"SubEntityController",controllerUrl:"customfield/managesubentity.controller"})).when("/theme",a.route({templateUrl:"views/secure/theme.html",controller:"ThemeController",controllerUrl:"usermanagement/theme.controller"})).when("/managegoal",a.route({templateUrl:"views/secure/managegoal.html",controller:"GoalController",controllerUrl:"goalmanagement/goal.controller"})).when("/managegoaltemplate",a.route({templateUrl:"views/secure/managegoaltemplate.html",controller:"GoalTemplateController",controllerUrl:"goalmanagement/goaltemplate.controller"})).when("/managenotification",a.route({templateUrl:"views/secure/shownotification.html",controller:"showNotification",controllerUrl:"notifications/show-notification.controller"})).when("/managenotificationconfig",a.route({templateUrl:"views/secure/managenotification.html",controller:"NotificationController",controllerUrl:"notifications/managenotification.controller"})).when("/manageCaratRange",a.route({templateUrl:"views/secure/managecaratrange.html",controller:"CaratRangeController",controllerUrl:"caratrange/caratrange.controller"})).when("/managePriceList",a.route({templateUrl:"views/secure/managepricelist.html",controller:"PriceListController",controllerUrl:"pricelist/pricelist.controller"})).when("/setup",a.route({templateUrl:"views/unsecure/setup.html",controller:"SetUpController",controllerUrl:"sync-demo/setup.controller"})).when("/printstatic",a.route({templateUrl:"views/secure/printstatic.html",controller:"PrintStaticController",controllerUrl:"printstatic/print-static.controller"})).when("/mergestock",a.route({templateUrl:"views/secure/mergestock.html",controller:"MergeStockController",controllerUrl:"stockmanagement/mergestock.controller"})).when("/issuereceive",a.route({templateUrl:"views/secure/issuereceivestock.html",controller:"IssueReceiveController",controllerAs:"IssueController",controllerUrl:"issuereceivestock/issuereceive-stock.controller"})).when("/statuschange",a.route({templateUrl:"views/secure/changestatus.html",controller:"ChangeStatusController",controllerUrl:"changestatus/change-status.controller"})).when("/trackstock",a.route({templateUrl:"views/secure/trackstock.html",controller:"TrackStockController",controllerUrl:"trackstock/track-stock.controller"})).when("/totalstock",a.route({templateUrl:"views/secure/totalstock.html",controller:"TrackStockController",controllerUrl:"trackstock/track-stock.controller"})).when("/printdynamic",a.route({templateUrl:"views/secure/printdynamic.html",controller:"PrintDynamicController",controllerUrl:"printdynamic/print-dynamic.controller"})).when("/managetransferemployee",a.route({templateUrl:"views/secure/managetransferemployee.html",controller:"TransferEmployeeController",controllerUrl:"usermanagement/transfer-employee.controller"})).when("/stockreroute",a.route({templateUrl:"views/secure/reroutstock.html",controller:"ReRoutStockController",controllerUrl:"rerout-stock/rerout-stock.controller"})).when("/splitstock",a.route({templateUrl:"views/secure/splitstock.html",controller:"SplitStockController",controllerAs:"SplitStock",controllerUrl:"stockmanagement/splitstock.controller"})).when("/allotment",a.route({templateUrl:"views/secure/allotment.html",controller:"AllotmentController",controllerUrl:"allotment/allotment.controller"})).when("/endoftheday",a.route({templateUrl:"views/secure/endoftheday.html",controller:"EndOfTheDayController",controllerUrl:"endoftheday/endoftheday.controller"})).when("/startoftheday",a.route({templateUrl:"views/secure/startoftheday.html",controller:"StartOfTheDayController",controllerUrl:"startoftheday/startoftheday.controller"})).when("/writeservice",a.route({templateUrl:"views/secure/writeservice.html",controller:"WriteServiceController",controllerUrl:"writeservice/writeservice.controller"})).when("/managegoalsheet",a.route({templateUrl:"views/secure/managegoalsheet.html",controller:"ManageGoalSheetController",controllerUrl:"goalsheet/goalsheet.controller"})).when("/transactionlog",a.route({templateUrl:"views/unsecure/transactionlog.html",controller:"TransactionLogController",controllerUrl:"transactionlog/transactionlog.controller"})).when("/editvalue",a.route({templateUrl:"views/secure/editvalue.html",controller:"EditValueController",controllerUrl:"stockmanagement/edit-value.controller"})).when("/generateslip",a.route({templateUrl:"views/secure/generateslip.html",controller:"GenerateSlipController",controllerUrl:"generateslip/generateslip.controller"})).when("/finalize",a.route({templateUrl:"views/secure/finalize.html",controller:"FinalizeServiceController",controllerUrl:"finalizeservice/finalizeservice.controller"})).when("/printbarcode",a.route({templateUrl:"views/secure/printbarcode.html",controller:"PrintBarcodeController",controllerUrl:"printbarcode/print-barcode.controller"})).when("/roughmerge",a.route({templateUrl:"views/secure/mergeparcel.html",controller:"MergeParcelController",controllerUrl:"stockmanagement/mergeparcel.controller"})).when("/configureDepartment",a.route({templateUrl:"views/secure/configureDepartment.html",controller:"DeptConfigController",controllerUrl:"departmentconfig/department.config.controller"})).when("/configureDesignation",a.route({templateUrl:"views/secure/configureDesignation.html",controller:"DesignConfigController",controllerUrl:"designationconfig/designation.config.controller"})).when("/printflow",a.route({templateUrl:"views/secure/printflow.html",controller:"DeptConfigController",controllerUrl:"departmentconfig/department.config.controller"})).when("/configureRule",a.route({templateUrl:"views/secure/configureRule.html",controller:"RuleConfigController",controllerUrl:"ruleconfig/ruleconfig.controller"})).when("/estimateprediction",a.route({templateUrl:"views/secure/estimateprediction.html",controller:"EstPredctController",controllerUrl:"estpredict/estimateprediction.controller"})).when("/roughcalcy",a.route({templateUrl:"views/secure/roughcalcy.html",controller:"RoughCalcyController",controllerUrl:"roughcalcy/roughcalcy.controller"})).when("/sublot",a.route({templateUrl:"views/secure/sublot.html",controller:"SublotController",controllerUrl:"sublot/sublot.controller"})).when("/roughpurchase",a.route({templateUrl:"views/secure/roughpurchase.html",controller:"RoughPurchaseController",controllerUrl:"roughpurchase/roughpurchase.controller"})).when("/parceltemplate",a.route({templateUrl:"views/secure/parceltemplate.html",controller:"ParcelTemplateController",controllerUrl:"parcelmanagement/parceltemplate.controller"})).when("/roughmakeable",a.route({templateUrl:"views/secure/roughmakeable.html",controller:"RoughMakeableController",controllerUrl:"roughmakeable/roughmakeable.controller"})).when("/finalmakeable",a.route({templateUrl:"views/secure/finalmakeable.html",controller:"FinalMakeableController",controllerUrl:"finalmakeable/finalmakeable.controller"}))
}]);
b.run(["$rootScope",function(d){d.masterHkgPath=corsConfig.masterHkgPath;
d.isMaster=corsConfig.isMaster;
d.apipath=d.masterHkgPath+"api/";
d.centerapipath="api/"
}]);
b.run(["$rootScope","$http","$location","DynamicFormService","$idle","$q","$route",function(l,j,h,k,d,e,f){d.watch();
l.bootstrapcsspath="css/default/bootstrap.min.css";
l.hkgcsspath="css/default/hkg.css";
l.notificationimagepath="css/default/notification.png";
l.composeimagepath="css/default/compose.png";
l.chatimagepath="css/default/chat.png";
l.printableChars="~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./";
initializeAngularScope(l,j,e,f);
l.setMessage=function(o,n,m){l.message={text:o,type:n,show:m,showLoginLink:false}
};
l.setMessage=function(q,p,m,o,n){setMessage(q,p,m,o,n)
};
l.canAccess=function(m){l.hasAccess=false;
l.canAccessMenu(m,l.menu);
return l.hasAccess
};
l.canAccessMenu=function(m,n){return l.containsFeature(m,n)
};
l.getProfilePicture=function(){return getProfilePicture()
};
l.containsFeature=function(m,n){if(n){angular.forEach(n,function(o){if(!l.hasAccess){if(o.featureName===m){return l.hasAccess=true
}else{if(o.children){return l.canAccessMenu(m,o.children)
}}}})
}return l.hasAccess
};
l.requests401=[];
l.requests403=[];
l.isMasterDown=false;
l.$on("event:loginRequired",function(){console.log("run : loginrequired event : loginTry : "+l.loginTry);
if(l.isLoggedIn){h.path("/accessdenied")
}else{h.path("/login")
}l.showLogin()
});
l.$on("$idleTimeout",function(){l.$broadcast("event:logoutRequest")
});
l.$on("event:loginConfirmed",function(n,p){var o={featureName:"login",entityId:null,entityType:"login",currentFieldValueMap:{username:l.session.userCode},dbType:null,otherEntitysIdMap:null};
var m=l.centerapipath+"executerule/postrule";
j.post(m,o).success(function(q){if(!!q.validationMessage){l.$broadcast("event:logoutRequest")
}},function(){});
l.isLoggedIn=true;
l.loginTry=-1;
if(l.menu){angular.forEach(l.menu,function(q){if(q.menuCategory===l.menuManage){l.menuManageAccess=true
}else{if(q.menuCategory===l.menuStock&&!l.isMaster){l.menuStockAccess=true
}else{if(q.menuCategory===l.menuReport){l.menuReportAccess=true
}}}})
}if(h.path()==="/login"){h.path("/dashboard")
}console.log("company id..."+l.session.companyId);
l.$broadcast("event:pollingStart");
if(p.indexOf(l.apipath)>=0){l.retrieveMinAge();
k.storeAllCustomFieldData(l.session.companyId)
}});
l.$on("event:logoutRequest",function(){l.isLoggedIn=false;
l.$broadcast("event:pollingStop");
l.beforeLogoutUrl=l.centerapipath+"common/adduseroperationbeforelogout";
j.get(l.beforeLogoutUrl).success(function(){console.log("run : logout request event");
h.path("/logout")
})
});
l.pingurl=l.centerapipath+"common/getsession";
l.pingServer=function(){var m=e.defer();
l.ping=false;
l.clientTimezoneOffset=new Date().getTimezoneOffset();
j.post(l.pingurl,l.clientTimezoneOffset).success(function(o){l.session=o;
l.session.currentDesignation=l.session.currentDesignation.toString();
l.menu=undefined;
if(o!==undefined&&o.features!==undefined){l.menu=o.features
}l.serverDate=new Date(l.session.serverDate);
l.checkUserTimezone(true);
l.switchLanguage(l.session.prefferedLang,l.session.companyId);
l.viewEncryptedData=l.canAccess("view_encrypted_data");
l.getCurrentServerDate();
l.$broadcast("event:loginConfirmed",l.pingurl);
var p=l.session.theme;
if(p!=undefined&&p!==null&&p.length>0){l.setThemeFolder(p)
}if(l.session.hasBg){l.retrieveWallpaper()
}else{l.setDefaultWallpaper()
}if(!!l.session.reportsToUsers){console.log(l.session.reportsToUsers);
var n=l.session.id;
var o={};
l.session.reportsToUsers[n]=l.session.firstName+" "+l.session.lastName;
if(!!!localStorage.getItem("proxyLogin")){localStorage.setItem("proxyLogin",l.session.id);
l.userProxy=localStorage.getItem("proxyLogin")
}else{l.userProxy=localStorage.getItem("proxyLogin")
}}m.resolve()
},function(){m.reject()
});
return m.promise
};
l.masterPingUrl=l.apipath+"common/getsession";
l.pingMasterServer=function(){return pingMaster()
};
l.closeLoginModal=function(){closeLoginModal()
};
l.loginToMaster=function(n,m){return loginToMaster(n,m)
};
l.checkUserTimezone=function(m){l.invalidTimezone=false;
var n=new Date(l.serverDate).toGMTString();
var o=new Date().toGMTString();
var p=new Date(n)-new Date(o);
if(p>=600000||p<=(-600000)){l.invalidTimezone=true;
l.setMessage("Server and system timezone are not in sync",l.failure,true);
l.logout()
}else{l.invalidTimezone=false
}};
l.fetchVersion=function(){j.get(l.apipath+"common/getbuildversion").success(function(m){l.projectBuildVersion=m
})
};
l.getServerDate=function(){j.get(l.apipath+"common/getserverdate").success(function(m){l.serverDate=new Date(parseInt(m));
l.checkUserTimezone(false)
})
};
l.setThemeFolder=function(m){l.bootstrapcsspath="css/"+m+"/bootstrap.min.css";
l.hkgcsspath="css/"+m+"/hkg.css";
l.notificationimagepath="css/"+m+"/notification.png";
l.composeimagepath="css/"+m+"/compose.png";
l.chatimagepath="css/"+m+"/chat.png";
l.session.theme=m
};
l.retrieveWallpaper=function(){var m=l.apipath+"employee/getwallpaper";
j.get(m).success(function(n){var o={src:n};
l.defaultWallpaperName=o.src;
l.defaultPath=l.apipath+"employee/getimage?file_name="+o.src+"&token="+l.authToken
})
};
l.setDefaultWallpaper=function(){var n=jQuery("#content.right-content-bg").css("background");
var m=n.substr(0,n.indexOf("no-repeat"));
l.defaultPath=m
};
l.appendAuthToken=function(m){return appendAuthToken(m)
};
l.getConfigHeaderForHttpRequest=function(m){return getConfigHeaderForHttpRequest(m)
};
var g=localStorage.getItem("user");
if(g!==undefined&&g!==null){l.authToken=g
}var i=localStorage.getItem("masterAuthToken");
if(i!==undefined&&i!==null){l.masterAuthToken=i
}}]);
b.config(["$httpProvider","$provide",function(e,d){addCustomRequestInterceptor(e);
var f=["$rootScope","$q",function(h,i){function j(m){var n=""+m.config.url;
if(n.indexOf("deployserver")>=0){console.log(JSON.stringify(m.data.messages))
}if((n.indexOf(h.apipath)>=0||n.indexOf(h.centerapipath)>=0)&&m.data.porcessResponse==true){var l=m.data.messages;
if(l&&l.length>0){for(var o=0;
o<l.length;
o++){var p=l[o];
if(p){h.addMessage(p.message,p.responseCode,null,null,p.translateValueMap)
}}}var k=m.data.data;
m.data=k
}return m
}function g(m){var k=m.status;
var l=i.defer();
var n={config:m.config,deferred:l};
if(k===401){error401(m,l,n);
i.reject(m)
}else{if(k===403){error403(m);
h.requests403.push(n);
h.$broadcast("event:loginRequired");
if(m.data!==""){h.addMessage(m.data,3);
h.setMessage(m.data,h.failure,true)
}return l.promise
}else{if(k===404){error404(m,i);
i.reject(m)
}else{if(k>=500){error500(m,i);
i.reject(m)
}}}}return i.reject(m)
}return function(k){return k.then(j,g)
}
}];
e.responseInterceptors.push(f)
}]);
b.factory("Notification",["$resource","$rootScope",function(f,e){var d=f(e.centerapipath+"notification/:action",{action:"@actionName"},{retrieveNotificationCount:{method:"GET",isArray:false,params:{action:"retrievecount"}},retrieveNotificationsPopUp:{method:"GET",isArray:true,params:{action:"retrievenotificationpopup"}}});
return d
}]);
b.factory("CustomField",["$resource","$q","$rootScope",function(g,d,e){var f=g(e.apipath+"customfield/:action",{action:"@actionName"},{retrieveSectionAndCustomFieldInfoTemplateByFeatureId:{method:"POST",isArray:false,params:{action:"retrievesectionandcustomfieldtemplate"},url:e.centerapipath},retrieveCustomFieldByFeatureName:{method:"POST",isArray:false,params:{action:"retrievesectionandcustomfieldtemplate"},url:e.centerapipath+"customfield/:action"},retriveAccesibleCustomFieldForFeature:{method:"POST",isArray:false,params:{action:"retriveAccesibleCustomFieldForFeature"}}});
return{retrieveSectionWiseCustomFieldInfos:function(i){var h=d.defer();
var j=null;
if(i){j=encryptPass(localStorage.getItem(i));
if(j!==null){h.resolve(JSON.parse(j));
return h.promise
}else{f.retrieveCustomFieldByFeatureName(i,function(k){if(k){var m=encryptPass(JSON.stringify(k));
try{localStorage.setItem(i,m)
}catch(l){}h.resolve(k)
}});
return h.promise
}}}};
return f
}]);
b.factory("LABELMASTER",["$resource",function(e){var d=e(rootScope.apipath+"locale/:action",{action:"@actionName"},{initLabelMaster:{method:"GET",isArray:false,params:{action:"initLabelMaster"}},});
return d
}]);
b.factory("ReportMaster",["$resource",function(e){var d=e(rootScope.apipath+"report/:action",{action:"@actionName"},{retrieveReportByFeature:{method:"POST",params:{action:"retrievereportbyfeature"}}});
return d
}]);
b.factory("Common",["$resource",function(e){var d=e(rootScope.centerapipath+"common/:action",{action:"@actionName"},{authenticateProxy:{method:"POST",params:{action:"authenticateProxy"}}});
return d
}]);
b.factory("MasterProxyLogin",["$resource",function(e){var d=e(rootScope.apipath+"common/:action",{action:"@actionName"},{authenticateProxy:{method:"POST",params:{action:"authenticateProxy"}}});
return d
}]);
b.factory("EmpTemp",["$resource",function(e){var d=e(rootScope.apipath+"employee/:action",{action:"@actionName"},{retrievePrerequisite:{method:"GET",params:{action:"retrieveprerequisite"}},});
return d
}]);
b.factory("WorkAllocation",["$resource","$rootScope",function(f,e){var d=f(e.centerapipath+"allotment/:action",{action:"@actionName"},{retrieveWorkAllocationCount:{method:"GET",params:{action:"retrieveworkallocationcount"}},});
return d
}]);
b.factory("MessagingPopup",["$resource","$rootScope",function(f,e){var d=f(e.apipath+"messaging/:action",{action:"@actionName"},{saveMessage:{method:"POST",isArray:false,params:{action:"savemessagepopup"}},retrieveUserList:{method:"POST",isArray:true,params:{action:"retrieve/users"}},retrieveRoleList:{method:"POST",isArray:true,params:{action:"retrieve/roles"}},retrieveDepartmentList:{method:"POST",isArray:true,params:{action:"retrieve/departments"}},retrieveActivityList:{method:"POST",isArray:true,params:{action:"retrieve/activities"}},retrieveGroupList:{method:"POST",isArray:true,params:{action:"retrieve/groups"}},retrieveAlerts:{method:"POST",params:{action:"retrieve/alerts"},url:e.centerapipath+"messaging/:action"},markAsClosed:{method:"POST",isArray:false,params:{action:"mark/closed"}},retrieveUnreadMessages:{method:"POST",isArray:true,params:{action:"retrieve/unreadmessages"},url:e.centerapipath+"messaging/:action"}});
return d
}]);
b.factory("prompt",function(){return prompt
});
b.factory("FranchiseConfig",["$resource","$rootScope",function(f,d){var e=f(d.centerapipath+"franchiseconfig/:action",{action:"@actionName"},{retrieveAllConfiguration:{method:"GET",params:{action:"retrieveallconfig"}},});
return e
}]);
a.bootstrap(b);
return b
});