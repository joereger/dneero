package com.dneero.htmluibeans;

import com.dneero.htmlui.Pagez;
import com.dneero.review.Reviewable;
import com.dneero.review.ReviewableUtil;

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
        reviewables = ReviewableUtil.getPendingForResearcher(Pagez.getUserSession().getUser().getResearcherid());
    }

    public ArrayList<Reviewable> getReviewables() {
        return reviewables;
    }

    public void setReviewables(ArrayList<Reviewable> reviewables) {
        this.reviewables=reviewables;
    }
}