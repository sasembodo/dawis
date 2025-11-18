# Create Attendance

Endpoint : POST /api/projects/{idProject}/attendances

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "date": "format-tanggal",
  "workerId": "random-string",
  "workerName": "Joni",
  "mandays": 1
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id": "random-string",
    "date": "format-tanggal",
    "workerId": "random-string",
    "workerName": "Joni",
    "nip" : "DES-240001",
    "recruitDate" : "format-tanggal",
    "position": "Tukang Halus",
    "mandays": 1,
    "wage": 110000,
    "bonuses": 0,
    "advances": 0,
    "subtotal": 110000,
    "paidStatus": "UNPAID"
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


## Get and Filter Attendance

Endpoint : GET /api/projects/{idProject}/attendances

Query Param :

- pay : String, attendance pay, PAID || UNPAID || ALL, default UNPAID
- page : Integer, start from 0, default 0
- size : Integer, default 50

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
      "date": "format-tanggal",
      "workerId": "random-string",
      "workerName": "Joni",
      "nip" : "DES-240001",
      "recruitDate" : "format-tanggal",
      "position": "Tukang Halus",
      "mandays": 1,
      "wage": 110000,
      "bonuses": 0,
      "advances": 0,
      "subtotal": 110000,
      "paidStatus": "UNPAID"
    },{
      "id": "random-string",
      "date": "format-tanggal",
      "workerId": "random-string",
      "workerName": "Samsul",
      "nip" : "FEB-250001",
      "recruitDate" : "format-tanggal",
      "position": "Kuli Bangunan",
      "mandays": 1,
      "wage": 95000,
      "bonuses": 0,
      "advances": 0,
      "subtotal": 95000,
      "paidStatus": "UNPAID"
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


## Update Attendance

Endpoint : PATCH /api/projects/{idProject}/attendances/{idAttendance}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "id": "random-string",
  "date": "format-tanggal",
  "workerId": "random-string",
  "mandays": 2,
  "wage": 120000,
  "bonuses": 20000,
  "advances": 10000,
  "paidStatus": "PAID"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": [
    {
      "id": "random-string",
      "date": "format-tanggal",
      "workerId": "random-string",
      "workerName": "Joni",
      "nip" : "DES-240001",
      "recruitDate" : "format-tanggal",
      "position": "Tukang Halus",
      "mandays": 2,
      "wage": 120000,
      "bonuses": 20000,
      "advances": 10000,
      "subtotal": 250000,
      "pay": "PAID"
    },{
      "id": "random-string",
      "date": "format-tanggal",
      "workerId": "random-string",
      "workerName": "Samsul",
      "nip" : "FEB-250001",
      "recruitDate" : "format-tanggal",
      "position": "Kuli Bangunan",
      "mandays": 1,
      "wage": 95000,
      "bonuses": 0,
      "advances": 0,
      "pay": "UNPAID"
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
  "rc": 400,
  "messages" : "Email format invalid, phone format invalid, ..."
}
```

## Remove Attendance

Endpoint : DELETE /api/projects/{idProject}/attendances/{idAttendance}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages" : "OK"
}
```

Response Body (Failed) :

```json
{
  "rc": 404,
  "messages" : "Attendance is not found"
}
```