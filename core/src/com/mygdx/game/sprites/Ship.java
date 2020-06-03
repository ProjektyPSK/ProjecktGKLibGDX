package com.mygdx.game.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import Scenes.Hud;
import screen.MenuScreen;
import screen.PlayScreen;

/**
 * Klasa statku gracza
 */
public class Ship extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion shipNew;
    private float shootTime;
    private boolean canShoot;
    private int lives;
    private Game game;
    private PlayScreen screen;
    private int shootType;

    /**
     * Konstruktor, w którym określana jest grafika picisku oraz jego rozmiar sprite
     * @param world
     * @param screen
     */
    public Ship(World world, PlayScreen screen , Game game){
        super(screen.getAtlas().findRegion("SpaceShipHero"));
        this.world = world;
        defineShip();
        this.game = game;
        this.screen = screen;
        shipNew = new TextureRegion(getTexture(),1,1,70,70);
        setBounds(500/ Main.PPM,500/ Main.PPM,70 / Main.PPM,70 / Main.PPM);
        setRegion(shipNew);
        shootTime = 1.2f;
        lives = 20;
        shootType = 1;
    }
    /**
     * funkcja odpowiedzialna za "przyklejenie" sprite'a do ciała obiektu
     * @param dt czas
     */
    public void update (float dt){
        setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2);
    }
    /**
     * Funkcja określa zasady kolizcji rozmiar obiketu oraz nadaje mu "ciało"

     */
    public  void defineShip(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(1000 / Main.PPM,50 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35 / Main.PPM);
        fdef.filter.categoryBits = Main.SHIP_HERO_BIT;
        fdef.filter.maskBits = Main.BORDER | Main.SHIP_ENEMY_BIT | Main.BLASTER_ENEMY |Main.UPGRADE_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        canShoot = false;

    }

    public float getShootTime() {
        return shootTime;
    }

    public void setShootTime(float shootTime) {
        this.shootTime = shootTime;
    }


    public boolean isCanShoot() {
        return canShoot;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    /**
     * określa zachowanie się w przypadku kolizji ze statkiem przeciwnika lub pociskiem
     */
    public void colideWithEntiti() {
        if (this.lives > 1) {
            this.lives--;
            Hud.updateLives(-1);
            Hud.updateScore(-300);
            if(shootTime > 0.2f) {
                shootTime *= 1.15;
            }
            else if (shootType > 1){
                shootType --;
            }
            else {
                shootTime *= 1.15;
            }

        }
        else {
            game.setScreen(new MenuScreen(game,2));
            screen.dispose();
        }
    }


    /**
     * określa zachowanie się w przypadku kolizcji z ulepszeniem
     */

    public void colideWithUpgrade (){
        if(shootTime > 0.2f) {
            shootTime *= 0.85;
        }
        else if (shootType < 3){
            shootType ++;
        }
        else {
            Hud.updateScore(300);
        }

    }
    public int getShootType() {
        return shootType;
    }

}
