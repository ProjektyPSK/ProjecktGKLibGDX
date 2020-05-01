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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.sprites.Blaster;
import com.mygdx.game.sprites.EnemyBlaster;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.Ship;

import java.util.ArrayList;
import java.util.List;


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
    private float lastTouch;

    private World world;
    private Box2DDebugRenderer b2dr;
    private EnemyShip enemy;
    private List <Blaster> blasterList;
    private List <Blaster> blasterListToRemove;
    private float lastHeroShotTimer;
    private B2WorldCreator creator;
    private List <EnemyBlaster> enemyBlasterListToRemove;

    public PlayScreen (Main game){
        atlas= new TextureAtlas("BigSprite.atlas");

        this.game=game;
        gamecam= new OrthographicCamera();
        gamePort = new FitViewport(Main.V_WIDTH / 50 ,Main.V_HEIGHT / 50 , gamecam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map , 1/ Main.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight()/2 , 0);

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();
        player = new Ship(world, this);


        creator = new B2WorldCreator (world, map, this);
        lastTouch=player.b2body.getPosition().x;


        world.setContactListener(new WorldContactListener());

        blasterList = new ArrayList<>();
        blasterListToRemove = new ArrayList<>();
        enemyBlasterListToRemove = new ArrayList<>();

        lastHeroShotTimer =0;
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

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (EnemyShip enemy: creator.getEnemyShips()) {
            enemy.draw(game.batch);
        }
        for (EnemyShip enemy: creator.getEnemyShipsStage2()) {
            enemy.draw(game.batch);
        }
        for (Blaster blaster: blasterList) {
            blaster.draw(game.batch);
        }
        for (EnemyBlaster blaster: creator.getEnemyShips().get(0).getBlasterList()) {
            blaster.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void handleInput(float dt) {


        if (Gdx.input.isTouched()) {
            lastTouch = Gdx.input.getX() / Main.PPM * 1.1f;
            if (((Gdx.input.getX() / Main.PPM * 1.1) > player.b2body.getPosition().x) && (Math.abs(lastTouch - player.b2body.getPosition().x) > 0.3)) {
                player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (((Gdx.input.getX() / Main.PPM * 1.1) < player.b2body.getPosition().x) && (Math.abs(lastTouch - player.b2body.getPosition().x) > 0.3)) {
                player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);

            }
            if(player.getShootTime() <= lastHeroShotTimer ) {
                blasterList.add(new Blaster(world, this, player.getX() + player.getWidth() / 2, player.getY() + 1));
                blasterList.get(blasterList.size() - 1).b2body.applyLinearImpulse(new Vector2(0, 10f), blasterList.get(blasterList.size() - 1).b2body.getWorldCenter(), true);
                lastHeroShotTimer = 0;
            }

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
        for (EnemyBlaster blaster: creator.getEnemyShips().get(0).getBlasterList() ){
            if (blaster.getStateTime() > 10 || blaster.isDestroyed() || blaster.isSetToDestroy()){
                enemyBlasterListToRemove.add(blaster);
            }
        }
        for (EnemyBlaster blaster: enemyBlasterListToRemove){
            creator.getEnemyShips().get(0).getBlasterList().remove(blaster);

        }
    }

    public void update(float dt) {
        world.step(1/60f, 6 , 2);
        player.update(dt);
        moveShipChecker();
        gamecam.position.y = player.b2body.getPosition().y+5;
        for (EnemyShip enemy: creator.getEnemyShips()) {
            enemy.update(dt , player);
        }
        for (EnemyShip enemy: creator.getEnemyShipsStage2()) {
            enemy.update(dt , player);
        }
        for (Blaster blaster: blasterList) {
            blaster.update(dt);
        }
        for (EnemyBlaster blaster: creator.getEnemyShips().get(0).getBlasterList()) {
            blaster.update(dt);
        }

        handleInput(dt);
        gamecam.update();
        renderer.setView(gamecam);
        updateBlasterList();
        updateEnemyBlasterList();
        lastHeroShotTimer += dt;
        System.out.println(creator.getEnemyShips().size);
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

    public OrthographicCamera getGamecam() {
        return gamecam;
    }

    public void setGamecam(OrthographicCamera gamecam) {
        this.gamecam = gamecam;
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
}
