## Tutto il pippone su Croquet
Qui lo risparmio anche se probabilmente andrà messo nella doc

## Architettura del programma
Ci sono due componenti principali:
- PreLobby
- Game

Questi due moduli si dividono in model e view:
- PreLobbyModel -> contiene i dati relativi alla prelobby
- PreLobbyView -> UI per la prelobby
- GameModel -> contiene i dati relativi al gioco (sudoku)
- GameView -> UI per il gioco

### I dati del model
1. Prelobby
    - lista degli utenti (non credo di utilizzarla)
    - lista delle partite (Game) aperte
2. Game
    - valore corrente della griglia
    - soluzione della griglia
    - difficoltà
    - lista dei cursori dei vari utenti

### Eventi publish/subscribe
DISCLAIMER: avrete notato se mi avete conosciuto un po' che dare i nomi alle variabili/classi etc non è il mio forte quindi sono aperto a suggerimenti ahahah

Dato che si tratta di Croquet, tutte le funzionalità di sola lettura non necessitano di una publish/subscribe, rendendo più veloce l'applicazione e leggibile il codice.  
Infatti per creare la lista di partite attualmente attive legge semplicemente da model i valori correnti all'interno della lista relativa.  

Quando l'utente clicca su una partita...
1. Istanzia la game view con il model corretto associato (sempre ottenuto leggendo nella corrispondente lista del model)
2. Distrugge la view di prelobby (obv)
3. Entra in Game ma ne parlo dopo

Quando l'utente clicca 'new game'...
1. Computa il nuovo sudoku (volevo farlo fare al model ma non avrebbe funzionato perchè Croquet non funziona così)
2. Lancia l'evento 'create-game' sottoscritto unicamente dal model di prelobby a cui passa il sudoku appena calcolato e lo user Id
3. Alla ricezione di 'create-game' i model di PreLobby creano una nuova istanza di GameModel con i valori ricevuti e aggiungono questa istanza alla lista dei Game
4. Sempre i PreLobbyModel lanciano quindi un evento 'game-created' in cui passa l'id del nuovo Game e l'utente che lo ha creato
5. le view sono in ascolto dell'evento 'game-created' e  
    5.1. se si tratta del creatore entra direttamente in partita
    5.2. se no aggiunge un elemento alla lista

Una volta all'interno di un Game ci sono 3 principali azioni:
- focus
- blur
- inserimento di un valore

Ognuna di queste 3 azioni è gestita con 2 eventi:
- 'cell-focus' / 'cell-focused'
- 'cell-blur' / 'cell-blurred'
- 'cell-value' / 'cell-valued'

Come funziona:
Quando l'utente clicca su una cella lancia l'evento 'cell-focus'
1. Il model associato riceve l'evento e aggiorna la lista dei puntatori degli utenti
2. Sempre il model quindi rilancia un evento 'cell-focused'
3.  