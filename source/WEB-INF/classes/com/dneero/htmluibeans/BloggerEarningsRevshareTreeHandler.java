package com.dneero.htmluibeans;


import com.dneero.util.Time;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Revshare;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

import java.util.*;
import java.io.Serializable;


/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:35 PM
 */
public class BloggerEarningsRevshareTreeHandler implements Serializable {

    private ArrayList<BloggerEarningsRevshareTreeNode> tree = new ArrayList<BloggerEarningsRevshareTreeNode>();;

    public BloggerEarningsRevshareTreeHandler(){}

    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        tree = new ArrayList<BloggerEarningsRevshareTreeNode>();
        if (userSession.getUser()!=null){
            tree = addChildren(userSession.getUser(), userSession.getUser(), tree, 0);
        }
    }


    private ArrayList<BloggerEarningsRevshareTreeNode> addChildren(User user, User rootuser, ArrayList<BloggerEarningsRevshareTreeNode> childTree, int level){
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
            if (amtEarnedFromThisBloggerAllTime>0){
                BloggerEarningsRevshareTreeNode newnode = new BloggerEarningsRevshareTreeNode(child.getFirstname()+" "+child.getLastname(), "userid"+child.getUserid(), level, amtEarnedFromThisBloggerAllTime, amtEarnedFromThisBlogger90Days);
                childTree.add(newnode);
            }
            childTree.addAll(addChildren(child, rootuser, childTree, level+1));
        }
        return childTree;
    }


    public ArrayList<BloggerEarningsRevshareTreeNode> getTree() {
        return tree;
    }

    public void setTree(ArrayList<BloggerEarningsRevshareTreeNode> tree) {
        this.tree=tree;
    }
}
