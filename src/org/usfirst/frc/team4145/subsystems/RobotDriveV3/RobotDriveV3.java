package org.usfirst.frc.team4145.subsystems.RobotDriveV3;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4145.Constants;
import org.usfirst.frc.team4145.RobotMap;
import org.usfirst.frc.team4145.shared.MixedDrive;


public class RobotDriveV3 extends Subsystem {

    //used internally for data
    private MixedDrive m_MixedDriveInstance;
    private Notifier m_NotifierInstance;

    private double[] lastTeleopOutput = {0,0}; //y, x
    private double[] lastAutoOutput = {0,0,0}; //left, right, turn (not used by profiling)

    public RobotDriveV3() {
        m_MixedDriveInstance = new MixedDrive(RobotMap.driveFrontLeft, RobotMap.driveRearLeft, RobotMap.driveFrontRight, RobotMap.driveRearRight);
        m_NotifierInstance = new Notifier(periodic);
        startPeriodic();
    }

    public void startPeriodic(){
        m_NotifierInstance.startPeriodic(Constants.DRIVETRAIN_UPDATE_RATE);
    }

    private Runnable periodic = () -> {
        if(DriverStation.getInstance().isEnabled() && DriverStation.getInstance().isOperatorControl()) {
            driveTank(150 * (512/75), 0 * (512/75));
        }
        smartDashboardUpdates();
    };

    public void setAutoDrive(double[] toSet){
        lastAutoOutput = toSet;
    }

    public double getGyro() {
        return ((RobotMap.ahrs.getYaw() + 360) % 360); //add 360 to make all positive then mod by 360 to get remainder
    }

    public void setNeutralMode(boolean brake) {
        if (brake) {
            setDynamicBrakeMode(true, true, true, true);
        } else {
            setDynamicBrakeMode(false, false, false, false);
        }
    }

    public void setDynamicBrakeMode(boolean brakeFL, boolean brakeRL, boolean brakeFR, boolean brakeRR) {
        if (brakeFL) {
            RobotMap.driveFrontLeft.setNeutralMode(NeutralMode.Brake);
        } else {
            RobotMap.driveFrontLeft.setNeutralMode(NeutralMode.Coast);
        }
        if (brakeRL) {
            RobotMap.driveRearLeft.setNeutralMode(NeutralMode.Brake);
        } else {
            RobotMap.driveRearLeft.setNeutralMode(NeutralMode.Coast);
        }
        if (brakeFR) {
            RobotMap.driveFrontRight.setNeutralMode(NeutralMode.Brake);
        } else {
            RobotMap.driveFrontRight.setNeutralMode(NeutralMode.Coast);
        }
        if (brakeRR) {
            RobotMap.driveRearRight.setNeutralMode(NeutralMode.Brake);
        } else {
            RobotMap.driveRearRight.setNeutralMode(NeutralMode.Coast);
        }
    }

    public void configTeleop(){
        RobotMap.driveFrontLeft.set(ControlMode.PercentOutput, 0);
        RobotMap.driveFrontRight.set(ControlMode.PercentOutput, 0);
        RobotMap.driveRearLeft.set(ControlMode.PercentOutput, 0);
        RobotMap.driveRearRight.set(ControlMode.PercentOutput, 0);
    }

    public void configAuto(){
        System.out.println("Init auto config");
        RobotMap.driveFrontLeft.set(ControlMode.Velocity,0);
        RobotMap.driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        RobotMap.driveFrontLeft.selectProfileSlot(0,0);
        RobotMap.driveFrontLeft.config_kF(0,Constants.getLeftKF(),0);
        RobotMap.driveFrontLeft.config_kP(0,Constants.getLeftKP(),0);
        RobotMap.driveFrontLeft.config_kI(0,Constants.getLeftKI(),0);
        RobotMap.driveFrontLeft.config_kD(0,Constants.getLeftKD(),0);
        RobotMap.driveFrontLeft.config_IntegralZone(0,0,0);
        System.out.println("left talon config'd");

        RobotMap.driveFrontRight.set(ControlMode.Velocity,0);
        RobotMap.driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        RobotMap.driveFrontRight.selectProfileSlot(0,0);
        RobotMap.driveFrontRight.config_kF(0,Constants.getRightKF(),0);
        RobotMap.driveFrontRight.config_kP(0,Constants.getRightKP(),0);
        RobotMap.driveFrontRight.config_kI(0,Constants.getRightKI(),0);
        RobotMap.driveFrontRight.config_kD(0,Constants.getRightKD(),0);
        RobotMap.driveFrontRight.config_IntegralZone(0,0,0);
        System.out.println("right talon config'd");

        RobotMap.driveRearLeft.set(ControlMode.Follower, RobotMap.driveFrontLeft.getBaseID());
        RobotMap.driveRearRight.set(ControlMode.Follower, RobotMap.driveFrontRight.getBaseID());
        System.out.println("rear talons config'd");
    }

    private void smartDashboardUpdates() {
        SmartDashboard.putNumber("FPGA Time", Timer.getFPGATimestamp());
        SmartDashboard.putNumber("Gyro Angle", getGyro());
        SmartDashboard.putNumber("Left Motor Voltage", RobotMap.driveFrontLeft.getMotorOutputVoltage());
        SmartDashboard.putNumber("Right Motor Voltage", RobotMap.driveFrontRight.getMotorOutputVoltage());
        SmartDashboard.putNumber("Left Talon Voltage", RobotMap.driveRearLeft.getBusVoltage());
        SmartDashboard.putNumber("Right Talon Voltage", RobotMap.driveRearRight.getBusVoltage());
    }

    /**
     * Method for exposing the underlying MixedDrive object to mecanum control
     *
     * @param ySpeed    forward / backward speed of the robot
     * @param xSpeed    left / right speed of the robot
     * @param zRotation rotational speed of the robot
     */
    private void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
        m_MixedDriveInstance.driveCartesian(ySpeed, xSpeed, zRotation);
    }

    /**
     * Method for exposing the underlying MixedDrive object for tank control
     *
     * @param leftSpeed  speed of left side of drivetrain
     * @param rightSpeed speed of right side of drivetrain
     */
    private void driveTank(double leftSpeed, double rightSpeed) {
        m_MixedDriveInstance.tankDrive(leftSpeed, rightSpeed);
    }

    protected void initDefaultCommand() {

    }

}
