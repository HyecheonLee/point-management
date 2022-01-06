package com.hyecheon.pointmanagement.point

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.math.BigInteger
import java.time.LocalDate

class PointCustomRepositoryImpl : QuerydslRepositorySupport(Point::class.java), PointCustomRepository {
	override fun sumByExpiredDate(alarmCriteriaDate: LocalDate, pageable: Pageable): Page<ExpiredPointSummary> {
		val query = from(QPoint.point)
			.select(
				QExpiredPointSummary(
					QPoint.point.pointWallet.userId,
					QPoint.point.amount.sum().coalesce(BigInteger.ZERO)
				)
			)
			.where(QPoint.point.expired.eq(true))
			.where(QPoint.point.used.eq(false))
			.where(QPoint.point.expireDate.eq(alarmCriteriaDate))
			.groupBy(QPoint.point.pointWallet)

		val expiredPointList = querydsl?.applyPagination(pageable, query)?.fetch() ?: emptyList()
		val elementCount = query.fetchCount()
		return PageImpl(
			expiredPointList,
			PageRequest.of(pageable.pageNumber, pageable.pageSize),
			elementCount
		)
	}
}