package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rpbytes.tikichapas.math.Circle;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.entities.Cap;

public class Player extends Actor{
	public final float MAXIMA_FUERZA= 100.0f;
	public TextureRegion fingerTexture;
	public float angle;
	public Cap cap;
	public Circle circle;
	public ShapeRenderer renderer;
	
	public Player(){
		fingerTexture = com.rpbytes.tikichapas.assets.Assets.lanzador;
		circle = new Circle();
		setX(0);
		setY(0);
		setSize(Constants.FINGER_WIDTH*2, Constants.FINGER_WIDTH*2);
		angle = 0.0f;
		cap = null;
		renderer = new ShapeRenderer();
	}
	
	public void setCap(Cap cap){
		this.cap = cap;
	}
	
	public void setAngle(){
		float catContiguo = cap.getCenterX()-getX();
		float catOpuesto = cap.getCenterY()-getY();
		float tg = catOpuesto/catContiguo;
		angle = (float) Math.toDegrees(Math.atan(tg));
		if(cap.getCenterX() > getX()){
			fingerTexture = com.rpbytes.tikichapas.assets.Assets.lanzador;
			fingerTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}else{
			fingerTexture = com.rpbytes.tikichapas.assets.Assets.lanzadorInverso;
			fingerTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public void setRadioCirculo(){
		float nuevoRadio;
		nuevoRadio = distanciaEntreDosPuntos(cap.getCenterX(), cap.getCenterY(), getX(), getY());
		if(nuevoRadio <= MAXIMA_FUERZA)
			circle.setRadio(nuevoRadio);
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		this.setRadioCirculo();
		this.setAngle();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());

		renderer.setColor(new Color(255,255,255,0.2f));
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.circle(cap.getCenterX(), cap.getCenterY(), circle.getRadio());
		renderer.end();

		renderer.setColor(new Color(0,0,0,1f));
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.circle(cap.getCenterX(), cap.getCenterY(), circle.getRadio());
		renderer.rectLine((cap.getCenterX()-getX())+ cap.getCenterX(),(cap.getCenterY()-getY())+ cap.getCenterY(),(cap.getCenterX()-getX())+ cap.getCenterX()+(cap.getCenterX()-getX()),(cap.getCenterY()-getY())+ cap.getCenterY()+ (cap.getCenterY()-getY()),3f);

		renderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
	}

	public float distanciaEntreDosPuntos(float p1X, float p1Y, float p2X, float p2Y){
		return (float) Math.sqrt(Math.pow(p1X-p2X, 2) + Math.pow(p1Y-p2Y, 2));
	}
	
	public float calcularCorteX(float tX, float tY, float touchX, float touchY){
		float m= (tY-touchY)/(tX-touchX);
		float n= tY - m*tX;
		float a= -2*tX;
		float b= -2*tY;
		float c= tX*tX + tY*tY - MAXIMA_FUERZA*MAXIMA_FUERZA;
		
		float A=(1+m*m);
		float B=(2*m*n + a + b*m);
		float C=(n*n + b*n + c);
		if(tX >= touchX)
			return (float) ((-B - Math.sqrt((B*B)-(4*A*C)))/(2*A));
		else
			return (float) ((-B + Math.sqrt((B*B)-(4*A*C)))/(2*A));
	}
	
	public float calcularCorteY(float tX, float tY, float touchX, float touchY, float x){
		//float x = calcularCorteX(tX,tY,touchX,touchY);
		float m= (tY-touchY)/(tX-touchX);
		float n= tY - m*tX;
		return m*x +n;
	}
}
