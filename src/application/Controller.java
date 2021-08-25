package application;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
	
	private Stage stage;
	private Scene scene;
	private Parent parent;
	
	public void MoveToRegister(ActionEvent event) {
		System.out.println("Going to Register page.");
	}
	
	public void MoveToLogin(ActionEvent event) {
		System.out.println("Going to Login page.");
	}
	
	public void Quit(ActionEvent event) {
		System.out.println("Quiting Application.");
		System.exit(0);
	}
}
