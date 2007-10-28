<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<head>
  <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1" />
  <title>dNeero.com</title>
  <link rel="stylesheet" type="text/css" href="/css/basic.css" />
  <link rel="stylesheet" type="text/css" href="/css/dneero.css" />
  <link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css" />
  <script type="text/javascript" src="/js/niftycube/niftycube.js"></script>
  <script type="text/javascript">
    NiftyLoad=function(){
        Nifty("div.rounded","big");
    }
  </script>
</head>

<body style="background-image: none;">


         <h:form id="lostpasswordform">

            <center>
            <div style="width: 650px; text-align: left;">
                <img src="/images/dneero-logo.gif" width="200" height="94"/>
                <br/><br/>
                <font class="pagetitlefont">Welcome to the dNeero Closed Beta!</font>
                <br/><br/>
                <div class="rounded">
                <font class="mediumfont">On the next screen you'll jump in as if you were visiting dNeero.com for the first time, post-closed beta.  But before we set you free, a few tips:</font>
                </div>
                <br/><br/>

                <table width="100%" cellpadding="5">
                <tr>
                <td valign="top">
                    <font class="smallfont" style="font-size: 13px;">
                    <ul>
                        <li><b>Click "Stay Logged In"</b>  -
                        After you activate your account and log in, check the "Stay Logged In" box.  It'll help you over some issues like email activation.
                        <br/><br/></li>

                        <li><b>Top Secret Stuff Here</b>  -
                        We probably don't even have to mention it, but everything you see is secret and shouldn't be shared with anybody.  Don't give out passwords, etc.  We're in our "dark" phase.  Thanks.
                        <br/><br/></li>

                        <li><b>It's Not You... It's the System</b>  -
                        If anything doesn't make sense, seems too difficult or appears just plain wacky... it's not you... it's the system.  If things are complicated, we're not doing our job well.  Tell us where we need to improve.
                        <br/><br/></li>
                    </ul>

                    </font>

                    <br/><br/>

                    <font class="smallfont" style="font-size: 13px;"><b>And now, imagine</b> that you're clicking on a link to dNeero.com for the very first time... you won't be logged-in to the actual tool yet so you'll have to learn about it and then sign up to create an account just as you will when the closed beta turns into a public beta.  Thanks for your help!</font>
                    <br/><br/>
                    <h:commandButton action="home" value="Enter dNeero.com" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                <img src="/images/beta-expectation.jpg"></img>     
                </td>
                </tr>
                </table>



                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

            </div>
            </center>

        </h:form>

</body>

</html>
