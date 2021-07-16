package com.camilomelges.hexagonal.application.adpters

import com.camilomelges.hexagonal.application.services.PaySlipService
import com.camilomelges.hexagonal.domain.models.BankSlip
import com.camilomelges.hexagonal.domain.ports.GetPort
import com.camilomelges.hexagonal.domain.ports.SavePort
import reactor.core.publisher.Mono
import java.lang.RuntimeException

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