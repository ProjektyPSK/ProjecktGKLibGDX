package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Main;
import screen.PlayScreen;

public class BossShip extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion shipNew;
    private boolean setToDestroy ;
    private boolean destroyed ;
    private float stateTime;
    private float size;
    private boolean growShrink;


    public BossShip (World world, PlayScreen screen, float x, float y){
        super(screen.getAtlas().findRegion("Hero_blaster"));
        this.world = world;
        defineBlaster(x, y);
        shipNew = new TextureRegion(getTexture(),217,73,30,30);
        setBounds(x/ Main.PPM,y/ Main.PPM,20 / Main.PPM,20 / Main.PPM);
        setRegion(shipNew);
        setToDestroy = false;
        destroyed = false;
        stateTime=0;
        growShrink = true;
        size = 20;
        setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2);

    }

    public void update (float dt){

        stateTime += dt;
        if (stateTime > 1.2f) {
            setToDestroy = true;
        }
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
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

    public  void defineBlaster(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Main.PPM);
        fdef.filter.categoryBits = Main.BLASTER_HERO;
        fdef.filter.maskBits =  Main.SHIP_ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void draw(Batch batch){
        if(!destroyed || stateTime < 1.2f)
            super.draw(batch);
    }



}
