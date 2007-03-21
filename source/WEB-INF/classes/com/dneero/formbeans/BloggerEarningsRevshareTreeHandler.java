package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Revshare;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:35 PM
 */
public class BloggerEarningsRevshareTreeHandler implements Serializable {

    public BloggerEarningsRevshareTreeHandler(){}

    public TreeModel getTreeModel(){
        UserSession userSession = Jsf.getUserSession();

        if (userSession.getUser()!=null){
            BloggerEarningsRevshareTreeNode basenode = new BloggerEarningsRevshareTreeNode("person", userSession.getUser().getFirstname()+" "+userSession.getUser().getLastname(), "userid"+userSession.getUser().getUserid(), false, 0, 0);
            basenode = addChildren(basenode, userSession.getUser(), userSession.getUser());
            return new TreeModelBase(basenode);
        }
        return new TreeModelBase(new TreeNodeBase());
    }

    private BloggerEarningsRevshareTreeNode addChildren(BloggerEarningsRevshareTreeNode node, User user, User rootuser){
        List children = HibernateUtil.getSession().createQuery("FROM User where referredbyuserid='"+user.getUserid()+"'").list();
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            User child = (User) iterator.next();
            //Calculate earnings from this user
            Date ninetydaysago = Time.xDaysAgoStart(Calendar.getInstance(), -90).getTime();
            double amtEarnedFromThisBloggerAllTime = 0;
            double amtEarnedFromThisBlogger90Days = 0;
            if (child.getBloggerid()>0 && Blogger.get(child.getBloggerid()).getBloggerid()>0 && rootuser.getBloggerid()>0 && Blogger.get(rootuser.getBloggerid()).getBloggerid()>0){
                List revshares = HibernateUtil.getSession().createQuery("FROM Revshare where sourcebloggerid='"+child.getBloggerid()+"' AND targetbloggerid='"+rootuser.getBloggerid()+"'").list();
                for (Iterator iterator1 = revshares.iterator(); iterator1.hasNext();) {
                    Revshare revshare =  (Revshare)iterator1.next();
                    amtEarnedFromThisBloggerAllTime = amtEarnedFromThisBloggerAllTime + revshare.getAmt();
                    if (revshare.getDate().after(ninetydaysago)){
                        amtEarnedFromThisBlogger90Days = amtEarnedFromThisBlogger90Days + revshare.getAmt();
                    }
                }
            }

            //Create the node
            BloggerEarningsRevshareTreeNode newnode = new BloggerEarningsRevshareTreeNode("person", child.getFirstname()+" "+child.getLastname(), "userid"+child.getUserid(), false, amtEarnedFromThisBloggerAllTime, amtEarnedFromThisBlogger90Days);
            newnode = addChildren(newnode, child, rootuser);
            node.getChildren().add(newnode);
        }
        return node;
    }





}
