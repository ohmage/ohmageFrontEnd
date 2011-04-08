package edu.ucla.cens.mobilize.client.view;

import java.util.List;
import com.google.gwt.user.client.ui.IsWidget;
import edu.ucla.cens.mobilize.client.model.CampaignConciseInfo;
import edu.ucla.cens.mobilize.client.model.CampaignDetailedInfo;

/**
 * Everything in the campaigns tab is part of the CampaignView.
 * (There are several subviews.)
 * 
 * @author vhajdik
 *
 */
public interface CampaignView extends IsWidget {
  
  // presenter management
  public interface Presenter {
    public void setView(CampaignView view);
    public void onCampaignSelected(String campaignId); // FIXME: CampaignId
    public void onCampaignCreate();
    public void onCampaignDelete(String campaignId); // FIXME: CampaignId
    public void onFilterChange(); // data filters
  }
  void setPresenter(Presenter presenter);
 
  // get gui elements that can be bound to events
  
  // set flags to control display for different roles
  void setCanCreate(boolean canEdit);
  
  // hide/show subviews
  void showList();
  void showDetail();
  void showCreateForm();
  void showEditForm();
  
  // show messages to user
  void showError(String msg);
  void showMsg(String msg);
  void hideMsg();

  // set data for display
  void setCampaignList(List<CampaignConciseInfo> campaigns); 
  void setCampaignDetail(CampaignDetailedInfo campaign, boolean canEdit);
  void setCampaignEdit(CampaignDetailedInfo campaign);
  void setParticipantsToChooseFrom(List<String> participants);
  void setPlotSideBarTitle(String title);
  
  // R plots
  void clearPlots();
  void addPlot(String imgUrl);
  void showPlots();
 
}
