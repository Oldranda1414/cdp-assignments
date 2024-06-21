package main

import (
    "fmt"
)

func main(){
    var numberOfPlayers int
    var maxValueForExtractedNumber int

    fmt.Println("Welcome to Guess the Number")

    fmt.Print("Enter the number of players: ")
    if _, err := fmt.Scanln(&numberOfPlayers); err != nil {
        fmt.Println("Invalid input. Please enter a valid integer.")
        return
    }

    fmt.Print("Enter the max value for the extracted number: ")
    if _, err := fmt.Scanln(&maxValueForExtractedNumber); err != nil {
        fmt.Println("Invalid input. Please enter a valid integer.")
        return
    }

    fmt.Println("The number of players is :", numberOfPlayers)
    fmt.Println("The range of the extacted number is: 0 -", maxValueForExtractedNumber)
}

func scanInteger(variable int){
    if _, err := fmt.Scanln(&variable); err != nil {
        fmt.Println("Invalid input. Please enter a valid integer.")
        return
    }
}
