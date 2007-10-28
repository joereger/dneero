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
    <ui:define name="title">#{publicResultsAnswersDetails.survey.title}<br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="false"/>


<h:form>
    <h:messages styleClass="RED"/>

    <font class="smallfont">#{publicResultsAnswersDetails.survey.description}</font>
    <br/><br/><br/>


    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="65%">
                <f:verbatim escape="false">#{publicResultsAnswersDetails.results}</f:verbatim>
            </td>
            <td valign="top" align="left">
                <div class="rounded" style="background: #00ff00; text-align: center;">
                    <div class="rounded" style="background: #ffffff; text-align: center;">
                        <center><img src="/images/undo-128.png" width="128" height="128"/></center>
                        <br/>
                        <a href="/surveyresults.jsf?surveyid=#{publicResultsAnswersDetails.survey.surveyid}&amp;show=results"><font class="mediumfont">Return to the Survey Results</font></a>    
                    </div>
                </div>
            </td>
        </tr>
    </table>




</h:form>

</ui:define>


</ui:composition>
</html>


