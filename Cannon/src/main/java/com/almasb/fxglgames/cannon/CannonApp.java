/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.fxglgames.cannon;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.ecs.Entity;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

/**
 * A basic FXGL game demo.
 *
 * Game:
 * The player shoots from a "cannon" and tries to
 * get the projectile in-between the barriers.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class CannonApp extends GameApplication {

    private GameEntity cannon;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Cannon");
        settings.setVersion("0.2.1");
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);
        settings.setProfilingEnabled(false);
        settings.setCloseConfirmation(false);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                shoot();
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initGame() {
        initScreenBounds();
        initCannon();
        initBasket();
    }

    private void initScreenBounds() {
        getGameWorld().addEntity(Entities.makeScreenBounds(100));
    }

    private void initCannon() {
        cannon = (GameEntity) getGameWorld().spawn("cannon", 50, getHeight() - 30);
    }

    private void initBasket() {
        getGameWorld().spawn("basketBarrier", 400, getHeight() - 300);
        getGameWorld().spawn("basketBarrier", 700, getHeight() - 300);
        getGameWorld().spawn("basketGround", 500, getHeight());
    }

    private void shoot() {
        getGameWorld().spawn("bullet", cannon.getPosition().add(70, 0));
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.addCollisionHandler(new CollisionHandler(CannonType.BULLET, CannonType.BASKET) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity basket) {
                bullet.removeFromWorld();
                getGameState().increment("score", +1000);

                GameEntity[] entities = getGameWorld().getEntitiesByType(CannonType.BASKET).toArray(new GameEntity[0]);

                Entities.animationBuilder()
                        .duration(Duration.seconds(0.2))
                        .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                        .scale(entities)
                        .from(new Point2D(1.2, 1))
                        .to(new Point2D(1, 1))
                        .buildAndPlay();
            }
        });
    }

    @Override
    protected void initUI() {
        Text scoreText = getUIFactory().newText("", Color.BLACK, 24);
        scoreText.setTranslateX(550);
        scoreText.setTranslateY(100);
        scoreText.textProperty().bind(getGameState().intProperty("score").asString("Score: [%d]"));

        getGameScene().addUINode(scoreText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
