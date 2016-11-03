package com.thirteensecondstoburn.CasinoPractice.Statistics;

/**
 * Created by Nick on 11/3/2016.
 */
public class StatisticType {
    private String key;
    private String display;

    public StatisticType(String key, String display) {
        this.key = key;
        this.display = display;
    }

    public String getKey() {return key;}
    public String getDisplay() {return display;}

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
}
