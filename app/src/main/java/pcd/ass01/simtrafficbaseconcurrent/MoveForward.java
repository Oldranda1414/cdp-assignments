package pcd.ass01.simtrafficbaseconcurrent;

import pcd.ass01.simengineseq_improved.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(double distance) implements Action {}
