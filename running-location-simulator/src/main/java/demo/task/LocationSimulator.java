package demo.task;



import demo.model.*;
import demo.service.PositionService;
import demo.support.NavUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


//thread simulator: implement Runnable interface
public class LocationSimulator implements Runnable{

    @Getter
    @Setter
    private long id;

//    immutable for handling multi-threading collision(concurrency)
//    set true = cancel
    private AtomicBoolean cancel = new AtomicBoolean();
    private double speedInMps;
    private boolean shouldMove;
    private boolean exportPositionToMessaging = true;
    private Integer reportInterval = 500;

    @Getter
    @Setter
    private PositionInfo positionInfo = null;

    @Setter
    private List<Leg> legs;

    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String runningId;

    @Setter
    private Point startPoint;

    private Date executionStartTime;
    private MedicalInfo medicalInfo;

    @Setter
    //multi-thread can't just inject directly
    private PositionService positionService;

//    constructor
//    param: request from frontend
    public LocationSimulator(GpsSimulatorRequest gpsSimulatorRequest) {
        this.shouldMove = gpsSimulatorRequest.isMove();
        this.exportPositionToMessaging = gpsSimulatorRequest.isExportPositionToMessaging();
        this.reportInterval = gpsSimulatorRequest.getReportInterval();
        this.runningId = gpsSimulatorRequest.getRunningId();
        this.runnerStatus = gpsSimulatorRequest.getRunnerStatus();
        this.medicalInfo = gpsSimulatorRequest.getMedicalInfo();
        //this.setSpeed(gpsSimulatorRequest.getSpeed());
    }


    //functions

    public void setSpeed(double speed){ this.speedInMps = speed; }

    public double getSpeed(){ return this.speedInMps; }

    void destroy(){ positionInfo = null; }

    //sleep
    private void sleep(long startTime) throws InterruptedException{
        long endTime = new Date().getTime();
        long elapsedTime = endTime - startTime;
        long sleepTime = reportInterval - elapsedTime > 0 ? reportInterval - elapsedTime : 0;
        Thread.sleep(sleepTime);
    }

    //set flag for killing thread
    public synchronized void cancel() { this.cancel.set(true);  }





    //main func for move
    //set new position of running based on current pos and speed
    private void moveRunningLocation() {
        double distance = speedInMps * reportInterval / 1000.0;
        double distanceFromStart = positionInfo.getDistanceFromStart() + distance;
        double excess = 0.0;

        //check if excess current leg
        for (int i = positionInfo.getLeg().getId(); i<legs.size(); i++) {
            Leg currentLeg = legs.get(i);
            excess = distanceFromStart > currentLeg.getLength() ? distanceFromStart-currentLeg.getLength() : 0.0;

            //rough compare
            if (Double.doubleToRawLongBits(excess) == 0){
                //same leg, update position only
                positionInfo.setDistanceFromStart(distanceFromStart);
                positionInfo.setLeg(currentLeg);
                //calculate and set new pos
                Point newPosition = NavUtils.getPosition(currentLeg.getStartPosition(),distanceFromStart,currentLeg.getHeading());
                positionInfo.setPosition(newPosition);
            }
            distanceFromStart = excess;
        }
        setStartPosition();
    }

    //reset vars
    public void setStartPosition() {
        positionInfo = new PositionInfo();
        positionInfo.setRunningId(this.runningId);
        Leg leg = legs.get(0);
        positionInfo.setLeg(leg);
        positionInfo.setPosition(leg.getStartPosition());
        positionInfo.setDistanceFromStart(0.0);
    }



    @Override
    public void run() {
        try{
            //thread start time
            executionStartTime = new Date();
            if (cancel.get()){
                destroy();
                return;
            }

            while(!Thread.interrupted()){
                long startTime = new Date().getTime();
                if (positionInfo != null){
                    //if not not arrived
                    if (shouldMove) {
                        moveRunningLocation();
                        positionInfo.setSpeed(speedInMps);
                    }
                    else{ positionInfo.setSpeed(0.0); }
                    //update runnerstatus and medicalinfo
                    positionInfo.setRunnerStatus(this.runnerStatus);
                    final MedicalInfo medicalInfoToUse;
                    switch (this.runnerStatus){
                        case SUPPLY_NOW:
                        case SUPPLY_SOON:
                        case STOP_NOW:
                            medicalInfoToUse = this.medicalInfo;
                            break;
                        default:
                            medicalInfoToUse = null;
                            break;
                    }
                    //currentposition
                    final CurrentPosition currentPosition = new CurrentPosition(this.positionInfo.getRunningId(),
                            new Point(this.positionInfo.getPosition().getLatitude(), this.positionInfo.getPosition().getLongitude()),
                            this.positionInfo.getRunnerStatus(),
                            this.positionInfo.getSpeed(),
                            this.positionInfo.getLeg().getHeading(),
                            medicalInfoToUse
                    );
                    //service to publish currentPosition to distribution service API
                    positionService.processPositionInfo(id, currentPosition, this.exportPositionToMessaging);
                }
                sleep(startTime);
            }
        }
        catch(InterruptedException ie){
            destroy();
            return;
        }
        destroy();
    }



}
