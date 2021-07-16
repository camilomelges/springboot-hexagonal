package com.camilomelges.hexagonal.application.configurations

import com.camilomelges.hexagonal.infrastructure.repositories.BankSlipMongoRepository
import com.camilomelges.hexagonal.infrastructure.repositories.BankSlipRepository
import com.camilomelges.hexagonal.application.adpters.InsertBankSlipAdapter
import com.camilomelges.hexagonal.application.adpters.PayBankSlipAdapter
import com.camilomelges.hexagonal.application.services.InsertSlipService
import com.camilomelges.hexagonal.application.services.PaySlipService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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