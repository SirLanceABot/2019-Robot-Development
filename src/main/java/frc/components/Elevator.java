/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.robot.SlabShuffleboard;
import frc.util.MotorConstants;
import frc.components.Elevator.Constants.ElevatorPosition;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

/**
 * Add your docs here.
 */
public class Elevator
{
    private WPI_TalonSRX masterElevatorMotor = new WPI_TalonSRX(Constants.MASTER_ELEVATOR_MOTOR_PORT);
    private WPI_VictorSPX slaveElevatorMotor = new WPI_VictorSPX(Constants.SLAVE_ELEVATOR_MOTOR_PORT);

    private double speedFactor = 1.0;

    private boolean isMoving = false;

    /**
     * Returns the pot value of the given position
     * 
     * <p> 0: Min Height
     * <p> 1: Max Height
     * <p> 2: Floor
     * <p> 3: Cargo Ship Cargo
     * <p> 4: Bottom Hatch
     * <p> 5: Center Hatch
     * <p> 6: Top Hatch
     * <p> 7: Bottom Cargo
     * <p> 8: Center Cargo
     * <p> 9: Top Cargo
     */
    private static int[] elevatorPositionPotValues = {111, 876, 111, 360, 111, 406, 744, 283, 636, 876};

   
    private static Elevator instance = new Elevator();

    public static Elevator getInstance()
	{
		return instance;
    }
    
    private Elevator()
	{
        System.out.println(this.getClass().getName() + ": Started Constructing");

        masterElevatorMotor.configFactoryDefault();
        slaveElevatorMotor.configFactoryDefault();

        masterElevatorMotor.setNeutralMode(NeutralMode.Brake);
        slaveElevatorMotor.setNeutralMode(NeutralMode.Brake);

        masterElevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog);

        // masterElevatorMotor.setSensorPhase(true);
        // masterElevatorMotor.setInverted(InvertType.InvertMotorOutput);
        // slaveElevatorMotor.setInverted(InvertType.InvertMotorOutput);

        masterElevatorMotor.configReverseSoftLimitThreshold(Constants.ElevatorPosition.kMinHeight.position);
        masterElevatorMotor.configForwardSoftLimitThreshold(Constants.ElevatorPosition.kMaxHeight.position);
        
        masterElevatorMotor.configForwardSoftLimitEnable(true);
        masterElevatorMotor.configReverseSoftLimitEnable(true);

