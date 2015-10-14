package th.ac.kmitl.ce.ooad.cest.dao.student;

import th.ac.kmitl.ce.ooad.cest.entity.Student;

import java.util.List;

public interface IStudentDao {
    public void saveStudent(Student student);
    public List<Student> findByStudentId(String studentId);
}
