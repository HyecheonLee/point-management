package com.hyecheon.pointmanagement.job.message

import com.hyecheon.pointmanagement.message.Message
import com.hyecheon.pointmanagement.point.ExpiredPointSummary
import com.hyecheon.pointmanagement.point.PointRepository
import org.springframework.batch.core.StepExecutionListener
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
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import javax.persistence.EntityManagerFactory

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/09
 */
@Configuration
class MessageExpireSoonPointStepConfiguration {

	@JobScope
	@Bean
	fun messageExpireSoonPointStep(
		stepBuilderFactory: StepBuilderFactory,
		platformTransactionManager: PlatformTransactionManager,
		inputExpireSoonPointAlarmCriteriaDateStepListener: StepExecutionListener,
		messageExpireSoonPointItemReader: RepositoryItemReader<ExpiredPointSummary>,
		messageExpireSoonPointItemProcessor: ItemProcessor<ExpiredPointSummary, Message>,
		messageExpireSoonPointItemWriter: JpaItemWriter<Message>
	) = run {
		stepBuilderFactory["messageExpireSoonPointStep"]
			.allowStartIfComplete(true)
			.transactionManager(platformTransactionManager)
			.listener(inputExpireSoonPointAlarmCriteriaDateStepListener)
			.chunk<ExpiredPointSummary, Message>(1000)
			.reader(messageExpireSoonPointItemReader)
			.processor(messageExpireSoonPointItemProcessor)
			.writer(messageExpireSoonPointItemWriter)
			.build()
	}

	@Bean
	@StepScope
	fun messageExpireSoonPointItemReader(
		pointRepository: PointRepository,
		@Value("#{T(java.time.LocalDate).parse(jobParameters[alarmCriteriaDate])}")
		alarmCriteriaDate: LocalDate
	) = run {
		RepositoryItemReaderBuilder<ExpiredPointSummary>()
			.name("messageExpireSoonPointItemReader")
			.repository(pointRepository)
			.methodName("sumBeforeCriteriaDate")
			.pageSize(1000)
			.arguments(alarmCriteriaDate)
			.sorts(mapOf("pointWallet" to Sort.Direction.ASC))
			.build()
	}

	@Bean
	@StepScope
	fun messageExpireSoonPointItemProcessor(
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
		today: LocalDate
	) = run {
		ItemProcessor<ExpiredPointSummary, Message> {
			Message.expireSoonPointMessage(it.userId, today, it.amount)
		}
	}

	@Bean
	@StepScope
	fun messageExpireSoonPointItemWriter(
		entityManagerFactory: EntityManagerFactory
	) = run {
		val jpaItemWriter = JpaItemWriter<Message>()
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory)
		jpaItemWriter
	}
}