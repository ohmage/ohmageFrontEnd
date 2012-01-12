package edu.ucla.cens.mobilize.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucla.cens.mobilize.client.common.HistoryTokens;
import edu.ucla.cens.mobilize.client.common.RoleClass;
import edu.ucla.cens.mobilize.client.dataaccess.DataService;
import edu.ucla.cens.mobilize.client.dataaccess.requestparams.ClassUpdateParams;
import edu.ucla.cens.mobilize.client.dataaccess.requestparams.UserSearchParams;
import edu.ucla.cens.mobilize.client.model.UserInfo;
import edu.ucla.cens.mobilize.client.model.UserSearchInfo;
import edu.ucla.cens.mobilize.client.ui.ErrorDialog;
import edu.ucla.cens.mobilize.client.utils.AwErrorUtils;
import edu.ucla.cens.mobilize.client.utils.StopWatch;
import edu.ucla.cens.mobilize.client.view.AdminUserListView;

public class AdminUserListPresenter implements Presenter {
  AdminUserListView view;
  UserInfo userInfo;
  EventBus eventBus;
  DataService dataService;

  private List<String> errors = new ArrayList<String>();
  
  private Logger _logger = Logger.getLogger(AdminUserListPresenter.class.getName());
  
  public AdminUserListPresenter(UserInfo userInfo, DataService dataService, EventBus eventBus) {
    this.userInfo = userInfo;
    this.dataService = dataService;
    this.eventBus = eventBus;
  }

  public void setView(AdminUserListView view) {
    this.view = view;
    addEventHandlersToView();
  }
  
  @Override
  public void go(Map<String, String> params) {
    view.clearSearchBoxes();
    view.showUserList();
    // special case: username=* means show all users
    if (params.containsKey("username") && params.get("username").equals("*")) {
      fetchAndShowAllUsers();
      return;
    }
    // special case: if no search terms are given, show instructions
    if (params.isEmpty()) {
      view.showInstructions();
      return;
    }
    String username = null;
    if (params.containsKey("username")) {
      username = params.get("username");
      view.setUsernameSearchString(username);
    }
    String personalId = null;
    if (params.containsKey("pid")) {
      personalId = params.get("pid");
      view.setPersonalIdSearchString(personalId);
    }
    Boolean isEnabled = null;
    if (params.containsKey("enabled")) {
      isEnabled = (params.get("enabled").equals("true")) ? true : false;
      view.setAdvancedSearchEnabled(isEnabled);
    }
    Boolean isAdmin = null;
    if (params.containsKey("admin")) {
      isAdmin = (params.get("admin").equals("true")) ? true : false;
      view.setAdvancedSearchIsAdmin(isAdmin);
    }
    Boolean canCreateCampaigns = null;
    if (params.containsKey("can_create")) {
      canCreateCampaigns = (params.get("can_create").equals("true")) ? true : false;
      view.setAdvancedSearchCanCreateCampaigns(canCreateCampaigns);
    }
    String firstNameSearchString = params.containsKey("first_name") ? params.get("first_name") : null;
    String lastNameSearchString = params.containsKey("last_name") ? params.get("last_name") : null;
    String emailSearchString = params.containsKey("email") ? params.get("email") : null;
    String organizationSearchString = params.containsKey("org") ? params.get("org") : null;
    String jsonSearchString = params.containsKey("json") ? params.get("json") : null;
    // set search terms in the advanced popup so user can open it to see what results are being displayed
    view.setAdvancedSearchFirstNameSearchString(firstNameSearchString);
    view.setAdvancedSearchLastNameSearchString(lastNameSearchString);
    view.setAdvancedSearchEmailSearchString(emailSearchString);
    view.setAdvancedSearchOrganizationSearchString(organizationSearchString);
    view.setAdvancedSearchJsonSearchString(jsonSearchString);
    this.fetchAndShowUserList(username,
                              personalId,
                              isEnabled, 
                              canCreateCampaigns, 
                              isAdmin, 
                              firstNameSearchString,
                              lastNameSearchString,
                              emailSearchString, 
                              organizationSearchString, 
                              jsonSearchString);
  }

  private void fetchAndShowAllUsers() {
    this.fetchAndShowUserList(null, null, null, null, null, null, null, null, null, null);
  }

