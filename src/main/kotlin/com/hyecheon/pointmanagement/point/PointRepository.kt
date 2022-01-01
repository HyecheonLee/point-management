package com.hyecheon.pointmanagement.point

import org.springframework.data.jpa.repository.JpaRepository

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
interface PointRepository : JpaRepository<Point, Long> {
}
