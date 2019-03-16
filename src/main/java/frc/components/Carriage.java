/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.components;

import frc.robot.SlabShuffleboard;
import frc.util.MotorConstants;
import frc.components.Carriage.Constants.CarriagePosition;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class Carriage
{
    public static class Constants
    {
        public static enum CarriagePosition
		{
            kMinHeight(0),
            kMaxHeight(1),
            kFloor(2),
            kCargoShipCargo(3),

            kBottomHatch(4),      // 1 ft 7 inches to center
            kCenterHatch(5),      // 3 ft 11 inches to center
            kTopHatch(6),         // 6 ft 3 inches to center

            kBottomCargo(7),       // 2 ft 3.5 inches to center
            kCenterCargo(8),       // 4 ft 7.5 inches to center
            kTopCargo(9),          // 6 ft 11.5 inches to center
            kThreshold(10),
            kNone(11),

            kMovingDown,
            kMovingUp,
            kNotMoving;

            private int value;

            private CarriagePosition(int value)
            {
                this.value = value;
            }

            private CarriagePosition()
            {

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
        // 10: Threshold
        // 11: None
                                                                            // 0    1    2    3    4    5    6    7    8    9   10  11
        public static final int[] COMPETITION_CARRIAGE_POSITION_POT_VALUES = {100, 876, 100, 471, 120, 397, 600, 283, 608, 840, 5, -1};
        public static final int[] PRACTICE_CARRIAGE_POSITION_POT_VALUES =    {99,  820, 101, 432,  99, 360, 607, 245, 571, 820, 5, -1};
    
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

        public static final int MASTER_CARRIAGE_MOTOR_PORT = 10;
        public static final int SLAVE_CARRIAGE_MOTOR_PORT = 11;

        public static final double CARRIAGE_SPEED = 0.5;
    }

    private WPI_TalonSRX masterCarriageMotor = new WPI_TalonSRX(Constants.MASTER_CARRIAGE_MOTOR_PORT);
    private WPI_VictorSPX slaveCarriageMotor = new WPI_VictorSPX(Constants.SLAVE_CARRIAGE_MOTOR_PORT);

    private double speedFactor = 1.0;

    private boolean isMoving = false;

    private Constants.CarriagePosition currentState = Constants.CarriagePosition.kFloor;
    private Constants.CarriagePosition targetState = Constants.CarriagePosition.kFloor;

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
    private static int[] carriagePositionPotValues = Constants.COMPETITION_CARRIAGE_POSITION_POT_VALUES;

    private static Carriage instance = new Carriage();
    
    private Carriage()
	{
        System.out.println(this.getClass().getName() + ": Started Constructing");

        masterCarriageMotor.configFactoryDefault();
        slaveCarriageMotor.configFactoryDefault();

        masterCarriageMotor.setNeutralMode(NeutralMode.Brake);
        slaveCarriageMotor.setNeutralMode(NeutralMode.Brake);

        masterCarriageMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog);

        masterCarriageMotor.setSensorPhase(true);
        masterCarriageMotor.setInverted(InvertType.InvertMotorOutput);
        slaveCarriageMotor.setInverted(InvertType.InvertMotorOutput);

        masterCarriageMotor.configReverseSoftLimitThreshold(getCarriagePositionPotValues(Constants.CarriagePosition.kMinHeight));
        masterCarriageMotor.configForwardSoftLimitThreshold(getCarriagePositionPotValues(Constants.CarriagePosition.kMaxHeight));
        
        masterCarriageMotor.configForwardSoftLimitEnable(true);
        masterCarriageMotor.configReverseSoftLimitEnable(true);

        masterCarriageMotor.configPeakCurrentLimit(MotorConstants.getMotorStallCurrent(MotorConstants.Constants.MotorType.kRedLine, 0.3));
        masterCarriageMotor.configPeakCurrentDuration(MotorConstants.Constants.PEAK_CURRENT_DURATION);
        masterCarriageMotor.configContinuousCurrentLimit(MotorConstants.Constants.CONTINOUS_CURRENT_LIMIT);
        masterCarriageMotor.configOpenloopRamp(MotorConstants.Constants.OPEN_LOOP_RAMP);
        masterCarriageMotor.enableCurrentLimit(true);
        
        slaveCarriageMotor.follow(masterCarriageMotor);

        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    public static Carriage getInstance()
	{
		return instance;
    }

    /**
     * Raises the Elevator at a constant speed
     */
    public void raiseCarriage()
    {
        raiseCarriage(Constants.CARRIAGE_SPEED);
    }

    /**
     * Raises the Elevator at a given speed
     */
    public void raiseCarriage(double speed)
    {
        masterCarriageMotor.set(speedFactor * Math.abs(speed));
        setIsMoving(true);
    }

    public void holdCarriage()
    {
        raiseCarriage(0.05);
    }

    /**
     * Lowers the Elevator at a constant speed
     */
    public void lowerCarriage()
    {
        lowerCarriage(-Constants.CARRIAGE_SPEED);
    }

    /**
     * lowers the Elevator at a given speed
     */
    public void lowerCarriage(double speed)
    {
        masterCarriageMotor.set(speedFactor * -Math.abs(speed));
        setIsMoving(false);
    }

    /**
     * Stops the Elevator motor
     */
    public void stopCarriage()
    {
        masterCarriageMotor.set(0);
    }

    /**
     * Returns the value of the potentiometer
     * 
     * @return Value of Potentiometer
     */
    public int getPotValue()
	{
		return masterCarriageMotor.getSelectedSensorPosition();
    }

    /**
     * Returns a boolean that says whether or not the carriage is moving
     * 
     * @return whether the carriage is moving or not
     */
    public boolean isMoving()
    {
        return isMoving;
    }

    /**
     * sets if the carriage is moving
     */
    public void setIsMoving(boolean isMoving)
    {
        this.isMoving = isMoving;
    }


    /**
     * sets the type of robot being used (competition or practice)
     */
    public void setRobotType(SlabShuffleboard.RobotType robotType)
    {
        if(robotType == SlabShuffleboard.RobotType.kCompetition)
        {
            carriagePositionPotValues = Constants.COMPETITION_CARRIAGE_POSITION_POT_VALUES;
        }
        else
        {
            carriagePositionPotValues = Constants.PRACTICE_CARRIAGE_POSITION_POT_VALUES;
        }

        System.out.println(this.getClass().getName() + ": carriagePositionPotValues = " + Arrays.toString(carriagePositionPotValues));

        masterCarriageMotor.configReverseSoftLimitThreshold(getCarriagePositionPotValues(Constants.CarriagePosition.kMinHeight));
        masterCarriageMotor.configForwardSoftLimitThreshold(getCarriagePositionPotValues(Constants.CarriagePosition.kMaxHeight));
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
    public int getCarriagePositionPotValues(CarriagePosition position)
    {
        return carriagePositionPotValues[position.value];
    }

    public void setState(CarriagePosition position)
    {
        targetState = position;
    }

    public void carriageControl()
    {
        int currentPotValue = getPotValue();
        switch(currentState)
        {
            case kFloor:
                switch(targetState)
                {
                    case kFloor:
                        currentState = Constants.CarriagePosition.kFloor;
                        break;
                    case kCargoShipCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kBottomHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kCenterHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kCenterCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                    
                }
                break;
            case kCargoShipCargo:
                switch(targetState)
                {
                    case kFloor:                   
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:
                        currentState = CarriagePosition.kCargoShipCargo;
                        break;
                    case kBottomHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopHatch:                        
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kBottomHatch:
                switch(targetState)
                {
                    case kFloor:
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kBottomHatch:
                        currentState = CarriagePosition.kBottomHatch;
                        break;
                    case kCenterHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kCenterCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kCenterHatch:
                switch(targetState)
                {
                    case kFloor:
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kBottomHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:
                        currentState = CarriagePosition.kCenterHatch;
                        break;
                    case kTopHatch:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kTopHatch:
                switch(targetState)
                {
                    case kFloor:
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kBottomHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopHatch:
                        currentState = CarriagePosition.kTopHatch;
                        break; 
                    case kBottomCargo:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterCargo:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kBottomCargo:
                switch(targetState)
                {
                    case kFloor:
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kBottomHatch:
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopHatch:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:
                        currentState = CarriagePosition.kBottomCargo;
                        break;
                    case kCenterCargo:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kTopCargo:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kCenterCargo:
                switch(targetState)
                {
                    case kFloor:                    
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kBottomHatch:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopHatch:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break; 
                    case kBottomCargo:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterCargo:                    
                        currentState = CarriagePosition.kCenterCargo;
                        break;
                    case kTopCargo:                    
                        raiseCarriage();
                        currentState = CarriagePosition.kMovingUp;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kTopCargo:
                switch(targetState)
                {
                    case kFloor:                    
                        lowerCarriage();
                        currentState = Constants.CarriagePosition.kMovingDown;
                        break;
                    case kCargoShipCargo:                      
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kBottomHatch:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterHatch:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopHatch:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break; 
                    case kBottomCargo:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kCenterCargo:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kMovingDown;
                        break;
                    case kTopCargo:                    
                        lowerCarriage();
                        currentState = CarriagePosition.kTopCargo;
                        break;
                    case kMovingDown:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kMovingUp:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                        break;
                    case kNotMoving:
                        holdCarriage();
                        currentState = CarriagePosition.kNotMoving;
                }
                break;
            case kMovingDown:
                if (currentPotValue > (targetState.value + 5))
                {
                    lowerCarriage();
                    targetState = CarriagePosition.kMovingUp;
                }
                else if (currentPotValue < (targetState.value - 5))
                {
                    raiseCarriage();
                    targetState = CarriagePosition.kMovingDown;
                }
                else
                {
                    holdCarriage();
                    currentState = targetState;
                }
                break;
            case kMovingUp:
                if (currentPotValue > (targetState.value + 5))
                {
                    targetState = CarriagePosition.kMovingUp;
                }
                else if (currentPotValue < (targetState.value - 5))
                {
                    targetState = CarriagePosition.kMovingDown;
                }
                else
                {
                    holdCarriage();
                    currentState = targetState;
                }
                break;
            case kNotMoving:
                holdCarriage();
                currentState = CarriagePosition.kNotMoving;
                break;
        }

    }

    public String getMasterLegCarriageMotorData()
    {
        return String.format("%6.3f,  %6d,  %6.3f,  %5.1f",
         masterCarriageMotor.get(), masterCarriageMotor.getSelectedSensorPosition(),
         masterCarriageMotor.getOutputCurrent(), masterCarriageMotor.getTemperature() * 9.0 / 5.0 + 32.0);
    }
    

    @Override
    public String toString()
    {
        return String.format("Pot Value: %d", getPotValue());
    }
}