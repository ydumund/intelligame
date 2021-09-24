package cn.edu.witpt.IntelliGame.collision;

import cn.edu.witpt.IntelliGame.IntelliGameType;
import cn.edu.witpt.IntelliGame.event.GameEvent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * @author nIck_
 */
public class MonsterPlayerHandler extends CollisionHandler {
    public MonsterPlayerHandler() {
        super(IntelliGameType.MONSTER,IntelliGameType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity monster, Entity player) {
        monster.removeFromWorld();
        FXGL.getEventBus()
                .fireEvent(new GameEvent(GameEvent.PLAYER_GOT_HIT));
    }
}
