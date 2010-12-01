<%@ page import="com.dneero.cache.providers.CacheFactory" %>
<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.privatelabel.PlFinder" %>
<%@ page import="com.dneero.privatelabel.PlVerification" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dneero.finders.CreateDefaultDemographics" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Label";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>

<%
    Pl pl = new Pl();
    pl.setWebhtmlheader("");
    pl.setWebhtmlfooter("");
    pl.setIshttpson(false);
    pl.setHomepagetemplate("");
    pl.setPeers("0");
    pl.setIsanybodyallowedtocreatesurveys(true);
    pl.setIsanybodyallowedtocreatetwitasks(true);
    pl.setIsbloggerdemographicrequired(true);
    pl.setIsemailactivationrequired(true);
    pl.setIsreferralprogramon(false);
    pl.setIsresellerprogramon(false);
    pl.setIsvenuerequired(false);
    pl.setMaincss("");
    pl.setTemplatedirectory("");
    pl.setGoogleanalyticsidweb("");
    pl.setGoogleanalyticsidflash("");
    pl.setSurveycalled("");
    pl.setSurveycalledplural("");
    pl.setIsanonymousresponseallowed(false);
    pl.setCustomvar1ison(false);
    pl.setCustomvar1name("");
    pl.setCustomvar2ison(false);
    pl.setCustomvar2name("");
    pl.setCustomvar3ison(false);
    pl.setCustomvar3name("");
    if (request.getParameter("plid")!=null && Num.isinteger(request.getParameter("plid"))){
        pl = Pl.get(Integer.parseInt(request.getParameter("plid")));
    }
