# Esame di Computazione Pervasiva - BeeSafe
<img src="https://user-images.githubusercontent.com/74026278/159058562-e24a8ce3-1d4d-4ca7-84f3-a9304b0ddb0f.png" width="500">

## Organizzazione

L'organizzazione dell'esame e delle relative lezioni è gestita tramite **Notion**, visualizzabile interamente al seguente [link](https://giulianoranauro.notion.site/Computazione-Pervasiva-6911e5ee644c4fd681840085c053923e).

**Per l'esecuzione dell'applicazione USARE API 29**, l'applicazione è ottimizzata e bug free solo per questa versione!

## Provenienza della repository

Questa repository è un clone della repository originale **privata** usata per caricare il codice dell'applicazione portata all'esame di **Computazione Pervasiva**. 

Per questo motivo **l'applicazione presente nella repository non è funzionante** (ma la release sottoforma di **apk** sì, quindi qualora si volesse provare l'applicazione è possibile scaricare l'apk dalle releases).

Il motivo per cui l'app non è funzionante è semplicemente riconducibile al fatto che nel progetto originale erano presenti delle API Keys per **Google Firebase**, che in questo clone sono state rimosse.

La **Documentazione** del sistema è interamente riportata quì sotto :arrow_down:



# Table of contents

- [Esame di Computazione Pervasiva](#esame-di-computazione-pervasiva)
  - [Organizzazione](#organizzazione)
- [BeeSafe](#beesafe)
  - [App Demo - Utente Base](#app-demo---utente-base)
  - [App Demo - Amministratore](#app-demo---amministratore)
  - [Miscellaneous](#miscellaneous)
- [Backend](#backend)
  - [Requisiti](#requisiti)
  - [Architettura adottata](#architettura-adottata)
  - [Autenticazione](#autenticazione)
- [Server REST](#server-rest)
  - [findReports](#findreports)
  - [addReport](#addreport)
  - [findReportByKindOfProblem](#findreportbykindofproblem)
  - [filterReportsByGravity](#filterreportsbygravity)
  - [filterReportsByUrgency](#filterreportsbyurgency)
  - [Schemi](#schemi)

# BeeSafe

## App Demo - Utente Base

### Registrazione di un utente

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/register_user/register.gif?raw=true" width="300">

Per registrare un utente l'app contatta sia il database Firebase sia l'autenticazione Firebase; quando si conferma la registrazione, viene aggiunto un utente sia nel realtime database, sia nell'auth Firebase.

### Login di un utente

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/login_user/login.gif?raw=true" width="300">

Quando un utente effettua l'accesso, viene contattato il database **MongoDB** per controllare se sono presenti dei reports non ancora scaricati in locale; questi vengono poi mostrati nella home. Le immagini sono inizialmente nascoste (per proteggere gli utenti da contenuti espliciti), ma basta un tap su quest'ultime per poterle visualizzare o nascondere.

### Aggiunta di un report

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/add_report/add_report.gif?raw=true" width="300">

Gli utenti di base possono aggiungere dei report che vengono poi visualizzati nella home. Quando si conferma l'aggiunta di un report, i dati del report vengono sia salvati in locale, che inviati al server REST per essere salvati sul database MongoDB.

Ho pensato che l'utente voglia inserire quanto meno dati possibile, per questo motivo alcuni dei dati importanti del report vengono prelevati automaticamente:

- Posizione corrente
- Numero di telefono
- Data ed ora

### Focus view di un report

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/report_focus_view/report_focus.gif?raw=true" width="300">

Effettuando un tap su di un report presente nella home, viene aperta una view dove vengono mostrate tutte le informazioni relative a quel report

### Visualizzare un report sulle mappe

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/map_view_user/map_view_user.gif?raw=true" width="300">

E' possibile visualizzare un report sulle mappe. Sono presenti due buttons che permettono di effettuare uno zoom controllato sul report e di riportare la mappa alla posizione originale.

### Profilo e logout

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/user/logout_and_profile/logout_and_profile.gif?raw=true" width="300">

Effettuando un tap sull'icona rossa in basso a sinistra presente nella home, è possibile effettuare il logout dall'app o visualizzare il proprio profilo.

All'interno del profilo vengono visualizzate le informazioni di base come Nome dell'utente ed email, come alcune statistiche relative ai reports creati. Sempre in questa view è possibile cambiare la propria password o effettuare il logout.

## App Demo - Amministratore

L'amministratore non può registrarsi all'applicazione, ma deve essere "scelto" da chi gestisce il sistema. Egli non è altro che un utente base, ma che è identificato come **ADM** invece che come **USR**. Ho strutturato il sistema backend in modo tale da poter avere quanti amministratore servano, e sopratutto, "nominare" un amministratore è abbastanza semplice: basta modificare, nel database degli utenti firebase (non autenticazione) l'identificativo da USR ad ADM.

### Login

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/admin_login/admin_login.gif?raw=true" width="300">

Quando un amministratore effettua il login, si ritrova una homepage leggermente diversa da quella dell'utente base:

- Non è più presente la possibilità di caricare reports (oltre alla mancanza fisica del button, ci sono delle protezioni software che impediscono l'aggiunta del report.)
- Sono presenti 4 icone che prima erano nascoste:
  - Visualizzazione di **tutti** i reports nelle mappe
  - Ordinamento per urgenza
  - Ordinamento per gravità
  - Filtro per tipo di problema

### Visualizzazione dei reports sulla mappa

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/all_reports_map/all_reports_map.gif?raw=true" width="300">

Effettuando un tap sull'icona delle mappe in basso a sinistra, è possibile aprire una view di Google Maps che ci consente di visualizzare tutti i report scaricati con l'ultima richiesta (quindi anche in questo caso è possibile filtrare ed ordinare). E' possibile anche scorrere in maniera controllata tra tutti i report visualizzati ed effettuare un focus su 3 livelli. 

Effettuando un tap sul **marker** del report, è possibile aprire una visualizzazione estesa del report stesso.

### Ordinamento e filtri

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/order_and_filter/order_and_filter.gif?raw=true" width="300">

E' possibile ordinare per **Urgenza, Gravità** e **filtrare** per i diversi tipi di problema previsti. Quando apriamo la mappa dopo aver applicato un ordinamento o filtro, questa visualizzerà i reports nell'ordine e filtro scelto.

E' possibile azzerare filtri ed ordinamento con l'icona di refresh in alto a destra.

### Chiamare l'autore del report

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/call_report_author/call_report_author.gif?raw=true" width="300">

Quando ci troviamo nella visualizzazione estesa di un report, è possibile visualizzarlo singolarmente sulla mappa (vengono nascoste le icone per scorrere tra i reports) o chiamare l'autore direttamente effettuando un tap sul numero di telefono visualizzato.



## Logs

### Visualizzazione dei logs

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/admin_view_logs/view_logs.gif?raw=true" width="300">

E' possibile accedere all'activity dei Logs tramite il menu a comparsa che viene visualizzato quando si effettua un tap sull'icona rossa in basso a sinistra; questa operazione è disponibile solo agli amministratori.

### Logs - Filtri

#### Ricerca completa

<img src="search_full_email.gif](https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/search_full_email_logs/search_full_email.gif?raw=true" width="300">

E' possibile effettuare la ricerca (o filtraggio) dei logs tramite l'email di chi ha effettuato l'operazione. Il filtro viene effettuato tramite una HashMap, quindi molto ottimizzato e veloce, anche con molti risultati. 

#### Ricerca parziale

<img src="search_all_ways.gif](https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/search_all_ways/search_all_ways.gif" width="300">

E' possibile effettuare ricerche / filtraggi anche in maniera parziale; basterà inserire una lettera, sequenza di lettere, o addirittura dominii per ottenere dei risultati.

Questa operazione, purtroppo è effettuata _the old fashioned way_, tramite un algoritmo che viene eseguito solo quando la ricerca completa non ha ottenuto risultati.

La computazione avviene in locale, quindi i server non verranno sovraccaricati.

### I risultati

Come sappiamo, in contesti reali vengono eseguite svariate richieste al secondo ad un servizio REST, ed è per questo motivo che vengono visualizzati sempre e solo gli ultimi 100 logs effettuati.

## Miscellaneous

### Assenza di connessione internet

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/no_internet/no_internet.gif?raw=true" width="300">

Se il dispositivo non è connesso ad internet, l'applicazione informa l'utente tramite un'icona rossa in alto; effettuando un tap su quest'ultima è possibile visualizzare un messaggio informativo.

### Assenza di conessione al server

<img src="https://github.com/follen99/BeeSafe/blob/main/Resources/AppDemos/admin/no_cloud/no_cloud.gif?raw=true" width="300">

Quando il dispositivo non riesce a connettersi al servizio REST, l'applicazione informa l'utente tramite un'icona rossa in alto; effettuando un tap su quest'ultima è possibile visualizzare un messaggio informativo.

L'applicazione effettua vari tentativi di connessione con un timeout esteso, e se dopo tutti questi tentativi di connessione non riesce ad avere una risposta, allora visualizzerà l'icona.

# Backend

## Requisiti

Sviluppare un sistema client-server per la richiesta di intervento da parte del pronto soccorso o delle forze dell'ordine.  

Ogni richiesta è accompagnata dai seguenti metadati:

- Nome del luogo    
- Una breve descrizione del problema (max 100 Caratteri) opzionale    
- Tipologia di problema  ( problema di salute , furto , atti di violenza )    
- Gravità (da 1 a 10) 
- Urgenza ( da 1 =poco urgente a 10 =critica )    
- Una o più immagini opzionali    
- La posizione geografica    
- Data e ora della segnalazione    
- Un numero di telefono opzionale da contattare da parte dell'ente interessato (pronto soccorso, forze dell'ordine)

L'applicazione mobile (da creare con l'Android SDK o il framework Flutter) deve permettere le operazioni essenziali (registrazione, login, invio segnalazione, elenco segnalazioni).  Oltre agli utenti che possono registrarsi normalmente, l'applicazione mobile prevede un utente amministratore predefinito che non puo' inviare richieste ma puo' visualizzare  tutte le richieste di tutti gli utenti ricercandole su mappa o in una lista per gravità, urgenza filtrando per tipologia.

Il servizio di backend implementa un'API rest e può essere realizzato utilizzando Spring, Vert.X o Firebase a scelta. Le funzionalità sono le seguenti:    

- Registrazione di un nuovo utente    
- Login    
- Upload di una richiesta con i relativi metadati di segnalazione (previo login)    
- Elenco delle richieste effettuate (previo login di utente amministratore)

## Architettura adottata

Per la realizzazione dell'applicazione finale ho deciso di utilizzare **l'Android SDK**; l'applicazione implementa tutte le funzionalità richieste, a seconda se l'utente loggato è un utente base o amministratore. L'applicazione effettua il cacheing dei reports su un database locale gestito tramite **Room**.

Per quanto riguarda il servizio backend, ho deciso di adottare sia **Firebase**, per la gestione degli **utenti**, **autenticazione** e delle **immagini** caricate, sia un server **REST** realizzato con **VERT.X** per l'implementazione di API REST che permettono di aggiungere e recuperare i reports.

I reports vengono salvati all'interno di un database remoto **MongoDB**; ho scelto un database di tipo **NoSQL** per via del fatto che l'applicazione in sè è abbastanza piccola, inoltre non ho un elevato bisogno di relazione tra le varie entità (infatti su MongoDB sono presenti solo i reports).

![](https://github.com/follen99/BeeSafe/blob/main/Resources/ThinkingProcess/app_overview.JPG?raw=true)

## Autenticazione

![](https://github.com/follen99/BeeSafe/blob/main/Resources/ThinkingProcess/auth.JPG?raw=true)

### Invio della richiesta

Quando l'utente vuole effettuare il login, firebase si occupa dell'invio dei dati e della loro convalida. Per quanto riguarda l'autenticazione al server REST che contatta il database MongoDB, invece, l'autenticazione è stata implementata da zero seguendo il ragionamento seguente:

Quando l'utente invia una richiesta HTTP al server REST, insieme al body (presente nelle richieste di tipo POST), viene inviato un **header** contenente: 

- **Digest**: il Digest viene calcolato tramite un **Segreto comune** sia al server che al client, che viene concatenato alla **password** dell'utente. Di questa stringa risultante, viene calcolato l'**MD5**.
- **Uid**: L'Uid viene generato da **Firebase** ed è un codice univoco appartente ad ogni utente.

Ogni richiesta, quindi, contiene all'interno dell'header queste due stringhe, e se la richiesta è di tipo POST, conterrà anche un body.

### Ricezione della richiesta

Il server riceve la richiesta, ne estrae il contenuto (body - se presente, uid e digest) ed effettua delle verifiche:

1. Contatta il database firebase e, una volta calcolato il digest in locale lo confronta con quello ricevuto dal client.
2. Se i digest combaciano, l'utente è **autenticato**.
3. Dopo aver autenticato l'utente, il server controlla se l'utente è **autorizzato**.
4. Se l'utente è anche autorizzato ad effettuare quella specifica richiesta, allora la richiesta viene soddisfatta, inviando una risposta.

## Logs

I logs sono uno strumento di fondamentale importanza nei sistemi reali, utili ad uno svariato numero di operazioni. In questa applicazione, ho provato ad implementare una soluzione basilare per la gestione dei Logs.

Ogni volta che un utente effettua un'operazione (che sia di lettura o scrittura), viene salvato sul realtime database Firebase un log che ne descrive:

| Nnome variabile |                    Descrizione variabile                     |
| :-------------: | :----------------------------------------------------------: |
|    timestamp    | Il tempo espresso in millisecondi dell'esecuzione della richiesta; questo valore verrà trasformato in-app in una data **human friendly**. |
|      user       |      l'email dell'utente che ha effettuato l'operazione      |
|   requestTyp    |             il tipo di richiesta: e.g. GET, POST             |
|    operation    |       Il tipo di operazione effettuata, e.g. READ_ALL        |
|     outcome     |          Il risultato dell'operazione, e.g. SUCCESS          |
|     isAdmin     | se l'utente che ha effettuato l'operazione è un amministratore |

### Architettura Logs

![Logs .JPG](https://github.com/follen99/BeeSafe/blob/main/Resources/ThinkingProcess/Logs%20.JPG?raw=true)

# Server REST

Il servizio REST è realizzato con VERT.X ed (dovrebbe - in corso d'opera) è eseguito su un server AWS tramite **LightSail**. Il server REST serve da tramite tra l'applicazione che l'utente utilizza ed il **Database remoto MongoDB**. La scelta architetturale di utilizzare una scelta combinata di Server REST / Firebase database / Firebase auth / database locale non era per nulla necessaria nè richiesta, ma ho voluto realizzare l'architettura backend in questo modo per via del fatto che non avevo mai provato il framework VERT.X, e volevo che il sistema comprendesse quanti più insegnamenti provenienti dal corso possibile.

E' possibile trovare la documentazione completa a questo [link](https://github.com/unisannio-pervasive-computing-2021/summer_session_assignment-follen99/blob/main/Resources/REST_documentation/REST_documentation.md).



## findReports

<a id="opIdfindReports"></a>

> Code samples

```shell
# E' possibile usare anche wget
curl -X GET /reports \
  -H 'Accept: application/json' \
  -H 'uid: string' \
  -H 'digest: string'

```

```http
GET /reports HTTP/1.1

Accept: application/json
uid: string
digest: string

```

`GET /reports`

ritorna tutti i reports al quale l'user ha accesso

### Parametri

| Name   | In     | Type   | Required | Description                                                  |
| ------ | ------ | ------ | -------- | ------------------------------------------------------------ |
| uid    | header | string | true     | l'id relativo all'utente che ha effettuato la richiesta      |
| digest | header | string | true     | il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> report response

```json
[
  {
    "longitude": 29.95,
    "description": "some text",
    "gravity": 29,
    "latitude": 9.16,
    "phoneNumber": 77,
    "urgency": 15,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 32,
    "kindOfProblem": "some text"
  },
  {
    "longitude": 91.79,
    "description": "some text",
    "gravity": 92,
    "latitude": 61.48,
    "phoneNumber": 68,
    "urgency": 28,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 31,
    "kindOfProblem": "some text"
  }
]
```

<h3 id="findreports-responses">Responses</h3>

| Status | Meaning                                                 | Description     | Schema |
| ------ | ------------------------------------------------------- | --------------- | ------ |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | report response | Inline |

## addReport

<a id="opIdaddReport"></a>

> Code samples

```shell
# You can also use wget
curl -X POST /reports \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'uid: string' \
  -H 'digest: string'

```

```http
POST /reports HTTP/1.1

Content-Type: application/json
Accept: application/json
uid: string
digest: string

```

`POST /reports`

Crea un nuovo report nel database. Sono ammessi duplicati

> Body parameter

```json
{
  "longitude": "14.0000",
  "description": "Si è divampato un incendio a partire dalla struttura principale",
  "gravity": "1",
  "latitude": "14.0000",
  "phoneNumber": "3105348309",
  "urgency": "2",
  "imagePath": "/path/to/image.jpg",
  "placeName": "Via dei mulini",
  "dateTime": "string",
  "id": "1",
  "kindOfProblem": "Violenza"
}
```

### Parametri

| Name   | In     | Type                    | Required | Description                                                  |
| ------ | ------ | ----------------------- | -------- | ------------------------------------------------------------ |
| body   | body   | [Report](#schemareport) | true     | none                                                         |
| uid    | header | string                  | true     | l'id relativo all'utente che ha effettuato la richiesta      |
| digest | header | string                  | true     | il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> Example responses

> report response

```json
{
  "longitude": 81.86,
  "description": "some text",
  "gravity": 34,
  "latitude": 39.27,
  "phoneNumber": 69,
  "urgency": 32,
  "imagePath": "some text",
  "placeName": "some text",
  "dateTime": "some text",
  "id": 33,
  "kindOfProblem": "some text"
}
```

<h3 id="addreport-responses">Responses</h3>

| Status | Meaning                                                 | Description     | Schema                  |
| ------ | ------------------------------------------------------- | --------------- | ----------------------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | report response | [Report](#schemareport) |

## findReportByKindOfProblem

<a id="opIdfindReportByKindOfProblem"></a>

> Code samples

```shell
# You can also use wget
curl -X GET /reports/{kindOfProblem} \
  -H 'Accept: application/json' \
  -H 'digest: string' \
  -H 'uid: string'

```

```http
GET /reports/{kindOfProblem} HTTP/1.1

Accept: application/json
digest: string
uid: string

```

`GET /reports/{kindOfProblem}`

Ritorna tutti i report aventi una specifica  tipologia di problema

### Parametri

| Name          | In     | Type   | Required | Description                                                  |
| ------------- | ------ | ------ | -------- | ------------------------------------------------------------ |
| kindOfProblem | path   | string | true     | Tipologia del problema                                       |
| digest        | header | string | true     | il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
| uid           | header | string | true     | l'id relativo all'utente che ha effettuato la richiesta      |

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> report response

```json
[
  {
    "longitude": 12.86,
    "description": "some text",
    "gravity": 67,
    "latitude": 2.53,
    "phoneNumber": 8,
    "urgency": 0,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 6,
    "kindOfProblem": "some text"
  },
  {
    "longitude": 91.28,
    "description": "some text",
    "gravity": 92,
    "latitude": 28.7,
    "phoneNumber": 78,
    "urgency": 57,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 90,
    "kindOfProblem": "some text"
  }
]
```

<h3 id="findreportbykindofproblem-responses">Responses</h3>

| Status | Meaning                                                 | Description     | Schema |
| ------ | ------------------------------------------------------- | --------------- | ------ |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | report response | Inline |

<h3 id="findreportbykindofproblem-responseschema">Response Schema</h3>

Status Code **200**

| Name            | Type                      | Required | Restrictions | Description                                                  |
| --------------- | ------------------------- | -------- | ------------ | ------------------------------------------------------------ |
| *anonymous*     | [[Report](#schemareport)] | false    | none         | none                                                         |
| » longitude     | number(double)            | false    | none         | longitudine della posizione geografica                       |
| » description   | string                    | false    | none         | Descrizione dell'emergenza                                   |
| » gravity       | integer                   | false    | none         | La gravità dell'emergenza                                    |
| » latitude      | number(double)            | false    | none         | latitudine della posizione geografica                        |
| » phoneNumber   | integer(int64)            | false    | none         | Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono |
| » urgency       | integer                   | false    | none         | Urgenza dell'emergenza                                       |
| » imagePath     | string                    | false    | none         | percorso all'immagine del report                             |
| » placeName     | string                    | false    | none         | Posto dove è avvenuta l'emergenza                            |
| » dateTime      | string                    | false    | none         | la data ed ora in cui è stato inviato il report              |
| » id            | integer                   | true     | none         | L'id del report                                              |
| » kindOfProblem | string                    | false    | none         | Tipo di emergenza                                            |
| » fromUser      | string                    | true     | none         | L'utente che ha inviato il report                            |

## filterReportsByGravity

<a id="opIdfilterReportsByGravity"></a>

> Code samples

```shell
# You can also use wget
curl -X GET /reports/orderby/gravity \
  -H 'Accept: application/json' \
  -H 'digest: string' \
  -H 'uid: string'

```

```http
GET /reports/orderby/gravity HTTP/1.1

Accept: application/json
digest: string
uid: string

```

> Il body di risposta è uguale alla richiesta findReportByKindOfProblem

`GET /reports/orderby/gravity`

ritorna tutti i report a cui l'utente può accedere.
I report vengono ordinati per gravità

### Parametri

| Name   | In     | Type   | Required | Description                                                  |
| ------ | ------ | ------ | -------- | ------------------------------------------------------------ |
| digest | header | string | true     | il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
| uid    | header | string | true     | l'id relativo all'utente che ha effettuato la richiesta      |

## filterReportsByUrgency

<a id="opIdfilterReportsByUrgency"></a>

> Code samples

```shell
# You can also use wget
curl -X GET /reports/orderby/urgency \
  -H 'Accept: application/json' \
  -H 'digest: string' \
  -H 'uid: string'

```

```http
GET /reports/orderby/urgency HTTP/1.1

Accept: application/json
digest: string
uid: string

```

>  Il body di risposta è uguale alla richiesta findReportByKindOfProblem

`GET /reports/orderby/urgency`

ritorna tutti i report a cui l'utente può accedere.
I report vengono ordinati per gravità

### Parametri

| Name   | In     | Type   | Required | Description                                                  |
| ------ | ------ | ------ | -------- | ------------------------------------------------------------ |
| digest | header | string | true     | il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
| uid    | header | string | true     | l'id relativo all'utente che ha effettuato la richiesta      |

## Schemi

<h2 id="tocS_Report">Report</h2>

<!-- backwards compatibility -->
<a id="schemareport"></a>
<a id="schema_Report"></a>
<a id="tocSreport"></a>
<a id="tocsreport"></a>

```json
{
  "longitude": "14.0000",
  "description": "Si è divampato un incendio a partire dalla struttura principale",
  "gravity": "1",
  "latitude": "14.0000",
  "phoneNumber": "3105348309",
  "urgency": "2",
  "imagePath": "/path/to/image.jpg",
  "placeName": "Via dei mulini",
  "dateTime": "string",
  "id": "1",
  "kindOfProblem": "Violenza",
  "fromUser": "user1@gmail.com"
}

```

### Proprietà

| Name          | Type           | Required | Restrictions  | Description                                                  |
| ------------- | -------------- | -------- | ------------- | ------------------------------------------------------------ |
| longitude     | number(double) | true     | none          | longitudine della posizione geografica                       |
| description   | string         | true     | max 100 chars | Descrizione dell'emergenza                                   |
| gravity       | integer        | true     | none          | La gravità dell'emergenza                                    |
| latitude      | number(double) | true     | none          | latitudine della posizione geografica                        |
| phoneNumber   | integer(int64) | false    | none          | Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono |
| urgency       | integer        | true     | none          | Urgenza dell'emergenza                                       |
| imagePath     | string         | false    | none          | percorso all'immagine del report                             |
| placeName     | string         | true     | none          | Posto dove è avvenuta l'emergenza                            |
| dateTime      | string         | true     | none          | la data ed ora in cui è stato inviato il report              |
| id            | integer        | true     | none          | L'id del report                                              |
| kindOfProblem | string         | false    | none          | Tipo di emergenza                                            |
| fromUser      | string         | true     | none          | L'utente che ha inviato il report                            |

