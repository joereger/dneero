package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.Impressionpaymentgroup;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountImpressionpaymentgroup extends SortableList implements Serializable {

    private ArrayList<AccountImpressionpaymentgroupListItem> list;

    public AccountImpressionpaymentgroup(){
        super("impressiondetailid");

    }

    private void load(int impressionpaymentgroupid){
        Impressionpaymentgroup impressionpaymentgroup = Impressionpaymentgroup.get(impressionpaymentgroupid);
        if (impressionpaymentgroup!=null && Jsf.getUserSession().getUser()!=null && impressionpaymentgroup.canRead(Jsf.getUserSession().getUser())){

            list = new ArrayList();
            List<Impressiondetail> impressiondetails = HibernateUtil.getSession().createCriteria(Impressiondetail.class)
                               .add(Restrictions.eq("impressionpaymentgroupid", impressionpaymentgroup.getImpressionpaymentgroupid()))
                               .list();



            for (Iterator<Impressiondetail> iterator = impressiondetails.iterator(); iterator.hasNext();) {
                Impressiondetail impressiondetail = iterator.next();
                Impression impression = Impression.get(impressiondetail.getImpressionid());
                Survey survey = Survey.get(impression.getSurveyid());
                AccountImpressionpaymentgroupListItem listitem = new AccountImpressionpaymentgroupListItem();
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
        String tmpImpressionpaymentgroupid = Jsf.getRequestParam("impressionpaymentgroupid");
        if (com.dneero.util.Num.isinteger(tmpImpressionpaymentgroupid)){
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.debug("beginView called: found impressionpaymentgroupid in request param="+tmpImpressionpaymentgroupid);
            load(Integer.parseInt(tmpImpressionpaymentgroupid));
        }
        return "accountimpressionpaymentgroup";
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                AccountImpressionpaymentgroupListItem obj1 = (AccountImpressionpaymentgroupListItem)o1;
                AccountImpressionpaymentgroupListItem obj2 = (AccountImpressionpaymentgroupListItem)o2;
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

    public ArrayList<AccountImpressionpaymentgroupListItem> getList() {
        return list;
    }

    public void setList(ArrayList<AccountImpressionpaymentgroupListItem> list) {
        this.list = list;
    }


 
}
