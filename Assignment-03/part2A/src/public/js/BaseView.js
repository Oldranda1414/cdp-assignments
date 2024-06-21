/**
 * The base view class. This class must be extended by all views in this project. It provides a basic API to support debugging and some useful methods.
 */
class BaseView extends Croquet.View {

    /**
     * All objects created by this view are placed here.
     */
    viewObjects = [];

    constructor(data) {
        super(data.model);
        this.model = data.model;
        this.subscribe(this.sessionId, "game-over", this._gameOver);
        this._subscribeAll();
        this._initialize(data);
        this.publish(this.viewId, "view-created", this);
    }

    /**
     * The method that should be overridden to subscribe to all the events.
     */
    _subscribeAll() {}

    /**
     * The method that should be overridden to initialize the view.
     */
    _initialize() {}

    /**
     * The method that should be overridden to handle the game over event.
     */
    _gameOver() {}

    /**
     * Automatically called by Croquet when the view is destroyed. It removes all the objects created by this view.
     */
    detach() {
        for (let obj of this.viewObjects) {
            obj.remove();
        }
        this.viewObjects = [];
        this.publish(this.viewId, "view-dropped", this);
        super.detach();
    }

    /**
     * A method to add elements to the HTML. This method ensures that those elements are removed when the view is destroyed.
     * @param {*} tag the element tag
     * @param {*} id the element id
     * @param {*} parent the parent element
     * @returns the element created
     */
    _addObjectToHTML(tag, id, parent) {
        var obj = document.createElement(tag);
        obj.id = id;
        parent.appendChild(obj);
        this.viewObjects.push(obj);
        return obj;
    }

    /**
     * Classic logger for debugging.
     * @param {*} message the message to log
     */
    _log(message) {
        console.log(this.constructor.name.toUpperCase() + ": " + message);
    }
}

export { BaseView };