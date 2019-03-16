package frc.visionForWhiteTape;

public class TargetDataController
{
    public TargetData targetData = new TargetData();

    // no modifier means private to the package
    synchronized void set(TargetData newTargetData)
    {
        targetData.center.x = newTargetData.center.x;
        targetData.center.y = newTargetData.center.y;
        targetData.size.width = newTargetData.size.width;
        targetData.size.height = newTargetData.size.height;
        targetData.angle = newTargetData.angle;
        targetData.fixedAngle = newTargetData.fixedAngle;
        targetData.isFreshData = true;
        targetData.isTargetFound = newTargetData.isTargetFound;
    }

    public synchronized TargetData get()
    {
        TargetData copyOfTargetData = new TargetData();
        copyOfTargetData.center.x = targetData.center.x;
        copyOfTargetData.center.y = targetData.center.y;
        copyOfTargetData.size.width = targetData.size.width;
        copyOfTargetData.size.height = targetData.size.height;
        copyOfTargetData.angle = targetData.angle;
        copyOfTargetData.fixedAngle = targetData.fixedAngle;
        targetData.isFreshData = false;
        copyOfTargetData.isFreshData = true;
        return copyOfTargetData;
    }
}