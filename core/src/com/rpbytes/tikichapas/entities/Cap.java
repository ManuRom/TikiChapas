package com.rpbytes.tikichapas.entities;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.match.Goal;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.math.OperationMath;
import com.rpbytes.tikichapas.match.Match;

import static com.rpbytes.tikichapas.utils.Constants.*;

public class Cap extends Entity {

	//Generals Attributes

	public enum CapType  { normal, speed, hard, technical, goalkeeper }
	public CapType type;

	protected float formationX, formationY;
	protected float centerX, centerY;
	protected boolean touched;
	protected Team team;
	private Match match;

	public Animation turnAnimation;
	public float animationTime;

	//Attributes IA
	protected float puntuacionIA;
	protected float coeficienteIA;

	public Cap(TextureRegion texture, CapType type, float radius, float x, float y, float restitution, float density, float friction, Team team, World world, Match match){
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();


		this.type = type;

		//this.number = dorsal;
		setTexture(texture);
		this.team = team;

		//Box2D attributes
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		//this.bodyDef.angle= 4.3f;
		setBody(world.createBody(bodyDef));
		fixtureDef.density = density;
		fixtureDef.friction= friction;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = shape;
		getBody().createFixture(fixtureDef);
		getBody().getFixtureList().first().setUserData("cap");
		setRadius(radius);

		getBody().setLinearDamping(1f);
		getBody().setAngularDamping(1f);

		setSize(radius*2*METERS_TO_PIXELS, radius*2*METERS_TO_PIXELS);
		//setPosition((x-radius)*METERS_TO_PIXELS, (y-radius)*METERS_TO_PIXELS);
		coeficienteIA = 1;

		Array<TextureRegion> animationFrames = new Array<TextureRegion>();
		animationFrames.add(Assets.turnAnimation1);
		animationFrames.add(Assets.turnAnimation2);
		animationFrames.add(Assets.turnAnimation3);
		animationFrames.add(Assets.turnAnimation4);

		this.match = match;

		turnAnimation = new Animation(0.2f,animationFrames);
	}



	//----------------------------------------------------
	// METODOS MODIFICADORES
	//----------------------------------------------------


	
	/*public void setPosition(float x, float y){
//		this.setX(x);
//		this.setY(y);
		this.body.setTransform(x, y, 0);
	}
	
	public void setX(float x){
		this.body.setTransform(x, getY(), getRotation());
	}
	
	public void setY(float y){
		this.body.setTransform(getX(),y, getRotation());
	}
	*/
	public void setEquipo(Team team){
		this.team = team;
	}
	
	public void setPulsada(boolean pulsada){
		touched = pulsada;
	}

	public void setFormationX(float formationX) {
		this.formationX = formationX;
	}

