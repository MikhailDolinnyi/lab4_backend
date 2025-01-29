package ru.mikhail.lab4_backend.services

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import ru.mikhail.lab4_backend.Lab4BackendApplication
import ru.mikhail.lab4_backend.repository.DotRepository
import ru.mikhail.lab4_backend.service.DotService
import kotlin.test.assertTrue
import org.mockito.Mockito.`when`
import ru.mikhail.lab4_backend.authenticate.JwtCore

@SpringBootTest(classes = [Lab4BackendApplication::class])
class DotServiceTest {

    @MockBean
    private lateinit var dotRepository: DotRepository

    @MockBean
    private lateinit var dotService: DotService

    @MockBean
    private lateinit var jwtCore: JwtCore

    @Test
    fun `should return empty list when no dots`() {
        `when`(dotRepository.findAll()).thenReturn(emptyList())

        val result = dotService.getList()

        assertTrue(result.isEmpty(), "Expected empty list")
    }
}
