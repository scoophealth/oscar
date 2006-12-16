/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(typeof dojo=="undefined"){
var dj_global=this;
var dj_currentContext=this;
function dj_undef(_1,_2){
return (typeof (_2||dj_currentContext)[_1]=="undefined");
}
if(dj_undef("djConfig",this)){
var djConfig={};
}
if(dj_undef("dojo",this)){
var dojo={};
}
dojo.global=function(){
return dj_currentContext;
};
dojo.locale=djConfig.locale;
dojo.version={major:0,minor:4,patch:1,flag:"",revision:Number("$Rev: 6824 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.evalProp=function(_3,_4,_5){
if((!_4)||(!_3)){
return undefined;
}
if(!dj_undef(_3,_4)){
return _4[_3];
}
return (_5?(_4[_3]={}):undefined);
};
dojo.parseObjPath=function(_6,_7,_8){
var _9=(_7||dojo.global());
var _a=_6.split(".");
var _b=_a.pop();
for(var i=0,l=_a.length;i<l&&_9;i++){
_9=dojo.evalProp(_a[i],_9,_8);
}
return {obj:_9,prop:_b};
};
dojo.evalObjPath=function(_e,_f){
if(typeof _e!="string"){
return dojo.global();
}
if(_e.indexOf(".")==-1){
return dojo.evalProp(_e,dojo.global(),_f);
}
var ref=dojo.parseObjPath(_e,dojo.global(),_f);
if(ref){
return dojo.evalProp(ref.prop,ref.obj,_f);
}
return null;
};
dojo.errorToString=function(_11){
if(!dj_undef("message",_11)){
return _11.message;
}else{
if(!dj_undef("description",_11)){
return _11.description;
}else{
return _11;
}
}
};
dojo.raise=function(_12,_13){
if(_13){
_12=_12+": "+dojo.errorToString(_13);
}else{
_12=dojo.errorToString(_12);
}
try{
if(djConfig.isDebug){
dojo.hostenv.println("FATAL exception raised: "+_12);
}
}
catch(e){
}
throw _13||Error(_12);
};
dojo.debug=function(){
};
dojo.debugShallow=function(obj){
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
function dj_eval(_15){
return dj_global.eval?dj_global.eval(_15):eval(_15);
}
dojo.unimplemented=function(_16,_17){
var _18="'"+_16+"' not implemented";
if(_17!=null){
_18+=" "+_17;
}
dojo.raise(_18);
};
dojo.deprecated=function(_19,_1a,_1b){
var _1c="DEPRECATED: "+_19;
if(_1a){
_1c+=" "+_1a;
}
if(_1b){
_1c+=" -- will be removed in version: "+_1b;
}
dojo.debug(_1c);
};
dojo.render=(function(){
function vscaffold(_1d,_1e){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_1d};
for(var i=0;i<_1e.length;i++){
tmp[_1e[i]]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _21={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_21;
}else{
for(var _22 in _21){
if(typeof djConfig[_22]=="undefined"){
djConfig[_22]=_21[_22];
}
}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
var _25=uri.lastIndexOf("/");
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _26={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},setModulePrefix:function(_27,_28){
this.modulePrefixes_[_27]={name:_27,value:_28};
},moduleHasPrefix:function(_29){
var mp=this.modulePrefixes_;
return Boolean(mp[_29]&&mp[_29].value);
},getModulePrefix:function(_2b){
if(this.moduleHasPrefix(_2b)){
return this.modulePrefixes_[_2b].value;
}
return _2b;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _2c in _26){
dojo.hostenv[_2c]=_26[_2c];
}
})();
dojo.hostenv.loadPath=function(_2d,_2e,cb){
var uri;
if(_2d.charAt(0)=="/"||_2d.match(/^\w+:/)){
uri=_2d;
}else{
uri=this.getBaseScriptUri()+_2d;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_2e?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_2e,cb);
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return true;
}
var _33=this.getText(uri,null,true);
if(!_33){
return false;
}
this.loadedUris[uri]=true;
if(cb){
_33="("+_33+")";
}
var _34=dj_eval(_33);
if(cb){
cb(_34);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_36,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_36,false));
};
dojo.loaded=function(){
};
dojo.unloaded=function(){
};
dojo.hostenv.loaded=function(){
this.loadNotifying=true;
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
this.modulesLoadedListeners=[];
this.loadNotifying=false;
dojo.loaded();
};
dojo.hostenv.unloaded=function(){
var mll=this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
};
dojo.addOnLoad=function(obj,_3d){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_3d]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_40){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_40]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
dojo.hostenv.callLoaded();
}
};
dojo.hostenv.callLoaded=function(){
if(typeof setTimeout=="object"){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
};
dojo.hostenv.getModuleSymbols=function(_42){
var _43=_42.split(".");
for(var i=_43.length;i>0;i--){
var _45=_43.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_45)){
_43[0]="../"+_43[0];
}else{
var _46=this.getModulePrefix(_45);
if(_46!=_45){
_43.splice(0,i,_46);
break;
}
}
}
return _43;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_47,_48,_49){
if(!_47){
return;
}
_49=this._global_omit_module_check||_49;
var _4a=this.findModule(_47,false);
if(_4a){
return _4a;
}
if(dj_undef(_47,this.loading_modules_)){
this.addedToLoadingCount.push(_47);
}
this.loading_modules_[_47]=1;
var _4b=_47.replace(/\./g,"/")+".js";
var _4c=_47.split(".");
var _4d=this.getModuleSymbols(_47);
var _4e=((_4d[0].charAt(0)!="/")&&!_4d[0].match(/^\w+:/));
var _4f=_4d[_4d.length-1];
var ok;
if(_4f=="*"){
_47=_4c.slice(0,-1).join(".");
while(_4d.length){
_4d.pop();
_4d.push(this.pkgFileName);
_4b=_4d.join("/")+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,!_49?_47:null);
if(ok){
break;
}
_4d.pop();
}
}else{
_4b=_4d.join("/")+".js";
_47=_4c.join(".");
var _51=!_49?_47:null;
ok=this.loadPath(_4b,_51);
if(!ok&&!_48){
_4d.pop();
while(_4d.length){
_4b=_4d.join("/")+".js";
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
_4d.pop();
_4b=_4d.join("/")+"/"+this.pkgFileName+".js";
if(_4e&&_4b.charAt(0)=="/"){
_4b=_4b.slice(1);
}
ok=this.loadPath(_4b,_51);
if(ok){
break;
}
}
}
if(!ok&&!_49){
dojo.raise("Could not load '"+_47+"'; last tried '"+_4b+"'");
}
}
if(!_49&&!this["isXDomain"]){
_4a=this.findModule(_47,false);
if(!_4a){
dojo.raise("symbol '"+_47+"' is not defined after loading '"+_4b+"'");
}
}
return _4a;
};
dojo.hostenv.startPackage=function(_52){
var _53=String(_52);
var _54=_53;
var _55=_52.split(/\./);
if(_55[_55.length-1]=="*"){
_55.pop();
_54=_55.join(".");
}
var _56=dojo.evalObjPath(_54,true);
this.loaded_modules_[_53]=_56;
this.loaded_modules_[_54]=_56;
return _56;
};
dojo.hostenv.findModule=function(_57,_58){
var lmn=String(_57);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_58){
dojo.raise("no loaded module named '"+_57+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_5a){
var _5b=_5a["common"]||[];
var _5c=_5a[dojo.hostenv.name_]?_5b.concat(_5a[dojo.hostenv.name_]||[]):_5b.concat(_5a["default"]||[]);
for(var x=0;x<_5c.length;x++){
var _5e=_5c[x];
if(_5e.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_5e);
}else{
dojo.hostenv.loadModule(_5e);
}
}
};
dojo.require=function(_5f){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(_60,_61){
var _62=arguments[0];
if((_62===true)||(_62=="common")||(_62&&dojo.render[_62].capable)){
var _63=[];
for(var i=1;i<arguments.length;i++){
_63.push(arguments[i]);
}
dojo.require.apply(dojo,_63);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(_65){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_66,_67){
return dojo.hostenv.setModulePrefix(_66,_67);
};
dojo.setModulePrefix=function(_68,_69){
dojo.deprecated("dojo.setModulePrefix(\""+_68+"\", \""+_69+"\")","replaced by dojo.registerModulePath","0.5");
return dojo.registerModulePath(_68,_69);
};
dojo.exists=function(obj,_6b){
var p=_6b.split(".");
for(var i=0;i<p.length;i++){
if(!obj[p[i]]){
return false;
}
obj=obj[p[i]];
}
return true;
};
dojo.hostenv.normalizeLocale=function(_6e){
var _6f=_6e?_6e.toLowerCase():dojo.locale;
if(_6f=="root"){
_6f="ROOT";
}
return _6f;
};
dojo.hostenv.searchLocalePath=function(_70,_71,_72){
_70=dojo.hostenv.normalizeLocale(_70);
var _73=_70.split("-");
var _74=[];
for(var i=_73.length;i>0;i--){
_74.push(_73.slice(0,i).join("-"));
}
_74.push(false);
if(_71){
_74.reverse();
}
for(var j=_74.length-1;j>=0;j--){
var loc=_74[j]||"ROOT";
var _78=_72(loc);
if(_78){
break;
}
}
};
dojo.hostenv.localesGenerated;
dojo.hostenv.registerNlsPrefix=function(){
dojo.registerModulePath("nls","nls");
};
dojo.hostenv.preloadLocalizations=function(){
if(dojo.hostenv.localesGenerated){
dojo.hostenv.registerNlsPrefix();
function preload(_79){
_79=dojo.hostenv.normalizeLocale(_79);
dojo.hostenv.searchLocalePath(_79,true,function(loc){
for(var i=0;i<dojo.hostenv.localesGenerated.length;i++){
if(dojo.hostenv.localesGenerated[i]==loc){
dojo["require"]("nls.dojo_"+loc);
return true;
}
}
return false;
});
}
preload();
var _7c=djConfig.extraLocale||[];
for(var i=0;i<_7c.length;i++){
preload(_7c[i]);
}
}
dojo.hostenv.preloadLocalizations=function(){
};
};
dojo.requireLocalization=function(_7e,_7f,_80,_81){
dojo.hostenv.preloadLocalizations();
var _82=dojo.hostenv.normalizeLocale(_80);
var _83=[_7e,"nls",_7f].join(".");
var _84="";
if(_81){
var _85=_81.split(",");
for(var i=0;i<_85.length;i++){
if(_82.indexOf(_85[i])==0){
if(_85[i].length>_84.length){
_84=_85[i];
}
}
}
if(!_84){
_84="ROOT";
}
}
var _87=_81?_84:_82;
var _88=dojo.hostenv.findModule(_83);
var _89=null;
if(_88){
if(djConfig.localizationComplete&&_88._built){
return;
}
var _8a=_87.replace("-","_");
var _8b=_83+"."+_8a;
_89=dojo.hostenv.findModule(_8b);
}
if(!_89){
_88=dojo.hostenv.startPackage(_83);
var _8c=dojo.hostenv.getModuleSymbols(_7e);
var _8d=_8c.concat("nls").join("/");
var _8e;
dojo.hostenv.searchLocalePath(_87,_81,function(loc){
var _90=loc.replace("-","_");
var _91=_83+"."+_90;
var _92=false;
if(!dojo.hostenv.findModule(_91)){
dojo.hostenv.startPackage(_91);
var _93=[_8d];
if(loc!="ROOT"){
_93.push(loc);
}
_93.push(_7f);
var _94=_93.join("/")+".js";
_92=dojo.hostenv.loadPath(_94,null,function(_95){
var _96=function(){
};
_96.prototype=_8e;
_88[_90]=new _96();
for(var j in _95){
_88[_90][j]=_95[j];
}
});
}else{
_92=true;
}
if(_92&&_88[_90]){
_8e=_88[_90];
}else{
_88[_90]=_8e;
}
if(_81){
return true;
}
});
}
if(_81&&_82!=_84){
_88[_82.replace("-","_")]=_88[_84.replace("-","_")];
}
};
(function(){
var _98=djConfig.extraLocale;
if(_98){
if(!_98 instanceof Array){
_98=[_98];
}
var req=dojo.requireLocalization;
dojo.requireLocalization=function(m,b,_9c,_9d){
req(m,b,_9c,_9d);
if(_9c){
return;
}
for(var i=0;i<_98.length;i++){
req(m,b,_98[i],_9d);
}
};
}
})();
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _9f=document.location.toString();
var _a0=_9f.split("?",2);
if(_a0.length>1){
var _a1=_a0[1];
var _a2=_a1.split("&");
for(var x in _a2){
var sp=_a2[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _a6=document.getElementsByTagName("script");
var _a7=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_a6.length;i++){
var src=_a6[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_a7);
if(m){
var _ab=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_ab+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_ab;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_ab;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=(drh.UA=navigator.userAgent);
var dav=(drh.AV=navigator.appVersion);
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _b3=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_b3>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_b3+6,_b3+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
var cm=document["compatMode"];
drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;
dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
var _b5=window["document"];
var tdi=_b5["implementation"];
if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
if(drh.safari){
var tmp=dua.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
}else{
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _b9=null;
var _ba=null;
try{
_b9=new XMLHttpRequest();
}
catch(e){
}
if(!_b9){
for(var i=0;i<3;++i){
var _bc=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_b9=new ActiveXObject(_bc);
}
catch(e){
_ba=e;
}
if(_b9){
dojo.hostenv._XMLHTTP_PROGIDS=[_bc];
break;
}
}
}
if(!_b9){
return dojo.raise("XMLHTTP not available",_ba);
}
return _b9;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_be,_bf){
if(!_be){
this._blockAsync=true;
}
var _c0=this.getXmlhttpObject();
function isDocumentOk(_c1){
var _c2=_c1["status"];
return Boolean((!_c2)||((200<=_c2)&&(300>_c2))||(_c2==304));
}
if(_be){
var _c3=this,_c4=null,gbl=dojo.global();
var xhr=dojo.evalObjPath("dojo.io.XMLHTTPTransport");
_c0.onreadystatechange=function(){
if(_c4){
gbl.clearTimeout(_c4);
_c4=null;
}
if(_c3._blockAsync||(xhr&&xhr._blockAsync)){
_c4=gbl.setTimeout(function(){
_c0.onreadystatechange.apply(this);
},10);
}else{
if(4==_c0.readyState){
if(isDocumentOk(_c0)){
_be(_c0.responseText);
}
}
}
};
}
_c0.open("GET",uri,_be?true:false);
try{
_c0.send(null);
if(_be){
return null;
}
if(!isDocumentOk(_c0)){
var err=Error("Unable to load "+uri+" status:"+_c0.status);
err.status=_c0.status;
err.responseText=_c0.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_bf)&&(!_be)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _c0.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_c8){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_c8);
}else{
try{
var _c9=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_c9){
_c9=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_c8));
_c9.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_c8+"</div>");
}
catch(e2){
window.status=_c8;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_cb,_cc,fp){
var _ce=_cb["on"+_cc]||function(){
};
_cb["on"+_cc]=function(){
fp.apply(_cb,arguments);
_ce.apply(_cb,arguments);
};
return true;
}
function dj_load_init(e){
var _d0=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_d0!="domcontentloaded"&&_d0!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _d1=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_d1();
dojo.hostenv.modulesLoaded();
}else{
dojo.hostenv.modulesLoadedListeners.unshift(_d1);
}
}
if(document.addEventListener){
if(dojo.render.html.opera||(dojo.render.html.moz&&!djConfig.delayMozLoadingFix)){
document.addEventListener("DOMContentLoaded",dj_load_init,null);
}
window.addEventListener("load",dj_load_init,null);
}
if(dojo.render.html.ie&&dojo.render.os.win){
document.attachEvent("onreadystatechange",function(e){
if(document.readyState=="complete"){
dj_load_init();
}
});
}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){
var _timer=setInterval(function(){
if(/loaded|complete/.test(document.readyState)){
dj_load_init();
}
},10);
}
if(dojo.render.html.ie){
dj_addNodeEvtHdlr(window,"beforeunload",function(){
dojo.hostenv._unloading=true;
window.setTimeout(function(){
dojo.hostenv._unloading=false;
},0);
});
}
dj_addNodeEvtHdlr(window,"unload",function(){
dojo.hostenv.unloaded();
if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){
dojo.hostenv.unloaded();
}
});
dojo.hostenv.makeWidgets=function(){
var _d3=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_d3=_d3.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_d3=_d3.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_d3.length>0)){
if(dojo.evalObjPath("dojo.widget.Parse")){
var _d4=new dojo.xml.Parse();
if(_d3.length>0){
for(var x=0;x<_d3.length;x++){
var _d6=document.getElementById(_d3[x]);
if(!_d6){
continue;
}
var _d7=_d4.parseElement(_d6,null,true);
dojo.widget.getParser().createComponents(_d7);
}
}else{
if(djConfig.parseWidgets){
var _d7=_d4.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_d7);
}
}
}
}
};
dojo.addOnLoad(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.namespaces.add("v","urn:schemas-microsoft-com:vml");
document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
if(!dj_undef("document",this)){
dj_currentDocument=this.document;
}
dojo.doc=function(){
return dj_currentDocument;
};
dojo.body=function(){
return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];
};
dojo.byId=function(id,doc){
if((id)&&((typeof id=="string")||(id instanceof String))){
if(!doc){
doc=dj_currentDocument;
}
var ele=doc.getElementById(id);
if(ele&&(ele.id!=id)&&doc.all){
ele=null;
eles=doc.all[id];
if(eles){
if(eles.length){
for(var i=0;i<eles.length;i++){
if(eles[i].id==id){
ele=eles[i];
break;
}
}
}else{
ele=eles;
}
}
}
return ele;
}
return id;
};
dojo.setContext=function(_dc,_dd){
dj_currentContext=_dc;
dj_currentDocument=_dd;
};
dojo._fireCallback=function(_de,_df,_e0){
if((_df)&&((typeof _de=="string")||(_de instanceof String))){
_de=_df[_de];
}
return (_df?_de.apply(_df,_e0||[]):_de());
};
dojo.withGlobal=function(_e1,_e2,_e3,_e4){
var _e5;
var _e6=dj_currentContext;
var _e7=dj_currentDocument;
try{
dojo.setContext(_e1,_e1.document);
_e5=dojo._fireCallback(_e2,_e3,_e4);
}
finally{
dojo.setContext(_e6,_e7);
}
return _e5;
};
dojo.withDoc=function(_e8,_e9,_ea,_eb){
var _ec;
var _ed=dj_currentDocument;
try{
dj_currentDocument=_e8;
_ec=dojo._fireCallback(_e9,_ea,_eb);
}
finally{
dj_currentDocument=_ed;
}
return _ec;
};
}
(function(){
if(typeof dj_usingBootstrap!="undefined"){
return;
}
var _ee=false;
var _ef=false;
var _f0=false;
if((typeof this["load"]=="function")&&((typeof this["Packages"]=="function")||(typeof this["Packages"]=="object"))){
_ee=true;
}else{
if(typeof this["load"]=="function"){
_ef=true;
}else{
if(window.widget){
_f0=true;
}
}
}
var _f1=[];
if((this["djConfig"])&&((djConfig["isDebug"])||(djConfig["debugAtAllCosts"]))){
_f1.push("debug.js");
}
if((this["djConfig"])&&(djConfig["debugAtAllCosts"])&&(!_ee)&&(!_f0)){
_f1.push("browser_debug.js");
}
var _f2=djConfig["baseScriptUri"];
if((this["djConfig"])&&(djConfig["baseLoaderUri"])){
_f2=djConfig["baseLoaderUri"];
}
for(var x=0;x<_f1.length;x++){
var _f4=_f2+"src/"+_f1[x];
if(_ee||_ef){
load(_f4);
}else{
try{
document.write("<scr"+"ipt type='text/javascript' src='"+_f4+"'></scr"+"ipt>");
}
catch(e){
var _f5=document.createElement("script");
_f5.src=_f4;
document.getElementsByTagName("head")[0].appendChild(_f5);
}
}
}
})();
dojo.provide("dojo.dom");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="function"){
try{
return wh instanceof Element;
}
catch(e){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _f7=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_f7.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_f9,_fa){
var _fb=_f9.firstChild;
while(_fb&&_fb.nodeType!=dojo.dom.ELEMENT_NODE){
_fb=_fb.nextSibling;
}
if(_fa&&_fb&&_fb.tagName&&_fb.tagName.toLowerCase()!=_fa.toLowerCase()){
_fb=dojo.dom.nextElement(_fb,_fa);
}
return _fb;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_fc,_fd){
var _fe=_fc.lastChild;
while(_fe&&_fe.nodeType!=dojo.dom.ELEMENT_NODE){
_fe=_fe.previousSibling;
}
if(_fd&&_fe&&_fe.tagName&&_fe.tagName.toLowerCase()!=_fd.toLowerCase()){
_fe=dojo.dom.prevElement(_fe,_fd);
}
return _fe;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(_ff,_100){
if(!_ff){
return null;
}
do{
_ff=_ff.nextSibling;
}while(_ff&&_ff.nodeType!=dojo.dom.ELEMENT_NODE);
if(_ff&&_100&&_100.toLowerCase()!=_ff.tagName.toLowerCase()){
return dojo.dom.nextElement(_ff,_100);
}
return _ff;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_102){
if(!node){
return null;
}
if(_102){
_102=_102.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_102&&_102.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_102);
}
return node;
};
dojo.dom.moveChildren=function(_103,_104,trim){
var _106=0;
if(trim){
while(_103.hasChildNodes()&&_103.firstChild.nodeType==dojo.dom.TEXT_NODE){
_103.removeChild(_103.firstChild);
}
while(_103.hasChildNodes()&&_103.lastChild.nodeType==dojo.dom.TEXT_NODE){
_103.removeChild(_103.lastChild);
}
}
while(_103.hasChildNodes()){
_104.appendChild(_103.firstChild);
_106++;
}
return _106;
};
dojo.dom.copyChildren=function(_107,_108,trim){
var _10a=_107.cloneNode(true);
return this.moveChildren(_10a,_108,trim);
};
dojo.dom.replaceChildren=function(node,_10c){
var _10d=[];
if(dojo.render.html.ie){
for(var i=0;i<node.childNodes.length;i++){
_10d.push(node.childNodes[i]);
}
}
dojo.dom.removeChildren(node);
node.appendChild(_10c);
for(var i=0;i<_10d.length;i++){
dojo.dom.destroyNode(_10d[i]);
}
};
dojo.dom.removeChildren=function(node){
var _110=node.childNodes.length;
while(node.hasChildNodes()){
dojo.dom.removeNode(node.firstChild);
}
return _110;
};
dojo.dom.replaceNode=function(node,_112){
return node.parentNode.replaceChild(_112,node);
};
dojo.dom.destroyNode=function(node){
if(node.parentNode){
node=dojo.dom.removeNode(node);
}
if(node.nodeType!=3){
if(dojo.evalObjPath("dojo.event.browser.clean",false)){
dojo.event.browser.clean(node);
}
if(dojo.render.html.ie){
node.outerHTML="";
}
}
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_116,_117){
var _118=[];
var _119=(_116&&(_116 instanceof Function||typeof _116=="function"));
while(node){
if(!_119||_116(node)){
_118.push(node);
}
if(_117&&_118.length>0){
return _118[0];
}
node=node.parentNode;
}
if(_117){
return null;
}
return _118;
};
dojo.dom.getAncestorsByTag=function(node,tag,_11c){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_11c);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_121,_122){
if(_122&&node){
node=node.parentNode;
}
while(node){
if(node==_121){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(node.xml){
return node.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
var _125=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _126=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_126.length;i++){
try{
doc=new ActiveXObject(_126[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_125.implementation)&&(_125.implementation.createDocument)){
doc=_125.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_129){
if(!_129){
_129="text/xml";
}
if(!dj_undef("DOMParser")){
var _12a=new DOMParser();
return _12a.parseFromString(str,_129);
}else{
if(!dj_undef("ActiveXObject")){
var _12b=dojo.dom.createDocument();
if(_12b){
_12b.async=false;
_12b.loadXML(str);
return _12b;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _12c=dojo.doc();
if(_12c.createElement){
var tmp=_12c.createElement("xml");
tmp.innerHTML=str;
if(_12c.implementation&&_12c.implementation.createDocument){
var _12e=_12c.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_12e.importNode(tmp.childNodes.item(i),true);
}
return _12e;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_131){
if(_131.firstChild){
_131.insertBefore(node,_131.firstChild);
}else{
_131.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_134){
if((_134!=true)&&(node===ref||node.nextSibling===ref)){
return false;
}
var _135=ref.parentNode;
_135.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_138){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_138!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_138);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_13c){
if((!node)||(!ref)||(!_13c)){
return false;
}
switch(_13c.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_13e,_13f){
var _140=_13e.childNodes;
if(!_140.length||_140.length==_13f){
_13e.appendChild(node);
return true;
}
if(_13f==0){
return dojo.dom.prependChild(node,_13e);
}
return dojo.dom.insertAfter(node,_140[_13f-1]);
};
dojo.dom.textContent=function(node,text){
if(arguments.length>1){
var _143=dojo.doc();
dojo.dom.replaceChildren(node,_143.createTextNode(text));
return text;
}else{
if(node.textContent!=undefined){
return node.textContent;
}
var _144="";
if(node==null){
return _144;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
_144+=dojo.dom.textContent(node.childNodes[i]);
break;
case 3:
case 2:
case 4:
_144+=node.childNodes[i].nodeValue;
break;
default:
break;
}
}
return _144;
}
};
dojo.dom.hasParent=function(node){
return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName==String(arguments[i])){
return String(arguments[i]);
}
}
}
return "";
};
dojo.dom.setAttributeNS=function(elem,_14a,_14b,_14c){
if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){
elem.setAttributeNS(_14a,_14b,_14c);
}else{
var _14d=elem.ownerDocument;
var _14e=_14d.createNode(2,_14b,_14a);
_14e.nodeValue=_14c;
elem.setAttributeNode(_14e);
}
};
dojo.provide("dojo.xml.Parse");
dojo.xml.Parse=function(){
var isIE=((dojo.render.html.capable)&&(dojo.render.html.ie));
function getTagName(node){
try{
return node.tagName.toLowerCase();
}
catch(e){
return "";
}
}
function getDojoTagName(node){
var _152=getTagName(node);
if(!_152){
return "";
}
if((dojo.widget)&&(dojo.widget.tags[_152])){
return _152;
}
var p=_152.indexOf(":");
if(p>=0){
return _152;
}
if(_152.substr(0,5)=="dojo:"){
return _152;
}
if(dojo.render.html.capable&&dojo.render.html.ie&&node.scopeName!="HTML"){
return node.scopeName.toLowerCase()+":"+_152;
}
if(_152.substr(0,4)=="dojo"){
return "dojo:"+_152.substring(4);
}
var djt=node.getAttribute("dojoType")||node.getAttribute("dojotype");
if(djt){
if(djt.indexOf(":")<0){
djt="dojo:"+djt;
}
return djt.toLowerCase();
}
djt=node.getAttributeNS&&node.getAttributeNS(dojo.dom.dojoml,"type");
if(djt){
return "dojo:"+djt.toLowerCase();
}
try{
djt=node.getAttribute("dojo:type");
}
catch(e){
}
if(djt){
return "dojo:"+djt.toLowerCase();
}
if((dj_global["djConfig"])&&(!djConfig["ignoreClassNames"])){
var _155=node.className||node.getAttribute("class");
if((_155)&&(_155.indexOf)&&(_155.indexOf("dojo-")!=-1)){
var _156=_155.split(" ");
for(var x=0,c=_156.length;x<c;x++){
if(_156[x].slice(0,5)=="dojo-"){
return "dojo:"+_156[x].substr(5).toLowerCase();
}
}
}
}
return "";
}
this.parseElement=function(node,_15a,_15b,_15c){
var _15d=getTagName(node);
if(isIE&&_15d.indexOf("/")==0){
return null;
}
try{
var attr=node.getAttribute("parseWidgets");
if(attr&&attr.toLowerCase()=="false"){
return {};
}
}
catch(e){
}
var _15f=true;
if(_15b){
var _160=getDojoTagName(node);
_15d=_160||_15d;
_15f=Boolean(_160);
}
var _161={};
_161[_15d]=[];
var pos=_15d.indexOf(":");
if(pos>0){
var ns=_15d.substring(0,pos);
_161["ns"]=ns;
if((dojo.ns)&&(!dojo.ns.allow(ns))){
_15f=false;
}
}
if(_15f){
var _164=this.parseAttributes(node);
for(var attr in _164){
if((!_161[_15d][attr])||(typeof _161[_15d][attr]!="array")){
_161[_15d][attr]=[];
}
_161[_15d][attr].push(_164[attr]);
}
_161[_15d].nodeRef=node;
_161.tagName=_15d;
_161.index=_15c||0;
}
var _165=0;
for(var i=0;i<node.childNodes.length;i++){
var tcn=node.childNodes.item(i);
switch(tcn.nodeType){
case dojo.dom.ELEMENT_NODE:
var ctn=getDojoTagName(tcn)||getTagName(tcn);
if(!_161[ctn]){
_161[ctn]=[];
}
_161[ctn].push(this.parseElement(tcn,true,_15b,_165));
if((tcn.childNodes.length==1)&&(tcn.childNodes.item(0).nodeType==dojo.dom.TEXT_NODE)){
_161[ctn][_161[ctn].length-1].value=tcn.childNodes.item(0).nodeValue;
}
_165++;
break;
case dojo.dom.TEXT_NODE:
if(node.childNodes.length==1){
_161[_15d].push({value:node.childNodes.item(0).nodeValue});
}
break;
default:
break;
}
}
return _161;
};
this.parseAttributes=function(node){
var _16a={};
var atts=node.attributes;
var _16c,i=0;
while((_16c=atts[i++])){
if(isIE){
if(!_16c){
continue;
}
if((typeof _16c=="object")&&(typeof _16c.nodeValue=="undefined")||(_16c.nodeValue==null)||(_16c.nodeValue=="")){
continue;
}
}
var nn=_16c.nodeName.split(":");
nn=(nn.length==2)?nn[1]:_16c.nodeName;
_16a[nn]={value:_16c.nodeValue};
}
return _16a;
};
};
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_16f,_170){
if(!dojo.lang.isFunction(_170)){
dojo.raise("dojo.inherits: superclass argument ["+_170+"] must be a function (subclass: ["+_16f+"']");
}
_16f.prototype=new _170();
_16f.prototype.constructor=_16f;
_16f.superclass=_170.prototype;
_16f["super"]=_170.prototype;
};
dojo.lang._mixin=function(obj,_172){
var tobj={};
for(var x in _172){
if((typeof tobj[x]=="undefined")||(tobj[x]!=_172[x])){
obj[x]=_172[x];
}
}
if(dojo.render.html.ie&&(typeof (_172["toString"])=="function")&&(_172["toString"]!=obj["toString"])&&(_172["toString"]!=tobj["toString"])){
obj.toString=_172.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_176){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_179,_17a){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_179.prototype,arguments[i]);
}
return _179;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_17d,_17e,_17f,_180){
if(!dojo.lang.isArrayLike(_17d)&&dojo.lang.isArrayLike(_17e)){
dojo.deprecated("dojo.lang.find(value, array)","use dojo.lang.find(array, value) instead","0.5");
var temp=_17d;
_17d=_17e;
_17e=temp;
}
var _182=dojo.lang.isString(_17d);
if(_182){
_17d=_17d.split("");
}
if(_180){
var step=-1;
var i=_17d.length-1;
var end=-1;
}else{
var step=1;
var i=0;
var end=_17d.length;
}
if(_17f){
while(i!=end){
if(_17d[i]===_17e){
return i;
}
i+=step;
}
}else{
while(i!=end){
if(_17d[i]==_17e){
return i;
}
i+=step;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_186,_187,_188){
return dojo.lang.find(_186,_187,_188,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_189,_18a){
return dojo.lang.find(_189,_18a)>-1;
};
dojo.lang.isObject=function(it){
if(typeof it=="undefined"){
return false;
}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));
};
dojo.lang.isArray=function(it){
return (it&&it instanceof Array||typeof it=="array");
};
dojo.lang.isArrayLike=function(it){
if((!it)||(dojo.lang.isUndefined(it))){
return false;
}
if(dojo.lang.isString(it)){
return false;
}
if(dojo.lang.isFunction(it)){
return false;
}
if(dojo.lang.isArray(it)){
return true;
}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){
return false;
}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(it){
return (it instanceof Function||typeof it=="function");
};
(function(){
if((dojo.render.html.capable)&&(dojo.render.html["safari"])){
dojo.lang.isFunction=function(it){
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
}
})();
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));
};
dojo.lang.isBoolean=function(it){
return (it instanceof Boolean||typeof it=="boolean");
};
dojo.lang.isNumber=function(it){
return (it instanceof Number||typeof it=="number");
};
dojo.lang.isUndefined=function(it){
return ((typeof (it)=="undefined")&&(it==undefined));
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_195,_196){
var fcn=(dojo.lang.isString(_196)?_195[_196]:_196)||function(){
};
return function(){
return fcn.apply(_195,arguments);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_198,_199,_19a){
var nso=(_199||dojo.lang.anon);
if((_19a)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_198){
return x;
}
}
catch(e){
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_198;
return ret;
};
dojo.lang.forward=function(_19e){
return function(){
return this[_19e].apply(this,arguments);
};
};
dojo.lang.curry=function(_19f,func){
var _1a1=[];
_19f=_19f||dj_global;
if(dojo.lang.isString(func)){
func=_19f[func];
}
for(var x=2;x<arguments.length;x++){
_1a1.push(arguments[x]);
}
var _1a3=(func["__preJoinArity"]||func.length)-_1a1.length;
function gather(_1a4,_1a5,_1a6){
var _1a7=_1a6;
var _1a8=_1a5.slice(0);
for(var x=0;x<_1a4.length;x++){
_1a8.push(_1a4[x]);
}
_1a6=_1a6-_1a4.length;
if(_1a6<=0){
var res=func.apply(_19f,_1a8);
_1a6=_1a7;
return res;
}else{
return function(){
return gather(arguments,_1a8,_1a6);
};
}
}
return gather([],_1a1,_1a3);
};
dojo.lang.curryArguments=function(_1ab,func,args,_1ae){
var _1af=[];
var x=_1ae||0;
for(x=_1ae;x<args.length;x++){
_1af.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[_1ab,func].concat(_1af));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(farr,cb,_1b5,_1b6){
if(!farr.length){
if(typeof _1b6=="function"){
_1b6();
}
return;
}
if((typeof _1b5=="undefined")&&(typeof cb=="number")){
_1b5=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_1b5){
_1b5=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_1b5,_1b6);
},_1b5);
};
dojo.provide("dojo.lang.array");
dojo.lang.mixin(dojo.lang,{has:function(obj,name){
try{
return typeof obj[name]!="undefined";
}
catch(e){
return false;
}
},isEmpty:function(obj){
if(dojo.lang.isObject(obj)){
var tmp={};
var _1bb=0;
for(var x in obj){
if(obj[x]&&(!tmp[x])){
_1bb++;
break;
}
}
return _1bb==0;
}else{
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length==0;
}
}
},map:function(arr,obj,_1bf){
var _1c0=dojo.lang.isString(arr);
if(_1c0){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_1bf)){
_1bf=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_1bf){
var _1c1=obj;
obj=_1bf;
_1bf=_1c1;
}
}
if(Array.map){
var _1c2=Array.map(arr,_1bf,obj);
}else{
var _1c2=[];
for(var i=0;i<arr.length;++i){
_1c2.push(_1bf.call(obj,arr[i]));
}
}
if(_1c0){
return _1c2.join("");
}else{
return _1c2;
}
},reduce:function(arr,_1c5,obj,_1c7){
var _1c8=_1c5;
if(arguments.length==1){
dojo.debug("dojo.lang.reduce called with too few arguments!");
return false;
}else{
if(arguments.length==2){
_1c7=_1c5;
_1c8=arr.shift();
}else{
if(arguments.lenght==3){
if(dojo.lang.isFunction(obj)){
_1c7=obj;
obj=null;
}
}else{
if(dojo.lang.isFunction(obj)){
var tmp=_1c7;
_1c7=obj;
obj=tmp;
}
}
}
}
var ob=obj?obj:dj_global;
dojo.lang.map(arr,function(val){
_1c8=_1c7.call(ob,_1c8,val);
});
return _1c8;
},forEach:function(_1cc,_1cd,_1ce){
if(dojo.lang.isString(_1cc)){
_1cc=_1cc.split("");
}
if(Array.forEach){
Array.forEach(_1cc,_1cd,_1ce);
}else{
if(!_1ce){
_1ce=dj_global;
}
for(var i=0,l=_1cc.length;i<l;i++){
_1cd.call(_1ce,_1cc[i],i,_1cc);
}
}
},_everyOrSome:function(_1d1,arr,_1d3,_1d4){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[_1d1?"every":"some"](arr,_1d3,_1d4);
}else{
if(!_1d4){
_1d4=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _1d7=_1d3.call(_1d4,arr[i],i,arr);
if(_1d1&&!_1d7){
return false;
}else{
if((!_1d1)&&(_1d7)){
return true;
}
}
}
return Boolean(_1d1);
}
},every:function(arr,_1d9,_1da){
return this._everyOrSome(true,arr,_1d9,_1da);
},some:function(arr,_1dc,_1dd){
return this._everyOrSome(false,arr,_1dc,_1dd);
},filter:function(arr,_1df,_1e0){
var _1e1=dojo.lang.isString(arr);
if(_1e1){
arr=arr.split("");
}
var _1e2;
if(Array.filter){
_1e2=Array.filter(arr,_1df,_1e0);
}else{
if(!_1e0){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_1e0=dj_global;
}
_1e2=[];
for(var i=0;i<arr.length;i++){
if(_1df.call(_1e0,arr[i],i,arr)){
_1e2.push(arr[i]);
}
}
}
if(_1e1){
return _1e2.join("");
}else{
return _1e2;
}
},unnest:function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
},toArray:function(_1e7,_1e8){
var _1e9=[];
for(var i=_1e8||0;i<_1e7.length;i++){
_1e9.push(_1e7[i]);
}
return _1e9;
}});
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(func,_1ec){
var _1ed=window,_1ee=2;
if(!dojo.lang.isFunction(func)){
_1ed=func;
func=_1ec;
_1ec=arguments[2];
_1ee++;
}
if(dojo.lang.isString(func)){
func=_1ed[func];
}
var args=[];
for(var i=_1ee;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
func.apply(_1ed,args);
},_1ec);
};
dojo.lang.clearTimeout=function(_1f1){
dojo.global().clearTimeout(_1f1);
};
dojo.lang.getNameInObj=function(ns,item){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===item){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(obj,deep){
var i,ret;
if(obj===null){
return null;
}
if(dojo.lang.isObject(obj)){
ret=new obj.constructor();
for(i in obj){
if(dojo.lang.isUndefined(ret[i])){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}
}else{
if(dojo.lang.isArray(obj)){
ret=[];
for(i=0;i<obj.length;i++){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}else{
ret=obj;
}
}
return ret;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.getObjPathValue=function(_1fa,_1fb,_1fc){
with(dojo.parseObjPath(_1fa,_1fb,_1fc)){
return dojo.evalProp(prop,obj,_1fc);
}
};
dojo.lang.setObjPathValue=function(_1fd,_1fe,_1ff,_200){
dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");
if(arguments.length<4){
_200=true;
}
with(dojo.parseObjPath(_1fd,_1ff,_200)){
if(obj&&(_200||(prop in obj))){
obj[prop]=_1fe;
}
}
};
dojo.provide("dojo.lang.declare");
dojo.lang.declare=function(_201,_202,init,_204){
if((dojo.lang.isFunction(_204))||((!_204)&&(!dojo.lang.isFunction(init)))){
var temp=_204;
_204=init;
init=temp;
}
var _206=[];
if(dojo.lang.isArray(_202)){
_206=_202;
_202=_206.shift();
}
if(!init){
init=dojo.evalObjPath(_201,false);
if((init)&&(!dojo.lang.isFunction(init))){
init=null;
}
}
var ctor=dojo.lang.declare._makeConstructor();
var scp=(_202?_202.prototype:null);
if(scp){
scp.prototyping=true;
ctor.prototype=new _202();
scp.prototyping=false;
}
ctor.superclass=scp;
ctor.mixins=_206;
for(var i=0,l=_206.length;i<l;i++){
dojo.lang.extend(ctor,_206[i].prototype);
}
ctor.prototype.initializer=null;
ctor.prototype.declaredClass=_201;
if(dojo.lang.isArray(_204)){
dojo.lang.extend.apply(dojo.lang,[ctor].concat(_204));
}else{
dojo.lang.extend(ctor,(_204)||{});
}
dojo.lang.extend(ctor,dojo.lang.declare._common);
ctor.prototype.constructor=ctor;
ctor.prototype.initializer=(ctor.prototype.initializer)||(init)||(function(){
});
var _20b=dojo.parseObjPath(_201,null,true);
_20b.obj[_20b.prop]=ctor;
return ctor;
};
dojo.lang.declare._makeConstructor=function(){
return function(){
var self=this._getPropContext();
var s=self.constructor.superclass;
if((s)&&(s.constructor)){
if(s.constructor==arguments.callee){
this._inherited("constructor",arguments);
}else{
this._contextMethod(s,"constructor",arguments);
}
}
var ms=(self.constructor.mixins)||([]);
for(var i=0,m;(m=ms[i]);i++){
(((m.prototype)&&(m.prototype.initializer))||(m)).apply(this,arguments);
}
if((!this.prototyping)&&(self.initializer)){
self.initializer.apply(this,arguments);
}
};
};
dojo.lang.declare._common={_getPropContext:function(){
return (this.___proto||this);
},_contextMethod:function(_211,_212,args){
var _214,_215=this.___proto;
this.___proto=_211;
try{
_214=_211[_212].apply(this,(args||[]));
}
catch(e){
throw e;
}
finally{
this.___proto=_215;
}
return _214;
},_inherited:function(prop,args){
var p=this._getPropContext();
do{
if((!p.constructor)||(!p.constructor.superclass)){
return;
}
p=p.constructor.superclass;
}while(!(prop in p));
return (dojo.lang.isFunction(p[prop])?this._contextMethod(p,prop,args):p[prop]);
},inherited:function(prop,args){
dojo.deprecated("'inherited' method is dangerous, do not up-call! 'inherited' is slated for removal in 0.5; name your super class (or use superclass property) instead.","0.5");
this._inherited(prop,args);
}};
dojo.declare=dojo.lang.declare;
dojo.provide("dojo.ns");
dojo.ns={namespaces:{},failed:{},loading:{},loaded:{},register:function(name,_21c,_21d,_21e){
if(!_21e||!this.namespaces[name]){
this.namespaces[name]=new dojo.ns.Ns(name,_21c,_21d);
}
},allow:function(name){
if(this.failed[name]){
return false;
}
if((djConfig.excludeNamespace)&&(dojo.lang.inArray(djConfig.excludeNamespace,name))){
return false;
}
return ((name==this.dojo)||(!djConfig.includeNamespace)||(dojo.lang.inArray(djConfig.includeNamespace,name)));
},get:function(name){
return this.namespaces[name];
},require:function(name){
var ns=this.namespaces[name];
if((ns)&&(this.loaded[name])){
return ns;
}
if(!this.allow(name)){
return false;
}
if(this.loading[name]){
dojo.debug("dojo.namespace.require: re-entrant request to load namespace \""+name+"\" must fail.");
return false;
}
var req=dojo.require;
this.loading[name]=true;
try{
if(name=="dojo"){
req("dojo.namespaces.dojo");
}else{
if(!dojo.hostenv.moduleHasPrefix(name)){
dojo.registerModulePath(name,"../"+name);
}
req([name,"manifest"].join("."),false,true);
}
if(!this.namespaces[name]){
this.failed[name]=true;
}
}
finally{
this.loading[name]=false;
}
return this.namespaces[name];
}};
dojo.ns.Ns=function(name,_225,_226){
this.name=name;
this.module=_225;
this.resolver=_226;
this._loaded=[];
this._failed=[];
};
dojo.ns.Ns.prototype.resolve=function(name,_228,_229){
if(!this.resolver||djConfig["skipAutoRequire"]){
return false;
}
var _22a=this.resolver(name,_228);
if((_22a)&&(!this._loaded[_22a])&&(!this._failed[_22a])){
var req=dojo.require;
req(_22a,false,true);
if(dojo.hostenv.findModule(_22a,false)){
this._loaded[_22a]=true;
}else{
if(!_229){
dojo.raise("dojo.ns.Ns.resolve: module '"+_22a+"' not found after loading via namespace '"+this.name+"'");
}
this._failed[_22a]=true;
}
}
return Boolean(this._loaded[_22a]);
};
dojo.registerNamespace=function(name,_22d,_22e){
dojo.ns.register.apply(dojo.ns,arguments);
};
dojo.registerNamespaceResolver=function(name,_230){
var n=dojo.ns.namespaces[name];
if(n){
n.resolver=_230;
}
};
dojo.registerNamespaceManifest=function(_232,path,name,_235,_236){
dojo.registerModulePath(name,path);
dojo.registerNamespace(name,_235,_236);
};
dojo.registerNamespace("dojo","dojo.widget");
dojo.provide("dojo.event.common");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_238){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _23b=dl.nameAnonFunc(args[2],ao.adviceObj,_238);
ao.adviceFunc=_23b;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _23b=dl.nameAnonFunc(args[0],ao.srcObj,_238);
ao.srcFunc=_23b;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _23b=dl.nameAnonFunc(args[1],dj_global,_238);
ao.srcFunc=_23b;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _23b=dl.nameAnonFunc(args[3],dj_global,_238);
ao.adviceObj=dj_global;
ao.adviceFunc=_23b;
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _23b=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_238);
ao.aroundFunc=_23b;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
if(!ao.adviceFunc){
dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);
dojo.debugShallow(ao);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.connect(ao);
}
ao.srcFunc="onkeypress";
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _23d={};
for(var x in ao){
_23d[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_23d.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_23d));
});
return mjps;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.log=function(a1,a2){
var _245;
if((arguments.length==1)&&(typeof a1=="object")){
_245=a1;
}else{
_245={srcObj:a1,srcFunc:a2};
}
_245.adviceFunc=function(){
var _246=[];
for(var x=0;x<arguments.length;x++){
_246.push(arguments[x]);
}
dojo.debug("("+_245.srcObj+")."+_245.srcFunc,":",_246.join(", "));
};
this.kwConnect(_245);
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this._kwConnectImpl=function(_24d,_24e){
var fn=(_24e)?"disconnect":"connect";
if(typeof _24d["srcFunc"]=="function"){
_24d.srcObj=_24d["srcObj"]||dj_global;
var _250=dojo.lang.nameAnonFunc(_24d.srcFunc,_24d.srcObj,true);
_24d.srcFunc=_250;
}
if(typeof _24d["adviceFunc"]=="function"){
_24d.adviceObj=_24d["adviceObj"]||dj_global;
var _250=dojo.lang.nameAnonFunc(_24d.adviceFunc,_24d.adviceObj,true);
_24d.adviceFunc=_250;
}
_24d.srcObj=_24d["srcObj"]||dj_global;
_24d.adviceObj=_24d["adviceObj"]||_24d["targetObj"]||dj_global;
_24d.adviceFunc=_24d["adviceFunc"]||_24d["targetFunc"];
return dojo.event[fn](_24d);
};
this.kwConnect=function(_251){
return this._kwConnectImpl(_251,false);
};
this.disconnect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(!ao.adviceFunc){
return;
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.disconnect(ao);
}
ao.srcFunc="onkeypress";
}
if(!ao.srcObj[ao.srcFunc]){
return null;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);
mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
return mjp;
};
this.kwDisconnect=function(_254){
return this._kwConnectImpl(_254,true);
};
};
dojo.event.MethodInvocation=function(_255,obj,args){
this.jp_=_255;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_25d){
this.object=obj||dj_global;
this.methodname=_25d;
this.methodfunc=this.object[_25d];
this.squelch=false;
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_25f){
if(!obj){
obj=dj_global;
}
if(!obj[_25f]){
obj[_25f]=function(){
};
if(!obj[_25f]){
dojo.raise("Cannot set do-nothing method on that object "+_25f);
}
}else{
if((!dojo.lang.isFunction(obj[_25f]))&&(!dojo.lang.isAlien(obj[_25f]))){
return null;
}
}
var _260=_25f+"$joinpoint";
var _261=_25f+"$joinpoint$method";
var _262=obj[_260];
if(!_262){
var _263=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_263=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_260,_261,_25f]);
}
}
var _264=obj[_25f].length;
obj[_261]=obj[_25f];
_262=obj[_260]=new dojo.event.MethodJoinPoint(obj,_261);
obj[_25f]=function(){
var args=[];
if((_263)&&(!arguments.length)){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
if(obj.event){
evt=obj.event;
}else{
evt=window.event;
}
}
}
}
catch(e){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(_263)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _262.run.apply(_262,args);
};
obj[_25f].__preJoinArity=_264;
}
return _262;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _26a=[];
for(var x=0;x<args.length;x++){
_26a[x]=args[x];
}
var _26c=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _26e=marr[0]||dj_global;
var _26f=marr[1];
if(!_26e[_26f]){
dojo.raise("function \""+_26f+"\" does not exist on \""+_26e+"\"");
}
var _270=marr[2]||dj_global;
var _271=marr[3];
var msg=marr[6];
var _273;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _26e[_26f].apply(_26e,to.args);
}};
to.args=_26a;
var _275=parseInt(marr[4]);
var _276=((!isNaN(_275))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _279=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event._canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_26c(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_271){
_270[_271].call(_270,to);
}else{
if((_276)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_26e[_26f].call(_26e,to);
}else{
_26e[_26f].apply(_26e,args);
}
},_275);
}else{
if(msg){
_26e[_26f].call(_26e,to);
}else{
_26e[_26f].apply(_26e,args);
}
}
}
};
var _27c=function(){
if(this.squelch){
try{
return _26c.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _26c.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_27c);
}
var _27d;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_27d=mi.proceed();
}else{
if(this.methodfunc){
_27d=this.object[this.methodname].apply(this.object,args);
}
}
}
catch(e){
if(!this.squelch){
dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_27c);
}
return (this.methodfunc)?_27d:null;
},getArr:function(kind){
var type="after";
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
type="before";
}else{
if(kind=="around"){
type="around";
}
}
if(!this[type]){
this[type]=[];
}
return this[type];
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"]);
},addAdvice:function(_282,_283,_284,_285,_286,_287,once,_289,rate,_28b){
var arr=this.getArr(_286);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_282,_283,_284,_285,_289,rate,_28b];
if(once){
if(this.hasAdvice(_282,_283,_286,arr)>=0){
return;
}
}
if(_287=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_28e,_28f,_290,arr){
if(!arr){
arr=this.getArr(_290);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _28f=="object")?(new String(_28f)).toString():_28f;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_28e)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_296,_297,_298,once){
var arr=this.getArr(_298);
var ind=this.hasAdvice(_296,_297,_298,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_296,_297,_298,arr);
}
return true;
}});
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_29c){
if(!this.topics[_29c]){
this.topics[_29c]=new this.TopicImpl(_29c);
}
return this.topics[_29c];
};
this.registerPublisher=function(_29d,obj,_29f){
var _29d=this.getTopic(_29d);
_29d.registerPublisher(obj,_29f);
};
this.subscribe=function(_2a0,obj,_2a2){
var _2a0=this.getTopic(_2a0);
_2a0.subscribe(obj,_2a2);
};
this.unsubscribe=function(_2a3,obj,_2a5){
var _2a3=this.getTopic(_2a3);
_2a3.unsubscribe(obj,_2a5);
};
this.destroy=function(_2a6){
this.getTopic(_2a6).destroy();
delete this.topics[_2a6];
};
this.publishApply=function(_2a7,args){
var _2a7=this.getTopic(_2a7);
_2a7.sendMessage.apply(_2a7,args);
};
this.publish=function(_2a9,_2aa){
var _2a9=this.getTopic(_2a9);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_2a9.sendMessage.apply(_2a9,args);
};
};
dojo.event.topic.TopicImpl=function(_2ad){
this.topicName=_2ad;
this.subscribe=function(_2ae,_2af){
var tf=_2af||_2ae;
var to=(!_2af)?dj_global:_2ae;
return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_2b2,_2b3){
var tf=(!_2b3)?_2b2:_2b3;
var to=(!_2b3)?null:_2b2;
return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this._getJoinPoint=function(){
return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");
};
this.setSquelch=function(_2b6){
this._getJoinPoint().squelch=_2b6;
};
this.destroy=function(){
this._getJoinPoint().disconnect();
};
this.registerPublisher=function(_2b7,_2b8){
dojo.event.connect(_2b7,_2b8,this,"sendMessage");
};
this.sendMessage=function(_2b9){
};
};
dojo.provide("dojo.event.browser");
dojo._ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_2bc){
var na;
var tna;
if(_2bc){
tna=_2bc.all||_2bc.getElementsByTagName("*");
na=[_2bc];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _2c0={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
try{
if(el&&el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
catch(e){
}
}
na=null;
};
};
if(dojo.render.html.ie){
dojo.addOnUnload(function(){
dojo._ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
if(dojo.widget){
for(var name in dojo.widget._templateCache){
if(dojo.widget._templateCache[name].node){
dojo.dom.destroyNode(dojo.widget._templateCache[name].node);
dojo.widget._templateCache[name].node=null;
delete dojo.widget._templateCache[name].node;
}
}
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo._ie_clobber.clobberNodes=[];
});
}
dojo.event.browser=new function(){
var _2c5=0;
this.normalizedEventName=function(_2c6){
switch(_2c6){
case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _2c6;
break;
default:
return _2c6.toLowerCase();
break;
}
};
this.clean=function(node){
if(dojo.render.html.ie){
dojo._ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!dojo.render.html.ie){
return;
}
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo._ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_2ca){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_2ca.length;x++){
node.__clobberAttrs__.push(_2ca[x]);
}
};
this.removeListener=function(node,_2cd,fp,_2cf){
if(!_2cf){
var _2cf=false;
}
_2cd=dojo.event.browser.normalizedEventName(_2cd);
if((_2cd=="onkey")||(_2cd=="key")){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_2cf);
}
_2cd="onkeypress";
}
if(_2cd.substr(0,2)=="on"){
_2cd=_2cd.substr(2);
}
if(node.removeEventListener){
node.removeEventListener(_2cd,fp,_2cf);
}
};
this.addListener=function(node,_2d1,fp,_2d3,_2d4){
if(!node){
return;
}
if(!_2d3){
var _2d3=false;
}
_2d1=dojo.event.browser.normalizedEventName(_2d1);
if((_2d1=="onkey")||(_2d1=="key")){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_2d3,_2d4);
}
_2d1="onkeypress";
}
if(_2d1.substr(0,2)!="on"){
_2d1="on"+_2d1;
}
if(!_2d4){
var _2d5=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_2d3){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_2d5=fp;
}
if(node.addEventListener){
node.addEventListener(_2d1.substr(2),_2d5,_2d3);
return _2d5;
}else{
if(typeof node[_2d1]=="function"){
var _2d8=node[_2d1];
node[_2d1]=function(e){
_2d8(e);
return _2d5(e);
};
}else{
node[_2d1]=_2d5;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_2d1]);
}
return _2d5;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_2db,_2dc){
if(typeof _2db!="function"){
dojo.raise("listener not a function: "+_2db);
}
dojo.event.browser.currentEvent.currentTarget=_2dc;
return _2db.call(_2dc,dojo.event.browser.currentEvent);
};
this._stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this._preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_2df){
if(!evt){
if(window["event"]){
evt=window.event;
}
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if(evt["type"]=="keydown"&&dojo.render.html.ie){
switch(evt.keyCode){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_LEFT_WINDOW:
case evt.KEY_RIGHT_WINDOW:
case evt.KEY_SELECT:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
case evt.KEY_NUMPAD_0:
case evt.KEY_NUMPAD_1:
case evt.KEY_NUMPAD_2:
case evt.KEY_NUMPAD_3:
case evt.KEY_NUMPAD_4:
case evt.KEY_NUMPAD_5:
case evt.KEY_NUMPAD_6:
case evt.KEY_NUMPAD_7:
case evt.KEY_NUMPAD_8:
case evt.KEY_NUMPAD_9:
case evt.KEY_NUMPAD_PERIOD:
break;
case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
case evt.KEY_PAGE_UP:
case evt.KEY_PAGE_DOWN:
case evt.KEY_END:
case evt.KEY_HOME:
case evt.KEY_LEFT_ARROW:
case evt.KEY_UP_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_INSERT:
case evt.KEY_DELETE:
case evt.KEY_F1:
case evt.KEY_F2:
case evt.KEY_F3:
case evt.KEY_F4:
case evt.KEY_F5:
case evt.KEY_F6:
case evt.KEY_F7:
case evt.KEY_F8:
case evt.KEY_F9:
case evt.KEY_F10:
case evt.KEY_F11:
case evt.KEY_F12:
case evt.KEY_F12:
case evt.KEY_F13:
case evt.KEY_F14:
case evt.KEY_F15:
case evt.KEY_CLEAR:
case evt.KEY_HELP:
evt.key=evt.keyCode;
break;
default:
if(evt.ctrlKey||evt.altKey){
var _2e1=evt.keyCode;
if(_2e1>=65&&_2e1<=90&&evt.shiftKey==false){
_2e1+=32;
}
if(_2e1>=1&&_2e1<=26&&evt.ctrlKey){
_2e1+=96;
}
evt.key=String.fromCharCode(_2e1);
}
}
}else{
if(evt["type"]=="keypress"){
if(dojo.render.html.opera){
if(evt.which==0){
evt.key=evt.keyCode;
}else{
if(evt.which>0){
switch(evt.which){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key=evt.which;
break;
default:
var _2e1=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_2e1+=32;
}
evt.key=String.fromCharCode(_2e1);
}
}
}
}else{
if(dojo.render.html.ie){
if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){
evt.key=String.fromCharCode(evt.keyCode);
}
}else{
if(dojo.render.html.safari){
switch(evt.keyCode){
case 25:
evt.key=evt.KEY_TAB;
evt.shift=true;
break;
case 63232:
evt.key=evt.KEY_UP_ARROW;
break;
case 63233:
evt.key=evt.KEY_DOWN_ARROW;
break;
case 63234:
evt.key=evt.KEY_LEFT_ARROW;
break;
case 63235:
evt.key=evt.KEY_RIGHT_ARROW;
break;
case 63236:
evt.key=evt.KEY_F1;
break;
case 63237:
evt.key=evt.KEY_F2;
break;
case 63238:
evt.key=evt.KEY_F3;
break;
case 63239:
evt.key=evt.KEY_F4;
break;
case 63240:
evt.key=evt.KEY_F5;
break;
case 63241:
evt.key=evt.KEY_F6;
break;
case 63242:
evt.key=evt.KEY_F7;
break;
case 63243:
evt.key=evt.KEY_F8;
break;
case 63244:
evt.key=evt.KEY_F9;
break;
case 63245:
evt.key=evt.KEY_F10;
break;
case 63246:
evt.key=evt.KEY_F11;
break;
case 63247:
evt.key=evt.KEY_F12;
break;
case 63250:
evt.key=evt.KEY_PAUSE;
break;
case 63272:
evt.key=evt.KEY_DELETE;
break;
case 63273:
evt.key=evt.KEY_HOME;
break;
case 63275:
evt.key=evt.KEY_END;
break;
case 63276:
evt.key=evt.KEY_PAGE_UP;
break;
case 63277:
evt.key=evt.KEY_PAGE_DOWN;
break;
case 63302:
evt.key=evt.KEY_INSERT;
break;
case 63248:
case 63249:
case 63289:
break;
default:
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;
}
}else{
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;
}
}
}
}
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=(_2df?_2df:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _2e3=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_2e3.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_2e3.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this._stopPropagation;
evt.preventDefault=this._preventDefault;
}
return evt;
};
this.stopEvent=function(evt){
if(window.event){
evt.cancelBubble=true;
evt.returnValue=false;
}else{
evt.preventDefault();
evt.stopPropagation();
}
};
};
dojo.provide("dojo.event.*");
dojo.provide("dojo.widget.Manager");
dojo.widget.manager=new function(){
this.widgets=[];
this.widgetIds=[];
this.topWidgets={};
var _2e5={};
var _2e6=[];
this.getUniqueId=function(_2e7){
var _2e8;
do{
_2e8=_2e7+"_"+(_2e5[_2e7]!=undefined?++_2e5[_2e7]:_2e5[_2e7]=0);
}while(this.getWidgetById(_2e8));
return _2e8;
};
this.add=function(_2e9){
this.widgets.push(_2e9);
if(!_2e9.extraArgs["id"]){
_2e9.extraArgs["id"]=_2e9.extraArgs["ID"];
}
if(_2e9.widgetId==""){
if(_2e9["id"]){
_2e9.widgetId=_2e9["id"];
}else{
if(_2e9.extraArgs["id"]){
_2e9.widgetId=_2e9.extraArgs["id"];
}else{
_2e9.widgetId=this.getUniqueId(_2e9.ns+"_"+_2e9.widgetType);
}
}
}
if(this.widgetIds[_2e9.widgetId]){
dojo.debug("widget ID collision on ID: "+_2e9.widgetId);
}
this.widgetIds[_2e9.widgetId]=_2e9;
};
this.destroyAll=function(){
for(var x=this.widgets.length-1;x>=0;x--){
try{
this.widgets[x].destroy(true);
delete this.widgets[x];
}
catch(e){
}
}
};
this.remove=function(_2eb){
if(dojo.lang.isNumber(_2eb)){
var tw=this.widgets[_2eb].widgetId;
delete this.widgetIds[tw];
this.widgets.splice(_2eb,1);
}else{
this.removeById(_2eb);
}
};
this.removeById=function(id){
if(!dojo.lang.isString(id)){
id=id["widgetId"];
if(!id){
dojo.debug("invalid widget or id passed to removeById");
return;
}
}
for(var i=0;i<this.widgets.length;i++){
if(this.widgets[i].widgetId==id){
this.remove(i);
break;
}
}
};
this.getWidgetById=function(id){
if(dojo.lang.isString(id)){
return this.widgetIds[id];
}
return id;
};
this.getWidgetsByType=function(type){
var lt=type.toLowerCase();
var _2f2=(type.indexOf(":")<0?function(x){
return x.widgetType.toLowerCase();
}:function(x){
return x.getNamespacedType();
});
var ret=[];
dojo.lang.forEach(this.widgets,function(x){
if(_2f2(x)==lt){
ret.push(x);
}
});
return ret;
};
this.getWidgetsByFilter=function(_2f7,_2f8){
var ret=[];
dojo.lang.every(this.widgets,function(x){
if(_2f7(x)){
ret.push(x);
if(_2f8){
return false;
}
}
return true;
});
return (_2f8?ret[0]:ret);
};
this.getAllWidgets=function(){
return this.widgets.concat();
};
this.getWidgetByNode=function(node){
var w=this.getAllWidgets();
node=dojo.byId(node);
for(var i=0;i<w.length;i++){
if(w[i].domNode==node){
return w[i];
}
}
return null;
};
this.byId=this.getWidgetById;
this.byType=this.getWidgetsByType;
this.byFilter=this.getWidgetsByFilter;
this.byNode=this.getWidgetByNode;
var _2fe={};
var _2ff=["dojo.widget"];
for(var i=0;i<_2ff.length;i++){
_2ff[_2ff[i]]=true;
}
this.registerWidgetPackage=function(_301){
if(!_2ff[_301]){
_2ff[_301]=true;
_2ff.push(_301);
}
};
this.getWidgetPackageList=function(){
return dojo.lang.map(_2ff,function(elt){
return (elt!==true?elt:undefined);
});
};
this.getImplementation=function(_303,_304,_305,ns){
var impl=this.getImplementationName(_303,ns);
if(impl){
var ret=_304?new impl(_304):new impl();
return ret;
}
};
function buildPrefixCache(){
for(var _309 in dojo.render){
if(dojo.render[_309]["capable"]===true){
var _30a=dojo.render[_309].prefixes;
for(var i=0;i<_30a.length;i++){
_2e6.push(_30a[i].toLowerCase());
}
}
}
}
var _30c=function(_30d,_30e){
if(!_30e){
return null;
}
for(var i=0,l=_2e6.length,_311;i<=l;i++){
_311=(i<l?_30e[_2e6[i]]:_30e);
if(!_311){
continue;
}
for(var name in _311){
if(name.toLowerCase()==_30d){
return _311[name];
}
}
}
return null;
};
var _313=function(_314,_315){
var _316=dojo.evalObjPath(_315,false);
return (_316?_30c(_314,_316):null);
};
this.getImplementationName=function(_317,ns){
var _319=_317.toLowerCase();
ns=ns||"dojo";
var imps=_2fe[ns]||(_2fe[ns]={});
var impl=imps[_319];
if(impl){
return impl;
}
if(!_2e6.length){
buildPrefixCache();
}
var _31c=dojo.ns.get(ns);
if(!_31c){
dojo.ns.register(ns,ns+".widget");
_31c=dojo.ns.get(ns);
}
if(_31c){
_31c.resolve(_317);
}
impl=_313(_319,_31c.module);
if(impl){
return (imps[_319]=impl);
}
_31c=dojo.ns.require(ns);
if((_31c)&&(_31c.resolver)){
_31c.resolve(_317);
impl=_313(_319,_31c.module);
if(impl){
return (imps[_319]=impl);
}
}
dojo.deprecated("dojo.widget.Manager.getImplementationName","Could not locate widget implementation for \""+_317+"\" in \""+_31c.module+"\" registered to namespace \""+_31c.name+"\". "+"Developers must specify correct namespaces for all non-Dojo widgets","0.5");
for(var i=0;i<_2ff.length;i++){
impl=_313(_319,_2ff[i]);
if(impl){
return (imps[_319]=impl);
}
}
throw new Error("Could not locate widget implementation for \""+_317+"\" in \""+_31c.module+"\" registered to namespace \""+_31c.name+"\"");
};
this.resizing=false;
this.onWindowResized=function(){
if(this.resizing){
return;
}
try{
this.resizing=true;
for(var id in this.topWidgets){
var _31f=this.topWidgets[id];
if(_31f.checkSize){
_31f.checkSize();
}
}
}
catch(e){
}
finally{
this.resizing=false;
}
};
if(typeof window!="undefined"){
dojo.addOnLoad(this,"onWindowResized");
dojo.event.connect(window,"onresize",this,"onWindowResized");
}
};
(function(){
var dw=dojo.widget;
var dwm=dw.manager;
var h=dojo.lang.curry(dojo.lang,"hitch",dwm);
var g=function(_324,_325){
dw[(_325||_324)]=h(_324);
};
g("add","addWidget");
g("destroyAll","destroyAllWidgets");
g("remove","removeWidget");
g("removeById","removeWidgetById");
g("getWidgetById");
g("getWidgetById","byId");
g("getWidgetsByType");
g("getWidgetsByFilter");
g("getWidgetsByType","byType");
g("getWidgetsByFilter","byFilter");
g("getWidgetByNode","byNode");
dw.all=function(n){
var _327=dwm.getAllWidgets.apply(dwm,arguments);
if(arguments.length>0){
return _327[n];
}
return _327;
};
g("registerWidgetPackage");
g("getImplementation","getWidgetImplementation");
g("getImplementationName","getWidgetImplementationName");
dw.widgets=dwm.widgets;
dw.widgetIds=dwm.widgetIds;
dw.root=dwm.root;
})();
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.moduleUri=function(_329,uri){
var loc=dojo.hostenv.getModuleSymbols(_329).join("/");
if(!loc){
return null;
}
if(loc.lastIndexOf("/")!=loc.length-1){
loc+="/";
}
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri()+loc,uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _32e=new dojo.uri.Uri(arguments[i].toString());
var _32f=new dojo.uri.Uri(uri.toString());
if((_32e.path=="")&&(_32e.scheme==null)&&(_32e.authority==null)&&(_32e.query==null)){
if(_32e.fragment!=null){
_32f.fragment=_32e.fragment;
}
_32e=_32f;
}else{
if(_32e.scheme==null){
_32e.scheme=_32f.scheme;
if(_32e.authority==null){
_32e.authority=_32f.authority;
if(_32e.path.charAt(0)!="/"){
var path=_32f.path.substring(0,_32f.path.lastIndexOf("/")+1)+_32e.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_32e.path=segs.join("/");
}
}
}
}
uri="";
if(_32e.scheme!=null){
uri+=_32e.scheme+":";
}
if(_32e.authority!=null){
uri+="//"+_32e.authority;
}
uri+=_32e.path;
if(_32e.query!=null){
uri+="?"+_32e.query;
}
if(_32e.fragment!=null){
uri+="#"+_32e.fragment;
}
}
this.uri=uri.toString();
var _333="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_333));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_333="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_333));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.provide("dojo.uri.*");
dojo.provide("dojo.html.common");
dojo.lang.mixin(dojo.html,dojo.dom);
dojo.html.body=function(){
dojo.deprecated("dojo.html.body() moved to dojo.body()","0.5");
return dojo.body();
};
dojo.html.getEventTarget=function(evt){
if(!evt){
evt=dojo.global().event||{};
}
var t=(evt.srcElement?evt.srcElement:(evt.target?evt.target:null));
while((t)&&(t.nodeType!=1)){
t=t.parentNode;
}
return t;
};
dojo.html.getViewport=function(){
var _337=dojo.global();
var _338=dojo.doc();
var w=0;
var h=0;
if(dojo.render.html.mozilla){
w=_338.documentElement.clientWidth;
h=_337.innerHeight;
}else{
if(!dojo.render.html.opera&&_337.innerWidth){
w=_337.innerWidth;
h=_337.innerHeight;
}else{
if(!dojo.render.html.opera&&dojo.exists(_338,"documentElement.clientWidth")){
var w2=_338.documentElement.clientWidth;
if(!w||w2&&w2<w){
w=w2;
}
h=_338.documentElement.clientHeight;
}else{
if(dojo.body().clientWidth){
w=dojo.body().clientWidth;
h=dojo.body().clientHeight;
}
}
}
}
return {width:w,height:h};
};
dojo.html.getScroll=function(){
var _33c=dojo.global();
var _33d=dojo.doc();
var top=_33c.pageYOffset||_33d.documentElement.scrollTop||dojo.body().scrollTop||0;
var left=_33c.pageXOffset||_33d.documentElement.scrollLeft||dojo.body().scrollLeft||0;
return {top:top,left:left,offset:{x:left,y:top}};
};
dojo.html.getParentByType=function(node,type){
var _342=dojo.doc();
var _343=dojo.byId(node);
type=type.toLowerCase();
while((_343)&&(_343.nodeName.toLowerCase()!=type)){
if(_343==(_342["body"]||_342["documentElement"])){
return null;
}
_343=_343.parentNode;
}
return _343;
};
dojo.html.getAttribute=function(node,attr){
node=dojo.byId(node);
if((!node)||(!node.getAttribute)){
return null;
}
var ta=typeof attr=="string"?attr:new String(attr);
var v=node.getAttribute(ta.toUpperCase());
if((v)&&(typeof v=="string")&&(v!="")){
return v;
}
if(v&&v.value){
return v.value;
}
if((node.getAttributeNode)&&(node.getAttributeNode(ta))){
return (node.getAttributeNode(ta)).value;
}else{
if(node.getAttribute(ta)){
return node.getAttribute(ta);
}else{
if(node.getAttribute(ta.toLowerCase())){
return node.getAttribute(ta.toLowerCase());
}
}
}
return null;
};
dojo.html.hasAttribute=function(node,attr){
return dojo.html.getAttribute(dojo.byId(node),attr)?true:false;
};
dojo.html.getCursorPosition=function(e){
e=e||dojo.global().event;
var _34b={x:0,y:0};
if(e.pageX||e.pageY){
_34b.x=e.pageX;
_34b.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_34b.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_34b.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _34b;
};
dojo.html.isTag=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName.toLowerCase()==String(arguments[i]).toLowerCase()){
return String(arguments[i]).toLowerCase();
}
}
}
return "";
};
if(dojo.render.html.ie&&!dojo.render.html.ie70){
if(window.location.href.substr(0,6).toLowerCase()!="https:"){
(function(){
var _350=dojo.doc().createElement("script");
_350.src="javascript:'dojo.html.createExternalElement=function(doc, tag){ return doc.createElement(tag); }'";
dojo.doc().getElementsByTagName("head")[0].appendChild(_350);
})();
}
}else{
dojo.html.createExternalElement=function(doc,tag){
return doc.createElement(tag);
};
}
dojo.html._callDeprecated=function(_353,_354,args,_356,_357){
dojo.deprecated("dojo.html."+_353,"replaced by dojo.html."+_354+"("+(_356?"node, {"+_356+": "+_356+"}":"")+")"+(_357?"."+_357:""),"0.5");
var _358=[];
if(_356){
var _359={};
_359[_356]=args[1];
_358.push(args[0]);
_358.push(_359);
}else{
_358=args;
}
var ret=dojo.html[_354].apply(dojo.html,args);
if(_357){
return ret[_357];
}else{
return ret;
}
};
dojo.html.getViewportWidth=function(){
return dojo.html._callDeprecated("getViewportWidth","getViewport",arguments,null,"width");
};
dojo.html.getViewportHeight=function(){
return dojo.html._callDeprecated("getViewportHeight","getViewport",arguments,null,"height");
};
dojo.html.getViewportSize=function(){
return dojo.html._callDeprecated("getViewportSize","getViewport",arguments);
};
dojo.html.getScrollTop=function(){
return dojo.html._callDeprecated("getScrollTop","getScroll",arguments,null,"top");
};
dojo.html.getScrollLeft=function(){
return dojo.html._callDeprecated("getScrollLeft","getScroll",arguments,null,"left");
};
dojo.html.getScrollOffset=function(){
return dojo.html._callDeprecated("getScrollOffset","getScroll",arguments,null,"offset");
};
dojo.provide("dojo.a11y");
dojo.a11y={imgPath:dojo.uri.dojoUri("src/widget/templates/images"),doAccessibleCheck:true,accessible:null,checkAccessible:function(){
if(this.accessible===null){
this.accessible=false;
if(this.doAccessibleCheck==true){
this.accessible=this.testAccessible();
}
}
return this.accessible;
},testAccessible:function(){
this.accessible=false;
if(dojo.render.html.ie||dojo.render.html.mozilla){
var div=document.createElement("div");
div.style.backgroundImage="url(\""+this.imgPath+"/tab_close.gif\")";
dojo.body().appendChild(div);
var _35c=null;
if(window.getComputedStyle){
var _35d=getComputedStyle(div,"");
_35c=_35d.getPropertyValue("background-image");
}else{
_35c=div.currentStyle.backgroundImage;
}
var _35e=false;
if(_35c!=null&&(_35c=="none"||_35c=="url(invalid-url:)")){
this.accessible=true;
}
dojo.body().removeChild(div);
}
return this.accessible;
},setCheckAccessible:function(_35f){
this.doAccessibleCheck=_35f;
},setAccessibleMode:function(){
if(this.accessible===null){
if(this.checkAccessible()){
dojo.render.html.prefixes.unshift("a11y");
}
}
return this.accessible;
}};
dojo.provide("dojo.widget.Widget");
dojo.declare("dojo.widget.Widget",null,function(){
this.children=[];
this.extraArgs={};
},{parent:null,isTopLevel:false,disabled:false,isContainer:false,widgetId:"",widgetType:"Widget",ns:"dojo",getNamespacedType:function(){
return (this.ns?this.ns+":"+this.widgetType:this.widgetType).toLowerCase();
},toString:function(){
return "[Widget "+this.getNamespacedType()+", "+(this.widgetId||"NO ID")+"]";
},repr:function(){
return this.toString();
},enable:function(){
this.disabled=false;
},disable:function(){
this.disabled=true;
},onResized:function(){
this.notifyChildrenOfResize();
},notifyChildrenOfResize:function(){
for(var i=0;i<this.children.length;i++){
var _361=this.children[i];
if(_361.onResized){
_361.onResized();
}
}
},create:function(args,_363,_364,ns){
if(ns){
this.ns=ns;
}
this.satisfyPropertySets(args,_363,_364);
this.mixInProperties(args,_363,_364);
this.postMixInProperties(args,_363,_364);
dojo.widget.manager.add(this);
this.buildRendering(args,_363,_364);
this.initialize(args,_363,_364);
this.postInitialize(args,_363,_364);
this.postCreate(args,_363,_364);
return this;
},destroy:function(_366){
if(this.parent){
this.parent.removeChild(this);
}
this.destroyChildren();
this.uninitialize();
this.destroyRendering(_366);
dojo.widget.manager.removeById(this.widgetId);
},destroyChildren:function(){
var _367;
var i=0;
while(this.children.length>i){
_367=this.children[i];
if(_367 instanceof dojo.widget.Widget){
this.removeChild(_367);
_367.destroy();
continue;
}
i++;
}
},getChildrenOfType:function(type,_36a){
var ret=[];
var _36c=dojo.lang.isFunction(type);
if(!_36c){
type=type.toLowerCase();
}
for(var x=0;x<this.children.length;x++){
if(_36c){
if(this.children[x] instanceof type){
ret.push(this.children[x]);
}
}else{
if(this.children[x].widgetType.toLowerCase()==type){
ret.push(this.children[x]);
}
}
if(_36a){
ret=ret.concat(this.children[x].getChildrenOfType(type,_36a));
}
}
return ret;
},getDescendants:function(){
var _36e=[];
var _36f=[this];
var elem;
while((elem=_36f.pop())){
_36e.push(elem);
if(elem.children){
dojo.lang.forEach(elem.children,function(elem){
_36f.push(elem);
});
}
}
return _36e;
},isFirstChild:function(){
return this===this.parent.children[0];
},isLastChild:function(){
return this===this.parent.children[this.parent.children.length-1];
},satisfyPropertySets:function(args){
return args;
},mixInProperties:function(args,frag){
if((args["fastMixIn"])||(frag["fastMixIn"])){
for(var x in args){
this[x]=args[x];
}
return;
}
var _376;
var _377=dojo.widget.lcArgsCache[this.widgetType];
if(_377==null){
_377={};
for(var y in this){
_377[((new String(y)).toLowerCase())]=y;
}
dojo.widget.lcArgsCache[this.widgetType]=_377;
}
var _379={};
for(var x in args){
if(!this[x]){
var y=_377[(new String(x)).toLowerCase()];
if(y){
args[y]=args[x];
x=y;
}
}
if(_379[x]){
continue;
}
_379[x]=true;
if((typeof this[x])!=(typeof _376)){
if(typeof args[x]!="string"){
this[x]=args[x];
}else{
if(dojo.lang.isString(this[x])){
this[x]=args[x];
}else{
if(dojo.lang.isNumber(this[x])){
this[x]=new Number(args[x]);
}else{
if(dojo.lang.isBoolean(this[x])){
this[x]=(args[x].toLowerCase()=="false")?false:true;
}else{
if(dojo.lang.isFunction(this[x])){
if(args[x].search(/[^\w\.]+/i)==-1){
this[x]=dojo.evalObjPath(args[x],false);
}else{
var tn=dojo.lang.nameAnonFunc(new Function(args[x]),this);
dojo.event.kwConnect({srcObj:this,srcFunc:x,adviceObj:this,adviceFunc:tn});
}
}else{
if(dojo.lang.isArray(this[x])){
this[x]=args[x].split(";");
}else{
if(this[x] instanceof Date){
this[x]=new Date(Number(args[x]));
}else{
if(typeof this[x]=="object"){
if(this[x] instanceof dojo.uri.Uri){
this[x]=dojo.uri.dojoUri(args[x]);
}else{
var _37b=args[x].split(";");
for(var y=0;y<_37b.length;y++){
var si=_37b[y].indexOf(":");
if((si!=-1)&&(_37b[y].length>si)){
this[x][_37b[y].substr(0,si).replace(/^\s+|\s+$/g,"")]=_37b[y].substr(si+1);
}
}
}
}else{
this[x]=args[x];
}
}
}
}
}
}
}
}
}else{
this.extraArgs[x.toLowerCase()]=args[x];
}
}
},postMixInProperties:function(args,frag,_37f){
},initialize:function(args,frag,_382){
return false;
},postInitialize:function(args,frag,_385){
return false;
},postCreate:function(args,frag,_388){
return false;
},uninitialize:function(){
return false;
},buildRendering:function(args,frag,_38b){
dojo.unimplemented("dojo.widget.Widget.buildRendering, on "+this.toString()+", ");
return false;
},destroyRendering:function(){
dojo.unimplemented("dojo.widget.Widget.destroyRendering");
return false;
},addedTo:function(_38c){
},addChild:function(_38d){
dojo.unimplemented("dojo.widget.Widget.addChild");
return false;
},removeChild:function(_38e){
for(var x=0;x<this.children.length;x++){
if(this.children[x]===_38e){
this.children.splice(x,1);
_38e.parent=null;
break;
}
}
return _38e;
},getPreviousSibling:function(){
var idx=this.getParentIndex();
if(idx<=0){
return null;
}
return this.parent.children[idx-1];
},getSiblings:function(){
return this.parent.children;
},getParentIndex:function(){
return dojo.lang.indexOf(this.parent.children,this,true);
},getNextSibling:function(){
var idx=this.getParentIndex();
if(idx==this.parent.children.length-1){
return null;
}
if(idx<0){
return null;
}
return this.parent.children[idx+1];
}});
dojo.widget.lcArgsCache={};
dojo.widget.tags={};
dojo.widget.tags.addParseTreeHandler=function(type){
dojo.deprecated("addParseTreeHandler",". ParseTreeHandlers are now reserved for components. Any unfiltered DojoML tag without a ParseTreeHandler is assumed to be a widget","0.5");
};
dojo.widget.tags["dojo:propertyset"]=function(_393,_394,_395){
var _396=_394.parseProperties(_393["dojo:propertyset"]);
};
dojo.widget.tags["dojo:connect"]=function(_397,_398,_399){
var _39a=_398.parseProperties(_397["dojo:connect"]);
};
dojo.widget.buildWidgetFromParseTree=function(type,frag,_39d,_39e,_39f,_3a0){
dojo.a11y.setAccessibleMode();
var _3a1=type.split(":");
_3a1=(_3a1.length==2)?_3a1[1]:type;
var _3a2=_3a0||_39d.parseProperties(frag[frag["ns"]+":"+_3a1]);
var _3a3=dojo.widget.manager.getImplementation(_3a1,null,null,frag["ns"]);
if(!_3a3){
throw new Error("cannot find \""+type+"\" widget");
}else{
if(!_3a3.create){
throw new Error("\""+type+"\" widget object has no \"create\" method and does not appear to implement *Widget");
}
}
_3a2["dojoinsertionindex"]=_39f;
var ret=_3a3.create(_3a2,frag,_39e,frag["ns"]);
return ret;
};
dojo.widget.defineWidget=function(_3a5,_3a6,_3a7,init,_3a9){
if(dojo.lang.isString(arguments[3])){
dojo.widget._defineWidget(arguments[0],arguments[3],arguments[1],arguments[4],arguments[2]);
}else{
var args=[arguments[0]],p=3;
if(dojo.lang.isString(arguments[1])){
args.push(arguments[1],arguments[2]);
}else{
args.push("",arguments[1]);
p=2;
}
if(dojo.lang.isFunction(arguments[p])){
args.push(arguments[p],arguments[p+1]);
}else{
args.push(null,arguments[p]);
}
dojo.widget._defineWidget.apply(this,args);
}
};
dojo.widget.defineWidget.renderers="html|svg|vml";
dojo.widget._defineWidget=function(_3ac,_3ad,_3ae,init,_3b0){
var _3b1=_3ac.split(".");
var type=_3b1.pop();
var regx="\\.("+(_3ad?_3ad+"|":"")+dojo.widget.defineWidget.renderers+")\\.";
var r=_3ac.search(new RegExp(regx));
_3b1=(r<0?_3b1.join("."):_3ac.substr(0,r));
dojo.widget.manager.registerWidgetPackage(_3b1);
var pos=_3b1.indexOf(".");
var _3b6=(pos>-1)?_3b1.substring(0,pos):_3b1;
_3b0=(_3b0)||{};
_3b0.widgetType=type;
if((!init)&&(_3b0["classConstructor"])){
init=_3b0.classConstructor;
delete _3b0.classConstructor;
}
dojo.declare(_3ac,_3ae,init,_3b0);
};
dojo.provide("dojo.widget.Parse");
dojo.widget.Parse=function(_3b7){
this.propertySetsList=[];
this.fragment=_3b7;
this.createComponents=function(frag,_3b9){
var _3ba=[];
var _3bb=false;
try{
if(frag&&frag.tagName&&(frag!=frag.nodeRef)){
var _3bc=dojo.widget.tags;
var tna=String(frag.tagName).split(";");
for(var x=0;x<tna.length;x++){
var ltn=tna[x].replace(/^\s+|\s+$/g,"").toLowerCase();
frag.tagName=ltn;
var ret;
if(_3bc[ltn]){
_3bb=true;
ret=_3bc[ltn](frag,this,_3b9,frag.index);
_3ba.push(ret);
}else{
if(ltn.indexOf(":")==-1){
ltn="dojo:"+ltn;
}
ret=dojo.widget.buildWidgetFromParseTree(ltn,frag,this,_3b9,frag.index);
if(ret){
_3bb=true;
_3ba.push(ret);
}
}
}
}
}
catch(e){
dojo.debug("dojo.widget.Parse: error:",e);
}
if(!_3bb){
_3ba=_3ba.concat(this.createSubComponents(frag,_3b9));
}
return _3ba;
};
this.createSubComponents=function(_3c1,_3c2){
var frag,_3c4=[];
for(var item in _3c1){
frag=_3c1[item];
if(frag&&typeof frag=="object"&&(frag!=_3c1.nodeRef)&&(frag!=_3c1.tagName)&&(!dojo.dom.isNode(frag))){
_3c4=_3c4.concat(this.createComponents(frag,_3c2));
}
}
return _3c4;
};
this.parsePropertySets=function(_3c6){
return [];
};
this.parseProperties=function(_3c7){
var _3c8={};
for(var item in _3c7){
if((_3c7[item]==_3c7.tagName)||(_3c7[item]==_3c7.nodeRef)){
}else{
var frag=_3c7[item];
if(frag.tagName&&dojo.widget.tags[frag.tagName.toLowerCase()]){
}else{
if(frag[0]&&frag[0].value!=""&&frag[0].value!=null){
try{
if(item.toLowerCase()=="dataprovider"){
var _3cb=this;
this.getDataProvider(_3cb,frag[0].value);
_3c8.dataProvider=this.dataProvider;
}
_3c8[item]=frag[0].value;
var _3cc=this.parseProperties(frag);
for(var _3cd in _3cc){
_3c8[_3cd]=_3cc[_3cd];
}
}
catch(e){
dojo.debug(e);
}
}
}
switch(item.toLowerCase()){
case "checked":
case "disabled":
if(typeof _3c8[item]!="boolean"){
_3c8[item]=true;
}
break;
}
}
}
return _3c8;
};
this.getDataProvider=function(_3ce,_3cf){
dojo.io.bind({url:_3cf,load:function(type,_3d1){
if(type=="load"){
_3ce.dataProvider=_3d1;
}
},mimetype:"text/javascript",sync:true});
};
this.getPropertySetById=function(_3d2){
for(var x=0;x<this.propertySetsList.length;x++){
if(_3d2==this.propertySetsList[x]["id"][0].value){
return this.propertySetsList[x];
}
}
return "";
};
this.getPropertySetsByType=function(_3d4){
var _3d5=[];
for(var x=0;x<this.propertySetsList.length;x++){
var cpl=this.propertySetsList[x];
var cpcc=cpl.componentClass||cpl.componentType||null;
var _3d9=this.propertySetsList[x]["id"][0].value;
if(cpcc&&(_3d9==cpcc[0].value)){
_3d5.push(cpl);
}
}
return _3d5;
};
this.getPropertySets=function(_3da){
var ppl="dojo:propertyproviderlist";
var _3dc=[];
var _3dd=_3da.tagName;
if(_3da[ppl]){
var _3de=_3da[ppl].value.split(" ");
for(var _3df in _3de){
if((_3df.indexOf("..")==-1)&&(_3df.indexOf("://")==-1)){
var _3e0=this.getPropertySetById(_3df);
if(_3e0!=""){
_3dc.push(_3e0);
}
}else{
}
}
}
return this.getPropertySetsByType(_3dd).concat(_3dc);
};
this.createComponentFromScript=function(_3e1,_3e2,_3e3,ns){
_3e3.fastMixIn=true;
var ltn=(ns||"dojo")+":"+_3e2.toLowerCase();
if(dojo.widget.tags[ltn]){
return [dojo.widget.tags[ltn](_3e3,this,null,null,_3e3)];
}
return [dojo.widget.buildWidgetFromParseTree(ltn,_3e3,this,null,null,_3e3)];
};
};
dojo.widget._parser_collection={"dojo":new dojo.widget.Parse()};
dojo.widget.getParser=function(name){
if(!name){
name="dojo";
}
if(!this._parser_collection[name]){
this._parser_collection[name]=new dojo.widget.Parse();
}
return this._parser_collection[name];
};
dojo.widget.createWidget=function(name,_3e8,_3e9,_3ea){
var _3eb=false;
var _3ec=(typeof name=="string");
if(_3ec){
var pos=name.indexOf(":");
var ns=(pos>-1)?name.substring(0,pos):"dojo";
if(pos>-1){
name=name.substring(pos+1);
}
var _3ef=name.toLowerCase();
var _3f0=ns+":"+_3ef;
_3eb=(dojo.byId(name)&&!dojo.widget.tags[_3f0]);
}
if((arguments.length==1)&&(_3eb||!_3ec)){
var xp=new dojo.xml.Parse();
var tn=_3eb?dojo.byId(name):name;
return dojo.widget.getParser().createComponents(xp.parseElement(tn,null,true))[0];
}
function fromScript(_3f3,name,_3f5,ns){
_3f5[_3f0]={dojotype:[{value:_3ef}],nodeRef:_3f3,fastMixIn:true};
_3f5.ns=ns;
return dojo.widget.getParser().createComponentFromScript(_3f3,name,_3f5,ns);
}
_3e8=_3e8||{};
var _3f7=false;
var tn=null;
var h=dojo.render.html.capable;
if(h){
tn=document.createElement("span");
}
if(!_3e9){
_3f7=true;
_3e9=tn;
if(h){
dojo.body().appendChild(_3e9);
}
}else{
if(_3ea){
dojo.dom.insertAtPosition(tn,_3e9,_3ea);
}else{
tn=_3e9;
}
}
var _3f9=fromScript(tn,name.toLowerCase(),_3e8,ns);
if((!_3f9)||(!_3f9[0])||(typeof _3f9[0].widgetType=="undefined")){
throw new Error("createWidget: Creation of \""+name+"\" widget failed.");
}
try{
if(_3f7&&_3f9[0].domNode.parentNode){
_3f9[0].domNode.parentNode.removeChild(_3f9[0].domNode);
}
}
catch(e){
dojo.debug(e);
}
return _3f9[0];
};
dojo.provide("dojo.html.style");
dojo.html.getClass=function(node){
node=dojo.byId(node);
if(!node){
return "";
}
var cs="";
if(node.className){
cs=node.className;
}else{
if(dojo.html.hasAttribute(node,"class")){
cs=dojo.html.getAttribute(node,"class");
}
}
return cs.replace(/^\s+|\s+$/g,"");
};
dojo.html.getClasses=function(node){
var c=dojo.html.getClass(node);
return (c=="")?[]:c.split(/\s+/g);
};
dojo.html.hasClass=function(node,_3ff){
return (new RegExp("(^|\\s+)"+_3ff+"(\\s+|$)")).test(dojo.html.getClass(node));
};
dojo.html.prependClass=function(node,_401){
_401+=" "+dojo.html.getClass(node);
return dojo.html.setClass(node,_401);
};
dojo.html.addClass=function(node,_403){
if(dojo.html.hasClass(node,_403)){
return false;
}
_403=(dojo.html.getClass(node)+" "+_403).replace(/^\s+|\s+$/g,"");
return dojo.html.setClass(node,_403);
};
dojo.html.setClass=function(node,_405){
node=dojo.byId(node);
var cs=new String(_405);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_405);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
};
dojo.html.removeClass=function(node,_408,_409){
try{
if(!_409){
var _40a=dojo.html.getClass(node).replace(new RegExp("(^|\\s+)"+_408+"(\\s+|$)"),"$1$2");
}else{
var _40a=dojo.html.getClass(node).replace(_408,"");
}
dojo.html.setClass(node,_40a);
}
catch(e){
dojo.debug("dojo.html.removeClass() failed",e);
}
return true;
};
dojo.html.replaceClass=function(node,_40c,_40d){
dojo.html.removeClass(node,_40d);
dojo.html.addClass(node,_40c);
};
dojo.html.classMatchType={ContainsAll:0,ContainsAny:1,IsOnly:2};
dojo.html.getElementsByClass=function(_40e,_40f,_410,_411,_412){
_412=false;
var _413=dojo.doc();
_40f=dojo.byId(_40f)||_413;
var _414=_40e.split(/\s+/g);
var _415=[];
if(_411!=1&&_411!=2){
_411=0;
}
var _416=new RegExp("(\\s|^)(("+_414.join(")|(")+"))(\\s|$)");
var _417=_414.join(" ").length;
var _418=[];
if(!_412&&_413.evaluate){
var _419=".//"+(_410||"*")+"[contains(";
if(_411!=dojo.html.classMatchType.ContainsAny){
_419+="concat(' ',@class,' '), ' "+_414.join(" ') and contains(concat(' ',@class,' '), ' ")+" ')";
if(_411==2){
_419+=" and string-length(@class)="+_417+"]";
}else{
_419+="]";
}
}else{
_419+="concat(' ',@class,' '), ' "+_414.join(" ') or contains(concat(' ',@class,' '), ' ")+" ')]";
}
var _41a=_413.evaluate(_419,_40f,null,XPathResult.ANY_TYPE,null);
var _41b=_41a.iterateNext();
while(_41b){
try{
_418.push(_41b);
_41b=_41a.iterateNext();
}
catch(e){
break;
}
}
return _418;
}else{
if(!_410){
_410="*";
}
_418=_40f.getElementsByTagName(_410);
var node,i=0;
outer:
while(node=_418[i++]){
var _41e=dojo.html.getClasses(node);
if(_41e.length==0){
continue outer;
}
var _41f=0;
for(var j=0;j<_41e.length;j++){
if(_416.test(_41e[j])){
if(_411==dojo.html.classMatchType.ContainsAny){
_415.push(node);
continue outer;
}else{
_41f++;
}
}else{
if(_411==dojo.html.classMatchType.IsOnly){
continue outer;
}
}
}
if(_41f==_414.length){
if((_411==dojo.html.classMatchType.IsOnly)&&(_41f==_41e.length)){
_415.push(node);
}else{
if(_411==dojo.html.classMatchType.ContainsAll){
_415.push(node);
}
}
}
}
return _415;
}
};
dojo.html.getElementsByClassName=dojo.html.getElementsByClass;
dojo.html.toCamelCase=function(_421){
var arr=_421.split("-"),cc=arr[0];
for(var i=1;i<arr.length;i++){
cc+=arr[i].charAt(0).toUpperCase()+arr[i].substring(1);
}
return cc;
};
dojo.html.toSelectorCase=function(_425){
return _425.replace(/([A-Z])/g,"-$1").toLowerCase();
};
dojo.html.getComputedStyle=function(node,_427,_428){
node=dojo.byId(node);
var _427=dojo.html.toSelectorCase(_427);
var _429=dojo.html.toCamelCase(_427);
if(!node||!node.style){
return _428;
}else{
if(document.defaultView&&dojo.html.isDescendantOf(node,node.ownerDocument)){
try{
var cs=document.defaultView.getComputedStyle(node,"");
if(cs){
return cs.getPropertyValue(_427);
}
}
catch(e){
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_427);
}else{
return _428;
}
}
}else{
if(node.currentStyle){
return node.currentStyle[_429];
}
}
}
if(node.style.getPropertyValue){
return node.style.getPropertyValue(_427);
}else{
return _428;
}
};
dojo.html.getStyleProperty=function(node,_42c){
node=dojo.byId(node);
return (node&&node.style?node.style[dojo.html.toCamelCase(_42c)]:undefined);
};
dojo.html.getStyle=function(node,_42e){
var _42f=dojo.html.getStyleProperty(node,_42e);
return (_42f?_42f:dojo.html.getComputedStyle(node,_42e));
};
dojo.html.setStyle=function(node,_431,_432){
node=dojo.byId(node);
if(node&&node.style){
var _433=dojo.html.toCamelCase(_431);
node.style[_433]=_432;
}
};
dojo.html.setStyleText=function(_434,text){
try{
_434.style.cssText=text;
}
catch(e){
_434.setAttribute("style",text);
}
};
dojo.html.copyStyle=function(_436,_437){
if(!_437.style.cssText){
_436.setAttribute("style",_437.getAttribute("style"));
}else{
_436.style.cssText=_437.style.cssText;
}
dojo.html.addClass(_436,dojo.html.getClass(_437));
};
dojo.html.getUnitValue=function(node,_439,_43a){
var s=dojo.html.getComputedStyle(node,_439);
if((!s)||((s=="auto")&&(_43a))){
return {value:0,units:"px"};
}
var _43c=s.match(/(\-?[\d.]+)([a-z%]*)/i);
if(!_43c){
return dojo.html.getUnitValue.bad;
}
return {value:Number(_43c[1]),units:_43c[2].toLowerCase()};
};
dojo.html.getUnitValue.bad={value:NaN,units:""};
dojo.html.getPixelValue=function(node,_43e,_43f){
var _440=dojo.html.getUnitValue(node,_43e,_43f);
if(isNaN(_440.value)){
return 0;
}
if((_440.value)&&(_440.units!="px")){
return NaN;
}
return _440.value;
};
dojo.html.setPositivePixelValue=function(node,_442,_443){
if(isNaN(_443)){
return false;
}
node.style[_442]=Math.max(0,_443)+"px";
return true;
};
dojo.html.styleSheet=null;
dojo.html.insertCssRule=function(_444,_445,_446){
if(!dojo.html.styleSheet){
if(document.createStyleSheet){
dojo.html.styleSheet=document.createStyleSheet();
}else{
if(document.styleSheets[0]){
dojo.html.styleSheet=document.styleSheets[0];
}else{
return null;
}
}
}
if(arguments.length<3){
if(dojo.html.styleSheet.cssRules){
_446=dojo.html.styleSheet.cssRules.length;
}else{
if(dojo.html.styleSheet.rules){
_446=dojo.html.styleSheet.rules.length;
}else{
return null;
}
}
}
if(dojo.html.styleSheet.insertRule){
var rule=_444+" { "+_445+" }";
return dojo.html.styleSheet.insertRule(rule,_446);
}else{
if(dojo.html.styleSheet.addRule){
return dojo.html.styleSheet.addRule(_444,_445,_446);
}else{
return null;
}
}
};
dojo.html.removeCssRule=function(_448){
if(!dojo.html.styleSheet){
dojo.debug("no stylesheet defined for removing rules");
return false;
}
if(dojo.render.html.ie){
if(!_448){
_448=dojo.html.styleSheet.rules.length;
dojo.html.styleSheet.removeRule(_448);
}
}else{
if(document.styleSheets[0]){
if(!_448){
_448=dojo.html.styleSheet.cssRules.length;
}
dojo.html.styleSheet.deleteRule(_448);
}
}
return true;
};
dojo.html._insertedCssFiles=[];
dojo.html.insertCssFile=function(URI,doc,_44b,_44c){
if(!URI){
return;
}
if(!doc){
doc=document;
}
var _44d=dojo.hostenv.getText(URI,false,_44c);
if(_44d===null){
return;
}
_44d=dojo.html.fixPathsInCssText(_44d,URI);
if(_44b){
var idx=-1,node,ent=dojo.html._insertedCssFiles;
for(var i=0;i<ent.length;i++){
if((ent[i].doc==doc)&&(ent[i].cssText==_44d)){
idx=i;
node=ent[i].nodeRef;
break;
}
}
if(node){
var _452=doc.getElementsByTagName("style");
for(var i=0;i<_452.length;i++){
if(_452[i]==node){
return;
}
}
dojo.html._insertedCssFiles.shift(idx,1);
}
}
var _453=dojo.html.insertCssText(_44d,doc);
dojo.html._insertedCssFiles.push({"doc":doc,"cssText":_44d,"nodeRef":_453});
if(_453&&djConfig.isDebug){
_453.setAttribute("dbgHref",URI);
}
return _453;
};
dojo.html.insertCssText=function(_454,doc,URI){
if(!_454){
return;
}
if(!doc){
doc=document;
}
if(URI){
_454=dojo.html.fixPathsInCssText(_454,URI);
}
var _457=doc.createElement("style");
_457.setAttribute("type","text/css");
var head=doc.getElementsByTagName("head")[0];
if(!head){
dojo.debug("No head tag in document, aborting styles");
return;
}else{
head.appendChild(_457);
}
if(_457.styleSheet){
var _459=function(){
try{
_457.styleSheet.cssText=_454;
}
catch(e){
dojo.debug(e);
}
};
if(_457.styleSheet.disabled){
setTimeout(_459,10);
}else{
_459();
}
}else{
var _45a=doc.createTextNode(_454);
_457.appendChild(_45a);
}
return _457;
};
dojo.html.fixPathsInCssText=function(_45b,URI){
if(!_45b||!URI){
return;
}
var _45d,str="",url="",_460="[\\t\\s\\w\\(\\)\\/\\.\\\\'\"-:#=&?~]+";
var _461=new RegExp("url\\(\\s*("+_460+")\\s*\\)");
var _462=/(file|https?|ftps?):\/\//;
regexTrim=new RegExp("^[\\s]*(['\"]?)("+_460+")\\1[\\s]*?$");
if(dojo.render.html.ie55||dojo.render.html.ie60){
var _463=new RegExp("AlphaImageLoader\\((.*)src=['\"]("+_460+")['\"]");
while(_45d=_463.exec(_45b)){
url=_45d[2].replace(regexTrim,"$2");
if(!_462.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_45b.substring(0,_45d.index)+"AlphaImageLoader("+_45d[1]+"src='"+url+"'";
_45b=_45b.substr(_45d.index+_45d[0].length);
}
_45b=str+_45b;
str="";
}
while(_45d=_461.exec(_45b)){
url=_45d[1].replace(regexTrim,"$2");
if(!_462.exec(url)){
url=(new dojo.uri.Uri(URI,url).toString());
}
str+=_45b.substring(0,_45d.index)+"url("+url+")";
_45b=_45b.substr(_45d.index+_45d[0].length);
}
return str+_45b;
};
dojo.html.setActiveStyleSheet=function(_464){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")){
a.disabled=true;
if(a.getAttribute("title")==_464){
a.disabled=false;
}
}
}
};
dojo.html.getActiveStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("title")&&!a.disabled){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.getPreferredStyleSheet=function(){
var i=0,a,els=dojo.doc().getElementsByTagName("link");
while(a=els[i++]){
if(a.getAttribute("rel").indexOf("style")!=-1&&a.getAttribute("rel").indexOf("alt")==-1&&a.getAttribute("title")){
return a.getAttribute("title");
}
}
return null;
};
dojo.html.applyBrowserClass=function(node){
var drh=dojo.render.html;
var _470={dj_ie:drh.ie,dj_ie55:drh.ie55,dj_ie6:drh.ie60,dj_ie7:drh.ie70,dj_iequirks:drh.ie&&drh.quirks,dj_opera:drh.opera,dj_opera8:drh.opera&&(Math.floor(dojo.render.version)==8),dj_opera9:drh.opera&&(Math.floor(dojo.render.version)==9),dj_khtml:drh.khtml,dj_safari:drh.safari,dj_gecko:drh.mozilla};
for(var p in _470){
if(_470[p]){
dojo.html.addClass(node,p);
}
}
};
dojo.provide("dojo.widget.DomWidget");
dojo.widget._cssFiles={};
dojo.widget._cssStrings={};
dojo.widget._templateCache={};
dojo.widget.defaultStrings={dojoRoot:dojo.hostenv.getBaseScriptUri(),baseScriptUri:dojo.hostenv.getBaseScriptUri()};
dojo.widget.fillFromTemplateCache=function(obj,_473,_474,_475){
var _476=_473||obj.templatePath;
var _477=dojo.widget._templateCache;
if(!_476&&!obj["widgetType"]){
do{
var _478="__dummyTemplate__"+dojo.widget._templateCache.dummyCount++;
}while(_477[_478]);
obj.widgetType=_478;
}
var wt=_476?_476.toString():obj.widgetType;
var ts=_477[wt];
if(!ts){
_477[wt]={"string":null,"node":null};
if(_475){
ts={};
}else{
ts=_477[wt];
}
}
if((!obj.templateString)&&(!_475)){
obj.templateString=_474||ts["string"];
}
if((!obj.templateNode)&&(!_475)){
obj.templateNode=ts["node"];
}
if((!obj.templateNode)&&(!obj.templateString)&&(_476)){
var _47b=dojo.hostenv.getText(_476);
if(_47b){
_47b=_47b.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _47c=_47b.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_47c){
_47b=_47c[1];
}
}else{
_47b="";
}
obj.templateString=_47b;
if(!_475){
_477[wt]["string"]=_47b;
}
}
if((!ts["string"])&&(!_475)){
ts.string=obj.templateString;
}
};
dojo.widget._templateCache.dummyCount=0;
dojo.widget.attachProperties=["dojoAttachPoint","id"];
dojo.widget.eventAttachProperty="dojoAttachEvent";
dojo.widget.onBuildProperty="dojoOnBuild";
dojo.widget.waiNames=["waiRole","waiState"];
dojo.widget.wai={waiRole:{name:"waiRole","namespace":"http://www.w3.org/TR/xhtml2",alias:"x2",prefix:"wairole:"},waiState:{name:"waiState","namespace":"http://www.w3.org/2005/07/aaa",alias:"aaa",prefix:""},setAttr:function(node,ns,attr,_480){
if(dojo.render.html.ie){
node.setAttribute(this[ns].alias+":"+attr,this[ns].prefix+_480);
}else{
node.setAttributeNS(this[ns]["namespace"],attr,this[ns].prefix+_480);
}
},getAttr:function(node,ns,attr){
if(dojo.render.html.ie){
return node.getAttribute(this[ns].alias+":"+attr);
}else{
return node.getAttributeNS(this[ns]["namespace"],attr);
}
},removeAttr:function(node,ns,attr){
var _487=true;
if(dojo.render.html.ie){
_487=node.removeAttribute(this[ns].alias+":"+attr);
}else{
node.removeAttributeNS(this[ns]["namespace"],attr);
}
return _487;
}};
dojo.widget.attachTemplateNodes=function(_488,_489,_48a){
var _48b=dojo.dom.ELEMENT_NODE;
function trim(str){
return str.replace(/^\s+|\s+$/g,"");
}
if(!_488){
_488=_489.domNode;
}
if(_488.nodeType!=_48b){
return;
}
var _48d=_488.all||_488.getElementsByTagName("*");
var _48e=_489;
for(var x=-1;x<_48d.length;x++){
var _490=(x==-1)?_488:_48d[x];
var _491=[];
if(!_489.widgetsInTemplate||!_490.getAttribute("dojoType")){
for(var y=0;y<this.attachProperties.length;y++){
var _493=_490.getAttribute(this.attachProperties[y]);
if(_493){
_491=_493.split(";");
for(var z=0;z<_491.length;z++){
if(dojo.lang.isArray(_489[_491[z]])){
_489[_491[z]].push(_490);
}else{
_489[_491[z]]=_490;
}
}
break;
}
}
var _495=_490.getAttribute(this.eventAttachProperty);
if(_495){
var evts=_495.split(";");
for(var y=0;y<evts.length;y++){
if((!evts[y])||(!evts[y].length)){
continue;
}
var _497=null;
var tevt=trim(evts[y]);
if(evts[y].indexOf(":")>=0){
var _499=tevt.split(":");
tevt=trim(_499[0]);
_497=trim(_499[1]);
}
if(!_497){
_497=tevt;
}
var tf=function(){
var ntf=new String(_497);
return function(evt){
if(_48e[ntf]){
_48e[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_490,tevt,tf,false,true);
}
}
for(var y=0;y<_48a.length;y++){
var _49d=_490.getAttribute(_48a[y]);
if((_49d)&&(_49d.length)){
var _497=null;
var _49e=_48a[y].substr(4);
_497=trim(_49d);
var _49f=[_497];
if(_497.indexOf(";")>=0){
_49f=dojo.lang.map(_497.split(";"),trim);
}
for(var z=0;z<_49f.length;z++){
if(!_49f[z].length){
continue;
}
var tf=function(){
var ntf=new String(_49f[z]);
return function(evt){
if(_48e[ntf]){
_48e[ntf](dojo.event.browser.fixEvent(evt,this));
}
};
}();
dojo.event.browser.addListener(_490,_49e,tf,false,true);
}
}
}
}
var _4a2=_490.getAttribute(this.templateProperty);
if(_4a2){
_489[_4a2]=_490;
}
dojo.lang.forEach(dojo.widget.waiNames,function(name){
var wai=dojo.widget.wai[name];
var val=_490.getAttribute(wai.name);
if(val){
if(val.indexOf("-")==-1){
dojo.widget.wai.setAttr(_490,wai.name,"role",val);
}else{
var _4a6=val.split("-");
dojo.widget.wai.setAttr(_490,wai.name,_4a6[0],_4a6[1]);
}
}
},this);
var _4a7=_490.getAttribute(this.onBuildProperty);
if(_4a7){
eval("var node = baseNode; var widget = targetObj; "+_4a7);
}
}
};
dojo.widget.getDojoEventsFromStr=function(str){
var re=/(dojoOn([a-z]+)(\s?))=/gi;
var evts=str?str.match(re)||[]:[];
var ret=[];
var lem={};
for(var x=0;x<evts.length;x++){
if(evts[x].length<1){
continue;
}
var cm=evts[x].replace(/\s/,"");
cm=(cm.slice(0,cm.length-1));
if(!lem[cm]){
lem[cm]=true;
ret.push(cm);
}
}
return ret;
};
dojo.declare("dojo.widget.DomWidget",dojo.widget.Widget,function(){
if((arguments.length>0)&&(typeof arguments[0]=="object")){
this.create(arguments[0]);
}
},{templateNode:null,templateString:null,templateCssString:null,preventClobber:false,domNode:null,containerNode:null,widgetsInTemplate:false,addChild:function(_4af,_4b0,pos,ref,_4b3){
if(!this.isContainer){
dojo.debug("dojo.widget.DomWidget.addChild() attempted on non-container widget");
return null;
}else{
if(_4b3==undefined){
_4b3=this.children.length;
}
this.addWidgetAsDirectChild(_4af,_4b0,pos,ref,_4b3);
this.registerChild(_4af,_4b3);
}
return _4af;
},addWidgetAsDirectChild:function(_4b4,_4b5,pos,ref,_4b8){
if((!this.containerNode)&&(!_4b5)){
this.containerNode=this.domNode;
}
var cn=(_4b5)?_4b5:this.containerNode;
if(!pos){
pos="after";
}
if(!ref){
if(!cn){
cn=dojo.body();
}
ref=cn.lastChild;
}
if(!_4b8){
_4b8=0;
}
_4b4.domNode.setAttribute("dojoinsertionindex",_4b8);
if(!ref){
cn.appendChild(_4b4.domNode);
}else{
if(pos=="insertAtIndex"){
dojo.dom.insertAtIndex(_4b4.domNode,ref.parentNode,_4b8);
}else{
if((pos=="after")&&(ref===cn.lastChild)){
cn.appendChild(_4b4.domNode);
}else{
dojo.dom.insertAtPosition(_4b4.domNode,cn,pos);
}
}
}
},registerChild:function(_4ba,_4bb){
_4ba.dojoInsertionIndex=_4bb;
var idx=-1;
for(var i=0;i<this.children.length;i++){
if(this.children[i].dojoInsertionIndex<=_4bb){
idx=i;
}
}
this.children.splice(idx+1,0,_4ba);
_4ba.parent=this;
_4ba.addedTo(this,idx+1);
delete dojo.widget.manager.topWidgets[_4ba.widgetId];
},removeChild:function(_4be){
dojo.dom.removeNode(_4be.domNode);
return dojo.widget.DomWidget.superclass.removeChild.call(this,_4be);
},getFragNodeRef:function(frag){
if(!frag){
return null;
}
if(!frag[this.getNamespacedType()]){
dojo.raise("Error: no frag for widget type "+this.getNamespacedType()+", id "+this.widgetId+" (maybe a widget has set it's type incorrectly)");
}
return frag[this.getNamespacedType()]["nodeRef"];
},postInitialize:function(args,frag,_4c2){
var _4c3=this.getFragNodeRef(frag);
if(_4c2&&(_4c2.snarfChildDomOutput||!_4c3)){
_4c2.addWidgetAsDirectChild(this,"","insertAtIndex","",args["dojoinsertionindex"],_4c3);
}else{
if(_4c3){
if(this.domNode&&(this.domNode!==_4c3)){
this._sourceNodeRef=dojo.dom.replaceNode(_4c3,this.domNode);
}
}
}
if(_4c2){
_4c2.registerChild(this,args.dojoinsertionindex);
}else{
dojo.widget.manager.topWidgets[this.widgetId]=this;
}
if(this.widgetsInTemplate){
var _4c4=new dojo.xml.Parse();
var _4c5;
var _4c6=this.domNode.getElementsByTagName("*");
for(var i=0;i<_4c6.length;i++){
if(_4c6[i].getAttribute("dojoAttachPoint")=="subContainerWidget"){
_4c5=_4c6[i];
}
if(_4c6[i].getAttribute("dojoType")){
_4c6[i].setAttribute("isSubWidget",true);
}
}
if(this.isContainer&&!this.containerNode){
if(_4c5){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,_4c5);
frag["dojoDontFollow"]=true;
}
}else{
dojo.debug("No subContainerWidget node can be found in template file for widget "+this);
}
}
var _4c9=_4c4.parseElement(this.domNode,null,true);
dojo.widget.getParser().createSubComponents(_4c9,this);
var _4ca=[];
var _4cb=[this];
var w;
while((w=_4cb.pop())){
for(var i=0;i<w.children.length;i++){
var _4cd=w.children[i];
if(_4cd._processedSubWidgets||!_4cd.extraArgs["issubwidget"]){
continue;
}
_4ca.push(_4cd);
if(_4cd.isContainer){
_4cb.push(_4cd);
}
}
}
for(var i=0;i<_4ca.length;i++){
var _4ce=_4ca[i];
if(_4ce._processedSubWidgets){
dojo.debug("This should not happen: widget._processedSubWidgets is already true!");
return;
}
_4ce._processedSubWidgets=true;
if(_4ce.extraArgs["dojoattachevent"]){
var evts=_4ce.extraArgs["dojoattachevent"].split(";");
for(var j=0;j<evts.length;j++){
var _4d1=null;
var tevt=dojo.string.trim(evts[j]);
if(tevt.indexOf(":")>=0){
var _4d3=tevt.split(":");
tevt=dojo.string.trim(_4d3[0]);
_4d1=dojo.string.trim(_4d3[1]);
}
if(!_4d1){
_4d1=tevt;
}
if(dojo.lang.isFunction(_4ce[tevt])){
dojo.event.kwConnect({srcObj:_4ce,srcFunc:tevt,targetObj:this,targetFunc:_4d1});
}else{
alert(tevt+" is not a function in widget "+_4ce);
}
}
}
if(_4ce.extraArgs["dojoattachpoint"]){
this[_4ce.extraArgs["dojoattachpoint"]]=_4ce;
}
}
}
if(this.isContainer&&!frag["dojoDontFollow"]){
dojo.widget.getParser().createSubComponents(frag,this);
}
},buildRendering:function(args,frag){
var ts=dojo.widget._templateCache[this.widgetType];
if(args["templatecsspath"]){
args["templateCssPath"]=args["templatecsspath"];
}
var _4d7=args["templateCssPath"]||this.templateCssPath;
if(_4d7&&!dojo.widget._cssFiles[_4d7.toString()]){
if((!this.templateCssString)&&(_4d7)){
this.templateCssString=dojo.hostenv.getText(_4d7);
this.templateCssPath=null;
}
dojo.widget._cssFiles[_4d7.toString()]=true;
}
if((this["templateCssString"])&&(!dojo.widget._cssStrings[this.templateCssString])){
dojo.html.insertCssText(this.templateCssString,null,_4d7);
dojo.widget._cssStrings[this.templateCssString]=true;
}
if((!this.preventClobber)&&((this.templatePath)||(this.templateNode)||((this["templateString"])&&(this.templateString.length))||((typeof ts!="undefined")&&((ts["string"])||(ts["node"]))))){
this.buildFromTemplate(args,frag);
}else{
this.domNode=this.getFragNodeRef(frag);
}
this.fillInTemplate(args,frag);
},buildFromTemplate:function(args,frag){
var _4da=false;
if(args["templatepath"]){
args["templatePath"]=args["templatepath"];
}
dojo.widget.fillFromTemplateCache(this,args["templatePath"],null,_4da);
var ts=dojo.widget._templateCache[this.templatePath?this.templatePath.toString():this.widgetType];
if((ts)&&(!_4da)){
if(!this.templateString.length){
this.templateString=ts["string"];
}
if(!this.templateNode){
this.templateNode=ts["node"];
}
}
var _4dc=false;
var node=null;
var tstr=this.templateString;
if((!this.templateNode)&&(this.templateString)){
_4dc=this.templateString.match(/\$\{([^\}]+)\}/g);
if(_4dc){
var hash=this.strings||{};
for(var key in dojo.widget.defaultStrings){
if(dojo.lang.isUndefined(hash[key])){
hash[key]=dojo.widget.defaultStrings[key];
}
}
for(var i=0;i<_4dc.length;i++){
var key=_4dc[i];
key=key.substring(2,key.length-1);
var kval=(key.substring(0,5)=="this.")?dojo.lang.getObjPathValue(key.substring(5),this):hash[key];
var _4e3;
if((kval)||(dojo.lang.isString(kval))){
_4e3=new String((dojo.lang.isFunction(kval))?kval.call(this,key,this.templateString):kval);
while(_4e3.indexOf("\"")>-1){
_4e3=_4e3.replace("\"","&quot;");
}
tstr=tstr.replace(_4dc[i],_4e3);
}
}
}else{
this.templateNode=this.createNodesFromText(this.templateString,true)[0];
if(!_4da){
ts.node=this.templateNode;
}
}
}
if((!this.templateNode)&&(!_4dc)){
dojo.debug("DomWidget.buildFromTemplate: could not create template");
return false;
}else{
if(!_4dc){
node=this.templateNode.cloneNode(true);
if(!node){
return false;
}
}else{
node=this.createNodesFromText(tstr,true)[0];
}
}
this.domNode=node;
this.attachTemplateNodes();
if(this.isContainer&&this.containerNode){
var src=this.getFragNodeRef(frag);
if(src){
dojo.dom.moveChildren(src,this.containerNode);
}
}
},attachTemplateNodes:function(_4e5,_4e6){
if(!_4e5){
_4e5=this.domNode;
}
if(!_4e6){
_4e6=this;
}
return dojo.widget.attachTemplateNodes(_4e5,_4e6,dojo.widget.getDojoEventsFromStr(this.templateString));
},fillInTemplate:function(){
},destroyRendering:function(){
try{
dojo.dom.destroyNode(this.domNode);
delete this.domNode;
}
catch(e){
}
if(this._sourceNodeRef){
try{
dojo.dom.destroyNode(this._sourceNodeRef);
}
catch(e){
}
}
},createNodesFromText:function(){
dojo.unimplemented("dojo.widget.DomWidget.createNodesFromText");
}});
dojo.provide("dojo.html.display");
dojo.html._toggle=function(node,_4e8,_4e9){
node=dojo.byId(node);
_4e9(node,!_4e8(node));
return _4e8(node);
};
dojo.html.show=function(node){
node=dojo.byId(node);
if(dojo.html.getStyleProperty(node,"display")=="none"){
dojo.html.setStyle(node,"display",(node.dojoDisplayCache||""));
node.dojoDisplayCache=undefined;
}
};
dojo.html.hide=function(node){
node=dojo.byId(node);
if(typeof node["dojoDisplayCache"]=="undefined"){
var d=dojo.html.getStyleProperty(node,"display");
if(d!="none"){
node.dojoDisplayCache=d;
}
}
dojo.html.setStyle(node,"display","none");
};
dojo.html.setShowing=function(node,_4ee){
dojo.html[(_4ee?"show":"hide")](node);
};
dojo.html.isShowing=function(node){
return (dojo.html.getStyleProperty(node,"display")!="none");
};
dojo.html.toggleShowing=function(node){
return dojo.html._toggle(node,dojo.html.isShowing,dojo.html.setShowing);
};
dojo.html.displayMap={tr:"",td:"",th:"",img:"inline",span:"inline",input:"inline",button:"inline"};
dojo.html.suggestDisplayByTagName=function(node){
node=dojo.byId(node);
if(node&&node.tagName){
var tag=node.tagName.toLowerCase();
return (tag in dojo.html.displayMap?dojo.html.displayMap[tag]:"block");
}
};
dojo.html.setDisplay=function(node,_4f4){
dojo.html.setStyle(node,"display",((_4f4 instanceof String||typeof _4f4=="string")?_4f4:(_4f4?dojo.html.suggestDisplayByTagName(node):"none")));
};
dojo.html.isDisplayed=function(node){
return (dojo.html.getComputedStyle(node,"display")!="none");
};
dojo.html.toggleDisplay=function(node){
return dojo.html._toggle(node,dojo.html.isDisplayed,dojo.html.setDisplay);
};
dojo.html.setVisibility=function(node,_4f8){
dojo.html.setStyle(node,"visibility",((_4f8 instanceof String||typeof _4f8=="string")?_4f8:(_4f8?"visible":"hidden")));
};
dojo.html.isVisible=function(node){
return (dojo.html.getComputedStyle(node,"visibility")!="hidden");
};
dojo.html.toggleVisibility=function(node){
return dojo.html._toggle(node,dojo.html.isVisible,dojo.html.setVisibility);
};
dojo.html.setOpacity=function(node,_4fc,_4fd){
node=dojo.byId(node);
var h=dojo.render.html;
if(!_4fd){
if(_4fc>=1){
if(h.ie){
dojo.html.clearOpacity(node);
return;
}else{
_4fc=0.999999;
}
}else{
if(_4fc<0){
_4fc=0;
}
}
}
if(h.ie){
if(node.nodeName.toLowerCase()=="tr"){
var tds=node.getElementsByTagName("td");
for(var x=0;x<tds.length;x++){
tds[x].style.filter="Alpha(Opacity="+_4fc*100+")";
}
}
node.style.filter="Alpha(Opacity="+_4fc*100+")";
}else{
if(h.moz){
node.style.opacity=_4fc;
node.style.MozOpacity=_4fc;
}else{
if(h.safari){
node.style.opacity=_4fc;
node.style.KhtmlOpacity=_4fc;
}else{
node.style.opacity=_4fc;
}
}
}
};
dojo.html.clearOpacity=function(node){
node=dojo.byId(node);
var ns=node.style;
var h=dojo.render.html;
if(h.ie){
try{
if(node.filters&&node.filters.alpha){
ns.filter="";
}
}
catch(e){
}
}else{
if(h.moz){
ns.opacity=1;
ns.MozOpacity=1;
}else{
if(h.safari){
ns.opacity=1;
ns.KhtmlOpacity=1;
}else{
ns.opacity=1;
}
}
}
};
dojo.html.getOpacity=function(node){
node=dojo.byId(node);
var h=dojo.render.html;
if(h.ie){
var opac=(node.filters&&node.filters.alpha&&typeof node.filters.alpha.opacity=="number"?node.filters.alpha.opacity:100)/100;
}else{
var opac=node.style.opacity||node.style.MozOpacity||node.style.KhtmlOpacity||1;
}
return opac>=0.999999?1:Number(opac);
};
dojo.provide("dojo.html.layout");
dojo.html.sumAncestorProperties=function(node,prop){
node=dojo.byId(node);
if(!node){
return 0;
}
var _509=0;
while(node){
if(dojo.html.getComputedStyle(node,"position")=="fixed"){
return 0;
}
var val=node[prop];
if(val){
_509+=val-0;
if(node==dojo.body()){
break;
}
}
node=node.parentNode;
}
return _509;
};
dojo.html.setStyleAttributes=function(node,_50c){
node=dojo.byId(node);
var _50d=_50c.replace(/(;)?\s*$/,"").split(";");
for(var i=0;i<_50d.length;i++){
var _50f=_50d[i].split(":");
var name=_50f[0].replace(/\s*$/,"").replace(/^\s*/,"").toLowerCase();
var _511=_50f[1].replace(/\s*$/,"").replace(/^\s*/,"");
switch(name){
case "opacity":
dojo.html.setOpacity(node,_511);
break;
case "content-height":
dojo.html.setContentBox(node,{height:_511});
break;
case "content-width":
dojo.html.setContentBox(node,{width:_511});
break;
case "outer-height":
dojo.html.setMarginBox(node,{height:_511});
break;
case "outer-width":
dojo.html.setMarginBox(node,{width:_511});
break;
default:
node.style[dojo.html.toCamelCase(name)]=_511;
}
}
};
dojo.html.boxSizing={MARGIN_BOX:"margin-box",BORDER_BOX:"border-box",PADDING_BOX:"padding-box",CONTENT_BOX:"content-box"};
dojo.html.getAbsolutePosition=dojo.html.abs=function(node,_513,_514){
node=dojo.byId(node,node.ownerDocument);
var ret={x:0,y:0};
var bs=dojo.html.boxSizing;
if(!_514){
_514=bs.CONTENT_BOX;
}
var _517=2;
var _518;
switch(_514){
case bs.MARGIN_BOX:
_518=3;
break;
case bs.BORDER_BOX:
_518=2;
break;
case bs.PADDING_BOX:
default:
_518=1;
break;
case bs.CONTENT_BOX:
_518=0;
break;
}
var h=dojo.render.html;
var db=document["body"]||document["documentElement"];
if(h.ie){
with(node.getBoundingClientRect()){
ret.x=left-2;
ret.y=top-2;
}
}else{
if(document.getBoxObjectFor){
_517=1;
try{
var bo=document.getBoxObjectFor(node);
ret.x=bo.x-dojo.html.sumAncestorProperties(node,"scrollLeft");
ret.y=bo.y-dojo.html.sumAncestorProperties(node,"scrollTop");
}
catch(e){
}
}else{
if(node["offsetParent"]){
var _51c;
if((h.safari)&&(node.style.getPropertyValue("position")=="absolute")&&(node.parentNode==db)){
_51c=db;
}else{
_51c=db.parentNode;
}
if(node.parentNode!=db){
var nd=node;
if(dojo.render.html.opera){
nd=db;
}
ret.x-=dojo.html.sumAncestorProperties(nd,"scrollLeft");
ret.y-=dojo.html.sumAncestorProperties(nd,"scrollTop");
}
var _51e=node;
do{
var n=_51e["offsetLeft"];
if(!h.opera||n>0){
ret.x+=isNaN(n)?0:n;
}
var m=_51e["offsetTop"];
ret.y+=isNaN(m)?0:m;
_51e=_51e.offsetParent;
}while((_51e!=_51c)&&(_51e!=null));
}else{
if(node["x"]&&node["y"]){
ret.x+=isNaN(node.x)?0:node.x;
ret.y+=isNaN(node.y)?0:node.y;
}
}
}
}
if(_513){
var _521=dojo.html.getScroll();
ret.y+=_521.top;
ret.x+=_521.left;
}
var _522=[dojo.html.getPaddingExtent,dojo.html.getBorderExtent,dojo.html.getMarginExtent];
if(_517>_518){
for(var i=_518;i<_517;++i){
ret.y+=_522[i](node,"top");
ret.x+=_522[i](node,"left");
}
}else{
if(_517<_518){
for(var i=_518;i>_517;--i){
ret.y-=_522[i-1](node,"top");
ret.x-=_522[i-1](node,"left");
}
}
}
ret.top=ret.y;
ret.left=ret.x;
return ret;
};
dojo.html.isPositionAbsolute=function(node){
return (dojo.html.getComputedStyle(node,"position")=="absolute");
};
dojo.html._sumPixelValues=function(node,_526,_527){
var _528=0;
for(var x=0;x<_526.length;x++){
_528+=dojo.html.getPixelValue(node,_526[x],_527);
}
return _528;
};
dojo.html.getMargin=function(node){
return {width:dojo.html._sumPixelValues(node,["margin-left","margin-right"],(dojo.html.getComputedStyle(node,"position")=="absolute")),height:dojo.html._sumPixelValues(node,["margin-top","margin-bottom"],(dojo.html.getComputedStyle(node,"position")=="absolute"))};
};
dojo.html.getBorder=function(node){
return {width:dojo.html.getBorderExtent(node,"left")+dojo.html.getBorderExtent(node,"right"),height:dojo.html.getBorderExtent(node,"top")+dojo.html.getBorderExtent(node,"bottom")};
};
dojo.html.getBorderExtent=function(node,side){
return (dojo.html.getStyle(node,"border-"+side+"-style")=="none"?0:dojo.html.getPixelValue(node,"border-"+side+"-width"));
};
dojo.html.getMarginExtent=function(node,side){
return dojo.html._sumPixelValues(node,["margin-"+side],dojo.html.isPositionAbsolute(node));
};
dojo.html.getPaddingExtent=function(node,side){
return dojo.html._sumPixelValues(node,["padding-"+side],true);
};
dojo.html.getPadding=function(node){
return {width:dojo.html._sumPixelValues(node,["padding-left","padding-right"],true),height:dojo.html._sumPixelValues(node,["padding-top","padding-bottom"],true)};
};
dojo.html.getPadBorder=function(node){
var pad=dojo.html.getPadding(node);
var _535=dojo.html.getBorder(node);
return {width:pad.width+_535.width,height:pad.height+_535.height};
};
dojo.html.getBoxSizing=function(node){
var h=dojo.render.html;
var bs=dojo.html.boxSizing;
if(((h.ie)||(h.opera))&&node.nodeName!="IMG"){
var cm=document["compatMode"];
if((cm=="BackCompat")||(cm=="QuirksMode")){
return bs.BORDER_BOX;
}else{
return bs.CONTENT_BOX;
}
}else{
if(arguments.length==0){
node=document.documentElement;
}
var _53a=dojo.html.getStyle(node,"-moz-box-sizing");
if(!_53a){
_53a=dojo.html.getStyle(node,"box-sizing");
}
return (_53a?_53a:bs.CONTENT_BOX);
}
};
dojo.html.isBorderBox=function(node){
return (dojo.html.getBoxSizing(node)==dojo.html.boxSizing.BORDER_BOX);
};
dojo.html.getBorderBox=function(node){
node=dojo.byId(node);
return {width:node.offsetWidth,height:node.offsetHeight};
};
dojo.html.getPaddingBox=function(node){
var box=dojo.html.getBorderBox(node);
var _53f=dojo.html.getBorder(node);
return {width:box.width-_53f.width,height:box.height-_53f.height};
};
dojo.html.getContentBox=function(node){
node=dojo.byId(node);
var _541=dojo.html.getPadBorder(node);
return {width:node.offsetWidth-_541.width,height:node.offsetHeight-_541.height};
};
dojo.html.setContentBox=function(node,args){
node=dojo.byId(node);
var _544=0;
var _545=0;
var isbb=dojo.html.isBorderBox(node);
var _547=(isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var ret={};
if(typeof args.width!="undefined"){
_544=args.width+_547.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_544);
}
if(typeof args.height!="undefined"){
_545=args.height+_547.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_545);
}
return ret;
};
dojo.html.getMarginBox=function(node){
var _54a=dojo.html.getBorderBox(node);
var _54b=dojo.html.getMargin(node);
return {width:_54a.width+_54b.width,height:_54a.height+_54b.height};
};
dojo.html.setMarginBox=function(node,args){
node=dojo.byId(node);
var _54e=0;
var _54f=0;
var isbb=dojo.html.isBorderBox(node);
var _551=(!isbb?dojo.html.getPadBorder(node):{width:0,height:0});
var _552=dojo.html.getMargin(node);
var ret={};
if(typeof args.width!="undefined"){
_54e=args.width-_551.width;
_54e-=_552.width;
ret.width=dojo.html.setPositivePixelValue(node,"width",_54e);
}
if(typeof args.height!="undefined"){
_54f=args.height-_551.height;
_54f-=_552.height;
ret.height=dojo.html.setPositivePixelValue(node,"height",_54f);
}
return ret;
};
dojo.html.getElementBox=function(node,type){
var bs=dojo.html.boxSizing;
switch(type){
case bs.MARGIN_BOX:
return dojo.html.getMarginBox(node);
case bs.BORDER_BOX:
return dojo.html.getBorderBox(node);
case bs.PADDING_BOX:
return dojo.html.getPaddingBox(node);
case bs.CONTENT_BOX:
default:
return dojo.html.getContentBox(node);
}
};
dojo.html.toCoordinateObject=dojo.html.toCoordinateArray=function(_557,_558,_559){
if(_557 instanceof Array||typeof _557=="array"){
dojo.deprecated("dojo.html.toCoordinateArray","use dojo.html.toCoordinateObject({left: , top: , width: , height: }) instead","0.5");
while(_557.length<4){
_557.push(0);
}
while(_557.length>4){
_557.pop();
}
var ret={left:_557[0],top:_557[1],width:_557[2],height:_557[3]};
}else{
if(!_557.nodeType&&!(_557 instanceof String||typeof _557=="string")&&("width" in _557||"height" in _557||"left" in _557||"x" in _557||"top" in _557||"y" in _557)){
var ret={left:_557.left||_557.x||0,top:_557.top||_557.y||0,width:_557.width||0,height:_557.height||0};
}else{
var node=dojo.byId(_557);
var pos=dojo.html.abs(node,_558,_559);
var _55d=dojo.html.getMarginBox(node);
var ret={left:pos.left,top:pos.top,width:_55d.width,height:_55d.height};
}
}
ret.x=ret.left;
ret.y=ret.top;
return ret;
};
dojo.html.setMarginBoxWidth=dojo.html.setOuterWidth=function(node,_55f){
return dojo.html._callDeprecated("setMarginBoxWidth","setMarginBox",arguments,"width");
};
dojo.html.setMarginBoxHeight=dojo.html.setOuterHeight=function(){
return dojo.html._callDeprecated("setMarginBoxHeight","setMarginBox",arguments,"height");
};
dojo.html.getMarginBoxWidth=dojo.html.getOuterWidth=function(){
return dojo.html._callDeprecated("getMarginBoxWidth","getMarginBox",arguments,null,"width");
};
dojo.html.getMarginBoxHeight=dojo.html.getOuterHeight=function(){
return dojo.html._callDeprecated("getMarginBoxHeight","getMarginBox",arguments,null,"height");
};
dojo.html.getTotalOffset=function(node,type,_562){
return dojo.html._callDeprecated("getTotalOffset","getAbsolutePosition",arguments,null,type);
};
dojo.html.getAbsoluteX=function(node,_564){
return dojo.html._callDeprecated("getAbsoluteX","getAbsolutePosition",arguments,null,"x");
};
dojo.html.getAbsoluteY=function(node,_566){
return dojo.html._callDeprecated("getAbsoluteY","getAbsolutePosition",arguments,null,"y");
};
dojo.html.totalOffsetLeft=function(node,_568){
return dojo.html._callDeprecated("totalOffsetLeft","getAbsolutePosition",arguments,null,"left");
};
dojo.html.totalOffsetTop=function(node,_56a){
return dojo.html._callDeprecated("totalOffsetTop","getAbsolutePosition",arguments,null,"top");
};
dojo.html.getMarginWidth=function(node){
return dojo.html._callDeprecated("getMarginWidth","getMargin",arguments,null,"width");
};
dojo.html.getMarginHeight=function(node){
return dojo.html._callDeprecated("getMarginHeight","getMargin",arguments,null,"height");
};
dojo.html.getBorderWidth=function(node){
return dojo.html._callDeprecated("getBorderWidth","getBorder",arguments,null,"width");
};
dojo.html.getBorderHeight=function(node){
return dojo.html._callDeprecated("getBorderHeight","getBorder",arguments,null,"height");
};
dojo.html.getPaddingWidth=function(node){
return dojo.html._callDeprecated("getPaddingWidth","getPadding",arguments,null,"width");
};
dojo.html.getPaddingHeight=function(node){
return dojo.html._callDeprecated("getPaddingHeight","getPadding",arguments,null,"height");
};
dojo.html.getPadBorderWidth=function(node){
return dojo.html._callDeprecated("getPadBorderWidth","getPadBorder",arguments,null,"width");
};
dojo.html.getPadBorderHeight=function(node){
return dojo.html._callDeprecated("getPadBorderHeight","getPadBorder",arguments,null,"height");
};
dojo.html.getBorderBoxWidth=dojo.html.getInnerWidth=function(){
return dojo.html._callDeprecated("getBorderBoxWidth","getBorderBox",arguments,null,"width");
};
dojo.html.getBorderBoxHeight=dojo.html.getInnerHeight=function(){
return dojo.html._callDeprecated("getBorderBoxHeight","getBorderBox",arguments,null,"height");
};
dojo.html.getContentBoxWidth=dojo.html.getContentWidth=function(){
return dojo.html._callDeprecated("getContentBoxWidth","getContentBox",arguments,null,"width");
};
dojo.html.getContentBoxHeight=dojo.html.getContentHeight=function(){
return dojo.html._callDeprecated("getContentBoxHeight","getContentBox",arguments,null,"height");
};
dojo.html.setContentBoxWidth=dojo.html.setContentWidth=function(node,_574){
return dojo.html._callDeprecated("setContentBoxWidth","setContentBox",arguments,"width");
};
dojo.html.setContentBoxHeight=dojo.html.setContentHeight=function(node,_576){
return dojo.html._callDeprecated("setContentBoxHeight","setContentBox",arguments,"height");
};
dojo.provide("dojo.html.util");
dojo.html.getElementWindow=function(_577){
return dojo.html.getDocumentWindow(_577.ownerDocument);
};
dojo.html.getDocumentWindow=function(doc){
if(dojo.render.html.safari&&!doc._parentWindow){
var fix=function(win){
win.document._parentWindow=win;
for(var i=0;i<win.frames.length;i++){
fix(win.frames[i]);
}
};
fix(window.top);
}
if(dojo.render.html.ie&&window!==document.parentWindow&&!doc._parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc._parentWindow||doc.parentWindow||doc.defaultView;
};
dojo.html.gravity=function(node,e){
node=dojo.byId(node);
var _57f=dojo.html.getCursorPosition(e);
with(dojo.html){
var _580=getAbsolutePosition(node,true);
var bb=getBorderBox(node);
var _582=_580.x+(bb.width/2);
var _583=_580.y+(bb.height/2);
}
with(dojo.html.gravity){
return ((_57f.x<_582?WEST:EAST)|(_57f.y<_583?NORTH:SOUTH));
}
};
dojo.html.gravity.NORTH=1;
dojo.html.gravity.SOUTH=1<<1;
dojo.html.gravity.EAST=1<<2;
dojo.html.gravity.WEST=1<<3;
dojo.html.overElement=function(_584,e){
_584=dojo.byId(_584);
var _586=dojo.html.getCursorPosition(e);
var bb=dojo.html.getBorderBox(_584);
var _588=dojo.html.getAbsolutePosition(_584,true,dojo.html.boxSizing.BORDER_BOX);
var top=_588.y;
var _58a=top+bb.height;
var left=_588.x;
var _58c=left+bb.width;
return (_586.x>=left&&_586.x<=_58c&&_586.y>=top&&_586.y<=_58a);
};
dojo.html.renderedTextContent=function(node){
node=dojo.byId(node);
var _58e="";
if(node==null){
return _58e;
}
for(var i=0;i<node.childNodes.length;i++){
switch(node.childNodes[i].nodeType){
case 1:
case 5:
var _590="unknown";
try{
_590=dojo.html.getStyle(node.childNodes[i],"display");
}
catch(E){
}
switch(_590){
case "block":
case "list-item":
case "run-in":
case "table":
case "table-row-group":
case "table-header-group":
case "table-footer-group":
case "table-row":
case "table-column-group":
case "table-column":
case "table-cell":
case "table-caption":
_58e+="\n";
_58e+=dojo.html.renderedTextContent(node.childNodes[i]);
_58e+="\n";
break;
case "none":
break;
default:
if(node.childNodes[i].tagName&&node.childNodes[i].tagName.toLowerCase()=="br"){
_58e+="\n";
}else{
_58e+=dojo.html.renderedTextContent(node.childNodes[i]);
}
break;
}
break;
case 3:
case 2:
case 4:
var text=node.childNodes[i].nodeValue;
var _592="unknown";
try{
_592=dojo.html.getStyle(node,"text-transform");
}
catch(E){
}
switch(_592){
case "capitalize":
var _593=text.split(" ");
for(var i=0;i<_593.length;i++){
_593[i]=_593[i].charAt(0).toUpperCase()+_593[i].substring(1);
}
text=_593.join(" ");
break;
case "uppercase":
text=text.toUpperCase();
break;
case "lowercase":
text=text.toLowerCase();
break;
default:
break;
}
switch(_592){
case "nowrap":
break;
case "pre-wrap":
break;
case "pre-line":
break;
case "pre":
break;
default:
text=text.replace(/\s+/," ");
if(/\s$/.test(_58e)){
text.replace(/^\s/,"");
}
break;
}
_58e+=text;
break;
default:
break;
}
}
return _58e;
};
dojo.html.createNodesFromText=function(txt,trim){
if(trim){
txt=txt.replace(/^\s+|\s+$/g,"");
}
var tn=dojo.doc().createElement("div");
tn.style.visibility="hidden";
dojo.body().appendChild(tn);
var _597="none";
if((/^<t[dh][\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody><tr>"+txt+"</tr></tbody></table>";
_597="cell";
}else{
if((/^<tr[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table><tbody>"+txt+"</tbody></table>";
_597="row";
}else{
if((/^<(thead|tbody|tfoot)[\s\r\n>]/i).test(txt.replace(/^\s+/))){
txt="<table>"+txt+"</table>";
_597="section";
}
}
}
tn.innerHTML=txt;
if(tn["normalize"]){
tn.normalize();
}
var _598=null;
switch(_597){
case "cell":
_598=tn.getElementsByTagName("tr")[0];
break;
case "row":
_598=tn.getElementsByTagName("tbody")[0];
break;
case "section":
_598=tn.getElementsByTagName("table")[0];
break;
default:
_598=tn;
break;
}
var _599=[];
for(var x=0;x<_598.childNodes.length;x++){
_599.push(_598.childNodes[x].cloneNode(true));
}
tn.style.display="none";
dojo.html.destroyNode(tn);
return _599;
};
dojo.html.placeOnScreen=function(node,_59c,_59d,_59e,_59f,_5a0,_5a1){
if(_59c instanceof Array||typeof _59c=="array"){
_5a1=_5a0;
_5a0=_59f;
_59f=_59e;
_59e=_59d;
_59d=_59c[1];
_59c=_59c[0];
}
if(_5a0 instanceof String||typeof _5a0=="string"){
_5a0=_5a0.split(",");
}
if(!isNaN(_59e)){
_59e=[Number(_59e),Number(_59e)];
}else{
if(!(_59e instanceof Array||typeof _59e=="array")){
_59e=[0,0];
}
}
var _5a2=dojo.html.getScroll().offset;
var view=dojo.html.getViewport();
node=dojo.byId(node);
var _5a4=node.style.display;
node.style.display="";
var bb=dojo.html.getBorderBox(node);
var w=bb.width;
var h=bb.height;
node.style.display=_5a4;
if(!(_5a0 instanceof Array||typeof _5a0=="array")){
_5a0=["TL"];
}
var _5a8,_5a9,_5aa=Infinity,_5ab;
for(var _5ac=0;_5ac<_5a0.length;++_5ac){
var _5ad=_5a0[_5ac];
var _5ae=true;
var tryX=_59c-(_5ad.charAt(1)=="L"?0:w)+_59e[0]*(_5ad.charAt(1)=="L"?1:-1);
var tryY=_59d-(_5ad.charAt(0)=="T"?0:h)+_59e[1]*(_5ad.charAt(0)=="T"?1:-1);
if(_59f){
tryX-=_5a2.x;
tryY-=_5a2.y;
}
if(tryX<0){
tryX=0;
_5ae=false;
}
if(tryY<0){
tryY=0;
_5ae=false;
}
var x=tryX+w;
if(x>view.width){
x=view.width-w;
_5ae=false;
}else{
x=tryX;
}
x=Math.max(_59e[0],x)+_5a2.x;
var y=tryY+h;
if(y>view.height){
y=view.height-h;
_5ae=false;
}else{
y=tryY;
}
y=Math.max(_59e[1],y)+_5a2.y;
if(_5ae){
_5a8=x;
_5a9=y;
_5aa=0;
_5ab=_5ad;
break;
}else{
var dist=Math.pow(x-tryX-_5a2.x,2)+Math.pow(y-tryY-_5a2.y,2);
if(_5aa>dist){
_5aa=dist;
_5a8=x;
_5a9=y;
_5ab=_5ad;
}
}
}
if(!_5a1){
node.style.left=_5a8+"px";
node.style.top=_5a9+"px";
}
return {left:_5a8,top:_5a9,x:_5a8,y:_5a9,dist:_5aa,corner:_5ab};
};
dojo.html.placeOnScreenPoint=function(node,_5b5,_5b6,_5b7,_5b8){
dojo.deprecated("dojo.html.placeOnScreenPoint","use dojo.html.placeOnScreen() instead","0.5");
return dojo.html.placeOnScreen(node,_5b5,_5b6,_5b7,_5b8,["TL","TR","BL","BR"]);
};
dojo.html.placeOnScreenAroundElement=function(node,_5ba,_5bb,_5bc,_5bd,_5be){
var best,_5c0=Infinity;
_5ba=dojo.byId(_5ba);
var _5c1=_5ba.style.display;
_5ba.style.display="";
var mb=dojo.html.getElementBox(_5ba,_5bc);
var _5c3=mb.width;
var _5c4=mb.height;
var _5c5=dojo.html.getAbsolutePosition(_5ba,true,_5bc);
_5ba.style.display=_5c1;
for(var _5c6 in _5bd){
var pos,_5c8,_5c9;
var _5ca=_5bd[_5c6];
_5c8=_5c5.x+(_5c6.charAt(1)=="L"?0:_5c3);
_5c9=_5c5.y+(_5c6.charAt(0)=="T"?0:_5c4);
pos=dojo.html.placeOnScreen(node,_5c8,_5c9,_5bb,true,_5ca,true);
if(pos.dist==0){
best=pos;
break;
}else{
if(_5c0>pos.dist){
_5c0=pos.dist;
best=pos;
}
}
}
if(!_5be){
node.style.left=best.left+"px";
node.style.top=best.top+"px";
}
return best;
};
dojo.html.scrollIntoView=function(node){
if(!node){
return;
}
if(dojo.render.html.ie){
if(dojo.html.getBorderBox(node.parentNode).height<=node.parentNode.scrollHeight){
node.scrollIntoView(false);
}
}else{
if(dojo.render.html.mozilla){
node.scrollIntoView(false);
}else{
var _5cc=node.parentNode;
var _5cd=_5cc.scrollTop+dojo.html.getBorderBox(_5cc).height;
var _5ce=node.offsetTop+dojo.html.getMarginBox(node).height;
if(_5cd<_5ce){
_5cc.scrollTop+=(_5ce-_5cd);
}else{
if(_5cc.scrollTop>node.offsetTop){
_5cc.scrollTop-=(_5cc.scrollTop-node.offsetTop);
}
}
}
}
};
dojo.provide("dojo.gfx.color");
dojo.gfx.color.Color=function(r,g,b,a){
if(dojo.lang.isArray(r)){
this.r=r[0];
this.g=r[1];
this.b=r[2];
this.a=r[3]||1;
}else{
if(dojo.lang.isString(r)){
var rgb=dojo.gfx.color.extractRGB(r);
this.r=rgb[0];
this.g=rgb[1];
this.b=rgb[2];
this.a=g||1;
}else{
if(r instanceof dojo.gfx.color.Color){
this.r=r.r;
this.b=r.b;
this.g=r.g;
this.a=r.a;
}else{
this.r=r;
this.g=g;
this.b=b;
this.a=a;
}
}
}
};
dojo.gfx.color.Color.fromArray=function(arr){
return new dojo.gfx.color.Color(arr[0],arr[1],arr[2],arr[3]);
};
dojo.extend(dojo.gfx.color.Color,{toRgb:function(_5d5){
if(_5d5){
return this.toRgba();
}else{
return [this.r,this.g,this.b];
}
},toRgba:function(){
return [this.r,this.g,this.b,this.a];
},toHex:function(){
return dojo.gfx.color.rgb2hex(this.toRgb());
},toCss:function(){
return "rgb("+this.toRgb().join()+")";
},toString:function(){
return this.toHex();
},blend:function(_5d6,_5d7){
var rgb=null;
if(dojo.lang.isArray(_5d6)){
rgb=_5d6;
}else{
if(_5d6 instanceof dojo.gfx.color.Color){
rgb=_5d6.toRgb();
}else{
rgb=new dojo.gfx.color.Color(_5d6).toRgb();
}
}
return dojo.gfx.color.blend(this.toRgb(),rgb,_5d7);
}});
dojo.gfx.color.named={white:[255,255,255],black:[0,0,0],red:[255,0,0],green:[0,255,0],lime:[0,255,0],blue:[0,0,255],navy:[0,0,128],gray:[128,128,128],silver:[192,192,192]};
dojo.gfx.color.blend=function(a,b,_5db){
if(typeof a=="string"){
return dojo.gfx.color.blendHex(a,b,_5db);
}
if(!_5db){
_5db=0;
}
_5db=Math.min(Math.max(-1,_5db),1);
_5db=((_5db+1)/2);
var c=[];
for(var x=0;x<3;x++){
c[x]=parseInt(b[x]+((a[x]-b[x])*_5db));
}
return c;
};
dojo.gfx.color.blendHex=function(a,b,_5e0){
return dojo.gfx.color.rgb2hex(dojo.gfx.color.blend(dojo.gfx.color.hex2rgb(a),dojo.gfx.color.hex2rgb(b),_5e0));
};
dojo.gfx.color.extractRGB=function(_5e1){
var hex="0123456789abcdef";
_5e1=_5e1.toLowerCase();
if(_5e1.indexOf("rgb")==0){
var _5e3=_5e1.match(/rgba*\((\d+), *(\d+), *(\d+)/i);
var ret=_5e3.splice(1,3);
return ret;
}else{
var _5e5=dojo.gfx.color.hex2rgb(_5e1);
if(_5e5){
return _5e5;
}else{
return dojo.gfx.color.named[_5e1]||[255,255,255];
}
}
};
dojo.gfx.color.hex2rgb=function(hex){
var _5e7="0123456789ABCDEF";
var rgb=new Array(3);
if(hex.indexOf("#")==0){
hex=hex.substring(1);
}
hex=hex.toUpperCase();
if(hex.replace(new RegExp("["+_5e7+"]","g"),"")!=""){
return null;
}
if(hex.length==3){
rgb[0]=hex.charAt(0)+hex.charAt(0);
rgb[1]=hex.charAt(1)+hex.charAt(1);
rgb[2]=hex.charAt(2)+hex.charAt(2);
}else{
rgb[0]=hex.substring(0,2);
rgb[1]=hex.substring(2,4);
rgb[2]=hex.substring(4);
}
for(var i=0;i<rgb.length;i++){
rgb[i]=_5e7.indexOf(rgb[i].charAt(0))*16+_5e7.indexOf(rgb[i].charAt(1));
}
return rgb;
};
dojo.gfx.color.rgb2hex=function(r,g,b){
if(dojo.lang.isArray(r)){
g=r[1]||0;
b=r[2]||0;
r=r[0]||0;
}
var ret=dojo.lang.map([r,g,b],function(x){
x=new Number(x);
var s=x.toString(16);
while(s.length<2){
s="0"+s;
}
return s;
});
ret.unshift("#");
return ret.join("");
};
dojo.provide("dojo.lfx.Animation");
dojo.lfx.Line=function(_5f0,end){
this.start=_5f0;
this.end=end;
if(dojo.lang.isArray(_5f0)){
var diff=[];
dojo.lang.forEach(this.start,function(s,i){
diff[i]=this.end[i]-s;
},this);
this.getValue=function(n){
var res=[];
dojo.lang.forEach(this.start,function(s,i){
res[i]=(diff[i]*n)+s;
},this);
return res;
};
}else{
var diff=end-_5f0;
this.getValue=function(n){
return (diff*n)+this.start;
};
}
};
dojo.lfx.easeDefault=function(n){
if(dojo.render.html.khtml){
return (parseFloat("0.5")+((Math.sin((n+parseFloat("1.5"))*Math.PI))/2));
}else{
return (0.5+((Math.sin((n+1.5)*Math.PI))/2));
}
};
dojo.lfx.easeIn=function(n){
return Math.pow(n,3);
};
dojo.lfx.easeOut=function(n){
return (1-Math.pow(1-n,3));
};
dojo.lfx.easeInOut=function(n){
return ((3*Math.pow(n,2))-(2*Math.pow(n,3)));
};
dojo.lfx.IAnimation=function(){
};
dojo.lang.extend(dojo.lfx.IAnimation,{curve:null,duration:1000,easing:null,repeatCount:0,rate:25,handler:null,beforeBegin:null,onBegin:null,onAnimate:null,onEnd:null,onPlay:null,onPause:null,onStop:null,play:null,pause:null,stop:null,connect:function(evt,_5ff,_600){
if(!_600){
_600=_5ff;
_5ff=this;
}
_600=dojo.lang.hitch(_5ff,_600);
var _601=this[evt]||function(){
};
this[evt]=function(){
var ret=_601.apply(this,arguments);
_600.apply(this,arguments);
return ret;
};
return this;
},fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,(args||[]));
}
return this;
},repeat:function(_605){
this.repeatCount=_605;
return this;
},_active:false,_paused:false});
dojo.lfx.Animation=function(_606,_607,_608,_609,_60a,rate){
dojo.lfx.IAnimation.call(this);
if(dojo.lang.isNumber(_606)||(!_606&&_607.getValue)){
rate=_60a;
_60a=_609;
_609=_608;
_608=_607;
_607=_606;
_606=null;
}else{
if(_606.getValue||dojo.lang.isArray(_606)){
rate=_609;
_60a=_608;
_609=_607;
_608=_606;
_607=null;
_606=null;
}
}
if(dojo.lang.isArray(_608)){
this.curve=new dojo.lfx.Line(_608[0],_608[1]);
}else{
this.curve=_608;
}
if(_607!=null&&_607>0){
this.duration=_607;
}
if(_60a){
this.repeatCount=_60a;
}
if(rate){
this.rate=rate;
}
if(_606){
dojo.lang.forEach(["handler","beforeBegin","onBegin","onEnd","onPlay","onStop","onAnimate"],function(item){
if(_606[item]){
this.connect(item,_606[item]);
}
},this);
}
if(_609&&dojo.lang.isFunction(_609)){
this.easing=_609;
}
};
dojo.inherits(dojo.lfx.Animation,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Animation,{_startTime:null,_endTime:null,_timer:null,_percent:0,_startRepeatCount:0,play:function(_60d,_60e){
if(_60e){
clearTimeout(this._timer);
this._active=false;
this._paused=false;
this._percent=0;
}else{
if(this._active&&!this._paused){
return this;
}
}
this.fire("handler",["beforeBegin"]);
this.fire("beforeBegin");
if(_60d>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_60e);
}),_60d);
return this;
}
this._startTime=new Date().valueOf();
if(this._paused){
this._startTime-=(this.duration*this._percent/100);
}
this._endTime=this._startTime+this.duration;
this._active=true;
this._paused=false;
var step=this._percent/100;
var _610=this.curve.getValue(step);
if(this._percent==0){
if(!this._startRepeatCount){
this._startRepeatCount=this.repeatCount;
}
this.fire("handler",["begin",_610]);
this.fire("onBegin",[_610]);
}
this.fire("handler",["play",_610]);
this.fire("onPlay",[_610]);
this._cycle();
return this;
},pause:function(){
clearTimeout(this._timer);
if(!this._active){
return this;
}
this._paused=true;
var _611=this.curve.getValue(this._percent/100);
this.fire("handler",["pause",_611]);
this.fire("onPause",[_611]);
return this;
},gotoPercent:function(pct,_613){
clearTimeout(this._timer);
this._active=true;
this._paused=true;
this._percent=pct;
if(_613){
this.play();
}
return this;
},stop:function(_614){
clearTimeout(this._timer);
var step=this._percent/100;
if(_614){
step=1;
}
var _616=this.curve.getValue(step);
this.fire("handler",["stop",_616]);
this.fire("onStop",[_616]);
this._active=false;
this._paused=false;
return this;
},status:function(){
if(this._active){
return this._paused?"paused":"playing";
}else{
return "stopped";
}
return this;
},_cycle:function(){
clearTimeout(this._timer);
if(this._active){
var curr=new Date().valueOf();
var step=(curr-this._startTime)/(this._endTime-this._startTime);
if(step>=1){
step=1;
this._percent=100;
}else{
this._percent=step*100;
}
if((this.easing)&&(dojo.lang.isFunction(this.easing))){
step=this.easing(step);
}
var _619=this.curve.getValue(step);
this.fire("handler",["animate",_619]);
this.fire("onAnimate",[_619]);
if(step<1){
this._timer=setTimeout(dojo.lang.hitch(this,"_cycle"),this.rate);
}else{
this._active=false;
this.fire("handler",["end"]);
this.fire("onEnd");
if(this.repeatCount>0){
this.repeatCount--;
this.play(null,true);
}else{
if(this.repeatCount==-1){
this.play(null,true);
}else{
if(this._startRepeatCount){
this.repeatCount=this._startRepeatCount;
this._startRepeatCount=0;
}
}
}
}
}
return this;
}});
dojo.lfx.Combine=function(_61a){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._animsEnded=0;
var _61b=arguments;
if(_61b.length==1&&(dojo.lang.isArray(_61b[0])||dojo.lang.isArrayLike(_61b[0]))){
_61b=_61b[0];
}
dojo.lang.forEach(_61b,function(anim){
this._anims.push(anim);
anim.connect("onEnd",dojo.lang.hitch(this,"_onAnimsEnded"));
},this);
};
dojo.inherits(dojo.lfx.Combine,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Combine,{_animsEnded:0,play:function(_61d,_61e){
if(!this._anims.length){
return this;
}
this.fire("beforeBegin");
if(_61d>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_61e);
}),_61d);
return this;
}
if(_61e||this._anims[0].percent==0){
this.fire("onBegin");
}
this.fire("onPlay");
this._animsCall("play",null,_61e);
return this;
},pause:function(){
this.fire("onPause");
this._animsCall("pause");
return this;
},stop:function(_61f){
this.fire("onStop");
this._animsCall("stop",_61f);
return this;
},_onAnimsEnded:function(){
this._animsEnded++;
if(this._animsEnded>=this._anims.length){
this.fire("onEnd");
}
return this;
},_animsCall:function(_620){
var args=[];
if(arguments.length>1){
for(var i=1;i<arguments.length;i++){
args.push(arguments[i]);
}
}
var _623=this;
dojo.lang.forEach(this._anims,function(anim){
anim[_620](args);
},_623);
return this;
}});
dojo.lfx.Chain=function(_625){
dojo.lfx.IAnimation.call(this);
this._anims=[];
this._currAnim=-1;
var _626=arguments;
if(_626.length==1&&(dojo.lang.isArray(_626[0])||dojo.lang.isArrayLike(_626[0]))){
_626=_626[0];
}
var _627=this;
dojo.lang.forEach(_626,function(anim,i,_62a){
this._anims.push(anim);
if(i<_62a.length-1){
anim.connect("onEnd",dojo.lang.hitch(this,"_playNext"));
}else{
anim.connect("onEnd",dojo.lang.hitch(this,function(){
this.fire("onEnd");
}));
}
},this);
};
dojo.inherits(dojo.lfx.Chain,dojo.lfx.IAnimation);
dojo.lang.extend(dojo.lfx.Chain,{_currAnim:-1,play:function(_62b,_62c){
if(!this._anims.length){
return this;
}
if(_62c||!this._anims[this._currAnim]){
this._currAnim=0;
}
var _62d=this._anims[this._currAnim];
this.fire("beforeBegin");
if(_62b>0){
setTimeout(dojo.lang.hitch(this,function(){
this.play(null,_62c);
}),_62b);
return this;
}
if(_62d){
if(this._currAnim==0){
this.fire("handler",["begin",this._currAnim]);
this.fire("onBegin",[this._currAnim]);
}
this.fire("onPlay",[this._currAnim]);
_62d.play(null,_62c);
}
return this;
},pause:function(){
if(this._anims[this._currAnim]){
this._anims[this._currAnim].pause();
this.fire("onPause",[this._currAnim]);
}
return this;
},playPause:function(){
if(this._anims.length==0){
return this;
}
if(this._currAnim==-1){
this._currAnim=0;
}
var _62e=this._anims[this._currAnim];
if(_62e){
if(!_62e._active||_62e._paused){
this.play();
}else{
this.pause();
}
}
return this;
},stop:function(){
var _62f=this._anims[this._currAnim];
if(_62f){
_62f.stop();
this.fire("onStop",[this._currAnim]);
}
return _62f;
},_playNext:function(){
if(this._currAnim==-1||this._anims.length==0){
return this;
}
this._currAnim++;
if(this._anims[this._currAnim]){
this._anims[this._currAnim].play(null,true);
}
return this;
}});
dojo.lfx.combine=function(_630){
var _631=arguments;
if(dojo.lang.isArray(arguments[0])){
_631=arguments[0];
}
if(_631.length==1){
return _631[0];
}
return new dojo.lfx.Combine(_631);
};
dojo.lfx.chain=function(_632){
var _633=arguments;
if(dojo.lang.isArray(arguments[0])){
_633=arguments[0];
}
if(_633.length==1){
return _633[0];
}
return new dojo.lfx.Chain(_633);
};
dojo.provide("dojo.html.color");
dojo.html.getBackgroundColor=function(node){
node=dojo.byId(node);
var _635;
do{
_635=dojo.html.getStyle(node,"background-color");
if(_635.toLowerCase()=="rgba(0, 0, 0, 0)"){
_635="transparent";
}
if(node==document.getElementsByTagName("body")[0]){
node=null;
break;
}
node=node.parentNode;
}while(node&&dojo.lang.inArray(["transparent",""],_635));
if(_635=="transparent"){
_635=[255,255,255,0];
}else{
_635=dojo.gfx.color.extractRGB(_635);
}
return _635;
};
dojo.provide("dojo.lfx.html");
dojo.lfx.html._byId=function(_636){
if(!_636){
return [];
}
if(dojo.lang.isArrayLike(_636)){
if(!_636.alreadyChecked){
var n=[];
dojo.lang.forEach(_636,function(node){
n.push(dojo.byId(node));
});
n.alreadyChecked=true;
return n;
}else{
return _636;
}
}else{
var n=[];
n.push(dojo.byId(_636));
n.alreadyChecked=true;
return n;
}
};
dojo.lfx.html.propertyAnimation=function(_639,_63a,_63b,_63c,_63d){
_639=dojo.lfx.html._byId(_639);
var _63e={"propertyMap":_63a,"nodes":_639,"duration":_63b,"easing":_63c||dojo.lfx.easeDefault};
var _63f=function(args){
if(args.nodes.length==1){
var pm=args.propertyMap;
if(!dojo.lang.isArray(args.propertyMap)){
var parr=[];
for(var _643 in pm){
pm[_643].property=_643;
parr.push(pm[_643]);
}
pm=args.propertyMap=parr;
}
dojo.lang.forEach(pm,function(prop){
if(dj_undef("start",prop)){
if(prop.property!="opacity"){
prop.start=parseInt(dojo.html.getComputedStyle(args.nodes[0],prop.property));
}else{
prop.start=dojo.html.getOpacity(args.nodes[0]);
}
}
});
}
};
var _645=function(_646){
var _647=[];
dojo.lang.forEach(_646,function(c){
_647.push(Math.round(c));
});
return _647;
};
var _649=function(n,_64b){
n=dojo.byId(n);
if(!n||!n.style){
return;
}
for(var s in _64b){
try{
if(s=="opacity"){
dojo.html.setOpacity(n,_64b[s]);
}else{
n.style[s]=_64b[s];
}
}
catch(e){
dojo.debug(e);
}
}
};
var _64d=function(_64e){
this._properties=_64e;
this.diffs=new Array(_64e.length);
dojo.lang.forEach(_64e,function(prop,i){
if(dojo.lang.isFunction(prop.start)){
prop.start=prop.start(prop,i);
}
if(dojo.lang.isFunction(prop.end)){
prop.end=prop.end(prop,i);
}
if(dojo.lang.isArray(prop.start)){
this.diffs[i]=null;
}else{
if(prop.start instanceof dojo.gfx.color.Color){
prop.startRgb=prop.start.toRgb();
prop.endRgb=prop.end.toRgb();
}else{
this.diffs[i]=prop.end-prop.start;
}
}
},this);
this.getValue=function(n){
var ret={};
dojo.lang.forEach(this._properties,function(prop,i){
var _655=null;
if(dojo.lang.isArray(prop.start)){
}else{
if(prop.start instanceof dojo.gfx.color.Color){
_655=(prop.units||"rgb")+"(";
for(var j=0;j<prop.startRgb.length;j++){
_655+=Math.round(((prop.endRgb[j]-prop.startRgb[j])*n)+prop.startRgb[j])+(j<prop.startRgb.length-1?",":"");
}
_655+=")";
}else{
_655=((this.diffs[i])*n)+prop.start+(prop.property!="opacity"?prop.units||"px":"");
}
}
ret[dojo.html.toCamelCase(prop.property)]=_655;
},this);
return ret;
};
};
var anim=new dojo.lfx.Animation({beforeBegin:function(){
_63f(_63e);
anim.curve=new _64d(_63e.propertyMap);
},onAnimate:function(_658){
dojo.lang.forEach(_63e.nodes,function(node){
_649(node,_658);
});
}},_63e.duration,null,_63e.easing);
if(_63d){
for(var x in _63d){
if(dojo.lang.isFunction(_63d[x])){
anim.connect(x,anim,_63d[x]);
}
}
}
return anim;
};
dojo.lfx.html._makeFadeable=function(_65b){
var _65c=function(node){
if(dojo.render.html.ie){
if((node.style.zoom.length==0)&&(dojo.html.getStyle(node,"zoom")=="normal")){
node.style.zoom="1";
}
if((node.style.width.length==0)&&(dojo.html.getStyle(node,"width")=="auto")){
node.style.width="auto";
}
}
};
if(dojo.lang.isArrayLike(_65b)){
dojo.lang.forEach(_65b,_65c);
}else{
_65c(_65b);
}
};
dojo.lfx.html.fade=function(_65e,_65f,_660,_661,_662){
_65e=dojo.lfx.html._byId(_65e);
var _663={property:"opacity"};
if(!dj_undef("start",_65f)){
_663.start=_65f.start;
}else{
_663.start=function(){
return dojo.html.getOpacity(_65e[0]);
};
}
if(!dj_undef("end",_65f)){
_663.end=_65f.end;
}else{
dojo.raise("dojo.lfx.html.fade needs an end value");
}
var anim=dojo.lfx.propertyAnimation(_65e,[_663],_660,_661);
anim.connect("beforeBegin",function(){
dojo.lfx.html._makeFadeable(_65e);
});
if(_662){
anim.connect("onEnd",function(){
_662(_65e,anim);
});
}
return anim;
};
dojo.lfx.html.fadeIn=function(_665,_666,_667,_668){
return dojo.lfx.html.fade(_665,{end:1},_666,_667,_668);
};
dojo.lfx.html.fadeOut=function(_669,_66a,_66b,_66c){
return dojo.lfx.html.fade(_669,{end:0},_66a,_66b,_66c);
};
dojo.lfx.html.fadeShow=function(_66d,_66e,_66f,_670){
_66d=dojo.lfx.html._byId(_66d);
dojo.lang.forEach(_66d,function(node){
dojo.html.setOpacity(node,0);
});
var anim=dojo.lfx.html.fadeIn(_66d,_66e,_66f,_670);
anim.connect("beforeBegin",function(){
if(dojo.lang.isArrayLike(_66d)){
dojo.lang.forEach(_66d,dojo.html.show);
}else{
dojo.html.show(_66d);
}
});
return anim;
};
dojo.lfx.html.fadeHide=function(_673,_674,_675,_676){
var anim=dojo.lfx.html.fadeOut(_673,_674,_675,function(){
if(dojo.lang.isArrayLike(_673)){
dojo.lang.forEach(_673,dojo.html.hide);
}else{
dojo.html.hide(_673);
}
if(_676){
_676(_673,anim);
}
});
return anim;
};
dojo.lfx.html.wipeIn=function(_678,_679,_67a,_67b){
_678=dojo.lfx.html._byId(_678);
var _67c=[];
dojo.lang.forEach(_678,function(node){
var _67e={};
var _67f,_680,_681;
with(node.style){
_67f=top;
_680=left;
_681=position;
top="-9999px";
left="-9999px";
position="absolute";
display="";
}
var _682=dojo.html.getBorderBox(node).height;
with(node.style){
top=_67f;
left=_680;
position=_681;
display="none";
}
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:1,end:function(){
return _682;
}}},_679,_67a);
anim.connect("beforeBegin",function(){
_67e.overflow=node.style.overflow;
_67e.height=node.style.height;
with(node.style){
overflow="hidden";
_682="1px";
}
dojo.html.show(node);
});
anim.connect("onEnd",function(){
with(node.style){
overflow=_67e.overflow;
_682=_67e.height;
}
if(_67b){
_67b(node,anim);
}
});
_67c.push(anim);
});
return dojo.lfx.combine(_67c);
};
dojo.lfx.html.wipeOut=function(_684,_685,_686,_687){
_684=dojo.lfx.html._byId(_684);
var _688=[];
dojo.lang.forEach(_684,function(node){
var _68a={};
var anim=dojo.lfx.propertyAnimation(node,{"height":{start:function(){
return dojo.html.getContentBox(node).height;
},end:1}},_685,_686,{"beforeBegin":function(){
_68a.overflow=node.style.overflow;
_68a.height=node.style.height;
with(node.style){
overflow="hidden";
}
dojo.html.show(node);
},"onEnd":function(){
dojo.html.hide(node);
with(node.style){
overflow=_68a.overflow;
height=_68a.height;
}
if(_687){
_687(node,anim);
}
}});
_688.push(anim);
});
return dojo.lfx.combine(_688);
};
dojo.lfx.html.slideTo=function(_68c,_68d,_68e,_68f,_690){
_68c=dojo.lfx.html._byId(_68c);
var _691=[];
var _692=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_68d)){
dojo.deprecated("dojo.lfx.html.slideTo(node, array)","use dojo.lfx.html.slideTo(node, {top: value, left: value});","0.5");
_68d={top:_68d[0],left:_68d[1]};
}
dojo.lang.forEach(_68c,function(node){
var top=null;
var left=null;
var init=(function(){
var _697=node;
return function(){
var pos=_692(_697,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_692(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_692(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_697,true);
dojo.html.setStyleAttributes(_697,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:(_68d.top||0)},"left":{start:left,end:(_68d.left||0)}},_68e,_68f,{"beforeBegin":init});
if(_690){
anim.connect("onEnd",function(){
_690(_68c,anim);
});
}
_691.push(anim);
});
return dojo.lfx.combine(_691);
};
dojo.lfx.html.slideBy=function(_69b,_69c,_69d,_69e,_69f){
_69b=dojo.lfx.html._byId(_69b);
var _6a0=[];
var _6a1=dojo.html.getComputedStyle;
if(dojo.lang.isArray(_69c)){
dojo.deprecated("dojo.lfx.html.slideBy(node, array)","use dojo.lfx.html.slideBy(node, {top: value, left: value});","0.5");
_69c={top:_69c[0],left:_69c[1]};
}
dojo.lang.forEach(_69b,function(node){
var top=null;
var left=null;
var init=(function(){
var _6a6=node;
return function(){
var pos=_6a1(_6a6,"position");
top=(pos=="absolute"?node.offsetTop:parseInt(_6a1(node,"top"))||0);
left=(pos=="absolute"?node.offsetLeft:parseInt(_6a1(node,"left"))||0);
if(!dojo.lang.inArray(["absolute","relative"],pos)){
var ret=dojo.html.abs(_6a6,true);
dojo.html.setStyleAttributes(_6a6,"position:absolute;top:"+ret.y+"px;left:"+ret.x+"px;");
top=ret.y;
left=ret.x;
}
};
})();
init();
var anim=dojo.lfx.propertyAnimation(node,{"top":{start:top,end:top+(_69c.top||0)},"left":{start:left,end:left+(_69c.left||0)}},_69d,_69e).connect("beforeBegin",init);
if(_69f){
anim.connect("onEnd",function(){
_69f(_69b,anim);
});
}
_6a0.push(anim);
});
return dojo.lfx.combine(_6a0);
};
dojo.lfx.html.explode=function(_6aa,_6ab,_6ac,_6ad,_6ae){
var h=dojo.html;
_6aa=dojo.byId(_6aa);
_6ab=dojo.byId(_6ab);
var _6b0=h.toCoordinateObject(_6aa,true);
var _6b1=document.createElement("div");
h.copyStyle(_6b1,_6ab);
if(_6ab.explodeClassName){
_6b1.className=_6ab.explodeClassName;
}
with(_6b1.style){
position="absolute";
display="none";
var _6b2=h.getStyle(_6aa,"background-color");
backgroundColor=_6b2?_6b2.toLowerCase():"transparent";
backgroundColor=(backgroundColor=="transparent")?"rgb(221, 221, 221)":backgroundColor;
}
dojo.body().appendChild(_6b1);
with(_6ab.style){
visibility="hidden";
display="block";
}
var _6b3=h.toCoordinateObject(_6ab,true);
with(_6ab.style){
display="none";
visibility="visible";
}
var _6b4={opacity:{start:0.5,end:1}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_6b4[type]={start:_6b0[type],end:_6b3[type]};
});
var anim=new dojo.lfx.propertyAnimation(_6b1,_6b4,_6ac,_6ad,{"beforeBegin":function(){
h.setDisplay(_6b1,"block");
},"onEnd":function(){
h.setDisplay(_6ab,"block");
_6b1.parentNode.removeChild(_6b1);
}});
if(_6ae){
anim.connect("onEnd",function(){
_6ae(_6ab,anim);
});
}
return anim;
};
dojo.lfx.html.implode=function(_6b7,end,_6b9,_6ba,_6bb){
var h=dojo.html;
_6b7=dojo.byId(_6b7);
end=dojo.byId(end);
var _6bd=dojo.html.toCoordinateObject(_6b7,true);
var _6be=dojo.html.toCoordinateObject(end,true);
var _6bf=document.createElement("div");
dojo.html.copyStyle(_6bf,_6b7);
if(_6b7.explodeClassName){
_6bf.className=_6b7.explodeClassName;
}
dojo.html.setOpacity(_6bf,0.3);
with(_6bf.style){
position="absolute";
display="none";
backgroundColor=h.getStyle(_6b7,"background-color").toLowerCase();
}
dojo.body().appendChild(_6bf);
var _6c0={opacity:{start:1,end:0.5}};
dojo.lang.forEach(["height","width","top","left"],function(type){
_6c0[type]={start:_6bd[type],end:_6be[type]};
});
var anim=new dojo.lfx.propertyAnimation(_6bf,_6c0,_6b9,_6ba,{"beforeBegin":function(){
dojo.html.hide(_6b7);
dojo.html.show(_6bf);
},"onEnd":function(){
_6bf.parentNode.removeChild(_6bf);
}});
if(_6bb){
anim.connect("onEnd",function(){
_6bb(_6b7,anim);
});
}
return anim;
};
dojo.lfx.html.highlight=function(_6c3,_6c4,_6c5,_6c6,_6c7){
_6c3=dojo.lfx.html._byId(_6c3);
var _6c8=[];
dojo.lang.forEach(_6c3,function(node){
var _6ca=dojo.html.getBackgroundColor(node);
var bg=dojo.html.getStyle(node,"background-color").toLowerCase();
var _6cc=dojo.html.getStyle(node,"background-image");
var _6cd=(bg=="transparent"||bg=="rgba(0, 0, 0, 0)");
while(_6ca.length>3){
_6ca.pop();
}
var rgb=new dojo.gfx.color.Color(_6c4);
var _6cf=new dojo.gfx.color.Color(_6ca);
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:rgb,end:_6cf}},_6c5,_6c6,{"beforeBegin":function(){
if(_6cc){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+rgb.toRgb().join(",")+")";
},"onEnd":function(){
if(_6cc){
node.style.backgroundImage=_6cc;
}
if(_6cd){
node.style.backgroundColor="transparent";
}
if(_6c7){
_6c7(node,anim);
}
}});
_6c8.push(anim);
});
return dojo.lfx.combine(_6c8);
};
dojo.lfx.html.unhighlight=function(_6d1,_6d2,_6d3,_6d4,_6d5){
_6d1=dojo.lfx.html._byId(_6d1);
var _6d6=[];
dojo.lang.forEach(_6d1,function(node){
var _6d8=new dojo.gfx.color.Color(dojo.html.getBackgroundColor(node));
var rgb=new dojo.gfx.color.Color(_6d2);
var _6da=dojo.html.getStyle(node,"background-image");
var anim=dojo.lfx.propertyAnimation(node,{"background-color":{start:_6d8,end:rgb}},_6d3,_6d4,{"beforeBegin":function(){
if(_6da){
node.style.backgroundImage="none";
}
node.style.backgroundColor="rgb("+_6d8.toRgb().join(",")+")";
},"onEnd":function(){
if(_6d5){
_6d5(node,anim);
}
}});
_6d6.push(anim);
});
return dojo.lfx.combine(_6d6);
};
dojo.lang.mixin(dojo.lfx,dojo.lfx.html);
dojo.provide("dojo.lfx.*");
dojo.provide("dojo.lfx.toggle");
dojo.lfx.toggle.plain={show:function(node,_6dd,_6de,_6df){
dojo.html.show(node);
if(dojo.lang.isFunction(_6df)){
_6df();
}
},hide:function(node,_6e1,_6e2,_6e3){
dojo.html.hide(node);
if(dojo.lang.isFunction(_6e3)){
_6e3();
}
}};
dojo.lfx.toggle.fade={show:function(node,_6e5,_6e6,_6e7){
dojo.lfx.fadeShow(node,_6e5,_6e6,_6e7).play();
},hide:function(node,_6e9,_6ea,_6eb){
dojo.lfx.fadeHide(node,_6e9,_6ea,_6eb).play();
}};
dojo.lfx.toggle.wipe={show:function(node,_6ed,_6ee,_6ef){
dojo.lfx.wipeIn(node,_6ed,_6ee,_6ef).play();
},hide:function(node,_6f1,_6f2,_6f3){
dojo.lfx.wipeOut(node,_6f1,_6f2,_6f3).play();
}};
dojo.lfx.toggle.explode={show:function(node,_6f5,_6f6,_6f7,_6f8){
dojo.lfx.explode(_6f8||{x:0,y:0,width:0,height:0},node,_6f5,_6f6,_6f7).play();
},hide:function(node,_6fa,_6fb,_6fc,_6fd){
dojo.lfx.implode(node,_6fd||{x:0,y:0,width:0,height:0},_6fa,_6fb,_6fc).play();
}};
dojo.provide("dojo.widget.HtmlWidget");
dojo.declare("dojo.widget.HtmlWidget",dojo.widget.DomWidget,{templateCssPath:null,templatePath:null,lang:"",toggle:"plain",toggleDuration:150,initialize:function(args,frag){
},postMixInProperties:function(args,frag){
if(this.lang===""){
this.lang=null;
}
this.toggleObj=dojo.lfx.toggle[this.toggle.toLowerCase()]||dojo.lfx.toggle.plain;
},createNodesFromText:function(txt,wrap){
return dojo.html.createNodesFromText(txt,wrap);
},destroyRendering:function(_704){
try{
if(this.bgIframe){
this.bgIframe.remove();
delete this.bgIframe;
}
if(!_704&&this.domNode){
dojo.event.browser.clean(this.domNode);
}
dojo.widget.HtmlWidget.superclass.destroyRendering.call(this);
}
catch(e){
}
},isShowing:function(){
return dojo.html.isShowing(this.domNode);
},toggleShowing:function(){
if(this.isShowing()){
this.hide();
}else{
this.show();
}
},show:function(){
if(this.isShowing()){
return;
}
this.animationInProgress=true;
this.toggleObj.show(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onShow),this.explodeSrc);
},onShow:function(){
this.animationInProgress=false;
this.checkSize();
},hide:function(){
if(!this.isShowing()){
return;
}
this.animationInProgress=true;
this.toggleObj.hide(this.domNode,this.toggleDuration,null,dojo.lang.hitch(this,this.onHide),this.explodeSrc);
},onHide:function(){
this.animationInProgress=false;
},_isResized:function(w,h){
if(!this.isShowing()){
return false;
}
var wh=dojo.html.getMarginBox(this.domNode);
var _708=w||wh.width;
var _709=h||wh.height;
if(this.width==_708&&this.height==_709){
return false;
}
this.width=_708;
this.height=_709;
return true;
},checkSize:function(){
if(!this._isResized()){
return;
}
this.onResized();
},resizeTo:function(w,h){
dojo.html.setMarginBox(this.domNode,{width:w,height:h});
if(this.isShowing()){
this.onResized();
}
},resizeSoon:function(){
if(this.isShowing()){
dojo.lang.setTimeout(this,this.onResized,0);
}
},onResized:function(){
dojo.lang.forEach(this.children,function(_70c){
if(_70c.checkSize){
_70c.checkSize();
}
});
}});
dojo.provide("dojo.widget.*");
dojo.provide("dojo.string.common");
dojo.string.trim=function(str,wh){
if(!str.replace){
return str;
}
if(!str.length){
return str;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.repeat=function(str,_713,_714){
var out="";
for(var i=0;i<_713;i++){
out+=str;
if(_714&&i<_713-1){
out+=_714;
}
}
return out;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.provide("dojo.string");
dojo.provide("dojo.io.common");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_723,_724,_725){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_723){
this.mimetype=_723;
}
if(_724){
this.transport=_724;
}
if(arguments.length>=4){
this.changeUrl=_725;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,load:function(type,data,_728,_729){
},error:function(type,_72b,_72c,_72d){
},timeout:function(type,_72f,_730,_731){
},handle:function(type,data,_734,_735){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_736){
if(_736["url"]){
_736.url=_736.url.toString();
}
if(_736["formNode"]){
_736.formNode=dojo.byId(_736.formNode);
}
if(!_736["method"]&&_736["formNode"]&&_736["formNode"].method){
_736.method=_736["formNode"].method;
}
if(!_736["handle"]&&_736["handler"]){
_736.handle=_736.handler;
}
if(!_736["load"]&&_736["loaded"]){
_736.load=_736.loaded;
}
if(!_736["changeUrl"]&&_736["changeURL"]){
_736.changeUrl=_736.changeURL;
}
_736.encoding=dojo.lang.firstValued(_736["encoding"],djConfig["bindEncoding"],"");
_736.sendTransport=dojo.lang.firstValued(_736["sendTransport"],djConfig["ioSendTransport"],false);
var _737=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_736[fn]&&_737(_736[fn])){
continue;
}
if(_736["handle"]&&_737(_736["handle"])){
_736[fn]=_736.handle;
}
}
dojo.lang.mixin(this,_736);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_73e){
if(!(_73e instanceof dojo.io.Request)){
try{
_73e=new dojo.io.Request(_73e);
}
catch(e){
dojo.debug(e);
}
}
var _73f="";
if(_73e["transport"]){
_73f=_73e["transport"];
if(!this[_73f]){
dojo.io.sendBindError(_73e,"No dojo.io.bind() transport with name '"+_73e["transport"]+"'.");
return _73e;
}
if(!this[_73f].canHandle(_73e)){
dojo.io.sendBindError(_73e,"dojo.io.bind() transport with name '"+_73e["transport"]+"' cannot handle this type of request.");
return _73e;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_73e))){
_73f=tmp;
break;
}
}
if(_73f==""){
dojo.io.sendBindError(_73e,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");
return _73e;
}
}
this[_73f].bind(_73e);
_73e.bindSuccess=true;
return _73e;
};
dojo.io.sendBindError=function(_742,_743){
if((typeof _742.error=="function"||typeof _742.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){
var _744=new dojo.io.Error(_743);
setTimeout(function(){
_742[(typeof _742.error=="function")?"error":"handle"]("error",_744,null,_742);
},50);
}else{
dojo.raise(_743);
}
};
dojo.io.queueBind=function(_745){
if(!(_745 instanceof dojo.io.Request)){
try{
_745=new dojo.io.Request(_745);
}
catch(e){
dojo.debug(e);
}
}
var _746=_745.load;
_745.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_746.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _748=_745.error;
_745.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_748.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_745);
dojo.io._dispatchNextQueueBind();
return _745;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_74b,last){
var enc=/utf/i.test(_74b||"")?encodeURIComponent:dojo.string.encodeAscii;
var _74e=[];
var _74f=new Object();
for(var name in map){
var _751=function(elt){
var val=enc(name)+"="+enc(elt);
_74e[(last==name)?"push":"unshift"](val);
};
if(!_74f[name]){
var _754=map[name];
if(dojo.lang.isArray(_754)){
dojo.lang.forEach(_754,_751);
}else{
_751(_754);
}
}
}
return _74e.join("&");
};
dojo.io.setIFrameSrc=function(_755,src,_757){
try{
var r=dojo.render.html;
if(!_757){
if(r.safari){
_755.location=src;
}else{
frames[_755.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_755.contentWindow.document;
}else{
if(r.safari){
idoc=_755.document;
}else{
idoc=_755.contentWindow;
}
}
if(!idoc){
_755.location=src;
return;
}else{
idoc.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.string.extras");
dojo.string.substituteParams=function(_75a,hash){
var map=(typeof hash=="object")?hash:dojo.lang.toArray(arguments,1);
return _75a.replace(/\%\{(\w+)\}/g,function(_75d,key){
if(typeof (map[key])!="undefined"&&map[key]!=null){
return map[key];
}
dojo.raise("Substitution not found: "+key);
});
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
if(arguments.length==0){
str=this;
}
var _760=str.split(" ");
for(var i=0;i<_760.length;i++){
_760[i]=_760[i].charAt(0).toUpperCase()+_760[i].substring(1);
}
return _760.join(" ");
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _765=escape(str);
var _766,re=/%u([0-9A-F]{4})/i;
while((_766=_765.match(re))){
var num=Number("0x"+_766[1]);
var _769=escape("&#"+num+";");
ret+=_765.substring(0,_766.index)+_769;
_765=_765.substring(_766.index+_766[0].length);
}
ret+=_765.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=dojo.lang.toArray(arguments,1);
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_76e){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_76e){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str){
return str.replace(/\\/gm,"\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm,"\\$1");
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}
return str.substring(0,len).replace(/\.+$/,"")+"...";
};
dojo.string.endsWith=function(str,end,_777){
if(_777){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_77b,_77c){
if(_77c){
str=str.toLowerCase();
_77b=_77b.toLowerCase();
}
return str.indexOf(_77b)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_782){
if(_782=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_782=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_784){
var _785=[];
for(var i=0,_787=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_784){
_785.push(str.substring(_787,i));
_787=i+1;
}
}
_785.push(str.substr(_787));
return _785;
};
dojo.provide("dojo.undo.browser");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState=this._createState(this.initialHref,args,this.initialHash);
},addToHistory:function(args){
this.forwardStack=[];
var hash=null;
var url=null;
if(!this.historyIframe){
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
if(this.historyStack.length==0&&this.initialState.urlHash==hash){
this.initialState=this._createState(url,args,hash);
return;
}else{
if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){
this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);
return;
}
}
this.changingUrl=true;
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
url=this._loadIframeHistory();
var _78c=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_78e){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_78c.apply(this,[_78e]);
};
if(args["back"]){
args.back=tcb;
}else{
if(args["backButton"]){
args.backButton=tcb;
}else{
if(args["handle"]){
args.handle=tcb;
}
}
}
var _78f=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_791){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_78f){
_78f.apply(this,[_791]);
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}else{
if(args["handle"]){
args.handle=tfw;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}else{
url=this._loadIframeHistory();
}
this.historyStack.push(this._createState(url,args,hash));
},checkLocation:function(){
if(!this.changingUrl){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},iframeLoaded:function(evt,_794){
if(!dojo.render.html.opera){
var _795=this._getUrlQuery(_794.href);
if(_795==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_795==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_795==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _796=this.historyStack.pop();
if(!_796){
return;
}
var last=this.historyStack[this.historyStack.length-1];
if(!last&&this.historyStack.length==0){
last=this.initialState;
}
if(last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_796);
},handleForwardButton:function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
},_createState:function(url,args,hash){
return {"url":url,"kwArgs":args,"urlHash":hash};
},_getUrlQuery:function(url){
var _79d=url.split("?");
if(_79d.length<2){
return null;
}else{
return _79d[1];
}
},_loadIframeHistory:function(){
var url=dojo.hostenv.getBaseScriptUri()+"iframe_history.html?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
return url;
}};
dojo.provide("dojo.io.BrowserIO");
if(!dj_undef("window")){
dojo.io.checkChildrenForFile=function(node){
var _7a0=false;
var _7a1=node.getElementsByTagName("input");
dojo.lang.forEach(_7a1,function(_7a2){
if(_7a0){
return;
}
if(_7a2.getAttribute("type")=="file"){
_7a0=true;
}
});
return _7a0;
};
dojo.io.formHasFile=function(_7a3){
return dojo.io.checkChildrenForFile(_7a3);
};
dojo.io.updateNode=function(node,_7a5){
node=dojo.byId(node);
var args=_7a5;
if(dojo.lang.isString(_7a5)){
args={url:_7a5};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
dojo.dom.destroyNode(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);
};
dojo.io.encodeForm=function(_7ac,_7ad,_7ae){
if((!_7ac)||(!_7ac.tagName)||(!_7ac.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_7ae){
_7ae=dojo.io.formFilter;
}
var enc=/utf/i.test(_7ad||"")?encodeURIComponent:dojo.string.encodeAscii;
var _7b0=[];
for(var i=0;i<_7ac.elements.length;i++){
var elm=_7ac.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_7ae(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_7b0.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(["radio","checkbox"],type)){
if(elm.checked){
_7b0.push(name+"="+enc(elm.value));
}
}else{
_7b0.push(name+"="+enc(elm.value));
}
}
}
var _7b6=_7ac.getElementsByTagName("input");
for(var i=0;i<_7b6.length;i++){
var _7b7=_7b6[i];
if(_7b7.type.toLowerCase()=="image"&&_7b7.form==_7ac&&_7ae(_7b7)){
var name=enc(_7b7.name);
_7b0.push(name+"="+enc(_7b7.value));
_7b0.push(name+".x=0");
_7b0.push(name+".y=0");
}
}
return _7b0.join("&")+"&";
};
dojo.io.FormBind=function(args){
this.bindArgs={};
if(args&&args.formNode){
this.init(args);
}else{
if(args){
this.init({formNode:args});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){
var form=dojo.byId(args.formNode);
if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==form){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,args);
this.form=form;
this.connect(form,"onsubmit","submit");
for(var i=0;i<form.elements.length;i++){
var node=form.elements[i];
if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){
this.connect(node,"onclick","click");
}
}
var _7bd=form.getElementsByTagName("input");
for(var i=0;i<_7bd.length;i++){
var _7be=_7bd[i];
if(_7be.type.toLowerCase()=="image"&&_7be.form==form){
this.connect(_7be,"onclick","click");
}
}
},onSubmit:function(form){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var node=e.currentTarget;
if(node.disabled){
return;
}
this.clickedButton=node;
},formFilter:function(node){
var type=(node.type||"").toLowerCase();
var _7c5=false;
if(node.disabled||!node.name){
_7c5=false;
}else{
if(dojo.lang.inArray(["submit","button","image"],type)){
if(!this.clickedButton){
this.clickedButton=node;
}
_7c5=node==this.clickedButton;
}else{
_7c5=!dojo.lang.inArray(["file","submit","reset","button"],type);
}
}
return _7c5;
},connect:function(_7c6,_7c7,_7c8){
if(dojo.evalObjPath("dojo.event.connect")){
dojo.event.connect(_7c6,_7c7,this,_7c8);
}else{
var fcn=dojo.lang.hitch(this,_7c8);
_7c6[_7c7]=function(e){
if(!e){
e=window.event;
}
if(!e.currentTarget){
e.currentTarget=e.srcElement;
}
if(!e.preventDefault){
e.preventDefault=function(){
window.event.returnValue=false;
};
}
fcn(e);
};
}
}});
dojo.io.XMLHTTPTransport=new function(){
var _7cb=this;
var _7cc={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_7ce,_7cf){
return url+"|"+_7ce+"|"+_7cf.toLowerCase();
}
function addToCache(url,_7d1,_7d2,http){
_7cc[getCacheKey(url,_7d1,_7d2)]=http;
}
function getFromCache(url,_7d5,_7d6){
return _7cc[getCacheKey(url,_7d5,_7d6)];
}
this.clearCache=function(){
_7cc={};
};
function doLoad(_7d7,http,url,_7da,_7db){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_7d7.method.toLowerCase()=="head"){
var _7dd=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _7dd;
};
var _7de=_7dd.split(/[\r\n]+/g);
for(var i=0;i<_7de.length;i++){
var pair=_7de[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_7d7.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_7d7.mimetype=="text/json"||_7d7.mimetype=="application/json"){
try{
ret=dj_eval("("+http.responseText+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_7d7.mimetype=="application/xml")||(_7d7.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_7db){
addToCache(url,_7da,_7d7.method,http);
}
_7d7[(typeof _7d7.load=="function")?"load":"handle"]("load",ret,http,_7d7);
}else{
var _7e1=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_7d7[(typeof _7d7.error=="function")?"error":"handle"]("error",_7e1,http,_7d7);
}
}
function setHeaders(http,_7e3){
if(_7e3["headers"]){
for(var _7e4 in _7e3["headers"]){
if(_7e4.toLowerCase()=="content-type"&&!_7e3["contentType"]){
_7e3["contentType"]=_7e3["headers"][_7e4];
}else{
http.setRequestHeader(_7e4,_7e3["headers"][_7e4]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
var now=null;
if(!dojo.hostenv._blockAsync&&!_7cb._blockAsync){
for(var x=this.inFlight.length-1;x>=0;x--){
try{
var tif=this.inFlight[x];
if(!tif||tif.http._aborted||!tif.http.readyState){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
}else{
if(tif.startTime){
if(!now){
now=(new Date()).getTime();
}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){
if(typeof tif.http.abort=="function"){
tif.http.abort();
}
this.inFlight.splice(x,1);
tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);
}
}
}
}
catch(e){
try{
var _7e8=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);
tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_7e8,tif.http,tif.req);
}
catch(e2){
dojo.debug("XMLHttpTransport error callback failed: "+e2);
}
}
}
}
clearTimeout(this.inFlightTimer);
if(this.inFlight.length==0){
this.inFlightTimer=null;
return;
}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
};
var _7e9=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_7ea){
return _7e9&&dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript","text/json","application/json"],(_7ea["mimetype"].toLowerCase()||""))&&!(_7ea["formNode"]&&dojo.io.formHasFile(_7ea["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_7eb){
if(!_7eb["url"]){
if(!_7eb["formNode"]&&(_7eb["backButton"]||_7eb["back"]||_7eb["changeUrl"]||_7eb["watchForURL"])&&(!djConfig.preventBackButtonFix)){
dojo.deprecated("Using dojo.io.XMLHTTPTransport.bind() to add to browser history without doing an IO request","Use dojo.undo.browser.addToHistory() instead.","0.4");
dojo.undo.browser.addToHistory(_7eb);
return true;
}
}
var url=_7eb.url;
var _7ed="";
if(_7eb["formNode"]){
var ta=_7eb.formNode.getAttribute("action");
if((ta)&&(!_7eb["url"])){
url=ta;
}
var tp=_7eb.formNode.getAttribute("method");
if((tp)&&(!_7eb["method"])){
_7eb.method=tp;
}
_7ed+=dojo.io.encodeForm(_7eb.formNode,_7eb.encoding,_7eb["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_7eb["file"]){
_7eb.method="post";
}
if(!_7eb["method"]){
_7eb.method="get";
}
if(_7eb.method.toLowerCase()=="get"){
_7eb.multipart=false;
}else{
if(_7eb["file"]){
_7eb.multipart=true;
}else{
if(!_7eb["multipart"]){
_7eb.multipart=false;
}
}
}
if(_7eb["backButton"]||_7eb["back"]||_7eb["changeUrl"]){
dojo.undo.browser.addToHistory(_7eb);
}
var _7f0=_7eb["content"]||{};
if(_7eb.sendTransport){
_7f0["dojo.transport"]="xmlhttp";
}
do{
if(_7eb.postContent){
_7ed=_7eb.postContent;
break;
}
if(_7f0){
_7ed+=dojo.io.argsFromMap(_7f0,_7eb.encoding);
}
if(_7eb.method.toLowerCase()=="get"||!_7eb.multipart){
break;
}
var t=[];
if(_7ed.length){
var q=_7ed.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_7eb.file){
if(dojo.lang.isArray(_7eb.file)){
for(var i=0;i<_7eb.file.length;++i){
var o=_7eb.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_7eb.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_7ed=t.join("\r\n");
}
}while(false);
var _7f6=_7eb["sync"]?false:true;
var _7f7=_7eb["preventCache"]||(this.preventCache==true&&_7eb["preventCache"]!=false);
var _7f8=_7eb["useCache"]==true||(this.useCache==true&&_7eb["useCache"]!=false);
if(!_7f7&&_7f8){
var _7f9=getFromCache(url,_7ed,_7eb.method);
if(_7f9){
doLoad(_7eb,_7f9,url,_7ed,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_7eb);
var _7fb=false;
if(_7f6){
var _7fc=this.inFlight.push({"req":_7eb,"http":http,"url":url,"query":_7ed,"useCache":_7f8,"startTime":_7eb.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}else{
_7cb._blockAsync=true;
}
if(_7eb.method.toLowerCase()=="post"){
if(!_7eb.user){
http.open("POST",url,_7f6);
}else{
http.open("POST",url,_7f6,_7eb.user,_7eb.password);
}
setHeaders(http,_7eb);
http.setRequestHeader("Content-Type",_7eb.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_7eb.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_7ed);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_7eb,{status:404},url,_7ed,_7f8);
}
}else{
var _7fd=url;
if(_7ed!=""){
_7fd+=(_7fd.indexOf("?")>-1?"&":"?")+_7ed;
}
if(_7f7){
_7fd+=(dojo.string.endsWithAny(_7fd,"?","&")?"":(_7fd.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
if(!_7eb.user){
http.open(_7eb.method.toUpperCase(),_7fd,_7f6);
}else{
http.open(_7eb.method.toUpperCase(),_7fd,_7f6,_7eb.user,_7eb.password);
}
setHeaders(http,_7eb);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_7eb,{status:404},url,_7ed,_7f8);
}
}
if(!_7f6){
doLoad(_7eb,http,url,_7ed,_7f8);
_7cb._blockAsync=false;
}
_7eb.abort=function(){
try{
http._aborted=true;
}
catch(e){
}
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
}
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_7ff,days,path,_802,_803){
var _804=-1;
if((typeof days=="number")&&(days>=0)){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_804=d.toGMTString();
}
_7ff=escape(_7ff);
document.cookie=name+"="+_7ff+";"+(_804!=-1?" expires="+_804+";":"")+(path?"path="+path:"")+(_802?"; domain="+_802:"")+(_803?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _808=document.cookie.substring(idx+name.length+1);
var end=_808.indexOf(";");
if(end==-1){
end=_808.length;
}
_808=_808.substring(0,end);
_808=unescape(_808);
return _808;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_80f,_810,_811){
if(arguments.length==5){
_811=_80f;
_80f=null;
_810=null;
}
var _812=[],_813,_814="";
if(!_811){
_813=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!_813){
_813={};
}
for(var prop in obj){
if(obj[prop]==null){
delete _813[prop];
}else{
if((typeof obj[prop]=="string")||(typeof obj[prop]=="number")){
_813[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in _813){
_812.push(escape(prop)+"="+escape(_813[prop]));
}
_814=_812.join("&");
}
dojo.io.cookie.setCookie(name,_814,days,path,_80f,_810);
};
dojo.io.cookie.getObjectCookie=function(name){
var _817=null,_818=dojo.io.cookie.getCookie(name);
if(_818){
_817={};
var _819=_818.split("&");
for(var i=0;i<_819.length;i++){
var pair=_819[i].split("=");
var _81c=pair[1];
if(isNaN(_81c)){
_81c=unescape(pair[1]);
}
_817[unescape(pair[0])]=_81c;
}
}
return _817;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _81d=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_81d=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.provide("dojo.io.*");