	public void setFormationY(float formationY) {
		this.formationY = formationY;
	}

	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}


	//----------------------------------------------------
	// METODOS OBSERVADORES
	//----------------------------------------------------
	
	/*public float getX(){
		return this.body.getPosition().x;
	}
	
	public float getY(){
		return this.body.getPosition().y;
	}
	
	public float getCentreX(){
		return body.getPosition().x - getRadius();
	}
	
	public float getCentreY(){
		return body.getPosition().y - getRadius();
	}
	
	public float getRotation() {
		return (body.getAngle()*180)/Double.valueOf(Math.PI).floatValue();
	}
	*/


	public boolean getPulsada(){
		return touched;
	}

	public Team getEquipo(){
		return this.team;
	}

	public float getFormationX() {
		return formationX;
	}

	public float getFormationY() {
		return formationY;
	}

	public float getCenterX() {
		return getX()+(getRadius()*METERS_TO_PIXELS);
	}

	public float getCenterY() {
		return getY()+(getRadius()*METERS_TO_PIXELS);
	}

	public Match getMatch(){ return this.match; }

	//----------------------------------------------------
	// METODOS CONDICIONALES
	//----------------------------------------------------
	

	public void esPulsada(float x, float y){
		if(x < (this.getX()*getScaleX() + this.getRadius()) && x > (this.getX()*getScaleX() - this.getRadius()) && y < (this.getY()*getScaleY() + this.getRadius()) &&
				y > (this.getY()*getScaleY() - this.getRadius())){
			this.setPulsada(true);
		}else
			this.setPulsada(false);
	}
	
	//----------------------------------------------------
	// METODO DIBUJAR
	//----------------------------------------------------
	
	/*
	public void draw(SpriteBatch batch){
		
		batch.draw(texture
				, getX()-getRadius(), getY()-getRadius()
				, getRadius(), getRadius()
				, getRadius()*2, getRadius()*2
				, 1, 1
				,(body.getAngle()*180)/Double.valueOf(Math.PI).floatValue());
		batch.draw(team.getTextureLocal()
				, getX()- 20, getY() -20
				, 20, 20
				, 40,40
				,1,1
				,(body.getAngle()*180)/Double.valueOf(Math.PI).floatValue() );

	}
	*/
	@Override
	public void act(float deltaTime)
	{
		super.act(deltaTime);
		animationTime += deltaTime;
	}


	public void draw(Batch batch, float parentAlpha) {
		if(!moveScene2D)
			setPosition((getBody().getPosition().x-getRadius())*METERS_TO_PIXELS,(getBody().getPosition().y-getRadius())*METERS_TO_PIXELS);

		if(((getMatch().canMove && getMatch().homeTurn && getMatch().matchSetting.getTeamLocal() == getEquipo()) ||
				(getMatch().canMove && getMatch().awayTurn && getMatch().matchSetting.getTeamVisitante() == getEquipo())) && getMatch().stateMatch != Match.state.end) {
			TextureRegion currentFrame = turnAnimation.getKeyFrame(animationTime, true);
			batch.draw(currentFrame, getX() - 5, getY() - 5, getWidth() / 2, getHeight() / 2, getWidth() + 10, getHeight() + 10, getScaleX(), getScaleY(), (getBody().getAngle() * 180) / Double.valueOf(Math.PI).floatValue());
		}

		batch.draw(getTexture(), getX(), getY(), getWidth()/2, getHeight()/2, getWidth(), getHeight(), getScaleX(), getScaleY(), (getBody().getAngle()*180)/Double.valueOf(Math.PI).floatValue());
		batch.draw(team.getCurrentTexture(), getCenterX()-15f, getCenterY()-15f, 15f, 15f, 30f, 30f, getScaleX(), getScaleY(), getRotation());
	}
	//----------------------------------------------------
	// METODO PARA MOVER
	//----------------------------------------------------
	
	public void moverTapon(float touchX, float touchY){

		float forceX = ((getCenterX() - touchX));
		float forceY = ((getCenterY() - touchY));
		body.applyForce(forceX, forceY, getCenterX()*PIXELS_TO_METERS, getCenterY()*PIXELS_TO_METERS, true);
	}

	public void setBodyToFormation(){
		getBody().setLinearVelocity(0f,0f);
		getBody().setAngularVelocity(0f);
		getBody().setTransform(getFormationX(),getFormationY(),0f);
		setPosition((getBody().getPosition().x-getRadius())*METERS_TO_PIXELS,(getBody().getPosition().y-getRadius())*METERS_TO_PIXELS);
	}

	//----------------------------------------------------
	// METODOS IA
	//----------------------------------------------------
	
	public float calculaPuntuacion(World world, Ball ball, Goal goal){
		float dist1 = OperationMath.distanciaEntreDosPuntos(this.getX(), this.getY(), ball.getX(), ball.getY());
		float dist2 = OperationMath.distanciaEntreDosPuntos(ball.getX(), ball.getY(), goal.getCentroX(), goal.getCentroY());
		
		puntuacionIA = (dist1 + dist2) * coeficienteIA;
		
		return puntuacionIA;
	}

	public float calculaIAFuerzaX(World world, Ball ball, Goal goal){
		//float fuerzaX = (getX()-ball.getX())*100;
		float fuerzaX = (getBody().getPosition().x - ball.getBody().getPosition().x);
		return fuerzaX;
	}

	public float calculaIAFuerzaY(World world, Ball ball, Goal goal){
		//float fuerzaY = (getY()-ball.getY())*100;
		float fuerzaY = (getBody().getPosition().y - ball.getBody().getPosition().y);
		return fuerzaY;
	}
	
	public float getIAPuntuacion() {
		return puntuacionIA;
	}
	
	//----------------------------------------------------
	// METODOS REMOVE
	//----------------------------------------------------
	
	public void removeTapon(){
		this.getBody().getWorld().destroyBody(getBody());
		this.remove();
	}

	public void dispose(){
		this.getBody().getWorld().destroyBody(getBody());
		this.remove();
	}
}
