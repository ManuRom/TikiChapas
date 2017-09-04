package com.rpbytes.tikichapas.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;


/**
 * Created by manoleichon on 26/01/17.
 */
public class Card extends Table {


    public Card(){}
    public Card(float width, float height, NinePatchDrawable background){
        this.setSize(width,height);
        this.setBackground(background);
        this.setSkin(new Skin(Gdx.files.internal("skin/uiskin.json")));
    }

}
