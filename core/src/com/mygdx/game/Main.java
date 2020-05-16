package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screen.MenuScreen;
import screen.PlayScreen;

public class Main extends Game {
	public  static final int V_HEIGHT =1080;
	public  static final int V_WIDTH = 2100;
	public  static final float PPM= 100;

	public  static final short DEFAULT= 1;
	public  static final short SHIP_HERO_BIT= 2;
	public  static final short SHIP_ENEMY_BIT= 4;
	public  static final short ASTEROID= 8;
	public  static final short BLASTER_HERO= 16;
	public  static final short BLASTER_ENEMY= 32;
	public  static final short BORDER= 64;
	public  static final short MENU_BIT= 128;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}


}
