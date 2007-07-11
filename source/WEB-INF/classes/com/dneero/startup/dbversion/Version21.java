package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version21 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        String[][] rstBloggers= Db.RunSQL("SELECT bloggerid FROM blogger");
        //-----------------------------------
        //-----------------------------------
        if (rstBloggers!=null && rstBloggers.length>0){
            for(int i=0; i<rstBloggers.length; i++){
                int bloggerid = Integer.parseInt(rstBloggers[i][0]);
                boolean hasbeenupdated = false;
                try{
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlog= Db.RunSQL("SELECT blogid, blogfocus FROM blog WHERE bloggerid='"+bloggerid+"'");
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlog!=null && rstBlog.length>0){
                        for(int j=0; j<rstBlog.length; j++){
                            if (rstBlog[j][1]!=null && !rstBlog[j][1].equals("")){
                                //-----------------------------------
                                //-----------------------------------
                                int count = Db.RunSQLUpdate("UPDATE blogger SET blogfocus='"+rstBlog[j][1]+"' WHERE bloggerid='"+bloggerid+"'");
                                //-----------------------------------
                                //-----------------------------------
                                hasbeenupdated = true;
                            }
                        }
                    }
                    if (!hasbeenupdated){
                        //-----------------------------------
                        //-----------------------------------
                        int count = Db.RunSQLUpdate("UPDATE blogger SET blogfocus='Personal' WHERE bloggerid='"+bloggerid+"'");
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
        String[][] rstImpressions= Db.RunSQL("SELECT impressionid FROM impression");
        //-----------------------------------
        //-----------------------------------
        if (rstImpressions!=null && rstImpressions.length>0){
            for(int i=0; i<rstImpressions.length; i++){
                int impressionid = Integer.parseInt(rstImpressions[i][0]);
                try{
                    int userid=0;
                    //-----------------------------------
                    //-----------------------------------
                    String[][] rstBlog = Db.RunSQL("SELECT blogid FROM joinblogimpression WHERE impressionid='"+impressionid+"'");
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlog!=null && rstBlog.length>0){
                        int blogid = Integer.parseInt(rstBlog[0][0]);
                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstBlogger= Db.RunSQL("SELECT bloggerid FROM blog WHERE blogid='"+blogid+"'");
                        //-----------------------------------
                        //-----------------------------------
                        if (rstBlogger!=null && rstBlogger.length>0){
                            int bloggerid = Integer.parseInt(rstBlogger[0][0]);
                            //-----------------------------------
                            //-----------------------------------
                            String[][] rstUser= Db.RunSQL("SELECT userid FROM blogger WHERE bloggerid='"+bloggerid+"'");
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
                        int counta = Db.RunSQLUpdate("UPDATE impression SET userid='"+userid+"' WHERE impressionid='"+impressionid+"'");
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
        String[][] rstResponses= Db.RunSQL("SELECT responseid, referredbyblogid FROM Response");
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
                    String[][] rstBlogger = Db.RunSQL("SELECT bloggerid FROM blog WHERE blogid='"+referredbyblogid+"'");
                    //-----------------------------------
                    //-----------------------------------
                    if (rstBlogger!=null && rstBlogger.length>0){
                        int bloggerid = Integer.parseInt(rstBlogger[0][0]);
                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstUser= Db.RunSQL("SELECT userid FROM user WHERE bloggerid='"+bloggerid+"'");
                        //-----------------------------------
                        //-----------------------------------
                        if (rstUser!=null && rstUser.length>0){
                            userid = Integer.parseInt(rstUser[0][0]);
                        }
                    }
                    try{
                        //-----------------------------------
                        //-----------------------------------
                        int counta = Db.RunSQLUpdate("UPDATE response SET referredbyuserid='"+userid+"' WHERE responseid='"+responseid+"'");
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
            int countassdd = Db.RunSQLUpdate("UPDATE responsepending SET referredbyuserid='0'");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countadsas = Db.RunSQLUpdate("ALTER TABLE response DROP referredbyblogid");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countadsas = Db.RunSQLUpdate("ALTER TABLE responsepending DROP referredbyblogid");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countasd = Db.RunSQLUpdate("DROP TABLE blog");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        try{
            //-----------------------------------
            //-----------------------------------
            int countdsa = Db.RunSQLUpdate("DROP TABLE joinblogimpression");
            //-----------------------------------
            //-----------------------------------
        } catch (Exception ex){
            logger.error("Error upgrading", ex);
        }

        logger.debug("doPostHibernateUpgrade() finish");
    }


    //Sample sql statements

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("DROP TABLE megafielduser");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE INDEX name_of_index ON table (field1, field2)");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count2 = Db.RunSQLUpdate("UPDATE survey SET embedlink='\u0001'");
    //-----------------------------------
    //-----------------------------------
}
