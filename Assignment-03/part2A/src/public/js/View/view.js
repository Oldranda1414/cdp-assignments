import { BaseView } from "../BaseView.js";
import { GameView } from "./GameView.js";

const CREATE_NEW_GAME_STR = 'Create new game';

class PreLobbyView extends BaseView {

    _initialize() {
        userID.innerHTML = this.viewId;
        this.showGamesList();
    }

    _subscribeAll() {
        this.subscribe(this.model.id, "game-created", this.gameCreationHandler);
    }

    showGamesList() {
        this.list = this._addObjectToHTML('ul', "", gamesListContainer);
        this.model.gamesList.forEach(game => this.addListItem(game.id));
        this.addListItem(CREATE_NEW_GAME_STR);
    }

    addListItem(game) {
        const item = this._addObjectToHTML('li', "", this.list);
        item.textContent = game;
        item.addEventListener('click', async () => await this.handleJoinCreateGame(game));
    }

    async handleJoinCreateGame(game) {
        if (game == CREATE_NEW_GAME_STR) {
            gamesListContainer.textContent = 'Creating game...';
            const newGrid = await this.model.getNewSudoku();
            this.publish(this.model.id, "create-game", { creator: this.viewId, ...newGrid });
        } else {
            this.gameCreationHandler({ game: game, creator: this.viewId });
        }
    }

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