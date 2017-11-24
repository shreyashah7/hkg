var screensize="(max-width: 600px)";
var $body=document.body;
$(function(){var b=document.body,a=b.getElementsByClassName("menu-trigger")[0];
if(typeof a!=="undefined"){a.addEventListener("click",function(){var d=window.matchMedia(screensize);
var c=$(window).width();
if(!d.matches){if(b.className.match("menu-active")){b.className="";
$("#content").addClass("col-xs-12");
$("#content").removeClass("col-xs-10");
$("#content").removeClass("col-xs-offset-2");
$("#content").addClass("slide-transition");
$("#leftmenu").addClass("left-col-hide");
$("#leftmenu").addClass("slide-transition")
}else{b.className="menu-active";
$("#content").removeClass("col-xs-12");
$("#content").addClass("col-xs-10");
$("#content").addClass(" col-xs-offset-2");
$("#content").addClass("slide-transition");
$("#leftmenu").removeClass("left-col-hide");
$("#leftmenu").addClass("slide-transition")
}}else{if(b.className.match("menu-active")){b.className="";
$("#content").addClass("col-xs-12");
$("#content").removeClass("col-xs-10");
$("#content").removeClass("col-xs-offset-2");
$("#content").addClass("slide-transition");
$("#leftmenu").addClass("left-col-hide");
$("#leftmenu").addClass("slide-transition")
}else{b.className="menu-active";
$("#content").removeClass("col-xs-12");
$("#content").addClass("col-xs-10");
$("#content").addClass(" col-xs-offset-2");
$("#content").addClass("slide-transition");
$("#leftmenu").removeClass("left-col-hide");
$("#leftmenu").addClass("slide-transition")
}}})
}});
$("#Notifications").popover({placement:"bottom",container:"body",html:true,content:function(){return $(this).next(".popper-content").html()
}});
$("#messages").popover({placement:"bottom",container:"body",html:true,content:function(){return $(this).next(".popper-content").html()
}});
$("#readmessages").popover({placement:"bottom",container:"body",html:true,content:function(){return $(this).next(".popper-content").html()
}});
$("body").on("click",function(a){$('[rel="popover"]').each(function(){if(!$(this).is(a.target)&&$(this).has(a.target).length===0&&$(".popover").has(a.target).length===0){$(this).popover("hide")
}})
});
function resetMenu(){if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}$("a#dashboardLink").removeClass("active")
}$("#dashboard").on("click",function(){if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}$("a#dashboardLink").addClass("active")
});
$("#collapseStock").on("show.bs.collapse",function(){$("a span.stock").removeClass("glyphicon-plus").addClass("glyphicon-minus");
if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}$("a#stockLink").addClass("active")
});
$("#collapseStock").on("hide.bs.collapse",function(){$("a span.stock").removeClass("glyphicon-minus").addClass("glyphicon-plus");
$("a#stockLink").removeClass("active");
if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}});
$("#collapseManage").on("show.bs.collapse",function(){$("a span.manage").removeClass("glyphicon-plus").addClass("glyphicon-minus");
if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}$("a#manageLink").addClass("active")
});
$("#collapseManage").on("hide.bs.collapse",function(){$("a span.manage").removeClass("glyphicon-minus").addClass("glyphicon-plus");
$("a#manageLink").removeClass("active");
if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#reportLink").hasClass("active")){$("a#reportLink").click();
$("a#reportLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}});
$("#collapseReport").on("show.bs.collapse",function(){$("a span.report").removeClass("glyphicon-plus").addClass("glyphicon-minus");
if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}$("a#reportLink").addClass("active")
});
$("#collapseReport").on("hide.bs.collapse",function(){$("a span.report").removeClass("glyphicon-minus").addClass("glyphicon-plus");
$("a#reportLink").removeClass("active");
if($("a#stockLink").hasClass("active")){$("a#stockLink").click();
$("a#stockLink").removeClass("active")
}if($("a#manageLink").hasClass("active")){$("a#manageLink").click();
$("a#manageLink").removeClass("active")
}if($("a#dashboardLink").hasClass("active")){$("a#dashboardLink").removeClass("active")
}});
if(matchMedia){var mq=window.matchMedia(screensize);
mq.addListener(WidthChange);
WidthChange(mq)
}function WidthChange(a){var b=(a.matches?"more":"less")+" than 500 pixels";
if(!a.matches){$body.className="menu-active";
$("#leftmenutablet").hide();
$("#leftmenu").show();
$("#tablogo").hide();
$("#weblogo").show();
$("#topheader").removeClass("col-xs-11");
$("#topheader").addClass("col-xs-10");
$("#content").removeClass("col-xs-11");
$("#content").removeClass("col-xs-offset-1");
$("#content").addClass("col-xs-10");
$("#content").addClass("col-xs-offset-2");
$("#leftmenu").removeClass("left-col-hide");
$("#leftmenu").removeClass("slide-transition")
}else{$body.className="menu-active";
$("#leftmenutablet").hide();
$("#leftmenu").show();
$("#tablogo").hide();
$("#weblogo").show();
$("#topheader").removeClass("col-xs-11");
$("#topheader").addClass("col-xs-10");
$("#content").removeClass("col-xs-11");
$("#content").removeClass("col-xs-offset-1");
$("#content").addClass("col-xs-10");
$("#content").addClass("col-xs-offset-2");
$("#leftmenu").removeClass("left-col-hide");
$("#leftmenu").removeClass("slide-transition")
}};