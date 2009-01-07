package com.dneero.review;

import com.dneero.htmlui.ValidationException;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Apr 29, 2008
 * Time: 11:06:32 AM
 */
public interface Reviewable {

    public int getId();
    public int getType();
    public int getUseridofcontentcreator();
    public int getUseridofresearcher();
    public String getTypeName();
    public String getTypeDescription();
    public String getShortSummary();
    public String getFullSummary();
    public void rejectByResearcher() throws ValidationException;
    public void rejectBySysadmin() throws ValidationException;
    public void approveByResearcher() throws ValidationException;
    public void approveBySysadmin() throws ValidationException;
    public ArrayList<Reviewable> getPendingForResearcher(int researcherid);
    public ArrayList<Reviewable> getPendingForSysadmin();
    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid);
    public ArrayList<Reviewable> getRejectedBySysadmin();
    public boolean getIsresearcherreviewed();
    public boolean getIssysadminreviewed();
    public boolean getIsresearcherrejected();
    public boolean getIssysadminrejected();
    public Date getDate();
    public boolean supportsScoringByResearcher();
    public boolean supportsScoringBySysadmin();
    public void scoreByResearcher(int score);
    public void scoreBySysadmin(int score);



}
