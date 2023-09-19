# stock-kt-sample
This project has been written based on spring boot framework and kotlin in order to manage stocks in the market. feel free to contribute!

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
You have it!

# How to use it?

The service exposes RESTful API to working with your stocks, We have prepared a documentation 
based on OpenAPI standard you can have look! how? you can once you run the application with previous step you have a look on
API specification in below link:

[API Specification](http://localhost:8080/swagger-ui/index.html)

# How to run tests?

you can run test through below commands:
```bash
mvn clean verify
```