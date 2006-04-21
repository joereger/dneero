package com.dneero.dao.hibernate.eventlisteners;

import org.hibernate.event.*;
import com.dneero.dao.extendedpropscache.ExtendedPropsFactory;

public class RegerPreDeleteEventListener implements PreDeleteEventListener {

    public boolean onPreDelete(PreDeleteEvent event){
        ExtendedPropsFactory.flushExtended(event.getEntity());
        return false;
    }



}
