package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Main;

public class B2WorldCreator {
    public B2WorldCreator (World world, TiledMap map){
    BodyDef bdef= new BodyDef();
    PolygonShape shape= new PolygonShape();
    FixtureDef fdef = new FixtureDef();
    Body body;

    // Creata body for borders
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject .class)){
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() / Main.PPM + rect.getWidth() / 2 / Main.PPM ,  rect.getY() / Main.PPM + rect.getHeight() / 2 / Main.PPM );

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM) ;
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

}
