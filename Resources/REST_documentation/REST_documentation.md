---
title: BeeSafe v1.0.0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
highlight_theme: darkula
headingLevel: 2

---

<h1 id="beesafe">BeeSafe v1.0.0</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.

prima versione della API lato server

<h1 id="beesafe-default">Default</h1>

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

```javascript

const headers = {
  'Accept':'application/json',
  'uid':'string',
  'digest':'string'
};

fetch('/reports',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => 'application/json',
  'uid' => 'string',
  'digest' => 'string'
}

result = RestClient.get '/reports',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': 'application/json',
  'uid': 'string',
  'digest': 'string'
}

r = requests.get('/reports', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => 'application/json',
    'uid' => 'string',
    'digest' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','/reports', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("/reports");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"application/json"},
        "uid": []string{"string"},
        "digest": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "/reports", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /reports`

ritorna tutti i reports al quale l'user ha accesso

<h3 id="findreports-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|uid|header|string|true|l'id relativo all'utente che ha effettuato la richiesta|
|digest|header|string|true|il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |

#### Detailed descriptions

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> Example responses

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

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|report response|Inline|

<h3 id="findreports-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Report](#schemareport)]|false|none|none|
|» longitude|number(double)|false|none|longitudine della posizione geografica|
|» description|string|false|none|Descrizione dell'emergenza|
|» gravity|integer|false|none|La gravità dell'emergenza|
|» latitude|number(double)|false|none|latitudine della posizione geografica|
|» phoneNumber|integer(int64)|false|none|Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono|
|» urgency|integer|false|none|Urgenza dell'emergenza|
|» imagePath|string|false|none|percorso all'immagine del report|
|» placeName|string|false|none|Posto dove è avvenuta l'emergenza|
|» dateTime|string|false|none|la data ed ora in cui è stato inviato il report|
|» id|integer|true|none|L'id del report|
|» kindOfProblem|string|false|none|Tipo di emergenza|
|» fromUser|string|true|none|L'utente che ha inviato il report|

<aside class="success">
This operation does not require authentication
</aside>

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

```javascript
const inputBody = '{
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
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'uid':'string',
  'digest':'string'
};

fetch('/reports',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => 'application/json',
  'uid' => 'string',
  'digest' => 'string'
}

result = RestClient.post '/reports',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'uid': 'string',
  'digest': 'string'
}

r = requests.post('/reports', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => 'application/json',
    'uid' => 'string',
    'digest' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','/reports', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("/reports");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"application/json"},
        "uid": []string{"string"},
        "digest": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "/reports", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

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

<h3 id="addreport-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[Report](#schemareport)|true|none|
|uid|header|string|true|l'id relativo all'utente che ha effettuato la richiesta|
|digest|header|string|true|il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |

#### Detailed descriptions

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

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|report response|[Report](#schemareport)|

<aside class="success">
This operation does not require authentication
</aside>

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

```javascript

const headers = {
  'Accept':'application/json',
  'digest':'string',
  'uid':'string'
};

fetch('/reports/{kindOfProblem}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => 'application/json',
  'digest' => 'string',
  'uid' => 'string'
}

result = RestClient.get '/reports/{kindOfProblem}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': 'application/json',
  'digest': 'string',
  'uid': 'string'
}

