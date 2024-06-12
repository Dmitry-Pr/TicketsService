package com.ticketingsystem.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
@ExperimentalEncodingApi
class JwtService {


    init {
        logger.info("Secret key: ${Base64.encode(SECRET_KEY.encoded)}")
    }

    fun extractNickname(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun extractEmail(token: String): String {
        return extractClaim(token) { claims -> claims["email"] as String }
    }

    fun extractUserId(token: String): Int {
        return try {
            (extractClaim(token) { claims -> claims["userId"] as String }).toInt()
        } catch (e: Exception) {
            -1
        }
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .payload

    fun generateToken(userDetails: UserDetails, userId: Int?, email: String): String {
        val claims = HashMap<String, Any>()
        claims["userId"] = userId.toString()
        claims["email"] = email
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000)) // 24 hours
            .issuer("spring-security")
            .signWith(SECRET_KEY)
            .compact()
    }


    fun validateToken(token: String): Boolean {
        return (!isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    companion object {
        private val jwtSecretKey = "zUKSU/yuV4rxDzfs2inwarrACkdLr1TXQs+7TP74rPE="
        private val SECRET_KEY = SecretKeySpec(DatatypeConverter.parseBase64Binary(jwtSecretKey), "HmacSHA256")
        private val logger = LoggerFactory.getLogger(JwtService::class.java)
    }
}