package com.dneero.htmluibeans;

import com.dneero.dao.Rankquestion;

/**
 * User: Joe Reger Jr
 * Date: Apr 19, 2008
 * Time: 8:52:27 AM
 */
public class ResearcherRankAddquestionDropdownListitem {

    private int id;
    private String possibleanswer="";
    private Rankquestion rankquestion;

    public String getPossibleanswer() {
        return possibleanswer;
    }

    public void setPossibleanswer(String possibleanswer) {
        this.possibleanswer = possibleanswer;
    }

    public Rankquestion getRankquestion() {
        return rankquestion;
    }

    public void setRankquestion(Rankquestion rankquestion) {
        this.rankquestion = rankquestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
