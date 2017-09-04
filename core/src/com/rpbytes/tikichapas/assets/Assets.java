package com.rpbytes.tikichapas.assets;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.rpbytes.tikichapas.utils.Text;

public class Assets {
	
	public static AssetManager manager;
	
	//Recursos Menu
	public static TextureAtlas menuScreenAtlas;
	public static AtlasRegion background;
	//public static AtlasRegion botonJugar;
	public static AtlasRegion botonPartidoRapido;
	public static AtlasRegion botonPartidoEnRed;
	public static AtlasRegion botonOpciones;
	public static AtlasRegion botonSalir;
	public static AtlasRegion next;
	public static AtlasRegion nextTouch;
	public static AtlasRegion tittle;
	public static AtlasRegion turnos;
	public static AtlasRegion button;
	public static AtlasRegion buttonTouch;
	public static AtlasRegion goals;
	public static AtlasRegion locked;
	public static AtlasRegion human;
	public static AtlasRegion ia;
	public static AtlasRegion tikiPoints;
	public static AtlasRegion add;	
	public static AtlasRegion addTouch;
	public static AtlasRegion backgroundButton;
	
	//Recursos Flags
	public static AtlasRegion brazil;
	public static AtlasRegion france;
	public static AtlasRegion germany;
	public static AtlasRegion spain;
	
	//Recursos Juego
	public static TextureAtlas gameScreenAtlas;
//	public static AtlasRegion terrenoDeJuego;
	//public static AtlasRegion chapaRoja;
	//public static AtlasRegion chapaAzul;
	//public static AtlasRegion chapaSeleccionada;
	public static AtlasRegion ball;
	public static AtlasRegion balon2;
	public static AtlasRegion balon3;
	public static AtlasRegion goal;
	public static AtlasRegion porteriaIzq;
	public static AtlasRegion dorsal1;
	public static AtlasRegion dorsal2;
	public static AtlasRegion dorsal3;
	public static AtlasRegion dorsal4;
	public static AtlasRegion dorsal5;
	public static AtlasRegion listo;
	public static AtlasRegion lanzador;
	public static AtlasRegion lanzadorInverso;
	public static AtlasRegion lanzador2;
	public static AtlasRegion lanzador2Inverso;
	public static AtlasRegion circulo;
	
	public static AtlasRegion hud;
	public static AtlasRegion turn;
	
	//Tipos de tapones
	public static AtlasRegion speedTapon;
	public static AtlasRegion normalTapon;
	public static AtlasRegion technicalTapon;
	public static AtlasRegion hardTapon;
	public static AtlasRegion goalkeeperTapon;

	//Animacion turno
	public static AtlasRegion turnAnimation1;
	public static AtlasRegion turnAnimation2;
	public static AtlasRegion turnAnimation3;
	public static AtlasRegion turnAnimation4;


	//Equipaciones equipos
	public static TextureAtlas equipacionesAtlas;
	public static AtlasRegion germanyHome;
	public static AtlasRegion germanyAway;
	//public static AtlasRegion argentinaLocal;
	//public static AtlasRegion argentinaVisitante;
	public static AtlasRegion brazilHome;
	public static AtlasRegion brazilAway;
	public static AtlasRegion spainHome;
	public static AtlasRegion spainAway;
	public static AtlasRegion franceHome;
	public static AtlasRegion franceAway;
	//public static AtlasRegion holandaLocal;
	//public static AtlasRegion holandaVisitante;
	//public static AtlasRegion inglaterraLocal;
	//public static AtlasRegion inglaterraVisitante;
	//public static AtlasRegion italiaLocal;
	//public static AtlasRegion italiaVisitante;
	//public static AtlasRegion portugalLocal;
	//public static AtlasRegion portugalVisitante;
	//public static AtlasRegion uruguayLocal;
	//public static AtlasRegion uruguayVisitante;
	
