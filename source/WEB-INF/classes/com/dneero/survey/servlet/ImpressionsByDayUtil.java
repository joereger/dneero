package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import java.util.HashMap;

import com.dneero.util.Num;

/**
 * User: Joe Reger Jr
 * Date: Oct 1, 2007
 * Time: 2:48:47 PM
 */
public class ImpressionsByDayUtil {

    private HashMap<Integer, Integer> impressionsbyday;
    public static int NUMBEROFDAYSTOSTOREASDAYBYDAY=90;

    public ImpressionsByDayUtil(String impressionsbydayStr){
        Logger logger = Logger.getLogger(this.getClass().getName());
        impressionsbyday = new HashMap<Integer, Integer>();
        try{
            if (impressionsbydayStr!=null && !impressionsbydayStr.equals("")){
                String[] split = impressionsbydayStr.split("-");
                for (int i=0; i<split.length; i++) {
                    String s = split[i];
                    if (s!=null && Num.isinteger(s)){
                        impressionsbyday.put(i, Integer.parseInt(s));
                    }
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public String getAsString(){
        StringBuffer out = new StringBuffer();
        for(int i=0; i<NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
            String toadd = "0";
            if (impressionsbyday!=null && impressionsbyday.containsKey(i)){
                toadd = String.valueOf(impressionsbyday.get(i));
            }
            if (i>0){
                out.append("-");
            }
            out.append(toadd);
        }
        return out.toString();
    }

    public int getImpressionsForParticularDay(int dayssincetakingsurvey){
        if (impressionsbyday!=null && impressionsbyday.containsKey(dayssincetakingsurvey)){
            return impressionsbyday.get(dayssincetakingsurvey);
        }
        return 0;
    }

    public void add(int impressions, int dayssincetakingsurvey){
        if (impressionsbyday==null){
            impressionsbyday = new HashMap<Integer, Integer>();
        }
        if (dayssincetakingsurvey<NUMBEROFDAYSTOSTOREASDAYBYDAY){
            impressionsbyday.put(dayssincetakingsurvey, getImpressionsForParticularDay(dayssincetakingsurvey)+impressions);
        }
    }

    public void add(ImpressionsByDayUtil ibdu){
        for(int i=0; i<NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
            if (ibdu!=null && ibdu.getImpressionsbyday()!=null && ibdu.getImpressionsbyday().containsKey(i)){
                add(ibdu.getImpressionsbyday().get(i), i);
            }
        }
    }

    public HashMap<Integer, Integer> getImpressionsbyday() {
        return impressionsbyday;
    }
}
