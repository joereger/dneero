package com.dneero.mail;

import com.dneero.dao.Mail;
import com.dneero.dao.Mailchild;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:09:51 PM
 */
public interface Mailtype {

    public int getMailtypeid();
    public String renderToHtml(Mailchild mailchild);


}
