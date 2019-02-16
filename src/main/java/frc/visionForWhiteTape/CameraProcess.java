package frc.visionForWhiteTape;

import frc.visionForWhiteTape.WhiteLineVision;
import org.opencv.core.*;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
//import edu.wpi.cscore.MjpegServer;
//import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import java.lang.Math;

public class CameraProcess implements Runnable
{
    public enum strafeDirection
    {
        kLeft, kRight, kNone;
    }

    public enum rotate
    {
        kLeft, kRight, kNone;
    }
   
    private static CameraProcess instance = new CameraProcess();

    private CameraProcess()
    {

    }
    
    public static CameraProcess getInstance()
    {
        return instance;
    }


    private TargetDataController targetInfo = new TargetDataController();
    // int heightOfMask = 76;
    //private CvSource outputStream;
    //private MjpegServer mjpegserver2;(

    public strafeDirection getStrafeDirection(TargetData targetDataParameter)
    {
        strafeDirection returnStrafeDirectionValue;
        if (targetDataParameter.getCenter().x < 79) 
        {
            returnStrafeDirectionValue = strafeDirection.kLeft;
        } 
        else if (targetDataParameter.getCenter().x > 81) 
        {
            returnStrafeDirectionValue = strafeDirection.kRight;
        } 
        else 
        {
            returnStrafeDirectionValue = strafeDirection.kNone;
        }
        return returnStrafeDirectionValue;

    }

    /**
     * return a value between -80 and 80 that is your strafe factor
     */
    public double getStrafeFactor(TargetData targetDataParameter)
    {
        double returnStrafeFactor = targetDataParameter.center.x - 80;

        return returnStrafeFactor;
    }

    public rotate getRotateDirection(TargetData targetDataParameter)
    {
        rotate returnRotateDirectionValue;
        if (targetDataParameter.fixedAngle < 89)
         {
            returnRotateDirectionValue = rotate.kLeft;
        } 
        else if (targetDataParameter.fixedAngle > 91) 
        {
            returnRotateDirectionValue = rotate.kRight;
        } 
        else
        {
            returnRotateDirectionValue = rotate.kNone;
        }

        return returnRotateDirectionValue;
    }

    /**
     * 
     * @param targetDataParameter
     * @return  returns the rotation factor between -90 and 90 
     */
    public double getRotateFactor(TargetData targetDataParameter)
    {
        double returnRotateFactor = targetDataParameter.fixedAngle - 90;

        return returnRotateFactor;
    }

    public TargetData getTargetData()
    {
        return targetInfo.get();
    }

    // public int getHeightOfMask()
    // {
    //     return heightOfMask;
    // }

    // public void setHeightOfMask(int newHeight)
    // {
    //     heightOfMask = newHeight;
    // }

    public void run()
    {
        visionPipeline();
    }

