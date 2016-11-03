package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.badlogic.gdx.utils.OrderedMap;
import com.thirteensecondstoburn.CasinoPractice.Statistics.StatisticType;

/**
 * Created by Nick on 10/20/2016.
 */
public class TableGameStatistics {

    // standard statistic types
    public static StatisticType Plays = new StatisticType("plays", "Plays");
    public static StatisticType Won = new StatisticType("won", "Won");
    public static StatisticType Lost = new StatisticType("lost", "Lost");
    public static StatisticType Wagered = new StatisticType("wagered", "Wagered");



    // for things that need to go up by an arbitrary amount
    public int Increment(StatisticType type, Integer value) {
        if (!gameStatistics.containsKey(type)) {
            return gameStatistics.put(type, value);
        } else {
            Integer oldValue = gameStatistics.get(type);
            return gameStatistics.put(type, oldValue + value);
        }
    }

    // for things that are just a count
    public int Increment(StatisticType type) {
        return Increment(type, 1);
    }

    private OrderedMap<StatisticType, Integer> gameStatistics;
}
