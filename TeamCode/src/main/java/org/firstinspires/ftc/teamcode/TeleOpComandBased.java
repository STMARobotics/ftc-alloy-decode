package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.RotateToHeading;
import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;

@TeleOp
public class TeleOpComandBased extends CommandOpMode {

    @Override
    public void initialize() {
        //create subystems
        DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem(hardwareMap);
        RotateToHeading rotateToHeading = new RotateToHeading(drivetrainSubsystem,
                90,
                ()-> -gamepad1.left_stick_y,
                ()-> -gamepad1.left_stick_x
        );
        //run drive command
        RunCommand teleOpDrive = new RunCommand(() -> drivetrainSubsystem.drive(
                -gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x
        ),drivetrainSubsystem);

        register(drivetrainSubsystem);

        drivetrainSubsystem.setDefaultCommand(teleOpDrive);

        //bind buttons to commands
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileHeld(rotateToHeading);
    }
}
