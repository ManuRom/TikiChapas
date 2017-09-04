package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by manoleichon on 28/04/17.
 */
public class RankingScreen extends BaseMenuScreen {

    public RankingScreen(TikiChapasGame game){
        setGame(game);
    }

    public void show(){
        super.show();
        getRankingRequest();
    }

    private void getRankingRequest() {
        Net.HttpRequest request = new Net.HttpRequest();
        String url = Url.BASE_URL+ Url.RANKING_URL;

        request.setMethod(Net.HttpMethods.GET);
        request.setUrl(url);
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Get Ranking Status Code", Integer.toString(httpResponse.getStatus().getStatusCode()));
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    //Gdx.app.log("Get Ranking Response",httpResponse.getResultAsString());
                    JSONObject response = new JSONObject(httpResponse.getResultAsString());
                    JSONArray usersJson = response.getJSONArray("users");

                    tableScroll.background(getSkin().getDrawable(SkinKeys.GREY_PANEL));

                    tableScroll.add(new Label(Text.POSITION, getSkin(), SkinKeys.LABEL)).left().padRight(Constants.PAD);
                    tableScroll.add(new Label(Text.PLAYER, getSkin(), SkinKeys.LABEL)).left().padRight(Constants.PAD);
                    tableScroll.add(new Label(Text.WINS, getSkin(), SkinKeys.LABEL)).left().padRight(Constants.PAD);
                    tableScroll.add(new Label(Text.LOOSES, getSkin(), SkinKeys.LABEL)).left().padRight(Constants.PAD);
                    tableScroll.row();

                    for(int i=0; i<usersJson.length(); i++){
                        JSONObject userJson = new JSONObject(usersJson.get(i).toString());
                        User user = new User(userJson);

                        String style;

                        if(user.getId() == getGame().getUser().getId())
                            style = SkinKeys.LABEL_ERROR;
                        else
                            style = SkinKeys.DEFAULT;

                        tableScroll.add(new Label(Integer.toString(i+1), getSkin(), style)).left().padRight(Constants.PAD);
                        tableScroll.add(new Label(user.getUsername(), getSkin(), style)).left().padRight(Constants.PAD);
                        tableScroll.add(new Label(Integer.toString(user.getWins()), getSkin(), style)).center().padRight(Constants.PAD);
                        tableScroll.add(new Label(Integer.toString(user.getLosses()), getSkin(), style)).center().padRight(Constants.PAD);
                        tableScroll.row();
                    }


                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Get Ranking", "Failed");
            }

            @Override
            public void cancelled() {

            }
        });
    }

    @Override
    public void initScrollTable() {

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
                getGame().setScreen(new MainNetScreen(getGame()));
            }
        });
    }
}
