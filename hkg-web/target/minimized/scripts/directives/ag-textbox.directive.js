define(["angular"],function(){angular.module("hkg.directives").directive("agTextbox",["$parse","$compile",function(c,b){return{restrict:"E",link:function a(p,g,m){var n=$(g).closest("form").attr("name");
var q=m.agName;
var d=m.agId;
var k=m.required===undefined?false:true;
var o=m.reqMsg;
var j=m.validMsg;
var h=m.agModel;
var l=m.agGridClass;
var f="";
var e="";
var i="";
if(k){f="<div class='"+l+"' ng-class={'has-error':"+n+"."+q+".$invalid}>";
e="<input type=text class=form-control id="+d+" name="+q+" ng-model="+h+" required /> ";
i="<div class='error help-block' ng-show='"+n+"."+q+".$dirty && "+n+"."+q+".$invalid'><span class='help-block' ng-show='"+n+"."+q+".$error.required'>"+o+"</span><span class='help-block' ng-show="+n+"."+q+".$error.pattern>"+j+"</span> </div></div>"
}else{f="<div class='"+l+"'>";
e="<input type=text class=form-control id="+d+" name="+q+" ng-model="+h+" /> ";
i=""
}g.html("").append(f+e+i);
b(g.contents())(p)
}}
}])
});