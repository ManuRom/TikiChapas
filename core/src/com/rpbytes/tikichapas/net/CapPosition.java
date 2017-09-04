package com.rpbytes.tikichapas.net;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by manoleichon on 29/12/16.
 */
public class CapPosition {

    private int index;
    private Vector2 position;
    private float rotation;
    private float angularVelocity;
    private boolean endMovement;


    public CapPosition(float x, float y, float rotation, int index, float angularVelocity, boolean endMovement){
        position = new Vector2(x,y);
        setRotation(rotation);
        setIndex(index);
        setAngularVelocity(angularVelocity);
        setEndMovement(endMovement);
    }

    public float getX(){
        return position.x;
    }

    public void setX(float x){
        getPosition().x = x;
    }

    public float getY(){
        return position.y;
    }

    public void setY(float y){
        getPosition().y = y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean isEndMovement() {
        return endMovement;
    }

    public void setEndMovement(boolean endMovement) {
        this.endMovement = endMovement;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
