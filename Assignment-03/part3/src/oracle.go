package main

import (
    "fmt"
    "strings"
    "strconv"
    "math/rand"
)

func Oracle(done chan bool, oracleChannel chan string, playerChannels []chan string, maxValueForSecretNumber int) {

    log("starting oracle")

    // generating secret number
    secretNumber := generateSecretNumber(maxValueForSecretNumber)

    // setting initial state
    currentState := roundStart

    //this variable is necessary to keep track of how many players have already attempted a guess this round
    playersDone := 0

    //saving the number of players
    nPlayers := len(playerChannels)

    //initializing game loop
    for {
        switch currentState {

        case roundStart:
            //notifing players that a new round is started
            notifyPlayers(playerChannels)

            currentState = awaitingPlayerGuess

        case awaitingPlayerGuess:

            log("awaiting player guess")

            //processing the message
            senderId, playerGuess := processMessage(oracleChannel)

            log(fmt.Sprintf("guess of value %d received from player %d", playerGuess, senderId))

            //keeping track of how many players have attempted to guess
            playersDone++

            //ending the game if the player guessed correctly
            //otherwise giving the hint
            if playerGuess == secretNumber {

                log(fmt.Sprintf("player %d guessed the secret number!", senderId))

                notifyGameOver(senderId, playerChannels)

                currentState = gameEnd
            } else {

                log(fmt.Sprintf("player %d guessed wrong, sending hint", senderId))
                
                //retrieving the sender channel
                senderIndex := senderId - 1
                playerChannel := playerChannels[senderIndex]

                giveHint(playerChannel, playerGuess, secretNumber)

                currentState = playerGivenHint
            }

        case playerGivenHint:

            //if the round is over, starting a new round
            //otherwise await more player guesses
            if playersDone == nPlayers {
                currentState = roundStart
            } else {
                currentState = awaitingPlayerGuess
            }

        case gameEnd:

            log("game over, shutting down")

            //comunicating to main that oracle is done
            done <- true

            //shutting down the oracle go routine
            return

        }

    }

}

func notifyPlayers(playerChannels []chan string) {
    log("notifing players that new round is starting")

    for i := range playerChannels {
        playerChannels[i] <- "roundStart"
    }
}

func processMessage(oracleChannel chan string) (int, int) {
    //receiving the message
    msg := <-oracleChannel

    //preprocesing the message
    splitMessage := strings.Split(msg, " ")
    playerNumber, _ := strconv.Atoi(splitMessage[0])
    playerGuess, _ := strconv.Atoi(splitMessage[2])

    return playerNumber, playerGuess
}

func notifyGameOver(winningPlayerNumber int, playerChannels []chan string){
    winningPlayerIndex := winningPlayerNumber - 1

    for i := range playerChannels {
        if i == winningPlayerIndex {
            playerChannels[i] <- "gameOver : winner"
        } else {
            playerChannels[i] <- "gameOver : loser"
        }
    }
}

func giveHint(playerChannel chan string, playerGuess int, secretNumber int) {
    if secretNumber < playerGuess {
        playerChannel <- "wrong : lower"
    } else {
        playerChannel <- "wrong : higher"
    }
}

func generateSecretNumber(max int) int {
    // Generate a random number between 0 and max (inclusive)
    randomNumber := rand.Intn(max + 1)

    return randomNumber
}

func log(message string) {
    fmt.Println("[Oracle] :", message)
}