package com.github.vincemann.springlemon.authtests;

import com.github.vincemann.springlemon.auth.LemonProperties;
import com.github.vincemann.springlemon.auth.config.LemonAdminAutoConfiguration;
import com.github.vincemann.springlemon.auth.domain.AbstractUser;
import com.github.vincemann.springlemon.auth.domain.AbstractUserRepository;
import com.github.vincemann.springlemon.auth.domain.LemonRoles;
import com.github.vincemann.springlemon.auth.mail.MailSender;
import com.github.vincemann.springlemon.auth.service.UserService;
import com.github.vincemann.springlemon.authtests.adapter.LemonTestAdapter;
import com.github.vincemann.springrapid.acl.proxy.AclManaging;
import com.github.vincemann.springrapid.acl.proxy.Unsecured;
import com.github.vincemann.springrapid.core.service.exception.BadEntityException;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQL)
//see application-dev.yml config for expected database config
//@Sql({"/test-data/resetTestData.sql"})

/**
 * Fills tokens Map in an integration test manner by creating and logging all users in
 */
//@Transactional - dont do transactional bc controller will be wrapped in transaction as well -> lazyLoad Exceptions ect. wont be detected
@SpringBootTest({
//        "logging.level.com.naturalprogrammer=ERROR", // logging.level.root=ERROR does not work: https://stackoverflow.com/questions/49048298/springboottest-not-overriding-logging-level
//        "logging.level.org.springframework=ERROR",
        "lemon.recaptcha.sitekey="
})
//activate everything for full integration tests
@ActiveProfiles(value = {"web", "service", "test", "webTest", "serviceTest", "dev"}, inheritProfiles = false)
@ImportAutoConfiguration(exclude = LemonAdminAutoConfiguration.class)
@Getter
public abstract class AbstractMvcTests {

    protected static final String ADMIN_EMAIL = "admin@example.com";
    protected static final String ADMIN_PASSWORD = "admin!";

    protected static final String SECOND_ADMIN_EMAIL = "secondAdmin@example.com";
    protected static final String SECOND_ADMIN_PASSWORD = "admin!second";

    protected static final String BLOCKED_ADMIN_EMAIL = "blockedAdmin@example.com";
    protected static final String BLOCKED_ADMIN_PASSWORD = "admin123!blocked";

    protected static final String USER_EMAIL = "user@example.com";
    protected static final String USER_PASSWORD = "Sanjay99!";

    protected static final String UNVERIFIED_USER_EMAIL = "unverifiedUser@example.com";
    protected static final String UNVERIFIED_USER_PASSWORD = "Sanjay99!unverified";

    protected static final String BLOCKED_USER_EMAIL = "blockedUser@example.com";
    protected static final String BLOCKED_USER_PASSWORD = "Sanjay99!blocked";

//    private static boolean initialized = false;

    @Autowired
    @AclManaging
    private UserService<AbstractUser<Long>, Long> aclUserService;

    //cant autowire if used with types, even with ? extends AbstractUser
    @Autowired
    @Unsecured
    private UserService<AbstractUser<Long>, Long> unsecuredUserService;

    //cant autowire if used with types, even with ? extends AbstractUser
    @Autowired
    private AbstractUserRepository/*<AbstractUser<Long>, Long>*/ userRepository;

    @SpyBean
    protected MailSender<?> mailSender;

    @Autowired
    protected DataSource dataSource;

    //use for stubbing i.E. Mockito.doReturn(mockedExpireTime).when(jwt).getExpirationMillis();
    @SpyBean
    protected LemonProperties properties;
    protected LemonProperties.Jwt jwt;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;
    protected Map<Long, String> tokens = new HashMap<>(6);

    private AbstractUser<Long> admin;
    private AbstractUser<Long> secondAdmin;
    private AbstractUser<Long> blockedAdmin;
    private AbstractUser<Long> user;
    private AbstractUser<Long> unverifiedUser;
    private AbstractUser<Long> blockedUser;

    @Autowired
    protected LemonTestAdapter testAdapter;

    @Autowired
    protected LemonProperties lemonProperties;

    @BeforeEach
    public void setup() throws Exception {
        configureMvc();
        System.err.println("creating test users");
        createTestUsers();
        System.err.println("test users created");
        System.err.println("logging in test users");
        loginTestUsers();
        System.err.println("test users logged in");
        setupSpies();
    }

    protected void setupSpies(){
        jwt = Mockito.spy(properties.getJwt());
        Mockito.doReturn(jwt).when(properties).getJwt();
    }

