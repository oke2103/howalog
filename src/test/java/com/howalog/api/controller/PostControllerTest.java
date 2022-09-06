package com.howalog.api.controller;

import com.howalog.api.domain.Post;
import com.howalog.api.repository.PostRepository;
import com.howalog.api.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/post 호출 시 정상 호출")
    void test() throws Exception {
        // expected
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"제목입니다.\", \"content\" : \"내용입니다.\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 호출 시 title 은 필수값")
        void test2 () throws Exception {
        // expected
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"\", \"content\" : \"\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 호출 시 db에 값이 저장된다.")
        void test3 () throws Exception {
        // when
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"제목입니다.\", \"content\" : \"내용입니다.\"}")
        )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");

    }
}
