package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.RouletteNumberBoard;
import com.thirteensecondstoburn.CasinoPractice.Actors.RouletteWheel;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 9/24/2015.
 */
public class RouletteScreen extends TableScreen implements ActionCompletedListener {

    public RouletteScreen(CasinoPracticeGame game) {
        super(game);
    }

    Image tableImage;
    TableButton spinButton;
    List<BettingSpot> bettingSpots;
    RouletteWheel wheel;
    RouletteNumberBoard numberBoard;

    @Override
    public void actionCompleted(Actor caller) {
        numberBoard.push(wheel.getNumber());
        calculateWinnings(wheel.getNumber());
    }

    @Override
    protected void setup() {
        Texture tableTex = assets.getTexture(Assets.TEX_NAME.ROULETTE_TABLE_EUROPEAN);
        tableImage = new Image(tableTex);
        tableImage.setPosition(280, 40);

        wheel = new RouletteWheel(assets);
        wheel.addActionListener(this);
        wheel.setPosition(stage.getWidth() - 450, stage.getHeight() - 450);

        numberBoard = new RouletteNumberBoard(assets);
        numberBoard.setPosition(400, stage.getHeight() - 275);

        spinButton = new TableButton(assets, "Spin", mainColor);
        spinButton.setPosition(stage.getWidth() - spinButton.getWidth(), 0);
        spinButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (spinButton.isInside(x, y)) {
                    wheel.spin();
                }
            }
        });

        bettingSpots = new ArrayList<>();
        bettingSpots.add(new BettingSpot(new GridPoint2(662, 703), 1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18})); // 1 - 18
        bettingSpots.add(new BettingSpot(new GridPoint2(1232, 703), 1, new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36})); // 19 - 36

        // loop to add the single numbers
        for(int i = 0; i < 12; i++) {
            bettingSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 588), 35, new int[]{i*3 + 3})); // top line
            bettingSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 460), 35, new int[]{i*3 + 2})); // middle line
            bettingSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 342), 35, new int[]{i*3 + 1})); // bottom line
        }
        bettingSpots.add(new BettingSpot(new GridPoint2(336, 460), 35, new int[]{0})); // single 0
        // TODO American 0 stuff 00 = 38

        bettingSpots.add(new BettingSpot(new GridPoint2(1574, 588), 2, new int[]{3,6,9,12,15,18,21,24,27,30,33,36})); // top dozen
        bettingSpots.add(new BettingSpot(new GridPoint2(1574, 460), 2, new int[]{2,5,8,11,14,17,20,23,26,29,32,35})); // middle dozen
        bettingSpots.add(new BettingSpot(new GridPoint2(1574, 342), 2, new int[]{1,4,7,10,13,16,19,22,25,28,31,34})); // bottom dozen

        // loop to add the split numbers
        for(int i = 0; i < 11; i++) {
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 588), 17, new int[]{i*3 + 3, (i+1)*3 + 3})); // top line
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 460), 17, new int[]{i*3 + 2, (i+1)*3 + 3})); // middle line
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 342), 17, new int[]{i*3 + 1, (i+1)*3 + 3})); // bottom line
        }

        // loop to add the corner bets
        for(int i=0; i < 11; i++) {
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 530), 8, new int[]{i*3 + 2, (i+1)*3 + 2, i*3 + 3, (i+1)*3 + 3})); // top line
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 410), 8, new int[]{i*3 + 1, (i+1)*3 + 1, i*3 + 2, (i+1)*3 + 2})); // bottom line
        }

        // loop to add the street bets
        for(int i=0; i < 12; i++) {
            bettingSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 650), 11, new int[]{i * 3 +1, i * 3 + 2, i * 3 + 3}));
        }

        // loop to add the line bets
        for (int i=0; i < 11; i++) {
            bettingSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 292), 5, new int[]{i * 3 +1, i * 3 + 2, i * 3 + 3, (i+1) * 3 +1, (i+1) * 3 + 2, (i+1) * 3 + 3}));
        }

        bettingSpots.add(new BettingSpot(new GridPoint2(380, 292), 6, new int[]{38,0,1,2,3})); // basket 00 = 38

        bettingSpots.add(new BettingSpot(new GridPoint2(568, 228), 2, new int[]{1,2,3,4,5,6,7,8,9,10,11,12})); // 1 - 12
        bettingSpots.add(new BettingSpot(new GridPoint2(952, 228), 2, new int[]{13,14,15,16,17,18,19,20,21,22,23,24})); // 13 - 24
        bettingSpots.add(new BettingSpot(new GridPoint2(1328, 228), 2, new int[]{25,26,27,28,29,30,31,32,33,34,35,36})); // 25 - 36
        bettingSpots.add(new BettingSpot(new GridPoint2(540, 110), 1, new int[]{2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36})); // even
        bettingSpots.add(new BettingSpot(new GridPoint2(812, 110), 1, new int[]{1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36})); // red
        bettingSpots.add(new BettingSpot(new GridPoint2(1094, 110), 1, new int[]{2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35})); // black
        bettingSpots.add(new BettingSpot(new GridPoint2(1390, 110), 1, new int[]{1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35})); //odd

        stage.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                BettingSpot closest = null;
                float minDistance = 1000000000;
                int currentWager = 0;
                for (BettingSpot spot : bettingSpots) {
                    currentWager += spot.stack.getTotal();
                    float distance = spot.distance(x, y);
                    if(distance < minDistance) {
                        closest = spot;
                        minDistance = distance;
                    }
                }
                if(minDistance > 40.0f) return; // has to be somewhat close to the point we're looking for

                int bet = placeBet(closest.stack, leftSide.getBetAmount());
                leftSide.setWagerText("" + (currentWager + bet));
                leftSide.updateBalance();
            }
        });

        // add the actors to the stage
        stage.addActor(tableImage);
        stage.addActor(wheel);
        stage.addActor(numberBoard);
        stage.addActor(spinButton);

        for (BettingSpot spot : bettingSpots) {
            stage.addActor(spot.stack);
        }
    }

    private void calculateWinnings(int number) {
        System.out.println("Number was: " + number);
        int won = 0;
        int currentWager = 0;
        for (BettingSpot spot : bettingSpots) {
            if(spot.stack.getTotal() > 0) {
                if(spot.winningNumbers.contains(number)) {
                    won += spot.stack.getTotal() * spot.payout;
                    spot.stack.popStack(true);
                    currentWager += spot.stack.getTotal();
                } else {
                    won -= spot.stack.getTotal();
                    spot.stack.popStack(false);
                    spot.stack.setTotal(0);
                }
            }
        }
        leftSide.setWagerText("" + (currentWager));
        leftSide.updateBalance();
        if(won > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(won < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);
        leftSide.setWonText("" + (won));
    }

    private class BettingSpot {
        public GridPoint2 center;
        public ChipStackGroup stack;
        public int payout;
        public List<Integer> winningNumbers;

        BettingSpot(GridPoint2 center, int payout, int[] numbers) {
            this.center = center;
            stack = new ChipStackGroup(game, assets, .5f);
            stack.setPosition(center.x - stack.getWidth() / 4, center.y - stack.getHeight() / 4);
            this.payout = payout;
            this.winningNumbers = new ArrayList<>();
            for (int number : numbers) {
                winningNumbers.add(number);
            }
        }

        public float distance(float originX, float originY) {
            return Vector2.dst(originX, originY, center.x, center.y);
        }
    }
}
