<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicCharity" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.charity.CharityReport" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/charity-128.png\" alt=\"\" border=\"0\" width=\"128\" height=\"128\" align=\"right\"/>Make (Real) Change<br/><font class=\"mediumfont\">Make great content and great contributions -- at the same time!</font>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicCharity publicCharity = (PublicCharity) Pagez.getBeanMgr().get("PublicCharity");
%>
<%@ include file="/template/header.jsp" %>

            <br/><br/><br/><br/><br/>
            <table cellpadding="10" cellspacing="3" border="0">
                <tr>                                                                    
                    <td valign="top"  width="50%">
                        <font class="mediumfont" style="color: #999999">We'll Give Your Earnings to Charity</font>
                        <br/>
                        dNeero will, if you so choose, donate your earnings to charity.  Each time you join a conversation you'll be able to choose whether to donate and which charity to donate to.  We currently have a number of charitable options and will look to add more in the future.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Results</font>
                        <br/>
                        Every quarter we'll write checks to the charities that you've chosen for the amounts that you've deferred.  We'll list your donations on this page.  When somebody clicks to dNeero from your blog we'll tell them that you donated your earnings to charity.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Social People: Avoid Losing Any Credibility... and Support a Great Cause!</font>
                        <br/>
                        One of the big concerns from social people is that the money in this model presents possible bias, and that your readers will not want to feel 'monetized'. Thanks to open and sharing people like <a href="http://www.ck-blog.com/cks_blog/2007/07/new-models-new-.html">CK</a> we decided to make donating to charity a way to shed this concern -- now you can give any of the earnings to a good cause, avoid bias... and increase the features within your posts!
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Researchers and Marketers: Do Some Good</font>
                        <br/>
                        When you ignite a conversation you can mark it as Charity Only which means that only bloggers who are willing to have their earnings donated to charity will be able to take it.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Most Recent Donations</font>
                        <br/>
                        <%if (publicCharity.getTopdonatingUsers()==null || publicCharity.getTopdonatingUsers().size()==0){%>
                            <font class="normalfont">There haven't yet been any charitable donations... yet.</font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                cols.add(new GridCol("Donator", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a>", false, "", "smallfont"));
                                //cols.add(new GridCol("What was donated.", "<$charitydonation.description$>", false, "", "smallfont"));
                                //cols.add(new GridCol("Amount", "<$amtForScreen$>", false, "", "smallfont"));
                                cols.add(new GridCol("Donated To", "<$charitydonation.charityname$>", false, "", "smallfont"));
                            %>
                            <%=Grid.render(publicCharity.getPublicCharityListItemsMostRecent(), cols, 50, "/charity.jsp", "pagedonations")%>
                        <%}%>
                    </td>
                    <td valign="top">
                        <center>
                        <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                            <font class="mediumfont" style="color: #999999">Top Donators</font>
                            <br/>
                            <%if (publicCharity.getTopdonatingUsers()==null || publicCharity.getTopdonatingUsers().size()==0){%>
                                <font class="normalfont">Nobody's donated... yet.</font>
                            <%} else {%>
                                <%
                                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                    cols.add(new GridCol("Name", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a>", false, "", "smallfont"));
                                    cols.add(new GridCol("Donations", "<$amtforscreen$>", false, "", "smallfont"));
                                %>
                                <%=Grid.render(publicCharity.getTopdonatingUsers(), cols, 100, "/charity.jsp", "pagecharity")%>
                            <%}%>
                            <br/>
                            <font class="tinyfont" style="color: #999999">(Updated Nightly)</font>
                        </div>

                        <br/><br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                            <font class="mediumfont" style="color: #999999">Available Charities</font>
                            <br/>These are the charities that are currently available for you to choose from:
                            <blockquote><a href="http://www.habitat.org/" target="charity">Habitat for Humanity</a>
                            <br/><a href="http://www.wish.org/" target="charity">Make-A-Wish Foundation</a>
                            <br/><a href="http://www.cancer.org/" target="charity">American Cancer Society</a>
                            <br/><a href="http://www.petsmartcharities.org/" target="charity">PetSmart Charities</a>
                            <br/><a href="http://en.wikipedia.org/wiki/Wikimedia_Foundation" target="charity">Wikimedia Foundation</a>
                            <br/><a href="http://www.conservationfund.org/" target="charity">The Conservation Fund</a>
                            </blockquote>
                            And you can add your own favorite charity when you create a dNeero conversation!
                        </div>

                        <br/><br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                            <font class="mediumfont" style="color: #999999">Quarterly Progress Reports</font>
                            <%=CharityReport.getFullreport()%>
                        </div>
                            
                        </center>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">

                    </td>
                </tr>

            </table>







<%@ include file="/template/footer.jsp" %>
