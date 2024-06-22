package main

import (
    "fmt"
    "strings"
)

func Oracle(done chan bool, oracleChannel chan string, playerChannels []chan string, maxValueForExtractedNumber int) {
    // initializing variable to check when game is over
    gameOver := flase

    //initializing game loop
    for {

        playersDone := 0

        //notifing players that a new round is started
        notifyPlayers(playerChannels)

        //processing players guesses until all players guesses have been processed
        for playersDone != len(playerChannels) {

            gameOver = processMessage(oracleChannel, playerChannels)

            playersDone++

            //ending the round if the game is over
            if !gameOver {
                break
            }

        }

        //ending game loop if the game is over
        if !gameOver {
            break
        }
    }

    //comunicating to main that oracle is done
    done <- true
}

func notifyPlayers(playerChannels []chan string) {
    for i := range playerChannels {
        playerChannels[i] <- fmt.Sprintf("roundStart")
    }
}

func processMessage(oracleChannel chan string, playerChannels []chan string, secretNumber int) bool {
    //receiving the message
    msg := <-oracleChannel

    //preprocesing the message
    splitMessage := strings.Split(str, " ")
    playerNumber := strconv.Atoi(splitMessage[0])
    playerGuess := strconv.Atoi(splitMessage[2])

    if playerGuess == secretNumber {
        notifyGameOver(playerNumber, playerChannels)
        return true
    } else {
        notifyWrongAnswer(secretNumber, playerGuess, playerChannels[playerNumber - 1])
        return false
    }
}

func notifyGameOver(winningPlayerNumber int, playerChannels []chan string){
    winningPlayerIndex := winningPlayerNumber - 1

    for i := range playerChannels {
        if i == winningPlayerIndex {
            playerChannels[i] <- fmt.Sprintf("gameOver : winner")
        } else {
            playerChannels[i] <- fmt.Sprintf("gameOver : loser")
        }
    }
}

func log(message string) {
    fmt.Println("[Oracle] :", message)
}