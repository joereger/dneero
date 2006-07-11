package com.dneero.util;

/**
 * User: Joe Reger Jr
 * Date: Jun 28, 2006
 * Time: 4:41:45 PM
 */
import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.updateactionlistener.UpdateActionListener;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;

/**
 * Tag handler for the Tomahawk UpdateActionListener tag
 *
 * @version $Revision: 1.2 $
 * @author $Author: arobinson74 $
 */
public class UpdateActionListenerTaghandler extends TagHandler {
    private static Log logger = LogFactory.getLog(UpdateActionListenerTaghandler.class);

	private TagAttribute converterAttr;
	private TagAttribute propertyAttr;
	private TagAttribute valueAttr;

	/**
	 * @param config
	 */
	public UpdateActionListenerTaghandler(ComponentConfig config){
		this((TagConfig)config);
	}

	/**
	 * @param config
	 */
	public UpdateActionListenerTaghandler(TagConfig config){
		super(config);
		valueAttr = getRequiredAttribute("value");
		propertyAttr = getRequiredAttribute("property");
		converterAttr = getAttribute("converter");
	}

	/**
	 * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext, javax.faces.component.UIComponent)
	 */
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
		logger.debug("Apply called. Component: " + parent);
		ActionSource actionSource = (ActionSource)parent;

		if (sourceHasProperty(actionSource))
			return;

		UpdateActionListener al = new UpdateActionListener();

		if (converterAttr != null)
			al.setConverter((Converter)converterAttr.getObject(ctx));

		Application app = ctx.getFacesContext().getApplication();

		ValueBinding vb = app.createValueBinding(valueAttr.getValue());
		al.setValueBinding(vb);
		vb = app.createValueBinding(propertyAttr.getValue());
		al.setPropertyBinding(vb);
		actionSource.addActionListener(al);
	}

	private boolean sourceHasProperty(ActionSource source){
		for (ActionListener listener : source.getActionListeners()){
			if (listener instanceof UpdateActionListener == false) continue;
			UpdateActionListener al = (UpdateActionListener)listener;
			if (al.getPropertyBinding().getExpressionString().equals(this.propertyAttr.getValue())){
				logger.debug("Action listener already has a listener for " + this.propertyAttr.getValue());
				return true;
			}
		}
		logger.debug("Action listener for property is not present. Property: " + this.propertyAttr.getValue());
		return false;
	}
}

