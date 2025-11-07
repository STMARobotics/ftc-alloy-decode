package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class TeleOp1 extends LinearOpMode {

    // This variable determines whether the following program
    // uses field-centric or robot-centric driving styles. The
    // differences between them can be read here in the docs:
    // https://docs.ftclib.org/ftclib/features/drivebases#control-scheme
    static final boolean FIELD_CENTRIC = true;

    @Override
    public void runOpMode() throws InterruptedException {
        //create motors
        Motor frontLeft =  new Motor(hardwareMap, "leftFront");
        Motor frontRight = new Motor(hardwareMap, "rightFront");
        Motor backLeft = new Motor(hardwareMap, "leftRear");
        Motor backRight = new Motor(hardwareMap, "rightRear");
        //invert all motors so it drives the right way
        frontLeft.setInverted(true);
        frontRight.setInverted(true);
        backLeft.setInverted(true);
        backRight.setInverted(true);

        // constructor takes in frontLeft, frontRight, backLeft, backRight motors
        // IN THAT ORDER
        MecanumDrive drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
        //Motor.GoBILDA.RPM_312

        // This is the built-in IMU in the REV hub.
        // We're initializing it by its default parameters
        // and name in the config ('imu'). The orientation
        // of the hub is important. Below is a model
        // of the REV Hub and the orientation axes for the IMU.
        //
        //                           | Z axis
        //                           |
        //     (Motor Port Side)     |   / X axis
        //                       ____|__/____
        //          Y axis     / *   | /    /|   (IO Side)
        //          _________ /______|/    //      I2C
        //                   /___________ //     Digital
        //                  |____________|/      Analog
        //
        //                 (Servo Port Side)
        //
        // (unapologetically stolen from the road-runner-quickstart)

        // Creates a PIDController with gains kP, kI, kD
        PIDController pid = new PIDController(.01  , 0, 0);
        pid.setSetPoint(90);
        pid.setTolerance(2); 

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();
        //RevIMU imu = new RevIMU(hardwareMap, "imu");
        //imu.init();

        // the extended gamepad object
        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {
            telemetry.addData("y-axis",driverOp.getLeftY());
            telemetry.addData("left x", driverOp.getLeftX());
            telemetry.addData("right x",driverOp.getRightX());
            telemetry.addData("pid value",pid.getP());
            telemetry.addData("imu",imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.update();

            //reset IMU
            if (driverOp.getButton(GamepadKeys.Button.START)) {
                imu.resetYaw();
                telemetry.addData("imu updated",1);
                telemetry.update();
            }

            // Driving the mecanum base takes 3 joystick parameters: leftX, leftY, rightX.
            // These are related to the left stick x value, left stick y value, and
            // right stick x value respectively. These values are passed in to represent the
            // strafing speed, the forward speed, and the turning speed of the robot frame
            // respectively from [-1, 1].

            if (driverOp.getButton(GamepadKeys.Button.LEFT_BUMPER)){
                //quarter power
                telemetry.addData("1/3 speed",1);
                drive.driveFieldCentric(
                        driverOp.getLeftX()/3,
                        driverOp.getLeftY()/3,
                        driverOp.getRightX()/3,
                        imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES),   // gyro value passed in here must be in degrees
                        false
                );
            } else if (driverOp.getButton(GamepadKeys.Button.A)) {
                //drive at fixed heading
                    /*int turn = 0;
                    int targetHeading = 90;
                    if (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) <= targetHeading) {
                        turn = 1;
                    } else if (imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) >= targetHeading) {
                        turn = -1;
                    } else {
                        turn = 0;
                    }
                     */
                double output = pid.calculate(
                        imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)  // the measured value
                );

                drive.driveFieldCentric(
                        driverOp.getLeftX(),
                        driverOp.getLeftY(),
                        -output,
                        imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES),   // gyro value passed in here must be in degrees
                        false
                );
                telemetry.addData("pid output", output);
            } else {
                //full power
                // optional fifth parameter for squared inputs
                drive.driveFieldCentric(
                        driverOp.getLeftX(),
                        driverOp.getLeftY(),
                        driverOp.getRightX(),
                        imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES),   // gyro value passed in here must be in degrees
                        false
                );
            }
        }
    }
}