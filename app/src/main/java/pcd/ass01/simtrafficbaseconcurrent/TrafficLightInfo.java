package pcd.ass01.simtrafficbaseconcurrent;

import pcd.ass01.simtrafficbaseconcurrent.environment.Road;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
