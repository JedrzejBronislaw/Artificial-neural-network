package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoaderRoot = new FXMLLoader();
			Parent root = fxmlLoaderRoot.load(getClass().getResource("/MainWindow.fxml").openStream());
			
			FXMLLoader fxmlLoaderNP = new FXMLLoader();
//			AnchorPane networkParameters = FXMLLoader.load(getClass().getResource("/NetworkParameters.fxml"));
			AnchorPane networkParameters = fxmlLoaderNP.load(getClass().getResource("/NetworkParameters.fxml").openStream());

			((AnchorPane)root).getChildren().add(networkParameters);
			((FXMLDocumentController)fxmlLoaderRoot.getController()).setNetworkParametersController(fxmlLoaderNP.getController());
			
			Scene scene = new Scene(root,1000,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Artificial neural network");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}



	}

	public static void main(String[] args) {
		launch(args);
	}

}