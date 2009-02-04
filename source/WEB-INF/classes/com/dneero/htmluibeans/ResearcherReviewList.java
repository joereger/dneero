package com.dneero.htmluibeans;

import com.dneero.htmlui.Pagez;
import com.dneero.review.Reviewable;
import com.dneero.review.ReviewableUtil;
import com.dneero.util.Num;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherReviewList implements Serializable {

    private ArrayList<Reviewable> reviewables;

    public ResearcherReviewList() {
    }

    public void initBean(){
        int onlytypeid=0;
        if (Pagez.getRequest().getParameter("onlytypeid")!=null && Num.isinteger(Pagez.getRequest().getParameter("onlytypeid"))){
            onlytypeid = Integer.parseInt(Pagez.getRequest().getParameter("onlytypeid"));
        }
        reviewables = ReviewableUtil.getPendingForResearcherSorted(Pagez.getUserSession().getUser().getResearcherid(), onlytypeid);
    }

    public ArrayList<Reviewable> getReviewables() {
        return reviewables;
    }

    public void setReviewables(ArrayList<Reviewable> reviewables) {
        this.reviewables=reviewables;
    }
}