package com.thirteensecondstoburn.Asteroids;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.Array;

public class AnimatedTile {

	private Array<Array<Decal>> decals = new Array<Array<Decal>>();
	
	public AnimatedTile(int frames, int frameSize, int sets, Texture texture, float scale) {
		for(int set=0; set<sets; set++) {
			Array<Decal> frameDecals = new Array<Decal>();
			for(int frame = 0; frame < frames; frame++) {
				Decal decal = Decal.newDecal(frameSize, frameSize, new TextureRegion(texture, set * frameSize, frame * frameSize, frameSize, frameSize), true);
		        //decal.setScale(scale);
		        frameDecals.add(decal);
			}
			decals.add(frameDecals);
		}
	}
	
	public Decal getDecal(int frame, int set) {
		return decals.get(set).get(frame);
	}
}
