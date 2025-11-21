package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.RecycleCommand;
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
                -30,
                ()-> -gamepad1.left_stick_x,
                ()-> -gamepad1.left_stick_y
        );
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);
        RecycleCommand recycleCommand = new RecycleCommand(shooterSubsystem, intakeSubsystem);
        //run drive command
        RunCommand teleOpDrive = new RunCommand(() -> drivetrainSubsystem.drive(
                -gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, true
        ),drivetrainSubsystem);

        register(drivetrainSubsystem);
        register(shooterSubsystem);
        register(intakeSubsystem);

        drivetrainSubsystem.setDefaultCommand(teleOpDrive);

        //bind buttons to commands
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);
        //drive at a fixed heading
        gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whileHeld(rotateToHeading);
        //set flywheel to high speed
        gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).toggleWhenPressed(
                new StartEndCommand(()-> shooterSubsystem.run(HIGH_SPEED), shooterSubsystem::stop, shooterSubsystem)
        );
        //set flywheel to low speed
        gamepadEx1.getGamepadButton(GamepadKeys.Button.B).toggleWhenPressed(
                new StartEndCommand(()-> shooterSubsystem.run(LOW_SPEED), shooterSubsystem::stop, shooterSubsystem)
        );
        //reset imu heading
        gamepadEx1.getGamepadButton(GamepadKeys.Button.START).whenPressed(
                new InstantCommand(drivetrainSubsystem::resetHeading, drivetrainSubsystem)
        );
        //run intake while held
        new Trigger(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) >= .8).whileActiveContinuous(
                new StartEndCommand(intakeSubsystem::runIntake, intakeSubsystem::stopIntake, intakeSubsystem)
        );
        //move balls to shoot while held
        new Trigger(()-> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) >= .8).whileActiveContinuous(
                new StartEndCommand(intakeSubsystem::moveToShoot, intakeSubsystem::stopIntake, intakeSubsystem)
        );
        //recycle ball
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(recycleCommand);

        //do telemetry
        RunCommand telemetryCommand = new RunCommand( ()-> {
            telemetry.addData("y-axis", -gamepad1.left_stick_y);
            telemetry.addData("x-axis", -gamepad1.left_stick_x);
            telemetry.addData("x-right-axis", -gamepad1.right_stick_x);
            shooterSubsystem.telemetrize(telemetry);
            telemetry.update();
        });
        schedule(telemetryCommand);
    }
}
