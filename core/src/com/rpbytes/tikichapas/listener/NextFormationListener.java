package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.utils.SkinKeys;


/**
 * Created by manoleichon on 18/05/17.
 */
public class NextFormationListener extends CarouselFormationListener {


    public NextFormationListener(Cell cell, Array<Formation> formations, MatchSettings matchSettings, Skin skin, boolean home){
        super(cell,formations,matchSettings,skin,home);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        for (int i=0;i<getFormations().size;i++) {
            if(getFormations().get(i).getMainImage() == ((Table) getCell().getActor()).getCells().peek().getActor()){
                if(i == getFormations().size-1) {
                    ((Table)getCell().getActor()).getCells().first().setActor(new Label(getFormations().get(0).getName(),getSkin(), SkinKeys.LABEL));
                    ((Table)getCell().getActor()).getCells().peek().setActor(getFormations().get(0).getMainImage());
                    if(isHome())
                        getMatchSettings().setHomeFormation(getFormations().get(0));
                    else
                        getMatchSettings().setAwayFormation(getFormations().get(0));
                    break;
                }else{
                    ((Table)getCell().getActor()).getCells().first().setActor(new Label(getFormations().get(i+1).getName(),getSkin(), SkinKeys.LABEL));
                    ((Table)getCell().getActor()).getCells().peek().setActor(getFormations().get(i+1).getMainImage());
                    if(isHome())
                        getMatchSettings().setHomeFormation(getFormations().get(i+1));
                    else
                        getMatchSettings().setAwayFormation(getFormations().get(i+1));
                    break;
                }
            }
        }
    }
}
