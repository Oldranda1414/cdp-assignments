import { BaseModel } from "../BaseModel.js";
import { GameModel } from "./GameModel.js";

class PreLobbyModel extends BaseModel {
    
    users = [];
    gameModel = null;

    _subscribeAll() {
        this.subscribe(this.sessionId, "view-join", this.viewJoin);
        this.subscribe(this.sessionId, "view-exit", this.viewDrop);
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
        this.users.splice(this.users.indexOf(viewId),1);
    }

    _gameOver() {
        this._log("Game over: restarting game model");
        this.future(1000).restart(); //safe time to be sure that all models have been destroyed
    }

    restart() {
        this.gameModel = GameModel.create({parent: this});
    }
}

PreLobbyModel.register("PreLobbyModel");

export { PreLobbyModel };