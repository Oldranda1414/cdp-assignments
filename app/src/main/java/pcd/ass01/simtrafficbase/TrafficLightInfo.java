package pcd.ass01.simtrafficbase;

import pcd.ass01.simtrafficbase.environment.Road;

public  record TrafficLightInfo(TrafficLight sem, Road road, double roadPos) {}
