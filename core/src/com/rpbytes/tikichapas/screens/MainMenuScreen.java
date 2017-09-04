package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.cards.IconAndButtonCard;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.math.Pair;
import com.rpbytes.tikichapas.items.BallItem;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.items.Stadium;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import java.util.Iterator;


/**
 * Created by manoleichon on 30/08/16.
 */
public class MainMenuScreen extends BaseMenuScreen {

    public IconAndButtonCard matchOffline, matchOnline;

    public MainMenuScreen(TikiChapasGame game) {
        setGame(game);
    }

    public void show() {
        super.show();
    }

    private void loadDefaultItems(){
        boolean charge=false;

        for(Iterator<Item> i = getGame().getItems().iterator();i.hasNext();){
            Item item = i.next();
            if(item.getId()<0){
                charge=true;
            }
        }
        if(!charge) {
            getGame().getItems().add(new Team(-1, "Blues", 0, false));
            getGame().getItems().add(new Team(-2, "Reds", 0, false));
            getGame().getItems().add(new BallItem(-3, "Original", 0, false));

            //Creamos la formation
            Array<Pair<Integer, Vector2>> formation = new Array<Pair<Integer, Vector2>>();
            formation.add(new Pair<Integer, Vector2>(1, new Vector2(0.2f, 0.6f)));
            formation.add(new Pair<Integer, Vector2>(1, new Vector2(0.4f, 0.6f)));
            formation.add(new Pair<Integer, Vector2>(1, new Vector2(0.6f, 0.2f)));
            formation.add(new Pair<Integer, Vector2>(1, new Vector2(0.6f, 1f)));
            formation.add(new Pair<Integer, Vector2>(1, new Vector2(0.8f, 0.6f)));
            getGame().getItems().add(new Formation(-4, "1-2-1", 0, false, formation));
            getGame().getItems().add(new Stadium(-5,Assets.cadizTerreno, Assets.cadizTribuna, Assets.cadizPreferencia, Assets.cadizFondoNorte, Assets.cadizFondoSur, Assets.cadizEsq1, Assets.cadizEsq2, Assets.cadizEsq3, Assets.cadizEsq4));
        } else {
            if(!getGame().isOffline())
                getUserRequest();
        }
    }



    @Override
    public void initScrollTable() {
        Assets.manager.get("music/musicMenu.mp3", Music.class).setLooping(true);
        Assets.manager.get("music/musicMenu.mp3", Music.class).setVolume(0.5f);
        if(getGame().getPreferences().getBoolean("music") == true)
            Assets.manager.get("music/musicMenu.mp3", Music.class).play();

        loadDefaultItems();

        matchOffline = new IconAndButtonCard(Constants.CARD_WIDTH,Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")),new Image(getSkin().getDrawable("ic_offline_match")),new TextButton(Text.BTN_OFFLINE_MATCH,getSkin()));

        if(!getGame().isOffline())
            matchOnline = new IconAndButtonCard(Constants.CARD_WIDTH,Constants.CARD_HEIGHT, new NinePatchDrawable(getSkin().getPatch("grey_panel")),new Image(getSkin().getDrawable("ic_online_match")),new TextButton(Text.BTN_ONLINE_MATCH,getSkin()));
        else
            matchOnline = new IconAndButtonCard(Constants.CARD_WIDTH,Constants.CARD_HEIGHT,new NinePatchDrawable(getSkin().getPatch(SkinKeys.GREY_PANEL)),new Image(getSkin().getDrawable("ic_user")),new TextButton(Text.BTN_LOGIN,getSkin()));


        tableScroll.add(matchOffline).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD*2).padRight(Constants.CARD_PAD);
        tableScroll.add(matchOnline).size(Constants.CARD_WIDTH,Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD);


        matchOffline.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new SelectTeamScreen(getGame()));

            }
        });


        matchOnline.getCategory().addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                if(!getGame().isOffline())
                    getGame().setScreen(new MainNetScreen(getGame()));
                else
                    getGame().setScreen(new LoginScreen(getGame()));
            }
        });

    }

    @Override
    public void initFooterTable() {}

}


