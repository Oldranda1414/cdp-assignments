import { PreLobbyView } from "./View/view.js";

/**
 * An empty, always on view that is the parent of all other views.
 * Useful to call automatically update on all views.
 */
class FirstView extends Croquet.View {

    /**
     * All views created by this view must be placed here.
     */
    children = [];

    constructor(data) {
        super(data);
        this.subscribe(this.viewId, "view-created", this.viewCreated);
        this.subscribe(this.viewId, "view-dropped", this.viewDropped);
        new PreLobbyView({model: data});
    }

    viewCreated(view) {
        console.log("view created");
        this.children.push(view);
    }

    viewDropped(view) {
        console.log("view dropped");
        this.children.splice(this.children.indexOf(view), 1);
    }

    update(data) {
        this.children?.forEach(c => c.update(data));
    }
}

export { FirstView };