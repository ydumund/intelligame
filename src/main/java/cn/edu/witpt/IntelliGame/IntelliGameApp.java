package cn.edu.witpt.IntelliGame;

import cn.edu.witpt.IntelliGame.collision.HammerMonsterHandler;
import cn.edu.witpt.IntelliGame.collision.MonsterHouseHandler;
import cn.edu.witpt.IntelliGame.collision.MonsterPlayerHandler;
import cn.edu.witpt.IntelliGame.components.PlayerComponent;
import cn.edu.witpt.IntelliGame.event.GameEvent;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.UI;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.IntStream;

import static cn.edu.witpt.IntelliGame.Config.*;
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nIck_
 */

public class IntelliGameApp extends GameApplication {
    private PlayerComponent playerComponent;
    private IntelliGameController uiController;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("IntelliGame");
        gameSettings.setVersion("1.0(呕心沥血)");
        gameSettings.setWidth(WIDTH);
        gameSettings.setHeight(HEIGHT);
        gameSettings.setProfilingEnabled(false);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setManualResizeEnabled(false);
        gameSettings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        gameSettings.getCredits().addAll(Arrays.asList(
                "赵力行", "易金鹏", "张恒锐", "张培祥", "郑伊龙", "李磊", "卢贤哲"
        ));
        gameSettings.setApplicationMode(ApplicationMode.RELEASE);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A,"向左移动",()->playerComponent.left());
        onKey(KeyCode.W,"向上移动",()->playerComponent.up());
        onKey(KeyCode.D,"向右移动",()->playerComponent.right());
        onKey(KeyCode.S,"向下移动",()->playerComponent.down());
        onBtn(MouseButton.PRIMARY, "扔锤子", () -> playerComponent.shoot());
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new IntelliGameFactory());
        spawnBackground();
        spawnHome(20,808);
        spawnHome(148,808);
        spawnHome(276,808);
        spawnHome(404,808);
        spawnHome(532,808);
        spawnHome(660,808);
        spawnHome(788,808);
        spawnPlayer();
        getGameTimer().runAtInterval(this::spawnMonster, Duration.seconds(1.5));
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", START_LIVES);
        vars.put("houseLives", START_HOUSE_LIVES);
        vars.put("monsterKilled", 0);
    }

    @Override
    protected void initUI() {
        uiController = new IntelliGameController(getGameScene());
        UI ui = getAssetLoader().loadUI(Asset.FXML_MAIN_UI, uiController);
        uiController.getLabelScore().textProperty().bind(getip("score").asString("得分: %d"));
        IntStream.range(0, geti("lives"))
                .forEach(i -> uiController.addLife());
        IntStream.range(0, geti("houseLives"))
                .forEach(i -> uiController.addHouseLife());
        getGameScene().addUI(ui);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(0.1);
        getSettings().setGlobalMusicVolume(1.0);
        loopBGM("bgm.mp3");
        onEvent(GameEvent.MONSTER_KILLED, this::onMonsterKilled);
        onEvent(GameEvent.PLAYER_GOT_HIT, this::onPlayerGotHit);
        onEvent(GameEvent.HOME_GOT_HIT, this::onHomeGotHit);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new HammerMonsterHandler());
        getPhysicsWorld().addCollisionHandler(new MonsterPlayerHandler());
        getPhysicsWorld().addCollisionHandler(new MonsterHouseHandler());
    }

    private void onHomeGotHit(GameEvent event) {
        getGameScene().getViewport().shakeTranslational(9.5);
        inc("houseLives", -1);
        uiController.loseHouseLife();
        if (geti("houseLives") == 0){
            showGameOver();
        }
    }

    private void onPlayerGotHit(GameEvent event) {
        getGameScene().getViewport().shakeTranslational(9.5);
        inc("lives", -1);
        uiController.loseLife();
        playerComponent.enableInvincibility();
        runOnce(playerComponent::disableInvincibility, Duration.seconds(INVINCIBILITY_TIME));
        if (geti("lives") == 0){
            showGameOver();
        }
    }

    private int scoreForKill() {
        return SCORE_MONSTER_KILL * (getSettings().getGameDifficulty().ordinal());
    }

    private void spawnBackground(){
        spawn("Background");
    }

    private void spawnHome(double x, double y) {
        spawn("Home", x, y);
    }

    private void spawnPlayer(){
        Entity player = spawn("Player", getAppWidth() / 2 - 20, getAppHeight() - 150);
        playerComponent = player.getComponent(PlayerComponent.class);
    }

    private void spawnMonster(){
        spawn("Monster", FXGLMath.random(20,getAppWidth()-60),50);
    }

    public void onMonsterKilled(GameEvent event) {
        inc("monsterKilled", +1);
        inc("score", scoreForKill());
    }

    private void showGameOver() {
        getDialogService().showConfirmationBox("游戏结束. 再玩一次?", ok -> {
            if (ok) {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                getGameController().startNewGame();
            } else {
                getGameController().exit();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
