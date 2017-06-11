package com.lovehunterx.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.lovehunterx.Assets;
import com.lovehunterx.LoveHunterX;
import com.lovehunterx.networking.Listener;
import com.lovehunterx.networking.Packet;

import java.util.ArrayList;

public class Player extends Group {
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;
    private int lastDirection = 1;
    private Vector2 velocity;

    private TextButton message;

    public Player(final String name, Integer sprite) {
        walkAnimation = new Animation<TextureRegion>(0.08f, Assets.getAnimation(sprite));

        TextureRegion frame = Assets.getAnimation(sprite)[0];

        setName(name);

        velocity = new Vector2();

        TextButton tag = new TextButton(name, Assets.SKIN);
        tag.setX(walkAnimation.getKeyFrame(0).getRegionWidth() / 2 - tag.getWidth() / 2);
        tag.setY(walkAnimation.getKeyFrame(0).getRegionHeight());
        addActor(tag);

        message = new TextButton("", Assets.SKIN);
    }

    public void setVelocityX(float velocityX) {
        if (velocityX != 0) {
            lastDirection = velocityX > 0 ? 1 : -1;
        }

        this.velocity.x = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocity.y = velocityY;
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        stateTime += deltaTime;

        setX(getX() + velocity.x * deltaTime * 100);
        setY(Math.max(0, getY() + velocity.y * deltaTime * 200));

        velocity.y = getY() != 0 ? velocity.y - 2F * deltaTime : 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion tex = walkAnimation.getKeyFrame(velocity.x == 0 && velocity.y == 0 || getY() != 0 ? 0.32f : stateTime, true);
        if ((velocity.x < 0 && !tex.isFlipX()) || (velocity.x > 0 && tex.isFlipX())) {
            tex.flip(true, false);
        } else if (velocity.x == 0 && (lastDirection == -1 && !tex.isFlipX() || lastDirection == 1 && tex.isFlipX())) {
            tex.flip(true, false);
        }

        batch.draw(tex, getX(), getY(), tex.getRegionWidth(), tex.getRegionHeight());
    }

    public void say(String msg) {
        message.remove();
        message.setText(msg);
        message.setSize(message.getPrefWidth(), message.getPrefHeight());
        message.setX(walkAnimation.getKeyFrame(0).getRegionWidth() / 2 - message.getWidth() / 2);
        message.setY(walkAnimation.getKeyFrame(0).getRegionHeight() + 35);
        message.clearActions();
        addActor(message);

        message.addAction(Actions.sequence(Actions.delay(4), Actions.removeActor()));
    }
}
