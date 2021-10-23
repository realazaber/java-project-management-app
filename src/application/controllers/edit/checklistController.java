package application.controllers.edit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import application.Model;
import application.controllers.dashboardController;
import application.domains.ActionItem;
import application.domains.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.BooleanStringConverter;

public class checklistController {

	private Stage stage;
	
    @FXML
    private Label lbl_heading;

    @FXML
    private Label lbl_notification;

    @FXML
    private TableView<ActionItem> table_actionItems;
    
    @FXML
    private TableColumn<ActionItem, Integer> col_actItemID;

    @FXML
    private TableColumn<ActionItem, String> col_actItemName;

    @FXML
    private TableColumn<ActionItem, String> col_actItemDescription;
    
    @FXML
    private TableColumn<ActionItem, Boolean> col_actItemCompleted;

    @FXML
    private Button btn_addActionItem;
    
    @FXML
    private Button btn_saveActionItem;

    @FXML
    private TextField textField_actItemName;

    @FXML
    private TextArea textArea_actItemDescription;
    
    @FXML
    private CheckBox checkBoxCompleted;

    @FXML
    private Button btn_deleteActionItem;

    @FXML
    private Button btn_deleteChecklist;

    Model model = new Model();
    
    int userID;
    
    int checkListID;
    
    ActionItem selectedActionItem = new ActionItem();
    
    ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
    
    //Load checklist details on the table.
    public void loadChecklist(int checkListID, int userID) throws SQLException {
    	
    	this.userID = userID;    	
    	this.checkListID = checkListID;
    	
    	//Load action items and display them on the table.
    	
    	//Load action items and convert them to a format that can be shown in the table.
    	actionItems = model.getProjectDAO().loadActionItems(checkListID);
    	ObservableList<ActionItem> tableActionItems = FXCollections.observableArrayList(actionItems);
    	
    	//Assign action item values to each column.
    	col_actItemID.setCellValueFactory(new PropertyValueFactory<ActionItem, Integer>("actionitemID"));
    	
    	col_actItemName.setCellValueFactory(new PropertyValueFactory<ActionItem, String>("name"));
    	
    	
    	col_actItemDescription.setCellValueFactory(new PropertyValueFactory<ActionItem, String>("description"));
    	
    	
    	col_actItemCompleted.setCellValueFactory(new PropertyValueFactory<ActionItem, Boolean>("completed"));
    	
    	//If the user clicks the mouse on a row.
    	table_actionItems.setOnMouseClicked((MouseEvent event) -> {
    		if (event.getClickCount() > 0) {    			
    			try {
    				/*
    				 * If they did not click on an empty
    				 * row then load the row details into
    				 * the input fields so the user can 
    				 * edit the action item.
    				 */
        	    	btn_addActionItem.setVisible(false);
        	    	btn_saveActionItem.setVisible(true);
        			
    				selectedActionItem = table_actionItems.getSelectionModel().getSelectedItems().get(0);
    				
    				textField_actItemName.setText(selectedActionItem.getName());
    				textArea_actItemDescription.setText(selectedActionItem.getDescription());
    				
    				if (selectedActionItem.isCompleted()) {
    					checkBoxCompleted.setSelected(true);
    				}
    				else {
    					checkBoxCompleted.setSelected(false);
    				}
				} catch (Exception e) {
					//Empty row.
					System.out.println("Empty row.");
				}				
			}
    	});
    	
    	/*
    	 * Show the save option to the user.
    	 */
    	table_actionItems.setItems(tableActionItems);    	
    	btn_addActionItem.setVisible(true);
    	btn_saveActionItem.setVisible(false);
    	
    
    }
    
