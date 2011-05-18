package edu.ucla.cens.mobilize.client.dataaccess.awdataobjects;

import com.google.gwt.core.client.JavaScriptObject;

public class PromptResponseAwData extends JavaScriptObject {
  protected PromptResponseAwData() {};

  public final native int getSurveyResponseKey() /*-{ return this.survey_key; }-*/;
  
  public final native String getDisplayType() /*-{ return this.prompt_display_type; }-*/;
  public final native String getPromptId() /*-{ return this.prompt_id; }-*/;
  public final native String getPromptText() /*-{ return this.prompt_text; }-*/;
  
  // response can be text, number, or uuid of an image. use prompt type to determine which
  public final native String getPromptResponse() /*-{ 
    return this.prompt_response + ""; // make sure it's a string 
  }-*/;
  public final native String getPromptType() /*-{ return this.prompt_type; }-*/;
  
  public final native String getSurveyDescription() /*-{ return this.survey_description; }-*/;
  public final native String getSurveyId() /*-{ return this.survey_id; }-*/;
  public final native String getSurveyTitle() /*-{ return this.survey_title; }-*/;
  
  // NOTE(2011/05/04): privacy state is included with each prompt_response but is
  // actually the same for all prompt responses within a single survey_response
  public final native String getPrivacy() /*-{ return this.privacy_state; }-*/;
  
  // both timestamp and timezone are needed to determine real time
  public final native String getTimestamp() /*-{ return this.timestamp; }-*/;
  public final native String getTimezone() /*-{ return this.timezone; }-*/;
  
  public final native String getUser() /*-{ return this.user; }-*/;
  
  // only makes sense for choice prompt_types (single_choice, multi_choice, etc)
  public final native String getChoiceLabelFromGlossary(String key) /*-{
    // glossary looks like:"prompt_choice_glossary":[{"1":{"label":"Clothes"}},{"0":{"label":"Food"}},{"7":{"label":"Health"}}]
    // FIXME: ^^^ is that really what they mean it to look like??? Array of objects with only one key each?
    var retval = "---";
    if (this.prompt_choice_glossary != undefined) {
      for (var i = 0; i < this.prompt_choice_glossary.length; i++) {
        if (this.prompt_choice_glossary[i][key] != undefined) {
          retval = this.prompt_choice_glossary[i][key].label;
          break;
        }
      }
    }
    return retval;
    
    // FIXME: uncomment if we change it to hash of hashes instead
    //return (this.prompt_choice_glossary != undefined && this.prompt_choice_glossary[key] != undefined) ?
    //  this.prompt_choice_glossary[key].label :
    //  "---"; 
  }-*/;
  
}