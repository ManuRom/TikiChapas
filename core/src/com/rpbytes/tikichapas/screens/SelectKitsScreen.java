package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.match.MatchSettings;

public class SelectKitsScreen extends BaseMenuScreen {
	
	public Image home, away;
	
	public MatchSettings matchSettings;

	public Label labelHome, labelAway;
	public ImageButton nextHome, beforeHome, nextAway, beforeAway;

	public SelectKitsScreen(TikiChapasGame game, MatchSettings settings){
		setGame(game);
		this.matchSettings = settings;
		home = new Image(matchSettings.getTeamLocal().getTextureHome());
		away = new Image(matchSettings.getTeamVisitante().getTextureHome());
	}

	public void show() {
		super.show();
	}

	@Override
	public void initScrollTable() {
		//Creamos Label
		labelHome = new Label("Local", getSkin(),"title");
		labelAway = new Label("Visitante", getSkin(),"title");

		//Creamos botones
		nextHome = new ImageButton(getSkin(),"next");
		beforeHome = new ImageButton(getSkin(),"before");

		nextAway = new ImageButton(getSkin(),"next");
		beforeAway = new ImageButton(getSkin(),"before");

		//nextScreen = new TextButton("Siguiente",getSkin());

		//añadimos a la scoreBoard
		tableScroll.add(labelHome).colspan(3); //0
		tableScroll.add(labelAway).colspan(3); //1
		tableScroll.row();
		tableScroll.add(beforeHome).padRight(20); //2
		tableScroll.add(home).size(150f); //3
		tableScroll.add(nextHome).padLeft(20).padRight(20); //4
		tableScroll.add(beforeAway).padRight(20).padLeft(20); //5
		tableScroll.add(away).size(150f); //6
		tableScroll.add(nextAway).padLeft(20); //7
		//tableScroll.row();
		//tableFooter.add(nextScreen).colspan(6); //8

		//listener de los botones
		ClickListener homeListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getEquipacionLocal() == MatchSettings.tipoCamiseta.HOME){
					matchSettings.setEquipacionLocal(MatchSettings.tipoCamiseta.AWAY);
					home = new Image(matchSettings.getTeamLocal().getTextureVisitante());
					tableScroll.getCells().get(3).setActor(home);
				}else{
					matchSettings.setEquipacionLocal(MatchSettings.tipoCamiseta.HOME);
					home = new Image(matchSettings.getTeamLocal().getTextureHome());
					tableScroll.getCells().get(3).setActor(home);
				}
			}
		};

		ClickListener awayListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getEquipacionVisitante() == MatchSettings.tipoCamiseta.HOME){
					matchSettings.setEquipacionVisitante(MatchSettings.tipoCamiseta.AWAY);
					home = new Image(matchSettings.getTeamVisitante().getTextureVisitante());
					tableScroll.getCells().get(6).setActor(home);
				}else{
					matchSettings.setEquipacionVisitante(MatchSettings.tipoCamiseta.HOME);
					home = new Image(matchSettings.getTeamVisitante().getTextureHome());
					tableScroll.getCells().get(6).setActor(home);
				}
			}
		};

		nextHome.addListener(homeListener);
		beforeHome.addListener(homeListener);

		nextAway.addListener(awayListener);
		beforeAway.addListener(awayListener);
	}

	public void initFooterTable(){
		prevScreen = new ImageButton(getSkin(),"back");
		nextScreen = new TextButton("Siguiente",getSkin());
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
					getGame().setScreen(new SettingMatchScreen(getGame(), matchSettings));
			}
		});

		prevScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new SelectTeamScreen(getGame(), matchSettings));
			}
		});
	}
}
