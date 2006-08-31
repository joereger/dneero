package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Revshare;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

import java.util.*;

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
public class BloggerEarningsRevshareTreeHandler  {

    public TreeModel getTreeModel(){
        UserSession userSession = Jsf.getUserSession();

        if (userSession.getUser()!=null){
            BloggerEarningsRevshareTreeNode basenode = new BloggerEarningsRevshareTreeNode("person", userSession.getUser().getFirstname()+" "+userSession.getUser().getLastname(), "userid"+userSession.getUser().getUserid(), false);
            basenode = addChildren(basenode, userSession.getUser());
            return new TreeModelBase(basenode);
        }
        return new TreeModelBase(new TreeNodeBase());
    }

    private BloggerEarningsRevshareTreeNode addChildren(BloggerEarningsRevshareTreeNode node, User user){
        List children = HibernateUtil.getSession().createQuery("FROM User where referredbyuserid='"+user.getUserid()+"'").list();
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            User child = (User) iterator.next();
            BloggerEarningsRevshareTreeNode newnode = new BloggerEarningsRevshareTreeNode("person", child.getFirstname()+" "+child.getLastname(), "userid"+child.getUserid(), false);
            newnode = addChildren(newnode, child);
            node.getChildren().add(newnode);
        }
        return node;
    }





}
