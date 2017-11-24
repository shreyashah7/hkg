define(["hkg","customFieldService","printService","ruleService","activityFlowService","lotService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm","accordionCollapse"],function(a){a.register.controller("PrintStaticController",["$rootScope","$scope","DynamicFormService","PrintService","RuleService","ActivityFlowService","CustomFieldService","LotService","$filter",function(m,p,o,c,f,j,n,k,h){m.maskLoading();
var b=o.retrieveSectionWiseCustomFieldInfo("invoice");
b.then(function(q){p.invoiceCustomData=angular.copy(q.genralSection)
});
var d=o.retrieveSectionWiseCustomFieldInfo("parcel");
d.then(function(q){p.parcelCustomData=angular.copy(q.genralSection)
});
var e=o.retrieveSectionWiseCustomFieldInfo("lot");
e.then(function(q){p.lotCustomData=angular.copy(q.genralSection)
});
var i=o.retrieveSectionWiseCustomFieldInfo("packet");
i.then(function(q){p.packetCustomData=angular.copy(q.genralSection)
});
p.selectOneParameter=false;
var l={};
var g={};
p.initializeData=function(){p.gridOptions={};
p.gridOptions.enableFiltering=true;
p.gridOptions.multiSelect=false;
p.gridOptions.columnDefs=[];
p.gridOptions.data=[];
p.gridOptions.onRegisterApi=function(r){p.gridApi=r;
r.selection.on.rowSelectionChanged(p,function(w){if(p.gridApi.selection.getSelectedRows().length>0){var u=p.gridApi.selection.getSelectedRows()[0];
var s=u["~@index"];
for(var t=0;
t<p.searchedData.length;
t++){if(p.searchedData[t].categoryCustom["~@index"]===s){var v=p.searchedData[t];
p.toPrint=v;
break
}}}else{delete p.toPrint
}})
};
p.printDataBean={};
p.submitted=false;
p.printdataflag=false;
p.searchedData=[];
p.searchedDataFromDb=[];
p.listFilled=false;
p.lotListTodisplay=[];
p.searchResetFlag=false;
p.reset();
var q=o.retrieveSearchWiseCustomFieldInfo("staticPrint");
p.flag={};
p.dbType={};
p.modelAndHeaderList=[];
p.modelAndHeaderListForLot=[];
q.then(function(x){var s=[];
var w=[];
var v=[];
var r=[];
p.searchInvoiceTemplate=[];
p.searchParcelTemplate=[];
p.searchLotTemplate=[];
p.searchPacketTemplate=[];
p.generalSearchTemplate=x.genralSection;
if(p.generalSearchTemplate!==undefined&&p.generalSearchTemplate!==null&&p.generalSearchTemplate.length>0){for(var t=0;
t<p.generalSearchTemplate.length;
t++){var u=p.generalSearchTemplate[t];
if(u.featureName.toLowerCase()==="invoice"){p.searchInvoiceTemplate.push(angular.copy(u));
s.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="parcel"){p.searchParcelTemplate.push(angular.copy(u));
w.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="lot"){p.searchLotTemplate.push(angular.copy(u));
v.push(angular.copy(u.model))
}else{if(u.featureName.toLowerCase()==="packet"){p.searchPacketTemplate.push(angular.copy(u));
r.push(angular.copy(u.model))
}}}}l[u.model]=u.featureName;
p.modelAndHeaderList.push({name:u.model,displayName:u.label,minWidth:200})
}if(s.length>0){g.invoice=s
}if(w.length>0){g.parcel=w
}if(v.length>0){g.lot=v
}if(r.length>0){g.packet=r
}}p.dataRetrieved=true;
p.searchResetFlag=true
},function(r){},function(r){});
f.retrievePrerequisite(function(r){p.entityList=r.entity
});
p.mapOfActivitySerice={};
j.retrievePrerequisite(function(r){if(!!r){j.retrieveServices(function(s){p.serviceList=s;
m.unMaskLoading();
j.retrieveDesignations(function(t){p.designationList=t.data;
j.retrieveActivityFlowVersion(r.activityflowbycompany[0]["custom2"][0].value,function(u){angular.forEach(u.data.activityFlowGroups,function(v){p.mapOfActivitySerice[v.flowGroupName]=[];
angular.forEach(v.nodeDataBeanList,function(x){var w=h("filter")(p.serviceList,function(z){return z.id===x.associatedService
})[0];
var y=h("filter")(p.designationList,function(z){return z.value===x.designationId
})[0];
w.designation=y.value;
p.mapOfActivitySerice[v.flowGroupName].push(w)
})
})
})
})
})
}})
};
p.retrieveSearchedData=function(){if(Object.getOwnPropertyNames(p.searchCustom).length>0){var v=false;
if(!jQuery.isEmptyObject(p.searchCustom)){var s=0;
var r=0;
for(var t in p.searchCustom){r++;
if(p.searchCustom[t]===""||p.searchCustom[t]===null){s++
}}if(parseInt(s)===parseInt(r)){v=true
}}if(!v){m.maskLoading();
p.printDataBean.featureCustomMapValue={};
p.map={};
var u={};
var q=o.convertSearchData(p.invoiceCustomData,p.parcelCustomData,p.lotCustomData,p.packetCustomData,angular.copy(p.searchCustom));
angular.forEach(l,function(A,y){var z=q[y];
if(z!==undefined){var x={};
if(!u[A]){x[y]=z;
u[A]=x
}else{var w=u[A];
w[y]=z;
u[A]=w
}}else{z=q["to"+y];
if(z!==undefined){var x={};
if(!u[A]){x["to"+y]=z;
u[A]=x
}else{var w=u[A];
w["to"+y]=z;
u[A]=w
}}z=q["from"+y];
if(z!==undefined){var x={};
if(!u[A]){x["from"+y]=z;
u[A]=x
}else{var w=u[A];
w["from"+y]=z;
u[A]=w
}}}});
p.printDataBean.featureCustomMapValue=u;
p.printDataBean.featureDbFieldMap=g;
c.search(p.printDataBean,function(w){m.unMaskLoading();
delete p.toPrint;
var x=o.retrieveSectionWiseCustomFieldInfo("staticPrint");
x.then(function(y){p.searchResetFlag=false;
p.printTemplate=y.genralSection;
p.searchedDataFromDb=angular.copy(w);
var z=function(A){p.searchedData=angular.copy(A);
var C=[];
var B=0;
angular.forEach(p.searchedData,function(D){angular.forEach(p.modelAndHeaderList,function(E){if(!D.categoryCustom.hasOwnProperty(E.name)){D.categoryCustom[E.name]="NA"
}else{if(D.categoryCustom.hasOwnProperty(E.name)){if(D.categoryCustom[E.name]===null||D.categoryCustom[E.name]===""||D.categoryCustom[E.name]===undefined){D.categoryCustom[E.name]="NA"
}}}});
D.categoryCustom["~@index"]=B;
B=B+1;
C.push(D.categoryCustom)
});
p.listFilled=true;
p.gridOptions.data=C;
p.gridOptions.columnDefs=p.modelAndHeaderList;
m.unMaskLoading()
};
o.convertorForCustomField(w,z);
p.reset()
},function(y){},function(y){})
},function(){var x="Could not retrieve, please try again.";
var w=m.error;
m.addMessage(x,w);
m.unMaskLoading()
})
}else{delete p.searchedData;
p.listFilled=false;
p.selectOneParameter=true
}}else{p.selectOneParameter=true
}};
p.onCanelOfSearch=function(){if(p.printstaticForm!==null){p.printstaticForm.$dirty=false
}p.searchedData=[];
p.listFilled=false;
p.searchResetFlag=false;
p.reset();
delete p.toPrint;
delete p.service;
delete p.activity;
p.printstaticForm.$setPristine()
};
p.setToPrintData=function(q){if(!!q){p.toPrint=q
}};
p.printData=function(){p.flag.printlot=false;
p.flag.printpacket=false;
p.submitted=true;
var v={};
var r=[];
var u=[];
var s=[];
var q=[];
if(p.printstaticForm.$valid){p.printdataflag=true;
n.retrieveDesignationBasedFields("staticPrint",function(x){p.invoiceCustom=o.resetSection(p.generaInvoiceTemplate);
var z=o.retrieveSectionWiseCustomFieldInfo("invoice");
p.invoiceDbType={};
z.then(function(D){p.generaInvoiceTemplate=D.genralSection;
var C=[];
var B=Object.keys(x).map(function(E,F){angular.forEach(this[E],function(G){if(E==="Invoice#P#"){C.push({Invoice:G})
}})
},x);
p.generaInvoiceTemplate=o.retrieveCustomData(p.generaInvoiceTemplate,C);
angular.forEach(p.generaInvoiceTemplate,function(E){if(E.model){r.push(E.model)
}});
if(r.length>0){v.invoiceDbFieldName=r
}},function(B){},function(B){});
p.parcelCustom=o.resetSection(p.generaParcelTemplate);
var A=o.retrieveSectionWiseCustomFieldInfo("parcel");
p.parcelDbType={};
A.then(function(D){p.generaParcelTemplate=D.genralSection;
var C=[];
var B=Object.keys(x).map(function(E,F){angular.forEach(this[E],function(G){if(E==="Parcel#P#"){C.push({Parcel:G})
}})
},x);
p.generaParcelTemplate=o.retrieveCustomData(p.generaParcelTemplate,C);
angular.forEach(p.generaParcelTemplate,function(E){if(E.model){u.push(E.model)
}});
if(u.length>0){v.parcelDbFieldName=u
}},function(B){},function(B){});
p.categoryCustom=o.resetSection(p.generaLotTemplate);
var w=o.retrieveSectionWiseCustomFieldInfo("lot");
p.lotDbType={};
w.then(function(C){p.generalLotTemplate=C.genralSection;
var D=[];
var B=Object.keys(x).map(function(E,F){angular.forEach(this[E],function(G){if(E==="Lot#P#"){D.push({Parcel:G})
}})
},x);
p.generalLotTemplate=o.retrieveCustomData(p.generalLotTemplate,D);
angular.forEach(p.generalLotTemplate,function(E){if(E.model){s.push(E.model)
}});
if(s.length>0){v.lotDbFieldName=s
}},function(B){},function(B){});
p.packetCustom=o.resetSection(p.generaPacketTemplate);
var y=o.retrieveSectionWiseCustomFieldInfo("packet");
p.packetDbType={};
y.then(function(D){p.generalPacketTemplate=D.genralSection;
var C=[];
var B=Object.keys(x).map(function(E,F){angular.forEach(this[E],function(G){if(E==="Packet#P#"){C.push({Packet:G})
}})
},x);
p.generalPacketTemplate=o.retrieveCustomData(p.generalPacketTemplate,C);
angular.forEach(p.generalPacketTemplate,function(E){if(E.model){q.push(E.model)
}});
if(q.length>0){v.packetDbFieldName=q
}},function(B){},function(B){});
m.unMaskLoading()
},function(){m.unMaskLoading();
var x="Failed to retrieve data";
var w=m.error;
m.addMessage(x,w)
});
p.designations=[];
p.designations.push(p.service.designation);
var t={featureName:"staticPrint",roles:p.designations};
m.maskLoading();
n.retrieveExternalDesignationBasedFields(t,function(w){p.finalPayload={};
m.unMaskLoading();
p.printLotCustom=o.resetSection(p.generalPrintLotTemplate);
var y=o.retrieveSectionWiseCustomFieldInfo("lot");
p.printLotDbType={};
y.then(function(A){p.generalPrintLotTemplate=A.genralSection;
var B=[];
var z=Object.keys(w).map(function(C,D){angular.forEach(this[C],function(E){if(C==="Lot"){B.push({Lot:E})
}})
},w);
p.generalPrintLotTemplate=o.retrieveCustomData(p.generalPrintLotTemplate,B);
angular.forEach(p.generalPrintLotTemplate,function(C){if(C.model){s.push(C.model)
}});
if(s.length>0){v.lotDbFieldName=(v.lotDbFieldName||[]).concat(s)
}},function(z){},function(z){});
p.printPacketCustom=o.resetSection(p.generalPrintPacketTemplate);
var x=o.retrieveSectionWiseCustomFieldInfo("packet");
p.printLotDbType={};
x.then(function(C){p.generalPrintPacketTemplate=C.genralSection;
var A=[];
var z=Object.keys(w).map(function(D,E){angular.forEach(this[D],function(F){if(D==="Packet"){A.push({Lot:F})
}})
},w);
p.generalPrintPacketTemplate=o.retrieveCustomData(p.generalPrintPacketTemplate,A);
angular.forEach(p.generalPrintPacketTemplate,function(D){if(D.model){q.push(D.model)
}});
if(q.length>0){v.packetDbFieldName=(v.packetDbFieldName||[]).concat(q)
}if(p.toPrint.id===null&&p.toPrint.value!==null){var B=[];
B.push(p.toPrint.value);
v.lotObjectId=B;
k.retrieveLotById(v,function(D){p.invoiceCustom=D.custom3;
p.parcelCustom=angular.copy(D.custom4);
p.categoryCustom=angular.copy(D.custom1);
p.printLotCustom=angular.copy(D.custom1);
p.flag.printlot=true;
m.unMaskLoading()
},function(){m.unMaskLoading();
var E="Could not retrieve lot, please try again.";
var D=m.error;
m.addMessage(E,D)
})
}else{if(p.toPrint.id!==null){var B=[];
B.push(p.toPrint.id);
v.packetId=B;
c.retrievePacketById(v,function(D){p.invoiceCustom=D.custom1;
p.parcelCustom=angular.copy(D.custom3);
p.categoryCustom=angular.copy(D.custom4);
p.printPacketCustom=angular.copy(D.custom5);
p.flag.printpacket=true;
m.unMaskLoading()
},function(){m.unMaskLoading();
var E="Could not retrieve packet, please try again.";
var D=m.error;
m.addMessage(E,D)
})
}}},function(z){},function(z){})
},function(){m.unMaskLoading();
var x="Failed to retrieve data";
var w=m.error;
m.addMessage(x,w)
})
}};
p.onBack=function(){p.printdataflag=false
};
p.generatePDFFromData=function(){var r={};
var q=[];
q.push({categoryCustom:p.printLotCustom});
o.convertorForCustomField(q,function(t){angular.forEach(p.generalPrintLotTemplate,function(v){var w=t[0].categoryCustom[v.model];
if(!!w){r[v.label]=w
}else{r[v.label]="N/A"
}});
var s={};
var u=[];
u.push({categoryCustom:p.printPacketCustom});
o.convertorForCustomField(u,function(w){angular.forEach(p.generalPrintPacketTemplate,function(z){var A=null;
if(!!p.printPacketCustom){A=w[0].categoryCustom[z.model]
}if(!!A){s[z.label]=A
}else{s[z.label]="N/A"
}});
var v=[];
var y={};
y.payload=r;
y.payload1=s;
if(!!p.toPrint){var x=p.toPrint;
if(!!x.id){y.idToPrint={id:x.categoryCustom["packetID$AG$String"]};
delete y.payload
}else{if(!!x.value){y.idToPrint={id:x.categoryCustom["lotID$AG$String"]};
delete y.payload1
}}}m.maskLoading();
c.generatePrintData(y,function(z){m.unMaskLoading();
if(!!z){p.fileName=z.filename;
var A=document.createElement("a");
A.href=m.appendAuthToken(m.centerapipath+"print/downloadPDFReport?fileName="+p.fileName);
A.target="_blank";
A.download="myFile.pdf";
A.click();
p.onBack()
}},function(){m.unMaskLoading()
})
})
})
};
p.reset=function(){p.searchCustom={};
delete p.generalSearchTemplate;
var q=o.retrieveSearchWiseCustomFieldInfo("staticPrint");
q.then(function(r){p.generalSearchTemplate=r.genralSection;
p.searchResetFlag=true;
p.flag.customFieldGenerated=true
},function(r){},function(r){})
};
m.unMaskLoading()
}])
});