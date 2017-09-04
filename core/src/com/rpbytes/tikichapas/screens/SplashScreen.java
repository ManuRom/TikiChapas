package com.rpbytes.tikichapas.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;


public class SplashScreen extends AbstractScreen {
	

    private Image splashImage;

    
 
    public SplashScreen( TikiChapasGame game )
    {
    	setGame(game);
    	//new TikiChapasGame();
		//this.batch = new SpriteBatch();
		//this.stage = new Stage();
    }
 
    public void show()
    {        
        super.show();

        Drawable splashDrawable = new TextureRegionDrawable( Assets.tittle );
        splashImage = new Image(splashDrawable , Scaling.none);
        
        splashImage.setFillParent( true );

        splashImage.getColor().a = 0f;
        splashImage.addAction( sequence( fadeIn( 2f ), delay( 2f ), Actions.moveBy(0, 1000, 0.5f),
            new Action() {
                public boolean act( float delta )
                {
                	getGame().setScreen( new LoginScreen(getGame()));
                    return true;
                }
            } ) );
        getMainStage().addActor( splashImage );
    } 

    public void resize(int width, int height )
    {
    	super.resize(width, height);
    }

    @Override
    public void dispose()
    {
    	/*System.out.println("Dispose de SplashScreen");
    	batch.dispose();
		stage.dispose();*/
        //splashTexture.dispose();
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
	public void render(float delta) {
        super.render(delta);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
