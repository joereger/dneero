package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.htmlui.Pagez;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:40 AM
 */
public class ResearcherResultsImpressions implements Serializable {


    private Survey survey;
    private ArrayList<ResearcherResultsImpressionsListitem> researcherResultsImpressionsListitems;


    public ResearcherResultsImpressions(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        researcherResultsImpressionsListitems = new ArrayList<ResearcherResultsImpressionsListitem>();
        if (survey!=null){
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    logger.debug("impressionid="+impression.getImpressionid()+" referer="+impression.getReferer() + " impressionspaid="+impression.getImpressionspaid()+" impressionstobepaid="+impression.getImpressionstobepaid());
                    ResearcherResultsImpressionsListitem robj = new ResearcherResultsImpressionsListitem();
                    robj.setImpressionid(impression.getImpressionid());
                    robj.setImpressionspaidandtobepaid(impression.getImpressionspaid() + impression.getImpressionstobepaid());
                    robj.setReferer(impression.getReferer());
                    robj.setReferertruncated(Str.truncateString(impression.getReferer(), 35));
                    robj.setImpressionquality(String.valueOf(impression.getQuality()));
                    researcherResultsImpressionsListitems.add(robj);
                }
            }
            logger.debug("done loading survey");
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


    public ArrayList<ResearcherResultsImpressionsListitem> getResearcherResultsImpressionsListitems() {
        return researcherResultsImpressionsListitems;
    }

    public void setResearcherResultsImpressionsListitems(ArrayList<ResearcherResultsImpressionsListitem> researcherResultsImpressionsListitems) {
        this.researcherResultsImpressionsListitems = researcherResultsImpressionsListitems;
    }
}
