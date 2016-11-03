package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.badlogic.gdx.utils.OrderedMap;
import com.thirteensecondstoburn.CasinoPractice.TableGame;

/**
 * Created by Nick on 10/20/2016.
 */
public class CasinoPracticeStatistics {
    public CasinoPracticeStatistics() {
        gameStatistics = new OrderedMap<>();
    }

    public OrderedMap<TableGame, TableGameStatistics> getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(OrderedMap<TableGame, TableGameStatistics> gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    OrderedMap<TableGame, TableGameStatistics> gameStatistics;
}
