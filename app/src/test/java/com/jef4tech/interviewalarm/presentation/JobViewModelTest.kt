package com.jef4tech.interviewalarm.presentation

import app.cash.turbine.test
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.model.JobStatus
import com.jef4tech.interviewalarm.domain.model.JobType
import com.jef4tech.interviewalarm.domain.usecase.GetUpcomingInterviewsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class JobViewModelTest {

    private val getUpcomingInterviewsUseCase: GetUpcomingInterviewsUseCase = mockk()
    private lateinit var classUnderTest: JobViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUpcomingInterviews updates state with list of jobs`() = runTest {
        // Given
        val mockJob = Job(
            id = "1", title = "Android Engineer", company = "Google",
            type = JobType.REMOTE, salaryRange = null, location = null,
            interviewDateTime = LocalDateTime.now().plusDays(1), status = JobStatus.INTERVIEWING
        )
        
        every { getUpcomingInterviewsUseCase() } returns flowOf(listOf(mockJob))
        
        // When
        classUnderTest = JobViewModel(getUpcomingInterviewsUseCase)
        
        // Then
        classUnderTest.uiState.test {
            // initial state before flow collect starts
            val initialState = awaitItem()
            assertEquals(false, initialState.isLoading) // As the test dispatcher hasn't advanced yet
            
            // Advance dispatcher to execute coroutine
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Expected loaded state
            val loadedState = expectMostRecentItem()
            assertEquals(false, loadedState.isLoading)
            assertEquals(1, loadedState.upcomingInterviews.size)
            assertEquals("Android Engineer", loadedState.upcomingInterviews.first().title)
        }
    }
}
