package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.MecaBot;
import org.firstinspires.ftc.teamcode.robot.MecaBotMove;
import org.firstinspires.ftc.teamcode.skystone.FieldSkystone;

@Autonomous(name = "Test MecaBotMove", group="Test")
@Disabled
public class TestMecaBotMoveAuto extends LinearOpMode {

    MecaBot robot = new MecaBot();   // Use Omni-Directional drive system
    MecaBotMove nav = new MecaBotMove(this, robot);

    @Override
    public void runOpMode() {
        ElapsedTime opmodeRunTime = new ElapsedTime();
        telemetry.setAutoClear(false);

        FieldSkystone.initRobotStartX();
        telemetry.addData("Test MecaBotMove Time:", opmodeRunTime.seconds());
        telemetry.addData("robotStartX:", FieldSkystone.robotStartX);
        telemetry.addData("robotStartY:", FieldSkystone.robotStartY);
        telemetry.update();

        robot.init(this.hardwareMap);
        waitForStart();
        // start odometry reading calculations before any driving begins
        nav.startOdometry();

        //testDrawBox();
        testMoveYX();

        robot.grabStoneWithSidearm();
        sleep(2000 );
        robot.releaseStoneWithSidearm();

        //nav.moveLeftRight(-12);

        //nav.goPark(24,24, false,true);
        sleep(5000);
      //  nav.goPark(0,24, true,true);



        /*
        double inchMove=10;  //2 scan 17 apart; 3 scan
        nav.moveForwardBack(inchMove);
        sleep(2000);
        nav.moveForwardBack(inchMove);
        sleep(2000);

        nav.grabTheStone();
        sleep(2000 );
        nav.releaseTheStone();
*/


        while (opModeIsActive()){

        }

    }

    private void testMoveYX(){
        double dist=24;
        //draw a box by moving with robot start of heading in positive X direction
        //     move forward,right
        nav.moveYX(dist,dist,0,0,true);
        //     move back, left
        nav.moveYX(0,0,dist,dist,true);
        sleep(3000);
        //nav.moveYX(-10,0,0,0,false);
        //nav.moveYX(0,-10,0,0,false);

    }

    private void testDrawBox(){
        double aInch=24;
        nav.moveForwardBack(aInch);
        nav.moveLeftRight(aInch);
        aInch=-24;
        nav.moveForwardBack(aInch);
        nav.moveLeftRight(aInch);
    }
    public void testColorSensor(){
        ColorSensor cs=robot.rightColorSensor;
        telemetry.addData("Blue Reading=", cs.blue());
        telemetry.addData("Red Reading=", cs.red());
        telemetry.addData("Alpha Reading=", cs.alpha());
        telemetry.update();

    }
}
