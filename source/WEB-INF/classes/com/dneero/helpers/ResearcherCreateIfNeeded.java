package com.dneero.helpers;

import com.dneero.dao.Researcher;
import com.dneero.dao.Userrole;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Mar 27, 2010
 * Time: 1:21:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResearcherCreateIfNeeded {

    public static void createIfNecessary(){
        Logger logger = Logger.getLogger(ResearcherCreateIfNeeded.class);
        if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getResearcherid()==0) ){
            try{
                Researcher researcher = new Researcher();
                researcher.setUserid(Pagez.getUserSession().getUser().getUserid());
                researcher.setCompanyname("");
                researcher.setCompanytype("Other");
                researcher.setPhone("");
                researcher.save();
                Pagez.getUserSession().getUser().setResearcherid(researcher.getResearcherid());
                Pagez.getUserSession().getUser().save();
                Userrole role = new Userrole();
                role.setUserid(Pagez.getUserSession().getUser().getUserid());
                role.setRoleid(Userrole.RESEARCHER);
                Pagez.getUserSession().getUser().getUserroles().add(role);
                role.save();
                Pagez.getUserSession().getUser().save();
                Pagez.getUserSession().getUser().refresh();
            } catch (Exception ex){
                logger.error("", ex);
                Pagez.getUserSession().setMessage("There has been some sort of error creating the Researcher object. Please try again.");
                Pagez.sendRedirect("/account/index.jsp");
                return;
            }
        }
    }


}
