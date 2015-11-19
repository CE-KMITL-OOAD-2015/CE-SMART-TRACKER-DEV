package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;
import th.ac.kmitl.ce.ooad.cest.domain.User;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.repository.UserRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Controller
public class LoginController
{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    /*@Autowired
    private Validator validator;*/

    @RequestMapping("/login")
    @ResponseBody
    public LoginResponse requestLogin(@RequestParam(value = "username", required = true) String username,
                                      @RequestParam(value = "password", required = true) String password)
    {
        return checkLogin(username.trim(), password);
    }

    private LoginResponse checkLogin(String username, String password)
    {
        User user = userRepository.findFirstByUsername(username);
        if (user != null)
        {
            try
            {
                if (HashingUtil.validate(password, user.getPassword(), user.getSalt()))
                {
                    // password is valid
                    LoginResponse loginResponse = new LoginResponse(ResponseEnum.SUCCESS);
                    // generate session if not exist
                    if (user.getSessionId() == null)
                    {
                        String sessionId = generateSessionId();
                        user.setSessionId(sessionId);
                        userRepository.save(user);
                        loginResponse.setSessionId(sessionId);
                    }
                    else
                    {
                        loginResponse.setSessionId(user.getSessionId());
                    }
                    // check user type
                    Student student = studentRepository.findFirstByUsername(username);
                    Teacher teacher = teacherRepository.findFirstByUsername(username);
                    if(student !=  null)
                    {
                        loginResponse.setUserType("student");
                        loginResponse.setStudent(student);
                    }
                    else if(teacher != null)
                    {
                        loginResponse.setUserType("teacher");
                        loginResponse.setTeacher(teacher);
                    }


                    return loginResponse;
                }
                else
                {
                    return new LoginResponse(ResponseEnum.INVALID_PASSWORD);
                }
            }
            catch (IOException e)
            {
                return new LoginResponse(ResponseEnum.ERROR);
            }
            catch (NoSuchAlgorithmException e)
            {
                return new LoginResponse(ResponseEnum.ERROR);
            }
        }
        else
        {
            return new LoginResponse(ResponseEnum.USERNAME_NOT_FOUND);
        }
    }

    private String generateSessionId() throws NoSuchAlgorithmException
    {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        String sSessionId;
        // loop until get a unique sessionId
        do
        {
            byte[] bSessionId = new byte[8];
            random.nextBytes(bSessionId);
            sSessionId = HashingUtil.byteToBase64(bSessionId);
        }
        while(userRepository.findFirstBySessionId(sSessionId) != null);
        return sSessionId;
    }

    @RequestMapping("/loginWithFacebook")
    @ResponseBody
    public LoginResponse requestLoginWithFacebook(@RequestParam(value = "facebookId", required = true) String facebookId)
    {
        return checkLogin(facebookId);
    }

    private LoginResponse checkLogin(String facebookId)
    {
        if(facebookId == null || facebookId == "")
        {
            return new LoginResponse(ResponseEnum.FACEBOOK_ID_NOT_FOUND);
        }
        User user = userRepository.findFirstByFacebookId(facebookId);
        if(user == null)
        {
            return new LoginResponse(ResponseEnum.FACEBOOK_ID_NOT_FOUND);
        }
        if (user.getSessionId() == null)
        {
            try
            {
                user.setSessionId(generateSessionId());
                userRepository.save(user);
            }
            catch (NoSuchAlgorithmException e)
            {
                return new LoginResponse(ResponseEnum.ERROR);
            }
        }

        Student student = studentRepository.findFirstByFacebookId(facebookId);
        Teacher teacher = teacherRepository.findFirstByFacebookId(facebookId);

        if(student != null)
        {
            return new LoginResponse(ResponseEnum.SUCCESS, student.getSessionId(), student);
        }
        else
        {
            return new LoginResponse(ResponseEnum.SUCCESS, teacher.getSessionId(), teacher);
        }
    }

    private class LoginResponse extends Response
    {
        private String sessionId;
        private String userType;
        private Teacher teacher;
        private Student student;

        public LoginResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }

        public LoginResponse(ResponseEnum responseEnum, String sessionId, Teacher teacher)
        {
            super(responseEnum);
            this.sessionId = sessionId;
            this.userType = "teacher";
            this.teacher = teacher;
        }

        public LoginResponse(ResponseEnum responseEnum, String sessionId, Student student)
        {
            super(responseEnum);
            this.sessionId = sessionId;
            this.userType = "student";
            this.student = student;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getUserType()
        {
            return userType;
        }

        public void setUserType(String userType)
        {
            this.userType = userType;
        }

        public Teacher getTeacher()
        {
            return teacher;
        }

        public void setTeacher(Teacher teacher)
        {
            this.teacher = teacher;
        }

        public Student getStudent()
        {
            return student;
        }

        public void setStudent(Student student)
        {
            this.student = student;
        }
    }
}
