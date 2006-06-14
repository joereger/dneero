<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>


<h:form>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Offerid: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.offerid}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Title: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.title}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Description: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.description}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Willing to Pay Per Respondent: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.willingtopayperrespondent}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Number of Respondents Requested: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.numberofrespondentsrequested}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>Start Date: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.startdate}"/>
    <f:verbatim><br></f:verbatim>
    <f:verbatim>End Date: </f:verbatim>
    <h:outputText value="#{BloggerOfferDetail.offer.enddate}"/>
</h:form>


