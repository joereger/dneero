package com.dneero.finders;

import com.dneero.constants.*;
import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Jun 30, 2010
 * Time: 11:04:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateDefaultDemographics {


    public static void create(Pl pl){
        checkGender(pl);
        checkEthnicity(pl);
        checkMaritalstatus(pl);
        checkIncome(pl);
        checkEducationlevel(pl);
        checkState(pl);
        checkCity(pl);
        checkCountry(pl);
        checkProfession(pl);
        checkBlogfocus(pl);
        checkPolitics(pl);
    }

    private static boolean doesDemographicExistForPl(Pl pl, String name){
        Logger logger = Logger.getLogger(CreateDefaultDemographics.class);
        try{
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", pl.getPlid()))
                                            .add(Restrictions.eq("name", name))
                                           .setCacheable(true)
                                           .list();
            if (demographics!=null && demographics.size()>0){
                return true;   
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return false;
    }

    private static void createDemographic(Pl pl, String name, String description, String possiblevalues, int ordernum, int type){
        Logger logger = Logger.getLogger(CreateDefaultDemographics.class);
        try{
            Demographic d = new Demographic();
            d.setPlid(pl.getPlid());
            d.setName(name);
            d.setDescription(description);
            d.setType(type);
            d.setIsrequired(true);
            d.setOrdernum(ordernum);
            d.setPossiblevalues(possiblevalues);
            d.save();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    private static String convertTreeSetToString(TreeSet<String> in){
        StringBuffer out = new StringBuffer();
        if (in!=null && in.size()>0){
            ArrayList arr = Util.treeSetToArrayList(in);
            out.append(DemographicsUtil.convert(arr));
        }
        return out.toString();
    }

    
    private static void checkGender(Pl pl){
        String name = "Gender";
        String description = "";
        int ordernum = 1;
        String possiblevalues = convertTreeSetToString(Genders.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkEthnicity(Pl pl){
        String name = "Ethnicity";
        String description = "";
        int ordernum = 2;
        String possiblevalues = convertTreeSetToString(Ethnicities.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkMaritalstatus(Pl pl){
        String name = "Marital Status";
        String description = "";
        int ordernum = 3;
        String possiblevalues = convertTreeSetToString(Maritalstatuses.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkIncome(Pl pl){
        String name = "Income";
        String description = "";
        int ordernum = 4;
        String possiblevalues = convertTreeSetToString(Incomes.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkEducationlevel(Pl pl){
        String name = "Education";
        String description = "";
        int ordernum = 5;
        String possiblevalues = convertTreeSetToString(Educationlevels.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }


    private static void checkCity(Pl pl){
        String name = "City";
        String description = "Make your best guess. International users choose Non-US.";
        int ordernum = 6;
        String possiblevalues = convertTreeSetToString(Cities.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkState(Pl pl){
        String name = "State";
        String description = "International users choose Non-US.";
        int ordernum = 7;
        String possiblevalues = convertTreeSetToString(States.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkCountry(Pl pl){
        String name = "Country";
        String description = "";
        int ordernum = 8;
        String possiblevalues = convertTreeSetToString(Countries.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkProfession(Pl pl){
        String name = "Profession";
        String description = "";
        int ordernum = 9;
        String possiblevalues = convertTreeSetToString(Professions.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkBlogfocus(Pl pl){
        String name = "Social Media Focus";
        String description = "What do you normally blog, tweet or facebook about?";
        int ordernum = 10;
        String possiblevalues = convertTreeSetToString(Blogfocuses.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }

    private static void checkPolitics(Pl pl){
        String name = "Politics";
        String description = "";
        int ordernum = 11;
        String possiblevalues = convertTreeSetToString(Politics.get());
        int type = Demographic.TYPE_SELECT;
        if (!doesDemographicExistForPl(pl, name)){
            createDemographic(pl, name, description, possiblevalues, ordernum, type);
        }
    }




}
