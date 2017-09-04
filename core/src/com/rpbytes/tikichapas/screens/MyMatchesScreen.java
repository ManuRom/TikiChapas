package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.match.CenterScoreboard;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by manoleichon on 6/09/16.
 */
public class MyMatchesScreen extends BaseMenuScreen {

    public MyMatchesScreen(TikiChapasGame game){
        setGame(game);
    }

    public void show(){
        super.show();
        sendGetMatchesRequest();
    }

    @Override
    public void initScrollTable() {

    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(), SkinKeys.BACK);
        TextButton updateMatches = new TextButton(Text.BTN_UPDATE,getSkin());
        Table tableFooterLeft = new Table();
        Table tableFooterRight = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(Constants.HEADER_BUTTONS_PAD).expand();
        tableFooterRight.add(updateMatches).right().padRight(Constants.HEADER_BUTTONS_PAD).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(Constants.HEADER_BUTTONS_PAD).left().width((getViewportWidth()/2));
        tableFooter.add(tableFooterRight).padBottom(Constants.HEADER_BUTTONS_PAD).right().width((getViewportWidth()/2));

        updateMatches.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MyMatchesScreen(getGame()));
            }
        });

        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainNetScreen(getGame()));
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void sendGetMatchesRequest(){
        Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.GET);
        request.setUrl(Url.BASE_URL+Url.USER_MATCHES_PATH);
        request.setHeader("Authorization","Bearer "+Gdx.app.getPreferences("data").getString("token"));

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                JSONObject response = new JSONObject(httpResponse.getResultAsString());
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    JSONArray matches = response.getJSONArray("matches");
                    for(int i = 0; i < matches.length() ; i++){
                        final JSONObject match = new JSONObject(matches.get(i).toString());
                        Table card = new Table(getSkin());

                        TextureRegion homeTexture, awayTexture;
                        homeTexture = Assets.manager.get("pack/flags.pack", TextureAtlas.class).findRegion("Reds");
                        awayTexture = Assets.manager.get("pack/flags.pack", TextureAtlas.class).findRegion("Blues");
                        for(int j = 0; j < getGame().getItems().size; j++) {
                            Item item = getGame().getItems().get(j);
                            if(item instanceof Team) {
                                if (item.getId() == match.getInt("team_home_id"))
                                    homeTexture = ((Team) item).getEmblem();
                                if (!match.get("team_away_id").equals(null)) {
                                    if (item.getId() == match.getInt("team_away_id"))
                                        awayTexture = ((Team) item).getEmblem();
                                }
                            }
                        }
                        CenterScoreboard scoreboard = new CenterScoreboard(match.getInt("goals_home"),match.getInt("goals_away"),homeTexture,awayTexture,getSkin());
                        NinePatchDrawable backgroundTable = new NinePatchDrawable(getSkin().getPatch(SkinKeys.GREY_PANEL));
                        card.background(backgroundTable);

                        if (!match.get("username").equals(null)) {
                            Label username = new Label(match.getString("username"), getSkin(), SkinKeys.LABEL);
                            card.add(username).center();
                        }
                        TextButton play = new TextButton(Text.BTN_CONTINUE, getSkin());
                        card.row();
                        card.add(scoreboard).center();
                        card.row();
                        if(match.get("user_away_id").equals(null)){
                            card.add(new Label(Text.WAITING_RIVAL, getSkin(), SkinKeys.DEFAULT)).center();
                        }else{
                            if(match.getInt("goals_home")==3 || match.getInt("goals_away")==3){
                                TextButton delete = new TextButton(Text.BTN_DELETE, getSkin());
                                card.getCells().first().colspan(2);
                                card.getCells().peek().colspan(2);
                                card.add(delete);
                                card.add(play);

                                play.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        Gdx.app.postRunnable(new Runnable() {
                                            @Override
                                            public void run() {
                                                getGame().setScreen(new LoadingMatchScreen(getGame(), match.getInt("id")));

                                            }
                                        });
                                    }
                                });
                                delete.addListener(new ClickListener(){
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        sendDeleteMatchRequest(match.getInt("id"));
                                    }
                                });
                            }else {
                                if (((match.getInt("turn_home") == 1) && (match.getInt("user_home_id") == getGame().getUser().getId())) ||
                                        ((match.getInt("turn_home") == 0) && (match.getInt("user_away_id") == getGame().getUser().getId()))) {
                                    card.add(play).center();
                                    play.addListener(new ClickListener() {
                                        @Override
                                        public void clicked(InputEvent event, float x, float y) {
                                            Gdx.app.postRunnable(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getGame().setScreen(new LoadingMatchScreen(getGame(), match.getInt("id")));

                                                }
                                            });
                                        }
                                    });
                                } else {
                                    card.add(new Label(Text.WAITING_MOVEMENT, getSkin(), SkinKeys.LABEL));
                                }
                            }
                        }
                        tableScroll.add(card).size(450,400).pad(0,20,0,20);
                    }
                }else{
                    Gdx.app.log("Get Matches Status Code", String.valueOf(httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Get Matches failed", t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }

    private void sendDeleteMatchRequest(int matchId){
        Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.DELETE);
        request.setUrl(Url.BASE_URL+Url.MATCH_PATH+matchId);
        request.setHeader("Authorization","Bearer "+Gdx.app.getPreferences("data").getString("token"));

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("DELETE MATCH STATUS", Integer.toString(httpResponse.getStatus().getStatusCode()));

                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            getGame().setScreen(new MyMatchesScreen(getGame()));
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

}
