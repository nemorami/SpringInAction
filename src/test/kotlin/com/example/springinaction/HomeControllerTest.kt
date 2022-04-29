package com.example.springinaction

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest
internal class HomeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun home() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(view().name("home"))
            .andExpect(MockMvcResultMatchers.content().string(containsString("Welcome to...")))
            .andDo(MockMvcResultHandlers.print())
    }
}