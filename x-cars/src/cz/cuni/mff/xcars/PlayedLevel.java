package cz.cuni.mff.xcars;

public class PlayedLevel {
    public byte starsNumber = 0;
    public float score = 0;
    
    public PlayedLevel(float score, byte stars) {
    	this.score = score;
    	this.starsNumber = stars;
    }
    
    @Override
    public String toString() {
    	return "stars:" + starsNumber + ";score:" + score;
    }
}