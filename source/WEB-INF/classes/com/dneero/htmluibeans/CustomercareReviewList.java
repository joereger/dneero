package com.dneero.htmluibeans;

import com.dneero.review.Reviewable;
import com.dneero.review.ReviewableUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class CustomercareReviewList implements Serializable {

    private ArrayList<Reviewable> reviewables;

    public CustomercareReviewList() {
    }

    public void initBean(){
        reviewables = ReviewableUtil.getPendingForSysadmin();
    }

    public ArrayList<Reviewable> getReviewables() {
        return reviewables;
    }

    public void setReviewables(ArrayList<Reviewable> reviewables) {
        this.reviewables=reviewables;
    }
}