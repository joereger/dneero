package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.Venue;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Dec 11, 2008
 * Time: 1:32:08 PM
 */
public class UserProfileCompletenessChecker {

    public static boolean isProfileComplete(User user){
        Logger logger = Logger.getLogger(UserProfileCompletenessChecker.class);
        logger.debug("isProfileComplete(userid="+user.getUserid()+") called");
        if (user.getBloggerid()<=0){
            logger.debug("returning true for userid="+user.getUserid()+" because user.getBloggerid()<=0");
            return true;
        }
        Pl pl = Pl.get(user.getPlid());
        if (!pl.getIsbloggerdemographicrequired()){
            logger.debug("returning true for userid="+user.getUserid()+" because !pl.getIsbloggerdemographicrequired()");
            return true;
        }
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger!=null && blogger.getBloggerid()>0){
            //We've got a blogger... let's see if the profile's up to date, using accurate info, etc

            if (!DemographicsXML.isDemographicProfileOK(user)){
                logger.debug("return false because blogger's demographic fields aren't complete");
                return false;
            }


            if (user.getFacebookuserid()<=0){
                if (Pagez.getUserSession().getPl().getIsvenuerequired()){
                    int venuecount = 0;
                    if (blogger.getVenues()!=null && blogger.getVenues().size()>0){
                        for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                            Venue venue=iterator.next();
                            if (venue.getIsactive() && !venue.getIssysadminrejected()){
                                venuecount = venuecount + 1;
                            }
                        }
                    }
                    if (venuecount==0){
                        logger.debug("return false because at least one posting venue url required");
                        return false;
                    }
                }
            }

        }
        return true;
    }



}
