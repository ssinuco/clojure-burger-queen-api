# burger-queen-api

A REST API which serves endpoints to create users, products and orders for a simple commerce. A swagger documentation is available [here](https://app.swaggerhub.com/apis-docs/ssinuco/BurgerQueenAPI/2.0.0-english).

## Endpoints

### `/users`

* `GET /users`
* `GET /users/:uid`
* `POST /users`
* `PUT /users/:uid`
* `DELETE /users/:uid`

### `/products`

* `GET /products`
* `GET /products/:productid`
* `POST /products`
* `PUT /products/:productid`
* `DELETE /products/:productid`

### `/orders`

* `GET /orders`
* `GET /orders/:orderId`
* `POST /orders`
* `PUT /orders/:orderId`
* `DELETE /orders/:orderId`

## Implementation details

SQLite is used as database. [next.jdbc](https://github.com/seancorfield/next-jdbc) is used for JDBC-based access to the database.

[ring](https://github.com/ring-clojure/ring) is used as web server.

[compojure](https://github.com/weavejester/compojure) is used as routing library.

[hugsql](https://www.hugsql.org/) is used to define database functions from sql queries for creating a clean separation of Clojure and SQL code.

[data.json](https://github.com/clojure/data.json) is used for generating JSON response.

A [next.jdbc.plan](https://cljdoc.org/d/com.github.seancorfield/next.jdbc/1.2.772/doc/getting-started#plan--reducing-result-sets) is used to reduce a join query.

A [next.jdbc.with-transaction](https://cljdoc.org/d/com.github.seancorfield/next.jdbc/1.2.772/doc/getting-started/transactions#connection-level-control) macro is used to warranty a correct order creation which requires insertion into two tables: orders and orders_products.

### Projects files

- connection.clj: connection and datasource for sqlite database
- handler.clj: project entry point
- middlewares.clj: web server middlewares
- orders.clj: functions to handle orders endpoints
- products.clj: function to handle products endpoints
- routes.clj: API routes
- users.clj: functions to handle users endpoints
- utils.clj: some common functions
- sql/orders.clj: function to reduce join query between orders and orders_products table.
- sql/orders.sql: orders table queries
- sql/products.sql: products table queries
- sql/users.sql: users table queries

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2022 FIXME
