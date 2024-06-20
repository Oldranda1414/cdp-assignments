import { BaseModel } from "../BaseModel.js";

class GameModel extends BaseModel {

    value = [];
    solution = [];
    difficulty;

    _initialize(data) {
        this.value = data.value;
        this.solution = data.solution;
        this.difficulty = data.difficulty;
    }

    _subscribeAll() {
    }
}

GameModel.register("GameModel");

export { GameModel };