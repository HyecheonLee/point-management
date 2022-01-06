package com.hyecheon.pointmanagement.message

import com.hyecheon.pointmanagement.point.IdEntity
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
data class Message(

	@Column(nullable = false)
	var userId: String,

	@Column(nullable = false)
	var title: String,

	@Column(nullable = false, columnDefinition = "text")
	var content: String,


	) : IdEntity() {
	companion object {
		fun of(userId: String, expiredDate: LocalDate, expiredAmount: BigInteger) = run {
			Message(
				userId, "$expiredAmount 포인트 만료",
				"${expiredDate.format(DateTimeFormatter.ISO_DATE)} 기준 $expiredAmount 포인트가 만료되었습니다."
			)
		}
	}
}