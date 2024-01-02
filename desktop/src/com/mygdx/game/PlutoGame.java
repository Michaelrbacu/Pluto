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
    private LevelHD110067a currentLevel;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f); // Initialize playerPosition
        miniMap = new MiniMap();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.0f;
        camera.position.set(playerPosition.x, playerPosition.y, 0);
        camera.update();
        batch = new SpriteBatch();
        setScreen(new GameScreen());
        planets = new ArrayList<>();
        createPlanets();
        currentLevel = new LevelHD110067a();
    }


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
        private Vector2 playerPosition;

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

    class Planet {
        private String name;
        private Color color;
        private Vector2 position;
        private Vector2 playerPosition;
        private float radius = 20f;
        private Level level;

        public Planet(String name, Color color, float x, float y, Level level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);
            this.level = level;
            this.playerPosition = playerPosition;
        }

        public Planet(String name, Color color, float x, float y, LevelHD110067a level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }

        public Planet(String name, Color color, float x, float y, LevelHD110067b level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }
        
        public Planet(String name, Color color, float x, float y, LevelHD110067c level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }
        
        public Planet(String name, Color color, float x, float y, LevelHD110067d level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }
		
        public Planet(String name, Color color, float x, float y, LevelHD110067e level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }
        
        public Planet(String name, Color color, float x, float y, LevelHD110067f level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }
        
        public Planet(String name, Color color, float x, float y, LevelHD110067g level, Vector2 playerPosition) {
            this.name = name;
            this.color = color;
            this.position = new Vector2(x, y);  // Ensure that position is initialized
            this.level = level;
            this.playerPosition = playerPosition;
        }

        private boolean hasCollided = false;

        
        public void draw() {
            if (isPlayerColliding(playerPosition) && !hasCollided) {
                onPlayerCollision();
                hasCollided = true;  // Set the flag to true after the first collision
            } else if (!isPlayerColliding(playerPosition)) {
                // Reset the flag if the player is not colliding with the planet
                hasCollided = false;
            }

		    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		    
		    if (color != null) {
		        shapeRenderer.setColor(color);
		    } else {
		        // Handle the case where color is null (e.g., set a default color)
		        shapeRenderer.setColor(Color.WHITE);
		    }
		    
		    shapeRenderer.circle(position.x, position.y, radius);
		    shapeRenderer.end();
		}



		public boolean isPlayerColliding(Vector2 playerPosition) {
		    if (playerPosition == null) {
		        // Handle the case where playerPosition is null
		        return false;  // Or whatever behavior is appropriate in your case
		    }
		    return playerPosition.dst2(position) < (radius * radius);
		}


		   public void onPlayerCollision() {
		        System.out.println("Touched " + name + "! Entering " + level.getName());
		    }
	    
	    public Vector2 getPosition() {
	        return position;
	    }

	    public void dispose() {
	        // Dispose any resources if needed
	    }
	}
    

}
