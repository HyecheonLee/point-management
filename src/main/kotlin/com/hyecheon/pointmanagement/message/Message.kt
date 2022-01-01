package com.hyecheon.pointmanagement.message

import com.hyecheon.pointmanagement.point.IdEntity
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
}