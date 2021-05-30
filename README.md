# microservices_ecommerce

if server.port=0 spring initialize application at random port
Allow to run multiple instances of application in Intellij

1. vault server -dev
2. vault kv put secret/order-service @order-service.json
3. vault kv put secret/product-service @product-service.json
4. vault kv get secret/order-service

Change spring.cloud.vault.token property in product-service and order-service as per your machine


To run rabbit mq on docker run below command 

 docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management
 
 You can access RabbitMQ dashboard at http://localhost:15672 user username and password as guest
