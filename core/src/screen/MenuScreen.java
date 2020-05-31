package screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.sprites.MenuButton;

import Scenes.Hud;

public class MenuScreen implements Screen {
    private final float PLAY_X = 1000;
    private final float PLAY_Y = 800;
    private final float BUTTON_BOUNDS_WIDTH = 440;
    private final float BUTTON_BOUNDS_HEIGHT = 160;
    private final float EXIT_X = 1000;
    private final float EXIT_Y = 600;
    private Viewport viewport;
    private Stage stage;
    private Box2DDebugRenderer b2dr;

    TextureAtlas atlas;
    MenuButton play;
    MenuButton exit;

    private World world;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private float screenHeight = 1080;
    private float screenWidth = 2100;
    private int tryb;
    private Hud hud;




    private Game game;

    public MenuScreen (Game game, int tryb){
        this.game=game;
        this.tryb = tryb;
        world = new World(new Vector2(0,0), true);
        atlas= new TextureAtlas("Menu.pack");
        viewport = new FitViewport(Main.V_WIDTH , Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport , ((Main) game).batch);
        b2dr = new Box2DDebugRenderer();
        gameCam= new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH  ,Main.V_HEIGHT , gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight()/2 , 0);
        play = new MenuButton(this,world, "PLAY", 1,418,900,415 , 500 ,500,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT , PLAY_X, PLAY_Y);
        exit = new MenuButton(this,world, "EXIT", 1,418,900,415 , 500 ,500,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT, EXIT_X, EXIT_Y);
        if(tryb ==2 )
        hud = new Hud(((Main) game).batch);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        b2dr.render(world, gameCam.combined);
        play.update(delta);
        exit.update(delta);

        if(Gdx.input.justTouched()){
            float maxScreenHeightFloat = Main.V_HEIGHT;
            float maxScreenWidthFloat = Main.V_WIDTH;
            float scalaX = maxScreenWidthFloat / screenWidth;
            float scalaY = maxScreenHeightFloat/ screenHeight;

            if(Gdx.input.getX() > (PLAY_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                    Gdx.input.getX() < (PLAY_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                   screenHeight - Gdx.input.getY() < (PLAY_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT/scalaY/2) &&
                    screenHeight - Gdx.input.getY() > (PLAY_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT/scalaY/2)) {
                game.setScreen(new PlayScreen(((Main) game), world));
                this.dispose();

            }
            if(Gdx.input.getX() > (EXIT_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                    Gdx.input.getX() < (EXIT_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                    screenHeight - Gdx.input.getY() < (EXIT_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT / scalaY / 2) &&
                    screenHeight - Gdx.input.getY() > (EXIT_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT / scalaY / 2)){
                Gdx.app.exit();

            }



        }
        Gdx.gl.glClearColor(0.3f,0.5f,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ((Main) game).batch.setProjectionMatrix(gameCam.combined);
        ((Main) game).batch.begin();

        play.draw(((Main) game).batch);
        exit.draw(((Main) game).batch);

        ((Main) game).batch.end();
        if(tryb ==2) {
            ((Main) game).batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
            hud.update(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        screenHeight = height;
        screenWidth = width;
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
    stage.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }
}
