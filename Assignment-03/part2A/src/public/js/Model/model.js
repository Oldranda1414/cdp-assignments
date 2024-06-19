import { BaseModel } from "../BaseModel.js";
import { GameModel } from "./GameModel.js";

class PreLobbyModel extends BaseModel {
    
    users = [];
    gameModel = null;
    gamesList = [];

    _subscribeAll() {
        this.subscribe(this.sessionId, "view-join", this.viewJoin);
        this.subscribe(this.sessionId, "view-exit", this.viewDrop);
        this.subscribe(this.id, "create-game", this.createGame);
    }

    _initialize() {
        this._log("This session id is " + this.sessionId); 
    }

    /**
     * Handle a new connected view.
     * @param {any} viewId the id of the new view connected.
     */
    viewJoin(viewId) {
        this.users.push(viewId);
        if (this.gameModel === null) {
            this.gameModel = GameModel.create({parent: this});
        }
    }

    /**
     * Handle the view left event.
     * @param {any} viewId the id of the outgoing view.
     */
    viewDrop(viewId){
        this.users.splice(this.users.indexOf(viewId), 1);
    }

    createGame(userId) {
        const game = GameModel.create({parent: this});
        this._log("created new game: " + game.id);
        this.gamesList.push(game);
        this.publish(userId, "game-created", game.id);
    }

    _gameOver() {
        this._log("Game over");
    }
}

PreLobbyModel.register("PreLobbyModel");

export { PreLobbyModel };