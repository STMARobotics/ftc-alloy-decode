package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ShooterSubsystem extends SubsystemBase {
    private final DcMotorEx flywheel;
    private final RevColorSensorV3 shootDistance;
    public ShooterSubsystem(HardwareMap hardwareMap) {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        flywheel.setPIDFCoefficients(
                DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(
                        575, 0.0, 0.0, 11.5
                )
        );
        shootDistance = hardwareMap.get(RevColorSensorV3.class, "shootDistance");
    }
    public void run(double rpm) {
        flywheel.setVelocity(rpm * 28.0 / 60.0);
    }
    public void stop() {
        flywheel.setVelocity(0);
    }
    public boolean ballDetected() {
        return shootDistance.getDistance(DistanceUnit.MM) < 20;
    }

    public void telemetrize(Telemetry telemetry) {
        telemetry.addData("flywheel speed", flywheel.getVelocity() * 60.0 / 28.0);
        telemetry.addData("ball detected", shootDistance.getDistance(DistanceUnit.MM) < 50);
    }
}
