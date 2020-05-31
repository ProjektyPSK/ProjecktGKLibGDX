package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;
import screen.PlayScreen;

/**
 * Jest odpowiedzialna za tworzenie dynamicznego tła
 */
public class Background extends Sprite {
    public World world;
    public Body b2body;
    private PlayScreen screen;
    boolean canShoot;
    int layer;
    float x;
    float y;

    /**
     *
     * @param world
     * @param screen
     * @param x
     * @param y
     * @param layer warstwa tła, im bliżej środka tym większy numer warstwy
     * @param path ścieszka do pliku z grafiką
     */
    public Background(World world, PlayScreen screen, float x, float y, int layer , String path){
        super(new Texture(Gdx.files.internal(path)), 0, 0);
        this.world = world;
        this.screen = screen;
        this.layer = layer;
        this.x = x;
        this.y = y;
        defineBacground(x,  y, layer);
        switch (layer) {
            case 0 :
                setBounds(x / Main.PPM, y / Main.PPM, 490 / Main.PPM, 1750 / Main.PPM);
                break;
            case 1 :
                setBounds(x / Main.PPM, y / Main.PPM, 350 / Main.PPM, 1750 / Main.PPM);
                break;
            case 2 :
                setBounds(x / Main.PPM, y / Main.PPM, 210 / Main.PPM, 1750 / Main.PPM);
                break;

        }
    }

    /**
     * funkcja odpowiedzialna za "przyklejenie" sprite'a do ciała obiektu
     * @param dt czas
     */
    public void update (float dt){
        setPosition(b2body.getPosition().x   , b2body.getPosition().y  );
    }

    /**
     * Funkcja definiuje rozmiar ciała obiektu oraz nadaje mu prędkość w zależności od warstwy, na której leży
     * @param x
     * @param y
     * @param layer warstwa tła, im bliżej środka tym większy numer warstwy
     */
    public  void defineBacground(float x, float y, int layer){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Main.PPM,y / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35 / Main.PPM);
        fdef.filter.categoryBits = Main.DEFAULT;
        fdef.filter.maskBits = Main.DEFAULT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        switch (layer) {
            case 0 :
                this.b2body.applyLinearImpulse(new Vector2(0, -1.0f), this.b2body.getWorldCenter(), true);
                break;
            case 1 :
                this.b2body.applyLinearImpulse(new Vector2(0, -2.0f), this.b2body.getWorldCenter(), true);
                break;
            case 2 :
                this.b2body.applyLinearImpulse(new Vector2(0, -3.0f), this.b2body.getWorldCenter(), true);
                break;
        }

        canShoot = false;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public int getLayer() {
        return layer;
    }

    public Body getB2body() {
        return b2body;
    }
}
