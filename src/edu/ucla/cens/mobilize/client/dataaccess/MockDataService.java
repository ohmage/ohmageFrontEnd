package edu.ucla.cens.mobilize.client.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucla.cens.mobilize.client.common.Privacy;
import edu.ucla.cens.mobilize.client.common.RunningState;
import edu.ucla.cens.mobilize.client.common.UserRole;
import edu.ucla.cens.mobilize.client.common.UserRoles;
import edu.ucla.cens.mobilize.client.common.UserStats;
import edu.ucla.cens.mobilize.client.model.AuthorizationTokenQueryAwData;
import edu.ucla.cens.mobilize.client.model.CampaignDetailedInfo;
import edu.ucla.cens.mobilize.client.model.CampaignConciseInfo;
import edu.ucla.cens.mobilize.client.model.DataPointAwData;
import edu.ucla.cens.mobilize.client.model.DataPointQueryAwData;
import edu.ucla.cens.mobilize.client.model.PromptInfo;
import edu.ucla.cens.mobilize.client.model.SurveyInfo;
import edu.ucla.cens.mobilize.client.model.SurveyResponse;
import edu.ucla.cens.mobilize.client.model.UserInfo;
import edu.ucla.cens.mobilize.client.model.SurveyResponse.ResponseStatus;
import edu.ucla.cens.mobilize.client.utils.XmlConfigTranslator;

public class MockDataService implements DataService {
  //List<CampaignDetailedInfo> campaigns = new ArrayList<CampaignDetailedInfo>();
  List<CampaignConciseInfo> campaignsConcise = new ArrayList<CampaignConciseInfo>();
  List<SurveyResponse> surveyResponses = new ArrayList<SurveyResponse>();
  
  Map<String, CampaignDetailedInfo> campaigns = new HashMap<String, CampaignDetailedInfo>();
  
  XmlConfigTranslator configTranslator = new XmlConfigTranslator();

  private static Logger _logger = Logger.getLogger(MockDataService.class.getName());
  
  public MockDataService() {
    loadFakeCampaigns();
  }
  
  private void loadFakeCampaigns() {
    
    campaigns.clear();
    campaignsConcise.clear();
    for (int i = 0; i < 3; i++) {
      CampaignDetailedInfo info = new CampaignDetailedInfo();
      
      // detail 1
      info.setCampaignName("NIH_SleepSens" + Integer.toString(i));
      List<UserRole> roles = new ArrayList<UserRole>();
      roles.add(UserRole.PARTICIPANT);
      roles.add(UserRole.ANALYST);
      info.setUserRoles(roles);
      info.setDescription("Monitor sleeping patterns");
      info.setPrivacy(Privacy.PUBLIC);
      info.setRunningState(RunningState.RUNNING);
      info.setXmlConfig(this.getConfigXml());
      
      campaigns.put(info.getCampaignId(), info);
      
      info = new CampaignDetailedInfo();
      info.setCampaignName("NIH_DietSens" + Integer.toString(i));
      roles = new ArrayList<UserRole>();
      roles.add(UserRole.PARTICIPANT);
      roles.add(UserRole.AUTHOR);
      info.setUserRoles(roles);
      info.setDescription("What people eat");
      info.setPrivacy(Privacy.PUBLIC);
      info.setRunningState(RunningState.RUNNING);
      info.setXmlConfig(this.getConfigXml());
      
      campaigns.put(info.getCampaignId(), info);
      
      info = new CampaignDetailedInfo();
      info.setCampaignName("Advertising" + Integer.toString(i));
      roles = new ArrayList<UserRole>();
      roles.add(UserRole.ADMIN);
      roles.add(UserRole.PARTICIPANT);
      roles.add(UserRole.ANALYST);
      info.setUserRoles(roles);
      info.setDescription("Raise awareness of advertisements in the community");
      info.setPrivacy(Privacy.PUBLIC);
      info.setRunningState(RunningState.STOPPED);
      info.setXmlConfig(this.getConfigXml());
      campaigns.put(info.getCampaignId(), info);
      
      // copy over relevant values
      for (CampaignDetailedInfo cdi : campaigns.values()) {
        campaignsConcise.add(new CampaignConciseInfo(cdi.getCampaignId(),
            cdi.getCampaignName(),
            cdi.getRunningState(),
            cdi.getPrivacy(),
            new UserRoles(cdi.getUserRoles())
            ));
      }
    }
  }
  
  private void loadFakeResponses() {
    this.surveyResponses.clear();
    for (CampaignDetailedInfo info : this.campaigns.values()) {
      for (int i = 0; i < 5; i++) {
        SurveyResponse resp = new SurveyResponse();
        // TODO
      }
    }
  }
  
