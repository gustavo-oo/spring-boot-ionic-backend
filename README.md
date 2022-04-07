<h1 align="center">Modelo Conceitual em SpringBoot</h1>

<p align="center"> This project implements an API in Java Using SpringBoot and Hibernate
    <br> 
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Database](#database)
- [Endpoints](#endpoints)
- [Built Using](#built_using)
- [Authors](#authors)

## 🧐 About <a name = "about"></a>
The project was developed during a course of SpringBoot Java tutorial, where was created this API that implements the model described in the file "Descrição.pdf'.

## 🏁 Getting Started <a name = "getting_started"></a>
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
 - Java JDK 11
 - SpringBoot 2.X.X


### Installing
After clone the repository, just open it with SpringBoot.

Run **CursomcApplication** as SpringBoot app

### Database <a name="database"></a>

The database could be analyzed in localhost:8080/h2-console

Type this in JDBC URL:
```
jdbc:h2:mem:testdb
```
and connect.

### 🎈 EndPoints <a name="endpoints"></a>
In this project was implemented three endpoints:

- /clientes/{id}: Returns a client, his adresses and phones.
- /categorias/{id}: Returns a category and their products
- /pedidos/{id}: Returns a order and their client, payment, order items and delivery adress.

If any request is made in a non implemented endpoint it displays 404 error message.

Example

```
localhost:8080/pedidos/1
```

## ⛏️ Built Using <a name = "built_using"></a>
- [SpringBoot](https://spring.io/projects/spring-boot) - IDE
- [Hibernate](https://hibernate.org/) - JPA Framework
- [H2](https://www.h2database.com/html/main.html) - Database
- [Java](https://openjdk.java.net/projects/jdk/11/) - Programming Language

## ✍️ Authors <a name = "authors"></a>
- [@gustavo-oo](https://github.com/gustavo-oo)
