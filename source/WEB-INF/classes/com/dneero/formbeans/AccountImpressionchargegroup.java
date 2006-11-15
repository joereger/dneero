package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountImpressionchargegroup extends SortableList {

    private ArrayList<AccountImpressionchargegroupListItem> list;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public AccountImpressionchargegroup(){
        super("impressiondetailid");

    }

    private void load(int impressionchargegroupid){
        Impressionchargegroup impressionchargegroup = Impressionchargegroup.get(impressionchargegroupid);
        if (impressionchargegroup!=null && Jsf.getUserSession().getUser()!=null && impressionchargegroup.canRead(Jsf.getUserSession().getUser())){

            list = new ArrayList();
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                               .add(Restrictions.eq("impressionchargegroupid", impressionchargegroup.getImpressionchargegroupid()))
                               .list();



            for (Iterator<Impressiondetail> iterator = impressiondetails.iterator(); iterator.hasNext();) {
                Impressiondetail impressiondetail = iterator.next();
                Impression impression = Impression.get(impressiondetail.getImpressionid());
                Survey survey = Survey.get(impression.getSurveyid());
                AccountImpressionchargegroupListItem listitem = new AccountImpressionchargegroupListItem();
                listitem.setImpressiondate(impressiondetail.getImpressiondate());
                listitem.setImpressiondetailid(impressiondetail.getImpressiondetailid());
                listitem.setIp(impressiondetail.getIp());
                listitem.setReferer(impression.getReferer());
                listitem.setSurveytitle(survey.getTitle());
                list.add(listitem);
            }

        }
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpImpressionchargegroupid = Jsf.getRequestParam("impressionchargegroupid");
        if (com.dneero.util.Num.isinteger(tmpImpressionchargegroupid)){
            logger.debug("beginView called: found impressionchargegroupid in request param="+tmpImpressionchargegroupid);
            load(Integer.parseInt(tmpImpressionchargegroupid));
        }
        return "accountimpressionchargegroup";
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AccountImpressionchargegroupListItem obj1 = (AccountImpressionchargegroupListItem)o1;
                AccountImpressionchargegroupListItem obj2 = (AccountImpressionchargegroupListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("impressiondetailid")) {
                    return ascending ? obj1.getImpressiondetailid()-obj2.getImpressiondetailid() : obj2.getImpressiondetailid()-obj1.getImpressiondetailid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (list != null && !list.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(list, comparator);
        }
    }

    public ArrayList<AccountImpressionchargegroupListItem> getList() {
        return list;
    }

    public void setList(ArrayList<AccountImpressionchargegroupListItem> list) {
        this.list = list;
    }



}
