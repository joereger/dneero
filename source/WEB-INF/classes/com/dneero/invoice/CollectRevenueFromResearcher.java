package com.dneero.invoice;

import com.dneero.dao.Invoice;
import com.dneero.dao.Survey;
import com.dneero.dao.Researcher;
import com.dneero.dao.Researcherbilling;

/**
 * User: Joe Reger Jr
 * Date: Jul 21, 2006
 * Time: 3:35:26 PM
 */
public class CollectRevenueFromResearcher {

    public static void collect(Invoice invoice){
        if (invoice.getStatus()!=Invoice.STATUS_PAID && invoice.getStatus()!=Invoice.STATUS_WAIVED){
            Survey survey = Survey.get(invoice.getSurveyid());
            if (survey!=null && survey.getSurveyid()>0){
                Researcher researcher = Researcher.get(survey.getResearcherid());
                if (researcher!=null && researcher.getResearcherid()>0){
                    Researcherbilling researcherbilling = researcher.getResearcherbilling();
                    
                }
            }
        }
    }



}
