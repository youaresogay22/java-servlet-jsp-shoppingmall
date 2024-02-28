package com.nhnacademy.shoppingmall.entity;

import com.nhnacademy.shoppingmall.common.config.RootConfig;
import com.nhnacademy.shoppingmall.common.config.WebConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@Transactional
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = WebConfig.class)
})
class usersTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testUsersEntity() {
        users user1 = entityManager.find(users.class, "admin");

        Assertions.assertEquals("admin", user1.getUserId());
        Assertions.assertEquals("관리자", user1.getUserName());
        Assertions.assertEquals("12345", user1.getUserPassword());
        Assertions.assertEquals("19000101", user1.getUserBirth());
        Assertions.assertEquals("ROLE_ADMIN", user1.getUserAuth());
        Assertions.assertEquals(1_560_000, user1.getUserPoint());
        Assertions.assertInstanceOf(LocalDateTime.class, user1.getAtCreated());
        Assertions.assertInstanceOf(LocalDateTime.class, user1.getLatestLoginAt());
    }

}