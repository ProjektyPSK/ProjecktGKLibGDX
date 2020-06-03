package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import screen.PlayScreen;

/**
 * Klasa odpowiedzialna za tworzenie obiektu ulepszenia
 */
public class Upgrade extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion shipNew;
    private boolean setToDestroy ;
    private boolean destroyed ;
    private float stateTime;
    private float xPPM;
    private float yPPM;
    private float size;
    private boolean growShrink;

    /**
     * Konstruktor, w którym określana jest grafika picisku oraz jego rozmiar i pozycja sprite
     * @param world
     * @param screen
     * @param x
     * @param y
     */
    public Upgrade(World world, PlayScreen screen, float x, float y){
        super(screen.getAtlas().findRegion("upgrade"));
        this.world = world;
        xPPM = x/ Main.PPM;
        yPPM = y/ Main.PPM;
        size = 20;
        defineUpgrade(x, y);
        shipNew = new TextureRegion(getTexture(),641, 626,30,30);
        setBounds(xPPM, yPPM, 20 / Main.PPM,20 / Main.PPM);
        setRegion(shipNew);
        setToDestroy = false;
        destroyed = false;
        stateTime=0;
        growShrink=true;
    }
    /**
     * Funckja odpowiedzialna za odświerzanie pozycji sprite oraz zmiany jego rozmiaru
     * @param dt czas
     */
    public void update (float dt){
        stateTime += dt;
        if (stateTime > 10) {
            setToDestroy = true;
        }
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            EnemyShip.setUpgrade(null);
        }
        else if (!destroyed){
            if(growShrink) {
                size++;
                setBounds(b2body.getPosition().x - getWidth() /2,b2body.getPosition().y - getHeight() /2, size / Main.PPM, size / Main.PPM);
            }
            if(!growShrink) {
                size--;
                setBounds(b2body.getPosition().x - getWidth() /2,b2body.getPosition().y - getHeight() /2, size / Main.PPM, size / Main.PPM);
            }

            if (size > 30)
                growShrink = false;
            if(size < 10)
                growShrink = true;

        }
    }

    /**
     * Funkcja określa zasady kolizcji rozmiar obiketu oraz jego pozycje
     * @param x
     * @param y
     */
    public  void defineUpgrade(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));
        MassData mass = new MassData();
        mass.mass = 0.0001f;
        b2body.setMassData(mass);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);
        fdef.filter.categoryBits = Main.UPGRADE_BIT;
        fdef.filter.maskBits = Main.SHIP_HERO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(0,-2f);
    }
    /**
     * Funkcja określa warunek do kiedy ma być on rysowany
     * @param batch zbiór grafik
     */
    public void draw(Batch batch){
        if(!destroyed) {
            super.draw(batch);
        }
    }

    public void setSetToDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public void dispose(){
        this.setToDestroy = true;
    }

    public void colideWithEntiti(){
        setToDestroy = true;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean isSetToDestroy() {
        return setToDestroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }


}
