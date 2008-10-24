package com.dneero.db;

/**
 * User: Joe Reger Jr
 * Date: Oct 23, 2008
 * Time: 1:24:51 PM
 */
public class DbConfig {

    private String url = "";
    private String driver = "";
    private String username = "";
    private String password = "";

    public DbConfig(String url, String driver, String username, String password) {
        this.url=url;
        this.driver=driver;
        this.username=username;
        this.password=password;
    }

    public String toString(){
        return url +"-"+ driver +"-"+ username +"-"+ password;    
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver=driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }
}
