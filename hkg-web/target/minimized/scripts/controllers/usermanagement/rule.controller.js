define(["hkg","ruleService","ruleField"],function(a,b){a.register.controller("RuleController",["$rootScope","$scope","RuleService","$log","$templateCache",function(e,g,f,h,d){e.maskLoading();
e.mainMenu="manageLink";
e.childMenu="manageRule";
e.activateMenu();
g.entity="RULE.";
g.editFlag=true;
g.submitted=false;
g.priorityList=[1,2,3,4,5,6,7,8,9,10];
g.displayFlag="view";
g.applyList=["all","any"];
g.componentType="";
g.htmltext=" ";
g.ruleObject={};
g.ruleList=[];
g.showtemplate=false;
g.entityList={};
g.operators={};
g.today=e.getCurrentServerDate();
g.$on("$viewContentLoaded",function(){c()
});
g.makeEditFlagFalse=function(){g.editFlag=false;
g.initAddRule()
};
var i=function(){var j={};
j.apply="all";
j.entity="";
j.field="";
j.operator="";
j.operatorList=[];
j.fieldList=[];
j.rowsubmitted=false;
j.priority=null;
j.values=[];
j.showtemplate=false;
return j
};
g.initAddRule=function(){if(!g.editFlag){g.ruleList=[];
g.editFlag=false;
g.ruleObject={};
g.ruleList=[];
var j=i();
g.ruleList.push(j);
g.ruleObject.remarks="";
g.ruleObject.ruleName="";
g.displayFlag="create";
f.retrievePrerequisite(function(k){g.entityList=k.entity;
console.log(g.entityList);
g.operators=k.operator
})
}};
function c(){f.retrievePrerequisite(function(j){console.log("-->>>"+JSON.stringify(j))
})
}g.onEntityChange=function(j){h.info(j+" index");
if(g.ruleList[j].entity!=null){console.log("entity--------------- "+g.ruleList[j].entity);
g.ruleList[j].operatorList=[];
var l=g.ruleList[j].entity;
var k={primaryKey:l};
f.retrieveFieldsByEntity('{"primaryKey": '+l+"}",function(m){g.ruleList[j].showtemplate=false;
h.info(JSON.stringify(m));
g.ruleList[j].fieldList=m
})
}};
g.onFieldChange=function(j){if(g.ruleList[j].field!=null){g.componentType=g.ruleList[j].field.type;
h.info("rule.field.shortcutCode"+g.ruleList[j].field.shortcutCode);
g.ruleList[j].operatorList=[];
h.info("field--------------- "+JSON.stringify(g.ruleList[j].field.type)+"    "+JSON.stringify(g.operators[g.componentType]));
g.ruleList[j].operatorList=g.operators[g.componentType];
g.ruleList[j].showtemplate=false
}};
g.onOperatorChange=function(j){g.ruleList[j].showtemplate=false;
if(g.ruleList[j].operator!=null&&g.componentType!=null){g.ruleList[j].showtemplate=true
}};
g.onOperatorClick=function(j){g.ruleList[j].showtemplate=false
};
g.addRule=function(n,k){g.ruleList[k].rowsubmitted=true;
if(n.$valid){var j=true;
var m=g.ruleList[k].operator.shortcutCode;
for(var l=0;
l<m;
l++){var p=g.ruleList[k].values[l];
if(p!==null&&p!==undefined&&p.trim()!==""){j=(j&&true)
}else{j=false
}}if(j){var o=i();
g.ruleList.push(o)
}}};
g.remove=function(j){g.ruleList.splice(j,1);
if(g.ruleList.length==0){var k=i();
g.ruleList.push(k);
g.ruleList[j].rowsubmitted=false
}};
g.save=function(l){g.submitted=true;
var n=[];
var k=g.ruleList.length;
for(var j=0;
j<k;
j++){g.ruleList[j].rowsubmitted=true;
var o=angular.copy(g.ruleList[j]);
var m={};
m.id=j;
m.priority=o.priority;
m.entity=o.entity;
m.field=o.field.id;
m.operator=o.operator.label;
m.fieldType=o.field.type;
m.value=o.values[0];
if(o.values.length>1){m.value1=o.values[1]
}n.push(m)
}if(l.$valid){alert("hello");
g.ruleObject.rules=n;
h.info(JSON.stringify(g.ruleObject));
f.createRule(g.ruleObject,function(p){h.info(JSON.stringify(p))
});
g.submitted=false
}};
g.ruleCancel=function(j){j.$setPristine();
g.initAddRule()
};
e.unMaskLoading()
}])
});