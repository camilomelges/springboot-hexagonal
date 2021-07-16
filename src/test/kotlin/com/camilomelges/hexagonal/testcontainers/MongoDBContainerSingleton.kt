package com.camilomelges.hexagonal.testcontainers

import org.springframework.beans.factory.annotation.Value
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.shaded.com.google.common.collect.ImmutableList
import java.time.Duration
import javax.annotation.PostConstruct

open class MongoDBContainerSingleton {

    @Value("\${spring.data.mongodb.database}")
    private fun setDatabase(database: String) {
        DATABASE = database
    }

    @Value("\${spring.data.mongodb.username}")
    private fun setUsername(username: String) {
        USERNAME = username
    }

    @Value("\${spring.data.mongodb.password}")
    private fun setPassword(password: String) {
        PASSWORD = password
    }

    @Value("\${spring.data.mongodb.port}")
    private fun setPort(port: String) {
        PORT = port
    }

    @Value("\${spring.data.mongodb.image.name}")
    private fun setImageName(imageName: String) {
        IMAGE_NAME = imageName
    }

    @Value("\${spring.data.mongodb.image.version}")
    private fun setImageVersion(imageVersion: String) {
        IMAGE_VERSION = imageVersion
    }

    var MONGODB_CONTAINER: GenericContainer<*>? = null
    private var DATABASE: String? = null
    private var USERNAME: String? = null
    private var PASSWORD: String? = null
    private var PORT: String? = null
    private var IMAGE_NAME: String? = null
    private var IMAGE_VERSION: String? = null

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

    @PostConstruct
    fun mongoSingleton() {
        if (MONGODB_CONTAINER == null) {
            MONGODB_CONTAINER = KGenericContainer(getMongoDBImageName)
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", USERNAME)
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", PASSWORD)
                    .withEnv("MONGO_INITDB_DATABASE", DATABASE)
                    .withExposedPorts(PORT!!.toInt())

            val list: MutableList<String> = ArrayList()
            list.add("MONGO_INITDB_ROOT_USERNAME=$USERNAME")
            list.add("MONGO_INITDB_ROOT_PASSWORD=$PASSWORD")
            list.add("MONGO_INITDB_DATABASE=$DATABASE")
            MONGODB_CONTAINER!!.env = list
            MONGODB_CONTAINER!!.withExposedPorts(PORT!!.toInt())
            MONGODB_CONTAINER!!.portBindings = ImmutableList.of("0.0.0.0:$PORT:$PORT")

            try {
                MONGODB_CONTAINER!!.start()
            } catch (e: ContainerLaunchException) {
                MONGODB_CONTAINER!!.waitingFor(WaitAllStrategy().withStartupTimeout(Duration.ofSeconds(5))) !!.start()
            }
        }
    }

    private val getMongoDBImageName: String
        get() = "$IMAGE_NAME:$IMAGE_VERSION"
}
