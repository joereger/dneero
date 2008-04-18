package com.dneero.rank;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 1:20:59 PM
 */
public class RankUnit {

    private int points = 0;
    private double normalizedpoints = 0;
    private int responseid = 0;
    private int rankquestionid = 0;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getNormalizedpoints() {
        return normalizedpoints;
    }

    public void setNormalizedpoints(double normalizedpoints) {
        this.normalizedpoints = normalizedpoints;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }

    public int getRankquestionid() {
        return rankquestionid;
    }

    public void setRankquestionid(int rankquestionid) {
        this.rankquestionid = rankquestionid;
    }
}
