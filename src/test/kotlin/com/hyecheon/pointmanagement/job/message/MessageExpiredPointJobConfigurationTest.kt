package com.hyecheon.pointmanagement.job.message

import com.hyecheon.pointmanagement.BatchTestSupport
import com.hyecheon.pointmanagement.point.Point
import com.hyecheon.pointmanagement.point.wallet.PointWallet
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*

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
 * Date: 2022/01/06
 */
class MessageExpiredPointJobConfigurationTest : BatchTestSupport() {

	@Autowired
	lateinit var messageExpiredPointJob: Job

	@DisplayName("1. messageExpiredPointJob")
	@Test
	internal fun test_1() {
		//given
		val earnDate = LocalDate.of(2022, 1, 1)
		val expireDate = LocalDate.of(2022, 9, 5)
		val notExpireDate = LocalDate.of(2025, 12, 31)
		val pointWallet1 = pointWalletRepository.save(
			PointWallet("user1", BigInteger.valueOf(3000))
		)
		val pointWallet2 = pointWalletRepository.save(
			PointWallet("user2", BigInteger.ZERO)
		)

		pointRepository.save(
			Point(
				pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate,
				used = false,
				expired = true
			)
		)
		pointRepository.save(
			Point(
				pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate,
				used = false,
				expired = true
			)
		)
		pointRepository.save(
			Point(
				pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate,
				used = false,
				expired = true
			)
		)
		pointRepository.save(
			Point(
				pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate,
				used = false,
				expired = true
			)
		)
		pointRepository.save(
			Point(
				pointWallet1,
				BigInteger.valueOf(1000),
				earnDate,
				expireDate,
				used = false,
				expired = true
			)
		)
		pointRepository.save(Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate))
		pointRepository.save(Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate))
		pointRepository.save(Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate))


		//when
		val jobParameters = JobParametersBuilder()
			.addString("today", "2022-09-06")
			.toJobParameters()

		val execution = launchJob(messageExpiredPointJob, jobParameters)

		//then

		assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
		val messages = messageRepository.findAll()
		assertThat(messages).hasSize(2)
		val message = messages.find { it.userId == "user1" }
		assertThat(message).isNotNull
		if (message == null) return
		assertThat(message.title).isEqualTo("3000 포인트 만료")
		assertThat(message.content).isEqualTo("2022-09-06 기준 3000 포인트가 만료되었습니다.")
		val message2 = messages.find { it.userId == "user2" }
		assertThat(message2).isNotNull
		if (message2 == null) return
		assertThat(message2.title).isEqualTo("2000 포인트 만료")
		assertThat(message2.content).isEqualTo("2022-09-06 기준 2000 포인트가 만료되었습니다.")
	}
}