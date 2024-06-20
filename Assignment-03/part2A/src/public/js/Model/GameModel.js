import { BaseModel } from "../BaseModel.js";

class GameModel extends BaseModel {

    value = [];
    solution = [];
    difficulty;

    _initialize() {
        this.setupSudokuData();
        this.awaitSudokuSetup();
    }

    awaitSudokuSetup() {
        if (this.value.length === 0) {
            this.future(500).awaitSudokuSetup();
        } else {
            this.publish(this.id, "sudoku-ready");
        }
    }

    setupSudokuData() {
        fetch("https://sudoku-api.vercel.app/api/dosuku")
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const grid = data.newboard.grids[0];
                this.value = grid.value;
                this.solution = grid.solution;
                this.difficulty = grid.difficulty;
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
                this._log("Retrying...");
                this.setupSudokuData();
            });
    }
}

GameModel.register("GameModel");

export { GameModel };