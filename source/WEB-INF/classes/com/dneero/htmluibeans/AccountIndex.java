package com.dneero.htmluibeans;

import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dbgrid.Grid;
import com.dneero.dbgrid.GridCol;
import com.dneero.htmlui.Pagez;
import com.dneero.money.PendingBalanceCalculator;
import com.dneero.session.PersistentLogin;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountIndex implements Serializable {

    private String currentbalance = "$0.00";
    private boolean userhasresponsependings = false;
    private String msg = "";
    private boolean isfirsttimelogin = false;
    private String pendingearnings = "$0.00";
    private double currentbalanceDbl = 0.0;
    private double pendingearningsDbl = 0.0;
    private String bloggerCompletedSurveysHtml = "";

    public AccountIndex(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(Pagez.getUserSession().getUser()!=null && Num.isinteger(String.valueOf(Pagez.getUserSession().getUser().getUserid()))){
            PendingBalanceCalculator pbc = new PendingBalanceCalculator(Pagez.getUserSession().getUser());
            currentbalanceDbl = Pagez.getUserSession().getUser().getCurrentbalance();
            pendingearningsDbl = pbc.getPendingearnings();
            currentbalance = "$"+Str.formatForMoney(currentbalanceDbl);
            pendingearnings = "$"+Str.formatForMoney(pendingearningsDbl);

            //Set persistent login cookie, if necessary
            if (Pagez.getRequest().getParameter("keepmeloggedin")!=null && Pagez.getRequest().getParameter("keepmeloggedin").equals("1")){
                //Get all possible cookies to set
                Cookie[] cookies = PersistentLogin.getPersistentCookies(Pagez.getUserSession().getUser().getUserid(), Pagez.getRequest());
                //Add a cookies to the response
                for (int j = 0; j < cookies.length; j++) {
                    Pagez.getResponse().addCookie(cookies[j]);
                }
            }

            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            if (responsependings.size()>0){
                userhasresponsependings = true;
            }

            if (Pagez.getUserSession().getUser().getBloggerid()>0){
                generateBloggerCompletedSurveysHtml();
            }
        }

    }

    private void generateBloggerCompletedSurveysHtml(){
        StringBuffer out = new StringBuffer();
        BloggerCompletedsurveys bloggerCompletedsurveys = new BloggerCompletedsurveys();
        bloggerCompletedsurveys.setMaxtodisplay(3);
        bloggerCompletedsurveys.initBean();
        StringBuffer template = new StringBuffer();
        template.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
"                        <table cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
"                            <tr>");
         template.append("<td width=\"10%\">");
         template.append("<img src=\"/images/ok-32.png\" alt=\"\" width=\"32\" height=\"32\"/><br/><font class=\"tinyfont\"><b>Joined</b></font>");
         template.append("</td>");
         template.append("<td>\n" +
"                                    <a href=\"/survey.jsp?surveyid=<$surveyid$>\"><font class=\"normalfont\" style=\"text-decoration: none; font-weight: bold; color: #0000ff;\"><$surveytitle$></font></a>\n"+
"                                    <br/><font class=\"tinyfont\"><b><a href=\"/survey.jsp?surveyid=<$surveyid$>\">Edit Your Answers</a></b></font>\n" +
"                                </td>");
         template.append("</tr>\n" +
"                        </table>\n" +
"                    </div>");
        
    if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){
        out.append("<font class=\"smallfont\">You haven't joined any "+Pagez._surveys()+".</font>");
        out.append("<br/><a href=\"/publicsurveylist.jsp\"><font class=\"smallfont\" style=\"font-weight: bold;\">Find "+Pagez._Surveys()+" to Join</font></a>");
    } else {
        ArrayList<GridCol> cols = new ArrayList<GridCol>();
        cols.add(new GridCol("", template.toString(), false, "", "", "", ""));
        out.append(Grid.render(bloggerCompletedsurveys.getList(), cols, 3, "/account/index.jsp", "pageyourconvos"));
        out.append("<br/><a href=\"/blogger/index.jsp\"><font class=\"smallfont\" style=\"font-weight: bold;\">See All "+Pagez._Surveys()+" You've Joined</font></a>");
    }

        bloggerCompletedSurveysHtml = out.toString();
    }


    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }


    public boolean getUserhasresponsependings() {
        return userhasresponsependings;
    }

    public void setUserhasresponsependings(boolean userhasresponsependings) {
        this.userhasresponsependings = userhasresponsependings;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getIsfirsttimelogin() {
        return isfirsttimelogin;
    }

    public void setIsfirsttimelogin(boolean isfirsttimelogin) {
        this.isfirsttimelogin = isfirsttimelogin;
    }

    public String getPendingearnings() {
        return pendingearnings;
    }

    public void setPendingearnings(String pendingearnings) {
        this.pendingearnings = pendingearnings;
    }

    public double getCurrentbalanceDbl() {
        return currentbalanceDbl;
    }

    public void setCurrentbalanceDbl(double currentbalanceDbl) {
        this.currentbalanceDbl = currentbalanceDbl;
    }

    public double getPendingearningsDbl() {
        return pendingearningsDbl;
    }

    public void setPendingearningsDbl(double pendingearningsDbl) {
        this.pendingearningsDbl = pendingearningsDbl;
    }

    public String getBloggerCompletedSurveysHtml() {
        return bloggerCompletedSurveysHtml;
    }

    public void setBloggerCompletedSurveysHtml(String bloggerCompletedSurveysHtml) {
        this.bloggerCompletedSurveysHtml = bloggerCompletedSurveysHtml;
    }
}
