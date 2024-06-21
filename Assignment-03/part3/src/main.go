package main

import (
    "bufio"
    "fmt"
    "os"
    "strconv"
    "strings"
	"time"
)

func main() {
    var numberOfPlayers int
    var maxValueForExtractedNumber int

    fmt.Println("Welcome to Guess the Number")

	//obtaining the user values
    fmt.Print("Enter the number of players: ")
    numberOfPlayers = takeIntegerInput()

    fmt.Print("Enter the max value for the extracted number: ")
    maxValueForExtractedNumber = takeIntegerInput()

    fmt.Println("The number of players is :", numberOfPlayers)
    fmt.Println("The range of the extracted number is: 0 -", maxValueForExtractedNumber)
	
	//starting the oracle goroutine
	go Oracle()

	//sleep added to execute the oracle routine before main terminates
	time.Sleep(1 * time.Second)

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
