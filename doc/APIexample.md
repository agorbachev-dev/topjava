# REST api example requests

### cURL:
##### Meals resource - /rest/meals
###### {root} - app url like 'http://ip:port/topjava'
- get all meals 
``` bash
$ curl -X GET {root}/rest/meals
```
- get meal by id 
``` bash
$ curl -X GET {root}/rest/meals/{id}
```
- create meal 
``` bash
$ curl -X POST 'http:{root}/rest/meals' \
  -H 'Content-Type: application/json' \
  -d '{
      "dateTime": "yyyy-MM-ddThh:mm:ss",
      "description": "description",
      "calories": 1000
  }' 
```
- update meal 
``` bash
$ curl -X PUT '{root}/rest/meals/{id}}' \
  -H 'Content-Type: application/json' \
  -d '{
      "dateTime": "yyyy-MM-ddThh:mm:ss",
      "description": "description",
      "calories": 300
  }'
```
- delete meal 
``` bash
$ curl -X 'DELETE {root}/rest/meals/{id}'
```
- get date/time filtered meals 
``` bash
$ curl -X GET '{root}/rest/meals/filter?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd&startTime=hh:mm&endTime=hh:mm'
```
