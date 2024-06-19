import { BaseView } from "../BaseView.js";

const CREATE_NEW_GAME_STR = 'create new game';

class PreLobbyView extends BaseView {
    

    _initialize() {
        this._log("This view is " + this.viewId);
        userID.innerHTML = this.viewId;
        this.showGamesList();
    }

    _subscribeAll() {
        this.subscribe(this.viewId, "game-created", this.handleJoinCreateGame);
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
        sudokuContainer.style.display = 'none';
    }

    showSudoku() {
        sudokuContainer.style.display = 'grid';
    }

    showGamesList() {
        const list = document.createElement('ul');
        this.model.gamesList.forEach(game => this.addListItem(game, list));
        this.addListItem(CREATE_NEW_GAME_STR, list);
        gamesListContainer.appendChild(list);
    }

    addListItem(game, list) {
        const item = document.createElement('li');
        item.textContent = game;
        item.addEventListener('click', () => this.handleJoinCreateGame(game));
        list.appendChild(item);
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

    handleJoinCreateGame(game) {
        if (game == CREATE_NEW_GAME_STR) {
            this.publish(this.model.id, "create-game", this.viewId);
        } else {
            //join game
            this._log("Joining game " + game);
        }
    }

    _gameOver() {
    }
}

export { PreLobbyView };