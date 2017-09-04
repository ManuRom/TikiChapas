package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rpbytes.tikichapas.assets.Assets;

import static com.rpbytes.tikichapas.utils.Constants.*;

public class Goal extends Actor {
	
	//CONSTANS
	private final float WIDTHP=55;
	private final float HEIGHTP=128;
	private final float GOALPOSTSLEFTX=0;
	private final float GOALPOSTSLEFTY=152;
	private final float GOALPOSTSRIGHTX=745;
	private final float GOALPOSTSRIGHTY=152;
	
	protected TextureRegion texture;

	public Goal(String equipo){
		setWidth(WIDTHP/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
		setHeight(HEIGHTP/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
		if (equipo == "R"){
			setX(GOALPOSTSLEFTX/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
			setY(GOALPOSTSLEFTY/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
			texture = Assets.goal;
		}else{
			setX(GOALPOSTSRIGHTX/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
			setY(GOALPOSTSRIGHTY/ com.rpbytes.tikichapas.utils.Constants.METERS_TO_PIXELS);
			texture = new TextureRegion(Assets.goal);
			texture.flip(true, false);
		}	
	}

	public Goal(float x, float y, boolean home, TextureRegion texture){
		setX(x*METERS_TO_PIXELS);
		setY(y*METERS_TO_PIXELS);

		setWidth(GOAL_WIDTH*METERS_TO_PIXELS);
		setHeight(GOAL_HEIGHT*METERS_TO_PIXELS);

		if(home)
			this.texture = new TextureRegion(texture);
		else{
			this.texture = new TextureRegion(texture);
			this.texture.flip(true,false);
		}
	}

	public void draw(Batch batch, float parentAlpha){
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public float getCentroX() {
		return getX() + getWidth()/2;
	}
	
	public float getCentroY() {
		return getY() + getHeight()/2;
	}


}