        masterElevatorMotor.configPeakCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kRedLine, 0.3));
        masterElevatorMotor.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        masterElevatorMotor.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        masterElevatorMotor.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        masterElevatorMotor.enableCurrentLimit(true);
        
        slaveElevatorMotor.follow(masterElevatorMotor);

        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    /**
     * Raises the Elevator at a constant speed
     */
    public void raiseElevator()
    {
        masterElevatorMotor.set(speedFactor * Constants.ELEVATOR_SPEED);
    }

    /**
     * Raises the Elevator at a given speed
     */
    public void raiseElevator(double speed)
    {
        masterElevatorMotor.set(speedFactor * Math.abs(speed));
    }

    /**
     * Lowers the Elevator at a constant speed
     */
    public void lowerElevator()
    {
        masterElevatorMotor.set(speedFactor * -Constants.ELEVATOR_SPEED);
    }

    /**
     * lowers the Elevator at a given speed
     */
    public void lowerElevator(double speed)
    {
        masterElevatorMotor.set(speedFactor * -Math.abs(speed));
    }

    /**
     * Stops the Elevator motor
     */
    public void stopElevator()
    {
        masterElevatorMotor.set(0);
    }

    /**
     * Returns the value of the potentiometer
     * 
     * @return Value of Potentiometer
     */
    public int getPotValue()
	{
		return masterElevatorMotor.getSelectedSensorPosition(0);
    }

    /**
     * Returns a boolean that says whether or not the elevator is moving
     * 
     * @return whether the elevator is moving or not
     */
    public boolean isMoving()
    {
        return isMoving;
    }

    /**
     * sets if the elevator is moving
     */
    public void setIsMoving(boolean isMoving)
    {
        this.isMoving = isMoving;
    }

    /**
    * gets the position value of the elevator
     */
    public int getPositionValue(ElevatorPosition position)
    {
        return position.position;
    }

    /**
     * sets the type of robot being used (competition or practice)
     */
    public void setRobotType(SlabShuffleboard.RobotType robotType)
    {
        if(robotType == SlabShuffleboard.RobotType.kCompetition)
        {
            elevatorPositionPotValues = Constants.COMPETITION_ELEVATOR_POSITION_POT_VALUES;
        }
        else
        {
            elevatorPositionPotValues = Constants.PRACTICE_ELEVATOR_POSITION_POT_VALUES;
        }
    }

    public void setMotorSpeedFactor(SlabShuffleboard.MotorSpeed speedFactor)
    {
        this.speedFactor = speedFactor.value;
    }

    /**
     * Returns the pot value of the given position
     * 
     * @param position which position to return:
     * <p> 0: Min Height
     * <p> 1: Max Height
     * <p> 2: Floor
     * <p> 3: Cargo Ship Cargo
     * <p> 4: Bottom Hatch
     * <p> 5: Center Hatch
     * <p> 6: Top Hatch
     * <p> 7: Bottom Cargo
     * <p> 8: Center Cargo
     * <p> 9: Top Cargo
     * 
     * @return pot value of the selected position
     */
    public int getElevatorPositionPotValues(int position)
    {
        return elevatorPositionPotValues[position];
    }

    @Deprecated
    /**
     * converts inches to pot ticks (bad math by darryl)
     */
    private static int inchesToTicks(double inches)
    {
        return (int)((1.00/8.3) * inches * 100); 
    }

    public String getMasterLegElevatorMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f",
         masterElevatorMotor.get(), masterElevatorMotor.getSelectedSensorPosition(),
         masterElevatorMotor.getOutputCurrent(), masterElevatorMotor.getTemperature() * 9.0 / 5.0 + 32.0);
    }
    

    @Override
    public String toString()
    {
        return String.format("Pot Value: %d", getPotValue());
    }

    public static class Constants
    {
        public static enum ElevatorPosition
		{
            kMinHeight(elevatorPositionPotValues[0], "Min Height"),
            kMaxHeight(elevatorPositionPotValues[1], "Max Height"),
            kFloor(elevatorPositionPotValues[2], "Floor"),
            kCargoShipCargo(elevatorPositionPotValues[3], "Cargo Ship Cargo"),
            kThreshold(5, "Threshold"),

            kBottomHatch(elevatorPositionPotValues[4], "Bottom Hatch"),      // 1 ft 7 inches to center
            kCenterHatch(elevatorPositionPotValues[5], "Center Hatch"),      // 3 ft 11 inches to center
            kTopHatch(elevatorPositionPotValues[6], "Top Hatch"),         // 6 ft 3 inches to center

            kBottomCargo(elevatorPositionPotValues[7], "Bottom Cargo"),       // 2 ft 3.5 inches to center
            kCenterCargo(elevatorPositionPotValues[8], "Center Cargo"),       // 4 ft 7.5 inches to center
            kTopCargo(elevatorPositionPotValues[9], "Top Cargo"),          // 6 ft 11.5 inches to center
            kNone(-1, "None");

            
            private final int position;
            private final String name;

            private ElevatorPosition(int value, String name)
            {
                this.position = value;
                this.name = name;
            }

            @Override
            public String toString()
            {
                return name;
            }
        }

        // 0: Min Height
        // 1: Max Height
        // 2: Floor
        // 3: Cargo Ship Cargo
        // 4: Bottom Hatch
        // 5: Center Hatch
        // 6: Top Hatch
        // 7: Bottom Cargo
        // 8: Center Cargo
        // 9: Top Cargo
        public static final int[] COMPETITION_ELEVATOR_POSITION_POT_VALUES = {111, 876, 111, 360, 111, 406, 744, 283, 636, 876};
        public static final int[] PRACTICE_ELEVATOR_POSITION_POT_VALUES = {111, 876, 111, 360, 111, 406, 744, 283, 636, 876};
    
        public static final int INITIAL_HEIGHT_TO_MIN_HEIGHT = 111;//inchesToTicks(-10);
        public static final int INITIAL_HEIGHT_TO_MAX_HEIGHT = 876;//inchesToTicks(85);
        public static final int INITIAL_HEIGHT_TO_FLOOR = 111; //inchesToTicks(-5);
        public static final int INITIAL_HEIGHT_TO_CARGO_SHIP_CARGO = 360; // inchesToTicks(10);
        public static final int INITIAL_HEIGHT_TO_BOTTOM_HATCH = 111; //inchesToTicks(5);
        public static final int INITIAL_HEIGHT_TO_CENTER_HATCH = 406; //inchesToTicks(15);
        public static final int INITIAL_HEIGHT_TO_TOP_HATCH = 744; //inchesToTicks(25);
        public static final int INITIAL_HEIGHT_TO_BOTTOM_CARGO = 283; //inchesToTicks(10);
        public static final int INITIAL_HEIGHT_TO_CENTER_CARGO = 636; //inchesToTicks(20);
        public static final int INITIAL_HEIGHT_TO_TOP_CARGO = 876; //inchesToTicks(30);

        public static final int MASTER_ELEVATOR_MOTOR_PORT = 10;
        public static final int SLAVE_ELEVATOR_MOTOR_PORT = 11;

        public static final double ELEVATOR_SPEED = 0.5;
    }
}