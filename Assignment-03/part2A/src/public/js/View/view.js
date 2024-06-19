import { BaseView } from "../BaseView.js";

class PreLobbyView extends BaseView {
    
    _initialize() {
        this._log("This view is " + this.viewId);
        userID.innerHTML = this.viewId;
        this.displaySudoku();
    }

    displaySudoku() {
        for (let i = 0; i < 81; i++) {
            const cell = document.createElement('div');
            cell.className = 'sudokuCell';
            cell.contentEditable = true;
            cell.setAttribute('data-index', i);
            cell.addEventListener('input', this.handleInput);
            sudokuContainer.appendChild(cell);
        }
    }

    handleInput() {
        const cell = e.target;
        const value = cell.textContent;
        // Allow only numbers 1-9
        if (!/^[1-9]$/.test(value)) {
            cell.textContent = '';
        }
    }

    _gameOver() {
    }
}

export { PreLobbyView };