package com.howalog.api.service;

import com.howalog.api.domain.Post;
import com.howalog.api.exception.PostNotFound;
import com.howalog.api.repository.PostRepository;
import com.howalog.api.request.PostCreate;
import com.howalog.api.request.PostEdit;
import com.howalog.api.request.PostSearch;
import com.howalog.api.response.PostResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void write() {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(request);

        // then
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 단건 조회 - 제목 10자리")
    void get() {
        // given
        Post requestPost = Post.builder()
                .title("123456789012345")
                .content("내용")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertThat(response).isNotNull();
        assertEquals(1L, postRepository.count());
        assertThat(response.getTitle()).isEqualTo("1234567890");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("글 단 조회 시 존재하지 않는 글 에러 발생")
    void test3() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // expected
        PostNotFound e = assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test4() {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목_" + i)
                        .content("내용_" + i)
                        .build())
                .collect(toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> result = postService.getList(postSearch);

        // then
        assertEquals(10L, result.size());
        assertEquals("제목_19", result.get(0).getTitle());

    }

    @Test
    @DisplayName("없는 글 수정")
    void test5() {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("내용")
                .build();

        // expedted
        assertThatThrownBy(() -> postService.edit(0L, postEdit))
                .isInstanceOf(PostNotFound.class);
    }

    @Test
    @DisplayName("글 수정")
    void edit() {
        // given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목2")
                .content("내용1")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        Post updatedPost = postRepository.findById(post.getId()).get();

        // then
        assertEquals("제목2", updatedPost.getTitle());
        assertEquals("내용1", updatedPost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void test7() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

}