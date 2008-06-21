<%@ page import="com.dneero.pageperformance.PagePerformanceUtil" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%
    String templateName = "";
    String template = "";
    if (Pagez.getUserSession().getPl()!=null && Pagez.getUserSession().getPl().getWebhtmlfooter()!=null && Pagez.getUserSession().getPl().getWebhtmlfooter().length()>0){
        template = Pagez.getUserSession().getPl().getWebhtmlfooter();
        templateName = "pageheader-plid-"+Pagez.getUserSession().getPl().getPlid();
    } else {
        template = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/footer-dneero.vm").toString();
        templateName = "pagefooter-plid-default";
    }
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    velocityContext.put("instancename", InstanceProperties.getInstancename());
    velocityContext.put("elapsedTime", Pagez.getElapsedTime());
    String footer = TemplateProcessor.process(templateName, template, velocityContext);
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
