package com.dneero.db;

import org.apache.log4j.Logger;

import java.sql.Connection;

import com.dneero.systemprops.InstanceProperties;

/**
 * User: Joe Reger Jr
 * Date: Oct 23, 2008
 * Time: 12:29:36 PM
 */
public interface DbImplementation {

    public String getName();
    public Connection getConnection();
    public String[][] RunSQL(String sql, int recordstoreturn);
    public String[][] RunSQL(String sql);
    public int RunSQLUpdate(String sql);
    public int RunSQLInsert(String sql);
    public int RunSQLInsert(String sql, boolean isloggingon);
    public String printDriverStats() throws Exception;

}