	//Estadios
	//Estadio de cadiz
	public static AtlasRegion cadizTerreno;
	public static AtlasRegion cadizTribuna;
	public static AtlasRegion cadizFondoNorte;
	public static AtlasRegion cadizFondoSur;
	public static AtlasRegion cadizPreferencia;
	public static AtlasRegion cadizEsq1;
	public static AtlasRegion cadizEsq2;
	public static AtlasRegion cadizEsq3;
	public static AtlasRegion cadizEsq4;
	
	
	//Musica
	public static Music musicMenu;
	
	//Efectos de sonido
	public static Sound silbato;
	public static Sound toque;
	public static Sound boton;
	public static Sound hit;
	public static Sound hitWall;
	public static Sound hitCap;
	public static Sound goalScore;
	public static Sound whistle;
	public static Music crowd;
	public static Sound clickButton;
	
	public static void loadManager(){
		manager = new AssetManager();
		manager.load("pack/assets.pack", TextureAtlas.class);
		manager.load("data/turn.pack", TextureAtlas.class);
		manager.load("pack/flags.pack", TextureAtlas.class);
		manager.load("pack/formations.txt", TextureAtlas.class);
		//Load Music and Sound
		manager.load("music/musicMenu.mp3", Music.class);
		manager.load("sound/hit.ogg", Sound.class);
		manager.load("sound/hitWall.ogg", Sound.class);
		manager.load("sound/hitCap.wav", Sound.class);
		manager.load("sound/goal.ogg", Sound.class);
		manager.load("sound/whistle.mp3", Sound.class);
		manager.load("sound/crowd.mp3", Music.class);
		manager.load("sound/button.wav", Sound.class);
	}
	//Metodo que carga los recursos que vamos a utilizar
	public static void load(){
		
		
		//Menu
		botonPartidoRapido = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/botonPartidoRapido");
		botonPartidoEnRed = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/botonPartidoEnRed");
		botonOpciones = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/botonOpciones");
		botonSalir = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/botonSalir");
//		botonJugar = menuScreenAtlas.findRegion("jugar");
		turnos = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/turnos");
		button = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/button");
		buttonTouch = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/buttonTouch");
		goals = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/goals");
		next = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/next");
		nextTouch = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/nexTouch");
		locked = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/locked");
		human = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/human");
		ia = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/ia");
		tikiPoints = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/tikiPoints");
		add = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/add");
		addTouch = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/addTouch");
		backgroundButton = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("menus/backgroundButton");
		
		
		
		
		//game
		background = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/background");
		tittle = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/tittle");
		ball = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/ball");
		goal = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/goal");
//		listo = gameScreenAtlas.findRegion("listo");
		
		speedTapon = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/speedTapon");
		normalTapon = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/normalTapon");
		technicalTapon = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/technicalTapon");
		hardTapon = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/hardTapon");
		goalkeeperTapon = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/goalKeeperTapon");

		turnAnimation1 = manager.get("data/turn.pack", TextureAtlas.class).findRegion("turn_animation1");
		turnAnimation2 = manager.get("data/turn.pack", TextureAtlas.class).findRegion("turn_animation2");
		turnAnimation3 = manager.get("data/turn.pack", TextureAtlas.class).findRegion("turn_animation3");
		turnAnimation4 = manager.get("data/turn.pack", TextureAtlas.class).findRegion("turn_animation4");

		hud = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/hud");
		turn = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("game/turn");
		
		//Tengo que incluirlos en el manager
		gameScreenAtlas = new TextureAtlas(Gdx.files.internal("data/gameScreen.atlas"));
		//PROVISIONALES
		lanzador = gameScreenAtlas.findRegion("lanzador");
		lanzadorInverso = gameScreenAtlas.findRegion("lanzadorInverso");
		lanzador2 = gameScreenAtlas.findRegion("lanzador2");
		lanzador2Inverso = gameScreenAtlas.findRegion("lanzador2Inverso");
		circulo = gameScreenAtlas.findRegion("circulo");
		
		//Estadios
		
		//Estadio de Cadiz
		cadizTerreno = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizTerreno");
		cadizTribuna = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizTribuna");
		cadizPreferencia = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizPrefencia");
		cadizFondoNorte = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizFondoNorte");
		cadizFondoSur = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizFondoSur");
		cadizEsq1 = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizEsq1");
		cadizEsq2 = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizEsq2");
		cadizEsq3 = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizEsq3");
		cadizEsq4 = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("estadios/EstadioDeCadiz/cadizEsq4");
		
		
		//Equipaciones
		germanyHome = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/germanyHome");
		germanyAway = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/germanyAway");
	    brazilHome = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/brazilHome");
		brazilAway = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/brazilAway");
		spainHome = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/spainHome");
		spainAway = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/spainAway");
		franceHome = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/franceHome");
		franceAway = manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/franceAway");

		//flags
		brazil = manager.get("pack/flags.pack", TextureAtlas.class).findRegion("brazil");
		france = manager.get("pack/flags.pack", TextureAtlas.class).findRegion("france");
		germany = manager.get("pack/flags.pack", TextureAtlas.class).findRegion("germany");
		spain = manager.get("pack/flags.pack", TextureAtlas.class).findRegion("spain");
		
		
		
		/*
		//menuScreenAtlas = new TextureAtlas(Gdx.files.internal("data/menuScreen.atlas"));
		
		background = menuScreenAtlas.findRegion("menu");
		botonPartidoRapido = menuScreenAtlas.findRegion("botonPartidoRapido");
		botonPartidoEnRed = menuScreenAtlas.findRegion("botonPartidoEnRed");
		botonOpciones = menuScreenAtlas.findRegion("botonOpciones");
		botonSalir = menuScreenAtlas.findRegion("botonSalir");
//		botonJugar = menuScreenAtlas.findRegion("jugar");
		next = menuScreenAtlas.findRegion("siguiente");
		nextTouch = menuScreenAtlas.findRegion("anterior");
		tittle = menuScreenAtlas.findRegion("letrasMenu");
		
		gameScreenAtlas = new TextureAtlas(Gdx.files.internal("data/gameScreen.atlas"));
//		terrenoDeJuego = gameScreenAtlas.findRegion("terrenoDeJuego");
		//chapaRoja = gameScreenAtlas.findRegion("chapaRoja");
		//chapaAzul = gameScreenAtlas.findRegion("chapaAzul");
		//chapaSeleccionada = gameScreenAtlas.findRegion("chapaSeleccionada");
		ball = gameScreenAtlas.findRegion("ball");
		balon2 = gameScreenAtlas.findRegion("balon2");
		balon3 = gameScreenAtlas.findRegion("balon3");
		goal = gameScreenAtlas.findRegion("porteriaDer");
		porteriaIzq = gameScreenAtlas.findRegion("porteriaIzq");
		dorsal1 = gameScreenAtlas.findRegion("dorsal1");
		dorsal2 = gameScreenAtlas.findRegion("dorsal2");
		dorsal3 = gameScreenAtlas.findRegion("dorsal3");
		dorsal4 = gameScreenAtlas.findRegion("dorsal4");
		dorsal5 = gameScreenAtlas.findRegion("dorsal5");
		listo = gameScreenAtlas.findRegion("listo");
		speedTapon = gameScreenAtlas.findRegion("speedTapon");
		normalTapon = gameScreenAtlas.findRegion("normalTapon");
		technicalTapon = gameScreenAtlas.findRegion("technicalTapon");
		hardTapon = gameScreenAtlas.findRegion("hardTapon");
		goalkeeperTapon = gameScreenAtlas.findRegion("goalkeeperTapon");
		lanzador = gameScreenAtlas.findRegion("lanzador");
		lanzadorInverso = gameScreenAtlas.findRegion("lanzadorInverso");
		lanzador2 = gameScreenAtlas.findRegion("lanzador2");
		lanzador2Inverso = gameScreenAtlas.findRegion("lanzador2Inverso");
		circulo = gameScreenAtlas.findRegion("circulo");
		//Estadios
		
		//Estadio de Cadiz
		cadizTerreno = gameScreenAtlas.findRegion("cadizTerreno");
		cadizTribuna = gameScreenAtlas.findRegion("cadizTribuna");
		cadizPreferencia = gameScreenAtlas.findRegion("cadizPreferencia");
		cadizFondoNorte = gameScreenAtlas.findRegion("cadizFondoNorte");
		cadizFondoSur = gameScreenAtlas.findRegion("cadizFondoSur");
		cadizEsq1 = gameScreenAtlas.findRegion("cadizEsq1");
		cadizEsq2 = gameScreenAtlas.findRegion("cadizEsq2");
		cadizEsq3 = gameScreenAtlas.findRegion("cadizEsq3");
		cadizEsq4 = gameScreenAtlas.findRegion("cadizEsq4");
		
		
		//Equipaciones
		equipacionesAtlas = new TextureAtlas(Gdx.files.internal("data/equipaciones.atlas"));
		germanyHome = equipacionesAtlas.findRegion("alemaniaLocal");
		germanyAway = equipacionesAtlas.findRegion("alemaniaVisitante");
		//argentinaLocal = equipacionesAtlas.findRegion("argentinaLocal");
	    //argentinaVisitante = equipacionesAtlas.findRegion("argentinaVisitante");
	    //brasilLocal = equipacionesAtlas.findRegion("brasilLocal");
		//brasilVisitante = equipacionesAtlas.findRegion("brasilVisitante");
		spainHome = equipacionesAtlas.findRegion("espanaLocal");
		spainAway = equipacionesAtlas.findRegion("espanaVisitante");
		franceHome = equipacionesAtlas.findRegion("franciaLocal");
		franceAway = equipacionesAtlas.findRegion("franciaVisitante");
		//holandaLocal = equipacionesAtlas.findRegion("holandaLocal");
		//holandaVisitante = equipacionesAtlas.findRegion("holandaVisitante");
		//inglaterraLocal = equipacionesAtlas.findRegion("inglaterraLocal");
		//inglaterraVisitante = equipacionesAtlas.findRegion("inglaterraVisitante");
		//italiaLocal = equipacionesAtlas.findRegion("italiaLocal");
		//italiaVisitante = equipacionesAtlas.findRegion("italiaVisitante");
		//portugalLocal = equipacionesAtlas.findRegion("portugalLocal");
		//portugalVisitante = equipacionesAtlas.findRegion("portugalVisitante");
		//uruguayLocal = equipacionesAtlas.findRegion("uruguayLocal");
		//uruguayVisitante = equipacionesAtlas.findRegion("uruguayVisitante");
		
		
		//Musica
		musicMenu = Gdx.audio.newMusic(Gdx.files.internal("music/musicMenu.mp3"));
		*/
		//Sound
		hit = manager.get("sound/hit.ogg", Sound.class);
		hitWall = manager.get("sound/hitWall.ogg", Sound.class);
		hitCap = manager.get("sound/hitCap.wav", Sound.class);
		goalScore = manager.get("sound/goal.ogg", Sound.class);
		whistle = manager.get("sound/whistle.mp3", Sound.class);
		crowd = manager.get("sound/crowd.mp3", Music.class);
		clickButton = manager.get("sound/button.wav", Sound.class);
		/*
		silbato = Gdx.audio.newSound(Gdx.files.internal("music/silbato.mp3"));
		toque = Gdx.audio.newSound(Gdx.files.internal("music/toque.mp3"));
		boton = Gdx.audio.newSound(Gdx.files.internal("music/boton.mp3"));
		*/
		
	}

	//Metodo que destruye los recursos
	public static void dispose(){
/*		menuScreenAtlas.dispose();
		gameScreenAtlas.dispose();
		equipacionesAtlas.dispose();*/
		manager.dispose();
		hit.dispose();
		hitWall.dispose();
		hitCap.dispose();
		goalScore.dispose();
		whistle.dispose();
		crowd.dispose();
		clickButton.dispose();
	}
	

}
