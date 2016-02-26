package osfma.mcm.fhooe.at.livetickerprivate.model;

/**
 * Created by Tob0t on 25.02.2016.
 */
public class GameSet {
    private int scoreTeam1;
    private int scoreTeam2;
    private boolean finished;
    private boolean running;

    public GameSet() {
    }

    public GameSet(int scoreTeam1, int scoreTeam2, boolean running) {
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.running = running;
    }

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isRunning() {
        return running;
    }
}
