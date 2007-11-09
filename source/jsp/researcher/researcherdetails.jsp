<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Researcher Profile";
String navtab = "researchers";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <t:div rendered="#{userSession.isloggedin and (userSession.user.researcherid eq 0)}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">One-time Researcher Configuration Step 1 of 1: Profile Setup</font>
            <br/>
            <font class="smallfont">We must take your profile so that we can provide you the highest level of service.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    </t:div>


    <h:form id="researcherdetails">


            <table cellpadding="0" cellspacing="0" border="0">


                <td valign="top">
                    <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Could be company name.  Just something to identify your research or marketing organization." styleClass="tinyfont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((ResearcherDetails)Pagez.getBeanMgr().get("ResearcherDetails")).getCompanyname()%>" id="companyname"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="companyname" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Type" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Choose the option that best describes you/your organization." styleClass="tinyfont"></h:outputText>
                </td>
                <td valign="top">
                    <h:selectOneMenu value="<%=((ResearcherDetails)Pagez.getBeanMgr().get("ResearcherDetails")).getCompanytype()%>" id="companytype" required="true">
                        <f:selectItems value="<%=((ResearcherDetails)Pagez.getBeanMgr().get("ResearcherDetails")).getCompanytypes()%>"/>
                    </h:selectOneMenu>
                </td>
                <td valign="top">
                    <h:message for="companytype" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Phone (Optional)" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">So we can contact you regarding<br/>billing and survey issues.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((ResearcherDetails)Pagez.getBeanMgr().get("ResearcherDetails")).getPhone()%>" id="phone" required="false"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="phone" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((ResearcherDetails)Pagez.getBeanMgr().get("ResearcherDetails")).getSaveAction()%>" value="Save Researcher Profile" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>





<%@ include file="/jsp/templates/footer.jsp" %>

