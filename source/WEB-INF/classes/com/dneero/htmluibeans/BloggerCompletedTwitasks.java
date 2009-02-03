package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.*;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;

import java.util.*;
import java.io.Serializable;

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
                Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                BloggerCompletedTwitasksListitem listitem = new BloggerCompletedTwitasksListitem();
                listitem.setTwitask(twitask);
                listitem.setTwitanswer(twitanswer);
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

                
                listitem.setEarningsTxt(twitask.getIncentive().getFullSummary());

                twitanswers.add(listitem);
            }
        }
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