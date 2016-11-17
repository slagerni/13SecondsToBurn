package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.MultiLineTableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.RouletteNumberBoard;
import com.thirteensecondstoburn.CasinoPractice.Actors.RouletteWheel;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;
import com.thirteensecondstoburn.CasinoPractice.Statistics.StatisticType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nick on 9/24/2015.
 */
public class RouletteScreen extends TableScreen implements ActionCompletedListener {

    public RouletteScreen(CasinoPracticeGame game) {
        super(game);
        menuButtonColor = game.getAssets().getSkin().getColor("menuButtonColor");
    }

    private enum BetType{ ON, LINE, CORNER };

    public static final StatisticType Spun = new StatisticType("spun", "Spun");
    public static final StatisticType ReturnPerSpin = new StatisticType("returnPerSpin", "Return per Spin");

    Image tableImage;
    TableButton spinButton;
    MultiLineTableButton undoBetButton;
    List<BettingSpot> onSpots;
    List<BettingSpot> lineSpots;
    List<BettingSpot> cornerSpots;
    RouletteWheel wheel;
    RouletteNumberBoard numberBoard;
    public static boolean isEuropean = true;
    LinkedList<BetHistory> betHistory = new LinkedList<>();
    BetType currentBettingType = BetType.ON;
    Button centerBetButton;
    Button lineBetButton;
    Button cornerBetButton;

    Color menuButtonColor;
    Color notSelectedColor = Color.GRAY;
    
    public void actionCompleted(Actor caller) {
        statistics.increment(Spun);
        numberBoard.push(wheel.getNumber());
        calculateWinnings(wheel.getNumber());
    }