  private void bind() {
    // TODO: listen for user data update events
  }
  
  private void addEventHandlersToView() {
    view.getUserDeleteButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        view.showConfirmDelete(view.getSelectedUsernames(), new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            deleteUsers(view.getSelectedUsernames());
          }
        });
      }
    });
    
    view.getUserDisableButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        disableUsers(view.getSelectedUsernames());
      }
    });
    
    view.getUserEnableButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        enableUsers(view.getSelectedUsernames());
      }
    });
    
    view.getUserAddToClassButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        showAddUsersToClassDialog();
      }
    });
    
    view.getSearchUsernameButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        fireHistoryTokenToMatchSearchQuery(view.getUsernameSearchString(), 
                                           null, null, null, null, null, null, null, null, null);
      }
    });
    
    view.getSearchUsernameTextBox().addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          fireHistoryTokenToMatchSearchQuery(view.getUsernameSearchString(), 
              null, null, null, null, null, null, null, null, null);
        }
      }
    });
    
    view.getSearchPersonalIdButton().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        fireHistoryTokenToMatchSearchQuery(null, // username
                                           view.getPersonalIdSearchString(),
                                           null, null, null, null, null, null, null, null);
      }
    });
    
    view.getSearchPersonalIdTextBox().addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          fireHistoryTokenToMatchSearchQuery(null, // username
                                             view.getPersonalIdSearchString(),
                                             null, null, null, null, null, null, null, null);          
        }
      }
    });
    
    // Show search form in popup. On submit, fire history token to show search results.
    view.getAdvancedSearchLink().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        view.showAdvancedSearchPopup(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            fireHistoryTokenToMatchAdvancedSearchQuery();
          }
        });
      }
    });
    
    view.getErrorLink().addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        view.showErrorList(errors);
        view.hideErrorLink();
      }
    });
  }
  
  private void fireHistoryTokenToMatchSearchQuery(String username,
                                                  String personalId,
                                                  Boolean isEnabled,
                                                  Boolean canCreate,
                                                  Boolean isAdmin,
                                                  String firstNameSearchString,
                                                  String lastNameSearchString,
                                                  String emailSearchString,
                                                  String organizationSearchString,
                                                  String jsonSearchString) {
    History.newItem(HistoryTokens.adminUserList(username,
                                                personalId,
                                                isEnabled, 
                                                canCreate, 
                                                isAdmin, 
                                                firstNameSearchString,
                                                lastNameSearchString,
                                                emailSearchString, 
                                                organizationSearchString, 
                                                jsonSearchString));
  }
  
  private void fireHistoryTokenToMatchAdvancedSearchQuery() {
    Boolean isEnabled = view.getAdvancedSearchEnabled();
    Boolean canCreate = view.getAdvancedSearchCanCreateCampaigns();
    Boolean isAdmin = view.getAdvancedSearchIsAdmin();
    String firstNameSearchString = view.getAdvancedSearchFirstNameSearchString();
    String lastNameSearchString = view.getAdvancedSearchLastNameSearchString();
    String emailSearchString = view.getAdvancedSearchEmailSearchString();
    String organizationSearchString = view.getAdvancedSearchOrganizationSearchString();
    String jsonSearchString = view.getAdvancedSearchJsonSearchString();
    History.newItem(HistoryTokens.adminUserList(null, // username 
                                                null, // personal id
                                                isEnabled, 
                                                canCreate, 
                                                isAdmin,
                                                firstNameSearchString,
                                                lastNameSearchString,
                                                emailSearchString, 
                                                organizationSearchString, 
                                                jsonSearchString));  
  }
  
  private void fetchAndShowUserList(String username,
                                    String personalId,
                                    Boolean isEnabled,
                                    Boolean canCreateCampaigns,
                                    Boolean isAdmin,
                                    String firstNameSearchString,
                                    String lastNameSearchString,
                                    String emailSearchString,
                                    String organizationSearchString,
                                    String jsonSearchString) {
    view.showWaitIndicator();
    UserSearchParams params = new UserSearchParams();
    params.username_opt = username;
    params.personalId_opt = personalId;
    params.enabled_opt = isEnabled;
    params.canCreateCampaigns_opt = canCreateCampaigns;
    params.admin_opt = isAdmin;
    params.firstName_opt = firstNameSearchString;
    params.lastName_opt = lastNameSearchString;
    params.email_opt = emailSearchString;
    params.organization_opt = organizationSearchString;
    params.jsonData_opt = jsonSearchString;
    StopWatch.start("fetch_users");
    dataService.fetchUserSearchResults(params, new AsyncCallback<List<UserSearchInfo>>() {
      @Override
      public void onFailure(Throwable caught) {
        view.hideWaitIndicator();
        AwErrorUtils.logoutIfAuthException(caught);
        view.showError("User search failed.", caught.getMessage());
      }

      @Override
      public void onSuccess(List<UserSearchInfo> result) {
        StopWatch.stop("fetch_users");
        view.hideWaitIndicator();
        StopWatch.start("sort_users");
        Collections.sort(result);
        StopWatch.stop("sort_users");
        StopWatch.start("render_users");
        view.setUserList(result);
        StopWatch.stop("render_users");
        _logger.finest(StopWatch.getTotalsString());
        StopWatch.resetAll();
      }
    });
  }
  
  private void deleteUsers(List<String> usernames) {
    dataService.deleteUsers(usernames, new AsyncCallback<String>() {

      @Override
      public void onFailure(Throwable caught) {
        AwErrorUtils.logoutIfAuthException(caught);
        view.showError("There was a problem deleting users.", caught.getMessage());
      }

      @Override
      public void onSuccess(String result) {
        History.newItem(HistoryTokens.adminUserList());
      }
    });
  }
  
  private void disableUsers(List<String> usernames) {
    for (final String username : usernames) {
      dataService.disableUser(username, new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
          AwErrorUtils.logoutIfAuthException(caught);
          addError(caught.getMessage());
        }

        @Override
        public void onSuccess(String result) {
          view.markUserDisabled(username);
        }
      });
    }
  }
  
  private void enableUsers(List<String> usernames) {
    for (final String username : usernames) {
      dataService.enableUser(username, new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
          AwErrorUtils.logoutIfAuthException(caught);
          addError(caught.getMessage());
        }

        @Override
        public void onSuccess(String result) {
          view.markUserEnabled(username);
        }
      });
    }
  }
  
  private void showAddUsersToClassDialog() {
    final List<String> usernames = view.getSelectedUsernames();
    dataService.fetchClassNamesAndUrns(new AsyncCallback<Map<String, String>>() {
      @Override
      public void onFailure(Throwable caught) {
        AwErrorUtils.logoutIfAuthException(caught); 
        view.showError("There was a problem fetching class urns.", caught.getMessage());
      }

      @Override
      public void onSuccess(Map<String, String> result) {
        List<String> classUrns = new ArrayList<String>(result.keySet());
        view.showAddUsersToClassDialog(usernames, classUrns, new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            addSelectedUsersToClass();
          }
        });
      }
    });
  }
  
  private void addSelectedUsersToClass() {
    String classUrn = view.getClassToAddUsers();
    if (classUrn != null && !classUrn.isEmpty()) { // if class isn't selected, do nothing
      List<String> usernames = view.getSelectedUsernames(); // dialog is modal so these won't have changed
      ClassUpdateParams params = new ClassUpdateParams();
      params.classId = classUrn;
      RoleClass role = view.getClassRoleForUsers();
      for (String username : usernames) {
        params.usersToAdd_opt.put(username, role);
        // gotcha: if user was already in class, previous role will be overwritten
      }
      dataService.updateClass(params, new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) {
          AwErrorUtils.logoutIfAuthException(caught);
          view.hideAddUsersToClassDialog();
          ErrorDialog.show("There was a problem adding users to the class.", caught.getMessage());
        }
  
        @Override
        public void onSuccess(String result) {
          view.hideAddUsersToClassDialog();
        }
      });
    }
  }
  
  private void clearErrors() {
    view.hideErrorLink();
  }
  
  private void addError(String msg) {
    this.errors.add(msg);
    view.setErrorCount(this.errors.size());
    // NOTE: errors are cleared when user clicks on the error link
  }
}