# Stock service
This project has been written based on [Spring Boot Framework](https://spring.io/) and [kotlin](https://kotlinlang.org/) in order to manage stocks in the market.

# How to prepare environment?
In order to run the application you need `docker-compose`, all you need is that go to the directory where docker-compose file
exist, below command take you there!

```bash
cd infra/docker
```

and then run it!  

```bash
docker-compose up -d
```
Now you stock-db up and running!

# How to run?

In order to run the application you can just run spring boot application with below command:

```bash
mvn spring-boot:run
```

You're all set!

# How to use it?

The service exposes RESTful APIs for working with your stocks. We have prepared documentation based on the OpenAPI standard.
You can access it once you've run the application using the following link:

[API Specification](http://localhost:8080/swagger-ui/index.html)

# How to run tests?

you can run test through below commands:
```bash
mvn clean verify
```

Feel free to contribute to this project and help make it even better!