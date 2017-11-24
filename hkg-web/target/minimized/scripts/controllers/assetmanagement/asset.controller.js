define(["hkg","assetService","fileUploadService","addMasterValue","printBarcodeValue","dynamicForm"],function(c,b,a,d){c.register.controller("CategoryController",["$rootScope","$scope","$location","AssetService","$timeout","$http","limitToFilter","DynamicFormService","FileUploadService","$filter",function(m,o,h,l,f,k,e,n,g,j){m.maskLoading();
m.mainMenu="manageLink";
m.childMenu="manageAssets";
o.parentId=0;
m.activateMenu();
o.categoryListDropdown=[];
o.categoryDropdownList=[];
o.selectedCategoryDropdown={};
o.categoryList=[];
o.category={};
o.dbType={};
o.addAssetData={};
o.issueAssetData={};
o.dbTypeForIssue={};
o.categoryData={};
o.categoryData=n.resetSection(o.categoryTemplate);
o.isAssetInvalid=true;
o.dbTypeForCategory={};
o.addAssetForm={};
o.categoryDropdown=[];
o.purchaserList=[];
o.addAssetCategory=false;
o.reloadFlag=true;
o.reload=true;
o.assetModel="";
o.setViewFlag=function(p){if(p!="search"){$("#scrollable-dropdown-menu.typeahead").typeahead("val","")
}o.displayFlag=p;
o.submitted=false
};
o.entity="ASSET.";
o.statusList=["Active","Remove"];
o.assetNames=[];
o.fileArray=[];
o.fileNames=[];
o.asset={};
o.asset.fileList=[];
o.multiselecttree={text:"All Assets",isChecked:false,items:[]};
o.fileCounter=0;
o.managedAsset=[];
o.unmanagedAsset=[];
o.selectedAssets=[];
o.searchedAssetRecords=[];
o.selectedCategory=[];
o.manifactAvail=true;
o.removeFileList=[];
o.listOfModelsOfDateType=[];
o.listOfModelsOfDateType2=[];
o.makeEditFlagFalse=function(){o.assetModel="";
o.editFlag=false;
if(o.selectedCategory.currentNode!=undefined){o.selectedCategory.currentNode.selected=undefined
}o.initCreateAsset()
};
o.categoryCustomData={};
o.categoryCustomData=n.resetSection(o.categoryTemplate);
o.initIssueAssetForm=function(){o.issueMaxDate=m.getCurrentServerDate();
o.issueasset={};
o.assetList=[];
o.selectedAssets=[];
o.selectedInString="Select asset";
o.assetUnitList=[];
o.managedAsset=[];
o.unmanagedAsset=[];
o.combinedAssetList=[];
o.issueAssetData=n.resetSection(o.issueAssetTemplate);
o.dbTypeForIssue={};
o.issueasset.parentName="Select asset category";
o.issueasset.issueTo="";
o.issueSubmitted=false;
o.isAssetInvalid=true;
o.issueasset.issuedOn=m.getCurrentServerDate()
};
o.printBarcode=function(){var p=window.open(m.appendAuthToken(m.apipath+"asset/getimage/"+o.asset.barcode));
p.print()
};
o.allNames=[];
o.select2Names=[];
o.popover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr> <tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
o.searchpopover="<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@C'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Categories</td></tr> </table> ";
o.assetpopover="<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td> \n '@E'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Employees</td></tr> </table> ";
o.$on("$viewContentLoaded",function(){o.setViewFlag("issue");
o.retrieveAssetCategories();
o.initIssueAssetForm();
l.cancelform();
var p=n.retrieveSectionWiseCustomFieldInfo("manageAssets");
p.then(function(q){o.customgeneralTemplateData=angular.copy(q.genralSection);
o.genralDepartmentTemplate=m.getCustomDataInSequence(o.customgeneralTemplateData);
if(o.genralDepartmentTemplate!==null&&o.genralDepartmentTemplate!==undefined){angular.forEach(o.genralDepartmentTemplate,function(r){if(r.type!==null&&r.type!==undefined&&r.type==="date"){o.listOfModelsOfDateType.push(r.model)
}})
}o.customIssueTemplateData=angular.copy(q.issueAsset);
o.issueAssetTemplate=m.getCustomDataInSequence(o.customIssueTemplateData);
o.customcategoryTemplateData=angular.copy(q.category);
o.categoryTemplate=m.getCustomDataInSequence(o.customcategoryTemplateData);
if(o.categoryTemplate!==null&&o.categoryTemplate!==undefined){angular.forEach(o.categoryTemplate,function(r){if(r.type!==null&&r.type!==undefined&&r.type==="date"){o.listOfModelsOfDateType2.push(r.model)
}})
}},function(q){},function(q){})
});
o.resetCustomFields=function(){var p=n.retrieveSectionWiseCustomFieldInfo("manageAssets");
p.then(function(q){o.customgeneralTemplateData=angular.copy(q.genralSection);
o.genralDepartmentTemplate=m.getCustomDataInSequence(o.customgeneralTemplateData);
o.customIssueTemplateData=angular.copy(q.issueAsset);
o.issueAssetTemplate=m.getCustomDataInSequence(o.customIssueTemplateData);
o.customcategoryTemplateData=angular.copy(q.category);
o.categoryTemplate=m.getCustomDataInSequence(o.customcategoryTemplateData)
},function(q){},function(q){})
};
o.setAssetForEdit=function(p){l.retriveAssetById(p,function(q){o.retrieveAsset(q)
},function(){m.addMessage("Could not retrive asset , please try again.",1)
})
};
o.getSearchedAsset=function(q){var p=$("#scrollable-dropdown-menu.typeahead").typeahead("val");
if(p.length>0){if(p.length<3){o.searchedAssetRecords=[];
o.setViewFlag("search")
}else{o.searchedAssetRecords=angular.copy(q);
o.setViewFlag("search")
}}};
o.retrieveManifacturer=function(){if(o.editFlag){l.fillstatuslist(o.asset.status,function(p){o.statuslist=p
},function(){})
}l.retrievemanifacturer(function(p){o.manifacturerList=[];
if(p!=null&&angular.isDefined(p)&&p.length>0){angular.forEach(p,function(q){q.label=m.translateValue("MF."+q.label);
o.manifacturerList.push(q)
})
}if(o.manifacturerList.length===0){o.manifactAvail=false;
m.addMessage("First add manufacturer from masters then try to add/edit masters.",m.failure)
}else{o.manifactAvail=true
}o.setViewFlag("create")
},function(){})
};
o.initCreateAsset=function(){o.addAssetCategory=false;
if(!o.editFlag){o.asset={};
o.asset.parentCategory="Select asset category";
o.today();
o.editFlag=false;
o.asset.fileList=[];
o.dbType={};
o.addAssetData=n.resetSection(o.genralDepartmentTemplate);
o.categoryCustomData=n.resetSection(o.categoryTemplate);
o.dbTypeForCategory={};
o.asset.canGenerateBarcode=false;
$("#recipients123").select2("data",[])
}o.retrieveManifacturer()
};
o.initForm=function(p){o.addAssetForm=p;
o.addAssetForm.$pristine;
o.submitted=false;
if(o.editFlag){o.initCreateAsset()
}};
o.retrieveAssetCategories=function(p){m.maskLoading();
l.retrieveCategories(function(q){o.categoryList=[];
if(q.length>0){if(q!==null&&angular.isDefined(q)&&q.length>0){angular.forEach(q,function(r){o.categoryList.push(r)
})
}o.setDefaultCategory(p)
}else{}m.unMaskLoading()
},function(){m.unMaskLoading()
})
};
o.unmanagedLabel=[];
o.combinedAssetList=[];
o.retrieveAssetsByCategory=function(){o.managedAsset=[];
o.unmanagedAsset=[];
o.combinedAssetList=[];
l.retrieveAssets(o.category,function(p){o.assetList=p;
if(o.assetList.length>0){angular.forEach(o.assetList,function(q){q.modelName=m.translateValue("AST_NM."+q.modelName);
if(q.assetType){o.managedAsset.push(q)
}else{o.unmanagedAsset.push(q)
}})
}o.combinedAssetList.push({modelName:"<strong>Managed assets</strong>",multiSelectGroup:true});
if(o.managedAsset.length>0){angular.forEach(o.managedAsset,function(q){o.combinedAssetList.push(q)
})
}if(o.unmanagedAsset.length>0){o.combinedAssetList.push({multiSelectGroup:false},{modelName:"<strong>Non-Managed assets</strong>",multiSelectGroup:true});
angular.forEach(o.unmanagedAsset,function(q){o.combinedAssetList.push(q)
})
}o.combinedAssetList.push({multiSelectGroup:false},{multiSelectGroup:false})
},function(){})
};
o.setDefaultCategory=function(){o.categoryListDropdown=[];
o.asset.parentCategory="Select asset category";
o.issueasset.parentName="Select asset category";
$.merge(o.categoryListDropdown,angular.copy(o.categoryList))
};
o.assetCancel=function(p){o.initIssueAssetForm();
o.setViewFlag("issue");
l.cancelform();
o.isAssetInvalid=false;
o.asset={};
o.initIssueAssetForm();
o.editFlag=false;
o.fileNames=[];
if(o.selectedCategory.currentNode!==undefined){o.selectedCategory.currentNode.selected=undefined
}if(o.selectedCategoryDropdown.currentNode!==undefined){o.selectedCategoryDropdown.currentNode.selected=undefined
}p.$setPristine()
};
o.categoryClick=function(p){o.category.parentId=p.currentNode.id;
o.category.parentName=p.currentNode.displayName
};
o.selectCategory=function(){m.maskLoading();
o.editFlag=false;
o.setViewFlag("view");
o.category=angular.copy(o.selectedCategory.currentNode);
o.parentId=o.category.parentId;
l.addCustomDataToCategoryDataBean(o.category,function(p){o.categoryCustomData=p.categoryCustomData;
if(!!o.categoryCustomData){angular.forEach(o.listOfModelsOfDateType2,function(q){if(o.categoryCustomData.hasOwnProperty(q)){if(o.categoryCustomData[q]!==null&&o.categoryCustomData[q]!==undefined){o.categoryCustomData[q]=new Date(o.categoryCustomData[q])
}else{o.categoryCustomData[q]=""
}}})
}});
l.retrieveAssets(o.category,function(p){m.unMaskLoading();
o.assetList=[];
o.assetList=p;
if(o.assetList.length>0){if(o.assetList.length>0){angular.forEach(o.assetList,function(q){q.modelName=m.translateValue("AST_NM."+q.modelName)
})
}}o.categoryDropdown=[];
o.categoryDropdown.push({id:"0",displayName:"None"});
$.merge(o.categoryDropdown,angular.copy(o.categoryList));
o.newDeptList=angular.copy(o.categoryDropdown);
o.deleteSelectedNode(o.newDeptList)
},function(){m.unMaskLoading();
o.categoryDropdown=[];
o.categoryDropdown.push({id:"0",displayName:"None"});
$.merge(o.categoryDropdown,angular.copy(o.categoryList));
o.newDeptList=angular.copy(o.categoryDropdown);
o.deleteSelectedNode(o.newDeptList)
})
};
o.updateCategory=function(p){o.submitted=true;
if(p.$valid){o.category.displayName=o.category.originalName;
if(o.category.status==="Remove"){o.conformationForRemovecategory()
}else{o.category.dbTypeForCategory=o.dbTypeForCategory;
o.category.categoryCustomData=o.categoryCustomData;
l.updatecategory(o.category,function(q){if(q.messages!==undefined){o.categoryExistsMsg=q.messages[0].message;
p.availabelAsset.$setValidity("exists",false)
}else{o.category={};
p.$setPristine();
o.retrieveAssetCategories();
o.initIssueAssetForm();
o.setViewFlag("issue");
o.initIssueAssetForm()
}},function(){m.addMessage("Could not save details, please try again.",1)
})
}}};
o.today=function(){o.asset.purchaseDt=m.getCurrentServerDate();
o.asset.inwardDt=m.getCurrentServerDate();
o.asset.assetType=true;
o.asset.canProduceImages=false;
o.asset.canGenerateBarcode=false
};
o.clear=function(){o.dt=null
};
o.disabled=function(p,q){return(q==="day"&&(p.getDay()===0||p.getDay()===6))
};
o.toggleMin=function(){var p=m.getCurrentServerDate();
o.minFromDate=m.getCurrentServerDate();
o.minToDate=m.getCurrentServerDate()
};
o.toggleMin();
o.setMaxDate=function(){o.maxDate=o.holiday.endDt
};
o.open=function(p){p.preventDefault();
p.stopPropagation()
};
o.openInwartDt=function(p){p.preventDefault();
p.stopPropagation()
};
o.dateOptions={formatYear:"yy",startingDay:1};
o.format=m.dateFormat;
o.asset={};
o.assetClick=function(p){o.reloadFlag=false;
o.$on("$locationChangeStart",function(r){if(p!==null){if(!confirm("You have unsaved changes, go back?")){r.preventDefault()
}else{p===null
}}});
o.isAssetInvalid=true;
if(!angular.equals(p,{})){o.isAssetInvalid=false
}if(o.displayFlag==="create"){o.asset.category=p.currentNode.id;
o.asset.parentCategory=p.currentNode.displayName;
o.asset.prefix=p.currentNode.categoryPrefix;
o.asset.serialNumber=p.currentNode.startIndex;
o.asset.pattern=p.currentNode.pattern;
if(o.asset.pattern!==undefined&&o.asset.pattern!==null){o.min_length=o.asset.pattern.length
}f(function(){o.reloadFlag=true
},100)
}if(o.displayFlag==="issue"){o.issueasset.parentName=p.currentNode.displayName;
o.issueasset.categoryId=p.currentNode.id;
o.category=p.currentNode;
o.assetUnitList=[];
o.retrieveAssetsByCategoryForIssue(q)
}if(o.displayFlag==="view"){o.parentId=p.currentNode.id;
o.category.parentId=p.currentNode.id;
o.category.parentName=p.currentNode.displayName
}var q=function(){o.selectedInString="Select assets";
o.selectedAssets=[]
}
};
o.issueAssetMethod=function(q){o.issueSubmitted=true;
if(q.$valid&&!o.isAssetInvalid){o.issueasset.assetDataBeanList=angular.copy(o.selectedAssets);
o.issueasset.nonManagedAssetDataBeans=angular.copy(o.assetUnitList);
o.issueasset.issueCustomData=o.issueAssetData;
o.issueasset.dbTypeForIssue=o.dbTypeForIssue;
var p=function(){o.issueasset={};
o.combinedAssetList=[];
o.issueasset.issueTo="";
q.$setPristine();
o.issueSubmitted=false;
o.initIssueAssetForm();
o.retrieveAssetCategories()
};
if(o.assetList.length===0){m.addMessage("Select atleast one asset",m.failure)
}else{l.issueAsset(angular.copy(o.issueasset),p,function(){o.issueasset={};
o.combinedAssetList=[];
o.issueasset.issueTo="";
q.$setPristine();
o.issueSubmitted=false;
o.initIssueAssetForm()
})
}}else{}};
o.names=[];
o.autoCompleteRecipient={allowClear:true,multiple:true,closeOnSelect:false,placeholder:"Select purchased by",initSelection:function(p,r){if(o.editFlag){var q=[];
angular.forEach(o.purchaserList,function(s){q.push({id:s.recipientInstance+":"+s.recipientType,text:s.recipientValue})
});
r(q)
}},formatResult:function(p){return p.text
},formatSelection:function(p){return p.text
},query:function(s){var r=s.term;
o.names=[];
var t=function(u){if(u.length!==0){o.names=u;
angular.forEach(u,function(v){o.names.push({id:v.value+":"+v.description,text:v.label})
})
}s.callback({results:o.names})
};
var p=function(){};
if(r.substring(0,2)=="@E"||r.substring(0,2)=="@e"){var q=s.term.slice(2);
l.retrieveUserList(q.trim(),t,p)
}else{s.callback({results:o.names})
}}};
o.autoCompleteIssueTo={multiple:true,closeOnSelect:false,maximumSelectionSize:1,placeholder:"Select issue to",initSelection:function(p,q){},formatResult:function(p){return p.text
},formatSelection:function(p){return p.text
},query:function(s){var r=s.term;
o.names=[];
var t=function(u){if(u.length!==0){o.names=u;
angular.forEach(u,function(v){o.names.push({id:v.value+":"+v.description,text:v.label})
})
}s.callback({results:o.names})
};
var p=function(){};
if(r.substring(0,2)=="@E"||r.substring(0,2)=="@e"){var q=s.term.slice(2);
l.retrieveUserList(q.trim(),t,p)
}else{if(r.substring(0,2)=="@D"||r.substring(0,2)=="@d"){var q=s.term.slice(2);
l.retrieveDepartmentList(q.trim(),t,p)
}else{s.callback({results:o.names})
}}}};
o.retrieveAsset=function(p){o.editFlag=true;
o.asset=angular.copy(p);
o.assetModel=o.asset.modelName;
o.asset.assetType=p.assetType.toString();
o.asset.canProduceImages=p.canProduceImages.toString();
if(p.pattern!==undefined&&p.pattern!==null){o.min_length=p.pattern.length
}l.addCustomDataToAssetDataBean(p,function(q){m.unMaskLoading();
o.asset=q;
if(o.asset.inwardDt!==null){o.asset.inwardDt=new Date(o.asset.inwardDt)
}if(o.asset.purchaseDt!==null){o.asset.purchaseDt=new Date(o.asset.purchaseDt)
}if(o.asset.issuedOn!==null){o.asset.issuedOn=new Date(o.asset.issuedOn)
}o.purchaserList=q.purchaserDataBeanList;
$(document).find($("#recipients123")).select2("val",[]);
o.addAssetData=o.asset.addAssetData;
if(!!o.addAssetData){angular.forEach(o.listOfModelsOfDateType,function(r){if(o.addAssetData.hasOwnProperty(r)){if(o.addAssetData[r]!==null&&o.addAssetData[r]!==undefined){o.addAssetData[r]=new Date(o.addAssetData[r])
}else{o.addAssetData[r]=""
}}})
}},function(){});
o.setViewFlag("create")
};
o.viewAssetCategory=function(){o.addAssetCategory=true;
o.categorySubmitted=false;
o.categoryDropdownList=[];
o.category={};
o.categoryData=n.resetSection(o.categoryTemplate);
o.categoryDropdownList.unshift({id:"0",displayName:"None",selected:"selected"});
$.merge(o.categoryDropdownList,angular.copy(o.categoryList));
o.category=angular.copy(o.categoryDropdownList[0]);
o.category.parentName=o.category.displayName;
o.category.displayName="";
$("#addcategoryPopUp").modal("show")
};
o.hideAssetCategory=function(p){o.category={};
o.categoryData=n.resetSection(o.categoryTemplate);
p.$setPristine();
o.categoryExistsMsg="";
$("#addcategoryPopUp").modal("hide");
m.removeModalOpenCssAfterModalHide()
};
o.productSelection="";
o.Products=[{name:"Apple"},{name:"Banana"},{name:"Carrort"},{name:"Dart"}];
o.addCategory=function(p){o.categorySubmitted=true;
if(p.$valid){o.category.categoryCustomData=o.categoryData;
o.category.dbTypeForCategory=o.dbTypeForCategory;
if(o.category.startIndex===null){o.category.startIndex=1
}l.createcategory(angular.copy(o.category),function(q){if(q.data!==undefined){if(q.data.categoryExsist!==undefined){o.categoryExistsMsg=q.data.categoryExsist;
p.categoryName.$setValidity("exists",false)
}else{if(q.data.prefixExsist!==undefined){o.categoryPrefixMsg=q.data.prefixExsist;
p.categoryPrefix.$setValidity("exists1",false)
}}}else{o.category={};
p.$setPristine();
o.retrieveAssetCategories();
$("#addcategoryPopUp").modal("hide");
m.removeModalOpenCssAfterModalHide()
}},function(){m.addMessage("Could not save details, please try again.",1)
})
}};
o.addAsset=function(p,q){o.reload=false;
o.submitted=true;
if(o.editFlag){if(p.$valid){if(o.fileArray.length>0){angular.forEach(o.fileArray,function(s){});
$.merge(o.asset.fileDataBeans,angular.copy(o.fileArray))
}if(o.asset.status==="D"){if(Object.prototype.toString.call(o.asset.purchasedBy)==="[object Array]"){var r="";
angular.forEach(o.asset.purchasedBy,function(s){if(r===""){r=s.id
}else{r=r+","+s.id
}});
o.asset.purchasedBy=r
}o.conformationForRemoveAsset()
}else{o.asset.dbType=o.dbType;
o.asset.addAssetData=o.addAssetData;
if(Object.prototype.toString.call(o.asset.purchasedBy)==="[object Array]"){var r="";
angular.forEach(o.asset.purchasedBy,function(s){if(r===""){r=s.id
}else{r=r+","+s.id
}});
o.asset.purchasedBy=r
}l.updateasset(angular.copy(o.asset),function(s){o.resetCustomFields();
o.reload=true;
if(s.messages!==undefined){o.categoryExistsMsg=s.messages[0].message;
p.serialNumber.$setValidity("exists",false)
}else{l.cancelform();
o.retrieveAssetCategories();
o.editFlag=false;
o.fileArray=[];
o.fileNames=[];
o.initIssueAssetForm();
o.setViewFlag("issue");
o.initCreateAsset();
o.submitted=false
}},function(){m.addMessage("Could not save details, please try again.",1)
})
}}else{}}else{if(p.$valid){o.asset.dbType=o.dbType;
o.asset.addAssetData=o.addAssetData;
if(!o.asset.assetType){o.asset.serialNumber=undefined
}if(o.fileNames.length>0){$.merge(o.asset.fileList,angular.copy(o.fileNames))
}l.createasset(angular.copy(o.asset),function(s){o.resetCustomFields();
o.reload=true;
if(s.messages!==undefined){o.categoryExistsMsg=s.messages[0].message;
if(p.serialNumber!==undefined){p.serialNumber.$setValidity("exists",false)
}}else{l.cancelform();
o.retrieveAssetCategories();
o.editFlag=false;
o.initCreateAsset();
o.setDefaultCategory("issue");
o.fileNames=[];
q.cancel();
o.submitted=false;
p.$setPristine()
}},function(){m.addMessage("Could not save details, please try again.",1)
})
}}};
o.uploadFile={target:m.apipath+"fileUpload/uploadFile",singleFile:false,testChunks:true,query:{fileType:o.seletedFileType,model:"Franchise"}};
o.submitFilesAndSaveAsset=function(q,p){if(!o.editFlag&&o.asset.parentCategory==="Select asset category"){m.addMessage("Select asset category",m.failure)
}else{o.addAsset(p,q)
}};
o.countFiles=function(s,q,x,u){var r=[s.name,w,o.seletedFileType];
var y="ASSET";
var w="Franchise";
var t=[s.name,y];
var v;
var p="true";
var r;
l.uploadFile(t,function(z){v=z.filename;
r=[s.name,w,v,p];
console.log("file in count :"+s.name);
g.uploadFiles(r,function(A){console.log("after upload : "+A.res);
s.msg=A.res;
o.removeFileList.push(A.res);
o.fileNames.push(A.res)
})
})
};
o.validFileFlag=false;
o.fileAdded=function(s,q){var p=5000000;
var r=s.size;
if((s.getExtension()!="jpg")&&(s.getExtension()!="jpeg")&&(s.getExtension()!="png")&&(s.getExtension()!="gif")&&(s.getExtension()!="txt")&&(s.getExtension()!="doc")&&(s.getExtension()!="docx")&&(s.getExtension()!="pdf")){o.validFileFlag=true;
o.fileNames.push(s.name);
alert("Only images and text formats are supported");
return false
}else{if(p<r){o.validFileFlag=true;
o.fileNames.push(s.name);
alert("You can upload a file upto 5 MB ");
return false
}return true
}console.log("file name :"+s.name);
o.fileNames.push(s.name);
console.log("file list :"+o.fileNames)
};
o.chooseCategory=function(p){if(p.currentNode.id!=0){o.categoryListDropdown[0].selected=undefined
}o.asset.category=p.currentNode.id;
o.asset.parentCategory=p.currentNode.displayName;
o.asset.prefix=p.currentNode.categoryPrefix
};
o.removeFromPage=function(p){o.fileArray.push(p)
};
o.setAsset=function(p){o.assetUnitList=[];
o.selectedAssets=[];
angular.forEach(p,function(q){o.selectedAssets.push(q);
if(!q.assetType){o.assetUnitList.push(q)
}})
};
o.updateasset=function(){o.submitted=true;
if(o.editFlag){var p=function(){o.setViewFlag("issue");
o.retrieveAssetCategories()
};
if(o.fileArray.length>0){$.merge(o.asset.fileDataBeans,angular.copy(o.fileArray))
}console.log("hello : "+JSON.stringify(angular.copy(o.asset)));
l.updateasset(angular.copy(o.asset),p);
o.hideconformationForRemoveAsset()
}};
o.conformationForRemoveAsset=function(){$("#removeAssetModal").modal("show")
};
o.hideconformationForRemoveAsset=function(){$("#removeAssetModal").modal("hide");
m.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
o.initIssueAssetForm();
o.setViewFlag("issue")
};
o.conformationForRemovecategory=function(){if(o.assetList.length>0){$("#messageModal").modal("show")
}else{$("#removecategoryModal").modal("show")
}};
o.hideconformationForRemovecategory=function(){$("#removecategoryModal").modal("hide");
m.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
o.category.status="Active"
};
o.successRemoveCat=function(){$("#removecategoryModal").modal("hide");
m.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
o.retrieveAssetCategories();
f(function(){o.setViewFlag("issue")
},400);
o.submitted=false
};
o.removeCategory=function(){var p=function(){o.successRemoveCat()
};
l.removecategory(o.category,p)
};
o.removeCanNotRemoveCategory=function(){$("#messageModal").modal("hide");
m.removeModalOpenCssAfterModalHide();
$(".modal-backdrop").remove();
o.category.status="Active"
};
o.assetNameList={multiple:true,closeOnSelect:false,maximumSelectionSize:1,width:"element",maximumInputLength:100,placeholder:"Select asset name or start typing..",data:function(){return{results:o.select2Names}
}};
o.retrievesamenamesuggestion=function(){if(o.asset.modelName!==undefined){l.retrievesamenamesuggestion(o.asset.modelName,function(q){var p=q.data;
$.each(p,function(r,s){o.select2Names.push({id:s,text:s})
});
$("#assetName").select2("val",null)
})
}};
o.retrievesamenamesuggestion();
o.checkValidity=function(r,q,p){i(q.assetUnitList[p].units,r,q.serialNumberForm.units)
};
function i(p,r,q){if(p>r){q.$setValidity("wrongInput",false)
}else{q.$setValidity("wrongInput",true)
}}o.checkSerialNumber=function(r,s,t,q,p){console.log("checking :"+p);
o.tempAsset={};
o.tempAsset.category=s;
o.tempAsset.serialNumber=r;
o.tempAsset.id=t;
o.tempAsset.assetType=p;
if(r!==undefined&&p){l.doesserialnumberexist(angular.copy(o.tempAsset),function(u){if(u.messages!==undefined&&u.messages!==null){o.categoryExistsMsg=u.messages[0].message;
q.serialNumber.$setValidity("exists",false)
}else{q.serialNumber.$setValidity("exists",true)
}},function(){m.addMessage("Could not save details, please try again.",1)
})
}else{q.serialNumber.$setValidity("exists",false)
}};
o.makeFlagDirty=function(p){p.$dirty=false
};
o.deleteSelectedNode=function(r){if(r===null||r===undefined){return
}for(var q=0;
q<r.length;
q++){if(angular.isDefined(o.selectedCategory.currentNode)&&o.selectedCategory.currentNode.id===r[q].id){r.splice(q,1)
}else{for(var p=0;
p<r.length;
p++){if(r[p].children!==null){o.deleteSelectedNode(r[p].children)
}}}}o.categoryDropdown=angular.copy(r)
};
o.unmanagedLabel=[];
o.combinedAssetList=[];
o.retrieveAssetsByCategoryForIssue=function(){o.managedAsset=[];
o.unmanagedAsset=[];
o.combinedAssetList=[];
l.retrieveAssetsForIssue(o.category,function(p){o.assetList=p;
if(o.assetList.length>0){angular.forEach(o.assetList,function(q){q.assetName=m.translateValue("AST_NM."+q.modelName);
if(q.assetType){o.managedAsset.push(q)
}else{o.unmanagedAsset.push(q)
}})
}o.combinedAssetList.push({assetName:"<strong>Managed assets</strong>",multiSelectGroup:true});
if(o.managedAsset.length>0){angular.forEach(o.managedAsset,function(q){o.combinedAssetList.push(q);
console.log("combined asse"+JSON.stringify(o.combinedAssetList))
})
}if(o.unmanagedAsset.length>0){o.combinedAssetList.push({multiSelectGroup:false},{assetName:"<strong>Non-Managed assets</strong>",multiSelectGroup:true});
angular.forEach(o.unmanagedAsset,function(q){o.combinedAssetList.push(q)
})
}o.combinedAssetList.push({multiSelectGroup:false},{multiSelectGroup:false})
},function(){})
};
o.removeFromSession=function(q){var p=o.fileNames.indexOf(q);
o.fileNames.splice(p,1);
l.removeFileFromSession(q)
};
o.setToDate=function(){if(o.asset.purchaseDt===""||o.asset.purchaseDt===undefined){o.minToDate=m.getCurrentServerDate()
}else{o.minToDate=o.asset.purchaseDt;
if(!o.isEdit){o.asset.inwardDt=o.asset.purchaseDt
}}if(o.asset.purchaseDt>o.asset.inwardDt){o.asset.inwardDt=o.asset.purchaseDt
}};
o.setFromDate=function(){o.maxFromDate=o.asset.inwardDt
};
o.barcodeList=[];
o.handleBarcodeGeneration=function(){if(o.asset.canGenerateBarcode){if(o.asset.serialNumber){var q=-1;
for(var r=0,p=o.barcodeList.length;
r<p;
r++){if(o.barcodeList[r].name===""+o.asset.prefix+o.asset.serialNumber){q=r;
break
}}if(q===-1){m.maskLoading();
l.generateBarcode(""+o.asset.prefix+o.asset.serialNumber,function(s){if(s){o.asset.barcode=s.tempFilePath;
o.barcodeList.push({name:""+o.asset.prefix+o.asset.serialNumber,path:s.tempFilePath})
}m.unMaskLoading()
},function(){m.unMaskLoading();
m.addMessage("Error in generating barcode",1)
})
}else{o.asset.barcode=o.barcodeList[q].path
}}else{m.addMessage("Could not generate barcode, please enter serial number.",1)
}}else{o.asset.barcode=""
}};
o.$watch("asset.parentCategory",function(p){if(p){o.handleBarcodeGeneration()
}});
o.makeDirtyFalse=function(p){m.maskLoading();
o.addAssetForm=p;
o.addAssetForm.$pristine;
o.submitted=false;
if(!o.editFlag){f(function(){o.addAssetForm.recipients123.$dirty=false
},700);
f(function(){o.flag=true;
m.unMaskLoading()
},1000)
}};
o.checkDateInProperFormat=function(p){var q=j("date")(p,"dd/MM/yyyy");
o.issueasset.issuedOn=angular.copy(q)
};
m.unMaskLoading()
}])
});