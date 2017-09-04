package com.rpbytes.tikichapas.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.match.Match;
import com.rpbytes.tikichapas.net.Movement;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.match.OfflineMatch;
import com.rpbytes.tikichapas.match.OnlineMatch;

public class MatchScreen extends AbstractScreen {
	
	public Match match;
	public Box2DDebugRenderer debug;
	public InputMultiplexer inputMultiplexer;

	public MatchScreen(TikiChapasGame game, MatchSettings matchOptions) {
		this.setGame(game);
		match = new OfflineMatch(game,matchOptions);
		debug = new Box2DDebugRenderer();
		inputMultiplexer = new InputMultiplexer();

		//Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public MatchScreen(TikiChapasGame game, MatchSettings matchSettings, int homeGoals, int awayGoals, Array<Movement> movements, int matchId, int stateTurn, int turnHome, User homeUser, User awayUser){
		this.setGame(game);
		inputMultiplexer = new InputMultiplexer();
		match = new OnlineMatch(game,matchSettings, homeGoals, awayGoals, movements, matchId, stateTurn, turnHome, homeUser, awayUser);
		//Gdx.input.setInputProcessor(match.getStage());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		//System.out.println ("entra en render menuScreen");
        match.update();
        match.draw();
        
		//Render de box2D
        //debug.render(match.world, match.getStage().getCamera().combined);

	}

	@Override
	public void resize(int width, int height) {
		match.getStage().getViewport().update(width, height);
		match.stageHud.getViewport().update(width,height);

	}

	@Override
	public void show() {
		match.show();
		Assets.manager.get("music/musicMenu.mp3", Music.class).stop();
		if(getGame().getPreferences().getBoolean("sound")) {
			Assets.crowd.play();
			Assets.crowd.setVolume(0.5f);
			Assets.crowd.setLooping(true);
		}

		inputMultiplexer.addProcessor(match.getStage());
		inputMultiplexer.addProcessor(match.gestureDetector);
		inputMultiplexer.addProcessor(match.stageHud);

		match.stateMatch = Match.state.active;

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void hide() {
		match.hide();
		// TODO Auto-generated method stub
		Assets.crowd.stop();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		if(getGame().getPreferences().getBoolean("sound"))
			Assets.crowd.play();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		match.dispose();
		Assets.crowd.stop();
	//		debug.dispose();

	}

}
