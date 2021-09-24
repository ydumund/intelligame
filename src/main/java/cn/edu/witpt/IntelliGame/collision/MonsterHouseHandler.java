package cn.edu.witpt.IntelliGame.collision;

import cn.edu.witpt.IntelliGame.IntelliGameType;
import cn.edu.witpt.IntelliGame.event.GameEvent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * @author nIck_
 */
public class MonsterHouseHandler extends CollisionHandler {
    public MonsterHouseHandler() {
        super(IntelliGameType.MONSTER,IntelliGameType.HOME);
    }

    @Override
    protected void onCollisionBegin(Entity monster, Entity home) {
        monster.removeFromWorld();
        FXGL.getEventBus().fireEvent(new GameEvent(GameEvent.HOME_GOT_HIT));
    }
}
