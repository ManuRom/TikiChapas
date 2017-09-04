package com.rpbytes.tikichapas.screens;

import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.cards.SettingImageCard;
import com.rpbytes.tikichapas.cards.SettingValueCard;
import com.rpbytes.tikichapas.items.BallItem;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.items.Stadium;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.utils.Text;

public class SettingMatchScreen extends BaseMenuScreen {

	public MatchSettings matchSettings;
	public ImageButton nextGoals, beforeGoals, nextBall, beforeBall, nextStadium, beforeStadium;
	public SettingValueCard goals;
	public SettingImageCard balls, stadiums;
	public Array<BallItem> arrayBalls;
	public Array<Stadium> arrayStadiums;
	

	public SettingMatchScreen(TikiChapasGame game, MatchSettings matchSettings) {
		setGame(game);
		this.matchSettings = matchSettings;
		this.arrayBalls = new Array<BallItem>();
		this.arrayStadiums = new Array<Stadium>();
		for(Iterator<Item> i= this.getGame().getItems().iterator(); i.hasNext();){
			Item item = i.next();
			if(item instanceof BallItem)
				if(!item.isLocked()) {
					arrayBalls.add((BallItem) item);
				}
		}
		this.matchSettings.setBall(arrayBalls.first());

		for(Item item: this.getGame().getItems()){
			if(item instanceof Stadium)
				arrayStadiums.add((Stadium) item);
		}
		this.matchSettings.setStadium(arrayStadiums.first());
	}
	
	@Override
	public void show() {
		super.show();
	}

	@Override
	public void initScrollTable() {
		//Creamos botones
		nextGoals = new ImageButton(getSkin(),"next");
		beforeGoals = new ImageButton(getSkin(),"before");

		nextBall = new ImageButton(getSkin(),"next");
		beforeBall = new ImageButton(getSkin(),"before");

		nextStadium = new ImageButton(getSkin(),"next");
		beforeStadium = new ImageButton(getSkin(),"before");

		goals = new SettingValueCard(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT,new NinePatchDrawable(getSkin().getPatch("grey_panel")), Text.GOALS ,3 , new Image(getSkin().getDrawable("ic_score")));
		balls = new SettingImageCard(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT,new NinePatchDrawable(getSkin().getPatch("grey_panel")),arrayBalls.first().getMainImage(), Text.BALL, new Image(getSkin().getDrawable("ic_balls")));
		stadiums = new SettingImageCard(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT,new NinePatchDrawable(getSkin().getPatch("grey_panel")),arrayStadiums.first().getMainImage(), Text.STADIUM, new Image(getSkin().getDrawable("ic_stadiums")));

		tableScroll.add(beforeGoals).padLeft(Constants.CARD_PAD*2).padRight(Constants.PAD);
		tableScroll.add(goals).size(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT);
		tableScroll.add(nextGoals).padRight(Constants.CARD_PAD).padLeft(Constants.PAD);

		tableScroll.add(beforeBall).padRight(Constants.PAD);
		tableScroll.add(balls).size(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT);
		tableScroll.add(nextBall).padRight(Constants.CARD_PAD).padLeft(Constants.PAD);

		tableScroll.add(beforeStadium).padRight(Constants.PAD);
		tableScroll.add(stadiums).size(Constants.CARD_WIDTH, Constants.CARD_SETTING_HEIGHT);
		tableScroll.add(nextStadium).padRight(Constants.CARD_PAD*2).padLeft(Constants.PAD);

		//listener
		beforeGoals.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(goals.getIntegerValue()<=7 && goals.getIntegerValue()>1){
					goals.setValue(goals.getIntegerValue()-1);
					matchSettings.setGolesMax(goals.getIntegerValue());
				}
			}
		});

		nextGoals.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(goals.getIntegerValue()<7 && goals.getIntegerValue()>=1){
					goals.setValue(goals.getIntegerValue()+1);
					matchSettings.setGolesMax(goals.getIntegerValue());
				}
			}
		});


		beforeBall.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<arrayBalls.size;i++){
					if(arrayBalls.get(i).getId()==matchSettings.getBall().getId()){
						if(i==0){
							matchSettings.setBall(arrayBalls.get(arrayBalls.size-1));
							balls.setSettingImage(new Image(arrayBalls.get(arrayBalls.size-1).getTexture()));
							break;
						}else{
							matchSettings.setBall(arrayBalls.get(i-1));
							balls.setSettingImage(new Image(arrayBalls.get(i-1).getTexture()));
							break;
						}
					}
				}
			}
		});

		nextBall.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<arrayBalls.size;i++){
					if(arrayBalls.get(i).getId()==matchSettings.getBall().getId()){
						if(i< (arrayBalls.size-1)){
							matchSettings.setBall(arrayBalls.get(i+1));
							balls.setSettingImage(new Image(arrayBalls.get(i+1).getTexture()));
							break;
						}else{
							matchSettings.setBall(arrayBalls.get(0));
							balls.setSettingImage(new Image(arrayBalls.get(0).getTexture()));
							break;
						}
					}
				}
			}
		});
	}

	@Override
	public void initFooterTable() {
		prevScreen = new ImageButton(getSkin(),"back");
		nextScreen = new TextButton("Empezar Partido",getSkin());
		Table tableFooterLeft = new Table();
		Table tableFooterRight = new Table();

		tableFooterLeft.add(prevScreen).left().padLeft(15f).expand();
		tableFooterRight.add(nextScreen).right().padRight(15f).expand();

		tableFooter.setWidth(getViewportWidth());
		tableFooter.add(tableFooterLeft).padBottom(15f).left().width((getViewportWidth()/2));;
		tableFooter.add(tableFooterRight).padBottom(15f).right().width((getViewportWidth()/2));;


		nextScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getBall().isLocked()) {
					if(getGame().getPreferences().getBoolean("sound"))
						Assets.hitWall.play();
				} else {
					getGame().setScreen(new SelectFormationScreen(getGame(), matchSettings));
				}
			}
		});

		prevScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new SelectKitsScreen(getGame(), matchSettings));
			}
		});
	}


}
