# Springboot Hexagonal

###### Project to training with hexagonal architecture

## Structure

```
application
    adapters -> Implementations of services
    configurations -> Beans of adapters
    services -> Services

domain
    models -> Entities of domain
    ports -> Use cases
    
infrastructure
    persistence -> Database repositories
```

## Domain

### Model
```
@Document(collection = "bankslips") 
@Table("bankslips")
class BankSlip(val value: BigDecimal) {
    @Id
    var id: Int? = null
    var paid: Boolean? = false
}
```

###### I created just one Model for two databases: 
    - @Document annotation for mongodb
    - @Table for SQL, in this case H2

### Ports
```
fun interface SavePort<T> {
    fun save(entity: T): Mono<T>
}

fun interface GetPort<T> {
    fun getById(id: Int): Mono<T>
}
```

###### Are generic and reusable interfaces for get any Mono<Entity> or insert any Entity on some service, this service can be one api or database, in this case are two databases, each one with your own repository

## Infrastructure

### Repositories
```
@Repository
class BankSlipRepository(private val repository: SpringBankSlipRepository): SavePort<BankSlip>, GetPort<BankSlip> {

    override fun save(bankSlip: BankSlip): Mono<BankSlip> {
        return repository.save(bankSlip)
    }

    override fun getById(id: Int): Mono<BankSlip> {
        return repository.findById(id)
    }
}
```
###### Repository SQL H2 for BankSlips, this repository implements ports

```
@Repository
class BankSlipMongoRepository(private val reactiveMongoTemplate: ReactiveMongoTemplate): SavePort<BankSlip>, GetPort<BankSlip> {

    override fun save(bankSlip: BankSlip): Mono<BankSlip> {
        return reactiveMongoTemplate.save(bankSlip)
    }

    override fun getById(id: String): Mono<BankSlip> {
        return reactiveMongoTemplate.findById(id, BankSlip::class.java)
    }
}
```
###### Repository MongoDB for BankSlips, this repository implements ports and use reactiveMongoTemplate to make actions in database

## Application

### Services
```
fun interface PaySlipService {
    fun run(id: String): Mono<BankSlip>
}

fun interface InsertSlipService {
    fun run(slip: BankSlip): Mono<BankSlip>
}
```
###### Services to use in application layer, to not broke encapsulation, only interfaces from application layer for external resources

### Adapters
```
class InsertBankSlipAdapter(private val savePort: SavePort<BankSlip>) : InsertSlipService {

    private val SLIP_CANNOT_BE_INSERTED: String = "Slip can't be inserted"

    override fun run(slip: BankSlip): Mono<BankSlip> {
        return savePort.save(slip)
                .switchIfEmpty(Mono.error(RuntimeException(SLIP_CANNOT_BE_INSERTED)))
    }
}
```
###### Adapter with implementation of InsertSlipService, that injects some SavePort, for example, BankSlipRepository

```
class PayBankSlipAdapter(private val savePort: SavePort<BankSlip>, private val getPort: GetPort<BankSlip>) : PaySlipService {

    private val SLIP_NOT_FOUND: String = "Slip not found"

    override fun run(id: String): Mono<BankSlip> {
        return getPort.getById(id)
                .switchIfEmpty(Mono.error(RuntimeException(SLIP_NOT_FOUND)))
                .map(::setPaid)
                .flatMap(savePort::save)
    }

    private fun setPaid(slip: BankSlip): BankSlip {
        slip.paid = true
        return slip
    }
}
```
###### Adapter with implementation of PaySlipService, that injects some SavePort and getPort, for example, BankSlipRepository

### Configurations
```
@Configuration
class BeanConfiguration {

    @Bean
    fun payBankSlipService(bankSlipRepository: BankSlipRepository): PaySlipService {
        return PayBankSlipAdapter(bankSlipRepository, bankSlipRepository)
    }

    @Bean
    fun insertBankSlipService(bankSlipRepository: BankSlipRepository): InsertSlipService {
        return InsertBankSlipAdapter(bankSlipRepository)
    }

    @Bean
    fun payBankSlipMongoService(bankSlipMongoRepository: BankSlipMongoRepository): PaySlipService {
        return PayBankSlipAdapter(bankSlipMongoRepository, bankSlipMongoRepository)
    }

    @Bean
    fun insertBankSlipMongoService(bankSlipMongoRepository: BankSlipMongoRepository): InsertSlipService {
        return InsertBankSlipAdapter(bankSlipMongoRepository)
    }
}
```
###### Configurations of beans, you can see that have two beans for same adapter, but one inject BankSlipRepository and other inject BankSlipMongoRepository, now we understand how we can construct reusable use cases just creating services in infrastructure layer that implement ports of domain, for example, some service that connect and use some restApi with a save method "POST" and getById "GET"

## Running

###### OBS.: Project use JDK 11, so configure your JDK for this version before run
###### I created this project just for example, so don't have docker-compose, Dockerfile etc... So you can configure by your own
###### But I created this application with a good practices, so you can easily configure passing JVM args

```
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:h2:mem:testdb}
    driverClassName: org.h2.Driver
    username: ${SPRING_DATASOURCE_USERNAME:sa}
    password: ${SPRING_DATASOURCE_PASSWORD:password}
  jpa:
    databasePlatform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddlAuto: update
  data:
    mongodb:
      host: ${SPRING_DATA_MONGODB_HOST:localhost}
      port: ${SPRING_DATA_MONGODB_PORT:27017}
      database: ${SPRING_DATA_MONGODB_DATABASE:bank}
      username: ${SPRING_DATA_MONGODB_USERNAME:root}
      password: ${SPRING_DATA_MONGODB_PASSWORD:root}
      authenticationDatabase: ${SPRING_DATA_MONGODB_AUTHENTICATION_DATABASE:admin}
```

#### Example of run passing host of mongodb:
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DSPRING_DATA_MONGODB_HOST=SOME_IP"

Just add some more variables after SOME_IP if you need
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DSPRING_DATA_MONGODB_HOST=SOME_IP -D..."
```

## Tests

###### OBS.: Project use JDK 11, so configure your JDK for this version before run tests
###### Project is construct with testcontainers, so you don't need to configure mongodb for test environment

```
mvn test
```