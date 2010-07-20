<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicFacebookAppAdd" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%String jspPageName="/facebookappadd.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = Pagez.getUserSession().getPl().getNameforui()+" "+Pagez._Surveys();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


        <table cellpadding="0" border="0" width="100%">
            <tr>
                <td valign="top">
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">

                        <table border="0" cellspacing="10">
                        <tr>
                        <td width="25%">
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <center>
                                    <a href="<%=((com.dneero.htmluibeans.PublicFacebookAppAdd)Pagez.getBeanMgr().get("PublicFacebookAppAdd")).getAddurl()%>" target="_top"><img src="/images/add-64.png" alt="" border="0"/></a>
                                    <br/><a href="<%=((PublicFacebookAppAdd)Pagez.getBeanMgr().get("PublicFacebookAppAdd")).getAddurl()%>" target="_top"><font class="formfieldnamefont">Add</font></a>
                                </center>
                            </div>
                        </td>
                        <td>
                            <font class="normalfont" style="font-weight: bold;">You need to <a href="<%=((PublicFacebookAppAdd)Pagez.getBeanMgr().get("PublicFacebookAppAdd")).getAddurl()%>" target="_top">add the <%=Pagez.getUserSession().getPl().getNameforui()%> Facebook application</a> before you can take part in <%=Pagez._surveys()%>.  <%=Pagez._Surveys()%> allow you to earn real money joining <%=Pagez._surveys()%> and posting links to your answers on your profile.  There are no gimmicks, tricks or abusive processes to follow.</font><br/>
                        </td>
                        </tr>
                        </table>
                    </div>

                </td>
            </tr>
        </table>



<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>