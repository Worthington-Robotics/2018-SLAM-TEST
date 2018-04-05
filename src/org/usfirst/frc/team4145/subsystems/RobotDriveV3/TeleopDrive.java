package org.usfirst.frc.team4145.subsystems.RobotDriveV3;

import edu.wpi.first.wpilibj.DriverStation;


public class TeleopDrive{

    //used internally for data

    private double[] lastInputSet = {0, 0, 0}; //last input set from joystick update


    //PID variables


    private double index = 0;

    TeleopDrive(){

    }

    public double[] update() {

        return lastInputSet;
    }

    private double[] motionProfileTestMode(){
        double[] test = {0.0, 0.0, 0.0};
        test[0] = -0.000104 * index;
        if (DriverStation.getInstance().isOperatorControl() && DriverStation.getInstance().isEnabled()) index++;
        else index = 0;
        return test;
    }


}
