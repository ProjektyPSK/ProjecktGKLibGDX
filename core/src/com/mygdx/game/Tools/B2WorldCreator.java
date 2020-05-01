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
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.sprites.EnemyShip;

import screen.PlayScreen;

public class B2WorldCreator {
    public Array<EnemyShip> enemyShips;
    public Array<EnemyShip> enemyShipsStage2;

    public B2WorldCreator(World world, TiledMap map, PlayScreen screen) {
        BodyDef bdef = new BodyDef();

        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.BORDER;
        Body body;

        // Create body for borders
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() / Main.PPM + rect.getWidth() / 2 / Main.PPM, rect.getY() / Main.PPM + rect.getHeight() / 2 / Main.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Main.BORDER;
            body.createFixture(fdef);
        }
        //create body for enemys ships
        enemyShips = new Array<EnemyShip>();
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemyShips.add(new EnemyShip(world ,screen , rect.getX()  , rect.getY() ));
        }

        enemyShipsStage2 = new Array<EnemyShip>();
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemyShipsStage2.add(new EnemyShip(world ,screen , rect.getX()  , rect.getY() ));
        }
    }

    public Array<EnemyShip> getEnemyShips() {
        return enemyShips;
    }

    public Array<EnemyShip> getEnemyShipsStage2() {
        return enemyShipsStage2;
    }
}
