package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChallengeScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    
    private Vector2 fixedBackgroundPosition;
    
    private SpriteBatch projectileBatch;
    private Texture projectileTexture;
    private Vector2 shootingDirection;
    private boolean shooting;

    private List<Projectile> projectiles;

    // Player sprite and position
    private Texture playerTexture;
    private Sprite playerSprite;
    private Vector2 playerPosition;

    private OrthographicCamera camera;
    private Vector2 backgroundPosition;

    private Vector2 object1Position;
    private Vector2 object2Position;
    private float objectRadius = 10f;
    private boolean gameOver = false;

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("challenge.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playerTexture = new Texture(Gdx.files.internal("temp.png"));
        playerSprite = new Sprite(playerTexture);
        playerPosition = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        playerSprite.setPosition(playerPosition.x - playerSprite.getWidth() / 2f, playerPosition.y - playerSprite.getHeight() / 2f);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        backgroundPosition = new Vector2(0, 0);

        object1Position = new Vector2(300, 300);
        object2Position = new Vector2(500, 500);

        projectileBatch = new SpriteBatch();
        projectileTexture = new Texture(Gdx.files.internal("white_pixel.png"));
        shootingDirection = new Vector2(0, 1); // Initial shooting direction (up)
        shooting = false;

        projectiles = new ArrayList<>();

        fixedBackgroundPosition = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateCamera();
        updateProjectiles(delta);
        checkCollisions();
        draw();
    }

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

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!shooting) {
                projectiles.add(new Projectile(new Vector2(playerPosition), new Vector2(shootingDirection)));
                shooting = true;
            }
        } else {
            shooting = false;
        }
    }

    private void updateCamera() {
        float lerp = 0.1f;
        camera.position.lerp(new Vector3(playerPosition.x, playerPosition.y, 0), lerp);
        camera.update();
    }
    
    private void updateProjectiles(float delta) {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update(delta);

            // Remove projectiles that go off-screen
            if (projectile.getPosition().x < 0 || projectile.getPosition().x > Gdx.graphics.getWidth() ||
                    projectile.getPosition().y < 0 || projectile.getPosition().y > Gdx.graphics.getHeight()) {
                iterator.remove();
            }
        }
    }

    private void drawProjectiles() {
        projectileBatch.begin();
        for (Projectile projectile : projectiles) {
            projectile.draw(projectileBatch);
        }
        projectileBatch.end();
    }

    private void checkCollisions() {
    	
        if (playerPosition.dst(object1Position) < (objectRadius + playerSprite.getWidth() / 2f)) {
            gameOver = true;
        }

        if (playerPosition.dst(object2Position) < (objectRadius + playerSprite.getWidth() / 2f)) {
            gameOver = true;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, backgroundPosition.x, backgroundPosition.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw objects (dots) in red
        batch.setColor(1, 0, 0, 1); // Set color to red
        batch.draw(playerTexture, object1Position.x - objectRadius, object1Position.y - objectRadius, objectRadius * 2, objectRadius * 2);
        batch.draw(playerTexture, object2Position.x - objectRadius, object2Position.y - objectRadius, objectRadius * 2, objectRadius * 2);
        batch.setColor(1, 1, 1, 1); // Reset color to white

        // Draw player sprite with scaling
        playerSprite.setPosition(playerPosition.x - playerSprite.getWidth() / 2f, playerPosition.y - playerSprite.getHeight() / 2f);
        playerSprite.draw(batch);

        drawProjectiles();

        batch.end();

        if (gameOver) {
            renderGameOver();
        }
    }

    private void renderGameOver() {
        System.out.println("Game Over!");
        Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        playerTexture.dispose();
        projectileBatch.dispose();
        projectileTexture.dispose();
    }
}
