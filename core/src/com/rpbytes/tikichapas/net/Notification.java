package com.rpbytes.tikichapas.net;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by manoleichon on 20/02/17.
 */
public abstract class Notification {
    public int id;
    public boolean read;
    public int typeId;

    public abstract String messageBuilder();
}
