package org.firstinspires.ftc.teamcode.Subsystems;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class IntakeSubsystem extends SubsystemBase {
    private final Motor intake;
    private final Motor inside;
    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = new Motor(hardwareMap, "intakeMotor");
        inside = new Motor(hardwareMap, "insideMotor");
        intake.setInverted(true);
        inside.setInverted(true);
        intake.setRunMode(Motor.RunMode.RawPower);
        inside.setRunMode(Motor.RunMode.RawPower);
    }
    public void runIntake() {
        intake.set(1);
        inside.set(1);
    }
    public void stopIntake() {
        intake.set(0);
        inside.set(0);
    }
    public void moveToShoot() {
        intake.set(1);
        inside.set(-1);
    }
}
