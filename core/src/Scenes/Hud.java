package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;

import java.awt.*;

public class Hud {
    public Stage stage;
    public Viewport viewport;

    private Integer worldTimer;
    private Integer score;
    private Integer timeCount;
    private Integer lives;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label livescountLabel;
    Label worldLabel;
    Label livesLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        lives = 3;

        viewport = new StretchViewport(Main.V_WIDTH ,Main.V_HEIGHT , new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle( new BitmapFont(),Color.WHITE));
        livescountLabel = new Label("3", new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        worldLabel = new Label("World", new Label.LabelStyle( new BitmapFont(), Color.WHITE));
        livesLabel = new Label("lives" , new Label.LabelStyle( new BitmapFont(),
                Color.WHITE));


        table.add(livesLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(livescountLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);



        stage.addActor(table);
    }

}
