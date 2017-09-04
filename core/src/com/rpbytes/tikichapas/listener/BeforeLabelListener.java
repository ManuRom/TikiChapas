package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Item;

/**
 * Created by manoleichon on 8/11/16.
 */
public class BeforeLabelListener extends ClickListener {
    Array<Item> items;
    Label label;
    Table table;

    public BeforeLabelListener(Array<Item> items, Label label, Table table){
        this.items = items;
        this.table = table;
        this.label = label;
    }

    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event,x,y);
        for(int i = 0;i < items.size;i++){
            if(items.get(i).getName().equals(label.getText().toString()))
            {
                if(i>0) {
                    label.setText(items.get(i - 1).getName());
                    break;
                }else {
                    label.setText(items.get(items.size - 1).getName());
                    break;
                }
            }
        }
        table.swapActor(table.findActor(label.getName()),label);
    }

}
