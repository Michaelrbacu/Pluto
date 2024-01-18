package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Projectile {

    private Vector2 position;
    private Vector2 velocity; 
    private float speed;
    private float width;

    private Sprite sprite;

    public Projectile(Vector2 position, Vector2 direction) {
        this.position = new Vector2(position);
        this.velocity = new Vector2(direction).nor(); // Normalize the direction vector to get velocity
        this.speed = 400f; // Adjust the speed as needed
        this.width = 10f; // Set the width of the projectile

        // Load a simple white pixel texture for the projectile
        Texture texture = new Texture(Gdx.files.internal("white_pixel.png"));
        sprite = new Sprite(texture);
        sprite.setSize(width, width);
        sprite.setPosition(position.x - width / 2f, position.y - width / 2f);
    }

    public boolean isOutOfBounds() {
        // Implement the logic to check whether the projectile is out of bounds
        // For example, check if the projectile is beyond the screen boundaries
        return position.x < 0 || position.x > Gdx.graphics.getWidth() ||
               position.y < 0 || position.y > Gdx.graphics.getHeight();
    }

    public void update(float delta) {
        position.x += velocity.x * speed * delta;
        position.y += velocity.y * speed * delta;
        sprite.setPosition(position.x - width / 2f, position.y - width / 2f);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Vector2 getPosition() {
        return position;
    }
    
    public float getWidth() {
        return width;
    }
}
