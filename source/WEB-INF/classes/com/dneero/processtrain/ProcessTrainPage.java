package com.dneero.processtrain;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 3, 2006
 * Time: 10:09:18 AM
 */
public class ProcessTrainPage implements Serializable {

    private String viewId;
    private String outcome;
    private String label;
    private boolean disabled;

    public ProcessTrainPage(){
        
    }

    public ProcessTrainPage(String viewId, String label){
        setViewId(viewId);
        setLabel(label);
        setDisabled(false);
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


}
