package com.hyecheon.pointmanagement

import com.hyecheon.pointmanagement.message.MessageRepository
import com.hyecheon.pointmanagement.point.PointRepository
import com.hyecheon.pointmanagement.point.reservation.PointReservationRepository
import com.hyecheon.pointmanagement.point.wallet.PointWalletRepository
import org.junit.jupiter.api.AfterEach
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2022/01/01
 */
@SpringBootTest
@ActiveProfiles("test")
abstract class BatchTestSupport {

	@Autowired
	lateinit var jobLauncher: JobLauncher

	@Autowired
	lateinit var jobRepository: JobRepository

	@Autowired
	lateinit var pointWalletRepository: PointWalletRepository

	@Autowired
	lateinit var pointRepository: PointRepository

	@Autowired
	lateinit var pointReservationRepository: PointReservationRepository

	@Autowired
	lateinit var messageRepository: MessageRepository

	fun launchJob(job: Job, jobParameters: JobParameters) = run {
		JobLauncherTestUtils().let {
			it.job = job
			it.jobLauncher = this.jobLauncher
			it.jobRepository = this.jobRepository
			it.launchJob(jobParameters)
		}
	}

	@AfterEach
	fun deleteAll() = run {
		pointRepository.deleteAll()
		pointReservationRepository.deleteAll()
		pointWalletRepository.deleteAll()
		messageRepository.deleteAll()
	}
}