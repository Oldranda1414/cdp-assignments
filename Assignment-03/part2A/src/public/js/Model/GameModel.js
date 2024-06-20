import { BaseModel } from "../BaseModel.js";

class GameModel extends BaseModel {

    value = [];
    solution = [];
    difficulty;
    userPointers = [];


    _initialize(data) {
        this.value = data.value;
        this.solution = data.solution;
        this.difficulty = data.difficulty;
    }

    _subscribeAll() {
        this.subscribe(this.id, 'cell-focus', this.handleCellFocus);
        this.subscribe(this.id, 'cell-blur', this.handleCellBlur);
        this.subscribe(this.id, 'cell-value', this.handleCellValue);
    }

    handleCellFocus(data) {
        if (!this.userPointers.map(p => p.index).includes(data.index)) {
            this.userPointers.push({ index: data.index, user: data.user });
            this.publish(this.id, 'cell-focused', data);
        }
    }

    handleCellBlur(user) {
        if (this.userPointers.map(p => p.user).includes(user)) {
            this.publish(this.id, 'cell-blurred', this.userPointers.filter(p => p.user === user)[0].index);
            this.userPointers.splice(this.userPointers.findIndex(pointer => pointer.user === user), 1);
        }
    }

    handleCellValue(data) {
        const x = data.index % 9;
        const y = Math.floor(data.index / 9);
        this.value[x][y] = data.value;
        this.publish(this.id, 'cell-valued', data.index);
    }
}

GameModel.register("GameModel");

export { GameModel };