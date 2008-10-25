package com.dneero.htmlui;

import com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider;
import com.dneero.util.DateDiff;
import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

/**
 * User: Joe Reger Jr
 * Date: Jun 9, 2008
 * Time: 11:16:08 AM
 */
public class PurgeOldUserSessions {

    public static int MAXUSERSESSIONAGEINSECONDS = 2400;

    public static void purge(String fqn, int levelsToDisplay){
        Logger logger = Logger.getLogger(PurgeOldUserSessions.class);
        logger.debug("PurgeOldUserSessions.purge("+fqn+", "+levelsToDisplay+") called");
        try{
            Set childrenNames = JbossTreeCacheAOPProvider.getCache().getNode(fqn).getChildrenNames();
            ArrayList<String> toPurge = dumpMap(childrenNames, 0, fqn, levelsToDisplay);
            for (Iterator<String> stringIterator=toPurge.iterator(); stringIterator.hasNext();) {
                String key = stringIterator.next();
                JbossTreeCacheAOPProvider cache = new JbossTreeCacheAOPProvider();
                cache.flush(key, "userSession");
            }
        } catch (Exception ex){
            logger.debug(ex);
        }
    }


    private static ArrayList<String> dumpMap(Set childrenNames, int nestinglevel, String fqnPrepend, int levelsToDisplay){
        Logger logger = Logger.getLogger(PurgeOldUserSessions.class);
        ArrayList<String> out = new ArrayList<String>();
        nestinglevel++;
        if(childrenNames!=null){
            for (Iterator chilIterator = childrenNames.iterator(); chilIterator.hasNext();) {
                Object childName = chilIterator.next();
                String fqnFull = fqnPrepend+"/"+childName;
                if (fqnPrepend.equals("/")){
                    fqnFull = fqnPrepend + childName;
                }
                logger.debug("childName.toString()="+childName.toString()+" - fqnFull="+fqnFull);

                //UserSession Special Output
                try{
                    Object objInCache = (Object)JbossTreeCacheAOPProvider.getCache().getNode(fqnFull);
                    if (objInCache!=null){
                        Class c = objInCache.getClass();
                        String s = c.getName();
                        logger.debug("c.getName()="+s);
                        if (objInCache instanceof Node){
                            Node node = (Node)objInCache;
                            Fqn fqn = node.getFqn();
                            //logger.debug("fqn.getName()="+fqn.getName());
                            Set keys = node.getKeys();
                            for (Iterator iterator=keys.iterator(); iterator.hasNext();) {
                                Object o=iterator.next();
                                logger.debug("o.toString()="+o.toString());
                                Object nodeObj = JbossTreeCacheAOPProvider.getCache().get(fqnFull, o);
                                if (nodeObj!=null){
                                    //UserSession
                                    if (nodeObj instanceof UserSession){
                                        UserSession userSession = (UserSession)nodeObj;
                                        int lastaccessedSecondsAgo=DateDiff.dateDiff("second", Calendar.getInstance(), userSession.getLastaccesseddate());
                                        if (lastaccessedSecondsAgo>MAXUSERSESSIONAGEINSECONDS){
                                            logger.debug("going to purge o.toString()="+o.toString());
                                            out.add(o.toString());
                                        } else {
                                            logger.debug("will not purge o.toString()="+o.toString());
                                        }
                                    } else {
                                        logger.debug("not sure what type of Object it is o.toString()="+o.toString());   
                                    }
                                }
                            }
                        }
                    } else {
                        logger.debug("No obj in cache with fqnFull="+fqnFull);
                    }
                } catch (Exception e){
                    //Nothing to do, it's not likely a UserSession
                    logger.debug(e);
                }

                try{
                    if (nestinglevel<=levelsToDisplay){
                        Set cNames = JbossTreeCacheAOPProvider.getCache().getNode(fqnFull).getChildrenNames();
                        if (cNames!=null){
                            logger.debug("cNames.size()="+cNames.size()+" fqnFull="+fqnFull);
                        } else {
                            logger.debug("cNames==null fqnFull="+fqnFull);
                        }
                        out.addAll(dumpMap(cNames, nestinglevel, fqnFull, levelsToDisplay));
                    }
                } catch (org.jboss.cache.CacheException cex){
                    logger.debug(cex);
                }

            }
        }
        return out;
    }


}
