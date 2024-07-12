import { PreLobbyView } from "./View/view.js";

/**
 * An empty, always on view that is the parent of all other views.
 * Useful to call automatically update on all views.
 */
class FirstView extends Croquet.View {

    /**
     * All views created by this view are placed here.
     */
    children = [];

    constructor(data) {
        super(data);
        this.subscribe(this.viewId, "view-created", this.#viewCreated);
        this.subscribe(this.viewId, "view-dropped", this.#viewDropped);
        new PreLobbyView({ model: data });
    }

    #viewCreated(view) {
        this.children.push(view);
    }

    #viewDropped(view) {
        this.children.splice(this.children.indexOf(view), 1);
    }

    /**
     * Automatically called by Croquet each frame. Since this method is called only to the first view passed to the croquet session this method 
     * is responsible for calling the update method on all other views.
     * @param {*} data information related to the update
     */
    update(data) {
        this.children?.forEach(c => c.update(data));
    }
}

export { FirstView };