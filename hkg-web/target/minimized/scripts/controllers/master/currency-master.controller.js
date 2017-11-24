define(["hkg","manageCurrencyMasterService"],function(a,b){a.register.controller("ManageCurrencyMaster",["$rootScope","$scope","$filter","ManageCurrencyMasterService",function(d,g,i,h){d.mainMenu="manageLink";
d.childMenu="manageReferenceRate";
g.entity="CURRENCY.";
var j=i("orderBy");
g.selectedEventCategoryDropdown={currentNode:""};
g.currencyMasterDataBean={};
g.currencyDropdown={};
g.tabManager={};
g.retrieveArchivedCurrency=function(l){var k=function(){var n="Failed to retrieve currency. Try again.";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading();
if(l){l()
}};
h.retrieveArchivedCurrency(function(m){g.tabManager.archivedList=[];
angular.forEach(m,function(o,n){var p={code:n,list:o};
if(n!=="$promise"&&n!=="$resolved"){g.tabManager.archivedList.push(p)
}});
if(l){l()
}},k)
};
g.retrieveCurrentCurrency=function(l){var k=function(){var n="Failed to retrieve currency. Try again.";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading();
if(l){l()
}};
h.retrieveCurrentCurrency(function(m){m=j(m,["-lastModifiedOn"]);
if(angular.isDefined(m)&&m!==null&&m.length>0){angular.forEach(m,function(n){n.applicableFrom=new Date(n.applicableFrom)
})
}g.currentCurrencyRateList=angular.copy(m);
g.currentCurrencyRateListFromDb=angular.copy(m);
g.currentRateListFilled=true;
d.unMaskLoading();
if(l){l()
}},k)
};
g.retrieveCurrencyDataBean=function(){var k=function(){var m="Failed to retrieve currency. Try again.";
var l=d.failure;
d.addMessage(m,l);
d.unMaskLoading()
};
g.listFilled=false;
h.retrieveCurrencyDataBean(function(l){g.currencyList=angular.copy(l);
g.currencyListFromDb=angular.copy(l);
g.listFilled=true;
d.unMaskLoading()
},k)
};
g.initCurrencyForm=function(k){g.currencyForm=k
};
g.initRefRateForm=function(k){g.referencerateForm=k
};
g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency();
g.isEdit=false;
g.setRootOption=function(){g.currencyDropdown.currentNode={id:"0",displayName:"Select"};
g.currencyId=g.currencyDropdown.currentNode.id
};
g.initializePage=function(){d.maskLoading();
if(g.referencerateform!=null){g.referencerateform.$dirty=false
}g.submitted=false;
var k=function(){var m="Failed to retrieve currency. Try again.";
var l=d.failure;
d.addMessage(m,l);
d.unMaskLoading()
};
h.retrieveCurrency(function(l){g.currencyCombo=l;
g.comboLabelAndDescriptionList=[];
g.comboLabelAndDescriptionList.unshift({id:"0",displayName:"Select"});
g.comboLabelAndDescriptionList=angular.copy(g.currencyCombo);
d.unMaskLoading()
},k)
};
g.tabChangeEvent=function(l){g.tabListFilled=false;
if(l!=null&&g.tabManager.archivedList!=null&&g.tabManager.archivedList.length>0){for(var k=0;
k<g.tabManager.archivedList.length;
k++){if(g.tabManager.archivedList[k].code===l.code){g.tabManager.tabItems=angular.copy((g.tabManager.archivedList[k]));
g.tabManager.tabItems.list=j(g.tabManager.tabItems.list,["-isActive"])
}}}g.tabListFilled=true
};
g.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate","MM/dd/yyyy"];
g.format=d.dateFormat;
g.datepicker={from:false,end:false};
g.open=function(k,l){k.preventDefault();
k.stopPropagation();
g.datepicker[l]=true
};
var f=d.getCurrentServerDate();
g.minDate=f;
g.referenceRate="";
g.$watch("referenceRate",function(n,l){var k=String(n).split("");
if(k.length===0){return
}if(k.length===1&&(k[0]===".")){return
}if(k.length===2&&n==="."){return
}if(isNaN(n)){if(n==undefined){g.referenceRate=n;
return
}g.referenceRate=l
}var m=String(n).split(".");
if(angular.isDefined(m[1])&&m[1].length>3){g.referenceRate=l;
return
}});
g.updateReferenceRate=function(){g.submitted=true;
if(angular.isDefined(g.currencyCombo)){for(var l=0;
l<g.currencyCombo.length;
l++){var n=g.currencyCombo[l];
for(var k in n){if(g.currency==n[k]){g.id=g.currencyCombo[l].value
}}}}var m={};
m={id:g.refId,applicableFrom:g.applicableFrom,referenceRate:g.referenceRate};
if(g.referencerateform.$valid){if(g.refId!=null&&g.isEdit){h.updateReferenceRate(m,function(o){g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency();
g.submitted=false;
g.onCanelOfReferencePage(g.referencerateform)
})
}else{m.code=g.currencyId;
h.addReferenceRate(m,function(o){g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency();
g.submitted=false;
g.onCanelOfReferencePage(g.referencerateform)
})
}}};
g.retrieveCurrenciesForCombo=function(k){var l=function(){var n="Failed to retrieve currency. Try again.";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading()
};
h.retrieveCurrencies(function(m){g.eventCategoriesTemp=angular.copy(m);
g.eventCategoryListDropdown=[];
if(g.eventCategoriesTemp!=null&&angular.isDefined(g.eventCategoriesTemp)&&g.eventCategoriesTemp.length>0){angular.forEach(g.eventCategoriesTemp,function(n){n.id=n.value;
n.displayName=n.label;
g.eventCategoryListDropdown.push(n)
})
}if(k===undefined||k===null){g.eventCategoryListDropdown.unshift({id:"D",displayName:"Select"});
g.selectedEventCategoryDropdown.currentNode=g.eventCategoryListDropdown[0];
g.eventCategoryId="D"
}},l)
};
g.initCurrency=function(k){g.submitted=false;
if(k!=null){k.$setPristine();
if(g.currencyMasterDataBean!=null){g.currencyMasterDataBean.symbolPosition=g.positon
}}else{g.currencyMasterDataBean={};
g.currencyMasterDataBean.symbolPosition=false
}if(g.currencyForm!=null){g.currencyForm.$dirty=false
}g.retrieveCurrenciesForCombo(k);
g.retrieveCurrencyDataBean();
if(g.listFilled){}};
g.setCurrencyOperation=function(){d.maskLoading();
g.showCurrencyPage=true;
g.initCurrency(undefined)
};
g.currencyDropdownClick=function(k){g.popupEventCategoryId=k.currentNode;
g.eventCategoryId=g.popupEventCategoryId.id;
if(g.eventCategoryId==="INR"){g.currencyMasterDataBean.lastReferenceRate=1
}};
g.checkPattern=function(k){var l="^[#.,]*$";
if(k!=null&&!k.match(l)){if(g.currencyMasterDataBean.format.length>1){g.currencyMasterDataBean.format=g.currencyMasterDataBean.format.substring(0,g.currencyMasterDataBean.format.length-1)
}else{g.currencyMasterDataBean.format=null
}}};
var e=function(k){g.currencyMasterDataBean.id=k
};
g.createCurrency=function(k){g.submitted=true;
if(k.$valid){if(g.currencyMasterDataBean!=null){if(g.currencyMasterDataBean.id!=null){g.updateCurrency(k)
}else{g.currencyMasterDataBean.code=g.popupEventCategoryId.id;
g.currencyMasterDataBean.symbol=g.popupEventCategoryId.description;
if(g.currencyMasterDataBean.symbolPosition){g.positon=true;
g.currencyMasterDataBean.symbolPosition="P"
}else{g.positon=false;
g.currencyMasterDataBean.symbolPosition="S"
}var l=function(){var n="Currency could not be added, please try again";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading()
};
d.maskLoading();
h.create(g.currencyMasterDataBean,function(m){if(g.currencyForm!=null){g.currencyForm.$dirty=false
}g.initCurrency(k);
g.retrieveCurrentCurrency(function(n){g.retrieveArchivedCurrency(function(o){if(m!==undefined&&m!==null){e(m.primaryKey)
}})
})
},l)
}}}};
g.editCurrency=function(k){g.currencyMasterDataBean={};
g.currencyMasterDataBean=angular.copy(k);
if(k!=null){d.maskLoading();
g.currencyMasterDataBean.symbolPosition=false;
if(k.symbolPosition==="P"){g.currencyMasterDataBean.symbolPosition=true
}g.selectedEventCategoryDropdown.currentNode.id=k.code;
g.selectedEventCategoryDropdown.currentNode.displayName=k.currencyName;
g.eventCategoryId=k.code;
d.unMaskLoading()
}};
g.onCanel=function(k){g.submitted=false;
$(document).find("#referenceRate").val("");
$(document).find("#format").val("");
if(k!=null){k.$setPristine()
}if(g.referencerateform!==undefined&&g.referencerateform!==null){g.referencerateform.$setPristine()
}g.currencyMasterDataBean={};
g.currencyMasterDataBean.lastReferenceRate=undefined;
g.currencyMasterDataBean.format=undefined;
g.showCurrencyPage=false;
d.maskLoading();
g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency()
};
g.onCanelOfReferencePage=function(k){if(k!=null){k.$setPristine()
}g.currencyDropdown={};
g.currencyId=undefined;
g.referenceRate=undefined;
g.applicableFrom=undefined;
g.refId=undefined;
g.isEdit=false;
g.initializePage()
};
var c=function(){d.maskLoading();
if(angular.isDefined(g.currentCurrencyRateListFromDb)&&g.currentCurrencyRateListFromDb!==null&&g.currentCurrencyRateListFromDb.length>0){if(g.currencyMasterDataBean!==undefined&&g.currencyMasterDataBean!=null){angular.forEach(g.currentCurrencyRateListFromDb,function(k){if(k.currencyId===g.currencyMasterDataBean.id){g.currentReferenceRateObject=k
}})
}}g.currencyMasterDataBean={};
g.showCurrencyPage=false;
if(g.currentReferenceRateObject!==undefined&&g.currencyMasterDataBean!==null){g.isEdit=true;
g.refId=g.currentReferenceRateObject.id;
g.applicableFrom=g.currentReferenceRateObject.applicableFrom;
g.minDate=g.currentReferenceRateObject.applicableFrom;
g.referenceRate=g.currentReferenceRateObject.referenceRate;
g.currencyId=g.currentReferenceRateObject.currencyId;
if(angular.isDefined(g.currencyDropdown.currentNode)){g.currencyDropdown.currentNode.displayName=g.currentReferenceRateObject.code+"("+g.currentReferenceRateObject.currencyName+")";
g.currencyDropdown.currentNode.id=g.currentReferenceRateObject.currencyId
}}d.unMaskLoading()
};
g.showPopUp=function(k){if(k!=null&&k.$valid){k.$setPristine();
g.detailChanged=false;
var l=false;
if(g.currencyMasterDataBean!=null&&g.currencyListFromDb!=null&&g.currencyListFromDb.length>0){angular.forEach(g.currencyListFromDb,function(n){if(g.currencyMasterDataBean.id!=null&&n.id===g.currencyMasterDataBean.id){var m="S";
if(g.currencyMasterDataBean.symbolPosition){m="P"
}if(g.currencyMasterDataBean.format!==n.format||m!==n.symbolPosition){l=true
}}})
}if(l){g.detailChanged=true;
$("#confirmationPopUp").modal("show")
}else{c()
}}};
g.hideConfirmationPopUp=function(){g.submitted=false;
if(g.referencerateform!==undefined&&g.referencerateform!==null){g.referencerateform.$setPristine()
}c();
$("#confirmationPopUp").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
g.updateCurrency=function(k){$("#confirmationPopUp").modal("hide");
d.removeModalOpenCssAfterModalHide();
g.submitted=true;
if(k.$valid){if(g.currencyMasterDataBean!=null){if(g.currencyMasterDataBean.symbolPosition){g.positon=true;
g.currencyMasterDataBean.symbolPosition="P"
}else{g.positon=false;
g.currencyMasterDataBean.symbolPosition="S"
}}var l=function(){var n="Failed to add currency. Try again.";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading()
};
h.update(g.currencyMasterDataBean,function(m){if(g.currencyForm!=null){g.currencyForm.$dirty=false
}if(g.detailChanged){g.showCurrencyPage=false;
c()
}else{if(k!=null){k.$dirty=false;
k.$setPristine()
}g.initCurrency(k);
g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency()
}},l)
}};
g.showdeletePopUp=function(k){g.row=k;
$("#deletePopUp").modal("show")
};
g.disableCurrency=function(){$("#deletePopUp").modal("hide");
d.removeModalOpenCssAfterModalHide();
var l={};
if(g.row!=null&&g.row.id!=null){l.primaryKey=g.row.id
}var k=function(){var n="Failed to disable. Try again.";
var m=d.failure;
d.addMessage(n,m);
d.unMaskLoading()
};
h.deleteById(l,function(m){g.row=undefined;
g.retrieveArchivedCurrency();
g.retrieveCurrentCurrency()
},k)
};
g.hidedeletePopUp=function(){$("#deletePopUp").modal("hide");
d.removeModalOpenCssAfterModalHide()
};
g.currencyRateDropdownClick=function(k,l){};
g.editReferenceRate=function(k){g.isEdit=true;
if(k!=null){g.refId=k.id;
g.applicableFrom=k.applicableFrom;
g.minDate=d.getCurrentServerDate();
g.referenceRate=k.referenceRate;
g.currencyId=k.code
}}
}])
});