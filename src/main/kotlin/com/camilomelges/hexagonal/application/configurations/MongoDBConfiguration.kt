package com.camilomelges.hexagonal.application.configurations

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories
class MongoDBConfiguration {

    @Value("\${spring.data.mongodb.database}")
    private val database: String? = null

    @Value("\${spring.data.mongodb.host}")
    private val host: String? = null

    @Value("\${spring.data.mongodb.port}")
    private val port: String? = null

    @Value("\${spring.data.mongodb.username}")
    private val username: String? = null

    @Value("\${spring.data.mongodb.password}")
    private val password: String? = null

    @Value("\${spring.data.mongodb.authenticationDatabase}")
    private val authenticationDatabase: String? = null

    fun mongoClient(): MongoClient {
        val conString = "mongodb://$username:$password@$host:$port/?authSource=$authenticationDatabase"
        return MongoClients.create(conString)
    }

    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(mongoClient(), getDatabaseName()!!)
    }

    protected fun getDatabaseName(): String? {
        return database
    }
}