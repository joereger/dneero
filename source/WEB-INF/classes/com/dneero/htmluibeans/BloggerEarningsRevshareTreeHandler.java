package com.dneero.htmluibeans;


import com.dneero.dao.Blogger;
import com.dneero.dao.Revshare;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;


/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:35 PM
 */
public class BloggerEarningsRevshareTreeHandler implements Serializable {

    private ArrayList<BloggerEarningsRevshareTreeNode> tree = new ArrayList<BloggerEarningsRevshareTreeNode>();

    public BloggerEarningsRevshareTreeHandler(){}

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        UserSession userSession = Pagez.getUserSession();
        tree = new ArrayList<BloggerEarningsRevshareTreeNode>();
        if (userSession.getUser()!=null){
            tree = addChildren(userSession.getUser(), userSession.getUser(), 0);
        }
    }


    private ArrayList<BloggerEarningsRevshareTreeNode> addChildren(User user, User rootuser, int level){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<BloggerEarningsRevshareTreeNode> out = new ArrayList<BloggerEarningsRevshareTreeNode>();
        //logger.debug("=======LEVEL"+level+"=======");
        List children = HibernateUtil.getSession().createQuery("FROM User where referredbyuserid='"+user.getUserid()+"'").list();
        logger.debug("user.userid="+user.getUserid()+" name="+ user.getNickname() + " children.size()="+children.size());
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            User child = (User) iterator.next();
            //logger.debug("level="+level+" child.userid="+child.getUserid()+" name="+child.getNickname());
            //Calculate earnings from this user
            Date ninetydaysago = Time.xDaysAgoStart(Calendar.getInstance(), -90).getTime();
            double amtEarnedFromThisBloggerAllTime = 0;
            double amtEarnedFromThisBlogger90Days = 0;
            if (child.getBloggerid()>0 && Blogger.get(child.getBloggerid()).getBloggerid()>0 && rootuser.getBloggerid()>0 && Blogger.get(rootuser.getBloggerid()).getBloggerid()>0){
                //logger.debug("looking to db for Revshare: "+"FROM Revshare where sourcebloggerid='"+child.getBloggerid()+"' AND targetbloggerid='"+rootuser.getBloggerid()+"'");
                List revshares = HibernateUtil.getSession().createQuery("FROM Revshare where sourcebloggerid='"+child.getBloggerid()+"' AND targetbloggerid='"+rootuser.getBloggerid()+"'").list();
                for (Iterator iterator1 = revshares.iterator(); iterator1.hasNext();) {
                    Revshare revshare =  (Revshare)iterator1.next();
                    //logger.debug("revshare.getAmt()="+revshare.getAmt() );
                    amtEarnedFromThisBloggerAllTime = amtEarnedFromThisBloggerAllTime + revshare.getAmt();
                    if (revshare.getDate().after(ninetydaysago)){
                        amtEarnedFromThisBlogger90Days = amtEarnedFromThisBlogger90Days + revshare.getAmt();
                    }
                }
            }
            //Create the node
            //if (amtEarnedFromThisBloggerAllTime>0){
                BloggerEarningsRevshareTreeNode newnode = new BloggerEarningsRevshareTreeNode(child.getNickname(), String.valueOf(child.getUserid()), level, amtEarnedFromThisBloggerAllTime, amtEarnedFromThisBlogger90Days);
                out.add(newnode);
                //logger.debug("adding newnode: "+newnode.getDescription()+" level="+level);
            //}
            ArrayList<BloggerEarningsRevshareTreeNode> allChildren = addChildren(child, rootuser, level+1);
            //logger.debug("allChildren.size()="+allChildren.size());
            out.addAll(allChildren);
        }
        //return childTree;
        return out;
    }


    public ArrayList<BloggerEarningsRevshareTreeNode> getTree() {
        return tree;
    }

    public void setTree(ArrayList<BloggerEarningsRevshareTreeNode> tree) {
        this.tree=tree;
    }
}
