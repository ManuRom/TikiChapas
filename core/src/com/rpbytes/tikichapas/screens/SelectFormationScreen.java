package com.rpbytes.tikichapas.screens;

import java.util.ArrayList;
import java.util.Iterator;


import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.entities.Cap;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.listener.BeforeFormationListener;
import com.rpbytes.tikichapas.listener.NextFormationListener;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

public class SelectFormationScreen extends BaseMenuScreen {

	public MatchSettings matchSettings;
	public ImageButton beforeHome, nextHome, beforeAway, nextAway;
	public TextButton nextScreen;
	
	public Array<Formation> homeFormations, awayFormations;
	public Label labelHome;
	public Label labelAway;
	public Table homeFormationWrapper, awayFormationWrapper;
	
	public World world;
	public ArrayList<Cap> tapones;

	public SelectFormationScreen(TikiChapasGame game, MatchSettings matchSettings){
		setGame(game);
		this.matchSettings = matchSettings;

		homeFormations = new Array<Formation>();
		awayFormations = new Array<Formation>();

		for(Iterator<Item> i= this.getGame().getItems().iterator(); i.hasNext();){
			Item item = i.next();
			if(item instanceof Formation){
				if(!item.isLocked()) {
					Formation home = new Formation(item.getId(), item.getName(), item.getPrice(), item.isLocked(), ((Formation) item).duplicateFormationData());
					Formation away = new Formation(item.getId(), item.getName(), item.getPrice(), item.isLocked(), ((Formation) item).duplicateFormationData());
					away.setFormationToAway();
					homeFormations.add(home);
					awayFormations.add(away);
					System.out.println("Home añadida:" + home.getName());
					System.out.println("Away añadida:" + away.getName());
				}
			}
		}

		this.matchSettings.setHomeFormation(homeFormations.first());
		this.matchSettings.setAwayFormation(awayFormations.first());
	}

	public void show(){
		super.show();
	}

	@Override
	public void initScrollTable() {

		nextHome = new ImageButton(getSkin(),"next");
		beforeHome = new ImageButton(getSkin(),"before");

		nextAway = new ImageButton(getSkin(),"next");
		beforeAway = new ImageButton(getSkin(),"before");

		homeFormationWrapper = new Table();
		labelHome = new Label(matchSettings.getHomeFormation().getName(), getSkin(),"label");
		homeFormationWrapper.add(labelHome);
		homeFormationWrapper.row();
		homeFormationWrapper.add(matchSettings.getHomeFormation().getMainImage()).width(300).height(360);

		//getMainStage().addActor(matchSettings.getHomeFormation());

		awayFormationWrapper = new Table();
		labelAway = new Label(matchSettings.getAwayFormation().getName(), getSkin(),"label");
		awayFormationWrapper.add(labelAway);
		awayFormationWrapper.row();
		awayFormationWrapper.add(matchSettings.getAwayFormation().getMainImage()).width(300).height(360);

		tableScroll.add(beforeHome);
		tableScroll.add(homeFormationWrapper).width((Constants.PITCH_WIDTH/2)*Constants.METERS_TO_PIXELS).height(Constants.PITCH_HEIGHT*Constants.METERS_TO_PIXELS).center();
		beforeHome.addListener(new BeforeFormationListener(tableScroll.getCells().peek(),homeFormations,matchSettings,getSkin(),true));
		nextHome.addListener(new NextFormationListener(tableScroll.getCells().peek(),homeFormations,matchSettings,getSkin(),true));

		tableScroll.add(nextHome).padRight(Constants.PAD);
		tableScroll.add(beforeAway).padLeft(Constants.PAD);

		tableScroll.add(awayFormationWrapper).width((Constants.PITCH_WIDTH/2)*Constants.METERS_TO_PIXELS).height(Constants.PITCH_HEIGHT*Constants.METERS_TO_PIXELS).center();
		beforeAway.addListener(new BeforeFormationListener(tableScroll.getCells().peek(),awayFormations,matchSettings,getSkin(),false));
		nextAway.addListener(new NextFormationListener(tableScroll.getCells().peek(),awayFormations,matchSettings,getSkin(),false));

		tableScroll.add(nextAway);



	}

	@Override
	public void initFooterTable() {
		prevScreen = new ImageButton(getSkin(), SkinKeys.BACK);
		nextScreen = new TextButton(Text.BTN_NEXT,getSkin());
		Table tableFooterLeft = new Table();
		Table tableFooterRight = new Table();

		tableFooterLeft.add(prevScreen).left().padLeft(Constants.HEADER_BUTTONS_PAD).expand();
		tableFooterRight.add(nextScreen).right().padRight(Constants.HEADER_BUTTONS_PAD).expand();

		tableFooter.setWidth(getViewportWidth());
		tableFooter.add(tableFooterLeft).padBottom(Constants.HEADER_BUTTONS_PAD).left().width((getViewportWidth()/2));;
		tableFooter.add(tableFooterRight).padBottom(Constants.HEADER_BUTTONS_PAD).right().width((getViewportWidth()/2));;


		nextScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(matchSettings.getHomeFormation().isLocked() || matchSettings.getAwayFormation().isLocked()){
					if(getGame().getPreferences().getBoolean("sound"))
						Assets.hitWall.play();
				} else {
					getGame().setScreen(new MatchScreen(getGame(), matchSettings));
				}
			}
		});

		prevScreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new SettingMatchScreen(getGame(), matchSettings));
			}
		});
	}
}
