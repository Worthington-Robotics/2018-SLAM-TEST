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
        setName("MecanumDifferentialDrive ");
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        kFrontLeft.set(ControlMode.Velocity, leftSpeed * m_maxOutput);
        kFrontRight.set(ControlMode.Velocity, -rightSpeed * m_maxOutput);
        m_safetyHelper.feed();
    }

    /**
     * Arcade drive method for differential drive platform.
     *
     * @param xSpeed        The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation     The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                      positive.
     * @param squaredInputs If set, decreases the input sensitivity at low speeds.
     */
    @SuppressWarnings("ParameterName")
    public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squaredInputs) {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        kRearLeft.set(limit(leftMotorOutput) * m_maxOutput);
        kFrontLeft.set(limit(leftMotorOutput) * m_maxOutput);
        kRearRight.set(-limit(rightMotorOutput) * m_maxOutput);
        kFrontRight.set(-limit(rightMotorOutput) * m_maxOutput);

        m_safetyHelper.feed();
    }

}
