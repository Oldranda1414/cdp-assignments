import { BaseView } from "../BaseView.js";

class PreLobbyView extends BaseView {
    
    _initialize() {
        this._log("This view is " + this.viewId);
        userID.innerHTML = this.viewId;
        this.setupSudoku();
    }

    setupSudoku() {
        for (let i = 0; i < 81; i++) {
            const cell = document.createElement('div');
            cell.className = 'sudokuCell';
            cell.contentEditable = true;
            cell.setAttribute('data-index', i);
            cell.addEventListener('keypress', this.handleKeyPress);
            sudokuContainer.appendChild(cell);
        }
        sudokuContainer.hidden = true;
    }

    showSudoku() {
        sudokuContainer.hidden = false;
    }

    handleInput() {
        const cell = e.target;
        const value = cell.textContent;
        // Allow only numbers 1-9
        if (!/^[1-9]$/.test(value)) {
            cell.textContent = '';
        }
    }

    handleKeyPress(e) {
        const key = e.key;
        const cell = e.target;
        // Prevent default behavior if the key is not a number between 1 and 9
        if (/^[1-9]$/.test(key) && key !== '0') {
            cell.textContent = key;
            cell.blur();
        } else e.preventDefault();
    }

    _gameOver() {
    }
}

export { PreLobbyView };