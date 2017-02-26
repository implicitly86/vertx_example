package com.implicitly

import com.implicitly.dao.IslandsDao
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {

    private val dao = IslandsDao()

    private val router = Router.router(vertx).apply {
        get("/").handler { ctx ->
            ctx.response().end("Welcome!")
        }
        get("/islands").handler { ctx ->
            val islands = dao.fetchIslands()
            ctx.response().endWithJson(islands)
        }
        get("/countries").handler { ctx ->
            val countries = dao.fetchCountries()
            ctx.response().endWithJson(countries)
        }
        get("/countries/:code").handler { ctx ->
            val code = ctx.request().getParam("code")
            val countries = dao.fetchCountries(code)
            if (countries.isEmpty()) {
                ctx.fail(404)
            } else {
                ctx.response().endWithJson(countries.first())
            }
        }
    }

    fun HttpServerResponse.endWithJson(obj: Any) {
        putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encodePrettily(obj))
    }

    override fun start(startFuture: Future<Void>?) {
        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(Integer.getInteger("http.port", 8080)) { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                }
    }

}