  private String getConfigXml() {
    return "<?xmlversion=\"1.0\"encoding=\"UTF-8\"?><campaign><campaignName>NIH</campaignName><campaignVersion>1.0</campaignVersion><serverUrl>https://heart.andwellness.org</serverUrl><surveys><survey><id>diet</id><title>Diet</title><description>Thisisthedietsurvey.</description><submitText>Thisisthedietsurveysubmittext.</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>mealPrepared</id><displayType>category</displayType><displayLabel>Howwasyourlastmealprepared</displayLabel><promptText>Howwasyourlastmealprepared?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Restaurant</label></property><property><key>1</key><label>Fastfood</label></property><property><key>2</key><label>Homemade</label></property><property><key>3</key><label>Pre-pared(i.e.,deli,saladbar)</label></property><property><key>4</key><label>Frozendinner</label></property><property><key>5</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>whoDidYouEatWith</id><displayType>category</displayType><displayLabel>Whodidyoueatwith</displayLabel><promptText>Whodidyoueatwith?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Alone</label></property><property><key>1</key><label>Friends</label></property><property><key>2</key><label>Family</label></property><property><key>3</key><label>Co-workers</label></property><property><key>4</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>howMuchDidYouEat</id><displayType>category</displayType><displayLabel>Howmuchdidyoueat</displayLabel><promptText>Howmuchdidyoueat?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Toolittle/notenough</label></property><property><key>1</key><label>Justright</label></property><property><key>2</key><label>Toomuch</label></property></properties><skippable>false</skippable></prompt><prompt><id>nutritionalQuality</id><displayType>category</displayType><displayLabel>Ratethenutritionalqualityofthismeal</displayLabel><promptText>Ratethenutritionalqualityofthismeal</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>low</label></property><property><key>1</key><label>medium</label></property><property><key>2</key><label>high</label></property></properties><skippable>false</skippable></prompt></contentList></survey><survey><id>exerciseAndActivity</id><title>ExerciseandActivity</title><description>Thisistheexerciseandactivitysurvey.</description><submitText>Thisistheexerciseandactivitysurveysubmittext.</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>didYouExercise</id><displayType>category</displayType><displayLabel>Haveyouexercisedtoday</displayLabel><promptText>Haveyouexercisedtoday?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>No</label></property><property><key>1</key><label>Yes</label></property></properties><skippable>false</skippable></prompt><prompt><id>planningToExercise</id><condition>didYouExercise==0</condition><displayType>category</displayType><displayLabel>Wereyouplanningtoexercisetoday</displayLabel><promptText>Wereyouplanningtoexercisetoday?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>No</label></property><property><key>1</key><label>Yes</label></property></properties><skippable>false</skippable></prompt><prompt><id>whyNotExercise</id><condition>didYouExercise==0andplanningToExercise==1</condition><displayType>category</displayType><displayLabel>Disruptedplan</displayLabel><promptText>Whatpreventedyoufromexercisingasplanned?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Childcare</label></property><property><key>1</key><label>Work</label></property><property><key>2</key><label>Personalneeds</label></property><property><key>3</key><label>Householdresponsibilities</label></property><property><key>4</key><label>Injured,inpain</label></property><property><key>5</key><label>Socialfunction</label></property><property><key>6</key><label>Medicalemergency</label></property><property><key>7</key><label>Noenergy</label></property><property><key>8</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>typeExercise</id><condition>didYouExercise==1</condition><displayType>category</displayType><displayLabel>TypeExercise</displayLabel><promptText>Describethetypesofexerciseyoudidtoday.</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Walk</label></property><property><key>1</key><label>Run</label></property><property><key>2</key><label>Yoga</label></property><property><key>3</key><label>Outdoorcyclingorsports</label></property><property><key>4</key><label>Indoorsports</label></property><property><key>5</key><label>Gym</label></property><property><key>6</key><label>Swim</label></property><property><key>7</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>timeSpentLight</id><condition>didYouExercise==1</condition><displayType>count</displayType><displayLabel>Timelightexercise</displayLabel><promptText>Howmanyminutesoflightexercisedidyoudotoday?(Noincreaseinbreathingorheartrate.)</promptText><promptType>number</promptType><properties><property><key>min</key><label>0</label></property><property><key>max</key><label>300</label></property></properties><default>15</default><skippable>false</skippable></prompt><prompt><id>timeSpentModerate</id><condition>didYouExercise==1</condition><displayType>count</displayType><displayLabel>Timemoderateexercise</displayLabel><promptText>Howmanyminutesofmoderateexercisedidyoudotoday?(Mildincreaseinbreathingorheartrate.)</promptText><promptType>number</promptType><properties><property><key>min</key><label>0</label></property><property><key>max</key><label>300</label></property></properties><default>15</default><skippable>false</skippable></prompt><prompt><id>timeSpentVigorous</id><condition>didYouExercise==1</condition><displayType>count</displayType><displayLabel>Timevigorousexercise</displayLabel><promptText>Howmanyminutesofvigorousexercisedidyoudotoday?(Significantincreaseinbreathingorheartrate.)</promptText><promptType>number</promptType><properties><property><key>min</key><label>0</label></property><property><key>max</key><label>300</label></property></properties><default>15</default><skippable>false</skippable></prompt><prompt><id>whoWith</id><condition>didYouExercise==1</condition><displayType>category</displayType><displayLabel>Whodidyouexercisewith</displayLabel><promptText>Whodidyoudothisexercisewith?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Alone</label></property><property><key>1</key><label>Friends</label></property><property><key>2</key><label>Child</label></property><property><key>3</key><label>Partner/Spouse</label></property><property><key>4</key><label>Co-workers</label></property><property><key>5</key><label>Classorgroup</label></property><property><key>6</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>physicalActivity</id><displayType>category</displayType><displayLabel>Whatotherkindsofphysicalactivitydidyoudotoday</displayLabel><promptText>Whatotherkindsofphysicalactivitydidyoudotoday?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>None</label></property><property><key>1</key><label>Housework/Gardening</label></property><property><key>2</key><label>Workrelated</label></property><property><key>3</key><label>Carryingheavyloads</label></property><property><key>4</key><label>Activeplaywithchildren</label></property><property><key>5</key><label>Walking</label></property><property><key>6</key><label>Other</label></property></properties><skippable>false</skippable></prompt><prompt><id>timeForYourself</id><displayType>count</displayType><displayLabel>Howmuchtimedidyouhaveforyourselftoday</displayLabel><promptText>Howmuchtimedidyouhaveforyourselftoday?</promptText><promptType>number</promptType><properties><property><key>min</key><label>0</label></property><property><key>max</key><label>300</label></property></properties><default>15</default><skippable>false</skippable></prompt></contentList></survey><survey><id>foodButton</id><title>Stress</title><description>Notdisplayed</description><submitText>Notdisplayed</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>foodPhoto</id><displayType>event</displayType><displayLabel>photo</displayLabel><promptText>Takeapictureofyourfood</promptText><abbreviatedText>abbreviatedTextmustnotbeemptyifshowSummaryonitsparentsurveyistrue.</abbreviatedText><promptType>photo</promptType><properties><property><key>res</key><label>720</label></property></properties><skippable>true</skippable><skipLabel>skip</skipLabel></prompt><prompt><id>foodSnackMeal</id><displayType>category</displayType><displayLabel>Type</displayLabel><promptText>Wasthisasnackorameal?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Snack</label><value>0</value></property><property><key>1</key><label>Meal</label><value>1</value></property></properties><skippable>false</skippable></prompt><prompt><id>foodHowHungry</id><displayType>count</displayType><displayLabel>Hunger</displayLabel><unit>hungeramount</unit><promptText>Howhungrywereyoubeforeeating?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Very</label><value>0</value></property><property><key>1</key><label>Moderately</label><value>1</value></property><property><key>2</key><label>Slightly</label><value>2</value></property><property><key>3</key><label>Notatall</label><value>3</value></property></properties><skippable>false</skippable></prompt><prompt><id>foodHowPrepared</id><displayType>category</displayType><displayLabel>HowPrepared</displayLabel><promptText>Howwasthisfoodprepared?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Restaurant</label><value>0</value></property><property><key>1</key><label>Fastfood</label><value>1</value></property><property><key>2</key><label>Homemade</label><value>2</value></property><property><key>3</key><label>Pre-pared(i.e.,deli,saladbar)</label><value>3</value></property><property><key>4</key><label>Frozendinner</label><value>4</value></property><property><key>5</key><label>Other</label><value>5</value></property></properties><skippable>false</skippable></prompt><prompt><id>foodWhoAteWith</id><displayType>category</displayType><displayLabel>WhoWith</displayLabel><promptText>Whodidyoueatwith?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Alone</label><value>0</value></property><property><key>1</key><label>Friends</label><value>1</value></property><property><key>2</key><label>Family</label><value>2</value></property><property><key>3</key><label>Co-workers</label><value>3</value></property><property><key>4</key><label>Other</label><value>4</value></property></properties><skippable>false</skippable></prompt><prompt><id>foodHowMuch</id><displayType>category</displayType><displayLabel>HowMuch</displayLabel><promptText>Howmuchdidyoueat?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Toolittle/notenough</label><value>0</value></property><property><key>1</key><label>Justright</label><value>1</value></property><property><key>2</key><label>Toomuch</label><value>2</value></property></properties><skippable>false</skippable></prompt><prompt><id>foodQuality</id><displayType>category</displayType><displayLabel>NutritionalQuality</displayLabel><promptText>Ratethenutritionalqualityofthismeal?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Low</label><value>0</value></property><property><key>1</key><label>Medium</label><value>1</value></property><property><key>2</key><label>High</label><value>2</value></property></properties><skippable>false</skippable></prompt></contentList></survey><survey><id>moodAndStress</id><title>MoodandStress</title><description>Thisisthemoodandstresssurvey.</description><submitText>Thisisthemoodandstresssurveysubmittext.</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>mood</id><displayType>category</displayType><displayLabel>Howwouldyoudescribeyourmoodatthismoment</displayLabel><promptText>Howwouldyoudescribeyourmoodatthismoment?</promptText><promptType>multi_choice</promptType><properties><property><key>0</key><label>Sad</label></property><property><key>1</key><label>Relaxed</label></property><property><key>2</key><label>Anxious</label></property><property><key>3</key><label>Tired</label></property><property><key>4</key><label>Happy</label></property><property><key>5</key><label>Upset</label></property><property><key>6</key><label>Energetic</label></property><property><key>7</key><label>Irritable</label></property><property><key>8</key><label>Calm</label></property><property><key>9</key><label>Bored</label></property><property><key>10</key><label>Focused</label></property><property><key>11</key><label>Stressed</label></property></properties><skippable>false</skippable></prompt><prompt><id>feltStress</id><displayType>category</displayType><displayLabel>Stressinlasttwohours</displayLabel><promptText>Haveyoufeltstressinthelasttwohours?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>No</label></property><property><key>1</key><label>Yes</label></property></properties><skippable>false</skippable></prompt><prompt><id>howStressed</id><displayType>measurement</displayType><unit>howstressed</unit><condition>feltStress==1</condition><displayLabel>Howstressed</displayLabel><promptText>Howstressedwereyou,onascaleof1-5?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>1</label><value>1</value></property><property><key>1</key><label>2</label><value>2</value></property><property><key>2</key><label>3</label><value>3</value></property><property><key>3</key><label>4</label><value>4</value></property><property><key>4</key><label>5</label><value>5</value></property></properties><skippable>false</skippable></prompt><prompt><id>whatEvent</id><condition>feltStress==1</condition><displayType>category</displayType><displayLabel>Cause</displayLabel><promptText>Whatwasthecauseofyourstress?</promptText><promptType>single_choice</promptType><properties><property><key>0</key><label>Finances</label></property><property><key>1</key><label>Work/School</label></property><property><key>2</key><label>Lackofcontrol</label></property><property><key>3</key><label>Family/Relationships</label></property><property><key>4</key><label>Health</label></property><property><key>5</key><label>Traffic</label></property><property><key>6</key><label>Other</label></property></properties><skippable>false</skippable></prompt></contentList></survey><survey><id>sleep</id><title>Sleep</title><description>Thisisthesleepsurvey.</description><submitText>Thisisthesleepsurveysubmittext.</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>hoursOfSleep</id><displayType>count</displayType><displayLabel>HoursofSleep</displayLabel><promptText>Howmanyhoursintotaldidyousleeplastnight?</promptText><promptType>number</promptType><properties><property><key>min</key><label>0</label></property><property><key>max</key><label>24</label></property></properties><skippable>false</skippable></prompt></contentList></survey><survey><id>stressButton</id><title>Stress</title><description>Notdisplayed</description><submitText>Notdisplayed</submitText><showSummary>false</showSummary><anytime>true</anytime><contentList><prompt><id>stress</id><displayType>metadata</displayType><displayLabel>Stress</displayLabel><promptText>Notdisplayed</promptText><promptType>text</promptType><properties><property><key>min</key><label>1</label></property><property><key>max</key><label>1</label></property></properties><skippable>false</skippable></prompt></contentList></survey></surveys></campaign>";
  }
  
