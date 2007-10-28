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
    <ui:define name="title">About Us<br/><br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>
        <h:form>

            <font class="mediumfont" style="color: #0bae17;"><b>Quick Summary</b></font>
            <br/>
            We're in Atlanta.  Not funded.  Not seeking funding.  Seeking press.  Seeking researchers to create surveys.  Seeking bloggers to take surveys.
            <br/><br/>
            dNeero was created by <a href='http://www.joereger.com'>Joe Reger, Jr.</a> who works on it full-time.  Time is also kindly donated by Joe's father, Joe's sister, Barbara Stafford and Lance Weatherby.
            <br/><br/>
            An outgrowth of <a href='http://www.reger.com'>Reger.com</a> datablogging, we've been working on dNeero for a little over a year.  We've been involved with the blogosphere as a provider of datablogs since 1999.
            <br/><br/>
            dNeero went into closed beta on April 24th, 2007 and into public beta on June 25th, 2007.  Thanks to all the bloggers, market researchers and business advisors who took the time to beat on the system prior to launch.
            <br/><br/>

            <font class="mediumfont" style="color: #0bae17;"><b>Contact Us</b></font>
            <br/>Always more than happy to give you a demo or answer any questions you may have:
            <br/>Phone: 404.394.6102
            <br/>Email: info@dneero.com

        </h:form>
    </ui:define>
</ui:composition>
</html>