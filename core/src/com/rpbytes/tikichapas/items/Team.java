package com.rpbytes.tikichapas.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.rpbytes.tikichapas.assets.Assets;

import org.json.JSONObject;

public class Team extends Item {

	protected TextureRegion emblem, home , away;
	protected int indicetapones_[];
	protected Formation formation;
	private boolean isHome = true;
	
	public Team(String nombre, TextureRegion camisetaLocal, TextureRegion camisetaVisitante, int indicetapones[]){
		this.setName(nombre);
		this.home = camisetaLocal;
		this.away = camisetaVisitante;
		this.indicetapones_ = indicetapones;
	}
	
	public Team(int id, String name, int price, boolean locked){
		//Atributos Generales de Item
		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		this.setLocked(locked);
		//Atributos equipo, texturas

		this.emblem = Assets.manager.get("pack/flags.pack", TextureAtlas.class).findRegion(getName());
		this.home = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Home");
		this.away = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Away");
		
		this.formation = null;

		setMainImage(new Image(emblem));
		if(testPrecondition(name))
			System.out.println(getName()+" creado");
	}
	
	public Team(Json json, JsonValue jsonMap){
		//Atributos Generales de Item
		this.setId(json.readValue("id", Integer.class, jsonMap));
		this.setName(json.readValue("name", String.class, jsonMap));
		this.setPrice(json.readValue("price", Integer.class, jsonMap));
		this.setLocked(true);
		//Atributos equipo, texturas
		this.emblem = Assets.manager.get("pack/flags.pack", TextureAtlas.class).findRegion(getName());
		this.home = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Home");
		this.away = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Away");
		this.formation = null;
		setMainImage(new Image(emblem));

	}

	public Team(JSONObject json){
		setId(json.getInt("id"));
		setName(json.getString("name"));
		setPrice(json.getInt("price"));
		setLocked(json.getBoolean("locked"));

		emblem = Assets.manager.get("pack/flags.pack", TextureAtlas.class).findRegion(getName());
		home = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Home");
		away = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+getName()+"Away");
		formation = null;
		setMainImage(new Image(emblem));
	}
	
	public TextureRegion getEmblem(){
		return this.emblem;
	}
	
	public TextureRegion getTextureHome(){
		return this.home;
	}
	
	public TextureRegion getTextureVisitante(){
		return this.away;
	}

	public int[] getIndiceTapones(){
		return this.indicetapones_;
	}
	
	public boolean isHome() {
		return isHome;
	}

	public void setIsHome(boolean isHome) {
		this.isHome = isHome;
	}
	
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(getEmblem(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		if(this.isLocked()){
			batch.draw(Assets.locked, (getX()+ getWidth()/2)-Assets.locked.getRegionWidth()/2, (getY()+ getHeight()/2)-Assets.locked.getRegionHeight()/2);
		}
	}
	
	public boolean testPrecondition(String name){
		return((Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("flags/"+name) != null) &&
		   (Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+name+"Home")!= null) &&
		   (Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("equipaciones/"+name+"Away")!= null));
	}

	public TextureRegion getCurrentTexture() {
		if(isHome())
			return getTextureHome();
		else
			return getTextureVisitante();
	}

	
	
}
