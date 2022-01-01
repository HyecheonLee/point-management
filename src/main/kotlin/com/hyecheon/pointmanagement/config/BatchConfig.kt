package com.hyecheon.pointmanagement.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@EnableBatchProcessing
@Configuration
class BatchConfig(
	private val jobBuilderFactory: JobBuilderFactory
) {
}