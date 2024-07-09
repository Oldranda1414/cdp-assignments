import { BaseModel } from "../BaseModel.js";
import { GameModel } from "./GameModel.js";

/**
 * Model class for the pre-lobby view.
 */
class PreLobbyModel extends BaseModel {

    /**
     * The list of all games not finished.
     */
    gamesList = [];

    _subscribeAll() {
        this.subscribe(this.id, "create-game", this.createGame);
    }

    /**
     * Creates a new game and adds it to the games list. It is called as response to the 'create-game' event and publishes a 'game-created' event.
     * @param {*} data 
     */
    createGame(data) {
        const game = GameModel.create({ parent: this, value: data.value, solution: data.solution, difficulty: data.difficulty });
        this.gamesList.push(game);
        this.publish(this.id, "game-created", { game: game.id, creator: data.creator });
    }

    /**
     * Fetches a new sudoku from an external API.
     * @returns the new sudoku, its solution and the difficulty
     */
    async getNewSudoku() {
        try {
            const response = await fetch("https://sudoku-api.vercel.app/api/dosuku");
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            const data = await response.json();
            const grid = data.newboard.grids[0];
            return { value: grid.value, solution: grid.solution, difficulty: grid.difficulty };
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
            this._log("Retrying...");
            return this.getNewSudoku();
        }
    }

    _gameOver(game) {
        this.gamesList.splice(this.gamesList.indexOf(game), 1);
    }
}

PreLobbyModel.register("PreLobbyModel");

export { PreLobbyModel };