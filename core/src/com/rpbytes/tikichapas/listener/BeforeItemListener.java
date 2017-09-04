package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Item;

/**
 * Created by manoleichon on 8/11/16.
 */
public class BeforeItemListener extends ClickListener {
    Array<Item> items;
    Table table;

    public BeforeItemListener(Array<Item> items, Table table){
        this.items = items;
        this.table = table;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        for(int i=0;i<items.size;i++) {
            for(int j=0;j<table.getCells().size;j++) {
                if (items.get(i) == table.getCells().get(j).getActor()) {
                    if (i>0) {
                        table.getCells().get(j).setActor(items.get(i-1));
                    } else {
                        table.getCells().get(j).setActor(items.get(items.size-1));
                    }
                    if(items.get(i) instanceof com.rpbytes.tikichapas.items.Team){
                        Image kitHome = new Image(((com.rpbytes.tikichapas.items.Team) table.getCells().get(j).getActor()).getTextureHome());
                        //Image kitAway = new Image(((com.rpbytes.tikichapas.items.Team) table.getCells().get(j).getActor()).getTextureVisitante());
                        ((Image) table.findActor("kit")).setDrawable(kitHome.getDrawable());
                        //((Image) table.findActor("kitAway")).setDrawable(kitAway.getDrawable());
                    }
                    return;
                }
            }
        }
    }
}
