package screen;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

public class PlayScreen implements Screen {
    private Main game;
    private Hud hud;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    private World world;
    private Box2DDebugRenderer b2dr;
    public PlayScreen (Main game){
        this.game=game;
        gamecam= new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH ,Main.V_HEIGHT , gamecam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight()/2 , 0);

        world= new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef= new BodyDef();
        PolygonShape shape= new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Creata body for borders
     /*   for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2 , rect.getY() + rect.getHeight() / 2 );

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 , rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }*/
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void hanleInput(float dt){

        if (Gdx.input.isTouched()){
            gamecam.position.y += 1000*dt;
        }

    }
    public void update(float dt) {
        hanleInput(dt);
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void resize(int width, int height) {
    gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
