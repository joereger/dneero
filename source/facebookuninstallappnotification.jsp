<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.User" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Date" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
if (request.getParameter("fb_sig_user") != null && Num.isinteger(request.getParameter("fb_sig_user"))) {
    //@todo somehow validate that this is a good request from the actual servers... don't want script kiddies uninstalling (not that it does any more than setting a flag)
    int facebookuserid = Integer.parseInt(request.getParameter("fb_sig_user"));
    List<User> users = HibernateUtil.getSession().createCriteria(User.class)
            .add(Restrictions.eq("facebookuserid", facebookuserid))
            .setCacheable(true)
            .list();
    for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
        User user = iterator.next();
        user.setIsfacebookappremoved(true);
        user.setFacebookappremoveddate(new Date());
        try {user.save();} catch (Exception ex) {logger.error(ex);}
    }
}
%>