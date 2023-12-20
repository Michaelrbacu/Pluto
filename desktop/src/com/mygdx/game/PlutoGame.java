package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class PlutoGame extends Game {
    private ShapeRenderer shapeRenderer;
    private Vector2 playerPosition;
    private MiniMap miniMap;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private List<Planet> planets;
    private Level currentLevel;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        miniMap = new MiniMap();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.0f;
        camera.position.set(playerPosition.x, playerPosition.y, 0);
        camera.update();
        batch = new SpriteBatch();
        setScreen(new GameScreen());
        planets = new ArrayList<>();
        createPlanets();
        currentLevel = new Level1(); 
    }

    private void createPlanets() {
        planets.add(new Planet("Mercury", Color.RED, 100, 100, new Level2()));
        planets.add(new Planet("Venus", Color.YELLOW, 300, 200, new Level3()));
        planets.add(new Planet("Mars", Color.RED, 500, 300, new Level4()));
        planets.add(new Planet("Jupiter", Color.ORANGE, 700, 400, new Level5()));
        planets.add(new Planet("Saturn", Color.YELLOW, 900, 500, new Level6()));
        planets.add(new Planet("Uranus", Color.BLUE, 1100, 600, new Level7()));
        planets.add(new Planet("Neptune", Color.BLUE, 1300, 700, new Level8()));
        planets.add(new Planet("Pluto", Color.GRAY, 1500, 800, new Level9()));
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        for (Planet planet : planets) {
            planet.dispose();
        }
    }

    class GameScreen implements com.badlogic.gdx.Screen {
        private Texture backgroundTexture;
        private Sprite backgroundSprite;

        @Override
        public void show() {
            backgroundTexture = new Texture(Gdx.files.internal("background.png"));
            backgroundSprite = new Sprite(backgroundTexture);
            backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void render(float delta) {
            handleInput();
            updateCamera();
            draw();
        }

        private void handleInput() {
            float speed = 200f * Gdx.graphics.getDeltaTime();
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
        }

        private void updateCamera() {
            float lerp = 0.1f;
            camera.position.lerp(new Vector3(playerPosition.x, playerPosition.y, 0), lerp);
            camera.update();
        }

        private void draw() {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            shapeRenderer.setProjectionMatrix(camera.combined);

            batch.begin();
            backgroundSprite.draw(batch);
            batch.end();

            for (Planet planet : planets) {
                planet.draw();
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(playerPosition.x, playerPosition.y, 5);
            shapeRenderer.end();

            miniMap.draw(playerPosition);
            currentLevel.update();
        }

        @Override
        public void resize(int width, int height) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }

        @Override
        public void dispose() {
            backgroundTexture.dispose();
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void hide() {
        }
    }

    class MiniMap {
        private static final float PLAYER_DOT_SIZE = 5f;

        void draw(Vector2 playerPosition) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            for (Planet planet : planets) {
                shapeRenderer.circle(planet.getPosition().x / 10, planet.getPosition().y / 10, PLAYER_DOT_SIZE);
            }
            shapeRenderer.end();
        }
    }

    class Planet {
        private String name;
        private Color color;
        private Vector2 position;
        private float radius = 20f;
        private Level level;

        public Planet(String name, Color color, float x, float y, Level level) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);
            this.level = level;
        }

        public void draw() {
            // Check for collision with the player
            if (isPlayerColliding(playerPosition)) {
                onPlayerCollision();
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(playerPosition.x, playerPosition.y, 5);
            shapeRenderer.end();

            miniMap.draw(playerPosition);
            currentLevel.update();
        

             shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
             shapeRenderer.setColor(Color.WHITE);
             shapeRenderer.circle(playerPosition.x, playerPosition.y, 5);
             shapeRenderer.end();

             miniMap.draw(playerPosition);
             currentLevel.update();
        }

        public boolean isPlayerColliding(Vector2 playerPosition) {
            return playerPosition.dst2(position) < (radius * radius);
        }

        public void onPlayerCollision() {
            System.out.println("Touched " + name + "! Entering " + level.getName());
            setScreen(level);
        }

        public Vector2 getPosition() {
            return position;
        }

        public void dispose() {
            // Dispose of planet resources if needed
        }
    }

    abstract class Level {
        public abstract String getName();
        public abstract void update();
    }

    class Level1 extends Level {
        @Override
        public String getName() {
            return "Level 1";
        }

        @Override
        public void update() {
            // Update Level 1 logic
        }
    }

    class Level2 extends Level {
        @Override
        public String getName() {
            return "Level 2";
        }

        @Override
        public void update() {
            // Update Level 2 logic
        }
    }

    class Level3 extends Level {
        @Override
        public String getName() {
            return "Level 3";
        }

        @Override
        public void update() {
            // Update Level 3 logic
        }
    }

    class Level4 extends Level {
        @Override
        public String getName() {
            return "Level 4";
        }

        @Override
        public void update() {
            // Update Level 4 logic
        }
    }

    class Level5 extends Level {
        @Override
        public String getName() {
            return "Level 5";
        }

        @Override
        public void update() {
            // Update Level 5 logic
        }
    }

    class Level6 extends Level {
        @Override
        public String getName() {
            return "Level 6";
        }

        @Override
        public void update() {
            // Update Level 6 logic
        }
    }

    class Level7 extends Level {
        @Override
        public String getName() {
            return "Level 7";
        }

        @Override
        public void update() {
            // Update Level 7 logic
        }
    }

    class Level8 extends Level {
        @Override
        public String getName() {
            return "Level 8";
        }

        @Override
        public void update() {
            // Update Level 8 logic
        }
    }

    class Level9 extends Level {
        @Override
        public String getName() {
            return "Level 9";
        }

        @Override
        public void update() {
            // Update Level 9 logic
        }
    }
}

