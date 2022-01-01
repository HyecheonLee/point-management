package com.hyecheon.pointmanagement.point

import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@MappedSuperclass
abstract class IdEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = true, updatable = false)
	var id: Long? = null


}