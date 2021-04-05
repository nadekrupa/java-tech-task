# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication

### Using App ###
- Import the postman collection in to the postman application
- Execute the "Recipe - by date" request to find the recipes by date. 
- Execute the "Recipe - by title" request to find the recipes by given title. 
- Execute the "Recipe - by excluding ingredients" request to find the recipes by excluding given ingredients. 
- Please note that this API does need any authentication

### Assumptions ###
- API to 'find the recipe by title' is POST as the title can have white space and special characters resulting into encoding issues
- API to 'find the recipe by excluding ingredients' is POST so that multiple ingredients can be provided in array
- A recipe will be filtered from response if it does not have any ingredients
