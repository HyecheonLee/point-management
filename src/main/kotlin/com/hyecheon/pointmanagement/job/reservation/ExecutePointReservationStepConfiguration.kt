package com.hyecheon.pointmanagement.job.reservation

import com.hyecheon.pointmanagement.point.Point
import com.hyecheon.pointmanagement.point.PointRepository
import com.hyecheon.pointmanagement.point.reservation.PointReservation
import com.hyecheon.pointmanagement.point.reservation.PointReservationRepository
import com.hyecheon.pointmanagement.point.wallet.PointWallet
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
 * Date: 2022/01/04
 */
@Configuration
class ExecutePointReservationStepConfiguration {

	@Bean
	@JobScope
	fun executePointReservationStep(
		stepBuilderFactory: StepBuilderFactory,
		platformTransactionManager: PlatformTransactionManager,
		executePointReservationItemReader: JpaPagingItemReader<PointReservation>,
		executeReservationItemProcessor: ItemProcessor<PointReservation, Triple<PointReservation, Point, PointWallet>>,
		executePointReservationItemWriter: ItemWriter<Triple<PointReservation, Point, PointWallet>>
	) = run {
		stepBuilderFactory["executePointReservationStep"]
			.allowStartIfComplete(true) //중복으로 여러번 실행 가능
			.transactionManager(platformTransactionManager)
			.chunk<PointReservation, Triple<PointReservation, Point, PointWallet>>(1000)
			.reader(executePointReservationItemReader)
			.processor(executeReservationItemProcessor)
			.writer(executePointReservationItemWriter)
			.build()
	}

	@Bean
	@StepScope
	fun executePointReservationItemReader(
		entityManagerFactory: EntityManagerFactory,
		@Value("#{T(java.time.LocalDate).parse(jobParameters[today])}") today: LocalDate
	) = run {
		JpaPagingItemReaderBuilder<PointReservation>()
			.name("executePointReservationItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select pr from PointReservation pr where pr.earnedDate = :today and pr.executed = false")
			.parameterValues(mapOf("today" to today))
			.pageSize(1000)
			.build()
	}

	@Bean
	@StepScope
	fun executeReservationItemProcessor() = run {
		ItemProcessor<PointReservation, Triple<PointReservation, Point, PointWallet>> { reservation: PointReservation ->
			reservation.executed = true

			val earnedPoint =
				Point(reservation.pointWallet, reservation.amount, reservation.earnedDate, reservation.getExpireDate())
			val pointWallet = reservation.pointWallet
			pointWallet.amount = pointWallet.amount?.add(earnedPoint.amount)

			Triple(reservation, earnedPoint, pointWallet)
		}
	}

	@Bean
	@StepScope
	fun executePointReservationItemWriter(
		pointReservationRepository: PointReservationRepository,
		pointRepository: PointRepository,
		pointWalletRepository: PointWalletRepository
	) = run {
		ItemWriter<Triple<PointReservation, Point, PointWallet>> { items ->
			items.forEach {
				pointReservationRepository.save(it.first)
				pointRepository.save(it.second)
				pointWalletRepository.save(it.third)
			}
		}
	}
}