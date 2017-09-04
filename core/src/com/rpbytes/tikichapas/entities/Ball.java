package com.rpbytes.tikichapas.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.rpbytes.tikichapas.utils.Constants;

import static com.rpbytes.tikichapas.utils.Constants.*;

public class Ball extends Entity{
	// Atributos
	public float initX, initY;

	// Constructor Predeterminado
	public Ball(World world, TextureRegion texture, float x, float y){
	
		BodyDef bodyDef =new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		
		setTexture(texture);
		//Box2D attributes
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x= x;
		bodyDef.position.y= y;
		bodyDef.angle=0.0f;
		setBody(world.createBody(bodyDef));
		fixtureDef.density = 0.2f;
		fixtureDef.friction= 0.1f;
		fixtureDef.restitution = 1f;
		fixtureDef.shape = shape;
		fixtureDef.shape.setRadius(Constants.BALL_RADIUS);
		getBody().createFixture(fixtureDef);
		getBody().getFixtureList().first().setUserData("ball");
		getBody().setLinearDamping(1f);
		getBody().setAngularDamping(1f);

		setRadius(BALL_RADIUS);

		setSize(getRadius()*METERS_TO_PIXELS*2, getRadius()*METERS_TO_PIXELS*2);
		setPosition((getBody().getPosition().x -getRadius()) * METERS_TO_PIXELS, (getBody().getPosition().y-getRadius()) * METERS_TO_PIXELS);
		setInitX(x);
		setInitY(y);
	}

	public void moveToInitPosition(){
		getBody().setTransform(getInitX(),getInitY(),0f);
		setPosition((getBody().getPosition().x -getRadius()) * METERS_TO_PIXELS, (getBody().getPosition().y-getRadius()) * METERS_TO_PIXELS);

	}

	public void draw(Batch batch, float parentAlpha) {
		if(!moveScene2D)
			setPosition((getBody().getPosition().x -getRadius()) * METERS_TO_PIXELS, (getBody().getPosition().y-getRadius()) * METERS_TO_PIXELS);

		batch.draw(getTexture(),
				 getX(), getY()
				, getWidth()/2, getHeight()/2
				, getWidth(), getHeight()
				, getScaleX(), getScaleY()
				, (getBody().getAngle()*180)/Double.valueOf(Math.PI).floatValue());
	}

	public float getInitX() {
		return initX;
	}

	public void setInitX(float initX) {
		this.initX = initX;
	}

	public float getInitY() {
		return initY;
	}

	public void setInitY(float initY) {
		this.initY = initY;
	}

}
