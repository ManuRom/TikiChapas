package com.rpbytes.tikichapas.match;

import java.util.Iterator;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.dialogs.ConfigDialog;
import com.rpbytes.tikichapas.entities.Ball;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.dialogs.EndMatchDialog;
import com.rpbytes.tikichapas.dialogs.NewGoalAnimation;
import com.rpbytes.tikichapas.entities.Cap;
import com.rpbytes.tikichapas.input.CameraController;
import com.rpbytes.tikichapas.items.Stadium;
import com.rpbytes.tikichapas.utils.Text;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public abstract class Match {
	
	/*
	 * ---------------------------
	 * * * CLASS ATTRIBUTES * * *
	 * ---------------------------
	*/
	
	//Constants
	public final float ROZAMIENTO = 0.005f;
	public final float PADDING = 15f;

	//Game instance
	public TikiChapasGame game;

	//Batch
	public Skin skin;
	
	//Option
	public com.rpbytes.tikichapas.match.MatchSettings matchSetting;
	
	//Stage
	public Stage stageMatch, stageHud;
	
	//Actors
	public Stadium stadium;
	public Goal goalRight, goalLeft;
	public Ball ball;
	public Array<Cap> caps;

	
	//Touch
	public Player player;
	//IA
	private PlayerIA playerIA;

	public boolean dragged = false;
	public boolean active = true;
		
	//Box2D
	public World world;
	public Box2DDebugRenderer debug;
	
	//Fps
	public BitmapFont fps;
		
	//Camera
	public OrthographicCamera camera;

	//Logic
	public enum state {active, reposition ,noActive, end};
	public state stateMatch;
	public boolean homeTurn = true;
	public boolean awayTurn = false;
	public boolean canMove = true;
	public int turnCount = 0;
	public int turnsCount =0;
	private boolean online;
	public boolean touchUpMovement=false;
	//public float acumulador;
	
	public Label startMatch;

	public int homeGoals = 0, awayGoals = 0;

	public Scoreboard scoreboard;
	public ImageButton back, config, zoomIn, zoomOut;
	public NewGoalAnimation newGoalAnimation;
	public EndMatchDialog endMatchDialog;
	public ConfigDialog configDialog;

	//Input
	public CameraController cameraController;
	public GestureDetector gestureDetector;

	public RayHandler rayHandler;

	public float viewportWidth = 1280;
	public float viewportHeight = 720;
	/*
	 * ---------------------------
	 * * * CLASS METHODS * * *
	 * ---------------------------
	*/
	
	//Construct
	public Match(TikiChapasGame game, com.rpbytes.tikichapas.match.MatchSettings matchSettings){
		this.game = game;
		this.matchSetting = matchSettings;
	}

	public Stage getStage() {
		return stageMatch;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public abstract void sendMovementRequest(float forceX, float forceY, int capIndex, boolean endMovement);

	public void show(){
		world = new World(new Vector2(0,0),true);
		World.setVelocityThreshold(0f);

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		caps = new Array<Cap>();

		stageMatch = new Stage(new ExtendViewport(viewportWidth, viewportHeight));
		camera = (OrthographicCamera)stageMatch.getCamera();

		player = new Player();

		stadium = matchSetting.getStadium();

		stadium.createBox2dBorders(world,stadium.getX()* Constants.PIXELS_TO_METERS,stadium.getY()* Constants.PIXELS_TO_METERS);

		float visibleWidth = (stadium.getBoundingBox().getWidth()<viewportWidth)?viewportWidth:stadium.getBoundingBox().getWidth();
		float visibleHeight = (stadium.getBoundingBox().getHeight()<viewportHeight)?viewportHeight:stadium.getBoundingBox().getHeight();


		cameraController = new com.rpbytes.tikichapas.input.CameraController(camera,visibleWidth,visibleHeight);
		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, cameraController);

		goalLeft = new Goal(stadium.getX()* Constants.PIXELS_TO_METERS - Constants.GOAL_WIDTH,
							stadium.getY()* Constants.PIXELS_TO_METERS + Constants.PITCH_HEIGHT/2 - Constants.GOAL_HEIGHT/2,
							true, com.rpbytes.tikichapas.assets.Assets.goal);
		goalRight = new Goal(stadium.getX()* Constants.PIXELS_TO_METERS + Constants.PITCH_WIDTH,
				  			 stadium.getY()* Constants.PIXELS_TO_METERS + Constants.PITCH_HEIGHT/2 - Constants.GOAL_HEIGHT/2,
							 false, com.rpbytes.tikichapas.assets.Assets.goal);

		ball = new Ball(world, matchSetting.getBall().getTexture(),
						stadium.getX()* Constants.PIXELS_TO_METERS + Constants.PITCH_WIDTH/2,
						stadium.getY()* Constants.PIXELS_TO_METERS + Constants.PITCH_HEIGHT/2);

		camera.translate((ball.getX()+ ball.getWidth()/2)-viewportWidth/2,(ball.getY()+ ball.getWidth()/2)-viewportHeight/2);


		stageMatch.addActor(stadium);
		stageMatch.addActor(ball);


		createCaps(matchSetting.getHomeFormation(),matchSetting.getTeamLocal());
		createCaps(matchSetting.getAwayFormation(),matchSetting.getTeamVisitante());

		stageMatch.addActor(player);
		for(Cap cap : caps){
			stageMatch.addActor(cap);
		}

		stageMatch.addActor(goalLeft);
		stageMatch.addActor(goalRight);

		addListenerToActors();

		createHud();

		createDialogs();

		// LIGHTS AND SHADOWS
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(new Color(.1f, .1f, .1f, .5f));
		rayHandler.setShadows(false);

		new PointLight(rayHandler,128,new Color(1,1,1,.8f), viewportWidth*1.6f,viewportWidth/3,viewportHeight+20f);

		/*Array<ConeLight> lights = new Array<ConeLight>();
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 250f, 20f, 90,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 500f, 20f, 90,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 750f, 20f, 90,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 1000f, 20f, 90,45));

		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 250f, 700f, 270,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 500f, 700f, 270,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 750f, 700f, 270,45));
		lights.add(new ConeLight(rayHandler, 128, new Color(1,1,1,.8f), 400f, 1000f, 700f, 270,45));*/


		world.setContactListener(new ContactListener() {

			private boolean areCollided(Contact contact, Object userA, Object userB) {
				Object userDataA = contact.getFixtureA().getUserData();
				Object userDataB = contact.getFixtureB().getUserData();

				if (userDataA == null || userDataB == null) {
					return false;
				}
				return (userDataA.equals(userA) && userDataB.equals(userB)) ||
						(userDataA.equals(userB) && userDataB.equals(userA));
			}

			@Override
			public void beginContact(Contact contact) {
				if(game.getPreferences().getBoolean("sound")) {
					if (areCollided(contact, "cap", "wallLeft") || areCollided(contact, "ball", "wallLeft"))
						com.rpbytes.tikichapas.assets.Assets.hitWall.play();
					if (areCollided(contact, "cap", "wallRight") || areCollided(contact, "ball", "wallRight"))
						com.rpbytes.tikichapas.assets.Assets.hitWall.play();
					if (areCollided(contact, "cap", "wallBottom") || areCollided(contact, "ball", "wallBottom"))
						com.rpbytes.tikichapas.assets.Assets.hitWall.play();
					if (areCollided(contact, "cap", "wallUp") || areCollided(contact, "ball", "wallUp"))
						com.rpbytes.tikichapas.assets.Assets.hitWall.play();
					if (areCollided(contact, "cap", "cap"))
						com.rpbytes.tikichapas.assets.Assets.hitCap.play();
					if (areCollided(contact, "cap", "ball"))
						com.rpbytes.tikichapas.assets.Assets.hitCap.play();
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
		});
	}

	public void hide(){
		world.dispose();
//		batchHud.dispose();
		stageMatch.clear();
		//stageMatch.dispose();
		//ball.dispose();
		/*for(Cap cap: caps){
			cap.dispose();
		}*/
		caps.clear();
	}
	// Observatories Methods

	//Update
	public void update() {
		updateEvents();
		stageMatch.act();
		stageHud.act();
		if(stateMatch == state.active)
			world.step(1/60f, 8, 3);
	}
	
	
	private void updateEvents(){
		cameraController.update();
		camera.update();
		listenFriction();
		listenCanMove();
		listenGoal();
		listenChangeTurn();
		listenEnd();
	}
	
	//Listener logic 
	
	private void listenFriction() {
		
		//Aplicar rozamiento a tapones
		for(Iterator<Cap> i = caps.iterator(); i.hasNext(); ){
			Cap cap = i.next();
			if(cap.getVelocityModulo()!=0f)
				if(cap.getVelocityModulo()<ROZAMIENTO)
					cap.stopEntity();
		}
				
		//Aplicar rozamiento a ball
		if(ball.getVelocityModulo()<(ROZAMIENTO))
					ball.stopEntity();
	}
	
	public abstract void listenCanMove();

	public boolean areStoppedCapsAndBall(){
		boolean stopped = true;
		for(Cap cap : caps){
			if (!cap.stopped())
				stopped = false;
		}
		if (!ball.stopped())
			stopped = false;

		return stopped;
	}

	public abstract void listenChangeTurn();

	public void changeTurn(Boolean local){
		if(local){
			homeTurn =false;
			awayTurn =true;
			turnCount =0;
			turnsCount +=1;
		}
		if(!local){
			homeTurn =true;
			awayTurn =false;
			turnCount =0;
			turnsCount +=1;
		}
	}

	public boolean isHomeGoal(){
		return (ball.getX() > goalRight.getX()) && (stateMatch == state.active);
	}

	public boolean isAwayGoal(){
		return ((ball.getX() + ball.getWidth()) < stadium.getX()) && (stateMatch == state.active);
	}

	public abstract void listenGoal();

	public void listenEnd(){
		if(stateMatch != state.end){
			if(matchSetting.isGoalMatch()){
				if(awayGoals == matchSetting.getGolesMax() || homeGoals == matchSetting.getGolesMax()) {
					stateMatch = state.end;
				}
			}else{
				if(turnsCount == matchSetting.getTurnosMax())
					stateMatch = state.end;
			}
		}
	}

	//Draw
	public void draw(){
		if(dragged)
			player.setVisible(true);
        else
        	player.setVisible(false);

		stageMatch.draw();
		stageHud.draw();
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
	}

	public void dispose(){
		stageMatch.dispose();
		rayHandler.dispose();
		for(Cap cap : caps){
			cap.dispose();
		}
	}

	public abstract void createEndMatchDialog();

	public void animateGoalScore(final boolean homeGoal) {
		newGoalAnimation.setVisible(true);
		if(game.getPreferences().getBoolean("matchCrowd"))
			Assets.goalScore.play();
		newGoalAnimation.addAction(new SequenceAction(Actions.moveToAligned(viewportWidth/2,viewportHeight/2,Align.center,0.5f), Actions.delay(3), Actions.moveToAligned(viewportWidth/2,viewportHeight*2,Align.center,0.5f), new Action() {
			@Override
			public boolean act(float delta) {
				if (homeGoal) {
					newGoalAnimation.getCenterScoreboard().getHomeGoals().removeAction(newGoalAnimation.getCenterScoreboard().getHomeGoals().getActions().peek());
					newGoalAnimation.getCenterScoreboard().getHomeGoals().addAction(Actions.alpha(1));
				} else {
					newGoalAnimation.getCenterScoreboard().getAwayGoals().removeAction(newGoalAnimation.getCenterScoreboard().getAwayGoals().getActions().peek());
					newGoalAnimation.getCenterScoreboard().getAwayGoals().addAction(Actions.alpha(1));
				}
				newGoalAnimation.getGoalLabel().removeAction(newGoalAnimation.getGoalLabel().getActions().peek());
				newGoalAnimation.setVisible(false);
				if(stateMatch == state.end){
					createEndMatchDialog();
				}else {
					stateMatch = state.active;
				}
				return true;
			}
		}));
		if (homeGoal){
			newGoalAnimation.getCenterScoreboard().getHomeGoals().addAction(Actions.forever(new SequenceAction(Actions.fadeIn(0.5f), Actions.fadeOut(0.5f))));
		}else{
			newGoalAnimation.getCenterScoreboard().getAwayGoals().addAction(Actions.forever(new SequenceAction(Actions.fadeIn(0.5f), Actions.fadeOut(0.5f))));
		}
		newGoalAnimation.getGoalLabel().addAction(Actions.forever(new SequenceAction(Actions.fadeIn(0.5f),Actions.fadeOut(0.5f))));
	}

	public void createHud(){
		stageHud = new Stage(new ExtendViewport(viewportWidth, viewportHeight));

		scoreboard = new Scoreboard(homeGoals,awayGoals,matchSetting.getTeamLocal().getName(),matchSetting.getTeamVisitante().getName(),matchSetting.getTeamLocal().getEmblem(),matchSetting.getTeamVisitante().getEmblem(),skin);
		back = new ImageButton(skin,"back");
		config = new ImageButton(skin,"config");
		zoomIn = new ImageButton(skin, "plus");
		zoomOut = new ImageButton(skin, "less");
		configDialog = new ConfigDialog(Text.CONFIG_DIALOG, skin, game);

		scoreboard.getHudScoreboard().setPosition(viewportWidth/2,viewportHeight-15-(back.getHeight()/2)+100, Align.center);

		back.setPosition(PADDING-100,viewportHeight-PADDING,Align.topLeft);
		config.setPosition(viewportWidth-PADDING+100, viewportHeight-PADDING, Align.topRight);

		stageHud.addActor(scoreboard.getHudScoreboard());
		stageHud.addActor(back);
		stageHud.addActor(config);

		back.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Dialog areYouSure = new Dialog("¿Estas seguro?",skin);
				TextButton exit = new TextButton("Salir",skin);
				areYouSure.button(exit);
				areYouSure.button(new TextButton("Cancelar",skin));
				areYouSure.show(stageHud);
				exit.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						game.setScreen(new com.rpbytes.tikichapas.screens.MainMenuScreen(game));
					}
				});
			}
		});

		if(Gdx.app.getType() == Application.ApplicationType.Desktop){

			zoomOut.setPosition(viewportWidth-PADDING, PADDING, Align.bottomRight);
			zoomIn.setPosition(viewportWidth-PADDING, 115, Align.bottomRight);

			zoomIn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					camera.zoom-=0.1;
					camera.zoom = MathUtils.clamp(camera.zoom,0.5f,1f);

				}
			});

			zoomOut.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					camera.zoom+=0.1;
					camera.zoom = MathUtils.clamp(camera.zoom,0.5f,1f);
				}
			});
			stageHud.addActor(zoomIn);
			stageHud.addActor(zoomOut);
		}

		scoreboard.getHudScoreboard().addAction(Actions.sequence(Actions.delay(1f),Actions.moveToAligned(viewportWidth/2,viewportHeight-15-(back.getHeight()/2), Align.center,0.5f)));
		back.addAction(Actions.sequence(Actions.delay(1f), Actions.moveToAligned(PADDING,viewportHeight-PADDING,Align.topLeft,0.5f)));
		config.addAction(Actions.sequence(Actions.delay(1f), Actions.moveToAligned(viewportWidth-PADDING, viewportHeight-PADDING, Align.topRight, 0.5f)));
		if(turnsCount == 0){
			startMatch = new Label("COMIENZA EL PARTIDO",skin, "animation_goal");
			startMatch.setPosition(viewportWidth/2,viewportHeight/2,Align.center);
			stageHud.addActor(startMatch);
			if(game.getPreferences().getBoolean("sound"))
				Assets.whistle.play();
			startMatch.addAction(Actions.sequence(Actions.fadeIn(0.5f),Actions.delay(1),Actions.fadeOut(0.5f)));
		}

		config.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				configDialog.show(stageHud);
			}
		});
	}

	public void createDialogs(){
		newGoalAnimation = new com.rpbytes.tikichapas.dialogs.NewGoalAnimation(skin, scoreboard.getCenterScoreboard());
		newGoalAnimation.setPosition(viewportWidth/2, viewportHeight*2, Align.center);
		stageHud.addActor(newGoalAnimation);
	}

	public void createCaps(com.rpbytes.tikichapas.items.Formation formation, com.rpbytes.tikichapas.items.Team team){

		for(int i=0; i<formation.getFormationData().size;i++) {
			switch (formation.getFormationData().get(i).getFirst()){
				case 1:
					Cap nCap = new Cap(com.rpbytes.tikichapas.assets.Assets.normalTapon, Cap.CapType.normal , Constants.NORMAL_CAP_RADIUS,
							stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x,
							stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y,
							Constants.NORMAL_CAP_RESTITUTION, Constants.NORMAL_CAP_DENSITY, Constants.NORMAL_CAP_FRICTION,team,world,this);

					nCap.setFormationX(stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x);
					nCap.setFormationY(stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y);
					caps.add(nCap);
					break;
				case 2:
					Cap gkCap = new Cap(com.rpbytes.tikichapas.assets.Assets.goalkeeperTapon, Cap.CapType.goalkeeper , Constants.GK_CAP_RADIUS,
							stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x,
							stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y,
							Constants.GK_CAP_RESTITUTION, Constants.GK_CAP_DENSITY, Constants.GK_CAP_FRICTION,team,world,this);
					gkCap.setFormationX(stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x);
					gkCap.setFormationY(stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y);
					caps.add(gkCap);
					break;
				case 3:
					Cap hCap = new Cap(com.rpbytes.tikichapas.assets.Assets.hardTapon, Cap.CapType.hard , Constants.HARD_CAP_RADIUS,
							stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x,
							stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y,
							Constants.HARD_CAP_RESTITUTION, Constants.HARD_CAP_DENSITY, Constants.HARD_CAP_FRICTION,team,world, this);
					hCap.setFormationX(stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x);
					hCap.setFormationY(stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y);
					caps.add(hCap);
					break;
				case 4:
					Cap sCap = new Cap(com.rpbytes.tikichapas.assets.Assets.speedTapon, Cap.CapType.speed , Constants.SPEED_CAP_RADIUS,
							stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x,
							stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y,
							Constants.SPEED_CAP_RESTITUTION, Constants.SPEED_CAP_DENSITY, Constants.SPEED_CAP_FRICTION,team,world, this);
					sCap.setFormationX(stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x);
					sCap.setFormationY(stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y);
					caps.add(sCap);
					break;
				case 5:
					Cap tCap = new Cap(com.rpbytes.tikichapas.assets.Assets.technicalTapon, Cap.CapType.technical , Constants.TECNHICAL_CAP_RADIUS,
							stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x,
							stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y,
							Constants.TECNHICAL_CAP_RESTITUTION, Constants.TECNHICAL_CAP_DENSITY, Constants.TECNHICAL_CAP_FRICTION,team,world,this);
					tCap.setFormationX(stadium.getX()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().x);
					tCap.setFormationY(stadium.getY()* Constants.PIXELS_TO_METERS + formation.getFormationData().get(i).getSecond().y);
					caps.add(tCap);
					break;
				default:
					break;
			}
		}
	}

	public void addListenerToActors(){
		for(Iterator<Cap> i = caps.iterator(); i.hasNext();){
			Cap cap = i.next();
			cap.addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					Vector3 touch = stageMatch.getCamera().unproject(new Vector3(event.getStageX(), event.getStageY(), 0));
					if((((Cap) event.getListenerActor()).getEquipo() == matchSetting.getTeamLocal()) && homeTurn){
						((Cap) event.getListenerActor()).setPulsada(true);
						return true;
					}
					if((((Cap) event.getListenerActor()).getEquipo() == matchSetting.getTeamVisitante()) && awayTurn){
						((Cap) event.getListenerActor()).setPulsada(true);
						return true;
					}
					return false;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					Cap cap = (Cap) event.getListenerActor();

					if(cap.getPulsada() && dragged){
						if(game.getPreferences().getBoolean("sound"))
							Assets.hit.play();
						int capIndex=0;
						for(int i=0;i<caps.size;i++){
							if(cap == caps.get(i))
								capIndex = i;
						}
						if(isOnline()){
							sendMovementRequest(player.getX(), player.getY(),capIndex, false);
						}
						cap.moverTapon(player.getX(), player.getY());
						turnCount++;
						touchUpMovement = true;
						dragged= false;
					}
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer) {
					Vector2 touch = new Vector2(event.getStageX(),event.getStageY());
					if( ((Cap) event.getListenerActor()).getPulsada() && canMove && stateMatch == state.active){
						Cap cap = (Cap) event.getListenerActor();
						dragged = true;

						if(player.distanciaEntreDosPuntos(touch.x, touch.y, cap.getCenterX(), cap.getCenterY()) > player.MAXIMA_FUERZA){
							player.circle.setRadio(player.MAXIMA_FUERZA);
							player.setX(player.calcularCorteX(cap.getCenterX(), cap.getCenterY(), touch.x, touch.y));
							player.setY(player.calcularCorteY(cap.getCenterX(), cap.getCenterY(), touch.x, touch.y , player.getX()));
							player.setCap(cap);
						}else{
							player.setX(touch.x);
							player.setY(touch.y);
							player.setCap(cap);
						}
					}
				}
			});

		}

	}



}
