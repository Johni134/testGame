package ru.brainmove.utils;

public class AnimateTimer {

    private float animateTimer;
    private float animateInterval;

    public AnimateTimer(float animateInterval) {
        this.animateInterval = animateInterval;
        this.animateTimer = 0f;
    }

    public boolean checkInterval(float deltaTime) {
        boolean resetedInterval = false;
        animateTimer += deltaTime;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            resetedInterval = true;
        }
        return resetedInterval;
    }
}
