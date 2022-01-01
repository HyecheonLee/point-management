package com.hyecheon.pointmanagement.job.expire

import com.hyecheon.pointmanagement.BatchTestSupport
import com.hyecheon.pointmanagement.point.Point
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
 * Date: 2022/01/01
 */
class ExpirePointJobConfigurationTest : BatchTestSupport() {

	@Autowired
	lateinit var expirePointJob: Job

	@DisplayName("1. expirePointJob")
	@Test
	fun test_1() {

		//given
		val earnDate = LocalDate.of(2022, 1, 1)
		val expireDate = LocalDate.of(2022, 1, 3)
		val pointWallet = pointWalletRepository.save(
			PointWallet("user123", BigInteger.valueOf(6000))
		)
		pointRepository.save(Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate))
		pointRepository.save(Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate))
		pointRepository.save(Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate))

		//when
		val jobParameters = JobParametersBuilder().addString("today", "2022-01-04")
			.toJobParameters()
		val execution = launchJob(expirePointJob, jobParameters)

		//then
		then(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)

		val points = pointRepository.findAll()
		then(points.stream().filter { it.expired }).hasSize(3)

		val changedPointWallet = pointWalletRepository.findById(pointWallet.id!!).orElseGet { null }
		then(changedPointWallet).isNotNull
		then(changedPointWallet.amount).isEqualByComparingTo(BigInteger.valueOf(3000))
	}
}