package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.odometry.OdometryGlobalPosition;
import org.firstinspires.ftc.teamcode.robot.MecaBot;
import org.firstinspires.ftc.teamcode.robot.MecaBotMove;

@TeleOp(name = "Test Odometry Mecabot", group = "Test")
public class TestMecabotOdometry extends LinearOpMode {

    private MecaBot robot;
    private MecaBotMove nav;
    private OdometryGlobalPosition globalPosition;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new MecaBot();
        robot.init(hardwareMap);

        nav = new MecaBotMove(this, robot);
        globalPosition = nav.getPosition();
        globalPosition.initGlobalPosition(14.0, 9.0, 90.0);

        telemetry.addData("Status", "Initialized");

        telemetry.addLine("Global Position ")
                .addData("X", "%3.2f", new Func<Double>() {
                    @Override public Double value() {
                        return globalPosition.getXinches();
                    }
                })
                .addData("Y", "%2.2f", new Func<Double>() {
                    @Override public Double value() {
                        return globalPosition.getYinches();
                    }
                })
                .addData("Angle", "%4.2f", new Func<Double>() {
                    @Override public Double value() {
                        return globalPosition.getOrientationDegrees();
                    }
                });
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // start odometry reading calculations before any driving begins
        nav.startOdometry();

        // run until the end of the match (driver presses STOP)
        while(opModeIsActive()){

            nav.goTowardsPosition(33.0, 32.5, 0.6);

//            telemetry.addData("Vertical left encoder", globalPosition.getVerticalLeftCount());
//            telemetry.addData("Vertical right encoder", globalPosition.getVerticalRightCount());
//            telemetry.addData("horizontal encoder", globalPosition.getHorizontalCount());

            telemetry.update();
        }

        //Stop the thread
        nav.stopOdometry();

    }
}
