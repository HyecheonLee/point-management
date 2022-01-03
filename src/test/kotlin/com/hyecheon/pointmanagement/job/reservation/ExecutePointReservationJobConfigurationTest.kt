package com.hyecheon.pointmanagement.job.reservation

import com.hyecheon.pointmanagement.BatchTestSupport
import com.hyecheon.pointmanagement.point.reservation.PointReservation
import com.hyecheon.pointmanagement.point.wallet.PointWallet
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigInteger
import java.time.LocalDate

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/04
 */

class ExecutePointReservationJobConfigurationTest : BatchTestSupport() {

	@Autowired
	lateinit var executePointReservationJob: Job

	@DisplayName("1. executePointReservationJob")
	@Test
	internal fun test_1() {
		//given
		val earnDate = LocalDate.of(2021, 1, 5)

		val pointWallet = pointWalletRepository.save(
			PointWallet("user1", BigInteger.valueOf(3000))
		)
		pointReservationRepository.save(
			PointReservation(
				pointWallet, BigInteger.valueOf(1000),
				earnDate,
				10
			)
		)

		//when

		val jobParameters = JobParametersBuilder()
			.addString("today", "2021-01-05")
			.toJobParameters()

		val jobExecution = launchJob(executePointReservationJob, jobParameters)

		//then
		then(jobExecution.exitStatus).isEqualTo(ExitStatus.COMPLETED)

		val reservations = pointReservationRepository.findAll()
		then(reservations).hasSize(1)
		then(reservations[0].executed).isTrue

		val points = pointRepository.findAll()
		then(points).hasSize(1)
		then(points[0].amount).isEqualByComparingTo(BigInteger.valueOf(1000))
		then(points[0].earnedDate).isEqualTo(LocalDate.of(2021, 1, 5))
		then(points[0].expireDate).isEqualTo(LocalDate.of(2021, 1, 15))

		val wallets = pointWalletRepository.findAll()
		then(points).hasSize(1)
		then(wallets[0].amount).isEqualByComparingTo(BigInteger.valueOf(4000))
	}
}