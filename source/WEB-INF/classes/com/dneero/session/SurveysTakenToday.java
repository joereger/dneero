package com.dneero.session;

import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2007
 * Time: 7:21:28 PM
 */
public class SurveysTakenToday {

    public static int MAXSURVEYSPERDAY = 5;

    public static int getNumberOfSurveysTakenToday(User user){
        Logger logger = Logger.getLogger(SurveysTakenToday.class);
        if (user!=null && user.getBloggerid()>0){
            Blogger blogger = Blogger.get(user.getBloggerid());
            if (blogger!=null & blogger.getBloggerid()>0){
                Calendar startCal = Time.xDaysAgoStart(Calendar.getInstance(), 0);
                Date startDate = startCal.getTime();
                int out = 0;
                try{
                    out = ((Long)HibernateUtil.getSession().createQuery("select count(*) from Response where bloggerid='"+blogger.getBloggerid()+"' and responsedate>'"+Time.dateformatfordb(startCal)+"'").uniqueResult()).intValue();
                } catch (Exception ex){
                    logger.error(ex);
                }
                return out;
            }
        }
        return 0;
    }


}