    private void visionPipeline()
    {
        WhiteLineVision gripPipelineWhiteTape = new WhiteLineVision();
        ArrayList<MatOfPoint> contoursFiltered;

        // Get a CvSink. This will capture Mats from the camera.
        CvSink inputStream = CameraServer.getInstance().getVideo();

        // Set up a CvSource. This will send images back to the Dashboard.
        CvSource outputStream = CameraServer.getInstance().putVideo("Robot Camera", 160, 120);
        // outputStream = new CvSource("Frame", VideoMode.PixelFormat.kMJPEG, 160, 120, 30);

        // mjpegserver2 = new MjpegServer("serve_Frame", 1183);
        // mjpegserver2.setSource(outputStream);

        // Mats are very memory expensive. Let's reuse this Mat.
        // all Java Mats have to be "pre-allocated" - they can't be in the loop because
        // they are never released and will cause out of memory
        Mat mat = new Mat();

        RotatedRect boundRect;

        TargetData bestTargetData = new TargetData();

        int frameNumber = 0;

        // This lets the robot stop this thread
        while (!Thread.interrupted())
        {
            frameNumber++;
            bestTargetData.reset();

            // Tell the CvSink to grab a frame from the camera and put it
            // in the source mat. If there is an error notify the output.
            if (inputStream.grabFrame(mat) == 0)
            {
                // Send the output the error.
                outputStream.notifyError(inputStream.getError());
                System.out.println("[CameraProcess] Frame Number: " + frameNumber + "ERROR with grabFrame()");
            }
            else
            {
                /*
                // Mask off the top of the screen
                Mat mask = new Mat(mat.rows(), mat.cols(), CvType.CV_8U, Scalar.all(0)); // Create mask with the size of the source image
                Imgproc.rectangle(mask, new Point(0.0,heightOfMask+1.0), new Point(mat.cols(),mat.rows()), new Scalar(255, 255, 255), -1);
                */
                gripPipelineWhiteTape.process(mat);
                

                contoursFiltered = new ArrayList<MatOfPoint>(gripPipelineWhiteTape.filterContoursOutput());
                gripPipelineWhiteTape.filterContoursOutput();
                // gripPipelineWhiteTape.maskOutput();

                // Check if there were any contours found
                if (contoursFiltered.isEmpty())
                {
                    System.out.println("[CameraProcess] Frame Number: " + frameNumber + " No Contours Found");
                }
                else
                {
                    // Draw the contours outputted by the pipeline
                    Imgproc.drawContours(mat, contoursFiltered, -1, new Scalar(255, 0, 0), 1);

                    // Loop thru all contours to find the best contour
                    // Each contour is reduced to a single point - the COG x, y of the contour
                    
                    int contourIndex = -1;
                    int bestContourIndex = -1;
                    Point endpoint = new Point();

                    for (MatOfPoint contour : contoursFiltered)
                    {
                        contourIndex++;

                        // Create a bounding upright rectangle for the contour's points
                        MatOfPoint2f  NewMtx = new MatOfPoint2f(contour.toArray() );
                        boundRect = Imgproc.minAreaRect(NewMtx);

                        // Draw a rotatedRect, using lines, that represents the minAreaRect
                        Point boxPts[] = new Point[4];
                        boundRect.points(boxPts);
                        for(int i = 0; i<4; i++)
                        {
                            Imgproc.line(mat, boxPts[i], boxPts[(i+1)%4], new Scalar(0, 255, 255));
                        }

                        // Determine if this is the best contour using center.y

                        if (bestTargetData.center.y < boundRect.center.y)
                        {
                            bestContourIndex = contourIndex;

                            // Find the center x, center y, width, height, and angle of the bounding rectangle
                            bestTargetData.center = boundRect.center;
                            bestTargetData.size = boundRect.size;
                            bestTargetData.angle = boundRect.angle;

                            // Create an accurate angle
                            if(Math.abs((int)(bestTargetData.size.width - bestTargetData.size.height)) <= 5)
                            {
                                bestTargetData.fixedAngle = 90.0;
                            }
                            else if(bestTargetData.size.width < bestTargetData.size.height)
                            {
                                bestTargetData.fixedAngle = bestTargetData.angle + 90;
                            }
                            else
                            {
                                bestTargetData.fixedAngle = bestTargetData.angle + 180;
                            }

                            
                            
                            //Update the target
                            bestTargetData.isFreshData = true;
                            bestTargetData.isTargetFound = true;
                        }
                    }
                Imgproc.drawContours(mat, contoursFiltered, bestContourIndex, new Scalar(0, 0, 255), 1);

                double angleInRadians = bestTargetData.fixedAngle * (Math.PI/180);
                endpoint.x = bestTargetData.center.x - ( (bestTargetData.size.height / 2) * Math.cos(angleInRadians) );
                endpoint.y = bestTargetData.center.y - ( (bestTargetData.size.height / 2) * Math.sin(angleInRadians) );
                Imgproc.line(mat, bestTargetData.center, endpoint,  new Scalar(255, 0, 255), 1);

                // // Print the points of the best contour
                // for(Point aPoint : contoursFiltered.get(bestContourIndex).toArray())System.out.print(" " + aPoint);
                // System.out.println("");

                // // Count the number of points in a contour
                // System.out.println("Number of points in best contour: " + contoursFiltered.get(bestContourIndex).toArray().length);

                } // end of processing all contours in this camera frame
                

                /*
                // Slope line
                if (bestTargetData.isFreshData == true && bestTargetData.isTargetFound == true)
                {
                    Point endpoint = new Point();
                    double angleInRadians = bestTargetData.angle * (Math.PI/180);
                    endpoint.x = bestTargetData.center.x + ( (bestTargetData.size.height / 2) * Math.cos(angleInRadians) );
                    endpoint.y = bestTargetData.center.y + ( (bestTargetData.size.height / 2) * Math.sin(angleInRadians) );
                    Imgproc.line(mat, bestTargetData.center, endpoint,  new Scalar(255, 0, 255), 1);
                    
                    bestTargetData.slope = (endpoint.y-bestTargetData.center.y)/(endpoint.x-bestTargetData.center.x);
                }
                */

                // Give the output stream a new image to display 

                outputStream.putFrame(mat);
                targetInfo.set(bestTargetData);
            }
            
        } // end of this camera frame

        // Interrupted thread so end the camera frame grab with one last targetInfo of
        // no data
        System.out.println("End of target frame grab");
        bestTargetData.reset();
        targetInfo.set(bestTargetData);
        mat.release();
        System.out.println("[CameraProcess] Camera Frame Grab Interrupted and Ended Thread");
    } // end of visionPipeline method

} // end of class camera_process