  @Override
  public void fetchUserInfo(String username, AsyncCallback<UserInfo> callback) {
    UserStats counts = new UserStats();
    
    counts.numUnreadResponses = Random.nextInt(20);
    
    boolean canCreate = true;
    List<String> groups = new ArrayList<String>();
    groups.add("CS101");
    groups.add("CS230");
    groups.add("MATH105");
    List<CampaignDetailedInfo> infos = new ArrayList<CampaignDetailedInfo>(this.campaigns.values());
    UserInfo user = new UserInfo(username, canCreate, infos, groups);
    
    callback.onSuccess(user);
  }

  @Override
  public void fetchCampaignList(Map<String, List<String>> params,
      AsyncCallback<List<CampaignConciseInfo>> callback) {
    callback.onSuccess(campaignsConcise);    
  }

  @Override
  public void fetchCampaignDetail(String campaignId,
      AsyncCallback<CampaignDetailedInfo> callback) {
    for (int i = 0; i < campaigns.size(); i++) {
      if (campaigns.containsKey(campaignId) && 
          campaigns.get(campaignId).getCampaignId().equals(campaignId)) {
        callback.onSuccess(campaigns.get(campaignId));
        break;
      }
    }
  }
  
  @Override
  public void fetchCampaignDetailList(List<String> campaignIds,
      AsyncCallback<List<CampaignDetailedInfo>> callback) {
    List<CampaignDetailedInfo> infos = new ArrayList<CampaignDetailedInfo>();
    for (String id : campaignIds) {
      if (campaigns.containsKey(id)) {
        infos.add(campaigns.get(id));
      }
    }
    callback.onSuccess(infos);
  }

