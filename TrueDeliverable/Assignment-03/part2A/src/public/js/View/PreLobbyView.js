import { BaseView } from "../BaseView.js";
import { GameView } from "./GameView.js";

const CREATE_NEW_GAME_STR = 'Create new game';

/**
 * View class for the pre-lobby view.
 */
class PreLobbyView extends BaseView {

    _initialize() {
        userID.innerHTML = this.viewId;
        this.showGamesList();
    }

    _subscribeAll() {
        this.subscribe(this.model.id, "game-created", this.gameCreationHandler);
    }

    /**
     * Shows the list of games.
     */
    showGamesList() {
        this.list = this._addObjectToHTML('ul', "", gamesListContainer);
        this.model.gamesList.forEach(game => this.addListItem(game.id));
        this.addListItem(CREATE_NEW_GAME_STR);
    }

    /**
     * Adds a list item to the games list.
     * @param {*} gameId the game id
     */
    addListItem(gameId) {
        const item = this._addObjectToHTML('li', "", this.list);
        item.textContent = this.parseNameId(gameId);
        item.addEventListener('click', async () => await this.#handleJoinCreateGame(gameId));
    }

    parseNameId(gameId) {
        if (gameId === CREATE_NEW_GAME_STR) return CREATE_NEW_GAME_STR;
        return "Game " + (gameId.slice(1) - 1);
    }

    async #handleJoinCreateGame(gameId) {
        if (gameId == CREATE_NEW_GAME_STR) {
            gamesListContainer.textContent = 'Creating game...';
            const newGrid = await this.model.getNewSudoku();
            this.publish(this.model.id, "create-game", { creator: this.viewId, ...newGrid });
        } else {
            this.gameCreationHandler({ game: gameId, creator: this.viewId });
        }
    }

    /**
     * Handles the creation of a game as response to the 'game-created' event.
     * @param {*} data the game and the creator
     */
    gameCreationHandler(data) {
        const game = data.game;
        const creator = data.creator;
        if (creator === this.viewId) {
            gamesListContainer.textContent = '';
            new GameView({ model: this.model.gamesList.find(g => g.id === game) });
            this.detach();
        } else {
            this.addListItem(game);
        }
    }

    _gameOver(game) {
        for (let elem of this.list.children) {
            if (elem.textContent === game) {
                this.list.removeChild(elem);
                break;
            }
        }
    }
}

export { PreLobbyView };