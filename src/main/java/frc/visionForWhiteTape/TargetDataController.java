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

        // System.out.println(targetData.center.x + " " + targetData.center.y);
    }

    public synchronized TargetData get()
    {
        // TargetData copyOfTargetData = new TargetData(targetData);
        // copyOfTargetData = targetData;
        TargetData copyOfTargetData = new TargetData();
        copyOfTargetData.center.x = targetData.center.x;
        copyOfTargetData.center.y = targetData.center.y;
        copyOfTargetData.size.width = targetData.size.width;
        copyOfTargetData.size.height = targetData.size.height;
        copyOfTargetData.angle = targetData.angle;
        copyOfTargetData.fixedAngle = targetData.fixedAngle;
        targetData.isFreshData = false;
        copyOfTargetData.isFreshData = true;
        // System.out.println(this.targetData.center.x + " " + this.targetData.center.y);

        return copyOfTargetData;
    }

    /*
    public synchronized String toString()
    {
        return String.format("[TargetInfo] center.x = %f, center.y = %f, size.width = %f, size.height = %f, angle = %f, fixedAngle = %f %s]", 
        center.x, center.y, size.width, size.height, angle, fixedAngle, isFreshData ? "FRESH" : "stale");
    }
    */
}
