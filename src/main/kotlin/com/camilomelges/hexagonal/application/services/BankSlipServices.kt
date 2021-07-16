package com.camilomelges.hexagonal.application.services

import com.camilomelges.hexagonal.domain.models.BankSlip
import reactor.core.publisher.Mono

fun interface PaySlipService {
    fun run(id: String): Mono<BankSlip>
}

fun interface InsertSlipService {
    fun run(slip: BankSlip): Mono<BankSlip>
}