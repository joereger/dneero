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
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            twitanswers = new ArrayList<BloggerCompletedTwitasksListitem>();
            List<Twitanswer> tanss = HibernateUtil.getSession().createQuery("from Twitanswer where userid='"+userSession.getUser().getUserid()+"' order by twitanswerid desc").setMaxResults(maxtodisplay).setCacheable(true).list();
            for (Iterator<Twitanswer> iterator = tanss.iterator(); iterator.hasNext();) {
                Twitanswer twitanswer = iterator.next();
                Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                BloggerCompletedTwitasksListitem listitem = new BloggerCompletedTwitasksListitem();
                listitem.setTwitask(twitask);
                listitem.setTwitanswer(twitanswer);
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