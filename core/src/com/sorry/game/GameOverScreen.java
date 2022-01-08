package com.sorry.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {

    private Sorry game;
    private OrthographicCamera camera;
    PieceColor winner;

    public GameOverScreen(Sorry sorry, PieceColor c) {
        this.winner = c;
        this.game = sorry;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 512, 512);
        // now that the game is over, create a new board to keep playing
        game.board = new Board(512, game.board.pieceAtlas);
    }
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

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

        game.batch.begin();
        game.font.draw(game.batch, winner + " wins!", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to return to the main menu.", 100, 100);
        game.batch.end();

        // TODO: got error "corrupted double-linked list" when trying to return to main menu
        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
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
        //game.dispose(); //TODO: this breaks things bad
    }
}
