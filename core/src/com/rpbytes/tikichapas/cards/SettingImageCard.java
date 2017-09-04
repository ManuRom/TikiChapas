package com.rpbytes.tikichapas.cards;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.utils.SkinKeys;

/**
 * Created by manoleichon on 27/01/17.
 */
public class SettingImageCard extends Card {

    public Label settingName;
    public Image settingImage;
    private Image icon;

    public SettingImageCard(float width, float height, NinePatchDrawable background, Image settingImage, String settingName, Image icon){
        super(width,height,background);
        this.settingImage = settingImage;
        this.settingName = new Label(settingName, getSkin(), SkinKeys.LABEL_ANIMATION_GOAL);
        this.setIcon(icon);

        this.add(icon).size(Constants.SETTING_ICON_SIZE).top();
        this.row();
        this.add(settingName).padBottom(Constants.PAD).padTop(5f);
        this.row();
        this.add(settingImage).size(Constants.CARD_CONTENT_SIZE);

    }

    public Image getSettingImage() {
        return settingImage;
    }

    public void setSettingImage(Image settingImage) {
        this.getCell(this.settingImage).setActor(settingImage);
        this.settingImage = settingImage;
    }

    public Label getSettingName() {
        return settingName;
    }

    public void setSettingName(Label settingName) {
        this.settingName = settingName;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }



}
