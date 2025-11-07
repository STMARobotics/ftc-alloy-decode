package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;

public class RotateToHeading extends CommandBase {
    private final DrivetrainSubsystem drivetrainSubsystem;
    // Creates a PIDController with gains kP, kI, kD
    private final PIDController pid = new PIDController(.01  , 0, 0);
    private final DoubleSupplier xSupplier;
    private final DoubleSupplier ySupplier;
    public RotateToHeading(DrivetrainSubsystem drivetrainSubsystem, double heading, DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        addRequirements(drivetrainSubsystem);
        pid.setSetPoint(heading);
        pid.setTolerance(2);
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    @Override
    public void initialize() {
        pid.reset();
    }

    @Override
    public void execute() {
        double pidOutput = -pid.calculate(
                drivetrainSubsystem.getHeading()  // the measured value
        );
        drivetrainSubsystem.drive(xSupplier.getAsDouble(), ySupplier.getAsDouble(), pidOutput);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.stop();
    }
}
