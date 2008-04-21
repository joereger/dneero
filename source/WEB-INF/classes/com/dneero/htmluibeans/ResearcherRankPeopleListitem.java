package com.dneero.htmluibeans;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2008
 * Time: 10:31:27 AM
 */
public class ResearcherRankPeopleListitem {

    private int userid=0;
    private String name="";
    private int points=0;
    private double avgnormalizedpoints=0.0;
    private String avgnormalizedpointsStr="";



    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getAvgnormalizedpoints() {
        return avgnormalizedpoints;
    }

    public void setAvgnormalizedpoints(double avgnormalizedpoints) {
        this.avgnormalizedpoints = avgnormalizedpoints;
    }

    public String getAvgnormalizedpointsStr() {
        return avgnormalizedpointsStr;
    }

    public void setAvgnormalizedpointsStr(String avgnormalizedpointsStr) {
        this.avgnormalizedpointsStr = avgnormalizedpointsStr;
    }
}
