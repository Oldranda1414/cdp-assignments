package main

import (
    "bufio"
    "fmt"
    "os"
    "strconv"
    "strings"
)

func main() {
    var numberOfPlayers int
    var maxValueForSecretNumber int


    fmt.Println("Welcome to Guess the Number")

	if len(os.Args) < 3 {
		//obtaining the user values
		fmt.Print("Enter the number of players: ")
		numberOfPlayers = takeIntegerInput()

		fmt.Print("Enter the max value for the extracted number: ")
		maxValueForSecretNumber = takeIntegerInput()
	} else {
		var err error

		numberOfPlayers, err = strconv.Atoi(os.Args[1])
		if err != nil {
			fmt.Println("Error occured with the first cli argument:", err)
			return
		}

		maxValueForSecretNumber, err = strconv.Atoi(os.Args[2])
		if err != nil {
			fmt.Println("Error occured with the second cli argument:", err)
			return
		}
	}

    fmt.Println("The number of players is :", numberOfPlayers)
    fmt.Println("The range of the extracted number is: 0 -", maxValueForSecretNumber)

	//creating a channel to syncronize with oracle routine
	oracleDone := make(chan bool, 1)

	//create the channel for the oracle
    //buffered channel initialized with large buffer (100)
	oracleChannel := make(chan string, numberOfPlayers)

	// create the list of player channels
    playerChannels := make([]chan string, numberOfPlayers)
    playerDoneChannels := make([]chan bool, numberOfPlayers)
    
    // Initialize each player and player channel
    for i := range numberOfPlayers {
        //buffered channels initialized with large buffers (100)
        playerChannels[i] = make(chan string, 1)
        playerDoneChannels[i] = make(chan bool, 1)
        playerId := i + 1
        go Player(playerChannels[i], oracleChannel, playerId, maxValueForSecretNumber, playerDoneChannels[i])
    }
	
	//starting the oracle goroutine
	go Oracle(oracleDone, oracleChannel, playerChannels, maxValueForSecretNumber)

	//wait for oracle routine to finish
	<-oracleDone

    for i := range numberOfPlayers {
	    <-playerDoneChannels[i]
    }

}

func scanInteger(variable *int) bool {
    reader := bufio.NewReader(os.Stdin)
    input, _ := reader.ReadString('\n')
    input = strings.TrimSpace(input)
    value, err := strconv.Atoi(input)
    if err != nil {
        return false
    }
    *variable = value
    return true
}

func takeIntegerInput() int {
    var result int
    for {
        if scanInteger(&result) {
            break
        }
        fmt.Println("Invalid input. Please enter a valid integer.")
    }
    return result
}
