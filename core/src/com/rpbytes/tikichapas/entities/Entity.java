package com.rpbytes.tikichapas.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by manoleichon on 10/05/17.
 */
public abstract class Entity extends Actor {

    private TextureRegion texture;
    public Body body;
    public boolean moveScene2D=false;

    public float getRadius(){
        return getBody().getFixtureList().first().getShape().getRadius();
    }

    public void setRadius(float radius){
        getBody().getFixtureList().first().getShape().setRadius(radius);
    }

    public float getVelocityModulo(){
        return this.body.getLinearVelocity().len2();
    }

    public Body getBody(){
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public TextureRegion getTexture(){
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void stopEntity(){
        getBody().setLinearVelocity(0f, 0f);
        getBody().setAngularVelocity(0f);
    }

    public boolean stopped(){
        return getVelocityModulo() == 0f;
    }

    public void dispose(){
        getBody().getWorld().destroyBody(getBody());
    }


}

