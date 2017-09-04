package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by manoleichon on 14/03/17.
 */
public class HudScoreboard extends Table {

    private final static float EMBLEM_SIZE = 70f;
    private final static float SCORE_SIZE = 70f;
    private final static float SIDE_PADDING = 10f;
    private final static float TOP_PADDING = 0f;
    private final static float WIDTH_NAME = 250f;
    private final static float HEIGHT_NAME = 60f;

    public Label homeGoals, awayGoals, homeName, awayName;

    public Image homeEmblem, awayEmblem;

    public HudScoreboard(int homeGoals, int awayGoals, String homeName, String awayName, TextureRegion homeEmblem, TextureRegion awayEmblem, Skin skin){
        super();

        this.homeGoals = new Label(Integer.toString(homeGoals),skin,"scoreboard_goal");
        this.awayGoals = new Label(Integer.toString(awayGoals),skin,"scoreboard_goal");
        this.homeName  = new Label(homeName, skin, "scoreboard_name");
        this.awayName = new Label(awayName, skin, "scoreboard_name");
        this.homeEmblem = new Image(homeEmblem);
        this.awayEmblem = new Image(awayEmblem);

        Table homeGoalsWrapper = new Table();
        homeGoalsWrapper.setBackground(skin.getDrawable("scoreboard_goals"));
        homeGoalsWrapper.setSize(SCORE_SIZE,SCORE_SIZE);
        homeGoalsWrapper.add(this.homeGoals).center();

        Table awayGoalsWrapper = new Table();
        awayGoalsWrapper.setBackground(skin.getDrawable("scoreboard_goals"));
        awayGoalsWrapper.setSize(SCORE_SIZE,SCORE_SIZE);
        awayGoalsWrapper.add(this.awayGoals).center();

        Table homeNameWrapper = new Table();
        homeNameWrapper.setBackground(skin.getDrawable("scoreboard_name"));
        homeNameWrapper.setSize(WIDTH_NAME,HEIGHT_NAME);
        homeNameWrapper.add(this.homeName).center();

        Table awayNameWrapper = new Table();
        awayNameWrapper.setBackground(skin.getDrawable("scoreboard_name"));
        awayNameWrapper.setSize(WIDTH_NAME,HEIGHT_NAME);
        awayNameWrapper.add(this.awayName).center();

        add(this.homeEmblem).size(EMBLEM_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(homeNameWrapper).height(HEIGHT_NAME).width(WIDTH_NAME).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(homeGoalsWrapper).size(SCORE_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);

        add(awayGoalsWrapper).size(SCORE_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(awayNameWrapper).height(HEIGHT_NAME).width(WIDTH_NAME).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(this.awayEmblem).size(EMBLEM_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
    }


    public Label getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals.setText(Integer.toString(homeGoals));
    }

    public Label getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals.setText(Integer.toString(awayGoals));
    }

    public void setHomeName(String name){
        this.homeName.setText(name);
    }

    public void setAwayName(String name){
        this.awayName.setText(name);
    }


}