r = requests.get('/reports/{kindOfProblem}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => 'application/json',
    'digest' => 'string',
    'uid' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','/reports/{kindOfProblem}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("/reports/{kindOfProblem}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"application/json"},
        "digest": []string{"string"},
        "uid": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "/reports/{kindOfProblem}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /reports/{kindOfProblem}`

Ritorna tutti i report aventi una specifica  tipologia di problema

<h3 id="findreportbykindofproblem-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|kindOfProblem|path|string|true|Tipologia del problema|
|digest|header|string|true|il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
|uid|header|string|true|l'id relativo all'utente che ha effettuato la richiesta|

#### Detailed descriptions

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> Example responses

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

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|report response|Inline|

<h3 id="findreportbykindofproblem-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Report](#schemareport)]|false|none|none|
|» longitude|number(double)|false|none|longitudine della posizione geografica|
|» description|string|false|none|Descrizione dell'emergenza|
|» gravity|integer|false|none|La gravità dell'emergenza|
|» latitude|number(double)|false|none|latitudine della posizione geografica|
|» phoneNumber|integer(int64)|false|none|Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono|
|» urgency|integer|false|none|Urgenza dell'emergenza|
|» imagePath|string|false|none|percorso all'immagine del report|
|» placeName|string|false|none|Posto dove è avvenuta l'emergenza|
|» dateTime|string|false|none|la data ed ora in cui è stato inviato il report|
|» id|integer|true|none|L'id del report|
|» kindOfProblem|string|false|none|Tipo di emergenza|
|» fromUser|string|true|none|L'utente che ha inviato il report|

<aside class="success">
This operation does not require authentication
</aside>

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

```javascript

const headers = {
  'Accept':'application/json',
  'digest':'string',
  'uid':'string'
};

fetch('/reports/orderby/gravity',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => 'application/json',
  'digest' => 'string',
  'uid' => 'string'
}

result = RestClient.get '/reports/orderby/gravity',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': 'application/json',
  'digest': 'string',
  'uid': 'string'
}

r = requests.get('/reports/orderby/gravity', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => 'application/json',
    'digest' => 'string',
    'uid' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','/reports/orderby/gravity', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("/reports/orderby/gravity");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"application/json"},
        "digest": []string{"string"},
        "uid": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "/reports/orderby/gravity", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /reports/orderby/gravity`

ritorna tutti i report a cui l'utente può accedere.
I report vengono ordinati per gravità

<h3 id="filterreportsbygravity-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|digest|header|string|true|il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
|uid|header|string|true|l'id relativo all'utente che ha effettuato la richiesta|

#### Detailed descriptions

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> Example responses

> report response

```json
[
  {
    "longitude": 73.42,
    "description": "some text",
    "gravity": 5,
    "latitude": 72.59,
    "phoneNumber": 70,
    "urgency": 40,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 59,
    "kindOfProblem": "some text"
  },
  {
    "longitude": 69.91,
    "description": "some text",
    "gravity": 73,
    "latitude": 43.93,
    "phoneNumber": 33,
    "urgency": 87,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 40,
    "kindOfProblem": "some text"
  }
]
```

<h3 id="filterreportsbygravity-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|report response|Inline|

<h3 id="filterreportsbygravity-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Report](#schemareport)]|false|none|none|
|» longitude|number(double)|false|none|longitudine della posizione geografica|
|» description|string|false|none|Descrizione dell'emergenza|
|» gravity|integer|false|none|La gravità dell'emergenza|
|» latitude|number(double)|false|none|latitudine della posizione geografica|
|» phoneNumber|integer(int64)|false|none|Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono|
|» urgency|integer|false|none|Urgenza dell'emergenza|
|» imagePath|string|false|none|percorso all'immagine del report|
|» placeName|string|false|none|Posto dove è avvenuta l'emergenza|
|» dateTime|string|false|none|la data ed ora in cui è stato inviato il report|
|» id|integer|true|none|L'id del report|
|» kindOfProblem|string|false|none|Tipo di emergenza|
|» fromUser|string|true|none|L'utente che ha inviato il report|

<aside class="success">
This operation does not require authentication
</aside>

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

```javascript

const headers = {
  'Accept':'application/json',
  'digest':'string',
  'uid':'string'
};

fetch('/reports/orderby/urgency',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => 'application/json',
  'digest' => 'string',
  'uid' => 'string'
}

result = RestClient.get '/reports/orderby/urgency',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': 'application/json',
  'digest': 'string',
  'uid': 'string'
}

r = requests.get('/reports/orderby/urgency', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => 'application/json',
    'digest' => 'string',
    'uid' => 'string',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','/reports/orderby/urgency', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("/reports/orderby/urgency");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"application/json"},
        "digest": []string{"string"},
        "uid": []string{"string"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "/reports/orderby/urgency", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /reports/orderby/urgency`

ritorna tutti i report a cui l'utente può accedere.
I report vengono ordinati per gravità

<h3 id="filterreportsbyurgency-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|digest|header|string|true|il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. |
|uid|header|string|true|l'id relativo all'utente che ha effettuato la richiesta|

#### Detailed descriptions

**digest**: il digest è la password dell'utente concatenata ad una stringa segreta comune sia al client che al server. 
Della stringa risultante viene calcolato l'MD5.
Se il digest inviato dal client e quello calcolato dal server coincidono l'utente è autenticato.

> Example responses

> report response

```json
[
  {
    "longitude": 72.12,
    "description": "some text",
    "gravity": 46,
    "latitude": 28.67,
    "phoneNumber": 81,
    "urgency": 56,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 85,
    "kindOfProblem": "some text"
  },
  {
    "longitude": 84.8,
    "description": "some text",
    "gravity": 8,
    "latitude": 81.03,
    "phoneNumber": 10,
    "urgency": 1,
    "imagePath": "some text",
    "placeName": "some text",
    "dateTime": "some text",
    "id": 93,
    "kindOfProblem": "some text"
  }
]
```

<h3 id="filterreportsbyurgency-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|report response|Inline|

<h3 id="filterreportsbyurgency-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Report](#schemareport)]|false|none|none|
|» longitude|number(double)|false|none|longitudine della posizione geografica|
|» description|string|false|none|Descrizione dell'emergenza|
|» gravity|integer|false|none|La gravità dell'emergenza|
|» latitude|number(double)|false|none|latitudine della posizione geografica|
|» phoneNumber|integer(int64)|false|none|Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono|
|» urgency|integer|false|none|Urgenza dell'emergenza|
|» imagePath|string|false|none|percorso all'immagine del report|
|» placeName|string|false|none|Posto dove è avvenuta l'emergenza|
|» dateTime|string|false|none|la data ed ora in cui è stato inviato il report|
|» id|integer|true|none|L'id del report|
|» kindOfProblem|string|false|none|Tipo di emergenza|
|» fromUser|string|true|none|L'utente che ha inviato il report|

<aside class="success">
This operation does not require authentication
</aside>

# Schemas

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
  "fromUser": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|longitude|number(double)|false|none|longitudine della posizione geografica|
|description|string|false|none|Descrizione dell'emergenza|
|gravity|integer|false|none|La gravità dell'emergenza|
|latitude|number(double)|false|none|latitudine della posizione geografica|
|phoneNumber|integer(int64)|false|none|Numero telefono di colui che invia il report.<br>Prelevato automaticamente dal telefono|
|urgency|integer|false|none|Urgenza dell'emergenza|
|imagePath|string|false|none|percorso all'immagine del report|
|placeName|string|false|none|Posto dove è avvenuta l'emergenza|
|dateTime|string|false|none|la data ed ora in cui è stato inviato il report|
|id|integer|true|none|L'id del report|
|kindOfProblem|string|false|none|Tipo di emergenza|
|fromUser|string|true|none|L'utente che ha inviato il report|

