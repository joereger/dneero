var DENG_SWF_URL = "deng.swf";
//var DOCUMENT_URL = "testsurvey.html";
//var DOCUMENT_URL = unescape(urlofsurvey);
//var DOCUMENT_URL = unescape(baseurl) + "f?s="+s+"&u="+u+"&ispreview="+ispreview;
var DOCUMENT_URL = "http://localhost/f?s=1&u=1&ispreview=1"
//This XmlSource is loaded from FlashVars
var SURVEY_AS_HTML = unescape(surveyashtml);
var footerheight = 35;

var done = false;

Stage.align = "TL";
Stage.scaleMode = "noScale";
Stage.addListener(this);

var host:String = "";
if (ExternalInterface.available) {
   host = String(ExternalInterface.call("eval", "window.location.host"));
}

this.createEmptyMovieClip("dengcontainer", 1);
this.dengcontainer.loadMovie(DENG_SWF_URL);
this.onEnterFrame = this.checkLoadProgress;
//this.displayStatus("Loading Survey: " + DOCUMENT_URL + " on " + host);
this.displayStatus("");



stop();

function checkLoadProgress() {
	var bl = this.dengcontainer.getBytesLoaded();
	var bt = this.dengcontainer.getBytesTotal();
	if(bl > 0 && bt > 0) {
		if(bl == bt) {
			delete this.onEnterFrame;
			this.initDeng();
		} else {
			var percent = Math.round(100 * bl / bt);
			//this.displayStatus("Loading Survey: " + percent + "% " + DOCUMENT_URL + " on " + host);
			this.displayStatus("");
		}
	}
}

function initDeng() {
	var deng = this.dengcontainer.deng_mc;
	deng.addListener(this);
	//deng.setDocumentUri(DOCUMENT_URL);
	if (SURVEY_AS_HTML=="" || SURVEY_AS_HTML==undefined){
		SURVEY_AS_HTML = "This is a blank survey."
	}
	deng.setXmlSource(SURVEY_AS_HTML);
	deng.setSize(Stage.width, Stage.height-footerheight);
	deng.render();
	//this.displayStatus("Loading Survey "+ DOCUMENT_URL);
	this.displayStatus("");
}

function onLoadXML(success, status) {
	this.displayStatus("");
	this.onResize();
}

function onParseCSS() {
	this.displayStatus("");
}

function onCreate() {
	this.displayStatus("");
}

function onSize() {
	this.displayStatus("");
}

function onPosition() {
	this.displayStatus("");
}

function onRender() {
	this.displayStatus();
	done = true;
}

function onClickHandler(linkUrl, targetStyle, targetPosition, targetName) {
	getURL(linkUrl);
}

function onResize() {
	this.dengcontainer.deng_mc.setSize(Stage.width, Stage.height-footerheight);
	this.tfDebug._width = Stage.width;
}

function displayStatus(txt) {
	if(txt == undefined) {
		this.tfDebug.removeTextField();
	} else if(!done) {
		if(this.tfDebug == undefined) {
			this.createTextField("tfDebug", 50000, 0, 0, Stage.width, 20);
			this.tfDebug.selectable = false;
			this.tfDebug.multiline = false;
			this.tfDebug.wordWrap = false;
			this.tfDebug.font = "_sans";
			this.tfDebug.text = "";
		}
		this.tfDebug.text = txt;
	}
}
