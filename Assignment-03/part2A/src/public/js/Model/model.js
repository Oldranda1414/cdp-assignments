import { BaseModel } from "../BaseModel.js";
import { GameModel } from "./GameModel.js";

class PreLobbyModel extends BaseModel {

    users = [];
    gamesList = [];

    _initialize() {
    }

    _subscribeAll() {
        this.subscribe(this.sessionId, "view-join", this.viewJoin);
        this.subscribe(this.sessionId, "view-exit", this.viewDrop);
        this.subscribe(this.id, "create-game", this.createGame);
    }

    /**
     * Handle a new connected view.
     * @param {any} viewId the id of the new view connected.
     */
    viewJoin(viewId) {
        this.users.push(viewId);
    }

    /**
     * Handle the view left event.
     * @param {any} viewId the id of the outgoing view.
     */
    viewDrop(viewId) {
        this.users.splice(this.users.indexOf(viewId), 1);
    }

    createGame(data) {
        const game = GameModel.create({ parent: this, value: data.value, solution: data.solution, difficulty: data.difficulty });
        this.gamesList.push(game);
        this.publish(this.id, "game-created", { game: game.id, creator: data.creator });
    }

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
}

PreLobbyModel.register("PreLobbyModel");

export { PreLobbyModel };