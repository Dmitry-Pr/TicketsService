package com.ticketingsystem.auth.controller

import com.ticketingsystem.auth.api.model.AuthenticationRequest
import com.ticketingsystem.auth.api.model.RegistrationRequest
import com.ticketingsystem.auth.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import kotlin.io.encoding.ExperimentalEncodingApi

@RestController
@RequestMapping
@ExperimentalEncodingApi
class AuthController (
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@RequestBody registrationRequest: RegistrationRequest) = userService.registerUser(registrationRequest)

    @PostMapping("/login")
    fun login(@RequestBody authenticationRequest: AuthenticationRequest, response: HttpServletResponse) = userService.loginUser(authenticationRequest, response)

    @GetMapping("/user")
    fun user(@CookieValue("jwt") jwt: String?) = userService.getUser(jwt)

    @PostMapping("/logout-user")
    fun logout(response: HttpServletResponse) = userService.userLogout(response)

}