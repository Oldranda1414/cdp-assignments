package main

import (
	"math/rand"
	"strings"
)

func processMessage(channel chan string) (string, string) {
	//receiving the message
	msg := <-channel

    var header string
    var body string

	//preprocesing the message
	splitMessage := strings.Split(msg, " ")
    if len(splitMessage) < 2 {
	    header = splitMessage[0]
        body = ""
    } else {
	    header = splitMessage[0]
        body = splitMessage[2]
    }

	return header, body
}

// Generate a random number between min and max (inclusive)
func generateRandomNumber(max int, min int) int {
	
	return rand.Intn(max - min + 1) + min
}