package com.dneero.twitter;

import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Feb 4, 2009
 * Time: 9:31:27 AM
 */
public class TwitterUsernameAlreadyInUse {

    public static boolean usernameExistsAlready(String twitterusername){
        //Fix once OAuth is implemented
        return false;
//        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
//                                           .add(Restrictions.eq("instantnotifytwitterusername", twitterusername.trim()))
//                                           .setCacheable(true)
//                                           .list();
//        if (users!=null && users.size()>0){
//            return true;
//        }
//        return false;
    }

    public static boolean usernameExistsAlreadyForSomebodyElse(String twitterusername, User user){
        //Fix once OAuth is implemented
        return false;
//        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
//                                           .add(Restrictions.eq("instantnotifytwitterusername", twitterusername.trim()))
//                                           .add(Restrictions.ne("userid", user.getUserid()))
//                                           .setCacheable(true)
//                                           .list();
//        if (users!=null && users.size()>0){
//            return true;
//        }
//        return false;
    }

}
