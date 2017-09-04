package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.cards.IconAndButtonCard;
import com.rpbytes.tikichapas.items.Stadium;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.items.BallItem;
import com.rpbytes.tikichapas.items.Formation;

/**
 * Created by manoleichon on 13/07/16.
 */
public class StoreScreen extends BaseMenuScreen {

    IconAndButtonCard balls, formations, stadiums, teams;


    public StoreScreen(TikiChapasGame game){
        setGame(game);
    }

    public void show(){
        super.show();
    }


    @Override
    public void initScrollTable() {

        balls = new IconAndButtonCard(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")), new Image(getSkin().getDrawable("ic_balls")), new TextButton("Balones",getSkin()));
        formations = new IconAndButtonCard(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")), new Image(getSkin().getDrawable("ic_formations")), new TextButton("Formaciones",getSkin()));
        stadiums = new IconAndButtonCard(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")), new Image(getSkin().getDrawable("ic_stadiums")), new TextButton("Estadios",getSkin()));
        teams = new IconAndButtonCard(Constants.CARD_WIDTH, Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")), new Image(getSkin().getDrawable("ic_teams")), new TextButton("Equipos",getSkin()));

        tableScroll.add(balls).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD*2).padRight(Constants.CARD_PAD);
        tableScroll.add(formations).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD);
        tableScroll.add(stadiums).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD);
        tableScroll.add(teams).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD*2);



        balls.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Balls Screen");
                getGame().setScreen(new StoreTypeScreen(getGame(), BallItem.class));
            }
        });

        formations.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Formations Screen");
                getGame().setScreen(new StoreTypeScreen(getGame(), Formation.class));

            }
        });

        teams.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Teams Screen");
                getGame().setScreen(new StoreTypeScreen(getGame(), Team.class));
            }
        });

        stadiums.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Stadiums Screen");
                getGame().setScreen(new StoreTypeScreen(getGame(), Stadium.class));
            }
        });

    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(),"back");
        Table tableFooterLeft = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(15f).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(15f).width((getViewportWidth()));

        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
            }
        });
    }
}
