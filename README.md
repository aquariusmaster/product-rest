# Product API

### How to use it:

API documentation:

```shell
curl --location --request GET 'http://localhost:8881/profile/products'
```

Get all products:

```shell
curl --location --request GET 'http://localhost:8881/products' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

GET product by id:

```shell
curl --location --request GET 'http://localhost:8881/products/1' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

POST new product:

```shell
curl --location --request POST 'http://localhost:8881/products' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "IPhone",
    "price": 23225.55,
    "unit": "UNIT",
    "description": "Phone"
}'
```

PUT new product:

```shell
curl --location --request PUT 'http://localhost:8881/products/2' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Pickle",
    "price": 2.33,
    "unit": "KG",
    "description": "New description"
}'
```

Batch uploading (curently supports only `xlsx` files, example: [product.xlsx](product.xlsx)):

```shell
curl --location --request POST 'http://localhost:8881/products/batch' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--form 'file=@"product.xlsx"'
```

#### Searching by fields:

```shell
curl --location --request GET 'http://localhost:8881/products/search'

curl --location --request GET 'http://localhost:8881/products/search/name?q=P' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='

curl --location --request GET 'http://localhost:8881/products/search/unit?q=KG' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='

curl --location --request GET 'http://localhost:8881/products/search/description?q=esc' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

