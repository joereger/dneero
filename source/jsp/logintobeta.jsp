<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >


    <h:form id="login">
        <br/><br/>
        <center>
            <font class="mediumfont"><a href="/index.jsf">We've emerged from the Closed Beta!  Click Here to continue!</a></font>
        </center>
        <!--
        <center>
        <img src="/images/dneero-site-placeholder.jpg" alt="dNeero... coming soon!"/>
        <br/>
        <table cellpadding="3" cellspacing="2" border="0">
            <tr>
                <td valign="top">
                    <font face="arial" size="-1"><b>Closed Beta Password: </b></font>
                </td>
                <td valign="top">
                    <h:inputSecret value="#{loginToBeta.betapassword}" id="betapassword" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                    <br/>
                    <font style="font-family: Arial, sans-serif; font-size: 9px;">You must click Let's Go button.</font>
                    <br/>
                    <h:message for="betapassword" styleClass="RED"></h:message>
                </td>
                <td valign="top">
                    <h:commandButton action="#{loginToBeta.login}" value="Let's Go!" styleClass="formsubmitbutton" style="color:#333;
                       font-family:'trebuchet ms',helvetica,sans-serif;
                       font-weight:bold;
                       font-size: 16px;
                       background-color:#00ff00;
                       border:4px solid;
                       border-top-color:#666666;
                       border-left-color:#666666;
                       border-right-color:#666666;
                       border-bottom-color:#666666;
                       margin: 2px;
                       cursor: pointer;
                       cursor: hand;"/>
                </td>
            </tr>
        </table>
        </center>
        -->
        
    </h:form>

</html>