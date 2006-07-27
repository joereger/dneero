package com.dneero.formbeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.invoice.BloggerIncomeCalculator;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerEarnings extends SortableList {

    private double totalearningsalltime;
    private Blogger blogger;
    private ArrayList<BloggerEarningsListItem> bloggerearningslistitems;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarnings(){
        super("title");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            blogger = userSession.getUser().getBlogger();
            totalearningsalltime = BloggerIncomeCalculator.getBloggerTotalPossibleEarningsAllTime(blogger);
            bloggerearningslistitems = new ArrayList();
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                BloggerEarningsListItem beli = new BloggerEarningsListItem();
                beli.setResponseid(response.getResponseid());
                beli.setResponsedate(response.getResponsedate());
                beli.setSurveytitle(response.getSurvey().getTitle());
                beli.setAmountearned(BloggerIncomeCalculator.getBloggerTotalPossibleIncomeForSurvey(blogger, response.getSurvey()));
                bloggerearningslistitems.add(beli);
            }

        }
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerEarningsListItem obj1 = (BloggerEarningsListItem)o1;
                BloggerEarningsListItem obj2 = (BloggerEarningsListItem)o2;
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
        if (bloggerearningslistitems != null && !bloggerearningslistitems.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(bloggerearningslistitems, comparator);
        }
    }

    public ArrayList<BloggerEarningsListItem> getBloggerearningslistitems() {
        return bloggerearningslistitems;
    }

    public void setBloggerearningslistitems(ArrayList<BloggerEarningsListItem> bloggerearningslistitems) {
        this.bloggerearningslistitems = bloggerearningslistitems;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public double getTotalearningsalltime() {
        return totalearningsalltime;
    }

    public void setTotalearningsalltime(double totalearningsalltime) {
        this.totalearningsalltime = totalearningsalltime;
    }


}
