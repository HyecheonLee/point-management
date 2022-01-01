package com.hyecheon.pointmanagement.point.reservation

import org.springframework.data.jpa.repository.JpaRepository

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
interface PointReservationRepository : JpaRepository<PointReservation, Long> {
}