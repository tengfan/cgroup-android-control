package com.cgroup.androidcontrolinterface;

import android.app.Application;

/**
 * Created by Teng on 2015/9/23.
 */
public class androidcontrolinterface extends Application {
    protected Boolean standardIsRunning;
    protected Boolean myoIsRunning;
    protected Boolean isRunning;
    protected Boolean forceToStop;
    protected byte myoRunStopReverse;
    protected byte myoDirection;

    public Boolean getStandardIsRunning() {
        return standardIsRunning;
    }

    public void setStandardIsRunning(Boolean standardIsRunning) {
        this.standardIsRunning = standardIsRunning;
    }

    public Boolean getMyoIsRunning() {
        return myoIsRunning;
    }

    public void setMyoIsRunning(Boolean myoIsRunning) {
        this.myoIsRunning = myoIsRunning;
    }
    public Boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Boolean getForceToStop() {
        return forceToStop;
    }

    public void setForceToStop(Boolean forceToStop) {
        this.forceToStop = forceToStop;
    }

    public byte getMyoRunStopReverse() {
        return myoRunStopReverse;
    }

    public byte getMyoDirection() {
        return myoDirection;
    }

    public void setMyoRunStopReverse(byte myoRunStopReverse) {
        this.myoRunStopReverse = myoRunStopReverse;
    }

    public void setMyoDirection(byte myoDirection) {
        this.myoDirection = myoDirection;
    }
}
