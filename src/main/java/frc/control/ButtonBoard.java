/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.control;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class ButtonBoard extends Joystick
{
    private static ButtonBoard instance = new ButtonBoard(Constants.PORT);

    public static ButtonBoard getInstance()
	{
		return instance;
	}

    private ButtonBoard(int port)
    {
        super(port);
        System.out.println(this.getClass().getName() + ": Started Constructing");
        System.out.println(this.getClass().getName() + ": Finished Constructing");
    }

    @Override
    public double getRawAxis(int axis)
    {
        if(axis == 1)
        {
            return -super.getRawAxis(axis);
        }
        else
        {
            return super.getRawAxis(axis);
        }
    }

    public static class Constants
    {        
        public static final int FLOOR_BUTTON = 1;
        public static final int CARGO_SHIP_CARGO_BUTTON = 2;

        public static final int BOTTOM_HATCH_BUTTON = 8;
        public static final int CENTER_HATCH_BUTTON = 9;
        public static final int TOP_HATCH_BUTTON = 10;

        public static final int BOTTOM_CARGO_BUTTON = 3;
        public static final int CENTER_CARGO_BUTTON = 4;
        public static final int TOP_CARGO_BUTTON = 5;
        
        public static final int ELEVATOR_BUTTON = 12;
        public static final int ARM_BUTTON = 11;

        public static final int X_AXIS = 0;
        public static final int Y_AXIS = 1;

        public static final int PORT = 1;
    }
}