    protected void configureMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }


    protected void removeTestUsers(){
        System.err.println("deleting users");
        userRepository.deleteAll();
        System.err.println("deleted users");

        System.err.println("deleting acl info");
        Connection connection = DataSourceUtils.getConnection(dataSource);
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("test-data/removeAclInfo.sql"));
        DataSourceUtils.releaseConnection(connection,dataSource);
        System.err.println("deleted acl info");
    }


    protected void createTestUsers() throws BadEntityException, InterruptedException {
        admin = aclUserService.save(testAdapter.createTestUser(ADMIN_EMAIL,/*"Admin",*/ ADMIN_PASSWORD, LemonRoles.ADMIN));
        secondAdmin = aclUserService.save(testAdapter.createTestUser(SECOND_ADMIN_EMAIL,/*"Second Admin",*/ SECOND_ADMIN_PASSWORD, LemonRoles.ADMIN));
        blockedAdmin = aclUserService.save(testAdapter.createTestUser(BLOCKED_ADMIN_EMAIL,/*"Blocked Admin",*/ BLOCKED_ADMIN_PASSWORD, LemonRoles.ADMIN, LemonRoles.BLOCKED));

        user = aclUserService.save(testAdapter.createTestUser(USER_EMAIL,/*"User",*/ USER_PASSWORD, LemonRoles.USER));
        unverifiedUser = aclUserService.save(testAdapter.createTestUser(UNVERIFIED_USER_EMAIL,/*"Unverified User",*/ UNVERIFIED_USER_PASSWORD, LemonRoles.USER,LemonRoles.UNVERIFIED));
        blockedUser = aclUserService.save(testAdapter.createTestUser(BLOCKED_USER_EMAIL,/*"Blocked User",*/ BLOCKED_USER_PASSWORD, LemonRoles.USER, LemonRoles.BLOCKED));
        // sleep so login shortly after wont result in obsolete token
        Thread.sleep(400);
    }



    protected void loginTestUsers() throws Exception {
        tokens.put(getAdmin().getId(), successful_login(ADMIN_EMAIL, ADMIN_PASSWORD));
        tokens.put(getSecondAdmin().getId(), successful_login(SECOND_ADMIN_EMAIL, SECOND_ADMIN_PASSWORD));
        tokens.put(getBlockedAdmin().getId(), successful_login(BLOCKED_ADMIN_EMAIL, BLOCKED_ADMIN_PASSWORD));

        tokens.put(getUser().getId(), successful_login(USER_EMAIL, USER_PASSWORD));
        tokens.put(getUnverifiedUser().getId(), successful_login(UNVERIFIED_USER_EMAIL, UNVERIFIED_USER_PASSWORD));
        tokens.put(getBlockedUser().getId(), successful_login(BLOCKED_USER_EMAIL, BLOCKED_USER_PASSWORD));
    }

    protected ResultActions login(String userName, String password) throws Exception {
        return mvc.perform(post(lemonProperties.getController().getLoginUrl())
                .param("username", userName)
                .param("password", password)
                .header("contentType", MediaType.APPLICATION_FORM_URLENCODED));
    }

    protected String successful_login(String userName, String password) throws Exception {
        MvcResult result = login(userName,password)
                .andExpect(status().is(200))
                .andReturn();

        return result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
    }

    protected String successful_login(String username, String password, long expirationMillis) throws Exception {
        Mockito.doReturn(expirationMillis).when(jwt).getExpirationMillis();
        return successful_login(username,password);
    }

    protected void ensureTokenWorks(String token) throws Exception {
        mvc.perform(get("/api/core/context")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(200));
//                .andExpect(jsonPath("$.user.id").value(getUnverifiedUser().getId()));
    }

    @AfterEach
    void tearDown() throws SQLException {
        System.err.println("clearing test data");
        tokens.clear();
        removeTestUsers();
        System.err.println("test data cleared");
        Mockito.reset(properties);
        Mockito.reset(jwt);
    }

    //    protected void initAcl() throws SQLException {
//        if (!initialized) {
//            //only do this expensive stuff once -> permissions stay the same
//            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("test-data/removeAclInfo.sql"));
////        User admin = userRepository.findById(getAdmin().getId()).get();
////        Authentication adminAuth = new UsernamePasswordAuthenticationToken(admin.getName(), admin.getPassword()
////                , Lists.newArrayList(new SimpleGrantedAuthority(Role.ADMIN)));
//            securityContext.runAsAdmin(() -> {
//                try {
//                    giveAdminFullPermissionOver(getUser().getId(), getUnverifiedUser().getId(), BLOCKED_USER_ID /*getAdmin().getId(), secondAdmin.getId(), blockedAdmin.getId()*/);
//                    giveFullPermissionAboutSelf(getAdmin().getId(), secondAdmin.getId(), blockedAdmin.getId(), getUser().getId(), getUnverifiedUser().getId(), BLOCKED_USER_ID);
//                }catch (Exception e){
//                    throw new RuntimeException(e);
//                }
//            });
//            initialized = true;
//        }
//    }

//    protected void giveFullPermissionAboutSelf(Long... ids) throws BadEntityException {
//        for (Long id : ids) {
//            AbstractUser user = userService.findById(id).get();
//            permissionService.addPermissionForUserOver(user, BasePermission.ADMINISTRATION, user.getEmail());
//        }
//    }
//
//    protected void giveAdminFullPermissionOver(Long... ids) throws BadEntityException {
//        for (Long id : ids) {
//            AbstractUser user = userService.findById(id).get();
//            permissionService.addPermissionForAuthorityOver(user, BasePermission.ADMINISTRATION, RapidRoles.ADMIN);
//        }
//    }


}

