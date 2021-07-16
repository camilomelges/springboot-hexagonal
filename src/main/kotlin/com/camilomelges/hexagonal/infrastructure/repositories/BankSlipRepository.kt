package com.camilomelges.hexagonal.infrastructure.repositories

import com.camilomelges.hexagonal.domain.models.BankSlip
import com.camilomelges.hexagonal.domain.ports.GetPort
import com.camilomelges.hexagonal.domain.ports.SavePort
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class BankSlipRepository(private val repository: SpringBankSlipRepository): SavePort<BankSlip>, GetPort<BankSlip> {

    override fun save(entity: BankSlip): Mono<BankSlip> {
        return repository.save(entity)
    }

    override fun getById(id: String): Mono<BankSlip> {
        return repository.findById(id)
    }
}