    @Override
    protected void setup() {
        isEuropean =  game.getRouletteType().equalsIgnoreCase("European");

        Texture tableTex;
        if(isEuropean) {
            tableTex = assets.getTexture(Assets.TEX_NAME.ROULETTE_TABLE_EUROPEAN);
        } else {
            tableTex = assets.getTexture(Assets.TEX_NAME.ROULETTE_TABLE_AMERICAN);
        }
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
                    betHistory.clear();
                    undoBetButton.setVisible(false);
                }
            }
        });

        undoBetButton = new MultiLineTableButton(assets, "Undo|Bet", mainColor);
        undoBetButton.setPosition(stage.getWidth() - undoBetButton.getWidth(), undoBetButton.getHeight() + 10);
        undoBetButton.setVisible(false);
        undoBetButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (undoBetButton.isInside(x, y)) {
                    if (betHistory.size() == 0) return;

                    BetHistory last = betHistory.pop();
                    int currentWager = 0;
                    for (BettingSpot spot : onSpots) {
                        currentWager += spot.stack.getTotal();
                    }
                    for (BettingSpot spot : lineSpots) {
                        currentWager += spot.stack.getTotal();
                    }
                    for (BettingSpot spot : cornerSpots) {
                        currentWager += spot.stack.getTotal();
                    }
                    currentWager -= last.amount;

                    placeBet(last.stack, -last.amount);
                    leftSide.setWagerText("" + (currentWager + currentWager));
                    if (betHistory.size() == 0) {
                        undoBetButton.setVisible(false);
                    }
                }
            }
        });

        onSpots = new ArrayList<>();
        lineSpots = new ArrayList<>();
        cornerSpots = new ArrayList<>();

        onSpots.add(new BettingSpot(new GridPoint2(662, 703), 1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18})); // 1 - 18
        onSpots.add(new BettingSpot(new GridPoint2(1232, 703), 1, new int[]{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36})); // 19 - 36

        // loop to add the single numbers
        for(int i = 0; i < 12; i++) {
            onSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 588), 35, new int[]{i*3 + 3})); // top line
            onSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 460), 35, new int[]{i*3 + 2})); // middle line
            onSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 342), 35, new int[]{i*3 + 1})); // bottom line
        }
        if(isEuropean) {
            onSpots.add(new BettingSpot(new GridPoint2(336, 460), 35, new int[]{0})); // single 0
        } else {
            // American 00 = 37
            onSpots.add(new BettingSpot(new GridPoint2(336, 524), 35, new int[]{37})); // single 00
            onSpots.add(new BettingSpot(new GridPoint2(336, 404), 35, new int[]{0})); // single 0
        }

        onSpots.add(new BettingSpot(new GridPoint2(1574, 588), 2, new int[]{3,6,9,12,15,18,21,24,27,30,33,36})); // top dozen
        onSpots.add(new BettingSpot(new GridPoint2(1574, 460), 2, new int[]{2,5,8,11,14,17,20,23,26,29,32,35})); // middle dozen
        onSpots.add(new BettingSpot(new GridPoint2(1574, 342), 2, new int[]{1,4,7,10,13,16,19,22,25,28,31,34})); // bottom dozen

        // loop to add the left/right split numbers
        for(int i = 0; i < 11; i++) {
            lineSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 588), 17, new int[]{i*3 + 3, (i+1)*3 + 3})); // top line
            lineSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 460), 17, new int[]{i*3 + 2, (i+1)*3 + 3})); // middle line
            lineSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 342), 17, new int[]{i*3 + 1, (i+1)*3 + 3})); // bottom line
        }

        // more split numbers, this time the top/bottom split
        for(int i = 0; i < 12; i++) {
            lineSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 530), 17, new int[]{i * 3 + 2, i * 3 + 3})); // top line
            lineSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 410), 17, new int[]{i * 3 + 1, i * 3 + 2})); // bottom line
        }

        // loop to add the corner bets
        for(int i=0; i < 11; i++) {
            cornerSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 530), 8, new int[]{i*3 + 2, (i+1)*3 + 2, i*3 + 3, (i+1)*3 + 3})); // top line
            cornerSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 410), 8, new int[]{i*3 + 1, (i+1)*3 + 1, i*3 + 2, (i+1)*3 + 2})); // bottom line
        }

        // loop to add the street bets
        for(int i=0; i < 12; i++) {
            lineSpots.add(new BettingSpot(new GridPoint2(428 + i * 95, 650), 11, new int[]{i * 3 +1, i * 3 + 2, i * 3 + 3}));
        }

        // loop to add the line bets
        for (int i=0; i < 11; i++) {
            onSpots.add(new BettingSpot(new GridPoint2(475 + i * 95, 292), 5, new int[]{i * 3 +1, i * 3 + 2, i * 3 + 3, (i+1) * 3 +1, (i+1) * 3 + 2, (i+1) * 3 + 3}));
        }

        onSpots.add(new BettingSpot(new GridPoint2(380, 292), 6, new int[]{37,0,1,2,3})); // basket 00 = 37

        onSpots.add(new BettingSpot(new GridPoint2(568, 228), 2, new int[]{1,2,3,4,5,6,7,8,9,10,11,12})); // 1 - 12
        onSpots.add(new BettingSpot(new GridPoint2(952, 228), 2, new int[]{13,14,15,16,17,18,19,20,21,22,23,24})); // 13 - 24
        onSpots.add(new BettingSpot(new GridPoint2(1328, 228), 2, new int[]{25,26,27,28,29,30,31,32,33,34,35,36})); // 25 - 36
        onSpots.add(new BettingSpot(new GridPoint2(540, 110), 1, new int[]{2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36})); // even
        onSpots.add(new BettingSpot(new GridPoint2(812, 110), 1, new int[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36})); // red
        onSpots.add(new BettingSpot(new GridPoint2(1094, 110), 1, new int[]{2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35})); // black
        onSpots.add(new BettingSpot(new GridPoint2(1390, 110), 1, new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35})); //odd

        stage.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                BettingSpot closest = null;
                float minDistance = 1000000000;
                int currentWager = 0;
                for (BettingSpot spot : onSpots) {
                    currentWager += spot.stack.getTotal();
                    float distance = spot.distance(x, y);
                    if (distance < minDistance && currentBettingType == BetType.ON) {
                        closest = spot;
                        minDistance = distance;
                    }
                }
                for (BettingSpot spot : lineSpots) {
                    currentWager += spot.stack.getTotal();
                    float distance = spot.distance(x, y);
                    if (distance < minDistance && currentBettingType == BetType.LINE) {
                        closest = spot;
                        minDistance = distance;
                    }
                }
                for (BettingSpot spot : cornerSpots) {
                    currentWager += spot.stack.getTotal();
                    float distance = spot.distance(x, y);
                    if (distance < minDistance && currentBettingType == BetType.CORNER) {
                        closest = spot;
                        minDistance = distance;
                    }
                }
                if (minDistance > 50.0f) return; // has to be somewhat close to the point we're looking for

                int before = closest.stack.getTotal();
                int after = placeBet(closest.stack, leftSide.getBetAmount());
                int bet = after - before;
                betHistory.push(new BetHistory(closest.stack, bet));
                undoBetButton.setVisible(true);
                leftSide.setWagerText("" + (currentWager + bet));
            }
        });

        centerBetButton = new Button(assets.getSkin());
        centerBetButton.setSize(100, 100);
        centerBetButton.setPosition(260, stage.getHeight() - 110);
        centerBetButton.setColor(menuButtonColor);
        Image centerBetImage = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_BET_CENTER));
        centerBetButton.add(centerBetImage);
        centerBetButton.pad(10);
        centerBetButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currentBettingType = BetType.ON;
                centerBetButton.setColor(menuButtonColor);
                lineBetButton.setColor(notSelectedColor);
                cornerBetButton.setColor(notSelectedColor);
            }
        });

        lineBetButton = new Button(assets.getSkin());
        lineBetButton.setSize(100, 100);
        lineBetButton.setPosition(260, stage.getHeight() - 210);
        lineBetButton.setColor(notSelectedColor);
        Image lineBetImage = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_BET_LINE));
        lineBetButton.add(lineBetImage);
        lineBetButton.pad(10);
        lineBetButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currentBettingType = BetType.LINE;
                centerBetButton.setColor(notSelectedColor);
                lineBetButton.setColor(menuButtonColor);
                cornerBetButton.setColor(notSelectedColor);
            }
        });

        cornerBetButton = new Button(assets.getSkin());
        cornerBetButton.setSize(100, 100);
        cornerBetButton.setPosition(260, stage.getHeight() - 310);
        cornerBetButton.setColor(notSelectedColor);
        Image cornerBetImage = new Image(assets.getTexture(Assets.TEX_NAME.ROULETTE_BET_CORNER));
        cornerBetButton.add(cornerBetImage);
        cornerBetButton.pad(10);
        cornerBetButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                currentBettingType = BetType.CORNER;
                cornerBetButton.setColor(notSelectedColor);
                lineBetButton.setColor(notSelectedColor);
                cornerBetButton.setColor(menuButtonColor);
            }
        });


        // add the actors to the stage
        stage.addActor(tableImage);
        stage.addActor(wheel);
        stage.addActor(numberBoard);
        stage.addActor(spinButton);
        stage.addActor(undoBetButton);
        stage.addActor(centerBetButton);
        stage.addActor(lineBetButton);
        stage.addActor(cornerBetButton);

        for (BettingSpot spot : onSpots) {
            stage.addActor(spot.stack);
        }
        for (BettingSpot spot : lineSpots) {
            stage.addActor(spot.stack);
        }
        for (BettingSpot spot : cornerSpots) {
            stage.addActor(spot.stack);
        }
    }

    private void calculateWinnings(int number) {
        int won = 0;
        for (BettingSpot spot : onSpots) {
            if(spot.stack.getTotal() > 0) {
                if(spot.winningNumbers.contains(number)) {
                    float stackWin = spot.stack.getTotal() * spot.payout;
                    won += stackWin;
                    System.out.println("Wager: " + spot.stack.getTotal());
                    System.out.println("Won: " + won);
                    System.out.println("Adding to balance: " + (stackWin + spot.stack.getTotal()));
                    addToBalance(stackWin + spot.stack.getTotal());
                    spot.stack.popStack(true);
                } else {
                    won -= spot.stack.getTotal();
                    spot.stack.popStack(false);
                }
                spot.stack.setTotal(0);
            }
        }
        for (BettingSpot spot : lineSpots) {
            if(spot.stack.getTotal() > 0) {
                if(spot.winningNumbers.contains(number)) {
                    float stackWin = spot.stack.getTotal() * spot.payout;
                    won += stackWin;
                    addToBalance(stackWin  + spot.stack.getTotal());
                    spot.stack.popStack(true);
                } else {
                    won -= spot.stack.getTotal();
                    spot.stack.popStack(false);
                }
                spot.stack.setTotal(0);
            }
        }
        for (BettingSpot spot : cornerSpots) {
            if(spot.stack.getTotal() > 0) {
                if(spot.winningNumbers.contains(number)) {
                    float stackWin = spot.stack.getTotal() * spot.payout;
                    won += stackWin;
                    addToBalance(stackWin + spot.stack.getTotal());
                    spot.stack.popStack(true);
                } else {
                    won -= spot.stack.getTotal();
                    spot.stack.popStack(false);
                }
                spot.stack.setTotal(0);
            }
        }
        leftSide.setWagerText("" + 0);
        if(won > 0) {
            statistics.increment(CasinoPracticeStatistics.TimesWon);
            statistics.increment(CasinoPracticeStatistics.Won, won);
            statistics.checkMaximumWon(won);
            leftSide.setWonColor(Color.GREEN);
        } else if(won < 0) {
            statistics.increment(CasinoPracticeStatistics.TimesLost);
            statistics.increment(CasinoPracticeStatistics.Lost, won);
            statistics.checkMaximumWon(won);
            leftSide.setWonColor(Color.RED);
        } else {
            statistics.increment(CasinoPracticeStatistics.TimesPushed);
            leftSide.setWonColor(Color.WHITE);
        }
        statistics.updateReturnPerHand(ReturnPerSpin, Spun);

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

    private class BetHistory {
        public ChipStackGroup stack;
        public int amount;

        BetHistory(ChipStackGroup stack, int amount) {
            this.stack = stack;
            this.amount = amount;
        }
    }
}
