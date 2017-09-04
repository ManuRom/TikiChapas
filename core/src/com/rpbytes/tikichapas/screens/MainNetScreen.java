package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.utils.Constants;

/**
 * Created by manoleichon on 4/10/16.
 */
public class MainNetScreen extends BaseMenuScreen {
    public TextButton myMatches, newMatch, tables;

    public MainNetScreen(TikiChapasGame game){
        setGame(game);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initScrollTable() {
        myMatches = new TextButton("Mis partidos",getSkin());
        newMatch = new TextButton("Nuevo partido",getSkin());
        tables = new TextButton("Clasificación", getSkin());
        myMatches.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MyMatchesScreen(getGame()));
            }
        });

        newMatch.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new NewOnlineMatchScreen(getGame()));
            }
        });

        tables.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new RankingScreen(getGame()));
            }
        });

        tableScroll.add(myMatches).pad(20);
        tableScroll.add(newMatch).pad(20);
        tableScroll.add(tables).pad(20);
    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(),"back");

        Table tableFooterLeft = new Table();
        Table tableFooterRight = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(Constants.HEADER_BUTTONS_PAD).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(Constants.HEADER_BUTTONS_PAD).left().width((getViewportWidth()/2));
        tableFooter.add(tableFooterRight).padBottom(Constants.HEADER_BUTTONS_PAD).right().width((getViewportWidth()/2));


        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
            }
        });
    }
}
