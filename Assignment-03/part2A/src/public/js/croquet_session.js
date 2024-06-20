// Croquet Tutorial 1
// Hello World 
// Croquet Corporation 
// 2021

import { FirstView } from "./FirstView.js";
import { PreLobbyModel } from "./Model/model.js";

Croquet.Session.join({
    apiKey: '16F1feJe0PFsp1D4y5b1AbKjf5OhcJfmKc3hgxxkO',
    appId: 'it.unibo.studio.filippo.gurioli.microverse',
    name: "unnamed",
    password: "secret",
    model: PreLobbyModel,
    view: FirstView
});