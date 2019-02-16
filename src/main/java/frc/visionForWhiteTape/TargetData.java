/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.visionForWhiteTape;

import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 * Add your docs here.
 */
public class TargetData
{
    // NOTE: No modifier means private to the package

    // Target data that we need
    Point center;
    Size size;
    double angle;
    public double fixedAngle;

    // Quality control of target data
    int frameNumber;
    boolean isFreshData;
    boolean isTargetFound;

    public TargetData()
    {
        center = new Point();
        size = new Size();
        reset();
        frameNumber = -1;
    }

    public TargetData(TargetData targetData)
    {
        center = new Point();
        center = targetData.center;
        size = new Size();
        size = targetData.size;
        angle = targetData.angle;
        fixedAngle = targetData.fixedAngle;
    }

    // no modifier means private to the package
    void reset()
    {
        // DO NOT reset the frameNumber
        center.x = -1.0;
        center.y = -1.0;
        size.width = -1.0;
        size.height = -1.0;
        angle = -1.0;
        fixedAngle = -1.0;

        isFreshData = false;
        isTargetFound = false;
    }

    public Point getCenter()
    {
        return center;
    }

    public Size getSize()
    {
        return size;
    }

    public double getAngle()
    {
        return angle;
    }

    public double getFixedAngle()
    {
        return fixedAngle;
    }

    public boolean isFreshData()
    {
        return isFreshData;
    }

    public boolean isTargetFound()
    {
        return isTargetFound;
    }

    public synchronized String toString()
    {
        return String.format("[TargetInfo] center.x = %f, center.y = %f, size.width = %f, size.height = %f, angle = %f, fixedAngle = %f %s", 
        center.x, center.y, size.width, size.height, angle, fixedAngle, isFreshData ? "FRESH" : "stale");
    }
}
