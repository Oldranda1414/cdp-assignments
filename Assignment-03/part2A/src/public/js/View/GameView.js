import { BaseView } from "../BaseView.js";
import { PreLobbyView } from "./view.js";

class GameView extends BaseView {
    

    _initialize() {
        this._log("This view is " + this.viewId);
        gameID.textContent = this.model.id;
        gameID.style.display = 'flex';
        this.setupSudoku();
        this.showBackButton();
    }

    _subscribeAll() {
    }

    setupSudoku() {
        for (let i = 0; i < 81; i++) {
            const cell = this._addObjectToHTML('div', '', sudokuContainer);
            cell.className = 'sudokuCell';
            cell.contentEditable = true;
            cell.setAttribute('data-index', i);
            cell.addEventListener('keypress', this.handleKeyPress);
        }
    }

    showBackButton() {
        backButton.addEventListener('click', () => this._gameOver());
        backButton.style.display = 'flex';
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
        gameID.style.display = 'none';
        backButton.style.display = 'none';
        new PreLobbyView(this.model.parent);
        this.detach();
    }
}

export { GameView };