// this file is the equivalent of an enum in go. It defines the oracle states
package main

// Define a custom type for the oracle states
type OracleState int

// Define the oracle states
const (
	roundStart OracleState = iota
	awaitingPlayerGuess
	playerGivenHint
	gameEnd
)