  @Override
  public CampaignDetailedInfo getCampaignDetail(String campaignId) {
    CampaignDetailedInfo info = null;
    if (campaigns.containsKey(campaignId)) {
      info = campaigns.get(campaignId); // FIXME: make a copy
    }
    return info;
  }
  
  @Override
  public void fetchAuthorizationToken(String userName, String password,
      AsyncCallback<AuthorizationTokenQueryAwData> callback) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteCampaign(String campaignId,
      AsyncCallback<ResponseDelete> callback) {
    // randomly fakes success or failure
    // FIXME: should onFailure be used when request returns successfully
    // but the operation wasn't completed?
    String responseJson = Random.nextBoolean() ? getSuccessJson() : getFailureJson();
    ResponseDelete resp = ResponseDelete.fromJson(responseJson); 
    if (resp.wasSuccessful()) {
      callback.onSuccess(resp);
    } else {
      callback.onFailure(new Exception(resp.getMsg()));
    }
  }
  
  // DELETEME
  private String getSuccessJson() {
    return "{\"result\":\"success\"}";
  }
  
  // DELETEME
  private String getFailureJson() {
    return "{\"result\":\"error\",\"msg\":\"Campaign can not be deleted because it has responses.\",\"error\":\"1\"}";
  }
  
  // DELETEME
  private String getDataPointArrayJson() {
    return "{\"result\":\"success\",\"data\":[{\"tz\":\"MST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-22 13:30:49\",\"type\":\"count\",\"long\":42.201098401087265,\"lat\":-19.99481899037932},{\"tz\":\"MST\",\"value\":\"2010-09-22 06:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-22 13:30:49\",\"type\":\"event\",\"long\":42.201098401087265,\"lat\":-19.99481899037932},{\"tz\":\"MST\",\"value\":\"2010-09-22 04:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-22 13:30:49\",\"type\":\"event\",\"long\":42.201098401087265,\"lat\":-19.99481899037932},{\"tz\":\"CST\",\"value\":\"2\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-22 01:30:49\",\"type\":\"count\",\"long\":98.87803474387354,\"lat\":39.58232897931106},{\"tz\":\"CST\",\"value\":\"2010-09-21 08:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-22 01:30:49\",\"type\":\"event\",\"long\":98.87803474387354,\"lat\":39.58232897931106},{\"tz\":\"CST\",\"value\":\"2010-09-21 17:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-22 01:30:49\",\"type\":\"event\",\"long\":98.87803474387354,\"lat\":39.58232897931106},{\"tz\":\"MST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-21 13:30:49\",\"type\":\"count\",\"long\":145.02013645382516,\"lat\":-22.18492537781594},{\"tz\":\"MST\",\"value\":\"2010-09-21 06:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-21 13:30:49\",\"type\":\"event\",\"long\":145.02013645382516,\"lat\":-22.18492537781594},{\"tz\":\"MST\",\"value\":\"2010-09-20 17:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-21 13:30:49\",\"type\":\"event\",\"long\":145.02013645382516,\"lat\":-22.18492537781594},{\"tz\":\"MST\",\"value\":\"1\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-21 01:30:49\",\"type\":\"count\",\"long\":113.65186526849861,\"lat\":62.2806354443809},{\"tz\":\"MST\",\"value\":\"2010-09-20 12:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-21 01:30:49\",\"type\":\"event\",\"long\":113.65186526849861,\"lat\":62.2806354443809},{\"tz\":\"MST\",\"value\":\"2010-09-20 15:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-21 01:30:49\",\"type\":\"event\",\"long\":113.65186526849861,\"lat\":62.2806354443809},{\"tz\":\"PST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-20 13:30:49\",\"type\":\"count\",\"long\":62.6374577947218,\"lat\":-55.51578188569921},{\"tz\":\"PST\",\"value\":\"2010-09-20 10:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-20 13:30:49\",\"type\":\"event\",\"long\":62.6374577947218,\"lat\":-55.51578188569921},{\"tz\":\"PST\",\"value\":\"2010-09-20 12:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-20 13:30:49\",\"type\":\"event\",\"long\":62.6374577947218,\"lat\":-55.51578188569921},{\"tz\":\"EST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-20 01:30:49\",\"type\":\"count\",\"long\":-18.869441230417472,\"lat\":71.15037323085525},{\"tz\":\"EST\",\"value\":\"2010-09-19 13:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-20 01:30:49\",\"type\":\"event\",\"long\":-18.869441230417472,\"lat\":71.15037323085525},{\"tz\":\"EST\",\"value\":\"2010-09-19 07:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-20 01:30:49\",\"type\":\"event\",\"long\":-18.869441230417472,\"lat\":71.15037323085525},{\"tz\":\"CST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-19 13:30:49\",\"type\":\"count\",\"long\":-175.31580709324746,\"lat\":-74.12724979328223},{\"tz\":\"CST\",\"value\":\"2010-09-19 04:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-19 13:30:49\",\"type\":\"event\",\"long\":-175.31580709324746,\"lat\":-74.12724979328223},{\"tz\":\"CST\",\"value\":\"2010-09-19 12:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-19 13:30:49\",\"type\":\"event\",\"long\":-175.31580709324746,\"lat\":-74.12724979328223},{\"tz\":\"CST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-19 01:30:49\",\"type\":\"count\",\"long\":116.5211927425387,\"lat\":-4.109759423362579},{\"tz\":\"CST\",\"value\":\"2010-09-18 23:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-19 01:30:49\",\"type\":\"event\",\"long\":116.5211927425387,\"lat\":-4.109759423362579},{\"tz\":\"CST\",\"value\":\"2010-09-18 21:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-19 01:30:49\",\"type\":\"event\",\"long\":116.5211927425387,\"lat\":-4.109759423362579},{\"tz\":\"MST\",\"value\":\"3\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-18 13:30:49\",\"type\":\"count\",\"long\":-49.66035332668126,\"lat\":10.210975143148275},{\"tz\":\"MST\",\"value\":\"2010-09-18 10:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-18 13:30:49\",\"type\":\"event\",\"long\":-49.66035332668126,\"lat\":10.210975143148275},{\"tz\":\"MST\",\"value\":\"2010-09-18 02:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-18 13:30:49\",\"type\":\"event\",\"long\":-49.66035332668126,\"lat\":10.210975143148275},{\"tz\":\"PST\",\"value\":\"3\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-18 01:30:49\",\"type\":\"count\",\"long\":-139.88337396790135,\"lat\":-87.577379371197},{\"tz\":\"PST\",\"value\":\"2010-09-18 00:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-18 01:30:49\",\"type\":\"event\",\"long\":-139.88337396790135,\"lat\":-87.577379371197},{\"tz\":\"PST\",\"value\":\"2010-09-17 16:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-18 01:30:49\",\"type\":\"event\",\"long\":-139.88337396790135,\"lat\":-87.577379371197},{\"tz\":\"PST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-17 13:30:49\",\"type\":\"count\",\"long\":154.0511614244373,\"lat\":40.800085009580776},{\"tz\":\"PST\",\"value\":\"2010-09-17 08:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-17 13:30:49\",\"type\":\"event\",\"long\":154.0511614244373,\"lat\":40.800085009580776},{\"tz\":\"PST\",\"value\":\"2010-09-16 17:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-17 13:30:49\",\"type\":\"event\",\"long\":154.0511614244373,\"lat\":40.800085009580776},{\"tz\":\"PST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-17 01:30:49\",\"type\":\"count\",\"long\":70.0356020452156,\"lat\":77.74868557197841},{\"tz\":\"PST\",\"value\":\"2010-09-16 20:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-17 01:30:49\",\"type\":\"event\",\"long\":70.0356020452156,\"lat\":77.74868557197841},{\"tz\":\"PST\",\"value\":\"2010-09-16 12:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-17 01:30:49\",\"type\":\"event\",\"long\":70.0356020452156,\"lat\":77.74868557197841},{\"tz\":\"PST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-16 13:30:49\",\"type\":\"count\",\"long\":-53.99917234240018,\"lat\":-67.67421452576248},{\"tz\":\"PST\",\"value\":\"2010-09-15 20:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-16 13:30:49\",\"type\":\"event\",\"long\":-53.99917234240018,\"lat\":-67.67421452576248},{\"tz\":\"PST\",\"value\":\"2010-09-16 05:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-16 13:30:49\",\"type\":\"event\",\"long\":-53.99917234240018,\"lat\":-67.67421452576248},{\"tz\":\"EST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-16 01:30:49\",\"type\":\"count\",\"long\":-105.41878630986679,\"lat\":-48.9517959223501},{\"tz\":\"EST\",\"value\":\"2010-09-15 10:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-16 01:30:49\",\"type\":\"event\",\"long\":-105.41878630986679,\"lat\":-48.9517959223501},{\"tz\":\"EST\",\"value\":\"2010-09-15 20:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-16 01:30:49\",\"type\":\"event\",\"long\":-105.41878630986679,\"lat\":-48.9517959223501},{\"tz\":\"MST\",\"value\":\"3\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-15 13:30:49\",\"type\":\"count\",\"long\":-166.4411439778137,\"lat\":89.32421319341908},{\"tz\":\"MST\",\"value\":\"2010-09-15 01:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-15 13:30:49\",\"type\":\"event\",\"long\":-166.4411439778137,\"lat\":89.32421319341908},{\"tz\":\"MST\",\"value\":\"2010-09-14 20:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-15 13:30:49\",\"type\":\"event\",\"long\":-166.4411439778137,\"lat\":89.32421319341908},{\"tz\":\"CST\",\"value\":\"1\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-15 01:30:49\",\"type\":\"count\",\"long\":25.710673371802898,\"lat\":-52.6087305529568},{\"tz\":\"CST\",\"value\":\"2010-09-14 04:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-15 01:30:49\",\"type\":\"event\",\"long\":25.710673371802898,\"lat\":-52.6087305529568},{\"tz\":\"CST\",\"value\":\"2010-09-14 05:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-15 01:30:49\",\"type\":\"event\",\"long\":25.710673371802898,\"lat\":-52.6087305529568},{\"tz\":\"PST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-14 13:30:49\",\"type\":\"count\",\"long\":-56.699369009863446,\"lat\":11.957914207107084},{\"tz\":\"PST\",\"value\":\"2010-09-14 13:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-14 13:30:49\",\"type\":\"event\",\"long\":-56.699369009863446,\"lat\":11.957914207107084},{\"tz\":\"PST\",\"value\":\"2010-09-13 19:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-14 13:30:49\",\"type\":\"event\",\"long\":-56.699369009863446,\"lat\":11.957914207107084},{\"tz\":\"CST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-14 01:30:49\",\"type\":\"count\",\"long\":-169.26546689348174,\"lat\":-48.19827151676857},{\"tz\":\"CST\",\"value\":\"2010-09-13 23:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-14 01:30:49\",\"type\":\"event\",\"long\":-169.26546689348174,\"lat\":-48.19827151676857},{\"tz\":\"CST\",\"value\":\"2010-09-13 19:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-14 01:30:49\",\"type\":\"event\",\"long\":-169.26546689348174,\"lat\":-48.19827151676857},{\"tz\":\"CST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-13 13:30:49\",\"type\":\"count\",\"long\":-130.5832244832806,\"lat\":29.93937448212851},{\"tz\":\"CST\",\"value\":\"2010-09-12 19:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-13 13:30:49\",\"type\":\"event\",\"long\":-130.5832244832806,\"lat\":29.93937448212851},{\"tz\":\"CST\",\"value\":\"2010-09-12 22:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-13 13:30:49\",\"type\":\"event\",\"long\":-130.5832244832806,\"lat\":29.93937448212851},{\"tz\":\"MST\",\"value\":\"3\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-13 01:30:49\",\"type\":\"count\",\"long\":-73.92383434889588,\"lat\":-20.967972040328608},{\"tz\":\"MST\",\"value\":\"2010-09-12 18:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-13 01:30:49\",\"type\":\"event\",\"long\":-73.92383434889588,\"lat\":-20.967972040328608},{\"tz\":\"MST\",\"value\":\"2010-09-13 01:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-13 01:30:49\",\"type\":\"event\",\"long\":-73.92383434889588,\"lat\":-20.967972040328608},{\"tz\":\"PST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-12 13:30:49\",\"type\":\"count\",\"long\":168.04909565236156,\"lat\":66.60350795102018},{\"tz\":\"PST\",\"value\":\"2010-09-12 02:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-12 13:30:49\",\"type\":\"event\",\"long\":168.04909565236156,\"lat\":66.60350795102018},{\"tz\":\"PST\",\"value\":\"2010-09-11 22:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-12 13:30:49\",\"type\":\"event\",\"long\":168.04909565236156,\"lat\":66.60350795102018},{\"tz\":\"EST\",\"value\":\"1\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-12 01:30:49\",\"type\":\"count\",\"long\":-4.327358389379802,\"lat\":11.707174525632974},{\"tz\":\"EST\",\"value\":\"2010-09-11 13:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-12 01:30:49\",\"type\":\"event\",\"long\":-4.327358389379802,\"lat\":11.707174525632974},{\"tz\":\"EST\",\"value\":\"2010-09-11 12:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-12 01:30:49\",\"type\":\"event\",\"long\":-4.327358389379802,\"lat\":11.707174525632974},{\"tz\":\"CST\",\"value\":\"0\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-11 13:30:49\",\"type\":\"count\",\"long\":-44.083193243378076,\"lat\":18.27958877702021},{\"tz\":\"CST\",\"value\":\"2010-09-11 03:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-11 13:30:49\",\"type\":\"event\",\"long\":-44.083193243378076,\"lat\":18.27958877702021},{\"tz\":\"CST\",\"value\":\"2010-09-10 16:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-11 13:30:49\",\"type\":\"event\",\"long\":-44.083193243378076,\"lat\":18.27958877702021},{\"tz\":\"CST\",\"value\":\"2\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-11 01:30:49\",\"type\":\"count\",\"long\":172.60194086614004,\"lat\":86.60530885230233},{\"tz\":\"CST\",\"value\":\"2010-09-10 19:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-11 01:30:49\",\"type\":\"event\",\"long\":172.60194086614004,\"lat\":86.60530885230233},{\"tz\":\"CST\",\"value\":\"2010-09-10 14:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-11 01:30:49\",\"type\":\"event\",\"long\":172.60194086614004,\"lat\":86.60530885230233},{\"tz\":\"EST\",\"value\":\"2\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-10 13:30:49\",\"type\":\"count\",\"long\":-139.37565289713106,\"lat\":-53.92353247323055},{\"tz\":\"EST\",\"value\":\"2010-09-09 19:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-10 13:30:49\",\"type\":\"event\",\"long\":-139.37565289713106,\"lat\":-53.92353247323055},{\"tz\":\"EST\",\"value\":\"2010-09-09 16:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-10 13:30:49\",\"type\":\"event\",\"long\":-139.37565289713106,\"lat\":-53.92353247323055},{\"tz\":\"EST\",\"value\":\"4\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-10 01:30:49\",\"type\":\"count\",\"long\":143.51016402588306,\"lat\":-71.14905646569675},{\"tz\":\"EST\",\"value\":\"2010-09-09 11:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-10 01:30:49\",\"type\":\"event\",\"long\":143.51016402588306,\"lat\":-71.14905646569675},{\"tz\":\"EST\",\"value\":\"2010-09-09 15:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-10 01:30:49\",\"type\":\"event\",\"long\":143.51016402588306,\"lat\":-71.14905646569675},{\"tz\":\"PST\",\"value\":\"1\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-09 13:30:49\",\"type\":\"count\",\"long\":119.97237169054317,\"lat\":-13.620240549844146},{\"tz\":\"PST\",\"value\":\"2010-09-08 18:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-09 13:30:49\",\"type\":\"event\",\"long\":119.97237169054317,\"lat\":-13.620240549844146},{\"tz\":\"PST\",\"value\":\"2010-09-08 16:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-09 13:30:49\",\"type\":\"event\",\"long\":119.97237169054317,\"lat\":-13.620240549844146},{\"tz\":\"EST\",\"value\":\"2\",\"label\":\"alcoholNumberOfDrinks\",\"datetime\":\"2010-09-09 01:30:49\",\"type\":\"count\",\"long\":31.883132359241056,\"lat\":-0.005754320113918299},{\"tz\":\"EST\",\"value\":\"2010-09-08 21:30:49\",\"label\":\"alcoholHowManyHoursFirst\",\"datetime\":\"2010-09-09 01:30:49\",\"type\":\"event\",\"long\":31.883132359241056,\"lat\":-0.005754320113918299},{\"tz\":\"EST\",\"value\":\"2010-09-08 14:30:49\",\"label\":\"alcoholHowManyHoursLast\",\"datetime\":\"2010-09-09 01:30:49\",\"type\":\"event\",\"long\":31.883132359241056,\"lat\":-0.005754320113918299}]}";
  }

