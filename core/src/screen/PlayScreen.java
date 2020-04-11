package screen;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.sprites.Ship;

import static sun.audio.AudioPlayer.player;

public class PlayScreen implements Screen {
    private Main game;
    private Hud hud;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Ship player;
    private TextureAtlas atlas;


    private World world;
    private Box2DDebugRenderer b2dr;
    public PlayScreen (Main game){
        atlas= new TextureAtlas("BigSprite.atlas");

        this.game=game;
        gamecam= new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH ,Main.V_HEIGHT , gamecam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight()/2 , 0);

        world = new World(new Vector2(0,+20), true);
        b2dr = new Box2DDebugRenderer();
        player = new Ship(world);


        new B2WorldCreator (world, map);
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

    public void handleInput(float dt){

      /*  if (Gdx.input.isTouched()){
            gamecam.position.y += 1000*dt;
        }*/
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0, 400f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.b2body.applyLinearImpulse(new Vector2(0, -400f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&&(player.b2body.getLinearVelocity().x <= 2)){
            player.b2body.applyLinearImpulse(new Vector2(200f, -0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)&&(player.b2body.getLinearVelocity().x >= 2)){
            player.b2body.applyLinearImpulse(new Vector2(-200f, -0), player.b2body.getWorldCenter(), true);
        }

    }
    public void update(float dt) {
        world.step(1/60f, 6 , 2);

        gamecam.position.y = player.b2body.getPosition().y;

        handleInput(dt);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
