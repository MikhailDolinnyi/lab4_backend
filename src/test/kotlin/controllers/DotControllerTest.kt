package ru.mikhail.lab4_backend.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.mikhail.lab4_backend.authenticate.JwtCore
import ru.mikhail.lab4_backend.authenticate.UserDetailsImpl
import ru.mikhail.lab4_backend.service.UserService

@SpringBootTest
@AutoConfigureMockMvc
class DotControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Autowired
    private lateinit var jwtCore: JwtCore

    @MockBean
    private lateinit var userService: UserService

    private lateinit var validToken: String

    @BeforeEach
    fun setup() {

        val userDetails = UserDetailsImpl(1L, "testUser", "testPassword")
        Mockito.`when`(userService.loadUserByUsername("testUser")).thenReturn(userDetails)


        val authentication = Mockito.mock(org.springframework.security.core.Authentication::class.java)
        Mockito.`when`(authentication.principal).thenReturn(userDetails)


        validToken = jwtCore.generateToken(authentication)
            ?: throw IllegalStateException("Token generation failed")
    }

    @Test
    fun `should return 401 when no token is provided`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/dot/get-list"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `should return 200 when valid token is provided`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/dot/get-list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $validToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }
}
