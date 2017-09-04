package com.rpbytes.tikichapas.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.rpbytes.tikichapas.assets.Assets;

import org.json.JSONObject;

public class BallItem extends Item {
	public TextureRegion textBall;
	
	public BallItem(int id, String name, int price, boolean locked){
		setName(name);
		setId(id);
		setPrice(price);
		setLocked(locked);
		textBall = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("balls/"+getName());
		setMainImage(new Image(textBall));
	}

	public BallItem(JSONObject json){
		setId(json.getInt("id"));
		setName(json.getString("name"));
		setPrice(json.getInt("price"));
		setLocked(json.getBoolean("locked"));
		textBall = Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("balls/"+getName());
		setMainImage(new Image(textBall));
	}

	
	public TextureRegion getTexture(){
		return textBall;
	}
	
	public boolean TestPrecondition(){
		return (Assets.manager.get("pack/assets.pack", TextureAtlas.class).findRegion("balls/"+getName()) != null);
	}
	
}
