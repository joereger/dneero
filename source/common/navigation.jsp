<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%--
<h:panelGrid columns="1" >
    <h:commandLink action="registration">
        <h:outputText value="Registration" />
    </h:commandLink>
    <h:commandLink action="userlist">
        <h:outputText value="User List" />
    </h:commandLink>
    <h:commandLink action="bloggerofferlist">
        <h:outputText value="Blogger Offer List" />
    </h:commandLink>
    <h:commandLink action="researcherofferlist">
        <h:outputText value="Researcher Offer List" />
    </h:commandLink>
</h:panelGrid>
--%>
<t:div id="subnavigation_outer">
    <t:div id="subnavigation">
        <h:form>
            <t:panelNavigation2 id="nav1" layout="list" itemClass="mypage" activeItemClass="selected" openItemClass="selected">
                <t:commandNavigation2 value="Home" action="home">
                    <t:commandNavigation2 action="registration">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Sign Up"/>
                    </t:commandNavigation2>
                </t:commandNavigation2>
                <t:commandNavigation2 value="Bloggers" action="bloggerhome">
                    <t:commandNavigation2 action="bloggerofferlist" >
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Offers"/>
                    </t:commandNavigation2>
                    <t:commandNavigation2 action="bloggerearnings">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Earnings"/>
                    </t:commandNavigation2>
                </t:commandNavigation2>
                <t:commandNavigation2 value="Researchers" action="researcherhome">
                    <t:commandNavigation2 action="researcherofferlist">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Offers"/>
                    </t:commandNavigation2>
                    <t:commandNavigation2 action="newoffer">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="New Offer"/>
                    </t:commandNavigation2>
                </t:commandNavigation2>
                <t:commandNavigation2 value="Admin" action="adminhome">
                    <t:commandNavigation2 action="userlist">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="User List"/>
                    </t:commandNavigation2>
                </t:commandNavigation2>
            </t:panelNavigation2>
        </h:form>
    </t:div>
</t:div>


