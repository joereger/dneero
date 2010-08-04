package com.dneero.sir;

import com.dneero.dao.Panel;
import com.dneero.dao.Panelmembership;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.mail.MailtypeReviewableRejection;
import com.dneero.mail.MailtypeReviewableWarning;
import com.dneero.util.Str;
import com.dneero.util.Time;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Feb 19, 2007
 * Time: 10:25:37 AM
 */
public class SocialInfluenceRating {

    public static int ALGORITHM = 1;



    //Convenience method
    public static double calculateSocialInfluenceRating(User user){
        Calendar startDate = Time.xYearsAgoStart(Calendar.getInstance(), 40);
        return calculateSocialInfluenceRating(user, startDate);
    }

    //Main calculation method
    public static double calculateSocialInfluenceRating(User user, Calendar startDate){
        Logger logger = Logger.getLogger(SocialInfluenceRating.class);
        long millisstart = Calendar.getInstance().getTimeInMillis();
        //Bump out some
        if (!user.getIsenabled() || user.getBloggerid()<=0){
            user.setSirpoints(0.0);
            user.setSirdate(new Date());
            user.setSirdebug("Not rated because not active or not a blogger.");
            user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
            try{user.save();}catch(Exception ex){logger.error("",ex);}
            return 0;
        }
        //Collect usage info for this user
        int convostotal = getConvostotal(user, startDate);
        int impressionstotal = getImpressionstotal(user, startDate);
        int responsereferrals = getResponseReferrals(user, startDate);
        double avgvenuerating = getAvgvenuerating(user, startDate);
        double avgquestionratingbysysadmin = getAvgquestionratingbysysadmin(user, startDate);
        double avgquestionratingbyresearcher = getAvgquestionratingbyresearcher(user, startDate);
        double avgresponseratingbysysadmin = getAvgresponseratingbysysadmin(user, startDate);
        double avgresponseratingbyresearcher = getAvgresponseratingbyresearcher(user, startDate);
        int peoplereferred = getPeoplereferred(user, startDate);
        int superpanels = getSuperpanels(user, startDate);
        int responsestouserquestions = getResponsestouserquestions(user, startDate);
        int convospaid = getConvospaid(user, startDate);
        double amtearnedtotal = getAmtearnedtotal(user, startDate);
        int contentrejections = getContentrejections(user, startDate);
        int contentwarnings = getContentwarnings(user, startDate);
        double impressionsperconvo = 0;
        if (convostotal>0){impressionsperconvo = new Double(impressionstotal) / new Double(convostotal);}
        double percentofconvospaid = 0;
        if (convostotal>0){percentofconvospaid = (new Double(convospaid) / new Double(convostotal)) * 100;}
        //Normalizing... choose typical/avg values for each component
        double convostotalNrml = normalizeNum(convostotal, 20);
        double impressionstotalNrml = normalizeNum(impressionstotal, 10000);
        double responsereferralsNrml = normalizeNum(responsereferrals, 5);
        double avgvenueratingNrml = normalizeNum(avgvenuerating, 5);
        double avgquestionratingbysysadminNrml = avgquestionratingbysysadmin;
        double avgquestionratingbyresearcherNrml = avgquestionratingbyresearcher;
        double avgresponseratingbysysadminNrml = avgresponseratingbysysadmin;
        double avgresponseratingbyresearcherNrml = avgresponseratingbyresearcher;
        double peoplereferredNrml = normalizeNum(peoplereferred, 3);
        double superpanelsNrml = normalizeNum(superpanels, 1);
        double responsestouserquestionsNrml = normalizeNum(responsestouserquestions, 20);
        double convospaidNrml = normalizeNum(convospaid, 20);
        double amtearnedtotalNrml = normalizeNum(amtearnedtotal, 20);
        double contentrejectionsNrml = normalizeNum(contentrejections, 1);
        double contentwarningsNrml = normalizeNum(contentwarnings, 1);
        double impressionsperconvoNrml = normalizeNum(impressionsperconvo, 400);
        double percentofconvospaidNrml = normalizeNum(percentofconvospaid, 100);
        //Weighting... choose how important each component is
        double convostotalPts = 10 * convostotalNrml;
        double impressionstotalPts = 10 * impressionstotalNrml;
        double responsereferralsPts = 10 * responsereferralsNrml;
        double avgvenueratingPts = 100 * avgvenueratingNrml;
        double avgquestionratingbysysadminPts = 50 * avgquestionratingbysysadminNrml;
        double avgquestionratingbyresearcherPts = 50 * avgquestionratingbyresearcherNrml;
        double avgresponseratingbysysadminPts = 50 * avgresponseratingbysysadminNrml;
        double avgresponseratingbyresearcherPts = 50 * avgresponseratingbyresearcherNrml;
        double peoplereferredPts = 10 * peoplereferredNrml;
        double superpanelsPts = 200 * superpanelsNrml;
        double responsestouserquestionsPts = 10 * responsestouserquestionsNrml;
        double convospaidPts = 10 * convospaidNrml;
        double amtearnedtotalPts = 10 * amtearnedtotalNrml;
        double contentrejectionsPts = -150 * contentrejectionsNrml;
        double contentwarningsPts = -75 * contentwarningsNrml;
        double impressionsperconvoPts = 30 * impressionsperconvoNrml;
        double percentofconvospaidPts = 10 * percentofconvospaidNrml;
        //Adding up the final score
        double totalpoints = 0;
        totalpoints = totalpoints + convostotalPts;
        totalpoints = totalpoints + impressionstotalPts;
        totalpoints = totalpoints + responsereferralsPts;
        totalpoints = totalpoints + avgvenueratingPts;
        totalpoints = totalpoints + avgquestionratingbysysadminPts;
        totalpoints = totalpoints + avgquestionratingbyresearcherPts;
        totalpoints = totalpoints + avgresponseratingbysysadminPts;
        totalpoints = totalpoints + avgresponseratingbyresearcherPts;
        totalpoints = totalpoints + peoplereferredPts;
        totalpoints = totalpoints + superpanelsPts;
        totalpoints = totalpoints + responsestouserquestionsPts;
        totalpoints = totalpoints + convospaidPts;
        totalpoints = totalpoints + amtearnedtotalPts;
        totalpoints = totalpoints + contentrejectionsPts;
        totalpoints = totalpoints + contentwarningsPts;
        totalpoints = totalpoints + impressionsperconvoPts;
        totalpoints = totalpoints + percentofconvospaidPts;
        long millisend = Calendar.getInstance().getTimeInMillis();
        //Debug
        StringBuffer debug = new StringBuffer();
        debug.append("\n\n");
        debug.append("====SocialInfluenceRating Start "+ user.getNickname()+" userid="+user.getUserid()+"====\n");
        debug.append("convostotal: "+f(convostotal)+"/"+f(convostotalNrml)+"/"+f(convostotalPts)+"pts/"+f(percent(convostotalPts, totalpoints))+"%"+"\n");
        debug.append("impressionstotal: "+f(impressionstotal)+"/"+f(impressionstotalNrml)+"/"+f(impressionstotalPts)+"pts/"+f(percent(impressionstotalPts, totalpoints))+"%"+"\n");
        debug.append("responsereferrals: "+f(responsereferrals)+"/"+f(responsereferralsNrml)+"/"+f(responsereferralsPts)+"pts/"+f(percent(responsereferralsPts, totalpoints))+"%"+"\n");
        debug.append("avgvenuerating: "+f(avgvenuerating)+"/"+f(avgvenueratingNrml)+"/"+f(avgvenueratingPts)+"pts/"+f(percent(avgvenueratingPts, totalpoints))+"%"+"\n");
        debug.append("avgquestionratingbysysadmin: "+f(avgquestionratingbysysadmin)+"/"+f(avgquestionratingbysysadminNrml)+"/"+f(avgquestionratingbysysadminPts)+"pts/"+f(percent(avgquestionratingbysysadminPts, totalpoints))+"%"+"\n");
        debug.append("avgquestionratingbyresearcher: "+f(avgquestionratingbyresearcher)+"/"+f(avgquestionratingbyresearcherNrml)+"/"+f(avgquestionratingbyresearcherPts)+"pts/"+f(percent(avgquestionratingbyresearcherPts, totalpoints))+"%"+"\n");
        debug.append("avgresponseratingbysysadmin: "+f(avgresponseratingbysysadmin)+"/"+f(avgresponseratingbysysadminNrml)+"/"+f(avgresponseratingbysysadminPts)+"pts/"+f(percent(avgresponseratingbysysadminPts, totalpoints))+"%"+"\n");
        debug.append("avgresponseratingbyresearcher: "+f(avgresponseratingbyresearcher)+"/"+f(avgresponseratingbyresearcherNrml)+"/"+f(avgresponseratingbyresearcherPts)+"pts/"+f(percent(avgresponseratingbyresearcherPts, totalpoints))+"%"+"\n");
        debug.append("peoplereferred: "+f(peoplereferred)+"/"+f(peoplereferredNrml)+"/"+f(peoplereferredPts)+"pts/"+f(percent(peoplereferredPts, totalpoints))+"%"+"\n");
        debug.append("superpanels: "+f(superpanels)+"/"+f(superpanelsNrml)+"/"+f(superpanelsPts)+"pts/"+f(percent(superpanelsPts, totalpoints))+"%"+"\n");
        debug.append("responsestouserquestions: "+f(responsestouserquestions)+"/"+f(responsestouserquestionsNrml)+"/"+f(responsestouserquestionsPts)+"pts/"+f(percent(responsestouserquestionsPts, totalpoints))+"%"+"\n");
        debug.append("convospaid: "+f(convospaid)+"/"+f(convospaidNrml)+"/"+f(convospaidPts)+"pts/"+f(percent(convospaidPts, totalpoints))+"%"+"\n");
        debug.append("amtearnedtotal: "+f(amtearnedtotal)+"/"+f(amtearnedtotalNrml)+"/"+f(amtearnedtotalPts)+"pts/"+f(percent(convostotalPts, totalpoints))+"%"+"\n");
        debug.append("contentrejections: "+f(contentrejections)+"/"+f(contentrejectionsNrml)+"/"+f(contentrejectionsPts)+"pts/"+f(percent(contentrejectionsPts, totalpoints))+"%"+"\n");
        debug.append("contentwarnings: "+f(contentwarnings)+"/"+f(contentwarningsNrml)+"/"+f(contentwarningsPts)+"pts/"+f(percent(contentwarningsPts, totalpoints))+"%"+"\n");
        debug.append("impressionsperconvo: "+f(impressionsperconvo)+"/"+f(impressionsperconvoNrml)+"/"+f(impressionsperconvoPts)+"pts/"+f(percent(impressionsperconvoPts, totalpoints))+"%"+"\n");
        debug.append("percentofconvospaid: "+f(percentofconvospaid)+"/"+f(percentofconvospaidNrml)+"/"+f(percentofconvospaidPts)+"pts/"+f(percent(percentofconvospaidPts, totalpoints))+"%"+"\n");
        debug.append("====totalpoints: "+f(totalpoints)+"\n");
        debug.append("====processingtime: "+(millisend-millisstart)+" millis"+"\n");
        logger.debug(debug);
        //Record
        user.setSirpoints(totalpoints);
        user.setSirdate(new Date());
        user.setSiralgorithm(SocialInfluenceRating.ALGORITHM);
        user.setSirdebug(debug.toString());
        try{user.save();}catch(Exception ex){logger.error("",ex);}
        //Return
        return totalpoints;
    }

