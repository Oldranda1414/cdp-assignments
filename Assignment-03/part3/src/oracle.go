package main

import (
    "fmt"
)

func Oracle(done chan bool) {
    fmt.Println("Oracle function is running")
    done <- true
}
