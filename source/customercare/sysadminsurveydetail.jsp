<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyDetail" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.survey.servlet.EmbedCacheFlusher" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey: "+((CustomercareSurveyDetail)Pagez.getBeanMgr().get("CustomercareSurveyDetail")).getSurvey().getTitle();
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareSurveyDetail customercareSurveyDetail=(CustomercareSurveyDetail) Pagez.getBeanMgr().get("CustomercareSurveyDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            customercareSurveyDetail.getSurvey().setStatus(Dropdown.getIntFromRequest("status", "Status", true));
            customercareSurveyDetail.getSurvey().setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            customercareSurveyDetail.getSurvey().setDescription(Textarea.getValueFromRequest("description", "Description", true));
            customercareSurveyDetail.getSurvey().setTemplate(Textarea.getValueFromRequest("template", "Template", false));
            customercareSurveyDetail.getSurvey().setIsspotlight(CheckboxBoolean.getValueFromRequest("isspotlight"));
            customercareSurveyDetail.getSurvey().setIsaggressiveslotreclamationon(CheckboxBoolean.getValueFromRequest("isaggressiveslotreclamationon"));
            customercareSurveyDetail.getSurvey().setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            customercareSurveyDetail.getSurvey().setEnddate(DateTime.getValueFromRequest("enddate", "End Date", true).getTime());
            customercareSurveyDetail.getSurvey().setWillingtopaypercpm(Textbox.getDblFromRequest("willingtopaypercpm", "Willing to Pay Per CPM", true, DatatypeDouble.DATATYPEID));
            customercareSurveyDetail.getSurvey().setNumberofrespondentsrequested(Textbox.getIntFromRequest("numberofrespondentsrequested", "Number of Respondents Requested", true, DatatypeInteger.DATATYPEID));
            customercareSurveyDetail.getSurvey().setMaxdisplaysperblog(Textbox.getIntFromRequest("maxdisplaysperblog", "Max Displays Per Blog", true, DatatypeInteger.DATATYPEID));
            customercareSurveyDetail.getSurvey().setMaxdisplaystotal(Textbox.getIntFromRequest("maxdisplaystotal", "Max Displays Total", true, DatatypeInteger.DATATYPEID));
            customercareSurveyDetail.saveSurvey();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("clearembedcache")) {
        try {
            EmbedCacheFlusher.flushCache(customercareSurveyDetail.getSurvey().getSurveyid());
            Pagez.getUserSession().setMessage("Embed cache cleared.");
        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Error: "+ex.getMessage());
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
        <form action="/customercare/sysadminsurveydetail.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/customercare/sysadminsurveydetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="surveyid" value="<%=customercareSurveyDetail.getSurvey().getSurveyid()%>">

                <img src="/images/clear.gif" width="700" height="1"/><br/>
                <font class="largefont"><%=customercareSurveyDetail.getSurvey().getTitle()%></font>
                <br/>
                <font class="smallfont"><%=customercareSurveyDetail.getSurvey().getDescription()%></font>
                <br/><br/>
                <font class="mediumfont">Survey ID: <%=customercareSurveyDetail.getSurvey().getSurveyid()%></font>
                <br/><br/>
                <a href="/customercare/userdetail.jsp?userid=<%=customercareSurveyDetail.getUser().getUserid()%>"><font class="mediumfont"><%=customercareSurveyDetail.getUser().getNickname()%> <%=customercareSurveyDetail.getUser().getEmail()%></font></a>
                <br/><br/>
                <a href="/customercare/sysadminsurveydetail.jsp?action=clearembedcache&surveyid=<%=customercareSurveyDetail.getSurvey().getSurveyid()%>"><font class="tinyfont">Clear Embed Cache (Expensive)</font></a>
                <br/><br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <%if (customercareSurveyDetail.getSurvey().getStatus()==1){%>
                        <font class="mediumfont">Survey Status: Draft</font>
                    <%} else if (customercareSurveyDetail.getSurvey().getStatus()==2){%>
                        <font class="mediumfont">Survey Status: Waiting for Funds</font>
                    <%} else if (customercareSurveyDetail.getSurvey().getStatus()==3){%>
                        <font class="mediumfont">Survey Status: Waiting for Start Date</font>
                    <%} else if (customercareSurveyDetail.getSurvey().getStatus()==4){%>
                        <font class="mediumfont">Survey Status: Open</font>
                    <%} else if (customercareSurveyDetail.getSurvey().getStatus()==5){%>
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
                            <%=Dropdown.getHtml("status",String.valueOf(customercareSurveyDetail.getSurvey().getStatus()), customercareSurveyDetail.getStatuses(), "","")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Title</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("title", customercareSurveyDetail.getSurvey().getTitle(), 255, 35, "", "")%>
                        </td>
                    </tr>
                     <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Description</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("description", customercareSurveyDetail.getSurvey().getDescription(), 3, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Template</font>
                        </td>
                        <td valign="top">
                            <%=Textarea.getHtml("template", customercareSurveyDetail.getSurvey().getTemplate(), 3, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spotlight?</font>
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("isspotlight", customercareSurveyDetail.getSurvey().getIsspotlight(), "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Is Aggressive Slot Reclamation On?</font>
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("isaggressiveslotreclamationon", customercareSurveyDetail.getSurvey().getIsaggressiveslotreclamationon(), "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Start Date</font>
                        </td>
                        <td valign="top">
                            <%=DateTime.getHtml("startdate", customercareSurveyDetail.getSurvey().getStartdate(), "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">End Date</font>
                        </td>
                        <td valign="top">
                            <%=DateTime.getHtml("enddate", customercareSurveyDetail.getSurvey().getEnddate(), "", "")%><br/>
                            <font class="tinyfont">Days since close: <%=customercareSurveyDetail.getDayssinceclose()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Slots Remaining</font>
                            <br/>
                            <font class="smallfont">The number of people that you would like to have join the conversation and post to their peers.  Once this number is reached no more people can join the conversation.  The minimum is 100.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("numberofrespondentsrequested", String.valueOf(customercareSurveyDetail.getSurvey().getNumberofrespondentsrequested()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Willing to Pay Per Thousand Displays on a Blog (CPM) ($USD)</font>
                            <br/>
                            <font class="smallfont">Once conversations are taken they are posted to a person's peers.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your conversation.  This value must be at least $1 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your conversation prominently to their peers.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("willingtopaypercpm", String.valueOf(customercareSurveyDetail.getSurvey().getWillingtopaypercpm()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Survey Displays Per Blog</font>
                            <br/>
                            <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your conversation... so they won't.  The minimum value is 1000.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("maxdisplaysperblog", String.valueOf(customercareSurveyDetail.getSurvey().getMaxdisplaysperblog()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Survey Displays Total</font>
                            <br/>
                            <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max conversations per account... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 1000.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("maxdisplaystotal", String.valueOf(customercareSurveyDetail.getSurvey().getMaxdisplaystotal()), 255, 35, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Displays</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=customercareSurveyDetail.getSurvey().getPublicsurveydisplays()%></font>
                            <br/>
                            <font class="tinyfont">Times somebody's looked at the conversation, considering whether or not to join it.</font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Respondents To Date</font>
                        </td>
                        <td valign="top">
                            <%=PercentCompleteBar.get(customercareSurveyDetail.getSurveyEnhancer().getSlotsremaining(), String.valueOf(customercareSurveyDetail.getSurvey().getNumberofrespondentsrequested()), "", "", "250")%>
                            <font class="smallfont">Up to <%=customercareSurveyDetail.getSurvey().getNumberofrespondentsrequested()%> people may complete this survey for pay.</font>
                            <br/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Blog Displays To Date</font>
                        </td>
                        <td valign="top">
                            <%=PercentCompleteBar.get(customercareSurveyDetail.getSurveyEnhancer().getImpressionsalreadygotten(), String.valueOf(customercareSurveyDetail.getSurvey().getMaxdisplaystotal()), "", "", "250")%>
                            <font class="smallfont">We'll pay for the first <%=customercareSurveyDetail.getSurvey().getMaxdisplaystotal()%> displays in blogs.</font>
                            <br/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Survey Responses to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=customercareSurveyDetail.getSms().getResponsesToDate()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spent on Responses to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont">$<%=Str.formatForMoney(customercareSurveyDetail.getSms().getSpentOnResponsesToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Blog Impressions to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=customercareSurveyDetail.getSms().getImpressionsToDate()%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Spent on Impressions to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(customercareSurveyDetail.getSms().getSpentOnImpressionsToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Total Spent to Date</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(customercareSurveyDetail.getSms().getSpentToDateIncludingdNeeroFee())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Max Possible Spend</font>
                        </td>
                        <td valign="top">
                            <font class="smallfont"><%=Str.formatForMoney(customercareSurveyDetail.getSms().getMaxPossibleSpend())%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Remaining Possible Spend</font>
                        </td>
                        <td valign="top">
                            <font class="formfieldnamefont"></font>
                            <font class="smallfont"><%=Str.formatForMoney(customercareSurveyDetail.getSms().getRemainingPossibleSpend())%></font>
                        </td>
                    </tr>
                </table>


            </form>
        </div>
        <div class="panel" id="panel2" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <%=customercareSurveyDetail.getSurveyForTakers()%>
        </div>
        <div class="panel" id="panel3" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <font class="smallfont"><%=customercareSurveyDetail.getSurveyOnBlogPreview()%></font>
        </div>
        <div class="panel" id="panel4" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <%=customercareSurveyDetail.getSurveyCriteriaAsHtml()%>
        </div>
    




<%@ include file="/template/footer.jsp" %>



