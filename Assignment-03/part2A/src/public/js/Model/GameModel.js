import { BaseModel } from "../BaseModel.js";

class GameModel extends BaseModel {
    
    _subscribeAll() {
    }

    _initialize() { 
        this._log("Parent id is " + this.parent.id);
        this._log("This game id is " + this.id);
    }

    _gameOver() {
    }
}

GameModel.register("GameModel");

export { GameModel };