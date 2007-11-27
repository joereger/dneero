<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminSurveyDetail" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.util.Str" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey: "+((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getTitle();
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminSurveyDetail sysadminSurveyDetail=(SysadminSurveyDetail) Pagez.getBeanMgr().get("SysadminSurveyDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminSurveyDetail.getSurvey().setStatus(Dropdown.getIntFromRequest("status", "Status", true));
            sysadminSurveyDetail.getSurvey().setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            sysadminSurveyDetail.getSurvey().setDescription(Textarea.getValueFromRequest("description", "Description", true));
            sysadminSurveyDetail.getSurvey().setTemplate(Textarea.getValueFromRequest("template", "Template", false));
            sysadminSurveyDetail.getSurvey().setIsspotlight(CheckboxBoolean.getValueFromRequest("isspotlight"));
            sysadminSurveyDetail.getSurvey().setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            sysadminSurveyDetail.getSurvey().setEnddate(DateTime.getValueFromRequest("enddate", "End Date", true).getTime());
            sysadminSurveyDetail.getSurvey().setWillingtopayperrespondent(Textbox.getDblFromRequest("willingtopayperrespondent", "Willing to Pay Per Respondent", true, DatatypeDouble.DATATYPEID));
            sysadminSurveyDetail.getSurvey().setWillingtopaypercpm(Textbox.getDblFromRequest("willingtopaypercpm", "Willing to Pay Per CPM", true, DatatypeDouble.DATATYPEID));
            sysadminSurveyDetail.getSurvey().setNumberofrespondentsrequested(Textbox.getIntFromRequest("numberofrespondentsrequested", "Number of Respondents Requested", true, DatatypeInteger.DATATYPEID));
            sysadminSurveyDetail.getSurvey().setMaxdisplaysperblog(Textbox.getIntFromRequest("maxdisplaysperblog", "Max Displays Per Blog", true, DatatypeInteger.DATATYPEID));
            sysadminSurveyDetail.getSurvey().setMaxdisplaystotal(Textbox.getIntFromRequest("maxdisplaystotal", "Max Displays Total", true, DatatypeInteger.DATATYPEID));
            sysadminSurveyDetail.saveSurvey();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2', 'panel3', 'panel4');
      var selectedTab = null;
      function showPanel(tab, name)
      {
        if (selectedTab)
        {
          selectedTab.style.backgroundColor = '';
          selectedTab.style.paddingTop = '';
          selectedTab.style.marginTop = '4px';
        }
        selectedTab = tab;
        selectedTab.style.backgroundColor = 'white';
        selectedTab.style.paddingTop = '6px';
        selectedTab.style.marginTop = '0px';

        for(i = 0; i < panels.length; i++){
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';
        }
        return false;
      }
    </script>
    <div id="tabs">
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" onclick="return false;">Summary</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Question Preview</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" onclick="return false;">On Blog</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel4');" onclick="return false;">Requirements</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
        <form action="/sysadmin/sysadminsurveydetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/sysadminsurveydetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="surveyid" value="<%=sysadminSurveyDetail.getSurvey().getSurveyid()%>">

                <img src="/images/clear.gif" width="700" height="1"/><br/>
                <font class="largefont"><%=sysadminSurveyDetail.getSurvey().getTitle()%></font>
                <br/>
                <font class="smallfont"><%=sysadminSurveyDetail.getSurvey().getDescription()%></font>
                <br/><br/>
                <font class="mediumfont">Survey ID: <%=sysadminSurveyDetail.getSurvey().getSurveyid()%></font>
                <br/><br/>
                <a href="/sysadmin/userdetail.jsp?userid=<%=sysadminSurveyDetail.getUser().getUserid()%>"><font class="mediumfont"><%=sysadminSurveyDetail.getUser().getFirstname()%> <%=sysadminSurveyDetail.getUser().getLastname()%> <%=sysadminSurveyDetail.getUser().getEmail()%></font></a>
                <br/><br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <%if (sysadminSurveyDetail.getSurvey().getStatus()==1){%>
                        <font class="mediumfont">Survey Status: Draft</font>
                    <%} else if (sysadminSurveyDetail.getSurvey().getStatus()==2){%>
                        <font class="mediumfont">Survey Status: Waiting for Funds</font>
                    <%} else if (sysadminSurveyDetail.getSurvey().getStatus()==3){%>
                        <font class="mediumfont">Survey Status: Waiting for Start Date</font>
                    <%} else if (sysadminSurveyDetail.getSurvey().getStatus()==4){%>
                        <font class="mediumfont">Survey Status: Open</font>
                    <%} else if (sysadminSurveyDetail.getSurvey().getStatus()==5){%>
                        <font class="mediumfont">Survey Status: Closed</font>
                    <%}%>
                </div>
                <br/><br/>
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Status</font>
                        </td>
                        <td valign="top">
                            <%=Dropdown.getHtml("status",String.valueOf(sysadminSurveyDetail.getSurvey().getStatus()), sysadminSurveyDetail.getStatuses(), "","")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Survey Title</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("title", sysadminSurveyDetail.getSurvey().getTitle(), 255, 35, "", "")%>
                        </td>
                    </tr>
                     <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Description</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("description", sysadminSurveyDetail.getSurvey().getDescription(), 3, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Template</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("template", sysadminSurveyDetail.getSurvey().getTemplate(), 3, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spotlight Survey?</font>
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("isspotlight", sysadminSurveyDetail.getSurvey().getIsspotlight(), "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Start Date</font>
                        </td>
                        <td valign="top">
                            <%=DateTime.getHtml("startdate", sysadminSurveyDetail.getSurvey().getStartdate(), "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">End Date</font>
                        </td>
                        <td valign="top">
                            <%=DateTime.getHtml("enddate", sysadminSurveyDetail.getSurvey().getEnddate(), "", "")%><br/>
                            <font class="tinyfont">Days since close: <%=sysadminSurveyDetail.getDayssinceclose()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Willing to Pay Per Respondent ($USD)</font>
                            <br/>
                            <font class="smallfont">Amount to pay to a person who fulfills the targeting criteria and successfully fills out the survey.  Paying more will attract more people.  The minimum is $.25.  A good starting point is $2.50.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("willingtopayperrespondent", String.valueOf(sysadminSurveyDetail.getSurvey().getWillingtopayperrespondent()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Respondents Requested</font>
                            <br/>
                            <font class="smallfont">The number of people that you would like to have fill out the survey and post to their blogs.  Once this number is reached no more people can take the survey.  The minimum is 100.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("numberofrespondentsrequested", String.valueOf(sysadminSurveyDetail.getSurvey().getNumberofrespondentsrequested()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Willing to Pay Per Thousand Survey Displays on a Blog (CPM) ($USD)</font>
                            <br/>
                            <font class="smallfont">Once surveys are taken they are posted to a person's blog.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your survey.  This value must be at least $1 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your survey prominently on their blog.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("willingtopaypercpm", String.valueOf(sysadminSurveyDetail.getSurvey().getWillingtopaypercpm()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Survey Displays Per Blog</font>
                            <br/>
                            <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your survey... so they won't.  The minimum value is 1000.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("maxdisplaysperblog", String.valueOf(sysadminSurveyDetail.getSurvey().getMaxdisplaysperblog()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Survey Displays Total</font>
                            <br/>
                            <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max surveys per blog... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 1000.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("maxdisplaystotal", String.valueOf(sysadminSurveyDetail.getSurvey().getMaxdisplaystotal()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <input type="submit" class="formsubmitbutton" value="Save">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Displays</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=sysadminSurveyDetail.getSurvey().getPublicsurveydisplays()%></font>
                            <br/>
                            <font class="tinyfont">Times somebody's looked at the survey, considering whether or not to take it.</font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Respondents To Date</font>
                        </td>
                        <td valign="top">
                            <%=PercentCompleteBar.get(sysadminSurveyDetail.getSurveyEnhancer().getResponsesalreadygotten(), String.valueOf(sysadminSurveyDetail.getSurvey().getNumberofrespondentsrequested()), "", "", "250")%>
                            <font class="smallfont">Up to <%=sysadminSurveyDetail.getSurvey().getNumberofrespondentsrequested()%> people may complete this survey for pay.</font>
                            <br/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Blog Displays To Date</font>
                        </td>
                        <td valign="top">
                            <%=PercentCompleteBar.get(sysadminSurveyDetail.getSurveyEnhancer().getImpressionsalreadygotten(), String.valueOf(sysadminSurveyDetail.getSurvey().getMaxdisplaystotal()), "", "", "250")%>
                            <font class="smallfont">We'll pay for the first <%=sysadminSurveyDetail.getSurvey().getMaxdisplaystotal()%> displays in blogs.</font>
                            <br/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Survey Responses to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=sysadminSurveyDetail.getSms().getResponsesToDate()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spent on Responses to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont">$<%=Str.formatForMoney(sysadminSurveyDetail.getSms().getSpentOnResponsesToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Blog Impressions to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=sysadminSurveyDetail.getSms().getImpressionsToDate()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spent on Impressions to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(sysadminSurveyDetail.getSms().getSpentOnImpressionsToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Total Spent to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(sysadminSurveyDetail.getSms().getSpentToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Possible Spend</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(sysadminSurveyDetail.getSms().getMaxPossibleSpend())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Remaining Possible Spend</font>
                        </td>
                        <td valign="top">
                            <font class="formfieldnamefont"></font>
                            <font class="smallfont"><%=Str.formatForMoney(sysadminSurveyDetail.getSms().getRemainingPossibleSpend())%></font>
                        </td>
                    </tr>
                </table>


            </form>
        </div>
        <div class="panel" id="panel2" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <%=sysadminSurveyDetail.getSurveyForTakers()%>
        </div>
        <div class="panel" id="panel3" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <font class="smallfont"><%=sysadminSurveyDetail.getSurveyOnBlogPreview()%></font>
        </div>
        <div class="panel" id="panel4" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <%=sysadminSurveyDetail.getSurveyCriteriaAsHtml()%>
        </div>
    




<%@ include file="/template/footer.jsp" %>



