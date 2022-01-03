package com.hyecheon.pointmanagement.job.reservation

import com.hyecheon.pointmanagement.job.validator.TodayJobParameterValidator
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/04
 */
@Configuration
class ExecutePointReservationJobConfiguration {

	@Bean
	fun executePointReservationJob(
		jobBuilderFactory: JobBuilderFactory,
		validator: TodayJobParameterValidator,
		executePointReservationStep: Step
	) = run {
		jobBuilderFactory.get("executePointReservationJob")
			.validator(validator)
			.incrementer(RunIdIncrementer())
			.start(executePointReservationStep)
			.build()
	}

}