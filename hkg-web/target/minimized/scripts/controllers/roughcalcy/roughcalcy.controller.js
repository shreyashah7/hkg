define(["hkg","dynamicForm","rapCalcyDirective","customFieldService"],function(a){a.register.controller("RoughCalcyController",["$rootScope","$scope","DynamicFormService","CustomFieldService","RapCalcyService",function(b,d,c,f,e){b.mainMenu="stockLink";
b.childMenu="roughCalcy";
b.activateMenu();
d.calc={};
d.calc.discountDetailsMap={};
d.conditions=[{cond:{"carat_type$DRP$Long":"Expected","size$DRP$long":0.001,"fluroscene$DRP$String":18,"clarity$DRP$String":9,"color$DRP$String":14},def:{field1$DRP$Long:{id:28,text:"3-F3"}}}];
f.retrieveDesignationBasedFields("calcyRough",function(g){var h=c.retrieveSectionWiseCustomFieldInfo("roughCalcyEntity");
h.then(function(k){d.generaRoughCalcyTemplate=k.genralSection;
var j=[];
var i=Object.keys(g).map(function(l,m){angular.forEach(this[l],function(n){if(l==="RoughCalcy"){j.push({RoughCalcy:n})
}})
},g);
d.generaRoughCalcyTemplate=c.retrieveCustomData(d.generaRoughCalcyTemplate,j)
},g)
},function(){b.unMaskLoading();
var h="Failed to retrieve data";
var g=b.error;
b.addMessage(h,g)
});
d.calculateRate=function(){var j=true;
d.calc.fourCMap={};
for(var g=0;
g<b.fourCMap.length;
g++){var h=d.calc.discountDetailsMap[b.fourCMap[g]];
if(!h){j=false;
break
}d.calc.fourCMap[b.fourCMap[g]]=h
}if(j){e.calculateDiamondPrice(d.calc,function(i){d.calcFinal=i.data;
if(d.calcFinal.baseAmount==null){d.calcFinal.baseAmount=0
}if(d.calcFinal.discount==null){d.calcFinal.discount=0
}if(d.calcFinal.amount==null){d.calcFinal.amount=0
}if(d.calcFinal.mixAmount==null){d.calcFinal.mixAmount=0
}})
}};
d.reset=function(){var g=angular.element("#field1").scope();
g.dropdown.push({id:5,text:"6-Raj"})
}
}])
});