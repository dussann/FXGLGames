package com.almasb.fxglgames.spaceinvaders.level;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.app.DSLKt.spawn;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class SpaceLevel {

    private List<Entity> enemies = new ArrayList<>();

    public abstract void init();
    public abstract void destroy();

    public boolean isFinished() {
        return enemies.stream().noneMatch(Entity::isActive);
    }

    protected Entity spawnEnemy(double x, double y) {
        Entity enemy = (Entity) spawn("Enemy", x, y);

        enemies.add(enemy);

        Entities.animationBuilder()
                .interpolator(Interpolators.ELASTIC.EASE_OUT())
                .duration(Duration.seconds(FXGLMath.random() * 2))
                .scale(enemy)
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .buildAndPlay();

        return enemy;
    }

    protected Entity spawnBoss(double x, double y) {
        Entity enemy = (Entity) spawn("Boss", x, y);

        enemies.add(enemy);

        Entities.animationBuilder()
                .interpolator(Interpolators.ELASTIC.EASE_OUT())
                .duration(Duration.seconds(FXGLMath.random() * 2))
                .scale(enemy)
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .buildAndPlay();

        return enemy;
    }
}
