package com.dneero.cache.providers.jboss;

import java.util.Iterator;
import java.util.Set;

/**
 * Dumps a class to html
 */
public class CacheDumper {

//    public static String getHtml(String fqn, int levelsToDisplay){
//        //reger.core.Debug.debug(5, "CacheDumper.java", "Starting Dump of: "+fqn);
//        try{
//            Set childrenNames = com.dneero.cache.providers.jboss.JbossTreeCacheAOPProvider.getTreeCache().getChildrenNames(fqn);
//            return dumpMap(childrenNames, 0, fqn, levelsToDisplay).toString();
//        } catch (Exception ex){
//            //reger.core.Debug.errorsave(ex, "CacheDumper.java");
//            return "Error retrieving cache: " + ex.getMessage();
//        }
//    }
//
//
//    private static StringBuffer dumpMap(Set childrenNames, int nestinglevel, String fqnPrepend, int levelsToDisplay){
//        //reger.core.Debug.debug(5, "CacheDumper.java", "nestinglevel: "+nestinglevel);
//        StringBuffer out = new StringBuffer();
//        nestinglevel++;
//        if(childrenNames!=null){
//            for (Iterator chilIterator = childrenNames.iterator(); chilIterator.hasNext();) {
//                Object childName = chilIterator.next();
//                String fqnFull = fqnPrepend+"/"+childName;
//                if (fqnPrepend.equals("/")){
//                    fqnFull = fqnPrepend + childName;
//                }
//                //reger.core.Debug.debug(5, "CacheDumper.java", "childName.toString()="+childName.toString()+"<br>fqnFull="+fqnFull);
//
//
//                out.append("<br>");
//
//
//                for(int i=0; i<=nestinglevel; i++){
//                    out.append("&nbsp;&nbsp;&nbsp;&nbsp;");
//                }
//                out.append("<a href='sessions.log?fqn="+fqnFull+"&levelstodisplay=1'>");
//                out.append(childName.toString());
//                out.append("</a>");
//
//                //UserSession Special Output
//                try{
//                    Object objInCache = (Object)reger.cache.providers.jboss.JbossTreeCacheAOPProvider.getTreeCache().getObject(fqnFull);
//                    if (objInCache!=null){
//                        Class c = objInCache.getClass();
//                        String s = c.getName();
//                        reger.core.Debug.debug(5, "CacheDumper.java", "c.getName()="+s);
//                        if (objInCache instanceof reger.UserSession){
//                            //Build some html to indent
//                            StringBuffer brAndIndent = new StringBuffer();
//                            brAndIndent.append("<br>");
//                            for(int i=0; i<=(nestinglevel+1); i++){
//                                brAndIndent.append("&nbsp;&nbsp;&nbsp;&nbsp;");
//                            }
//                            //Get the object
//                            reger.UserSession us = (reger.UserSession)objInCache;
//                            //Most recent activity
//                            out.append(brAndIndent+"Most Recent Activity: " + reger.core.TimeUtils.dateformatcompactwithtime(us.getMostRecentActivity()));
//                            //Servername
//                            out.append(brAndIndent+"RawIncomingServerName: "+us.getUrlSplitter().getRawIncomingServername());
//                            //Account
//                            if (us.getAccount()!=null){
//                                out.append(brAndIndent+"Account.siterooturl: "+us.getAccount().ext().getSiteRootUrl());
//                            }
//                            //User
//                            if (us.getAccountuser()!=null){
//                                out.append(brAndIndent+"Is Logged In? ");
//                                if (us.getAccountuser().ext().getIsLoggedIn()){
//                                    out.append(" YES");
//                                    out.append(brAndIndent+"Email: "+us.getAccountuser().getEmail());
//                                } else {
//                                    out.append(" no");
//                                }
//                            }
//                        }
//                    } else {
//                        reger.core.Debug.debug(5, "CacheDumper.java", "No obj in cache with fqnFull="+fqnFull);
//                    }
//                } catch (Exception e){
//                    //Nothing to do, it's not likely a UserSession
//                    reger.core.Debug.debug(5, "CacheDumper.java", e);
//                }
//
//                try{
//                    if (nestinglevel<=levelsToDisplay){
//                        Set cNames = reger.cache.providers.jboss.JbossTreeCacheAOPProvider.getTreeCache().getChildrenNames(fqnFull);
//                        out.append(dumpMap(cNames, nestinglevel, fqnFull, levelsToDisplay));
//                    }
//                } catch (org.jboss.cache.CacheException cex){
//                    reger.core.Debug.debug(5, "CacheDumper.java", cex.getMessage());
//                }
//
//            }
//        }
//        return out;
//    }






}
