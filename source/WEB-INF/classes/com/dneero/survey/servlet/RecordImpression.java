package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:08:26 PM
 */
public class RecordImpression {

    public static void record(Survey survey, User user, Blog blog, HttpServletRequest request){
        //Create an IAO
        ImpressionActivityObject iao = new ImpressionActivityObject();
        iao.setSurveyid(survey.getSurveyid());
        if (blog!=null){
            iao.setBlogid(blog.getBlogid());
        } else {
            iao.setBlogid(0);
        }

        //Write iao to db
        //@todo cache impression activity for performance gain
        //I've already built the iao object so that it's easy to put into a simple memory list and then parse on some interval, writing to the database
        ImpressionActivityObjectStorage.store(iao);
    }

}
