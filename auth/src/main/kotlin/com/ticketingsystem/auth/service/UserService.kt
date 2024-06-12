package com.ticketingsystem.auth.service

import com.ticketingsystem.auth.api.model.AuthenticationRequest
import com.ticketingsystem.auth.api.model.RegistrationRequest
import com.ticketingsystem.auth.model.Session
import com.ticketingsystem.auth.model.User
import com.ticketingsystem.auth.repository.SessionRepository
import com.ticketingsystem.auth.repository.UserRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestBody
import java.sql.Timestamp
import java.time.Instant
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
@ExperimentalEncodingApi
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val defaultUserDetailsService: DefaultUserDetailsService,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val jwtService: JwtService,
    private val validationService: ValidationService
) {
    @Transactional
    fun registerUser(@RequestBody registrationRequest: RegistrationRequest): HttpEntity<String> {
        if (userRepository.findByEmail(registrationRequest.email) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists")

        if (!validationService.isValidEmail(registrationRequest.email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address")

        if (!validationService.isValidPassword(registrationRequest.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("The password must be at least eight characters long, including uppercase and lowercase letters, digits, and special characters")
        }

        val encodedPassword = passwordEncoder.encode(registrationRequest.password)
        userRepository.save(
            User(
                nickname = registrationRequest.nickname,
                email = registrationRequest.email,
                password = encodedPassword,
                created = Timestamp.from(Instant.now())
            )
        )

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
    }

    @Transactional
    fun loginUser(@RequestBody authenticationRequest: AuthenticationRequest, response: HttpServletResponse): HttpEntity<String> {
        val user = userRepository.findByEmail(authenticationRequest.email) ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("User not found")

        if (!user.comparePassword(authenticationRequest.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password")
        }

        val userDetails: UserDetails =
            defaultUserDetailsService.loadUserByUsername(authenticationRequest.email)
        val jwt = jwtService.generateToken(userDetails, user.getId(), user.getEmail())

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        val expires = Timestamp(System.currentTimeMillis() + 60 * 60 * 24 * 1000) // 24 hours
        sessionRepository.save(
            Session(
                userId = user.getId(),
                token = jwt,
                expires = expires
            )
        )

        response.addCookie(cookie)

        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully")
    }

    @Transactional
    fun getUser(jwt: String?): ResponseEntity<Any> {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No JWT token provided")
        }
        try {
            val user = userRepository.findByEmail(jwtService.extractEmail(jwt)) ?: return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
            )
                .body("User not found")

            return ResponseEntity.status(HttpStatus.OK).body(user)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token")
        }
    }

    @Transactional
    fun userLogout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.status(HttpStatus.OK).body("success")
    }

}