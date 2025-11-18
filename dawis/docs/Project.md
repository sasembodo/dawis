# Project API Spec

## Create Project

Endpoint : POST /api/projects

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "project": "Pembuatan salurab irigasi di Ds. Bangsri",
  "type": "Kontruksi",
  "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
  "coordinates": "-7.175392, 114.827153",
  "status": "Work in progress",
  "startDate": "format-tanggal"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id": "random-string",
    "projectCode": "BGN/001/dd-mm-yy",
    "project": "Pembuatan salurab irigasi di Ds. Bangsri",
    "type": "Kontruksi",
    "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
    "coordinates": "-7.175392, 114.827153",
    "status": "Work in progress",
    "startDate": "format-tanggal",
    "finishDate": "-" 
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

## Get Project

Endpoint : GET /api/projects/{idProject}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id": "random-string",
    "projectCode": "BGN/001/dd-mm-yy",
    "project": "Pembuatan salurab irigasi di Ds. Bangsri",
    "type": "Kontruksi",
    "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
    "coordinates": "-7.175392, 114.827153",
    "status": "Work in progress",
    "startDate": "format-tanggal",
    "finishDate": "-"
  }
}
```

Response Body (Failed, 404) :

```json
{
  "rc": 404,
  "messages" : "Project is not found"
}
```

## Search Project

Endpoint : GET /api/projects

Query Param :

- projectCode : String, project code, using like query, optional
- project : String, project name, using like query, optional
- location : String, project location, using like query, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": [{
    "id": "random-string",
    "projectCode": "BGN/001/dd-mm-yy",
    "project": "Pembuatan salurab irigasi di Ds. Bangsri",
    "type": "Kontruksi",
    "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
    "coordinates": "-7.175392, 114.827153",
    "status": "Work in progress",
    "startDate": "format-tanggal",
    "finishDate": "-"
  }],
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

## Filter Project

Endpoint : GET /api/projects

Query Param :

- type : String, project type, Konstruksi(BGN) || Renovasi(RENV) || Pengadaan(SUP) || Fabrikasi(FAB), default ALL
- status : String, project status, ONGOING(001) || WORK IN PROGRESS(002) || FINISH(003), default ALL
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
        "projectCode": "BGN/001/dd-mm-yy",
        "project": "Pembuatan saluran irigasi di Ds. Bangsri",
        "type": "Kontruksi",
        "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
        "coordinates": "-7.175392, 114.827153",
        "status": "Work in progress",
        "startDate": "format-tanggal",
        "finishDate": "-"
      },{
        "id": "random-string",
        "projectCode": "RENV/001/dd-mm-yy",
        "project": "Pengecatan dinding luar",
        "type": "Renovasi",
        "location": "Jl. Pahlawan no. 16, Kel. Bendogerit, Kota Blitar",
        "coordinates": "-7.175392, 114.827153",
        "status": "Work in progress",
        "startDate": "format-tanggal",
        "finishDate": "-"
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

## Update Project

Endpoint : PUT /api/projects/{idProject}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "project": "Pembuatan salurab irigasi di Ds. Bangsri",
  "type": "Kontruksi",
  "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
  "coordinates": "-7.175392, 114.827153",
  "status": "Work in progress"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": {
    "id": "random-string",
    "projectCode": "BGN/001/dd-mm-yy",
    "project": "Pembuatan salurab irigasi di Ds. Bangsri",
    "type": "Kontruksi",
    "location": "Jl. Palem, Ds. Bangsri, Kab. Blitar",
    "coordinates": "-7.175392, 114.827153",
    "status": "Work in progress",
    "startDate": "format-tanggal",
    "finishDate": "-"
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

## Remove Contact

Endpoint : DELETE /api/contacts/{idContact}

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
  "messages" : "Contact is not found"
}
```