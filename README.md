# Xonalarni band qilish uchun API

Bu API Impactt co-working markazi rezidentlariga majlis xonalarni oldindan oson band qilish uchun yaratildi.

## Tizim funksiyalari:

- Xonalar haqida ma'lumot saqlash va taqdim qila olish;
- Xonalar yaratish, bor xonalarni yangilash va o'chirish
- Xonani ko'rsatilgan vaqt oralig'i uchun band qila olish;
- Bir xonaning band qilingan vaqtlari ustma-ust tushmasligi kerak;

## Ishlatilgan Texnalogiyalar:

- Dasturlash tili: **Java**
- Kutubxona va freymvork: **Spring**
- Ma'lumotlar ombori: **PostgreSQL**
---

## Swagger UI 
* path: `http://localhost:8080/swagger-ui/index.html`
* api-docs: `http://localhost:8080/api-docs/users`

## Xona yaratish uchun API
Eslatma:

- `type`: mavjud xona turlari (`focus`, `team`, `conference`)

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

HTTP 201: Xona muvaffaqiyatli yaratilganda

```json
{
  "id": 1,
  "name": "ybky",
  "type": "team",
  "capacity": 14
}
```

HTTP 400: Parametrlar xato bo'lganda

```json
{
  "error": "kiritilgan parametrlar xato"
}
```

---


## Mavjud xonalarni olish uchun API

```
GET /api/rooms
```

Parametrlar:

- `search`: Xona nomi orqali qidirish
- `type`: xona turi bo'yicha saralash (`focus`, `team`, `conference`)
- `page`: sahifa tartib raqami
- `page_size`: sahifadagi maksimum natijalar soni

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

## Xonani id orqali olish uchun API

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
  "error": "topilmadi"
}
```

---


## Mavjud xonalarni o'zgartirish uchun API

Eslatma:

- `type`: mavjud xona turlari (`focus`, `team`, `conference`)


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

HTTP 200: Xona muvaffaqiyatli o'zgartirilganda

```json
{
  "id": 1,
  "name": "ybky backend",
  "type": "focus",
  "capacity": 24
}
```

HTTP 404: Xona mavjud bo'lmasa

```json
{
  "error": "topilmadi"
}
```

HTTP 400: Parametrlar xato bo'lganda

```json
{
  "error": "kiritilgan parametrlar xato"
}
```

---


## Xonaning bo'sh vaqtlarini olish uchun API

```
GET /api/rooms/{id}/availability
```

Parametrlar:

- `date`: sana (ko'rsatilmasa bugungi sana olinadi)

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

## Xonani band qilish uchun API

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

HTTP 201: Xona muvaffaqiyatli band qilinganda

```json
{
  "message": "xona muvaffaqiyatli band qilindi"
}
```

HTTP 410: Tanlangan vaqtda xona band bo'lganda

```json
{
  "error": "uzr, siz tanlagan vaqtda xona band"
}
```

---

## Xonani o'chirish uchun API

```
DELETE /api/rooms/{id}
```
---

HTTP 201: Xona muvaffaqiyatli band qilinganda

```json
{
  "message": "xona muvaffaqiyatli o'chirildi"
}
```

HTTP 404: Xona mavjud bo'lmasa

```json
{
  "error": "topilmadi"
}
```

