package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import screen.PlayScreen;

public class Ship extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion shipNew;

    public Ship(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("SpaceShipHero"));
        this.world = world;
        defineShip();
        shipNew = new TextureRegion(getTexture(),145,1,70,70);
        setBounds(500,500,70,70);
        setRegion(shipNew);
    }

    public void update (float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public  void defineShip(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(500,500);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody((bdef));

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35);

        fdef.shape = shape;
        b2body.createFixture(fdef);


    }
}
