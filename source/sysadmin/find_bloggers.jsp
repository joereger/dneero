<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Find Bloggers";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="../template/auth.jsp" %>
<%@ include file="../template/header.jsp" %>



    <t:div rendered="#{sysadminFindBloggers.msg ne '' and sysadminFindBloggers.msg ne null}">
        <center>
            <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                <img src="/images/lightbulb_on.png" alt="" align="right"/>
                <%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getMsg()%>
        <br/><br/></font></div></center>
        <br/><br/>
    </t:div>

    <t:saveState id="save" value="#{sysadminFindBloggers}"/>
    <h:selectOneMenu value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getPanelid()%>" id="panelid" required="true" rendered="#{!empty sysadminFindBloggers.listitems}">
       <f:selectItems value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getPanelids()%>"/>
    </h:selectOneMenu>
    <h:commandButton action="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getAddAllToPanel()%>" value="Add All Bloggers Listed to Panel" styleClass="formsubmitbutton" rendered="#{!empty sysadminFindBloggers.listitems}"></h:commandButton>
    <t:div rendered="#{!empty sysadminFindBloggers.listitems}">
        <br/>
        <font class="smallfont">You can add an individual blogger to a panel by viewing his/her Blogger Profile.</font>
        <br/><br/>
    </t:div>
    <t:dataScroller for="datatable" maxPages="5" rendered="#{!empty sysadminFindBloggers.listitems}"/>
    <t:dataTable id="datatable" value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getListitems()%>" rows="50" var="listitem" rendered="#{!empty sysadminFindBloggers.listitems}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Blogger Name"/>
        </f:facet>
        <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getUser().getLastname()%>, <%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getUser().getFirstname()%>" styleClass="normalfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Social Influence Rating (TM)"/>
        </f:facet>
        <h:outputText value="Top <%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSocialinfluenceratingpercentile()%>%" styleClass="normalfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="-" style="color: #ffffff;"/>
        </f:facet>
        <h:commandLink action="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getBeginView()%>">
            <h:outputText value="Blogger's Profile" escape="false" styleClass="smallfont"/>
            <f:param name="bloggerid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getBlogger().getBloggerid()%>" />
        </h:commandLink>
      </h:column>
    </t:dataTable>
    <t:dataScroller id="scroll_1" for="datatable" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
        <f:facet name="first" >
            <t:graphicImage url="/images/datascroller/play-first.png" border="0" />
        </f:facet>
        <f:facet name="last">
            <t:graphicImage url="/images/datascroller/play-forward.png" border="0" />
        </f:facet>
        <f:facet name="previous">
            <t:graphicImage url="/images/datascroller/play-back.png" border="0" />
        </f:facet>
        <f:facet name="next">
            <t:graphicImage url="/images/datascroller/play.png" border="0" />
        </f:facet>
    </t:dataScroller>
    <t:div rendered="#{!empty sysadminFindBloggers.listitems}">
        <br/><br/>
    </t:div>


    <h:panelGrid columns="4" cellpadding="3" border="0" rendered="#{empty sysadminFindBloggers.listitems}">



        <td valign="top">
            <h:outputText value="Social Influence Rating (TM)" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:outputText value="Social Influence Rating takes site traffic, conversation referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership." styleClass="smallfont"></h:outputText>
            <br/>
            <h:message for="minsocialinfluencepercentile" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectOneMenu value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getMinsocialinfluencepercentile()%>" id="minsocialinfluencepercentile">
               <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getPercentiles()%>"/>
            </h:selectOneMenu>
        </td>


        <td valign="top">
            <h:outputText value="Social Influence Rating 90 Days" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="minsocialinfluencepercentile90days" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectOneMenu value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getMinsocialinfluencepercentile90days()%>" id="minsocialinfluencepercentile90days">
               <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getPercentiles()%>"/>
            </h:selectOneMenu>
        </td>

        



        <td valign="top">
            <h:outputText value="Blog Quality of At Least" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:outputText value="Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating." styleClass="smallfont"></h:outputText>
            <br/>
            <h:message for="blogquality" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectOneListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getBlogquality()%>" size="1" id="blogquality">
                <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getBlogqualities()%>"/>
            </h:selectOneListbox>
        </td>


        <td valign="top">
            <h:outputText value="Blog Quality Over Last 90 Days of At Least" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="blogquality90days" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectOneListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getBlogquality90days()%>" size="1" id="blogquality90days">
                <f:selectItems value="<%=((StaticVariables)Pagez.getBeanMgr().get("StaticVariables")).getBlogqualities()%>"/>
            </h:selectOneListbox>
        </td>






        <td valign="top">
            <h:outputText value="Age Range" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="agemin" styleClass="RED"></h:message>
            <h:message for="agemax" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:inputText value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getAgemin()%>" id="agemin" size="3" required="true">
                <f:validateDoubleRange minimum="1" maximum="120"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value=" - "></h:outputText>
            <h:inputText value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getAgemax()%>" id="agemax" size="3" required="true">
                <f:validateDoubleRange minimum="1" maximum="120"></f:validateDoubleRange>
            </h:inputText>
        </td>




        <td valign="top">
            <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="gender" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyCheckbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getGender()%>" id="gender" required="true">
                <f:selectItems value="#{genders}"/>
            </h:selectManyCheckbox>
        </td>


        <td valign="top">
            <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="ethnicity" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getEthnicity()%>" id="ethnicity" size="6" required="true">
                <f:selectItems value="#{ethnicities}"/>
            </h:selectManyListbox>
        </td>


        <td valign="top">
            <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="maritalstatus" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyCheckbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getMaritalstatus()%>" id="maritalstatus" required="true">
                <f:selectItems value="#{maritalstatuses}"/>
            </h:selectManyCheckbox>
        </td>

        <td valign="top">
            <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="income" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getIncome()%>" size="5" id="income" required="true">
                <f:selectItems value="#{incomes}"/>
            </h:selectManyListbox>
        </td>

        <td valign="top">
            <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="educationlevel" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyCheckbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getEducationlevel()%>" id="educationlevel" layout="pageDirection" required="true">
                <f:selectItems value="#{educationlevels}"/>
            </h:selectManyCheckbox>
        </td>



        <td valign="top">
            <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="state" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getState()%>" size="5" id="state" required="true">
                <f:selectItems value="#{states}"/>
            </h:selectManyListbox>
        </td>

        <td valign="top">
            <h:outputText value="City" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="city" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getCity()%>" size="5" id="city" required="true">
                <f:selectItems value="#{cities}"/>
            </h:selectManyListbox>
        </td>


        <td valign="top">
            <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="profession" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getProfession()%>" size="5" id="profession" required="true">
                <f:selectItems value="#{professions}"/>
            </h:selectManyListbox>
        </td>


        <td valign="top">
            <h:outputText value="Blog Focus" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="blogfocus" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getBlogfocus()%>" size="5" id="blogfocus" required="true">
                <f:selectItems value="#{blogfocuses}"/>
            </h:selectManyListbox>
        </td>


        <td valign="top">
            <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="politics" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:selectManyListbox value="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getPolitics()%>" size="5" id="politics" required="true">
                <f:selectItems value="#{politics}"/>
            </h:selectManyListbox>
        </td>

        <td valign="top">
        </td>
        <td valign="top">
        </td>








    </table>
    <br/><br/>
    <h:commandButton action="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getSearch()%>" value="Find Bloggers" styleClass="formsubmitbutton" rendered="#{empty sysadminFindBloggers.listitems}"></h:commandButton>
    <h:commandButton action="<%=((SysadminFindBloggers)Pagez.getBeanMgr().get("SysadminFindBloggers")).getResetSearch()%>" value="Reset Search" styleClass="formsubmitbutton" rendered="#{!empty sysadminFindBloggers.listitems}"></h:commandButton>
        


<%@ include file="../template/footer.jsp" %>

