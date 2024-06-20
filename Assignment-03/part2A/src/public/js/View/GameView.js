import { BaseView } from "../BaseView.js";
import { PreLobbyView } from "./view.js";

class GameView extends BaseView {

    _initialize() {
        gameID.textContent = this.model.id;
        gameID.style.display = 'flex';
        this.showBackButton();
        sudokuContainer.textContent = 'Loading...';
    }

    _subscribeAll() {
        this.subscribe(this.model.id, "sudoku-ready", this.showSudoku);
    }

    showSudoku() {
        sudokuContainer.textContent = '';
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
            cell.addEventListener('keypress', this.handleKeyPress);
        } else {
            cell.textContent = cellData;
        }
    }

    showBackButton() {
        const backButton = this._addObjectToHTML('button', 'backButton', backButtonContainer);
        backButton.textContent = 'Back';
        backButton.addEventListener('click', () => this._gameOver());
    }

    handleKeyPress(e) {
        const key = e.key;
        const cell = e.target;
        if (/^[1-9]$/.test(key) && key !== '0') {
            cell.textContent = key;
            cell.blur();
        } else e.preventDefault();
    }

    _gameOver() {
        gameID.style.display = 'none';
        backButton.style.display = 'none';
        new PreLobbyView({ model: this.model.parent });
        for (let cell of sudokuContainer.children) {
            cell.removeEventListener('keypress', this.handleKeyPress);
        }
        this.detach();
    }
}

export { GameView };