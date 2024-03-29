package com.dneero.survey.servlet;

import com.dneero.dao.Blogpost;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmluibeans.PublicBlog;
import com.dneero.systemprops.BaseUrl;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class BlogRss extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());


        StringBuffer fd = new StringBuffer();

        //Create Rome Feed Object
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");

        //Channel Title
        String channelTitle = "Blog";
        feed.setTitle(channelTitle);

        //Channel Description
        String channelDesc = "Blog";
        feed.setDescription(channelDesc);

        //Channel Link
        String channelLink = BaseUrl.get(false)+"blog.jsp";
        feed.setLink(channelLink);

        //Create Items
        List entries = new ArrayList();


        List<Blogpost> blogposts = HibernateUtil.getSession().createQuery("from Blogpost where plid='"+ Pagez.getUserSession().getPl().getPlid() +"' order by date DESC").setCacheable(true).setMaxResults(20).list();
        for (int i = 0; i < blogposts.size(); i++) {
            Blogpost blogpost = blogposts.get(i);

            //Create a Rome RSS Item
            SyndEntry entry = new SyndEntryImpl();

            //Date
            entry.setPublishedDate(blogpost.getDate());

            //Item Link
            entry.setLink(BaseUrl.get(false)+"blogpost.jsp?blogpostid="+blogpost.getBlogpostid());

            //Item title
            String itemTitle = blogpost.getTitle();
            entry.setTitle(itemTitle);

            //Item Description
            String bodyTmp = blogpost.getBody().replaceAll( PublicBlog.CARRIAGERETURN + PublicBlog.LINEBREAK, "<br>");
            SyndContent desc = new SyndContentImpl();
            desc.setType("text/html");
            desc.setValue(bodyTmp);
            entry.setDescription(desc);

            //Item Author
            entry.setAuthor(blogpost.getAuthor());

            //Categories
            ArrayList<SyndCategoryImpl> categories = new ArrayList<SyndCategoryImpl>();
            if (blogpost.getCategories()!=null && !blogpost.getCategories().equals("")){
                String[] cats = blogpost.getCategories().split(",");
                for (int j = 0; j < cats.length; j++) {
                    String cat = cats[j];
                    SyndCategoryImpl rsscat = new SyndCategoryImpl();
                    rsscat.setName(cat.trim());
                    categories.add(rsscat);
                }
            }
            entry.setCategories(categories);

            //Add the item to the list of items
            entries.add(entry);
        }

        //Add the list of items to the rss/channel
        feed.setEntries(entries);

        //Start the output of the feed
        try{
            SyndFeedOutput feedOut = new SyndFeedOutput();
            fd.append(feedOut.outputString(feed));
            logger.debug("Successfully output Rome RSS Feed");
        } catch (FeedException fe) {
            logger.error("Rome RSS Feed died:" + fe.toString(), fe);
        } catch (Exception e){
            logger.error("", e);
        }


        //Debug
        //logger.debug("Rss Feed=" + fd.toString());

        ServletOutputStream outStream = response.getOutputStream();
        response.setContentType("text/xml");
        outStream.write(fd.toString().getBytes());
        outStream.close();


    }



    


}
