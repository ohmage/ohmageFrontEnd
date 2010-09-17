package edu.ucla.cens.AndWellnessVisualizations.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A single error from a list of errors returned from the server.
 * 
 * @author jhicks
 *
 */
public class ErrorAwData extends JavaScriptObject {
    protected ErrorAwData() {};
    
    public final native String getCode() /*-{ return this.code; }-*/;
    public final native String getText() /*-{ return this.text; }-*/;
}