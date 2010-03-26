package com.dneero.htmluibeans;

import com.dneero.dao.Twitask;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherTwitaskDetail02 implements Serializable {

    private String title;
    private Twitask twitask;



    public ResearcherTwitaskDetail02(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            logger.debug("Found twitask in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            title = twitask.getQuestion();
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("twitask.canEdit(Pagez.getUserSession().getUser())="+twitask.canEdit(Pagez.getUserSession().getUser()));
                //If we're already authorized for Twitter rock on and redir to the next step in the process
                if (twitask.getTwitteraccesstoken()!=null && !twitask.getTwitteraccesstoken().equals("")){
                    if (twitask.getTwitteraccesstokensecret()!=null && !twitask.getTwitteraccesstokensecret().equals("")){
                        Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+twitask.getTwitaskid());
                        return;
                    }
                }
            }
        }
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask = twitask;
    }
}