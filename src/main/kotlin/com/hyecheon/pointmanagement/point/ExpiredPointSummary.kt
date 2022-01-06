package com.hyecheon.pointmanagement.point

import com.querydsl.core.annotations.QueryProjection
import java.math.BigInteger

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/06
 */

data class ExpiredPointSummary @QueryProjection constructor(
	val userId: String,
	val amount: BigInteger,
)