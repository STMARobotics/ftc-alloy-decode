package org.firstinspires.ftc.teamcode.Commands;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.LOW_SPEED;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.RECYCLE_SPEED;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ShooterSubsystem;

public class RecycleCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private boolean ballInside = false;
    public RecycleCommand(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(shooterSubsystem);
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {
        shooterSubsystem.run(RECYCLE_SPEED);
        intakeSubsystem.moveToShoot();
    }

    @Override
    public void execute() {
        if (shooterSubsystem.ballDetected() && ballInside) {
            end(false);
        } else if (shooterSubsystem.ballDetected() && !ballInside) {
            ballInside = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stop();
        intakeSubsystem.stopIntake();
    }
}
