package com.dneero.helpers;

import com.dneero.dao.Survey;
import com.dneero.dao.Response;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Aug 5, 2008
 * Time: 7:14:44 AM
 */
public class SlotsRemainingInConvo {

    public static int getSlotsRemaining(Survey survey){
        Logger logger = Logger.getLogger(SlotsRemainingInConvo.class);
        //@todo Cache this number
        int slotsremaining = 0;
        int numberofrespondentsrequested = survey.getNumberofrespondentsrequested();
        int totalresponses = survey.getResponses().size();
        //Simple slot calculation
        slotsremaining = numberofrespondentsrequested - totalresponses;

        //Aggressive slot calculation
        try{
            if (survey.getIsaggressiveslotreclamationon()){

                int paidresponses = 0;
                for (Iterator<Response> iterator=survey.getResponses().iterator(); iterator.hasNext();) {
                    Response response=iterator.next();
                    if (response.getIspaid()){
                        paidresponses = paidresponses + 1;
                    }
                }

                int responsesthatwontbepaid = 0;
                for (Iterator<Response> iterator=survey.getResponses().iterator(); iterator.hasNext();) {
                    Response response=iterator.next();
                    if (!response.getIspaid()){
                        if (response.getPoststatus()==Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED){
                            responsesthatwontbepaid = responsesthatwontbepaid + 1;
                        }
                    }
                }

                int responsesthatmightbepaid = 0;
                for (Iterator<Response> iterator=survey.getResponses().iterator(); iterator.hasNext();) {
                    Response response=iterator.next();
                    if (!response.getIspaid()){
                        if (response.getPoststatus()==Response.POSTATUS_NOTPOSTED || response.getPoststatus()==Response.POSTATUS_POSTEDATLEASTONCE || response.getPoststatus()==Response.POSTATUS_POSTED){
                            responsesthatmightbepaid = responsesthatmightbepaid + 1;
                        }
                    }
                }

                //Aggressive reclamation calculation
                slotsremaining = numberofrespondentsrequested - paidresponses - responsesthatmightbepaid;
            }
        } catch (Exception ex){
            logger.error("", ex);
        }

        return slotsremaining;
    }

}
