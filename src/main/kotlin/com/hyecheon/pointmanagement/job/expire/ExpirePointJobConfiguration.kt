package com.hyecheon.pointmanagement.job.expire

import com.hyecheon.pointmanagement.job.validator.TodayJobParameterValidator
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@Configuration
class ExpirePointJobConfiguration {

	@Bean
	fun expirePointJob(
		jobBuilderFactory: JobBuilderFactory,
		validator: TodayJobParameterValidator,
		expirePointStep: Step
	) = run {
		jobBuilderFactory.get("expirePointJob")
			.validator(validator)
			.incrementer(RunIdIncrementer())
			.start(expirePointStep)
			.build()
	}
}