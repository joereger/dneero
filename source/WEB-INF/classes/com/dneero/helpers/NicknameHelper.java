package com.dneero.helpers;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.List;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.util.Util;
import com.dneero.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Jan 14, 2009
 * Time: 1:05:53 PM
 */
public class NicknameHelper {

    public static boolean nicknameExistsAlready(String nickname){
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("nickname", nickname.trim().toLowerCase()))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>0){
            return true;
        }
        return false;
    }

    public static boolean nicknameExistsAlreadyForSomebodyElse(String nickname, User user){
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("nickname", nickname.trim().toLowerCase()))
                                           .add(Restrictions.ne("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>0){
            return true;
        }
        return false;
    }

    public static String getNameOrNickname(User user){
        if (user!=null && user.getUserid()>0){
            if (user.getNickname()!=null && !user.getNickname().equals("")){
                return user.getNickname();
            } else {
                return user.getFirstname()+" "+user.getLastname();
            }
        }
        return "";
    }






}
