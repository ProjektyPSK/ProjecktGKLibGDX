package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ship extends Sprite {
    public World world;
    public Body b2body;

    public Ship(World world){
        this.world = world;
        defineShip();
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
