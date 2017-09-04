package com.rpbytes.tikichapas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.sun.glass.ui.SystemClipboard;

import org.json.JSONObject;

import java.util.HashMap;


public class User {
	
	private int id;
	private String username;
	private String email;
	private int coins;
	private int wins;
	private int losses;
	private String created_at, updated_at;
	
	//Crea un usuario a traves de un Json que envia el servidor
	public User(Json json, JsonValue jsonMap){

		id = json.readValue("id", Integer.class, jsonMap.child());
		username = json.readValue("username", String.class, jsonMap.child());
		email = json.readValue("email", String.class, jsonMap.child());
		coins = json.readValue("coins", Integer.class, jsonMap.child());
		wins = json.readValue("wins", Integer.class, jsonMap.child());
		losses = json.readValue("losses", Integer.class, jsonMap.child());
		System.out.println("id:"+id+", username:"+username+", email:"+email+", coins:"+coins+", wins:"+wins+", losses:"+losses);

	}

	public User(JSONObject json){
		Gdx.app.log("jsonObject",json.toString());
		id= json.getInt("id");
		username = json.getString("username");
		email = json.getString("email");
		coins = json.getInt("coins");
		wins = json.getInt("wins");
		losses = json.getInt("losses");
		System.out.println("id:"+id+", username:"+username+", email:"+email+", coins:"+coins+", wins:"+wins+", losses:"+losses);

	}

	
	public User() {
		username= null;
		wins=0;
		losses=0;
		coins=0;
	}
	


	//Actualiza la informacion al servidor.
	public void updateUser(){

	}
	
	//Getter and Setter
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void setCoins(int coins) {
		this.coins = coins;
	}
	

	public int getWins() {
		return wins;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public void setLosses(int losses) {
		this.losses = losses;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
