package screen;

import Scenes.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.sprites.Blaster;
import com.mygdx.game.sprites.EnemyBlaster;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.MenuButton;
import com.mygdx.game.sprites.Ship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PlayScreen implements Screen {
    private final float RESUME_X = 1000 / Main.PPM;
    private final float RESUME_Y = 800 / Main.PPM;
    private final float SAVE_X =1000 / Main.PPM;
    private final float SAVE_Y = 620 / Main.PPM;
    private final float LOAD_X = 1000 / Main.PPM;
    private final float LOAD_Y = 440 / Main.PPM;
    private final float EXIT_X = 1000 / Main.PPM;
    private final float EXIT_Y = 260 / Main.PPM;
    private final float BUTTON_BOUNDS_WIDTH = 440 / Main.PPM;
    private final float BUTTON_BOUNDS_HEIGHT = 160 / Main.PPM;
    private final float MENU_X = 40 / Main.PPM;
    private final float MENU_Y = 1040 / Main.PPM;
    private final float BUTTON_MENU_WIDTH = 40 / Main.PPM;
    private final float BUTTON_MENU_HEIGHT = 40 / Main.PPM;


    private Main game;
    private Hud hud;
    private OrthographicCamera gameCam;
    private Viewport gamePort;


    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Ship player;
    private TextureAtlas atlas;
    private TextureAtlas menuAtlas;
    private float lastTouch;


    private World world;
    private Box2DDebugRenderer b2dr;
    private EnemyShip enemy;
    private List <Blaster> blasterList;
    private List <Blaster> blasterListToRemove;
    private float lastHeroShotTimer;
    private B2WorldCreator creator;
    private List <EnemyBlaster> enemyBlasterListToRemove;
    private Array<EnemyShip> enemyShips;
    private int waveCounter;
    private boolean menuActive;
    private boolean pauseActive;
    MenuButton openMenu;
    MenuButton resume;
    MenuButton save;
    MenuButton load;
    MenuButton exit;
    float scalaX;
    float scalaY;
    float screenHeight;
    float screenWidth;
    int beginWaveScore;


    public PlayScreen (Main game , World world){
        atlas= new TextureAtlas("BigSprite.atlas");
        menuAtlas= new TextureAtlas("Menu.pack");

        this.world=world;
        this.game=game;
        gameCam= new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM ,Main.V_HEIGHT / Main.PPM , gameCam);
        player = new Ship(world, this, game);
        hud = new Hud(game.batch , player);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map , 1/ Main.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight()/2 , 0);


        b2dr = new Box2DDebugRenderer();



        creator = new B2WorldCreator (world, map, this);
        lastTouch=player.b2body.getPosition().x;


        world.setContactListener(new WorldContactListener());

        blasterList = new ArrayList<>();
        blasterListToRemove = new ArrayList<>();
        enemyBlasterListToRemove = new ArrayList<>();

        lastHeroShotTimer =0;
        enemyShips = new Array<>();
        creator.CreateNewWave(enemyShips, 2);
        waveCounter=1;
        menuActive=false;
        pauseActive = true;
        openMenu = new MenuButton(this, world, "open_menu", 1,1,512,512 , 512 ,512,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT, MENU_X, MENU_Y);
        resume = new MenuButton(this, world, "PLAY", 1,418,900,415 , 512 ,512,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT, RESUME_X, RESUME_Y);
        save = new MenuButton(this, world, "SAVE", 1,1,900,415 , 512 ,512,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT, SAVE_X, SAVE_Y);
        load = new MenuButton(this, world, "LOAD", 1,1,900,415 , 512 ,512,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT, LOAD_X, LOAD_Y);
        exit = new MenuButton(this, world, "EXIT", 1,418,900,415 , 512 ,512,BUTTON_BOUNDS_WIDTH,BUTTON_BOUNDS_HEIGHT, EXIT_X, EXIT_Y);



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
        // POKAZUJE GRANICE OBIEKTU
       // b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        drawBatch();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float dt) {
        world.step(1/60f, 6 , 2);
        gameCam.position.y = player.b2body.getPosition().y+5;
        if (!menuActive) {
            updateDependOnMenu(dt);
        }
        player.update(dt);
        pauseControl();
        handleInput(dt);
        gameCam.update();
        renderer.setView(gameCam);
    }

    public void drawBatch(){
        game.batch.begin();
        player.draw(game.batch);
        for (EnemyShip enemy : enemyShips) {
            enemy.draw(game.batch);
        }
        for (Blaster blaster : blasterList) {
            blaster.draw(game.batch);
        }
        if (EnemyShip.getBlasterList().size() > 0) {
            for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                blaster.draw(game.batch);
            }
        }
        openMenu.draw(game.batch);
        if(menuActive){
            resume.draw(game.batch);
            save.draw(game.batch);
            load.draw(game.batch);
            exit.draw(game.batch);
        }
        game.batch.end();
    }

    public void handleInput(float dt) {

        if (Gdx.input.isTouched() ) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            // JEŚLI MENU NIE JEST AKTYWNE
            if (!menuActive) {
                lastTouch = x / Main.PPM * 1.12f ;
                if (((x / Main.PPM * 1.12) > player.b2body.getPosition().x) && (Math.abs(lastTouch - player.b2body.getPosition().x) > 0.3)) {
                    player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
                }
                if (((x / Main.PPM * 1.12) < player.b2body.getPosition().x) && (Math.abs(lastTouch - player.b2body.getPosition().x) > 0.3)) {
                    player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);
                }
                if ((player.getShootTime() <= lastHeroShotTimer) && (player.isCanShoot())) {
                    blasterList.add(new Blaster(world, this, player.getX() + player.getWidth() / 2, player.getY() + 1));
                    blasterList.get(blasterList.size() - 1).b2body.applyLinearImpulse(new Vector2(0, 10f), blasterList.get(blasterList.size() - 1).b2body.getWorldCenter(), true);
                    lastHeroShotTimer = 0;
                }
                // AKCJA PRZYCISKU OTWIERANIA MENU
                if(x / Main.PPM +0.2f > (MENU_X / scalaX) - (BUTTON_MENU_WIDTH / scalaX / 2)  &&
                        x / Main.PPM -0.2f < (MENU_X / scalaX) + (BUTTON_MENU_WIDTH / scalaX / 2) &&
                        screenHeight / Main.PPM - y / Main.PPM -0.2f  < (MENU_Y / scalaY) + (BUTTON_MENU_HEIGHT/scalaY/2) &&
                        screenHeight  / Main.PPM - y / Main.PPM +0.2f  > (MENU_Y / scalaY) - (BUTTON_MENU_HEIGHT/scalaY/2)) {

                    menuActive = true;
                }

            }
            // JEŚLI MENU JEST AKTYWNE
            else{
                //AKCJA PRZYCSIKU RESUME
                if (x / Main.PPM  > (RESUME_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                        x / Main.PPM  < (RESUME_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                        screenHeight / Main.PPM - y / Main.PPM  < (RESUME_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT/scalaY/2) &&
                        screenHeight  / Main.PPM - y / Main.PPM   > (RESUME_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT/scalaY/2))
                    menuActive = false;
                // AKCJA PRZYCISKU ZAPISZ
                if (x / Main.PPM  > (SAVE_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                        x / Main.PPM  < (SAVE_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                        screenHeight / Main.PPM - y / Main.PPM  < (SAVE_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT/scalaY/2) &&
                        screenHeight  / Main.PPM - y / Main.PPM   > (SAVE_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT/scalaY/2)){
                    try {
                        saveGame();
                        menuActive = false;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println("zapisano");

                }
                // AKCJA PRZYCISKU WCZYTAJ
                if (x / Main.PPM  > (LOAD_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                        x / Main.PPM  < (LOAD_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                        screenHeight / Main.PPM - y / Main.PPM  < (LOAD_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT/scalaY/2) &&
                        screenHeight  / Main.PPM - y / Main.PPM   > (LOAD_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT/scalaY/2)){

                    try {
                        loadGame();
                        menuActive = false;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("WCZYTANO");

                }

                // AKCJA PRZYCISKU ZAMKNIJ
                if (x / Main.PPM  > (EXIT_X / scalaX) - (BUTTON_BOUNDS_WIDTH / scalaX / 2)  &&
                        x / Main.PPM  < (EXIT_X / scalaX) + (BUTTON_BOUNDS_WIDTH / scalaX / 2) &&
                        screenHeight / Main.PPM - y / Main.PPM  < (EXIT_Y / scalaY) + (BUTTON_BOUNDS_HEIGHT/scalaY/2) &&
                        screenHeight  / Main.PPM - y / Main.PPM   > (EXIT_Y / scalaY) - (BUTTON_BOUNDS_HEIGHT/scalaY/2)){

                    Gdx.app.exit();

                }
            }

        }
        if (!menuActive)
            lastHeroShotTimer += dt;

            if (lastHeroShotTimer > 4) {
                player.setCanShoot(true);
            }

    }
    public void moveShipChecker(){
        if (Math.abs( lastTouch - player.b2body.getPosition().x ) < 0.3){
            player.b2body.setLinearVelocity(0,0);
        }
    }
    public void updateBlasterList(){
        for (Blaster blaster: blasterList){
            if (blaster.getStateTime() > 2 || blaster.isDestroyed() || blaster.isSetToDestroy()){
                blasterListToRemove.add(blaster);
            }
        }
        for (Blaster blaster: blasterListToRemove){
            blasterList.remove(blaster);
        }
    }
    public void updateEnemyBlasterList () {
        if (enemyShips.size > 0) {
            for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                if (blaster.getStateTime() > 10 || blaster.isDestroyed() || blaster.isSetToDestroy()) {
                    enemyBlasterListToRemove.add(blaster);
                }
            }
            for (EnemyBlaster blaster : enemyBlasterListToRemove) {
                if (EnemyShip.getBlasterList().size() > 0)
                    EnemyShip.getBlasterList().remove(blaster);
            }
        }
    }



    public void updateDependOnMenu (float dt) {
        moveShipChecker();
        updateEnemiesWave(dt);

        for (Blaster blaster : blasterList) {
            blaster.update(dt);
        }
        if (EnemyShip.getBlasterList().size() > 0) {
            for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                blaster.update(dt);
            }
        }
        updateBlasterList();
        updateEnemyBlasterList();
        hud.update(dt);
    }

    public void updateEnemiesWave (float dt){
        for (EnemyShip enemy: enemyShips) {
            enemy.update(dt , player);
            if (enemyShips.size < 1){
                switch (waveCounter){
                    case 0: creator.CreateNewWave(enemyShips, 2);
                        player.setCanShoot(false);
                        beginWaveScore = hud.getScore();
                        break;
                    case 1: creator.CreateNewWave(enemyShips, 3);
                        player.setCanShoot(false);
                        beginWaveScore = hud.getScore();
                        break;
                    case 2: creator.CreateNewWave(enemyShips, 4);
                        player.setCanShoot(false);
                        beginWaveScore = hud.getScore();
                        break;
                    default:System.out.println("Skonczyly się fale");
                    break;
                }
                waveCounter++;
            }
        }

    }

    public void pauseControl (){
        if(menuActive && pauseActive){
            player.b2body.setLinearVelocity(0f,0f);
            for (EnemyShip enemy : enemyShips) {
                enemy.b2body.setLinearVelocity(0f,0f);
            }
            for (Blaster blaster : blasterList) {
                blaster.b2body.setLinearVelocity(0f,0f);
            }
            if (EnemyShip.getBlasterList().size() > 0) {
                for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                    blaster.b2body.setLinearVelocity(0f,0f);
                }
            }
            pauseActive=false;
        }
        if(!menuActive && !pauseActive){


            for (Blaster blaster : blasterList) {
                blaster.b2body.setLinearVelocity(0f,10f);
            }
            if (EnemyShip.getBlasterList().size() > 0) {
                for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                    blaster.b2body.setLinearVelocity(0f,-2f);
                }
            }
            pauseActive = true;
        }
    }
    public void saveGame() throws FileNotFoundException {
        PrintWriter zapis = new PrintWriter("save.txt");
        zapis.println(waveCounter);
        zapis.println(player.getLives());
        zapis.println(beginWaveScore);
        zapis.close();
    }

    public void loadGame () throws FileNotFoundException {
        File file = new File("save.txt");
        Scanner in = new Scanner(file);

        waveCounter = Integer.parseInt(in.nextLine());
        waveCounter --;
        for (EnemyShip enemy: enemyShips ){
            enemy.setSetToDestroy(true);
        }
        if (EnemyShip.getBlasterList().size() > 0) {
            for (EnemyBlaster blaster : EnemyShip.getBlasterList()) {
                blaster.setSetToDestroy(true);
            }
        }
        for (Blaster blaster : blasterList) {
            blaster.setSetToDestroy(true);
        }
        player.setLives(Integer.parseInt(in.nextLine()));
        hud.setScore(0);
        Hud.updateScore(Integer.parseInt(in.nextLine()));

    }

    @Override
    public void resize(int width, int height) {

    screenHeight = height;
    screenWidth= width;
        scalaX = Main.V_WIDTH / screenWidth;
        scalaY = Main.V_HEIGHT / screenHeight;

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
       // renderer.dispose();
       // world.dispose();
        //b2dr.dispose();
        hud.dispose();
        EnemyBlaster.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Main getGame() {
        return game;
    }

    public void setGame(Main game) {
        this.game = game;
    }

    public Hud getHud() {
        return hud;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    public OrthographicCamera getgameCam() {
        return gameCam;
    }

    public void setgameCam(OrthographicCamera gameCam) {
        this.gameCam = gameCam;
    }

    public Viewport getGamePort() {
        return gamePort;
    }

    public void setGamePort(Viewport gamePort) {
        this.gamePort = gamePort;
    }

    public TmxMapLoader getMapLoader() {
        return mapLoader;
    }

    public void setMapLoader(TmxMapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(OrthogonalTiledMapRenderer renderer) {
        this.renderer = renderer;
    }

    public Ship getPlayer() {
        return player;
    }

    public void setPlayer(Ship player) {
        this.player = player;
    }

    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public float getLastTouch() {
        return lastTouch;
    }

    public void setLastTouch(float lastTouch) {
        this.lastTouch = lastTouch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Box2DDebugRenderer getB2dr() {
        return b2dr;
    }

    public void setB2dr(Box2DDebugRenderer b2dr) {
        this.b2dr = b2dr;
    }

    public EnemyShip getEnemy() {
        return enemy;
    }

    public void setEnemy(EnemyShip enemy) {
        this.enemy = enemy;
    }

    public List<Blaster> getBlasterList() {
        return blasterList;
    }

    public void setBlasterList(List<Blaster> blasterList) {
        this.blasterList = blasterList;
    }

    public List<Blaster> getBlasterListToRemove() {
        return blasterListToRemove;
    }

    public void setBlasterListToRemove(List<Blaster> blasterListToRemove) {
        this.blasterListToRemove = blasterListToRemove;
    }

    public float getLastHeroShotTimer() {
        return lastHeroShotTimer;
    }

    public void setLastHeroShotTimer(float lastHeroShotTimer) {
        this.lastHeroShotTimer = lastHeroShotTimer;
    }

    public B2WorldCreator getCreator() {
        return creator;
    }

    public void setCreator(B2WorldCreator creator) {
        this.creator = creator;
    }

    public List<EnemyBlaster> getEnemyBlasterListToRemove() {
        return enemyBlasterListToRemove;
    }

    public void setEnemyBlasterListToRemove(List<EnemyBlaster> enemyBlasterListToRemove) {
        this.enemyBlasterListToRemove = enemyBlasterListToRemove;
    }


    public Array<EnemyShip> getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(Array<EnemyShip> enemyShips) {
        this.enemyShips = enemyShips;
    }

    public TextureAtlas getMenuAtlas() {
        return menuAtlas;
    }
}
