package com.dneero.htmluibeans;

import com.dneero.htmlui.Pagez;
import com.dneero.review.Reviewable;
import com.dneero.review.ReviewableFactory;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherReviewDetail implements Serializable {

    private Reviewable reviewable;

    public ResearcherReviewDetail() {
    }

    public void initBean(){
        int id = 0;
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("id"))){
            id = Integer.parseInt(Pagez.getRequest().getParameter("id"));
        }
        int type = 0;
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("type"))){
            type = Integer.parseInt(Pagez.getRequest().getParameter("type"));
        }
        if (id>0 && type>0){
            reviewable = ReviewableFactory.get(id, type);
        }
    }

    public Reviewable getReviewable() {
        return reviewable;
    }

    public void setReviewable(Reviewable reviewable) {
        this.reviewable=reviewable;
    }
}