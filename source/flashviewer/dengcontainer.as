var footerheight = 35;
var DENG_SWF_URL = unescape(baseurl) + "deng.swf";
//var DOCUMENT_URL = "testsurvey.html";
//var DOCUMENT_URL = unescape(urlofsurvey);
//var DOCUMENT_URL = unescape(baseurl) + "f?s="+s+"&u="+u+"&ispreview="+ispreview;
//var DOCUMENT_URL = "http://localhost/f?s=1&u=1&ispreview=1"
//This XmlSource is loaded from FlashVars
//var SURVEY_AS_HTML = unescape(surveyashtml);
//SURVEY_AS_HTML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><title>dNeero Survey</title></head><body><p><p style=\"font-family: Arial Black, Arial Black, Gadget, sans-serif; font-size: 12px; font-weight: bold;\">Why?</p><p style=\"font-family: Arial, Arial, Helvetica, sans-serif; font-size: 12px;\">Not answered.</p></p></body></html>";
//SURVEY_AS_HTML = unescape("%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%3C%21DOCTYPE+html+PUBLIC+%22-%2F%2FW3C%2F%2FDTD+XHTML+1.1%2F%2FEN%22+%22http%3A%2F%2Fwww.w3.org%2FTR%2Fxhtml11%2FDTD%2Fxhtml11.dtd%22%3E%3Chtml+xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F1999%2Fxhtml%22+xml%3Alang%3D%22en%22+lang%3D%22en%22%3E%3Chead%3E%3Ctitle%3EdNeero+Survey%3C%2Ftitle%3E%3C%2Fhead%3E%3Cbody%3E%3Cp%3E%3Cp+style%3D%22font-family%3A+Arial+Black%2C+Arial+Black%2C+Gadget%2C+sans-serif%3B+font-size%3A+12px%3B+font-weight%3A+bold%3B%22%3EWhy%3F%3C%2Fp%3E%3Cp+style%3D%22font-family%3A+Arial%2C+Arial%2C+Helvetica%2C+sans-serif%3B+font-size%3A+12px%3B%22%3E%3Cp%3ENot+answered+dude.%3C%2Fp%3E%3C%2Fp%3E%3C%2Fp%3E%3C%2Fbody%3E%3C%2Fhtml%3E");


var done = false;

Stage.align = "TL";
Stage.scaleMode = "noScale";
Stage.addListener(this);

//this.displayStatus("1 DENG_SWF_URL="+DENG_SWF_URL);
this.createEmptyMovieClip("dengcontainer", 1);
this.dengcontainer.loadMovie(DENG_SWF_URL);
this.onEnterFrame = this.checkLoadProgress;
//this.displayStatus("2 DENG_SWF_URL="+DENG_SWF_URL + " SURVEY_AS_HTML="+SURVEY_AS_HTML);

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
			//this.displayStatus(percent + "% -- 3 DENG_SWF_URL="+DENG_SWF_URL);
		}
	}
}

function initDeng() {
    //this.displayStatus("4 DENG_SWF_URL="+DENG_SWF_URL);
	var deng = this.dengcontainer.deng_mc;
	deng.addListener(this);
	//deng.setDocumentUri(DOCUMENT_URL);
	SURVEY_AS_HTML = unescape(SURVEY_AS_HTML);
	if (SURVEY_AS_HTML=="" || SURVEY_AS_HTML==undefined){
		SURVEY_AS_HTML = "This is a blank survey."
	}
	deng.setXmlSource(SURVEY_AS_HTML);
	deng.setSize(Stage.width, Stage.height-footerheight);
	deng.render();
	//this.displayStatus("Loading Survey "+ DOCUMENT_URL);
	//this.displayStatus("5 DENG_SWF_URL="+DENG_SWF_URL);
}

function onLoadXML(success, status) {
	//this.displayStatus("");
	this.onResize();
}

function onParseCSS() {
	//this.displayStatus("");
}

function onCreate() {
	//this.displayStatus("");
}

function onSize() {
	//this.displayStatus("");
}

function onPosition() {
	//this.displayStatus("");
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
			this.createTextField("tfDebug", 50000, 0, 0, Stage.width, 200);
			this.tfDebug.selectable = false;
			this.tfDebug.multiline = true;
			this.tfDebug.wordWrap = true;
			this.tfDebug.font = "_sans";
			this.tfDebug.text = "";
		}
		this.tfDebug.text = txt;
	}
}
