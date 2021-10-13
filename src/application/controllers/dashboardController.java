package application.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import application.Model;
import application.objects.Column;
import application.objects.Project;
import application.objects.Task;
import application.objects.User;
import application.dao.projectDAO;
import application.dao.userDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class dashboardController {
	
	String[] quotes = {"You are epic smart", "I owe you kfc", "You are a chad", "You are epic cool"};

	private Stage stage;
	
	@FXML
	private TabPane tabpane_mainTab;
	
	@FXML
	private TabPane tab_projects;
	
	@FXML
	private Label lbl_fname;
	
	@FXML
	private Label lbl_inspirationalQuote;

	@FXML
	private Button btn_createProject;
		
    @FXML
    private TextField txtFieldFName;
    
    @FXML
    private TextField txtFieldLName;
    
    @FXML
    private TextField txtFieldusername;
    
    @FXML
    private TextField txtFieldPassword;
    
    @FXML
    private ImageView profilePic;
    
    @FXML
    private File newProfile;
    
    private InputStream newProfileStream;
	
	int userId;
	String username;
	String firstName;
	
	public User currentUser = new User();
	
	private Model model = new Model();
	
    //Open file explorer and let user choose profile image.
    public void chooseProfile(ActionEvent event) throws Exception {  	
    	
    	System.out.println("Uploading image.");
    	FileChooser fileChooser = new FileChooser();
    	
    	//Add filters so only images can be added.
        FileChooser.ExtensionFilter JPG_Filter = new FileChooser.ExtensionFilter("JPG images (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter jpg_Filter = new FileChooser.ExtensionFilter("jpg images (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter jpeg_Filter = new FileChooser.ExtensionFilter("jpeg images (*.jpeg)", "*.jpeg");
        FileChooser.ExtensionFilter PNG_Filter = new FileChooser.ExtensionFilter("PNG images (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter png_Filter = new FileChooser.ExtensionFilter("png images (*.png)", "*.png");
        
    	
    	fileChooser.getExtensionFilters().addAll(JPG_Filter, jpg_Filter, jpeg_Filter, PNG_Filter, png_Filter);
    	fileChooser.setTitle("Select image");
    	newProfile = fileChooser.showOpenDialog(stage);
    	System.out.println("File chosen: " + newProfile);

    	try {
    		InputStream fileInputStream = new FileInputStream(newProfile);
    		Image selectedImage = new Image(fileInputStream);
    		System.out.println("Chosen image " + selectedImage);
    		
    		profilePic.setImage(selectedImage);
    		System.out.println("Set " + newProfile + " as preview.");
		} catch (Exception e) {
			System.out.println("Error uploading custom profile: " + e);
			System.out.println("Image url: " + newProfile);
		}

    }
	
	//Load user details in profile tab.
	public void loadUser(User currentUser) {
		this.currentUser = currentUser;
		
		
		txtFieldFName.setText(currentUser.getFirstName());
		txtFieldLName.setText(currentUser.getLastName());
		txtFieldusername.setText(currentUser.getUsername());
		txtFieldPassword.setText(currentUser.getPassword());
		newProfileStream = currentUser.getProfilePicture();
	
		Image profile = new Image(newProfileStream);
		profilePic.setImage(profile);
		
		
	}
	
    //Get the input fields and update the user details with them.
	public void saveProfileChanges(ActionEvent event) throws Exception {  	
		System.out.println("Saving changes to " + currentUser.getUserID() + " " + currentUser.getFirstName());
		
		if (newProfile == null) {
			System.out.println("Saving changes with no new profile picture.");
			model.getUserDAO().saveProfileChanges(currentUser.getUserID(), txtFieldFName.getText(), txtFieldLName.getText(), txtFieldPassword.getText(), currentUser.getProfilePicture());
		}
		else {
			System.out.println("Saving changes and uploading new profile picture.");
			BufferedInputStream newProfileStream = new BufferedInputStream(new FileInputStream(newProfile));
			model.getUserDAO().saveProfileChanges(currentUser.getUserID(), txtFieldFName.getText(), txtFieldLName.getText(), txtFieldPassword.getText(), newProfileStream);
		}
		
		//Prepare to load dashboard.
		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
    	Parent root = dashboardScene.load();
    	dashboardController dashboardController = dashboardScene.getController();
    	     
    			        
    	User tmpUser = model.getUserDAO().getUser(currentUser.getUserID());
    	
    	//Apply parameters to dashboard controller so appropriate name and projects are shown.
    	dashboardController.setUserID(tmpUser.getUserID());
    	dashboardController.setWelcomeMessage(tmpUser.getFirstName());
    	dashboardController.setQuote();
    	dashboardController.showProjects(currentUser.getUserID());
    	dashboardController.loadUser(tmpUser);
    	dashboardController.tabpane_mainTab.getSelectionModel().select(2);
		
    	//Load the dashboard.
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		
	}
	
	
	
	//Show all of the user's projects.
	public void showProjects(int userID) throws Exception {
		//Load all projects into userProjects ArrayList.
		ArrayList<Project> userProjects = model.getProjectDAO().loadProjects(userID);
						
		for (Project project : userProjects) {
							
			//Create a tab for each project.
			Tab tab_project = new Tab(project.getProjectName());
			ScrollPane scrollPane = new ScrollPane();
						
			Pane pane_tabContent = new Pane();
			
			
			Label lbl_notification = new Label();
			lbl_notification.setLayoutX(200);
			lbl_notification.setLayoutY(100);

			Button btn_editProject = new Button("Edit Project");
			btn_editProject.setLayoutX(10);
			btn_editProject.setLayoutY(10);
			
			Button btn_newColumn = new Button("New Column");
			btn_newColumn.setLayoutX(110);
			btn_newColumn.setLayoutY(10);

			Button btn_deleteProject = new Button("Delete Project");
			btn_deleteProject.setLayoutX(220);
			btn_deleteProject.setLayoutY(10);
			
			
			//Add behaviour to edit project button (go to edit project page).
			btn_editProject.setOnAction(new EventHandler<ActionEvent>() {
			
				@Override
				public void handle(ActionEvent arg0) {
					System.out.println("Edit project " + project.getProjectName());
					
					//Prepare new project scene.
					FXMLLoader editProjectScene = new FXMLLoader(getClass().getResource("/application/views/EditProject.fxml"));
					
					Parent root;
					try {
						root = editProjectScene.load();
						//Apply parameters to the newcolumn scene.
						
						editProjectController editProjectController = editProjectScene.getController();
						editProjectController.loadProject(project);
						
						//Load the new project window.
						stage = (Stage)((Node)arg0.getSource()).getScene().getWindow();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

				}
			
			});
			
			btn_newColumn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					//Print log of opening add project window.
					System.out.println("Opening add task window.");
					System.out.println("User ID: " + userId);
					System.out.println("Project ID: " + project.getProjectID());
					
					
					try {
						

						//Prepare new project scene.
						FXMLLoader newColumnScene = new FXMLLoader(getClass().getResource("/application/views/NewColumn.fxml"));
						
						Parent root = newColumnScene.load();
						
						//Apply parameters to the newcolumn scene.
						newColumnController newColumnController = newColumnScene.getController();
						newColumnController.setHeading(project.getProjectName());
						newColumnController.setProjectID(project.getProjectID());
						newColumnController.setUserID(userID);
						//Load the new project window.
						stage = (Stage)((Node)arg0.getSource()).getScene().getWindow();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					


				}
				
			});
			
			
			//Add behaviour to delete project button. 
			btn_deleteProject.setOnAction(new EventHandler<ActionEvent>() {
				
				//Delete project.
				@Override
				public void handle(ActionEvent arg0) {
					
					//Deletes the project and notifies the user.
					try {
						model.getProjectDAO().deleteProject(project.getProjectID());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
					lbl_notification.setText(project.getProjectName() + " has been deleted!");
						        	
		            //Refreshes the page on the same tab.
		        	try {
		        		//Prepare to load dashboard.
		        		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
			        	Parent root = dashboardScene.load();
			        	dashboardController dashboardController = dashboardScene.getController();
			        	
			        	//Prepare user details.
			        	dashboardController.setUserID(userID);			     
			        				        
			        	User tmpUser = model.getUserDAO().getUser(userID);
			        	
			        	//Apply parameters to dashboard controller so appropriate name and projects are shown.
			        	dashboardController.setWelcomeMessage(tmpUser.getFirstName());
			        	dashboardController.setQuote();
			        	dashboardController.showProjects(userID);
			        	dashboardController.loadUser(tmpUser);
			        	dashboardController.tabpane_mainTab.getSelectionModel().select(1);
						
			        	//Load the dashboard.
			        	stage = (Stage)((Node)arg0.getSource()).getScene().getWindow();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
						
			        	
					} catch (Exception e) {
						System.out.println("Error: " + e);
					}
				}
			});
			

			
			HBox hboxProjects = new HBox(50);
			
			ArrayList<Column> columns = model.getProjectDAO().loadColumns(project.getProjectID());
			ArrayList<VBox> vboxColumns = new ArrayList<VBox>();
			
			
			for (Column column : columns) {
				VBox vboxColumn = new VBox(10);
				vboxColumn.setMaxWidth(250);
				Pane columnDetailsPane = new Pane();

				
				
				Label lbl_columnTitle = new Label("Name: " + column.getColumn_name());
				Label lbl_description = new Label("Description: \n" + column.getDescription());
				lbl_description.setWrapText(true);
				Label lbl_date = new Label("Due date: " + column.getDue_date().toString());
				
				Button btn_editColumn = new Button("Edit column");
				Button btn_deleteColumn = new Button("Delete column");
				columnDetailsPane.getChildren().addAll(lbl_columnTitle, lbl_description, lbl_date, btn_editColumn, btn_deleteColumn);
				
				
				
				btn_editColumn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0)  {
						System.out.println("Edit column " + column.getColumn_name());
						
						//Prepare new project scene.
						FXMLLoader editColumnScene = new FXMLLoader(getClass().getResource("/application/views/EditColumn.fxml"));
						
						Parent root;
						try {
							root = editColumnScene.load();
							//Apply parameters to the newcolumn scene.
							editColumnController editColumnController = editColumnScene.getController();
							editColumnController.loadColumn(column);
							editColumnController.setUserID(userID);
							
							//Load the new project window.
							stage = (Stage)((Node)arg0.getSource()).getScene().getWindow();
							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

						
						
					}
				});
				
				btn_deleteColumn.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						System.out.println("Delete column " + column.getColumn_name());
						try {
							
							int selectedProjectTab = tab_projects.getSelectionModel().getSelectedIndex();
							model.getProjectDAO().deleteColumn(column.getColumnID());
							System.out.println("Column " + column.getColumnID() + " deleted.");
							
			        		//Prepare to load dashboard.
			        		FXMLLoader dashboardScene = new FXMLLoader(getClass().getResource("/application/views/Dashboard.fxml"));
				        	Parent root = dashboardScene.load();
				        	dashboardController dashboardController = dashboardScene.getController();
				        	
				        	//Prepare user details.
				        	dashboardController.setUserID(userID);			     
				        				        
				        	User tmpUser = model.getUserDAO().getUser(userID);
				        	
				        	//Apply parameters to dashboard controller so appropriate name and projects are shown.
				        	dashboardController.setWelcomeMessage(tmpUser.getFirstName());
				        	dashboardController.setQuote();
				        	dashboardController.showProjects(userID);
				        	dashboardController.loadUser(tmpUser);
				        	dashboardController.tabpane_mainTab.getSelectionModel().select(1);
				        	dashboardController.tab_projects.getSelectionModel().select(selectedProjectTab);				    				    
							
				        	//Load the dashboard.
				        	stage = (Stage)((Node)arg0.getSource()).getScene().getWindow();
							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.exit(0);
						}
					}
				
				});
				
				
				HBox hbox_columnBtns = new HBox(5);
				hbox_columnBtns.getChildren().addAll(btn_editColumn, btn_deleteColumn);
				
				
				
				
				
				
				Label lbl_tasksHeading = new Label("Tasks");
				lbl_tasksHeading.setContentDisplay(ContentDisplay.CENTER);
				
				
				ArrayList<Pane> taskPanes = new ArrayList<Pane>();
				
				
				VBox vboxTasks = new VBox(3);
				ArrayList<Task> tasks = model.getProjectDAO().loadTasks(column.getColumnID());
				for (Task task : tasks) {
					VBox taskVbox = new VBox(3);
					Pane taskPane = new Pane();
					Label taskName = new Label(task.getTaskName());
					Label taskDescription = new Label(task.getDescription());
					Label taskDueDate = new Label(task.getDueDate().toString());
					Label taskCompleted = new Label();
					if (task.isCompleted()) {
						taskCompleted.setText("Completed");
					}
					else {
						taskCompleted.setText("Not finished yet");
					}
					
					HBox hboxTaskButtons = new HBox(3);

					Button taskEdit = new Button("Edit");
					Button taskDelete = new Button("Delete");
					hboxTaskButtons.getChildren().addAll(taskEdit, taskDelete);
					
					taskVbox.getChildren().addAll(taskName, taskDescription, taskDueDate, taskCompleted, hboxTaskButtons);
					
					
					taskPane.getChildren().addAll(taskVbox);
					taskPane.setStyle("-fx-border-color: black;");
					
					taskPanes.add(taskPane);
					vboxTasks.getChildren().addAll(taskPanes);
					
				}
				
				Pane pane_columnDetails = new Pane();
				pane_columnDetails.setStyle("-fx-border-color: grey; -fx-padding: 10px;");
				
				VBox vbox_columnDetails = new VBox(3);
				vbox_columnDetails.getChildren().addAll(lbl_columnTitle, lbl_description, lbl_date, hbox_columnBtns);
				pane_columnDetails.getChildren().addAll(vbox_columnDetails);
				
				Button btn_taskAdd = new Button("Create task");
				
				
				vboxColumn.getChildren().addAll(pane_columnDetails, lbl_tasksHeading, btn_taskAdd, vboxTasks);
				vboxColumns.add(vboxColumn);
			}
			
			hboxProjects.getChildren().addAll(vboxColumns);
			hboxProjects.setLayoutY(60);
			
			pane_tabContent.getChildren().addAll(lbl_notification, btn_newColumn, btn_editProject, btn_deleteProject, hboxProjects);
			
			scrollPane.setContent(pane_tabContent);
			
			
			tab_project.setContent(scrollPane);
			tab_projects.getTabs().add(tab_project);
			
			if (project.isDefault()) {
				tab_projects.getSelectionModel().select(tab_project);	
			}
			
		}
	}
	
	//Creates an inspirational quote.
	public void setQuote() {
		int randomInt = new Random().nextInt(quotes.length);
		lbl_inspirationalQuote.setText(quotes[randomInt]);
	}
	
	public void setUserID(int userID) {
		this.userId = userID;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
		
	public void setWelcomeMessage(String firstName) {
		lbl_fname.setText(firstName);
	}
	
	
	//Open the window for adding the new project.
	public void addProjectWindow(ActionEvent event) throws IOException {
		//Print log of opening add project window.
		System.out.println("Opening add project window.");
		System.out.println("User ID: " + userId);
		
		//Prepare new project scene.
		FXMLLoader newProjectScene = new FXMLLoader(getClass().getResource("/application/views/NewProject.fxml"));
		Parent root = newProjectScene.load();
		
		//Apply parameters to the newproject scene.
		newProjectController newProjectController = newProjectScene.getController();
		newProjectController.setUserID(userId);
		
		//Load the new project window.
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
	

	
	public void saveProjectChanges(ActionEvent event, int projectID, String projectName) {
		
	}
	
	
	public void logout(ActionEvent event) throws IOException {
		//Print log of logging out.
		System.out.println("logging out.");
		
		//Prepare home scene.
		FXMLLoader homeScene = new FXMLLoader(getClass().getResource("/application/views/Home.fxml"));
		Parent root = homeScene.load();
		
		//Load home scene.
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
		
	
	public void addTask(ActionEvent event, int taskID, String description, boolean completed) {
		
	}
	
	public void saveTaskChanges(ActionEvent event, String description, boolean completed) {
		
	}
	
	public void deleteTask(ActionEvent event, int taskItemID) {
		
	}
	
}