import { BaseView } from "../BaseView.js";

class PreLobbyView extends BaseView {
    
    _initialize() {
        this._log("This view is " + this.viewId);
        userID.innerHTML = this.viewId;
    }

    _gameOver() {
    }
}

export { PreLobbyView };