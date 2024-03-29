package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.*;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerCompletedsurveys implements Serializable {

    private ArrayList<BloggerCompletedsurveysListitem> list;
    private ArrayList<BloggerCompletedsurveysListitem> listrecent;
    private int maxtodisplay = 10000;

    public BloggerCompletedsurveys(){

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            list = new ArrayList();
            listrecent = new ArrayList();
            //HibernateUtil.getSession().saveOrUpdate(Blogger.get(userSession.getUser().getBloggerid()));
            List<Response> responses = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+userSession.getUser().getBloggerid()+"' order by responseid desc").setMaxResults(maxtodisplay).setCacheable(true).list();
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Survey survey = Survey.get(response.getSurveyid());
                //int totalimpressions = UserImpressionFinder.getTotalImpressions(Blogger.get(userSession.getUser().getBloggerid()), survey);
                int totalimpressions = 0;
                //int paidandtobepaidimpressions = UserImpressionFinder.getPaidAndToBePaidImpressions(Blogger.get(userSession.getUser().getBloggerid()), survey);
                int paidandtobepaidimpressions = 0;
                BloggerCompletedsurveysListitem listitem = new BloggerCompletedsurveysListitem();
                listitem.setTotalimpressions(totalimpressions);
                listitem.setPaidandtobepaidimpressions(paidandtobepaidimpressions);
                listitem.setResponsedate(response.getResponsedate());
                listitem.setResponseid(response.getResponseid());
                listitem.setSurveyid(survey.getSurveyid());
                listitem.setSurveytitle(survey.getTitle());
                listitem.setResponse(response);
                list.add(listitem);
                int dayssinceresponse = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(listitem.getResponse().getResponsedate()));
                //Keep it listed for five days after it's paid
                if (dayssinceresponse<=(UpdateResponsePoststatus.MAXPOSTINGPERIODINDAYS+5)){
                    listrecent.add(listitem);
                }
            }
        }
    }

    public void refreshResponseHtml (int responseid) throws ValidationException {
        Response response = Response.get(responseid);
        if (response.getResponseid()>0){
            UpdateResponsePoststatus.processSingleResponse(response);        
        }
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerCompletedsurveysListitem obj1 = (BloggerCompletedsurveysListitem)o1;
                BloggerCompletedsurveysListitem obj2 = (BloggerCompletedsurveysListitem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? obj1.getSurveytitle().compareTo(obj2.getSurveytitle()) : obj2.getSurveytitle().compareTo(obj1.getSurveytitle());
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

    public ArrayList<BloggerCompletedsurveysListitem> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerCompletedsurveysListitem> list) {
        this.list = list;
    }


    public ArrayList<BloggerCompletedsurveysListitem> getListrecent() {
        return listrecent;
    }

    public void setListrecent(ArrayList<BloggerCompletedsurveysListitem> listrecent) {
        this.listrecent = listrecent;
    }

    public int getMaxtodisplay() {
        return maxtodisplay;
    }

    public void setMaxtodisplay(int maxtodisplay) {
        this.maxtodisplay=maxtodisplay;
    }
}
