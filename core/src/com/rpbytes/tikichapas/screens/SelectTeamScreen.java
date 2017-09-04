package com.rpbytes.tikichapas.screens;

import java.util.Iterator;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.match.MatchSettings;

public class SelectTeamScreen extends BaseMenuScreen {
	
	public MatchSettings matchSettings;
	

	
	public Label labelHome, labelAway;
	public ImageButton nextHome, beforeHome, nextAway, beforeAway;
	public ImageButton nextHomePlayer, nextAwayPlayer, beforeHomePlayer, beforeAwayPlayer;
	public ImageButton playerOne, playerTwo, playerHomeCPU, playerAwayCPU;

	public Array<Team> equiposHome, equiposAway;
	
	public SelectTeamScreen (TikiChapasGame game){
		setGame(game);
	}

	public SelectTeamScreen (TikiChapasGame game, MatchSettings matchSettings){
		setGame(game);
		this.matchSettings = matchSettings;
	}


	@Override
	public void show() {
		//Inicializamos los array de equipos
		equiposHome = new Array<Team>();
		equiposAway = new Array<Team>();
		for(Iterator<Item> i = getGame().getItems().iterator(); i.hasNext();){
			Item item = i.next();
			if(item instanceof Team){
				Team home = new Team(item.getId(),item.getName(),item.getPrice(),item.isLocked());
				Team away = new Team(item.getId(),item.getName(),item.getPrice(),item.isLocked());
				equiposHome.add(home);
				equiposAway.add(away);
			}
		}

		if(matchSettings == null)
			matchSettings = new MatchSettings(equiposHome.get(0), equiposAway.get(0));

		super.show();
	}

