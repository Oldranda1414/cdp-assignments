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
    var maxValueForExtractedNumber int


    fmt.Println("Welcome to Guess the Number")

	if len(os.Args) < 3 {
		//obtaining the user values
		fmt.Print("Enter the number of players: ")
		numberOfPlayers = takeIntegerInput()

		fmt.Print("Enter the max value for the extracted number: ")
		maxValueForExtractedNumber = takeIntegerInput()
	} else {
		var err error

		numberOfPlayers, err = strconv.Atoi(os.Args[1])
		if err != nil {
			fmt.Println("Error occured with the first cli argument:", err)
			return
		}

		maxValueForExtractedNumber, err = strconv.Atoi(os.Args[2])
		if err != nil {
			fmt.Println("Error occured with the second cli argument:", err)
			return
		}
	}

    fmt.Println("The number of players is :", numberOfPlayers)
    fmt.Println("The range of the extracted number is: 0 -", maxValueForExtractedNumber)

	//creating a channel to syncronize with oracle routine
	done := make(chan bool)
	
	//starting the oracle goroutine
	go Oracle(done)

	//wait for oracle routine to finish
	<-done

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

func convertStringToInteger(str string) {
}
