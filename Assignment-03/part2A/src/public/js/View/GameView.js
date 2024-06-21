import { BaseView } from "../BaseView.js";
import { PreLobbyView } from "./view.js";

/**
 * View class for the sudoku game.
 */
class GameView extends BaseView {

    _initialize() {
        this.setupUserColors();
        this.showGameInfos();
        this.showBackButton();
        this.showSudoku();
        this._log("solution");
        console.log(this.model.solution);
    }

    _subscribeAll() {
        this.subscribe(this.model.id, 'cell-focused', this.handleCellFocus);
        this.subscribe(this.model.id, 'cell-blurred', this.handleCellBlur);
        this.subscribe(this.model.id, 'cell-valued', this.handleCellValue);
    }

    /**
     * Sets the user cursor color and the other user cursor color.
     */
    setupUserColors() {
        this.userColor = '#0000FF';
        this.otherUserColor = '#FF0000';
    }

    /**
     * @returns a random color
     */
    getRandomColor() {
        return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
    }

    /**
     * Shows the game id and the difficulty.
     */
    showGameInfos() {
        gameID.textContent = this.model.id;
        gameID.style.display = 'flex';
        gameDifficulty.textContent = this.model.difficulty;
        gameDifficulty.style.display = 'flex';
    }

    /**
     * Shows the back button.
     */
    showBackButton() {
        const backButton = this._addObjectToHTML('button', 'backButton', backButtonContainer);
        backButton.textContent = 'Back';
        backButton.addEventListener('click', () => this._gameOver(this.model.id, false));
    }

    /**
     * Shows the sudoku.
     */
    showSudoku() {
        for (let i = 0; i < 81; i++) {
            const cell = this._addObjectToHTML('div', '', sudokuContainer);
            cell.className = 'sudokuCell';
            cell.setAttribute('data-index', i);
            this.setupCell(cell, i);
        }
    }

    /**
     * Sets up a cell of the sudoku. It is editable if the value is 0.
     * @param {*} cell the cell
     * @param {*} i the index of the cell
     */
    setupCell(cell, i) {
        const cellData = this.model.value[i % 9][Math.floor(i / 9)];
        if (cellData === 0) {
            cell.contentEditable = true;
            cell.addEventListener('keydown', (e) => {
                e.preventDefault();
                const key = e.key;
                const cell = e.target;
                if (/^[1-9]$/.test(key)) {
                    this.publish(this.model.id, 'cell-value', { index: cell.getAttribute('data-index'), value: key })
                }
            });
            cell.addEventListener('mousedown', (e) => {
                e.preventDefault(); 
                this.publish(this.model.id, 'cell-focus', { user: this.viewId, index: i });
            });
            cell.addEventListener('blur', (e) => {
                e.preventDefault(); 
                this.publish(this.model.id, 'cell-blur', this.viewId);
            });
        } else {
            cell.textContent = cellData;
            cell.style.backgroundColor = '#f0f0f0';
        }
    }

    /**
     * It's the handler for the 'cell-focused' event. It sets the focus on the cell.
     * @param {*} data index and user
     */
    handleCellFocus(data) {
        const cell = document.querySelector(`[data-index="${data.index}"]`);
        if (data.user === this.viewId) cell.focus();
        cell.style.outline = `2px solid ${data.user === this.viewId ? this.userColor : this.otherUserColor}`;
    }

    /**
     * It's the handler for the 'cell-blurred' event. It removes the focus from the cell.
     * @param {*} index index of the cell
     */
    handleCellBlur(index) {
        const cell = document.querySelector(`[data-index="${index}"]`);
        cell.blur();
        cell.style.outline = 'none';
    }

    /**
     * It's the handler for the 'cell-valued' event. It sets the value of the cell and removes the focus.
     * @param {*} index index of the cell
     */
    handleCellValue(index) {
        const cell = document.querySelector(`[data-index="${index}"]`);
        cell.textContent = this.model.value[index % 9][Math.floor(index / 9)];
        if (cell === document.activeElement) cell.blur();
    }

    _gameOver(game, wait = true) {
        if (game === this.model.id) {
            if (wait) {
                const victoryLabel = this._addObjectToHTML('div', 'victoryLabel', sudokuContainer);
                victoryLabel.textContent = 'Game Over! - Victory!';
                this.future(5000).changeScene();
            } else {
                this.changeScene();
            }
        }
    }

    /**
     * Changes the scene to the pre-lobby view.
     */
    changeScene() {
        gameID.style.display = 'none';
        gameDifficulty.style.display = 'none';
        new PreLobbyView({ model: this.model.parent });
        this.detach();
    }
}

export { GameView };