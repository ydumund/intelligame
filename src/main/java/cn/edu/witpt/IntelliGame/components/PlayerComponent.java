package cn.edu.witpt.IntelliGame.components;

import cn.edu.witpt.IntelliGame.Config;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nIck_
 */
@Required(InvincibleComponent.class)
public class PlayerComponent extends Component {
    private InvincibleComponent invincibility;
    private double dx = 0;
    private double dy = 0;
    private boolean canShoot = true;
    private double lastTimeShoot = 0;

    // 实时更新
    // tpf = time per frame

    @Override
    public void onUpdate(double tpf) {
        dx = Config.PLAYER_MOVE_SPEED * tpf;
        dy = Config.PLAYER_MOVE_SPEED * tpf;

        if (!canShoot){
            double attackSpeed = Config.PLAYER_ATTACK_SPEED;
            if ((getGameTimer().getNow() - lastTimeShoot) >= 1.0 / attackSpeed){
                canShoot = true;
            }
        }
    }

    public void disableInvincibility() {
        invincibility.setValue(false);
    }

    public void enableInvincibility() {
        invincibility.setValue(true);
    }

    private Image particle;

    public void spawnParticles(){
        if (particle == null){
            particle = image("player.png",40,30);
        }
        entityBuilder()
                .at(getEntity().getCenter().subtract(particle.getWidth()/2,particle.getHeight()/2))
                .view(new Texture(particle))
                .zIndex(-300)
                .with(new ExpireCleanComponent(Duration.seconds(0.2)).animateOpacity())
                .buildAndAttach();
    }

    public void left(){
        if (getEntity().getX() - dx >= 0){
            getEntity().translateX(-dx);
        }
        spawnParticles();
    }

    public void up(){
        if (getEntity().getY() - dy >=0){
            getEntity().translateY(-dy);
        }
        spawnParticles();
    }

    public void right(){
        if (getEntity().getX() + getEntity().getWidth() + dx <= Config.WIDTH){
            getEntity().translateX(dx);
        }
        spawnParticles();
    }

    public void down(){
        if (getEntity().getY() + getEntity().getHeight() + dy <= Config.HEIGHT){
            getEntity().translateY(dy);
        }
        spawnParticles();
    }

    public void shoot() {
        if (!canShoot) {
            return;
        }
        canShoot = false;
        lastTimeShoot = getGameTimer().getNow();
        spawn("Hammer", new SpawnData(0, 0).put("owner", getEntity()));
        play("shoot.wav");
    }
}
