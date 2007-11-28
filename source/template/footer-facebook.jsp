            <!-- End Body -->
            </td>
        </tr>
    </table>

    <br/><br/>
    <table width="90%" cellspacing="0" border="0" cellpadding="0">
        <tr>
            <td valign="top" align="right">
                <font class="tinyfont" style="color: #cccccc; padding-right: 10px">At Your Service is a Server Called: INSTANCENAME</font>
            </td>
        </tr>
    </table>

    <%
    String pgFooter = "/";
    if (request.getParameter("dpage")!=null){
        pgFooter = request.getParameter("dpage");
    }
    %>
    <fb:google-analytics uacct="UA-208946-2" page="<%=pgFooter%>"/>

</body>
</html>
