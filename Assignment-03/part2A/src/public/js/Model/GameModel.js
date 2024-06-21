import { BaseModel } from "../BaseModel.js";

/**
 * Model class for the sudoku game.
 */
class GameModel extends BaseModel {

    /**
     * Current state of the sudoku grid.
     */
    value = [];

    /**
     * Solution of the sudoku grid.
     */
    solution = [];

    /**
     * Difficulty of the sudoku grid.
     */
    difficulty;

    /**
     * List of pointers to the cells that are currently focused by the users.
     */
    userPointers = [];

    _initialize(data) {
        this.difficulty = data.difficulty;
        this.value = data.value;
        this.solution = data.solution;
    }

    _subscribeAll() {
        this.subscribe(this.id, 'cell-focus', this.handleCellFocus);
        this.subscribe(this.id, 'cell-blur', this.handleCellBlur);
        this.subscribe(this.id, 'cell-value', this.handleCellValue);
    }

    /**
     * It's called as response to the 'cell-focus' event and publishes a 'cell-focused' event. It adds the user pointer to the list of user pointers.
     * @param {*} data index of the pointed cell and the user that pointed it
     */
    handleCellFocus(data) {
        if (!this.userPointers.map(p => p.index).includes(data.index)) {
            this.userPointers.push({ index: data.index, user: data.user });
            this.publish(this.id, 'cell-focused', data);
        }
    }

    /**
     * It's called as response to the 'cell-blur' event and publishes a 'cell-blurred' event. It removes the user pointer from the list of user pointers.
     * @param {*} user the user that blurred the cell
     */
    handleCellBlur(user) {
        if (this.userPointers.map(p => p.user).includes(user)) {
            this.publish(this.id, 'cell-blurred', this.userPointers.filter(p => p.user === user)[0].index);
            this.userPointers.splice(this.userPointers.findIndex(pointer => pointer.user === user), 1);
        }
    }

    /**
     * It's called as response to the 'cell-value' event. It sets the value of the cell and publishes a 'cell-valued' event. 
     * If the value of the grid is equal to the solution, it publishes a 'game-over' event and destroys the game.
     * @param {*} data index of the cell and the value
     */
    handleCellValue(data) {
        const x = data.index % 9;
        const y = Math.floor(data.index / 9);
        this.value[x][y] = data.value;
        this.publish(this.id, 'cell-valued', data.index);
        if (this.value.toString() === this.solution.toString()) {
            this.publish(this.sessionId, 'game-over', this.id);
            this.destroy();
        }
    }
}

GameModel.register("GameModel");

export { GameModel };