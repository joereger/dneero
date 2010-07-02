package com.dneero.startup.dbversion;

import com.dneero.dao.Blogger;
import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.finders.DemographicsXML;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version86 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");



        //-----------------------------------
        //-----------------------------------
        String[][] rstSi= Db.RunSQL("SELECT userid, bloggerid, plid from user order by userid asc", 100000, dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstSi!=null && rstSi.length>0){
            for(int i=0; i<rstSi.length; i++){
                try{
                    int userid = Integer.parseInt(rstSi[i][0]);
                    int bloggerid = Integer.parseInt(rstSi[i][1]);
                    int plid = Integer.parseInt(rstSi[i][2]);
                    logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" ( record "+i+"/"+rstSi.length+")");
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlog= Db.RunSQL("SELECT gender, ethnicity, maritalstatus, incomerange, educationlevel, state, city, country, profession, politics, blogfocus FROM blogger WHERE bloggerid='"+bloggerid+"' AND demographicsxml=''", dbConfig);
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlog!=null && rstBlog.length>0){
                        for(int j=0; j<rstBlog.length; j++){
                            String gender = rstBlog[j][0];
                            String ethnicity = rstBlog[j][1];
                            String maritalstatus = rstBlog[j][2];
                            String incomerange = rstBlog[j][3];
                            String educationlevel = rstBlog[j][4];
                            String state = rstBlog[j][5];
                            String city = rstBlog[j][6];
                            String country = rstBlog[j][7];
                            String profession = rstBlog[j][8];
                            String politics = rstBlog[j][9];
                            String blogfocus = rstBlog[j][10];
                            //Convert these values into XML
                            DemographicsXML dXML = new DemographicsXML(Pl.get(plid));
                            //Set each value
                            if (1==1){
                                String name = "Gender";
                                String value = gender;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Ethnicity";
                                String value = ethnicity;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Marital Status";
                                String value = maritalstatus;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Income";
                                String value = incomerange;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Education";
                                String value = educationlevel;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "City";
                                String value = city;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "State";
                                String value = state;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Country";
                                String value = country;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Profession";
                                String value = profession;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Politics";
                                String value = politics;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            if (1==1){
                                String name = "Social Media Focus";
                                String value = blogfocus;
                                int demographicid = getDemographicid(name, plid);
                                if (demographicid>0){
                                    ArrayList<String> arr = new ArrayList<String>();
                                    arr.add(value);
                                    dXML.setValues(demographicid, arr);
                                } else {logger.debug("userid="+userid+" bloggerid="+bloggerid+" plid="+plid+" demographicid<=0 for '"+name+"'");}
                            }
                            //Get XML out as string
                            String demographicsxml = dXML.getAsString();
                            //Blogger blogger = Blogger.get(bloggerid);
                            //blogger.setDemographicsxml(demographicsxml);
                            //blogger.save();
                            try{
                                //-----------------------------------
                                //-----------------------------------
                                int count = Db.RunSQLUpdate("UPDATE blogger SET demographicsxml='"+ Str.cleanForSQL(demographicsxml)+"' WHERE bloggerid='"+bloggerid+"'", dbConfig);
                                //-----------------------------------
                                //-----------------------------------
                            } catch (Exception ex){
                                logger.error("Triggering Retry via Hibernate", ex);
                                Blogger blogger = Blogger.get(bloggerid);
                                blogger.setDemographicsxml(demographicsxml);
                                blogger.save();
                            }
                        }
                    }
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }

        logger.debug("doPostHibernateUpgrade() finish");
    }


    private int getDemographicid(String name, int plid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", plid))
                                            .add(Restrictions.eq("name", name))
                                           .setCacheable(true)
                                           .list();
            if (demographics!=null && demographics.size()>0){
                for (Iterator<Demographic> dit = demographics.iterator(); dit.hasNext();) {
                    Demographic demographic = dit.next();
                    return demographic.getDemographicid();
                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return 0;
    }





}