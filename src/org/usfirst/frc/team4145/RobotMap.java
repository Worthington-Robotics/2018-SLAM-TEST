/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4145;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4145.shared.LoggingSystem;
import org.usfirst.frc.team4145.subsystems.RobotDriveV4.RobotDriveV4;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    //logging system
    public static LoggingSystem loggingSystem;

    // actuators
    public static WPI_TalonSRX driveFrontLeft, driveRearLeft, driveFrontRight, driveRearRight; // need to use WPI_talonSRX for drivetrain use

    // sensors
    public static AHRS ahrs; // AHRS system on navx

    public static RobotDriveV4 robotDriveV4;


    public static void init() {
        // all general objects instantated here
        loggingSystem = new LoggingSystem();

        // all Motor controller objects
        driveFrontLeft = new WPI_TalonSRX(1);
        driveRearLeft = new WPI_TalonSRX(2);
        driveFrontRight = new WPI_TalonSRX(3);
        driveRearRight = new WPI_TalonSRX(4);

        // all sensor objects here
        ahrs = new AHRS(SPI.Port.kMXP); // finish declaring AHRS to MXP SPI bus
        ahrs.reset();

        robotDriveV4 = new RobotDriveV4();
        robotDriveV4.reset();

        addLoggingKeys();
    }

    private static void addLoggingKeys(){
        RobotMap.loggingSystem.addWatchKey("Auto State");
        RobotMap.loggingSystem.addWatchKey("Left Wheel Encoder");
        RobotMap.loggingSystem.addWatchKey("Right Wheel Encoder");
        RobotMap.loggingSystem.addWatchKey("Gyro Angle");
        RobotMap.loggingSystem.addWatchKey("Gyro Target");
        RobotMap.loggingSystem.addWatchKey("Lift Encoder");
        RobotMap.loggingSystem.addWatchKey("Lift Encoder Target");
        RobotMap.loggingSystem.addWatchKey("In Auto");
        RobotMap.loggingSystem.addWatchKey("Left Motor Voltage");
        RobotMap.loggingSystem.addWatchKey("Right Motor Voltage");
        RobotMap.loggingSystem.addWatchKey("Left Talon Voltage");
    }
}
