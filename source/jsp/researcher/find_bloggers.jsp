<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Find Bloggers";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <t:div rendered="#{researcherFindBloggers.msg ne '' and researcherFindBloggers.msg ne null}">
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        #{researcherFindBloggers.msg}
        <br/><br/></font></div></center>
        <br/><br/>
    </t:div>

    <t:saveState id="save" value="#{researcherFindBloggers}"/>
    <h:selectOneMenu value="#{researcherFindBloggers.panelid}" id="panelid" required="true" rendered="#{!empty researcherFindBloggers.listitems}">
       <f:selectItems value="#{researcherFindBloggers.panelids}"/>
    </h:selectOneMenu>
    <h:commandButton action="#{researcherFindBloggers.addAllToPanel}" value="Add All Bloggers Listed to Panel" styleClass="formsubmitbutton" rendered="#{!empty researcherFindBloggers.listitems}"></h:commandButton>
    <t:div rendered="#{!empty researcherFindBloggers.listitems}">
        <br/>
        <font class="smallfont">You can add an individual blogger to a panel by viewing his/her Blogger Profile.</font>
        <br/><br/>
    </t:div>
    <t:dataScroller for="datatable" maxPages="5" rendered="#{!empty researcherFindBloggers.listitems}"/>
    <t:dataTable id="datatable" value="#{researcherFindBloggers.listitems}" rows="50" var="listitem" rendered="#{!empty researcherFindBloggers.listitems}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Blogger Name"/>
        </f:facet>
        <h:outputText value="#{listitem.user.lastname}, #{listitem.user.firstname}" styleClass="normalfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Social Influence Rating (TM)"/>
        </f:facet>
        <h:outputText value="Top #{listitem.socialinfluenceratingpercentile}%" styleClass="normalfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="-" style="color: #ffffff;"/>
        </f:facet>
        <h:commandLink action="#{publicProfile.beginView}">
            <h:outputText value="Blogger's Profile" escape="false" styleClass="smallfont"/>
            <f:param name="bloggerid" value="#{listitem.blogger.bloggerid}" />
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
    <t:div rendered="#{!empty researcherFindBloggers.listitems}">
        <br/><br/>
    </t:div>


    <h:panelGrid columns="4" cellpadding="3" border="0" rendered="#{empty researcherFindBloggers.listitems}">



        <h:panelGroup>
            <h:outputText value="Social Influence Rating (TM)" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:outputText value="Social Influence Rating takes site traffic, survey referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership." styleClass="smallfont"></h:outputText>
            <br/>
            <h:message for="minsocialinfluencepercentile" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectOneMenu value="#{researcherFindBloggers.minsocialinfluencepercentile}" id="minsocialinfluencepercentile">
               <f:selectItems value="#{staticVariables.percentiles}"/>
            </h:selectOneMenu>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Social Influence Rating 90 Days" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="minsocialinfluencepercentile90days" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectOneMenu value="#{researcherFindBloggers.minsocialinfluencepercentile90days}" id="minsocialinfluencepercentile90days">
               <f:selectItems value="#{staticVariables.percentiles}"/>
            </h:selectOneMenu>
        </h:panelGroup>

        



        <h:panelGroup>
            <h:outputText value="Blog Quality of At Least" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:outputText value="Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating." styleClass="smallfont"></h:outputText>
            <br/>
            <h:message for="blogquality" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectOneListbox value="#{researcherFindBloggers.blogquality}" size="1" id="blogquality">
                <f:selectItems value="#{staticVariables.blogqualities}"/>
            </h:selectOneListbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Blog Quality Over Last 90 Days of At Least" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="blogquality90days" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectOneListbox value="#{researcherFindBloggers.blogquality90days}" size="1" id="blogquality90days">
                <f:selectItems value="#{staticVariables.blogqualities}"/>
            </h:selectOneListbox>
        </h:panelGroup>






        <h:panelGroup>
            <h:outputText value="Age Range" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="agemin" styleClass="RED"></h:message>
            <h:message for="agemax" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherFindBloggers.agemin}" id="agemin" size="3" required="true">
                <f:validateDoubleRange minimum="1" maximum="120"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value=" - "></h:outputText>
            <h:inputText value="#{researcherFindBloggers.agemax}" id="agemax" size="3" required="true">
                <f:validateDoubleRange minimum="1" maximum="120"></f:validateDoubleRange>
            </h:inputText>
        </h:panelGroup>




        <h:panelGroup>
            <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="gender" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyCheckbox value="#{researcherFindBloggers.gender}" id="gender" required="true">
                <f:selectItems value="#{genders}"/>
            </h:selectManyCheckbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="ethnicity" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.ethnicity}" id="ethnicity" size="6" required="true">
                <f:selectItems value="#{ethnicities}"/>
            </h:selectManyListbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="maritalstatus" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyCheckbox value="#{researcherFindBloggers.maritalstatus}" id="maritalstatus" required="true">
                <f:selectItems value="#{maritalstatuses}"/>
            </h:selectManyCheckbox>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="income" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.income}" size="5" id="income" required="true">
                <f:selectItems value="#{incomes}"/>
            </h:selectManyListbox>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="educationlevel" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyCheckbox value="#{researcherFindBloggers.educationlevel}" id="educationlevel" layout="pageDirection" required="true">
                <f:selectItems value="#{educationlevels}"/>
            </h:selectManyCheckbox>
        </h:panelGroup>



        <h:panelGroup>
            <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="state" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.state}" size="5" id="state" required="true">
                <f:selectItems value="#{states}"/>
            </h:selectManyListbox>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="City" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="city" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.city}" size="5" id="city" required="true">
                <f:selectItems value="#{cities}"/>
            </h:selectManyListbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="profession" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.profession}" size="5" id="profession" required="true">
                <f:selectItems value="#{professions}"/>
            </h:selectManyListbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Blog Focus" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="blogfocus" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.blogfocus}" size="5" id="blogfocus" required="true">
                <f:selectItems value="#{blogfocuses}"/>
            </h:selectManyListbox>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <h:message for="politics" styleClass="RED"></h:message>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectManyListbox value="#{researcherFindBloggers.politics}" size="5" id="politics" required="true">
                <f:selectItems value="#{politics}"/>
            </h:selectManyListbox>
        </h:panelGroup>

        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>








    </h:panelGrid>
    <br/><br/>
    <h:commandButton action="#{researcherFindBloggers.search}" value="Find Bloggers" styleClass="formsubmitbutton" rendered="#{empty researcherFindBloggers.listitems}"></h:commandButton>
    <h:commandButton action="#{researcherFindBloggers.resetSearch}" value="Reset Search" styleClass="formsubmitbutton" rendered="#{!empty researcherFindBloggers.listitems}"></h:commandButton>
        


<%@ include file="/jsp/templates/footer.jsp" %>

