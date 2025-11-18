# User API Specs

## Register

Endpoint : POST api/users

Request Body :

```json
{
  "username" : "art.projects",
  "password" : "12345"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data": "Berhasil mendaftar"
}
```

Response Body (Failed) :
```json
{
  "rc": 400,
  "messages": "Bad Request"
}
```

## Login User

Endpoint : POST api/auth/login

Request Body :

```json
{
  "username" : "art.projects",
  "password" : "12345"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 1234567890 
  }
}
```

Response Body (Failed) :

```json
{
  "rc": 400,
  "messages": "Username or password is wrong"
}
```

## Get User

Endpoint : GET api/users/current

Request Header ;
- X-API-TOKEN : Token (mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data" : {
    "username" : "art.projects",
    "companyName" : "ART Projects"
  }
}
```

Response Body (Failed) :

```json
{
  "rc": 401,
  "messages": "Unauthorized"
}
```

## Update User

Endpoint : PATCH api/users/current

Request Header ;
- X-API-TOKEN : Token (mandatory)

Request Body :

```json
{
  "username" : "art.projects",
  "password" : "new 12345",
  "companyName" : "Proyek ART"
}
```

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK",
  "data" : {
    "username" : "art.projects",
    "companyName" : "Proyek ART"
  }
}
```

Response Body (Failed) :

```json
{
  "rc": 401,
  "messages": "Unauthorized"
}
```

## Logout User

Endpoint : DELETE api/auth/logout

Request Header ;
- X-API-TOKEN : Token (mandatory)

Response Body (Success) :

```json
{
  "rc": 200,
  "messages": "OK"
}
```