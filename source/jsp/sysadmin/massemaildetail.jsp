<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Mass Email Detail: #{sysadminMassemailDetail.massemail.subject}";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{sysadminMassemailDetail}"/>

    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_b" label="Details">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:panelGrid columns="3" cellpadding="3" border="0">


                <h:panelGroup>
                    <h:outputText value="Subject" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{sysadminMassemailDetail.massemail.subject}" id="subject" required="true" size="60" maxlength="200"></h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="subject" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Text Message" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{sysadminMassemailDetail.massemail.txtmessage}" id="txtmessage" required="true" cols="60" rows="8">
                    </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="txtmessage" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Html Message" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{sysadminMassemailDetail.massemail.htmlmessage}" id="htmlmessage" required="true" cols="60" rows="8">
                    </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="htmlmessage" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Send to Bloggers?" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox id="issenttobloggers" value="#{sysadminMassemailDetail.massemail.issenttobloggers}"/>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="issenttobloggers" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Send to Researchers?" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox id="issenttoresearchers" value="#{sysadminMassemailDetail.massemail.issenttoresearchers}"/>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="issenttoresearchers" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="(Dangerous!)Send to Users who opted out of non-critical email?" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox id="issenttouserswhooptoutofnoncriticalemails" value="#{sysadminMassemailDetail.massemail.issenttouserswhooptoutofnoncriticalemails}"/>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="issenttouserswhooptoutofnoncriticalemails" styleClass="RED"></h:message>
                </h:panelGroup>

             </h:panelGrid>

             <h:commandButton action="#{sysadminMassemailDetail.save}" value="Save and Preview" styleClass="formsubmitbutton"></h:commandButton>
             <h:commandButton action="#{sysadminMassemailDetail.send}" value="Send" styleClass="formsubmitbutton"></h:commandButton>

             <br/><br/>
             <font class="formfieldnamefont">Test-send to this email address:</font>
             <br/>
             <h:inputText value="#{sysadminMassemailDetail.testsendemailaddress}" id="testsendemailaddress" size="20" maxlength="200"></h:inputText>
             <h:commandButton action="#{sysadminMassemailDetail.testSend}" value="Test Send" styleClass="formsubmitbutton"></h:commandButton>

             <br/><br/>
             <font class="formfieldnamefont">Create a copy of this Mass Email:</font>
             <br/>
             <h:commandButton action="#{sysadminMassemailDetail.copy}" value="Copy" styleClass="formsubmitbutton"></h:commandButton>

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
            <f:verbatim escape="false">#{sysadminMassemailDetail.htmlPreview}</f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_d" label="Txt Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:inputTextarea value="#{sysadminMassemailDetail.txtPreview}" id="txtPreview" cols="80" rows="12">
            </h:inputTextarea>
        </t:panelTab>
        <t:panelTab id="panel_e" label="Subject Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false">#{sysadminMassemailDetail.subjectPreview}</f:verbatim>
        </t:panelTab>
    </t:panelTabbedPane>
    


</h:form>

</ui:define>


</ui:composition>
</html>



