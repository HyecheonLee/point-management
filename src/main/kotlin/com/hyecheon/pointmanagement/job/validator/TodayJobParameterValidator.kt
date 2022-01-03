package com.hyecheon.pointmanagement.job.validator

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/04
 */
@Component
class TodayJobParameterValidator : JobParametersValidator {
	override fun validate(parameters: JobParameters?) {
		if (parameters === null) {
			throw JobParametersInvalidException("job paramter today is required")
		}
		val todayStr =
			parameters.getString("today") ?: throw JobParametersInvalidException("job paramter today is required")
		try {
			LocalDate.parse(todayStr)
		} catch (ex: DateTimeParseException) {
			throw JobParametersInvalidException("job paramter today is required")
		}
	}
}