    private static String f(int in){
        return f(new Double(in));
    }
    private static String f(double in){
        return Str.format(in, 2);
    }


    //Returns a normalized value
    private static double normalizeNum(double num, double normalValue){
        if (normalValue>0){
            return num/normalValue;
        }
        return 0;
    }

    private static double normalizeNum(int num, double normalValue){
        return normalizeNum(new Double(num), normalValue);
    }

    private static double percent(double numerator, double denominator){
        if (denominator>0){
            return numerator/denominator * 100;
        }
        return 0;
    }

    //-----
    //Individual component calculations
    //-----

    private static int getContentrejections(User user, Calendar startDate){
        if (user!=null){
            return NumFromUniqueResult.getInt("select count(*) from Mailchild mc, Mail m where mc.mailid=m.mailid and m.userid='"+user.getUserid()+"' and mc.mailtypeid='"+ MailtypeReviewableRejection.TYPEID +"' and m.date>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getContentwarnings(User user, Calendar startDate){
        if (user!=null){
            return NumFromUniqueResult.getInt("select count(*) from Mailchild mc, Mail m where mc.mailid=m.mailid and m.userid='"+user.getUserid()+"' and mc.mailtypeid='"+ MailtypeReviewableWarning.TYPEID +"' and m.date>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static double getAmtearnedtotal(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"' and isbloggermoney=true and date>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getConvospaid(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select count(*) from Response where bloggerid='"+user.getBloggerid()+"' and ispaid=true and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getResponsestouserquestions(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select count(*) from Question q, Questionresponse qr, Survey s where q.questionid=qr.questionid and q.surveyid=s.surveyid and q.userid='"+user.getUserid()+"' and s.enddate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getPeoplereferred(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select count(*) from User where referredbyuserid='"+user.getUserid()+"' and createdate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static double getAvgresponseratingbysysadmin(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select avg(scorebysysadmin) from Response where bloggerid='"+user.getBloggerid()+"' and scorebysysadmin>'0' and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static double getAvgresponseratingbyresearcher(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select avg(scorebyresearcher) from Response where bloggerid='"+user.getBloggerid()+"' and scorebyresearcher>'0' and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static double getAvgvenuerating(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select avg(scorebysysadmin) from Venue where bloggerid='"+user.getBloggerid()+"' and scorebysysadmin>'0'");
        }
        return 0;
    }

    private static double getAvgquestionratingbysysadmin(User user, Calendar startDate){
        //@todo incorporate startDate by joining with Response
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select avg(scorebysysadmin) from Question where userid='"+user.getUserid()+"' and scorebysysadmin>'0'");
        }
        return 0;
    }

    private static double getAvgquestionratingbyresearcher(User user, Calendar startDate){
        //@todo incorporate startDate by joining with Response
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getDouble("select avg(scorebyresearcher) from Question where userid='"+user.getUserid()+"' and scorebyresearcher>'0'");
        }
        return 0;
    }

    private static int getConvostotal(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select count(*) from Response where bloggerid='"+user.getBloggerid()+"' and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getImpressionstotal(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select sum(impressionstotal) from Response where bloggerid='"+user.getBloggerid()+"' and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }




    private static int getResponseReferrals(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            return NumFromUniqueResult.getInt("select count(*) from Response where referredbyuserid='"+user.getUserid()+"' and responsedate>'"+Time.dateformatfordb(startDate)+"'");
        }
        return 0;
    }

    private static int getSuperpanels(User user, Calendar startDate){
        if (user!=null && user.getBloggerid()>0){
            //Get arraylist of all superpanelids... should go fast w/caching
            ArrayList<Integer> superpanelids = new ArrayList<Integer>();
            List<Panel> superpanels = HibernateUtil.getSession().createCriteria(Panel.class)
                                               .add(Restrictions.eq("issystempanel", true))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Panel> panelIterator=superpanels.iterator(); panelIterator.hasNext();) {
                Panel panel=panelIterator.next();
                superpanelids.add(panel.getPanelid());
            }
            if (superpanelids.size()>0){
                //See if user's in any
                List<Panelmembership> panelmemberships = HibernateUtil.getSession().createCriteria(Panelmembership.class)
                                                   .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                                                   .add(Restrictions.in("panelid", superpanelids))
                                                   .setCacheable(true)
                                                   .list();
                if (panelmemberships!=null){
                    return panelmemberships.size();
                }
            }
        }
        return 0;
    }



}
