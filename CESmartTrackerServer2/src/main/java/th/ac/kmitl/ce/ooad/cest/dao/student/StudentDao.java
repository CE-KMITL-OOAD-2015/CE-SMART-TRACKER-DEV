package th.ac.kmitl.ce.ooad.cest.dao.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import th.ac.kmitl.ce.ooad.cest.entity.Student;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class StudentDao implements IStudentDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void saveStudent(Student student){
        hibernateTemplate.save(student);
    }
    public List<Student> findByStudentId(String studentId)
    {
        List<Student> students = (List<Student>)hibernateTemplate.find("from Student s where s.studentId = ?", studentId);
        return students;
    }
}
