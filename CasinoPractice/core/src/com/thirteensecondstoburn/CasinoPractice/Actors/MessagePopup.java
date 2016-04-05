package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

import java.util.HashMap;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

/**
 * Created by Nick on 1/22/2015.
 */
public class MessagePopup extends Actor {
    Assets assets;
    Message message;
    Image image;
    Random random = new Random();
    HashMap<Message, Image> images = new HashMap<>();

    public enum Message {
        WIN(Assets.TEX_NAME.WIN_POPUP, Color.YELLOW),
        LOSE(Assets.TEX_NAME.LOSE_POPUP, Color.RED),
        THREEX(Assets.TEX_NAME.THREEX_POPUP, Color.CYAN),
        FOURX(Assets.TEX_NAME.FOURX_POPUP, Color.CYAN),
        FIVEX(Assets.TEX_NAME.FIVEX_POPUP, Color.CYAN),
        SIXX(Assets.TEX_NAME.SIXX_POPUP, Color.CYAN);

        private final Assets.TEX_NAME texName;
        private final Color color;

        Message(Assets.TEX_NAME texName, Color color) {
            this.texName = texName;
            this.color = color;
        }

        public Assets.TEX_NAME getTexName() {return texName;}
        public Color getColor() {return color;}
    }

    class HideRunnable implements Runnable {
        @Override
        public void run() {
            setVisible(false);
        }
    }

    public MessagePopup(Assets assets) {
        this.assets = assets;

        setVisible(false);

        for(Message message : Message.values()) {
            Image image = new Image(assets.getTexture(message.getTexName()));
            image.setOrigin(image.getWidth()/2, image.getHeight()/2);
            image.setColor(message.getColor());
            images.put(message, image);
        }
    }
    public void pop(Message message, float startX, float startY) {
        pop(message, startX, startY, 1);
    }

    public void pop(Message message, float startX, float startY, float scale) {
        setColor(Color.WHITE); // so I reset the alpha of the actor
        this.message = message;
        Image image = images.get(message);
        image.setPosition(0, 0);

        setVisible(true);

        setScale(scale);
        setPosition(startX, startY);

        int xOffset = random.nextInt(200) - 100;
        int yOffset = random.nextInt(100) + 50;

        addAction(sequence(
                parallel(moveTo(startX + xOffset, startY + yOffset, 2f), scaleTo(scale * 2f, scale * 2f, scale * 2f), fadeOut(2f)),
                run(new HideRunnable())));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Image toDraw = images.get(message);

        toDraw.setPosition(this.getX(), this.getY());
        toDraw.setRotation(this.getRotation());
        toDraw.setScale(this.getScaleX(), this.getScaleY());
        toDraw.setColor(toDraw.getColor().r, toDraw.getColor().g, toDraw.getColor().b, getColor().a);
        toDraw.draw(batch, parentAlpha);
    }

}
