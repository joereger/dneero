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
        if (type==ReviewableQuestion.TYPE){
            return new ReviewableQuestion(id);
        }
        if (type==ReviewableResponse.TYPE){
            return new ReviewableResponse(id);
        }
        if (type==ReviewableVenue.TYPE){
            return new ReviewableVenue(id);
        }
        if (type==ReviewableSuperPanelmembership.TYPE){
            return new ReviewableSuperPanelmembership(id);
        }
        return null;
    }

    public static ArrayList<Reviewable> getAllTypes(){
        ArrayList<Reviewable> types = new ArrayList<Reviewable>();
        types.add(new ReviewableSurveydiscuss(0));
        types.add(new ReviewableQuestion(0));
        types.add(new ReviewableResponse(0));
        types.add(new ReviewableVenue(0));
        types.add(new ReviewableSuperPanelmembership(0));
        return types;
    }


}
