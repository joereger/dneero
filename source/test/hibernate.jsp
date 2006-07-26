<%@ page import="com.dneero.dao.Blogger"%>
<%@ page import="com.dneero.util.GeneralException"%>
<%@ page import="com.dneero.util.Jsf"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.dneero.dao.Userrole"%>
<%@ page import="com.dneero.session.Roles"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.dneero.dao.User"%>
<%

org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass().getName());

User user = User.get(1);
if (user.getBlogger()==null){
    user.setBlogger(new Blogger());
}

out.print("<br><br>-------------Initial Load");
out.print("<br>user.getUserid()="+user.getUserid());
out.print("<br>user.getBlogger().getBloggerid()="+user.getBlogger().getBloggerid());
out.print("<br>user.user.getUserroles().size()="+user.getUserroles().size());
for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
    Userrole userrole = iterator.next();
    out.print("<br> userrole.getRoleid()="+userrole.getUserroleid()+" - ("+userrole.getUserid()+","+userrole.getRoleid()+")");
}

user.getBlogger().setBirthdate(new Date());
user.getBlogger().setGender(1);
user.getBlogger().setCity(1);
user.getBlogger().setEducationlevel(1);
user.getBlogger().setEthnicity(1);
user.getBlogger().setIncomerange(1);
user.getBlogger().setMaritalstatus(1);
user.getBlogger().setPolitics(1);
user.getBlogger().setProfession(1);
user.getBlogger().setState(1);

boolean hasroleassigned = false;
if (user!=null && user.getUserroles()!=null){
    for (Iterator iterator = user.getUserroles().iterator(); iterator.hasNext();) {
        Userrole role =  (Userrole)iterator.next();
        if (role.getRoleid()== Roles.BLOGGER){
            hasroleassigned = true;
        }
    }
}
if (!hasroleassigned && user!=null){
    Userrole role = new Userrole();
    role.setUserid(user.getUserid());
    role.setRoleid(Roles.BLOGGER);
    user.getUserroles().add(role);
}


try{
    user.save();
} catch (GeneralException gex){
    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
}

out.print("<br><br>-------------After Save");
out.print("<br>user.getUserid()="+user.getUserid());
out.print("<br>user.getBlogger().getBloggerid()="+user.getBlogger().getBloggerid());
out.print("<br>user.user.getUserroles().size()="+user.getUserroles().size());
for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
    Userrole userrole = iterator.next();
    out.print("<br> userrole.getRoleid()="+userrole.getUserroleid()+" - ("+userrole.getUserid()+","+userrole.getRoleid()+")");
}


user.refresh();

out.print("<br><br>-------------After Refresh");
out.print("<br>user.getUserid()="+user.getUserid());
out.print("<br>user.getBlogger().getBloggerid()="+user.getBlogger().getBloggerid());
out.print("<br>user.user.getUserroles().size()="+user.getUserroles().size());
for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
    Userrole userrole = iterator.next();
    out.print("<br> userrole.getRoleid()="+userrole.getUserroleid()+" - ("+userrole.getUserid()+","+userrole.getRoleid()+")");
}



%>