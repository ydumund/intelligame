package cn.edu.witpt.IntelliGame.components;

import cn.edu.witpt.IntelliGame.Config;
import cn.edu.witpt.IntelliGame.event.GameEvent;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGL.fire;
import static com.almasb.fxgl.dsl.FXGL.spawn;

/**
 * @author ydumund
 */

public class MonsterComponent extends Component {

    @Override
    public void onUpdate(double tpf) {
        double dy = Config.MONSTER_MOVE_SPEED * tpf;
        entity.translateY(dy);
    }

    public void die() {
        spawn("Explosion", entity.getCenter());
        spawn("ParticleExplosion", entity.getCenter());
        entity.removeFromWorld();
        fire(new GameEvent(GameEvent.MONSTER_KILLED));
    }
}
