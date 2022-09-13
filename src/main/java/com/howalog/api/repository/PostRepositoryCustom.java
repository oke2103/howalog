package com.howalog.api.repository;

import com.howalog.api.domain.Post;
import com.howalog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
