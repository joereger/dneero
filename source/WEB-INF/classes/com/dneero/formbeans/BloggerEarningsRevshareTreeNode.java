package com.dneero.formbeans;

import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * User: Joe Reger Jr
 * Date: Aug 31, 2006
 * Time: 2:23:37 PM
 */
public class BloggerEarningsRevshareTreeNode extends TreeNodeBase {

   private double amtEarnedFromThisBloggerAllTime = 0;
   private double amtEarnedFromThisBlogger90Days = 0;



    public BloggerEarningsRevshareTreeNode(String type, String description, String identifier, boolean leaf, double amtEarnedFromThisBloggerAllTime, double amtEarnedFromThisBlogger90Days){
        super(type, description, identifier, leaf);
        this.amtEarnedFromThisBloggerAllTime = amtEarnedFromThisBloggerAllTime;
        this.amtEarnedFromThisBlogger90Days = amtEarnedFromThisBlogger90Days;
    }

    public double getAmtEarnedFromThisBloggerAllTime() {
        return amtEarnedFromThisBloggerAllTime;
    }

    public void setAmtEarnedFromThisBloggerAllTime(double amtEarnedFromThisBloggerAllTime) {
        this.amtEarnedFromThisBloggerAllTime = amtEarnedFromThisBloggerAllTime;
    }

    public double getAmtEarnedFromThisBlogger90Days() {
        return amtEarnedFromThisBlogger90Days;
    }

    public void setAmtEarnedFromThisBlogger90Days(double amtEarnedFromThisBlogger90Days) {
        this.amtEarnedFromThisBlogger90Days = amtEarnedFromThisBlogger90Days;
    }
}
