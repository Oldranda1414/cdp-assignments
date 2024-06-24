// this file is the equivalent of an enum in go. It defines the player states
package main

// Define a custom type for the player states
type PlayerState int

// Define the player states
const (
	awatingRoundStart PlayerState = iota
	awaitingVerdict
)
