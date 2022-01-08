package com.sorry.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Board {
    int width, height, squareSize;
    Texture pieceTextures;
    TextureAtlas pieceAtlas;

    Piece pieces[][];

    HashMap<String, Integer> spriteIndexMap; // maps sprite to index
    Array<Sprite> sprites;
    HashMap<String, Piece> pieceMap; // makes grabbing pieces O(1)

    public Board(int size, TextureAtlas pieceAtlas) {
        this.width = width;
        this.height = height;
        squareSize = size/16;

        this.pieceAtlas = pieceAtlas;
        sprites = pieceAtlas.createSprites();
        float spriteSize = (int) sprites.get(0).getHeight();
        float scale = ((float) squareSize)/spriteSize;

        for (Sprite sprite: sprites) {
            sprite.setScale(scale);
        }

        spriteIndexMap = new HashMap<String, Integer>();
        pieceMap = new HashMap<String, Piece>();

        int index = 0;
        for (PieceColor color : PieceColor.values()) {
            String spriteName = new Piece(color).getSpriteName();
            spriteIndexMap.put(spriteName, index++);
        }

        pieces = new Piece[16][16];

        pieces[4][14] = new Piece(PieceColor.BLUE);
        pieces[4][14].setX(4);
        pieces[4][14].setY(14);
        pieceMap.put("blue", pieces[4][14]);

        pieces[14][11] = new Piece(PieceColor.YELLOW);
        pieces[14][11].setX(14);
        pieces[14][11].setY(11);
        pieceMap.put("yellow", pieces[14][11]);

        pieces[11][1] = new Piece(PieceColor.GREEN);
        pieces[11][1].setX(11);
        pieces[11][1].setY(1);
        pieceMap.put("green", pieces[11][1]);

        pieces[1][4] = new Piece(PieceColor.RED);
        pieces[1][4].setX(1);
        pieces[1][4].setY(4);
        pieceMap.put("red", pieces[1][4]);
    }

    private void drawPiece(SpriteBatch batch, int col, int row) {
        Piece p = pieces[col][row];
        if (p == null) {
            return;
        }
        String spriteName = p.getSpriteName();

        Sprite sprite = sprites.get(spriteIndexMap.get(spriteName));
        sprite.setX(-16 + (col * squareSize));
        sprite.setY(-16 + (row * squareSize));
        sprite.draw(batch);
    }

    public void draw(SpriteBatch batch) {
        for (int col = 0; col < 16; col++) {
            for (int row = 0; row < 16; row++) {
                drawPiece(batch, col, row);
            }
        }
    }

    public void move(Piece p, int distance) {
        System.out.println("called move.");
        switch(p.getColor()) {
            case BLUE:
                moveBlue(p, distance);
                break;
            case YELLOW:
                moveYellow(p, distance);
                break;
            case GREEN:
                moveGreen(p, distance);
                break;
            case RED:
                moveRed(p, distance);
                break;
        }
    }

    public void movePiece(Piece p, int fromX, int fromY, int toX, int toY) {
        // if a piece already exists where we are moving, bump it
        System.out.println("fromX:" + fromX + " fromY:" + fromY + " toX:" + toX + " toY:" + toY);
        if (pieces[toX][toY] != null) { bumpPiece(pieces[toX][toY]); }
        pieces[toX][toY] = p;
        pieces[toX][toY].setX(toX);
        pieces[toX][toY].setY(toY);
        pieces[fromX][fromY] = null;
    }

    public void bumpPiece(Piece p) {
        System.out.println("bumped piece " + p.pieceColor);
        if (p == null) { return; }
        // top row
        if (p.getX() >= 0 && p.getX() < 15 && p.getY() == 15) {
            movePiece(p, p.getX(), p.getY(), p.getX() + 1, p.getY());
        }
        // right col
        if (p.getX() == 15 && p.getY() <= 15 && p.getY() > 0) {
            movePiece(p, p.getX(), p.getY(), p.getX(), p.getY() - 1);

        }
        // bot row
        if (p.getX() <= 15 && p.getX() > 0 && p.getY() == 0) {
            movePiece(p, p.getX(), p.getY(), p.getX() - 1, p.getY());

        }
        // left col
        if (p.getX() == 0 && p.getY() >= 0 && p.getY() < 15) {
            movePiece(p, p.getX(), p.getY(), p.getX(), p.getY() + 1);
        }
    }

    private void colorAgnosticMove(Piece p, int distance) {
        // top row
        if (p.getX() >= 0 && p.getX() < 15 && p.getY() == 15) {
            System.out.println("color ag. top normal.");
            if ((p.getX() + distance) > 15) {
                System.out.println("color ag. top corner.");
                // top row corner case
                int remainder = (p.getX() + distance) - 15;
                movePiece(p, p.getX(), p.getY(), 15, 15 - remainder);
            } else {
                movePiece(p, p.getX(), p.getY(), p.getX() + distance, p.getY());
            }
            return;
        }
        // right col
        if (p.getX() == 15 && p.getY() <= 15 && p.getY() > 0) {
            System.out.println("color ag. right normal.");
            if ((p.getY() - distance) < 0) {
                // right row corner case
                int remainder = distance - p.getY();
                movePiece(p, p.getX(), p.getY(), 15 - remainder, 0);
            } else {
                movePiece(p, p.getX(), p.getY(), p.getX(), p.getY() - distance);
            }
            return;
        }
        // bot row
        if (p.getX() <= 15 && p.getX() > 0 && p.getY() == 0) {
            System.out.println("color ag. bot normal.");
            if ((p.getX() - distance) < 0) {
                // bot row corner case
                int remainder = distance - p.getX();
                movePiece(p, p.getX(), p.getY(), 0, remainder);
            } else {
                movePiece(p, p.getX(), p.getY(), p.getX() - distance, p.getY());
            }
            return;
        }
        // left col
        if (p.getX() == 0 && p.getY() >= 0 && p.getY() < 15) {
            System.out.println("color ag. normal.");
            if ((p.getY() + distance) > 15) {
                // left col corner case
                int remainder = distance - (15 - p.getY());
                movePiece(p, p.getX(), p.getY(), remainder, 15);
            } else {
                movePiece(p, p.getX(), p.getY(), p.getX(), p.getY() + distance);
            }
            return;
        }
    }

    // if blue piece is in [2][9] don't move, blue wins.
    // if the blue piece is in pieces[4][14], then move up 1 before moving right the remainder
    // if the blue piece is in pieces[2][15], then move into the safe zone run
    // if the blue piece is already the safe zone run
    // if the blue pieces x value + the distance to be moved is > 15: corner case

    public void moveBlue(Piece p, int distance) {
        // special start cases
        if (p.getX() == 2 && p.getY() == 9) { // already in safe zone
            return;
        } else if (p.getX() == 4 && p.getY() == 14) { // start zone
            if ((p.getX() + distance) > 15) {
                //start zone to corner case
                System.out.println("blue top right corner.");
                int remainder = (p.getX() + distance - 1) - 15;
                pieces[15][15-remainder] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(15);
                p.setY(15-remainder);
            } else {
                // move up 1 then right the remainder
                int remainder = distance - 1;
                System.out.println("blue start.");
                pieces[4+remainder][15] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(4+remainder);
                p.setY(15);
            }
        } else if (p.getX() == 2 && p.getY() <= 15 && p.getY() > 9) { // safe zone run
            System.out.println("blue safe zone.");
            // move into safe zone
            if ((p.getY() - distance) > 9) {
                pieces[2][p.getY() - distance] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(2);
                p.setY(p.getY() - distance);
            } else {
                // TODO: increment blue score
                pieces[2][9] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(2);
                p.setY(9);
            }
        } else { // color agnostic cases
            colorAgnosticMove(p, distance);
        }
    }

    public  void moveYellow(Piece p, int distance) {
        // special start cases
        if (p.getX() == 9 && p.getY() == 13) { // already in safe zone
            return;
        } else if (p.getX() == 14 && p.getY() == 11) { // start zone
            if ((p.getY() - distance) < 0) { // yellow start corner case
                System.out.println("yellow bot right corner.");
                int remainder = (distance - p.getY() - 1);
                pieces[15-remainder][0] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(15-remainder);
                p.setY(0);
            } else { // yellow start normal case
                System.out.println("yellow start.");
                int remainder = (distance - 1);
                pieces[15][11-remainder] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(15);
                p.setY(11-remainder);
            }
        } else if (p.getX() > 9 && p.getX() <= 15 && p.getY() == 13) { // safe zone run
            System.out.println("yellow safe zone.");
            if ((p.getX() - distance) > 9) { // move within safe zone run
                pieces[p.getX()-distance][p.getY()] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(p.getX() - distance);
                p.setY(13);
            } else { // move to safe zone
                // TODO: increment yellow score
                pieces[9][13] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(9);
                p.setY(13);
            }
        } else { // color agnostic cases
            colorAgnosticMove(p, distance);
        }
    }

    public void moveGreen(Piece p, int distance) {
        // TODO:
        // special start cases
        if (p.getX() == 13 && p.getY() == 6) { // already in safe zone
            return;
        } else if (p.getX() == 11 && p.getY() == 1) { // start zone
                if ((p.getX() - distance - 1) < 0) { // green start corner case
                    System.out.println("green bot left corner.");
                    int remainder = (distance - p.getX());
                    pieces[0][remainder] = p;
                    pieces[p.getX()][p.getY()] = null;
                    p.setX(0);
                    p.setY(remainder);
                } else {
                    System.out.println("green start.");
                    int remainder = (distance - 1);
                    pieces[11-remainder][0] = p;
                    pieces[p.getX()][p.getY()] = null;
                    p.setX(11-remainder);
                    p.setY(0);
                }
        } else if (p.getX() == 13 && p.getY() >= 0 & p.getY() < 6) { //safe zone run
            System.out.println("green safe zone run.");
            if ((p.getY() + distance) < 6) { // move within safe zone run
                    pieces[13][p.getY() + distance] = p;
                    pieces[p.getX()][p.getY()] = null;
                    p.setX(13);
                    p.setY(p.getY() + distance);
                } else { // move to safe zone
                    // TODO: increment green score
                    pieces[13][6] = p;
                    pieces[p.getX()][p.getY()] = null;
                    p.setX(13);
                    p.setY(6);
                }
        } else { // color agnostic cases
            colorAgnosticMove(p, distance);
        }
    }

    public void moveRed(Piece p, int distance) {
        // TODO:
        if (p.getX() == 6 && p.getY() == 2) { // already in safe zone
            return;
        } else if (p.getX() == 1 && p.getY() == 4) { // red start zone
            // TODO: do we need color specific corner cases? I don't think we do
            if ((p.getY() + distance - 1) > 15) { // red start corner case
                System.out.println("red start corner case.");
                int remainder = (p.getY() + distance - 1) - 15;
                pieces[remainder][15] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(remainder);
                p.setY(15);
            } else { // red start normal case
                System.out.println("red start.");
                int remainder = (distance - 1);
                pieces[0][4 + remainder] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(0);
                p.setY(4 + remainder);
            }
        } else if (p.getX() >= 0 && p.getX() < 6 && p.getY() == 2) { // red safe zone
            System.out.println("red safe zone run.");
            if ((p.getX() + distance) < 6) { // move within safe zone run
                pieces[p.getX() + distance][2] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(p.getX() + distance);
                p.setY(2);
            } else { // move to safe zone
                // TODO: increment red score
                pieces[6][2] = p;
                pieces[p.getX()][p.getY()] = null;
                p.setX(6);
                p.setY(2);
            }
        } else { // color agnostic cases
            colorAgnosticMove(p, distance);
        }
    }



}
