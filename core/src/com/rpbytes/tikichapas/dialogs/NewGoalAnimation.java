package com.rpbytes.tikichapas.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.match.CenterScoreboard;

/**
 * Created by manoleichon on 14/03/17.
 */
public class NewGoalAnimation extends Table{

    private CenterScoreboard centerScoreboard;

    private Label goalLabel;

    public NewGoalAnimation(Skin skin, CenterScoreboard centerScoreboard){
        super();
        setSkin(skin);
        this.centerScoreboard = centerScoreboard;
        Table scoreboardWrapper = new Table();
        scoreboardWrapper.setBackground(new NinePatchDrawable(getSkin().getPatch("grey_panel")));
        scoreboardWrapper.setSize(500,450);
        scoreboardWrapper.add(this.centerScoreboard).center();

        goalLabel = new Label("GOAL!!!",getSkin(),"animation_goal");

        add(scoreboardWrapper).center();
        row();
        add(goalLabel).center();
        setVisible(false);
    }

    public CenterScoreboard getCenterScoreboard() {
        return centerScoreboard;
    }

    public void setCenterScoreboard(CenterScoreboard centerScoreboard) {
        this.centerScoreboard = centerScoreboard;
    }

    public Label getGoalLabel() {
        return goalLabel;
    }

}
