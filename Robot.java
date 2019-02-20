/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
//import edu.wpi.cscore.UsbCamera;
//import edu.wpi.first.cameraserver.*;

public class Robot extends TimedRobot {

  // Link robot to code.
  AHRS gyro;
  Spark left = new Spark(0);
  Spark right = new Spark(1);
  DifferentialDrive drive = new DifferentialDrive(left, right);
  PWMVictorSPX lift = new PWMVictorSPX(2);
  PWMVictorSPX intake = new PWMVictorSPX(3);
  Servo lance = new Servo(4);
  Servo tulip = new Servo(5);

  // Link controller to code.
  Joystick controller = new Joystick(0);
  JoystickButton A = new JoystickButton(controller, 6);
  JoystickButton B = new JoystickButton(controller, 7);
  JoystickButton X = new JoystickButton(controller, 8);
  JoystickButton Y = new JoystickButton(controller, 9);
  JoystickButton LBumber = new JoystickButton(controller, 10);
  JoystickButton RBumper = new JoystickButton(controller, 11);

  // Control variables.
  boolean lowerLance = true;

  @Override
  public void robotInit() {
    // Camera
    //CameraServer.getInstance().startAutomaticCapture();

    // Gyroscope.
    gyro = new AHRS(SPI.Port.kMXP);

    // Victor SPX lift.
    lift.enableDeadbandElimination(true);
    lift.set(1.0);

    // Victor SPX intake.
    intake.enableDeadbandElimination(true);
    intake.set(1.0);
  }
  
  @Override
  public void autonomousInit() {
    gyro.reset();
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopPeriodic() {
    // Drive robot. This is the leap of faith.
    drive.arcadeDrive(-controller.getY(), controller.getX());

    // Toggle the lance's position.
    if (Y.get()) {
      lowerLance = !lowerLance;
    }

    // Lower the lance.
    if ((lowerLance) && (lance.getRaw() != 0)) {
      lance.setRaw(0);
    }
    else if ((!lowerLance) && (lance.getRaw() != 255)) {
      lance.setRaw(255);
    }

    // Control lift.
    if ((controller.getRawAxis(5) < -0.05) || (controller.getRawAxis(5) > 0.05)) {
      lift.set(controller.getRawAxis(5));
    }
    else {
      lift.set(0.0);
    }

    // Send robot angle to dashboard.
    SmartDashboard.putNumber("Angle", Math.round((float)gyro.getAngle()));
  }
}