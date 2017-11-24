define(["hkg","localesService","franchiseService"],function(a,b,c){a.register.controller("LocalesContoller",["$rootScope","$scope","$filter","ManageLocalesService","FranchiseService",function(e,i,h,j,d){e.maskLoading();
e.mainMenu="manageLink";
e.childMenu="manageLocales";
e.activateMenu();
i.i18Locales="Locales.";
i.templateUrl="templates/messages.html";
i.categoryTreeId={};
i.categoryList=[];
i.$on("$viewContentLoaded",function(){i.categoryList.push({id:"LABEL",displayName:"Labels",children:null,parentId:null,parentName:null});
i.categoryList.push({id:"MESSAGE",displayName:"Messages",children:null,parentId:null,parentName:null});
i.categoryList.push({id:"MASTER",displayName:"Masters",children:null,parentId:null,parentName:null});
i.categoryList.push({id:"REPORT",displayName:"Reports",children:null,parentId:null,parentName:null});
i.categoryList.push({id:"NOTIFICATION",displayName:"Notifications",children:null,parentId:null,parentName:null});
i.categoryList.push({id:"CONTENT",displayName:"Content",children:null,parentId:null,parentName:null});
k();
f()
});
function f(){var l=0;
i.categoryTreeId.currentNode=i.categoryList[l];
i.categoryTreeId.currentNode.selected="selected";
g(i.categoryList[l]);
i.setClickedLetter(i.alphabets[0])
}i.openSelectedCategoryPage=function(l){if(angular.isDefined(l)){g(l);
if(l.id=="MESSAGE"||l.id=="CONTENT"||l.id=="LABEL"||l.id=="MASTER"||l.id=="NOTIFICATION"||l.id=="REPORT"){i.clientSidePgintnOfLocales()
}}};
function g(l){i.selectedCategoryInTree=l
}i.alphabets=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
i.setClickedLetter=function(l){console.log("Clicked letter : "+l);
i.searchLetter=l;
i.localesList=[];
i.clientSidePgintnOfLocales()
};
function k(){var m=function(n){i.languageList=n.sort(e.predicateBy("name"));
angular.forEach(n,function(p,o){if(p.code==="EN"){i.selectedLanguage=p
}})
};
var l=function(n){console.log(" retrieveAllLanguage : failure")
};
j.retrieveAllLanguages(null,m,l)
}i.updateTranslation=function(){var m=function(n){};
var l=function(n){};
j.updateAllLocales(i.localesList,m,l)
};
i.showAllLabels=function(){i.showAll=true;
i.clientSidePgintnOfLocales()
};
i.clientSidePgintnOfLocales=function(){i.isDisplayTable=false;
var m={};
m.type=i.selectedCategoryInTree.id;
if(i.showAll==true){m.searchText=""
}else{m.searchText=i.searchLetter
}i.showAll=false;
m.languageDataBean=i.selectedLanguage;
if(!angular.isDefined(i.selectedLanguage)){m.languageDataBean={};
m.languageDataBean.code="EN";
m.languageDataBean.country="IN";
m.languageDataBean.name="English"
}if(i.selectedCategoryInTree.id=="CONTENT"){m.entity=i.selectedContentType.toUpperCase()
}else{if(i.selectedCategoryInTree.id=="REPORT"){m.entity="report"
}}if(i.selectedCategoryInTree.id=="NOTIFICATION"){m.searchText=""
}var n=function(p){e.unMaskLoading();
i.localesList=p;
i.shortLabel;
for(var o=0;
o<i.localesList.length;
o++){var q=i.localesList[o].defaultText;
i.shortLabel=q.slice(0,45);
i.localesList[o].shortLabel=i.shortLabel
}if(p.length>0){i.isDisplayTable=true
}};
var l=function(o){console.log("failure")
};
e.maskLoading();
j.retrieveLocalesBySearchFields(m,n,l)
};
i.filterTypeahead=function(){var l=h("filter")(i.categoryList,{name:i.searchLocale});
if(l.length>0){i.isInValidSearchLocale=false
}else{i.isInValidSearchLocale=true
}};
i.initFnForContentTranslation=function(){console.log(" initFnForContentTranslation calling ");
i.showAll=true;
function l(){var n=function(o){i.contentTypeList=o.contentlist;
if(o&&o.contentlist.length>0){i.selectedContentType=o.contentlist[1];
i.clientSidePgintnOfLocales()
}};
var m=function(o){console.log("retrieveContentList failure")
};
j.retrieveContentTypeList(n,m)
}i.getListByFranchiseId=function(m){i.selectedFranchise=m;
i.clientSidePgintnOfLocales()
};
i.getListByContentType=function(m){i.selectedContentType=m;
i.clientSidePgintnOfLocales()
};
l()
};
i.initFnForMessageTranslation=function(){console.log(" initFnForMessageTranslation calling ")
};
i.initFnForMasterTranslation=function(){console.log(" initFnForMasterTranslation calling ")
};
i.initFnForReportTranslation=function(){i.showAllLabels()
};
i.initFnForNotificationTranslation=function(){i.clientSidePgintnOfLocales()
};
a.register.filter("localeFilter",function(){return function(l,p){if(angular.isDefined(l)){if(angular.isDefined(p)&&p!==""){var m=[];
for(var n=0;
n<l.length;
n++){var o=l[n];
if((o.defaultText!==null&&o.defaultText.toLowerCase().indexOf(p.toLowerCase())>=0)||(o.entity!==null&&o.entity.toLowerCase().indexOf(p.toLowerCase())>=0)||(o.text!==null&&o.text.toString().toLowerCase().indexOf(p.toLowerCase())>=0)){m.push(o)
}}return m
}else{return l
}}}
});
e.unMaskLoading()
}])
});