package fr.TheWolf.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import fr.trxyy.alternative.alternative_api.GameConnect;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameFolder;
import fr.trxyy.alternative.alternative_api.GameForge;
import fr.trxyy.alternative.alternative_api.GameLinks;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.LauncherPreferences;
import fr.trxyy.alternative.alternative_api.maintenance.GameMaintenance;
import fr.trxyy.alternative.alternative_api.maintenance.Maintenance;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.config.LauncherConfig;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherMain extends AlternativeBase {
	
	public static LauncherMain instance;
	public static LauncherPane contentPane;
	private Scene scene;
	public static GameFolder gameFolder = new GameFolder("oraxfun");
	private LauncherPreferences launcherPreferences = new LauncherPreferences("Oraxia Launcher", 950, 600, Mover.MOVE);
	public static GameLinks gameLinks = new GameLinks("https://oraxfungames.000webhostapp.com/libs/OraxLauncherlibs/","1.12.2-forge-14.23.5.2859.json"); // 1.12.2-forge-14.23.5.2859.json
    private GameEngine gameEngine = new GameEngine(gameFolder, gameLinks, launcherPreferences, GameStyle.FORGE_1_8_TO_1_12_2); 	
    public static GameForge gameForge;
    private GameMaintenance gameMaintenance = new GameMaintenance(Maintenance.USE, gameEngine);
    private static GameConnect gameConnect = new GameConnect("oraxfun.falix.gg","27884");
    public static Media media;
	//private static MediaPlayer mediaPlayer;
    public LauncherConfig config;
    
    
    @Override
	public void start(Stage primaryStage) throws Exception {
    	instance = this;
    	
    	createContent();
    	this.gameEngine.reg(primaryStage);
		if(LauncherMain.netIsAvailable()) {
			this.gameEngine.reg(this.gameMaintenance);
		}
		LauncherBase launcherBase = new LauncherBase(primaryStage, scene, StageStyle.TRANSPARENT, this.gameEngine);
		launcherBase.setIconImage(primaryStage, "neworaxlogo.png");
	}
    
    

    private Parent createContent() throws IOException {
		LauncherMain.contentPane = new LauncherPane(this.gameEngine);
		scene = new Scene(LauncherMain.getContentPane());
		Rectangle rectangle = new Rectangle(this.gameEngine.getLauncherPreferences().getWidth(),
				this.gameEngine.getLauncherPreferences().getHeight());
		this.gameEngine.reg(LauncherMain.gameLinks);
		rectangle.setArcWidth(15.0);
		rectangle.setArcWidth(15.0);
		LauncherMain.getContentPane().setClip(rectangle);
		LauncherMain.getContentPane().setStyle("-fx-background-color: transparent;");
		//LauncherPanel panel = new LauncherPanel(LauncherMain.getContentPane(), this.gameEngine);
		//readVersion(panel);
		/*final JackInTheBox animationOUVERTURE = new JackInTheBox(LauncherMain.getContentPane());
		animationOUVERTURE.setSpeed(0.5);
		animationOUVERTURE.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent actionEvent) {
				new Tada(LauncherPanel.getTiktokButton()).play();
				new Tada(LauncherPanel.getMinestratorButton()).play();
				new Tada(LauncherPanel.getTwitterButton()).play();
				new Tada(LauncherPanel.getYoutubeButton()).play();
			}
		});
		//animationOUVERTURE.play();*/
		return LauncherMain.getContentPane();
	}
	
	public static void main(String[] args) {
		Logger.log("Launching Oraxia ! Have Fun ;)");
		Application.launch(args);
	}
	
	public static GameLinks getGameLinks() {
		return gameLinks;
	}

	public static LauncherPane getContentPane() {
		return LauncherMain.contentPane;
	}
	
	public static void setContentPane(LauncherPane contentPane) {
		LauncherMain.contentPane = contentPane;
	}

	public static LauncherMain getInstance() {
		return instance;
	}

	public static GameFolder getGameFolder() {
		return gameFolder;
	}

	public static GameConnect getGameConnect() {
		return gameConnect;
	}

	public static void setGameConnect(GameConnect gameConnect) {
		LauncherMain.gameConnect = gameConnect;
	}
	
	public static boolean netIsAvailable() {
	    try {
	        final URL url = new URL("http://www.google.com");
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        return false;
	    }
	}
    
	
}
