package com.dneero.helpers;

import com.dneero.dao.Question;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Aug 13, 2009
 * Time: 9:03:19 AM
 */
public class QuestionOrder {

    public static int calculateNewQuestionOrder(Question question){
        Logger logger = Logger.getLogger(QuestionOrder.class);
        if (question==null){ logger.debug("returning 0 because question==null"); return 0; }
        if (question.getQuestionorder()>0){logger.debug("returning "+question.getQuestionorder()+" because that's what question.getQuestionorder() is"); return question.getQuestionorder(); }
        try{
            if (!question.getIsuserquestion()){
                int maxAlready = NumFromUniqueResult.getInt("select max(questionorder) from Question where surveyid='"+question.getSurveyid()+"' and isuserquestion='false'");
                logger.debug("returning "+(maxAlready+1)+" because maxAlready="+maxAlready+" for surveyid="+question.getSurveyid());
                return maxAlready + 1;
            } else {
                int maxAlready = NumFromUniqueResult.getInt("select max(questionorder) from Question where surveyid='"+question.getSurveyid()+"' and isuserquestion='true'");
                logger.debug("returning "+(maxAlready+1)+" because maxAlready="+maxAlready+" for surveyid="+question.getSurveyid());
                return maxAlready + 1;
            }
        } catch (Error ex){
            logger.error("", ex);
        }
        logger.debug("returning 0 because it's the default");
        return 0;
    }


}
