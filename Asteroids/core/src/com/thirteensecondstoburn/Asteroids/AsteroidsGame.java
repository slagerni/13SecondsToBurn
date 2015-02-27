package com.thirteensecondstoburn.Asteroids;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class AsteroidsGame implements ApplicationListener {
	public OrthographicCamera cam;
    public ModelBatch modelBatch;
    public DecalBatch decalBatch;
    public Environment environment;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public BitmapFont font;
    public BitmapFont razerFont;
    public SpriteBatch batch;
	public BoundingBox shipBox = new BoundingBox();
	public ModelInstance shipInstance;
	public Array<ModelInstance> edgeInstances = new Array<ModelInstance>();
    Array<ModelInstance> asteroids = new Array<ModelInstance>();
    Array<ModelInstance> torps = new Array<ModelInstance>();
    Array<BitmapFontCache> speedLabels = new Array<BitmapFontCache>();
    public float angle = 0f;
    public float rate = 1.0f;
    Model torpModel;
    Model damageSphereModel;
    
    AnimatedTile explosions;
    AnimatedTile torpClouds;
    
    InputPolling inputPoll = new InputPolling();
       
    public boolean loading;
    
    public Data data;
    
	@Override
	public void create() {		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glDepthRangef(.1f, 3000.0f);

		// set up the input
		Gdx.input.setInputProcessor(new BasicInput());
		
        modelBatch = new ModelBatch();
        batch = new SpriteBatch();
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        
        cam = new OrthographicCamera(Gdx.graphics.getHeight()/2, Gdx.graphics.getHeight()/2);
        cam.position.set(0f, 0f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 3000f;
        cam.update();

        decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

        assets = new AssetManager();
        assets.load("data/OriBB.g3dj", Model.class);
        assets.load("data/Asteroid.g3dj", Model.class);
        assets.load("data/KliBB.g3dj", Model.class);
        assets.load("data/damageSphere.g3dj", Model.class);
        assets.load("data/ore.g3dj", Model.class);
        
		assets.load("data/torp.png", Texture.class);
		assets.load("data/explosionTextures.png", Texture.class);
		assets.load("data/torpClouds.png", Texture.class);
		assets.load("data/target.png", Texture.class);
		assets.load("data/fire.png", Texture.class);
		assets.load("data/stars.png", Texture.class);
		assets.load("data/arrowUp.png", Texture.class);
		assets.load("data/arrowDown.png", Texture.class);
		assets.load("data/crosshair.png", Texture.class);
		assets.load("data/lava.jpg", Texture.class);
		assets.load("data/Tundra.jpg", Texture.class);
		assets.load("data/craters.jpg", Texture.class);
		
		assets.load("data/Arial32Shadow.fnt",BitmapFont.class);
		assets.load("data/RazerOblique50Gradient.fnt",BitmapFont.class);
       		
        Material matWhite = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshPartBuilder tileBuilder;
        
        modelBuilder.begin();
        tileBuilder = modelBuilder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, matWhite);
        tileBuilder.setUVRange(0,0,5f/16f,5f/16f);
        tileBuilder.rect(0f, 0f, 0f,   1f, 0f, 0f,    1f, 1f, 0f,  0f, 1f, 0f,  0f, 1f, 0f);
        torpModel = modelBuilder.end();

        loading = true;
        
        data = new Data();
        Data.startNewGame();
        data.start();
        
	}
	
	private void centerAndNormalize(Model model) {
		Mesh m = model.meshes.get(0);
		BoundingBox mBox = m.calculateBoundingBox();
		Vector3 center = mBox.getCenter();
		m.transform(new Matrix4().trn(-center.x, -center.y, -center.z));
		float max = mBox.max.x;
		if(mBox.max.y > max) max = mBox.max.y;
		if(mBox.max.z > max) max = mBox.max.z;
		if(Math.abs(mBox.min.x) > max) max = Math.abs(mBox.min.x);
		if(Math.abs(mBox.min.y) > max) max = Math.abs(mBox.min.y);
		if(Math.abs(mBox.min.z) > max) max = Math.abs(mBox.min.z);
		m.transform(new Matrix4().scl(1.0f/max));
	}
	
	private void doneLoading() {
		Model ship = assets.get("data/OriBB.g3dj", Model.class);
		Model ship2 = assets.get("data/KliBB.g3dj", Model.class);
		damageSphereModel = assets.get("data/damageSphere.g3dj", Model.class);
		font = assets.get("data/Arial32Shadow.fnt");
		razerFont = assets.get("data/RazerOblique50Gradient.fnt");
		BitmapFontCache speedLabel = new BitmapFontCache(razerFont);
		speedLabel.setColor(Color.RED);
		speedLabel.setText("STOPPED", 15f, 680f);
		speedLabels.add(speedLabel);
		speedLabel = new BitmapFontCache(razerFont);
		speedLabel.setColor(Color.YELLOW);
		speedLabel.setText("LOW", 15f, 680f);
		speedLabels.add(speedLabel);
		speedLabel = new BitmapFontCache(razerFont);
		speedLabel.setColor(Color.GREEN);
		speedLabel.setText("HIGH", 15f, 680f);
		speedLabels.add(speedLabel);
		
		centerAndNormalize(ship);
		ship.meshes.get(0).transform(new Matrix4().scl(Data.shipSizes[Data.mainShip.getSizeIndex()]).rotate(0, 0, 1, -90));
		centerAndNormalize(ship2);
		ship2.meshes.get(0).transform(new Matrix4().scl(Data.shipSizes[Data.mainShip.getSizeIndex()]).rotate(0, 0, 1, -90));
		centerAndNormalize(torpModel);
		centerAndNormalize(damageSphereModel);
		
		Model oreModel = assets.get("data/ore.g3dj", Model.class);
		centerAndNormalize(oreModel);
		oreModel.meshes.get(0).transform(new Matrix4().scl(Data.oreSizes[1]));
		
		damageSphereModel.meshes.get(0).transform(new Matrix4().scl(2));
		explosions = new AnimatedTile(5, 66, 4, assets.get("data/explosionTextures.png", Texture.class), 40);
		torpClouds = new AnimatedTile(5, 9, 1, assets.get("data/torpClouds.png", Texture.class), 9);
		
		shipInstance = new ModelInstance(ship2); // Ship 1 and it's fancy texture don't work.
		
		createAsteroids();
		
		torpModel.meshes.get(0).transform(new Matrix4().scl(Data.torpSizes[1]));
		for(int i=0; i < Data.maxTorps; i++) {
			Texture texTile = assets.get("data/torp.png", Texture.class);
			Material mat = new Material(TextureAttribute.createDiffuse(texTile));
			ModelInstance torpInstance = new ModelInstance(torpModel);
			torpInstance.nodes.get(0).parts.get(0).material.set(mat);

			torps.add(torpInstance);
		}
			
		decalBatch.setGroupStrategy(new CameraGroupStrategy(cam));

		loading = false;
	}
	
	private void createAsteroids() {
		Model asteroid = assets.get("data/Asteroid.g3dj", Model.class);
		centerAndNormalize(asteroid);
		asteroid.meshes.get(0).transform(new Matrix4().scl(Data.asteroidSizes[1]));
	
		TextureAttribute newTex;
		for(int i=0; i < Data.asteroidCount; i++) {
			ColorAttribute color;
			if(i%4 == 0) {
				color = ColorAttribute.createDiffuse(1f, .5f, .5f, 1);
			} else if(i%4 == 1) {
				color = ColorAttribute.createDiffuse(.5f, 1f, .5f, 1);
			} else if(i%4 == 2) {
				color = ColorAttribute.createDiffuse(.5f, .5f, 1f, 1);
			} else {
				color = ColorAttribute.createDiffuse(1f, 1f, 1f, 1);
			}
			asteroids.add(new ModelInstance(asteroid));
			asteroids.get(i).materials.get(0).set(color);
			int rand = MovingObject.rand.nextInt(4);
			switch (rand) {
			case 0:
				newTex = new TextureAttribute(TextureAttribute.Diffuse, assets.get("data/lava.jpg", Texture.class));
				asteroids.get(i).materials.get(0).set(newTex);
				break;
			case 1:
				newTex = new TextureAttribute(TextureAttribute.Diffuse, assets.get("data/Tundra.jpg", Texture.class));
				asteroids.get(i).materials.get(0).set(newTex);
				break;
			case 2:
				newTex = new TextureAttribute(TextureAttribute.Diffuse, assets.get("data/craters.jpg", Texture.class));
				asteroids.get(i).materials.get(0).set(newTex);
				break;
			default:
				break;
			}
		}

	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
		font.dispose();
		batch.dispose();
		instances.clear();
		assets.dispose();
		data.stop = true;
		try {
			data.join();
		} catch (InterruptedException e) {
		}
		torpModel.dispose();
		decalBatch.dispose();
	}

	@Override
	public void render() {
		inputPoll.check();
		
		if (loading && assets.update()) {
			doneLoading();
		}
		
		if(!loading) {	
			if(Data.asteroids.size() > asteroids.size) {
				createAsteroids();
			}
			Gdx.gl.glViewport((Gdx.graphics.getWidth() - Gdx.graphics.getHeight())/2, 0, Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
	
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			
			if(Data.currentGameState == Data.GAME_STATE_ACTIVE || Data.currentGameState == Data.GAME_STATE_DEAD) {
				updateInstanceModels();
				
				cam.position.x = (float) Data.mainShip.getXPos();
				cam.position.y = (float) Data.mainShip.getYPos();
				cam.lookAt((float) Data.mainShip.getXPos(), (float) Data.mainShip.getYPos(), (float) 0);
				cam.update();
	
				modelBatch.begin(cam);
				modelBatch.render(instances, environment);
		        modelBatch.end();

		        createStars();
		 
		        decalBatch.flush();
			}
			
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			Sprite targetSprite = new Sprite(assets.get("data/target.png", Texture.class),420, 420);			
			targetSprite.setBounds(0, 0, 420, 420);
			Sprite fireSprite = new Sprite(assets.get("data/fire.png", Texture.class),420, 420);			
			fireSprite.setBounds(Gdx.graphics.getWidth() - 420, 0, 420, 420);

			Sprite speedArrow = null;
			if(Data.mainShip.desiredVelocity > Data.mainShip.getVelocity()) {
				speedArrow = new Sprite(assets.get("data/arrowUp.png", Texture.class),420, 210);
				speedArrow.setV2(.5f);
			} else if(Data.mainShip.desiredVelocity < Data.mainShip.getVelocity()) {
				speedArrow = new Sprite(assets.get("data/arrowDown.png", Texture.class),420, 210);
				speedArrow.setV2(.5f);
			}
			
			batch.begin();
			targetSprite.draw(batch);
			fireSprite.draw(batch);
			if(speedArrow != null) {
				speedArrow.setBounds(0, 420, 420, 210);
				speedArrow.draw(batch);
			}
	        razerFont.draw(batch, "Score " + Data.mainShip.score, 15, Gdx.graphics.getHeight() - 40);
	        razerFont.draw(batch, "Level " + Data.level, 15, Gdx.graphics.getHeight() - 80);
	        razerFont.draw(batch, "Lives " + Data.lives, 15, Gdx.graphics.getHeight() - 120);
	        
	        speedLabels.get(Data.mainShip.speedIndex).draw(batch);
	        
	        if(Data.currentGameState == Data.GAME_STATE_DEAD) {
	        	String text = "" + Data.lives + " Lives Left\nTap to Continue";
	        	TextBounds bounds = calculateMultiline(text, razerFont, 10);
	        	razerFont.drawMultiLine(batch, text, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + bounds.height/2, 10, HAlignment.CENTER);        	
	        } else if (Data.currentGameState == Data.GAME_STATE_OVER) {
	        	String text = "Game Over!\nYour Score Was " + Data.mainShip.score + "\nTap to Try Again";
	        	TextBounds bounds = calculateMultiline(text, razerFont, 10);
	        	razerFont.drawMultiLine(batch, text, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + bounds.height/2, 10, HAlignment.CENTER);        	
	        } else if (Data.currentGameState == Data.GAME_STATE_PRE_GAME){
	        	String text = "Asteroids!\n\nLevel " + Data.level + "\nTap to Start";
	        	TextBounds bounds = calculateMultiline(text, razerFont, 10);
	        	razerFont.drawMultiLine(batch, text, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + bounds.height/2, 10, HAlignment.CENTER);        	
	        }
	        
	        if(Data.debugString.length() > 0) {
	        	font.drawMultiLine(batch, Data.debugString, 15, 300);
	        }
	        batch.end();	        

		}
        
	}
	
	public TextBounds calculateMultiline(String string, BitmapFont font, int gap) {
		String[] parts = string.split("\\n");
		float width = 0;
		float height = font.getBounds(parts[0]).height;
		height = height * parts.length + (parts.length -1 * gap);
		for(int i=0; i<parts.length; ++i) {
			TextBounds bounds = font.getBounds(parts[i]);
			if(bounds.width > width) width = bounds.width;
		}
		TextBounds tb = new TextBounds();
		tb.width = width;
		tb.height = height;
		
		return tb;
	}
	
	private void updateInstanceModels() {
		instances.clear();
		double size = Data.universeSize;

		for(int i = 0; i<Data.asteroids.size(); ++i) {
			Asteroid dAst = Data.asteroids.get(i);
			if(dAst.isActive()) {
				ModelInstance ast = asteroids.get(i);
				double offx, offy;
				offx = Math.abs(Data.mainShip.getXPos() - dAst.getXPos());
				if(offx > size/2) {
					if(Data.mainShip.getXPos() > dAst.getXPos()) {
						offx = size;
					}					
					else {
						offx = -size;
					}
				} else {
					offx = 0;
				}
				offy = Math.abs(Data.mainShip.getYPos() - dAst.getYPos());
				if(offy > size/2) {
					if(Data.mainShip.getYPos() > dAst.getYPos()) {
						offy = size;
					}					
					else {
						offy = -size;
					}
				} else {
					offy = 0;
				}
				ast.transform.setToTranslation((float)(dAst.getXPos()+offx), (float)(dAst.getYPos()+offy), 0f).scl(Data.asteroidScales[Data.asteroids.get(i).getSizeIndex()]).rotate(dAst.xRotation,dAst.yRotation,dAst.zRotation, dAst.getRotation());
				instances.add(ast);
				
				int damageLeft = dAst.getDamage();				
				while(damageLeft > 0) {
					ModelInstance dSphere = new ModelInstance(damageSphereModel, (float)(dAst.getXPos()+offx), (float)(dAst.getYPos()+offy), 0f);
					ColorAttribute color;
					if(damageLeft >= 10) {
						color = ColorAttribute.createDiffuse(0f, 1f, 0f, 1);					
					} else {
						int percent = damageLeft % 10;
						if(percent > 6) {
							color = ColorAttribute.createDiffuse(0f, 1f, 0f, 1);
						} else if(percent > 3) {
							color = ColorAttribute.createDiffuse(1f, 1f, 0f, 1);
						} else {
							color = ColorAttribute.createDiffuse(1f, 0f, 0f, 1);
						}
					}
					dSphere.materials.get(0).set(color);			
					dSphere.transform.rotate(0f, 0f, 1f, 360f/dAst.getMaxDamage()*10 * (damageLeft/10) + 90f + dAst.getRotation()).translate(Data.asteroidSizes[dAst.getSizeIndex()] + 5, 0, 0);
					instances.add(dSphere);
					
					damageLeft -= 10;
				}				
			}
		}

		if(Data.mainShip.isActive()) {
			shipInstance.transform.setToTranslation((float) Data.mainShip.getXPos(), (float) Data.mainShip.getYPos(), 0).rotate(0, 0, 1, Data.mainShip.getDirection()).rotate(1,0,0, -Gdx.input.getPitch()*Data.mainShip.speedIndex);
			instances.add(shipInstance);			
		} else if (Data.mainShip.getState() == MovingObject.STATE_EXPLODE) {
			Decal eInst = createExplosionDecal((float)Data.mainShip.getXPos(), (float)Data.mainShip.getYPos(), Data.mainShip.getExplosionState(), Data.mainShip.explosionIndex);
			decalBatch.add(eInst);
		}

		for(int i=0; i<Data.mainShip.getTorps().length; i++) {
			Torp shipTorp = Data.mainShip.getTorps()[i];
			if(shipTorp.isActive()) {
				ModelInstance t = torps.get(i);
				double offx, offy;
				offx = Math.abs(Data.mainShip.getXPos() - shipTorp.getXPos());
				if(offx > size/2) {
					if(Data.mainShip.getXPos() > shipTorp.getXPos()) {
						offx = size;
					}					
					else {
						offx = -size;
					}
				} else {
					offx = 0;
				}
				offy = Math.abs(Data.mainShip.getYPos() - shipTorp.getYPos());
				if(offy > size/2) {
					if(Data.mainShip.getYPos() > shipTorp.getYPos()) {
						offy = size;
					}					
					else {
						offy = -size;
					}
				} else {
					offy = 0;
				}
				t.transform.setTranslation((float)(offx + shipTorp.getXPos()), (float)(offy+shipTorp.getYPos()), 0f).rotate(0, 0, 1, shipTorp.getRotation());
				instances.add(t);
			}
		}
		
		for(int i=0; i<Data.oreInstances.size(); i++) {
			Ore ore = Data.oreInstances.get(i);
			ModelInstance oreInstance = new ModelInstance(assets.get("data/ore.g3dj", Model.class));
			double offx, offy;
			offx = Math.abs(Data.mainShip.getXPos() - ore.getXPos());
			if(offx > size/2) {
				if(Data.mainShip.getXPos() > ore.getXPos()) {
					offx = size;
				}					
				else {
					offx = -size;
				}
			} else {
				offx = 0;
			}
			offy = Math.abs(Data.mainShip.getYPos() - ore.getYPos());
			if(offy > size/2) {
				if(Data.mainShip.getYPos() > ore.getYPos()) {
					offy = size;
				}					
				else {
					offy = -size;
				}
			} else {
				offy = 0;
			}
			oreInstance.transform.setToTranslation((float)(ore.getXPos()+offx), (float)(ore.getYPos()+offy), 0f).scl(Data.oreScales[ore.getSizeIndex()]).rotate(ore.xRotation,ore.yRotation,ore.zRotation, ore.getRotation());
			instances.add(oreInstance);
			
		}
		
		// Decal things need to come after models or the Z- axis is wrong
		if(Data.mainShip.isActive()) {
			Decal crosshair = Decal.newDecal(10f, 10f, new TextureRegion(assets.get("data/crosshair.png", Texture.class)), true);
			crosshair.setPosition((float)Data.mainShip.getXPos() - 75f, (float)Data.mainShip.getYPos() - 75f, 85);
			crosshair.transformationOffset = new Vector2(75f, 75f);		
			crosshair.rotateZ(Data.mainShip.targetingDirection + 135);
			decalBatch.add(crosshair);
		}
		
		for(int i = 0; i<Data.asteroids.size(); ++i) {
			Asteroid dAst = Data.asteroids.get(i);
			if(dAst.getExplosionState() > -1) {
				double offx, offy;
				offx = Math.abs(Data.mainShip.getXPos() - dAst.getXPos());
				if(offx > size/2) {
					if(Data.mainShip.getXPos() > dAst.getXPos()) {
						offx = size;
					}					
					else {
						offx = -size;
					}
				} else {
					offx = 0;
				}
				offy = Math.abs(Data.mainShip.getYPos() - dAst.getYPos());
				if(offy > size/2) {
					if(Data.mainShip.getYPos() > dAst.getYPos()) {
						offy = size;
					}					
					else {
						offy = -size;
					}
				} else {
					offy = 0;
				}

				// just in case since Data is out of sync potentially
				try {
				int state = dAst.getExplosionState();
					if(state > -1) {
						Decal eInst = createExplosionDecal((float)dAst.getXPos(), (float)dAst.getYPos(), state, dAst.explosionIndex);
						eInst.setScale(Data.asteroidScales[dAst.getSizeIndex()]);
						decalBatch.add(eInst);
					}
				}
				catch (Exception ex) {
					Gdx.app.error("Drawing", "Error drawing explosions" + ex);
				}

			}
		}		
		
		// again, group torp explosions together to reduce texture switching
		for(int i=0; i<Data.mainShip.getTorps().length; i++) {
			Torp shipTorp = Data.mainShip.getTorps()[i];
			if(shipTorp.getState() == Torp.STATE_EXPLODE) {
				double offx, offy;
				offx = Math.abs(Data.mainShip.getXPos() - shipTorp.getXPos());
				if(offx > size/2) {
					if(Data.mainShip.getXPos() > shipTorp.getXPos()) {
						offx = size;
					}					
					else {
						offx = -size;
					}
				} else {
					offx = 0;
				}
				offy = Math.abs(Data.mainShip.getYPos() - shipTorp.getYPos());
				if(offy > size/2) {
					if(Data.mainShip.getYPos() > shipTorp.getYPos()) {
						offy = size;
					}					
					else {
						offy = -size;
					}
				} else {
					offy = 0;
				}
				// just in case since Data is out of sync potentially
				try {
					int state = shipTorp.getExplosionState();
					if(state > -1) {
						Decal tInst = createTorpCloudDecal((float)(offx + shipTorp.getXPos()), (float)(offy+shipTorp.getYPos()), shipTorp.getExplosionState(), shipTorp.explosionIndex);
						decalBatch.add(tInst);
					}
				}
				catch (Exception ex) {
					Gdx.app.error("Drawing", "Error drawing torp explosions" + ex);
				}
			}
		}

	}

	private Decal createExplosionDecal(float xPos, float yPos, int frame, int index) {
		double size = Data.universeSize;
		double offx, offy;
		offx = Math.abs(Data.mainShip.getXPos() - xPos);
		if(offx > size/2) {
			if(Data.mainShip.getXPos() > xPos) {
				offx = size;
			}					
			else {
				offx = -size;
			}
		} else {
			offx = 0;
		}
		offy = Math.abs(Data.mainShip.getYPos() - yPos);
		if(offy > size/2) {
			if(Data.mainShip.getYPos() > yPos) {
				offy = size;
			}					
			else {
				offy = -size;
			}
		} else {
			offy = 0;
		}

		Decal eInstance = explosions.getDecal(frame, index);
		eInstance.setPosition((float)(xPos + offx), (float)(yPos+offy), 0f);
		return eInstance;
	}
	
	private Decal createTorpCloudDecal(float xPos, float yPos, int frame, int index) {
		double size = Data.universeSize;
		double offx, offy;
		offx = Math.abs(Data.mainShip.getXPos() - xPos);
		if(offx > size/2) {
			if(Data.mainShip.getXPos() > xPos) {
				offx = size;
			}					
			else {
				offx = -size;
			}
		} else {
			offx = 0;
		}
		offy = Math.abs(Data.mainShip.getYPos() - yPos);
		if(offy > size/2) {
			if(Data.mainShip.getYPos() > yPos) {
				offy = size;
			}					
			else {
				offy = -size;
			}
		} else {
			offy = 0;
		}

		Decal tInstance = torpClouds.getDecal(frame, index);
		tInstance.setPosition((float)(xPos + offx), (float)(yPos+offy), 0f);
		return tInstance;
	}
	
	private void createStars() {
		for(int i=-Data.universeSize; i<=Data.universeSize; i+=Data.universeSize) {
			for(int j=-Data.universeSize; j<=Data.universeSize; j+=Data.universeSize) {
				Texture starTexture = assets.get("data/stars.png", Texture.class);
				starTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
				TextureRegion tr = new TextureRegion(starTexture);
				tr.setU2(((float)Gdx.graphics.getHeight())/512f);
				tr.setV2(((float)Gdx.graphics.getHeight())/512f);
				Decal stars = Decal.newDecal(Data.universeSize, Data.universeSize, tr);
				stars.setPosition(i + Data.universeSize / 2.0f, j + Data.universeSize / 2.0f, -2000f);
				decalBatch.add(stars);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
