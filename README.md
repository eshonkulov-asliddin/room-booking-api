Here is the translated README.md:

"""
# API for Booking Rooms

This API is created to help resident users of Impactt co-working zone easily pre-book meeting rooms.

## System functionalities:

- Storing and providing information about rooms;
- Creating, updating and deleting rooms
- Blocking a room for a certain period;
- One room should not be blocked multiple times at the same time.

## Technologies Used:

- Programming Language: **Java**
- Library and Framework: **Spring**
- Database: **PostgreSQL**
---

## Swagger UI
* path: `http://localhost:8080/swagger-ui/index.html`
* api-docs: `http://localhost:8080/api-docs/users`

## API to create a room
Note:

- `type` : available room types (`focus`, `team`, `conference`)

```
POST /api/rooms
```

```json
{
  "name": "ybky",
  "type": "team",
  "capacity": 14
}
```
---

HTTP 201: Room successfully created

```json
{
  "id": 1,
  "name": "ybky",
  "type": "team",
  "capacity": 14
}
```

HTTP 400: In case of wrong parameters

```json
{
  "error": "wrong parameters"
}
```

---


## API to get available rooms

```
GET /api/rooms
```

Parameters:

- `search` : Search by room name
- `type` : sort by room type (`focus`, `team`, `conference`)
- `page` : page sequence number
- `page_size` : maximum results on a page

HTTP 200

```json
{
  "page": 1,
  "count": 3,
  "page_size": 10,
  "results": [
    {
      "id": 1,
      "name": "mytaxi",
      "type": "focus",
      "capacity": 1
    },
    {
      "id": 2,
      "name": "workly",
      "type": "team",
      "capacity": 5
    },
    {
      "id": 3,
      "name": "express24",
      "type": "conference",
      "capacity": 15
    }
  ]
}
```

---

## API to get room by id

```
GET /api/rooms/{id}
```

HTTP 200

```json
{
  "id": 3,
  "name": "express24",
  "type": "conference",
  "capacity": 15
}
```

HTTP 404

```json
{
  "error": "not found"
}
```

---


## API to change existing rooms

Note:

- `type` : available room types (`focus`, `team`, `conference`)


```
PUT /api/rooms/{id}
```

```json
{
  "name": "ybky backend",
  "type": "focus",
  "capacity": 24
}
```

HTTP 200: Room successfully updated

```json
{
  "id": 1,
  "name": "ybky backend",
  "type": "focus",
  "capacity": 24
}
```

HTTP 404: If room does not exist

```json
{
  "error": "not found"
}
```

HTTP 400: In case of wrong parameters

```json
{
  "error": "wrong parameters"
}
```

---


## API to get available times for a room

```
GET /api/rooms/{id}/availability
```

Parameters:

- `date` : date (if not specified, the current date is taken)

Response 200

```json
[
  {
    "start": "05-06-2023 9:00:00",
    "end": "05-06-2023 11:00:00"
  },
  {
    "start": "05-06-2023 13:00:00",
    "end": "05-06-2023 18:00:00"
  }
]
```

---

## API to block a room

```
POST /api/rooms/{id}/book
```

```json
{
  "resident": {
    "name": "Anvar Sanayev"
  },
  "start": "05-06-2023 9:00:00",
  "end": "05-06-2023 10:00:00"
}
```

---

HTTP 201: Room successfully blocked

```json
{
  "message": "room successfully blocked"
}
```

HTTP 410: In case of room already blocked

```json
{
  "error": "Sorry, the room is already blocked at your chosen times"
}
```

---

## API to delete a room

```
DELETE /api/rooms/{id}
```
---

HTTP 201: Room successfully deleted

```json
{
  "message": "room successfully deleted"
}
```

HTTP 404: If room does not exist

```json
{
  "error": "not found"
}
```
"""
