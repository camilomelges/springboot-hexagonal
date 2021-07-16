package com.camilomelges.hexagonal.application.services.h2

import com.camilomelges.hexagonal.application.services.InsertSlipService
import com.camilomelges.hexagonal.domain.models.BankSlip
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@SpringBootTest
class InsertBankSlipAdapterTest {

    @Autowired
    private lateinit var insertBankSlipService: InsertSlipService

    private val value: Double = 22.21

    @Test
    fun shouldBeInsertIfNotPassPaid() {
        val bankSlip = insertBankSlipService.run(BankSlip(BigDecimal(value))).block()

        assertEquals(value, bankSlip?.value?.toDouble())
        assertEquals(false, bankSlip?.paid)
        assertNotNull(bankSlip?.id)
    }
}