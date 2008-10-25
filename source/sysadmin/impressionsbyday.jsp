<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.survey.servlet.ImpressionsByDayUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dneero.dao.hibernate.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Impressions by Days Since Response";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


<%

    HashMap<Integer, ImpressionsByDayUtil> responses = new HashMap<Integer, ImpressionsByDayUtil>();

    HashMap<Integer, Integer> dayImpressions = new HashMap<Integer, Integer>();
    for(int i=0; i<ImpressionsByDayUtil.NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
        dayImpressions.put(i, 0);
    }
    HashMap<Integer, Integer> dayResponsesCount = new HashMap<Integer, Integer>();
    for(int i=0; i<ImpressionsByDayUtil.NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
        dayResponsesCount.put(i, 0);
    }

    //Get all impressions, big sql call
    List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                   .setCacheable(true)
                                   .list();
    for (Iterator<Impression> impressionIterator=impressions.iterator(); impressionIterator.hasNext();) {
        Impression impression=impressionIterator.next();
        //Get impressionsbyday for this impression
        ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil(impression.getImpressionsbyday());
        //Combine these impressions into the responses hashmap
        if (responses.containsKey(impression.getResponseid())){
            ImpressionsByDayUtil ibduAlreadyThere = responses.get(impression.getResponseid());
            ibduAlreadyThere.add(ibdu);
            responses.put(impression.getResponseid(), ibduAlreadyThere);
        } else {
            responses.put(impression.getResponseid(), ibdu);
        }
    }

    //Iterate impressions
    Iterator keyValuePairs = responses.entrySet().iterator();
    for (int j = 0; j < responses.size(); j++){
        Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
        int responseid = (Integer)mapentry.getKey();
        ImpressionsByDayUtil ibdu = (ImpressionsByDayUtil)mapentry.getValue();
        //Iterate each day
        for(int i=0; i<ImpressionsByDayUtil.NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
            int impressionsOnThisDayForThisResponse = ibdu.getImpressionsForParticularDay(i);
            //if (impressionsOnThisDayForThisResponse>0){
                dayImpressions.put(i, dayImpressions.get(i)+impressionsOnThisDayForThisResponse);
                dayResponsesCount.put(i, dayResponsesCount.get(i)+1);
            //}
        }

    }





%>

<table cellpadding="2" cellspacing="0" border="0">
<td valign="top">
        <font class="tinyfont"></font>
    </td>
    <td valign="top">
        <font class="tinyfont">TotImp</font>
    </td>
    <td valign="top">
        <font class="tinyfont">TotResp</font>
    </td>
    <td valign="top">
        <font class="tinyfont">AvgImp/Resp</font>
    </td>
<%
for(int i=0; i<ImpressionsByDayUtil.NUMBEROFDAYSTOSTOREASDAYBYDAY; i++){
    %>
    <tr>
        <td valign="top">
            <font class="tinyfont">Day <%=i%></font>
        </td>
        <td valign="top">
            <font class="tinyfont"><%=dayImpressions.get(i)%></font>
        </td>
        <td valign="top">
            <font class="tinyfont"><%=dayResponsesCount.get(i)%></font>
        </td>
        <td valign="top">
            <%if (dayResponsesCount.get(i)>0){%>
                <font class="tinyfont"><%=Double.parseDouble(String.valueOf(dayImpressions.get(i)))/Double.parseDouble(String.valueOf(dayResponsesCount.get(i)))%></font>
            <%} else {%>
                <font class="tinyfont">0</font>
            <%}%>
        </td>
    </tr>
    <%
}
%>
</table>



<%@ include file="/template/footer.jsp" %>