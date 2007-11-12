<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Mass Email Detail: <%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getSubject()%>";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{sysadminMassemailDetail}"/>

    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_b" label="Details">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <table cellpadding="0" cellspacing="0" border="0">


                <td valign="top">
                    <h:outputText value="Subject" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getSubject()%>" id="subject" required="true" size="60" maxlength="200"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="subject" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Text Message" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getTxtmessage()%>" id="txtmessage" required="true" cols="60" rows="8">
                    </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="txtmessage" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Html Message" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getHtmlmessage()%>" id="htmlmessage" required="true" cols="60" rows="8">
                    </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="htmlmessage" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Send to Bloggers?" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox id="issenttobloggers" value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getIssenttobloggers()%>"/>
                </td>
                <td valign="top">
                    <h:message for="issenttobloggers" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Send to Researchers?" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox id="issenttoresearchers" value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getIssenttoresearchers()%>"/>
                </td>
                <td valign="top">
                    <h:message for="issenttoresearchers" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="(Dangerous!)Send to Users who opted out of non-critical email?" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox id="issenttouserswhooptoutofnoncriticalemails" value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getMassemail().getIssenttouserswhooptoutofnoncriticalemails()%>"/>
                </td>
                <td valign="top">
                    <h:message for="issenttouserswhooptoutofnoncriticalemails" styleClass="RED"></h:message>
                </td>

             </table>

             <h:commandButton action="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getSave()%>" value="Save and Preview" styleClass="formsubmitbutton"></h:commandButton>
             <h:commandButton action="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getSend()%>" value="Send" styleClass="formsubmitbutton"></h:commandButton>

             <br/><br/>
             <font class="formfieldnamefont">Test-send to this email address:</font>
             <br/>
             <h:inputText value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getTestsendemailaddress()%>" id="testsendemailaddress" size="20" maxlength="200"></h:inputText>
             <h:commandButton action="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getTestSend()%>" value="Test Send" styleClass="formsubmitbutton"></h:commandButton>

             <br/><br/>
             <font class="formfieldnamefont">Create a copy of this Mass Email:</font>
             <br/>
             <h:commandButton action="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getCopy()%>" value="Copy" styleClass="formsubmitbutton"></h:commandButton>

             <br/><br/>
             <font class="smallfont">
                 <br/>&lt;$user.email$>
                 <br/>&lt;$user.firstname$>
                 <br/>&lt;$user.lastname$>
                 <br/>&lt;$user.userid$>
                 <br/>&lt;$baseUrl.includinghttp$>
             </font>

        </t:panelTab>
        <t:panelTab id="panel_c" label="Html Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false"><%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getHtmlPreview()%></f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_d" label="Txt Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:inputTextarea value="<%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getTxtPreview()%>" id="txtPreview" cols="80" rows="12">
            </h:inputTextarea>
        </t:panelTab>
        <t:panelTab id="panel_e" label="Subject Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false"><%=((SysadminMassemailDetail)Pagez.getBeanMgr().get("SysadminMassemailDetail")).getSubjectPreview()%></f:verbatim>
        </t:panelTab>
    </t:panelTabbedPane>
    




<%@ include file="/jsp/templates/footer.jsp" %>



