package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by manoleichon on 14/03/17.
 */
public class Scoreboard {
    private int homeGoals, awayGoals;

    private HudScoreboard hudScoreboard;
    private CenterScoreboard centerScoreboard;

    public Scoreboard(int homeGoals, int awayGoals, String homeName, String awayName, TextureRegion homeEmblem, TextureRegion awayEmblem, Skin skin){
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;

        hudScoreboard = new HudScoreboard(homeGoals,awayGoals,homeName,awayName,homeEmblem,awayEmblem,skin);

        centerScoreboard = new CenterScoreboard(homeGoals,awayGoals,homeEmblem,awayEmblem,skin);

    }

    public void setHomeGoals(int homeGoals){
        this.homeGoals = homeGoals;
        this.hudScoreboard.setHomeGoals(homeGoals);
        this.centerScoreboard.setHomeGoals(homeGoals);
    }

    public void setAwayGoals(int awayGoals){
        this.awayGoals = awayGoals;
        this.hudScoreboard.setAwayGoals(awayGoals);
        this.centerScoreboard.setAwayGoals(awayGoals);
    }

    public int getHomeGoals(){
        return homeGoals;
    }

    public int getAwayGoals(){
        return awayGoals;
    }

    public HudScoreboard getHudScoreboard(){
        return this.hudScoreboard;
    }

    public CenterScoreboard getCenterScoreboard(){
        return this.centerScoreboard;
    }
}
