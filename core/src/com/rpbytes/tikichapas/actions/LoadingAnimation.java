package com.rpbytes.tikichapas.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingAnimation extends Actor {
	private Animation animation;
	private TextureRegion currentFrame;
	public float stateTime;


	public LoadingAnimation(TextureRegion[] textAnimation){
		animation = new Animation(0.2f, textAnimation);
		stateTime = 0f;
		currentFrame = textAnimation[0];
		setSize(150,150);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		currentFrame = animation.getKeyFrame(stateTime, true);
		stateTime += Gdx.graphics.getDeltaTime();
		batch.draw(currentFrame, getX(), getY());
	}

	/*public void draw(Batch batch, float delta){
		currentFrame = animation.getKeyFrame(stateTime, true);
		stateTime += Gdx.graphics.getDeltaTime();
		batch.draw(currentFrame, getX(), getY());
	}*/
}