	@Override
	public void initScrollTable() {
		//Creamos Label
		labelHome = new Label("Local", getSkin(), "label");
		labelAway = new Label("Visitante", getSkin(), "label");

		//Creamos botones
		nextHome = new ImageButton(getSkin(),"next");
		beforeHome = new ImageButton(getSkin(),"before");

		nextAway = new ImageButton(getSkin(),"next");
		beforeAway = new ImageButton(getSkin(),"before");



		playerOne = new ImageButton(getSkin(),"player_one");
		playerTwo = new ImageButton(getSkin(),"player_two");
		playerHomeCPU = new ImageButton(getSkin(),"player_cpu");
		playerAwayCPU = new ImageButton(getSkin(),"player_cpu");

		beforeAwayPlayer = new ImageButton(getSkin(),"before_mini");
		beforeHomePlayer = new ImageButton(getSkin(),"before_mini");

		nextAwayPlayer = new ImageButton(getSkin(),"next_mini");
		nextHomePlayer = new ImageButton(getSkin(),"next_mini");




		//Añadimos a la scoreBoard los botones y label creados
		tableScroll.add(labelHome).colspan(3).center();
		tableScroll.add(labelAway).colspan(3).center();
		tableScroll.row();

		tableScroll.add(beforeHomePlayer).pad(30f).right();
		tableScroll.add(playerOne).pad(30f);
		tableScroll.add(nextHomePlayer).left().pad(30f);

		tableScroll.add(beforeAwayPlayer).right().pad(30f);
		tableScroll.add(playerTwo).pad(30f);
		tableScroll.add(nextAwayPlayer).left().pad(30f);

		tableScroll.row();

		tableScroll.add(beforeHome).right().pad(30f);
		tableScroll.add(matchSettings.getTeamLocal()).pad(30f).size(200f);
		tableScroll.add(nextHome).left().pad(30f);

		tableScroll.add(beforeAway).right().pad(30f);
		tableScroll.add(matchSettings.getTeamVisitante()).pad(30f).size(200f);
		tableScroll.add(nextAway).left().pad(30f);


		ClickListener playerHomeSelectedListener = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getJugadorLocal().equals(MatchSettings.tipoJugador.HUMANO)){
					matchSettings.setJugadorLocal(MatchSettings.tipoJugador.IA);
					tableScroll.getCells().get(3).setActor(playerHomeCPU);
				}else{
					matchSettings.setJugadorLocal(MatchSettings.tipoJugador.HUMANO);
					tableScroll.getCells().get(3).setActor(playerOne);
				}

			}
		};

		ClickListener playerAwaySelectedListener = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getJugadorVisitante().equals(MatchSettings.tipoJugador.HUMANO)){
					matchSettings.setJugadorVisitante(MatchSettings.tipoJugador.IA);
					tableScroll.getCells().get(6).setActor(playerAwayCPU);
				}else{
					matchSettings.setJugadorVisitante(MatchSettings.tipoJugador.HUMANO);
					tableScroll.getCells().get(6).setActor(playerTwo);
				}
			}
		};

		beforeHomePlayer.addListener(playerHomeSelectedListener);
		nextHomePlayer.addListener(playerHomeSelectedListener);

		beforeAwayPlayer.addListener(playerAwaySelectedListener);
		nextAwayPlayer.addListener(playerAwaySelectedListener);



		nextHome.addListener(new ClickListener(){
			@SuppressWarnings("unchecked")
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<equiposHome.size;i++){
					if(equiposHome.get(i).getId()==matchSettings.getTeamLocal().getId()){
						if(i< (equiposHome.size-1)){
							matchSettings.setTeamLocal(equiposHome.get(i+1));
							tableScroll.getCells().get(9).setActor(equiposHome.get(i+1));
							break;
						}else{
							matchSettings.setTeamLocal(equiposHome.get(0));
							tableScroll.getCells().get(9).setActor(matchSettings.getTeamLocal());
							break;
						}
					}
				}
			};
		});

		beforeHome.addListener(new ClickListener(){
			@SuppressWarnings("unchecked")
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<equiposHome.size;i++){
					if(equiposHome.get(i).getId()==matchSettings.getTeamLocal().getId()){
						if(i==0){
							matchSettings.setTeamLocal(equiposHome.get(equiposHome.size-1));
							tableScroll.getCells().get(9).setActor(matchSettings.getTeamLocal());
							break;
						}else{
							matchSettings.setTeamLocal(equiposHome.get(i-1));
							tableScroll.getCells().get(9).setActor(matchSettings.getTeamLocal());
							break;
						}
					}
				}
			};
		});

		nextAway.addListener(new ClickListener(){
			@SuppressWarnings("unchecked")
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<equiposAway.size;i++){
					if(equiposAway.get(i).getId()==matchSettings.getTeamVisitante().getId()){
						if(i< (equiposAway.size-1)){
							matchSettings.setTeamVisitante(equiposAway.get(i+1));
							tableScroll.getCells().get(12).setActor(matchSettings.getTeamVisitante());
							break;
						}else{
							matchSettings.setTeamVisitante(equiposAway.get(0));
							tableScroll.getCells().get(12).setActor(matchSettings.getTeamVisitante());
							break;
						}
					}
				}
			};
		});

		beforeAway.addListener(new ClickListener(){
			@SuppressWarnings("unchecked")
			public void clicked(InputEvent event, float x, float y) {
				for(int i=0; i<equiposAway.size;i++){
					if(equiposAway.get(i).getId()==matchSettings.getTeamVisitante().getId()){
						if(i==0){
							matchSettings.setTeamVisitante(equiposAway.get(equiposAway.size-1));
							tableScroll.getCells().get(12).setActor(matchSettings.getTeamVisitante());
							break;
						}else{
							matchSettings.setTeamVisitante(equiposAway.get(i-1));
							tableScroll.getCells().get(12).setActor(matchSettings.getTeamVisitante());
							break;
						}
					}
				}
			};
		});
	}

	public void initFooterTable(){
		prevScreen = new ImageButton(getSkin(),"back");
		nextScreen = new TextButton("Siguiente",getSkin());
		Table tableFooterLeft = new Table();
		Table tableFooterRight = new Table();

		tableFooterLeft.add(prevScreen).left().padLeft(Constants.HEADER_BUTTONS_PAD).expand();
		tableFooterRight.add(nextScreen).right().padRight(Constants.HEADER_BUTTONS_PAD).expand();

		tableFooter.setWidth(getViewportWidth());
		tableFooter.add(tableFooterLeft).padBottom(Constants.HEADER_BUTTONS_PAD).left().width((getViewportWidth()/2));
		tableFooter.add(tableFooterRight).padBottom(Constants.HEADER_BUTTONS_PAD).right().width((getViewportWidth()/2));

		nextScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!lockedItems())
					getGame().setScreen(new SelectKitsScreen(getGame(), matchSettings));
				else
					System.out.println("ITEM BLOQUEADO");
			}
		});

		prevScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new MainMenuScreen(getGame()));
			}
		});
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() { super.pause(); }

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	private boolean lockedItems(){
		if(matchSettings.getTeamLocal().isLocked())
			return true;
		else{
			if(matchSettings.getTeamVisitante().isLocked())
				return true;
			else
				return false;
		}
	}
}
