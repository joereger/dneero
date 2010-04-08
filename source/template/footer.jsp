<%@ page import="com.dneero.pageperformance.PagePerformanceUtil" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <!--</div>-->
    <%
    String templateFName = "pagefooter-plid-"+Pagez.getUserSession().getPl().getPlid();
    String templateF = PlTemplate.getWebhtmlfooter(Pagez.getUserSession().getPl());
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    velocityContext.put("instancename", InstanceProperties.getInstancename());
    velocityContext.put("elapsedTime", Pagez.getElapsedTime());
    velocityContext.put("googleanalyticsidweb", Pagez.getUserSession().getPl().getGoogleanalyticsidweb());
    String footer = TemplateProcessor.process(templateFName, templateF, velocityContext);
    %>
    <%=footer%>
<% } else { %>
    <%@ include file="footer-facebook.jsp" %>
<% }%>

<%
    //Performance recording
    try {
        String prePendPageId = "";
        if (Pagez.getUserSession().getIsfacebookui()){
            prePendPageId = "[FB]";
        }
        long elapsedtimeFooter = Pagez.getElapsedTime();
        PagePerformanceUtil.add(prePendPageId+request.getServletPath(), InstanceProperties.getInstancename(), elapsedtimeFooter);
    } catch (Exception ex) {
        logger.error("", ex);
    }
%>
