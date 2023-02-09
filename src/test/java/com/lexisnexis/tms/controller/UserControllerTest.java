package com.lexisnexis.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.UserLogin;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.LoginRepository;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.services.LoginService;
import com.lexisnexis.tms.services.PdfService;
import com.lexisnexis.tms.services.UserPdfExporter;
import com.lexisnexis.tms.services.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.lexisnexis.tms.services")
@AutoConfigureMockMvc
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class UserControllerTest {
    @InjectMocks
    UserController userController;


    @Autowired
    MockMvc mockmvc;

    @Mock
    UserService userService;

    @Mock
    PasswEncrypt passwEncrypt;

    @Mock
    LoginService loginService;

    @Mock
    PdfService pdfService;

    @Mock
    LoginRepository loginRepository;

    @Mock
    private UserPdfExporter userPdfExporter ;

    private UserEntity testUser;

    private WorkHistory testWorkHistory;

    private LoginDto testloginUser;

    private UserLogin testLoggedInUser;

    private APIResponse apiResponse;

    private  List<WorkHistory> workHistoryList;

    private List<UserEntity> userDetailsList;

    private ChangePassword testChangePassword;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockmvc = MockMvcBuilders.standaloneSetup(userController).build();
        testUser = new UserEntity();
        testUser.setUserName("ravikant");
        testUser.setFirstName("Ravikant");
        testUser.setLastName("Madas");
        testUser.setEmail("ravi@gmail.com");
        testUser.setMobileNo("9175950073");
        testUser.setPassword("Ravi@123");
        testUser.setLocation("Solapur");

        testWorkHistory = new WorkHistory();
        testWorkHistory.setUserName("ravikant");
        testWorkHistory.setWorkingArea("Registration of TMS");
        testWorkHistory.setComments("Done..");

        testloginUser = new LoginDto();
        testloginUser.setUserName("ravikant");
        testloginUser.setPassword("ravi@123");

        testLoggedInUser = new UserLogin();
        testLoggedInUser.setIsLocked(Boolean.FALSE);
        testLoggedInUser.setUserName(testloginUser.getUserName());
        testLoggedInUser.onSave();
        testLoggedInUser.setLoginStatus(Boolean.TRUE);
        testLoggedInUser.setFailureAttempts(0);
        loginRepository.save(testLoggedInUser);

        workHistoryList=new ArrayList<>();
        WorkHistory workHistoryUser1=new WorkHistory();
        workHistoryUser1.setUserName("ravikant");
        workHistoryUser1.setWorkingArea("Registration of TMS");
        workHistoryUser1.setComments("Done..");
        WorkHistory workHistoryUser2=new WorkHistory();
        workHistoryUser2.setUserName("Prem");
        workHistoryUser2.setWorkingArea("Registration of TMS");
        workHistoryUser2.setComments("Done..");
        workHistoryList.add(workHistoryUser2);
        workHistoryList.add(workHistoryUser1);

        userDetailsList=new ArrayList<>();
        userDetailsList.add(testUser);

        testChangePassword=new ChangePassword();
        testChangePassword.setOldPassword("Ravi@123");
        testChangePassword.setNewPassword("Ravikant@123");
    }

    @Test
    void importCsvToDBJob() {
    }

    @Test
    @Order(1)
    void testRegisterNewUserShouldReturnSuccess() throws Exception {
        Mockito.when(userService.registerNewUser(testUser)).thenReturn("user registration ");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testUser);
        mockmvc.perform(MockMvcRequestBuilders.post("/tms/api/v1/register")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    @Order(2)
    void testLogin() throws Exception {
        apiResponse=new APIResponse();
        apiResponse.setData("UserEntity logged in");
        apiResponse.setStatus(200);
        Mockito.when(loginService.login(testloginUser)).thenReturn(apiResponse);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testloginUser);
        mockmvc.perform(MockMvcRequestBuilders.post("/tms/api/v1/login")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(200))
                .andDo(print());
    }

    @Test
    @Order(3)
    void testSaveWorkHistory() throws Exception {
        Mockito.when(userService.updateWorkHistory(testWorkHistory)).thenReturn(testWorkHistory);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testWorkHistory);
        mockmvc.perform(MockMvcRequestBuilders.post("/tms/api/v1/workHistory")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(4)
    void testCreatePdf(@Autowired  MockHttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);
        final String currentDateTime = dateFormatter.format(new Date());
        final String headerKey = "Content-Disposition";
        final String headerValue = "attachment; filename=logWork_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        Mockito.when(pdfService.getAll()).thenReturn(workHistoryList);
        Mockito.doNothing().when(userPdfExporter).export(response,workHistoryList);
        mockmvc.perform(MockMvcRequestBuilders
                        .get("/tms/api/v1/users/report"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void testFetchAllEmpDetail() throws Exception {
        Mockito.when(userService.fetchAllUserDetail()).thenReturn(userDetailsList);
        mockmvc.perform(MockMvcRequestBuilders
                        .get("/tms/api/v1/getAllUserDetails")
                )
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(6)
    void testGetData() throws Exception {
        Mockito.when(userService.getDataByUserName("ravikant")).thenReturn(testUser);
        mockmvc.perform(MockMvcRequestBuilders
                        .get("/tms/api/v1//getDataByUsername/ravikant")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void testUpdateUser() throws Exception {
        testUser = new UserEntity();
        testUser.setUserName("ravikant");
        testUser.setFirstName("Ravikant");
        testUser.setLastName("Madas");
        testUser.setEmail("ravi@gmail.com");
        testUser.setMobileNo("9175950073");
        testUser.setPassword("Ravi@123");
        testUser.setLocation("Solapur");
        Mockito.when(userService.updateUser(testUser)).thenReturn("UserEntity Data updated successfully");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testUser);
        mockmvc.perform(MockMvcRequestBuilders.post("/tms/api/v1/updateUser")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
//                .andExpect(content().string("UserEntity Data updated successfully"));
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(8)
    void testForgotPassword() throws Exception {
        Mockito.when(userService.forgotPassword(testUser)).thenReturn("Password has been update successfully");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(testUser);
        mockmvc.perform(MockMvcRequestBuilders.post("/tms/api/v1/forgotPassword")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void testChangePassword() throws Exception {
        testChangePassword=new ChangePassword();
        testChangePassword.setOldPassword("Ravi@123");
        testChangePassword.setNewPassword("Ravikant@123");
        Mockito.when(userService.changePassword("ravikant",testChangePassword)).thenReturn("Password Changed successfully");
        mockmvc.perform(
                        MockMvcRequestBuilders.post("/tms/api/v1/changePassword/ravikant")
                                .content(JSONObject.toJSONString(
                                        Map.of("newPassword", "ravi@12345",
                                                "oldPassword", "ravi@123")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
//                .andExpect(content().string("Password Changed successfully"));
                .andExpect(status().isOk());
    }
    @Test
    @Order(10)
    void testDeleteDataByUserName() throws Exception {
        Mockito.doNothing().when(userService).deleteDataByUserName("ravikant");
        mockmvc.perform(MockMvcRequestBuilders
                        .delete("/tms/api/v1/deleteDataByUsername/ravikant"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("UserEntity removed successfully ravikant"));
    }
}