package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Nick on 11/3/2016.
 */
public class StatisticType implements Comparable{
    private String key;
    private String display;
    private int displayOrder;

    @JsonCreator
    public StatisticType(@JsonProperty("key") String key, @JsonProperty("display") String display, @JsonProperty("displayOrder") int displayOrder) {
        this.key = key;
        this.display = display;
        this.displayOrder = displayOrder;
    }

    public String getKey() {return key;}
    public String getDisplay() {return display;}
    public int getDisplayOrder() {return displayOrder;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!StatisticType.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final StatisticType other = (StatisticType) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "COULD NOT WRITE STATISTIC TYPE";
        }
    }

    public static StatisticType parse(String toParse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(toParse, StatisticType.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * key.hashCode();
    }

    // sort these by display order
    @Override
    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (!StatisticType.class.isAssignableFrom(obj.getClass())) {
            return -1;
        }
        final StatisticType other = (StatisticType) obj;
        return ((Integer)displayOrder).compareTo(other.displayOrder);
    }
}
