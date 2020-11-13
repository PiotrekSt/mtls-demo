### Sample mTLS application

Sample Spring Boot application which implements the communication between two servicies secured by mTLS.

##### How to run and test
1. Start the `server` application (it will start on port 9000)
    ```sh
    $ cd server
    $ mvn spring-boot:run
    ```
2. Start the `client` application (it will start on port 9050)
    ```sh
    $ cd client
    $ mvn spring-boot:run
    ```
3. Execute the GET HTTP request to client application
    ```sh
    curl -X GET localhost:9050/client-endpoint -v
    ```
