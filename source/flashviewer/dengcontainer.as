var footerheight = 35;
var dneeroDengWidth = 415;
var dneeroDengHeight = 203;
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
			this.displayStatus("Loading: " + percent + "% " + DOCUMENT_URL);
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
	//SURVEY_AS_HTML = "<p>topk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f mtopk jh;kjh;kj hlkj hlkjghk gfjhgdfjhgfk gfnghfc ngmf gmffm ff mf f m</p>";
	deng.setXmlSource(SURVEY_AS_HTML);
	deng.setSize(dneeroDengWidth, dneeroDengHeight);
	deng._x=5;
	deng._y=10;
	deng.render();
	//this.displayStatus("5 DENG_SWF_URL="+DENG_SWF_URL);
}

function onLoadXML(success, status) {
	
	var _err = "";
	switch(status) {
		case 0: _err = ""; break;
		case -2: _err = "A CDATA section was not properly terminated."; break;
		case -3: _err = "The XML declaration was not properly terminated."; break;
		case -4: _err = "The DOCTYPE declaration was not properly terminated."; break;
		case -5: _err = "A comment was not properly terminated."; break;
		case -6: _err = "An XML element was malformed."; break;
		case -7: _err = "Out of memory."; break;
		case -8: _err = "An attribute value was not properly terminated."; break;
		case -9: _err = "A start-tag was not matched with an end-tag."; break;
		case -10: _err = "An end-tag was encountered without a matching start-tag."; break;
		case -100: _err = "Rendering in progress..."; break;
		default: _err = "An unknown error occured (" + status + ")"; break;
	}
	if (_err!=""){
		this.displayStatus("We're Sorry!\n\nFailure to Render the Survey:\n" + _err + "\n\nHTML:\n"+SURVEY_AS_HTML);
		//deng.setXmlSource("<p>We're Sorry!!!\n\nThere was a Failure to Render the Survey:\n" + _err + "\n\nHTML:\n<pre>"+SURVEY_AS_HTML+"<pre></p>");
		//deng.render();
	}
	this.onResize();
}

function onParseCSS() {
	//this.displayStatus("onParseCSS");
}

function onCreate() {
	//this.displayStatus("onCreate");
}

function onSize() {
	//this.displayStatus("onSize");
}

function onPosition() {
	//this.displayStatus("onPosition");
}

function onRender() {
    //this.displayStatus("onRender");
	this.displayStatus();
	done = true;
}

function onClickHandler(linkUrl, targetStyle, targetPosition, targetName) {
	getURL(linkUrl);
}

function onResize() {
	this.dengcontainer.deng_mc.setSize(dneeroDengWidth, dneeroDengHeight);
	this.tfDebug._width = dneeroDengWidth;
}

function displayStatus(txt) {
	if(txt == undefined) {
		this.tfDebug.removeTextField();
	} else if(!done) {
		if(this.tfDebug == undefined) {
			this.createTextField("tfDebug", 50000, 0, 0, dneeroDengWidth, dneeroDengHeight);
			this.tfDebug.selectable = true;
			this.tfDebug.multiline = true;
			this.tfDebug.wordWrap = true;
			this.tfDebug.font = "_sans";
			this.tfDebug.text = "";
		}
		this.tfDebug.text = txt;
	}
}
