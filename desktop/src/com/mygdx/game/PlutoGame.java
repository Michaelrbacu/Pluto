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
    // Rendering tools
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    // Player position
    private Vector2 playerPosition;

    // Mini-map
    private MiniMap miniMap;

    // Camera
    private OrthographicCamera camera;

    // Planets
    private List<Planet> planets;
    private LevelHD110067a currentLevel;

    @Override
    public void create() {
        // Initialize rendering tools
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        // Initialize player position at the center of the screen
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        // Initialize mini-map
        miniMap = new MiniMap();

        // Initialize camera
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.0f;
        camera.position.set(playerPosition.x, playerPosition.y, 0);
        camera.update();

        // Set the screen to the GameScreen
        setScreen(new GameScreen());

        // Initialize planets
        planets = new ArrayList<>();
        createPlanets();

        // Set the current level to LevelHD110067a
        currentLevel = new LevelHD110067a();
    }

    // Method to create planets
    private void createPlanets() {
        planets.add(new Planet("HD110067a", Color.RED, 100, 100, new LevelHD110067a(), playerPosition));
        planets.add(new Planet("HD110067b", Color.YELLOW, 300, 200, new LevelHD110067b(), playerPosition));
        planets.add(new Planet("HD110067c", Color.BLUE, 500, 300, new LevelHD110067c(), playerPosition));
        planets.add(new Planet("HD110067d", Color.ORANGE, 700, 400, new LevelHD110067d(), playerPosition));
        planets.add(new Planet("HD110067e", Color.PURPLE, 900, 500, new LevelHD110067e(), playerPosition));
        planets.add(new Planet("HD110067f", Color.WHITE, 1100, 600, new LevelHD110067f(), playerPosition));
        planets.add(new Planet("HD110067g", Color.GREEN, 1300, 700, new LevelHD110067g(), playerPosition));
    }

    @Override
    public void dispose() {
        // Dispose of rendering tools
        shapeRenderer.dispose();
        batch.dispose();

        // Dispose each planet
        for (Planet planet : planets) {
            planet.dispose();
        }
    }

    // GameScreen class implementing Screen interface
    class GameScreen implements com.badlogic.gdx.Screen {
        // Background texture and sprite
        private Texture backgroundTexture;
        private Sprite backgroundSprite;

        @Override
        public void show() {
            // Load and set up the background texture and sprite
            backgroundTexture = new Texture(Gdx.files.internal("background.png"));
            backgroundSprite = new Sprite(backgroundTexture);
            backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void render(float delta) {
            // Handle player input, update camera, and draw elements
            handleInput();
            updateCamera();
            draw();
        }

        // Handle player input
        private void handleInput() {
            float speed = 200f * Gdx.graphics.getDeltaTime();

            // Move player based on input
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

        // Update camera position
        private void updateCamera() {
            float lerp = 0.1f;
            camera.position.lerp(new Vector3(playerPosition.x, playerPosition.y, 0), lerp);
            camera.update();
        }

        // Draw elements on the screen
        private void draw() {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            shapeRenderer.setProjectionMatrix(camera.combined);

            // Draw background
            batch.begin();
            backgroundSprite.draw(batch);
            batch.end();

            // Draw planets
            for (Planet planet : planets) {
                planet.draw();
            }

            // Draw player position on the mini-map
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(playerPosition.x, playerPosition.y, 5);
            shapeRenderer.end();

            // Draw mini-map
            miniMap.draw(playerPosition);

            // Update the current level
            currentLevel.update();
        }

        @Override
        public void resize(int width, int height) {
            // Adjust camera viewport on resize
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }

        @Override
        public void dispose() {
            // Dispose of background texture
            backgroundTexture.dispose();
        }

        // Other methods not implemented for simplicity
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

    // MiniMap class to handle mini-map drawing
    class MiniMap {
        // Size of the player dot on the mini-map
        private static final float PLAYER_DOT_SIZE = 5f;

        // Player position on the mini-map
        private Vector2 playerPosition;

        // Draw mini-map with planets
        void draw(Vector2 playerPosition) {
            this.playerPosition = playerPosition;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLUE);
            for (Planet planet : planets) {
                shapeRenderer.circle(planet.getPosition().x / 10, planet.getPosition().y / 10, PLAYER_DOT_SIZE);
            }
            shapeRenderer.end();
        }
    }

    // Planet class representing celestial bodies in the game
    class Planet {
        // Planet properties
        private String name;
        private Color color;
        private Vector2 position;
        private Vector2 playerPosition;
        private float radius = 20f;
        private Level level;

        // Flag to track player collisions
        private boolean hasCollided = false;


		public Planet(String name, Color color, float x, float y, LevelHD110067a level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y); 
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067b level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y); 
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067c level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y); 
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067d level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y);
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067e level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y);
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067f level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y);
			this.level = level;
			this.playerPosition = playerPosition;
		}

		public Planet(String name, Color color, float x, float y, LevelHD110067g level, Vector2 playerPosition) {
			this.name = name;
			this.color = color;
			this.position = new Vector2(x, y);
			this.level = level;
			this.playerPosition = playerPosition;
		}

        // Draw the planet
        public void draw() {
            // Check for player collision and handle appropriately
            if (isPlayerColliding(playerPosition) && !hasCollided) {
                onPlayerCollision();
                hasCollided = true;
            } else if (!isPlayerColliding(playerPosition)) {
                hasCollided = false;
            }

            // Draw the planet using shapeRenderer
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (color != null) {
                shapeRenderer.setColor(color);
            } else {
                shapeRenderer.setColor(Color.WHITE);
            }
            shapeRenderer.circle(position.x, position.y, radius);
            shapeRenderer.end();
        }

        // Check if the player is colliding with the planet
        public boolean isPlayerColliding(Vector2 playerPosition) {
            if (playerPosition == null) {
                return false;
            }
            return playerPosition.dst2(position) < (radius * radius);
        }

        // Handle actions when player collides with the planet
        public void onPlayerCollision() {
            System.out.println("Touched " + name + "! Entering " + level.getName());
            
            switchToChallengeScreen();

        }
        
        // Method to switch to the level screen
        private void switchToChallengeScreen() {
            setScreen(new ChallengeScreen());
        }


        // Get the position of the planet
        public Vector2 getPosition() {
            return position;
        }

        public void dispose() {
            // Dispose any resources if needed
        }
    }
}