import { BaseView } from "../BaseView.js";
import { PreLobbyView } from "./view.js";

class GameView extends BaseView {

    _initialize() {
        this.userColor = '#0000FF';
        this.otherUserColor = '#FF0000';
        gameID.textContent = this.model.id;
        gameID.style.display = 'flex';
        this.showBackButton();
        this.awaitSudokuData();
    }

    _subscribeAll() {
        this.subscribe(this.model.id, 'cell-focused', this.handleCellFocus);
        this.subscribe(this.model.id, 'cell-blurred', this.handleCellBlur);
        this.subscribe(this.model.id, 'cell-valued', this.handleCellValue);
    }

    getRandomColor() {
        return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
    }

    awaitSudokuData() {
        if (this.model.value.length === 0) {
            this.future(500).awaitSudokuData();
        } else {
            this.showSudoku();
            gameDifficulty.textContent = this.model.difficulty;
            gameDifficulty.style.display = 'flex';
            console.log("solution: ");
            console.log(this.model.solution);
        }
    }

    showSudoku() {
        for (let i = 0; i < 81; i++) {
            const cell = this._addObjectToHTML('div', '', sudokuContainer);
            cell.className = 'sudokuCell';
            cell.setAttribute('data-index', i);
            this.setupCell(cell, i);
        }
    }

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
        }
    }

    showBackButton() {
        const backButton = this._addObjectToHTML('button', 'backButton', backButtonContainer);
        backButton.textContent = 'Back';
        backButton.addEventListener('click', () => this._gameOver(this.model.id, false));
    }

    handleCellValue(index) {
        const cell = document.querySelector(`[data-index="${index}"]`);
        cell.textContent = this.model.value[index % 9][Math.floor(index / 9)];
        if (cell === document.activeElement) cell.blur();
    }

    handleCellFocus(data) {
        const cell = document.querySelector(`[data-index="${data.index}"]`);
        cell.focus();
        cell.style.outline = `2px solid ${data.user === this.viewId ? this.userColor : this.otherUserColor}`;
    }

    handleCellBlur(index) {
        const cell = document.querySelector(`[data-index="${index}"]`);
        cell.blur();
        cell.style.outline = 'none';
    }

    _gameOver(game, wait = true) {
        if (game === this.model.id) {
            if (wait) {
                gameID.textContent = 'Game Over! - Victory!';
                this.future(1000).changeScene();
            } else {
                this.changeScene();
            }
        }
    }

    changeScene() {
        gameID.style.display = 'none';
        gameDifficulty.style.display = 'none';
        new PreLobbyView({ model: this.model.parent });
        this.detach();
    }
}

export { GameView };