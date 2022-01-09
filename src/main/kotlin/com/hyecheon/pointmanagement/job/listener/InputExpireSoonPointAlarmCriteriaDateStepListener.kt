package com.hyecheon.pointmanagement.job.listener

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/06
 */
@Component
class InputExpireSoonPointAlarmCriteriaDateStepListener : StepExecutionListener {
	override fun beforeStep(stepExecution: StepExecution) {
		val todayParameter = stepExecution.jobParameters.parameters["today"]
		if (todayParameter === null) return
		val today = LocalDate.parse(todayParameter.value.toString())
		val context = stepExecution.executionContext
		context.put("alarmCriteriaDate", today.plusDays(7).format(DateTimeFormatter.ISO_DATE))
		stepExecution.executionContext = context
	}

	override fun afterStep(stepExecution: StepExecution): ExitStatus? {
		return null
	}
}