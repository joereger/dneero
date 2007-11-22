<%@ page import="com.dneero.htmlui.UserSession" %>
<%@ page import="com.dneero.htmlui.Authorization" %>
<%
//Hide from snooping eyes... only sysadmins can play
UserSession userSession = (UserSession) session.getAttribute("userSession");
if (userSession == null || !userSession.getIsloggedin() || !Authorization.isUserSysadmin(userSession.getUser())) {
    response.sendRedirect("/");
    return;
}
%>
<%

//    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass().getName());
//
//    User user = User.get(1);
//    Blogger blogger = Blogger.get(user.getBloggerid());
//
//    out.print("<br><br>-------------Initial Load");
//    out.print("<br>user.getUserid()=" + user.getUserid());
//    out.print("<br>blogger.getBloggerid()=" + blogger.getBloggerid());
//    out.print("<br>user.user.getUserroles().size()=" + user.getUserroles().size());
//    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
//        Userrole userrole = iterator.next();
//        out.print("<br> userrole.getRoleid()=" + userrole.getUserroleid() + " - (" + userrole.getUserid() + "," + userrole.getRoleid() + ")");
//    }
//
//    blogger.setBirthdate(new Date());
//    blogger.setGender("1");
//    blogger.setCity("1");
//    blogger.setEducationlevel("1");
//    blogger.setEthnicity("1");
//    blogger.setIncomerange("1");
//    blogger.setMaritalstatus("1");
//    blogger.setPolitics("1");
//    blogger.setProfession("1");
//    blogger.setState("1");
//
//    boolean hasroleassigned = false;
//    if (user != null && user.getUserroles() != null) {
//        for (Iterator iterator = user.getUserroles().iterator(); iterator.hasNext();) {
//            Userrole role = (Userrole) iterator.next();
//            if (role.getRoleid() == Userrole.BLOGGER) {
//                hasroleassigned = true;
//            }
//        }
//    }
//    if (!hasroleassigned && user != null) {
//        Userrole role = new Userrole();
//        role.setUserid(user.getUserid());
//        role.setRoleid(Userrole.BLOGGER);
//        user.getUserroles().add(role);
//    }
//
//
//    try {
//        user.save();
//    } catch (GeneralException gex) {
//        logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
//    }
//
//    out.print("<br><br>-------------After Save");
//    out.print("<br>user.getUserid()=" + user.getUserid());
//    out.print("<br>blogger.getBloggerid()=" + blogger.getBloggerid());
//    out.print("<br>user.user.getUserroles().size()=" + user.getUserroles().size());
//    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
//        Userrole userrole = iterator.next();
//        out.print("<br> userrole.getRoleid()=" + userrole.getUserroleid() + " - (" + userrole.getUserid() + "," + userrole.getRoleid() + ")");
//    }
//
//
//    user.refresh();
//
//    out.print("<br><br>-------------After Refresh");
//    out.print("<br>user.getUserid()=" + user.getUserid());
//    out.print("<br>blogger.getBloggerid()=" + blogger.getBloggerid());
//    out.print("<br>user.user.getUserroles().size()=" + user.getUserroles().size());
//    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
//        Userrole userrole = iterator.next();
//        out.print("<br> userrole.getRoleid()=" + userrole.getUserroleid() + " - (" + userrole.getUserid() + "," + userrole.getRoleid() + ")");
//    }


%>