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
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.EnemyShip;

import java.util.ArrayList;
import java.util.List;

import screen.PlayScreen;

public class B2WorldCreator {
    public Array<EnemyShip> enemyShips;
    public Array<EnemyShip> enemyShipsStage2;
    private TiledMap map;
    private World world;
    private PlayScreen screen;

    public B2WorldCreator(World world, TiledMap map, PlayScreen screen) {
        BodyDef bdef = new BodyDef();
        this.map = map;
        this.world = world;
        this.screen = screen;
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.BORDER;
        Body body;

        // Create body for borders
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() / Main.PPM + rect.getWidth() / 2 / Main.PPM, rect.getY() / Main.PPM + rect.getHeight() / 2 / Main.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / Main.PPM, rect.getHeight() / 2 / Main.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Main.BORDER;
            body.createFixture(fdef);
        }


    }
    public List<Background> createBackground (int layer, String path){
        List<Background> background = new ArrayList<>();
        for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
           background.add(new Background(world, screen, rect.getX(), rect.getY() , layer, path));
        }
        return background;
    }
    public void CreateNewWave (Array<EnemyShip> EnemyShip, int layer, short movementType) {
        for (MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            EnemyShip.add(new EnemyShip(world, screen, rect.getX(), rect.getY(), movementType));
        }
    }

    public Array<EnemyShip> getEnemyShips() {
        return enemyShips;
    }

    public Array<EnemyShip> getEnemyShipsStage2() {
        return enemyShipsStage2;
    }
}
