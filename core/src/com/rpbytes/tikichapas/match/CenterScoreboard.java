package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by manoleichon on 14/03/17.
 */
public class CenterScoreboard extends Table {

    private final static float WIDTH = 400f;
    private final static float HEIGHT = 400f;

    private final static float EMBLEM_SIZE = 100f;
    private final static float SCORE_SIZE = 70f;
    private final static float SIDE_PADDING = 10f;
    private final static float TOP_PADDING = 0f;

    private Label homeGoals, awayGoals;
    private Image homeEmblem, awayEmblem;

    public CenterScoreboard(int homeGoals, int awayGoals, TextureRegion homeEmblem, TextureRegion awayEmblem, Skin skin){
        super();

        /*//Background
        NinePatchDrawable background = new NinePatchDrawable(skin.getPatch("grey_panel"));
        setBackground(background);
        //EndBackground*/

        setWidth(WIDTH);
        setHeight(HEIGHT);

        this.homeGoals = new Label(Integer.toString(homeGoals),skin,"animation_goal");
        this.awayGoals = new Label(Integer.toString(awayGoals),skin,"animation_goal");

        Table homeGoalsWrapper = new Table();
        homeGoalsWrapper.setBackground(skin.getDrawable("scoreboard_goals"));
        homeGoalsWrapper.setSize(100,100);
        homeGoalsWrapper.add(this.homeGoals).center();

        Table awayGoalsWrapper = new Table();
        awayGoalsWrapper.setBackground(skin.getDrawable("scoreboard_goals"));
        awayGoalsWrapper.setSize(100,100);
        awayGoalsWrapper.add(this.awayGoals).center();

        this.homeEmblem = new Image(homeEmblem);
        this.awayEmblem = new Image(awayEmblem);

        this.homeEmblem.setSize(EMBLEM_SIZE,EMBLEM_SIZE);
        this.awayEmblem.setSize(EMBLEM_SIZE,EMBLEM_SIZE);

        add(this.homeEmblem).size(EMBLEM_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(this.awayEmblem).size(EMBLEM_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        row();
        add(homeGoalsWrapper).size(SCORE_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);
        add(awayGoalsWrapper).size(SCORE_SIZE).pad(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);

        center();
    }

    public void setHomeGoals(int homeGoals){
        this.homeGoals.setText(Integer.toString(homeGoals));
    }

    public void setAwayGoals(int awayGoals){
        this.awayGoals.setText(Integer.toString(awayGoals));
    }

    public Label getHomeGoals(){
        return this.homeGoals;
    }

    public Label getAwayGoals(){
        return this.awayGoals;
    }

}
