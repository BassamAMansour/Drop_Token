package com.drop_token.data_types;

public class Slot {
    private int tokenId;
    private int streakHorizontal;
    private int streakVertical;
    private int streakDiagonal;

    public Slot(int tokenId) {
        this(tokenId, 0, 0, 0);
    }

    public Slot(int tokenId, int streakHorizontal, int streakVertical, int streakDiagonal) {
        this.tokenId = tokenId;
        this.streakHorizontal = streakHorizontal;
        this.streakVertical = streakVertical;
        this.streakDiagonal = streakDiagonal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == getClass()) {
            return ((Slot) obj).getTokenId() == tokenId;
        }
        return super.equals(obj);
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getStreakHorizontal() {
        return streakHorizontal;
    }

    public void setStreakHorizontal(int streakHorizontal) {
        this.streakHorizontal = streakHorizontal;
    }

    public int getStreakVertical() {
        return streakVertical;
    }

    public void setStreakVertical(int streakVertical) {
        this.streakVertical = streakVertical;
    }

    public int getStreakDiagonal() {
        return streakDiagonal;
    }

    public void setStreakDiagonal(int streakDiagonal) {
        this.streakDiagonal = streakDiagonal;
    }
}
