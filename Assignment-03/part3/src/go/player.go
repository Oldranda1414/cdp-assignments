package main

import (
    "fmt"
)

func Player(playerChannel chan string, oracleChannel chan string, playerId int, maxValueForSecretNumber int, done chan bool) {
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
			playerLog(playerId, "listening for messages")

			//receiving message
			header, body := processMessage(playerChannel)

			playerLog(playerId, "message received")

			switch header {
			case "roundStart":
				currentGuess = generateRandomNumber(upperBoundry, lowerBoundry)

				playerLog(playerId, fmt.Sprintf("attempting to guess with value %d", currentGuess))

				sendGuess(oracleChannel, currentGuess, playerId)

				currentState = awaitingVerdict

			case "gameover":
				processGameOver(playerId, body)

				done <- true

				return
			}
		case awaitingVerdict:
			playerLog(playerId, "listening for messages")

			//receiving message
			header, body := processMessage(playerChannel)

			playerLog(playerId, "message received")

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
				
				currentState = awatingRoundStart

			case "gameover":
				processGameOver(playerId, body)

				done <- true

				return
			}
		}
	}
}

func sendGuess(oracleChannel chan string, guess int, playerId int) {
	oracleChannel <- fmt.Sprintf("%d : %d", playerId, guess)
}

func processGameOver( playerId int, result string) {
	switch result {
	case "winner":
	playerLog(playerId, "hurray, I win!")
	
	case "loser":
	playerLog(playerId, "Oh no, I lost")

	}
}

func playerLog(playerId int, msg string) {
	fmt.Println("[ Player", playerId, "] :", msg)
	// fmt.Sprintln("[player %d] :" + msg, playerId)
}