    //Create action item.
    @FXML
    void createActionItem(ActionEvent event) {
    	try {    		
    		//If require text fields are filled in add the action item to the database.
    		if (textField_actItemName.getText() != "" && textArea_actItemDescription.getText() != "") {
    			//If the action item already exists do not add it to database and let the user now.
        		if (!model.getProjectDAO().addActionItem(checkListID, textField_actItemName.getText(), textArea_actItemDescription.getText(), checkBoxCompleted.isSelected())) {
    				System.out.println(textField_actItemName.getText() + " already exists!");
    				Alert alertActionItemExists = new Alert(AlertType.ERROR);
    				alertActionItemExists.setTitle(textField_actItemName.getText() + " already exists!");
    				alertActionItemExists.setHeaderText(textField_actItemName.getText() + " already exists!");
    				alertActionItemExists.showAndWait();
    			}
        		//If action item doesn't exist then add it to the database.
        		else {
        			System.out.println(textField_actItemName.getText() + " added to checklist.");
        			textField_actItemName.clear();
        			textArea_actItemDescription.clear();
        			checkBoxCompleted.setSelected(false);
        		}
        		        		
        		refreshTable();
			}
    		//If required fields are not filled in, prompt the user to fill them in.
    		else {
				Alert alertFillInAllFields= new Alert(AlertType.ERROR);
				alertFillInAllFields.setTitle("Fill in all fields");
				alertFillInAllFields.setHeaderText("Please fill in all fields");
				alertFillInAllFields.showAndWait();
    		}
    		
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
    	
    }
    
    @FXML    
    void saveActionItem(ActionEvent event) throws SQLException {
    	System.out.println("Saving changes");

    	//Apply changes to database.
    	model.getProjectDAO().changeActionItem(selectedActionItem.getActionitemID(), textField_actItemName.getText(), textArea_actItemDescription.getText(), checkBoxCompleted.isSelected());
		
    	//Clear input fields.
    	textField_actItemName.clear();
		textArea_actItemDescription.clear();
		checkBoxCompleted.setSelected(false);
		
		//Reset the buttons.
    	btn_addActionItem.setVisible(true);
    	btn_saveActionItem.setVisible(false);
    	
    	//Refresh the table.
    	refreshTable();
    }
    
    //Refreshs the table.
    public void refreshTable() throws SQLException {
		actionItems = model.getProjectDAO().loadActionItems(checkListID);
		ObservableList<ActionItem> tableActionItems = FXCollections.observableArrayList(actionItems);
		table_actionItems.setItems(tableActionItems);
    }
    
    //Delete action item.
    @FXML
    void deleteActionItem(ActionEvent event) throws SQLException {
    	//Delete the action item.
    	model.getProjectDAO().deleteActionItem(selectedActionItem.getActionitemID());
    	
    	//Clear input fields.
		textField_actItemName.clear();
		textArea_actItemDescription.clear();
		checkBoxCompleted.setSelected(false);
		
		//Reset the buttons.
    	btn_addActionItem.setVisible(true);
    	btn_saveActionItem.setVisible(false);
    	
    	refreshTable();
    	
    }

    //Delete the checklist and all items in the checklist.
    @FXML
    void deleteChecklist(ActionEvent event) throws Exception {
		Alert alertDeleteChecklist = new Alert(AlertType.CONFIRMATION);
		alertDeleteChecklist.setTitle("Delete this checklist?");
		alertDeleteChecklist.setHeaderText("Are you sure you want to delete this checklist?");
		Optional<ButtonType> choice = alertDeleteChecklist.showAndWait();
		
		//If user chooses to delete checklist, delete it and go back to dashboard.
		if (choice.isPresent() && choice.get() == ButtonType.OK) {
			model.getProjectDAO().deleteCheckList(checkListID);
			for (ActionItem actionItem : actionItems) {
				model.getProjectDAO().deleteActionItem(actionItem.getActionitemID());
			}
			back(event);
		}
    }
    
    
	//Go back to dashboard.
	@FXML
	void back(ActionEvent event) throws Exception {
		System.out.println("Back to dashboard");
		
		//Load the dashboard and set the neccessary parameters.
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
		Parent root = dashboardScene.load();
		dashboardController dashboardController = dashboardScene.getController();
		dashboardController.setQuote();
		dashboardController.setUserID(userID);
		User user = model.getUserDAO().getUser(userID);		
		dashboardController.setWelcomeMessage(user.getFirstName());
		dashboardController.showProjects(userID);
		dashboardController.tabpane_mainTab.getSelectionModel().select(1);
		dashboardController.loadUser(user);
		
		//Go to dashboard.
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}