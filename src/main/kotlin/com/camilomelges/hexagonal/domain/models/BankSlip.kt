package com.camilomelges.hexagonal.domain.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Document(collection = "bankslips")
@Table("bankslips")
class BankSlip(val value: BigDecimal) {
    @Id
    var id: String? = null
    var paid: Boolean? = false
}