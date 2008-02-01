<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.htmluibeans.BloggerIndex" %>
<%@ page import="com.dneero.htmluibeans.PublicIndex" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.cachedstuff.RecentSurveyResponses" %>
<%@ page import="com.dneero.cachedstuff.GetCachedStuff" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="com.dneero.cachedstuff.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicIndex publicIndex = (PublicIndex)Pagez.getBeanMgr().get("PublicIndex");
%>
<%
    if (request.getParameter("accesscode")!=null && !request.getParameter("accesscode").equals("")) {
        Pagez.getUserSession().setAccesscode(request.getParameter("accesscode"));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("enteraccesscode")) {
        try {
            publicIndex.enterAccessCode();
            Pagez.getUserSession().setMessage("Sorry, no surveys were found for that Access Code.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>




<script language="JavaScript">
<!--
if (document.images) {
 body1 = new Image;
 body1.src = "images/hpv2-default-body.gif";
 body2 = new Image;
 body2.src = "images/hpv2-socialpeople-body.gif";
 body3 = new Image;
 body3.src = "images/hpv2-researchers-body.gif";
 spbutton1 = new Image;
 spbutton1.src = "images/hpv2-socialpeople-button-off.gif";
 spbutton2 = new Image;
 spbutton2.src = "images/hpv2-socialpeople-button-on.gif";
 rbutton1 = new Image;
 rbutton1.src = "images/hpv2-researchers-button-off.gif";
 rbutton2 = new Image;
 rbutton2.src = "images/hpv2-researchers-button-on.gif";
}
function bodyImgAct(imgname){
 if (document.images){
  window.onerror = null;
  document.bodyimg.src=eval(imgname + ".src");
 }
}
function bodyImgInact(){
 if (document.images){
  window.onerror = null;
  document.bodyimg.src=body1.src;
 }
}
function spButtonImgAct(imgname){
 if (document.images){
  window.onerror = null;
  document.spbutton.src=eval(imgname + ".src");
 }
}
function spButtonImgInact(){
 if (document.images){
  window.onerror = null;
  document.spbutton.src=spbutton1.src;
 }
}
function rButtonImgAct(imgname){
 if (document.images){
  window.onerror = null;
  document.rbutton.src=eval(imgname + ".src");
 }
}
function rButtonImgInact(){
 if (document.images){
  window.onerror = null;
  document.rbutton.src=rbutton1.src;
 }
}
//-->
</script>

<table cellspacing="0" cellpadding="0" border="0" width="757">
<tr>
    <td><a href="/blogger/index.jsp?showmarketingmaterial=1" onmouseover="bodyImgAct('body2'), spButtonImgAct('spbutton2')" onmouseout="bodyImgInact(), spButtonImgInact()"><img src="images/hpv2-socialpeople-button-off.gif" width="227" height="121" border="0" name="spbutton"></a></td>
    <td rowspan="2"><img src="images/hpv2-default-body.gif" width="530" height="241" name="bodyimg"></td>
</tr>
<tr>
    <td><a href="/researcher/index.jsp?showmarketingmaterial=1" onmouseover="bodyImgAct('body3'), rButtonImgAct('rbutton2')" onmouseout="bodyImgInact(), rButtonImgInact()"><img src="images/hpv2-researchers-button-off.gif" width="227" height="120" border="0" name="rbutton"></a></td>
</tr>
</table>

<br clear="all"/><br/>

    <table cellpadding="0" cellspacing="10" border="0" width="757">

       <tr>
           <td width="225">


               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <%TotalSurveysTaken tst = (TotalSurveysTaken) GetCachedStuff.get(new TotalSurveysTaken());%>
                    <font class="mediumfont" style="color: #999999;">Surveys Taken</font><br/>
                    <font class="largefont" style="color: #666666;"><%=tst.getHtml()%></font>
               </div>

               <br/>

               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <% TotalImpressions ti = (TotalImpressions) GetCachedStuff.get(new TotalImpressions());%>
                    <font class="mediumfont" style="color: #999999;">Survey Embeds</font><br/>
                    <font class="largefont" style="color: #666666;"><%=ti.getHtml()%></font><br/>
                    <font class="tinyfont">Times a survey's been displayed while embedded in a blog or social media profile.</font><br/>
               </div>

               <br/>

               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                   <% MostActiveUsersByTotalSurveysTaken maubtst = (MostActiveUsersByTotalSurveysTaken) GetCachedStuff.get(new MostActiveUsersByTotalSurveysTaken());%>
                   <font class="mediumfont" style="color: #999999;">Are You Experienced?</font><br/>
                   <%=maubtst.getHtml()%>
               </div>

               <br/>

               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <form action="/index.jsp" method="post">
                        <input type="hidden" name="action" value="enteraccesscode">
                        <font class="mediumfont" style="color: #999999;">Got an Access Code?</font><br/>
                        <%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                        <input type="submit" class="formsubmitbutton" value="Use Access Code" style="font-size: 11px;">
                    </form>
                </div>

               <br/>

               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <% DonationsToCharityMiniReport dtcmr = (DonationsToCharityMiniReport) GetCachedStuff.get(new DonationsToCharityMiniReport());%>
                    <font class="mediumfont" style="color: #999999;">Donations to Charity</font><br/>
                    <%=dtcmr.getHtml()%>
                    <br/>
                   <div class="rounded" style="background: #ffffff; padding: 10px;">
                        <font class="tinyfont">dNeero allows you to donate your earnings to charity with a single click!  And as a researcher you can create surveys that require people to donate.  We even give you the ability to add your own charity.<br/><a href="/charity.jsp">Learn more.</a></font><br/>
                   </div>
               </div>

               <br/>

               <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <% MostRecentDonations mrd = (MostRecentDonations) GetCachedStuff.get(new MostRecentDonations());%>
                    <font class="mediumfont" style="color: #999999;">Recent Charity Donators</font><br/>
                    <%=mrd.getHtml()%>
               </div>

           </td>
           <td>
                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <% ActiveSurveys as = (ActiveSurveys) GetCachedStuff.get(new ActiveSurveys());%>
                    <font class="mediumfont" style="color: #999999;">Active Surveys</font><br/>
                    <%=as.getHtml()%>
                </div>

                <br/>

                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <div style="text-align: right; float: right; width: 100px;"><font class="tinyfont" style="color: #999999;">updated every 5 min</font></div>
                    <% RecentSurveyResponses rsr = (RecentSurveyResponses) GetCachedStuff.get(new RecentSurveyResponses()); %>
                    <font class="mediumfont" style="color: #999999;">Recent Survey Responses</font><br/>
                    <%=rsr.getHtml()%>
                </div>

                <br/>

                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <table cellpadding="0" cellspacing="10" border="0" width="100%">
                        <tr>
                            <td width="50%">
                                <div class="rounded" style="background: #ffffff; padding: 10px;">
                                    <% MostRecentPaidInBalance mrpib = (MostRecentPaidInBalance) GetCachedStuff.get(new MostRecentPaidInBalance());%>
                                    <font class="mediumfont" style="color: #999999;">Recent Money Earners</font><br/>
                                    <%=mrpib.getHtml()%>
                                </div>
                            </td>
                            <td width="50%">
                                <div class="rounded" style="background: #ffffff; padding: 10px;">
                                    <% NewestUsers nu = (NewestUsers) GetCachedStuff.get(new NewestUsers());%>
                                    <font class="mediumfont" style="color: #999999;">Newest Users</font><br/>
                                    <%=nu.getHtml()%>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>

                <br/>

                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <% BlogPosts bp = (BlogPosts) GetCachedStuff.get(new BlogPosts()); %>
                    <font class="mediumfont" style="color: #999999;">Blog Posts/Latest News/System Status</font><br/>
                    <%=bp.getHtml()%>
                </div>
           </td>
       </tr>

    </table>








<%
    //MostActiveImpressionLocations mail = (MostActiveImpressionLocations) GetCachedStuff.get(new MostActiveImpressionLocations());
%>
<%//=mail.getHtml()%>









<%@ include file="/template/footer.jsp" %>