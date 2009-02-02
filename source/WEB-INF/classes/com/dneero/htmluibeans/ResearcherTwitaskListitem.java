package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;

/**
 * User: Joe Reger Jr
 * Date: Nov 12, 2007
 * Time: 6:04:15 PM
 */
public class ResearcherTwitaskListitem {

    private Twitask twitask;
    private String status;
    private String editorreviewlink;
    private String resultslink;
    private String copylink;
    private String deletelink;


    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getEditorreviewlink() {
        return editorreviewlink;
    }

    public void setEditorreviewlink(String editorreviewlink) {
        this.editorreviewlink=editorreviewlink;
    }

    public String getResultslink() {
        return resultslink;
    }

    public void setResultslink(String resultslink) {
        this.resultslink=resultslink;
    }

    public String getCopylink() {
        return copylink;
    }

    public void setCopylink(String copylink) {
        this.copylink=copylink;
    }

    public String getDeletelink() {
        return deletelink;
    }

    public void setDeletelink(String deletelink) {
        this.deletelink=deletelink;
    }
}