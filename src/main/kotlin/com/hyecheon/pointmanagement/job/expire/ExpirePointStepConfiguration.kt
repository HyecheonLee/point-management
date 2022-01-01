package com.hyecheon.pointmanagement.job.expire

import com.hyecheon.pointmanagement.point.Point
import com.hyecheon.pointmanagement.point.PointRepository
import com.hyecheon.pointmanagement.point.wallet.PointWalletRepository
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import javax.persistence.EntityManagerFactory

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@Configuration
class ExpirePointStepConfiguration {

	@Bean
	@JobScope
	fun expirePointStep(
		stepBuilderFactory: StepBuilderFactory,
		platformTransactionManager: PlatformTransactionManager,
		expirePointItemReader: JpaPagingItemReader<Point>,
		expirePointItemProcessor: ItemProcessor<Point, Point>,
		expirePointItemWriter: ItemWriter<Point>
	) = run {
		stepBuilderFactory["expirePointStep"]
			.allowStartIfComplete(true)
			.transactionManager(platformTransactionManager)
			.chunk<Point, Point>(1000)
			.reader(expirePointItemReader)
			.processor(expirePointItemProcessor)
			.writer(expirePointItemWriter)
			.build()
	}

	@Bean
	@StepScope
	fun expirePointItemReader(
		entityManagerFactory: EntityManagerFactory,
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
		today: LocalDate
	) = run {
		JpaPagingItemReaderBuilder<Point>()
			.name("expirePointItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select p from Point p where p.expireDate < :today and used = false and expired = false")
			.parameterValues(mapOf("today" to today))
			.pageSize(1000)
			.build()
	}

	@Bean
	@StepScope
	fun expirePointItemProcessor() = run {
		ItemProcessor<Point, Point> { point ->
			point.expired = true
			point.pointWallet.let { it.amount = it.amount?.subtract(point.amount) }
			point
		}
	}

	@Bean
	@StepScope
	fun expirePointItemWriter(
		pointRepository: PointRepository,
		pointWalletRepository: PointWalletRepository
	) = run {
		ItemWriter<Point> { points ->
			points.filter(Point::expired).forEach {
				pointRepository.save(it)
				pointWalletRepository.save(it.pointWallet)
			}
		}
	}
}