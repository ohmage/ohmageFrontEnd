package edu.ucla.cens.mobilize.client.view;

import java.util.List;
import java.util.Map;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;

import edu.ucla.cens.mobilize.client.common.RoleClass;
import edu.ucla.cens.mobilize.client.model.ClassInfo;
import edu.ucla.cens.mobilize.client.model.UserShortInfo;
import edu.ucla.cens.mobilize.client.ui.ClassDetail;
import edu.ucla.cens.mobilize.client.ui.ClassEditForm;
import edu.ucla.cens.mobilize.client.ui.ClassList;


public class ClassViewImpl extends Composite implements ClassView {

  private static ClassViewUiBinder uiBinder = GWT
  .create(ClassViewUiBinder.class);
  
  @UiTemplate("ClassView.ui.xml")
  interface ClassViewUiBinder extends UiBinder<Widget, ClassViewImpl> {
  }
  
  @UiField ClassList classList;
  @UiField ClassDetail classDetail;
  @UiField ClassEditForm classEdit;

  @UiField HTMLPanel msgBox;
  @UiField InlineLabel msgLabel;
  @UiField Anchor closeMsg;
  
  ClassView.Presenter presenter;
  
  public ClassViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    hideMsg();     
    setList(null);
    bind();
  }
  
  private void bind() {
  }
  
  private void hideAllWidgets() {
    classList.setVisible(false);
    classDetail.setVisible(false);
    classEdit.setVisible(false);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }


  @Override
  public void showListSubview() {
    hideAllWidgets();
    classList.setVisible(true);
  }

  @Override
  public void showDetailSubview() {
    hideAllWidgets();
    this.classDetail.setVisible(true);
  }

  @Override
  public void showEditSubview() {
    hideAllWidgets();
    this.classEdit.setVisible(true);
  }
  
  @Override
  public void setList(List<ClassInfo> classes) {
    this.classList.setClasses(classes);
  }

  @Override
  public void setDetail(ClassInfo classDetail) {
    this.classDetail.setClassDetail(classDetail);
  }

  @Override
  public void setEdit(ClassInfo classDetail) {
    this.classEdit.setClassDetail(classDetail);
  }

  @Override
  public void showError(String msg) {
    final DialogBox errorDialog = new DialogBox();
    errorDialog.setGlassEnabled(true);
    errorDialog.setText(msg);
    Button dismissButton = new Button("OK");
    dismissButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        errorDialog.hide(); 
      }
    });
    errorDialog.add(dismissButton);
    // TODO: add style
    errorDialog.center();
  }

  @Override
  public void showMsg(String msg) {
    msgLabel.setText(msg);
    msgBox.setVisible(true);
  }

  @Override
  public void hideMsg() {
    msgLabel.setText("");
    msgBox.setVisible(false);
  }

  @Override
  public HasClickHandlers getEditFormSubmitButton() {
    return this.classEdit.getSubmitButton();    
  }

  @Override
  public String getDescription() {
    return this.classEdit.getDescription();
  }

  @Override
  public List<String> getMembers() {
    return this.classEdit.getRestrictedMembers();
  }

  @Override
  public String getClassId() {
    return this.classEdit.getClassId();
  }

  @Override
  public HasClickHandlers getEditFormCancelButton() {
    return this.classEdit.getCancelButton();
  }

  @Override
  public void clearEditForm() {
    this.classEdit.clearForm();
  }

  @Override
  public void showEditFormAddMembersDialog(List<String> userLoginsToChooseFrom) {
    this.classEdit.showMemberChoices(userLoginsToChooseFrom);
  }

  @Override
  public HasClickHandlers getEditFormAddMembersButton() {
    return this.classEdit.getAddMembersButton();
  }

  @Override
  public void setDetailClassMembers(List<UserShortInfo> members, Map<String, RoleClass> usernameToRoleMap) {
    this.classDetail.setClassMemberDetails(members, usernameToRoleMap);
  }

  @Override
  public void clearClassMembers() {
    this.classDetail.clearClassMembers();
  }

}
