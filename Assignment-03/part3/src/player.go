package main

import (
    "fmt"
)

func Player(playerChannel chan string, oracleChannel chan string, playerId int, maxValueForSecretNumber int) {
	playerLog(playerId, "starting player")

	currentState := awatingRoundStart

	//initializing guess boundries
	lowerBoundry := 0
	upperBoundry := maxValueForSecretNumber

	//variable to keep track of current guess
	var currentGuess int

	for {
		switch currentState {
		case awatingRoundStart:
			playerLog(playerId, "awaiting round start message")

			//receiving message
			header, _ := processMessage(playerChannel)

			playerLog(playerId, "round start message received, sending guess")
			//the following switch case is useless as it only has one case
			//it is present to improve code readability
			switch header {
			case "roundStart":

				currentGuess = generateRandomNumber(upperBoundry, lowerBoundry)

				playerLog(playerId, fmt.Sprintf("attempting to guess with value %d", currentGuess))

				sendGuess(oracleChannel, currentGuess, playerId)

				currentState = awaitingVerdict
			}
		case awaitingVerdict:
			playerLog(playerId, "awaiting verdict")

			//receiving message
			header, body := processMessage(playerChannel)

			playerLog(playerId, "verdict recieved")

			//processing response
			switch header {
			case "wrong":
				playerLog(playerId, "guess was wrong, adjusting boundaries")

				switch body {
				case "higher":
					lowerBoundry = currentGuess
				case "lower":
					upperBoundry = currentGuess
				}
			case "gameover":
				switch body {
				case "winner":
				playerLog(playerId, "hurray, I win!")
				
				case "loser":
				playerLog(playerId, "Oh no, I lost")

				}
				return
			}
		}
	}
}

func sendGuess(oracleChannel chan string, guess int, playerId int) {
	oracleChannel <- fmt.Sprintf("%d : %d", playerId, guess)
}

func playerLog(playerId int, msg string) {
	fmt.Sprintln("[player %d] :" + msg, playerId)
}