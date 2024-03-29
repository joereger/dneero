<%@ page import="com.dneero.helpers.ResearcherCreateIfNeeded" %>
<%@ page import="com.dneero.htmlui.CheckboxBoolean" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textarea" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%String jspPageName="/researcher/researchertwitaskdetail_01.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "" +
"        <font class=\"pagetitlefont\">"+((ResearcherTwitaskDetail01) Pagez.getBeanMgr().get("ResearcherTwitaskDetail01")).getQuestion()+"</font>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%
    ResearcherCreateIfNeeded.createIfNecessary();
%>
<%
if (!Pagez.getUserSession().getPl().getIsanybodyallowedtocreatetwitasks()){
    if (!Pagez.getUserSession().getIsCreateTwitasks()){
        Pagez.getUserSession().setMessage("Sorry, you don't appear to have Create Twitter Questions permission.  Please contact a system administrator if you believe this is an error.");
        Pagez.sendRedirect("/researcher/index-twitask.jsp");
        return;
    }
}
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail01 researcherTwitaskDetail01= (ResearcherTwitaskDetail01)Pagez.getBeanMgr().get("ResearcherTwitaskDetail01");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail01.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid());
                return;
            }
            researcherTwitaskDetail01.setQuestion(Textarea.getValueFromRequest("question", "Question", true));
            //researcherTwitaskDetail01.setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            researcherTwitaskDetail01.setIsfree(!CheckboxBoolean.getValueFromRequest("isfree"));
            researcherTwitaskDetail01.setIsopentoanybody(!CheckboxBoolean.getValueFromRequest("isopentoanybody"));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_02.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" has been saved.");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid()+"&ispreviousclick=1");
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchertwitaskdetail_01.jsp" method="post"  class="niceform" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_01.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail01.getTwitask().getTwitaskid()%>"/>


        <%--<center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">--%>
        <%----%>
        <%--</font></div></center>--%>

        <br/><br/>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">

                    <script type="text/javascript">
                        function textCounter( field, countfield, maxlimit ) {
                          if ( field.value.length > maxlimit ){
                            field.value = field.value.substring( 0, maxlimit );
                            return false;
                          } else {
                            countfield.value = maxlimit - field.value.length;
                          }
                        }
                    </script>

                    

                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td valign="top" colspan="2">
                                <font class="mediumfont">What do you want to ask your Twitter followers?</font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                                    <%int maxchars = 120;%>
                                    <textarea name="question" rows="3" cols="40" style="width: 100%; font-size: 25px;" onkeypress="textCounter(this,this.form.counter,<%=maxchars%>);"><%=researcherTwitaskDetail01.getQuestion()%></textarea>
                                    <br/><font class="tinyfont">ex: "Do you like old Michael Jackson videos?" or "On a scale of 1-10 (10 being highest) do you care about the environment?"</font>
                                    <br/><input type="text" name="counter" id="counter" maxlength="3" size="3" style="font-size: 8px;" value="<%=maxchars%>" onblur="textCounter(this.form.question,this,<%=maxchars%>);"><font class="tinyfont"> chars left</font>
                                    <br/><br/><br/>
                                <%} else {%>
                                    <font class="smallfont"><%=researcherTwitaskDetail01.getQuestion()%></font>
                                <%}%>
                            </td>
                        </tr>
                        <%--<tr>--%>
                            <%--<td valign="top" colspan="2">--%>
                                <%--<font class="formfieldnamefont">About when should your question be asked?</font>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <%--<tr>--%>
                            <%--<td valign="top" colspan="2">--%>
                                <%--<%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>--%>
                                    <%--<%=DateTime.getHtml("startdate", Time.getCalFromDate(researcherTwitaskDetail01.getStartdate()), "", "")%>--%>
                                <%--<%} else {%>--%>
                                    <%--<font class="normalfont"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(researcherTwitaskDetail01.getStartdate()))%></font>--%>
                                <%--<%}%>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont"></font>
                            </td>
                            <td valign="top">
                                <%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                                    <%=CheckboxBoolean.getHtml("isfree", !researcherTwitaskDetail01.getIsfree(), "", "")%> I want to pay participants via cash, coupon or charitable donation (recommendation: keep unchecked)
                                <%} else {%>
                                    <font class="normalfont"></font>
                                <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont"></font>
                            </td>
                            <td valign="top">
                                <%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                                    <%=CheckboxBoolean.getHtml("isopentoanybody", !researcherTwitaskDetail01.getIsopentoanybody(), "", "")%> I want to limit who can participate (recommendation: keep unchecked)
                                <%} else {%>
                                    <font class="normalfont"></font>
                                <%}%>
                            </td>
                        </tr>
                    </table>

                </td>
                <td valign="top">
                    <!-- Right Side placeholder -->
                </td>
            </tr>
        </table>
        
        <br/><br/>
        <!-- Start Bottom Nav -->
        <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <!--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">-->
            </td>
            <td valign="top" align="right">
                <%if (researcherTwitaskDetail01.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->


</form>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>