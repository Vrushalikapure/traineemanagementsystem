package com.lexisnexis.tms.controller;

import com.lexisnexis.tms.dto.ChangePassword;
import com.lexisnexis.tms.dto.LoginDto;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.exception.UserNotLoginException;
import com.lexisnexis.tms.exception.UserNotLoginExceptions;
import com.lexisnexis.tms.exception.UserPasswordDoesNotMatching;
import com.lexisnexis.tms.response.APIResponse;
import com.lexisnexis.tms.services.LoginService;
import com.lexisnexis.tms.services.PdfService;
import com.lexisnexis.tms.services.UserPdfExporter;
import com.lexisnexis.tms.services.UserService;
import com.lexisnexis.tms.util.PasswEncrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/tms/api/v1")
public class UserController {
//    @Value("${dbusername}")
//    private String dbusername;
//    @Value("${dbpassword}")
//    private String dbpassword;

    @Autowired
    UserService userService;

    @Autowired
    PasswEncrypt passwEncrypt;

    @Autowired
    LoginService loginService;

    @Autowired
    PdfService pdfService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    UserPdfExporter userPdfExporter;

    @Autowired
    private Job job;


    private static Logger logger = LogManager.getLogger();

    @PostMapping("/importUsers")
    public void importCsvToDBJob() {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException batchException) {
            logger.info(batchException.getMessage());
        }
    }


    // Register api
    @PostMapping("/register")
    public String registerNewUser(@RequestBody @Valid UserEntity userEntity) throws NoSuchAlgorithmException {
        userEntity.setPassword(passwEncrypt.encryptPass(userEntity.getPassword()));
        return userService.registerNewUser(userEntity);
    }

    @PostMapping("/workHistory")
    public String saveWorkHistory(@RequestBody @Valid WorkHistory workHistory)
            throws UserNotLoginException, UserNotFoundException {
        userService.updateWorkHistory(workHistory);
        return "work history updated";
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody @Valid LoginDto loginDto) throws InterruptedException, NoSuchAlgorithmException {
        final APIResponse login = loginService.login(loginDto);
        return ResponseEntity.status(login == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(login);
    }

    @GetMapping("/users/report")
    public void createpdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH);
        final String currentDateTime = dateFormatter.format(new Date());
        final String headerKey = "Content-Disposition";
        final String headerValue = "attachment; filename=logWork_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        final List<WorkHistory> list = pdfService.getAll();
        userPdfExporter.export(response, list);
    }

    @GetMapping("/getAllUserDetails")
    public ResponseEntity<List<UserEntity>> fetchAllEmpDetail() throws UserNotFoundException {
        return new ResponseEntity<>(this.userService.fetchAllUserDetail(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getDataByUsername/{userName}")
    public ResponseEntity<UserEntity> getData(@PathVariable @Valid String userName) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getDataByUserName(userName), HttpStatus.OK);
    }

    @DeleteMapping("/deleteDataByUsername/{userName}")
    public String deleteDataByUserName(@PathVariable @Valid String userName) throws UserNotFoundException {
        logger.info(Thread.currentThread().getName());
        userService.deleteDataByUserName(userName);
        logger.info(Thread.currentThread().getName());
        return "UserEntity removed successfully " + userName;
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody @Valid UserEntity userUpdate) {
        final String updateUser = userService.updateUser(userUpdate);
        return new ResponseEntity<>(updateUser, HttpStatus.ACCEPTED);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid UserEntity user) throws NoSuchAlgorithmException {
        final String forgotPassword = userService.forgotPassword(user);
        return new ResponseEntity<String>(forgotPassword, HttpStatus.OK);
    }

    @PostMapping("/changePassword/{userName}")
    public ResponseEntity<String> changePassword(@PathVariable @Valid String userName, @RequestBody @Valid ChangePassword changePassword)
            throws NoSuchAlgorithmException, UserNotLoginException, UserPasswordDoesNotMatching, UserNotFoundException, UserNotLoginExceptions {
        final String password = userService.changePassword(userName, changePassword);
        return new ResponseEntity<String>(password, HttpStatus.OK);
    }
}
