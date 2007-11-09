<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Your Profile";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>





    <t:div rendered="#{userSession.isloggedin and (userSession.user.bloggerid eq 0)}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">Quick One-time Configuration Required</font>
            <br/>
            <font class="smallfont">Before you can make money we need to collect a few pieces of information about you.  This will only take a minute or two and you won't have to do it again on future visits.  You'll be filling out surveys, posting them socially and making money in no time! We must take your profile so that we can find offers that fit you.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    </t:div>

    <table cellpadding="10" cellspacing="0" border="0" width="90%">
        <tr>
            <td valign="top">
                <h:form id="bloggerdetails">
                    <table cellpadding="0" cellspacing="0" border="0">

                        <td valign="top">
                            <h:outputText value="Birthdate" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <t:inputDate value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getBirthdate()%>" type="date" popupCalendar="true" id="birthdate" required="true"></t:inputDate>
                            <br/>
                            <font class="tinyfont">(dd, mm, yyyy)... click the three dots for popup chooser</font>
                        </td>
                        <td valign="top">
                            <h:message for="birthdate" styleClass="RED"></h:message>
                        </td>


                        <td valign="top">
                            <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneRadio value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getGender()%>" id="gender" required="true">
                                <f:selectItems value="#{genders}"/>
                            </h:selectOneRadio>
                        </td>
                        <td valign="top">
                            <h:message for="gender" styleClass="RED"></h:message>
                        </td>


                        <td valign="top">
                            <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getEthnicity()%>" id="ethnicity" required="true">
                                <f:selectItems value="#{ethnicities}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="ethnicity" styleClass="RED"></h:message>
                        </td>

                        <td valign="top">
                            <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getMaritalstatus()%>" id="maritalstatus" required="true">
                                <f:selectItems value="#{maritalstatuses}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="maritalstatus" styleClass="RED"></h:message>
                        </td>

                        <td valign="top">
                            <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getIncome()%>" id="income" required="true">
                                <f:selectItems value="#{incomes}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="income" styleClass="RED"></h:message>
                        </td>

                        <td valign="top">
                            <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getEducationlevel()%>" id="educationlevel" required="true">
                                <f:selectItems value="#{educationlevels}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="educationlevel" styleClass="RED"></h:message>
                        </td>



                        <td valign="top">
                            <h:outputText value="Nearest City" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getCity()%>" id="city" required="true">
                                <f:selectItems value="#{cities}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="city" styleClass="RED"></h:message>
                        </td>


                        <td valign="top">
                            <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getState()%>" id="state" required="true">
                                <f:selectItems value="#{states}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="state" styleClass="RED"></h:message>
                        </td>


                        <td valign="top">
                            <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getProfession()%>" id="profession" required="true">
                                <f:selectItems value="#{professions}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="profession" styleClass="RED"></h:message>
                        </td>



                        <td valign="top">
                            <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getPolitics()%>" id="politics" required="true">
                                <f:selectItems value="#{politics}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="politics" styleClass="RED"></h:message>
                        </td>

                        <td valign="top">
                            <h:outputText value="Your Blog's Focus" styleClass="formfieldnamefont"></h:outputText>
                            <br/>
                            <font class="tinyfont">The most accurate description of what<br/>you blog about or discuss on<br/>social networks.</font>
                        </td>
                        <td valign="top">
                            <h:selectOneMenu value="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getBlogfocus()%>" id="blogfocus" required="true">
                                <f:selectItems value="#{blogfocuses}"/>
                            </h:selectOneMenu>
                        </td>
                        <td valign="top">
                            <h:message for="blogfocus" styleClass="RED"></h:message>
                        </td>



                        <td valign="top">
                        </td>
                        <td valign="top">
                            <h:commandButton action="<%=((BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails")).getSaveAction()%>" value="Save Profile" styleClass="formsubmitbutton"></h:commandButton>
                        </td>
                        <td valign="top">
                        </td>


                    </table>

                </h:form>
            </td>
            <td valign="top" width="200">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <center>
                            <img src="/images/lock-64.png" width="64" height="64"/>
                            <br/>
                            <font class="mediumfont" style="color: #666666;">This information will be kept private.</font> 
                        </center>
                    </div>
                    <font class="smallfont">
                        <br/><br/><b>Why do you collect this information?</b><br/>
                        For two reasons. First, to present you with the most relevant surveys.  Second, because market researchers need to be able to target surveys to particular demographics

                        <br/><br/><b>Will this data ever be made available to others?</b><br/>
                        It will not be made available in a way that can be tied back to you.  However, we may create aggregate results on surveys that say things like "people aged 23-25 answered this way."  When we do this we're using the information from this page but it's in a way that your particular identity isn't shared.

                        <br/><br/><b>Check the survey criteria</b><br/>
                        If a survey has a sufficiently limited demographic target it's possible that survey readers can infer demographic data about you.  For example, if a researcher creates a survey targeted only to individuals with an income of between $30,000 and $40,000 per year and you take the survey then people can infer what your salary is.  When a survey is displayed both the criteria and who took the survey are visible.  Take this into account and if you're not comfortable with the scope of the demographic criteria that the researcher has chosen, tell them... they may choose to widen it.
                    </font>
                    <br/><br/>

                </div>
            </td>
        </tr>
    </table>


<%@ include file="/jsp/templates/footer.jsp" %>

