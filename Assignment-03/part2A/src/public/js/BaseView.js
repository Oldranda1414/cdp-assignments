class BaseView extends Croquet.View {

    /**
     * All views created by this view must be placed here.
     */
    children = [];

    /**
     * All objects created by this view must be placed here.
     */
    viewObjects = [];

    constructor(data) {
        if (data.hasOwnProperty("model")) {
            super(data.model);
            this.model = data.model;
        } else { //the first view initialized by croquet has only the ref to the model.
            super(data);
            this.model = data;
        }
        this._log("Created. Model associated: " + this.model.id.substring(this.model.id.length - 2));
        this.subscribe(this.sessionId, "game-over", this._gameOver);
        this._subscribeAll();   //Croquet subscription method
        this._initialize(data); //Variables init method
    }

    update(data) {
        this.children?.forEach(c => c.update(data));
        this._update(data);
    }

    _subscribeAll() {}

    _initialize() {}

    _update() {}

    _endScene() {}

    _gameOver() {}

    detach(skipForwarding = false) {
        for (let obj of this.viewObjects) obj.remove();
        super.detach();
        if (!skipForwarding) this.children.forEach(c => c.detach()); //the if is used to forward only if is croquet who calls detach
        this._log("detach");
    }

    _addObjectToHTML(tag, id, parent) {
        var obj = document.createElement(tag);
        obj.id = id;
        parent.appendChild(obj);
        this.viewObjects.push(obj);
        return obj;
    }

    _log(message) {
        console.log(this.constructor.name.toUpperCase() + ": " + message);
    }
}

export { BaseView };