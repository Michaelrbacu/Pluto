package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Projectile {

    private static final float SPEED = 500f; // Adjust the speed of the projectile as needed
    private static final float SIZE = 5f; // Adjust the size of the projectile as needed

    private Sprite sprite;
    private Vector2 position;
    private Vector2 velocity;

    public Projectile(Vector2 position, Vector2 direction) {
        this.position = new Vector2(position);
        this.velocity = new Vector2(direction).nor().scl(SPEED);

        // Load a simple white pixel texture for the projectile
        Texture texture = new Texture(Gdx.files.internal("white_pixel.png"));
        sprite = new Sprite(texture);
        sprite.setSize(SIZE, SIZE);
        sprite.setPosition(position.x - SIZE / 2f, position.y - SIZE / 2f);
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        sprite.setPosition(position.x - SIZE / 2f, position.y - SIZE / 2f);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Vector2 getPosition() {
        return position;
    }
}
