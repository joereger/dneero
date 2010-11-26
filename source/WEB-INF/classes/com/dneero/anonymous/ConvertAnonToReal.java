package com.dneero.anonymous;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.helpers.CreateEmptyBloggerProfile;
import com.dneero.helpers.DeleteUser;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Nov 26, 2010
 * Time: 9:44:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertAnonToReal {


    public static void convert(User anonUser, User realUser){
        Logger logger = Logger.getLogger(ConvertAnonToReal.class);
        //Get anonBloggerid
        int anonBloggerid = 0;
        if (anonUser.getBloggerid()<=0){
            Blogger anonBlogger = CreateEmptyBloggerProfile.create(anonUser);
        } else {
            anonBloggerid = anonUser.getBloggerid();
        }
        //Get realBloggerid
        int realBloggerid = 0;
        if (realUser.getBloggerid()<=0){
            Blogger realBlogger = CreateEmptyBloggerProfile.create(realUser);
        } else {
            realBloggerid = realUser.getBloggerid();
        }
        //Move responses to realUser
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", anonBloggerid))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> responseIterator = responses.iterator(); responseIterator.hasNext();) {
            Response response = responseIterator.next();
            response.setBloggerid(realBloggerid);
            try{response.save();}catch(Exception ex){logger.error("", ex);}
        }
        //Move questions
        List<Question> questions = HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("userid", anonUser.getUserid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Question> questionIterator = questions.iterator(); questionIterator.hasNext();) {
            Question question = questionIterator.next();
            question.setUserid(realUser.getUserid());
            try{question.save();}catch(Exception ex){logger.error("", ex);}
        }
        //Move Balance
        List<Balance> balances = HibernateUtil.getSession().createCriteria(Balance.class)
                                           .add(Restrictions.eq("userid", anonUser.getUserid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Balance> balanceIterator = balances.iterator(); balanceIterator.hasNext();) {
            Balance balance = balanceIterator.next();
            balance.setUserid(realUser.getUserid());
            try{balance.save();}catch(Exception ex){logger.error("", ex);}
        }
        //Move Balancetransaction
        List<Balancetransaction> balancetransactions = HibernateUtil.getSession().createCriteria(Balancetransaction.class)
                                           .add(Restrictions.eq("userid", anonUser.getUserid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Balancetransaction> balanceIterator = balancetransactions.iterator(); balanceIterator.hasNext();) {
            Balancetransaction balancetransaction = balanceIterator.next();
            balancetransaction.setUserid(realUser.getUserid());
            try{balancetransaction.save();}catch(Exception ex){logger.error("", ex);}
        }
        //Delete anon user
        DeleteUser.delete(anonUser);

    }


}
