<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<h:form>
<h:panelGrid columns="1" >
    <h:commandLink value="Home" action="home"/>
    <h:commandLink value="Sign Up" action="registration" style="padding-left: 25px;"/>
    <h:commandLink value="   Log In" action="login" style="padding-left: 25px;"/>
    <h:commandLink value="Bloggers" action="bloggerhome"/>
    <h:commandLink value="   Offers" action="bloggerofferlist" style="padding-left: 25px;"/>
    <h:commandLink value="   Earnings" action="bloggerearnings" style="padding-left: 25px;"/>
    <h:commandLink value="Researchers" action="researcherhome"/>
    <h:commandLink value="   Offers" action="researcherofferlist" style="padding-left: 25px;"/>
    <h:commandLink value="   New Offer" action="newoffer" style="padding-left: 25px;"/>
    <h:commandLink value="Admins" action="adminhome"/>
    <h:commandLink value="   Users" action="userlist" style="padding-left: 25px;"/>
</h:panelGrid>
</h:form>
<%--
<t:div id="subnavigation_outer">
    <t:div id="subnavigation">
        <h:form>
            <t:panelNavigation2 id="nav1" layout="list" itemClass="mypage" activeItemClass="selected" openItemClass="selected">
                <t:commandNavigation2 value="Home" action="home">
                    <t:commandNavigation2 action="registration">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Sign Up"/>
                    </t:commandNavigation2>
                    <t:commandNavigation2 action="login">
                        <f:verbatim> </f:verbatim>
                        <t:outputText value="Log In"/>
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
--%>


