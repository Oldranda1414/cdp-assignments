import { BaseView } from "../BaseView.js";
import { GameView } from "./GameView.js";

const canvas = document.getElementById("renderCanvas"); 

class RootView extends BaseView {
    
    _initialize() {
        this._log("This view is " + this.viewId);
    }

    _gameOver() {
    }
}

export { RootView };