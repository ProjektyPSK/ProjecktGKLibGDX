package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Scenes.Hud;
import screen.PlayScreen;

public class EnemyShip extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion shipNew;
    private boolean setToDestroy ;
    private boolean destroyed ;
    private float movementTime;
    private float diveTime;
    private float shootTime;
    private Random rand;
    private static List<EnemyBlaster> blasterList = new ArrayList<>();
    private PlayScreen screen;
    private boolean canShoot;
    private boolean loadChecker;
    private boolean diving;
    short movementType;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveDown;
    private short moveLeftOrRight;


    public EnemyShip(World world, PlayScreen screen, float x, float y, short movementType){
        super(screen.getAtlas().findRegion("SpaceShipEnemy"));
        rand = new Random();
        this.world = world;
        this.screen = screen;
        this.movementType = movementType;
        defineShip(x, y);
        shipNew = new TextureRegion(getTexture(),73,33,70,70);
        setBounds(x/ Main.PPM,y/ Main.PPM,70 / Main.PPM,70 / Main.PPM);
        setRegion(shipNew);
        setToDestroy = false;
        destroyed = false;
        movementTime = 0;

        if(movementType == 0) {
            diveTime = 5;
            diving = false;
        }

        if(movementType == 1) {
            moveLeft = false;
            moveRight = true;
            moveDown = false;
            moveLeftOrRight = 1;
        }
    }

    public void update (float dt, Ship player){
        if (setToDestroy && !destroyed){
            if(loadChecker) {
                Hud.updateScore(20);
            }
            world.destroyBody(b2body);
            destroyed = true;
            screen.getEnemyShips().removeValue(this, true);
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
        if (movementType == 0)
        fdef.filter.maskBits =  Main.BORDER | Main.SHIP_ENEMY_BIT | Main.SHIP_HERO_BIT | Main.BLASTER_HERO;
        else if (movementType == 1)
            fdef.filter.maskBits = Main.SHIP_ENEMY_BIT | Main.SHIP_HERO_BIT | Main.BLASTER_HERO;
        else if (movementType == 2)
            fdef.filter.maskBits =  Main.SHIP_HERO_BIT | Main.BLASTER_HERO;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        this.b2body.applyLinearImpulse(new Vector2(0, -0.6f), this.b2body.getWorldCenter(), true);
        canShoot = false;
    }
    public void shoot(float dt){

        if(shootTime  >= (rand.nextInt(5000)  /1.3 ) &&(canShoot)){
           blasterList.add(new EnemyBlaster(world, screen , this.getX() + this.getWidth() / 2, this.getY())) ;
           blasterList.get(blasterList.size()-1).b2body.setLinearVelocity(0,-2f);

           shootTime = 0;
        }
        shootTime += dt;
        if (shootTime > 4 && !diving){
            this.canShoot=true;
        }
    }
    public void movement(float dt, Ship player){
        movementTime += dt;
if(movementType == 0) {
    if (movementTime >= (rand.nextFloat() + 0.5f) && !diving) {
        if (Math.abs(player.getY() - this.getY()) > 8.5f)
            this.b2body.setLinearVelocity(new Vector2(0, -1));
        else if (Math.abs(player.getY() - this.getY()) < 0.5f)
            this.b2body.setLinearVelocity(new Vector2(0, 1));
        else {
            float randX = (rand.nextFloat() - 0.5f);
            float randY = (rand.nextFloat() - 0.5f);

            this.b2body.setLinearVelocity(new Vector2(randX, randY));
        }
        movementTime = 0;
    }
    if (diveTime >= (rand.nextInt(10000)) + 5) {
        if (!diving) {
            this.b2body.setLinearVelocity(new Vector2(0, -2));
            b2body.destroyFixture(b2body.getFixtureList().get(0));

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(35 / Main.PPM);

            fdef.filter.categoryBits = Main.SHIP_ENEMY_BIT;
            fdef.filter.maskBits = Main.BORDER | Main.SHIP_HERO_BIT | Main.BLASTER_HERO;
            fdef.shape = shape;
            b2body.createFixture(fdef).setUserData(this);
            MassData mass = new MassData();
            mass.mass = 0.0001f;
            b2body.setMassData(mass);
        }
        diving = true;

    }
    if (b2body.getTransform().getPosition().y < -1.0f) {
        b2body.setTransform(b2body.getPosition().x, 12f, 0);
        diving = false;

        b2body.destroyFixture(b2body.getFixtureList().get(0));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35 / Main.PPM);

        fdef.filter.categoryBits = Main.SHIP_ENEMY_BIT;
        fdef.filter.maskBits = Main.BORDER | Main.SHIP_ENEMY_BIT | Main.SHIP_HERO_BIT | Main.BLASTER_HERO;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        MassData mass = new MassData();
        mass.mass = 1f;
        b2body.setMassData(mass);

    }
}
if(movementType == 1){
    if(movementTime > 1f && moveRight && canShoot) {
        this.b2body.setLinearVelocity(new Vector2(1f, 0));
        moveRight = false;
        moveDown = true;
        movementTime = 0;
    }
    if(movementTime > 2f && moveDown) {
        moveLeftOrRight ++;
        this.b2body.setLinearVelocity(new Vector2(0, -1f));
        if(moveLeftOrRight == 1) {
            moveRight = true;
            moveDown = false;
        }
        if(moveLeftOrRight == 2){
            moveLeft = true;
            moveDown = false;
            moveLeftOrRight = 0;
        }
        movementTime = 0;
    }
    if(movementTime > 1f && moveLeft) {
        this.b2body.setLinearVelocity(new Vector2(-1f, 0));
        moveLeft = false;
        moveDown = true;
        movementTime = 0;
    }
    if (b2body.getTransform().getPosition().y < -1.0f) {
        b2body.setTransform(b2body.getPosition().x, 11f, 0);
    }

}
if(movementType == 2) {
    if (movementTime < 7f)
        this.b2body.setLinearVelocity(new Vector2(0, -1));
    else {
        this.b2body.setLinearVelocity(new Vector2(2.5f, 0));
        if (b2body.getTransform().getPosition().x > 21.5f) {
            b2body.setTransform(-0.5f, b2body.getPosition().y, 0);
        }
    }

}
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void colideWithEntiti(){
        loadChecker = true;
            setToDestroy = true;
            }

    public void setSetToDestroy(boolean setToDestroy) {
        loadChecker = false;
        this.setToDestroy = setToDestroy;
    }

    public static List<EnemyBlaster> getBlasterList() {
        return blasterList;
    }

    public boolean isCanShoot() {
        return canShoot;
    }
}
