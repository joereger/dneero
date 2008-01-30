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



    <table cellpadding="0" cellspacing="0" border="0" width="750">
       <tr>
           <td align="center">
               <a href="/blogger/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-blogger.png" width="376" height="355" border="0" alt=""/></a>
           </td>
           <td align="center">
                <a href="/researcher/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-researcher.png" width="375" height="355" border="0" alt=""/></a>
           </td>
       </tr>



       <tr>
           <td valign="middle" align="center">
               <a href="/publicsurveylist.jsp"><font class="mediumfont" style="font-weight: bold;">See All Surveys</font></a>
           </td>
           <td valign="top" align="right">
                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <form action="/index.jsp" method="post">
                        <input type="hidden" name="action" value="enteraccesscode">
                        <font class="normalfont"><b>Got an Access Code?</b></font>
                        <%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                        <input type="submit" class="formsubmitbutton" value="Go">
                    </form>
                </div>
           </td>
       </tr>





    </table>

<br clear="all"/>

<%//---------------%>
<br/><br/>
<%
    ActiveSurveys as = (ActiveSurveys) GetCachedStuff.get(new ActiveSurveys());
%>
Active Surveys:
<br/>
<%=as.getHtml()%>


<%//---------------%>
<br/><br/>
<%
    DonationsToCharityMiniReport dtcmr = (DonationsToCharityMiniReport) GetCachedStuff.get(new DonationsToCharityMiniReport());
%>
DonationsToCharityMiniReport:
<br/>
<%=dtcmr.getHtml()%>


<%//---------------%>
<br/><br/>
<%
    MostActiveImpressionLocations mail = (MostActiveImpressionLocations) GetCachedStuff.get(new MostActiveImpressionLocations());
%>
MostActiveImpressionLocations:
<br/>
<%=mail.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    MostActiveUsersByTotalSurveysTaken maubtst = (MostActiveUsersByTotalSurveysTaken) GetCachedStuff.get(new MostActiveUsersByTotalSurveysTaken());
%>
MostActiveUsersByTotalSurveysTaken:
<br/>
<%=maubtst.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    MostRecentPaidInBalance mrpib = (MostRecentPaidInBalance) GetCachedStuff.get(new MostRecentPaidInBalance());
%>
MostRecentPaidInBalance:
<br/>
<%=mrpib.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    NewestUsers nu = (NewestUsers) GetCachedStuff.get(new NewestUsers());
%>
NewestUsers:
<br/>
<%=nu.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    RecentSurveyResponses rsr = (RecentSurveyResponses) GetCachedStuff.get(new RecentSurveyResponses());
%>
Recent Survey Responses:
<br/>
<%=rsr.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    TotalImpressions ti = (TotalImpressions) GetCachedStuff.get(new TotalImpressions());
%>
TotalImpressions:
<br/>
<%=ti.getHtml()%>

<%//---------------%>
<br/><br/>
<%
    TotalSurveysTaken tst = (TotalSurveysTaken) GetCachedStuff.get(new TotalSurveysTaken());
%>
TotalSurveysTaken:
<br/>
<%=tst.getHtml()%>




<%@ include file="/template/footer.jsp" %>