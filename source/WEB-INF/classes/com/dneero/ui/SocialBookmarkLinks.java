package com.dneero.ui;

import com.dneero.dao.Survey;

/**
 * User: Joe Reger Jr
 * Date: Apr 4, 2007
 * Time: 1:16:17 PM
 */
public class SocialBookmarkLinks {


    public String getSocialBookmarkLinks(Survey survey){
        StringBuffer sb = new StringBuffer();
        //@todo generate survey url for digg/reddit links
        //@todo urlencode links and title
        //@todo spacing/placement on page of digg/reddit links
        String url = "";
        String title = survey.getTitle();

        String out = "<a href=\"http://technorati.com/faves?add="+url+"\" target=\"_blank\">Technorati</a> " +
                     "<a href=\"http://digg.com/submit?phase=2&url="+url+"\" target=\"_blank\">Digg</a>" +
                     "<a href=\"http://del.icio.us/post?url="+url+";title="+title+"\" target=\"_blank\">del.icio.us</a>" +
                     "<a href=\"http://myweb2.search.yahoo.com/myresults/bookmarklet?t="+title+"&u="+url+"\" target=\"_blank\">Yahoo</a>" +
                     "<a href=\"http://www.blinklist.com/index.php?Action=Blink/addblink.php&Url="+url+"&Title="+title+"\" target=\"_blank\">BlinkList</a>" +
                     "<a href=\"http://www.spurl.net/spurl.php?url="+url+"&title="+title+"\" target=\"_blank\">Spurl</a>" +
                     "<a href=\"http://reddit.com/submit?url="+url+"&title="+title+"\" target=\"_blank\">reddit</a>" +
                     "<a href=\"http://www.furl.net/storeIt.jsp?t="+title+"&u=url\" target=\"_blank\">Furl</a>";
        

        return out;
    }


}
