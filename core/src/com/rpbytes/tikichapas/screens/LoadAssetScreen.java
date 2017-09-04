package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;

public class LoadAssetScreen extends AbstractScreen {

	public int progress;
	public Label percent;
	
	public LoadAssetScreen(TikiChapasGame game) {
		setGame(game);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		if(Assets.manager.update()){
			Assets.load();
			getGame().setScreen(new SplashScreen(getGame()));
		}
		progress =(int) (Assets.manager.getProgress()*100);

		percent.setText(Integer.toString(progress)+" %");

		getMainStage().act(delta);
		getMainStage().draw();

		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width,height);
	}

	@Override
	public void show() {
		super.show();

		percent = new Label("",getSkin());
		percent.setPosition(getViewportWidth()/2, getViewportHeight()/2, Align.center);

		getMainStage().addActor(percent);

		Assets.loadManager();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		//percent_.dispose();
		getGame().dispose();

	}
	

}
