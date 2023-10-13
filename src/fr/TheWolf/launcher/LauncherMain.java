package fr.TheWolf.launcher;

import fr.trxyy.alternative.alternative_api.GameConnect;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameFolder;
import fr.trxyy.alternative.alternative_api.GameLinks;
import fr.trxyy.alternative.alternative_api.GameStyle;
import fr.trxyy.alternative.alternative_api.LauncherPreferences;
import fr.trxyy.alternative.alternative_api.utils.Logger;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.WindowStyle;
import fr.trxyy.alternative.alternative_api_ui.LauncherBackground;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherMain extends AlternativeBase {
	public static GameFolder gameFolder = new GameFolder("ØraxFunlauncher");
	private LauncherPreferences launcherPreferences = new LauncherPreferences("ØraxFun Launcher", 950, 600, Mover.MOVE);
	private GameLinks gameLinks = new GameLinks("https://oraxfungames.000webhostapp.com/libs/OraxLauncherlibs/","1.12.2-forge-14.23.5.2859.json"); // 1.12.2-forge-14.23.5.2859.json
    private GameEngine gameEngine = new GameEngine(gameFolder, gameLinks, launcherPreferences, GameStyle.FORGE_1_8_TO_1_12_2); 		
    private GameConnect gameConnect = new GameConnect("oraxfun.falix.gg","27884");
    
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		LauncherBase launcherBase = new LauncherBase(primaryStage, scene, StageStyle.TRANSPARENT, this.gameEngine);
		launcherBase.setIconImage(primaryStage, "neworaxlogo.png");
	}
    
    

	private Parent createContent() {
		LauncherPane contentPane = new LauncherPane(this.gameEngine, 5, WindowStyle.TRANSPARENT);
		/**Direct Connect**/
		this.gameEngine.reg(gameConnect);
		new LauncherBackground(this.gameEngine, "background.mp4", contentPane);
		new LauncherPanel(contentPane, this.gameEngine);
		return contentPane;
	} 
	
	public static void main(String[] args) {
		Logger.log("Launching ØraxFun ! Have Fun ;)");
		Application.launch(args);
	}
    
	
}
