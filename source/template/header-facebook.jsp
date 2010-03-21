<%@ page import="com.dneero.util.Io" %>
<%@ page import="com.dneero.systemprops.WebAppRootDir" %>
<!--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"-->
          <!--"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">-->
<!--<html>-->
<!--<head>-->
  <!--<title>dNeero Social Surveys</title>-->
  <style type="text/css"><%=Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"/css/dneero-facebook.css").toString()%></style>
  <!--<link rel="stylesheet" type="text/css" href="/css/dneero-facebook.css" />-->
  <!--<style type="text/css"><%//=Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"/js/niftycube/niftyCorners.css").toString()%></style>-->
  <!--<link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css" />-->
  <!--<script type="text/javascript" src="/js/niftycube/niftycube.js"></script>-->
  <!--<script type="text/javascript">
   NiftyLoad=function(){
       Nifty("div.rounded","big");
   }
  </script>-->
<!--</head>-->
<!--<body>-->
          

    <table width="100%" cellspacing="0" border="0" cellpadding="0">
        <tr>
            <td bgcolor="#dadada" style="text-align: left; vertical-align: bottom; border-bottom: 1px solid #666666;" colspan="5" height="45">
                <div width="100%" style="background: #cccccc">
                    <a href="/publicsurveylist.jsp"><font class="subnavfont" style="padding-left: 6px; padding-right: 6px; color: #000000; font-size: 12px; background: #00ff00;">Conversations for You</font></a>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%><a href="/blogger/bloggercompletedsurveys.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Completed</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%><a href="/blogger/bloggerdetails.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Profile</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/account/accountbalance.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Balance</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/account/accountsettings.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Settings</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/account/awards.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Awards</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/blogger/bloggerearningsrevshare.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000; background: #cccccc;">Referrals</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/account/reseller.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Reseller</font></a><%}%>
                    <a href="/blog.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Blog</font></a>
                    <a href="/blogger/facebookfaq.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">FAQ</font></a>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/account/inbox.jsp"><font class="subnavfont" style="padding-left: 5px; color: #000000; background: #cccccc;">Inbox</font></a><%}%>
                </div>
            </td>
        </tr>
        <!--<tr>
            <td  bgcolor="#666666" colspan="5"><img src="/images/clear.gif" width="1" height="1"/></td>
        </tr>-->
        <%if (Pagez.getUserSession().getIsloggedin()){%>
            <tr>
                <td bgcolor="#ffffff" style="text-align: right; vertical-align: top;" colspan="5" height="15">
                    <div style="padding: 0px; text-align: right;">
                        <font class="subnavfont">Hi, <%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>! <%if (Pagez.getUserSession().getUser().getPaymethodpaypaladdress().equals("")){%><img src="/images/clear.gif" width="5" height="1"/>We <a href="/account/accountsettings.jsp">need</a> a PayPal address before we can pay you.<%}%><img src="/images/clear.gif" width="15" height="1"/></font>
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
                 <%
                 if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
                    %>
                    <br/>
                    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
                    <br/><br/>
                    <%
                    //Clear the message since it's been displayed
                    Pagez.getUserSession().setMessage("");
                 }
                 %>
                 <!-- Begin Body -->

