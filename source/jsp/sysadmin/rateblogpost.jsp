<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText value="No posts require review at this time." styleClass="largefont" rendered="#{!rateBlogPost.haveposttoreview}"></h:outputText>

         <h:form rendered="#{rateBlogPost.haveposttoreview}">
            <h:messages styleClass="RED"/>
            <t:saveState id="save" value="#{rateBlogPost}"/>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                <center>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td valign="top">
                                <font class="largefont">Bad</font>
                            </td>
                            <td valign="top">
                                <h:selectOneRadio value="#{rateBlogPost.quality}" id="quality" required="true" layout="lineDirection">
                                    <f:selectItems value="#{staticVariables.blogqualities}"/>
                                </h:selectOneRadio>
                            </td>
                            <td valign="top">
                                <font class="largefont">Good</font>
                            </td>
                            <td valign="top">
                                <img src="/images/clear.gif" width="10" height="1"></img>
                            </td>
                            <td valign="top">
                                <h:commandButton action="#{rateBlogPost.rateAction}" value="Rate Post" styleClass="formsubmitbutton"></h:commandButton>
                                <f:verbatim><br/></f:verbatim>
                                <h:outputText value="#{rateBlogPost.remainingtoreview} posts remaining" styleClass="tinyfont"></h:outputText>
                            </td>
                        </tr>
                    </table>
                </center>
            </div>
            <center><font class="tinyfont" style="color: #666666;">#{rateBlogPost.impression.referer}</font></center>

            <f:verbatim><br/></f:verbatim>
            <h:outputText value="#{rateBlogPost.iframestr}" escape="false"></h:outputText>

         </h:form>

    </ui:define>


</ui:composition>
</html>