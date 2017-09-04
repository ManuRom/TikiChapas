package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.net.Movement;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.items.Team;
import com.rpbytes.tikichapas.listener.BeforeItemListener;
import com.rpbytes.tikichapas.listener.NextItemListener;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.listener.SwapKitListener;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.utils.Carousel;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by manoleichon on 4/10/16.
 */
public class NewOnlineMatchScreen extends BaseMenuScreen {

    public ImageButton nextFormation, beforeFormation, nextTeam, beforeTeam, nextKit, beforeKit;
    //public Label formation;
    public Image kit;
    public Team team;
    public Array<Item> teams;
    public Array<Formation> formations;
    public TextButton newMatch;
    public Dialog loading;
    public SwapKitListener kitListener;
    public Carousel formationCarousel;
    public Formation formation;


    public NewOnlineMatchScreen(TikiChapasGame game){
        setGame(game);
        teams = new Array<Item>();
        formations = new Array<Formation>();
    }

    @Override
    public void show() {
        updateTeams();
        updateFormations();
        super.show();
        loading = new Dialog(Text.LOADING_TITLE,getSkin());
    }

    @Override
    public void initScrollTable() {
        nextFormation = new ImageButton(getSkin(),SkinKeys.NEXT);
        beforeFormation = new ImageButton(getSkin(),SkinKeys.BEFORE);
        nextTeam = new ImageButton(getSkin(), SkinKeys.NEXT);
        beforeTeam = new ImageButton(getSkin(), SkinKeys.BEFORE);
        nextKit = new ImageButton(getSkin(), SkinKeys.NEXT_MINI);
        beforeKit = new ImageButton(getSkin(), SkinKeys.BEFORE_MINI);

        //formation = new Label(formations.first().getName(),getSkin(),SkinKeys.LABEL);
        //formation.setName("formation");
        team = (Team) teams.first();

        kit = new Image(((Team) team).getTextureHome());
        kit.setName("kit");

        Table teamWrapper = new Table();
        teamWrapper.add(beforeTeam);
        teamWrapper.add(team).size(150);
        teamWrapper.add(nextTeam);
        teamWrapper.row();
        teamWrapper.add(beforeKit);
        teamWrapper.add(kit).pad(10);
        teamWrapper.add(nextKit);
        tableScroll.add(teamWrapper).padRight(30);
        /*tableScroll.add(beforeTeam);
        tableScroll.add(team).size(150);
        tableScroll.add(nextTeam);
        tableScroll.add(beforeKit);
        tableScroll.add(kit).pad(10);
        tableScroll.add(nextKit);
        tableScroll.row();*/

        Table formationWrapper = new Table();
        final Label formationName = new Label(formations.first().getName(), getSkin(),"label");
        formationWrapper.add(formationName);
        formationWrapper.row();
        formationWrapper.add(formations.first().getMainImage()).width(300).height(360);

        tableScroll.add(beforeFormation).padLeft(30);
        tableScroll.add(formationWrapper).width((Constants.PITCH_WIDTH/2)*Constants.METERS_TO_PIXELS).height(Constants.PITCH_HEIGHT*Constants.METERS_TO_PIXELS).center();
        formationCarousel = new Carousel<Formation>(formations, tableScroll.getCells().peek(),getSkin());
        tableScroll.add(nextFormation);

        beforeFormation.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                formationCarousel.before();
                formation = (Formation) formationCarousel.getCurrentActor();

            }
        });
        nextFormation.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                formationCarousel.next();
                formation = (Formation) formationCarousel.getCurrentActor();
            }
        });
        /*nextFormation.addListener(new NextListener(formations,formation,tableScroll));
        beforeFormation.addListener(new BeforeLabelListener(formations,formation,tableScroll));*/

        nextTeam.addListener(new NextItemListener(teams,teamWrapper));
        beforeTeam.addListener(new BeforeItemListener(teams,teamWrapper));

        kitListener = new SwapKitListener(tableScroll);

        nextKit.addListener(kitListener);
        beforeKit.addListener(kitListener);

    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(), SkinKeys.BACK);
        newMatch = new TextButton(Text.BTN_NEW_MATCH,getSkin());
        Table tableFooterLeft = new Table();
        Table tableFooterRight = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(15f).expand();
        tableFooterRight.add(newMatch).right().padRight(15f).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(15f).left().width((getViewportWidth()/2));
        tableFooter.add(tableFooterRight).padBottom(15f).right().width((getViewportWidth()/2));


        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainNetScreen(getGame()));
            }
        });

        newMatch.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendPostMatchRequest();
            }
        });
    }


    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void sendPostMatchRequest(){
        JSONObject content = new JSONObject();
        content.put("team_id", ((Team) tableScroll.findActor("team")).getId());
        content.put("kit_id",kitListener.getKitId());
        content.put("formation_id", formation.getId());

        Gdx.app.log("json request", content.toString());
        Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.POST);
        request.setUrl(Url.BASE_URL+Url.FIND_MATCH_PATH);
        request.setContent(content.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final JSONObject response = new JSONObject(httpResponse.getResultAsString());
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_CREATED) {
                    if(response.getString("status").equals("waiting_oponent")){
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                getGame().setScreen(new MyMatchesScreen(getGame()));
                            }
                        });
                    }
                }
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    final MatchSettings settings = new MatchSettings(response,getGame());
                    final Array<Movement> movements = new Array<Movement>();

                    final User userHome = new User(response.getJSONObject("home_user"));
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            getGame().setScreen(new MatchScreen(getGame(),settings,response.getInt("goals_home"),response.getInt("goals_away"),movements,response.getInt("id"),response.getInt("state_turn"),response.getInt("turn_home"), userHome,getGame().getUser()));
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("New Match failed",t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });

    }


    private void updateTeams() {
        for (Iterator<Item> i = getGame().getItems().iterator(); i.hasNext(); ) {
            Item item = i.next();
            if (item instanceof Team) {
                if(!item.isLocked()) {
                    Team team = new Team(item.getId(), item.getName(), item.getPrice(), item.isLocked());
                    team.setName("team");
                    teams.add(team);
                }
            }
        }
    }

    private void updateFormations(){
        for(Iterator<Item> i = getGame().getItems().iterator(); i.hasNext();){
            Item item = i.next();
            if (item instanceof Formation){
                if(!item.isLocked()){
                    Formation formation = new Formation(item.getId(), item.getName(), item.getPrice(), item.isLocked(), ((Formation) item).getFormationData());
                    //formation.setName("formation");
                    formations.add(formation);
                    Gdx.app.log("Formation added",Integer.toString(formation.getId()));
                }
            }
        }
        formation = (Formation)formations.first();
    }

}
