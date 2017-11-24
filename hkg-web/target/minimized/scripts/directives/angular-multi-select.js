define(["angular"],function(){angular.module("hkg.directives").directive("multiSelect",["$sce","$timeout",function(a,b){return{restrict:"AE",replace:true,scope:{inputModel:"=",outputModel:"=",buttonLabel:"@",defaultLabel:"@",directiveId:"@",helperElements:"@",isDisabled:"=",itemLabel:"@",maxLabels:"@",orientation:"@",selectionMode:"@",tickProperty:"@",disableProperty:"@",groupProperty:"@",maxHeight:"@",onClose:"&",onItemClick:"&",onOpen:"&",onReset:"&",onSelectAll:"&",onSelectNone:"&"},template:'<span class="multiSelect inlineBlock"><button type="button" class="button multiSelectButton" ng-click="toggleCheckboxes( $event ); refreshSelectedItems(); refreshButton();" ng-bind-html="varButtonLabel"></button><div class="checkboxLayer"><form><div class="helperContainer" ng-if="displayHelper( \'filter\' ) || displayHelper( \'all\' ) || displayHelper( \'none\' ) || displayHelper( \'reset\' )"><div ng-if="displayHelper( \'all\' ) || displayHelper( \'none\' ) || displayHelper( \'reset\' )"><button type="button" ng-click="select( \'all\',   $event );"    class="helperButton" ng-if="!isDisabled && displayHelper( \'all\' )">   &#10003;&nbsp; Select All</button> <button type="button" ng-click="select( \'none\',  $event );"   class="helperButton" ng-if="!isDisabled && displayHelper( \'none\' )">  &times;&nbsp; Select None</button>&nbsp;<button type="button" ng-click="select( \'reset\', $event );"  class="helperButton" ng-if="!isDisabled && displayHelper( \'reset\' )" style="float:right">&#8630;&nbsp; Reset</button></div><div style="position:relative" ng-if="displayHelper( \'filter\' )"><input placeholder="Search..." type="text" ng-click="select( \'filter\', $event )" ng-model="inputLabel.labelFilter" ng-change="updateFilter();$scope.getFormElements();" class="inputFilter" /><button type="button" class="clearButton" ng-click="inputLabel.labelFilter=\'\';updateFilter();prepareGrouping();prepareIndex();select( \'clear\', $event )">&times;</button> </div></div><div class="checkBoxContainer" style="{{setHeight();}}"><div ng-repeat="item in filteredModel | filter:removeGroupEndMarker" class="multiSelectItem"ng-class="{selected: item[ tickProperty ], horizontal: orientationH, vertical: orientationV, multiSelectGroup:item[ groupProperty ], disabled:itemIsDisabled( item )}"ng-click="syncItems( item, $event, $index );"ng-mouseleave="removeFocusStyle( tabIndex );"><div class="acol" ng-if="item[ spacingProperty ] > 0" ng-repeat="i in numberToArray( item[ spacingProperty ] ) track by $index">&nbsp;</div><div class="acol"><label><input class="checkbox focusable" type="checkbox" ng-disabled="itemIsDisabled( item )" ng-checked="item[ tickProperty ]" ng-click="syncItems( item, $event, $index )" /><span ng-class="{disabled:itemIsDisabled( item )}" ng-bind-html="writeLabel( item, \'itemLabel\' )"></span></label></div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="tickMark" ng-if="item[ groupProperty ] !== true && item[ tickProperty ] === true">&#10004;</span></div></div><form></div></span>',link:function(d,f,e){d.backUp=[];
d.varButtonLabel="";
d.scrolled=false;
d.spacingProperty="";
d.indexProperty="";
d.checkBoxLayer="";
d.orientationH=false;
d.orientationV=true;
d.filteredModel=[];
d.inputLabel={labelFilter:""};
d.selectedItems=[];
d.formElements=[];
d.tabIndex=0;
d.clickedItem=null;
prevTabIndex=0;
helperItems=[];
helperItemsLength=0;
d.setHeight=function(){if(typeof d.maxHeight!=="undefined"){return"max-height: "+d.maxHeight+"; overflow-y:scroll"
}};
d.numberToArray=function(g){return new Array(g)
};
d.updateFilter=function(){d.filteredModel=[];
var j=0;
if(typeof d.inputModel==="undefined"){return[]
}for(j=d.inputModel.length-1;
j>=0;
j--){if(typeof d.inputModel[j][d.groupProperty]!=="undefined"&&d.inputModel[j][d.groupProperty]===false){d.filteredModel.push(d.inputModel[j])
}var g=false;
if(typeof d.inputModel[j][d.groupProperty]==="undefined"){for(var h in d.inputModel[j]){if(typeof d.inputModel[j][h]!=="boolean"&&String(d.inputModel[j][h]).toUpperCase().indexOf(d.inputLabel.labelFilter.toUpperCase())>=0){g=true;
break
}}if(g===true){d.filteredModel.push(d.inputModel[j])
}}if(typeof d.inputModel[j][d.groupProperty]!=="undefined"&&d.inputModel[j][d.groupProperty]===true){if(typeof d.filteredModel[d.filteredModel.length-1][d.groupProperty]!=="undefined"&&d.filteredModel[d.filteredModel.length-1][d.groupProperty]===false){d.filteredModel.pop()
}else{d.filteredModel.push(d.inputModel[j])
}}}d.filteredModel.reverse();
b(function(){d.getFormElements()
},0)
};
d.getFormElements=function(){d.formElements=[];
for(var g=0;
g<f[0].getElementsByTagName("FORM")[0].elements.length;
g++){d.formElements.push(f[0].getElementsByTagName("FORM")[0].elements[g])
}};
d.isGroupMarker=function(h,g){if(typeof h[d.groupProperty]!=="undefined"&&h[d.groupProperty]===g){return true
}return false
};
d.removeGroupEndMarker=function(g){if(typeof g[d.groupProperty]!=="undefined"&&g[d.groupProperty]===false){return false
}return true
};
d.displayHelper=function(g){if(typeof e.helperElements==="undefined"){return true
}switch(g.toUpperCase()){case"ALL":if(e.selectionMode&&d.selectionMode.toUpperCase()==="SINGLE"){return false
}else{if(e.helperElements&&d.helperElements.toUpperCase().indexOf("ALL")>=0){return true
}}break;
case"NONE":if(e.selectionMode&&d.selectionMode.toUpperCase()==="SINGLE"){return false
}else{if(e.helperElements&&d.helperElements.toUpperCase().indexOf("NONE")>=0){return true
}}break;
case"RESET":if(e.helperElements&&d.helperElements.toUpperCase().indexOf("RESET")>=0){return true
}break;
case"FILTER":if(e.helperElements&&d.helperElements.toUpperCase().indexOf("FILTER")>=0){return true
}break;
default:return false;
break
}};
d.syncItems=function(t,p,g){p.preventDefault();
p.stopPropagation();
if(typeof e.disableProperty!=="undefined"&&t[d.disableProperty]===true){return false
}if(typeof e.isDisabled!=="undefined"&&d.isDisabled===true){return false
}if(typeof t[d.groupProperty]!=="undefined"&&t[d.groupProperty]===false){return false
}index=d.filteredModel.indexOf(t);
if(typeof t[d.groupProperty]!=="undefined"&&t[d.groupProperty]===true){if(e.selectionMode&&d.selectionMode.toUpperCase()==="SINGLE"){return false
}var n,m,l;
var s=0;
var o=d.filteredModel.length-1;
var r=[];
var h=0;
for(n=index;
n<d.filteredModel.length;
n++){if(h===0&&n>index){break
}if(typeof d.filteredModel[n][d.groupProperty]!=="undefined"&&d.filteredModel[n][d.groupProperty]===true){if(r.length===0){s=n+1
}h=h+1
}else{if(typeof d.filteredModel[n][d.groupProperty]!=="undefined"&&d.filteredModel[n][d.groupProperty]===false){h=h-1;
if(r.length>0&&h===0){var q=true;
o=n;
for(m=0;
m<r.length;
m++){if(typeof r[m][d.tickProperty]!=="undefined"&&r[m][d.tickProperty]===false){q=false;
break
}}if(q===true){for(m=s;
m<=o;
m++){if(typeof d.filteredModel[m][d.groupProperty]==="undefined"){if(typeof e.disableProperty==="undefined"){d.filteredModel[m][d.tickProperty]=false;
inputModelIndex=d.filteredModel[m][d.indexProperty];
d.inputModel[inputModelIndex][d.tickProperty]=false
}else{if(d.filteredModel[m][d.disableProperty]!==true){d.filteredModel[m][d.tickProperty]=false;
inputModelIndex=d.filteredModel[m][d.indexProperty];
d.inputModel[inputModelIndex][d.tickProperty]=false
}}}}}else{for(m=s;
m<=o;
m++){if(typeof d.filteredModel[m][d.groupProperty]==="undefined"){if(typeof e.disableProperty==="undefined"){d.filteredModel[m][d.tickProperty]=true;
inputModelIndex=d.filteredModel[m][d.indexProperty];
d.inputModel[inputModelIndex][d.tickProperty]=true
}else{if(d.filteredModel[m][d.disableProperty]!==true){d.filteredModel[m][d.tickProperty]=true;
inputModelIndex=d.filteredModel[m][d.indexProperty];
d.inputModel[inputModelIndex][d.tickProperty]=true
}}}}}}}else{r.push(d.filteredModel[n])
}}}}else{d.filteredModel[index][d.tickProperty]=!d.filteredModel[index][d.tickProperty];
inputModelIndex=d.filteredModel[index][d.indexProperty];
d.inputModel[inputModelIndex][d.tickProperty]=d.filteredModel[index][d.tickProperty];
if(e.selectionMode&&d.selectionMode.toUpperCase()==="SINGLE"){d.filteredModel[index][d.tickProperty]=true;
for(n=0;
n<d.filteredModel.length;
n++){if(n!==index){d.filteredModel[n][d.tickProperty]=false
}}d.toggleCheckboxes(p)
}}d.clickedItem=angular.copy(t);
prevTabIndex=d.tabIndex;
d.tabIndex=g+helperItemsLength;
p.target.focus();
d.removeFocusStyle(prevTabIndex);
d.setFocusStyle(d.tabIndex)
};
d.refreshSelectedItems=function(){d.selectedItems=[];
angular.forEach(d.inputModel,function(h,g){if(typeof h!=="undefined"){if(typeof h[d.groupProperty]==="undefined"){if(h[d.tickProperty]===true){d.selectedItems.push(h)
}}}})
};
d.refreshOutputModel=function(){if(typeof e.outputModel!=="undefined"){d.outputModel=angular.copy(d.selectedItems);
angular.forEach(d.outputModel,function(h,g){delete h[d.indexProperty];
delete h[d.spacingProperty]
})
}};
d.refreshButton=function(){d.varButtonLabel="";
ctr=0;
if(d.selectedItems.length===0){d.varButtonLabel=(typeof d.defaultLabel!=="undefined")?d.defaultLabel:"None selected"
}else{var g=d.selectedItems.length;
if(typeof d.maxLabels!=="undefined"&&d.maxLabels!==""){g=d.maxLabels
}if(d.selectedItems.length>g){d.more=true
}else{d.more=false
}angular.forEach(d.selectedItems,function(j,h){if(typeof j!=="undefined"){if(ctr<g){d.varButtonLabel+=(d.varButtonLabel.length>0?'</div>, <div class="buttonLabel">':'<div class="buttonLabel">')+d.writeLabel(j,"buttonLabel")
}ctr++
}});
if(d.more===true){if(g>0){d.varButtonLabel+=", ... "
}d.varButtonLabel+="(Total: "+d.selectedItems.length+")"
}}d.varButtonLabel=a.trustAsHtml(d.varButtonLabel+'<span class="caret"></span>')
};
d.itemIsDisabled=function(g){if(typeof e.disableProperty!=="undefined"&&g[d.disableProperty]===true){return true
}else{if(d.isDisabled===true){return true
}else{return false
}}};
d.writeLabel=function(k,j){var h="";
var g=d[j].split(" ");
angular.forEach(g,function(l,m){if(typeof l!=="undefined"){angular.forEach(k,function(n,o){if(o==l){h+="&nbsp;"+n
}})
}});
if(j.toUpperCase()==="BUTTONLABEL"){return h
}return a.trustAsHtml(h)
};
d.toggleCheckboxes=function(g){d.checkBoxLayer=f.children()[1];
clickedEl=f.children()[0];
angular.element(document).unbind("click",d.externalClickListener);
angular.element(document).unbind("keydown",d.keyboardListener);
d.inputLabel.labelFilter="";
d.updateFilter();
if(g.keyCode===27){angular.element(d.checkBoxLayer).removeClass("show");
angular.element(clickedEl).removeClass("buttonClicked");
angular.element(document).unbind("click",d.externalClickListener);
angular.element(document).unbind("keydown",d.keyboardListener);
d.removeFocusStyle(d.tabIndex);
d.onClose({data:f});
return true
}if(angular.element(d.checkBoxLayer).hasClass("show")){angular.element(d.checkBoxLayer).removeClass("show");
angular.element(clickedEl).removeClass("buttonClicked");
angular.element(document).unbind("click",d.externalClickListener);
angular.element(document).unbind("keydown",d.keyboardListener);
d.removeFocusStyle(d.tabIndex);
d.onClose({data:f})
}else{helperItems=[];
helperItemsLength=0;
angular.element(d.checkBoxLayer).addClass("show");
angular.element(clickedEl).addClass("buttonClicked");
angular.element(document).bind("click",d.externalClickListener);
angular.element(document).bind("keydown",d.keyboardListener);
d.getFormElements();
d.tabIndex=0;
var h=angular.element(f[0].querySelector(".helperContainer"))[0];
if(typeof h!=="undefined"){for(i=0;
i<h.getElementsByTagName("BUTTON").length;
i++){helperItems[i]=h.getElementsByTagName("BUTTON")[i]
}helperItemsLength=helperItems.length+h.getElementsByTagName("INPUT").length
}if(f[0].querySelector(".inputFilter")){f[0].querySelector(".inputFilter").focus();
d.tabIndex=d.tabIndex+helperItemsLength-2
}else{d.formElements[d.tabIndex].focus()
}d.onOpen({data:f})
}};
d.externalClickListener=function(h){targetsArr=f.find(h.target.tagName);
for(var g=0;
g<targetsArr.length;
g++){if(h.target==targetsArr[g]){return
}}angular.element(d.checkBoxLayer.previousSibling).removeClass("buttonClicked");
angular.element(d.checkBoxLayer).removeClass("show");
angular.element(document).unbind("click",d.externalClickListener);
angular.element(document).unbind("keydown",d.keyboardListener);
b(function(){d.onClose({data:f})
},0)
};
d.findUpTag=function(j,g,h){while(j.parentNode){j=j.parentNode;
if(typeof j.tagName!=="undefined"){if(j.tagName.toUpperCase()===g.toUpperCase()&&j.className.indexOf(h)>-1){return j
}}}return null
};
d.select=function(g,h){helperIndex=helperItems.indexOf(h.target);
d.tabIndex=helperIndex;
switch(g.toUpperCase()){case"ALL":angular.forEach(d.filteredModel,function(k,j){if(typeof k!=="undefined"&&k[d.disableProperty]!==true){if(typeof k[d.groupProperty]==="undefined"){k[d.tickProperty]=true
}}});
d.refreshOutputModel();
d.refreshButton();
d.onSelectAll();
break;
case"NONE":angular.forEach(d.filteredModel,function(k,j){if(typeof k!=="undefined"&&k[d.disableProperty]!==true){if(typeof k[d.groupProperty]==="undefined"){k[d.tickProperty]=false
}}});
d.refreshOutputModel();
d.refreshButton();
d.onSelectNone();
break;
case"RESET":angular.forEach(d.filteredModel,function(k,j){if(typeof k[d.groupProperty]==="undefined"&&typeof k!=="undefined"&&k[d.disableProperty]!==true){temp=k[d.indexProperty];
k[d.tickProperty]=d.backUp[temp][d.tickProperty]
}});
d.refreshOutputModel();
d.refreshButton();
d.onReset();
break;
case"CLEAR":d.tabIndex=d.tabIndex+1;
break;
case"FILTER":d.tabIndex=helperItems.length-1;
break;
default:}};
genRandomString=function(k){var h="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
var g="";
for(var j=0;
j<k;
j++){g+=h.charAt(Math.floor(Math.random()*h.length))
}return g
};
d.prepareGrouping=function(){var g=0;
angular.forEach(d.filteredModel,function(j,h){j[d.spacingProperty]=g;
if(j[d.groupProperty]===true){g+=2
}else{if(j[d.groupProperty]===false){g-=2
}}})
};
d.prepareIndex=function(){ctr=0;
angular.forEach(d.filteredModel,function(h,g){h[d.indexProperty]=ctr;
ctr++
})
};
d.keyboardListener=function(k){var h=k.keyCode?k.keyCode:k.which;
var j=false;
if(h===27){d.toggleCheckboxes(k)
}else{if(h===40||h===39||(!k.shiftKey&&h==9)){j=true;
prevTabIndex=d.tabIndex;
d.tabIndex++;
if(d.tabIndex>d.formElements.length-1){d.tabIndex=0;
prevTabIndex=d.formElements.length-1
}while(d.formElements[d.tabIndex].disabled===true){d.tabIndex++;
if(d.tabIndex>d.formElements.length-1){d.tabIndex=0
}}}else{if(h===38||h===37||(k.shiftKey&&h==9)){j=true;
prevTabIndex=d.tabIndex;
d.tabIndex--;
if(d.tabIndex<0){d.tabIndex=d.formElements.length-1;
prevTabIndex=0
}while(d.formElements[d.tabIndex].disabled===true){d.tabIndex--;
if(d.tabIndex<0){d.tabIndex=d.formElements.length-1
}}}}}if(j===true){k.preventDefault();
k.stopPropagation();
d.formElements[d.tabIndex].focus();
var g=document.activeElement;
if(g.type.toUpperCase()==="CHECKBOX"){d.setFocusStyle(d.tabIndex);
d.removeFocusStyle(prevTabIndex)
}else{d.removeFocusStyle(prevTabIndex);
d.removeFocusStyle(helperItemsLength);
d.removeFocusStyle(d.formElements.length-1)
}}j=false
};
d.setFocusStyle=function(g){angular.element(d.formElements[g]).parent().parent().parent().addClass("multiSelectFocus")
};
d.removeFocusStyle=function(g){angular.element(d.formElements[g]).parent().parent().parent().removeClass("multiSelectFocus")
};
var c=genRandomString(5);
d.indexProperty="idx_"+c;
d.spacingProperty="spc_"+c;
if(typeof e.orientation!=="undefined"){if(e.orientation.toUpperCase()==="HORIZONTAL"){d.orientationH=true;
d.orientationV=false
}else{d.orientationH=false;
d.orientationV=true
}}d.$watch("inputModel",function(g){if(g){d.refreshSelectedItems();
d.refreshOutputModel();
d.refreshButton();
if(d.clickedItem!==null){b(function(){d.onItemClick({data:d.clickedItem});
d.clickedItem=null
},0)
}}},true);
d.$watch("inputModel",function(g){if(g){d.backUp=angular.copy(d.inputModel);
d.updateFilter();
d.prepareGrouping();
d.prepareIndex();
d.refreshSelectedItems();
d.refreshOutputModel();
d.refreshButton()
}});
d.$watch("isDisabled",function(g){d.isDisabled=g
});
angular.element(document).bind("touchstart",function(g){d.$apply(function(){d.scrolled=false
})
});
angular.element(document).bind("touchmove",function(g){d.$apply(function(){d.scrolled=true
})
});
if(!Array.prototype.indexOf){Array.prototype.indexOf=function(j,h){h=h||0;
var g=this.length;
while(h<g){if(this[h]===j){return h
}++h
}return -1
}
}}}
}])
});