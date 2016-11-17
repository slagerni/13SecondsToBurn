package com.thirteensecondstoburn.CasinoPractice.Statistics;

import java.util.HashMap;
import java.util.Map;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.TableGame;

/**
 * Created by Nick on 10/20/2016.
 */
public class CasinoPracticeStatistics {
    // standard statistic types
    public static final StatisticType Dealt = new StatisticType("plays", "Dealt");
    public static final StatisticType Won = new StatisticType("won", "Total Won");
    public static final StatisticType Lost = new StatisticType("lost", "Total Lost");
    public static final StatisticType Wagered = new StatisticType("wagered", "Total Wagered");
    public static final StatisticType TimesWon = new StatisticType("timesWon", "Times Won");
    public static final StatisticType TimesLost = new StatisticType("timesLost", "Times Lost");
    public static final StatisticType TimesPushed = new StatisticType("timesPushed", "Times Pushed");
    public static final StatisticType Balance = new StatisticType("balance", "Balance");
    public static final StatisticType ReturnPerHand = new StatisticType("returnPerHand", "Return per Hand");

    public CasinoPracticeStatistics() {
        gameStatistics = new HashMap<>();
    }

    public Map<TableGame, Map<StatisticType, Double>> getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(Map<TableGame, Map<StatisticType, Double>> gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    Map<TableGame, Map<StatisticType, Double>> gameStatistics;

    public void increment(StatisticType key, double amount) {
        TableGame currentGame = CasinoPracticeGame.getCurrentGame();
        Map<StatisticType, Double> stats = gameStatistics.get(currentGame);
        if(stats == null) {
            stats = initializeTableGameStatistics(currentGame);
        }
        if(stats.containsKey(key)) {
            double current = stats.get(key);
            stats.put(key, current + amount);
        } else {
            stats.put(key, amount);
        }
    }

    public void increment(StatisticType key) {
        increment(key, 1);
    }

    // convenience since card games are all per hand
    public void updateReturnPerHand() {
        updateReturnPerHand(CasinoPracticeStatistics.ReturnPerHand, CasinoPracticeStatistics.Dealt);
    }

    public void updateReturnPerHand(StatisticType perRound, StatisticType countType) {
        if(gameStatistics.keySet().contains(CasinoPracticeGame.getCurrentGame())) {
            double balance = gameStatistics.get(CasinoPracticeGame.getCurrentGame()).get(CasinoPracticeStatistics.Balance);
            double count = gameStatistics.get(CasinoPracticeGame.getCurrentGame()).get(countType);
            gameStatistics.get(CasinoPracticeGame.getCurrentGame()).put(perRound, balance / count);
        }
    }

    private Map<StatisticType, Double> initializeTableGameStatistics(TableGame tableGame) {
        Map<StatisticType, Double> stats = new HashMap<>();
        gameStatistics.put(tableGame, stats);
        return stats;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (TableGame game : gameStatistics.keySet()) {
            builder.append("{");
            builder.append(game);
            builder.append(" : ");
            builder.append(gameStatistics.get(game));
            builder.append("}");
        }
        return builder.toString();
    }
}
