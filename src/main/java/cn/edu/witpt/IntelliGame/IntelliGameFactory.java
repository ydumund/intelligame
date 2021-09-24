package cn.edu.witpt.IntelliGame;

import cn.edu.witpt.IntelliGame.components.*;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static cn.edu.witpt.IntelliGame.IntelliGameType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nIck_
 */

public class IntelliGameFactory implements EntityFactory {

    @Spawns("Background")
    public Entity newBackground(SpawnData data){
        return entityBuilder()
                .at(-10,-10)
                .view(texture("background/background.jpg",Config.WIDTH+20,Config.HEIGHT+20))
                .zIndex(-500)
                .build();
    }

    @Spawns("Home")
    public Entity newHome(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(HOME)
                .viewWithBBox(texture("home.png", 88, 88).outline(Color.GREEN))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        Texture texture = texture("player.png");
        texture.setPreserveRatio(true);
        texture.outline(Color.BLUE);
        texture.setFitHeight(80);
        return entityBuilder()
                .from(data)
                .type(PLAYER)
                .viewWithBBox(texture)
                .with(new CollidableComponent(true))
                .with(new InvincibleComponent())
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("Monster")
    public Entity newEnemy(SpawnData data) {
        return entityBuilder()
                .from(data)
                .type(MONSTER)
                .viewWithBBox(texture("monster" + ((int)(Math.random() * 4) + 1) + ".png",40,40)
                        .outline(Color.RED))
                .with(new CollidableComponent(true), new HealthComponent(2), new TimeComponent(1.0))
                .with(new MonsterComponent(), new EffectComponent())
                .build();
    }

    @Spawns("Hammer")
    public Entity newLaser(SpawnData data) {
        Entity owner = data.get("owner");
        Texture t = texture("hammer.png",40,40);
        t.relocate(-2, -20);
        t.setEffect(new Bloom(0.5));
        t.outline(Color.GOLD);
        return entityBuilder()
                .type(HAMMER)
                .at(owner.getCenter().add(-18, -20))
                .bbox(new HitBox(BoundingShape.box(40, 40)))
                .view(t)
                .with(new CollidableComponent(true), new OwnerComponent(owner.getType()))
                .with(new OffscreenCleanComponent(), new HammerComponent(200.0))
                .build();
    }

    @Spawns("Explosion")
    public Entity newExplosion(SpawnData data) {
        play("explosion.wav");
        var texture = texture("explosion.png", 80 * 16, 80)
                .toAnimatedTexture(16, Duration.seconds(0.5));
        var e = entityBuilder()
                .at(data.getX() - 40, data.getY() - 40)
                .view(texture.loop())
                .build();
        texture.setOnCycleFinished(() -> e.removeFromWorld());
        return e;
    }

    @Spawns("ParticleExplosion")
    public Entity newParticleExplosion(SpawnData data) {
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(getAppWidth());
        emitter.setStartColor(Color.web("ffffe0"));
        emitter.setSize(3, 5);
        emitter.setNumParticles(8);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());
        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
}
