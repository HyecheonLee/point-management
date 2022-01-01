package com.hyecheon.pointmanagement.point.wallet

import com.hyecheon.pointmanagement.point.IdEntity
import java.math.BigInteger
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@Entity
@Table
data class PointWallet(

	@Column(unique = true, nullable = false)
	var userId: String,

	@Column(columnDefinition = "BIGINT")
	var amount: BigInteger? = null
) : IdEntity() {

}