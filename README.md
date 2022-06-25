# Resplan
Resplan modella una Digital Audio Workstation (DAW) specifica per contenuti vocali, quali podcast, audiolibri e audiolezioni.
L’applicazione si propone di fornire un ambiente semplice ed intuitivo nel quale l’utente potrà pianificare, registrare ed editare in tracce separate vari file audio.

## Funzionalità minimali:
- [x] Creazione di canali corrispondenti a speaker, file audio, effetti sonori o altri elementi a discrezione dell’utente.
- [x] Posizionamento nella timeline dei contenuti audio da importare o registrare per ogni canale.
- [x] Possibilità di associare testi ad ogni contenuto audio da registrare.
- [x] Suddivisione della timeline in capitoli.
- [x] Possibilità di registrare le parti corrispondenti agli oratori. I canali vengono poi sommati nel canale master.
- [x] Integrazione con tracce audio preesistenti da importare nel progetto.
- [x] Editare audio (tagliare/spostare parti nella timeline, modificare il volume).
- [x] Esportazione del canale master in un file audio.
- [x] Salvataggio del progetto su file per poterlo riaprire successivamente.

## Funzionalità opzionali:
- [x] Effetti audio nei canali e nel canale master: equalizzatore, compressore, limiter, riverbero. 
- [x] Parametrizzazione degli effetti automatizzata sulla base del tipo ad alto livello del canale su cui sono applicati.
- [x] Possibilità di impostare un progetto di default da aprire all’avvio.
- [ ] Visualizzazione in real-time del testo associato ad una parte.
- [ ] Controllo dei parametri dell’applicazione tramite interfaccia MIDI.
- [ ] Aggiunta del dithering nel file esportato.
- [ ] Mastering automatizzato.

## Run the jar! 
``` 
java -jar Resplan.jar
```

Nel file report.pdf è presente una sezione che illustra come utilizzare le varie funzionalità dell'applicazione.
