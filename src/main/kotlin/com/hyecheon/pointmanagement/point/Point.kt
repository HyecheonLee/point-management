package com.hyecheon.pointmanagement.point

import com.hyecheon.pointmanagement.point.wallet.PointWallet
import java.math.BigInteger
import java.time.LocalDate
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@Table
@Entity
data class Point(

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "point_wallet_id", nullable = false)
	var pointWallet: PointWallet,

	@Column(nullable = false, columnDefinition = "BIGINT")
	var amount: BigInteger,

	@Column(nullable = false)
	var earnedDate: LocalDate,

	@Column(nullable = false)
	var expireDate: LocalDate,

	@Column(nullable = false, name = "is_used")
	var used: Boolean,

	@Column(nullable = false, name = "is_expired")
	var expired: Boolean
) : IdEntity() {

	fun expire() = run {
		if (!this.used) this.expired = true
	}
}