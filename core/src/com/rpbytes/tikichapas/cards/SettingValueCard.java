package com.rpbytes.tikichapas.cards;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.utils.SkinKeys;

/**
 * Created by manoleichon on 27/01/17.
 */
public class SettingValueCard extends Card {


    private Label value;
    private Label title;
    private Image icon;

    public SettingValueCard(float width, float height, NinePatchDrawable background, String title, int value, Image icon){
        super(width,height,background);
        this.title = new Label(title,getSkin());
        this.value = new Label(Integer.toString(value), getSkin(), SkinKeys.LABEL_ANIMATION_GOAL);
        this.icon = icon;

        this.value.setFontScale(2f);

        Table valueWrapper = new Table();
        valueWrapper.setBackground(getSkin().getDrawable("scoreboard_goals"));
        valueWrapper.setSize(150f, 150f);
        valueWrapper.add(getValue()).center();

        this.add(icon).size(Constants.SETTING_ICON_SIZE).top();
        this.row();
        this.add(this.title).padBottom(Constants.PAD).padTop(5f);
        this.row();
        this.add(valueWrapper).size(Constants.CARD_CONTENT_SIZE);
    }

    public Label getValue() {
        return value;
    }

    public int getIntegerValue(){
        return Integer.valueOf(value.getText().toString());
    }

    public void setValue(Label value) {
        this.value = value;
    }

    public void setValue(Integer value){
        this.value.setText(Integer.toString(value));
    }

    public Label getTitle(){
        return title;
    }

    public void setTitle(Label title){
        this.title = title;
    }


    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }
}
