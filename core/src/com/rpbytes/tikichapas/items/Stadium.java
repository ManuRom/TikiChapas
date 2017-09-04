package com.rpbytes.tikichapas.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import static com.rpbytes.tikichapas.utils.Constants.*;

public class Stadium extends Item {
	
	public TextureRegion bottomTerraces;
	public TextureRegion upTerraces;
	public TextureRegion leftTerraces;
	public TextureRegion rightTerraces;
	public TextureRegion pitch;
	public TextureRegion upLeftCornerTerraces;
	public TextureRegion upRightCornerTerraces;
	public TextureRegion bottomLeftCornerTerraces;
	public TextureRegion bottomRightCornerTerraces;
	
	private boolean box2D = false;

	public BoundingBox boundingBox;



	public Stadium(int id,TextureRegion pitch, TextureRegion bottomTerraces, TextureRegion upTerraces, TextureRegion leftTerraces, TextureRegion rightTerraces,
				   TextureRegion upLeftCornerTerraces, TextureRegion upRightCornerTerraces, TextureRegion bottomLeftCornerTerraces, TextureRegion bottomRightCornerTerraces){
		setId(id);
		this.pitch = pitch;
		this.bottomTerraces = bottomTerraces;
		this.upTerraces = upTerraces;
		this.leftTerraces = leftTerraces;
		this.rightTerraces = rightTerraces;
		this.upLeftCornerTerraces = upLeftCornerTerraces;
		this.upRightCornerTerraces = upRightCornerTerraces;
		this.bottomLeftCornerTerraces = bottomLeftCornerTerraces;
		this.bottomRightCornerTerraces = bottomRightCornerTerraces;
		
		//setX(x);
		//setY(y);

		setWidth(PITCH_WIDTH * METERS_TO_PIXELS);
		setHeight(PITCH_HEIGHT * METERS_TO_PIXELS);
		//setWidth(this.pitch.getRegionWidth());
		//setHeight(this.pitch.getRegionHeight());

		//Vector3 minimum = new Vector3(getX() - scaleWidthTerraces(bottomLeftCornerTerraces), getY() - scaleHeightTerraces(bottomLeftCornerTerraces),0);
		Vector3 minimum = new Vector3(0,0,0);
		Vector3 maximum = new Vector3(scaleWidthTerraces(leftTerraces)+getWidth()+ scaleWidthTerraces(rightTerraces), scaleHeightTerraces(bottomTerraces) + getHeight()+ scaleHeightTerraces(upTerraces),0);
		setBoundingBox(new BoundingBox(minimum,maximum));
		setX(getBoundingBox().getWidth()-scaleWidthTerraces(rightTerraces)-getWidth());
		setY(getBoundingBox().getHeight()-scaleHeightTerraces(upTerraces)-getHeight());


		setMainImage(new Image(this.upTerraces));

	}
	

	
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(pitch, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.draw(bottomTerraces, getX(), getY() - scaleHeightTerraces(bottomTerraces), getOriginX(), getOriginY(), scaleWidthTerraces(bottomTerraces), scaleHeightTerraces(bottomTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(leftTerraces ,getX() - scaleWidthTerraces(leftTerraces), getY(), getOriginX(), getOriginY(), scaleWidthTerraces(leftTerraces), scaleHeightTerraces(leftTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(rightTerraces, getX() + getWidth(), getY(), getOriginX(), getOriginY(), scaleWidthTerraces(rightTerraces), scaleHeightTerraces(rightTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(upTerraces, getX(), getY() + getHeight(), getOriginX(), getOriginY(), scaleWidthTerraces(upTerraces), scaleHeightTerraces(upTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(upLeftCornerTerraces, getX() - scaleWidthTerraces(upLeftCornerTerraces), getY() + getHeight(), getOriginX(), getOriginY(), scaleWidthTerraces(upLeftCornerTerraces), scaleHeightTerraces(upLeftCornerTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(upRightCornerTerraces, getX() + getWidth(), getY() + getHeight(), getOriginX(), getOriginY(), scaleWidthTerraces(upRightCornerTerraces), scaleHeightTerraces(upRightCornerTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(bottomLeftCornerTerraces, getX() - scaleWidthTerraces(bottomLeftCornerTerraces), getY() - scaleHeightTerraces(bottomLeftCornerTerraces), getOriginX(), getOriginY(), scaleWidthTerraces(bottomLeftCornerTerraces), scaleHeightTerraces(bottomLeftCornerTerraces), getScaleX(), getScaleY(), getRotation());
		batch.draw(bottomRightCornerTerraces, getX() + getWidth(), getY() - scaleHeightTerraces(bottomRightCornerTerraces), getOriginX(), getOriginY(), scaleWidthTerraces(bottomRightCornerTerraces), scaleHeightTerraces(bottomRightCornerTerraces), getScaleX(), getScaleY(), getRotation());
	}



	public boolean isBox2D() {
		return box2D;
	}



	public void setBox2D(boolean box2d) {
		this.box2D = box2d;
	}


	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}


	public void scaleToBox2D(){
		this.setScale(1/ METERS_TO_PIXELS);
		this.setPosition(this.getX()/ METERS_TO_PIXELS,
						 this.getY()/ METERS_TO_PIXELS);
		this.setBox2D(true);
	}

	private float scaleWidthTerraces(TextureRegion terraces){
		return (getWidth()*terraces.getRegionWidth())/pitch.getRegionWidth();
	}

	private float scaleHeightTerraces(TextureRegion terraces){
		return (getHeight()*terraces.getRegionHeight())/pitch.getRegionHeight();
	}

	/**
	 * x and y must be in meters
	 *
	 * @param world
	 * @param x
     * @param y
     */
	public void createBox2dBorders(World world, float x, float y){
		//Bottom border
		BodyDef bodyDefBottom =new BodyDef();
		bodyDefBottom.type = BodyDef.BodyType.KinematicBody;
		bodyDefBottom.position.set(new Vector2(x + PITCH_WIDTH/2, y - BOX_WIDTH));
		Body bodyBottom = world.createBody(bodyDefBottom);
		PolygonShape shapeBottom = new PolygonShape();
		shapeBottom.setAsBox(PITCH_WIDTH/2, BOX_WIDTH);
		FixtureDef fixtureBottom = new FixtureDef();
		fixtureBottom.density=STADIUM_DENSITY;
		fixtureBottom.friction=STADIUM_DENSITY;
		fixtureBottom.restitution=STADIUM_RESTITUTION;
		fixtureBottom.shape = shapeBottom;
		bodyBottom.createFixture(fixtureBottom);
		bodyBottom.getFixtureList().first().setUserData("wallBottom");


		//Up Border
		BodyDef bodyDefUp =new BodyDef();
		bodyDefUp.type = BodyDef.BodyType.KinematicBody;
		bodyDefUp.position.set(x + PITCH_WIDTH/2, y + PITCH_HEIGHT + BOX_WIDTH);
		Body bodyUp = world.createBody(bodyDefUp);
		PolygonShape shapeUp = new PolygonShape();
		shapeUp.setAsBox(PITCH_WIDTH/2, BOX_WIDTH);
		FixtureDef fixtureUp = new FixtureDef();
		fixtureUp.density=STADIUM_DENSITY;
		fixtureUp.friction=STADIUM_FRICTION;
		fixtureUp.restitution=STADIUM_RESTITUTION;
		fixtureUp.shape = shapeUp;
		bodyUp.createFixture(fixtureUp);
		bodyUp.getFixtureList().first().setUserData("wallUp");


		//linea de fondo derecha arriba
		BodyDef bodyDefRightUp =new BodyDef();
		bodyDefRightUp.type = BodyDef.BodyType.KinematicBody;
		bodyDefRightUp.position.set(x + PITCH_WIDTH + BOX_WIDTH, y + PITCH_HEIGHT - (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		Body bodyRightUp= world.createBody(bodyDefRightUp);
		PolygonShape shapeRightUp = new PolygonShape();
		shapeRightUp.setAsBox(BOX_WIDTH, (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		FixtureDef fixtureRightUp = new FixtureDef();
		fixtureRightUp.density=STADIUM_DENSITY;
		fixtureRightUp.friction=STADIUM_FRICTION;
		fixtureRightUp.restitution=STADIUM_RESTITUTION;
		fixtureRightUp.shape = shapeRightUp;
		bodyRightUp.createFixture(fixtureRightUp);
		bodyRightUp.getFixtureList().first().setUserData("wallRight");


		//linea de fondo derecha bajo
		BodyDef bodyDefRightBottom =new BodyDef();
		bodyDefRightBottom.type = BodyDef.BodyType.KinematicBody;
		bodyDefRightBottom.position.set(x + PITCH_WIDTH + BOX_WIDTH, y + (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		Body bodyRightBottom = world.createBody(bodyDefRightBottom);
		PolygonShape shapeRightBottom = new PolygonShape();
		shapeRightBottom.setAsBox(BOX_WIDTH, (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		FixtureDef fixtureRightBottom = new FixtureDef();
		fixtureRightBottom.density=STADIUM_DENSITY;
		fixtureRightBottom.friction= STADIUM_FRICTION;
		fixtureRightBottom.restitution=STADIUM_RESTITUTION;
		fixtureRightBottom.shape = shapeRightBottom;
		bodyRightBottom.createFixture(fixtureRightBottom);
		bodyRightBottom.getFixtureList().first().setUserData("wallRight");


		//linea Red derecha
		BodyDef bodyDefRightGoal =new BodyDef();
		bodyDefRightGoal.type = BodyDef.BodyType.KinematicBody;
		bodyDefRightGoal.position.set(x + PITCH_WIDTH + GOAL_WIDTH + BOX_WIDTH, y + PITCH_HEIGHT /2);
		Body bodyRightGoal = world.createBody(bodyDefRightGoal);
		PolygonShape shapeRightGoal = new PolygonShape();
		shapeRightGoal.setAsBox(BOX_WIDTH, GOAL_HEIGHT /2);
		FixtureDef fixtureRightGoal = new FixtureDef();
		fixtureRightGoal.density=STADIUM_DENSITY;
		fixtureRightGoal.friction=STADIUM_FRICTION;
		fixtureRightGoal.restitution=STADIUM_RESTITUTION;
		fixtureRightGoal.shape = shapeRightGoal;
		bodyRightGoal.createFixture(fixtureRightGoal);
		bodyRightGoal.getFixtureList().first().setUserData("net");


		//linea de fondo izquierda arriba
		BodyDef bodyDefLeftUp =new BodyDef();
		bodyDefLeftUp.type = BodyDef.BodyType.KinematicBody;
		bodyDefLeftUp.position.set(x - BOX_WIDTH, y + PITCH_HEIGHT - (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		Body bodyLeftUp = world.createBody(bodyDefLeftUp);
		PolygonShape shapeLeftUp = new PolygonShape();
		shapeLeftUp.setAsBox(BOX_WIDTH,(PITCH_HEIGHT - GOAL_HEIGHT)/4);
		FixtureDef fixtureLeftUp = new FixtureDef();
		fixtureLeftUp.density= STADIUM_DENSITY;
		fixtureLeftUp.friction= STADIUM_FRICTION;
		fixtureLeftUp.restitution= STADIUM_RESTITUTION;
		fixtureLeftUp.shape = shapeLeftUp;
		bodyLeftUp.createFixture(fixtureLeftUp);
		bodyLeftUp.getFixtureList().first().setUserData("wallLeft");


		//linea de fondo izquierda bajo
		BodyDef bodyDefLeftBottom =new BodyDef();
		bodyDefLeftBottom.type = BodyDef.BodyType.KinematicBody;
		bodyDefLeftBottom.position.set(x - BOX_WIDTH, y + (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		Body bodyLeftBottom = world.createBody(bodyDefLeftBottom);
		PolygonShape shapeLeftBottom = new PolygonShape();
		shapeLeftBottom.setAsBox(BOX_WIDTH, (PITCH_HEIGHT - GOAL_HEIGHT)/4);
		FixtureDef fixtureLeftBottom = new FixtureDef();
		fixtureLeftBottom.density= STADIUM_DENSITY;
		fixtureLeftBottom.friction=STADIUM_FRICTION;
		fixtureLeftBottom.restitution=STADIUM_RESTITUTION;
		fixtureLeftBottom.shape = shapeLeftBottom;
		bodyLeftBottom.createFixture(fixtureLeftBottom);
		bodyLeftBottom.getFixtureList().first().setUserData("wallLeft");



		//linea Red izq
		BodyDef bodyDefLeftGoal =new BodyDef();
		bodyDefLeftGoal.type = BodyDef.BodyType.KinematicBody;
		bodyDefLeftGoal.position.set(x - GOAL_WIDTH - BOX_WIDTH, y + PITCH_HEIGHT /2);
		Body bodyLeftGoal = world.createBody(bodyDefLeftGoal);
		PolygonShape shapeLeftGoal = new PolygonShape();
		shapeLeftGoal.setAsBox(BOX_WIDTH, GOAL_HEIGHT /2);
		FixtureDef fixtureLeftGoal = new FixtureDef();
		fixtureLeftGoal.density=STADIUM_DENSITY;
		fixtureLeftGoal.friction=STADIUM_FRICTION;
		fixtureLeftGoal.restitution=STADIUM_RESTITUTION;
		fixtureLeftGoal.shape = shapeLeftGoal;
		bodyLeftGoal.createFixture(fixtureLeftGoal);
		bodyLeftGoal.getFixtureList().first().setUserData("net");

		BodyDef bodyDefCornerUpLeft =new BodyDef();
		bodyDefCornerUpLeft.type = BodyDef.BodyType.KinematicBody;
		bodyDefCornerUpLeft.position.set(x, y + PITCH_HEIGHT);
		Body bodyCornerUpLeft = world.createBody(bodyDefCornerUpLeft);
		PolygonShape shapeCornerUpLeft = new PolygonShape();
		shapeCornerUpLeft.setAsBox(CORNER_SIZE, CORNER_SIZE);
		FixtureDef fixtureCornerUpLeft = new FixtureDef();
		fixtureCornerUpLeft.density=STADIUM_DENSITY;
		fixtureCornerUpLeft.friction=STADIUM_FRICTION;
		fixtureCornerUpLeft.restitution=STADIUM_RESTITUTION;
		fixtureCornerUpLeft.shape = shapeCornerUpLeft;
		bodyCornerUpLeft.createFixture(fixtureCornerUpLeft);
		bodyCornerUpLeft.setTransform(bodyCornerUpLeft.getPosition().x,bodyCornerUpLeft.getPosition().y,(float)Math.PI/4);
		bodyCornerUpLeft.getFixtureList().first().setUserData("corner");

		BodyDef bodyDefCornerDownLeft =new BodyDef();
		bodyDefCornerDownLeft.type = BodyDef.BodyType.KinematicBody;
		bodyDefCornerDownLeft.position.set(x, y);
		Body bodyCornerDownLeft = world.createBody(bodyDefCornerDownLeft);
		PolygonShape shapeCornerDownLeft = new PolygonShape();
		shapeCornerDownLeft.setAsBox(CORNER_SIZE, CORNER_SIZE);
		FixtureDef fixtureCornerDownLeft = new FixtureDef();
		fixtureCornerDownLeft.density=STADIUM_DENSITY;
		fixtureCornerDownLeft.friction=STADIUM_FRICTION;
		fixtureCornerDownLeft.restitution=STADIUM_RESTITUTION;
		fixtureCornerDownLeft.shape = shapeCornerDownLeft;
		bodyCornerDownLeft.createFixture(fixtureCornerDownLeft);
		bodyCornerDownLeft.setTransform(bodyCornerDownLeft.getPosition().x,bodyCornerDownLeft.getPosition().y,(float)Math.PI/4);
		bodyCornerDownLeft.getFixtureList().first().setUserData("corner");

		BodyDef bodyDefCornerUpRight =new BodyDef();
		bodyDefCornerUpRight.type = BodyDef.BodyType.KinematicBody;
		bodyDefCornerUpRight.position.set(x + PITCH_WIDTH, y + PITCH_HEIGHT);
		Body bodyCornerUpRight = world.createBody(bodyDefCornerUpRight);
		PolygonShape shapeCornerUpRight = new PolygonShape();
		shapeCornerUpRight.setAsBox(CORNER_SIZE, CORNER_SIZE);
		FixtureDef fixtureCornerUpRight = new FixtureDef();
		fixtureCornerUpRight.density=STADIUM_DENSITY;
		fixtureCornerUpRight.friction=STADIUM_FRICTION;
		fixtureCornerUpRight.restitution=STADIUM_RESTITUTION;
		fixtureCornerUpRight.shape = shapeCornerUpRight;
		bodyCornerUpRight.createFixture(fixtureCornerUpRight);
		bodyCornerUpRight.setTransform(bodyCornerUpRight.getPosition().x,bodyCornerUpRight.getPosition().y,(float)Math.PI/4);
		bodyCornerUpRight.getFixtureList().first().setUserData("corner");

		BodyDef bodyDefCornerDownRight =new BodyDef();
		bodyDefCornerDownRight.type = BodyDef.BodyType.KinematicBody;
		bodyDefCornerDownRight.position.set(x + PITCH_WIDTH, y);
		Body bodyCornerDownRight = world.createBody(bodyDefCornerDownRight);
		PolygonShape shapeCornerDownRight = new PolygonShape();
		shapeCornerDownRight.setAsBox(CORNER_SIZE, CORNER_SIZE);
		FixtureDef fixtureCornerDownRight = new FixtureDef();
		fixtureCornerDownRight.density=STADIUM_DENSITY;
		fixtureCornerDownRight.friction=STADIUM_FRICTION;
		fixtureCornerDownRight.restitution=STADIUM_RESTITUTION;
		fixtureCornerDownRight.shape = shapeCornerDownRight;
		bodyCornerDownRight.createFixture(fixtureCornerDownRight);
		bodyCornerDownRight.setTransform(bodyCornerDownRight.getPosition().x,bodyCornerDownRight.getPosition().y,(float)Math.PI/4);
		bodyCornerDownRight.getFixtureList().first().setUserData("corner");
	}
	
}
