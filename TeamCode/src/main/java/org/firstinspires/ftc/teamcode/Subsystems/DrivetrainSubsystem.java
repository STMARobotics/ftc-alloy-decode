package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DrivetrainSubsystem extends SubsystemBase {
    private final MecanumDrive solversDrive;
    private final IMU revimu;
    public DrivetrainSubsystem(HardwareMap hardwareMap) {
        //create motors
        Motor frontLeft =  new Motor(hardwareMap, "leftFront");
        Motor frontRight = new Motor(hardwareMap, "rightFront");
        Motor backLeft = new Motor(hardwareMap, "leftRear");
        Motor backRight = new Motor(hardwareMap, "rightRear");
        //invert all motors so it drives the right way
        //frontLeft.setInverted(true);
        //frontRight.setInverted(true);
        //backLeft.setInverted(true);
        //backRight.setInverted(true);
        //create solvers mecanum drive
        solversDrive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
        //create imu
        revimu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        revimu.initialize(parameters);
        revimu.resetYaw();
    }
    public void drive(double x, double y,double r, boolean squareInputs) {
        solversDrive.driveFieldCentric(
                x,
                y,
                r,
                getHeading(),   // gyro value passed in here must be in degrees
                squareInputs
        );
    }
    public double getHeading() {
        return revimu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    public void stop() {
        solversDrive.stop();
    }
    public  void resetHeading() {
        revimu.resetYaw();
    }
}
