package ru.netology.repository;


import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepository {
    private final ConcurrentMap<Long, Post> allPosts;
    private final AtomicLong idCounter = new AtomicLong();

    public PostRepository() {
        this.allPosts = new ConcurrentHashMap<>();
    }

    public Collection<Post> all() {
        return allPosts.values();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(allPosts.get(id));
    }

   public Post save(Post savePost) {
        if (savePost.getId() == 0) {
            long id = idCounter.incrementAndGet();
            savePost.setId(id);
            allPosts.put(id, savePost);
        } else if (allPosts.containsKey(savePost.getId())) {
            allPosts.replace(savePost.getId(), savePost);
        } else {
            throw new NotFoundException();
        }
        return savePost;
    }

    public void removeById(long id) {
        allPosts.remove(id);
    }
}
