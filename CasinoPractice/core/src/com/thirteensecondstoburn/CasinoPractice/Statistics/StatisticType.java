package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Nick on 11/3/2016.
 */
public class StatisticType {
    private String key;
    private String display;

    @JsonCreator
    public StatisticType(@JsonProperty("key") String key, @JsonProperty("display") String display) {
        this.key = key;
        this.display = display;
    }

    public String getKey() {return key;}
    public String getDisplay() {return display;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            System.out.println("StatisticType equals failed because null");
            return false;
        }
        if (!StatisticType.class.isAssignableFrom(obj.getClass())) {
            System.out.println("StatisticType equals failed because not Statistic Type");
            return false;
        }
        final StatisticType other = (StatisticType) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            System.out.println("StatisticType equals failed because not equal");
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
//        return "{" +
//                "key='" + key + '\'' +
//                ", display='" + display + '\'' +
//                '}';
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
}
