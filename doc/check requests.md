#REST api example requests

###cURL:
##### Meal resource - {root}/rest/meals
- get all meals 
``` bash
$ curl -X GET http://localhost:8080/topjava/rest/meals
```
- get meal by id 
``` bash
$ curl -X GET http://localhost:8080/topjava/rest/meals/100002
```
- create meal 
``` bash
$ curl -X POST 'http://localhost:8080/topjava/rest/meals' \
  -H 'Content-Type: application/json' \
  -d '{
      "dateTime": "2020-02-01T18:01:00",
      "description": "Созданный ужин",
      "calories": 1000
  }' 
```
- update meal 
``` bash
$ curl -X PUT 'http://localhost:8080/topjava/rest/meals/100003' \
  -H 'Content-Type: application/json' \
  -d '{
      "dateTime": "2020-02-01T18:00:00",
      "description": "Updated meal",
      "calories": 300
  }'
```
- delete meal 
``` bash
$ curl -X DELETE http://localhost:8080/topjava/rest/meals/100002
```
- get date/time filtered meals 
``` bash
$ curl -X GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=00:00&endTime=23:59'
```
