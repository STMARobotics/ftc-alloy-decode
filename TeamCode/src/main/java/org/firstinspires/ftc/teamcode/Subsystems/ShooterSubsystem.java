package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class ShooterSubsystem extends SubsystemBase {
    private final Motor flywheel;
    public ShooterSubsystem(HardwareMap hardwareMap) {
        flywheel = new Motor(hardwareMap, "shooterMotor");
        flywheel.setRunMode(Motor.RunMode.RawPower);
    }
    public void run() {
        flywheel.set(.45);
    }
    public void stop() {
        flywheel.set(0);
    }
}
