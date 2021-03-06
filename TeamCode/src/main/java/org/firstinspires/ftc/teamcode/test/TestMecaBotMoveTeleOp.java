/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.MecaBot;
import org.firstinspires.ftc.teamcode.robot.MecaBotMove;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Test MecaBotMove functions", group="Test")
@Disabled
public class TestMecaBotMoveTeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    /* Declare OpMode members. */
    private MecaBot robot = new MecaBot();   // Use a Mecabot's hardware
    private MecaBotMove nav = new MecaBotMove(this, robot);

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        // Set all motors to RUN_USING_ENCODERS
        robot.resetDriveEncoder();
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // start odometry reading calculations before any driving begins
        nav.startOdometry();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.a && gamepad1.b) {
                robot.stopDriving();
                robot.resetDriveEncoder();
                robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            else if (gamepad1.b) {
                robot.releaseFoundation();
            }
            else if (gamepad1.a) {
                robot.grabFoundation();
            }
            else if (gamepad1.left_bumper) {
                nav.encoderTurn(55, true, 1.0);
            }
            else if (gamepad1.right_bumper) {
                nav.encoderTurn(55, false, 1.0);
            }
            else if (gamepad1.x) {
                nav.encoderRotate(26, true);
            }
            else if (gamepad1.y) {
                nav.encoderRotate(26, false);
            }
            else if (gamepad2.a) {
                nav.moveForwardBack(12);
                sleep(500);
                nav.moveRightLeft(12);
                sleep(500);
                nav.moveForwardBack(-12);
                sleep(500);
                nav.moveRightLeft(-12);
            }
            else {
                // forward press on joystick is negative, backward press (towards human) is positive
                // right press on joystick is positive value, left press is negative value
                // reverse sign of joystick values to match the expected sign in driveTank() method.
                robot.driveTank(-gamepad1.left_stick_y, -gamepad1.right_stick_x);
            }
/*
            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
*/
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "LF (%.2f), RF (%.2f)", robot.leftFrontDrive.getPower(), robot.rightFrontDrive.getPower());
            telemetry.addData("Motors", "LB (%.2f), RB (%.2f)", robot.leftBackDrive.getPower(), robot.rightBackDrive.getPower());
            telemetry.addData("Encoders", "LF (%5d), RF (%5d)", robot.leftFrontDrive.getCurrentPosition(), robot.rightFrontDrive.getCurrentPosition());
            telemetry.addData("Encoders", "LB (%5d), RB (%5d)", robot.leftBackDrive.getCurrentPosition(), robot.rightBackDrive.getCurrentPosition());
            telemetry.update();
        }
    }
}
