package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.FunctionalCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.DrivetrainCommand;
import org.firstinspires.ftc.teamcode.Commands.RecycleCommand;
import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

@TeleOp
public class TeleOpComandBased extends CommandOpMode {

    @Override
    public void initialize() {
        //create subystems
        DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem(hardwareMap);
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);
        RecycleCommand recycleCommand = new RecycleCommand(shooterSubsystem, intakeSubsystem);

        //create drive command
        DrivetrainCommand teleOpDrive = new DrivetrainCommand(
                drivetrainSubsystem,
                ()-> -gamepad1.left_stick_y,
                ()-> -gamepad1.left_stick_x,
                ()-> -gamepad1.right_stick_x
        );

        //register subystems
        register(drivetrainSubsystem);
        register(shooterSubsystem);
        register(intakeSubsystem);

        //run drivetrain
        drivetrainSubsystem.setDefaultCommand(teleOpDrive);

        //bind buttons to commands
        GamepadEx gamepadEx1 = new GamepadEx(gamepad1);
        //set flywheel to high speed
        gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).toggleWhenPressed(
                new StartEndCommand(()-> shooterSubsystem.run(HIGH_SPEED), shooterSubsystem::stop, shooterSubsystem)
        );
        //set flywheel to low speed
        gamepadEx1.getGamepadButton(GamepadKeys.Button.B).toggleWhenPressed(
                new StartEndCommand(()-> shooterSubsystem.run(LOW_SPEED), shooterSubsystem::stop, shooterSubsystem)
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
        //pedroPathing path testing
        gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenHeld(
                new StartEndCommand(drivetrainSubsystem::goToPose, drivetrainSubsystem::stop, drivetrainSubsystem)
        );

        //do telemetry
        RunCommand telemetryCommand = new RunCommand( ()-> {
            telemetry.addData("y-axis", -gamepad1.left_stick_y);
            telemetry.addData("x-axis", -gamepad1.left_stick_x);
            telemetry.addData("x-right-axis", -gamepad1.right_stick_x);
            shooterSubsystem.telemetrize(telemetry);
            drivetrainSubsystem.telemetrize(telemetry);
            telemetry.update();
        });
        schedule(telemetryCommand);
    }
}
