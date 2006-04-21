package com.dneero.dao.hibernate;

import com.dneero.util.GeneralException;

/**
 * Simple validator interface
 */
public interface RegerEntity {

    public void load();
    public void validateRegerEntity() throws GeneralException;

}
