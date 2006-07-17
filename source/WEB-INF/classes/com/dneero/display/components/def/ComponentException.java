package com.dneero.display.components.def;

/**
 * A standard validation exception.
 */

public class ComponentException extends Exception {

    private String[] validationErrors = new String[0];

    public ComponentException(){

    }

    public ComponentException(String validationError){
        addValidationError(validationError);
    }

    public String getErrorsAsSingleString(){
        StringBuffer mb = new StringBuffer();
        for (int i = 0; i < validationErrors.length; i++) {
            String validationError = validationErrors[i];
            mb.append(validationError + "<br>");
        }
        return mb.toString();
    }

    public void addErrorsFromAnotherGeneralException(ComponentException errors){
        for (int i = 0; i < errors.getErrors().length; i++) {
            addValidationError(errors.getErrors()[i]);
        }
    }

    public String[] getErrors(){
        return validationErrors;
    }

    public void addValidationError(String validationError){
        if (validationErrors==null){
            validationErrors = new String[0];
        }
        validationErrors = com.dneero.util.Str.addToStringArray(validationErrors, validationError);
    }

}
