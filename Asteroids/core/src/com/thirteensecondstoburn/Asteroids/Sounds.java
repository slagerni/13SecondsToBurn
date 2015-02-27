package com.thirteensecondstoburn.Asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    private static Sound torpFireSound = Gdx.audio.newSound(Gdx.files.internal("data/fireTorp.ogg"));
    private static Sound torpExplodeSound = Gdx.audio.newSound(Gdx.files.internal("data/torpHit.ogg"));
    private static Sound explosionSelfSound = Gdx.audio.newSound(Gdx.files.internal("data/explosionSelf.ogg"));
    private static Sound explosionOtherSound = Gdx.audio.newSound(Gdx.files.internal("data/explosionOther.ogg"));
    private static Sound speedDownSound = Gdx.audio.newSound(Gdx.files.internal("data/speedDown.ogg"));
    private static Sound speedUpSound = Gdx.audio.newSound(Gdx.files.internal("data/speedUp.ogg"));
    private static Sound collectSound = Gdx.audio.newSound(Gdx.files.internal("data/collect.ogg"));

    public static long fireTorp() {
    	return torpFireSound.play(.25f);
    }
    
    public static long torpHit() {
    	return torpExplodeSound.play(.75f);
    }
    
    public static long myExplosion() {
    	return explosionSelfSound.play(.25f);
    }
    
    public static long otherExplode() {
    	return explosionOtherSound.play(.75f);
    }
    
    public static long speedDown() {
    	return speedDownSound.play(.25f);
    }
    
    public static long speedUp() {
    	return speedUpSound.play(.25f);    	
    }

    public static long collect() {
    	return collectSound.play(.5f);    	
    }
}