  @Override
  public void fetchPrivateSurveyResponses(final String campaignId,
      final AsyncCallback<List<SurveyResponse>> callback) {
    // TODO: get real data:
    // fetch private data points for all surveys in specified campaign
    //   on success, fetch a single campaigninfo (hopefully already cached)
    //     on success, use the data points + survey infos from the campaign info
    //       to build list of surveyresponse objects and pass them to original callback
    
    List<SurveyResponse> responses = new ArrayList<SurveyResponse>();
    // get info about campaign (assumes its already loaded!)
    CampaignDetailedInfo campaignInfo = getCampaignDetail(campaignId); 
    // get datapoints from server (this should be in callback)
    DataPointQueryAwData serverResponse = DataPointQueryAwData.fromJsonString(getDataPointArrayJson());
    JsArray<DataPointAwData> dataPoints = serverResponse.getData();
    // get list of survey ids from this campaign that had private responses    
    Set<String> surveyIds = new HashSet<String>();
    for (int i = 0; i < dataPoints.length(); i++) {
      surveyIds.add(dataPoints.get(i).getSurveyId());
    }
    // create a SurveyResponse object for each survey id
    Iterator<String> it = surveyIds.iterator();
    while (it.hasNext()) {
      String surveyId = it.next();
      SurveyResponse surveyResponse = new SurveyResponse();
      surveyResponse.setSurveyId(surveyId);
      surveyResponse.setCampaignId(campaignId);
      surveyResponse.setCampaignName(campaignInfo.getCampaignName());
      surveyResponse.setResponseDate("1/31/1981"); // FIXME
      surveyResponse.setResponseStatus(ResponseStatus.PRIVATE);
      // create PromptResponse object for all data points that match
      // this survey id and add them to the prompt response list
      // FIXME: O(n^2) loop
      for (int i = 0; i < dataPoints.length(); i++) {
        if (dataPoints.get(i).getSurveyId().equals(surveyId)) {
          SurveyInfo sinfo = campaignInfo.getSurvey(surveyId);
          if (sinfo != null) {
            String promptId = dataPoints.get(i).getPromptId();
            PromptInfo pinfo = sinfo.getPrompt(promptId);
            if (pinfo != null) {
              surveyResponse.addPromptResponse(pinfo, dataPoints.get(i));
            }
          }
        }
      }
      // save 
      responses.add(surveyResponse);
    }
    // invoke callback with saved objects
    callback.onSuccess(responses);
  }

  @Override
  public void fetchPrivateDataPoints(String campaignId, 
                                     AsyncCallback<List<DataPointAwData>> callback) {
    
  }
  

}
