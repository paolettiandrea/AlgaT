package algat.model.lesson.block.interactive;

import javafx.animation.Transition;
import javafx.util.Duration;


public abstract class InteractiveContentController {

    private Duration breakDuration;
    private boolean isCompletelyEndedFlag = false;
    private double minRate;
    private double maxRate;


    protected InteractiveContentController(Duration breakDuration, double minRate, double maxRate) {
        this.breakDuration = breakDuration;
        this.minRate = minRate;
        this.maxRate = maxRate;
    }


    protected void animationEnded() {
        isCompletelyEndedFlag = true;
    }

    public abstract Transition step();

    public void reset() {
        isCompletelyEndedFlag = false;
    }

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public double getMinRate() {
        return minRate;
    }

    public double getMaxRate() {
        return maxRate;
    }

    public boolean isCompletelyEnded() {
        return  isCompletelyEndedFlag;
    }
}
