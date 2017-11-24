define(["hkg","invoiceService","customFieldService","ngload!uiGrid","addMasterValue","customsearch.directive","printBarcodeValue","dynamicForm"],function(a,b){a.register.controller("AddInvoiceController",["$rootScope","$scope","InvoiceService","$timeout","$filter","$location","$window","DynamicFormService","CustomFieldService",function(g,k,h,d,f,e,c,j,i){g.mainMenu="stockLink";
g.childMenu="addInvoice";
g.activateMenu();
k.flag={};
g.maskLoading();
k.invoiceDataBean={};
k.showAddInvoicePage=true;
i.retrieveDesignationBasedFields("invoiceAdd",function(l){k.response=l;
k.invoiceCustom={};
var m=j.retrieveSectionWiseCustomFieldInfo("invoice");
k.dbType={};
m.then(function(s){var q=[];
var o=Object.keys(l).map(function(t,u){angular.forEach(this[t],function(v){if(t==="Invoice"){q.push({Invoice:v})
}})
},l);
k.generaInvoiceTemplate=s.genralSection;
k.generaInvoiceTemplate=j.retrieveCustomData(k.generaInvoiceTemplate,q);
k.fieldNotConfigured=false;
k.showConfigMsg=false;
var n=[];
k.mandatoryFields=["carat_of_invoice$NM$Double","in_stock_of_invoice$UMS$String","invoiceNumber$TF$String"];
if(k.generaInvoiceTemplate!==undefined&&k.generaInvoiceTemplate!==null&&k.generaInvoiceTemplate.length>0){for(var p=0;
p<k.generaInvoiceTemplate.length;
p++){if(k.generaInvoiceTemplate[p].model==="carat_of_invoice$NM$Double"){k.generaInvoiceTemplate[p].required=true
}else{if(k.generaInvoiceTemplate[p].model==="in_stock_of_invoice$UMS$String"){k.generaInvoiceTemplate[p].required=true
}else{if(k.generaInvoiceTemplate[p].model==="invoiceNumber$TF$String"){k.generaInvoiceTemplate[p].required=true
}}}n.push(angular.copy(k.generaInvoiceTemplate[p].model))
}}if(n.length>0&&k.mandatoryFields!=null&&k.mandatoryFields.length>0){for(var r=0;
r<k.mandatoryFields.length;
r++){if(n.indexOf(k.mandatoryFields[r])===-1){k.fieldNotConfigured=true;
break
}}}else{k.fieldNotConfigured=true
}if(k.fieldNotConfigured){k.showConfigMsg=true
}k.invoiceAddShow=true;
g.unMaskLoading();
k.flag.customFieldGenerated=true;
k.invoiceDataBean.invoiceDbType=k.dbType
},function(n){},function(n){})
},function(){g.unMaskLoading();
var m="Failed to retrieve data";
var l=g.error;
g.addMessage(m,l)
});
k.onCanel=function(){k.invoiceAddShow=false;
if(k.addInvoiceForm!=null){k.addInvoiceForm.$dirty=false
}k.submitted=false;
k.invoiceAddShow=false;
k.reset();
k.invoiceAddShow=true
};
k.initAddInvoiceForm=function(l){k.addInvoiceForm=l
};
k.reset=function(){k.invoiceCustom={};
var l=j.retrieveSectionWiseCustomFieldInfo("invoice");
k.dbType={};
l.then(function(o){var n=[];
var m=Object.keys(k.response).map(function(p,q){angular.forEach(this[p],function(r){if(p==="Invoice"){n.push({Invoice:r})
}})
},k.response);
k.generaInvoiceTemplate=o.genralSection;
k.generaInvoiceTemplate=j.retrieveCustomData(k.generaInvoiceTemplate,n);
k.invoiceAddShow=true;
g.unMaskLoading();
k.flag.customFieldGenerated=true;
k.invoiceDataBean.invoiceDbType=k.dbType
},function(m){},function(m){})
};
k.saveInvoice=function(l){console.log("error in form"+JSON.stringify(l.$error));
k.submitted=true;
k.invoiceDataBean.invoiceCustom=angular.copy(k.invoiceCustom);
if(Object.getOwnPropertyNames(k.invoiceDataBean.invoiceCustom).length>0){var m=false;
for(var o in k.invoiceCustom){if(typeof k.invoiceCustom[o]==="object"&&k.invoiceCustom[o]!=null){var n=angular.copy(k.invoiceCustom[o].toString());
if(typeof n==="string"&&n!==null&&n!==undefined&&n.length>0){m=true;
break
}}if(typeof k.invoiceCustom[o]==="string"&&k.invoiceCustom[o]!==null&&k.invoiceCustom[o]!==undefined&&k.invoiceCustom[o].length>0){m=true;
break
}if(typeof k.invoiceCustom[o]==="number"&&!!(k.invoiceCustom[o])){m=true;
break
}if(typeof k.invoiceCustom[o]==="boolean"){m=true;
break
}}if(l.$valid&&m){k.invoiceAddShow=false;
k.createdInvoiceId=undefined;
h.create(k.invoiceDataBean,function(p){g.showParcelLink=true;
console.log(JSON.stringify(p));
k.createdInvoiceId=angular.copy(p.primaryKey);
k.onCanel();
k.invoiceAddShow=true;
d(function(){g.showParcelLink=false
},30000)
},function(){g.unMaskLoading();
var q="Could not create invoice, please try again.";
var p=g.error;
g.addMessage(q,p)
})
}}};
k.addParcel=function(){if(k.createdInvoiceId!==undefined){g.maskLoading();
g.createdInvoiceId=angular.copy(k.createdInvoiceId);
e.path("/addparcel");
g.showParcelLink=false
}}
}])
});