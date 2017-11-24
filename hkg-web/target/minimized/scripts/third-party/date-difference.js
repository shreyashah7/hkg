var DateDiff={inDays:function(d,c){var a=c.getTime();
var b=d.getTime();
return parseInt((a-b)/(24*3600*1000))
},inWeeks:function(d,c){var a=c.getTime();
var b=d.getTime();
return parseInt((a-b)/(24*3600*1000*7))
},inMonths:function(f,d){var a=f.getFullYear();
var c=d.getFullYear();
var b=f.getMonth();
var e=d.getMonth();
return(e+12*c)-(b+12*a)
},inYears:function(b,a){return a.getFullYear()-b.getFullYear()
}};