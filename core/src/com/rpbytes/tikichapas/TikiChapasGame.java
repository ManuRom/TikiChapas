package com.rpbytes.tikichapas;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.net.OnlineMatchNotification;
import com.rpbytes.tikichapas.screens.LoadAssetScreen;
import com.rpbytes.tikichapas.items.Item;

public class TikiChapasGame extends Game {

	private SpriteBatch batch;
	private User user;
	private String token;
	private Array<Item> items;
	private Array<OnlineMatchNotification> notifications;
	private boolean offline;
	
	
	@Override
	public void create () {
		setBatch(new SpriteBatch());
		setUser(new User());
		setItems(new Array<Item>());
		setNotifications(new Array<OnlineMatchNotification>());
		initializePreferences();
		setScreen(new LoadAssetScreen(this));
	}
	
	public void setScreen(Screen screen)
	{
		super.setScreen(screen);
	}
	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}
	
	@Override
	public void dispose() {
		//getBatch().dispose();
//		getScreen().dispose();
		Assets.dispose();
		super.dispose();
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Array<Item> getItems() {
		return items;
	}

	public void setItems(Array<Item> items) {
		this.items = items;
	}

	public String getToken(){ return token; }

	public void setToken(String token){ this.token = token; }

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public void setNotifications(Array<OnlineMatchNotification> notifications) {
		this.notifications = notifications;
	}
	public Array<OnlineMatchNotification> getNotifications() {
		return notifications;
	}

	public void initializePreferences(){
		if(!getPreferences().contains("music"))
			getPreferences().putBoolean("music",false);
		if(!getPreferences().contains("sound"))
			getPreferences().putBoolean("sound",true);
	}
	public Preferences getPreferences(){
		return Gdx.app.getPreferences("data");
	}
}
