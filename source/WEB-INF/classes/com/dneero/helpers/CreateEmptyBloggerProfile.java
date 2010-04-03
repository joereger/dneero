package com.dneero.helpers;

import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Apr 3, 2010
 * Time: 7:53:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateEmptyBloggerProfile {

    public static Blogger create(User user){
        Logger logger = Logger.getLogger(CreateEmptyBloggerProfile.class);

        Blogger blogger = new Blogger();
        blogger.setUserid(user.getUserid());
        blogger.setBirthdate(Time.xYearsAgoStart(Calendar.getInstance(), 25).getTime());
        blogger.setEducationlevel("NA");
        blogger.setEthnicity("NA");
        blogger.setGender("NA");
        blogger.setIncomerange("NA");
        blogger.setMaritalstatus("NA");
        blogger.setState("NA");
        blogger.setCity("NA");
        blogger.setProfession("NA");
        blogger.setPolitics("NA");
        blogger.setBlogfocus("NA");
        blogger.setCountry("NA");

        try{
            blogger.save();
        } catch (Exception ex){
            logger.error("", ex);
        }

        try{
            user.setBloggerid(blogger.getBloggerid());
            user.save();
        } catch (Exception ex){
            logger.error("", ex);
        }

        //User role
        Userrole role = new Userrole();
        role.setUserid(user.getUserid());
        role.setRoleid(Userrole.BLOGGER);
        user.getUserroles().add(role);
        try{
            role.save();
            user.save();
        } catch (Exception ex){
            logger.error("", ex);
        }

        return blogger;
    }

}
