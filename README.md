# Agile Engine Photos
This Api connects with the Image gallery Search.
The purpose of this api is obtain data through API and persist it in cache data, in this case I used Redis
like Cache persistent.
I've used Akka actors in order to schedule a task to obtain data from Api.
Also, The framework stack that I've used was:
    - Play framework 2.8
    - Akka actors
    - Cats Effects
    - Redis

First of all, you have to install docker and docker-compose and then, you can run docker-compose to run Redis.
After that, you run the app.

### run Redis environment
docker-compose $ docker-compose up -d

### run app
$ sbt "run 9000"

#### Environment Variables
API_INTERVALS: time connections to get data from API
REDIS_HOST: redis host
REDIS_PORT: redis port
API_URL: url endpoint API from get data
API_KEY: apikey.

For more information, see conf/application.conf file.


### Endpoint
GET         /images?page=:page&size=:size 
page: number of Page.
size: items per page.

GET         /image/:id
id: identity of image  

