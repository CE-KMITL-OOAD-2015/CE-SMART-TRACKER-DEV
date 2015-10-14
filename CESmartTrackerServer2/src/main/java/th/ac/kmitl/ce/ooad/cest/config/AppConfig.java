package th.ac.kmitl.ce.ooad.cest.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import th.ac.kmitl.ce.ooad.cest.dao.course.CourseDao;
import th.ac.kmitl.ce.ooad.cest.dao.course.ICourseDao;
import th.ac.kmitl.ce.ooad.cest.dao.student.IStudentDao;
import th.ac.kmitl.ce.ooad.cest.dao.student.StudentDao;
import th.ac.kmitl.ce.ooad.cest.entity.Course;
import th.ac.kmitl.ce.ooad.cest.entity.Student;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public ICourseDao courseDao() {
        return new CourseDao();
    }
    @Bean
    public IStudentDao studentDao() {
        return new StudentDao();
    }
    @Bean
    public HibernateTemplate hibernateTemplate() {
        return new HibernateTemplate(sessionFactory());
    }
    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(getDataSource())
                .addAnnotatedClasses(Course.class)
                .addAnnotatedClasses(Student.class)
                .buildSessionFactory();
    }
    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://128.199.101.200:3306/ce_smart_tracker?characterEncoding=UTF-8");
        dataSource.setUsername("");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public HibernateTransactionManager hibTransMan(){
        return new HibernateTransactionManager(sessionFactory());
    }

}