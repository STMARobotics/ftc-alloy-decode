package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.TriggerReader;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class MotorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //create motors
        Motor frontLeft =  new Motor(hardwareMap, "leftFront");
        Motor frontRight = new Motor(hardwareMap, "rightFront");
        Motor backLeft = new Motor(hardwareMap, "leftRear");
        Motor backRight = new Motor(hardwareMap, "rightRear");
        //invert all motors so it drives the right way
        frontLeft.setInverted(true);
        //frontRight.setInverted(true);
        backLeft.setInverted(true);
        //backRight.setInverted(true);

        // constructor takes in frontLeft, frontRight, backLeft, backRight motors
        // IN THAT ORDER
        MecanumDrive drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();
        //RevIMU imu = new RevIMU(hardwareMap, "imu");
        //imu.init();

        // the extended gamepad object
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {
            telemetry.addData("y-axis",gamepad1.left_stick_y);
            telemetry.addData("left x", gamepad1.left_stick_x);
            telemetry.addData("right x", gamepad1.right_stick_x);
            telemetry.addData("imu",imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.update();

            //reset IMU
            if (gamepadEx1.getButton(GamepadKeys.Button.START)) {
                imu.resetYaw();
                telemetry.addData("imu updated",1);
                telemetry.update();
            } else if (gamepad1.right_bumper) {
                frontRight.set(1);
            } else if (gamepad1.left_bumper) {
                frontLeft.set(1);
            } else if (gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) == 1) {
                backRight.set(1);
            } else if (gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) == 1) {
                backLeft.set(1);
            } else {
                frontLeft.set(0);
                frontRight.set(0);
                backLeft.set(0);
                backRight.set(0);
            }
        }
    }
}