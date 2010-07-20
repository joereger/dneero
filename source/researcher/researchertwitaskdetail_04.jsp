<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail04" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%String jspPageName="/researcher/researchertwitaskdetail_04.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherTwitaskDetail04) Pagez.getBeanMgr().get("ResearcherTwitaskDetail04")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail04 researcherTwitaskDetail04= (ResearcherTwitaskDetail04)Pagez.getBeanMgr().get("ResearcherTwitaskDetail04");
%>
<%if (researcherTwitaskDetail04.getTwitask().getIsopentoanybody()){
    if (request.getParameter("ispreviousclick")!=null && request.getParameter("ispreviousclick").equals("1")){
        Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+researcherTwitaskDetail04.getTwitask().getTwitaskid()+"&ispreviousclick=1");
        return;
    }
    Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+researcherTwitaskDetail04.getTwitask().getTwitaskid());
    return;
}%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail04.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid()+"&ispreviousclick=1");
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                    return;
                }
            }

            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl().getPlid()))
                           .addOrder(Order.asc("ordernum"))
                           .setCacheable(true)
                           .list();
            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                Demographic demographic = demographicIterator.next();
                researcherTwitaskDetail04.getDemographicsXML().setValues(demographic.getDemographicid(), DropdownMultiselect.getValueFromRequest("demographic_"+demographic.getDemographicid(), demographic.getName(), false));
            }


            researcherTwitaskDetail04.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherTwitaskDetail04.setDayssincelastsurvey(Textbox.getIntFromRequest("dayssincelastsurvey", "Days Since Last Survey", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setTotalsurveystakenatleast(Textbox.getIntFromRequest("totalsurveystakenatleast", "Total "+Pagez._Surveys()+" Joined of At Least", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setTotalsurveystakenatmost(Textbox.getIntFromRequest("totalsurveystakenatmost", "Total "+Pagez._Surveys()+" Joined of At Most", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setPanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("panels", "Panels", false)));
            researcherTwitaskDetail04.setSuperpanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("superpanels", "SuperPanels", false)));
            researcherTwitaskDetail04.setDneerousagemethods(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("dneerousagemethods", "Usage Methods", false)));
            researcherTwitaskDetail04.setIsopentoanybody(CheckboxBoolean.getValueFromRequest("isopentoanybody"));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" has been saved.");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid()+"&ispreviousclick=1");
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<form action="/researcher/researchertwitaskdetail_04.jsp" method="post"  class="niceform" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_04.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail04.getTwitask().getTwitaskid()%>"/>



        <%
            String isopenChecked = "";
            if (researcherTwitaskDetail04.getIsopentoanybody()){
                isopenChecked = "checked=\"checked\"";
            }
        %>
        <input type="checkbox" id="isopentoanybody" name="isopentoanybody" value="1" <%=isopenChecked%> /> Anybody can participate

        <div id="togglepage">


            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            Target your Twitter Question to the correct demographic.   Be careful not to cast too narrow a net.  Often it's best to start with campaigns open to all... or all U.S., for example.
            <br/><br/><br/>
            </font></div></center>

            <br/><br/>



            <%if (researcherTwitaskDetail04.getTwitask().getStatus()>Twitask.STATUS_DRAFT) {%>
                <%=researcherTwitaskDetail04.getSurveyCriteriaAsHtml()%>
                <br/>
                <b>Panels:</b>
                <%=researcherTwitaskDetail04.getPanelsStr()%>
                <br/>
                <b>SuperPanels:</b>
                <%=researcherTwitaskDetail04.getSuperpanelsStr()%>
            <%}%>

            <%if (researcherTwitaskDetail04.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                <table cellpadding="5" cellspacing="0" border="0">


                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                            <br/>
                            <font class="smallfont">Social Influence Rating takes site traffic, <%=Pagez._survey()%> referrals and a number of other metrics into account to give you some measure of this social person's influence with his/her readership.</font>
                        </td>
                        <td valign="top">
                            <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherTwitaskDetail04.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                        </td>


                        <td valign="top">
                            <font class="formfieldnamefont">Usage Method</font>
                            <br/>
                            <font class="tinyfont">There are multiple ways that users can access Twitter Questions.  This control allows you to target each access method individually.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("dneerousagemethods", Util.stringArrayToArrayList(researcherTwitaskDetail04.getDneerousagemethods()), Util.treeSetToTreeMap(Dneerousagemethods.get()), 3, "", "")%>
                        </td>
                    </tr>



                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of <%=Pagez._Surveys()%> Joined</font>
                            <br/>
                            <font class="smallfont">Reflects the experience level of the respondent.  Enter a minimum and maximum number of <%=Pagez._surveys()%> that qualified respondents are allowed to have joined.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("totalsurveystakenatleast", String.valueOf(researcherTwitaskDetail04.getTotalsurveystakenatleast()), 5, 4, "", "")%>
                            -
                            <%=Textbox.getHtml("totalsurveystakenatmost", String.valueOf(researcherTwitaskDetail04.getTotalsurveystakenatmost()), 5, 4, "", "")%>
                        </td>

                        <td valign="top">
                            <font class="formfieldnamefont">Days Since Taking Last <%=Pagez._Survey()%> of At Least</font>
                            <br/>
                            <font class="smallfont">A qualifying respondent must have not taken another <%=Pagez._survey()%> in at least this many days.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("dayssincelastsurvey", String.valueOf(researcherTwitaskDetail04.getDayssincelastsurvey()), 5, 3, "", "")%>
                        </td>
                    </tr>



                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Age Range</font>
                        </td>
                        <td valign="top" colspan="3">
                            <%=Textbox.getHtml("agemin", String.valueOf(researcherTwitaskDetail04.getAgemin()), 5, 3, "", "")%>
                            -
                            <%=Textbox.getHtml("agemax", String.valueOf(researcherTwitaskDetail04.getAgemax()), 5, 3, "", "")%>
                        </td>

                    </tr>

                    <%
                            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl().getPlid()))
                                           .addOrder(Order.asc("ordernum"))
                                           .setCacheable(true)
                                           .list();
                            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                                Demographic demographic = demographicIterator.next();
                                %>

                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont"><%=demographic.getName()%></font>
                                        <%if (demographic.getDescription().length()>0){ %>
                                            <br/><font class="tinyfont"><%=demographic.getDescription()%></font>
                                        <%}%>
                                    </td>
                                    <td valign="top" colspan="3">
                                        <%=DropdownMultiselect.getHtml("demographic_"+demographic.getDemographicid(),
                                                            researcherTwitaskDetail04.getDemographicsXML().getValues(demographic.getDemographicid()),
                                                            Util.stringArrayToTreeMap(researcherTwitaskDetail04.getDemographicsXML().getAllPossibleValues(demographic.getDemographicid())),
                                                            5,
                                                            "",
                                                            "")%>
                                    </td>
                                </tr>

                                <%
                            }
                    %>


                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Panel Membership</font>
                            <br/>
                            <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("panels", Util.stringArrayToArrayList(researcherTwitaskDetail04.getPanels()), researcherTwitaskDetail04.getPanelsavailable(), 6, "", "")%>
                        </td>

                        <td valign="top">
                            <font class="formfieldnamefont">SuperPanel Membership</font>
                            <br/>
                            <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("superpanels", Util.stringArrayToArrayList(researcherTwitaskDetail04.getSuperpanels()), researcherTwitaskDetail04.getSuperpanelsavailable(), 6, "", "")%>
                        </td>
                    </tr>



                </table>
            <%}%>

            </div>
            <script>
                $("#isopentoanybody").change(function() {
                    $("#togglepage").toggle();
                });
                <% if (researcherTwitaskDetail04.getIsopentoanybody()){%>
                $("#togglepage").hide();
                <% }  %>
            </script>
            <script>
                $("#helplink").click(function() {
                    $("#togglehelp").toggle();
                });
                $("#togglehelp").hide();
            </script>

            <br/><br/>
            <!-- Start Bottom Nav -->
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td valign="top" align="left">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">
                    </td>
                    <td valign="top" align="right">
                        <%if (researcherTwitaskDetail04.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                            <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                        <%}%>
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">
                    </td>
                </tr>
            </table>
            <!-- End Bottom Nav -->
    </form>



<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>