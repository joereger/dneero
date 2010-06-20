package com.dneero.htmluibeans;

import com.dneero.dao.Twitanswer;
import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerCompletedTwitasks implements Serializable {

    private ArrayList<BloggerCompletedTwitasksListitem> twitanswers;
    private int maxtodisplay = 10000;


    public BloggerCompletedTwitasks(){

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        twitanswers = new ArrayList<BloggerCompletedTwitasksListitem>();
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            List<Twitanswer> tanss = HibernateUtil.getSession().createQuery("from Twitanswer where userid='"+userSession.getUser().getUserid()+"' order by twitanswerid desc").setMaxResults(maxtodisplay).setCacheable(true).list();
            for (Iterator<Twitanswer> iterator = tanss.iterator(); iterator.hasNext();) {
                Twitanswer twitanswer = iterator.next();
                if (twitanswer!=null && twitanswer.getTwitaskid()>0){
                    Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                    if (twitask!=null && twitask.getTwitaskid()>0){
                        BloggerCompletedTwitasksListitem listitem = new BloggerCompletedTwitasksListitem();
                        listitem.setTwitask(twitask);
                        listitem.setTwitanswer(twitanswer);

                        //Status
                        if (twitanswer.getStatus()==Twitanswer.STATUS_ALREADYANSWERED){
                            listitem.setStatusTxt("You've already answered this Twitter Question.");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_APPROVED){
                            if (twitanswer.getIspaid()){
                                listitem.setStatusTxt("Paid");
                            } else {
                                listitem.setStatusTxt("Approved, Payment is Pending");
                            }
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_DOESNTQUALIFY){
                            listitem.setStatusTxt("Your profile doesn't qualify for this question.  This is just part of the game.  Keep trying... lots more questions.");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_NOBLOGGER){
                            listitem.setStatusTxt("You need to activate your Social Person profile by clicking the Social People tab.");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_NOTWITASK){
                            listitem.setStatusTxt("Your reply wasn't associated with a question.  In Twitter you need to click the Reply button and then type your answer.");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_PENDINGREVIEW){
                            listitem.setStatusTxt("Pending Review");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_REJECTED){
                            listitem.setStatusTxt("Rejected");
                        }
                        if (twitanswer.getStatus()==Twitanswer.STATUS_TOOLATE){
                            listitem.setStatusTxt("Too late... the Twitter Question had already gotten enough responses... you've got to be quick!");
                        }

                        //Earnings text
                        listitem.setEarningsTxt(twitask.getIncentive().getFullSummary());

                        //Html pay form for user to choose award
                        listitem.setHtmlpayform(getHtmlPayForm(twitask, twitanswer));

                        //Add to list
                        twitanswers.add(listitem);
                    }
                }
            }
        }
    }


    private String getHtmlPayForm(Twitask twitask, Twitanswer twitanswer){
        StringBuffer htmlpayform = new StringBuffer();
        //Note that this same if statement appears on BloggerCompletedTwitasks.java and bloggercompletedtwitasks.jsp
        if (twitanswer.getStatus()==Twitanswer.STATUS_APPROVED && !twitanswer.getIspaid() && !twitanswer.getIssysadminrejected() && twitanswer.getIscriteriaxmlqualified()){
            htmlpayform.append("<br/>");
            htmlpayform.append("<div class=\"rounded\" style=\"background: #ffffff; padding: 5px;\">");
            htmlpayform.append("<form action=\"/blogger/bloggercompletedtwitasks.jsp\" method=\"post\" name=\"surveyform\" style=\"margin: 0px; padding: 0px;\">");
            htmlpayform.append("<input type=\"hidden\" name=\"dpage\" value=\"/blogger/bloggercompletedtwitasks.jsp\">");
            htmlpayform.append("<input type=\"hidden\" name=\"action\" value=\"handleaward\">");
            htmlpayform.append("<input type=\"hidden\" name=\"twitanswerid\" value=\""+twitanswer.getTwitanswerid()+"\">");
            htmlpayform.append("<table cellpadding=\"5\" cellspacing=\"0\" border=\"0\">");
            htmlpayform.append("<tr>");
            htmlpayform.append("<td valign=\"top\" align=\"left\" width=\"20\">");
            if (!twitask.getIscharityonly()){
            htmlpayform.append("<input type=\"checkbox\" name=\"charity-isforcharity\" value=\"1\"/>");
            }
            if (twitask.getIscharityonly()){
            htmlpayform.append("<input type=\"hidden\" name=\"charity-isforcharity\" value=\"1\"/>");
            }
            htmlpayform.append("</td>");
            htmlpayform.append("<td valign=\"top\" width=\"155\" align=\"left\">");
            if (!twitask.getIscharityonly()){
            htmlpayform.append("<font class=\"formfieldnamefont\">Don't Pay Me. Give My Earnings to this Charity:</font>");
            }
            if (twitask.getIscharityonly()){
            htmlpayform.append("<font class=\"formfieldnamefont\">Earnings Must be Given to Charity:</font>");
            }
            htmlpayform.append("<br/>");
            htmlpayform.append("<select name=\"charity-charityname\">");
            if (!twitask.getCharityonlyallowcustom()){
            htmlpayform.append("<option value=\"Habitat for Humanity\">Habitat for Humanity</option>");
            htmlpayform.append("<option value=\"Make-A-Wish Foundation\">Make-A-Wish Foundation</option>");
            htmlpayform.append("<option value=\"American Cancer Society\">American Cancer Society</option>");
            htmlpayform.append("<option value=\"PetSmart Charities\">PetSmart Charities</option>");
            htmlpayform.append("<option value=\"Wikimedia Foundation\">Wikimedia Foundation</option>");
            htmlpayform.append("<option value=\"The Conservation Fund\">The Conservation Fund</option>");
            }
            if (!twitask.getCharitycustom().equals("")){
            htmlpayform.append("<option value=\""+Str.cleanForHtml(twitask.getCharitycustom())+"\">"+twitask.getCharitycustom()+"</option>");
            }
            htmlpayform.append("</select>");
            htmlpayform.append("<br/>");
            htmlpayform.append("<font class=\"tinyfont\">");
            htmlpayform.append("If you check the box we'll donate your earnings to the charity of your choice.");
            htmlpayform.append("</font>");
            htmlpayform.append("</td>");
            htmlpayform.append("<td valign=\"top\" align=\"left\">");
            htmlpayform.append("<font class=\"tinyfont\">");
            htmlpayform.append("Learn about each of the charities:");
            if (!twitask.getCharityonlyallowcustom()){
            htmlpayform.append("<br/><a href=\"http://www.habitat.org/\" target=\"charity\">Habitat for Humanity</a>");
            htmlpayform.append("<br/><a href=\"http://www.wish.org/\" target=\"charity\">Make-A-Wish Foundation</a>");
            htmlpayform.append("<br/><a href=\"http://www.cancer.org/\" target=\"charity\">American Cancer Society</a>");
            htmlpayform.append("<br/><a href=\"http://www.petsmartcharities.org/\" target=\"charity\">PetSmart Charities</a>");
            htmlpayform.append("<br/><a href=\"http://en.wikipedia.org/wiki/Wikimedia_Foundation\" target=\"charity\">Wikimedia Foundation</a>");
            htmlpayform.append("<br/><a href=\"http://www.conservationfund.org/\" target=\"charity\">The Conservation Fund</a>");
            }
            if (!twitask.getCharitycustom().equals("")){
            htmlpayform.append("<br/><a href=\""+Str.cleanForHtml(twitask.getCharitycustomurl())+"\" target=\"charity\">"+twitask.getCharitycustom()+"</a>");
            }
            htmlpayform.append("</font>");
            htmlpayform.append("</td>");
            htmlpayform.append("<td valign=\"top\">");
            htmlpayform.append("<input type=\"submit\" class=\"formsubmitbutton\" value=\"Claim My Award\">");
            htmlpayform.append("</td>");
            htmlpayform.append("</tr>");
            htmlpayform.append("</table>");
            htmlpayform.append("</form>");
            htmlpayform.append("</div>");
        }
        return htmlpayform.toString();
    }

    public ArrayList<BloggerCompletedTwitasksListitem> getTwitanswers() {
        return twitanswers;
    }

    public void setTwitanswers(ArrayList<BloggerCompletedTwitasksListitem> twitanswers) {
        this.twitanswers=twitanswers;
    }

    public int getMaxtodisplay() {
        return maxtodisplay;
    }

    public void setMaxtodisplay(int maxtodisplay) {
        this.maxtodisplay=maxtodisplay;
    }
}