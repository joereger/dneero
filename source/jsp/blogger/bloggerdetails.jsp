<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Your Profile<br/><br/></ui:define>
    <ui:param name="navtab" value="bloggers"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <t:div rendered="#{userSession.isloggedin and (userSession.user.bloggerid eq 0)}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">Quick One-time Configuration Required</font>
            <br/>
            <font class="smallfont">Before you can make money we need to collect a few pieces of information about you.  This will only take a minute or two and you won't have to do it again on future visits.  You'll be filling out surveys, posting them socially and making money in no time! We must take your profile so that we can find offers that fit you.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    </t:div>

    <table cellpadding="10" cellspacing="0" border="0" width="90%">
        <tr>
            <td valign="top">
                <h:form id="bloggerdetails">
                    <h:panelGrid columns="3" cellpadding="3" border="0">

                        <h:panelGroup>
                            <h:outputText value="Birthdate" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <t:inputDate value="#{bloggerDetails.birthdate}" type="date" popupCalendar="true" id="birthdate" required="true"></t:inputDate>
                            <br/>
                            <font class="tinyfont">(dd, mm, yyyy)... click the three dots for popup chooser</font>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="birthdate" styleClass="RED"></h:message>
                        </h:panelGroup>


                        <h:panelGroup>
                            <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneRadio value="#{bloggerDetails.gender}" id="gender" required="true">
                                <f:selectItems value="#{genders}"/>
                            </h:selectOneRadio>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="gender" styleClass="RED"></h:message>
                        </h:panelGroup>


                        <h:panelGroup>
                            <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.ethnicity}" id="ethnicity" required="true">
                                <f:selectItems value="#{ethnicities}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="ethnicity" styleClass="RED"></h:message>
                        </h:panelGroup>

                        <h:panelGroup>
                            <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.maritalstatus}" id="maritalstatus" required="true">
                                <f:selectItems value="#{maritalstatuses}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="maritalstatus" styleClass="RED"></h:message>
                        </h:panelGroup>

                        <h:panelGroup>
                            <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.income}" id="income" required="true">
                                <f:selectItems value="#{incomes}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="income" styleClass="RED"></h:message>
                        </h:panelGroup>

                        <h:panelGroup>
                            <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.educationlevel}" id="educationlevel" required="true">
                                <f:selectItems value="#{educationlevels}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="educationlevel" styleClass="RED"></h:message>
                        </h:panelGroup>



                        <h:panelGroup>
                            <h:outputText value="Nearest City" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.city}" id="city" required="true">
                                <f:selectItems value="#{cities}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="city" styleClass="RED"></h:message>
                        </h:panelGroup>


                        <h:panelGroup>
                            <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.state}" id="state" required="true">
                                <f:selectItems value="#{states}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="state" styleClass="RED"></h:message>
                        </h:panelGroup>


                        <h:panelGroup>
                            <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.profession}" id="profession" required="true">
                                <f:selectItems value="#{professions}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="profession" styleClass="RED"></h:message>
                        </h:panelGroup>



                        <h:panelGroup>
                            <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.politics}" id="politics" required="true">
                                <f:selectItems value="#{politics}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="politics" styleClass="RED"></h:message>
                        </h:panelGroup>

                        <h:panelGroup>
                            <h:outputText value="Your Blog's Focus" styleClass="formfieldnamefont"></h:outputText>
                            <br/>
                            <font class="tinyfont">The most accurate description of what<br/>you blog about or discuss on<br/>social networks.</font>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:selectOneMenu value="#{bloggerDetails.blogfocus}" id="blogfocus" required="true">
                                <f:selectItems value="#{blogfocuses}"/>
                            </h:selectOneMenu>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:message for="blogfocus" styleClass="RED"></h:message>
                        </h:panelGroup>



                        <h:panelGroup>
                        </h:panelGroup>
                        <h:panelGroup>
                            <h:commandButton action="#{bloggerDetails.saveAction}" value="Save Profile" styleClass="formsubmitbutton"></h:commandButton>
                        </h:panelGroup>
                        <h:panelGroup>
                        </h:panelGroup>


                    </h:panelGrid>

                </h:form>
            </td>
            <td valign="top" width="200">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <center>
                            <img src="/images/lock-64.png" width="64" height="64"/>
                            <br/>
                            <font class="mediumfont" style="color: #666666;">This information will be kept private.</font> 
                        </center>
                    </div>
                    <font class="smallfont">
                        <br/><br/><b>Why do you collect this information?</b><br/>
                        For two reasons. First, to present you with the most relevant surveys.  Second, because market researchers need to be able to target surveys to particular demographics

                        <br/><br/><b>Will this data ever be made available to others?</b><br/>
                        It will not be made available in a way that can be tied back to you.  However, we may create aggregate results on surveys that say things like "people aged 23-25 answered this way."  When we do this we're using the information from this page but it's in a way that your particular identity isn't shared.

                        <br/><br/><b>Check the survey criteria</b><br/>
                        If a survey has a sufficiently limited demographic target it's possible that survey readers can infer demographic data about you.  For example, if a researcher creates a survey targeted only to individuals with an income of between $30,000 and $40,000 per year and you take the survey then people can infer what your salary is.  When a survey is displayed both the criteria and who took the survey are visible.  Take this into account and if you're not comfortable with the scope of the demographic criteria that the researcher has chosen, tell them... they may choose to widen it.
                    </font>
                    <br/><br/>

                </div>
            </td>
        </tr>
    </table>


</ui:define>


</ui:composition>
</html>


