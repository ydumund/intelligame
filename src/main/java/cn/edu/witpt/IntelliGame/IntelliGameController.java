package cn.edu.witpt.IntelliGame;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UIController;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nIck_
 */
public class IntelliGameController implements UIController {
    @FXML
    private Label labelScore;

    @FXML
    private double livesX;

    @FXML
    private double livesY;

    @FXML
    private double houseLivesX;

    @FXML
    private double houseLivesY;

    private List<Texture> lives = new ArrayList<>();
    private List<Texture> houseLives = new ArrayList<>();

    private GameScene gameScene;

    public void addHouseLife() {
        int numHouseLives = houseLives.size();
        Texture texture = getAssetLoader()
                .loadTexture("houselife.png", 32, 32);
        texture.setTranslateX(houseLivesX + 64 * numHouseLives);
        texture.setTranslateY(houseLivesY);
        houseLives.add(texture);
        gameScene.addUINode(texture);
    }

    public void loseHouseLife() {
        loseLife(houseLives);
    }


    /*
    * 玩家和房子掉血
    * 提取出来的方法
    */

    private void loseLife(List<Texture> heal) {
        Texture t = heal.get(heal.size() - 1);
        heal.remove(t);
        Animation animation = getAnimationLoseLife(t);
        animation.setOnFinished(e -> gameScene.removeUINode(t));
        animation.play();
        Viewport viewport = gameScene.getViewport();
        Node flash = new Rectangle(viewport.getWidth()
                , viewport.getHeight()
                , Color.rgb(190, 10, 15, 0.5)
        );
        gameScene.addUINode(flash);
        runOnce(() -> gameScene.removeUINode(flash)
                , Duration.seconds(1)
        );
    }

    public void addLife() {
        int numLives = lives.size();
        Texture texture = getAssetLoader()
                .loadTexture("life.png", 32, 42);
        texture.setTranslateX(livesX + 64 * numLives);
        texture.setTranslateY(livesY);
        lives.add(texture);
        gameScene.addUINode(texture);
    }

    public void loseLife() {
        loseLife(lives);
    }

    private Animation getAnimationLoseLife(Texture texture) {
        texture.setFitWidth(64);
        texture.setFitHeight(64);
        Viewport viewport = gameScene.getViewport();
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.66), texture);
        tt.setToX(viewport.getWidth() / 2 - texture.getFitWidth() / 2);
        tt.setToY(viewport.getHeight() / 2 - texture.getFitHeight() / 2);
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.66), texture);
        st.setToX(0);
        st.setToY(0);
        return new SequentialTransition(tt, st);
    }

    public IntelliGameController(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    @Override
    public void init() {
        labelScore.setFont(getUIFactoryService().newFont(32));
    }

    public Label getLabelScore() {
        return labelScore;
    }
}