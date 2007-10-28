<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1" />
  <title>dNeero Social Surveys</title>
  <link rel="stylesheet" type="text/css" href="/css/basic.css" />
  <link rel="stylesheet" type="text/css" href="/css/dneero-facebook.css" />
  <link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css" />
  <link rel="alternate" type="application/rss+xml" title="dNeero Social Surveys Blog" href="http://www.dneero.com/rss.xml" />
  <meta name="description" content="dNeero connects advertisers and market researchers with a large pool of qualified and motivated bloggers. Marketers initiate offers to blog (i.e. We'll pay $2 for a blogged review of our latest movie.), dNeero publishes those offers in a Yahoo-style directory and bloggers earn money (for themselves or for charity) taking surveys and posting their own results to their blogs. " />
  <meta name="keywords" content="market research,survey,blog,web 2.0,startup,money,market intelligence" />
  <script type="text/javascript">
        <%if (Pagez.getUserSession().getIsfacebookui()){%>
        var userSessionIsFacebookui = true;
        <%}%>
        <%if (!Pagez.getUserSession().getIsfacebookui()){%>
        var userSessionIsFacebookui = false;
        <%}%>
        if (userSessionIsFacebookui) {
            if (window==top){
                top.location.href="#{baseUrl.includinghttp}facebookexitui.jsf";
            }
        } else {
            if (window!=top){
                //alert("Your session may have timed out.  If you're using the Facebook App please click your left-hand menu link to restart your session.  Thanks.");
                top.location.href="#{baseUrl.includinghttp}facebookenterui.jsf";
            }
        }
  </script>
  <script type="text/javascript" src="/js/niftycube/niftycube.js"></script>
  <script type="text/javascript">
   NiftyLoad=function(){
       Nifty("div.rounded","big");
   }
  </script>
</head>
<body>
          

    <table width="100%" cellspacing="0" border="0" cellpadding="0">
        <tr>
            <td bgcolor="#dadada" style="text-align: left; vertical-align: middle;" colspan="5" height="25">
                <a href="/jsp/publicsurveylist.jsp"><font class="subnavfont" style="padding-left: 12px; padding-right: 12px; padding-top: 3px; padding-bottom: 3px; color: #000000; font-size: 12px; background: #00ff00;">Surveys You Can Take</font></a>
                <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%><a href="/jsp/blogger/bloggercompletedsurveys.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Completed Surveys</font></a><%}%>
                <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%><a href="/jsp/blogger/bloggerdetails.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Demographic</font></a><%}%>
                <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/jsp/account/accountbalance.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Balance</font></a><%}%>
                <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/jsp/account/accountsettings.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Account Settings</font></a><%}%>
                <a href="/jsp/blog.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Blog</font></a>
                <a href="/jsp/blogger/facebookfaq.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">FAQ</font></a>
                <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/jsp/account/accountsupportissueslist.jsp"><font class="subnavfont" style="padding-left: 12px; color: #000000;">Help</font></a><%}%>
            </td>
        </tr>
        <tr>
            <td  bgcolor="#666666" colspan="5"><img src="/images/clear.gif" width="1" height="1"/></td>
        </tr>
        <%if (Pagez.getUserSession().getIsloggedin()){%>
            <tr>
                <td bgcolor="#ffffff" style="text-align: right; vertical-align: top;" colspan="5" height="15">
                    <div style="padding: 0px; text-align: right;">
                        <font class="subnavfont">Hi, <%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>! <%if (Pagez.getUserSession().getUser().getPaymethodpaypaladdress().equals("")){%><img src="/images/clear.gif" width="5" height="1"/>We <a href="/jsp/account/accountsettings.jsp">need</a> a PayPal address before we can pay you.<%}%><img src="/images/clear.gif" width="5" height="1"/></font>
                    </div>
                </td>
            </tr>
        <%}%>

    </table>
    <br/>

    <table width="624" cellspacing="0" border="0" cellpadding="5">
        <tr>
            <td valign="top">
                <table width="100%" cellspacing="0" border="0" cellpadding="1">
                    <tr>
                        <td valign="top">
                            <font class="pagetitlefont"><%=pagetitle%></font>
                        </td>
                        <td valign="top" width="100">
                             <img src="/images/dneero-logo-small.gif" width="100" height="46" border="0"/>
                        </td>
                    </tr>
                 </table>
                 <!-- Begin Body -->

