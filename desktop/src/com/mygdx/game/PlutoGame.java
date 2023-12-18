package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;



public class PlutoGame extends com.badlogic.gdx.Game {
    private ShapeRenderer shapeRenderer;
    private Vector2 playerPosition;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    class GameScreen implements com.badlogic.gdx.Screen {
        @Override
        public void show() {
            // Initialization code if needed
        }

        @Override
        public void render(float delta) {
            handleInput();
            draw();
        }

        private void handleInput() {
            float speed = 200f * Gdx.graphics.getDeltaTime();

            // Move the player based on input
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                playerPosition.y += speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                playerPosition.x -= speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                playerPosition.y -= speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                playerPosition.x += speed;
            }

            // Clamp player position to screen bounds
            playerPosition.x = Math.max(0, Math.min(Gdx.graphics.getWidth(), playerPosition.x));
            playerPosition.y = Math.max(0, Math.min(Gdx.graphics.getHeight(), playerPosition.y));
        }

        private void draw() {
            // Clear the screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Draw the player dot
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(playerPosition.x, playerPosition.y, 10); // Adjust the radius as needed
            shapeRenderer.end();
        }

        @Override
        public void resize(int width, int height) {
            // Resize code if needed
        }

        @Override
        public void pause() {
            // Pause code if needed
        }

        @Override
        public void resume() {
            // Resume code if needed
        }

        @Override
        public void hide() {
            // Hide code if needed
        }

        @Override
        public void dispose() {
            // Dispose code if needed
        }
    }
}


