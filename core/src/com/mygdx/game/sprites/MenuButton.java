package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import screen.MenuScreen;
import screen.PlayScreen;

/**
 * Klasa przycisku dostepnego w menu na początku gry oraz w menu pauzy
 */
public class MenuButton extends Sprite {

    public Body b2body;
    private TextureRegion menu;
    private World world;

    /**
     * Konstruktor przycsku, tworzonego w klasie MenuScreen
     * @param screen
     * @param world
     * @param region region w atlasie Sprite'ów
     * @param xAtlas pozycja Y w atlasie Sprite'ów
     * @param yAtlas pozycja X w atlasie Sprite'ów
     * @param widthAtlas szerokość w atlasie Sprite'ów
     * @param heightAtlas wysokość w atlasie Sprite'ów
     * @param boundsX  pozycja Y przycisku
     * @param boundsY pozycja Y przycisku
     * @param boundsWidth szerokość przycisku
     * @param boundsHeight  wysokość przycisku
     * @param x
     * @param y
     */
    public MenuButton(MenuScreen screen, World world, String region, int xAtlas, int yAtlas, int widthAtlas, int heightAtlas, int boundsX, int boundsY, float boundsWidth, float boundsHeight , float x, float y) {
        super(screen.getAtlas().findRegion(region));
        this.world=world;
        defineMenu(x,y);
        menu = new TextureRegion(getTexture(), xAtlas, yAtlas, widthAtlas, heightAtlas);
        setBounds(boundsX / Main.PPM, boundsY, boundsWidth, boundsHeight );
        setRegion(menu);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    /**
     * Konstruktor przycsku, tworzonego w klasie PlayScreen
     * @param screen
     * @param world
     * @param region region w atlasie Sprite'ów
     * @param xAtlas pozycja Y w atlasie Sprite'ów
     * @param yAtlas pozycja X w atlasie Sprite'ów
     * @param widthAtlas szerokość w atlasie Sprite'ów
     * @param heightAtlas wysokość w atlasie Sprite'ów
     * @param boundsX  pozycja Y przycisku
     * @param boundsY pozycja Y przycisku
     * @param boundsWidth szerokość przycisku
     * @param boundsHeight  wysokość przycisku
     * @param x
     * @param y
     */
    public MenuButton(PlayScreen screen, World world, String region, int xAtlas, int yAtlas, int widthAtlas, int heightAtlas, int boundsX, int boundsY, float boundsWidth, float boundsHeight , float x, float y) {
        super(screen.getMenuAtlas().findRegion(region));
        this.world=world;
        defineMenu(x,y);
        menu = new TextureRegion(getTexture(), xAtlas, yAtlas, widthAtlas, heightAtlas);
        setBounds(boundsX / Main.PPM, boundsY, boundsWidth, boundsHeight );
        setRegion(menu);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }



    public void update(float dt) {
    }

    /**
     * Tworzenie ciała dla przycisków
     * @param x
     * @param y
     */
    public void defineMenu(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1/Main.PPM);
        fdef.filter.categoryBits = Main.MENU_BIT;
        fdef.filter.maskBits = Main.DEFAULT;
        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData(this);

    }

}



