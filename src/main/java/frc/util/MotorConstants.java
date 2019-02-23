package frc.util;

public class MotorConstants
{
    public static int getMotorStallCurrent(Constants.MotorType motorType, double multiplier)
    {
        double currentLimit = 0;
        
        switch(motorType)
        {
            case kBagMotor: currentLimit = multiplier * (Constants.BAG_MOTOR_STALL_CURRENT - Constants.BAG_MOTOR_FREE_CURRENT) + Constants.BAG_MOTOR_FREE_CURRENT;
                    break;
            case kCimMotor: currentLimit = multiplier * (Constants.CIM_MOTOR_STALL_CURRENT- Constants.CIM_MOTOR_STALL_CURRENT) + Constants.CIM_MOTOR_STALL_CURRENT;
                    break;
            case kPro775Motor: currentLimit = multiplier * (Constants.PRO_775_MOTOR_STALL_CURRENT- Constants.PRO_775_MOTOR_STALL_CURRENT) + Constants.PRO_775_MOTOR_STALL_CURRENT;
                    break;
            case k9015Motor: currentLimit = multiplier * (Constants.MOTOR_9015_STALL_CURRENT- Constants.MOTOR_9015_STALL_CURRENT) + Constants.MOTOR_9015_STALL_CURRENT;
                    break;
            case kNeoMotor: currentLimit = multiplier * (Constants.NEO_MOTOR_STALL_CURRENT- Constants.NEO_MOTOR_STALL_CURRENT) + Constants.NEO_MOTOR_STALL_CURRENT;
                break;
            case kRedLine: currentLimit = multiplier * (Constants.REDLINE_MOTOR_STALL_CURRENT- Constants.REDLINE_MOTOR_STALL_CURRENT) + Constants.REDLINE_MOTOR_STALL_CURRENT;
                    break;
            default: currentLimit = 0;
                    break;
        }
        return (int)currentLimit;
    }

    public static double getMotorFreeCurrent(Constants.MotorType motorType)
    {
        double freeCurrent = 0;

        switch(motorType)
        {
            case kBagMotor: freeCurrent = Constants.BAG_MOTOR_FREE_CURRENT;
                    break;
            case kCimMotor: freeCurrent = Constants.CIM_MOTOR_FREE_CURRENT;
                    break;
            case kPro775Motor: freeCurrent = Constants.PRO_775_MOTOR_FREE_CURRENT;
                    break;
            case k9015Motor: freeCurrent = Constants.MOTOR_9015_FREE_CURRENT;
                    break;
            case kNeoMotor: freeCurrent = Constants.NEO_MOTOR_FREE_CURRENT;
                break;
            case kRedLine: freeCurrent = Constants.REDLINE_MOTOR_FREE_CURRENT;
                    break;
            default: freeCurrent = 0;
                    break;
        }

        return freeCurrent;
    }
    

    public static class Constants
    {
        public static enum MotorType
        {
            kBagMotor, kCimMotor, kPro775Motor, k9015Motor, kNeoMotor, kRedLine;
        }
        
        public static final int BAG_MOTOR_STALL_CURRENT = 41;
        public static final int CIM_MOTOR_STALL_CURRENT = 133;
        public static final int PRO_775_MOTOR_STALL_CURRENT = 134;
        public static final int MOTOR_9015_STALL_CURRENT = 63;
        public static final int NEO_MOTOR_STALL_CURRENT = 150;
        public static final int REDLINE_MOTOR_STALL_CURRENT = 134;

        public static final double BAG_MOTOR_FREE_CURRENT = 1.8;
        public static final double CIM_MOTOR_FREE_CURRENT = 2.7;
        public static final double PRO_775_MOTOR_FREE_CURRENT = 0.7;
        public static final double MOTOR_9015_FREE_CURRENT = 1.2;
        public static final double NEO_MOTOR_FREE_CURRENT = 1.8;
        public static final double REDLINE_MOTOR_FREE_CURRENT = 0.7;

        public static final int PEAK_CURRENT_LIMIT = 20;        // In Amps
        public static final int PEAK_CURRENT_DURATION = 250;    // In milliseconds
        public static final int CONTINOUS_CURRENT_LIMIT = 20;   // In Amps
        public static final double OPEN_LOOP_RAMP = 0.05;       // In seconds
    }
}