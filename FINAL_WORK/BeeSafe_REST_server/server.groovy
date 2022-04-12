def server = vertx.createHttpServer()

server.requestHandler { request ->

    request.response.putHeader("Content-Type", "text/plain")
    request.response.end('Hello Heroku')

}.listen( System.getenv('PORT') as int, '0.0.0.0' )