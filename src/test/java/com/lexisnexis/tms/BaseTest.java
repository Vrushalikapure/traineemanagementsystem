package com.lexisnexis.tms;

import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.WorkHistory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface BaseTest {

    @RegisterExtension
    WorkHistoryResolver workHistoryResolver = new WorkHistoryResolver();

    @RegisterExtension
    UserResolver userResolver = new UserResolver();

    UserEntity user = new UserEntity();

    @BeforeAll
    @BeforeEach
    default void clearMocks(){
        Mockito.framework().clearInlineMocks();
        MockitoAnnotations.initMocks(this);
    }

    class WorkHistoryResolver implements ParameterResolver {

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == WorkHistory.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            WorkHistory workHistory = new WorkHistory();
            workHistory.setLoginAt(LocalDateTime.of(2023, 1, 31,0, 0));
            workHistory.setComments("working hard");
            workHistory.setUserName("admin1");
            workHistory.setWorkingArea("some working area");
            return workHistory;
        }
    }

    class UserResolver implements ParameterResolver {

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == UserEntity.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            UserEntity user = new UserEntity();
            user.setUserName("admin1");
            user.setFirstName("admin");
            user.setLastName("1");
            user.setMobileNo("12358671");
            user.setEmail("some@email.com");
            user.setLocation("someplace");
            user.setPassword("password");
            return user;
        }
    }
}