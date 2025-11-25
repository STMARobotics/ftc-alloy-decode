package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;

public class DrivetrainCommand extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;
    private final DoubleSupplier xSupplier;
    private final DoubleSupplier ySupplier;
    private final DoubleSupplier rSupplier;
    public DrivetrainCommand(DrivetrainSubsystem drivetrainSubsystem,
                             DoubleSupplier xSupplier,
                             DoubleSupplier ySupplier,
                             DoubleSupplier rSupplier
    ) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.rSupplier = rSupplier;
        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        drivetrainSubsystem.startTeleop();
    }

    @Override
    public void execute() {
        drivetrainSubsystem.drive(
                square(xSupplier.getAsDouble()),
                square(ySupplier.getAsDouble()),
                square(rSupplier.getAsDouble())
        );
    }

    @Override
    public boolean isFinished() {
        return false; // Dont stop pls its kinda important
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.stop();
    }

    private double square(double input) {
        return Math.copySign(input*input, input);
    }
}
