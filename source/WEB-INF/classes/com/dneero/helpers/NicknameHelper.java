package com.dneero.helpers;

import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
        if (user==null || user.getUserid()==0){
            return nicknameExistsAlready(nickname);
        }
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

    public static String generateUniqueNickname(String nickname, User user){
        nickname = Str.onlyKeepLettersAndDigits(nickname.trim());
        if (!nicknameExistsAlreadyForSomebodyElse(nickname, user)){
            return nickname;
        }
        int appendtoend = 1;
        while (nicknameExistsAlreadyForSomebodyElse(nickname+appendtoend, user) && appendtoend<25){
            appendtoend++;
        }
        nickname = nickname + appendtoend;
        return nickname;
    }


}
