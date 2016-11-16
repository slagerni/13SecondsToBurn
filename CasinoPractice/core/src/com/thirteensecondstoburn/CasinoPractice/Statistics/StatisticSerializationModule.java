package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.thirteensecondstoburn.CasinoPractice.TableGame;

/**
 * Created by Nick on 11/16/2016.
 * Thanks to the Jackson Joda module for the example of how to get this set up
 */
public class StatisticSerializationModule extends SimpleModule {
    public StatisticSerializationModule() {
        super(PackageVersion.VERSION);
//        // deserializers
//        addDeserializer(TableGame.class, new TableGameDeserializer());
//        addDeserializer(StatisticType.class, new StatisticTypeDeserializer());
//
//        // serializers
//        addSerializer(TableGame.class, new TableGameSerializer());
//        addSerializer(StatisticType.class, new StatisticTypeSerializer());

        // key deserializers
        addKeyDeserializer(TableGame.class, new TableGameKeyDeserializer());
        addKeyDeserializer(StatisticType.class, new StatisticTypeKeyDeserializer());
    }

    // yes, will try to avoid duplicate registrations (if MapperFeature enabled)
    @Override
    public String getModuleName() {
        return getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
