package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherDetails {

    private String companyname;
    private String companytype;
    private String phone;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherDetails(){
        load();
    }

    public void load(){
        logger.debug("load called");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            Researcher researcher = Researcher.get(userSession.getUser().getResearcherid());
            companyname = researcher.getCompanyname();
            companytype = researcher.getCompanytype();
            phone = researcher.getPhone();
        }


    }

    public String saveAction(){

        UserSession userSession = Jsf.getUserSession();

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
            researcher.setCompanyname(companyname);
            researcher.setCompanytype(companytype);
            researcher.setPhone(phone);

            try{
                researcher.save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            if (isnewresearcher){
                userSession.getUser().setResearcherid(researcher.getResearcherid());
                try{userSession.getUser().save();}catch(Exception ex){logger.error(ex);}
            }

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
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }





            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            userSession.getUser().refresh();

            if (isnewresearcher){
                return "success_newresearcher";
            } else {
                return "researcherhome";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }

    public LinkedHashMap getCompanytypes(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Other", "Other");
        out.put("Advertising/Marketing Agency", "Advertising/Marketing Agency");
        out.put("Public Relations Agency", "Public Relations Agency");
        out.put("Market Research Firm", "Market Research Firm");
        out.put("Internet Marketing Firm", "Internet Marketing Firm");
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
}
