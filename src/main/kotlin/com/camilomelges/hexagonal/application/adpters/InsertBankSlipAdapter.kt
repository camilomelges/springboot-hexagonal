package com.camilomelges.hexagonal.application.adpters

import com.camilomelges.hexagonal.application.services.InsertSlipService
import com.camilomelges.hexagonal.domain.models.BankSlip
import com.camilomelges.hexagonal.domain.ports.SavePort
import reactor.core.publisher.Mono
import java.lang.RuntimeException

class InsertBankSlipAdapter(private val savePort: SavePort<BankSlip>) : InsertSlipService {

    private val SLIP_CANNOT_BE_INSERTED: String = "Slip can't be inserted"

    override fun run(slip: BankSlip): Mono<BankSlip> {
        return savePort.save(slip)
                .switchIfEmpty(Mono.error(RuntimeException(SLIP_CANNOT_BE_INSERTED)))
    }
}