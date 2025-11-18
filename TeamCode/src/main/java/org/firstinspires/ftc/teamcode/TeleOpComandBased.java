package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Commands.RotateToHeading;
import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@TeleOp
public class TeleOpComandBased extends CommandOpMode {

    @Override
    public void initialize() {
        //create subystems
        DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem(hardwareMap);
        RotateToHeading rotateToHeading = new RotateToHeading(drivetrainSubsystem,
                90,
                ()-> -gamepad1.left_stick_x,
                ()-> gamepad1.left_stick_y
        );
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);
        //run drive command
        RunCommand teleOpDrive = new RunCommand(() -> drivetrainSubsystem.drive(
                -gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, true
        ),drivetrainSubsystem);

        register(drivetrainSubsystem);
        register(shooterSubsystem);
        register(intakeSubsystem);

        drivetrainSubsystem.setDefaultCommand(teleOpDrive);

        //bind buttons to commands
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);
        //
        gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileHeld(rotateToHeading);
        //
        gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).toggleWhenPressed(
                new StartEndCommand(shooterSubsystem::run, shooterSubsystem::stop, shooterSubsystem)
        );
        //
        gamepadEx1.getGamepadButton(GamepadKeys.Button.START).whenPressed(
                new InstantCommand(drivetrainSubsystem::resetHeading, drivetrainSubsystem)
        );
        //
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenHeld(
                new StartEndCommand(intakeSubsystem::runIntake, intakeSubsystem::stopIntake, intakeSubsystem)
        );
        //
        gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenHeld(
                new StartEndCommand(intakeSubsystem::moveToShoot, intakeSubsystem::stopIntake, intakeSubsystem)
        );
    }
}
