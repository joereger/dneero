<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>


<h:form>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Earnings: </f:verbatim>
    <h:outputText value="#{BloggerEarnings.earnings}"/>

</h:form>


