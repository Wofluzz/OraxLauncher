package fr.TheWolf.launcher;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.jfoenix.controls.JFXProgressBar;

import animatefx.animation.ZoomOutDown;
import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameMemory;
import fr.trxyy.alternative.alternative_api.GameSize;
import fr.trxyy.alternative.alternative_api.JVMArguments;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.Mover;
import fr.trxyy.alternative.alternative_api.utils.config.EnumConfig;
import fr.trxyy.alternative.alternative_api.utils.config.LauncherConfig;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherPasswordField;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherProgressBar;
import fr.trxyy.alternative.alternative_api_ui.components.LauncherTextField;
import fr.trxyy.alternative.alternative_auth.account.AccountType;
import fr.trxyy.alternative.alternative_auth.base.GameAuth;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LauncherPanel extends IScreen {

	private final LauncherPane panel = LauncherMain.getContentPane();
	private static LauncherPanel instance;

	public LauncherConfig config;
	// Auth de Microsoft
	private GameAuth gameAuthentication;

	/** TOP */
	private LauncherButton closeButton;
	private LauncherButton reduceButton;
	/** LOGIN */
	private LauncherTextField usernameField;
	private LauncherPasswordField passwordField;
	private LauncherButton loginButton, settingsButton, microsoftButton;
	private final GameUpdater gameUpdater = new GameUpdater();
	/** GAMEENGINE REQUIRED */
	private GameEngine gameEngine;
	private LauncherProgressBar progressBar;
	private LauncherLabel updateLabel;
//	private LauncherLabel launcherversionText;
//	private LauncherLabel serverversionText;
//	private LauncherLabel mcversionText;

	private Rectangle updateRectangle;
	private GameUpdater updater;
	private Thread updateThread;
	/** LOGGED ING **/
	private Rectangle loggedRectangle;
	private LauncherImage headImage;
	private LauncherLabel accountLabel, newsTitle, newsSubtitle;
	/** SETTINGS **/
	// private LauncherButton saveButton;
	private LauncherLabel memorySliderLabel;
	private Slider memorySlider;
	private LauncherLabel windowSizeLabel;
	private ComboBox<String> windowsSizeList;
	private CheckBox autoLogin;
	public JFXProgressBar bar;
	private Font customFont = FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 18F);

	/** PLUS **/
	private Rectangle news;

	public LauncherPanel(Pane root, GameEngine engine) {
		instance = this;
		this.gameEngine = engine;
		//this.usernameSaver = new UsernameSaver(engine);
		
		this.config = new LauncherConfig(engine);
        this.config.loadConfiguration();

		this.drawRect(root, 0, 0, gameEngine.getWidth(), gameEngine.getHeight(), Color.rgb(255, 255, 255, 0.10));
		/** ===================== RECTANGLE NOIR A GAUCHE ===================== */
		this.drawRect(root, 0, 0, engine.getWidth() / 12, engine.getHeight(), Color.rgb(0, 0, 0, 0.4));
		/** News */
		news = this.drawRect(root, 250, engine.getHeight() - 500, engine.getWidth() / 2, 300, Color.rgb(0, 0, 0, 0.4));
		news.setArcHeight(15.0);
		news.setArcWidth(15.0);
		news.setVisible(true);
		this.newsTitle = new LauncherLabel(root);
		this.newsTitle.setText("News :");
		this.newsTitle.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 25F));
		this.newsTitle.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.newsTitle.setPosition(engine.getWidth() / 2 - 20, engine.getHeight() - 500);
		this.newsTitle.setOpacity(0.7);
		this.newsTitle.setSize(500, 40);
		this.newsTitle.setVisible(true);

		this.newsSubtitle = new LauncherLabel(root);
		this.newsSubtitle.setText("Nouvelle Mise a jour du Launcher !!!");
		this.newsSubtitle.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 20F));
		this.newsSubtitle.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
		this.newsSubtitle.setPosition(engine.getWidth() / 2 - 140, engine.getHeight() - 470);
		this.newsSubtitle.setOpacity(0.7);
		this.newsSubtitle.setSize(500, 40);
		this.newsSubtitle.setVisible(true);
		/** ===================== AFFICHER UN LOGO ===================== */
		this.drawImage(engine, loadImage(gameEngine, "neworaxlogo.png"),
                engine.getWidth() / 2 - 70, 40, 150, 150, root, Mover.DONT_MOVE);
		/** ===================== BOUTON FERMER ===================== */
		this.closeButton = new LauncherButton(root);
		this.closeButton.setInvisible();
		this.closeButton.setBounds(gameEngine.getWidth() - 50, -3, 40, 20);
		LauncherImage closeImage = new LauncherImage(root, loadImage(gameEngine, "close.png"));
		closeImage.setSize(40, 20);
		this.closeButton.setGraphic(closeImage);
		this.closeButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		/** ===================== BOUTON REDUIRE ===================== */
		this.reduceButton = new LauncherButton(root);
		this.reduceButton.setInvisible();
		this.reduceButton.setBounds(gameEngine.getWidth() - 91, -3, 40, 20);
		LauncherImage reduceImage = new LauncherImage(root, loadImage(gameEngine, "minimize.png"));
		reduceImage.setSize(40, 20);
		this.reduceButton.setGraphic(reduceImage);
		reduceButton.setOnAction(event -> {
            final ZoomOutDown animation2 = new ZoomOutDown(panel);
            animation2.setOnFinished(actionEvent -> {
                Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
                stage.setIconified(true);
            });
            animation2.setResetOnFinished(true);
            animation2.play();
		/** ===================== CASE PSEUDONYME ===================== */
		//this.usernameField = new LauncherTextField(usernameSaver.getUsername(), root);
		this.usernameField.setBounds(this.gameEngine.getWidth() - 360, this.gameEngine.getHeight() - 99, 220, 20);
		this.setFontSize(14.0F);
		this.usernameField.setFont(this.customFont);
		this.usernameField.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.usernameField.addStyle("-fx-text-fill: black;");
		this.usernameField.addStyle("-fx-border-radius: 0 0 0 0;");
		this.usernameField.addStyle("-fx-background-radius: 0 0 0 0;");
		this.usernameField.setVoidText("Nom de compte / Mail");
		/** ===================== CASE MOT DE PASSE ===================== */
		this.passwordField = new LauncherPasswordField(root);
		this.passwordField.setBounds(this.gameEngine.getWidth() - 360, this.gameEngine.getHeight() - 60, 220, 20);
		this.setFontSize(14.0F);
		this.passwordField.setFont(this.customFont);
		this.passwordField.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.passwordField.addStyle("-fx-text-fill: black;");
		this.passwordField.addStyle("-fx-border-radius: 0 0 0 0;");
		this.passwordField.addStyle("-fx-background-radius: 0 0 0 0;");
		this.passwordField.setVoidText("Mot de passe (vide = crack)");
		/** ===================== BOUTON DE CONNEXION ===================== */
		this.loginButton = new LauncherButton("Connexion", root);
		this.setFontSize(12.5F);
		this.loginButton.setFont(this.customFont);
		this.loginButton.setBounds(this.gameEngine.getWidth() - 130, this.gameEngine.getHeight() - 60, 90, 20);
		this.loginButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.loginButton.addStyle("-fx-text-fill: black;");
		this.loginButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.loginButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.loginButton.setOnAction(event -> {
            if (LauncherMain.netIsAvailable()) {
				/**
				 * ===================== AUTHENTIFICATION OFFLINE (CRACK) =====================
				 */
            	if (usernameField.getText().length() < 3) {
                    new LauncherAlert("Authentification echouee",
                            "Il y a un probleme lors de la tentative de connexion: Le pseudonyme doit comprendre au moins 3 caracteres.");
                } else if (usernameField.getText().length() > 3) {
                    gameAuthentication = new GameAuth(usernameField.getText(), passwordField.getText(),
                            AccountType.OFFLINE);
                    if (gameAuthentication.isLogged()) {
//                        if ((boolean) config.getValue(EnumConfig.USE_FORGE) && verif == 1) {
//                            update(gameAuthentication);
//                        } else {
                            updateGame(gameAuthentication);
//                        }
                    }
                    connectAccountCrackCO(root);
                }
            }
            });
		/** ===================== BOUTON DES OPTIONS ===================== */
		this.settingsButton = new LauncherButton("Options", root);
		this.setFontSize(12.5F);
		this.settingsButton.setFont(this.customFont);
		this.settingsButton.setBounds(this.gameEngine.getWidth() - 130, this.gameEngine.getHeight() - 99, 105, 20);
		LauncherImage settingsImage = new LauncherImage(root, loadImage(gameEngine, "close.png"));
		settingsImage.setSize(40, 20);
		this.closeButton.setGraphic(closeImage);
		this.settingsButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				final JDialog frame = new JDialog();
				frame.setTitle("Modification des parametres");
				frame.setContentPane(new SettingsPanel(engine));
				frame.setResizable(false);
				frame.setModal(true);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setSize(630, 630);
				frame.setVisible(true);
			}
		});
		/** ===================== BOUTON DE CONNEXION MICROSOFT ===================== */
		this.microsoftButton = new LauncherButton("Connexion avec Microsoft", root);
		this.setFontSize(12.5F);
		this.microsoftButton.setFont(this.customFont);
		this.microsoftButton.setBounds(this.gameEngine.getWidth() - 345, this.gameEngine.getHeight() - 33, 190, 20);
		this.microsoftButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.microsoftButton.addStyle("-fx-text-fill: black;");
		this.microsoftButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.microsoftButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.microsoftButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				gameAuth = new GameAuth();
				showMicrosoftAuth(gameEngine, gameAuth);
				if (gameAuth.isLogged()) {
					gameSession = gameAuth.getSession();
					File jsonFile = downloadVersion(engine.getGameLinks().getJsonUrl(), engine);
					updateGame(gameSession, jsonFile);
				}
			}
		});
