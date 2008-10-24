package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version21 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        String[][] rstBloggers= Db.RunSQL("SELECT bloggerid FROM blogger", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstBloggers!=null && rstBloggers.length>0){
            for(int i=0; i<rstBloggers.length; i++){
                int bloggerid = Integer.parseInt(rstBloggers[i][0]);
                boolean hasbeenupdated = false;
                try{
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlog= Db.RunSQL("SELECT blogid, blogfocus FROM blog WHERE bloggerid='"+bloggerid+"'", dbConfig);
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlog!=null && rstBlog.length>0){
                        for(int j=0; j<rstBlog.length; j++){
                            if (rstBlog[j][1]!=null && !rstBlog[j][1].equals("")){
                                //-----------------------------------
                                //-----------------------------------
                                int count = Db.RunSQLUpdate("UPDATE blogger SET blogfocus='"+rstBlog[j][1]+"' WHERE bloggerid='"+bloggerid+"'", dbConfig);
                                //-----------------------------------
                                //-----------------------------------
                                hasbeenupdated = true;
                            }
                        }
                    }
                    if (!hasbeenupdated){
                        //-----------------------------------
                        //-----------------------------------
                        int count = Db.RunSQLUpdate("UPDATE blogger SET blogfocus='Personal' WHERE bloggerid='"+bloggerid+"'", dbConfig);
                        //-----------------------------------
                        //-----------------------------------
                    }
                } catch (Exception ex){
                    logger.error("Error upgrading", ex);
                }
            }
        }




        //-----------------------------------
        //-----------------------------------
        String[][] rstImpressions= Db.RunSQL("SELECT impressionid FROM impression", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstImpressions!=null && rstImpressions.length>0){
            for(int i=0; i<rstImpressions.length; i++){
                int impressionid = Integer.parseInt(rstImpressions[i][0]);
                try{
                    int userid=0;
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlog = Db.RunSQL("SELECT blogid FROM joinblogimpression WHERE impressionid='"+impressionid+"'", dbConfig);
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlog!=null && rstBlog.length>0){
                        int blogid = Integer.parseInt(rstBlog[0][0]);
                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstBlogger= Db.RunSQL("SELECT bloggerid FROM blog WHERE blogid='"+blogid+"'", dbConfig);
                        //-----------------------------------
                        //-----------------------------------
                        if (rstBlogger!=null && rstBlogger.length>0){
                            int bloggerid = Integer.parseInt(rstBlogger[0][0]);
                            //-----------------------------------
                            //-----------------------------------
                            String[][] rstUser= Db.RunSQL("SELECT userid FROM blogger WHERE bloggerid='"+bloggerid+"'", dbConfig);
                            //-----------------------------------
                            //-----------------------------------
                            if (rstUser!=null && rstUser.length>0){
                                userid = Integer.parseInt(rstUser[0][0]);
                            }
                        }
                    }
                    try{
                        //-----------------------------------
                        //-----------------------------------
                        int counta = Db.RunSQLUpdate("UPDATE impression SET userid='"+userid+"' WHERE impressionid='"+impressionid+"'", dbConfig);
                        //-----------------------------------
                        //-----------------------------------
                    } catch (Exception ex){
                        logger.error("Error upgrading", ex);
                    }
                } catch (Exception ex){
                    logger.error("Error upgrading", ex);
                }
            }
        }




        //-----------------------------------
        //-----------------------------------
        String[][] rstResponses= Db.RunSQL("SELECT responseid, referredbyblogid FROM Response", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstResponses!=null && rstResponses.length>0){
            for(int i=0; i<rstResponses.length; i++){
                try{
                    int responseid = Integer.parseInt(rstResponses[i][0]);
                    int referredbyblogid = Integer.parseInt(rstResponses[i][1]);
                    int userid = 0;
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlogger = Db.RunSQL("SELECT bloggerid FROM blog WHERE blogid='"+referredbyblogid+"'", dbConfig);
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlogger!=null && rstBlogger.length>0){
                        int bloggerid = Integer.parseInt(rstBlogger[0][0]);
                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstUser= Db.RunSQL("SELECT userid FROM user WHERE bloggerid='"+bloggerid+"'", dbConfig);
                        //-----------------------------------
                        //-----------------------------------
                        if (rstUser!=null && rstUser.length>0){
                            userid = Integer.parseInt(rstUser[0][0]);
                        }
                    }
                    try{
                        //-----------------------------------
                        //-----------------------------------
                        int counta = Db.RunSQLUpdate("UPDATE response SET referredbyuserid='"+userid+"' WHERE responseid='"+responseid+"'", dbConfig);
                        //-----------------------------------
                        //-----------------------------------
                    } catch (Exception ex){
                        logger.error("Error upgrading", ex);
                    }
                } catch (Exception ex){
                    logger.error("Error upgrading", ex);
                }
            }
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countassdd = Db.RunSQLUpdate("UPDATE responsepending SET referredbyuserid='0'", dbConfig);
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countadsas = Db.RunSQLUpdate("ALTER TABLE response DROP referredbyblogid", dbConfig);
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countadsas = Db.RunSQLUpdate("ALTER TABLE responsepending DROP referredbyblogid", dbConfig);
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countasd = Db.RunSQLUpdate("DROP TABLE blog", dbConfig);
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countdsa = Db.RunSQLUpdate("DROP TABLE joinblogimpression", dbConfig);
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        logger.debug("doPostHibernateUpgrade() finish");
    }



}
