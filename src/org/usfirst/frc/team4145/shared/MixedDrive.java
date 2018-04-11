package org.usfirst.frc.team4145.shared;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class MixedDrive extends MecanumDrive{

    private WPI_TalonSRX kFrontLeft;
    private WPI_TalonSRX kFrontRight;
    private WPI_TalonSRX kRearLeft;
    private WPI_TalonSRX kRearRight;

    public MixedDrive (WPI_TalonSRX frontLeft, WPI_TalonSRX rearLeft, WPI_TalonSRX frontRight, WPI_TalonSRX rearRight){
        super(frontLeft, rearLeft, frontRight, rearRight);
        kFrontLeft = frontLeft;
        kFrontRight = frontRight;
        kRearLeft = rearLeft;
        kRearRight = rearRight;
        setName("MecanumDifferentialDrive");
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        kFrontLeft.set(ControlMode.Velocity, leftSpeed);
        kFrontRight.set(ControlMode.Velocity, -rightSpeed);
        kRearLeft.set(ControlMode.Follower, kFrontLeft.getDeviceID());
        kRearRight.set(ControlMode.Follower, kFrontRight.getDeviceID());
        m_safetyHelper.feed();
    }

}
