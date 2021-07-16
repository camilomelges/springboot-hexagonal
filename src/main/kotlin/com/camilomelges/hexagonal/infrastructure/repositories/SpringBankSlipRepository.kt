package com.camilomelges.hexagonal.infrastructure.repositories

import com.camilomelges.hexagonal.domain.models.BankSlip
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface SpringBankSlipRepository: R2dbcRepository<BankSlip, String>