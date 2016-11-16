package com.thirteensecondstoburn.CasinoPractice.Statistics;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.thirteensecondstoburn.CasinoPractice.TableGame;

import java.io.IOException;

/**
 * Created by Nick on 11/16/2016.
 */
public class TableGameKeyDeserializer extends KeyDeserializer implements java.io.Serializable {
    @Override
    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        if(key.length() == 0) {
            return null;
        }
        return TableGame.parse(key);
    }
}
