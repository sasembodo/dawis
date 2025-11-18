# Worker API Spec

## Create Worker

Endpoint : POST /api/workers

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "John Doe",
  "position" : "Tukang Kasar",
  "wage" : 100000
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id" : "random-string",
    "name" : "John Doe",
    "nip" : "DES-240001",
    "recruitDate" : "format-tanggal",
    "position" : "Tukang Kasar",
    "wage" : 100000
  }
}
```

## Get Worker

Endpoint : GET /api/workers/{idWorker}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id": "random-string",
    "name": "John Doe",
    "nip" : "DES-240001",
    "recruitDate" : "format-tanggal",
    "position": "Tukang Kasar",
    "wage": 100000,
    "details": [
      {
        "date": "format-tanggal",
        "projectCode": "BGN/001/dd-mm-yy",
        "project": "Pembuatan salurab irigasi di Ds. Bangsri",
        "mandays": 2,
        "bonuses": 20000,
        "advances": 10000,
        "pay": "UNPAID"
      },{
        "date": "format-tanggal",
        "projectCode": "BGN/001/dd-mm-yy",
        "project": "Pembuatan salurab irigasi di Ds. Bangsri",
        "mandays": 2,
        "bonuses": 10000,
        "advances": 10000,
        "pay": "UNPAID"
      },{
        "date": "format-tanggal",
        "projectCode": "BGN/001/dd-mm-yy",
        "project": "Pembuatan salurab irigasi di Ds. Bangsri",
        "mandays": 2,
        "bonuses": 20000,
        "advances": 10000,
        "pay": "UNPAID"
      }
    ],
    "totalWages": 600000,
    "totalMandays": 6,
    "totalBonuses": 50000,
    "totalAdvances": 30000,
    "earnings": 620000
  }
}
```

Response Body (Failed, 404) :

```json
{
  "rc": 404,
  "messages" : "Employee is not found"
}
```

## Search Worker

Endpoint : GET /api/workers

Query Param :

- name : String, employee name, using like query, optional
- position : String, employee position, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": [
    {
      "id": "random-string",
      "name": "John Doe",
      "nip" : "DES-240001",
      "recruitDate" : "format-tanggal",
      "position": "Tukang Kasar",
      "wage": 100000,
      "totalWages": 600000,
      "totalMandays": 6,
      "totalBonuses": 50000,
      "totalAdvances": 30000,
      "earnings": 620000
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
  }
}
```

Response Body (Failed) :

```json
{
  "rc": 401,
  "messages" : "Unauthorized"
}
```

## Update Worker

Endpoint : PATCH /api/workers/{idWorker}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "John Doe",
  "position" : "Tukang Halus",
  "wage" : 120000
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id" : "random-string",
    "name" : "John Doe",
    "nip" : "DES-240001",
    "recruitDate" : "format-tanggal",
    "position" : "Tukang Halus",
    "wage" : 120000
  }
}
```

Response Body (Failed) :

```json
{
  "rc": 400,
  "messages" : "Bad Request"
}
```

## Remove Worker

Endpoint : DELETE /api/workers/{idWorker}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages" : "Data pekerja tersebut telah terhapus!"
}
```

Response Body (Failed) :

```json
{
  "rc": 404,
  "messages" : "Employee is not found!"
}
```