package com.rpbytes.tikichapas.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.screens.MatchScreen;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.screens.UpdateUserScreen;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

/**
 * Created by manoleichon on 31/01/17.
 */
public class ConfigDialog extends Dialog {

    public CheckBox sound, music;
    public TextButton aceptar, updateUser, exit;
    public Label username, userId, userWins, userLosses;
    private TikiChapasGame game;

    public ConfigDialog(String text, Skin skin, TikiChapasGame game){
        super(text,skin);
        this.game = game;

        sound = new CheckBox(" Sonido", skin);
        if(getGame().getPreferences().contains("sound")){
            if(getGame().getPreferences().getBoolean("sound")){
                sound.setChecked(true);
            }else{
                sound.setChecked(false);
            }
        }else{
            getGame().getPreferences().putBoolean("sound", true).flush();
        }
        music = new CheckBox(" Musica", skin);

        if(getGame().getPreferences().contains("music")){
            if(getGame().getPreferences().getBoolean("music")){
                music.setChecked(true);
            }else{
                music.setChecked(false);
            }
        }else{
            getGame().getPreferences().putBoolean("music", true).flush();
        }





        exit = new TextButton(Text.BTN_EXIT_GAME, skin);

        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog areYouSure = new Dialog(Text.ARE_YOU_SURE, getSkin());
                TextButton exit = new TextButton(Text.BTN_EXIT, getSkin());
                areYouSure.button(new TextButton(Text.BTN_CANCEL, getSkin())).padLeft(Constants.PAD).padRight(Constants.PAD);
                areYouSure.button(exit).padLeft(Constants.PAD).padRight(Constants.PAD);

                exit.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.exit();
                    }
                });
                areYouSure.show(getStage());
            }
        });

        Label settings = new Label(Text.SETTINGS, skin, SkinKeys.LABEL_TITLE);

        Table leftTable = new Table();

        leftTable.add(settings).left().padBottom(Constants.PAD);
        leftTable.row();
        if(game.getScreen().getClass() != MatchScreen.class) {
            leftTable.add(music).left().padBottom(Constants.PAD);
            leftTable.row();
        }
        leftTable.add(sound).left();

        Label userData = new Label(Text.USER_DATA, skin, SkinKeys.LABEL_TITLE);

        Table rightTable = new Table();
        rightTable.add(userData).left().padBottom(Constants.PAD);
        rightTable.row();

        if(game.isOffline()) {
            rightTable.add(new Label(Text.OFFLINE_MODE,skin));
        } else {
            rightTable.add(this.createUserDataTable());
        }

        this.getContentTable().add(leftTable).top().left().pad(Constants.PAD).padRight(Constants.PAD*2);
        this.getContentTable().add(rightTable).top().right().pad(Constants.PAD).padLeft(Constants.PAD*2);


        music.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCheckedMusic();
            }
        });

        sound.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCheckedSound();
            }
        });

        aceptar = new TextButton(Text.BTN_ACCEPT, skin);
        if(game.getScreen().getClass() != MatchScreen.class) {
            this.button(exit).padLeft(Constants.PAD).padRight(Constants.PAD);
            if(!game.isOffline())
                this.button(updateUser).padLeft(Constants.PAD).padRight(Constants.PAD);
        }
        this.button(aceptar).padLeft(Constants.PAD).padRight(Constants.PAD);
    }

    public void setCheckedMusic(){
        if(getGame().getPreferences().getBoolean("music")){
            music.setChecked(false);
            getGame().getPreferences().putBoolean("music", false).flush();
            if(getGame().getScreen().getClass() != MatchScreen.class)
                Assets.manager.get("music/musicMenu.mp3", Music.class).stop();
        }else{
            music.setChecked(true);
            getGame().getPreferences().putBoolean("music", true).flush();
            Assets.manager.get("music/musicMenu.mp3", Music.class).play();
        }
    }

    public void setCheckedSound(){
        if(getGame().getPreferences().getBoolean("sound")){
            sound.setChecked(false);
            getGame().getPreferences().putBoolean("sound", false).flush();
            if(getGame().getScreen().getClass() == MatchScreen.class)
                Assets.crowd.stop();
        }else{
            sound.setChecked(true);
            getGame().getPreferences().putBoolean("sound", true).flush();
            if(getGame().getScreen().getClass() == MatchScreen.class) {
                Assets.crowd.play();
                Assets.crowd.setVolume(0.5f);
                Assets.crowd.setLooping(true);
            }
        }
    }

    private Table createUserDataTable(){

        username = new Label(game.getUser().getUsername(), getSkin(), SkinKeys.LABEL_GREY);
        userId = new Label("id: " + Integer.toString(game.getUser().getId()), getSkin(), SkinKeys.LABEL_GREY);
        userId.setFontScale(0.7f);
        Label userWinsLabel = new Label(Text.WINS, getSkin(), SkinKeys.LABEL_GREY);
        userWins = new Label(Integer.toString(game.getUser().getWins()), getSkin(), SkinKeys.DEFAULT);
        Label userLoosesLabel = new Label(Text.LOOSES, getSkin(), SkinKeys.LABEL_GREY);
        userLosses = new Label(Integer.toString(game.getUser().getLosses()), getSkin(), SkinKeys.DEFAULT);
        updateUser = new TextButton(Text.BTN_UPDATE_USER, getSkin());

        updateUser.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new UpdateUserScreen(getGame(), getGame().getScreen()));
            }
        });

        Table rightTable = new Table();

        Table userScoreTable = new Table();

        Label divider = new Label("-", getSkin(), SkinKeys.LABEL_GREY);

        userScoreTable.add(userWinsLabel);
        userScoreTable.add(divider).padLeft(Constants.PAD).padRight(Constants.PAD);
        userScoreTable.add(userLoosesLabel);
        userScoreTable.row();
        userScoreTable.add(userWins).center();
        userScoreTable.add();
        userScoreTable.add(userLosses).center();


        rightTable.add(username).left().padBottom(Constants.PAD);
        rightTable.row();
        rightTable.add(userScoreTable).left();
        rightTable.row();
        rightTable.add(userId).left().padTop(Constants.PAD);

        return rightTable;
    }

    public TikiChapasGame getGame() {
        return game;
    }

    public void setGame(TikiChapasGame game) {
        this.game = game;
    }

}
