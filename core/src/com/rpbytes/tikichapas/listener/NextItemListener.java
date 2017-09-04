package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.items.Team;

/**
 * Created by manoleichon on 8/11/16.
 */
public class NextItemListener extends ClickListener {

    Array<Item> items;
    Table table;

    public NextItemListener(Array<Item> items, Table table){
        this.items = items;
        this.table = table;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        for(int i=0;i<items.size;i++) {
            for(int j=0;j<table.getCells().size;j++) {
                if (items.get(i) == table.getCells().get(j).getActor()) {
                    if (i < items.size - 1) {
                        table.getCells().get(j).setActor(items.get(i+1));
                        items.get(i+1).setName("team");
                    } else {
                        table.getCells().get(j).setActor(items.get(0));
                        items.get(0).setName("team");
                    }
                    if(items.get(i) instanceof Team){
                        Image kitHome = new Image(((Team) table.getCells().get(j).getActor()).getTextureHome());
                        //Image kitAway = new Image(((Team) table.getCells().get(j).getActor()).getTextureVisitante());
                        ((Image) table.findActor("kit")).setDrawable(kitHome.getDrawable());
                        //((Image) table.findActor("kitAway")).setDrawable(kitAway.getDrawable());
                    }
                    return;
                }
            }
        }
    }
}
