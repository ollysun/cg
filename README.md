CG

The project is dockerize with the dockerfile inside.

Follow this process carefully

Clone the project and type

`mvn clean install` .

This will generate the jar file

Build the dockerfile with this command

`docker build --tag=cgtest:latest .`

Check the image using: `docker image ls`

Run the project with this command.
`docker run -p8091:8090 cgtest:latest`

Then you can test the project on postman or any other tools.


To add transaction
http://localhost:8091/transaction

Payload. make sure the timestamp is within 60 seconds
else you get 204 error code or future which is 422 code.
Those with 204 or 422 are not added except 201 created successfully
# payload
`
{
"amount":"12.3343",
"timestamp":"2021-05-01T09:59:51.312Z"
}
`

To view statistics
http://localhost:8091/statistics

To delete transactions
http://localhost:8091/transactions

