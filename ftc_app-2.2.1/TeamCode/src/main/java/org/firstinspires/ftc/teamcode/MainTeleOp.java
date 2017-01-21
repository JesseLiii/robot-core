/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all iterative OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Single Motor TestOp", group = "Iterative Opmode")
public class MainTeleOp extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    // Servo constants (you can't change them once you set them)
    static final double MAXPOS = 1.0;
    static final double MINPOS = 0.0;
    static final double INCREMENT = 0.01; // The value by which to increase/decrease servo pos
    static final int TICKINC=10; // The time in msec to wait before incrementing again
    double lowleftposition = (MAXPOS-MINPOS)/2;
    double upleftposition = (MAXPOS-MINPOS)/2;
    long nextTick = System.currentTimeMillis(); // Current position

    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor armMotor1,armMotor2;
    Servo lowerLeftArmServo, upperLeftArmServo, lowerRightArmServo, upperRightArmServo;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.addData("LL Servo: ",lowerLeftArmServo.getPortNumber());
        telemetry.addData("UL Servo: ",upperLeftArmServo.getPortNumber());

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftMotor = hardwareMap.dcMotor.get("left motor");
        rightMotor = hardwareMap.dcMotor.get("right motor");

        armMotor1 = hardwareMap.dcMotor.get("armMotor1");
        armMotor2 = hardwareMap.dcMotor.get("armMotor2");

        lowerLeftArmServo = hardwareMap.servo.get("lowerLeftArmServo");
        upperLeftArmServo = hardwareMap.servo.get("upperLeftArmServo");
        lowerRightArmServo = hardwareMap.servo.get("lowerRightArmServo");
        upperRightArmServo = hardwareMap.servo.get("upperRightArmServo");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        // eg: Set the drive motor directions:
        // Reverse the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // If you put the cursor on a comment that says "region" and press command-minus, you can collapse the code

        //region Telemetry
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("LL Servo Pos: ",lowerLeftArmServo.getPosition());
        telemetry.addData("UL Servo Pos: ",upperLeftArmServo.getPosition());
        telemetry.addData("LR Servo Pos: ",lowerRightArmServo.getPosition());
        telemetry.addData("UR Servo Pos: ",upperRightArmServo.getPosition());
        //endregion

        //region WHEELS
        float left1 = gamepad1.left_stick_y;
        left1 = Range.clip(left1, (float) -1.0, (float) 1.0);
        left1 = (float) scaleInput(left1);
        leftMotor.setPower(left1);

        float right1 = gamepad1.right_stick_y;
        right1 = Range.clip(right1, (float) -1.0, (float) 1.0);
        right1 = (float) scaleInput(right1);
        rightMotor.setPower(right1);
        //endregion

        //region Lifter motors
        // TODO: check which way motor spins & customize
        if (gamepad1.left_bumper) {
            armMotor1.setPower(1.0);
            armMotor2.setPower(1.0);

        }
        else if (gamepad1.right_bumper){
            armMotor1.setPower(-1.0);
            armMotor2.setPower(-1.0);
        }
        else {
            armMotor1.setPower(0.0);
            armMotor2.setPower(0.0);
        }
        //endregion

        //region Arm servos
        if (gamepad1.dpad_down) {
            if (lowleftposition>0.0 && System.currentTimeMillis()>=nextTick) {
                lowleftposition -= INCREMENT;
                nextTick=System.currentTimeMillis()+TICKINC;
            }
        }
        if (gamepad1.dpad_up) {
            if (lowleftposition<1.0 && System.currentTimeMillis()>=nextTick) {
                lowleftposition += INCREMENT;
                nextTick=System.currentTimeMillis()+TICKINC;
            }
        }
        if (gamepad1.dpad_left) {
            if (upleftposition>0.0 && System.currentTimeMillis()>=nextTick) {
                upleftposition -= INCREMENT;
                nextTick=System.currentTimeMillis()+TICKINC;
            }
        }
        if (gamepad1.dpad_right) {
            if (upleftposition<1.0 && System.currentTimeMillis()>=nextTick) {
                upleftposition += INCREMENT;
                nextTick=System.currentTimeMillis()+TICKINC;
            }
        }
        lowerLeftArmServo.setPosition(lowleftposition);
        lowerRightArmServo.setPosition(1.0-lowleftposition);
        upperLeftArmServo.setPosition(upleftposition);
        upperRightArmServo.setPosition(1.0-upleftposition);
        //endregion
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