%>
<%
    String templatedirectory = "default";
    if (!pl.getTemplatedirectory().equals("")){
        templatedirectory = pl.getTemplatedirectory();
    }
    String defaultWebHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/webhtmlheader.vm").toString();
    String defaultWebFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/webhtmlfooter.vm").toString();
    String defaultEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/emailheader.html").toString();
    String defaultEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/emailfooter.html").toString();
    String defaultHomepagetemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/homepagetemplate.vm").toString();
    String defaultMaincss = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+templatedirectory+"/main.css").toString();
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("save")){
                StringBuffer peers = new StringBuffer();
                String[] peersStrArray = Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("peers", "Peers", false));
                if (peersStrArray!=null){
                    for (int i=0; i<peersStrArray.length; i++) {
                        String s=peersStrArray[i];
                        peers.append(s);
                        if (i<peersStrArray.length-1){
                            peers.append(",");
                        }
                    }
                }
                pl.setPeers(peers.toString());
                pl.setName(Textbox.getValueFromRequest("name", "Name", true, DatatypeString.DATATYPEID));
                pl.setNameforui(Textbox.getValueFromRequest("nameforui", "Name For UI", true, DatatypeString.DATATYPEID));
                pl.setCustomdomain1(Textbox.getValueFromRequest("customdomain1", "Customdomain1", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain2(Textbox.getValueFromRequest("customdomain2", "Customdomain2", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain3(Textbox.getValueFromRequest("customdomain3", "Customdomain3", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setSubdomain(Textbox.getValueFromRequest("subdomain", "subdomain", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setTemplatedirectory(Textbox.getValueFromRequest("templatedirectory", "Template Directory", false, DatatypeString.DATATYPEID));
                pl.setWebhtmlheader(Textarea.getValueFromRequest("webhtmlheader", "Web Html Header", false));
                pl.setWebhtmlfooter(Textarea.getValueFromRequest("webhtmlfooter", "Web Html Footer", false));
                pl.setEmailhtmlheader(Textarea.getValueFromRequest("emailhtmlheader", "Email Html Header", false));
                pl.setEmailhtmlfooter(Textarea.getValueFromRequest("emailhtmlfooter", "Email Html Footer", false));
                pl.setHomepagetemplate(Textarea.getValueFromRequest("homepagetemplate", "Homepage Html Template", false));
                pl.setMaincss(Textarea.getValueFromRequest("maincss", "Main CSS Template", false));
                pl.setIshttpson(CheckboxBoolean.getValueFromRequest("ishttpson"));
                pl.setIsanybodyallowedtocreatesurveys(CheckboxBoolean.getValueFromRequest("isanybodyallowedtocreatesurveys"));
                pl.setIsanybodyallowedtocreatetwitasks(CheckboxBoolean.getValueFromRequest("isanybodyallowedtocreatetwitasks"));
                pl.setIsbloggerdemographicrequired(CheckboxBoolean.getValueFromRequest("isbloggerdemographicrequired"));
                pl.setIsemailactivationrequired(CheckboxBoolean.getValueFromRequest("isemailactivationrequired"));
                pl.setIsreferralprogramon(CheckboxBoolean.getValueFromRequest("isreferralprogramon"));
                pl.setIsresellerprogramon(CheckboxBoolean.getValueFromRequest("isresellerprogramon"));
                pl.setIsvenuerequired(CheckboxBoolean.getValueFromRequest("isvenuerequired"));
                pl.setGoogleanalyticsidweb(Textbox.getValueFromRequest("googleanalyticsidweb", "Google Analytics Web", false, DatatypeString.DATATYPEID));
                pl.setGoogleanalyticsidflash(Textbox.getValueFromRequest("googleanalyticsidflash", "Google Analytics Flash", false, DatatypeString.DATATYPEID));
                pl.setSurveycalled(Textbox.getValueFromRequest("surveycalled", "Survey Called", false, DatatypeString.DATATYPEID));
                pl.setSurveycalledplural(Textbox.getValueFromRequest("surveycalledplural", "Survey Called Plural", false, DatatypeString.DATATYPEID));
                pl.setIsanonymousresponseallowed(CheckboxBoolean.getValueFromRequest("isanonymousresponseallowed"));
                pl.setCustomvar1name(Textbox.getValueFromRequest("customvar1name", "Custom Var 1 Name", false, DatatypeString.DATATYPEID));
                pl.setCustomvar1ison(CheckboxBoolean.getValueFromRequest("customvar1ison"));
                pl.setCustomvar2name(Textbox.getValueFromRequest("customvar2name", "Custom Var 2 Name", false, DatatypeString.DATATYPEID));
                pl.setCustomvar2ison(CheckboxBoolean.getValueFromRequest("customvar2ison"));
                pl.setCustomvar3name(Textbox.getValueFromRequest("customvar3name", "Custom Var 3 Name", false, DatatypeString.DATATYPEID));
                pl.setCustomvar3ison(CheckboxBoolean.getValueFromRequest("customvar3ison"));
                //Validate data
                if (PlVerification.isValid(pl)){
                    //Save
                    pl.save();
                    //If was new, create default demographic fields
                    if (request.getParameter("plid")==null || !Num.isinteger(request.getParameter("plid"))){
                        CreateDefaultDemographics.create(pl);
                    }
                    //Flush and redirect
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Saved!");
                    Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
                    return;
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlheadertodefault")){
                pl.setWebhtmlheader(defaultWebHeader);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlfootertodefault")){
                pl.setWebhtmlfooter(defaultWebFooter);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setemailhtmlheadertodefault")){
                pl.setEmailhtmlheader(defaultEmailHeader);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setmaincsstodefault")){
                pl.setMaincss(defaultMaincss);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("sethomepagetemplatetodefault")){
                pl.setHomepagetemplate(defaultHomepagetemplate);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setemailhtmlfootertodefault")){
                pl.setEmailhtmlfooter(defaultEmailFooter);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



        <form action="/sysadmin/privatelabeledit.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/sysadmin/privatelabeledit.jsp">
            <input type="hidden" name="action" value="save" id="action">
            <input type="hidden" name="plid" value="<%=pl.getPlid()%>">

            <table cellpadding="3" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", pl.getName(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name for UI</font>
                        <br/><font class="tinyfont">Ex("dNeero")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("nameforui", pl.getNameforui(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Ishttpson?</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("ishttpson", pl.getIshttpson(), "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subdomain</font>
                        <br/><font class="tinyfont">Ex("www", "someconame")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("subdomain", pl.getSubdomain(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain1</font>
                        <br/><font class="tinyfont">Ex("www.mypldomain.com")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain1", pl.getCustomdomain1(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain2</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain2", pl.getCustomdomain2(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain3</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain3", pl.getCustomdomain3(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Demographic Fields</font>
                        <br/>
                        <font class="tinyfont">Add/edit/delete fields like Age, Gender, Income, etc.</font>
                    </td>
                    <td valign="top">
                        <a href="/sysadmin/privatelabel_demographics.jsp?plid=<%=pl.getPlid()%>">Edit</a>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Google Analytics ID Web</font>
                        <br/>
                        <font class="tinyfont">ex. UA-208946-2</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("googleanalyticsidweb", pl.getGoogleanalyticsidweb(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Google Analytics ID Flash</font>
                        <br/>
                        <font class="tinyfont">ex. UA-208946-2</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("googleanalyticsidflash", pl.getGoogleanalyticsidflash(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Survey Called</font>
                        <br/>
                        <font class="tinyfont">ex. survey</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("surveycalled", pl.getSurveycalled(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Survey Called Plural</font>
                        <br/>
                        <font class="tinyfont">ex. surveys</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("surveycalledplural", pl.getSurveycalledplural(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Private Label Peers</font>
                        <br/><font class="tinyfont">Selecting All will make you peered with all pls.</font>
                        <br/><font class="tinyfont"><%=pl.getPeers()%></font>
                    </td>
                    <td valign="top">
                        <%
                        String[] peersSelected = new String[0];
                        if (pl.getPeers()!=null && pl.getPeers().length()>0){
                            peersSelected = pl.getPeers().split(",");
                        }
                        %>
                        <%
                        TreeMap<String, String> plsToChooseFrom = new TreeMap<String, String>();
                        plsToChooseFrom.put("0", "All Private Labels");
                        List results = HibernateUtil.getSession().createQuery("from Pl").list();
                        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                            Pl plOption = (Pl) iterator.next();
                            plsToChooseFrom.put(String.valueOf(plOption.getPlid()), Str.truncateString(plOption.getName(), 40));
                        }
                        %>
                        <%=DropdownMultiselect.getHtml("peers", Util.stringArrayToArrayList(peersSelected), plsToChooseFrom, 6, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isanybodyallowedtocreatesurveys", pl.getIsanybodyallowedtocreatesurveys(), "", "")%>
                        <font class="formfieldnamefont">Anybody Can Create Surveys</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isanybodyallowedtocreatetwitasks", pl.getIsanybodyallowedtocreatetwitasks(), "", "")%>
                        <font class="formfieldnamefont">Anybody Can Create Twitasks</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isbloggerdemographicrequired", pl.getIsbloggerdemographicrequired(), "", "")%>
                        <font class="formfieldnamefont">Blogger Demographic Required</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isemailactivationrequired", pl.getIsemailactivationrequired(), "", "")%>
                        <font class="formfieldnamefont">Email Activation is Required</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isreferralprogramon", pl.getIsreferralprogramon(), "", "")%>
                        <font class="formfieldnamefont">Referral Program On</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isresellerprogramon", pl.getIsresellerprogramon(), "", "")%>
                        <font class="formfieldnamefont">Reseller Program On</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isvenuerequired", pl.getIsvenuerequired(), "", "")%>
                        <font class="formfieldnamefont">Venues Required to Post</font>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isanonymousresponseallowed", pl.getIsanonymousresponseallowed(), "", "")%>
                        <font class="formfieldnamefont">Anonymous Responses Allowed When Survey Permits</font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">CustomVar1</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("customvar1ison", pl.getCustomvar1ison(), "", "")%> Is On/Active
                        <br/>
                        Name:
                        <br/>
                        <%=Textbox.getHtml("customvar1name", pl.getCustomvar1name(), 150, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">CustomVar2</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("customvar2ison", pl.getCustomvar2ison(), "", "")%> Is On/Active
                        <br/>
                        Name:
                        <br/>
                        <%=Textbox.getHtml("customvar2name", pl.getCustomvar2name(), 150, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">CustomVar3</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("customvar3ison", pl.getCustomvar3ison(), "", "")%> Is On/Active
                        <br/>
                        Name:
                        <br/>
                        <%=Textbox.getHtml("customvar3name", pl.getCustomvar3name(), 150, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Template Directory</font>
                        <br/><font class="tinyfont">Setting to "default" or "myco" will use hard-coded files in that /template/myco/ directory and then override with boxes below.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("templatedirectory", pl.getTemplatedirectory(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Main CSS</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setmaincsstodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default or files in templatedirectory)</font>
                        <br/>
                        <%=Textarea.getHtml("maincss", pl.getMaincss(), 24, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Web Html Header</font>
                        <br/><font class="tinyfont">Uses <a href="http://velocity.apache.org/engine/releases/velocity-1.5/vtl-reference-guide.html">VTL</a></font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlheadertodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default or files in templatedirectory)</font>
                        <br/>
                        <%=Textarea.getHtml("webhtmlheader", pl.getWebhtmlheader(), 24, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Web Html Footer</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlfootertodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default or files in templatedirectory)</font>
                        <br/>
                        <%=Textarea.getHtml("webhtmlfooter", pl.getWebhtmlfooter(), 24, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Homepage Html Template</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=sethomepagetemplatetodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default or files in templatedirectory)</font>
                        <br/>
                        <%=Textarea.getHtml("homepagetemplate", pl.getHomepagetemplate(), 24, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Email Html Header</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setemailhtmlheadertodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                        <br/><font class="tinyfont">
                            totalConvoJoins,
                            totalConvoEmbeds,
                            usersWithMostConvos,
                            donationsMiniReport,
                            recentDonations,
                            recentConvos,
                            recentConvoJoins,
                            twitterAnswers,
                            recentlyPaid,
                            newestUsers,
                            blogPosts
                        </font>
                        <br/>
                        <%=Textarea.getHtml("emailhtmlheader", pl.getEmailhtmlheader(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Email Html Footer</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setemailhtmlfootertodefault">Reset</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default or files in templatedirectory)</font>
                        <br/>
                        <%=Textarea.getHtml("emailhtmlfooter", pl.getEmailhtmlfooter(), 8, 80, "", "width: 100%;")%>
                    </td>
                </tr>






             </table>
             <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save"><br/>




        </form>



<%@ include file="/template/footer.jsp" %>



