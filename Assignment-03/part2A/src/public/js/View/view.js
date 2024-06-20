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

    gameCreationHandler(data) {
        const game = data.game;
        const creator = data.creator;
        if (creator === this.viewId) {
            new GameView({ model: this.model.gamesList.find(g => g.id === game) });
            this.detach();
        } else {
            this.addListItem(game);
        }
    }

    showGamesList() {
        this.list = this._addObjectToHTML('ul', "", gamesListContainer);
        this.model.gamesList.forEach(game => this.addListItem(game.id));
        this.addListItem(CREATE_NEW_GAME_STR);
    }

    addListItem(game) {
        const item = this._addObjectToHTML('li', "", this.list);
        item.textContent = game;
        item.addEventListener('click', () => this.handleJoinCreateGame(game));
    }

    handleJoinCreateGame(game) {
        if (game == CREATE_NEW_GAME_STR) {
            this.publish(this.model.id, "create-game", this.viewId);
        } else {
            this.gameCreationHandler({ game: game, creator: this.viewId });
        }
    }
}

export { PreLobbyView };