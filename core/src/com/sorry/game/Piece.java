package com.sorry.game;

enum PieceColor {
    BLUE,
    YELLOW,
    GREEN,
    RED
}

public class Piece {
    int x, y;
    PieceColor pieceColor;

    public Piece(PieceColor c) {
        super();
        this.pieceColor = c;
    }

    public Piece(int a, int b) {
        super();
        this.x = a;
        this.y = b;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PieceColor getColor() {
        return pieceColor;
    }

    public String getSpriteName() {
        String s = new String();
        if (pieceColor.compareTo(PieceColor.BLUE) == 0) {
            s = "1blue";
        }
        if (pieceColor.compareTo(PieceColor.YELLOW) == 0) {
            s = "2green";
        }
        if (pieceColor.compareTo(PieceColor.GREEN) == 0) {
            s = "3green";
        }
        if (pieceColor.compareTo(PieceColor.RED) == 0) {
            s = "4red";
        }
        return s;
    }

    public Piece getPiece() {
        return this;
    }
}
