package com.hyecheon.pointmanagement.point.reservation

import com.hyecheon.pointmanagement.point.IdEntity
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
data class PointReservation(
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "point_wallet_id", nullable = false)
	var pointWallet: PointWallet,

	@Column(nullable = false, columnDefinition = "BIGINT")
	var amount: BigInteger,

	@Column(nullable = false)
	var earnedDate: LocalDate,

	@Column(nullable = false)
	var availableDays: Long,

	@Column(name = "is_executed")
	var executed: Boolean = false
) : IdEntity() {

	fun execute() = run {
		this.executed = true
	}

	fun getExpireDate(): LocalDate = run {
		earnedDate.plusDays(this.availableDays)
	}
}