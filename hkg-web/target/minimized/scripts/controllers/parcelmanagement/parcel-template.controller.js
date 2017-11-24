define(["hkg","ngload!uiGrid","parcelService","ParcelTemplateService"],function(a){a.register.controller("ParcelTemplateController",["$scope","$rootScope","ParcelService","DynamicFormService","CustomFieldService","ParcelTemplateService","$timeout",function(n,j,g,m,l,d,c){n.editableParcelRows=[];
n.allParcelIds=[];
n.editableFieldNames=["Stock Carat","Stock Pieces","exchangeRate","origAmountInDollar$FRM$Double","amountInRs","origRateInDollar$NM$Double","parcelId$AG$String"];
n.entityParcelTemplate=d.getEntityName();
n.tableFooterData={};
n.searchedDataFromDbForUiGrid=[];
n.gridOptions={enableRowSelection:true,enableSelectAll:false,enableFiltering:true,multiSelect:true,data:[]};
n.flag={};
n.selectedInvoice=[];
n.gridOptions.onRegisterApi=function(o){n.gridApi=o;
o.selection.on.rowSelectionChanged(n,function(p){if(p.isSelected){f(p.entity)
}else{e(p.entity)
}n.allParcelIds=[];
angular.forEach(o.selection.getSelectedRows(),function(q){n.allParcelIds.push(q.$$parcelId$$)
});
n.refreshFooterData()
})
};
var i=function(r){var p={};
var q;
for(var o in n.editableFieldNames){q=n.editableFieldNames[o];
p[q]=r[q]
}p.value=r["$$$value"];
if(!!d.getSelectedParcels()&&d.getSelectedParcels()[p.value]!=null){var s=d.getSelectedParcels()[p.value];
p["Stock Carat"]=s.carat;
p["Stock Pieces"]=s.pieces;
p.exchangeRate=s.exRate;
p.oldPieces=r["Stock Pieces"]+p["Stock Pieces"];
p.oldCarat=r["Stock Carat"]+p["Stock Carat"]
}else{p.oldPieces=r["Stock Pieces"];
p.oldCarat=r["Stock Carat"]
}return p
};
var f=function(p){var o=i(p);
n.calculateAmounts(o);
n.editableParcelRows.push(o)
};
var e=function(p){var o=i(p);
n.editableParcelRows.splice(n.editableParcelRows.indexOf(o))
};
n.calculateAmounts=function(p,o){if(o!=null){if(!o.carat.$error.min&&!o.carat.$error.max&&!o.carat.$error.required&&!o.pieces.$error.min&&!o.pieces.$error.max&&!o.pieces.$error.required){if(h(p,o)){k(p)
}}}else{k(p)
}};
var h=function(p,o){var r=p.oldPieces-p["Stock Pieces"];
var q=p.oldCarat-p["Stock Carat"];
if((r==0&&q!=0)||(r!=0&&q==0)){o.carat.$setValidity("caratpieces",false);
o.pieces.$setValidity("caratpieces",false);
return false
}else{o.carat.$setValidity("caratpieces",true);
o.pieces.$setValidity("caratpieces",true);
return true
}};
var k=function(o){if(!!o["Stock Carat"]&&!!o["origRateInDollar$NM$Double"]){o["origAmountInDollar$FRM$Double"]=o["Stock Carat"]*o["origRateInDollar$NM$Double"]
}if(!!o.exchangeRate&&!!o["origAmountInDollar$FRM$Double"]){o.amountInRs=o.exchangeRate*o["origAmountInDollar$FRM$Double"]
}n.refreshFooterData()
};
n.$watch("editableParcelRows",function(){b()
},true);
var b=function(){var q={};
var o={roughStock:[],footerData:{}};
for(var p=0;
p<n.editableParcelRows.length;
p++){q={};
q.changedPieces=n.editableParcelRows[p]["Stock Pieces"];
q.changedCarat=n.editableParcelRows[p]["Stock Carat"];
q.changedRate=n.editableParcelRows[p]["origRateInDollar$NM$Double"];
q.changedExchangeRate=n.editableParcelRows[p]["exchangeRate"];
q.changedAmountInDollar=n.editableParcelRows[p]["origAmountInDollar$FRM$Double"];
q.changedAmountInRs=n.editableParcelRows[p]["amountInRs"];
q.parcel=n.editableParcelRows[p]["value"];
o.roughStock.push(q)
}o.footerData.changedPieces=n.tableFooterData["Stock Pieces"];
o.footerData.changedCarat=n.tableFooterData["Stock Carat"];
o.footerData.changedRate=n.tableFooterData["origRateInDollar$NM$Double"];
o.footerData.changedExchangeRate=n.tableFooterData.exchangeRate;
o.footerData.changedAmountInDollar=n.tableFooterData["origAmountInDollar$FRM$Double"];
o.footerData.changedAmountInRs=n.tableFooterData.amountInRs;
d.setSelectedParcelData(o)
};
n.sellParcelScreenRule=function(q,p){var o;
if(!!q.entity.screenRuleDetailsWithDbFieldName&&q.entity.screenRuleDetailsWithDbFieldName[p]!==undefined&&q.entity.screenRuleDetailsWithDbFieldName[p]!==null){if(n.allParcelIds.length===0||(n.allParcelIds.indexOf(q.entity.$$parcelId$$)===-1)){o=q.entity.screenRuleDetailsWithDbFieldName[p].colorCode
}}return o
};
n.parcelLabelListForUiGrid=[];
n.retrieveParcels=function(){j.maskLoading();
n.parameters=[d.getFeatureName(),"GEN"];
l.retrieveDesignationBasedFieldsBySection(n.parameters,function(o){n.response=angular.copy(o);
var p=m.retrieveSectionWiseCustomFieldInfo("parcel");
if(!(n.dbTypeForUpdate!==null)){n.dbTypeForUpdate={}
}p.then(function(x){n.parcelCommonTemplate=null;
n.parcelCommonTemplate=x.genralSection;
n.listOfModelsOfDateType=[];
var t=[];
var r=Object.keys(o).map(function(y,z){angular.forEach(this[y],function(A){if(y==="Parcel"){t.push({Parcel:A})
}})
},o);
n.editFieldName=[];
n.fieldIdNameMap={};
n.parcelCommonTemplate=m.retrieveCustomData(n.parcelCommonTemplate,t);
angular.forEach(n.parcelCommonTemplate,function(y){if(y.fromModel){n.fieldIdNameMap[y.fieldId]=y.fromModel;
if(n.editFieldName.indexOf(y.fromModel)===-1){n.editFieldName.push(y.fromModel)
}}else{if(y.toModel){n.fieldIdNameMap[y.fieldId]=y.toModel;
if(n.editFieldName.indexOf(y.toModel)===-1){n.editFieldName.push(y.toModel)
}}else{if(y.model){n.fieldIdNameMap[y.fieldId]=y.model;
if(n.editFieldName.indexOf(y.model)===-1){n.editFieldName.push(y.model)
}}}}});
n.fieldNotConfigured=false;
n.showConfigMsg=false;
var q=[];
n.mandatoryFields=["origAmountInDollar$FRM$Double","origRateInDollar$NM$Double","parcelId$AG$String"];
if(n.parcelCommonTemplate!==undefined&&n.parcelCommonTemplate!==null&&n.parcelCommonTemplate.length>0){for(var u=0;
u<n.parcelCommonTemplate.length;
u++){q.push(angular.copy(n.parcelCommonTemplate[u].model))
}}if(q.length>0&&n.mandatoryFields!=null&&n.mandatoryFields.length>0){for(var w=0;
w<n.mandatoryFields.length;
w++){if(q.indexOf(n.mandatoryFields[w])===-1){n.fieldNotConfigured=true;
break
}}}else{n.fieldNotConfigured=true
}if(n.fieldNotConfigured){n.showConfigMsg=true
}angular.forEach(n.parcelCommonTemplate,function(y){if(y.fromModel){n.parcelLabelListForUiGrid.push({name:y.fromModel,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellParcelScreenRule(row, '"+y.fromModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(y.toModel){n.parcelLabelListForUiGrid.push({name:y.toModel,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellParcelScreenRule(row, '"+y.toModel+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}else{if(y.model){n.parcelLabelListForUiGrid.push({name:y.model,displayName:y.label,minWidth:200,cellTemplate:"<div class=\"ui-grid-cell-contents\" ng-style=\"{ 'background-color': grid.appScope.sellParcelScreenRule(row, '"+y.model+'\') }" title="TOOLTIP"> {{COL_FIELD CUSTOM_FILTERS}} </div>'})
}}}});
n.map={};
n.map.dbFieldNames=n.editFieldName;
var v=[];
var s=d.getSelectedParcels();
angular.forEach(s,function(z,y){v.push(y)
});
n.map.parcelIds=v;
n.parcelDataBean={};
n.parcelDataBean.featureDbFieldMap=n.map;
n.parcelDataBean.ruleConfigMap={fieldIdNameMap:n.fieldIdNameMap,featureName:"roughSale"};
if(n.editFieldName.length>0){g.retrieveAllParcels(n.parcelDataBean,function(y){if(!(y!==undefined&&y!==null&&y.length>0)){n.searchedDataFromDbForUiGrid=undefined;
j.unMaskLoading()
}var z=function(A){n.parcelData=angular.copy(A);
n.parcelLabelListForUiGrid.push({name:"Stock Carat",displayName:"Stock Carat",minWidth:200},{name:"Stock Pieces",displayName:"Stock Pieces",minWidth:200});
n.searchedDataFromDbForUiGrid=[];
angular.forEach(n.parcelData,function(B){angular.forEach(n.parcelLabelListForUiGrid,function(C){if(!B.categoryCustom.hasOwnProperty(C.name)){B.categoryCustom[C.name]="NA"
}else{if(B.categoryCustom.hasOwnProperty(C.name)){if(B.categoryCustom[C.name]===null||B.categoryCustom[C.name]===""||B.categoryCustom[C.name]===undefined){B.categoryCustom[C.name]="NA"
}}}B.categoryCustom["$$$value"]=B.value
});
B.categoryCustom.$$parcelId$$=B.value;
B.categoryCustom.screenRuleDetailsWithDbFieldName=B.screenRuleDetailsWithDbFieldName;
n.searchedDataFromDbForUiGrid.push(B.categoryCustom)
});
if(n.searchedDataFromDbForUiGrid.length===0){n.searchedDataFromDbForUiGrid=undefined
}n.gridOptions.data=n.searchedDataFromDbForUiGrid;
n.gridOptions.columnDefs=n.parcelLabelListForUiGrid;
if(!!d.getSelectedParcels()&&Object.keys(d.getSelectedParcels()).length>0){c(function(){for(var C in d.getSelectedParcels()){for(var B=0;
B<n.gridOptions.data.length;
B++){if(n.gridOptions.data[B]["$$$value"]==C){n.gridApi.selection.selectRow(n.gridOptions.data[B]);
n.gridOptions.data[B]["Stock Carat"]+=d.getSelectedParcels()[C].carat;
n.gridOptions.data[B]["Stock Pieces"]+=d.getSelectedParcels()[C].pieces;
n.gridOptions.data[B]["exchangeRate"]+=d.getSelectedParcels()[C].exRate;
break
}}}})
}window.setTimeout(function(){$(window).resize();
$(window).resize()
},100);
j.unMaskLoading()
};
m.convertorForCustomField(y,z)
},function(){var z="Could not save, please try again.";
var y=j.error;
j.addMessage(z,y);
j.unMaskLoading()
})
}},function(q){},function(q){})
},function(){j.unMaskLoading();
var p="Failed to retrieve data";
var o=j.error;
j.addMessage(p,o)
})
};
n.refreshFooterData=function(){var o={};
for(var p=0;
p<n.editableFieldNames.length;
p++){o[n.editableFieldNames[p]]=0
}for(var q=0;
q<n.editableParcelRows.length;
q++){for(var p=0;
p<n.editableFieldNames.length;
p++){if(n.editableParcelRows[q][n.editableFieldNames[p]]==undefined||n.editableParcelRows[q][n.editableFieldNames[p]]==null){n.editableParcelRows[q][n.editableFieldNames[p]]=0
}o[n.editableFieldNames[p]]=Number(o[n.editableFieldNames[p]]);
if(n.editableFieldNames[p]!="parcelId$AG$String"){n.editableParcelRows[q][n.editableFieldNames[p]]=Number(n.editableParcelRows[q][n.editableFieldNames[p]])
}o[n.editableFieldNames[p]]+=n.editableParcelRows[q][n.editableFieldNames[p]]
}}n.tableFooterData=o;
if(n.editableParcelRows.length>0){n.tableFooterData["origRateInDollar$NM$Double"]=n.tableFooterData["origRateInDollar$NM$Double"]/n.editableParcelRows.length;
n.tableFooterData.exchangeRate=n.tableFooterData.exchangeRate/n.editableParcelRows.length
}};
n.retrieveParcels()
}])
});