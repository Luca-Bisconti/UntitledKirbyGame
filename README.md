# Untitled Kirby Game

## Descrizione

Framework modulare per lo sviluppo di giochi 2D in Java, con particolare attenzione alla pixel art, accompagnato da una demo interattiva basata su Kirby.

L'architettura è progettata per favorire l'estendibilità e la separazione tra logica di gioco e backend grafico.

Tra le funzionalità implementate:

- Scene Graph gerarchico e sistema di componenti.
- Gestione dell'input, delle animazioni e delle trasformazioni.
- Camera, frustum culling e parallasse.
- Scriptable Rendering Pipeline con render target multipli.
- Post-processing con effetto bloom.
- Backend grafico JavaFX.

## Compilazione ed esecuzione

Dalla directory principale del progetto:

**Compilazione e test**

```bash
./gradlew build
```

**Avvio della demo**

```bash
./gradlew run
```
# Dichiarazione sull'utilizzo dell'Intelligenza Artificiale

Durante lo sviluppo del progetto sono stati utilizzati strumenti di Intelligenza Artificiale generativa come supporto alle attività di programmazione, debugging e documentazione.

In particolare, l'AI è stata impiegata nei seguenti ambiti:

### Revisione e correzione degli algoritmi

Supporto nell'analisi, nella verifica e nella correzione di alcuni algoritmi matematici utilizzati dal framework, tra cui:

- **Composizione delle trasformazioni:** verifica delle operazioni matematiche per combinare posizione, rotazione e scala.
- **Trasformazioni gerarchiche:** correzione del calcolo delle trasformazioni globali dei nodi a partire dalle trasformazioni locali e da quelle ereditate dai rispettivi genitori.
- **Parallasse:** revisione dell'algoritmo per lo spostamento relativo degli elementi dello sfondo rispetto alla camera.

### Debugging

Supporto nell'individuazione e nella risoluzione di errori di implementazione, attraverso l'analisi del codice e la proposta di possibili correzioni.

### Documentazione

Supporto nella stesura, revisione e organizzazione della documentazione tecnica del framework.

