package com.sorry.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private Sorry game;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(Sorry sorry) {
        this.game = sorry;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 512, 512);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        map = new TmxMapLoader().load("sorry.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        renderer.setView(camera);
        renderer.render();

        game.batch.begin();
        game.board.draw(game.batch);

        // TODO:using isKeyJustPressed for testing
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            Piece p = (Piece) game.board.pieceMap.get("blue"); // TODO: change to currentTurn
            game.board.move(p, randomRoll());
            game.board.draw(game.batch);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            Piece p = (Piece) game.board.pieceMap.get("yellow");
            game.board.move(p, randomRoll());
            game.board.draw(game.batch);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            Piece p = (Piece) game.board.pieceMap.get("green");
            game.board.move(p, randomRoll());
            game.board.draw(game.batch);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            Piece p = (Piece) game.board.pieceMap.get("red");
            game.board.move(p, randomRoll());
            game.board.draw(game.batch);
        }

        game.batch.end();

    }

    // used for testing, returns a random int between 1 and 12
    private int randomRoll() {
        return (int) ((Math.random() * (12 - 1)) + 1);
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        game.dispose();
    }
}
