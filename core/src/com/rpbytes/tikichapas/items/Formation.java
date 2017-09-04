package com.rpbytes.tikichapas.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.math.Pair;


import org.json.JSONObject;

//tipo de items ---> id_type = 3
public class Formation extends Item {

	public Array<Pair<Integer,Vector2>> formation;

	public Formation(int id, String name, int price, boolean locked, Array<Pair<Integer, Vector2>> formation){
		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		this.setLocked(locked);
		this.formation = formation;
		this.setMainImage(new Image(Assets.manager.get("pack/formations.txt", TextureAtlas.class).findRegion(getName().toLowerCase())));
	}

	public Formation(JSONObject item, JSONObject formation){
		setId(item.getInt("id"));
		setName(item.getString("name"));
		setPrice(item.getInt("price"));
		setLocked(item.getBoolean("locked"));

		this.formation = new Array<Pair<Integer,Vector2>>();
		this.formation.add(new Pair<Integer, Vector2>(formation.getInt("cap1_type_id"),new Vector2((float) formation.getDouble("x1"),(float) formation.getDouble("y1"))));
		this.formation.add(new Pair<Integer, Vector2>(formation.getInt("cap2_type_id"),new Vector2((float) formation.getDouble("x2"),(float) formation.getDouble("y2"))));
		this.formation.add(new Pair<Integer, Vector2>(formation.getInt("cap3_type_id"),new Vector2((float) formation.getDouble("x3"),(float) formation.getDouble("y3"))));
		this.formation.add(new Pair<Integer, Vector2>(formation.getInt("cap4_type_id"),new Vector2((float) formation.getDouble("x4"),(float) formation.getDouble("y4"))));
		this.formation.add(new Pair<Integer, Vector2>(formation.getInt("cap5_type_id"),new Vector2((float) formation.getDouble("x5"),(float) formation.getDouble("y5"))));
		this.setMainImage(new Image(Assets.manager.get("pack/formations.txt", TextureAtlas.class).findRegion(getName().toLowerCase())));

	}
	
	public void setFormationToAway(){
		for(int i=0; i<getFormationData().size;i++){
			getFormationData().get(i).getSecond().x = Constants.PITCH_WIDTH-getFormationData().get(i).getSecond().x;
		}

	}
	
	public void scalePositionsToBox2D(){
		scalePositionsTo(1/Constants.METERS_TO_PIXELS);
	}
	
	public void scalePositionsTo(float scale){
		for(int i=0;i< getFormationData().size;i++){
			getFormationData().get(i).getSecond().x = getFormationData().get(i).getSecond().x*scale;
			getFormationData().get(i).getSecond().y = getFormationData().get(i).getSecond().y*scale;
		}
	}

	public Array<Pair<Integer,Vector2>> duplicateFormationData(){
		Array<Pair<Integer,Vector2>> result = new Array<Pair<Integer,Vector2>>();
		for(int i=0; i<formation.size;i++){
			result.add(new Pair<Integer, Vector2>(formation.get(i).getFirst(), new Vector2(formation.get(i).getSecond().x,formation.get(i).getSecond().y)));
		}
		return result;
	}

	public Array<Pair<Integer,Vector2>> getFormationData(){
		return formation;
	}

}
