(function(a){if(typeof a.fn.each2=="undefined"){a.extend(a.fn,{each2:function(f){var d=a([0]),e=-1,b=this.length;
while(++e<b&&(d.context=d[0]=this[e])&&f.call(d[0],e,d)!==false){}return this
}})
}})(jQuery);
(function(E,m){if(window.Select2!==m){return
}var L,O,y,c,a,q,o={x:0,y:0},w,x,L={TAB:9,ENTER:13,ESC:27,SPACE:32,LEFT:37,UP:38,RIGHT:39,DOWN:40,SHIFT:16,CTRL:17,ALT:18,PAGE_UP:33,PAGE_DOWN:34,HOME:36,END:35,BACKSPACE:8,DELETE:46,isArrow:function(P){P=P.which?P.which:P;
switch(P){case L.LEFT:case L.RIGHT:case L.UP:case L.DOWN:return true
}return false
},isControl:function(Q){var P=Q.which;
switch(P){case L.SHIFT:case L.CTRL:case L.ALT:return true
}if(Q.metaKey){return true
}return false
},isFunctionKey:function(P){P=P.which?P.which:P;
return P>=112&&P<=123
}},C="<div class='select2-measure-scrollbar'></div>",d={"\u24B6":"A","\uFF21":"A","\u00C0":"A","\u00C1":"A","\u00C2":"A","\u1EA6":"A","\u1EA4":"A","\u1EAA":"A","\u1EA8":"A","\u00C3":"A","\u0100":"A","\u0102":"A","\u1EB0":"A","\u1EAE":"A","\u1EB4":"A","\u1EB2":"A","\u0226":"A","\u01E0":"A","\u00C4":"A","\u01DE":"A","\u1EA2":"A","\u00C5":"A","\u01FA":"A","\u01CD":"A","\u0200":"A","\u0202":"A","\u1EA0":"A","\u1EAC":"A","\u1EB6":"A","\u1E00":"A","\u0104":"A","\u023A":"A","\u2C6F":"A","\uA732":"AA","\u00C6":"AE","\u01FC":"AE","\u01E2":"AE","\uA734":"AO","\uA736":"AU","\uA738":"AV","\uA73A":"AV","\uA73C":"AY","\u24B7":"B","\uFF22":"B","\u1E02":"B","\u1E04":"B","\u1E06":"B","\u0243":"B","\u0182":"B","\u0181":"B","\u24B8":"C","\uFF23":"C","\u0106":"C","\u0108":"C","\u010A":"C","\u010C":"C","\u00C7":"C","\u1E08":"C","\u0187":"C","\u023B":"C","\uA73E":"C","\u24B9":"D","\uFF24":"D","\u1E0A":"D","\u010E":"D","\u1E0C":"D","\u1E10":"D","\u1E12":"D","\u1E0E":"D","\u0110":"D","\u018B":"D","\u018A":"D","\u0189":"D","\uA779":"D","\u01F1":"DZ","\u01C4":"DZ","\u01F2":"Dz","\u01C5":"Dz","\u24BA":"E","\uFF25":"E","\u00C8":"E","\u00C9":"E","\u00CA":"E","\u1EC0":"E","\u1EBE":"E","\u1EC4":"E","\u1EC2":"E","\u1EBC":"E","\u0112":"E","\u1E14":"E","\u1E16":"E","\u0114":"E","\u0116":"E","\u00CB":"E","\u1EBA":"E","\u011A":"E","\u0204":"E","\u0206":"E","\u1EB8":"E","\u1EC6":"E","\u0228":"E","\u1E1C":"E","\u0118":"E","\u1E18":"E","\u1E1A":"E","\u0190":"E","\u018E":"E","\u24BB":"F","\uFF26":"F","\u1E1E":"F","\u0191":"F","\uA77B":"F","\u24BC":"G","\uFF27":"G","\u01F4":"G","\u011C":"G","\u1E20":"G","\u011E":"G","\u0120":"G","\u01E6":"G","\u0122":"G","\u01E4":"G","\u0193":"G","\uA7A0":"G","\uA77D":"G","\uA77E":"G","\u24BD":"H","\uFF28":"H","\u0124":"H","\u1E22":"H","\u1E26":"H","\u021E":"H","\u1E24":"H","\u1E28":"H","\u1E2A":"H","\u0126":"H","\u2C67":"H","\u2C75":"H","\uA78D":"H","\u24BE":"I","\uFF29":"I","\u00CC":"I","\u00CD":"I","\u00CE":"I","\u0128":"I","\u012A":"I","\u012C":"I","\u0130":"I","\u00CF":"I","\u1E2E":"I","\u1EC8":"I","\u01CF":"I","\u0208":"I","\u020A":"I","\u1ECA":"I","\u012E":"I","\u1E2C":"I","\u0197":"I","\u24BF":"J","\uFF2A":"J","\u0134":"J","\u0248":"J","\u24C0":"K","\uFF2B":"K","\u1E30":"K","\u01E8":"K","\u1E32":"K","\u0136":"K","\u1E34":"K","\u0198":"K","\u2C69":"K","\uA740":"K","\uA742":"K","\uA744":"K","\uA7A2":"K","\u24C1":"L","\uFF2C":"L","\u013F":"L","\u0139":"L","\u013D":"L","\u1E36":"L","\u1E38":"L","\u013B":"L","\u1E3C":"L","\u1E3A":"L","\u0141":"L","\u023D":"L","\u2C62":"L","\u2C60":"L","\uA748":"L","\uA746":"L","\uA780":"L","\u01C7":"LJ","\u01C8":"Lj","\u24C2":"M","\uFF2D":"M","\u1E3E":"M","\u1E40":"M","\u1E42":"M","\u2C6E":"M","\u019C":"M","\u24C3":"N","\uFF2E":"N","\u01F8":"N","\u0143":"N","\u00D1":"N","\u1E44":"N","\u0147":"N","\u1E46":"N","\u0145":"N","\u1E4A":"N","\u1E48":"N","\u0220":"N","\u019D":"N","\uA790":"N","\uA7A4":"N","\u01CA":"NJ","\u01CB":"Nj","\u24C4":"O","\uFF2F":"O","\u00D2":"O","\u00D3":"O","\u00D4":"O","\u1ED2":"O","\u1ED0":"O","\u1ED6":"O","\u1ED4":"O","\u00D5":"O","\u1E4C":"O","\u022C":"O","\u1E4E":"O","\u014C":"O","\u1E50":"O","\u1E52":"O","\u014E":"O","\u022E":"O","\u0230":"O","\u00D6":"O","\u022A":"O","\u1ECE":"O","\u0150":"O","\u01D1":"O","\u020C":"O","\u020E":"O","\u01A0":"O","\u1EDC":"O","\u1EDA":"O","\u1EE0":"O","\u1EDE":"O","\u1EE2":"O","\u1ECC":"O","\u1ED8":"O","\u01EA":"O","\u01EC":"O","\u00D8":"O","\u01FE":"O","\u0186":"O","\u019F":"O","\uA74A":"O","\uA74C":"O","\u01A2":"OI","\uA74E":"OO","\u0222":"OU","\u24C5":"P","\uFF30":"P","\u1E54":"P","\u1E56":"P","\u01A4":"P","\u2C63":"P","\uA750":"P","\uA752":"P","\uA754":"P","\u24C6":"Q","\uFF31":"Q","\uA756":"Q","\uA758":"Q","\u024A":"Q","\u24C7":"R","\uFF32":"R","\u0154":"R","\u1E58":"R","\u0158":"R","\u0210":"R","\u0212":"R","\u1E5A":"R","\u1E5C":"R","\u0156":"R","\u1E5E":"R","\u024C":"R","\u2C64":"R","\uA75A":"R","\uA7A6":"R","\uA782":"R","\u24C8":"S","\uFF33":"S","\u1E9E":"S","\u015A":"S","\u1E64":"S","\u015C":"S","\u1E60":"S","\u0160":"S","\u1E66":"S","\u1E62":"S","\u1E68":"S","\u0218":"S","\u015E":"S","\u2C7E":"S","\uA7A8":"S","\uA784":"S","\u24C9":"T","\uFF34":"T","\u1E6A":"T","\u0164":"T","\u1E6C":"T","\u021A":"T","\u0162":"T","\u1E70":"T","\u1E6E":"T","\u0166":"T","\u01AC":"T","\u01AE":"T","\u023E":"T","\uA786":"T","\uA728":"TZ","\u24CA":"U","\uFF35":"U","\u00D9":"U","\u00DA":"U","\u00DB":"U","\u0168":"U","\u1E78":"U","\u016A":"U","\u1E7A":"U","\u016C":"U","\u00DC":"U","\u01DB":"U","\u01D7":"U","\u01D5":"U","\u01D9":"U","\u1EE6":"U","\u016E":"U","\u0170":"U","\u01D3":"U","\u0214":"U","\u0216":"U","\u01AF":"U","\u1EEA":"U","\u1EE8":"U","\u1EEE":"U","\u1EEC":"U","\u1EF0":"U","\u1EE4":"U","\u1E72":"U","\u0172":"U","\u1E76":"U","\u1E74":"U","\u0244":"U","\u24CB":"V","\uFF36":"V","\u1E7C":"V","\u1E7E":"V","\u01B2":"V","\uA75E":"V","\u0245":"V","\uA760":"VY","\u24CC":"W","\uFF37":"W","\u1E80":"W","\u1E82":"W","\u0174":"W","\u1E86":"W","\u1E84":"W","\u1E88":"W","\u2C72":"W","\u24CD":"X","\uFF38":"X","\u1E8A":"X","\u1E8C":"X","\u24CE":"Y","\uFF39":"Y","\u1EF2":"Y","\u00DD":"Y","\u0176":"Y","\u1EF8":"Y","\u0232":"Y","\u1E8E":"Y","\u0178":"Y","\u1EF6":"Y","\u1EF4":"Y","\u01B3":"Y","\u024E":"Y","\u1EFE":"Y","\u24CF":"Z","\uFF3A":"Z","\u0179":"Z","\u1E90":"Z","\u017B":"Z","\u017D":"Z","\u1E92":"Z","\u1E94":"Z","\u01B5":"Z","\u0224":"Z","\u2C7F":"Z","\u2C6B":"Z","\uA762":"Z","\u24D0":"a","\uFF41":"a","\u1E9A":"a","\u00E0":"a","\u00E1":"a","\u00E2":"a","\u1EA7":"a","\u1EA5":"a","\u1EAB":"a","\u1EA9":"a","\u00E3":"a","\u0101":"a","\u0103":"a","\u1EB1":"a","\u1EAF":"a","\u1EB5":"a","\u1EB3":"a","\u0227":"a","\u01E1":"a","\u00E4":"a","\u01DF":"a","\u1EA3":"a","\u00E5":"a","\u01FB":"a","\u01CE":"a","\u0201":"a","\u0203":"a","\u1EA1":"a","\u1EAD":"a","\u1EB7":"a","\u1E01":"a","\u0105":"a","\u2C65":"a","\u0250":"a","\uA733":"aa","\u00E6":"ae","\u01FD":"ae","\u01E3":"ae","\uA735":"ao","\uA737":"au","\uA739":"av","\uA73B":"av","\uA73D":"ay","\u24D1":"b","\uFF42":"b","\u1E03":"b","\u1E05":"b","\u1E07":"b","\u0180":"b","\u0183":"b","\u0253":"b","\u24D2":"c","\uFF43":"c","\u0107":"c","\u0109":"c","\u010B":"c","\u010D":"c","\u00E7":"c","\u1E09":"c","\u0188":"c","\u023C":"c","\uA73F":"c","\u2184":"c","\u24D3":"d","\uFF44":"d","\u1E0B":"d","\u010F":"d","\u1E0D":"d","\u1E11":"d","\u1E13":"d","\u1E0F":"d","\u0111":"d","\u018C":"d","\u0256":"d","\u0257":"d","\uA77A":"d","\u01F3":"dz","\u01C6":"dz","\u24D4":"e","\uFF45":"e","\u00E8":"e","\u00E9":"e","\u00EA":"e","\u1EC1":"e","\u1EBF":"e","\u1EC5":"e","\u1EC3":"e","\u1EBD":"e","\u0113":"e","\u1E15":"e","\u1E17":"e","\u0115":"e","\u0117":"e","\u00EB":"e","\u1EBB":"e","\u011B":"e","\u0205":"e","\u0207":"e","\u1EB9":"e","\u1EC7":"e","\u0229":"e","\u1E1D":"e","\u0119":"e","\u1E19":"e","\u1E1B":"e","\u0247":"e","\u025B":"e","\u01DD":"e","\u24D5":"f","\uFF46":"f","\u1E1F":"f","\u0192":"f","\uA77C":"f","\u24D6":"g","\uFF47":"g","\u01F5":"g","\u011D":"g","\u1E21":"g","\u011F":"g","\u0121":"g","\u01E7":"g","\u0123":"g","\u01E5":"g","\u0260":"g","\uA7A1":"g","\u1D79":"g","\uA77F":"g","\u24D7":"h","\uFF48":"h","\u0125":"h","\u1E23":"h","\u1E27":"h","\u021F":"h","\u1E25":"h","\u1E29":"h","\u1E2B":"h","\u1E96":"h","\u0127":"h","\u2C68":"h","\u2C76":"h","\u0265":"h","\u0195":"hv","\u24D8":"i","\uFF49":"i","\u00EC":"i","\u00ED":"i","\u00EE":"i","\u0129":"i","\u012B":"i","\u012D":"i","\u00EF":"i","\u1E2F":"i","\u1EC9":"i","\u01D0":"i","\u0209":"i","\u020B":"i","\u1ECB":"i","\u012F":"i","\u1E2D":"i","\u0268":"i","\u0131":"i","\u24D9":"j","\uFF4A":"j","\u0135":"j","\u01F0":"j","\u0249":"j","\u24DA":"k","\uFF4B":"k","\u1E31":"k","\u01E9":"k","\u1E33":"k","\u0137":"k","\u1E35":"k","\u0199":"k","\u2C6A":"k","\uA741":"k","\uA743":"k","\uA745":"k","\uA7A3":"k","\u24DB":"l","\uFF4C":"l","\u0140":"l","\u013A":"l","\u013E":"l","\u1E37":"l","\u1E39":"l","\u013C":"l","\u1E3D":"l","\u1E3B":"l","\u017F":"l","\u0142":"l","\u019A":"l","\u026B":"l","\u2C61":"l","\uA749":"l","\uA781":"l","\uA747":"l","\u01C9":"lj","\u24DC":"m","\uFF4D":"m","\u1E3F":"m","\u1E41":"m","\u1E43":"m","\u0271":"m","\u026F":"m","\u24DD":"n","\uFF4E":"n","\u01F9":"n","\u0144":"n","\u00F1":"n","\u1E45":"n","\u0148":"n","\u1E47":"n","\u0146":"n","\u1E4B":"n","\u1E49":"n","\u019E":"n","\u0272":"n","\u0149":"n","\uA791":"n","\uA7A5":"n","\u01CC":"nj","\u24DE":"o","\uFF4F":"o","\u00F2":"o","\u00F3":"o","\u00F4":"o","\u1ED3":"o","\u1ED1":"o","\u1ED7":"o","\u1ED5":"o","\u00F5":"o","\u1E4D":"o","\u022D":"o","\u1E4F":"o","\u014D":"o","\u1E51":"o","\u1E53":"o","\u014F":"o","\u022F":"o","\u0231":"o","\u00F6":"o","\u022B":"o","\u1ECF":"o","\u0151":"o","\u01D2":"o","\u020D":"o","\u020F":"o","\u01A1":"o","\u1EDD":"o","\u1EDB":"o","\u1EE1":"o","\u1EDF":"o","\u1EE3":"o","\u1ECD":"o","\u1ED9":"o","\u01EB":"o","\u01ED":"o","\u00F8":"o","\u01FF":"o","\u0254":"o","\uA74B":"o","\uA74D":"o","\u0275":"o","\u01A3":"oi","\u0223":"ou","\uA74F":"oo","\u24DF":"p","\uFF50":"p","\u1E55":"p","\u1E57":"p","\u01A5":"p","\u1D7D":"p","\uA751":"p","\uA753":"p","\uA755":"p","\u24E0":"q","\uFF51":"q","\u024B":"q","\uA757":"q","\uA759":"q","\u24E1":"r","\uFF52":"r","\u0155":"r","\u1E59":"r","\u0159":"r","\u0211":"r","\u0213":"r","\u1E5B":"r","\u1E5D":"r","\u0157":"r","\u1E5F":"r","\u024D":"r","\u027D":"r","\uA75B":"r","\uA7A7":"r","\uA783":"r","\u24E2":"s","\uFF53":"s","\u00DF":"s","\u015B":"s","\u1E65":"s","\u015D":"s","\u1E61":"s","\u0161":"s","\u1E67":"s","\u1E63":"s","\u1E69":"s","\u0219":"s","\u015F":"s","\u023F":"s","\uA7A9":"s","\uA785":"s","\u1E9B":"s","\u24E3":"t","\uFF54":"t","\u1E6B":"t","\u1E97":"t","\u0165":"t","\u1E6D":"t","\u021B":"t","\u0163":"t","\u1E71":"t","\u1E6F":"t","\u0167":"t","\u01AD":"t","\u0288":"t","\u2C66":"t","\uA787":"t","\uA729":"tz","\u24E4":"u","\uFF55":"u","\u00F9":"u","\u00FA":"u","\u00FB":"u","\u0169":"u","\u1E79":"u","\u016B":"u","\u1E7B":"u","\u016D":"u","\u00FC":"u","\u01DC":"u","\u01D8":"u","\u01D6":"u","\u01DA":"u","\u1EE7":"u","\u016F":"u","\u0171":"u","\u01D4":"u","\u0215":"u","\u0217":"u","\u01B0":"u","\u1EEB":"u","\u1EE9":"u","\u1EEF":"u","\u1EED":"u","\u1EF1":"u","\u1EE5":"u","\u1E73":"u","\u0173":"u","\u1E77":"u","\u1E75":"u","\u0289":"u","\u24E5":"v","\uFF56":"v","\u1E7D":"v","\u1E7F":"v","\u028B":"v","\uA75F":"v","\u028C":"v","\uA761":"vy","\u24E6":"w","\uFF57":"w","\u1E81":"w","\u1E83":"w","\u0175":"w","\u1E87":"w","\u1E85":"w","\u1E98":"w","\u1E89":"w","\u2C73":"w","\u24E7":"x","\uFF58":"x","\u1E8B":"x","\u1E8D":"x","\u24E8":"y","\uFF59":"y","\u1EF3":"y","\u00FD":"y","\u0177":"y","\u1EF9":"y","\u0233":"y","\u1E8F":"y","\u00FF":"y","\u1EF7":"y","\u1E99":"y","\u1EF5":"y","\u01B4":"y","\u024F":"y","\u1EFF":"y","\u24E9":"z","\uFF5A":"z","\u017A":"z","\u1E91":"z","\u017C":"z","\u017E":"z","\u1E93":"z","\u1E95":"z","\u01B6":"z","\u0225":"z","\u0240":"z","\u2C6C":"z","\uA763":"z"};
w=E(document);
a=(function(){var P=1;
return function(){return P++
}
}());
function p(P){var Q=E(document.createTextNode(""));
P.before(Q);
Q.before(P);
Q.remove()
}function e(S){var Q,R,P,T;
if(!S||S.length<1){return S
}Q="";
for(R=0,P=S.length;
R<P;
R++){T=S.charAt(R);
Q+=d[T]||T
}return Q
}function r(R,S){var Q=0,P=S.length;
for(;
Q<P;
Q=Q+1){if(u(R,S[Q])){return Q
}}return -1
}function N(){var P=E(C);
P.appendTo("body");
var Q={width:P.width()-P[0].clientWidth,height:P.height()-P[0].clientHeight};
P.remove();
return Q
}function u(Q,P){if(Q===P){return true
}if(Q===m||P===m){return false
}if(Q===null||P===null){return false
}if(Q.constructor===String){return Q+""===P+""
}if(P.constructor===String){return P+""===Q+""
}return false
}function i(Q,S){var T,R,P;
if(Q===null||Q.length<1){return[]
}T=Q.split(S);
for(R=0,P=T.length;
R<P;
R=R+1){T[R]=E.trim(T[R])
}return T
}function h(P){return P.outerWidth(false)-P.width()
}function G(Q){var P="keyup-change-value";
Q.on("keydown",function(){if(E.data(Q,P)===m){E.data(Q,P,Q.val())
}});
Q.on("keyup",function(){var R=E.data(Q,P);
if(R!==m&&Q.val()!==R){E.removeData(Q,P);
Q.trigger("keyup-change")
}})
}w.on("mousemove",function(P){o.x=P.pageX;
o.y=P.pageY
});
function K(P){P.on("mousemove",function(R){var Q=o;
if(Q===m||Q.x!==R.pageX||Q.y!==R.pageY){E(R.target).trigger("mousemove-filtered",R)
}})
}function k(S,Q,P){P=P||m;
var R;
return function(){var T=arguments;
window.clearTimeout(R);
R=window.setTimeout(function(){Q.apply(P,T)
},S)
}
}function t(R){var P=false,Q;
return function(){if(P===false){Q=R();
P=true
}return Q
}
}function l(P,R){var Q=k(P,function(S){R.trigger("scroll-debounced",S)
});
R.on("scroll",function(S){if(r(S.target,R.get())>=0){Q(S)
}})
}function J(P){if(P[0]===document.activeElement){return
}window.setTimeout(function(){var S=P[0],T=P.val().length,R;
P.focus();
var Q=(S.offsetWidth>0||S.offsetHeight>0);
if(Q&&S===document.activeElement){if(S.setSelectionRange){S.setSelectionRange(T,T)
}else{if(S.createTextRange){R=S.createTextRange();
R.collapse(false);
R.select()
}}}},0)
}function f(P){P=E(P)[0];
var S=0;
var Q=0;
if("selectionStart" in P){S=P.selectionStart;
Q=P.selectionEnd-S
}else{if("selection" in document){P.focus();
var R=document.selection.createRange();
Q=document.selection.createRange().text.length;
R.moveStart("character",-P.value.length);
S=R.text.length-Q
}}return{offset:S,length:Q}
}function B(P){P.preventDefault();
P.stopPropagation()
}function b(P){P.preventDefault();
P.stopImmediatePropagation()
}function n(Q){if(!q){var P=Q[0].currentStyle||window.getComputedStyle(Q[0],null);
q=E(document.createElement("div")).css({position:"absolute",left:"-10000px",top:"-10000px",display:"none",fontSize:P.fontSize,fontFamily:P.fontFamily,fontStyle:P.fontStyle,fontWeight:P.fontWeight,letterSpacing:P.letterSpacing,textTransform:P.textTransform,whiteSpace:"nowrap"});
q.attr("class","select2-sizer");
E("body").append(q)
}q.text(Q.val());
return q.width()
}function j(Q,U,P){var S,T=[],R;
S=Q.attr("class");
if(S){S=""+S;
E(S.split(" ")).each2(function(){if(this.indexOf("select2-")===0){T.push(this)
}})
}S=U.attr("class");
if(S){S=""+S;
E(S.split(" ")).each2(function(){if(this.indexOf("select2-")!==0){R=P(this);
if(R){T.push(R)
}}})
}Q.attr("class",T.join(" "))
}function v(U,T,R,P){var S=e(U.toUpperCase()).indexOf(e(T.toUpperCase())),Q=T.length;
if(S<0){R.push(P(U));
return
}R.push(P(U.substring(0,S)));
R.push("<span class='select2-match'>");
R.push(P(U.substring(S,S+Q)));
R.push("</span>");
R.push(P(U.substring(S+Q,U.length)))
}function H(P){var Q={"\\":"&#92;","&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;","/":"&#47;"};
return String(P).replace(/[&<>"'\/\\]/g,function(R){return Q[R]
})
}function F(Q){var T,R=null,U=Q.quietMillis||100,S=Q.url,P=this;
return function(V){window.clearTimeout(T);
T=window.setTimeout(function(){var Y=Q.data,X=S,aa=Q.transport||E.fn.select2.ajaxDefaults.transport,W={type:Q.type||"GET",cache:Q.cache||false,jsonpCallback:Q.jsonpCallback||m,dataType:Q.dataType||"json"},Z=E.extend({},E.fn.select2.ajaxDefaults.params,W);
Y=Y?Y.call(P,V.term,V.page,V.context):null;
X=(typeof X==="function")?X.call(P,V.term,V.page,V.context):X;
if(R&&typeof R.abort==="function"){R.abort()
}if(Q.params){if(E.isFunction(Q.params)){E.extend(Z,Q.params.call(P))
}else{E.extend(Z,Q.params)
}}E.extend(Z,{url:X,dataType:Q.dataType,data:Y,success:function(ac){var ab=Q.results(ac,V.page);
V.callback(ab)
}});
R=aa.call(P,Z)
},U)
}
}function I(Q){var T=Q,S,R,U=function(V){return""+V.text
};
if(E.isArray(T)){R=T;
T={results:R}
}if(E.isFunction(T)===false){R=T;
T=function(){return R
}
}var P=T();
if(P.text){U=P.text;
if(!E.isFunction(U)){S=P.text;
U=function(V){return V[S]
}
}}return function(X){var W=X.term,V={results:[]},Y;
if(W===""){X.callback(T());
return
}Y=function(aa,ac){var ab,Z;
aa=aa[0];
if(aa.children){ab={};
for(Z in aa){if(aa.hasOwnProperty(Z)){ab[Z]=aa[Z]
}}ab.children=[];
E(aa.children).each2(function(ad,ae){Y(ae,ab.children)
});
if(ab.children.length||X.matcher(W,U(ab),aa)){ac.push(ab)
}}else{if(X.matcher(W,U(aa),aa)){ac.push(aa)
}}};
E(T().results).each2(function(aa,Z){Y(Z,V.results)
});
X.callback(V)
}
}function A(Q){var P=E.isFunction(Q);
return function(T){var S=T.term,R={results:[]};
E(P?Q():Q).each(function(){var U=this.text!==m,V=U?this.text:this;
if(S===""||T.matcher(S,V)){R.results.push(U?this:{id:this,text:this})
}});
T.callback(R)
}
}function z(P,Q){if(E.isFunction(P)){return true
}if(!P){return false
}if(typeof(P)==="string"){return true
}throw new Error(Q+" must be a string, function, or falsy value")
}function D(Q){if(E.isFunction(Q)){var P=Array.prototype.slice.call(arguments,1);
return Q.apply(null,P)
}return Q
}function s(P){var Q=0;
E.each(P,function(R,S){if(S.children){Q+=s(S.children)
}else{Q++
}});
return Q
}function g(X,Y,V,P){var Q=X,Z=false,S,W,T,R,U;
if(!P.createSearchChoice||!P.tokenSeparators||P.tokenSeparators.length<1){return m
}while(true){W=-1;
for(T=0,R=P.tokenSeparators.length;
T<R;
T++){U=P.tokenSeparators[T];
W=X.indexOf(U);
if(W>=0){break
}}if(W<0){break
}S=X.substring(0,W);
X=X.substring(W+U.length);
if(S.length>0){S=P.createSearchChoice.call(this,S,Y);
if(S!==m&&S!==null&&P.id(S)!==m&&P.id(S)!==null){Z=false;
for(T=0,R=Y.length;
T<R;
T++){if(u(P.id(S),P.id(Y[T]))){Z=true;
break
}}if(!Z){V(S)
}}}}if(Q!==X){return X
}}function M(P,Q){var R=function(){};
R.prototype=new P;
R.prototype.constructor=R;
R.prototype.parent=P.prototype;
R.prototype=E.extend(R.prototype,Q);
return R
}O=M(Object,{bind:function(Q){var P=this;
return function(){Q.apply(P,arguments)
}
},init:function(T){var R,Q,U=".select2-results";
this.opts=T=this.prepareOpts(T);
this.id=T.id;
if(T.element.data("select2")!==m&&T.element.data("select2")!==null){T.element.data("select2").destroy()
}this.container=this.createContainer();
this.liveRegion=E("<span>",{role:"status","aria-live":"polite"}).addClass("select2-hidden-accessible").appendTo(document.body);
this.containerId="s2id_"+(T.element.attr("id")||"autogen"+a()).replace(/([;&,\-\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g,"\\$1");
this.containerSelector="#"+this.containerId;
this.container.attr("id",this.containerId);
this.body=t(function(){return T.element.closest("body")
});
j(this.container,this.opts.element,this.opts.adaptContainerCssClass);
this.container.attr("style",T.element.attr("style"));
this.container.css(D(T.containerCss));
this.container.addClass(D(T.containerCssClass));
this.elementTabIndex=this.opts.element.attr("tabindex");
this.opts.element.data("select2",this).attr("tabindex","-1").before(this.container).on("click.select2",B);
this.container.data("select2",this);
this.dropdown=this.container.find(".select2-drop");
j(this.dropdown,this.opts.element,this.opts.adaptDropdownCssClass);
this.dropdown.addClass(D(T.dropdownCssClass));
this.dropdown.data("select2",this);
this.dropdown.on("click",B);
this.results=R=this.container.find(U);
this.search=Q=this.container.find("input.select2-input");
this.queryCount=0;
this.resultsPage=0;
this.context=null;
this.initContainer();
this.container.on("click",B);
K(this.results);
this.dropdown.on("mousemove-filtered touchstart touchmove touchend",U,this.bind(this.highlightUnderEvent));
this.dropdown.on("touchend",U,this.bind(this.selectHighlighted));
this.dropdown.on("touchmove",U,this.bind(this.touchMoved));
this.dropdown.on("touchstart touchend",U,this.bind(this.clearTouchMoved));
l(80,this.results);
this.dropdown.on("scroll-debounced",U,this.bind(this.loadMoreIfNeeded));
E(this.container).on("change",".select2-input",function(V){V.stopPropagation()
});
E(this.dropdown).on("change",".select2-input",function(V){V.stopPropagation()
});
if(E.fn.mousewheel){R.mousewheel(function(Y,Z,W,V){var X=R.scrollTop();
if(V>0&&X-V<=0){R.scrollTop(0);
B(Y)
}else{if(V<0&&R.get(0).scrollHeight-R.scrollTop()+V<=R.height()){R.scrollTop(R.get(0).scrollHeight-R.height());
B(Y)
}}})
}G(Q);
Q.on("keyup-change input paste",this.bind(this.updateResults));
Q.on("focus",function(){Q.addClass("select2-focused")
});
Q.on("blur",function(){Q.removeClass("select2-focused")
});
this.dropdown.on("mouseup",U,this.bind(function(V){if(E(V.target).closest(".select2-result-selectable").length>0){this.highlightUnderEvent(V);
this.selectHighlighted(V)
}}));
this.dropdown.on("click mouseup mousedown focusin",function(V){V.stopPropagation()
});
this.nextSearchTerm=m;
if(E.isFunction(this.opts.initSelection)){this.initSelection();
this.monitorSource()
}if(T.maximumInputLength!==null){this.search.attr("maxlength",T.maximumInputLength)
}var S=T.element.prop("disabled");
if(S===m){S=false
}this.enable(!S);
var P=T.element.prop("readonly");
if(P===m){P=false
}this.readonly(P);
x=x||N();
this.autofocus=T.element.prop("autofocus");
T.element.prop("autofocus",false);
if(this.autofocus){this.focus()
}this.search.attr("placeholder",T.searchInputPlaceholder)
},destroy:function(){var Q=this.opts.element,P=Q.data("select2");
this.close();
if(this.propertyObserver){delete this.propertyObserver;
this.propertyObserver=null
}if(P!==m){P.container.remove();
P.liveRegion.remove();
P.dropdown.remove();
Q.removeClass("select2-offscreen").removeData("select2").off(".select2").prop("autofocus",this.autofocus||false);
if(this.elementTabIndex){Q.attr({tabindex:this.elementTabIndex})
}else{Q.removeAttr("tabindex")
}Q.show()
}},optionToData:function(P){if(P.is("option")){return{id:P.prop("value"),text:P.text(),element:P.get(),css:P.attr("class"),disabled:P.prop("disabled"),locked:u(P.attr("locked"),"locked")||u(P.data("locked"),true)}
}else{if(P.is("optgroup")){return{text:P.attr("label"),children:[],element:P.get(),css:P.attr("class")}
}}},prepareOpts:function(U){var S,Q,P,T,R=this;
S=U.element;
if(S.get(0).tagName.toLowerCase()==="select"){this.select=Q=U.element
}if(Q){E.each(["id","multiple","ajax","query","createSearchChoice","initSelection","data","tags"],function(){if(this in U){throw new Error("Option '"+this+"' is not allowed for Select2 when attached to a <select> element.")
}})
}U=E.extend({},{populateResults:function(W,X,Z){var Y,aa=this.opts.id,V=this.liveRegion;
Y=function(ah,ab,ag){var ai,ad,an,ak,ae,am,ac,al,aj,af;
ah=U.sortResults(ah,ab,Z);
for(ai=0,ad=ah.length;
ai<ad;
ai=ai+1){an=ah[ai];
ae=(an.disabled===true);
ak=(!ae)&&(aa(an)!==m);
am=an.children&&an.children.length>0;
ac=E("<li></li>");
ac.addClass("select2-results-dept-"+ag);
ac.addClass("select2-result");
ac.addClass(ak?"select2-result-selectable":"select2-result-unselectable");
if(ae){ac.addClass("select2-disabled")
}if(am){ac.addClass("select2-result-with-children")
}ac.addClass(R.opts.formatResultCssClass(an));
ac.attr("role","presentation");
al=E(document.createElement("div"));
al.addClass("select2-result-label");
al.attr("id","select2-result-label-"+a());
al.attr("role","option");
af=U.formatResult(an,al,Z,R.opts.escapeMarkup);
if(af!==m){al.html(af);
ac.append(al)
}if(am){aj=E("<ul></ul>");
aj.addClass("select2-result-sub");
Y(an.children,aj,ag+1);
ac.append(aj)
}ac.data("select2-data",an);
ab.append(ac)
}V.text(U.formatMatches(ah.length))
};
Y(X,W,0)
}},E.fn.select2.defaults,U);
if(typeof(U.id)!=="function"){P=U.id;
U.id=function(V){return V[P]
}
}if(E.isArray(U.element.data("select2Tags"))){if("tags" in U){throw"tags specified as both an attribute 'data-select2-tags' and in options of Select2 "+U.element.attr("id")
}U.tags=U.element.data("select2Tags")
}if(Q){U.query=this.bind(function(Z){var Y={results:[],more:false},X=Z.term,W,V,aa;
aa=function(ab,ad){var ac;
if(ab.is("option")){if(Z.matcher(X,ab.text(),ab)){ad.push(R.optionToData(ab))
}}else{if(ab.is("optgroup")){ac=R.optionToData(ab);
ab.children().each2(function(ae,af){aa(af,ac.children)
});
if(ac.children.length>0){ad.push(ac)
}}}};
W=S.children();
if(this.getPlaceholder()!==m&&W.length>0){V=this.getPlaceholderOption();
if(V){W=W.not(V)
}}W.each2(function(ab,ac){aa(ac,Y.results)
});
Z.callback(Y)
});
U.id=function(V){return V.id
}
}else{if(!("query" in U)){if("ajax" in U){T=U.element.data("ajax-url");
if(T&&T.length>0){U.ajax.url=T
}U.query=F.call(U.element,U.ajax)
}else{if("data" in U){U.query=I(U.data)
}else{if("tags" in U){U.query=A(U.tags);
if(U.createSearchChoice===m){U.createSearchChoice=function(V){return{id:E.trim(V),text:E.trim(V)}
}
}if(U.initSelection===m){U.initSelection=function(V,X){var W=[];
E(i(V.val(),U.separator)).each(function(){var Z={id:this,text:this},Y=U.tags;
if(E.isFunction(Y)){Y=Y()
}E(Y).each(function(){if(u(this.id,Z.id)){Z=this;
return false
}});
W.push(Z)
});
X(W)
}
}}}}}}if(typeof(U.query)!=="function"){throw"query function not defined for Select2 "+U.element.attr("id")
}if(U.createSearchChoicePosition==="top"){U.createSearchChoicePosition=function(W,V){W.unshift(V)
}
}else{if(U.createSearchChoicePosition==="bottom"){U.createSearchChoicePosition=function(W,V){W.push(V)
}
}else{if(typeof(U.createSearchChoicePosition)!=="function"){throw"invalid createSearchChoicePosition option must be 'top', 'bottom' or a custom function"
}}}return U
},monitorSource:function(){var Q=this.opts.element,R,P;
Q.on("change.select2",this.bind(function(S){if(this.opts.element.data("select2-change-triggered")!==true){this.initSelection()
}}));
R=this.bind(function(){var T=Q.prop("disabled");
if(T===m){T=false
}this.enable(!T);
var S=Q.prop("readonly");
if(S===m){S=false
}this.readonly(S);
j(this.container,this.opts.element,this.opts.adaptContainerCssClass);
this.container.addClass(D(this.opts.containerCssClass));
j(this.dropdown,this.opts.element,this.opts.adaptDropdownCssClass);
this.dropdown.addClass(D(this.opts.dropdownCssClass))
});
Q.on("propertychange.select2",R);
if(this.mutationCallback===m){this.mutationCallback=function(S){S.forEach(R)
}
}P=window.MutationObserver||window.WebKitMutationObserver||window.MozMutationObserver;
if(P!==m){if(this.propertyObserver){delete this.propertyObserver;
this.propertyObserver=null
}this.propertyObserver=new P(this.mutationCallback);
this.propertyObserver.observe(Q.get(0),{attributes:true,subtree:false})
}},triggerSelect:function(Q){var P=E.Event("select2-selecting",{val:this.id(Q),object:Q});
this.opts.element.trigger(P);
return !P.isDefaultPrevented()
},triggerChange:function(P){P=P||{};
P=E.extend({},P,{type:"change",val:this.val()});
this.opts.element.data("select2-change-triggered",true);
this.opts.element.trigger(P);
this.opts.element.data("select2-change-triggered",false);
this.opts.element.click();
if(this.opts.blurOnChange){this.opts.element.blur()
}},isInterfaceEnabled:function(){return this.enabledInterface===true
},enableInterface:function(){var P=this._enabled&&!this._readonly,Q=!P;
if(P===this.enabledInterface){return false
}this.container.toggleClass("select2-container-disabled",Q);
this.close();
this.enabledInterface=P;
return true
},enable:function(P){if(P===m){P=true
}if(this._enabled===P){return
}this._enabled=P;
this.opts.element.prop("disabled",!P);
this.enableInterface()
},disable:function(){this.enable(false)
},readonly:function(P){if(P===m){P=false
}if(this._readonly===P){return
}this._readonly=P;
this.opts.element.prop("readonly",P);
this.enableInterface()
},opened:function(){return this.container.hasClass("select2-dropdown-open")
},positionDropdown:function(){var R=this.dropdown,U=this.container.offset(),ad=this.container.outerHeight(false),ae=this.container.outerWidth(false),Z=R.outerHeight(false),ab=E(window),ai=ab.width(),X=ab.height(),Q=ab.scrollLeft()+ai,ah=ab.scrollTop()+X,S=U.top+ad,af=U.left,P=S+Z<=ah,W=(U.top-Z)>=ab.scrollTop(),aa=R.outerWidth(false),ak=af+aa<=Q,aj=R.hasClass("select2-drop-above"),V,ag,T,Y,ac;
if(aj){ag=true;
if(!W&&P){T=true;
ag=false
}}else{ag=false;
if(!P&&W){T=true;
ag=true
}}if(T){R.hide();
U=this.container.offset();
ad=this.container.outerHeight(false);
ae=this.container.outerWidth(false);
Z=R.outerHeight(false);
Q=ab.scrollLeft()+ai;
ah=ab.scrollTop()+X;
S=U.top+ad;
af=U.left;
aa=R.outerWidth(false);
ak=af+aa<=Q;
R.show()
}if(this.opts.dropdownAutoWidth){ac=E(".select2-results",R)[0];
R.addClass("select2-drop-auto-width");
R.css("width","");
aa=R.outerWidth(false)+(ac.scrollHeight===ac.clientHeight?0:x.width);
aa>ae?ae=aa:aa=ae;
ak=af+aa<=Q
}else{this.container.removeClass("select2-drop-auto-width")
}if(this.body().css("position")!=="static"){V=this.body().offset();
S-=V.top;
af-=V.left
}if(!ak){af=U.left+this.container.outerWidth(false)-aa
}Y={left:af,width:ae};
if(ag){Y.top=U.top-Z;
Y.bottom="auto";
this.container.addClass("select2-drop-above");
R.addClass("select2-drop-above")
}else{Y.top=S;
Y.bottom="auto";
this.container.removeClass("select2-drop-above");
R.removeClass("select2-drop-above")
}Y=E.extend(Y,D(this.opts.dropdownCss));
R.css(Y)
},shouldOpen:function(){var P;
if(this.opened()){return false
}if(this._enabled===false||this._readonly===true){return false
}P=E.Event("select2-opening");
this.opts.element.trigger(P);
return !P.isDefaultPrevented()
},clearDropdownAlignmentPreference:function(){this.container.removeClass("select2-drop-above");
this.dropdown.removeClass("select2-drop-above")
},open:function(){if(!this.shouldOpen()){return false
}this.opening();
return true
},opening:function(){var U=this.containerId,P="scroll."+U,S="resize."+U,R="orientationchange."+U,Q;
this.container.addClass("select2-dropdown-open").addClass("select2-container-active");
this.clearDropdownAlignmentPreference();
if(this.dropdown[0]!==this.body().children().last()[0]){this.dropdown.detach().appendTo(this.body())
}Q=E("#select2-drop-mask");
if(Q.length==0){Q=E(document.createElement("div"));
Q.attr("id","select2-drop-mask").attr("class","select2-drop-mask");
Q.hide();
Q.appendTo(this.body());
Q.on("mousedown touchstart click",function(W){p(Q);
var X=E("#select2-drop"),V;
if(X.length>0){V=X.data("select2");
if(V.opts.selectOnBlur){V.selectHighlighted({noFocus:true})
}V.close();
W.preventDefault();
W.stopPropagation()
}})
}if(this.dropdown.prev()[0]!==Q[0]){this.dropdown.before(Q)
}E("#select2-drop").removeAttr("id");
this.dropdown.attr("id","select2-drop");
Q.show();
this.positionDropdown();
this.dropdown.show();
this.positionDropdown();
this.dropdown.addClass("select2-drop-active");
var T=this;
this.container.parents().add(window).each(function(){E(this).on(S+" "+P+" "+R,function(V){T.positionDropdown()
})
})
},close:function(){if(!this.opened()){return
}var S=this.containerId,P="scroll."+S,R="resize."+S,Q="orientationchange."+S;
this.container.parents().add(window).each(function(){E(this).off(P).off(R).off(Q)
});
this.clearDropdownAlignmentPreference();
E("#select2-drop-mask").hide();
this.dropdown.removeAttr("id");
this.dropdown.hide();
this.container.removeClass("select2-dropdown-open").removeClass("select2-container-active");
this.results.empty();
this.clearSearch();
this.search.removeClass("select2-active");
this.opts.element.trigger(E.Event("select2-close"))
},externalSearch:function(P){this.open();
this.search.val(P);
this.updateResults(false)
},clearSearch:function(){},getMaximumSelectionSize:function(){return D(this.opts.maximumSelectionSize)
},ensureHighlightVisible:function(){var S=this.results,R,P,W,V,T,U,Q;
P=this.highlight();
if(P<0){return
}if(P==0){if(this.opts.closeOnSelect){S.scrollTop(0)
}return
}R=this.findHighlightableChoices().find(".select2-result-label");
W=E(R[P]);
if(!W.length){return
}V=W.offset().top+W.outerHeight(true);
if(P===R.length-1){Q=S.find("li.select2-more-results");
if(Q.length>0){V=Q.offset().top+Q.outerHeight(true)
}}T=S.offset().top+S.outerHeight(true);
if(V>T){S.scrollTop(S.scrollTop()+(V-T))
}U=W.offset().top-S.offset().top;
if(U<0&&W.css("display")!="none"){S.scrollTop(S.scrollTop()+U)
}},findHighlightableChoices:function(){return this.results.find(".select2-result-selectable:not(.select2-disabled):not(.select2-selected)")
},moveHighlight:function(S){var R=this.findHighlightableChoices(),Q=this.highlight();
while(Q>-1&&Q<R.length){Q+=S;
var P=E(R[Q]);
if(P.hasClass("select2-result-selectable")&&!P.hasClass("select2-disabled")&&!P.hasClass("select2-selected")){this.highlight(Q);
break
}}},highlight:function(Q){var S=this.findHighlightableChoices(),P,R;
if(arguments.length===0){return r(S.filter(".select2-highlighted")[0],S.get())
}if(Q>=S.length){Q=S.length-1
}if(Q<0){Q=0
}this.removeHighlight();
P=E(S[Q]);
P.addClass("select2-highlighted");
this.search.attr("aria-activedescendant",P.find(".select2-result-label").attr("id"));
this.ensureHighlightVisible();
this.liveRegion.text(P.text());
R=P.data("select2-data");
if(R){this.opts.element.trigger({type:"select2-highlight",val:this.id(R),choice:R})
}},removeHighlight:function(){this.results.find(".select2-highlighted").removeClass("select2-highlighted")
},touchMoved:function(){this._touchMoved=true
},clearTouchMoved:function(){this._touchMoved=false
},countSelectableResults:function(){return this.findHighlightableChoices().length
},highlightUnderEvent:function(Q){var P=E(Q.target).closest(".select2-result-selectable");
if(P.length>0&&!P.is(".select2-highlighted")){var R=this.findHighlightableChoices();
this.highlight(R.index(P))
}else{if(P.length==0){this.removeHighlight()
}}},loadMoreIfNeeded:function(){var T=this.results,S=T.find("li.select2-more-results"),V,U=this.resultsPage+1,P=this,R=this.search.val(),Q=this.context;
if(S.length===0){return
}V=S.offset().top-T.offset().top-T.height();
if(V<=this.opts.loadMorePadding){S.addClass("select2-active");
this.opts.query({element:this.opts.element,term:R,page:U,context:Q,matcher:this.opts.matcher,callback:this.bind(function(W){if(!P.opened()){return
}P.opts.populateResults.call(this,T,W.results,{term:R,page:U,context:Q});
P.postprocessResults(W,false,false);
if(W.more===true){S.detach().appendTo(T).text(D(P.opts.formatLoadMore,U+1));
window.setTimeout(function(){P.loadMoreIfNeeded()
},10)
}else{S.remove()
}P.positionDropdown();
P.resultsPage=U;
P.context=W.context;
this.opts.element.trigger({type:"select2-loaded",items:W})
})})
}},tokenize:function(){},updateResults:function(X){var ab=this.search,V=this.results,P=this.opts,U,aa=this,Y,T=ab.val(),R=E.data(this.container,"select2-last-term"),Z;
if(X!==true&&R&&u(T,R)){return
}E.data(this.container,"select2-last-term",T);
if(X!==true&&(this.showSearchInput===false||!this.opened())){return
}function W(){ab.removeClass("select2-active");
aa.positionDropdown();
if(V.find(".select2-no-results,.select2-selection-limit,.select2-searching").length){aa.liveRegion.text(V.text())
}else{aa.liveRegion.text(aa.opts.formatMatches(V.find(".select2-result-selectable").length))
}}function Q(ac){V.html(ac);
W()
}Z=++this.queryCount;
var S=this.getMaximumSelectionSize();
if(S>=1){U=this.data();
if(E.isArray(U)&&U.length>=S&&z(P.formatSelectionTooBig,"formatSelectionTooBig")){Q("<li class='select2-selection-limit'>"+D(P.formatSelectionTooBig,S)+"</li>");
return
}}if(ab.val().length<P.minimumInputLength){if(z(P.formatInputTooShort,"formatInputTooShort")){Q("<li class='select2-no-results'>"+D(P.formatInputTooShort,ab.val(),P.minimumInputLength)+"</li>")
}else{Q("")
}if(X&&this.showSearch){this.showSearch(true)
}return
}if(P.maximumInputLength&&ab.val().length>P.maximumInputLength){if(z(P.formatInputTooLong,"formatInputTooLong")){Q("<li class='select2-no-results'>"+D(P.formatInputTooLong,ab.val(),P.maximumInputLength)+"</li>")
}else{Q("")
}return
}if(P.formatSearching&&this.findHighlightableChoices().length===0){Q("<li class='select2-searching'>"+D(P.formatSearching)+"</li>")
}ab.addClass("select2-active");
this.removeHighlight();
Y=this.tokenize();
if(Y!=m&&Y!=null){ab.val(Y)
}this.resultsPage=1;
P.query({element:P.element,term:ab.val(),page:this.resultsPage,context:null,matcher:P.matcher,callback:this.bind(function(ad){var ac;
if(Z!=this.queryCount){return
}if(!this.opened()){this.search.removeClass("select2-active");
return
}this.context=(ad.context===m)?null:ad.context;
if(this.opts.createSearchChoice&&ab.val()!==""){ac=this.opts.createSearchChoice.call(aa,ab.val(),ad.results);
if(ac!==m&&ac!==null&&aa.id(ac)!==m&&aa.id(ac)!==null){if(E(ad.results).filter(function(){return u(aa.id(this),aa.id(ac))
}).length===0){this.opts.createSearchChoicePosition(ad.results,ac)
}}}if(ad.results.length===0&&z(P.formatNoMatches,"formatNoMatches")){Q("<li class='select2-no-results'>"+D(P.formatNoMatches,ab.val())+"</li>");
return
}V.empty();
aa.opts.populateResults.call(this,V,ad.results,{term:ab.val(),page:this.resultsPage,context:null});
if(ad.more===true&&z(P.formatLoadMore,"formatLoadMore")){V.append("<li class='select2-more-results'>"+aa.opts.escapeMarkup(D(P.formatLoadMore,this.resultsPage))+"</li>");
window.setTimeout(function(){aa.loadMoreIfNeeded()
},10)
}this.postprocessResults(ad,X);
W();
this.opts.element.trigger({type:"select2-loaded",items:ad})
})})
},cancel:function(){this.close()
},blur:function(){if(this.opts.selectOnBlur){this.selectHighlighted({noFocus:true})
}this.close();
this.container.removeClass("select2-container-active");
if(this.search[0]===document.activeElement){this.search.blur()
}this.clearSearch();
this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus")
},focusSearch:function(){J(this.search)
},selectHighlighted:function(Q){if(this._touchMoved){this.clearTouchMoved();
return
}var P=this.highlight(),R=this.results.find(".select2-highlighted"),S=R.closest(".select2-result").data("select2-data");
if(S){this.highlight(P);
this.onSelect(S,Q)
}else{if(Q&&Q.noFocus){this.close()
}}},getPlaceholder:function(){var P;
return this.opts.element.attr("placeholder")||this.opts.element.attr("data-placeholder")||this.opts.element.data("placeholder")||this.opts.placeholder||((P=this.getPlaceholderOption())!==m?P.text():m)
},getPlaceholderOption:function(){if(this.select){var P=this.select.children("option").first();
if(this.opts.placeholderOption!==m){return(this.opts.placeholderOption==="first"&&P)||(typeof this.opts.placeholderOption==="function"&&this.opts.placeholderOption(this.select))
}else{if(P.text()===""&&P.val()===""){return P
}}}},initContainerWidth:function(){function Q(){var V,T,W,U,S,R;
if(this.opts.width==="off"){return null
}else{if(this.opts.width==="element"){return this.opts.element.outerWidth(false)===0?"auto":this.opts.element.outerWidth(false)+"px"
}else{if(this.opts.width==="copy"||this.opts.width==="resolve"){V=this.opts.element.attr("style");
if(V!==m){T=V.split(";");
for(U=0,S=T.length;
U<S;
U=U+1){R=T[U].replace(/\s/g,"");
W=R.match(/^width:(([-+]?([0-9]*\.)?[0-9]+)(px|em|ex|%|in|cm|mm|pt|pc))/i);
if(W!==null&&W.length>=1){return W[1]
}}}if(this.opts.width==="resolve"){V=this.opts.element.css("width");
if(V.indexOf("%")>0){return V
}return(this.opts.element.outerWidth(false)===0?"auto":this.opts.element.outerWidth(false)+"px")
}return null
}else{if(E.isFunction(this.opts.width)){return this.opts.width()
}else{return this.opts.width
}}}}}var P=Q.call(this);
if(P!==null){this.container.css("width",P)
}}});
y=M(O,{createContainer:function(){var P=E(document.createElement("div")).attr({"class":"select2-container"}).html(["<a href='javascript:void(0)' class='select2-choice' tabindex='-1'>","   <span class='select2-chosen'>&nbsp;</span><abbr class='select2-search-choice-close'></abbr>","   <span class='select2-arrow' role='presentation'><b role='presentation'></b></span>","</a>","<label for='' class='select2-offscreen'></label>","<input class='select2-focusser select2-offscreen' type='text' aria-haspopup='true' role='button' />","<div class='select2-drop select2-display-none'>","   <div class='select2-search'>","       <label for='' class='select2-offscreen'></label>","       <input type='text' autocomplete='off' autocorrect='off' autocapitalize='off' spellcheck='false' class='select2-input' role='combobox' aria-expanded='true'","       aria-autocomplete='list' />","   </div>","   <ul class='select2-results' role='listbox'>","   </ul>","</div>"].join(""));
return P
},enableInterface:function(){if(this.parent.enableInterface.apply(this,arguments)){this.focusser.prop("disabled",!this.isInterfaceEnabled())
}},opening:function(){var R,Q,P;
if(this.opts.minimumResultsForSearch>=0){this.showSearch(true)
}this.parent.opening.apply(this,arguments);
if(this.showSearchInput!==false){this.search.val(this.focusser.val())
}this.search.focus();
R=this.search.get(0);
if(R.createTextRange){Q=R.createTextRange();
Q.collapse(false);
Q.select()
}else{if(R.setSelectionRange){P=this.search.val().length;
R.setSelectionRange(P,P)
}}if(this.search.val()===""){if(this.nextSearchTerm!=m){this.search.val(this.nextSearchTerm);
this.search.select()
}}this.focusser.prop("disabled",true).val("");
this.updateResults(true);
this.opts.element.trigger(E.Event("select2-open"))
},close:function(){if(!this.opened()){return
}this.parent.close.apply(this,arguments);
this.focusser.prop("disabled",false);
if(this.opts.shouldFocusInput(this)){this.focusser.focus()
}},focus:function(){if(this.opened()){this.close()
}else{this.focusser.prop("disabled",false);
if(this.opts.shouldFocusInput(this)){this.focusser.focus()
}}},isFocused:function(){return this.container.hasClass("select2-container-active")
},cancel:function(){this.parent.cancel.apply(this,arguments);
this.focusser.prop("disabled",false);
if(this.opts.shouldFocusInput(this)){this.focusser.focus()
}},destroy:function(){E("label[for='"+this.focusser.attr("id")+"']").attr("for",this.opts.element.attr("id"));
this.parent.destroy.apply(this,arguments)
},initContainer:function(){var S,P=this.container,U=this.dropdown,T=a(),R;
if(this.opts.minimumResultsForSearch<0){this.showSearch(false)
}else{this.showSearch(true)
}this.selection=S=P.find(".select2-choice");
this.focusser=P.find(".select2-focusser");
S.find(".select2-chosen").attr("id","select2-chosen-"+T);
this.focusser.attr("aria-labelledby","select2-chosen-"+T);
this.results.attr("id","select2-results-"+T);
this.search.attr("aria-owns","select2-results-"+T);
this.focusser.attr("id","s2id_autogen"+T);
R=E("label[for='"+this.opts.element.attr("id")+"']");
this.focusser.prev().text(R.text()).attr("for",this.focusser.attr("id"));
var Q=this.opts.element.attr("title");
this.opts.element.attr("title",(Q||R.text()));
this.focusser.attr("tabindex",this.elementTabIndex);
this.search.attr("id",this.focusser.attr("id")+"_search");
this.search.prev().text(E("label[for='"+this.focusser.attr("id")+"']").text()).attr("for",this.search.attr("id"));
this.search.on("keydown",this.bind(function(V){if(!this.isInterfaceEnabled()){return
}if(V.which===L.PAGE_UP||V.which===L.PAGE_DOWN){B(V);
return
}switch(V.which){case L.UP:case L.DOWN:this.moveHighlight((V.which===L.UP)?-1:1);
B(V);
return;
case L.ENTER:this.selectHighlighted();
B(V);
return;
case L.TAB:this.selectHighlighted({noFocus:true});
return;
case L.ESC:this.cancel(V);
B(V);
return
}}));
this.search.on("blur",this.bind(function(V){if(document.activeElement===this.body().get(0)){window.setTimeout(this.bind(function(){if(this.opened()){this.search.focus()
}}),0)
}}));
this.focusser.on("keydown",this.bind(function(V){if(!this.isInterfaceEnabled()){return
}if(V.which===L.TAB||L.isControl(V)||L.isFunctionKey(V)||V.which===L.ESC){return
}if(this.opts.openOnEnter===false&&V.which===L.ENTER){return
}if(V.which==L.DOWN||V.which==L.UP||(V.which==L.ENTER&&this.opts.openOnEnter)){if(V.altKey||V.ctrlKey||V.shiftKey||V.metaKey){return
}this.open();
B(V);
return
}if(V.which==L.DELETE||V.which==L.BACKSPACE){if(this.opts.allowClear){this.clear()
}B(V);
return
}}));
G(this.focusser);
this.focusser.on("keyup-change input",this.bind(function(V){if(this.opts.minimumResultsForSearch>=0){V.stopPropagation();
if(this.opened()){return
}this.open()
}}));
S.on("mousedown touchstart","abbr",this.bind(function(V){if(!this.isInterfaceEnabled()){return
}this.clear();
b(V);
this.close();
this.selection.focus()
}));
S.on("mousedown touchstart",this.bind(function(V){p(S);
if(!this.container.hasClass("select2-container-active")){this.opts.element.trigger(E.Event("select2-focus"))
}if(this.opened()){this.close()
}else{if(this.isInterfaceEnabled()){this.open()
}}B(V)
}));
U.on("mousedown touchstart",this.bind(function(){this.search.focus()
}));
S.on("focus",this.bind(function(V){B(V)
}));
this.focusser.on("focus",this.bind(function(){if(!this.container.hasClass("select2-container-active")){this.opts.element.trigger(E.Event("select2-focus"))
}this.container.addClass("select2-container-active")
})).on("blur",this.bind(function(){if(!this.opened()){this.container.removeClass("select2-container-active");
this.opts.element.trigger(E.Event("select2-blur"))
}}));
this.search.on("focus",this.bind(function(){if(!this.container.hasClass("select2-container-active")){this.opts.element.trigger(E.Event("select2-focus"))
}this.container.addClass("select2-container-active")
}));
this.initContainerWidth();
this.opts.element.addClass("select2-offscreen");
this.setPlaceholder()
},clear:function(R){var S=this.selection.data("select2-data");
if(S){var Q=E.Event("select2-clearing");
this.opts.element.trigger(Q);
if(Q.isDefaultPrevented()){return
}var P=this.getPlaceholderOption();
this.opts.element.val(P?P.val():"");
this.selection.find(".select2-chosen").empty();
this.selection.removeData("select2-data");
this.setPlaceholder();
if(R!==false){this.opts.element.trigger({type:"select2-removed",val:this.id(S),choice:S});
this.triggerChange({removed:S})
}}},initSelection:function(){var Q;
if(this.isPlaceholderOptionSelected()){this.updateSelection(null);
this.close();
this.setPlaceholder()
}else{var P=this;
this.opts.initSelection.call(null,this.opts.element,function(R){if(R!==m&&R!==null){P.updateSelection(R);
P.close();
P.setPlaceholder();
P.nextSearchTerm=P.opts.nextSearchTerm(R,P.search.val())
}})
}},isPlaceholderOptionSelected:function(){var P;
if(!this.getPlaceholder()){return false
}return((P=this.getPlaceholderOption())!==m&&P.prop("selected"))||(this.opts.element.val()==="")||(this.opts.element.val()===m)||(this.opts.element.val()===null)
},prepareOpts:function(){var Q=this.parent.prepareOpts.apply(this,arguments),P=this;
if(Q.element.get(0).tagName.toLowerCase()==="select"){Q.initSelection=function(R,T){var S=R.find("option").filter(function(){return this.selected&&!this.disabled
});
T(P.optionToData(S))
}
}else{if("data" in Q){Q.initSelection=Q.initSelection||function(S,U){var T=S.val();
var R=null;
Q.query({matcher:function(V,Y,W){var X=u(T,Q.id(W));
if(X){R=W
}return X
},callback:!E.isFunction(U)?E.noop:function(){U(R)
}})
}
}}return Q
},getPlaceholder:function(){if(this.select){if(this.getPlaceholderOption()===m){return m
}}return this.parent.getPlaceholder.apply(this,arguments)
},setPlaceholder:function(){var P=this.getPlaceholder();
if(this.isPlaceholderOptionSelected()&&P!==m){if(this.select&&this.getPlaceholderOption()===m){return
}this.selection.find(".select2-chosen").html(this.opts.escapeMarkup(P));
this.selection.addClass("select2-default");
this.container.removeClass("select2-allowclear")
}},postprocessResults:function(U,Q,T){var S=0,P=this,V=true;
this.findHighlightableChoices().each2(function(W,X){if(u(P.id(X.data("select2-data")),P.opts.element.val())){S=W;
return false
}});
if(T!==false){if(Q===true&&S>=0){this.highlight(S)
}else{this.highlight(0)
}}if(Q===true){var R=this.opts.minimumResultsForSearch;
if(R>=0){this.showSearch(s(U.results)>=R)
}}},showSearch:function(P){if(this.showSearchInput===P){return
}this.showSearchInput=P;
this.dropdown.find(".select2-search").toggleClass("select2-search-hidden",!P);
this.dropdown.find(".select2-search").toggleClass("select2-offscreen",!P);
E(this.dropdown,this.container).toggleClass("select2-with-searchbox",P)
},onSelect:function(R,Q){if(!this.triggerSelect(R)){return
}var P=this.opts.element.val(),S=this.data();
this.opts.element.val(this.id(R));
this.updateSelection(R);
this.opts.element.trigger({type:"select2-selected",val:this.id(R),choice:R});
this.nextSearchTerm=this.opts.nextSearchTerm(R,this.search.val());
this.close();
if((!Q||!Q.noFocus)&&this.opts.shouldFocusInput(this)){this.focusser.focus()
}if(!u(P,this.id(R))){this.triggerChange({added:R,removed:S})
}},updateSelection:function(S){var Q=this.selection.find(".select2-chosen"),R,P;
this.selection.data("select2-data",S);
Q.empty();
if(S!==null){R=this.opts.formatSelection(S,Q,this.opts.escapeMarkup)
}if(R!==m){Q.append(R)
}P=this.opts.formatSelectionCssClass(S,Q);
if(P!==m){Q.addClass(P)
}this.selection.removeClass("select2-default");
if(this.opts.allowClear&&this.getPlaceholder()!==m){this.container.addClass("select2-allowclear")
}},val:function(){var T,Q=false,R=null,P=this,S=this.data();
if(arguments.length===0){return this.opts.element.val()
}T=arguments[0];
if(arguments.length>1){Q=arguments[1]
}if(this.select){this.select.val(T).find("option").filter(function(){return this.selected
}).each2(function(U,V){R=P.optionToData(V);
return false
});
this.updateSelection(R);
this.setPlaceholder();
if(Q){this.triggerChange({added:R,removed:S})
}}else{if(!T&&T!==0){this.clear(Q);
return
}if(this.opts.initSelection===m){throw new Error("cannot call val() if initSelection() is not defined")
}this.opts.element.val(T);
this.opts.initSelection(this.opts.element,function(U){P.opts.element.val(!U?"":P.id(U));
P.updateSelection(U);
P.setPlaceholder();
if(Q){P.triggerChange({added:U,removed:S})
}})
}},clearSearch:function(){this.search.val("");
this.focusser.val("")
},data:function(R){var Q,P=false;
if(arguments.length===0){Q=this.selection.data("select2-data");
if(Q==m){Q=null
}return Q
}else{if(arguments.length>1){P=arguments[1]
}if(!R){this.clear(P)
}else{Q=this.data();
this.opts.element.val(!R?"":this.id(R));
this.updateSelection(R);
if(P){this.triggerChange({added:R,removed:Q})
}}}}});
c=M(O,{createContainer:function(){var P=E(document.createElement("div")).attr({"class":"select2-container select2-container-multi"}).html(["<ul class='select2-choices'>","  <li class='select2-search-field'>","    <label for='' class='select2-offscreen'></label>","    <input type='text' autocomplete='off' autocorrect='off' autocapitalize='off' spellcheck='false' class='select2-input'>","  </li>","</ul>","<div class='select2-drop select2-drop-multi select2-display-none'>","   <ul class='select2-results'>","   </ul>","</div>"].join(""));
return P
},prepareOpts:function(){var Q=this.parent.prepareOpts.apply(this,arguments),P=this;
if(Q.element.get(0).tagName.toLowerCase()==="select"){Q.initSelection=function(R,T){var S=[];
R.find("option").filter(function(){return this.selected&&!this.disabled
}).each2(function(U,V){S.push(P.optionToData(V))
});
T(S)
}
}else{if("data" in Q){Q.initSelection=Q.initSelection||function(R,U){var S=i(R.val(),Q.separator);
var T=[];
Q.query({matcher:function(V,Y,W){var X=E.grep(S,function(Z){return u(Z,Q.id(W))
}).length;
if(X){T.push(W)
}return X
},callback:!E.isFunction(U)?E.noop:function(){var V=[];
for(var Y=0;
Y<S.length;
Y++){var Z=S[Y];
for(var X=0;
X<T.length;
X++){var W=T[X];
if(u(Z,Q.id(W))){V.push(W);
T.splice(X,1);
break
}}}U(V)
}})
}
}}return Q
},selectChoice:function(P){var Q=this.container.find(".select2-search-choice-focus");
if(Q.length&&P&&P[0]==Q[0]){}else{if(Q.length){this.opts.element.trigger("choice-deselected",Q)
}Q.removeClass("select2-search-choice-focus");
if(P&&P.length){this.close();
P.addClass("select2-search-choice-focus");
this.opts.element.trigger("choice-selected",P)
}}},destroy:function(){E("label[for='"+this.search.attr("id")+"']").attr("for",this.opts.element.attr("id"));
this.parent.destroy.apply(this,arguments)
},initContainer:function(){var P=".select2-choices",Q;
this.searchContainer=this.container.find(".select2-search-field");
this.selection=Q=this.container.find(P);
var R=this;
this.selection.on("click",".select2-search-choice:not(.select2-locked)",function(S){R.search[0].focus();
R.selectChoice(E(this))
});
this.search.attr("id","s2id_autogen"+a());
this.search.prev().text(E("label[for='"+this.opts.element.attr("id")+"']").text()).attr("for",this.search.attr("id"));
this.search.on("input paste",this.bind(function(){if(!this.isInterfaceEnabled()){return
}if(!this.opened()){this.open()
}}));
this.search.attr("tabindex",this.elementTabIndex);
this.keydowns=0;
this.search.on("keydown",this.bind(function(W){if(!this.isInterfaceEnabled()){return
}++this.keydowns;
var U=Q.find(".select2-search-choice-focus");
var V=U.prev(".select2-search-choice:not(.select2-locked)");
var T=U.next(".select2-search-choice:not(.select2-locked)");
var X=f(this.search);
if(U.length&&(W.which==L.LEFT||W.which==L.RIGHT||W.which==L.BACKSPACE||W.which==L.DELETE||W.which==L.ENTER)){var S=U;
if(W.which==L.LEFT&&V.length){S=V
}else{if(W.which==L.RIGHT){S=T.length?T:null
}else{if(W.which===L.BACKSPACE){if(this.unselect(U.first())){this.search.width(10);
S=V.length?V:T
}}else{if(W.which==L.DELETE){if(this.unselect(U.first())){this.search.width(10);
S=T.length?T:null
}}else{if(W.which==L.ENTER){S=null
}}}}}this.selectChoice(S);
B(W);
if(!S||!S.length){this.open()
}return
}else{if(((W.which===L.BACKSPACE&&this.keydowns==1)||W.which==L.LEFT)&&(X.offset==0&&!X.length)){this.selectChoice(Q.find(".select2-search-choice:not(.select2-locked)").last());
B(W);
return
}else{this.selectChoice(null)
}}if(this.opened()){switch(W.which){case L.UP:case L.DOWN:this.moveHighlight((W.which===L.UP)?-1:1);
B(W);
return;
case L.ENTER:this.selectHighlighted();
B(W);
return;
case L.TAB:this.selectHighlighted({noFocus:true});
this.close();
return;
case L.ESC:this.cancel(W);
B(W);
return
}}if(W.which===L.TAB||L.isControl(W)||L.isFunctionKey(W)||W.which===L.BACKSPACE||W.which===L.ESC){return
}if(W.which===L.ENTER){if(this.opts.openOnEnter===false){return
}else{if(W.altKey||W.ctrlKey||W.shiftKey||W.metaKey){return
}}}this.open();
if(W.which===L.PAGE_UP||W.which===L.PAGE_DOWN){B(W)
}if(W.which===L.ENTER){B(W)
}}));
this.search.on("keyup",this.bind(function(S){this.keydowns=0;
this.resizeSearch()
}));
this.search.on("blur",this.bind(function(S){this.container.removeClass("select2-container-active");
this.search.removeClass("select2-focused");
this.selectChoice(null);
if(!this.opened()){this.clearSearch()
}S.stopImmediatePropagation();
this.opts.element.trigger(E.Event("select2-blur"))
}));
this.container.on("click",P,this.bind(function(S){if(!this.isInterfaceEnabled()){return
}if(E(S.target).closest(".select2-search-choice").length>0){return
}this.selectChoice(null);
this.clearPlaceholder();
if(!this.container.hasClass("select2-container-active")){this.opts.element.trigger(E.Event("select2-focus"))
}this.open();
this.focusSearch();
S.preventDefault()
}));
this.container.on("focus",P,this.bind(function(){if(!this.isInterfaceEnabled()){return
}if(!this.container.hasClass("select2-container-active")){this.opts.element.trigger(E.Event("select2-focus"))
}this.container.addClass("select2-container-active");
this.dropdown.addClass("select2-drop-active");
this.clearPlaceholder()
}));
this.initContainerWidth();
this.opts.element.addClass("select2-offscreen");
this.clearSearch()
},enableInterface:function(){if(this.parent.enableInterface.apply(this,arguments)){this.search.prop("disabled",!this.isInterfaceEnabled())
}},initSelection:function(){var Q;
if(this.opts.element.val()===""&&this.opts.element.text()===""){this.updateSelection([]);
this.close();
this.clearSearch()
}if(this.select||this.opts.element.val()!==""){var P=this;
this.opts.initSelection.call(null,this.opts.element,function(R){if(R!==m&&R!==null){P.updateSelection(R);
P.close();
P.clearSearch()
}})
}},clearSearch:function(){var Q=this.getPlaceholder(),P=this.getMaxSearchWidth();
if(Q!==m&&this.getVal().length===0&&this.search.hasClass("select2-focused")===false){this.search.val(Q).addClass("select2-default");
this.search.width(P>0?P:this.container.css("width"))
}else{this.search.val("").width(10)
}},clearPlaceholder:function(){if(this.search.hasClass("select2-default")){this.search.val("").removeClass("select2-default")
}},opening:function(){this.clearPlaceholder();
this.resizeSearch();
this.parent.opening.apply(this,arguments);
this.focusSearch();
if(this.search.val()===""){if(this.nextSearchTerm!=m){this.search.val(this.nextSearchTerm);
this.search.select()
}}this.updateResults(true);
this.search.focus();
this.opts.element.trigger(E.Event("select2-open"))
},close:function(){if(!this.opened()){return
}this.parent.close.apply(this,arguments)
},focus:function(){this.close();
this.search.focus()
},isFocused:function(){return this.search.hasClass("select2-focused")
},updateSelection:function(S){var R=[],Q=[],P=this;
E(S).each(function(){if(r(P.id(this),R)<0){R.push(P.id(this));
Q.push(this)
}});
S=Q;
this.selection.find(".select2-search-choice").remove();
E(S).each(function(){P.addSelectedChoice(this)
});
P.postprocessResults()
},tokenize:function(){var P=this.search.val();
P=this.opts.tokenizer.call(this,P,this.data(),this.bind(this.onSelect),this.opts);
if(P!=null&&P!=m){this.search.val(P);
if(P.length>0){this.open()
}}},onSelect:function(Q,P){if(!this.triggerSelect(Q)){return
}this.addSelectedChoice(Q);
this.opts.element.trigger({type:"selected",val:this.id(Q),choice:Q});
this.nextSearchTerm=this.opts.nextSearchTerm(Q,this.search.val());
this.clearSearch();
this.updateResults();
if(this.select||!this.opts.closeOnSelect){this.postprocessResults(Q,false,this.opts.closeOnSelect===true)
}if(this.opts.closeOnSelect){this.close();
this.search.width(10)
}else{if(this.countSelectableResults()>0){this.search.width(10);
this.resizeSearch();
if(this.getMaximumSelectionSize()>0&&this.val().length>=this.getMaximumSelectionSize()){this.updateResults(true)
}else{if(this.nextSearchTerm!=m){this.search.val(this.nextSearchTerm);
this.updateResults();
this.search.select()
}}this.positionDropdown()
}else{this.close();
this.search.width(10)
}}this.triggerChange({added:Q});
if(!P||!P.noFocus){this.focusSearch()
}},cancel:function(){this.close();
this.focusSearch()
},addSelectedChoice:function(T){var V=!T.locked,R=E("<li class='select2-search-choice'>    <div></div>    <a href='#' class='select2-search-choice-close' tabindex='-1'></a></li>"),W=E("<li class='select2-search-choice select2-locked'><div></div></li>");
var S=V?R:W,P=this.id(T),Q=this.getVal(),U,X;
U=this.opts.formatSelection(T,S.find("div"),this.opts.escapeMarkup);
if(U!=m){S.find("div").replaceWith("<div>"+U+"</div>")
}X=this.opts.formatSelectionCssClass(T,S.find("div"));
if(X!=m){S.addClass(X)
}if(V){S.find(".select2-search-choice-close").on("mousedown",B).on("click dblclick",this.bind(function(Y){if(!this.isInterfaceEnabled()){return
}this.unselect(E(Y.target));
this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus");
B(Y);
this.close();
this.focusSearch()
})).on("focus",this.bind(function(){if(!this.isInterfaceEnabled()){return
}this.container.addClass("select2-container-active");
this.dropdown.addClass("select2-drop-active")
}))
}S.data("select2-data",T);
S.insertBefore(this.searchContainer);
Q.push(P);
this.setVal(Q)
},unselect:function(R){var T=this.getVal(),S,Q;
R=R.closest(".select2-search-choice");
if(R.length===0){throw"Invalid argument: "+R+". Must be .select2-search-choice"
}S=R.data("select2-data");
if(!S){return
}var P=E.Event("select2-removing");
P.val=this.id(S);
P.choice=S;
this.opts.element.trigger(P);
if(P.isDefaultPrevented()){return false
}while((Q=r(this.id(S),T))>=0){T.splice(Q,1);
this.setVal(T);
if(this.select){this.postprocessResults()
}}R.remove();
this.opts.element.trigger({type:"select2-removed",val:this.id(S),choice:S});
this.triggerChange({removed:S});
return true
},postprocessResults:function(T,Q,S){var U=this.getVal(),V=this.results.find(".select2-result"),R=this.results.find(".select2-result-with-children"),P=this;
V.each2(function(X,W){var Y=P.id(W.data("select2-data"));
if(r(Y,U)>=0){W.addClass("select2-selected");
W.find(".select2-result-selectable").addClass("select2-selected")
}});
R.each2(function(X,W){if(!W.is(".select2-result-selectable")&&W.find(".select2-result-selectable:not(.select2-selected)").length===0){W.addClass("select2-selected")
}});
if(this.highlight()==-1&&S!==false){P.highlight(0)
}if(!this.opts.createSearchChoice&&!V.filter(".select2-result:not(.select2-selected)").length>0){if(!T||T&&!T.more&&this.results.find(".select2-no-results").length===0){if(z(P.opts.formatNoMatches,"formatNoMatches")){this.results.append("<li class='select2-no-results'>"+D(P.opts.formatNoMatches,P.search.val())+"</li>")
}}}},getMaxSearchWidth:function(){return this.selection.width()-h(this.search)
},resizeSearch:function(){var U,S,R,P,Q,T=h(this.search);
U=n(this.search)+10;
S=this.search.offset().left;
R=this.selection.width();
P=this.selection.offset().left;
Q=R-(S-P)-T;
if(Q<U){Q=R-T
}if(Q<40){Q=R-T
}if(Q<=0){Q=U
}this.search.width(Math.floor(Q))
},getVal:function(){var P;
if(this.select){P=this.select.val();
return P===null?[]:P
}else{P=this.opts.element.val();
return i(P,this.opts.separator)
}},setVal:function(Q){var P;
if(this.select){this.select.val(Q)
}else{P=[];
E(Q).each(function(){if(r(this,P)<0){P.push(this)
}});
this.opts.element.val(P.length===0?"":P.join(this.opts.separator))
}},buildChangeDetails:function(P,S){var S=S.slice(0),P=P.slice(0);
for(var R=0;
R<S.length;
R++){for(var Q=0;
Q<P.length;
Q++){if(u(this.opts.id(S[R]),this.opts.id(P[Q]))){S.splice(R,1);
if(R>0){R--
}P.splice(Q,1);
Q--
}}}return{added:S,removed:P}
},val:function(S,Q){var R,P=this;
if(arguments.length===0){return this.getVal()
}R=this.data();
if(!R.length){R=[]
}if(!S&&S!==0){this.opts.element.val("");
this.updateSelection([]);
this.clearSearch();
if(Q){this.triggerChange({added:this.data(),removed:R})
}return
}this.setVal(S);
if(this.select){this.opts.initSelection(this.select,this.bind(this.updateSelection));
if(Q){this.triggerChange(this.buildChangeDetails(R,this.data()))
}}else{if(this.opts.initSelection===m){throw new Error("val() cannot be called if initSelection() is not defined")
}this.opts.initSelection(this.opts.element,function(U){var T=E.map(U,P.id);
P.setVal(T);
P.updateSelection(U);
P.clearSearch();
if(Q){P.triggerChange(P.buildChangeDetails(R,P.data()))
}})
}this.clearSearch()
},onSortStart:function(){if(this.select){throw new Error("Sorting of elements is not supported when attached to <select>. Attach to <input type='hidden'/> instead.")
}this.search.width(0);
this.searchContainer.hide()
},onSortEnd:function(){var Q=[],P=this;
this.searchContainer.show();
this.searchContainer.appendTo(this.searchContainer.parent());
this.resizeSearch();
this.selection.find(".select2-search-choice").each(function(){Q.push(P.opts.id(E(this).data("select2-data")))
});
this.setVal(Q);
this.triggerChange()
},data:function(R,S){var Q=this,T,P;
if(arguments.length===0){return this.selection.children(".select2-search-choice").map(function(){return E(this).data("select2-data")
}).get()
}else{P=this.data();
if(!R){R=[]
}T=E.map(R,function(U){return Q.opts.id(U)
});
this.setVal(T);
this.updateSelection(R);
this.clearSearch();
if(S){this.triggerChange(this.buildChangeDetails(P,this.data()))
}}}});
E.fn.select2=function(){var U=Array.prototype.slice.call(arguments,0),Q,T,P,W,Y,X=["val","destroy","opened","open","close","focus","isFocused","container","dropdown","onSortStart","onSortEnd","enable","disable","readonly","positionDropdown","data","search"],V=["opened","isFocused","container","dropdown"],R=["val","data"],S={search:"externalSearch"};
this.each(function(){if(U.length===0||typeof(U[0])==="object"){Q=U.length===0?{}:E.extend({},U[0]);
Q.element=E(this);
if(Q.element.get(0).tagName.toLowerCase()==="select"){Y=Q.element.prop("multiple")
}else{Y=Q.multiple||false;
if("tags" in Q){Q.multiple=Y=true
}}T=Y?new window.Select2["class"].multi():new window.Select2["class"].single();
T.init(Q)
}else{if(typeof(U[0])==="string"){if(r(U[0],X)<0){throw"Unknown method: "+U[0]
}W=m;
T=E(this).data("select2");
if(T===m){return
}P=U[0];
if(P==="container"){W=T.container
}else{if(P==="dropdown"){W=T.dropdown
}else{if(S[P]){P=S[P]
}W=T[P].apply(T,U.slice(1))
}}if(r(U[0],V)>=0||(r(U[0],R)&&U.length==1)){return false
}}else{throw"Invalid arguments to select2 plugin: "+U
}}});
return(W===m)?this:W
};
E.fn.select2.defaults={width:"copy",loadMorePadding:0,closeOnSelect:true,openOnEnter:false,containerCss:{},dropdownCss:{},containerCssClass:"",dropdownCssClass:"",formatResult:function(Q,R,T,P){var S=[];
v(Q.text,T.term,S,P);
return S.join("")
},formatSelection:function(R,Q,P){return R?P(R.text):m
},sortResults:function(Q,P,R){return Q
},formatResultCssClass:function(P){return P.css
},formatSelectionCssClass:function(Q,P){return m
},formatMatches:function(P){return P+" results are available, use up and down arrow keys to navigate."
},formatNoMatches:function(){return"No matches found"
},formatInputTooShort:function(P,Q){var R=Q-P.length;
return"Please enter "+R+" or more character"+(R==1?"":"s")
},formatInputTooLong:function(Q,P){var R=Q.length-P;
return"Please delete "+R+" character"+(R==1?"":"s")
},formatSelectionTooBig:function(P){return"You can only select "+P
},formatLoadMore:function(P){return"Loading more results…"
},formatSearching:function(){return"Searching…"
},minimumResultsForSearch:0,minimumInputLength:0,maximumInputLength:null,maximumSelectionSize:0,id:function(P){return P==m?null:P.id
},matcher:function(P,Q){return e(""+Q).toUpperCase().indexOf(e(""+P).toUpperCase())>=0
},separator:",",tokenSeparators:[],tokenizer:g,escapeMarkup:H,blurOnChange:false,selectOnBlur:false,adaptContainerCssClass:function(P){return P
},adaptDropdownCssClass:function(P){return null
},nextSearchTerm:function(P,Q){return m
},searchInputPlaceholder:"",createSearchChoicePosition:"top",shouldFocusInput:function(P){if(P.opts.minimumResultsForSearch<0){return false
}return true
}};
E.fn.select2.ajaxDefaults={transport:E.ajax,params:{type:"GET",cache:false,dataType:"json"}};
window.Select2={query:{ajax:F,local:I,tags:A},util:{debounce:k,markMatch:v,escapeMarkup:H,stripDiacritics:e},"class":{"abstract":O,single:y,multi:c}}
}(jQuery));