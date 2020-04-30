package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import java.util.Random;

import screen.PlayScreen;

public class EnemyShip extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion shipNew;
    private boolean setToDestroy ;
    private boolean destroyed ;
    private float movementTime;
    private float shootTime;
    private Random rand;
    private EnemyBlaster blaster;
    private PlayScreen screen;


    public EnemyShip(World world, PlayScreen screen, float x, float y){
        super(screen.getAtlas().findRegion("SpaceShipEnemy"));
        rand = new Random();
        this.world = world;
        this.screen = screen;
        defineShip(x, y);
        shipNew = new TextureRegion(getTexture(),73,1,70,70);
        setBounds(x/ Main.PPM,y/ Main.PPM,70 / Main.PPM,70 / Main.PPM);
        setRegion(shipNew);
        setToDestroy = false;
        destroyed = false;
        movementTime = 0;
    }

    public void update (float dt, Ship player){

        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

        else if (!destroyed){
            setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2);
            movement(dt , player);
            shoot(dt);
        }


    }

    public  void defineShip(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Main.PPM,y / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35 / Main.PPM);
        fdef.filter.categoryBits = Main.SHIP_ENEMY_BIT;
        fdef.filter.maskBits = Main.DEFAULT | Main.BORDER | Main.SHIP_ENEMY_BIT | Main.SHIP_HERO_BIT | Main.BLASTER_HERO;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        this.b2body.applyLinearImpulse(new Vector2(0, -0.2f), this.b2body.getWorldCenter(), true);
    }
    public void shoot(float dt){
        shootTime += dt;
        if(shootTime  >= (rand.nextInt(5000)  /1.3 )){
           blaster =  new EnemyBlaster(world, screen , this.getX() + this.getWidth() / 2, this.getY() - 1);
           blaster.b2body.applyLinearImpulse(new Vector2(0, -2f),blaster.b2body.getWorldCenter(), true);
           shootTime = 0;
        }
    }
    public void movement(float dt, Ship player){
        movementTime += dt;
        if(movementTime  >= (rand.nextFloat() + 0.5f)) {
            if (Math.abs(player.getY() - this.getY()) > 9)
                this.b2body.setLinearVelocity(new Vector2(0, -1));
            else if (Math.abs(player.getY() - this.getY()) < 0.5f)
                this.b2body.setLinearVelocity(new Vector2(0, 1));
            else {
                float randX = (rand.nextFloat() - 0.5f) ;
                float randY = (rand.nextFloat() - 0.5f) ;

                this.b2body.setLinearVelocity(new Vector2(randX, randY));
            }
                movementTime = 0;
        }

    }

    public void reverceVelocity (boolean x , boolean y){
  //     if (x)
          //  velocity
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void colideWithEntiti(){
            setToDestroy = true;
            }
}
