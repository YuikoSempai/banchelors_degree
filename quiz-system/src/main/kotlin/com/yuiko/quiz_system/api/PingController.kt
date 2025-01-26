package com.yuiko.quiz_system.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yuiko.quiz_system.api.PingApiController

@RestController
class PingController: PingApiController() {

    override fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("PONG")
    }
}