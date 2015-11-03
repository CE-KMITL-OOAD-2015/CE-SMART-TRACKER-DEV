package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Account;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;
import th.ac.kmitl.ce.ooad.cest.repository.AccountRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.LoginStatus;
import th.ac.kmitl.ce.ooad.cest.service.response.LoginStatusEnum;
import th.ac.kmitl.ce.ooad.cest.service.response.StatusEnum;
import th.ac.kmitl.ce.ooad.cest.util.CredentialUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Controller
public class LoginController
{

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/login")
    @ResponseBody
    public LoginStatus requestCreateStudent(@RequestParam(value = "username", required = true) String username,
                                            @RequestParam(value = "password", required = true) String password)
    {
        return checkLogin2(username, password);
    }

    private LoginStatus checkLogin2(String username, String password)
    {
        Account account = accountRepository.findFirstByUsername(username);
        if (account != null)
        {
            try
            {
                if (CredentialUtil.validate(password, account.getPassword(), account.getSalt()))
                {
                    LoginStatus loginStatus = new LoginStatus(StatusEnum.SUCCESS);
                    if (account.getSessionId() != null)
                    {
                        loginStatus.setSessionId(account.getSessionId());
                    }
                    else
                    {
                        String sessionId = generateSessionId();
                        account.setSessionId(sessionId);
                        accountRepository.save(account);
                        loginStatus.setSessionId(sessionId);
                    }
                    return loginStatus;
                }
                else
                {
                    return new LoginStatus(LoginStatusEnum.INVALID_PASSWORD);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return new LoginStatus(StatusEnum.ERROR);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
                return new LoginStatus(StatusEnum.ERROR);
            }
        }
        else
        {
            return new LoginStatus(LoginStatusEnum.USERNAME_NOT_FOUND);
        }
    }

    private String generateSessionId() throws NoSuchAlgorithmException
    {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bSessionId;
        String sSessionId;

        do
        {
            bSessionId = new byte[8];
            random.nextBytes(bSessionId);
            sSessionId = CredentialUtil.byteToBase64(bSessionId);
        }
        while (studentRepository.findFirstBySessionId(sSessionId) != null ||
                teacherRepository.findFirstBySessionId(sSessionId) != null);

        return sSessionId;
    }

    /*private LoginStatus checkLogin(String username, String password)
    {
        Student student = studentRepository.findFirstByUsername(username);
        Teacher teacher = teacherRepository.findFirstByUsername(username);
        try
        {
            if (student != null)
            {
                if (CredentialUtil.validate(password, student.getPassword(), student.getSalt()))
                {
                    LoginStatus loginStatus = new LoginStatus(StatusEnum.SUCCESS);
                    if (student.getSessionId() != null)
                    {
                        loginStatus.setSessionId(student.getSessionId());
                    }
                    else
                    {
                        String sessionId = generateSessionId();
                        student.setSessionId(sessionId);
                        studentRepository.save(student);
                        loginStatus.setSessionId(sessionId);
                    }
                    return loginStatus;
                }
                else
                {
                    return new LoginStatus(LoginStatusEnum.INVALID_PASSWORD);
                }
            }
            else if (teacher != null)
            {
                try
                {
                    if (CredentialUtil.validate(password, teacher.getPassword(), teacher.getSalt()))
                    {
                        LoginStatus loginStatus = new LoginStatus(StatusEnum.SUCCESS);
                        if (teacher.getSessionId() != null)
                        {
                            loginStatus.setSessionId(teacher.getSessionId());
                        }
                        else
                        {
                            String sessionId = generateSessionId();
                            teacher.setSessionId(sessionId);
                            teacherRepository.save(teacher);
                            loginStatus.setSessionId(sessionId);
                        }
                        return loginStatus;
                    }
                    else
                    {
                        return new LoginStatus(LoginStatusEnum.INVALID_PASSWORD);
                    }
                }
                catch (NoSuchAlgorithmException e)
                {
                    return new LoginStatus(StatusEnum.ERROR);
                }
                catch (UnsupportedEncodingException e)
                {
                    return new LoginStatus(StatusEnum.ERROR);
                }
                catch (IOException e)
                {
                    return new LoginStatus(StatusEnum.ERROR);
                }
            }
            else
            {
                return new LoginStatus(LoginStatusEnum.USERNAME_NOT_FOUND);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            return new LoginStatus(StatusEnum.ERROR);
        }
        catch (UnsupportedEncodingException e)
        {
            return new LoginStatus(StatusEnum.ERROR);
        }
        catch (IOException e)
        {
            return new LoginStatus(StatusEnum.ERROR);
        }
    }*/

}
