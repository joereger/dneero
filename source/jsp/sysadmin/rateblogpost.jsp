<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText value="No posts require review at this time." styleClass="largefont" rendered="<%=((!rateBlogPost)Pagez.getBeanMgr().get("!rateBlogPost")).getHaveposttoreview()%>"></h:outputText>

         <h:form rendered="<%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getHaveposttoreview()%>">
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
                                <h:selectOneRadio value="<%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getQuality()%>" id="quality" required="true" layout="lineDirection">
                                    <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getBlogqualities()%>"/>
                                </h:selectOneRadio>
                            </td>
                            <td valign="top">
                                <font class="largefont">Good</font>
                            </td>
                            <td valign="top">
                                <img src="/images/clear.gif" width="10" height="1"></img>
                            </td>
                            <td valign="top">
                                <h:commandButton action="<%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getRateAction()%>" value="Rate Post" styleClass="formsubmitbutton"></h:commandButton>
                                <f:verbatim><br/></f:verbatim>
                                <h:outputText value="<%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getRemainingtoreview()%> posts remaining" styleClass="tinyfont"></h:outputText>
                            </td>
                        </tr>
                    </table>
                </center>
            </div>
            <center><font class="tinyfont" style="color: #666666;"><%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getImpression().getReferer()%></font></center>

            <f:verbatim><br/></f:verbatim>
            <h:outputText value="<%=((RateBlogPost)Pagez.getBeanMgr().get("RateBlogPost")).getIframestr()%>" escape="false"></h:outputText>



<%@ include file="/jsp/templates/footer.jsp" %>