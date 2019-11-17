package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class MecaBotMove {

    private final static double WHEEL_DIA = 75;  //in millimeter
    private final static int    MOTOR_TICK_COUNT = 1120;
    private static final float  mmPerInch        = 25.4f;
    private LinearOpMode  myOpMode;       // Access to the OpMode object
    private MecaBot       robot;        // Access to the Robot hardware
    private double speed=0.5;
    private final double LOWSPEED = 0.1;
    private final double HIGHSPEED = 0.8;

    /* Constructor */
    public MecaBotMove(LinearOpMode opMode, MecaBot aRobot) {
        // Save reference to OpMode and Hardware map
        myOpMode = opMode;
        robot = aRobot;
    }

    public double getWheelMoveInches(){
        int tickMoved=robot.rightBackDrive.getCurrentPosition();
        double mmMoved= tickMoved/MOTOR_TICK_COUNT * WHEEL_DIA;
        double inchMoved=mmMoved/mmPerInch;
        return inchMoved;
    }

    public void moveForward(double inches) {
        double mm = inches * mmPerInch;
        moveForwardBack(mm, true);
    }

    public void moveBackward(double inches) {
        double mm = inches * mmPerInch;
        myOpMode.telemetry.addData(">>moveBackward:",mm);
        myOpMode.telemetry.update();
        moveForwardBack(mm, false);
        myOpMode.telemetry.addData("<<moveBackward:",mm);
        myOpMode.telemetry.update();
    }

    private void moveForwardBack(double mm, boolean goForward) {
        moveDistance( mm, goForward, false);
    }


    public void moveRight(double inches) {
        double mm = inches * mmPerInch;
        moveLeftRight(mm, true);
    }

    public void moveLeft(double inches) {
        double mm = inches * mmPerInch;
        moveLeftRight(mm, false);
    }

    // Move robot left or right
    public void moveLeftRight(double mm, boolean goRight) {
        moveDistance(mm, goRight, true);

    }

    //DEBUG: move all wheels with equal power
    public void  testMove(){
        myOpMode.telemetry.addData(">>testMove:","none");
        robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setWheelSpeed(LOWSPEED);
        myOpMode.sleep(4000);
        robot.stopDriving();
        myOpMode.telemetry.addData("<<testMove:","none");
        myOpMode.telemetry.update();
    }



//2nd parameter: forward true, back false
    private void moveDistance(double mm, boolean goForwardOrRight, boolean mecanumSideways) {
        mm=Math.PI * WHEEL_DIA;  //  DEBUG: test one wheel turn

        myOpMode.telemetry.addData(">>moveDistance:",mm);
        myOpMode.telemetry.update();
        robot.resetDriveEncoder();

        //cw: convert millimeter to tick counts
        double circumference = Math.PI * WHEEL_DIA;
        double numRotation = mm/circumference;
        int driverEncoderTarget = (int) (MOTOR_TICK_COUNT * numRotation);

        myOpMode.telemetry.addData("Rotation needed:", "numRotation");

        // Vishesh todo Multiply the distance we require by a determined constant to tell the motors how far to turn/set our target position

        // flip direction for reverse (this also applies for Left sideways when mecanumSideways is true)
        if (!goForwardOrRight) {
            driverEncoderTarget = -driverEncoderTarget;  // reverse the encoder target direction
            myOpMode.telemetry.addData("Drive Reverse:", "driverEncoderTarget");
        }

        // default is drive straight all wheels drive same direction (forward or backward depending on sign)
        int leftFront = driverEncoderTarget;
        int leftBack = driverEncoderTarget;
        int rightFront = driverEncoderTarget;
        int rightBack = driverEncoderTarget;

        // for mecanum sideways movement, move Right when goForward is true
        // Right wheels move inside, Left wheels move outside
        //TODO:????? Does this move right????
        if (mecanumSideways) {
            leftFront = -driverEncoderTarget;
            leftBack = driverEncoderTarget;
            rightFront = driverEncoderTarget;
            rightBack = -driverEncoderTarget;
        }
        // same code above works for mecanum move Left also. False value of goForwardOrRight already flipped the sign above


/*
        // start moving to target position
        robot.leftFrontDrive.setTargetPosition(leftFront);
        robot.leftBackDrive.setTargetPosition(leftBack);
        robot.rightFrontDrive.setTargetPosition(rightFront);
        robot.rightBackDrive.setTargetPosition(rightBack);

        // Set the motors to run to the necessary target position
        robot.leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
*/
        robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBackDrive.setTargetPosition(rightBack);
        robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Loop until both motors are no longer busy.
        myOpMode.telemetry.addData("Start Driving distance mm =", mm);
        myOpMode.telemetry.update();
        myOpMode.sleep(2000);

        // Set the power of the motors to whatever speed is needed
        //robot.driveStraight(LOWSPEED);
        setWheelSpeed(LOWSPEED);

        while (robot.rightBackDrive.isBusy()) {
            myOpMode.telemetry.addData("rightBackDrive position = ", robot.rightBackDrive.getCurrentPosition());
            myOpMode.telemetry.update();
        }
        /*
        while (robot.leftFrontDrive.isBusy() || robot.rightFrontDrive.isBusy() || robot.leftBackDrive.isBusy() || robot.rightBackDrive.isBusy()) {

            // no need to do any checks
            // the documentation says that motors stop automatically in RUN_TO_POSITION mode and isBusy() will return false after that

            double distanceToTarget = robot.leftBackDrive.getCurrentPosition() - robot.leftBackDrive.getTargetPosition();
            if (robot.leftBackDrive.getCurrentPosition() > robot.leftBackDrive.getTargetPosition() - 10 && robot.leftBackDrive.getCurrentPosition() < robot.leftBackDrive.getTargetPosition() + 10) {
                if (robot.rightBackDrive.getCurrentPosition() > robot.rightBackDrive.getTargetPosition() - 10 && robot.rightBackDrive.getCurrentPosition() < robot.rightBackDrive.getCurrentPosition() + 10) {
                    break;
                }
            }

            myOpMode.telemetry.addData("rightBackDrive position = ", robot.rightBackDrive.getCurrentPosition());
            myOpMode.telemetry.update();
            myOpMode.sleep(50);
        }
*/
        // Stop powering the motors - robot has moved to intended position
        robot.stopDriving();

        myOpMode.telemetry.addData("Stopped at Target, rightBackDrive position = ", robot.rightBackDrive.getCurrentPosition());
        myOpMode.telemetry.update();
    }


    private void setWheelSpeed(double speed) {

        robot.leftFrontDrive.setPower(speed);
        robot.leftBackDrive.setPower(speed);
        robot.rightFrontDrive.setPower(speed);
        robot.rightBackDrive.setPower(speed);
    }

    private void setDriveMode() {
        robot.leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    // Rotate
    public void turn(int degrees, boolean counterClockwise) {
    }

    // Claw and Damper movements
    public void grabTheStone(){
        robot.grabStoneWithSidearm();
    }

    public void releaseTheStone(){
        robot.releaseStoneWithSidearm();
    }

    public void grabFoundation(){
        robot.grabFoundation();
    }

    public void releaseFoundation(){
        robot.releaseFoundation();
    }
}