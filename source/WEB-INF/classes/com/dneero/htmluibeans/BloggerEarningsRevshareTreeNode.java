package com.dneero.htmluibeans;


import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 31, 2006
 * Time: 2:23:37 PM
 */
public class BloggerEarningsRevshareTreeNode implements Serializable {

   private double amtEarnedFromThisBloggerAllTime = 0;
   private double amtEarnedFromThisBlogger90Days = 0;
   private String description = "";
   private String identifier = "";
   private int level = 0;

   public BloggerEarningsRevshareTreeNode(){}

    public BloggerEarningsRevshareTreeNode(String description, String identifier, int level, double amtEarnedFromThisBloggerAllTime, double amtEarnedFromThisBlogger90Days){
        this.amtEarnedFromThisBloggerAllTime = amtEarnedFromThisBloggerAllTime;
        this.amtEarnedFromThisBlogger90Days = amtEarnedFromThisBlogger90Days;
        this.description = description;
        this.identifier = identifier;
        this.level = level;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier=identifier;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level=level;
    }
}
