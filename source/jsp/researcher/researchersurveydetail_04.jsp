<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-04.gif\" align=\"right\" width=\"350\" height=\"73\"></img>\n" +
"        <h:outputText value=\"<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getTitle()%>\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail04.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Target your survey to the correct demographic.   Be careful not to cast too narrow a net.  In the final step we'll tell you how many bloggers fulfill your criteria and you'll have the opportunity to widen the search.
    <br/><br/><br/>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>

    <h:messages/>

    <t:div rendered="#{researcherSurveyDetail04.status ne 1}">
        <f:verbatim escape="false"><%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getSurveyCriteriaAsHtml()%></f:verbatim>
        <br/>
        <b>Panels:</b>
        <h:outputText value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getPanelsStr()%>" rendered="#{researcherSurveyDetail04.status ne 1}" escape="false"></h:outputText>
    </t:div>

    <t:div rendered="#{researcherSurveyDetail04.status eq 1}">
        <table cellpadding="0" cellspacing="0" border="0">

            <td valign="top">
                <h:outputText value="Social Influence Rating (TM)" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="Social Influence Rating takes site traffic, survey referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership." styleClass="smallfont"></h:outputText>
                <br/>
                <h:message for="minsocialinfluencepercentile" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectOneMenu value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getMinsocialinfluencepercentile()%>" id="minsocialinfluencepercentile" required="true">
                   <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getPercentiles()%>"/>
                </h:selectOneMenu>
            </td>


            <td valign="top">
                <h:outputText value="Social Influence Rating 90 Days" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="minsocialinfluencepercentile90days" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectOneMenu value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getMinsocialinfluencepercentile90days()%>" id="minsocialinfluencepercentile90days" required="true">
                   <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getPercentiles()%>"/>
                </h:selectOneMenu>
            </td>




            <td valign="top">
                <h:outputText value="Blog Quality of At Least" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating." styleClass="smallfont"></h:outputText>
                <br/>
                <h:message for="blogquality" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectOneListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getBlogquality()%>" size="1" id="blogquality" layout="pageDirection" required="true">
                    <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getBlogqualities()%>"/>
                </h:selectOneListbox>
            </td>


            <td valign="top">
                <h:outputText value="Blog Quality Over Last 90 Days of At Least" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="blogquality90days" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectOneListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getBlogquality90days()%>" size="1" id="blogquality90days" layout="pageDirection" required="true">
                    <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getBlogqualities()%>"/>
                </h:selectOneListbox>
            </td>







            <td valign="top">
                <h:outputText value="Age Range" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="agemin" styleClass="RED"></h:message>
                <h:message for="agemax" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:inputText value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getAgemin()%>" id="agemin" size="3" required="true">
                    <f:validateDoubleRange minimum="13" maximum="120"></f:validateDoubleRange>
                </h:inputText>
                <h:outputText value=" - "></h:outputText>
                <h:inputText value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getAgemax()%>" id="agemax" size="3" required="true">
                    <f:validateDoubleRange minimum="13" maximum="120"></f:validateDoubleRange>
                </h:inputText>
            </td>




            <td valign="top">
                <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="gender" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyCheckbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getGender()%>" id="gender" required="true">
                    <f:selectItems value="#{genders}"/>
                </h:selectManyCheckbox>
            </td>


            <td valign="top">
                <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="ethnicity" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getEthnicity()%>" id="ethnicity" size="6" layout="pageDirection" required="true">
                    <f:selectItems value="#{ethnicities}"/>
                </h:selectManyListbox>
            </td>


            <td valign="top">
                <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="maritalstatus" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyCheckbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getMaritalstatus()%>" id="maritalstatus" required="true">
                    <f:selectItems value="#{maritalstatuses}"/>
                </h:selectManyCheckbox>
            </td>

            <td valign="top">
                <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="income" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getIncome()%>" size="5" id="income" layout="pageDirection" required="true">
                    <f:selectItems value="#{incomes}"/>
                </h:selectManyListbox>
            </td>

            <td valign="top">
                <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="educationlevel" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyCheckbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getEducationlevel()%>" id="educationlevel" layout="pageDirection" required="true">
                    <f:selectItems value="#{educationlevels}"/>
                </h:selectManyCheckbox>
            </td>



            <td valign="top">
                <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="state" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getState()%>" size="5" id="state" layout="pageDirection" required="true">
                    <f:selectItems value="#{states}"/>
                </h:selectManyListbox>
            </td>

            <td valign="top">
                <h:outputText value="City" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="city" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getCity()%>" size="5" id="city" layout="pageDirection" required="true">
                    <f:selectItems value="#{cities}"/>
                </h:selectManyListbox>
            </td>


            <td valign="top">
                <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="profession" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getProfession()%>" size="5" id="profession" layout="pageDirection" required="true">
                    <f:selectItems value="#{professions}"/>
                </h:selectManyListbox>
            </td>


            <td valign="top">
                <h:outputText value="Blog Focus" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="blogfocus" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getBlogfocus()%>" size="5" id="blogfocus" layout="pageDirection" required="true">
                    <f:selectItems value="#{blogfocuses}"/>
                </h:selectManyListbox>
            </td>


            <td valign="top">
                <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="politics" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getPolitics()%>" size="5" id="politics" layout="pageDirection" required="true">
                    <f:selectItems value="#{politics}"/>
                </h:selectManyListbox>
            </td>

            <td valign="top">
                <h:outputText value="Panel Membership" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="panels" styleClass="RED"></h:message>
            </td>
            <td valign="top">
                <h:selectManyListbox value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getPanels()%>" id="panels" size="6" layout="pageDirection" required="false">
                    <f:selectItems value="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getPanelsavailable()%>"/>
                </h:selectManyListbox>
            </td>


        </table>
    </t:div>

    <f:verbatim><br/><br/></f:verbatim>
    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"><h:commandButton action="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getPreviousStep()%>" value="Previous Step" styleClass="formsubmitbutton"/></div><div class="surveyeditnextbutton"><h:commandButton action="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getSaveSurveyAsDraft()%>" value="Save and Continue Later" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail04.status eq 1}"/><h:commandButton action="<%=((ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getSaveSurvey()%>" value="Next Step" styleClass="formsubmitbutton"/></div></div>



<%@ include file="/jsp/templates/footer.jsp" %>