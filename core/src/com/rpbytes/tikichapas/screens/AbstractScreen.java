package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;

/**
 * Created by manoleichon on 2/02/17.
 */
public abstract class AbstractScreen implements Screen {

    private float viewportWidth = 1280f, viewportHeight = 720f;
    private TikiChapasGame game;
    private Stage mainStage;
    private Skin skin;
    
    @Override
    public void show() {
        mainStage = new Stage(new FitViewport(viewportWidth,viewportHeight));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Image background = new Image(skin.getDrawable("background"));
        background.setSize(viewportWidth,viewportHeight);

        mainStage.addActor(background);

        mainStage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(event.getTarget().getClass().getSuperclass().equals(Widget.class) || event.getTarget().getClass().getSuperclass().equals(Button.class)){
                    if(getGame().getPreferences().getBoolean("sound"))
                        Assets.clickButton.play();
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        mainStage.act(delta);
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        getGame().dispose();
    }

    public float getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public TikiChapasGame getGame() {
        return game;
    }

    public void setGame(TikiChapasGame game) {
        this.game = game;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
