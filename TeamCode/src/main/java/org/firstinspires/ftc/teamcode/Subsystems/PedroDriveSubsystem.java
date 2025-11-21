package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PedroDriveSubsystem extends SubsystemBase {
    private final Follower follower;
    private Pose currentPose = new Pose();
    public PedroDriveSubsystem(HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);
    }
    public void drive(double x, double y, double r) {
        follower.setTeleOpDrive(
                x,
                y,
                r,
                false
        );
    }
    public void stop() {
        follower.setTeleOpDrive(0,0,0,true);
    }
    public void startTeleop() {
        follower.startTeleopDrive();
        follower.setMaxPower(1);
    }
    public void telemetrize(Telemetry telemetry) {
        // Log the position to the telemetry
        telemetry.addData("X coordinate", currentPose.getX());
        telemetry.addData("Y coordinate", currentPose.getY());
        telemetry.addData("Heading angle (radians)", currentPose.getHeading());
    }
}
