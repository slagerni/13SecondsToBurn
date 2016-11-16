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
    public static final StatisticType Plays = new StatisticType("plays", "Plays (Times Dealt, Spun or Rolled)");
    public static final StatisticType Won = new StatisticType("won", "Won");
    public static final StatisticType Lost = new StatisticType("lost", "Lost");
    public static final StatisticType Wagered = new StatisticType("wagered", "Wagered");

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

    public void Increment(StatisticType key, double amount) {
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

    public void Increment(StatisticType key) {
        Increment(key, 1);
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
