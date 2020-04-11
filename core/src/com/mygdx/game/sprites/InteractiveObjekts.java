package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Rectangle;

public class InteractiveObjekts {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveObjekts (World world, TiledMap map,Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float)bounds.getX() + (float)bounds.getWidth() / 2 ,  (float)bounds.getY() + (float)bounds.getHeight() / 2 );

        body = world.createBody(bdef);
        shape.setAsBox((float)bounds.getWidth() / 2 , (float)bounds.getHeight() / 2);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
}
