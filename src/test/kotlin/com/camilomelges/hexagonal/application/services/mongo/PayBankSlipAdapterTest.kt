package com.camilomelges.hexagonal.application.services.mongo

import com.camilomelges.hexagonal.application.services.InsertSlipService
import com.camilomelges.hexagonal.application.services.PaySlipService
import com.camilomelges.hexagonal.domain.models.BankSlip
import com.camilomelges.hexagonal.testcontainers.MongoDBContainerSingleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.RuntimeException
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@SpringBootTest
class PayBankSlipAdapterTest: MongoDBContainerSingleton() {

    @Autowired
    private lateinit var payBankSlipMongoService: PaySlipService

    @Autowired
    private lateinit var insertBankSlipMongoService: InsertSlipService

    private val value: Double = 22.21

    private val SLIP_NOT_FOUND: String = "Slip not found"

    @Test
    fun shouldBeReturnExceptionWhenNotFoundSlipById() {
        assertThrows(RuntimeException::class.java, { payBankSlipMongoService.run(Int.MAX_VALUE.toString()).block() }, SLIP_NOT_FOUND)
    }

    @Test
    fun shouldBeChangePaidToTrue() {
        var bankSlip = insertBankSlipMongoService.run(BankSlip(BigDecimal(value))).block()

        assertEquals(value, bankSlip?.value?.toDouble())
        assertEquals(false, bankSlip?.paid)
        assertNotNull(bankSlip?.id)

        val id = bankSlip?.id

        bankSlip = payBankSlipMongoService.run(bankSlip?.id!!).block()

        assertEquals(value, bankSlip?.value?.toDouble())
        assertEquals(true, bankSlip?.paid)
        assertEquals(id, bankSlip?.id)
    }
}