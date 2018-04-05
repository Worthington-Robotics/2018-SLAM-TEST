package org.usfirst.frc.team4145.subsystems.RobotDriveV3;


public class AutoDrive implements DriveUpdater{

    public double[] toWrite = {0, 0};

    AutoDrive() {

    }

    public double[] update() {
        return toWrite;
    }




}
