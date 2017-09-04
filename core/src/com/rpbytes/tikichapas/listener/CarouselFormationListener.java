package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.match.MatchSettings;

/**
 * Created by manoleichon on 18/05/17.
 */
public abstract class CarouselFormationListener extends ClickListener{

    private Cell cell;
    private Array<Formation> formations;
    private MatchSettings matchSettings;
    private Skin skin;
    private Boolean home;

    public CarouselFormationListener(Cell cell, Array<Formation> formations, MatchSettings matchSettings,Skin skin, boolean home){
        this.cell = cell;
        this.formations = formations;
        this.matchSettings = matchSettings;
        this.skin = skin;
        this.home = home;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Array<Formation> getFormations() {
        return formations;
    }

    public void setFormations(Array<Formation> formations) {
        this.formations = formations;
    }

    public MatchSettings getMatchSettings() {
        return matchSettings;
    }

    public void setMatchSettings(MatchSettings matchSettings) {
        this.matchSettings = matchSettings;
    }

    public Boolean isHome() {
        return home;
    }

    public void setHome(Boolean home) {
        this.home = home;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
