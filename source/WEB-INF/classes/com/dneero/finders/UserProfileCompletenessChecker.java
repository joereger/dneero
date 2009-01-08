package com.dneero.finders;

import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.Venue;
import com.dneero.constants.*;
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
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger!=null && blogger.getBloggerid()>0){
            //We've got a blogger... let's see if the profile's up to date, using accurate info, etc

            if (blogger.getGender()==null || !Genders.get().contains(blogger.getGender())){
                logger.debug("returning false because Gender not a valid value");
                return false;
            }
            if (blogger.getEthnicity()==null || !Ethnicities.get().contains(blogger.getEthnicity())){
                logger.debug("returning false because Ethnicity not a valid value");
                return false;
            }
            if (blogger.getMaritalstatus()==null || !Maritalstatuses.get().contains(blogger.getMaritalstatus())){
                logger.debug("returning false because Marital Status not a valid value");
                return false;
            }
            if (blogger.getIncomerange()==null || !Incomes.get().contains(blogger.getIncomerange())){
                logger.debug("returning false because Income Range not a valid value");
                return false;
            }
            if (blogger.getEducationlevel()==null || !Educationlevels.get().contains(blogger.getEducationlevel())){
                logger.debug("returning false because Education Level not a valid value");
                return false;
            }
            if (blogger.getCity()==null || !Cities.get().contains(blogger.getCity())){
                logger.debug("returning false because City not a valid value");
                return false;
            }
            if (blogger.getState()==null || !States.get().contains(blogger.getState())){
                logger.debug("returning false because State not a valid value");
                return false;
            }
            if (blogger.getCountry()==null || !Countries.get().contains(blogger.getCountry())){
                logger.debug("returning false because Country not a valid value");
                return false;
            }
            if (blogger.getProfession()==null || !Professions.get().contains(blogger.getProfession())){
                logger.debug("returning false because Profession not a valid value");
                return false;
            }
//            if (blogger.getBlogfocus()==null || !Blogfocuses.get().contains(blogger.getBlogfocus())){
//                logger.debug("returning false because Blog Focus not a valid value");
//                return false;
//            }
            if (user.getFacebookuserid()<=0){
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
        return true;
    }



}
