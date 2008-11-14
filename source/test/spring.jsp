<%@ page import="com.dneero.htmlui.UserSession" %>
<%@ page import="com.dneero.htmlui.Authorization" %>
<%@ page import="com.dneero.dao.Springtest" %>
<%@ page import="com.dneero.util.RandomString" %>


<%

try{

Springtest springtest =  Springtest.getNew();
springtest.setFoo("Foo-"+ RandomString.randomAlphabetic(5));
springtest.setBar("Bar-"+ RandomString.randomAlphabetic(5));
springtest.save();


%>


springtest.getSpringtestid()=<%=springtest.getSpringtestid()%>

<%
    } catch (Exception ex){
        ex.printStackTrace();
        %>Error: <%=ex.getMessage()%><%
    }
%>