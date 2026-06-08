Use base URL:

`http://localhost:8080`

Headers for JSON requests:

```http
Content-Type: application/json
```

Because Spring Security is enabled, use Basic Auth unless you add a custom security config:

```text
Username: user
Password: check console log: "Using generated security password: ..."
```

**Inventory APIs**

`POST /api/inventory`

```json
{
  "itemName": "Tomato",
  "category": "VEGETABLE",
  "unitType": "KG",
  "availableStock": 25.5,
  "minimumStock": 5,
  "pricePerUnit": 40,
  "batchNumber": "B001",
  "expiryDate": "2026-06-20",
  "receivedDate": "2026-06-02"
}
```

`GET /api/inventory`

No body.

`GET /api/inventory/1`

No body.

`PUT /api/inventory/1`

```json
{
  "itemName": "Tomato Premium",
  "category": "VEGETABLE",
  "unitType": "KG",
  "availableStock": 30,
  "minimumStock": 6,
  "pricePerUnit": 45,
  "batchNumber": "B001",
  "expiryDate": "2026-06-25",
  "receivedDate": "2026-06-02"
}
```

`DELETE /api/inventory/1`

No body.

**Stock APIs**

`POST /api/inventory/add-stock`

```json
{
  "inventoryId": 1,
  "quantity": 10,
  "remarks": "New stock received"
}
```

`POST /api/inventory/reduce-stock`

```json
{
  "inventoryId": 1,
  "quantity": 3,
  "remarks": "Used for order"
}
```

**History APIs**

`GET /api/inventory/history`

No body.

`GET /api/inventory/history/1`

No body.

**Alert APIs**

`GET /api/inventory/low-stock`

No body.

`GET /api/inventory/expiring-items`

No body.

**Dashboard APIs**

`GET /api/dashboard/inventory/summary`

`GET /api/dashboard/inventory/daily-consumption`

`GET /api/dashboard/inventory/weekly-consumption`

`GET /api/dashboard/inventory/monthly-consumption`

`GET /api/dashboard/inventory/top-consumed-items`

No body for dashboard APIs.

Valid `category` values include: `VEGETABLE`, `FRUIT`, `DAIRY`, `GRAINS`, `PULSES`, `SPICES`, `OIL`, `BEVERAGE`, `MEAT`, `SEAFOOD`, `BAKERY`, `FROZEN_FOOD`, `PACKAGING`, `CLEANING_SUPPLIES`, `OTHER`.

Valid `unitType` values: `KG`, `GRAM`, `LTR`, `ML`, `PCS`, `PACK`, `BOX`, `BOTTLE`, `TRAY`.