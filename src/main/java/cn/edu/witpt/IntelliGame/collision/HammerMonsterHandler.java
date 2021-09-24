package cn.edu.witpt.IntelliGame.collision;

import cn.edu.witpt.IntelliGame.IntelliGameType;
import cn.edu.witpt.IntelliGame.components.HealthComponent;
import cn.edu.witpt.IntelliGame.components.MonsterComponent;
import com.almasb.fxgl.dsl.components.Effect;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

/**
 * @author nIck_
 */
public class HammerMonsterHandler extends CollisionHandler {
    public HammerMonsterHandler() {
        super(IntelliGameType.HAMMER, IntelliGameType.MONSTER);
    }

    @Override
    protected void onCollisionBegin(Entity hammer, Entity monster) {
        hammer.removeFromWorld();
        HealthComponent hp = monster.getComponent(HealthComponent.class);
        hp.setValue(hp.getValue() - 1);
        if (hp.getValue() <= 0) {
            if (monster.hasComponent(MonsterComponent.class)) {
                monster.getComponent(MonsterComponent.class)
                        .die();
            }
        } else {
            monster.getComponentOptional(EffectComponent.class)
                    .ifPresent(e -> e.startEffect(new Effect(Duration.seconds(1)) {
                @Override
                public void onStart(Entity entity) {
                    entity.getComponent(TimeComponent.class)
                            .setValue(0.15);
                }

                @Override
                public void onEnd(Entity entity) {
                    entity.getComponent(TimeComponent.class)
                            .setValue(1);
                }
            }));
        }
    }
}
