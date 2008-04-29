package com.dneero.review;

import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Apr 29, 2008
 * Time: 11:09:19 AM
 */
public class ReviewableFactory {

    public static Reviewable get(int id, int type){
        if (type==ReviewableSurveydiscuss.TYPE){
            return new ReviewableSurveydiscuss(id);
        }
        return null;
    }

    public static ArrayList<Reviewable> getAllTypes(){
        ArrayList<Reviewable> types = new ArrayList<Reviewable>();
        types.add(new ReviewableSurveydiscuss(0));
        return types;
    }


}
