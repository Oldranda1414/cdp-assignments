/**
 * Custom base class that will be extended from all models in this project. It provides a basic API to support debugging and some useful methods.
 */
class BaseModel extends Croquet.Model {

    /**
     * Automatically called by Croquet when the model is instantiated. It initializes the model.
     * @param {*} data 
     */
    init(data) {
        this._log("init");
        this.parent = data?.parent;
        this.subscribe(this.sessionId, "game-over", this._gameOver);
        this._subscribeAll();
        this._initialize(data);
        super.init();
    }

    /**
     * The method that should be overridden to subscribe to all the events.
     */
    _subscribeAll() {}

    /**
     * The method that should be overridden to initialize the model.
     */
    _initialize() {}

    /**
     * The method that should be overridden to handle the game over event.
     */
    _gameOver() {}

    /**
     * Classic logger for debugging.
     * @param {*} message the message to log
     */
    _log(message) {
        console.log(this.constructor.name.toUpperCase() + " | " + this.id.substring(this.id.length - 2) + ": " + message);
    }
}

export { BaseModel };