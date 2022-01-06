package com.hyecheon.pointmanagement.job.message

import com.hyecheon.pointmanagement.job.listener.InputExpiredPointAlarmCriteriaDateStepListener
import com.hyecheon.pointmanagement.message.Message
import com.hyecheon.pointmanagement.point.ExpiredPointSummary
import com.hyecheon.pointmanagement.point.PointRepository
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import javax.persistence.EntityManagerFactory

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/06
 */
@Configuration
class MessageExpiredPointStepConfiguration {

	@Bean
	@JobScope
	fun messageExpiredPointStep(
		stepBuilderFactory: StepBuilderFactory,
		platformTransactionManager: PlatformTransactionManager,
		listener: InputExpiredPointAlarmCriteriaDateStepListener,
		messageExpiredPointItemReader: RepositoryItemReader<ExpiredPointSummary>,
		messageExpiredPointProcessor: ItemProcessor<ExpiredPointSummary, Message>,
		messageExpiredWriter: JpaItemWriter<Message>
	) = run {
		stepBuilderFactory["messageExpiredPointStep"]
			.allowStartIfComplete(true)
			.transactionManager(platformTransactionManager)
			.listener(listener)
			.chunk<ExpiredPointSummary, Message>(1000)
			.reader(messageExpiredPointItemReader)
			.processor(messageExpiredPointProcessor)
			.writer(messageExpiredWriter)
			.build()
	}

	@Bean
	@StepScope
	fun messageExpiredPointItemReader(
		pointRepository: PointRepository,
		@Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
		alarmCriteriaDate: LocalDate
	) = run {
		RepositoryItemReaderBuilder<ExpiredPointSummary>()
			.name("messageExpiredPointItemReader")
			.repository(pointRepository)
			.methodName("sumByExpiredDate")
			.pageSize(1000)
			.arguments(alarmCriteriaDate)
			.sorts(mapOf("pointWallet" to Sort.Direction.ASC))
			.build()
	}

	@Bean
	@StepScope
	fun messageExpiredPointItemProcessor(
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
		today: LocalDate
	) = run {
		ItemProcessor<ExpiredPointSummary, Message> {
			Message.of(it.userId, today, it.amount)
		}
	}

	@Bean
	@StepScope
	fun messageExpiredPointItemWriter(
		entityManagerFactory: EntityManagerFactory
	) = run {
		val jpaItemWriter = JpaItemWriter<Message>()
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory)
		jpaItemWriter
	}
}