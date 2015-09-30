package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.Screens.RouletteScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 9/24/2015.
 */
public class RouletteWheel extends Actor {

    List<ActionCompletedListener> listeners = new ArrayList<>();
    private Random rand = new Random();
    private int number = -1;
    Image wheel;
    Image ball;
    int direction = 1;

    public RouletteWheel(Assets assets) {
        if (RouletteScreen.isEuropean) {
            wheel = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_WHEEL_EUROPEAN));
        } else {
            wheel = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_WHEEL_AMERICAN));
        }
        wheel.setOrigin(225, 225);
        wheel.setPosition(this.getX(), this.getY());

        setWidth(wheel.getImageWidth());
        setHeight(wheel.getImageHeight());

        ball = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_WHEEL_BALL));
        ball.setPosition(this.getX(), this.getY());
        ball.setOrigin(225, 225);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        wheel.setPosition(x, y);
        ball.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        wheel.draw(batch, parentAlpha);
        ball.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        wheel.act(delta);
        ball.act(delta);
    }

    public int getNumber() {
        return number;
    }

    class NotifyRunnable implements Runnable {
        @Override
        public void run() {
            // make sure the wheel/ball end up on exact angles, set their positions and then set the ball number
            HashMap<Double, Integer> map = europeanAngles;
            if(!RouletteScreen.isEuropean) {
                map = americanAngles;
            }

            double ballRotation = getClosestAngle(map, ball.getRotation());
            ball.setRotation((float) ballRotation);
            double wheelRotation = getClosestAngle(map, wheel.getRotation());
            wheel.setRotation((float) wheelRotation);
            if (ballRotation > wheelRotation) ballRotation -= 360;
            double offsetAngle = getClosestAngle(map, Math.abs(ballRotation - wheelRotation));
            number = map.get(offsetAngle);

            notifyListeners();
        }
    }

    public void spin() {
        int numbers = 37;
        if(!RouletteScreen.isEuropean) {
            number = 38;
        }

        ball.addAction(Actions.rotateBy(-1 * direction * (rand.nextInt(numbers) * 360 / numbers + 1080), 5f, Interpolation.sineOut));
        wheel.addAction(sequence(
                Actions.rotateBy(direction * (rand.nextInt(numbers) * 360 / numbers + 1080), 5f, Interpolation.sineOut),
                run(new NotifyRunnable())));

        direction = direction * -1;
    }

    public void addActionListener(ActionCompletedListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (ActionCompletedListener l : listeners) {
            l.actionCompleted(this);
        }
    }

    private double getClosestAngle(HashMap<Double, Integer> map, double refAngle) {
        Set<Double> keys = map.keySet();
        if (Math.abs(refAngle) > 360) {
            double timesGreater = (int) Math.abs(refAngle) / 360.0;
            if (refAngle >= 0) {
                refAngle -= 360.0 * (int) timesGreater;
            } else {
                refAngle += 360.0 * (int) timesGreater + 360;
            }
        }
        double closest = 1000000.0;
        double closestAngle = 0;
        for (double angle : keys) {
            double distance = Math.abs(refAngle - angle);
            if (distance < closest) {
                closest = distance;
                closestAngle = angle;
            }
        }
        return closestAngle;
    }

    private static final HashMap<Double, Integer> europeanAngles = new HashMap<>();

    static {
        europeanAngles.put(0.0, 0);
        europeanAngles.put(9.72972972972973, 32);
        europeanAngles.put(19.45945945945946, 15);
        europeanAngles.put(29.18918918918919, 19);
        europeanAngles.put(38.91891891891892, 4);
        europeanAngles.put(48.648648648648646, 21);
        europeanAngles.put(58.37837837837838, 2);
        europeanAngles.put(68.10810810810811, 25);
        europeanAngles.put(77.83783783783784, 17);
        europeanAngles.put(87.56756756756756, 34);
        europeanAngles.put(97.29729729729729, 6);
        europeanAngles.put(107.02702702702703, 27);
        europeanAngles.put(116.75675675675676, 13);
        europeanAngles.put(126.48648648648648, 36);
        europeanAngles.put(136.21621621621622, 11);
        europeanAngles.put(145.94594594594594, 30);
        europeanAngles.put(155.67567567567568, 8);
        europeanAngles.put(165.40540540540542, 23);
        europeanAngles.put(175.13513513513513, 10);
        europeanAngles.put(184.86486486486487, 5);
        europeanAngles.put(194.59459459459458, 24);
        europeanAngles.put(204.32432432432432, 16);
        europeanAngles.put(214.05405405405406, 33);
        europeanAngles.put(223.78378378378378, 1);
        europeanAngles.put(233.51351351351352, 20);
        europeanAngles.put(243.24324324324326, 14);
        europeanAngles.put(252.97297297297297, 31);
        europeanAngles.put(262.7027027027027, 9);
        europeanAngles.put(272.43243243243245, 22);
        europeanAngles.put(282.1621621621622, 18);
        europeanAngles.put(291.8918918918919, 29);
        europeanAngles.put(301.6216216216216, 7);
        europeanAngles.put(311.35135135135135, 28);
        europeanAngles.put(321.0810810810811, 12);
        europeanAngles.put(330.81081081081084, 35);
        europeanAngles.put(340.5405405405405, 3);
        europeanAngles.put(350.27027027027026, 26);
    }

    private static final HashMap<Double, Integer> americanAngles = new HashMap<>();

    static {
        americanAngles.put(0.0, 37);
        americanAngles.put(9.473684210526315, 27);
        americanAngles.put(18.94736842105263, 10);
        americanAngles.put(28.42105263157895, 25);
        americanAngles.put(37.89473684210526, 29);
        americanAngles.put(47.36842105263158, 12);
        americanAngles.put(56.8421052631579, 8);
        americanAngles.put(66.3157894736842, 19);
        americanAngles.put(75.78947368421052, 31);
        americanAngles.put(85.26315789473684, 18);
        americanAngles.put(94.73684210526316, 6);
        americanAngles.put(104.21052631578948, 21);
        americanAngles.put(113.6842105263158, 33);
        americanAngles.put(123.15789473684211, 16);
        americanAngles.put(132.6315789473684, 4);
        americanAngles.put(142.10526315789474, 23);
        americanAngles.put(151.57894736842104, 35);
        americanAngles.put(161.05263157894737, 14);
        americanAngles.put(170.52631578947367, 2);
        americanAngles.put(180.0, 0);
        americanAngles.put(189.47368421052633, 28);
        americanAngles.put(198.94736842105263, 9);
        americanAngles.put(208.42105263157896, 26);
        americanAngles.put(217.89473684210526, 30);
        americanAngles.put(227.3684210526316, 11);
        americanAngles.put(236.8421052631579, 7);
        americanAngles.put(246.31578947368422, 20);
        americanAngles.put(255.78947368421052, 32);
        americanAngles.put(265.2631578947368, 17);
        americanAngles.put(274.7368421052632, 5);
        americanAngles.put(284.2105263157895, 22);
        americanAngles.put(293.6842105263158, 34);
        americanAngles.put(303.1578947368421, 15);
        americanAngles.put(312.63157894736844, 3);
        americanAngles.put(322.10526315789474, 24);
        americanAngles.put(331.57894736842104, 36);
        americanAngles.put(341.05263157894734, 13);
        americanAngles.put(350.5263157894737, 1);
    }
}
