<%@ page import="java.util.List" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.test.HibernateStaticMemoryClustering" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.dneero.session.UserSession" %>
<%@ page import="com.dneero.session.Authorization" %>
<%@ page import="com.dneero.util.Str" %>
<%
//Hide from snooping eyes... only sysadmins can play
UserSession userSession = (UserSession) session.getAttribute("userSession");
if (userSession == null || !userSession.getIsloggedin() || !Authorization.isUserSysadmin(userSession.getUser())) {
    response.sendRedirect("/");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
%>
<%
if (request.getParameter("action") != null && request.getParameter("action").equals("loadtomemory")) {
    List surveys = HibernateUtil.getSession().createQuery("from Survey order by surveyid asc").setCacheable(true).setMaxResults(5).list();
    ArrayList tmp = new ArrayList();
    int[] surveyids = new int[5];
    int index = 0;
    for (Iterator iterator = surveys.iterator(); iterator.hasNext();) {
        Survey survey = (Survey) iterator.next();
        tmp.add(survey);
        surveyids[index] = survey.getSurveyid();
        index++;
    }
    HibernateStaticMemoryClustering.setSurveys(tmp);
    HibernateStaticMemoryClustering.setSurveyids(surveyids);
}
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("editlocalinstances")) {
        List surveys = HibernateUtil.getSession().createQuery("from Survey order by surveyid asc").setCacheable(true).setMaxResults(5).list();
        for (Iterator iterator = surveys.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();
            survey.setStatus(Survey.STATUS_DRAFT);
            survey.setTitle("Surveyid=" + survey.getSurveyid() + " Edited: " + Time.dateformatcompactwithtime(Calendar.getInstance()));
            try {
                survey.save();
            } catch (Exception ex) {
                logger.error("",ex);
            }
        }
    }
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("saveallinmemory")){
    List surveysMem = HibernateStaticMemoryClustering.getSurveys();
    for (Iterator iterator = surveysMem.iterator(); iterator.hasNext();) {
        Survey survey = (Survey) iterator.next();
        try{survey.save();}catch(Exception ex){logger.error("",ex);}
    }
}
%>

<a href='hibernate-staticmemory-clustering.jsp'>Refresh</a><br/><br/>
<a href='hibernate-staticmemory-clustering.jsp?action=loadtomemory'>Load to Memory</a><br/>
<a href='hibernate-staticmemory-clustering.jsp?action=editlocalinstances'>Edit Local Instance (not objs in memory)</a><br/>
<a href='hibernate-staticmemory-clustering.jsp?action=saveallinmemory'>Call save() on all in Memory</a><br/>

<%
    out.print("<br/><br/>");
    out.print("<font face=arial size=+1>Objects from Hibernate Query</font>");
    out.print("<br/>");
    out.print("<font face=arial size=-1>");
    out.print("<table cellpadding='10' cellspacing='5' border='1'>");
    //Display some objects
    List surveys = HibernateUtil.getSession().createQuery("from Survey order by surveyid asc").setCacheable(true).setMaxResults(5).list();
    for (Iterator iterator = surveys.iterator(); iterator.hasNext();) {
        Survey survey = (Survey) iterator.next();
        out.print("<tr>");
        out.print("<td valign='top'>");
        out.print(survey.getSurveyid());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getTitle());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getStatus());
        out.print("</td>");
        out.print("</tr>");
    }
    out.print("</table>");
    out.print("</font>");
%>

<%
    out.print("<br/><br/>");
    out.print("<font face=arial size=+1>Objects from Memory ArrayList</font>");
    out.print("<br/>");
    out.print("<font face=arial size=-1>");
    out.print("<table cellpadding='10' cellspacing='5' border='1'>");
    //Display some objects
    List surveysMem = HibernateStaticMemoryClustering.getSurveys();
    for (Iterator iterator = surveysMem.iterator(); iterator.hasNext();) {
        Survey survey = (Survey) iterator.next();
        out.print("<tr>");
        out.print("<td valign='top'>");
        out.print(survey.getSurveyid());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getTitle());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getStatus());
        out.print("</td>");
        out.print("</tr>");
    }
    out.print("</table>");
    out.print("</font>");
%>

<%
    out.print("<br/><br/>");
    out.print("<font face=arial size=+1>Objects from Memory int[] and Survey.get(surveyid)</font>");
    out.print("<br/>");
    out.print("<font face=arial size=-1>");
    out.print("<table cellpadding='10' cellspacing='5' border='1'>");
    //Display some objects
    int[] surveyids = HibernateStaticMemoryClustering.getSurveyids();
    for (int i = 0; i < surveyids.length; i++) {
        int surveyid = surveyids[i];
        Survey survey = Survey.get(surveyid);
        out.print("<tr>");
        out.print("<td valign='top'>");
        out.print(survey.getSurveyid());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getTitle());
        out.print("</td>");
        out.print("<td valign='top'>");
        out.print(survey.getStatus());
        out.print("</td>");
        out.print("</tr>");
    }
    out.print("</table>");
    out.print("</font>");
%>