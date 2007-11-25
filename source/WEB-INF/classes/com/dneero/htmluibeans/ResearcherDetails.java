package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.io.Serializable;

import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.dao.*;
import com.dneero.systemprops.SystemProperty;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.helpers.UserInputSafe;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherDetails implements Serializable {

    private String companyname;
    private String companytype;
    private String phone;

    private boolean isnewresearcher;


    public ResearcherDetails(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load called");
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            Researcher researcher = Researcher.get(userSession.getUser().getResearcherid());
            companyname = researcher.getCompanyname();
            companytype = researcher.getCompanytype();
            phone = researcher.getPhone();
        }


    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        UserSession userSession = Pagez.getUserSession();

        Researcher researcher;
        boolean isnewresearcher = false;
        if (userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            researcher =  Researcher.get(userSession.getUser().getResearcherid());
        } else {
            researcher = new Researcher();
            isnewresearcher = true;
        }

        if (userSession.getUser()!=null){


            researcher.setUserid(userSession.getUser().getUserid());
            researcher.setCompanyname(UserInputSafe.clean(companyname));
            researcher.setCompanytype(UserInputSafe.clean(companytype));
            researcher.setPhone(UserInputSafe.clean(phone));

            try{
                researcher.save();
            } catch (GeneralException gex){
                vex.addValidationError("Error saving record. ");
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                throw vex;
            }

            if (isnewresearcher){
                userSession.getUser().setResearcherid(researcher.getResearcherid());
                try{userSession.getUser().save();}catch(Exception ex){logger.error("",ex);}
            }

            //Beta mode cash to researcher
            //if (isnewresearcher && SystemProperty.getProp(SystemProperty.PROP_ISBETA).equals("1")){
                //MoveMoneyInAccountBalance.pay(userSession.getUser(), 100000, "Beta mode researcher startup cash.", false);
            //}

            boolean hasroleassigned = false;
            if (userSession.getUser()!=null && userSession.getUser().getUserroles()!=null){
                for (Iterator iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Userrole.RESEARCHER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && userSession.getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(userSession.getUser().getUserid());
                role.setRoleid(Userrole.RESEARCHER);
                userSession.getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    vex.addValidationError("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    throw vex;
                }
            }





            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                vex.addValidationError("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                throw vex;
            }

            userSession.getUser().refresh();

        } else {
            vex.addValidationError("UserSession.getUser() is null.  Please log in.");
            throw vex;
        }
    }

    public TreeMap<String, String> getCompanytypes(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("Other", "Other");
        out.put("Individual", "Individual");
        out.put("Advertising/Marketing Agency", "Advertising/Marketing Agency");
        out.put("Public Relations Agency", "Public Relations Agency");
        out.put("Market Research Firm", "Market Research Firm");
        out.put("Internet Marketing Firm", "Internet Marketing Firm");
        out.put("Journalist", "Journalist");
        return out;
    }


    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsnewresearcher() {
        return isnewresearcher;
    }

    public void setIsnewresearcher(boolean isnewresearcher) {
        this.isnewresearcher=isnewresearcher;
    }
}
