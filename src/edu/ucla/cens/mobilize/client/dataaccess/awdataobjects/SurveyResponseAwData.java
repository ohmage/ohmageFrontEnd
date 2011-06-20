package edu.ucla.cens.mobilize.client.dataaccess.awdataobjects;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class SurveyResponseAwData extends JavaScriptObject {
  protected SurveyResponseAwData() {}
    
  public final native int getSurveyResponseKey() /*-{ return this.survey_key; }-*/;
  public final native String getSurveyDescription() /*-{ return this.survey_description; }-*/;
  public final native String getSurveyId() /*-{ return this.survey_id; }-*/;
  public final native String getSurveyTitle() /*-{ return this.survey_title; }-*/;
  public final native String getPrivacy() /*-{ return this.privacy_state; }-*/;
  public final native String getTimestamp() /*-{ return this.timestamp; }-*/;
  public final native String getTimezone() /*-{ return this.timezone; }-*/;
  public final native String getUser() /*-{ return this.user; }-*/;

  public final native JsArrayString getPromptIdsAsJsArray() /*-{
    var retval = [];
    for (var prompt_id in this.responses) {
      retval.push(prompt_id);
    }
    return retval;
  }-*/;
  
  public final native PromptResponseAwData getPromptResponseById(String promptId) /*-{
    if (this.responses && promptId in this.responses) return this.responses[promptId]
    else return null;
  }-*/;
}
