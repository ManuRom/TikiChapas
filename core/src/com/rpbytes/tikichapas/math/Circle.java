package com.rpbytes.tikichapas.math;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Circle {
	public float radio_, posX_, posY_;
	public TextureRegion text_;
	
	
	public Circle(){
		text_ = com.rpbytes.tikichapas.assets.Assets.circulo;
	}
	
	public void setRadio(float radio){
		radio_ = radio;
	}
	
	public void setX(float x){
		posX_=x;
	}
	public void setY(float y){
		posY_=y;
	}
	
	public float getRadio(){
		return radio_;
	}
	
	public float getX(){
		return posX_;
	}
	
	public float getY(){
		return posY_;
	}
	
}
