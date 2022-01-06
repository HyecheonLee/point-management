package com.hyecheon.pointmanagement.point

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/06
 */
interface PointCustomRepository {
	fun sumByExpiredDate(alarmCriteriaDate: LocalDate, pageable: Pageable): Page<ExpiredPointSummary>
}