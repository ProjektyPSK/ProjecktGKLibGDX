package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.sprites.Ship;

import java.awt.*;

/**
 * klasa tworząca Hud gry
 */
public class Hud implements Disposable {
    public Stage stage;
    public Viewport viewport;

    private static Integer worldTimer;
    private static Integer score;
    private float timeCount;

    private static Integer lives;
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private static Label livesLabel;
    private Label livesTextLabel;
    private Label scoreTextLabel;
    private int tryb;

    /**
     * Konstruktor wykonywany w klasie PlayScreen, tworzący wygląd Hud
     * @param sb
     * @param player
     */
    public Hud(SpriteBatch sb, Ship player){
        this.tryb = 1;
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        lives = player.getLives();

        viewport = new FitViewport(Main.V_WIDTH  ,Main.V_HEIGHT  , new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle( new BitmapFont(),Color.WHITE));
        scoreTextLabel = new Label("SCORE" , new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        livesTextLabel = new Label("LIVES" , new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        livesLabel = new Label(String.format("%02d", lives), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(2.5f);
        countdownLabel.setFontScale(2.5f);
        scoreTextLabel.setFontScale(2.5f);
        timeLabel.setFontScale(2.5f);
        livesLabel.setFontScale(2.5f);
        livesTextLabel.setFontScale(2.5f);

        table.add(livesTextLabel).expandX().padTop(10);
        table.add(scoreTextLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(livesLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);

        stage.addActor(table);
    }
    /**
     * Konstruktor wykonywany w klasie MenuScreen, tworzący wygląd Hud
     * @param sb
     */
    public Hud(SpriteBatch sb){
        this.tryb = 2;

        viewport = new FitViewport(Main.V_WIDTH  ,Main.V_HEIGHT  , new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle( new BitmapFont(), Color.WHITE));

        scoreLabel.setFontScale(2.5f);
        countdownLabel.setFontScale(2.5f);

        table.add(scoreLabel).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    /**
     * Odlicza czas w grze lub przelicza czas na wynik w menui
     * @param dt
     */
    public void update (float dt){
        if(tryb ==1) {
            timeCount += dt;
            if (timeCount >= 1) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
            }
        }
        if(tryb == 2){
        if( worldTimer > 0){
            updateScore(5);
            worldTimer --;
            countdownLabel.setText(String.format("%03d", worldTimer));
        }
            if( worldTimer < 0){
                updateScore(-5);
                worldTimer ++;
                countdownLabel.setText(String.format("%03d", worldTimer));
            }

        }
    }

    /**
     * służy do zmiany wyniku i wyświetlenia go
     * @param value
     */
    public static void updateScore (int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    /**
     * służy do zmiany ilości żyć i wyświetlenia jej
     * @param value
     */
    public static void updateLives (int value){
        lives += value;
        livesLabel.setText(String.format("%02d", lives));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public void setTimeCount(Integer timeCount) {
        this.timeCount = timeCount;
    }

    public Integer getLives() {
        return lives;
    }

    public void setLives(Integer lives) {
        this.lives = lives;
    }

    public Label getCountdownLabel() {
        return countdownLabel;
    }

    public void setCountdownLabel(Label countdownLabel) {
        this.countdownLabel = countdownLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    public Label getLivesLabel() {
        return livesLabel;
    }

    public void setLivesLabel(Label livesLabel) {
        this.livesLabel = livesLabel;
    }

    public Integer getWorldTimer() {
        return worldTimer;
    }

    public void setWorldTimer(Integer worldTimer) {
        this.worldTimer = worldTimer;
    }
}
