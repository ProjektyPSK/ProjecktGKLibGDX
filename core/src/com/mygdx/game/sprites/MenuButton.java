package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

import screen.MenuScreen;
import screen.PlayScreen;


public class MenuButton extends Sprite {

    public Body b2body;
    private TextureRegion menu;
    private World world;

    public MenuButton(MenuScreen screen, World world, String region, int xAtlas, int yAtlas, int widthAtlas, int heightAtlas, int boundsX, int boundsY, float boundsWidth, float boundsHeight , float x, float y) {
        super(screen.getAtlas().findRegion(region));
        this.world=world;
        defineMenu(x,y);
        menu = new TextureRegion(getTexture(), xAtlas, yAtlas, widthAtlas, heightAtlas);
        setBounds(boundsX / Main.PPM, boundsY, boundsWidth, boundsHeight );
        setRegion(menu);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
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



