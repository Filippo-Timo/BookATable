# BookATable

REST API for restaurant table booking, built with Spring Boot

---

## Descrizione

BookATable è una REST API fullstack per la prenotazione di tavoli nei ristoranti.  
Gli utenti possono scoprire ristoranti nella propria città, visualizzarne il menu, lasciare recensioni e prenotare un
tavolo in pochi click.  
I ristoratori possono registrarsi, gestire il proprio ristorante e visualizzare le prenotazioni ricevute.

---

## Features

### Utente

- Registrazione e login con autenticazione JWT
- Visualizzazione ristoranti nella propria città
- Ricerca ristoranti per città e tipologia
- Prenotazione tavolo (data, orario, numero coperti, preferenza interno/esterno)
- Gestione prenotazioni (visualizza, cancella)
- Scrittura recensioni con valutazione

### Ristoratore

- Registrazione e login
- Creazione e gestione del proprio ristorante
- Gestione menu e piatti
- Visualizzazione prenotazioni ricevute

---

## Endpoints principali

### Auth

| Method | Endpoint             | Descrizione                      |
|--------|----------------------|----------------------------------|
| POST   | `/api/auth/register` | Registrazione utente/ristoratore |
| POST   | `/api/auth/login`    | Login e ottenimento token JWT    |

### Ristoranti

| Method | Endpoint                           | Descrizione                             |
|--------|------------------------------------|-----------------------------------------|
| GET    | `/api/ristoranti`                  | Lista ristoranti (città utente loggato) |
| GET    | `/api/ristoranti/cerca?città=Roma` | Cerca ristoranti per città              |
| GET    | `/api/ristoranti/{id}`             | Dettaglio ristorante                    |
| POST   | `/api/ristoranti`                  | Crea ristorante (solo RISTORATORE)      |
| PUT    | `/api/ristoranti/{id}`             | Modifica ristorante (solo RISTORATORE)  |
| DELETE | `/api/ristoranti/{id}`             | Elimina ristorante (solo RISTORATORE)   |

### Prenotazioni

| Method | Endpoint                 | Descrizione           |
|--------|--------------------------|-----------------------|
| GET    | `/api/prenotazioni`      | Le mie prenotazioni   |
| POST   | `/api/prenotazioni`      | Crea prenotazione     |
| DELETE | `/api/prenotazioni/{id}` | Cancella prenotazione |

### Recensioni

| Method | Endpoint                          | Descrizione                 |
|--------|-----------------------------------|-----------------------------|
| GET    | `/api/ristoranti/{id}/recensioni` | Recensioni di un ristorante |
| POST   | `/api/ristoranti/{id}/recensioni` | Scrivi recensione           |
| DELETE | `/api/recensioni/{id}`            | Elimina recensione          |

### Menu & Piatti

| Method | Endpoint                    | Descrizione                        |
|--------|-----------------------------|------------------------------------|
| GET    | `/api/ristoranti/{id}/menu` | Menu del ristorante                |
| POST   | `/api/ristoranti/{id}/menu` | Crea menu (solo RISTORATORE)       |
| POST   | `/api/menu/{id}/piatti`     | Aggiungi piatto (solo RISTORATORE) |
| DELETE | `/api/piatti/{id}`          | Elimina piatto (solo RISTORATORE)  |

---

## Autore

**Filippo Timo**  
[![GitHub](https://img.shields.io/badge/GitHub-filippotimo-black?style=flat-square&logo=github)](https://github.com/Filippo-Timo)

---

## Licenza

Questo progetto è sviluppato a scopo didattico.