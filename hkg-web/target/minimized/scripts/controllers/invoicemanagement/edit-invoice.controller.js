define(["hkg","invoiceService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a,b){a.register.controller("EditInvoiceController",["$rootScope","$scope","InvoiceService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService","$interval","$q",function(l,p,m,e,j,g,c,o,n,i,h){l.mainMenu="stockLink";
l.childMenu="editInvoice";
p.invoiceLabelListForUiGrid=[];
p.searchedDataFromDbForUiGrid=[];
l.activateMenu();
p.invoiceDataBean={};
p.searchCustom=o.resetSection(p.generalInvoiceTemplate);
var f=o.retrieveSearchWiseCustomFieldInfo("invoiceEdit");
p.flag={};
p.flag.searchFieldNotAvailable=false;
p.flag.showUpdatePage=false;
p.dbType={};
var k={};
p.invoiceDataBean.invoiceDbType=angular.copy(p.dbType);
f.then(function(q){p.generalInvoiceTemplate=[];
p.generalInvoiceTemplate=q.genralSection;
if(p.generalInvoiceTemplate===undefined||p.generalInvoiceTemplate.length===0){p.flag.searchFieldNotAvailable=true
}p.generalTemplate=angular.copy(q.genralSection);
p.invoiceLabelList=[];
p.dbFieldNames=[];
angular.forEach(p.generalTemplate,function(r){p.invoiceLabelList.push({model:r.model,label:r.label});
if(r.fromModel){p.invoiceLabelListForUiGrid.push({name:r.fromModel,displayName:r.label,minWidth:200});
p.dbFieldNames.push(r.fromModel)
}else{if(r.toModel){p.dbFieldNames.push(r.toModel);
p.invoiceLabelListForUiGrid.push({name:r.toModel,displayName:r.label,minWidth:200})
}else{if(r.model){p.dbFieldNames.push(r.model);
p.invoiceLabelListForUiGrid.push({name:r.model,displayName:r.label,minWidth:200})
}}}});
p.searchResetFlag=true;
p.dataRetrieved=true
},function(q){},function(q){});
var d=o.retrieveSectionWiseCustomFieldInfo("invoice");
d.then(function(q){p.invoiceCustomData=angular.copy(q.genralSection)
});
p.initEditInvoiceForm=function(q){p.editInvoiceForm=q
};
p.initUpdateInvoiceForm=function(q){p.updateInvoiceForm=q
};
p.retrieveSearchedData=function(){p.selectOneParameter=false;
p.searchedData=[];
p.listFilled=false;
if(Object.getOwnPropertyNames(p.searchCustom).length>0){p.gridOptions={};
p.gridOptions.data=[];
p.searchedDataFromDbForUiGrid=[];
var s=false;
p.searchFinal=angular.copy(p.searchCustom);
for(var v in p.searchFinal){if(typeof p.searchFinal[v]==="object"&&p.searchFinal[v]!=null){var t=angular.copy(p.searchFinal[v].toString());
if(typeof t==="string"&&t!==null&&t!==undefined&&t.length>0){s=true;
break
}}if(typeof p.searchFinal[v]==="string"&&p.searchFinal[v]!==null&&p.searchFinal[v]!==undefined&&p.searchCustom[v].length>0){s=true;
break
}if(typeof p.searchFinal[v]==="number"&&!!(p.searchFinal[v])){s=true;
break
}if(typeof p.searchFinal[v]==="boolean"){s=true;
break
}}if(s){l.maskLoading();
var q=o.convertSearchData(p.invoiceCustomData,null,null,null,p.searchFinal);
p.invoiceDataBean.invoiceCustom=angular.copy(q);
p.invoiceDataBean.dbFieldNames=angular.copy(p.dbFieldNames);
p.invoiceDataBean.searchOnParameter=true;
m.search(p.invoiceDataBean,function(w){var x=o.retrieveSectionWiseCustomFieldInfo("invoice");
x.then(function(y){p.gridOptions.enableFiltering=true;
p.gridOptions.multiSelect=false;
p.gridOptions.enableRowSelection=true;
p.gridOptions.enableSelectAll=false;
p.selectedInvoice=[];
p.gridOptions.onRegisterApi=function(A){p.gridApi=A;
A.selection.on.rowSelectionChanged(p,function(B){if(p.selectedInvoice.length>0){$.each(p.selectedInvoice,function(D,C){if(C["$$hashKey"]===B.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedInvoice.splice(D,1);
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedInvoice.push(B.entity)
}}else{p.selectedInvoice.push(B.entity)
}if(p.selectedInvoice.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}});
A.selection.on.rowSelectionChangedBatch(p,function(B){if(p.selectedInvoice.length>0){angular.forEach(B,function(C){$.each(p.selectedInvoice,function(E,D){if(D["$$hashKey"]===C.entity["$$hashKey"]){p.flag.repeatedRow=true;
p.selectedInvoice=[];
return false
}else{p.flag.repeatedRow=false
}});
if(!p.flag.repeatedRow){p.selectedInvoice.push(C.entity)
}})
}else{angular.forEach(B,function(C){p.selectedInvoice.push(C.entity)
})
}if(p.selectedInvoice.length>0){p.flag.rowSelectedflag=true
}else{p.flag.rowSelectedflag=false
}})
};
p.invoiceTemplate=y.genralSection;
p.searchedDataFromDb=angular.copy(w);
var z=function(A){p.searchedData=angular.copy(A);
p.searchedDataFromDbForUiGrid=[];
p.mapToArray=[];
angular.forEach(p.searchedData,function(B){angular.forEach(p.invoiceLabelListForUiGrid,function(C){if(!B.categoryCustom.hasOwnProperty(C.name)){B.categoryCustom[C.name]="NA"
}else{if(B.categoryCustom.hasOwnProperty(C.name)){if(B.categoryCustom[C.name]===null||B.categoryCustom[C.name]===""||B.categoryCustom[C.name]===undefined){B.categoryCustom[C.name]="NA"
}}}B.categoryCustom["$$$value"]=B.value
});
p.searchedDataFromDbForUiGrid.push(B.categoryCustom)
});
p.gridOptions.data=p.searchedDataFromDbForUiGrid;
p.gridOptions.columnDefs=p.invoiceLabelListForUiGrid;
window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
p.listFilled=true;
l.unMaskLoading()
};
o.convertorForCustomField(w,z)
},function(y){},function(y){})
},function(){var x="Could not retrieve, please try again.";
var w=l.error;
l.addMessage(x,w);
l.unMaskLoading()
})
}else{var u="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(u,r)
}}else{var u="Please select atleast one search criteria for search";
var r=l.warning;
l.addMessage(u,r)
}};
p.editInvoice=function(){p.parcelAddShow=false;
var q=angular.copy(p.selectedInvoice[0]);
p.invoiceId=angular.copy(q.$$$value);
p.invoiceIdForConstraint=q.$$$value;
if(q!=null){l.maskLoading();
angular.forEach(p.searchedDataFromDb,function(r){if(r.value===q.$$$value){q=angular.copy(r.categoryCustom)
}});
p.flag.showUpdatePage=true;
n.retrieveDesignationBasedFields("invoiceEdit",function(r){p.response=angular.copy(r);
p.invoiceDataBean={};
p.invoiceCustom=o.resetSection(p.updateInvoiceTemplate);
var s=o.retrieveSectionWiseCustomFieldInfo("invoice");
if(!(p.dbTypeForUpdate!=null)){p.dbTypeForUpdate={}
}s.then(function(x){p.updateInvoiceTemplate=null;
p.updateInvoiceTemplate=x.genralSection;
p.listOfModelsOfDateType=[];
if(p.updateInvoiceTemplate!==null&&p.updateInvoiceTemplate!==undefined){angular.forEach(p.updateInvoiceTemplate,function(z){if(z.type!==null&&z.type!==undefined&&z.type==="date"){p.listOfModelsOfDateType.push(z.model)
}})
}var v=[];
var t=Object.keys(r).map(function(z,A){angular.forEach(this[z],function(B){if(z==="Invoice"){v.push({Invoice:B})
}})
},r);
p.editFieldName=[];
p.updateInvoiceTemplate=o.retrieveCustomData(p.updateInvoiceTemplate,v);
angular.forEach(p.updateInvoiceTemplate,function(z){if(z.fromModel){if(p.editFieldName.indexOf(z.fromModel)===-1){p.editFieldName.push(z.fromModel)
}}else{if(z.toModel){if(p.editFieldName.indexOf(z.toModel)===-1){p.editFieldName.push(z.toModel)
}}else{if(z.model){if(p.editFieldName.indexOf(z.model)===-1){p.editFieldName.push(z.model)
}}}}});
var y=o.retrieveSectionWiseCustomFieldInfo("invoice");
p.dbTypeForUpdateParent={};
y.then(function(C){p.updateInvoiceParentTemplate=null;
p.updateInvoiceParentTemplate=C.genralSection;
var B=angular.copy(C.genralSection);
var A=[];
var z=Object.keys(r).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Invoice#P#"){A.push({Invoice:F})
}})
},r);
p.updateInvoiceParentTemplate=o.retrieveCustomData(p.updateInvoiceParentTemplate,A);
p.parcelAddShow=true;
angular.forEach(p.updateInvoiceParentTemplate,function(D){if(D.fromModel){if(p.editFieldName.indexOf(D.fromModel)===-1){p.editFieldName.push(D.fromModel)
}}else{if(D.toModel){if(p.editFieldName.indexOf(D.toModel)===-1){p.editFieldName.push(D.toModel)
}}else{if(D.model){if(p.editFieldName.indexOf(D.model)===-1){p.editFieldName.push(D.model)
}}}}});
p.flag.customFieldGeneratedForUpdate=true
},function(z){},function(z){});
var w={};
var u=[];
u.push(p.invoiceId);
w.invoiceObjectId=u;
w.dbFieldNames=p.editFieldName;
m.retrieveInvoice(w,function(z){p.invoiceEditShow=true;
p.flag.customFieldGeneratedForUpdate=true;
if(z!==undefined){p.invoiceCustom=angular.copy(z.categoryCustom);
if(!!p.invoiceCustom){angular.forEach(p.listOfModelsOfDateType,function(A){if(p.invoiceCustom.hasOwnProperty(A)){if(p.invoiceCustom[A]!==null&&p.invoiceCustom[A]!==undefined){p.invoiceCustom[A]=new Date(p.invoiceCustom[A])
}else{p.invoiceCustom[A]=""
}}})
}p.invoiceCustomForUpdate=angular.copy(z.categoryCustom);
p.parentEntityValue=angular.copy(z.categoryCustom)
}p.invoiceDataBean.id=angular.copy(p.invoiceId);
p.invoiceDataBean.invoiceDbType=angular.copy(p.dbTypeForUpdate);
l.unMaskLoading()
},function(){var A="Could not save, please try again.";
var z=l.error;
l.addMessage(A,z);
l.unMaskLoading()
})
},function(t){},function(t){})
},function(){l.unMaskLoading();
var s="Failed to retrieve data";
var r=l.error;
l.addMessage(s,r)
})
}};
p.reset=function(r){if(r==="searchCustom"){p.searchCustom={};
var q=o.retrieveSearchWiseCustomFieldInfo("invoiceEdit");
q.then(function(s){p.generalInvoiceTemplate=s.genralSection;
p.searchResetFlag=true;
l.unMaskLoading();
p.flag.customFieldGenerated=true
},function(s){},function(s){})
}else{if(r==="updateInvoiceCustom"){p.invoiceCustom={};
var q=o.retrieveSectionWiseCustomFieldInfo("invoice");
q.then(function(u){p.updateInvoiceTemplate=angular.copy(u.genralSection);
var t=[];
var s=Object.keys(p.response).map(function(v,w){angular.forEach(this[v],function(x){if(v==="Invoice#P#"){t.push({Invoice:x})
}})
},p.response);
p.updateInvoiceTemplate=[];
p.updateInvoiceTemplate=angular.copy(u.genralSection);
p.updateInvoiceTemplate=o.retrieveCustomData(p.updateInvoiceTemplate,t);
p.invoiceEditShow=true;
l.unMaskLoading();
p.flag.customFieldGenerated=true
},function(s){},function(s){})
}}};
p.onCanel=function(){if(p.updateInvoiceForm!=null){p.updateInvoiceForm.$dirty=false
}p.flag.showUpdatePage=false;
p.invoiceEditShow=false;
p.flag.showUpdatePage=false;
p.selectedInvoice=[];
p.reset("updateInvoiceCustom")
};
p.onCanelOfSearch=function(){if(p.editInvoiceForm!=null){p.editInvoiceForm.$dirty=false
}p.searchedData=[];
p.listFilled=false;
p.submitted=false;
p.flag.showUpdatePage=false;
p.searchResetFlag=false;
p.selectedInvoice=[];
p.gridOptions={};
p.reset("searchCustom")
};
p.updateInvoice=function(q){angular.forEach(p.listOfModelsOfDateType,function(r){if(p.invoiceCustom.hasOwnProperty(r)){if(p.invoiceCustom[r]!==null&&p.invoiceCustom[r]!==undefined){p.invoiceCustom[r]=new Date(p.invoiceCustom[r])
}else{p.invoiceCustom[r]=""
}}});
p.submitted=true;
p.invoiceDataBean.invoiceCustom=p.invoiceCustom;
p.invoiceDataBean.id=p.invoiceId;
p.invoiceDataBean.invoiceDbType=p.dbTypeForUpdate;
p.invoiceDataBean.isArchive=false;
if(q.$valid){l.maskLoading();
m.update(p.invoiceDataBean,function(r){if(p.editInvoiceForm!=null){p.editInvoiceForm.$dirty=false
}p.onCanelOfSearch()
},function(){l.unMaskLoading();
var s="Could not update invoice, please try again.";
var r=l.error;
l.addMessage(s,r)
})
}};
p.showPopUp=function(q){$("#deleteInvoiceDialog").modal("show")
};
p.hidePopUp=function(){$("#deleteInvoiceDialog").modal("hide");
l.removeModalOpenCssAfterModalHide()
};
p.deleteInvoice=function(q){p.invoiceDataBean.id=p.invoiceId;
p.invoiceDataBean.isArchive=true;
l.maskLoading();
$("#deleteInvoiceDialog").modal("hide");
l.removeModalOpenCssAfterModalHide();
m.update(p.invoiceDataBean,function(r){if(p.editInvoiceForm!=null){p.editInvoiceForm.$dirty=false
}p.onCanelOfSearch()
},function(){l.unMaskLoading();
var s="Could not update invoice, please try again.";
var r=l.error;
l.addMessage(s,r)
})
}
}])
});