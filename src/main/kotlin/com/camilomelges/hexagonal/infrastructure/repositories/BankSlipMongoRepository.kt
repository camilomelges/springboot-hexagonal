package com.camilomelges.hexagonal.infrastructure.repositories

import com.camilomelges.hexagonal.domain.models.BankSlip
import com.camilomelges.hexagonal.domain.ports.GetPort
import com.camilomelges.hexagonal.domain.ports.SavePort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class BankSlipMongoRepository(private val reactiveMongoTemplate: ReactiveMongoTemplate): SavePort<BankSlip>, GetPort<BankSlip> {

    override fun save(entity: BankSlip): Mono<BankSlip> {
        return reactiveMongoTemplate.save(entity)
    }

    override fun getById(id: String): Mono<BankSlip> {
        return reactiveMongoTemplate.findById(id, BankSlip::class.java)
    }
}