package com.rpbytes.tikichapas.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.rpbytes.tikichapas.assets.Assets;

public class Item extends Actor {
	private boolean locked;
	private int price;
	private int id;
	private String name;
	private String img_path;
	private Image mainImage;

	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getImgPath(){
		return img_path;
	}

	public void setImgPath(String img_path){
		this.img_path = img_path;
	}

	public JsonReader getReader(){
		JsonReader reader = new JsonReader();
		return reader;
	}

	public Image getMainImage() {
		return mainImage;
	}

	public void setMainImage(Image mainImage) {
		this.mainImage = mainImage;
	}

}