//		/** ===================== TEXTE DE VERSIONS ===================== */ 

		// Launcher;
//		this.launcherversionText = new LauncherLabel(root);
//		this.launcherversionText.setText("2.0.1");
//		this.launcherversionText.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 15F));
//		this.launcherversionText.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
//		this.launcherversionText.setPosition(engine.getWidth() / 2 + 375, 550);
//		this.launcherversionText.setOpacity(0.7);
//		this.launcherversionText.setSize(500, 40);
//		//Server 
//		this.serverversionText = new LauncherLabel(root);
//		this.serverversionText.setText("Serveur : 1.0.0");
//		this.serverversionText.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 15F));
//		this.serverversionText.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
//		this.serverversionText.setPosition(engine.getWidth() / 2 + 375, 570);
//		this.serverversionText.setOpacity(0.7);
//		this.serverversionText.setSize(500, 40);
//		//Minecraft
//		this.mcversionText = new LauncherLabel(root);
//		this.mcversionText.setText("Minecraft : 1.12.2 - Forge");
//		this.mcversionText.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 15F));
//		this.mcversionText.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
//		this.mcversionText.setPosition(engine.getWidth() / 2 - 470, 570);
//		this.mcversionText.setOpacity(0.7);
//		this.mcversionText.setSize(500, 40);
//		//Saison
//		this.mcversionText = new LauncherLabel(root);
//		this.mcversionText.setText("Season 0 - Bêta");
//		this.mcversionText.setFont(FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", 15F));
//		this.mcversionText.setStyle("-fx-background-color: transparent; -fx-text-fill: white");
//		this.mcversionText.setPosition(engine.getWidth() / 2 - 470, 550);
//		this.mcversionText.setOpacity(0.7);
//		this.mcversionText.setSize(500, 40);

		/** ======================================================== **/
		this.updateLabel = new LauncherLabel(root);
		this.updateLabel.setText("Mise a jour...");
		this.setFontSize(30.0F);
		this.updateLabel.setFont(this.customFont);
		this.updateLabel.setBounds(this.gameEngine.getWidth() - 250, this.gameEngine.getHeight() - 70, 200, 20);
		this.updateLabel.addStyle("-fx-text-fill: white;");
		this.updateLabel.setOpacity(0.0D);
		this.updateLabel.setVisible(false);

		this.loggedRectangle = this.drawRect(root, this.gameEngine.getWidth() / 2 - 115, 50, 230, 200,
				Color.rgb(0, 0, 0, 0.4));
		this.loggedRectangle.setOpacity(0.0D);
		this.loggedRectangle.setVisible(false);

		this.headImage = new LauncherImage(root);
		this.headImage.setFitWidth(120);
		this.headImage.setFitHeight(120);
		this.headImage.setLayoutX(this.gameEngine.getWidth() / 2 - 60);
		this.headImage.setLayoutY(70);
		this.headImage.setOpacity(0.0D);
		this.headImage.setVisible(false);

		this.accountLabel = new LauncherLabel(root);
		this.accountLabel.setAlignment(Pos.CENTER);
		this.setFontSize(20.0F);
		this.accountLabel.setFont(this.customFont);
		this.accountLabel.setBounds(this.gameEngine.getWidth() / 2 - 110, this.gameEngine.getHeight() / 2 - 50, 220,
				20);
		this.accountLabel.addStyle("-fx-text-fill: white;");
		this.accountLabel.setOpacity(0.0D);
		this.accountLabel.setVisible(false);

		this.updateRectangle = this.drawRect(root, this.gameEngine.getWidth() / 2 - 250,
				this.gameEngine.getHeight() / 2 + 70, 500, 30, Color.rgb(0, 0, 0, 0.4));
		this.updateRectangle.setOpacity(0.0D);
		this.updateRectangle.setVisible(false);

		this.progressBar = new LauncherProgressBar(root);
		this.progressBar.setBounds(this.gameEngine.getWidth() / 2 - 245, this.gameEngine.getHeight() / 2 + 75, 490, 21);
		this.progressBar.setOpacity(0.0D);
		this.progressBar.setVisible(false);

	}

	private void updateGame(GameAuth auth) {
		// this.accountLabel.setText(auth.Ge());
		this.fadeOut(this.loginButton, 300).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newsTitle.setVisible(false);
				newsSubtitle.setVisible(false);
				news.setVisible(false);

				loginButton.setVisible(false);
				updateLabel.setVisible(true);
				fadeIn(updateLabel, 300);

				progressBar.setVisible(true);
				fadeIn(progressBar, 300);

				loggedRectangle.setVisible(true);
				fadeIn(loggedRectangle, 300);

				headImage.setImage(new Image("https://minotar.net/helm/" + auth.getUsername() + "/120.png"));
				headImage.setVisible(true);
				fadeIn(headImage, 300);

				updateRectangle.setVisible(true);
				fadeIn(updateRectangle, 300);

				loggedRectangle.setVisible(true);
				fadeIn(loggedRectangle, 300);

				accountLabel.setVisible(true);
				fadeIn(accountLabel, 300);
			}
		});
		this.fadeOut(this.usernameField, 300).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				usernameField.setVisible(false);
			}
		});
		this.fadeOut(this.passwordField, 300).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				passwordField.setVisible(false);
			}
		});
		this.fadeOut(this.settingsButton, 300).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				settingsButton.setVisible(false);
			}
		});
		this.fadeOut(this.microsoftButton, 300).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				microsoftButton.setVisible(false);
			}
		});

		this.gameEngine.reg(LauncherMain.gameLinks);
		this.gameEngine.getGameLinks().JSON_URL = this.gameEngine.getGameLinks().BASE_URL
				+ this.config.getValue(EnumConfig.VERSION) + ".json";
		this.gameUpdater.reg(this.gameEngine);
		this.gameUpdater.reg(auth.getSession());

		/*
		 * Change settings in GameEngine from launcher_config.json
		 */
		this.gameEngine.reg(GameMemory.getMemory(Double.parseDouble((String) this.config.getValue(EnumConfig.RAM))));
		this.gameEngine
				.reg(GameSize.getWindowSize(Integer.parseInt((String) this.config.getValue(EnumConfig.GAME_SIZE))));
		boolean useVmArgs = (Boolean) config.getValue(EnumConfig.USE_VM_ARGUMENTS);
		String vmArgs = (String) config.getValue(EnumConfig.VM_ARGUMENTS);
		String[] s = null;
		if (useVmArgs) {
			if (vmArgs.length() > 3) {
				s = vmArgs.split(" ");
			}
			assert s != null;
			JVMArguments arguments = new JVMArguments(s);
			this.gameEngine.reg(arguments);
		}
		this.gameEngine.reg(this.gameUpdater);

		Thread updateThread = new Thread(() -> this.gameEngine.getGameUpdater().start());
		updateThread.start();

		/*
		 * ===================== REFAICHIR LE NOM DU FICHIER, PROGRESSBAR, POURCENTAGE
		 * =====================
		 **/
		Timeline timeline = new Timeline(
				new KeyFrame(javafx.util.Duration.seconds(0.0D), event -> timelineUpdate2(this.gameEngine)),
				new KeyFrame(javafx.util.Duration.seconds(0.1D)));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	public void timelineUpdate2(GameEngine engine) {
		if (engine.getGameUpdater().downloadedFiles > 0) {
			this.percentageLabel.setText(decimalFormat.format(
					engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload) + "%");
		}
		this.currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
		this.currentStep.setText(engine.getGameUpdater().getCurrentInfo());
		double percent = (engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload
				/ 100.0D);
		this.bar.setProgress(percent);
	}

	private void setFontSize(float size) {
		this.customFont = FontLoader.loadFont("Roboto-Light.ttf", "Roboto Light", size);
	}

}