package com.hyecheon.pointmanagement.job.message

import com.hyecheon.pointmanagement.job.validator.TodayJobParameterValidator
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/06
 */
@Configuration
class MessageExpiredPointJobConfiguration {

	@Bean
	fun messageExpiredPointJob(
		jobBuilderFactory: JobBuilderFactory,
		validator: TodayJobParameterValidator,
		messageExpiredPointStep: Step
	) = run {
		jobBuilderFactory["messageExpiredPointJob"]
			.validator(validator)
			.incrementer(RunIdIncrementer())
			.start(messageExpiredPointStep)
			.build()
	}
}